////package com.example.drishtimukesh.screen
////
////import android.app.Activity
////import android.os.Bundle
////import android.util.Log
////import android.widget.Toast
////import androidx.activity.ComponentActivity
////import androidx.activity.compose.setContent
////import androidx.compose.material3.MaterialTheme
////import androidx.compose.material3.Surface
////import com.example.drishtimukesh.screen.PaymentScreen
////import com.razorpay.*
////import org.json.JSONObject
////import androidx.navigation.NavController
////import java.lang.Exception
////
//////import android.app.Activity
////import android.content.Context
////import android.content.ContextWrapper
//////import android.util.Log
////import androidx.compose.runtime.Composable
////import androidx.compose.ui.platform.LocalContext
//////import com.example.drishtimukesh.payment.PaymentActivity
////fun Context.findActivity(): Activity? = when (this) {
////    is Activity -> this
////    is ContextWrapper -> baseContext.findActivity()
////    else -> null
////}
/////**
//// * This Activity is the actual host for the PaymentScreen and implements the
//// * complete Razorpay logic using the robust PaymentResultWithDataListener interface.
//// */
////class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener, ExternalWalletListener {
////
////    val TAG: String = "PaymentActivity"
////
////    // IMPORTANT: Replace this with your actual Test (rzp_test_...) or Live (rzp_live_...) Key ID.
////    // Ensure the scheme in AndroidManifest.xml is the lowercase version of this key ID prefix.
////    private val RAZORPAY_KEY_ID = "rzp_test_RXDOmPAbFmZi7C"
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////        // Use Activity context instead of applicationContext
////        Checkout.preload(this)
////
////        setContent {
////            val mockNavController = androidx.navigation.NavController(this)
////
////            MaterialTheme {
////                Surface {
////                    val courseId = intent.getStringExtra("COURSE_ID") ?: "100"
////                    val courseName = intent.getStringExtra("COURSE_NAME") ?: "Unknown Course"
////                    val finalPrice = intent.getIntExtra("FINAL_PRICE", 100)
////                    val userEmail = "test@example.com"  // replace with actual user email if available
////                    val userContact = "9999999999"      // replace with actual contact number
////
////                    if (finalPrice > 0) {
////                        Log.i(TAG, "Auto-starting payment for $courseName, ₹$finalPrice")
////                        startRazorpayPayment(
////                            activity = this,
////                            amountInRupees = finalPrice.toDouble(),
////                            userEmail = userEmail,
////                            userContact = userContact
////                        )
////                    } else {
////                        Log.e(TAG, "Invalid amount: $finalPrice. Payment not started.")
////                    }
////
////                }
////            }
////        }
////    }
////
////    /**
////     * Called by the PaymentBottomBar composable to start the payment process.
////     */
////    fun startRazorpayPayment(
////        activity: Activity,
////        amountInRupees: Double,
////        userEmail: String,
////        userContact: String
////    ) {
////        val co = Checkout()
////        // Use the configured key ID. This is critical.
////        co.setKeyID(RAZORPAY_KEY_ID)
////
////        try {
////            val options = JSONObject()
////            options.put("name", "Drishti Mukesh Education")
////            options.put("description", "Subscription Purchase")
////            // Amount is in smallest currency unit (paise), so multiply by 100
////            options.put("amount", (amountInRupees * 100).toInt())
////            options.put("currency", "INR")
////            options.put("theme.color", "#FFB330")
////            options.put("send_sms_hash", true)
////
////            val prefill = JSONObject()
////            prefill.put("email", userEmail)
////            prefill.put("contact", userContact)
////
////            options.put("prefill", prefill)
////
////            co.open(activity, options)
////
////        } catch (e: Exception) {
////            Log.e(TAG, "Error in starting Razorpay Checkout", e)
////            Toast.makeText(activity, "Payment Error: ${e.message}", Toast.LENGTH_LONG).show()
////            e.printStackTrace()
////        }
////    }
////
////    // --- Razorpay PaymentResultWithDataListener Implementation ---
////
////    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
////        // Handle successful payment: typically, this involves calling your backend
////        val paymentDetails = paymentData?.data.toString()
////        Toast.makeText(
////            this,
////            "Payment Successful! ID: $razorpayPaymentId",
////            Toast.LENGTH_LONG
////        ).show()
////        Log.i(TAG, "Payment Success: ID: $razorpayPaymentId, Details: $paymentDetails")
////
////        // Navigate to a success screen or update UI
////    }
////
////    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
////        // Handle payment failure: show a user-friendly error
////        val paymentDetails = paymentData?.data.toString()
////        Toast.makeText(
////            this,
////            "Payment Failed. Please try again. Code: $code",
////            Toast.LENGTH_LONG
////        ).show()
////        Log.e(TAG, "Payment Failed. Code: $code, Response: $response, Details: $paymentDetails")
////        // Navigate back or show a retry option
////    }
////
////    // --- ExternalWalletListener Implementation ---
////
////    override fun onExternalWalletSelected(walletName: String?, paymentData: PaymentData?) {
////        // Handle when a non-Razorpay wallet (like Paytm, PhonePe) is selected outside of checkout
////        val paymentDetails = paymentData?.data.toString()
////        Toast.makeText(
////            this,
////            "External Wallet Selected: $walletName",
////            Toast.LENGTH_SHORT
////        ).show()
////        Log.d(TAG, "External Wallet Selected: $walletName, Details: $paymentDetails")
////    }
////}
//
////version2
//
//package com.example.drishtimukesh.screen
//
//import android.app.Activity
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import com.razorpay.*
//import org.json.JSONObject
//import androidx.navigation.NavController
//import java.lang.Exception
//import android.content.Context
//import android.content.ContextWrapper
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//import com.example.drishtimukesh.logTransactionToFirestore
//
//fun Context.findActivity(): Activity? = when (this) {
//    is Activity -> this
//    is ContextWrapper -> baseContext.findActivity()
//    else -> null
//}
//
//class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener, ExternalWalletListener {
//
//    val TAG: String = "PaymentActivity"
//    private val RAZORPAY_KEY_ID = "rzp_test_RXDOmPAbFmZi7C"
//
//    // Store payment details for Firestore logging
//    private var courseId: String = ""
//    private var courseName: String = ""
//    private var subscriptionMonths: Int = 0
//    private var finalPrice: Double = 0.0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Retrieve data from intent
//        courseId = intent.getStringExtra("COURSE_ID") ?: ""
//        courseName = intent.getStringExtra("COURSE_NAME") ?: "Unknown Course"
//        subscriptionMonths = intent.getIntExtra("SUBSCRIPTION_MONTHS", 1)
//        finalPrice = intent.getIntExtra("FINAL_PRICE", 0).toDouble()
//
//        Checkout.preload(this)
//
//        setContent {
//            MaterialTheme {
//                Surface {
//                    // Empty composable since we're handling payment directly
//                }
//            }
//        }
//
//        // Start payment automatically when activity creates
//        if (finalPrice > 0 && courseId.isNotEmpty()) {
//            Log.i(TAG, "Starting payment for $courseName, ₹$finalPrice, $subscriptionMonths months")
//            startRazorpayPayment()
//        } else {
//            Log.e(TAG, "Invalid payment data: Price=$finalPrice, CourseID=$courseId")
//            Toast.makeText(this, "Invalid payment data", Toast.LENGTH_LONG).show()
//            finish()
//        }
//    }
//
//    private fun startRazorpayPayment() {
//        val co = Checkout()
//        co.setKeyID(RAZORPAY_KEY_ID)
//
//        try {
//            val options = JSONObject()
//            options.put("name", "Drishti Mukesh Education")
//            options.put("description", "Subscription for $courseName")
//            options.put("amount", (finalPrice * 100).toInt())
//            options.put("currency", "INR")
//            options.put("theme.color", "#FFB330")
//            options.put("send_sms_hash", true)
//
//            val prefill = JSONObject()
//            prefill.put("email", "test@example.com") // Replace with actual user email
//            prefill.put("contact", "9999999999")     // Replace with actual contact
//
//            options.put("prefill", prefill)
//
//            co.open(this, options)
//
//        } catch (e: Exception) {
//            Log.e(TAG, "Error in starting Razorpay Checkout", e)
//            Toast.makeText(this, "Payment Error: ${e.message}", Toast.LENGTH_LONG).show()
//            finish()
//        }
//    }
//
//    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
//        val paymentDetails = paymentData?.data.toString()
//
//        Toast.makeText(
//            this,
//            "Payment Successful! ID: $razorpayPaymentId",
//            Toast.LENGTH_LONG
//        ).show()
//
//        Log.i(TAG, "Payment Success: $razorpayPaymentId")
//
//        // Log transaction to Firestore
//        if (razorpayPaymentId != null) {
//            lifecycleScope.launch {
//                try {
//                    logTransactionToFirestore(
//                        context = this@PaymentActivity,
//                        courseId = courseId,
//                        subscriptionMonths = subscriptionMonths,
//                        amountPaid = finalPrice,
//                        razorpayPaymentId = razorpayPaymentId,
//                        paymentDetails = paymentDetails
//                    )
//                    Log.d(TAG, "Transaction logged successfully")
//
//                    // Show success message
//                    Toast.makeText(
//                        this@PaymentActivity,
//                        "Subscription activated successfully!",
//                        Toast.LENGTH_LONG
//                    ).show()
//
//                } catch (e: Exception) {
//                    Log.e(TAG, "Failed to log transaction", e)
//                    Toast.makeText(
//                        this@PaymentActivity,
//                        "Payment successful but failed to activate subscription. Please contact support.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } finally {
//                    finish()
//                }
//            }
//        } else {
//            finish()
//        }
//    }
//
//    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
//        val errorMsg = "Payment Failed: $response (Code: $code)"
//        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
//        Log.e(TAG, errorMsg)
//        finish()
//    }
//
//    override fun onExternalWalletSelected(walletName: String?, paymentData: PaymentData?) {
//        Toast.makeText(this, "External Wallet: $walletName", Toast.LENGTH_SHORT).show()
//        Log.d(TAG, "External Wallet: $walletName")
//    }
//}
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
import kotlinx.coroutines.launch
import com.example.drishtimukesh.logTransactionToFirestore

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener, ExternalWalletListener {

    private val TAG = "PaymentActivity"
    private val RAZORPAY_KEY_ID = "rzp_test_RXDOmPAbFmZi7C"

    private var courseId: String = ""
    private var courseName: String = ""
    private var subscriptionMonths: Int = 0
    private var finalPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        courseId = intent.getStringExtra("COURSE_ID") ?: ""
        courseName = intent.getStringExtra("COURSE_NAME") ?: "Unknown Course"
        subscriptionMonths = intent.getIntExtra("SUBSCRIPTION_MONTHS", 1)
        finalPrice = intent.getIntExtra("FINAL_PRICE", 0).toDouble()

        Checkout.preload(this)

        setContent {
            MaterialTheme {
                Surface {}
            }
        }

        if (finalPrice > 0 && courseId.isNotEmpty()) {
            Log.i(TAG, "Starting payment for $courseName, ₹$finalPrice, $subscriptionMonths months")
            startRazorpayPayment()
        } else {
            Toast.makeText(this, "Invalid payment data", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun startRazorpayPayment() {
        val co = Checkout()
        co.setKeyID(RAZORPAY_KEY_ID)

        try {
            val options = JSONObject()
            options.put("name", "Drishti Mukesh Education")
            options.put("description", "Subscription for $courseName")
            options.put("amount", (finalPrice * 100).toInt()) // in paise
            options.put("currency", "INR")
            options.put("theme.color", "#FFB330")
            options.put("send_sms_hash", true)

            val prefill = JSONObject()
            prefill.put("email", "test@example.com")
            prefill.put("contact", "9999999999")
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
                logTransactionToFirestore(
                    context = this@PaymentActivity,
                    courseId = courseId,
                    subscriptionMonths = subscriptionMonths,
                    amountPaid = finalPrice,
                    razorpayPaymentId = razorpayPaymentId,
                    paymentDetails = paymentDetails
                )

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
