//package com.example.drishtimukesh.screen
//
//import android.content.Intent
//import android.net.Uri
//import android.widget.Toast
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.example.drishtimukesh.R
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.ktx.Firebase
//
//// --- PLACEHOLDER DEFINITIONS (Delete if they exist elsewhere) ---
//
//enum class ClassType {
//    CLASS_9, CLASS_10, CLASS_11, CLASS_12
//}
//
//data class User(
//    val userName: String = "",
//    val name: String = "",
//    val email: String = "",
//    val phone: String = "",
//    val userClass: String = ClassType.CLASS_9.name,
//    val password: String = "",
//    val coins: Int = 0,
//    val listOfCourses: List<String> = emptyList(),
//    val deviceId: String = ""
//)
//
//// Placeholder for RevolvingDashedOutlinedTextField to allow compilation
//@Composable
//fun RevolvingDashedOutlinedTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: @Composable (() -> Unit)? = null,
//    enabled: Boolean = true,
//    readOnly: Boolean = false,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    modifier: Modifier = Modifier,
//) {
//    // Using a standard OutlinedTextField as a stand-in for the custom component
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = label,
//        enabled = enabled,
//        readOnly = readOnly,
//        trailingIcon = trailingIcon,
//        keyboardOptions = keyboardOptions,
//        keyboardActions = keyboardActions,
//        visualTransformation = visualTransformation,
//        modifier = modifier.height(56.dp) // Added fixed height for stability
//    )
//}
//
//
//// --- PROFILE SCREEN IMPLEMENTATION ---
//
////@Composable
////fun ProfileScreen(navController: NavController) {
////    val context = LocalContext.current
////    val firestore = FirebaseFirestore.getInstance()
////    val auth = Firebase.auth
////    val currentUser = auth.currentUser
////
////    // State for user data
////    var userData by remember { mutableStateOf<User?>(null) }
////    var isLoading by remember { mutableStateOf(true) }
////
////    // Use this state to track if the profile was not found (specific error)
////    var profileNotFound by remember { mutableStateOf(false) }
////    var errorMessage by remember { mutableStateOf<String?>(null) }
////
////    // State for editable fields
////    var isEditing by remember { mutableStateOf(false) }
////    var newPhone by remember { mutableStateOf("") }
////    var newClass by remember { mutableStateOf(ClassType.CLASS_9.name) }
////    var newPassword by remember { mutableStateOf("") }
////    var confirmPassword by remember { mutableStateOf("") }
////    var passwordVisible by remember { mutableStateOf(false) }
////    var conffirmPasswordVisible by remember { mutableStateOf(false) }
////
////    // Password validation rules
////    val hasMinLength = newPassword.length >= 8
////    val hasUppercase = newPassword.any { it.isUpperCase() }
////    val hasNumber = newPassword.any { it.isDigit() }
////    val hasSpecialChar = newPassword.any { !it.isLetterOrDigit() }
////    val isPasswordValid = hasMinLength && hasUppercase && hasNumber && hasSpecialChar && newPassword == confirmPassword
////
////    // Function to fetch data
////    LaunchedEffect(currentUser?.uid) {
////        if (currentUser?.uid == null) {
////            errorMessage = "User not logged in."
////            isLoading = false
////            return@LaunchedEffect
////        }
////        firestore.collection("users").document(currentUser.uid).get()
////            .addOnSuccessListener { document ->
////                if (document.exists()) {
////                    val user = document.toObject(User::class.java)
////                    userData = user
////                    newPhone = user?.phone ?: ""
////                    newClass = user?.userClass ?: ClassType.CLASS_9.name
////                    profileNotFound = false // Found the profile
////                } else {
////                    errorMessage = "Your user profile data is missing. Please complete your details."
////                    profileNotFound = true // Profile not found
////                }
////                isLoading = false
////            }
////            .addOnFailureListener { e ->
////                errorMessage = e.localizedMessage
////                isLoading = false
////            }
////    }
////
////    // Function to handle sign out
////    val handleSignOut: () -> Unit = {
////        auth.signOut()
////        Toast.makeText(context, "Signed out successfully.", Toast.LENGTH_SHORT).show()
////        navController.navigate("signin") { // Assuming "signin" is the route to your sign-in screen
////            popUpTo(navController.graph.id) { inclusive = true }
////        }
////    }
////
////    // Function to handle save
////    val handleSave: () -> Unit = {
////        if (currentUser?.uid == null) {
////            Toast.makeText(context, "Authentication error. Please sign in again.", Toast.LENGTH_SHORT).show()
////            isEditing = false
////        }
////        else if (newPassword.isNotBlank() && !isPasswordValid) {
////            Toast.makeText(context, "New password does not meet requirements.", Toast.LENGTH_LONG).show()
////        }
////        else {
////            // 1. Update Firestore (Phone and Class)
////            val updates = mutableMapOf<String, Any>()
////            if (newPhone != userData?.phone) {
////                updates["phone"] = newPhone
////            }
////            if (newClass != userData?.userClass) {
////                updates["userClass"] = newClass
////            }
////
////            if (updates.isNotEmpty()) {
////                firestore.collection("users").document(currentUser.uid)
////                    .update(updates)
////                    .addOnSuccessListener {
////                        userData = userData?.copy(phone = newPhone, userClass = newClass)
////                        Toast.makeText(context, "Profile details updated.", Toast.LENGTH_SHORT).show()
////                        isEditing = false
////                    }
////                    .addOnFailureListener {
////                        Toast.makeText(context, "Failed to update profile: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
////                    }
////            }
////
////            // 2. Update Firebase Auth Password (if provided)
////            if (newPassword.isNotBlank() && newPassword == confirmPassword) {
////                currentUser.updatePassword(newPassword)
////                    .addOnSuccessListener {
////                        Toast.makeText(context, "Password updated successfully.", Toast.LENGTH_SHORT).show()
////                        newPassword = ""
////                        confirmPassword = ""
////                        isEditing = false
////                    }
////                    .addOnFailureListener {
////                        Toast.makeText(context, "Password update failed. Re-authentication may be required: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
////                    }
////            } else if (updates.isEmpty()) {
////                Toast.makeText(context, "No changes made to save.", Toast.LENGTH_SHORT).show()
////                isEditing = false
////            }
////        }
////    }
////
////    // Function to navigate to DetailPage
////    val navigateToDetailPage: () -> Unit = {
////        navController.navigate("user_detail") {
////            // Clear the back stack to prevent navigation back to the error state
////            popUpTo(navController.graph.id) { inclusive = false }
////        }
////    }
////
////    Box(modifier = Modifier.fillMaxSize()) {
////        // Background setup
////        Image(
////            painter = painterResource(id = R.drawable.lightmode), // Your background image
////            contentDescription = "Background",
////            contentScale = ContentScale.Crop,
////            modifier = Modifier.matchParentSize()
////        )
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .background(
////                    brush = Brush.linearGradient(
////                        colors = listOf(
////                            Color(0xFFFFFFFF).copy(alpha = 0.15f),
////                            Color(0xFFFFFFFF).copy(alpha = 0.05f)
////                        ),
////                        start = Offset(0f, Float.POSITIVE_INFINITY),
////                        end = Offset(Float.POSITIVE_INFINITY, 0f)
////                    )
////                )
////        ) {
////            // Header
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(top = 48.dp, start = 32.dp, end = 32.dp),
////                horizontalArrangement = Arrangement.SpaceBetween,
////                verticalAlignment = Alignment.CenterVertically
////            ) {
////                Column {
////                    Text(text = "My Profile", fontSize = 32.sp, fontWeight = FontWeight.Bold)
////                    Text(text = "Manage your account details", fontSize = 14.sp, color = Color.Gray)
////                }
////                // Edit button only visible if profile is loaded and not loading
////                if (!isLoading && userData != null) {
////                    IconButton(
////                        onClick = { isEditing = !isEditing },
////                        modifier = Modifier.background(
////                            color = if (isEditing) Color(0xFFFFC856) else Color.LightGray.copy(alpha = 0.5f),
////                            shape = RoundedCornerShape(12.dp)
////                        )
////                    ) {
////                        Icon(
////                            imageVector = Icons.Default.Edit,
////                            contentDescription = if (isEditing) "Stop Editing" else "Start Editing",
////                            tint = if (isEditing) Color.Black else Color.DarkGray
////                        )
////                    }
////                }
////            }
////
////            if (isLoading) {
////                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
////                    CircularProgressIndicator(color = Color(0xFFFCDB39))
////                }
////            }
////            // 🚨 PROFILE NOT FOUND / ERROR STATE WITH BUTTON
////            else if (profileNotFound) {
////                Column(
////                    modifier = Modifier
////                        .fillMaxSize()
////                        .padding(32.dp),
////                    horizontalAlignment = Alignment.CenterHorizontally,
////                    verticalArrangement = Arrangement.Center
////                ) {
////                    Text("⚠️ Profile Setup Required", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFA000))
////                    Spacer(modifier = Modifier.height(16.dp))
////                    Text(
////                        text = errorMessage ?: "Your full account details were not found in the database. Please complete the setup now.",
////                        color = Color.DarkGray,
////                        textAlign = TextAlign.Center,
////                        modifier = Modifier.padding(horizontal = 16.dp)
////                    )
////                    Spacer(modifier = Modifier.height(32.dp))
////
////                    // The required button to complete setup
////                    Button(
////                        onClick = navigateToDetailPage,
////                        modifier = Modifier
////                            .fillMaxWidth(0.8f)
////                            .height(50.dp)
////                            .background(
////                                brush = Brush.horizontalGradient(colors = listOf(Color(0xFF221932), Color(0xFF492f4e))),
////                                shape = RoundedCornerShape(16.dp)
////                            ),
////                        contentPadding = PaddingValues(),
////                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
////                        border = BorderStroke(
////                            width = 2.dp,
////                            brush = Brush.horizontalGradient(colors = listOf(Color(0xFFFFB330), Color(0xFFFFFCC0)))
////                        ),
////                        shape = RoundedCornerShape(16.dp)
////                    ) {
////                        Text(text = "Complete Profile Details", color = Color(0xFFFFC856), fontWeight = FontWeight.Bold)
////                    }
////
////                    Spacer(modifier = Modifier.height(32.dp))
////                    Button(
////                        onClick = handleSignOut,
////                        modifier = Modifier.fillMaxWidth(0.8f),
////                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
////                    ) {
////                        Text("Sign Out", color = Color.White)
////                    }
////                }
////            }
////            // 👤 REGULAR PROFILE CONTENT
////            else if (userData != null) {
////                ProfileContent(
////                    userData = userData!!,
////                    isEditing = isEditing,
////                    newPhone = newPhone,
////                    onPhoneChange = { newPhone = it },
////                    newClass = newClass,
////                    onClassChange = { newClass = it },
////                    newPassword = newPassword,
////                    onNewPasswordChange = { newPassword = it },
////                    confirmPassword = confirmPassword,
////                    onConfirmPasswordChange = { confirmPassword = it },
////                    handleSave = handleSave,
////                    handleSignOut = handleSignOut,
////                    passwordVisible = passwordVisible,
////                    onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
////                    conffirmPasswordVisible = conffirmPasswordVisible,
////                    onConfirmPasswordVisibilityToggle = { conffirmPasswordVisible = !conffirmPasswordVisible },
////                    isPasswordValid = isPasswordValid,
////                    hasMinLength = hasMinLength,
////                    hasUppercase = hasUppercase,
////                    hasNumber = hasNumber,
////                    hasSpecialChar = hasSpecialChar
////                )
////            }
////        }
////    }
////}
//
//@Composable
//fun ProfileScreen(navController: NavController) {
//    val context = LocalContext.current
//    val firestore = FirebaseFirestore.getInstance()
//    val auth = Firebase.auth
//    val currentUser = auth.currentUser
//
//    var userData by remember { mutableStateOf<User?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var profileNotFound by remember { mutableStateOf(false) }
//
//    var isEditing by remember { mutableStateOf(false) }
//    var newPhone by remember { mutableStateOf("") }
//    var newClass by remember { mutableStateOf(ClassType.CLASS_9.name) }
//
//    var newPassword by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//
//    val hasMinLength = newPassword.length >= 8
//    val hasUppercase = newPassword.any { it.isUpperCase() }
//    val hasNumber = newPassword.any { it.isDigit() }
//    val hasSpecialChar = newPassword.any { !it.isLetterOrDigit() }
//    val isPasswordValid =
//        hasMinLength && hasUppercase && hasNumber && hasSpecialChar && newPassword == confirmPassword
//
//    LaunchedEffect(currentUser?.uid) {
//        if (currentUser?.uid == null) {
//            isLoading = false
//            return@LaunchedEffect
//        }
//
//        firestore.collection("users").document(currentUser.uid).get()
//            .addOnSuccessListener { doc ->
//                if (doc.exists()) {
//                    val user = doc.toObject(User::class.java)
//                    userData = user
//                    newPhone = user?.phone ?: ""
//                    newClass = user?.userClass ?: ClassType.CLASS_9.name
//                } else {
//                    profileNotFound = true
//                }
//                isLoading = false
//            }
//            .addOnFailureListener {
//                isLoading = false
//            }
//    }
//
//    val handleSignOut: () -> Unit = {
//        auth.signOut()
//        Toast.makeText(context, "Signed out successfully.", Toast.LENGTH_SHORT).show()
//        navController.navigate("signin") {
//            popUpTo(navController.graph.id) { inclusive = true }
//        }
//    }
//
//    val handleSave: () -> Unit = {
//        val currentUserId = currentUser?.uid
//        if (currentUserId == null) {
//            Toast.makeText(context, "Please sign in again.", Toast.LENGTH_SHORT).show()
//            return@handleSave // ✅ Correct way to exit this lambda
//        }
//
//
//        val updates = mutableMapOf<String, Any>()
//        if (newPhone != userData?.phone) updates["phone"] = newPhone
//        if (newClass != userData?.userClass) updates["userClass"] = newClass
//
//        if (updates.isNotEmpty()) {
//            firestore.collection("users").document(currentUserId)
//                .update(updates)
//                .addOnSuccessListener {
//                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT)
//                        .show()
//                    isEditing = false
//                }
//        }
//
//        if (newPassword.isNotBlank() && newPassword == confirmPassword && isPasswordValid) {
//            currentUser.updatePassword(newPassword)
//                .addOnSuccessListener {
//                    Toast.makeText(context, "Password changed!", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(context, "Password update failed.", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    // ---------------- UI ----------------
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Image(
//            painter = painterResource(id = R.drawable.lightmode),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.matchParentSize()
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .background(
//                    Brush.verticalGradient(
//                        listOf(Color.White.copy(alpha = 0.9f), Color(0xFFFFF7E0).copy(alpha = 0.7f))
//                    )
//                )
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                "My Profile",
//                style = MaterialTheme.typography.headlineMedium.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF3B2C35)
//                )
//            )
//            Text("Manage your account", color = Color.Gray, fontSize = 14.sp)
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            when {
//                isLoading -> CircularProgressIndicator(color = Color(0xFFFFB330))
//                profileNotFound -> Text("Profile not found.", color = Color.Red)
//                userData != null -> {
//                    ProfileContent(
//                        userData = userData!!,
//                        isEditing = isEditing,
//                        newPhone = newPhone,
//                        onPhoneChange = { newPhone = it },
//                        newClass = newClass,
//                        onClassChange = { newClass = it },
//                        newPassword = newPassword,
//                        onNewPasswordChange = { newPassword = it },
//                        confirmPassword = confirmPassword,
//                        onConfirmPasswordChange = { confirmPassword = it },
//                        handleSave = handleSave,
//                        handleSignOut = handleSignOut,
//                        passwordVisible = passwordVisible,
//                        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
//                        conffirmPasswordVisible = confirmPasswordVisible, // ✅ Correct parameter spelling
//                        onConfirmPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
//                        isPasswordValid = isPasswordValid,
//                        hasMinLength = hasMinLength,
//                        hasUppercase = hasUppercase,
//                        hasNumber = hasNumber,
//                        hasSpecialChar = hasSpecialChar
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(40.dp))
//            DeveloperSection()
//        }
//    }
//}
//
//@Composable
//fun DeveloperSection() {
//    val context = LocalContext.current
//
//    val devs = listOf(
//        Developer(
//            name = "Advik Srivastav",
//            role = "App Developer",
//            link = "YOUR_LINK_HERE" // 🔗 Replace this
//        ),
//        Developer(
//            name = "Mukesh Kumar",
//            role = "Backend & Firebase Engineer",
//            link = "YOUR_LINK_HERE" // 🔗 Replace this
//        ),
//        Developer(
//            name = "Ravi Sharma",
//            role = "UI Designer",
//            link = "YOUR_LINK_HERE" // 🔗 Replace this
//        )
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(
//                Brush.linearGradient(
//                    listOf(Color(0xFF3B2C35), Color(0xFF5C3D6B))
//                ),
//                shape = RoundedCornerShape(20.dp)
//            )
//            .padding(20.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Developed By", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
//        Spacer(modifier = Modifier.height(10.dp))
//
//        devs.forEach { dev ->
//            DeveloperCard(dev, context)
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            "© 2025 Drishti Institute",
//            color = Color.White.copy(alpha = 0.7f),
//            fontSize = 12.sp,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//data class Developer(
//    val name: String,
//    val role: String,
//    val link: String
//)
//
//@Composable
//fun DeveloperCard(dev: Developer, context: android.content.Context) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dev.link))
//                context.startActivity(intent)
//            },
//        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
//        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Developer",
//                tint = Color(0xFFFFC856),
//                modifier = Modifier.size(40.dp)
//            )
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(dev.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                Text(dev.role, color = Color(0xFFFFC856), fontSize = 13.sp)
//            }
//        }
//    }
//}
//
//// (ProfileContent and PasswordRuleItem remain unchanged from the previous code block)
//
//@Composable
//fun ProfileContent(
//    userData: User,
//    isEditing: Boolean,
//    newPhone: String,
//    onPhoneChange: (String) -> Unit,
//    newClass: String,
//    onClassChange: (String) -> Unit,
//    newPassword: String,
//    onNewPasswordChange: (String) -> Unit,
//    confirmPassword: String,
//    onConfirmPasswordChange: (String) -> Unit,
//    handleSave: () -> Unit,
//    handleSignOut: () -> Unit,
//    passwordVisible: Boolean,
//    onPasswordVisibilityToggle: () -> Unit,
//    conffirmPasswordVisible: Boolean,
//    onConfirmPasswordVisibilityToggle: () -> Unit,
//    isPasswordValid: Boolean,
//    hasMinLength: Boolean,
//    hasUppercase: Boolean,
//    hasNumber: Boolean,
//    hasSpecialChar: Boolean
//) {
//    val focusManager = LocalFocusManager.current
//    var expanded by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(32.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // --- Static Details (Non-editable) ---
//
//        // Username
//        RevolvingDashedOutlinedTextField(
//            value = userData.userName,
//            onValueChange = {},
//            label = { Text("Username (Read-only)") },
//            enabled = false,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Name
//        RevolvingDashedOutlinedTextField(
//            value = userData.name,
//            onValueChange = {},
//            label = { Text("Name (Read-only)") },
//            enabled = false,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Email
//        RevolvingDashedOutlinedTextField(
//            value = userData.email,
//            onValueChange = {},
//            label = { Text("Email (Read-only)") },
//            enabled = false,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // --- Editable Fields ---
//
//        // Phone Number
//        RevolvingDashedOutlinedTextField(
//            value = newPhone,
//            onValueChange = onPhoneChange,
//            label = { Text("Phone Number") },
//            enabled = isEditing,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Class Selection
//        Box(modifier = Modifier.fillMaxWidth()) {
//            RevolvingDashedOutlinedTextField(
//                value = newClass,
//                onValueChange = {},
//                label = { Text("Class") },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = isEditing,
//                readOnly = !isEditing,
//                trailingIcon = {
//                    if (isEditing) {
//                        IconButton(onClick = { expanded = true }) {
//                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Class")
//                        }
//                    }
//                }
//            )
//
//            DropdownMenu(
//                expanded = expanded && isEditing,
//                onDismissRequest = { expanded = false },
//                modifier = Modifier.width(300.dp) // Set dropdown width
//            ) {
//                ClassType.values().forEach { cls ->
//                    DropdownMenuItem(
//                        text = { Text(cls.name) },
//                        onClick = {
//                            onClassChange(cls.name)
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // --- Password Fields (Only visible in edit mode) ---
//        AnimatedVisibility(visible = isEditing) {
//            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
//                Text("Change Password (Optional)", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
//
//                RevolvingDashedOutlinedTextField(
//                    value = newPassword,
//                    onValueChange = onNewPasswordChange,
//                    label = { Text(text = "New Password") },
//                    modifier = Modifier.fillMaxWidth(),
//                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        val iconRes = if (passwordVisible) R.drawable.show else R.drawable.hide
//                        IconButton(onClick = onPasswordVisibilityToggle) {
//                            Icon(painter = painterResource(id = iconRes), contentDescription = "Toggle Password Visibility")
//                        }
//                    },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
//                )
//
//                RevolvingDashedOutlinedTextField(
//                    value = confirmPassword,
//                    onValueChange = onConfirmPasswordChange,
//                    label = { Text(text = "Confirm New Password") },
//                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
//                    visualTransformation = if (conffirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        val iconRes = if (conffirmPasswordVisible) R.drawable.show else R.drawable.hide
//                        IconButton(onClick = onConfirmPasswordVisibilityToggle) {
//                            Icon(painter = painterResource(id = iconRes), contentDescription = "Toggle Confirm Password Visibility")
//                        }
//                    },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
//                )
//
//                // Password Rules/Mismatch Indicators
//                if (newPassword.isNotBlank()) {
//                    Column(
//                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
//                    ) {
//                        PasswordRuleItem("At least 8 characters", hasMinLength)
//                        PasswordRuleItem("At least 1 uppercase letter", hasUppercase)
//                        PasswordRuleItem("At least 1 number", hasNumber)
//                        PasswordRuleItem("At least 1 special character", hasSpecialChar)
//                        if (newPassword != confirmPassword) {
//                            Text("Passwords do not match", color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- Save Button (Visible only in edit mode) ---
//        AnimatedVisibility(visible = isEditing) {
//            Button(
//                onClick = handleSave,
//                enabled = !newPassword.isNotBlank() || (newPassword.isNotBlank() && isPasswordValid),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .background(
//                        brush = Brush.horizontalGradient(colors = listOf(Color(0xFF221932), Color(0xFF492f4e))),
//                        shape = RoundedCornerShape(16.dp)
//                    ),
//                contentPadding = PaddingValues(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Transparent,
//                    disabledContainerColor = Color.Transparent,
//                    disabledContentColor = Color.Gray
//                ),
//                border = BorderStroke(
//                    width = 2.dp,
//                    brush = Brush.horizontalGradient(colors = listOf(Color(0xFFFFB330), Color(0xFFFFFCC0)))
//                ),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Text(
//                    text = "Save Changes",
//                    color = if (!newPassword.isNotBlank() || (newPassword.isNotBlank() && isPasswordValid)) Color(0xFFFFC856) else Color.Gray
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // --- Sign Out Button ---
//        Button(
//            onClick = handleSignOut,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Red.copy(alpha = 0.8f),
//                contentColor = Color.White
//            ),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Text(text = "Sign Out", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//        }
//    }
//}
//
//@Composable
//fun PasswordRuleItem(text: String, isValid: Boolean) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        Icon(
//            imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
//            contentDescription = null,
//            tint = if (isValid) Color(0xFF4CAF50) else Color(0xFFF44336),
//            modifier = Modifier.size(16.dp)
//        )
//        Spacer(Modifier.width(8.dp))
//        Text(text, fontSize = 13.sp)
//    }
//}
//
//// Helper Preview for development
//@Preview
//@Composable
//private fun ProfileScreenPreview() {
//    ProfileScreen(rememberNavController())
//}
package com.doorknob.drishti.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.doorknob.drishti.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

// -----------------------------
// Supporting Models & Enums
// -----------------------------

enum class ClassType {
    CLASS_9, CLASS_10, CLASS_11, CLASS_12
}

data class User(
    val userName: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val userClass: String = ClassType.CLASS_9.name,
    val password: String = "",
    val coins: Int = 0,
    val listOfCourses: List<String> = emptyList(),
    val deviceId: String = ""
)

@Composable
fun RevolvingDashedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        enabled = enabled,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        modifier = modifier.height(56.dp)
    )
}

// -----------------------------
// MAIN PROFILE SCREEN
// -----------------------------

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    var userData by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var profileNotFound by remember { mutableStateOf(false) }

    var isEditing by remember { mutableStateOf(false) }
    var newPhone by remember { mutableStateOf("") }
    var newClass by remember { mutableStateOf(ClassType.CLASS_9.name) }

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val hasMinLength = newPassword.length >= 8
    val hasUppercase = newPassword.any { it.isUpperCase() }
    val hasNumber = newPassword.any { it.isDigit() }
    val hasSpecialChar = newPassword.any { !it.isLetterOrDigit() }
    val isPasswordValid =
        hasMinLength && hasUppercase && hasNumber && hasSpecialChar && newPassword == confirmPassword

    // Fetch user data
    LaunchedEffect(currentUser?.uid) {
        if (currentUser?.uid == null) {
            isLoading = false
            return@LaunchedEffect
        }

        firestore.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val user = doc.toObject(User::class.java)
                    userData = user
                    newPhone = user?.phone ?: ""
                    newClass = user?.userClass ?: ClassType.CLASS_9.name
                } else {
                    profileNotFound = true
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    val handleSignOut: () -> Unit = {
        auth.signOut()
        Toast.makeText(context, "Signed out successfully.", Toast.LENGTH_SHORT).show()
        navController.navigate("signin") {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    val handleSave: () -> Unit = {
        val currentUserId = currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(context, "Please sign in again.", Toast.LENGTH_SHORT).show()
        } else {
            val updates = mutableMapOf<String, Any>()
            if (newPhone != userData?.phone) updates["phone"] = newPhone
            if (newClass != userData?.userClass) updates["userClass"] = newClass

            if (updates.isNotEmpty()) {
                firestore.collection("users").document(currentUserId)
                    .update(updates)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT)
                            .show()
                        isEditing = false
                    }
            }

            if (newPassword.isNotBlank() && newPassword == confirmPassword && isPasswordValid) {
                currentUser.updatePassword(newPassword)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Password changed!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Password update failed.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    // ---------------- UI ----------------
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.9f), Color(0xFFFFF7E0).copy(alpha = 0.7f))
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "My Profile",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B2C35)
                )
            )
            Text("Manage your account", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))

            when {
                isLoading -> CircularProgressIndicator(color = Color(0xFFFFB330))
                profileNotFound -> Text("Profile not found.", color = Color.Red)
                userData != null -> {
                    ProfileContent(
                        userData = userData!!,
                        isEditing = isEditing,
                        newPhone = newPhone,
                        onPhoneChange = { newPhone = it },
                        newClass = newClass,
                        onClassChange = { newClass = it },
                        newPassword = newPassword,
                        onNewPasswordChange = { newPassword = it },
                        confirmPassword = confirmPassword,
                        onConfirmPasswordChange = { confirmPassword = it },
                        handleSave = handleSave,
                        handleSignOut = handleSignOut,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                        conffirmPasswordVisible = confirmPasswordVisible,
                        onConfirmPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                        isPasswordValid = isPasswordValid,
                        hasMinLength = hasMinLength,
                        hasUppercase = hasUppercase,
                        hasNumber = hasNumber,
                        hasSpecialChar = hasSpecialChar
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            DeveloperSection()
        }
    }
}

