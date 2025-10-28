package com.example.drishtimukesh.signup

//package com.example.drishtimukesh.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

//import com.example.drishtimukesh.AuthService // Import the AuthService object

// Make sure you have the Firebase dependencies added in your build.gradle (app) file:
// implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
// implementation("com.google.firebase:firebase-auth-ktx")

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object AuthService {

    private val auth = FirebaseAuth.getInstance()

    /**
     * Attempts to create a new user with email and password,
     * and immediately sends a verification email.
     * @return A message indicating success or the specific error.
     */
    suspend fun signUpAndSendVerification(email: String, password: String): String {
        return try {
            // 1. Create User
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            val user = userCredential.user

            // 2. Send Verification Email
            user?.sendEmailVerification()?.await()

            // 3. Success message
            "SUCCESS: Verification link sent to $email. Please check your inbox."

        } catch (e: Exception) {
            // Log the error for debugging (use Log.e in a real app)
            e.printStackTrace()
            // Return user-friendly error message
            when (e.message) {
                // Common Firebase errors
                "The email address is already in use by another account." -> "This email is already registered."
                "The email address is badly formatted." -> "Invalid email format."
                "Password should be at least 6 characters" -> "Password must be at least 6 characters."
                else -> "Sign up failed: ${e.message?.substringBefore("(") ?: "Unknown Error"}"
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen2(navController: NavHostController) {
    // --- State Variables ---
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    // Coroutine scope for running async tasks
    val scope = rememberCoroutineScope()

    // --- UI Layout ---
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Create Account") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Sign up to start earning coins!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 1. Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = "Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 2. Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password (min 6 chars)") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))

            // 3. Sign Up Button
            Button(
                onClick = {
                    if (email.isNotBlank() && password.length >= 6) {
                        isLoading = true
                        statusMessage = null
                        scope.launch {
                            val result = AuthService.signUpAndSendVerification(email, password)
                            statusMessage = result
                            isLoading = false

                            // Optional: If successful, you might want to navigate
                            // if (result.startsWith("SUCCESS")) {
                            //    navController.navigate("email_check_info_screen")
                            // }
                        }
                    } else {
                        statusMessage = "Please enter a valid email and a password of 6+ characters."
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Sign Up")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Status Message/Error Display
            statusMessage?.let { message ->
                Text(
                    text = message,
                    color = if (message.startsWith("SUCCESS")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Navigation to Login
            TextButton(onClick = {
                // Navigate to your login screen
                // navController.navigate("login_screen")
            }) {
                Text("Already have an account? Log In")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}