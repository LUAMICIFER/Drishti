package com.example.drishtimukesh.screen

import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drishtimukesh.R

// --- Placeholder for your background and colors ---
// NOTE: I'm using a simple white background with a subtle gray border for the form area
// since I can't access your R.drawable.lightmode. You can replace the BoxWithConstraints
// background and the Image composable with your actual background implementation.
val DarkBackground = Color(0xFF1E1E1E) // Dark block color from image
val PrimaryText = Color.White
val SecondaryText = Color(0xFFAAAAAA)

@Composable
fun ContactUsScreen() {
    // State for form fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("Doe") } // Default 'Doe' as in image
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+1 012 3456 789") } // Default as in image
    var subject by remember { mutableStateOf("General Inquiry") }
    var message by remember { mutableStateOf("") }

    val subjects = listOf("General Inquiry", "Support", "Billing", "Partnerships")

    BoxWithConstraints(
        modifier = Modifier// Background color of the whole screen
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val padding = if (screenWidth < 600.dp) 16.dp else 32.dp
        val titleFontSize = if (screenWidth < 600.dp) 32.sp else 43.6.sp

        // --- YOUR BACKGROUND IMAGE/PAINTER LOGIC GOES HERE ---
         Image(
             painter = painterResource(id = R.drawable.lightmode), // Your background image
             contentDescription = "Background",
             contentScale = ContentScale.Crop,
             modifier = Modifier.matchParentSize()
         )
        // For the sake of a runnable example, I'm just using the Box background color.

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
                    color = Color.Black,
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
                        .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 0.dp)) // Adjust shape if needed
                        .background(DarkBackground)
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
                        label = "",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    ContactInfoItem(
                        icon = Icons.Default.Email,
                        text = "drishtiinstitute1920@gmail.com",
                        label = "",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    ContactInfoItem(
                        icon = Icons.Default.LocationOn,
                        text = "",
                        label = "",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Spacer(Modifier.height(40.dp))

                    // Social Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SocialIcon(iconResId = android.R.drawable.ic_menu_agenda) // Placeholder for Twitter/X
                        SocialIcon(iconResId = android.R.drawable.ic_menu_camera) // Placeholder for Instagram
                        SocialIcon(iconResId = android.R.drawable.ic_dialog_map) // Placeholder for Discord
                    }
                }
            }

            item {
                // --- Contact Form Block (Light) ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // Form area is white
                        .padding(horizontal = padding, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- First Name Field ---
                    ContactInputField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = "First Name",
                        placeholder = "First Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // --- Last Name Field ---
                    ContactInputField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = "Last Name",
                        placeholder = "Doe",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // --- Email Field ---
                    ContactInputField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        placeholder = "Email",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // --- Phone Number Field ---
                    ContactInputField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        placeholder = "+1 012 3456 789",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    // --- Subject Selection ---
                    Text(
                        text = "Select Subject?",
                        fontWeight = FontWeight.Medium,
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
                        label = { Text("Write your message..") },
                        minLines = 5,
                        maxLines = 10,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = SecondaryText
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .heightIn(min = 100.dp)
                    )

                    Spacer(Modifier.height(32.dp))

                    // --- Send Message Button ---
                    Button(
                        onClick = {
                            // TODO: Implement actual submission logic here
                            println("Form Submitted: $firstName $lastName, $email, $phone, $subject, $message")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Send Message", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --- Helper Composable for Contact Info Items ---
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
            tint = Color.White,
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

// --- Helper Composable for Social Icons ---
@Composable
fun SocialIcon(iconResId: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f)) // Subtle transparent background
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId), // Using placeholder resource
            contentDescription = null,
            tint = PrimaryText,
            modifier = Modifier.size(20.dp)
        )
    }
}

// --- Helper Composable for Text Fields ---
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
        Text(text = label, fontWeight = FontWeight.Medium)
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
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = SecondaryText
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// --- Helper Composable for Subject Radio Buttons ---
@Composable
fun SubjectSelectionRow(
    subjects: List<String>,
    selectedSubject: String,
    onSubjectSelected: (String) -> Unit
) {
    // The image shows a 2x2 grid of radio buttons.
    // We'll arrange them in two rows for better mobile responsiveness.
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
                selectedColor = Color.Black,
                unselectedColor = SecondaryText
            )
        )
        Text(
            text = label,
            color = if (isSelected) Color.Black else Color.DarkGray,
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