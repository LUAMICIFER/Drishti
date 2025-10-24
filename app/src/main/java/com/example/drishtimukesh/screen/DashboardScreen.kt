package com.example.drishtimukesh.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drishtimukesh.R

// --- Consistent Theme Colors ---
val DarkBackground = Color(0xFF1E1E1E) // Dark block color from original
val PrimaryText = Color.White
val SecondaryText = Color(0xFFAAAAAA)
val AccentColor = Color(0xFFFFAD05) // Yellow/Orange Accent from CoursesScreen
val BlackText = Color(0xFF1B1126) // Dark text from CoursesScreen
val FormBackground = Color.White // Keeping the form background white/light as in the original design mock-up
val ErrorColor = Color(0xFFB00020) // Defined error color for validation

// --- Utility Functions for Validation ---

/**
 * Utility function for basic email validation.
 */
fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

/**
 * Utility function for basic phone number validation (checks for digits and typical length,
 * supports country codes like +91).
 */
fun isValidPhone(phone: String): Boolean {
    return phone.matches("^(\\+?\\d{1,3})?[\\s\\-]?(\\d{3})[\\s\\-]?(\\d{3})[\\s\\-]?(\\d{4})$".toRegex())
}

// --- Social Media Links and Icons (Placeholder setup) ---
data class SocialHandle(val id: Int, val url: String, val description: String)
// NOTE: Using temporary Android default icons for a runnable example.
val socialHandles = listOf(
    SocialHandle(android.R.drawable.ic_menu_agenda, "https://www.youtube.com/yourchannel", "YouTube"),
    SocialHandle(android.R.drawable.ic_menu_camera, "https://www.instagram.com/yourhandle", "Instagram"),
    SocialHandle(android.R.drawable.ic_dialog_email, "mailto:drishtiinstitute1920@gmail.com", "Gmail"),
    SocialHandle(android.R.drawable.ic_dialog_map, "https://discord.gg/yourserver", "Discord")
)

/**
 * Utility function to open a URL using an implicit Intent.
 */
fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        // Log the error or show a Toast if no app can handle the intent
        println("Could not open URL: $url. Error: ${e.localizedMessage}")
        Toast.makeText(context, "Cannot open link.", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ContactUsScreen() {
    val context = LocalContext.current // Get the current context for Intents

    // State for form fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("General Inquiry") }
    var message by remember { mutableStateOf("") }

    // State for validation errors
    var isEmailError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }

    val subjects = listOf("General Inquiry", "Support", "Billing", "Partnerships")

    // --- FUNCTIONALITY: Email Sender Logic ---
    fun sendEmailIntent() {
        // Run validation checks
        isEmailError = email.isNotBlank() && !isValidEmail(email)
        isPhoneError = phone.isNotBlank() && !isValidPhone(phone)

        // Check required fields (FirstName, Email, Subject, Message) and validation status
        if (firstName.isNotBlank() && email.isNotBlank() && subject.isNotBlank() && message.isNotBlank() && !isEmailError && !isPhoneError) {
            val recipientEmail = "drishtiinstitute1920@gmail.com" // IMPORTANT: Replace with the actual target email
            val subjectLine = "Contact Form Inquiry: $subject"
            val body = "Name: $firstName $lastName\n" +
                    "Email: $email\n" +
                    "Phone: ${if (phone.isNotBlank()) phone else "N/A"}\n\n" +
                    "Message:\n$message"

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                // Use mailto: for email intent and ensure recipient is set
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
                putExtra(Intent.EXTRA_SUBJECT, subjectLine)
                putExtra(Intent.EXTRA_TEXT, body)
            }

            try {
                // Start the activity to open the email app
                context.startActivity(
                    Intent.createChooser(emailIntent, "Send email using...")
                )
                // Optional: Clear fields after successful attempt to open client
                firstName = ""
                lastName = ""
                email = ""
                phone = ""
                subject = subjects.first() // Reset subject to default
                message = ""
            } catch (e: Exception) {
                // Handle case where no application can handle the intent
                Toast.makeText(context, "No email client found on device.", Toast.LENGTH_LONG).show()
            }
        } else {
            // Show toast for general required field errors
            Toast.makeText(context, "Please fill in all required fields and correct errors.", Toast.LENGTH_SHORT).show()

            // Re-trigger validation for blank fields (only if necessary for visual feedback)
            isEmailError = email.isBlank() || !isValidEmail(email)
            isPhoneError = phone.isNotBlank() && !isValidPhone(phone)
        }
    }
    // --- END FUNCTIONALITY ---


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val padding = if (screenWidth < 600.dp) 16.dp else 32.dp
        val titleFontSize = if (screenWidth < 600.dp) 32.sp else 43.6.sp

        // --- BACKGROUND IMAGE ---
        Image(
            painter = painterResource(id = R.drawable.lightmode), // Your background image
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(40.dp))

                // --- Global Header ---
                Text(
                    text = "Contact Us",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = BlackText, // Using a darker text color for contrast on the light background
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = padding)
                )
                Text(
                    text = "Any question or remarks?\nJust write us a message!",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                // --- Contact Information Block (Dark) ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
                        .background(DarkBackground) // Dark background color
                        .padding(vertical = 40.dp, horizontal = padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Contact Information",
                        color = PrimaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Say something to start a live chat!",
                        color = SecondaryText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    // Contact Details
                    ContactInfoItem(
                        icon = Icons.Default.Phone,
                        text = "+91 9288071920",
                        label = "Phone",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    ContactInfoItem(
                        icon = Icons.Default.Email,
                        text = "drishtiinstitute1920@gmail.com",
                        label = "Email",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    ContactInfoItem(
                        icon = Icons.Default.LocationOn,
                        text = "Naya bajar,bihta",
                        label = "Location",
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Spacer(Modifier.height(40.dp))

                    // Social Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        socialHandles.forEach { handle ->
                            SocialButton(
                                iconResId = handle.id,
                                contentDescription = handle.description,
                                onClick = { openUrl(context, handle.url) }
                            )
                        }
                    }
                }
            }

            item {
                // --- Contact Form Block (Light) ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FormBackground) // Form area is white
                        .padding(horizontal = padding, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- Name Fields ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ContactInputField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = "First Name *",
                            placeholder = "Mukesh",
                            modifier = Modifier.weight(1f)
                        )
                        ContactInputField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = "Last Name",
                            placeholder = "Kumar",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // --- Email Field (Now with Validation and Error Display) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Email *", fontWeight = FontWeight.Medium, color = BlackText)
                            Spacer(Modifier.height(4.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    isEmailError = email.isNotBlank() && !isValidEmail(it)
                                },
                                placeholder = { Text("Email", color = SecondaryText) },
                                singleLine = true,
                                isError = isEmailError,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                supportingText = {
                                    if (isEmailError) Text("Invalid email format.", color = ErrorColor)
                                },
                                trailingIcon = {
                                    if (isEmailError) Icon(Icons.Filled.Warning, "Error", tint = ErrorColor)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = AccentColor,
                                    unfocusedBorderColor = if (isEmailError) ErrorColor else SecondaryText,
                                    errorBorderColor = ErrorColor,
                                    errorSupportingTextColor = ErrorColor
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // --- Phone Field (Now with Validation and Error Display) ---
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Phone Number", fontWeight = FontWeight.Medium, color = BlackText)
                            Spacer(Modifier.height(4.dp))
                            OutlinedTextField(
                                value = phone,
                                onValueChange = {
                                    phone = it
                                    isPhoneError = phone.isNotBlank() && !isValidPhone(it)
                                },
                                placeholder = { Text("+9198765...", color = SecondaryText) },
                                singleLine = true,
                                isError = isPhoneError,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                supportingText = {
                                    if (isPhoneError) Text("Invalid phone number.", color = ErrorColor)
                                },
                                trailingIcon = {
                                    if (isPhoneError) Icon(Icons.Filled.Warning, "Error", tint = ErrorColor)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = AccentColor,
                                    unfocusedBorderColor = if (isPhoneError) ErrorColor else SecondaryText,
                                    errorBorderColor = ErrorColor,
                                    errorSupportingTextColor = ErrorColor
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // --- Subject Selection ---
                    Text(
                        text = "Select Subject?",
                        fontWeight = FontWeight.Medium,
                        color = BlackText,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    SubjectSelectionRow(
                        subjects = subjects,
                        selectedSubject = subject,
                        onSubjectSelected = { subject = it }
                    )

                    Spacer(Modifier.height(24.dp))

                    // --- Message Field ---
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Write your message.. *") },
                        minLines = 5,
                        maxLines = 10,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = SecondaryText
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .heightIn(min = 100.dp)
                    )

                    Spacer(Modifier.height(32.dp))

                    // --- Send Message Button (Functional) ---
                    Button(
                        onClick = { sendEmailIntent() }, // Calls the function to trigger email client
                        colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Send Message", color = BlackText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --- Helper Composables (Kept original structure) ---

@Composable
fun ContactInfoItem(
    icon: ImageVector,
    text: String,
    label: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = AccentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            color = PrimaryText,
            fontSize = 16.sp,
            textAlign = textAlign
        )
    }
}

@Composable
fun SocialButton(iconResId: Int, contentDescription: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f))
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            tint = PrimaryText,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ContactInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier) {
        Text(text = label, fontWeight = FontWeight.Medium, color = BlackText)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = SecondaryText) },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = AccentColor,
                unfocusedBorderColor = SecondaryText
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SubjectSelectionRow(
    subjects: List<String>,
    selectedSubject: String,
    onSubjectSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SubjectRadioButton(
                label = subjects[0],
                isSelected = selectedSubject == subjects[0],
                onSelect = { onSubjectSelected(subjects[0]) },
                modifier = Modifier.weight(1f)
            )
            SubjectRadioButton(
                label = subjects[1],
                isSelected = selectedSubject == subjects[1],
                onSelect = { onSubjectSelected(subjects[1]) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SubjectRadioButton(
                label = subjects.getOrElse(2) { "" },
                isSelected = selectedSubject == subjects.getOrElse(2) { "" },
                onSelect = { onSubjectSelected(subjects.getOrElse(2) { "" }) },
                modifier = Modifier.weight(1f)
            )
            SubjectRadioButton(
                label = subjects.getOrElse(3) { "" },
                isSelected = selectedSubject == subjects.getOrElse(3) { "" },
                onSelect = { onSubjectSelected(subjects.getOrElse(3) { "" }) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SubjectRadioButton(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = AccentColor,
                unselectedColor = SecondaryText
            )
        )
        Text(
            text = label,
            color = if (isSelected) BlackText else Color.DarkGray,
            fontSize = 14.sp
        )
    }
}

// --- Preview (Optional) ---
@Preview(showBackground = true)
@Composable
fun PreviewContactUsScreen() {
    ContactUsScreen()
}