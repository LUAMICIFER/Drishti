package com.example.drishtimukesh.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.drishtimukesh.Course
import com.example.drishtimukesh.R
import com.example.drishtimukesh.RevolvingDashedOutlinedTextField
import com.example.drishtimukesh.Topper
import com.example.drishtimukesh.YourSubject
import com.example.drishtimukesh.addFullCourseHierarchy
import com.example.drishtimukesh.getAllToppersFromFirebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(navController : NavHostController) {
    BoxWithConstraints(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
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
            TopBar(navController)
            var search by remember { mutableStateOf("") }
            Spacer(Modifier.height(24.dp))
            RevolvingDashedOutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(52.dp), // Reduced height
                label = {
                    Text(
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
                    )
                },
                leadingIcon = {
                    val iconRes = R.drawable.search
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Toggle Password Visibility"
                    )
                },
                trailingIcon = {
                    val iconRes = R.drawable.filter
                    IconButton(onClick = { }) {
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
//            TopperPager(toppers)
//            var toppers by remember { mutableStateOf<List<Topper>>(emptyList()) }
//            var isLoading by remember { mutableStateOf(true) }

            // Use LaunchedEffect to fetch data when the composable enters composition
//            LaunchedEffect(Unit) {
//                toppers = getAllToppersFromFirebase()
//                isLoading = false
//            }
//            TopperPager(toppers)

            var toppers by remember { mutableStateOf<List<Topper>>(emptyList()) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                try {
                    toppers = getAllToppersFromFirebase()
                    if (toppers.isEmpty()) {
                        error = "No topper data found"
                    }
                } catch (e: Exception) {
                    error = "Failed to load toppers: ${e.message}"
                    Log.e("HomeScreen", "Firebase error", e)
                } finally {
                    isLoading = false
                }
            }

            // Show loading, error, or content
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                TopperPager(toppers)
            }

            // Show loading or content

            Spacer(modifier = Modifier.height(32.dp))

//             You can add more sections here (achievements, featured courses etc.)
            popularCourses(
                imageUrl = "https://picsum.photos/200/300",
                title = "Design Training",
                subtitle = "Become a Pro Beginner Today!",
                price = "₹ 450/-",
                oldPrice = "₹ 600/-",
                onEnrollClick = { /* Handle click */ }
            )
            //temp
            val context = LocalContext.current

            // Creates a CoroutineScope that is tied to the lifecycle of this Composable function.
            // When CourseCreationScreen leaves composition, the scope is cancelled.
            val scope = rememberCoroutineScope()
            Button(
                onClick = {
                    // Launch the suspend function inside the coroutine scope
                    scope.launch {
                        Toast.makeText(context, "Adding course hierarchy...", Toast.LENGTH_SHORT)
                            .show()

                        // Call the function defined in DrishtiCourseData.kt
                        val newCourseId = addFullCourseHierarchy("Class_10")

                        if (newCourseId != null) {
                            Toast.makeText(
                                context,
                                "Course structure created! ID: $newCourseId",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to create course structure. Check Logcat for details.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            ) {
                Text("Add Sample Class 12 Course")
            }


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
fun TopBar(navController: NavController) {
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
                    .clip(CircleShape).clickable {
                        navController.navigate("refferal") // Note: Fixed the typo as well!
                    }
                    .padding(8.dp) // Space between border and icon
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.component_34),
                    contentDescription = "coin",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}



//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun TopperPager(
//    toppers: List<Topper>,
//    autoSlideDuration: Long = 3000L // 3 seconds per slide
//) {
//    val pagerState = rememberPagerState(initialPage = 0) { toppers.size }
//
//    // 🔁 Auto-slide logic using coroutine
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(autoSlideDuration)
//            val nextPage = (pagerState.currentPage + 1) % toppers.size
//            pagerState.animateScrollToPage(nextPage)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Horizontal Pager showing topper cards
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(340.dp)
//        ) { page ->
//            val topper = toppers[page]
//            Card(
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(8.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//                    .fillMaxWidth()
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    AsyncImage(
//                        model = topper.imageUrl,
//                        contentDescription = "Topper Image",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(110.dp)
//                            .clip(CircleShape)
//                    )
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    Text(
//                        text = topper.name,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = "Subject: ${topper.subject}",
//                        fontSize = 15.sp,
//                        color = Color(0xFF3F51B5)
//                    )
//
//                    Text(
//                        text = "Marks: ${topper.marks}/100",
//                        fontSize = 15.sp,
//                        color = Color(0xFF444444)
//                    )
//
//                    Text(
//                        text = "Exam: ${topper.exam}",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//
//                    Text(
//                        text = "Year: ${topper.year}",
//                        fontSize = 13.sp,
//                        color = Color(0xFF757575)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Pager Indicator (dots)
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            repeat(toppers.size) { index ->
//                val isSelected = pagerState.currentPage == index
//                Box(
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .width(if (isSelected) 24.dp else 12.dp)
//                        .height(8.dp)
//                        .background(
//                            color = if (isSelected) Color(0xFFFFC107) else Color.LightGray,
//                            shape = RoundedCornerShape(50)
//                        )
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopperPager(
    toppers: List<Topper>,
    autoSlideDuration: Long = 3000L // 3 seconds per slide
) {
    val pagerState = rememberPagerState(initialPage = 0) { toppers.size }

    // 🔁 Auto-slide logic using coroutine
    LaunchedEffect(Unit) {
        while (true) {
            delay(autoSlideDuration)
            val nextPage = (pagerState.currentPage + 1) % toppers.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    if (toppers.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No topper data available")
        }
        return
    }

//    val pagerState = rememberPagerState(initialPage = 0) { toppers.size }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Horizontal Pager showing topper cards
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Increased height to accommodate multiple subjects
        ) { page ->
            val topper = toppers[page]
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Profile Image
                    AsyncImage(
                        model = convertDriveLink(topper.imageUrl),
                        contentDescription = "Topper Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Name
                    Text(
                        text = topper.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Exam and Year
                    Text(
                        text = "${topper.exam} • ${topper.year}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Subjects List
                    Text(
                        text = "Subjects & Marks",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3F51B5)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Display all subjects
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        topper.subjects.forEach { subject ->
                            SubjectRow(subject = subject)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Total Marks
                    val totalMarks = topper.subjects.sumOf { it.marks }
                    val maxTotalMarks = topper.subjects.sumOf { it.maxMarks }
                    Text(
                        text = "Total: $totalMarks/$maxTotalMarks",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pager Indicator (dots)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(toppers.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(if (isSelected) 24.dp else 12.dp)
                        .height(8.dp)
                        .background(
                            color = if (isSelected) Color(0xFFFFC107) else Color.LightGray,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

@Composable
fun SubjectRow(subject: YourSubject) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = subject.subjectName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF444444),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${subject.marks}/${subject.maxMarks}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2196F3)
        )
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
@Composable
fun TopperCard(name: String, marks: Int, exam: String, imageUrl: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(200.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.size(100.dp).clip(CircleShape))
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Marks: $marks/100")
            Text(text = "Exam: $exam")
        }
    }
}
fun convertDriveLink(originalLink: String): String {
    val regex = "https://drive\\.google\\.com/file/d/(.*?)/".toRegex()
    val match = regex.find(originalLink)
    val fileId = match?.groupValues?.get(1)
    return if (fileId != null) {
        "https://drive.google.com/uc?export=view&id=$fileId"
    } else {
        originalLink // return as-is if not a drive link
    }
}
@Preview
@Composable
private fun homes() {
//    HomeCheckScreen(navController  = rememberNavController())
    HomeScreenContainer(navController = rememberNavController())
}