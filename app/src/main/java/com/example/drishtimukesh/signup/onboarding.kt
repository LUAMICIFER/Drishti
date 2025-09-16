package com.example.drishtimukesh.signup

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.drishtimukesh.R
import com.example.drishtimukesh.ui.theme.Montserrat
import kotlinx.coroutines.flow.first


val Context.dataStore by preferencesDataStore("onboarding_prefs")

suspend fun saveOnboardingCompleted(context: Context) {
    context.dataStore.edit { prefs ->
        prefs[booleanPreferencesKey("onboarding_completed")] = true
    }
}

suspend fun isOnboardingCompleted(context: Context): Boolean {
    val prefs = context.dataStore.data.first()
    return prefs[booleanPreferencesKey("onboarding_completed")] ?: false
}
data class OnboardingPage(val imageRes: Int, val title: String,val title2: String, val description: String)
val onboardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.onboarding1final,
        title = "Stuck Somewhere?\n",
        title2 = "Ask Privately,\nLearn Confidently",
        description = "Too shy to ask in class? Here it's \njust you your solution"
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding2,
        title = "Learn on the Go..\n",
        title2 = "Anytime,\nAnywhere access",
        description = "Download lessons and study\noffline, anytime you like."
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding2,
        title = "No To FOMO Vibes..\n",
        title2 = "Live + Replays =\nUnlimited Learning",
        description = "Real time classes that untangle\n\"What is this chapter\" moments"
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding2,
        title = "Board Prep? Sorted..\n",
        title2 = "Courses For\nClass 9-12 Minds",
        description = "NCERT aligned content for class\n9-12 Learn what matters."
    )

)
@Composable
fun OnboardingScreen(onFinish: () -> Unit, onSignUpClick: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize().alpha(0.8f)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color(0xFFFFFFFF).copy(
//                                alpha = 0.3f
//                            ), Color(0xFFFCDB39).copy(alpha = 0.2f)
//                        )
//                    )
//                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFCDB39).copy(alpha = 0.25f),Color(0xFFFFFFFF).copy(
                            alpha = 0.1f)),
                        start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
                        end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
                    )
                )

        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(700.dp)
            ) { page ->
                val onboardingPage = onboardingPages[page]

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
//                    Image(
//                        painter = painterResource(id = onboardingPage.imageRes),
//                        contentDescription = onboardingPage.title,
//                        modifier = Modifier.size(300.dp)
//                    )
                    AnimatedImageScreen(
                        imageResId = onboardingPage.imageRes,
                        visible = pagerState.currentPage == page
                    )

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = onboardingPage.title,
                        lineHeight = 20.sp,
                        fontFamily = Montserrat,
                        color =Color(0xFF412A48),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center, fontSize = 24.sp
                    )
                    Text(
                        text = onboardingPage.title2,
                        fontFamily = Montserrat,
                        color =Color(0xFF412A48),
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 32.sp,
                        textAlign = TextAlign.Center, fontSize = 24.sp
                    )
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = onboardingPage.description,
                        fontFamily = Montserrat,
                        color =Color(0xFF2D1F3A),
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center, fontSize = 12.sp
                    )
                }
            }
//            Spacer(Modifier.height(12.dp))
            DotIndicator(
                pageCount = onboardingPages.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                activeColor = Color(0xFF412A48)
//                inactiveColor = Color.Gray
            )
            val coroutineScope = rememberCoroutineScope()
            if (pagerState.currentPage == onboardingPages.lastIndex) {
                Button(
                    onClick = onFinish,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .width(280.dp)
                        .height(50.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF221932),  // Left color
                                    Color(0xFF492f4e)   // Right color
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFB330), // Left border color
                                Color(0xFFFFFCC0)  // Right border color
                            )
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                ){
                    Text("Get Started", color = Color(0xFFFFC856))

                }
                SignupText {
//                    TODO button lagana hai
//                    SignUpMain()
                }


            }
            else{
                Button(
                    onClick = onFinish,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .width(150.dp)
                        .height(50.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF221932),  // Left color
                                    Color(0xFF492f4e)   // Right color
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFB330), // Left border color
                                Color(0xFFFFFCC0)  // Right border color
                            )
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                ){
//                    Text("Get Started", color = Color(0xFFFFC856))
                    Icon(
                        painter = painterResource(id = R.drawable.arrow___right),
                        contentDescription = "Next",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFFFFC856)
                    )

                }
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            val nextPage = (pagerState.currentPage + 1).coerceAtMost(pagerState.pageCount - 1)
//                            pagerState.animateScrollToPage(nextPage)
//                        }
//                    },
//                    modifier = Modifier
//                        .align(Alignment.CenterHorizontally)
//                        .padding(16.dp)
//                        .size(56.dp),
//                    shape = CircleShape,
//                    contentPadding = PaddingValues(0.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.arrow___right_circle),
//                        contentDescription = "Next",
//                        modifier = Modifier.size(32.dp),
//                        tint = Color.White
//                    )
//                }
            }

        }
    }
}

@Composable
fun DotIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF412A48),
    inactiveColor: Color = Color(0xFFB0B0B0)
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = if (page == currentPage) 16.dp else 8.dp, height = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = if (page == currentPage) activeColor else inactiveColor)
            )
        }
    }
}

@Composable
fun SignupText(onSignUpClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        // "Create an account?" styled
        withStyle(
            style = SpanStyle(
                color = Color(0xFF221932),
//                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("Create an account? ")
        }

        // "Sign Up" styled
        pushStringAnnotation(
            tag = "SIGN_UP",
            annotation = "sign_up"
        )
        withStyle(
            style = SpanStyle(
                color = Color(0xFF492F4E),
//                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Sign Up")
        }
        pop()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = "SIGN_UP",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    onSignUpClick()
                }
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
fun AnimatedImageScreen(imageResId: Int, visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(700)) + slideInVertically(
            animationSpec = tween(700),
            initialOffsetY = { it / 2 }
        ),
        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
            animationSpec = tween(300),
            targetOffsetY = { it / 2 }
        )
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
        )
    }
}



//@Preview(showBackground = true)
//@Composable
//private fun bla() {
//    OnboardingScreen(
//        onFinish = {}
//    ) {
//        navController.navigate("signup") {
//            popUpTo("start") { inclusive = false }
//        }
//    } // No-op for preview
//
//}