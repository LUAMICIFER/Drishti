package com.example.drishtimukesh.signup

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.drishtimukesh.ClassType
import com.example.drishtimukesh.R
import com.example.drishtimukesh.RevolvingDashedOutlinedTextField
import com.example.drishtimukesh.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.String
import android.provider.Settings
import com.google.firebase.auth.EmailAuthProvider

@Composable
fun DetailPage(navController: NavController){
    val user = Firebase.auth.currentUser
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf(user?.displayName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var selectedClass by remember { mutableStateOf(ClassType.CLASS_9) }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var accepted by remember { mutableStateOf(false) }
    var showDialog by remember {mutableStateOf(false)}
    var passwordVisible by remember {mutableStateOf(false)}
    var conffirmPasswordVisible by remember {mutableStateOf(false)}
//    val isFormValid = accepted && password == confirmPassword && password.isNotEmpty()

//    var availability by remember { mutableStateOf<String?>(null) }
    var availability by remember { mutableStateOf<Boolean?>(null) }
    val isFormValid = accepted && password == confirmPassword && password.isNotEmpty() && (availability == true)
    //password rules
    var showRules by remember { mutableStateOf(false) }
    val hasMinLength = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasNumber = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    val context = LocalContext.current

    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.lightmode),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
//                .graphicsLayer {
//                    rotationZ = rotation   // apply rotation
//                    scaleX = 2f       // scale a bit larger to avoid cut
//                    scaleY = 2f
//                }
        )
        Column(
//            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFCDB39).copy(alpha = 0.25f), Color(0xFFFFFFFF).copy(
                                alpha = 0.1f
                            )
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
                        end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
                    )
                )

        ) {
            Text(text = "User Details",Modifier.padding(top=48.dp, start = 32.dp,), fontSize = 32.sp)
            Text(text = "We are here to help you reach the \npeaks of learning",Modifier.padding(start = 32.dp,top=8.dp), fontSize = 14.sp, color = Color.Gray)

            Box()
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Components go here
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RevolvingDashedOutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier
                                .weight(8f),
//                            .fillMaxWidth()
//                            .padding(16.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Unspecified,   // ✅ numeric keypad
                                imeAction = ImeAction.Next            // ✅ show "Next"
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Right) } // ⬇️ moves to next field
                            )
                        )
                        Button(
                            onClick = {
                                checkUsernameAvailability(username) { available ->
                                    availability = available
                                }
                            },
                            modifier = Modifier.size(65.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Gray
                            ),
                            border = BorderStroke(2.dp, Color.Gray),
                            contentPadding = PaddingValues(0.dp) // Removes default padding
                        ) {
                            Text("Verify", fontSize = 10.sp, maxLines = 1)
                        }
                    }
                    availability?.let { available ->
                        Text(
                            text = if (available) "✅ Username available" else "❌ Username already taken",
                            color = if (available) Color.Green else Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .padding(16.dp),
                        ,verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // 8dp gap
                    ) {
                        // Name field (60%)
                        RevolvingDashedOutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your name") },
                            modifier = Modifier.weight(0.55f) // 60%
                            ,keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,   // ✅ numeric keypad
                                imeAction = ImeAction.Next            // ✅ show "Next"
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Right) } // ⬇️ moves to next field
                            )
                        )

                        // Dropdown (40%)
                        Box(modifier = Modifier.weight(0.45f)) {
                            RevolvingDashedOutlinedTextField(
                                value = selectedClass.name,
                                onValueChange = {}, // readOnly, so no change
                                label = { Text("Class", fontSize = 10.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = true,
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select Class"
                                        )
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified,   // ✅ numeric keypad
                                    imeAction = ImeAction.Next            // ✅ show "Next"
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) } // ⬇️ moves to next field
                                )
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                ClassType.values().forEach { cls ->
                                    DropdownMenuItem(
                                        text = { Text(cls.name) },
                                        onClick = {
                                            selectedClass = cls
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    RevolvingDashedOutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text= "Email") },
                        enabled = email.isEmpty(),
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,   // ✅ numeric keypad
                            imeAction = ImeAction.Next            // ✅ show "Next"
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) } // ⬇️ moves to next field
                        )

                    )
                    RevolvingDashedOutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text(text= "Phone") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,   // ✅ numeric keypad
                            imeAction = ImeAction.Next            // ✅ show "Next"
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) } // ⬇️ moves to next field
                        )
                    )
                    RevolvingDashedOutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                showRules = focusState.isFocused
                            },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val iconRes = if (passwordVisible) R.drawable.show else R.drawable.hide
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,   // ✅ numeric keypad
                            imeAction = ImeAction.Next              // ✅ show "Next"
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) } // ⬇️ moves to next field
                        )
                    )

                    // 🔹 Show rules below the field when focused
                    AnimatedVisibility(visible = showRules) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text("Password must include:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(Modifier.height(6.dp))

                            RuleItem("At least 8 characters", hasMinLength)
                            RuleItem("At least 1 uppercase letter", hasUppercase)
                            RuleItem("At least 1 number", hasNumber)
                            RuleItem("At least 1 special character", hasSpecialChar)
                        }
                    }
                    RevolvingDashedOutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text(text= "Confirm Password") },
                        modifier = Modifier
