package com.doorknob.drishti.screen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//import androidx.compose.ui.Modifier

//import androidx.navigation.NavHostController

@Composable
fun HomeCheckScreen(navController: NavHostController) {
    val context = LocalContext.current
    var checking by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        checkDeviceIdAndLogin(
            context = context,
            onSuccess = { user ->
                // device ok -> navigate to the real home and remove the check screen
                navController.navigate("home_main") {
                    popUpTo("home") { inclusive = true } // remove the wrapper from backstack
                    launchSingleTop = true
                }
                checking = false
            },
            onMismatch = {
                // device mismatch -> user was signed out in helper
                errorMessage = "Account is logged in on another device."
                checking = false

                // navigate to signin & clear backstack so user cannot return to home
                navController.navigate("signin") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onFailure = { e ->
                errorMessage = e.message
                checking = false

                // optional: navigate to signin so user can retry login
                navController.navigate("signin") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }

    // simple progress / placeholder UI while checking
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (checking) {
            CircularProgressIndicator()
        } else {
            // you can show error toast here if needed
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun HomeScreenContainer(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            CustomBottomNavigation(
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    selectedItem = item
                    bottomNavController.navigate(item.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController, // ✅ now correct type
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Courses.route) { CoursesScreen(navController) }
            composable(Screen.Dashboard.route) { ContactUsScreen() }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
        }
    }
}






@Preview
@Composable
private fun chekc() {
    HomeScreenContainer(navController = rememberNavController())

}