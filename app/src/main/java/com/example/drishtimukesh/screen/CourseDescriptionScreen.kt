//package com.example.drishtimukesh.screen
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.navigation.NavController
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CourseDetailScreen(
//    courseId: String? = "climb101", // Default to sample data ID
//    navController: NavController // Assuming you pass NavController for back navigation
//) {
//    // In a real app, you would fetch data based on courseId.
//    // For this example, we use the sample data and simulate the subscription state.
//    val SampleCourseDetail = null
//    val course = SampleCourseDetail
//
//    // State to simulate subscription status.
//    // Replace with a real state/viewmodel call later (e.g., val isSubscribed by viewModel.isSubscribed.collectAsState())
////    var isSubscribed by remember { mutableStateOf(course.isSubscribed) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(course.name, maxLines = 1) },
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
//                    println("Navigating to enrollment for ${course.name}")
//                },
//                onLiveNowClick = {
//                    // 2. Logic for Live Now (Navigation to video player)
//                    println("Navigating to live player for ${course.name}")
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
//                painter = rememberAsyncImagePainter(model = course.imageUrl),
//                contentDescription = "Course Banner: ${course.name}",
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
//                    text = course.name,
//                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        Icons.Default.PlayCircle,
//                        contentDescription = "Lectures",
//                        modifier = Modifier.size(16.dp),
//                        tint = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "${course.totalLectures} Lectures • ${course.totalDuration}",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // 3. About Section (matching screenshot style)
//                Text(
//                    text = "About this course",
//                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = course.description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.DarkGray
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // 4. Curriculum Title
//                Text(
//                    text = "Course Curriculum",
//                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // 5. Curriculum Structure (Subject -> Chapter -> Lecture)
//                CourseCurriculum(course.curriculum)
//            }
//        }
//    }
//}
//
//// --- Dynamic Bottom Bar ---
//
//@Composable
//fun BottomEnrollmentBar(
//    course: CourseDetail,
//    isSubscribed: Boolean,
//    onEnrollClick: () -> Unit,
//    onLiveNowClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//        elevation = CardDefaults.cardElevation(8.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            if (!isSubscribed) {
//                Column {
//                    Text(
//                        text = "Price:",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color.Gray
//                    )
//                    Text(
//                        text = "₹${course.price}",
//                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
//                    )
//                }
//            } else {
//                Text(
//                    text = "Access Included",
//                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Green.copy(alpha = 0.8f), fontWeight = FontWeight.SemiBold)
//                )
//            }
//
//            // Dynamic Button based on subscription state
//            val buttonText = if (isSubscribed) "Live Now" else "Enroll Now"
//            val buttonColor = if (isSubscribed) PrimaryBlue else Color(0xFFE53935)
//            val onClickAction = if (isSubscribed) onLiveNowClick else onEnrollClick
//
//            Button(
//                onClick = onClickAction,
//                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
//                modifier = Modifier
//                    .width(160.dp)
//                    .height(50.dp)
//            ) {
//                Text(buttonText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//            }
//        }
//    }
//}
//
//// --- Curriculum Composables (Subject -> Chapter -> Lecture) ---
//
//@Composable
//fun CourseCurriculum(curriculum: List<Subject>) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        curriculum.forEach { subject ->
//            SubjectItem(subject = subject)
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//    }
//}
//
//@Composable
//fun SubjectItem(subject: Subject) {
//    var isExpanded by remember { mutableStateOf(subject.id == 1) } // Expand first subject by default
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { isExpanded = !isExpanded },
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)), // Light blue background
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = subject.name,
//                style = MaterialTheme.typography.titleMedium.copy(
//                    fontWeight = FontWeight.SemiBold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            )
//            Icon(
//                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                contentDescription = if (isExpanded) "Collapse" else "Expand",
//                tint = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
//
//    AnimatedVisibility(visible = isExpanded) {
//        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
//            subject.chapters.forEach { chapter ->
//                ChapterItem(chapter = chapter)
//                Spacer(modifier = Modifier.height(4.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun ChapterItem(chapter: Chapter) {
//    var isExpanded by remember { mutableStateOf(false) }
//
//    // Chapter Header (Looks like a nested card/box in the screenshots)
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { isExpanded = !isExpanded },
//        shape = RoundedCornerShape(8.dp),
//        colors = CardDefaults.cardColors(containerColor = LightGrayBackground),
//        elevation = CardDefaults.cardElevation(0.dp) // No elevation for nested cards
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = chapter.name,
//                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
//            )
//            Icon(
//                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                contentDescription = if (isExpanded) "Collapse" else "Expand",
//                tint = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//    }
//
//    AnimatedVisibility(visible = isExpanded) {
//        Column(modifier = Modifier.padding(top = 4.dp, start = 12.dp, end = 12.dp)) {
//            chapter.lectures.forEach { lecture ->
//                LectureItem(lecture = lecture)
//                Divider(color = DividerColor, thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun LectureItem(lecture: Lecture) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { /* Handle lecture click / start video */ }
//            .padding(vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            // Play Icon (Colored when completed, perhaps?)
//            Icon(
//                imageVector = Icons.Default.PlayArrow,
//                contentDescription = "Play Lecture",
//                tint = MaterialTheme.colorScheme.primary, // Blue play icon
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(
//                    text = lecture.name,
//                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
//                )
//                Text(
//                    text = "${lecture.durationMin} min",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
//            }
//        }
//
//        // Navigation Arrow / Status Icon
//        Icon(
//            imageVector = if (lecture.isCompleted) Icons.Default.CheckCircle else Icons.Default.KeyboardArrowRight,
//            contentDescription = "View lecture details",
//            tint = if (lecture.isCompleted) Color.Green.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
//        )
//    }
//}
