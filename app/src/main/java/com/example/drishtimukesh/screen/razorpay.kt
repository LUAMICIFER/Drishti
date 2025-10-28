package com.example.drishtimukesh.screen
import android.app.Activity
import android.content.Context
import com.example.drishtimukesh.R
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
////
////import android.app.Activity
////import android.os.Bundle
////import android.widget.Toast
////import androidx.appcompat.app.AppCompatActivity
////import com.razorpay.Checkout
////import com.razorpay.PaymentResultListener
////import org.json.JSONObject
////import kotlin.math.roundToInt
////
////// NOTE: This Activity must implement the PaymentResultListener interface
////class PaymentActivity : AppCompatActivity(), PaymentResultListener {
////
////    // 🚨 IMPORTANT: Replace with your actual Test Key ID (rzp_test_...)
////    // You should load this from a secure configuration or a backend/API call in a real app.
////    private val RAZORPAY_KEY_ID = "rzp_test_RXDOmPAbFmZi7C"
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        // Ensure the Razorpay SDK preloads for faster checkout opening. This is good practice.
////        Checkout.preload(applicationContext)
////        // ... rest of your onCreate setup (e.g., setting your layout XML)
////
////        // Example of where you might initiate payment (e.g., after clicking a 'Pay' button)
////        // For a Compose/Button click, see the example in section 2 below.
////    }
////
////    /**
////     * Initializes and launches the Razorpay Checkout payment screen.
////     * * @param activity The current Activity instance (required by Razorpay SDK).
////     * @param amountInRupees The transaction amount in full INR (e.g., 50.00).
////     * @param userEmail The email address to prefill the checkout form.
////     * @param userContact The mobile number to prefill the checkout form.
////     */
////    fun startRazorpayPayment(
////        activity: Activity,
////        amountInRupees: Double,
////        userEmail: String,
////        userContact: String
////    ) {
////        val checkout = Checkout()
////        // Set the Key ID
////        checkout.setKeyID(RAZORPAY_KEY_ID)
////
////        // Convert the amount from Rupees to Paise (multiply by 100 and convert to Int)
////        // Razorpay expects the amount in the smallest currency unit (Paise for INR).
////        val amountInPaise = (amountInRupees * 100).roundToInt()
////
////        try {
////            // Build the JSON payload for the Razorpay Checkout
////            val options = JSONObject()
////
////            // Basic details
////            options.put("name", "Drishti Mukesh Courses")
////            options.put("description", "Course Enrollment Fee")
////            options.put("currency", "INR")
////            options.put("amount", amountInPaise)
////
////            // Pre-fill user contact details
////            val prefill = JSONObject()
////            prefill.put("email", userEmail)
////            prefill.put("contact", userContact)
////            options.put("prefill", prefill)
////
////            // Optional: Customize theme color
////            options.put("theme.color", "#4338CA")
////
////            // Open the Razorpay Checkout form
////            checkout.open(activity, options)
////
////        } catch (e: Exception) {
////            Toast.makeText(activity, "Payment initialization error: ${e.message}", Toast.LENGTH_LONG).show()
////            e.printStackTrace()
////        }
////    }
////
////    // --- Required PaymentResultListener Methods ---
////
////    /**
////     * Called when the payment is successful.
////     * This is where you should trigger a server-side verification.
////     */
////    override fun onPaymentSuccess(razorpayPaymentID: String?) {
////        try {
////            Toast.makeText(this, "Payment Successful: $razorpayPaymentID", Toast.LENGTH_LONG).show()
////
////            // 🟢 SUCCESS HANDLING:
////            // 1. **VERIFY PAYMENT ON YOUR SERVER:** Pass the razorpayPaymentID
////            //    to your backend to confirm the payment is legitimate.
////            // 2. Update your local database (e.g., grant coins/access).
////
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////    }
////
////    /**
////     * Called when the payment fails (e.g., user cancelled, card declined).
////     */
////    override fun onPaymentError(code: Int, response: String?) {
////        try {
////            // Error codes documentation: https://razorpay.com/docs/api/errors/
////            Toast.makeText(this, "Payment Failed: Code $code, Response: $response", Toast.LENGTH_LONG).show()
////
////            // 🔴 ERROR HANDLING:
////
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////    }
////}
//
//import android.app.Activity
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import com.example.drishtimukesh.screen.PaymentScreen
//import com.razorpay.Checkout
//import com.razorpay.PaymentResultListener
//import org.json.JSONObject
//
///**
// * This Activity is the actual host for the PaymentScreen, and implements the
// * Razorpay logic to handle the payment gateway flow.
// *
// * NOTE: You must include the Razorpay SDK dependency in your build.gradle file.
// */
//class PaymentActivity : ComponentActivity(), PaymentResultListener {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Ensure Razorpay SDK is initialized (can be done in Application class too)
//        Checkout.preload(applicationContext)
//
//        setContent {
//            MaterialTheme {
//                Surface {
//                    // Assuming you navigate to this activity with a courseId
//                    val courseId = intent.getStringExtra("COURSE_ID") ?: "100"
//                    // Note: You might need to pass the NavController differently
//                    // or handle navigation from inside this Activity.
//                    PaymentScreen(courseId = courseId, navController = TODO_NAV_CONTROLLER)
//                }
//            }
//        }
//    }
//
//    // A placeholder to satisfy the compiler and mock the NavController in the Activity context
//    private val TODO_NAV_CONTROLLER = androidx.navigation.NavController(this)
//
//    /**
//     * Called by the PaymentBottomBar composable to start the payment process.
//     * This method must exist in the hosting Activity for the PaymentScreen to work.
//     */
//    fun startRazorpayPayment(
//        activity: Activity,
//        amountInRupees: Double,
//        userEmail: String,
//        userContact: String
//    ) {
//        val checkout = Checkout()
//        // Replace with your actual key (rzp_live_...)
//        checkout.setKeyID("rzp_test_pL1Xh7j123456")
//
//        try {
//            val options = JSONObject()
//            options.put("name", "Drishti Mukesh Education")
//            options.put("description", "Subscription Purchase")
//            // Amount is in smallest currency unit (paise), so multiply by 100
//            options.put("amount", (amountInRupees * 100).toInt())
//            options.put("currency", "INR")
//            options.put("theme.color", "#FFB330")
//
//            val prefill = JSONObject()
//            prefill.put("email", userEmail)
//            prefill.put("contact", userContact)
//            options.put("prefill", prefill)
//
//            checkout.open(activity, options)
//
//        } catch (e: Exception) {
//            Log.e("PaymentActivity", "Error in starting Razorpay Checkout", e)
//            Toast.makeText(activity, "Payment Error: ${e.message}", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    // --- Razorpay PaymentResultListener Implementation ---
//
//    override fun onPaymentSuccess(razorpayPaymentId: String?) {
//        // Handle successful payment: update UI, grant access to course, log transaction
//        Toast.makeText(this, "Payment Successful: $razorpayPaymentId", Toast.LENGTH_LONG).show()
//        Log.d("PaymentActivity", "Payment ID: $razorpayPaymentId")
//        // Typically, you would navigate to a success screen here
//    }
//
//    override fun onPaymentError(code: Int, response: String?) {
//        // Handle payment failure: show error message to user
//        Toast.makeText(this, "Payment Failed: $response (Code: $code)", Toast.LENGTH_LONG).show()
//        Log.e("PaymentActivity", "Payment Failed. Code: $code, Response: $response")
//        // Typically, you would navigate to a failure screen here
//    }
//}
// Rename this interface to avoid conflict
//interface MyPaymentResultListener {
//    fun onPaymentSuccess(razorpayPaymentId: String?)
//    fun onPaymentError(errorCode: Int, response: String?)
//}
//
//class RazorpayPaymentHelper(
//    private val context: Context,
//    private val myPaymentResultListener: MyPaymentResultListener // Use the renamed interface
//) : PaymentResultListener { // Still implement Razorpay's interface
//
//    private lateinit var checkout: Checkout
//
//    init {
//        initializeRazorpay()
//    }
//
//    private fun initializeRazorpay() {
//        checkout = Checkout()
//        checkout.setKeyID("rzp_test_YOUR_TEST_KEY_HERE") // Replace with your actual test key
//
//        // Set the payment result listener to THIS class (which implements Razorpay's interface)
//        checkout.setImage(R.drawable.component_34) // Optional: Set your app icon
//    }
//
//    fun startPayment(amount: Double, currency: String = "INR", description: String = "Payment") {
//        try {
//            val activity = context as? Activity
//            activity?.let {
//                val options = JSONObject()
//
//                // Set payment details
//                options.put("name", "Your App Name")
//                options.put("description", description)
//                options.put("currency", currency)
//                options.put("amount", (amount * 100).toInt()) // Convert to paise
//
//                // Pre-fill customer details (optional)
//                val prefill = JSONObject()
//                prefill.put("email", "customer@example.com")
//                prefill.put("contact", "9999999999")
//                options.put("prefill", prefill)
//
//                // Set theme color (optional)
//                options.put("theme.color", "#4338CA")
//
//                // Open Razorpay checkout - pass THIS as the listener
//                checkout.open(it, options, this)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            myPaymentResultListener.onPaymentError(0, "Error initializing payment: ${e.message}")
//        }
//    }
//
//    // Implement Razorpay's PaymentResultListener methods
//    override fun onPaymentSuccess(razorpayPaymentId: String?) {
//        // Forward to your custom listener
//        myPaymentResultListener.onPaymentSuccess(razorpayPaymentId)
//    }
//
//    override fun onPaymentError(errorCode: Int, response: String?) {
//        // Forward to your custom listener
//        myPaymentResultListener.onPaymentError(errorCode, response)
//    }
//}