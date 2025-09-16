package com.example.drishtimukesh.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.drishtimukesh.Course
import com.example.drishtimukesh.R
import com.example.drishtimukesh.RevolvingDashedOutlinedTextField


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen() {
    BoxWithConstraints(modifier = Modifier
        .background(Color.White)
        .fillMaxSize()) {
        val screenWidth = maxWidth
        val padding = if (screenWidth < 600.dp) 16.dp else 32.dp
        val titleFontSize = if (screenWidth < 600.dp) 32.sp else 43.6.sp
        Image(
//            painter = painterResource(id = R.drawable.doodle), // Your background image
            painter = painterResource(id = R.drawable.lightmode), // Your background image
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(padding).verticalScroll(rememberScrollState())
        ) {
            TopBar()
            var search by remember { mutableStateOf("") }
            Spacer(Modifier.height(24.dp))
            RevolvingDashedOutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(52.dp), // Reduced height
                label = { Text(
                        buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                                    color = Color(0xFF999999)
                                )
                            ) {
                                append("Search ")
                            }
                            withStyle(
                                style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
                                    color = Color(0xFF1B1126)
                                )
                            ) {
                                append("Course")
                            }
                        }
                    ) },
                leadingIcon = {
                    val iconRes = R.drawable.search
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Toggle Password Visibility"
                        )
                },
                trailingIcon = {
                    val iconRes = R.drawable.filter
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

//            Spacer()
            StartExcellingSection()
            Spacer(modifier = Modifier.height(24.dp))

//            ExploreButton()

            Spacer(modifier = Modifier.height(24.dp))

//            PromoCard(screenWidth)
            PromoPager(
                screenWidth = 400.dp,
                imageUrls = listOf(
                    "https://picsum.photos/800/400",   // Random image (changes on refresh)
                    "https://placekitten.com/800/400", // Cute kittens 🐱
                    "https://via.placeholder.com/800x400.png?text=Promo+1", // Placeholder with text
                    "https://loremflickr.com/800/400/nature", // Random nature photo 🌿
                    "https://dummyimage.com/800x400/000/fff&text=Demo+Banner" // Custom text banner
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

//             You can add more sections here (achievements, featured courses etc.)
            popularCourses (
                imageUrl = "https://picsum.photos/200/300",
                title = "Design Training",
                subtitle = "Become a Pro Beginner Today!",
                price = "₹ 450/-",
                oldPrice = "₹ 600/-",
                onEnrollClick = { /* Handle click */ }
            )


        }
    }
}

@Composable
fun popularCourses(imageUrl: String,
                   title: String,
                   subtitle: String,
                   price: String,
                   oldPrice: String,
                   onEnrollClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Course Image
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Course Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Price Section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = price,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = oldPrice,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Enroll Button
                    Button(
                        onClick = onEnrollClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Enroll Now")
                    }
                }
            }
        }


}
//popularCourses()
@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row{
            Icon(
                painter = painterResource(id = R.drawable.arrow___right_2),
                contentDescription = "Arrow Right",
                modifier = Modifier.size(32.dp),
                tint = Color(0xFFFFAD05)
            )
            Text(
                text = buildAnnotatedString {
                    // Drishti - Black, Bigger
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Drishti")
                    }
                    // stitute - Yellow
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFFFAD05),
                            fontSize = 12.sp
                        )
                    ) {
                        append("nstitute")
                    }
                },
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
        Row{
            Box(
                modifier = Modifier
                    .size(30.dp) // Size of the circle
                    .border(
                        width = 1.dp,
                        color = Color.Gray, // Border color
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .padding(8.dp) // Space between border and icon
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.component_34),
                    contentDescription = "coin",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Box {
                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notifications",
                    tint = Color(0xFFFFAD05),
                    modifier = Modifier.size(32.dp)
                )

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Red, shape = CircleShape)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

//@Composable
@Composable
fun ExploreButton() {
    Button(
        onClick = { /* Navigate to explore */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, Color.Yellow),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Explore our exclusively curated courses",
            style = MaterialTheme.typography.labelSmall
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PromoPager(
    screenWidth: Dp,
    imageUrls: List<String> // list of image URLs
) {
    val pagerState = rememberPagerState(initialPage = 0) { imageUrls.size }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        // Pager (Carousel)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                AsyncImage(
                    model = imageUrls[page],
                    contentDescription = "Promo Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // crop nicely
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Indicator (dots)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(imageUrls.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(if (isSelected) 24.dp else 16.dp) // 👈 wider for rectangle
                        .height(8.dp) // 👈 fixed height
                        .background(
                            color = if (isSelected) Color.Yellow else Color.Gray,
                            shape = RoundedCornerShape(50) // 👈 makes it pill-like
                        )
                )
            }
        }
    }
}
@Composable
fun StartExcellingSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.fillMaxWidth()){
            Column(
//                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Ready to start \nexcelling in school exams?",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp, lineHeight = 24.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Join our courses & start learning early\nwith Drishti Classroom Programme",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Explore Our Exclusively\nFree Content Now!",
                    style = MaterialTheme.typography.labelSmall
                )


                Spacer(modifier = Modifier.height(8.dp))

                WatchFreeButton()
            }

//            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){
                Spacer(modifier = Modifier.height(65.dp))

                Image(
                    painter = painterResource(id = R.drawable.bookmedalf), // replace with your books image
                    contentDescription = "Books Image",
                    modifier = Modifier
                        .size(185.dp)
//                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }


    }
}

@Composable
fun WatchFreeButton() {
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFF7E0)
        ),
        border = BorderStroke(1.dp, Color(0xFFFFAD05)),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.video), // replace with your REC icon
            contentDescription = "REC Icon",
            tint = Color(0xFFFFAD05),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = " Watch Free ",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.Black, fontSize = 16.sp, lineHeight = 5.sp
                )
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFFFAD05),
                            fontSize = 8.sp
                        )
                    ) {
                        append("  Unlimited Access\n  ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    ) {
                        append("Demo Lectures")
                    }
                }
            )

        }
    }
}

@Preview
@Composable
private fun homes() {
    HomeScreen()
}