// -----------------------------
// DEVELOPER SECTION
// -----------------------------

@Composable
fun DeveloperSection() {
    val context = LocalContext.current
    val devs = listOf(
        Developer(
            name = "Advik Srivastav",
            role = "Android App Developer",
            link = "https://github.com/LUAMICIFER"
        ),
        Developer(
            name = "Yashaswi Abhinav",
            role = "Fullstack Developer",
            link = "https://github.com/YashaswiAbhinav"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF3B2C35), Color(0xFF5C3D6B))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Developed By", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        devs.forEach { dev ->
            DeveloperCard(dev, context)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "© 2025 Drishti Institute",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

data class Developer(
    val name: String,
    val role: String,
    val link: String
)

@Composable
fun DeveloperCard(dev: Developer, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dev.link))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Developer",
                tint = Color(0xFFFFC856),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(dev.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(dev.role, color = Color(0xFFFFC856), fontSize = 13.sp)
            }
        }
    }
}

// -----------------------------
// PROFILE CONTENT
// -----------------------------

@Composable
fun ProfileContent(
    userData: User,
    isEditing: Boolean,
    newPhone: String,
    onPhoneChange: (String) -> Unit,
    newClass: String,
    onClassChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    handleSave: () -> Unit,
    handleSignOut: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    conffirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityToggle: () -> Unit,
    isPasswordValid: Boolean,
    hasMinLength: Boolean,
    hasUppercase: Boolean,
    hasNumber: Boolean,
    hasSpecialChar: Boolean
) {
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RevolvingDashedOutlinedTextField(
            value = userData.userName,
            onValueChange = {},
            label = { Text("Username (Read-only)") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        RevolvingDashedOutlinedTextField(
            value = userData.name,
            onValueChange = {},
            label = { Text("Name (Read-only)") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        RevolvingDashedOutlinedTextField(
            value = userData.email,
            onValueChange = {},
            label = { Text("Email (Read-only)") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        RevolvingDashedOutlinedTextField(
            value = newPhone,
            onValueChange = onPhoneChange,
            label = { Text("Phone Number") },
            enabled = isEditing,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            RevolvingDashedOutlinedTextField(
                value = newClass,
                onValueChange = {},
                label = { Text("Class") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing,
                readOnly = !isEditing,
                trailingIcon = {
                    if (isEditing) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Class")
                        }
                    }
                }
            )

            DropdownMenu(
                expanded = expanded && isEditing,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(300.dp)
            ) {
                ClassType.values().forEach { cls ->
                    DropdownMenuItem(
                        text = { Text(cls.name) },
                        onClick = {
                            onClassChange(cls.name)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(visible = isEditing) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text("Change Password (Optional)", fontWeight = FontWeight.SemiBold)
                RevolvingDashedOutlinedTextField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    label = { Text(text = "New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onPasswordVisibilityToggle) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) R.drawable.show else R.drawable.hide
                                ),
                                contentDescription = "Toggle Password"
                            )
                        }
                    }
                )

                RevolvingDashedOutlinedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text(text = "Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (conffirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onConfirmPasswordVisibilityToggle) {
                            Icon(
                                painter = painterResource(
                                    id = if (conffirmPasswordVisible) R.drawable.show else R.drawable.hide
                                ),
                                contentDescription = "Toggle Confirm Password"
                            )
                        }
                    }
                )

                if (newPassword.isNotBlank()) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        PasswordRuleItem("At least 8 characters", hasMinLength)
                        PasswordRuleItem("At least 1 uppercase letter", hasUppercase)
                        PasswordRuleItem("At least 1 number", hasNumber)
                        PasswordRuleItem("At least 1 special character", hasSpecialChar)
                        if (newPassword != confirmPassword) {
                            Text("Passwords do not match", color = Color.Red, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = isEditing) {
            Button(
                onClick = handleSave,
                enabled = !newPassword.isNotBlank() || (newPassword.isNotBlank() && isPasswordValid),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF492f4e)),
                border = BorderStroke(1.dp, Color(0xFFFFB330)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = handleSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
        ) {
            Text("Sign Out", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PasswordRuleItem(text: String, isValid: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isValid) Color(0xFF4CAF50) else Color(0xFFF44336),
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 13.sp)
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(rememberNavController())
}
