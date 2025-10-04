package com.example.drishtimukesh.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.drishtimukesh.getAllCourses
import com.example.drishtimukesh.getCoursesByClass
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar


@Composable
fun CoursesScreen(navController: NavHostController) {
//aaise call kar sakte hai
//    LaunchedEffect(Unit) {
//        val courses = getCoursesByClass("Class 10")
//        courses.forEach {
//            Log.d("FirestoreTest", "Course: ${it.name}, Id: ${it.id}")
//        }
//    }
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
                .padding(padding)
//                .verticalScroll(rememberScrollState())
        ) {
            var search by remember { mutableStateOf("") }
            TopBar2()
            Text("Course", style = MaterialTheme.typography.titleMedium.copy(fontSize = 32.sp,color = Color.Black))
            Spacer(Modifier.height(8.dp))
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
            Spacer(Modifier.height(8.dp))
            Text("Choice your Course", style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp,color = Color.Black))
            Spacer(Modifier.height(8.dp))

//          varibales for the class filters
            // 1. STATE VARIABLES
            // State to track the currently selected class filter
            var selectedClass by remember { mutableStateOf("all") }

            // State to hold the list of courses fetched from Firebase
            var courses by remember { mutableStateOf(emptyList<Course>()) }

            // State to manage loading status
            var isLoading by remember { mutableStateOf(false) }

            // Coroutine scope to launch suspend functions (like Firebase calls)
            val coroutineScope = rememberCoroutineScope()
            val classFilters = remember { listOf("all", "Class_9", "Class_10", "Class_11", "Class_12") }
            val loadCourses = remember<(String) -> Unit> {
                { newClassFilter ->
                    selectedClass = newClassFilter
                    isLoading = true
                    coroutineScope.launch {
                        // Determine which function to call based on the filter
                        val fetchedCourses = if (newClassFilter == "all") {
                            getAllCourses() // Use your existing getAllCourses function for "all"
                        } else {
                            getCoursesByClass(newClassFilter)
                        }
                        courses = fetchedCourses
                        isLoading = false
                    }
                }
            }

            // Load initial data when the Composable first appears.
            LaunchedEffect(Unit) {
                loadCourses(selectedClass)
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // 1. Filter Buttons Row
                FilterButtonsRow(
                    classFilters = classFilters,
                    selectedClass = selectedClass,
                    onFilterSelected = loadCourses,
                    isEnabled = !isLoading // Disable buttons while a fetch is in progress
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Content Display (Loading, Empty, or List)
                CourseContent(
                    isLoading = isLoading,
                    courses = courses,
                    selectedClass = selectedClass,
                    navController =navController
                )
            }



        }
    }
}

@Composable
fun TopBar2() {
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
@Composable
private fun CourseItem(course: Course, onEnrollClick:() -> Unit) { // <-- Added onEnrollClick
        // Helper variables to map course data to the card structure
        val imageUrl = course.baseImage[0]
        val title = course.name
        val subtitle = "Class: ${course.clas.replace("_", " ")}"
        val priceText = "₹${course.price}"
        // Assuming 'oldPrice' is another field in your Course object (or hardcoded for now)
        val oldPriceText = "₹${course.price + 500}" // Placeholder for an original price

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp), // Adjusted padding
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Course Image
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Course Image for ${course.name}",
                    modifier = Modifier
                        .size(110.dp) // Slightly smaller size for better fit
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 2. Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                         maxLines = 2, // Optional: for long titles
                         overflow = TextOverflow.Ellipsis
                    )
                    // 3. Subtitle (Class)
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 4. Price Section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = priceText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = oldPriceText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5. Enroll Button
                    Button(
                        onClick = onEnrollClick,
                        // Using Material Theme primary color is usually better practice,
                        // but keeping your specified red for consistency with your request
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Enroll Now", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

}
//CourseDesciptionScreen()
@Composable
private fun FilterButtonsRow(
    classFilters: List<String>,
    selectedClass: String,
    onFilterSelected: (String) -> Unit,
    isEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        classFilters.forEach { classLevel ->
            val displayText = if (classLevel == "all") "All" else classLevel.replace("_", " ")

            Button(
                onClick = { onFilterSelected(classLevel) },
                enabled = isEnabled,
                // Optional: Highlight the selected button
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (classLevel == selectedClass) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(displayText)
            }
        }
    }
}

@Composable
private fun CourseContent(
    isLoading: Boolean,
    courses: List<Course>,
    selectedClass: String,navController: NavController
) {
    val selectedClassText = if (selectedClass == "all") "All Classes" else selectedClass.replace("_", " ")

    if (isLoading) {
        // Show loading indicator
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (courses.isEmpty()) {
        // Handle empty results
        Text(
            "No courses found for $selectedClassText.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        // Display the fetched courses in a LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(courses, key = { it.id }) { course ->
                CourseItem(course = course, onEnrollClick = {
                    navController.navigate("CourseDescriptionScreen/${course.id}")
                })
            }
        }
    }
}

@Preview
@Composable
private fun card() {
//    CoursesScreen(rememberNavController())

}
