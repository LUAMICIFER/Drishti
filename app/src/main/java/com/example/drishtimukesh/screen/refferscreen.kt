package com.example.drishtimukesh.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.drishtimukesh.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// --- FIREBASE AND DATA MODEL INTEGRATION ---

/**
 * Data class representing the relevant user fields from Firestore for this screen.
 * Mirrors the userName (String?) and coins (Int) fields from your provided 'User' data class.
 */
data class ReferralUserState(
    val userName: String = "Loading...", // Default state while fetching
    val coins: Int = 0,
    val isLoading: Boolean = true
)

/**
 * Placeholder function for fetching the user's referral state from Firestore.
 * NOTE: In a real Android app, you would get the actual user ID from FirebaseAuth.
 * This implementation uses a hardcoded ID for demonstration within a single Composable file.
 */
suspend fun fetchReferralUserState(userId: String): ReferralUserState {
    // We assume FirebaseFirestore.getInstance() is initialized in your Application class.
    val db = FirebaseFirestore.getInstance()
    val defaultErrorState = ReferralUserState(userName = "Error", coins = 0, isLoading = false)

    // The user's profile is assumed to be in the 'users' collection keyed by their UID.
    return try {
        val snapshot = db.collection("users").document(userId).get().await()
        if (snapshot.exists()) {
            val userName = snapshot.getString("userName") ?: "Unknown User"
            val coins = snapshot.getLong("coins")?.toInt() ?: 0

            ReferralUserState(
                userName = userName,
                coins = coins,
                isLoading = false
            )
        } else {
            // Document does not exist (New user or error)
            defaultErrorState
        }
    } catch (e: Exception) {
        // Log the error (cannot use Android Log.e here)
        e.printStackTrace()
        defaultErrorState
    }
}

// --- PLACEHOLDER SETUP (For preview and structure) ---
// Placeholder for Android Resource ID
//val R = object {
//    object drawable {
//        val lightmode = 1 // Placeholder ID for your background image
//    }
//}
// --- END PLACEHOLDER SETUP ---


/**
 * Main screen for the Refer to Earn feature, fetching data from Firestore.
 */
@Composable
@Suppress("UnusedBoxWithConstraintsScope")
fun ReferralScreen(navController: NavHostController) {
    // --- State Management for Firestore Data ---
    // Mocked User ID for demonstration (Replace with actual FirebaseAuth.getInstance().currentUser?.uid in production)
    val currentUserId: String = remember {
        // Returns the UID if the user is logged in, otherwise an empty string.
        // Assumes FirebaseAuth is initialized.
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    var userState by remember { mutableStateOf(ReferralUserState()) }

    // Coroutine to fetch data when the composable is first launched
    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotBlank()) {
            val fetchedState = fetchReferralUserState(currentUserId)
            userState = fetchedState
        }
    }
    // ------------------------------------------

    BoxWithConstraints(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth
        // Responsive sizing logic
        val padding = if (screenWidth < 600.dp) 16.dp else 32.dp
        val titleFontSize = if (screenWidth < 600.dp) 32.sp else 43.6.sp

        // Background Image (Ensure R.drawable.lightmode is a valid resource in your project)
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Main Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Show loading indicator or the content
            if (userState.isLoading) {
                LoadingIndicator()
            } else {
                // Coin Balance Display
                CoinBalanceCard(
                    coins = userState.coins.toLong(),
                    titleFontSize = titleFontSize
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "How to Earn More Coins",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937), // Matches gray-800
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Your Referral Code Section (Uses fetched userName)
                ReferralCodeSection(
                    username = userState.userName
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Referral Benefits Section
                ReferralBenefitsSection()
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color(0xFF4338CA))
        Spacer(Modifier.height(16.dp))
        Text("Fetching user data...", color = Color(0xFF4B5563))
    }
}

@Composable
fun CoinBalanceCard(coins: Long, titleFontSize: androidx.compose.ui.unit.TextUnit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEEF2FF) // indigo-50
        ),
        border = BorderStroke(2.dp, Color(0xFFC7D2FE)), // indigo-200
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🪙", // Coin emoji placeholder for SVG icon
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Your Coin Balance",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4338CA) // indigo-700
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Current Total:",
                fontSize = 14.sp,
                color = Color(0xFF4B5563), // gray-600
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = coins.toLocaleString(),
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3730A3) // indigo-800
            )
        }
    }
}

@Composable
fun ReferralCodeSection(username: String) {
    val clipboardManager = LocalClipboardManager.current
    var showToast by remember { mutableStateOf(false) } // State to control a simple toast display

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBEB) // yellow-50
        ),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)) // yellow-200
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "1. Share Your Referral Code",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFB45309) // yellow-700
            )
            Text(
                text = "Give your friends your unique username. When they sign up and use it, you both win!",
                color = Color(0xFF4B5563), // gray-600
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .border(1.dp, Color(0xFFFCD34D), MaterialTheme.shapes.small) // yellow-300 border
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your Code:",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280) // gray-500
                    )
                    Text(
                        text = username, // Displaying fetched username
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E) // yellow-800
                    )
                }

                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(username))
                        showToast = true
                        // In a real app, use a Coroutine to hide the toast after a delay
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B) // yellow-500
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text("Copy", fontSize = 14.sp, color = Color.White)
                }
            }
            if (showToast) {
                // In a real app, use a SnackbarHostState for proper toast/snackbar behavior
                Text("Code copied to clipboard!", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun ReferralBenefitsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0FFF4) // green-50
        ),
        border = BorderStroke(1.dp, Color(0xFFD1FAE5)) // green-200
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "2. Referral Rewards",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF047857) // green-700
            )
            Spacer(modifier = Modifier.height(8.dp))
            BenefitListItem("When a friend uses your code: You earn 50 Coins!", Color(0xFF065F46))
            BenefitListItem("When you successfully use a friend's code: You get a 10 Coin starter bonus.", Color(0xFF065F46))
            BenefitListItem("You can use these coins within the app for exclusive features and content.", Color(0xFF4B5563))
        }
    }
}

@Composable
fun BenefitListItem(text: String, color: Color) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "•", fontSize = 16.sp, color = color, modifier = Modifier.padding(end = 8.dp))
        Text(text = text, color = Color(0xFF4B5563))
    }
}

// Extension function placeholder for easy formatting
private fun Long.toLocaleString(): String {
    return String.format("%,d", this)
}

// Preview setup for the Composable
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewReferralScreen() {
    val navController = rememberNavController()
    // Using a MaterialTheme to ensure colors/shapes are defined for the Card and Button
    MaterialTheme {
        ReferralScreen(navController = navController)
    }
}
