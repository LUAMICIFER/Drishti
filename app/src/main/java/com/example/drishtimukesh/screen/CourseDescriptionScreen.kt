package com.example.drishtimukesh.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.drishtimukesh.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(courseId: String, navController: NavController) {
    var course by remember { mutableStateOf<Course?>(null) }
    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }
    var isSubscribed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Load course info
    LaunchedEffect(courseId) {
        course = getCourseById(courseId)
        subjects = getSubjectsByCourseId(courseId)
        isSubscribed = hasActiveSubscription(courseId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course?.name ?: "Course Not Found", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        bottomBar = {
            if (!isLoading && course != null) {
                BottomEnrollmentBar(
                    course = course!!,
                    isSubscribed = isSubscribed,
                    onEnrollClick = {
                        val route = "differentPaymentScreen/${course!!.id}"
                        navController.navigate(route)
                    },
                    onLiveNowClick = {
                        // Navigate to live video or player
                        val encodedUrl = URLEncoder.encode(course!!.liveUrl, StandardCharsets.UTF_8.toString())
//                        navController.navigate("videoPlayerScreen/$encodedUrl")

                        navController.navigate("videoPlayerScreen/${encodedUrl}")
                        println("Navigate to live player for ${course!!.name}")
                    }
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                // Banner Image
                if (course?.baseImage?.isNotEmpty() == true) {
                    Image(
                        painter = rememberAsyncImagePainter(model = course!!.baseImage.first()),
                        contentDescription = "Course Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = course?.name ?: "Unnamed",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "About this course",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = course?.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Course Curriculum",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    CourseCurriculum(courseId = courseId, subjects = subjects, navController = navController, isSubscribed = isSubscribed)
                }
            }
        }
    }
}

@Composable
fun BottomEnrollmentBar(
    course: Course,
    isSubscribed: Boolean,
    onEnrollClick: () -> Unit,
    onLiveNowClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isSubscribed) {
                Column {
                    Text("Price:", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        "₹${course.price}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            } else {
                Text(
                    "Access Included",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            val buttonText = if (isSubscribed) "Live Now" else "Enroll Now"
            val buttonColor = if (isSubscribed) Color.Blue else Color(0xFFE53935)
            val onClickAction = if (isSubscribed) onLiveNowClick else onEnrollClick

            Button(
                onClick = onClickAction,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier.width(160.dp).height(50.dp)
            ) {
                Text(buttonText, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CourseCurriculum(courseId: String, subjects: List<Subject>, navController: NavController, isSubscribed: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        subjects.forEach { subject ->
            SubjectItem(courseId = courseId, subject = subject, navController = navController, isSubscribed = isSubscribed)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SubjectItem(courseId: String, subject: Subject, navController: NavController, isSubscribed: Boolean) {
    var isExpanded by remember { mutableStateOf(false) }
    var chapters by remember { mutableStateOf<List<Chapter>>(emptyList()) }

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            chapters = getChaptersBySubjectId(courseId, subject.id)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(subject.name, style = MaterialTheme.typography.titleMedium)
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }

    AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            chapters.forEach { chapter ->
                ChapterItem(courseId = courseId, subjectId = subject.id, chapter = chapter, navController = navController, isSubscribed = isSubscribed)
            }
        }
    }
}

@Composable
fun ChapterItem(courseId: String, subjectId: String, chapter: Chapter, navController: NavController, isSubscribed: Boolean) {
    var isExpanded by remember { mutableStateOf(false) }
    var lectures by remember { mutableStateOf<List<Lecture>>(emptyList()) }

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            lectures = getLecturesByChapterId(courseId, subjectId, chapter.id)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(top = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(chapter.name)
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }

    AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            lectures.forEach { lecture ->
                LectureItem(lecture, navController, isSubscribed)
            }
        }
    }
}

@Composable
fun LectureItem(lecture: Lecture, navController: NavController, isSubscribed: Boolean) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Lecture Status",
                tint = if (isSubscribed) Color.Green else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                lecture.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isSubscribed) Color.Unspecified else Color.Gray
            )
        }

        if (isSubscribed) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (lecture.pdfLink.isNotBlank()) {
                    IconButton(onClick = { uriHandler.openUri(lecture.pdfLink) }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "View Notes PDF",
                            tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
                        )
                    }
                }

                IconButton(onClick = {
                    val encodedUrl = URLEncoder.encode(lecture.videoUrl, StandardCharsets.UTF_8.toString())
                    navController.navigate("VideoPlayerScreen/$encodedUrl")
                }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play Video", tint = MaterialTheme.colorScheme.primary)
                }
            }
        } else {
            IconButton(onClick = {
                Toast.makeText(context, "Subscribe to unlock this content", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.Lock, contentDescription = "Locked Content", tint = Color.Gray)
            }
        }
    }
}
@Preview
@Composable
fun PreviewCourseDetailScreen() {
    CourseDetailScreen("100", rememberNavController())
}