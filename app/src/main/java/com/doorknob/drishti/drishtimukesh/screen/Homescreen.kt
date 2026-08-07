package com.doorknob.drishti.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.doorknob.drishti.Course
import com.doorknob.drishti.RevolvingDashedOutlinedTextField
import com.doorknob.drishti.Topper
import com.doorknob.drishti.YourSubject
import com.doorknob.drishti.getAllCourses
import com.doorknob.drishti.getAllToppersFromFirebase
import com.doorknob.drishti.getCoursesByClass
import com.doorknob.drishti.getUserEnrolledCourses
import com.doorknob.drishti.R
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(navController: NavHostController) {
    var owncourses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var toppers by remember { mutableStateOf<List<Topper>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showFilter by remember { mutableStateOf(false) }

    val classFilters = remember {
        listOf("all", "Class_9", "Class_10", "Class_11", "Class_12")
    }
    var selectedClass by remember { mutableStateOf("all") }

    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var filteredCourses by remember { mutableStateOf<List<Course>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()
//    var isLoading by remember { mutableStateOf(false) }
    val loadCourses = remember<(String) -> Unit> {
        { newClassFilter ->
            selectedClass = newClassFilter
            isLoading = true
            coroutineScope.launch {
                val fetchedCourses = if (newClassFilter == "all") {
                    getAllCourses()
                } else {
                    getCoursesByClass(newClassFilter)
                }
                courses = fetchedCourses
                filteredCourses = fetchedCourses // reset search filter
                isLoading = false
            }
        }
    }


    BoxWithConstraints(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val padding = if (screenWidth < 600.dp) 16.dp else 32.dp
        val titleFontSize = if (screenWidth < 600.dp) 32.sp else 43.6.sp
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(navController)
            Spacer(Modifier.height(18.dp))

            SearchBarWithFilter(
                modifier = Modifier,
                onSearch = { query ->
                    searchQuery = query
                    filteredCourses =
                        if (query.isBlank()) courses
                        else courses.filter {
                            it.name.contains(query, ignoreCase = true) ||
                                    it.description.contains(query, ignoreCase = true)
                        }
                },
                onFilterClick = { showFilter = true }
            )
            if (filteredCourses.isNotEmpty()) {
                Text(
                    "Search Results",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A3C74)
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    filteredCourses.forEach { course ->
                        PremiumCourseCard(
                            course = course,
                            navController = navController
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))
            }


            CourseFilterBottomSheet(
                show = showFilter,
                classFilters = classFilters,
                selectedClass = selectedClass,
                onDismiss = { showFilter = false },
                onApply = { newClass ->
                    loadCourses(newClass)
                }
            )

            Spacer(Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(24.dp))

            StartExcellingSection()
            Spacer(modifier = Modifier.height(24.dp))

            // FIXED: Proper error handling for both toppers and courses
            LaunchedEffect(Unit) {
                try {
                    isLoading = true
                    error = null

                    // Load both data in parallel
                    val toppersDeferred = async { getAllToppersFromFirebase() }
                    val coursesDeferred = async { getUserEnrolledCourses() }

                    // Await both results
                    toppers = toppersDeferred.await()
                    owncourses = coursesDeferred.await()

                    // Handle individual empty states without overwriting errors
                    val errors = mutableListOf<String>()

                    if (toppers.isEmpty()) {
                        errors.add("No topper data found")
                    }

                    if (owncourses.isEmpty()) {
                        errors.add("No enrolled courses found")
                    }

                    // Set combined error message if any
                    if (errors.isNotEmpty()) {
                        error = errors.joinToString(". ")
                    }

                } catch (e: Exception) {
                    // More specific error handling
                    error = when {
                        e is CancellationException -> throw e // Re-throw cancellation
                        e is FirebaseFirestoreException -> "Network error: Please check your connection"
                        else -> "Failed to load data: ${e.message ?: "Unknown error"}"
                    }
                    Log.e("HomeScreen", "Firebase error", e)
                } finally {
                    isLoading = false
                }
            }

            // Show loading, error, or content for toppers section
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
                // Show error but still try to display available data
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }

                // Still show toppers if we have them, even with error
                if (toppers.isNotEmpty()) {
                    Text(
                        "Our top performers",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp, lineHeight = 24.sp)
                    )
                    TopperPager(toppers)
                }
            } else {
                // Normal flow - show toppers if available
                if (toppers.isNotEmpty()) {
                    Text(
                        "Our top performers",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp, lineHeight = 24.sp)
                    )
                    TopperPager(toppers)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Show enrolled courses
            if (owncourses.isNotEmpty()) {
                Text(
                    "My Enrolled Courses",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                EnrolledCoursesPager(courses = owncourses, navController = navController)
            } else if (!isLoading && error == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You are not enrolled in any courses yet",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
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
                    .clip(CircleShape)
                    .clickable {
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
    val context = LocalContext.current

    Button(
        onClick = {
            // 🔹 Your YouTube channel or video link
            val youtubeUrl = "https://www.youtube.com/@drishtiinstitute5667" // <-- change this
            openYoutubeChannel(context, youtubeUrl)
        },
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

/**
 * 🔹 Opens YouTube app if installed, otherwise falls back to browser.
 */
fun openYoutubeChannel(context: Context, channelUrl: String) {
    try {
        // Try opening in YouTube app first
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl))
        intent.setPackage("com.google.android.youtube")
        context.startActivity(intent)
    } catch (e: Exception) {
        // If YouTube app not available, open in browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl))
        context.startActivity(browserIntent)
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
            AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier
                .size(100.dp)
                .clip(CircleShape))
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnrolledCoursesPager(
    courses: List<Course>,
    navController: NavHostController,
    autoSlideDuration: Long = 5000L // 5 seconds per slide
) {
    val pagerState = rememberPagerState(initialPage = 0) { courses.size }
    val coroutineScope = rememberCoroutineScope()

    // Auto-slide logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(autoSlideDuration)
            if (courses.isNotEmpty()) {
                val nextPage = (pagerState.currentPage + 1) % courses.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Horizontal Pager showing enrolled courses
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp), // Adjusted height for course cards
            contentPadding = PaddingValues(horizontal = 40.dp) // Add padding for peek effect
        ) { page ->
            val course = courses[page]
            EnrolledCourseCard(
                course = course,
                onEnrollClick = {
                    navController.navigate("CourseDescriptionScreen/${course.id}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pager Indicator (dots)
        if (courses.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(courses.size) { index ->
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
}
@Composable
fun EnrolledCourseCard(
    course: Course,
    onEnrollClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Course Image
            AsyncImage(
                model = course.baseImage.getOrNull(0) ?: "",
                contentDescription = "Course Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.lightmode) // Add a placeholder drawable
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Course Title
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

//                 Course Description
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Gradient Button
                Button(
                    onClick = onEnrollClick,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
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
                ) {
                    Text("Continue", color = Color(0xFFFFC856))
                }
            }
        }
    }
}

@Composable
fun SearchBarWithFilter(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF9F7FF))
            .border(
                width = 1.dp,
                color = Color(0xFFE8DFFB),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp)
    ) {
        RevolvingDashedOutlinedTextField(
            value = query,
            onValueChange = {
                query = it

                debounceJob?.cancel()
                debounceJob = coroutine.launch {
                    delay(900)
                    onSearch(query.trim())
                }
            },
            label = {
                Text("Search Course", color = Color.Gray, fontSize = 8.sp)
            },
            modifier = Modifier.height(60.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = Color(0xFF6D4DCF)
                )
            },
            trailingIcon = {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = null,
                        tint = Color(0xFF6D4DCF)
                    )
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFilterBottomSheet(
    show: Boolean,
    classFilters: List<String>,
    selectedClass: String,
    onDismiss: () -> Unit,
    onApply: (String) -> Unit
) {
    if (!show) return

    var tempSelected by remember { mutableStateOf(selectedClass) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFDFBFF),
        tonalElevation = 8.dp,
        dragHandle = {}
    ) {
        Column(
            Modifier
                .fillMaxWidth()
//                .padding(20.dp)
                .navigationBarsPadding()
        ) {


            Text(
                "Filter Courses",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3C74)
            )

            Spacer(Modifier.height(20.dp))

            classFilters.forEach { cls ->
                val active = tempSelected == cls

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (active) Color(0xFFEDE2FF)
                            else Color.White
                        )
                        .border(
                            width = if (active) 2.dp else 1.dp,
                            color = if (active) Color(0xFF6D4DCF) else Color(0xFFE0D7F5),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { tempSelected = cls }
                        .padding(14.dp)
                ) {
                    Text(
                        cls.replace("_", " "),
                        fontSize = 16.sp,
                        color = Color(0xFF4A3C74),
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    onApply(tempSelected)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D4DCF)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Apply Filter", color = Color.White, fontSize = 17.sp)
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
@Composable
fun PremiumCourseCard(
    course: Course,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AsyncImage(
                model = course.baseImage.firstOrNull(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(Modifier.weight(1f)) {
                Text(
                    course.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF32275F),
                    maxLines = 1
                )

                Text(
                    course.description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate("CourseDescriptionScreen/${course.id}")
                    },
                    modifier = Modifier
                        .height(42.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6D4DCF)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("View", color = Color.White)
                }
            }
        }
    }
}
