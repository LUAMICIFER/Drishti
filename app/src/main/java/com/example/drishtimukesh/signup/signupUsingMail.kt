package com.example.drishtimukesh.signup

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.drishtimukesh.R // Assuming R.drawable.lightmode and R.drawable.show/hide exist
import com.example.drishtimukesh.RevolvingDashedOutlinedTextField // Custom component
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun SignUpScreenMail(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val auth = Firebase.auth

    val isPasswordValid = password.length >= 8
    val isFormValid = email.isNotEmpty() && isPasswordValid

    // ⏳ Polling to check email verification status
    if (isVerifying) {
        LaunchedEffect(Unit) {
            val maxChecks = 100 // 5 minutes max
            repeat(maxChecks) {
                // Reload the user data to get the latest verification status
                auth.currentUser?.reload()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (auth.currentUser?.isEmailVerified == true) {
                            // 🎉 Success! Email verified
                            isVerifying = false
                            Toast.makeText(context, "Email verified! Redirecting...", Toast.LENGTH_LONG).show()
                            // ➡️ Redirect to user_detail page
                            navController.navigate("user_detail") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    }
                }
                if (isVerifying) {
                    delay(3000L) // Wait 3 seconds before next check
                } else {
                    return@LaunchedEffect
                }
            }
            // Timeout after maxChecks
            if(isVerifying) {
                Toast.makeText(context, "Verification timed out. Check your inbox!", Toast.LENGTH_LONG).show()
                isVerifying = false
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image and Gradient (Matching DetailPage)
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFCDB39).copy(alpha = 0.25f), Color(0xFFFFFFFF).copy(alpha = 0.1f)
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // 📝 Title and Subtitle
            Text(
                text = "Create Account",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sign up to begin your learning journey!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 📧 Email Field
            RevolvingDashedOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 🔒 Password Field
            RevolvingDashedOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val iconRes = if (passwordVisible) R.drawable.show else R.drawable.hide
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            // ⚠️ Password Rule Check
            if (password.isNotEmpty() && !isPasswordValid) {
                Text(
                    "Password must be at least 8 characters long.",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🛑 Error Message Display
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // ✅ Verification Note (New Addition)
            if (isVerifying) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFFFF7E6), // Light Yellow/Cream background
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "📧 Verification Link Sent",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFA000) // Darker Yellow/Orange
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Please click the link in your email to verify your account. If you don't see it, **check your Spam/Junk folder**, as it sometimes ends up there.",
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            // ➕ Sign Up Button (Themed like DetailPage)
            Button(
                onClick = {
                    loading = true
                    errorMessage = null
                    handleSignUp(
                        email = email,
                        password = password,
                        context = context,
                        onSuccess = {
                            loading = false
                            isVerifying = true // Start the verification polling
                        },
                        onFailure = { error ->
                            loading = false
                            errorMessage = error.message
                        }
                    )
                },
                enabled = isFormValid && !loading && !isVerifying,
                modifier = Modifier
                    .width(400.dp)
                    .height(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF221932), Color(0xFF492f4e))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Gray
                ),
                border = BorderStroke(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFFB330), Color(0xFFFFFCC0))
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loading || isVerifying) {
                        CircularProgressIndicator(color = Color(0xFFFFC856), modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isVerifying) "Awaiting Verification..." else "Signing Up...",
                            color = Color(0xFFFFC856)
                        )
                    } else {
                        Text(
                            text = "Sign Up",
                            color = if (isFormValid) Color(0xFFFFC856) else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ➡️ Login/Sign In Link
            Row {
                Text(text = "Already have an account? ")
                Text(
                    text = "Sign In",
                    color = Color(0xFFFFC856),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("signin") }
                )
            }
        }
    }
}

// 🔐 Firebase Sign Up and Email Verification Logic
fun handleSignUp(
    email: String,
    password: String,
    context: Context,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Sign up successful. Verification email sent to $email",
                                Toast.LENGTH_LONG
                            ).show()
                            // 🔥 Firebase sign out to force verification check on next login/redirect
                            // Note: We use polling, so we keep the session active for now
                            onSuccess()
                        } else {
                            // User created but email failed to send (rare, but handle it)
                            user.delete() // Clean up the unverified user
                            onFailure(emailTask.exception ?: Exception("Failed to send verification email."))
                        }
                    }
            } else {
                onFailure(task.exception ?: Exception("Sign up failed."))
            }
        }
}


@Preview
@Composable
private fun SignUpScreenPreview() {
    SignUpScreenMail(rememberNavController())
}