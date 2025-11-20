package com.example.drishtimukesh.screen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.razorpay.*
import org.json.JSONObject
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.lifecycleScope
import com.example.drishtimukesh.logTransactionToFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener, ExternalWalletListener {

    private val TAG = "PaymentActivity"
    private val RAZORPAY_KEY_ID = "rzp_live_RWyjFVWqp4RTuh"

    private var courseId: String = ""
    private var courseName: String = ""
    private var subscriptionMonths: Int = 0
    private var finalPrice: Double = 0.0
    private var referralUsername: String = ""
    private var coinsUsed: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Get intent extras
        courseId = intent.getStringExtra("COURSE_ID") ?: ""
        courseName = intent.getStringExtra("COURSE_NAME") ?: "Unknown Course"
        subscriptionMonths = intent.getIntExtra("SUBSCRIPTION_MONTHS", 1)
        finalPrice = intent.getIntExtra("FINAL_PRICE", 0).toDouble()
        referralUsername = intent.getStringExtra("REFERRAL_USERNAME") ?: ""
        coinsUsed = intent.getIntExtra("COINS_USED", 0)

        Checkout.preload(this)

        setContent {
            MaterialTheme {
                Surface {}
            }
        }

        if (finalPrice > 0 && courseId.isNotEmpty()) {

            lifecycleScope.launch {
                val auth = FirebaseAuth.getInstance()
                val userId = auth.currentUser?.uid ?: return@launch

                val snap = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .get()
                    .await()

                val email = snap.getString("email") ?: auth.currentUser?.email ?: ""
                val phone = snap.getString("phone") ?: auth.currentUser?.phoneNumber ?: ""

                startRazorpayPayment(email, phone)
            }
        }else {
            Toast.makeText(this, "Invalid payment data", Toast.LENGTH_LONG).show()
            finish()
        }
    }


    private fun startRazorpayPayment(email: String, phone: String) {
        val co = Checkout()
        co.setKeyID(RAZORPAY_KEY_ID)

        try {
            val options = JSONObject()
//            options.put("id",RAZORPAY_KEY_ID)
            options.put("name", "Drishti Institute Bihta")
            options.put("description", "Subscription for $courseName")
            options.put("amount", (finalPrice * 100).toInt()) // in paise
            options.put("currency", "INR")
            options.put("theme.color", "#FFB330")
            options.put("send_sms_hash", true)
//            options.put("order_id", createdOrderId)

            val prefill = JSONObject()
            prefill.put("email", email)
            prefill.put("contact", phone)
            options.put("prefill", prefill)

            co.open(this, options)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Razorpay Checkout", e)
            Toast.makeText(this, "Payment Error: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        val paymentDetails = paymentData?.data.toString()

        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show()
        Log.i(TAG, "Payment Success: $razorpayPaymentId")

        if (razorpayPaymentId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            try {
                // ✅ Log transaction (existing function)
                logTransactionToFirestore(
                    context = this@PaymentActivity,
                    courseId = courseId,
                    subscriptionMonths = subscriptionMonths,
                    amountPaid = finalPrice,
                    razorpayPaymentId = razorpayPaymentId,
                    paymentDetails = paymentDetails
                )

                // ✅ After logging, apply coin and referral updates
                handleCoinsAndReferral()

                Toast.makeText(
                    this@PaymentActivity,
                    "Subscription activated successfully!",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Log.e(TAG, "Error in payment success flow", e)
                Toast.makeText(
                    this@PaymentActivity,
                    "Payment successful but failed to activate subscription.",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                finish()
            }
        }
    }

    /**
     * ✅ Deduct used coins and add 50 coins to referrer if applicable
     */
    private suspend fun handleCoinsAndReferral() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser ?: return

        val userId = currentUser.uid
        val batch = db.batch()

        try {
            val userDoc = db.collection("users").document(userId)

            // --- 🔹 Deduct used coins ---
            if (coinsUsed > 0) {
                Log.d(TAG, "Deducting $coinsUsed coins from user $userId")
                batch.update(userDoc, "coins", FieldValue.increment(-coinsUsed.toLong()))
            }

            // --- 🔹 Add 50 coins to referral user ---
            if (referralUsername.isNotEmpty()) {
                val referralQuery = db.collection("users")
                    .whereEqualTo("userName", referralUsername.trim().lowercase())
                    .get()
                    .await()

                if (!referralQuery.isEmpty) {
                    val referrerDoc = referralQuery.documents.first().reference
                    Log.d(TAG, "Adding 50 coins to referrer: $referralUsername")
                    batch.update(referrerDoc, "coins", FieldValue.increment(50))
                } else {
                    Log.w(TAG, "Referral username '$referralUsername' not found — skipping coin bonus")
                }
            }

            // ✅ Commit all updates together
            batch.commit().await()
            Log.i(TAG, "Coins and referral bonuses applied successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Error applying coin/referral updates: ${e.message}", e)
        }
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        val errorMsg = "Payment Failed: $response (Code: $code)"
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        Log.e(TAG, errorMsg)
        finish()
    }

    override fun onExternalWalletSelected(walletName: String?, paymentData: PaymentData?) {
        Toast.makeText(this, "External Wallet: $walletName", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "External Wallet: $walletName")
    }
}
