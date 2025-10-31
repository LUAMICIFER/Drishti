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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.drishtimukesh.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay


@Composable
fun SignUpScreen(navController: NavHostController) {
    var context = LocalContext.current
    val googleSignInOptions= remember{ GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("750496390361-5dmnki45c2ous8f90nhqbqe9gcp4vabj.apps.googleusercontent.com").requestEmail().build() }
    val googleSignInClient = remember(context, googleSignInOptions) {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing), // 10s full rotation
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAnim"
    )
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult())
    { result->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                    task->
                if(task.isSuccessful){
                    Toast.makeText(context,"Google Sign-up Completed",Toast.LENGTH_SHORT).show()
                    navController.navigate("user_detail"){
                        popUpTo("signup"){
                            inclusive = true
                        }
                    }
                }else{
                    Toast.makeText(context,"Google Sign-up Failed",Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e: ApiException){
            Log.e("GoogleSignI", "Code: ${e.statusCode}, Message: ${e.localizedMessage}")
            Toast.makeText(context,"Google Sign-up Failed: ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
                .graphicsLayer {
                    rotationZ = rotation   // apply rotation
                    scaleX = 2f       // scale a bit larger to avoid cut
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
                            Color(0xFFFCDB39).copy(alpha = 0.25f), Color(0xFFFFFFFF).copy(
                                alpha = 0.1f
                            )
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
                        end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
                    )
                )

        ) {
            Box(modifier = Modifier
                .fillMaxSize()
//                .padding(24.dp),
                ,
                contentAlignment = Alignment.Center
            ) {
                TypewriterEffect()
                Column(
                    modifier = Modifier
                        .fillMaxWidth().align(Alignment.BottomEnd)
                        .background(Color.Black, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
//                     Google Sign-In Button
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Google Button
                        SignUpOptionButton(
                            text = "Continue with Google",
                            icon = R.drawable.icons8_google, // add google icon to drawable
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        ) {
                            val signInIntent = googleSignInClient.signInIntent
                            launcher.launch(signInIntent)
                        }

                        // Email Button
                        SignUpOptionButton(
                            text = "Sign up with Email",
                            icon = R.drawable.message, // add email icon
                            backgroundColor = Color(0xFF4285F4),
                            contentColor = Color.White
                        ) {
                            navController.navigate("signupMail")
                        }

                        // Phone Button
                        SignUpOptionButton(
                            text = "Sign-in",
                            icon = R.drawable.profile, // add phone icon
                            backgroundColor = Color(0xFF34A853),
                            contentColor = Color.White
                        ) {
                            navController.navigate("signin")
//                            AuthFlow()

                        }
                    }


                }
            }
        }
    }
}
@Composable
fun TypewriterEffect() {
    val fullText = "DrishtiInstitute"
    var text by remember { mutableStateOf("") }
    var showCursor by remember { mutableStateOf(true) }

    // Typewriter animation
    LaunchedEffect(Unit) {
        fullText.forEachIndexed { i, _ ->
            text = fullText.substring(0, i + 1)
            delay(150) // speed of typing
        }
    }

    // Blinking cursor
    LaunchedEffect(Unit) {
        while (true) {
            showCursor = !showCursor
            delay(500)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            buildAnnotatedString {
                append(text)
                withStyle(
                    style = SpanStyle(
                        color = if (showCursor) Color(0xFF1E88E5) else Color.Transparent
                    )
                ) {
                    append("|")
                }
            },
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5),
                fontFamily = FontFamily.Serif
            )
        )

    }
}
@Composable
fun SignUpOptionButton(
    text: String,
    icon: Int,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = if (contentColor == Color.Black) Color.Unspecified else contentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontWeight = FontWeight.Medium)
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun Helllllo() {
    SignUpScreen(rememberNavController())
//    AuthFlow()
}