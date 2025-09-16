package com.example.drishtimukesh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drishtimukesh.screen.HomeCheckScreen
import com.example.drishtimukesh.screen.HomeScreenContainer
import com.example.drishtimukesh.signup.DetailPage
import com.example.drishtimukesh.signup.OnboardingScreen
import com.example.drishtimukesh.signup.SignUpScreen
import com.example.drishtimukesh.signup.saveOnboardingCompleted
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            DrishtiMukeshTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                var context = LocalContext.current

                    val navController = rememberNavController()

                    NavHost(navController= navController, startDestination = "onboarding"){

                        composable ("onboarding"){
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

                        }
                        composable("home") {
                            HomeCheckScreen(navController = navController)
                        }

                        // 2) actual home UI (bottom navigation + inner nav host)
                        composable("home_main") {
                            HomeScreenContainer(navController = navController)
                        }
                    }

                }
//            }
        }
    }
}

@Composable
fun Succes() {
    Text("done")
}