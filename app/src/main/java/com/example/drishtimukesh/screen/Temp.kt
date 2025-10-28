package com.example.drishtimukesh.screen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.drishtimukesh.screen.PaymentScreen
import com.razorpay.*
import org.json.JSONObject
import androidx.navigation.NavController
import java.lang.Exception

//import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
//import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
//import com.example.drishtimukesh.payment.PaymentActivity
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
/**
 * This Activity is the actual host for the PaymentScreen and implements the
 * complete Razorpay logic using the robust PaymentResultWithDataListener interface.
 */
class PaymentActivity : ComponentActivity(), PaymentResultWithDataListener, ExternalWalletListener {

    val TAG: String = "PaymentActivity"

    // IMPORTANT: Replace this with your actual Test (rzp_test_...) or Live (rzp_live_...) Key ID.
    // Ensure the scheme in AndroidManifest.xml is the lowercase version of this key ID prefix.
    private val RAZORPAY_KEY_ID = "rzp_test_RXDOmPAbFmZi7C"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure Razorpay SDK preloads for faster checkout opening.
        Checkout.preload(applicationContext)

        setContent {
            // Setup a minimal NavController for the Composable signature
            val mockNavController = androidx.navigation.NavController(this)

            MaterialTheme {
                Surface {
                    // Assuming you navigate to this activity with a courseId
                    val courseId = intent.getStringExtra("COURSE_ID") ?: "100"

                    PaymentScreen(courseId = courseId, navController = mockNavController)
                }
            }
        }
    }

    /**
     * Called by the PaymentBottomBar composable to start the payment process.
     */
    fun startRazorpayPayment(
        activity: Activity,
        amountInRupees: Double,
        userEmail: String,
        userContact: String
    ) {
        val co = Checkout()
        // Use the configured key ID. This is critical.
        co.setKeyID(RAZORPAY_KEY_ID)

        try {
            val options = JSONObject()
            options.put("name", "Drishti Mukesh Education")
            options.put("description", "Subscription Purchase")
            // Amount is in smallest currency unit (paise), so multiply by 100
            options.put("amount", (amountInRupees * 100).toInt())
            options.put("currency", "INR")
            options.put("theme.color", "#FFB330")
            options.put("send_sms_hash", true)

            val prefill = JSONObject()
            prefill.put("email", userEmail)
            prefill.put("contact", userContact)

            options.put("prefill", prefill)

            co.open(activity, options)

        } catch (e: Exception) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e)
            Toast.makeText(activity, "Payment Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    // --- Razorpay PaymentResultWithDataListener Implementation ---

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        // Handle successful payment: typically, this involves calling your backend
        val paymentDetails = paymentData?.data.toString()
        Toast.makeText(
            this,
            "Payment Successful! ID: $razorpayPaymentId",
            Toast.LENGTH_LONG
        ).show()
        Log.i(TAG, "Payment Success: ID: $razorpayPaymentId, Details: $paymentDetails")
        // Navigate to a success screen or update UI
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        // Handle payment failure: show a user-friendly error
        val paymentDetails = paymentData?.data.toString()
        Toast.makeText(
            this,
            "Payment Failed. Please try again. Code: $code",
            Toast.LENGTH_LONG
        ).show()
        Log.e(TAG, "Payment Failed. Code: $code, Response: $response, Details: $paymentDetails")
        // Navigate back or show a retry option
    }

    // --- ExternalWalletListener Implementation ---

    override fun onExternalWalletSelected(walletName: String?, paymentData: PaymentData?) {
        // Handle when a non-Razorpay wallet (like Paytm, PhonePe) is selected outside of checkout
        val paymentDetails = paymentData?.data.toString()
        Toast.makeText(
            this,
            "External Wallet Selected: $walletName",
            Toast.LENGTH_SHORT
        ).show()
        Log.d(TAG, "External Wallet Selected: $walletName, Details: $paymentDetails")
    }
}
