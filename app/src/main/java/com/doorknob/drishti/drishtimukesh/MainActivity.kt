//package com.doorknob.drishti
//import android.os.Bundle
//import android.view.WindowManager
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.calculateEndPadding
//import androidx.compose.foundation.layout.calculateStartPadding
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.produceState
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.doorknob.drishti.screen.HomeCheckScreen
//import com.doorknob.drishti.screen.HomeScreenContainer
//import com.doorknob.drishti.screen.CourseDetailScreen
//import com.doorknob.drishti.screen.PaymentScreen
//import com.doorknob.drishti.screen.ReferralScreen
//import com.doorknob.drishti.screen.VideoPlayerScreen
//import com.doorknob.drishti.signup.DetailPage
//import com.doorknob.drishti.signup.ForgotPasswordScreen
//import com.doorknob.drishti.signup.OnboardingScreen
//import com.doorknob.drishti.signup.SignInScreen
//import com.doorknob.drishti.signup.SignUpScreen
//import com.doorknob.drishti.signup.SignUpScreenMail
//import com.doorknob.drishti.signup.saveOnboardingCompleted
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.coroutines.launch
//import java.net.URLDecoder
//import java.nio.charset.StandardCharsets
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )
//        enableEdgeToEdge()
//        setContent {
//            val context = LocalContext.current
//            val navController = rememberNavController()
//
//            // ✅ Firebase Auth check
//            val auth = FirebaseAuth.getInstance()
//            val isLoggedIn = auth.currentUser != null
//
//            // ✅ Onboarding flag
//            val hasSeenOnboarding by produceState(initialValue = false, context) {
//                value = readOnboardingCompleted(context)
//            }
//
//            // ✅ Decide start destination
//            val startDestination = when {
//                isLoggedIn -> "home_main"
//                !hasSeenOnboarding -> "onboarding"
//                else -> "signup"
//            }
//
//            // ✅ Scaffold with proper padding applied to children
//            Scaffold(
//                modifier = Modifier.fillMaxSize()
//            ) { innerPadding ->
//                NavHost(
//                    navController = navController,
//                    startDestination = startDestination,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(
//                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
//                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
//                            bottom = innerPadding.calculateBottomPadding()
//                        )
//
//                ) {
//                    composable("onboarding") {
//                        val coroutineScope = rememberCoroutineScope()
//                        OnboardingScreen(
//                            onFinish = {
//                                coroutineScope.launch {
//                                    saveOnboardingCompleted(context)
//                                    navController.navigate("signup") {
//                                        popUpTo("onboarding") { inclusive = true }
//                                    }
//                                }
//                            },
//                            onSignUpClick = {
//                                navController.navigate("signup") {
//                                    popUpTo("onboarding") { inclusive = true }
//                                }
//                            }
//                        )
//                    }
//
//                    composable("signup") {
//                        SignUpScreen(navController)
//                    }
//
//                    composable("user_detail") {
//                        DetailPage(navController)
//                    }
//
//                    composable("signin") {
//                        SignInScreen(navController)
//                    }
//
//                    composable("home") {
//                        HomeCheckScreen(navController = navController)
//                    }
//
//                    composable("home_main") {
//                        HomeScreenContainer(navController = navController)
//                    }
//
//                    composable(
//                        route = "CourseDescriptionScreen/{courseId}",
//                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
//                    ) { backStackEntry ->
//                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
//                        CourseDetailScreen(courseId = courseId, navController = navController)
//                    }
//
//                    composable(
//                        route = "videoPlayerScreen/{videoUrl}",
//                        arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
//                    ) { backStackEntry ->
//                        val encodedUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
//                        val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
//
//                        VideoPlayerScreen(videoUrl = decodedUrl, navController = navController)
//                    }
//
//                    composable("refferal") {
//                        ReferralScreen(navController = navController)
//                    }
//
//                    composable(
//                        route = "differentPaymentScreen/{courseId}",
//                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
//                    ) { backStackEntry ->
//                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
//                        PaymentScreen(courseId = courseId, navController = navController)
//                    }
//
//                    composable("signupMail") {
//                        SignUpScreenMail(navController)
//                    }
//                    composable("forgot_password") { ForgotPasswordScreen(navController) }
//
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Succes() {
//    Text("done")
//}
package com.doorknob.drishti

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.doorknob.drishti.screen.*
import com.doorknob.drishti.signup.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 Prevent screen recording
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        enableEdgeToEdge()

        // ✅ Step 1: Create Notification Channel
        createNotificationChannel()

        // ✅ Step 2: Ask Notification Permission (Android 13+)
        requestNotificationPermission()

        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()

            val auth = FirebaseAuth.getInstance()
            val isLoggedIn = auth.currentUser != null

            val hasSeenOnboarding by produceState(initialValue = false, context) {
                value = readOnboardingCompleted(context)
            }

            val startDestination = when {
                isLoggedIn -> "home_main"
                !hasSeenOnboarding -> "onboarding"
                else -> "signup"
            }

            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                ) {

                    composable("onboarding") {
                        val coroutineScope = rememberCoroutineScope()
                        OnboardingScreen(
                            onFinish = {
                                coroutineScope.launch {
                                    saveOnboardingCompleted(context)
                                    navController.navigate("signup") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            },
                            onSignUpClick = {
                                navController.navigate("signup") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("signup") { SignUpScreen(navController) }
                    composable("user_detail") { DetailPage(navController) }
                    composable("signin") { SignInScreen(navController) }
                    composable("home") { HomeCheckScreen(navController) }
                    composable("home_main") { HomeScreenContainer(navController) }

                    composable(
                        route = "CourseDescriptionScreen/{courseId}",
                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                    ) {
                        val courseId = it.arguments?.getString("courseId") ?: ""
                        CourseDetailScreen(courseId, navController)
                    }

                    composable(
                        route = "videoPlayerScreen/{videoUrl}",
                        arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
                    ) {
                        val encodedUrl = it.arguments?.getString("videoUrl") ?: ""
                        val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                        VideoPlayerScreen(decodedUrl, navController)
                    }

                    composable("refferal") { ReferralScreen(navController) }

                    composable(
                        route = "differentPaymentScreen/{courseId}",
                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                    ) {
                        val courseId = it.arguments?.getString("courseId") ?: ""
                        PaymentScreen(courseId, navController)
                    }

                    composable("signupMail") { SignUpScreenMail(navController) }
                    composable("forgot_password") { ForgotPasswordScreen(navController) }
                }

                // ✅ Step 3: Handle notification click
                LaunchedEffect(Unit) {
                    val openLive = intent.getBooleanExtra("open_live", false)
                    if (openLive) {
                        navController.navigate("home_main")
                    }
                }
            }
        }
    }

    // 🔥 Notification Channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "live_channel",
                "Live Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    // 🔥 Notification Permission
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }
}

@Composable
fun Succes() {
    Text("done")
}