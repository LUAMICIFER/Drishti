package com.example.drishtimukesh.screen

//import android.graphics.Color
import android.graphics.Color.blue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.drishtimukesh.Course
import com.example.drishtimukesh.Subject
import com.example.drishtimukesh.getCourseById
import com.example.drishtimukesh.getSubjectsByCourseId
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.drishtimukesh.Chapter
import com.example.drishtimukesh.Lecture
import com.example.drishtimukesh.getChaptersBySubjectId
import com.example.drishtimukesh.getLecturesByChapterId
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CourseDetailScreen(courseId : String,navController: NavController) {
//    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }
//    LaunchedEffect(courseId) {
//        subjects = getSubjectsByCourseId(courseId) // ✅ safe call
//    }
//    var course by remember { mutableStateOf<Course?>(null) }
//
//    LaunchedEffect(courseId) {
//        course = getCourseById(courseId)
//    }
//    var isSubscribed : Boolean = true
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(course?.name ?: "NOT FOUND", maxLines = 1) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            BottomEnrollmentBar(
//                course = course,
//                isSubscribed = isSubscribed,
//                onEnrollClick = {
//                    // 1. Logic for Enrollment (Payment/Navigation to checkout)
//                    isSubscribed = true // For demo: instantly simulate subscription
//                    println("Navigating to enrollment for ${course?.name}")
//                },
//                onLiveNowClick = {
//                    // 2. Logic for Live Now (Navigation to video player)
//                    println("Navigating to live player for ${course?.name}")
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(paddingValues)
//        ) {
//            // 1. Header Image Section
//            Image(
//                painter = rememberAsyncImagePainter(model = course?.baseImage[1]),
//                contentDescription = "Course Banner: ${course?.name}",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(220.dp),
//                contentScale = ContentScale.Crop
//            )
//
//            // 2. Main Content
//            Column(modifier = Modifier.padding(16.dp)) {
//                // Course Title and Info
//                Text(
//                    text = course?.name ?: "No",
//                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        Icons.Default.CheckCircle,
//                        contentDescription = "Lectures",
//                        modifier = Modifier.size(16.dp),
//                        tint = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "About this course",
//                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = course?.description ?: "NOT FOUND",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.DarkGray
//                )
//                Text(
//                    text = "Course Curriculum",
//                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // 5. Curriculum Structure (Subject -> Chapter -> Lecture)
//                CourseCurriculum(subjects)
//
//            }
//
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(courseId: String, navController: NavController) {
    var course by remember { mutableStateOf<Course?>(null) }
    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }

    // Load course info
    LaunchedEffect(courseId) {
        course = getCourseById(courseId)
        subjects = getSubjectsByCourseId(courseId)
    }

    var isSubscribed by remember { mutableStateOf(true) }

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
            BottomEnrollmentBar(
                course = course,
                isSubscribed = isSubscribed,
                onEnrollClick = {
                    isSubscribed = true
                },
                onLiveNowClick = {
                    println("Navigate to live player for ${course?.name}")
                }
            )
        }
    ) { paddingValues ->
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

                CourseCurriculum(courseId = courseId, subjects = subjects,navController)
            }
        }
    }
}

@Composable
fun BottomEnrollmentBar(
    course: Course?,
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
                    Text(
                        text = "Price:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "₹${course?.price}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            } else {
                Text(
                    text = "Access Included",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.Green.copy(
                            alpha = 0.8f
                        ), fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Dynamic Button based on subscription state
            val buttonText = if (isSubscribed) "Live Now" else "Enroll Now"
            val buttonColor = if (isSubscribed) Color.Blue else Color(0xFFE53935)
            val onClickAction = if (isSubscribed) onLiveNowClick else onEnrollClick

            Button(
                onClick = onClickAction,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .width(160.dp)
                    .height(50.dp)
            ) {
                Text(
                    buttonText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun CourseCurriculum(courseId: String, subjects: List<Subject>,navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        subjects.forEach { subject ->
            SubjectItem(courseId = courseId, subject = subject, navController)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SubjectItem(courseId: String, subject: Subject,navController: NavController) {
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
                ChapterItem(courseId, subject.id, chapter, navController)
            }
        }
    }
}

@Composable
fun ChapterItem(courseId: String, subjectId: String, chapter: Chapter,navController: NavController) {
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
                LectureItem(lecture,navController)
            }
        }
    }
}

@Composable
fun LectureItem(lecture: Lecture, navController: NavController) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Lecture Name and Status
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Lecture Status", tint = Color.Green, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                lecture.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Actions: PDF and Video
        Row(verticalAlignment = Alignment.CenterVertically) {
            // PDF Resource Button (Open Link)
            if (lecture.pdfLink.isNotBlank()) {
                IconButton(onClick = {
                    // Open the PDF link in an external browser
                    uriHandler.openUri(lecture.pdfLink)
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu, // Icon for documents/PDFs
                        contentDescription = "View Notes PDF",
                        tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f) // Softer icon color
                    )
                }
            }

            // Play Video Button (Navigate to player)
            IconButton(onClick = {
                // Encode the URL before passing it as a route argument
                val encodedUrl = URLEncoder.encode(lecture.videoUrl, StandardCharsets.UTF_8.toString())
                navController.navigate("VideoPlayerScreen/$encodedUrl")
            }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow, // Icon for video playback
                    contentDescription = "Play Video",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
@Preview
@Composable
private fun hiiiiiii() {
    CourseDetailScreen("100", rememberNavController())
}