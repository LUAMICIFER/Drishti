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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.drishtimukesh.Course
import com.example.drishtimukesh.R
import com.example.drishtimukesh.RevolvingDashedOutlinedTextField
import com.example.drishtimukesh.getCoursesByClass
import java.sql.Date
import java.util.Calendar


@Composable
fun CoursesScreen(modifier: Modifier = Modifier) {
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
                .verticalScroll(rememberScrollState())
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

            val scrollState = rememberScrollState()
            Row(Modifier.padding().fillMaxWidth().horizontalScroll(scrollState), horizontalArrangement = Arrangement.spacedBy(8.dp)){
                Button(onClick = {
                    //aaise call kar sakte hai
//lifecycleScope.launch {
//    val courses = getCoursesByClass("Class_10")
//    courses.forEach {
//        Log.d("Firestore", it.name)
//    }
//}

                }){
                    Text("all")
                }
                Button(onClick = {}){
                    Text("Class 9")
                }
                Button(onClick = {}){
                    Text("Class 10")
                }
                Button(onClick = {}){
                    Text("Class 11")
                }
                Button(onClick = {}){
                    Text("Class 12")
                }

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
fun coursesCard(course: Course, onEnrollClick: () -> Unit) {
    Box(Modifier.padding(8.dp).height(400.dp).width(400.dp)){
        Column {
            AsyncImage(
                model = course.baseImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(course.name)
//            course.
            Button(onEnrollClick) {
                Text("Enroll Now")
            }
        }
    }

}

@Preview
@Composable
private fun card() {

//    coursesCard(temp, onEnrollClick = {})
//    HomeCheckScreen(navController = rememberNavController())
//    HomeScreenContainer(navController = rememberNavController())
    CoursesScreen()

}
