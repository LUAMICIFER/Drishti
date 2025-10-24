package com.example.drishtimukesh.signup

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled
//import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.drishtimukesh.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
//import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ktx.firestore // 🚨 NEW
import com.google.firebase.auth.ktx.auth // 🚨 NEW
import com.google.firebase.ktx.Firebase // 🚨 NEW


@Composable
fun SignInScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Local State for Email/Password Sign-in
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // --- Authentication Setup ---

    // 1. Google Sign-in Setup
    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("750496390361-5dmnki45c2ous8f90nhqbqe9gcp4vabj.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember(context, googleSignInOptions) {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    // 🚨 CORRECTED: Asynchronous Navigation Logic
    val navigateAfterSignIn: () -> Unit = {
        // Use the asynchronous check
        checkUserDetailsAvailability { hasDetails ->
            if (hasDetails) {
                Toast.makeText(context, "Sign-in successful, navigating to home.", Toast.LENGTH_SHORT).show()
                navController.navigate("home_main") {
                    popUpTo("signin") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Sign-in successful, completing details.", Toast.LENGTH_SHORT).show()
                navController.navigate("user_detail") {
                    popUpTo("signin") { inclusive = true }
                }
            }
        }
    }

    // Google Sign-in Launcher
    val googleLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navigateAfterSignIn() // 👈 Call the asynchronous navigation
                } else {
                    Toast.makeText(context, "Google Sign-in Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Code: ${e.statusCode}, Message: ${e.localizedMessage}")
            Toast.makeText(context, "Google Sign-in Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 2. Email/Password Sign-in Action
    val signInWithEmail: () -> Unit = {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
        } else {
            Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navigateAfterSignIn() // 👈 Call the asynchronous navigation
                } else {
                    Toast.makeText(context, "Sign-in Failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // --- UI Layout ---

    // Background and Animation Setup
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotationAnim"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
                .graphicsLayer {
                    rotationZ = rotation
                    scaleX = 2f
                    scaleY = 2f
                }
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFCDB39).copy(alpha = 0.25f),
                            Color(0xFFFFFFFF).copy(alpha = 0.1f)
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )

        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Typewriter effect for the logo/name
                // Assuming TypewriterEffect is defined/imported correctly
                TypewriterEffect()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .background(
                            Color.Black,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 1. Google Sign-In Button (TOP)
                    // Assuming SignUpOptionButton is defined/imported correctly
                    SignUpOptionButton(
                        text = "Continue with Google",
                        icon = R.drawable.icons8_google,
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ) {
                        val signInIntent = googleSignInClient.signInIntent
                        googleLauncher.launch(signInIntent)
                    }

                    // 2. Separator (OR)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(Color.Gray.copy(alpha = 0.5f)))
                        Text(text = " OR ", color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
                        Box(modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(Color.Gray.copy(alpha = 0.5f)))
                    }


                    // 3. Email Input
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            cursorColor = Color.White,
                            focusedLabelColor = Color(0xFFFCDB39),
                            unfocusedLabelColor = Color.Gray,
                            // ✅ CORRECT M3 parameter names for the border/indicator:
                            focusedIndicatorColor = Color(0xFFFCDB39),
                            unfocusedIndicatorColor = Color.Gray,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 4. Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            // ✅ MODIFIED: Using R.drawable for icons
                            val iconRes = if (passwordVisible) R.drawable.show else R.drawable.hide
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = iconRes), // Using painterResource
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = Color.Gray // Keep tint if you want it to be grey
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            cursorColor = Color.White,
                            focusedLabelColor = Color(0xFFFCDB39),
                            unfocusedLabelColor = Color.Gray,
                            focusedIndicatorColor = Color(0xFFFCDB39), // Corrected M3 param
                            unfocusedIndicatorColor = Color.Gray,     // Corrected M3 param
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Forgot Password Link
                    Text(
                        text = "Forgot Password?",
                        color = Color(0xFFFCDB39),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp)
                            .clickable {
                                // Implement navigation to Forgot Password screen
                                // navController.navigate("forgot_password")
                            }
                            .align(Alignment.End)
                    )

                    // 5. Sign In Button (Final Action)
                    Button(
                        onClick = signInWithEmail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFCDB39),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Sign In", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }

                    // Link to Sign Up
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Don't have an account?", color = Color.White.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = {
                                navController.navigate("signup") {
                                    popUpTo("signin") { inclusive = true } // Clear signin from backstack
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFFFCDB39)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("Sign Up")
                        }
                    }
                }
            }
        }
    }
}

// 🚨 COMPLETED ASYNCHRONOUS CHECK FUNCTION
/**
 * Checks Firestore to see if a user profile document exists for the current Firebase user.
 * This determines whether to send the user to the detail page or the home screen.
 *
 * @param onResult A callback function that receives 'true' if the details document exists,
 * or 'false' otherwise.
 */
fun checkUserDetailsAvailability(onResult: (Boolean) -> Unit) {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore

    if (user == null) {
        onResult(false)
        return
    }

    // Check for a document with the user's UID in the "users" collection
    db.collection("users").document(user.uid).get()
        .addOnSuccessListener { documentSnapshot ->
            // documentSnapshot.exists() is the crucial check
            onResult(documentSnapshot.exists())
        }
        .addOnFailureListener { e ->
            Log.e("UserDetailsCheck", "Error fetching user document: ${e.message}")
            // Assume incomplete if the check fails
            onResult(false)
        }
}

@Preview
@Composable
private fun Hii() {
    SignInScreen(rememberNavController())
}