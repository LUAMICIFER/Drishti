package com.doorknob.drishti.screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.doorknob.drishti.Course
import com.doorknob.drishti.SubscriptionOption
import com.doorknob.drishti.getCourseById
import com.doorknob.drishti.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

fun getSubscriptionOptions(baseMonthlyPrice: Double, courseClass: String): List<SubscriptionOption> {
    val isHigherClass = courseClass == "Class_11" || courseClass == "Class_12"

    val options = if (isHigherClass) {
        listOf(
            SubscriptionOption(
                name = "Quarterly Plan",
                durationInMonths = 3,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 5.0,
                finalPrice = (baseMonthlyPrice * 3)
            ),
            SubscriptionOption(
                name = "Half-Yearly Plan",
                durationInMonths = 6,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 7.5,
                finalPrice = (baseMonthlyPrice * 6) - 500
            ),
            SubscriptionOption(
                name = "Yearly Plan",
                durationInMonths = 12,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 10.0,
                finalPrice = (baseMonthlyPrice * 12) - 1000
            )
        )
    } else {
        listOf(
            SubscriptionOption(
                name = "Monthly Plan",
                durationInMonths = 1,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 0.0,
                finalPrice = baseMonthlyPrice
            ),
            SubscriptionOption(
                name = "Half Yearly Plan",
                durationInMonths = 6,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 5.0,
                finalPrice = (baseMonthlyPrice * 6) - (baseMonthlyPrice / 2)
            ),
            SubscriptionOption(
                name = "Yearly Plan",
                durationInMonths = 12,
                monthlyPrice = baseMonthlyPrice,
                discountPercent = 10.0,
                finalPrice = (baseMonthlyPrice * 12) - (baseMonthlyPrice)
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

    // --- NEW STATE ---
    var userCoins by remember { mutableStateOf(0) }
    var coinsApplied by remember { mutableStateOf(false) }
    var discountedPrice by remember { mutableStateOf(0) }
    var referralCode by remember { mutableStateOf("") }
    var referralApplied by remember { mutableStateOf(false) }
    var referralError by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    // --- Load Course & User Coins ---
    LaunchedEffect(courseId) {
        val fetchedCourse = getCourseById(courseId)
        course = fetchedCourse
        if (fetchedCourse != null) {
            options = getSubscriptionOptions(fetchedCourse.price.toDouble(), fetchedCourse.clas)
            selectedOption = options.lastOrNull()
            discountedPrice = selectedOption?.finalPrice?.roundToInt() ?: 0
        }

        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    val coins = doc.getLong("coins")?.toInt() ?: 0
                    userCoins = coins
                }
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
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { Text("Loading course details...") }
            }

            course == null -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { Text("Error: Course not found.") }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lightmode),
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.15f),
                                        Color.White.copy(alpha = 0.05f)
                                    ),
                                    start = Offset(0f, Float.POSITIVE_INFINITY),
                                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                                )
                            )
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
                                onSelect = {
                                    selectedOption = it
                                    discountedPrice = it.finalPrice.roundToInt()
                                    coinsApplied = false
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // --- COINS BLOCK ---
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Available Coins: $userCoins",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                    Button(
                                        onClick = {
                                            if (!coinsApplied && userCoins > 0 && selectedOption != null) {
                                                discountedPrice =
                                                    (selectedOption!!.finalPrice.roundToInt() - userCoins).coerceAtLeast(0)
                                                coinsApplied = true
                                            }
                                        },
                                        enabled = !coinsApplied && userCoins > 0,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC856))
                                    ) {
                                        Text(if (coinsApplied) "Applied" else "Use Coins", color = Color.Black)
                                    }
                                }

                                if (coinsApplied) {
                                    Text(
                                        text = "New Price: ₹$discountedPrice",
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // --- REFERRAL CODE FIELD ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = referralCode,
                                onValueChange = { referralCode = it },
                                label = { Text("Referral Code") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                placeholder = { Text("Enter friend's username") }
                            )
                            Button(
                                onClick = {
                                    referralError = null
                                    if (referralCode.isNotEmpty()) {
                                        db.collection("users")
                                            .whereEqualTo("userName", referralCode.trim())
                                            .get()
                                            .addOnSuccessListener { docs ->
                                                Log.d("ReferralCheck", "Docs found: ${docs.size()}")
                                                if (!docs.isEmpty) {
                                                    referralApplied = true
                                                    referralError = null
                                                } else {
                                                    referralApplied = false
                                                    referralError = "User not found"
                                                }
                                            }
                                            .addOnFailureListener {
                                                referralError = "Error checking referral"
                                            }
                                    } else {
                                        referralError = "Please enter a referral code"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC856))
                            ) {
                                Text(if (referralApplied) "Applied" else "Apply", color = Color.Black)
                            }
                        }

                        referralError?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- PROCEED BUTTON ---
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                if (course != null && selectedOption != null) {
                                    val intent = Intent(context, PaymentActivity::class.java).apply {
                                        putExtra("COURSE_ID", course!!.id)
                                        putExtra("COURSE_NAME", course!!.name)
                                        putExtra("COURSE_PRICE", course!!.price)
                                        putExtra("PLAN_NAME", selectedOption!!.name)
                                        putExtra("SUBSCRIPTION_MONTHS", selectedOption!!.durationInMonths)
                                        putExtra("FINAL_PRICE", discountedPrice)
                                        putExtra("REFERRAL_USERNAME", if (referralApplied) referralCode else "")
                                        putExtra("COINS_USED", if (coinsApplied) userCoins else 0)
                                    }
                                    context.startActivity(intent)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC856)),
                            modifier = Modifier.fillMaxWidth().height(55.dp),
                            enabled = selectedOption != null && course != null
                        ) {
                            Text("Proceed", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Spacer(modifier = Modifier.height(50.dp))
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
            .border(BorderStroke(2.dp, borderColor), RoundedCornerShape(16.dp))
            .clickable { onSelect(option) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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

                Text(
                    text = "₹${option.finalPrice.roundToInt()}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE53935)
                    )
                )
            }

            Divider(Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Effective Monthly", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        "₹${(option.finalPrice / option.durationInMonths).roundToInt()}",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (option.savingsPercent > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("You Save", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "₹${option.totalDiscount.roundToInt()}",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Green.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}
