package com.example.drishtimukesh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.drishtimukesh.screen.CourseDetailScreen
import com.example.drishtimukesh.screen.HomeCheckScreen
import com.example.drishtimukesh.screen.HomeScreenContainer
import com.example.drishtimukesh.screen.PaymentScreen
import com.example.drishtimukesh.screen.ReferralScreen
import com.example.drishtimukesh.screen.VideoPlayerScreen
import com.example.drishtimukesh.signup.DetailPage
import com.example.drishtimukesh.signup.OnboardingScreen
import com.example.drishtimukesh.signup.SignInScreen
import com.example.drishtimukesh.signup.SignUpScreen
import com.example.drishtimukesh.signup.SignUpScreenMail
import com.example.drishtimukesh.signup.saveOnboardingCompleted
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
////            DrishtiMukeshTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                var context = LocalContext.current
//
//                    val navController = rememberNavController()
//
//                    NavHost(navController= navController, startDestination = "home"){
//
//                        composable ("onboarding"){
//                            val coroutineScope = rememberCoroutineScope()
//                            OnboardingScreen(
//                                onFinish = {
//                                    coroutineScope.launch {
//                                        saveOnboardingCompleted(context)
//                                        navController.navigate("signup") {
//                                            popUpTo("onboarding") { inclusive = true }
//                                        }
//                                    }
//                                },
//                                onSignUpClick = {
//                                    navController.navigate("signup") {
//                                        popUpTo("onboarding") { inclusive = true }
//                                    }
//                                }
//                            )
//                        }
//                        composable("signup") {
//                            SignUpScreen(navController)
//                        }
//                        composable("user_detail") {
//                            DetailPage(navController)
//                        }
//                        composable("signin") {
//
//                        }
//                        composable("home") {
//                            HomeCheckScreen(navController = navController)
//                        }
//
//                        // 2) actual home UI (bottom navigation + inner nav host)
//                        composable("home_main") {
//                            HomeScreenContainer(navController = navController)
//                        }
//                    }
//
//                }
////            }
//        }
//    }
//}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()

            // ✅ Get login state
            val auth = FirebaseAuth.getInstance()
            val isLoggedIn = auth.currentUser != null

            // ✅ Get onboarding state (suspend -> need produceState)
            val hasSeenOnboarding by produceState(initialValue = false, context) {
                value = readOnboardingCompleted(context)
            }

            // ✅ Decide startDestination
            val startDestination = when {
                isLoggedIn -> "home_main"        // Already logged in
                !hasSeenOnboarding -> "onboarding" // First time user
                else -> "signup"                // Onboarding done, but not logged in
            }

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                NavHost(navController = navController, startDestination = startDestination) {

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

                    composable("signup") {
                        SignUpScreen(navController)
                    }

                    composable("user_detail") {
                        DetailPage(navController)
                    }

                    composable("signin") {
                        SignInScreen(navController)
                    }

                    composable("home") {
                        HomeCheckScreen(navController = navController)
                    }

                    composable("home_main") {
                        HomeScreenContainer(navController = navController)
                    }
                    composable(
                        route = "CourseDescriptionScreen/{courseId}",
                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        CourseDetailScreen(courseId = courseId, navController = navController)
                    }
//                    composable("VideoPlayerScreen/{videoUrl}",
//                        arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
//                    ) { backStackEntry ->
//                        // Retrieve the encoded URL argument
//                        val encodedUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
//
//                        // CRUCIAL: Decode the URL before passing it to the composable
//                        val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
//
//                        VideoPlayerScreen(
//                            videoUrl = decodedUrl,
//                            navController = navController
//                        )
//                    }
                    composable(
                        route = "videoPlayerScreen/{videoUrl}",
                        arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val encodedUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                        val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())

                        VideoPlayerScreen(videoUrl = decodedUrl, navController = navController)
                    }

                    composable("refferal") { ReferralScreen(navController=navController) }
                    composable(
                        route = "differentPaymentScreen/{courseId}",
                        // 1. Define the argument type and name
                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        // 2. Extract the argument value from the backStackEntry
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""

                        // 3. Pass the extracted value to your Composable
                        PaymentScreen(courseId = courseId, navController = navController)
                    }
                    composable("signupMail") {SignUpScreenMail(navController)  }
                }
            }
        }
    }
}

@Composable
fun Succes() {
    Text("done")
}