package com.example.drishtimukesh.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun LegalDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Text(
                    text = "Close",
                    color = Color(0xFFFFC856),
                    modifier = Modifier.clickable { onDismiss() }
                )
            },
            title = { Text("Terms and Conditions & Privacy Policy") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // limit dialog height
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = """
📜 Terms and Conditions


Welcome to Drishti Institute App. By downloading, accessing, or using this App, you agree to be bound by these Terms and Conditions (“Terms”). Please read them carefully before using our services.

1. Acceptance of Terms

By using this App, you confirm that you:

Are at least 13 years old (or the minimum legal age in your country).

Have read, understood, and agreed to these Terms.

If under 18, you have parental/guardian consent to use the App.

2. Purpose of the App

This App is designed as an educational platform where students can:

Access video lectures.

Download or view study notes.

Attempt quizzes and practice questions.

The content is for educational purposes only and should not be treated as professional or official exam material unless explicitly stated.

3. User Responsibilities

When using this App, you agree to:

Use the App only for lawful and educational purposes.

Not share your login credentials with others.

Not attempt to hack, copy, or distribute App content without permission.

Respect intellectual property rights of our notes, lectures, and quizzes.

4. Intellectual Property

All lectures, notes, quizzes, and study materials provided in the App are owned by Drishti Institute or licensed to us.

You are granted a limited, non-transferable, non-commercial license to use the content for your personal learning only.

Any reproduction, resale, or distribution without prior written consent is strictly prohibited.

5. Payments & Subscriptions (if applicable)

Some content may be free, while premium content may require payment.

Payments are non-refundable unless required by law.

Subscriptions, if any, will auto-renew unless cancelled before the renewal date.

6. Disclaimer of Warranties

We strive to provide accurate and helpful educational content, but we do not guarantee that the information will always be complete, correct, or up-to-date.

The App is provided “as is” without warranties of any kind.

7. Limitation of Liability

We are not responsible for any loss, damage, or academic outcome resulting from the use of this App.

Use of the App is at your own risk.

8. Privacy

Your privacy is important to us. Please refer to our Privacy Policy (provided separately) to understand how we collect, use, and protect your data.

9. Termination of Use

We may suspend or terminate your account if you:

Violate these Terms.

Misuse the App in any unlawful or harmful way.

10. Changes to Terms

We may update these Terms from time to time. Continued use of the App after updates means you agree to the revised Terms.

11. Governing Law

These Terms shall be governed by and interpreted under the laws of [Your Country/State]. Any disputes shall be resolved in [Your City/Court Jurisdiction].

12. Contact Us

For any questions regarding these Terms, please contact us at:
📧 drishtiinstitute1920@gmail.com

📜 Privacy Policy

1. Information We Collect
- Name, email, login credentials
- Usage (lectures, quizzes, progress)
- Device info (OS, IP address)...

2. How We Use Your Info
- Provide lectures & notes
- Track progress
- Communicate updates
- Secure the app

3. Sharing Info
We do NOT sell your data. Shared only with service providers or if required by law.

4. Security
We use reasonable measures, but 100% security is not guaranteed.

5. Children’s Privacy
Under 13 cannot use. Under 18 requires parental consent.

6. Your Rights
Request access, correction, or deletion of your data.

7. Data Retention
We keep data while your account is active.

8. Third-Party Links
We’re not responsible for external sites.

9. Updates
We may update this Privacy Policy.

10. Contact
📧 drishtiinstitute1920@gmail.com
""".trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
    }
}
