package com.example.drishtimukesh.screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

import android.R.attr.colorPrimary
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.drishtimukesh.Course
import com.example.drishtimukesh.R
import com.example.drishtimukesh.SubscriptionOption
import com.example.drishtimukesh.getCourseById
import kotlin.math.roundToInt

fun getSubscriptionOptions(baseMonthlyPrice: Double, courseClass: String): List<SubscriptionOption> {
    val isHigherClass = courseClass == "Class_11" || courseClass == "Class_12"

    val options = if (isHigherClass) {
        // Tier 2: Quarterly, Half-Yearly, Yearly
        listOf(
            // Quarterly (3 months): 5% discount
            SubscriptionOption(
                name = "Quarterly Plan",
                durationInMonths = 3,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 5.0,
                finalPrice = (baseMonthlyPrice * 3) * 0.95
            ),
            // Half-Yearly (6 months): 7.5% discount (deduced as midpoint)
            SubscriptionOption(
                name = "Half-Yearly Plan",
                durationInMonths = 6,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 7.5,
                finalPrice = (baseMonthlyPrice * 6) * 0.925
            ),
            // Yearly (12 months): 10% discount (complete payment)
            SubscriptionOption(
                name = "Yearly Plan",
                durationInMonths = 12,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 10.0,
                finalPrice = (baseMonthlyPrice * 12) * 0.90
            )
        )
    } else {
        // Tier 1: Monthly, Quarterly, Yearly
        listOf(
            // Monthly (1 month): 0% discount
            SubscriptionOption(
                name = "Monthly Plan",
                durationInMonths = 1,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 0.0,
                finalPrice = baseMonthlyPrice
            ),
            // Quarterly (3 months): 5% discount
            SubscriptionOption(
                name = "Quarterly Plan",
                durationInMonths = 3,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 5.0,
                finalPrice = (baseMonthlyPrice * 3) * 0.95
            ),
            // Yearly (12 months): 10% discount (complete payment)
            SubscriptionOption(
                name = "Yearly Plan",
                durationInMonths = 12,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 10.0,
                finalPrice = (baseMonthlyPrice * 12) * 0.90
            )
        )
    }
    return options.sortedBy { it.durationInMonths }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(courseId: String, navController: NavController) {
    var course by remember { mutableStateOf<Course?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedOption by remember { mutableStateOf<SubscriptionOption?>(null) }
    var options by remember { mutableStateOf<List<SubscriptionOption>>(emptyList()) }

    LaunchedEffect(courseId) {
        val fetchedCourse = getCourseById(courseId)
        course = fetchedCourse
        if (fetchedCourse != null) {
            options = getSubscriptionOptions(fetchedCourse.price.toDouble(), fetchedCourse.clas)
            selectedOption = options.lastOrNull() // Default to the longest plan (best value)
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course?.name ?: "Select Plan", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        },
        bottomBar = {
            PaymentBottomBar(selectedOption)
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Loading course details...")
            }
        } else if (course == null) {
            Box(Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Error: Course not found.")
            }
        } else {
            Box(            modifier = Modifier
                .fillMaxSize()
                // Apply scaffold padding here so the background covers the whole area inside the top/bottom bars
                .padding(paddingValues)
            ){
                Box(
                    modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFFF0F0F0)) // Placeholder color if resource is missing
                ) {

                    // --- USER: REPLACE THIS BLOCK WITH YOUR IMAGE COMPONENT ---
                    // UNCOMMENT THIS SECTION AND ADD YOUR RESOURCE PAINTER

                    Image(
                        // You need to import painterResource and use your actual resource ID
                        // E.g., painter = painterResource(id = R.drawable.lightmode),
                        painter = painterResource(id = R.drawable.lightmode),
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    // -----------------------------------------------------------

                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFFFFF).copy(alpha = 0.15f), // Start with a very light, low-opacity white
                                    Color(0xFFFFFFFF).copy(alpha = 0.05f)  // End with an almost transparent white
                                ),
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                end = Offset(Float.POSITIVE_INFINITY, 0f)
                            )
                        )
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Unlock ${course!!.name}",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Choose the best plan for ${course!!.clas}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    options.forEach { option ->
                        SubscriptionCard(
                            option = option,
                            isSelected = option == selectedOption,
                            onSelect = { selectedOption = it }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(
    option: SubscriptionOption,
    isSelected: Boolean,
    onSelect: (SubscriptionOption) -> Unit
) {
    val borderColor = if (isSelected) Color(0xFFFFB330) else Color.LightGray
    val containerColor = if (isSelected) Color(0xFFFCDB39).copy(alpha = 0.1f) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect(option) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Plan Name & Checkbox
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.Check else Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = if (isSelected) Color(0xFFFFB330) else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        option.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Total Price
                Text(
                    text = "₹${option.finalPrice.roundToInt()}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE53935)
                    )
                )
            }

            Divider(Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))

            // Savings and Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Effective Monthly Price
                Column {
                    Text("Effective Monthly", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        "₹${(option.finalPrice / option.durationInMonths).roundToInt()}",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Discount/Savings
                if (option.savingsPercent > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("You Save", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "₹${option.totalDiscount.roundToInt()} (${option.savingsPercent}%)",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Green.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

//@Composable
//fun PaymentBottomBar(selectedOption: SubscriptionOption?) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//        elevation = CardDefaults.cardElevation(16.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(
//                    text = selectedOption?.name ?: "Select a Plan",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.Gray
//                )
//                Text(
//                    text = "₹${selectedOption?.finalPrice?.roundToInt() ?: 0}",
//                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
//                )
//            }
//
//            Button(
//                onClick = {
//                    println("PROCEED TO PAYMENT for ${selectedOption?.name}")
//                    // TODO: Implement actual payment gateway navigation
//
//                },
//                enabled = selectedOption != null,
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC856)),
//                modifier = Modifier
//                    .width(180.dp)
//                    .height(50.dp)
//            ) {
//                Text(
//                    "Proceed",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//            }
//        }
//    }
//}
@Composable
fun PaymentBottomBar(selectedOption: SubscriptionOption?) {
    // CRITICAL: Get the current context for accessing the Activity
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = selectedOption?.name ?: "Select a Plan",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Text(
                    text = "₹${selectedOption?.finalPrice?.roundToInt() ?: 0}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
            }

            Button(
                onClick = {
                    // 1. Find the hosting Activity
                    val activity = context.findActivity()

                    // 2. Check if the activity is the one hosting the payment logic
                    if (activity is PaymentActivity) {
                        val amount = selectedOption?.finalPrice ?: 0.0

                        if (amount > 0) {
                            Log.i("Payment", "Initiating payment for: ${selectedOption?.name} - ₹$amount")
                            activity.startRazorpayPayment(
                                activity = activity,
                                amountInRupees = amount,
                                // IMPORTANT: Replace these mock values with actual user data from your app's state
                                userEmail = "current.user@example.com",
                                userContact = "9999999999"
                            )
                        }
                    } else {
                        // This error should only occur if the screen is previewed or launched incorrectly
                        Log.e("Payment", "ERROR: Hosting Activity is not PaymentActivity. Cannot initiate payment.")
                    }
                },
                enabled = selectedOption != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC856)),
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp)
            ) {
                Text(
                    "Proceed",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

// --- PREVIEWS ---

@Preview
@Composable
private fun CourseDetailScreenPreview() {
    // Shows the Course Detail Screen with the "Enroll Now" button
    CourseDetailScreen("100", rememberNavController())
}
@Preview
@Composable
private fun PaymentScreenPreview() {
    // Shows the new Payment Screen for a CLASS_10 course
    PaymentScreen("100", rememberNavController())
}
