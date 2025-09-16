package com.example.drishtimukesh.screen
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.example.drishtimukesh.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.jvm.java

@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
}

/**
 * Checks deviceId in Firestore for the currently signed-in Firebase user.
 * - if stored deviceId is empty -> updates it with current deviceId and calls onSuccess(user)
 * - if stored deviceId == current -> calls onSuccess(user)
 * - if mismatch -> signs out and calls onMismatch()
 *
 * Note: document id is assumed to be uid (recommended).
 */
fun checkDeviceIdAndLogin(
    context: Context,
    onSuccess: (User) -> Unit,
    onMismatch: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val firebaseUser = auth.currentUser

    if (firebaseUser == null) {
        onFailure(Exception("No Firebase user signed in"))
        return
    }

    val uid = firebaseUser.uid
    val deviceId = getDeviceId(context)
    val userRef = db.collection("users").document(uid)

    userRef.get()
        .addOnSuccessListener { doc ->
            if (!doc.exists()) {
                onFailure(Exception("User not found in Firestore"))
                return@addOnSuccessListener
            }

            val user = doc.toObject(User::class.java) ?: run {
                onFailure(Exception("Failed to parse user"))
                return@addOnSuccessListener
            }

            val storedDeviceId = user.deviceId

            when {
                storedDeviceId.isEmpty() -> {
                    // first-time login on this account: bind device
                    userRef.update("deviceId", deviceId)
                        .addOnSuccessListener {
                            onSuccess(user.copy(deviceId = deviceId))
                        }
                        .addOnFailureListener { e -> onFailure(e) }
                }
                storedDeviceId == deviceId -> {
                    // allowed
                    onSuccess(user)
                }
                else -> {
                    // mismatch -> sign out and report
                    FirebaseAuth.getInstance().signOut()
                    onMismatch()
                }
            }
        }
        .addOnFailureListener { e -> onFailure(e) }
}