//                            .weight(8f)
                            .fillMaxWidth(),
//                            .padding(16.dp),
                        visualTransformation = if (conffirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val iconRes = if (conffirmPasswordVisible) R.drawable.show else R.drawable.hide
                            IconButton(onClick = { conffirmPasswordVisible = !conffirmPasswordVisible }) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,   // ✅ numeric keypad
                            imeAction = ImeAction.Done        // ✅ show "Next"
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                // 🔑 Do login/register action here
                            }
//                            onNext = { focusManager.moveFocus(FocusDirection.Down) } // ⬇️ moves to next field
                        )
                    )
                    if(password!=confirmPassword) {
                        Text("Password and Confirm Password must match", color = Color.Red)
                    }
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = accepted,
                            onCheckedChange = { accepted = it }
                        )

                        Text(text = "I accept ")

                        Text(
                            text = "Terms and Conditions",
                            color = Color(0xFFFFC856),
                            modifier = Modifier.clickable { showDialog = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            loading = true
                            val user = User(
                                userName = username,
                                name = name,
                                email = email,
                                phone = phone,
                                userClass = selectedClass.name,
                                password = password,
                                coins=0,
                                listOfCourses = emptyList(),
                                deviceId= getDeviceId(context)
                            )
                            createAccount(
                                user,
                                onSuccess = {
                                    loading = false
                                    Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home") {   // ✅ Navigate to Home
                                        popUpTo("user_detail") { inclusive = true }
                                    }
                                },
                                onFailure = {
                                    loading = false
                                    errorMessage = it.localizedMessage
                                }
                            )
//                            navController.navigate("home")
                                  },
                        enabled = isFormValid, // 👈 checkbox controls button state
                        modifier = Modifier
                            .width(400.dp)
                            .height(50.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF221932),
                                        Color(0xFF492f4e)
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent, // transparent even when disabled
                            disabledContentColor = Color.Gray // text/icon color when disabled
                        ),
                        border = BorderStroke(
                            width = 2.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFFB330),
                                    Color(0xFFFFFCC0)
                                )
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Create an Account",
                                color = if (accepted) Color(0xFFFFC856) else Color.Gray
                            )
                        }
                    }
                }
                // Terms & Conditions Dialog
                LegalDialog(showDialog = showDialog, onDismiss = { showDialog = false })
            }

        }



    }

}
fun checkUsernameAvailability(username: String, onResult: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("userName", username)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // Username is available
                onResult(true)
            } else {
                // Username already taken
                onResult(false)
            }
        }
        .addOnFailureListener {
            // Handle error (optional)
            onResult(false)
        }
}
@Composable
fun RuleItem(text: String, isValid: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isValid) Color(0xFF4CAF50) else Color(0xFFF44336) // ✅ green / ❌ red
        )
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 13.sp)
    }
}
fun createAccount(
    user: User,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    // Check username uniqueness first
    db.collection("users").whereEqualTo("userName", user.userName).get()
        .addOnSuccessListener { usernameDocs ->
            if (!usernameDocs.isEmpty) {
                onFailure(Exception("Username already taken"))
                return@addOnSuccessListener
            }

            val currentUser = Firebase.auth.currentUser
            val credential = EmailAuthProvider.getCredential(user.email, user.password)

            // 🔗 Link Email/Password with Google user
            currentUser?.linkWithCredential(credential)
                ?.addOnCompleteListener { linkTask ->
                    if (linkTask.isSuccessful) {
                        val uid = linkTask.result?.user?.uid ?: return@addOnCompleteListener

                        // ✅ Save user details under the same UID
                        val userRef = db.collection("users").document(uid)
                        userRef.set(user)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { e -> onFailure(e) }
                    } else {
                        onFailure(linkTask.exception ?: Exception("Linking failed"))
                    }
                }
        }
        .addOnFailureListener { e -> onFailure(e) }
}


@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}
@Preview
@Composable
private fun detailpagepreview() {
    DetailPage(rememberNavController())

}