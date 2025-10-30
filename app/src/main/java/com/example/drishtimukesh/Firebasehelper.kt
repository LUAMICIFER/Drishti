package com.example.drishtimukesh

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

suspend fun getAllCourses(): List<Course> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses").get().await()
        snapshot.map { doc ->
            doc.toObject(Course::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching all courses: ${e.message}", e)
        emptyList()
    }
}

suspend fun getSubjectsByCourseId(courseId: String): List<Subject> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .document(courseId)
            .collection("subjects")
            .get()
            .await()
        snapshot.map { doc ->
            doc.toObject(Subject::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching subjects for course $courseId: ${e.message}", e)
        emptyList()
    }
}

suspend fun getCoursesByClass(clasValue: String): List<Course> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .whereEqualTo("clas", clasValue)
            .get()
            .await()
        snapshot.map { doc ->
            doc.toObject(Course::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching courses by class $clasValue: ${e.message}", e)
        emptyList()
    }
}

suspend fun getCoursesByName(courseName: String): List<Course> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .whereEqualTo("name", courseName)
            .get()
            .await()
        snapshot.map { doc ->
            doc.toObject(Course::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching courses by name $courseName: ${e.message}", e)
        emptyList()
    }
}

suspend fun getCourseById(courseId: String): Course? {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .document(courseId)
            .get()
            .await()
        if (snapshot.exists()) {
            snapshot.toObject(Course::class.java)?.copy(id = snapshot.id)
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching course by ID $courseId: ${e.message}", e)
        null
    }
}

suspend fun getChaptersBySubjectId(courseId: String, subjectId: String): List<Chapter> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .document(courseId)
            .collection("subjects") // Using lowercase 'subjects' for consistency
            .document(subjectId)
            .collection("chapters")
            .get()
            .await()

        snapshot.map { doc ->
            doc.toObject(Chapter::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching chapters for $courseId/$subjectId: ${e.message}", e)
        emptyList()
    }
}

suspend fun getLecturesByChapterId(
    courseId: String,
    subjectId: String,
    chapterId: String
): List<Lecture> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .document(courseId)
            .collection("subjects")
            .document(subjectId)
            .collection("chapters")
            .document(chapterId)
            .collection("lectures")
            .get()
            .await()
        snapshot.map { doc ->
            doc.toObject(Lecture::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching lectures for $courseId/$subjectId/$chapterId: ${e.message}", e)
        emptyList()
    }
}


suspend fun addFullCourseHierarchy(clasValue: String = "Class_10"): String? {
    val db = FirebaseFirestore.getInstance()

    // --- 1. Define Sample Data ---
    val now = System.currentTimeMillis()
    val future = now + (90L * 24 * 60 * 60 * 1000) // 90 days later

    val sampleCourse = Course(
        name = "Complete $clasValue Prep",
        clas = clasValue,
        courseId = clasValue.toLowerCase() + "full_prep",
        description = "A comprehensive preparation course for $clasValue students.",
        price = 4999,
        baseImage = listOf(
            "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1581090700227-1e37b190418e?auto=format&fit=crop&w=800&q=80"
        ),
        startDate = now,
        endDate = future,
    )

    // --- 2. Add Course Document ---
    val courseDocRef: DocumentReference
    try {
        courseDocRef = db.collection("courses")
            .add(sampleCourse) // Firestore generates the unique ID here
            .await()
        Log.d("Firestore", "Course added successfully with ID: ${courseDocRef.id}")
    } catch (e: Exception) {
        Log.e("Firestore", "Error adding course: ${e.message}", e)
        return null
    }

    // --- 3. Add Subjects Subcollection ---
    val subjectsRef: CollectionReference = courseDocRef.collection("subjects")
    val subjectData = listOf(
        Subject(name = "Physics", subjectID = "physics"),
        Subject(name = "Chemistry", subjectID = "chemistry")
    )

    for (subject in subjectData) {
        val subjectDocRef: DocumentReference
        try {
            subjectDocRef = subjectsRef.add(subject).await()
            Log.d("Firestore", "Subject ${subject.name} added with ID: ${subjectDocRef.id}")
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding subject ${subject.name}: ${e.message}", e)
            continue
        }

        // --- 4. Add Chapters Subcollection ---
        val chaptersRef: CollectionReference = subjectDocRef.collection("chapters")
        val chapterData = when (subject.subjectID) {
            "physics" -> listOf(
                Chapter(name = "Kinematics"),
                Chapter(name = "Newton's Laws")
            )
            "chemistry" -> listOf(
                Chapter(name = "Stoichiometry"),
                Chapter(name = "Chemical Bonding")
            )
            else -> emptyList()
        }

        for (chapter in chapterData) {
            val chapterDocRef: DocumentReference
            try {
                chapterDocRef = chaptersRef.add(chapter).await()
                Log.d("Firestore", "Chapter ${chapter.name} added with ID: ${chapterDocRef.id}")
            } catch (e: Exception) {
                Log.e("Firestore", "Error adding chapter ${chapter.name}: ${e.message}", e)
                continue
            }

            // --- 5. Add Lectures Subcollection (pdfLink added) ---
            val lecturesRef: CollectionReference = chapterDocRef.collection("lectures")
            val lectureData = listOf(
                Lecture(
                    name = "Lecture 1: Introduction",
                    videoUrl = "https://youtube.com/watch/l1-intro",
                    pdfLink = "https://example.com/notes_intro.pdf" // Sample PDF link
                ),
                Lecture(
                    name = "Lecture 2: Numericals",
                    videoUrl = "https://youtube.com/watch/l2-nums",
                    pdfLink = "https://example.com/worksheet_numericals.pdf" // Sample PDF link
                )
            )

            for (lecture in lectureData) {
                try {
                    lecturesRef.add(lecture).await()
                    Log.d("Firestore", "Lecture ${lecture.name} added.")
                } catch (e: Exception) {
                    Log.e("Firestore", "Error adding lecture ${lecture.name}: ${e.message}", e)
                }
            }
        }
    }

    return courseDocRef.id
}







// FirestoreUtils.kt


//suspend fun logTransactionToFirestore(
//    context: Context,
//    courseId: String,
//    subscriptionMonths: Int,
//    amountPaid: Double,
//    razorpayPaymentId: String,
//    paymentDetails: String
//) {
//    try {
//        val auth = Firebase.auth
//        val currentUser = auth.currentUser
//
//        if (currentUser == null) {
//            Log.e("Firestore", "User not authenticated")
//            return
//        }
//
//        val userId = currentUser.uid
//        val db = Firebase.firestore
//
//        // Calculate subscription dates
//        val calendar = Calendar.getInstance()
//        val subscriptionStartDate = calendar.time
//
//        calendar.add(Calendar.MONTH, subscriptionMonths)
//        val subscriptionEndDate = calendar.time
//
//        val transactionData = hashMapOf(
//            "userId" to userId,
//            "courseId" to courseId,
//            "subscriptionMonths" to subscriptionMonths,
//            "amountPaid" to amountPaid,
//            "razorpayPaymentId" to razorpayPaymentId,
//            "paymentDetails" to paymentDetails,
//            "subscriptionStartDate" to subscriptionStartDate,
//            "subscriptionEndDate" to subscriptionEndDate,
//            "status" to "active",
//            "timestamp" to FieldValue.serverTimestamp()
//        )
//
//        // Add transaction to transactions collection
//        val transactionResult = db.collection("transactions")
//            .add(transactionData)
//            .await()
//
//        Log.d("Firestore", "Transaction logged with ID: ${transactionResult.id}")
//
//        // Update user's subscription
//        updateUserSubscription(userId, courseId, subscriptionEndDate, transactionResult.id)
//
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error in logTransactionToFirestore", e)
//        throw e
//    }
//}

private suspend fun updateUserSubscription(
    userId: String,
    courseId: String,
    subscriptionEndDate: Date,
    transactionId: String
) {
    val db = Firebase.firestore

    val subscriptionData = hashMapOf(
        "subscriptionEndDate" to subscriptionEndDate,
        "transactionId" to transactionId,
        "isActive" to true,
        "lastUpdated" to FieldValue.serverTimestamp()
    )

    // Update user's subscriptions array
    db.collection("users").document(userId)
        .update(
            "subscriptions.$courseId", subscriptionData,
            "lastSubscriptionUpdate" to FieldValue.serverTimestamp()
        )
        .await()

    Log.d("Firestore", "User subscription updated successfully for course: $courseId")
}
private const val TAG = "FirebaseHelper"

/**
 * Logs the transaction to Firestore and updates user’s subscription.
 */
private suspend fun resolveUserDocId(): String {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser ?: throw IllegalStateException("No logged-in user")

    val uid = user.uid
    val email = user.email

    // 1) If document with UID exists, return uid
    val uidDocRef = db.collection("users").document(uid)
    val uidSnapshot = uidDocRef.get().await()
    if (uidSnapshot.exists()) {
        Log.d(TAG, "Found user doc by UID: $uid")
        return uid
    }

    // 2) If document with EMAIL exists, migrate/merge it into UID doc and return uid
    if (!email.isNullOrBlank()) {
        val emailDocRef = db.collection("users").document(email)
        val emailSnapshot = emailDocRef.get().await()
        if (emailSnapshot.exists()) {
            val existingData = emailSnapshot.data ?: emptyMap<String, Any>()
            // Merge existing email-doc data into uid-doc
            uidDocRef.set(existingData, SetOptions.merge()).await()

            // Also keep email as a field in uid doc (useful for lookups)
            uidDocRef.set(mapOf("email" to email), SetOptions.merge()).await()

            Log.d(TAG, "Migrated user doc from email ($email) to uid ($uid) and merged data.")
            return uid
        }
    }

    // 3) If neither exists, we'll use uid (new user doc). Also add email field if available.
    val initData = if (!email.isNullOrBlank()) mapOf("email" to email) else emptyMap()
    if (initData.isNotEmpty()) {
        uidDocRef.set(initData, SetOptions.merge()).await()
    }
    Log.d(TAG, "No existing user doc found; using uid ($uid) and creating doc if needed.")
    return uid
}

/**
 * Logs the transaction to Firestore and then updates user's subscription.
 * Uses resolved user doc id (uid preferred) to avoid creating duplicate docs.
 */

//transaction data in the firestore
suspend fun logTransactionToFirestore(
    context: Context,
    courseId: String,
    subscriptionMonths: Int,
    amountPaid: Double,
    razorpayPaymentId: String,
    paymentDetails: String
) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    if (user == null) {
        Log.e(TAG, "No logged-in user found for transaction")
        return
    }

    try {
        // Resolve the doc id we'll update (uid preferred; migrate from email if necessary)
        val resolvedUserDocId = try {
            resolveUserDocId()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to resolve user doc id", e)
            // fallback to uid if resolve fails for some reason
            user.uid
        }

        // Step 1: Log transaction record (transactions collection)
        val transactionData = hashMapOf(
            "userId" to resolvedUserDocId,
            "userEmail" to (user.email ?: ""),
            "courseId" to courseId,
            "subscriptionMonths" to subscriptionMonths,
            "amountPaid" to amountPaid,
            "razorpayPaymentId" to razorpayPaymentId,
            "paymentDetails" to paymentDetails,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("transactions")
            .add(transactionData)
            .await()

        Log.d(TAG, "Transaction logged for resolvedUserDocId=$resolvedUserDocId")

        // Step 2: Update user's subscription fields (safe merge)
        updateUserSubscription(resolvedUserDocId, courseId, subscriptionMonths)

    } catch (e: Exception) {
        Log.e(TAG, "Error logging transaction", e)
        throw e
    }
}

/**
 * Adds the purchased course to listOfCourses, increments coins, and sets per-course expiry.
 * Uses SetOptions.merge() to safely create or update the doc.
 */
suspend fun updateUserSubscription(userDocId: String, courseId: String, months: Int) {
    val db = FirebaseFirestore.getInstance()
    val userDocRef = db.collection("users").document(userDocId)

    try {
        // compute expiry millis for this course
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, months)
        val expiryMillis = calendar.timeInMillis

        // Prepare updates: add to array, increment coins, set subscriptionExpiry.<courseId>
        val updates = mapOf(
            "listOfCourses" to FieldValue.arrayUnion(courseId),
            "coins" to FieldValue.increment(10), // example bonus coins
            "subscriptionExpiry.$courseId" to expiryMillis
        )

        // Merge these updates into the user document
        userDocRef.set(updates, SetOptions.merge()).await()

        Log.d(TAG, "Successfully updated subscription for userDocId=$userDocId with courseId=$courseId")

    } catch (e: Exception) {
        Log.e(TAG, "Failed to update user subscription for userDocId=$userDocId", e)
        throw e
    }
}


//  subscription check function
suspend fun hasActiveSubscription(courseId: String): Boolean {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser ?: return false

    return try {
        val transactions = db.collection("transactions")
            .whereEqualTo("userId", user.uid)
            .whereEqualTo("courseId", courseId)
            .get()
            .await()
            .documents

        if (transactions.isEmpty()) {
            Log.d(TAG, "No transactions found for course $courseId")
            return false
        }

        val latestTransaction = transactions.maxByOrNull { it.getLong("timestamp") ?: 0L } ?: return false
        val months = latestTransaction.getLong("subscriptionMonths") ?: 0L
        val purchaseTime = latestTransaction.getLong("timestamp") ?: 0L
        val expiryTime = purchaseTime + months * 30L * 24 * 60 * 60 * 1000 // 30 days per month

        val isActive = System.currentTimeMillis() < expiryTime

        Log.d(TAG, "Subscription active: $isActive (expires: $expiryTime)")

        isActive
    } catch (e: Exception) {
        Log.e(TAG, "Error checking subscription", e)
        false
    }
}
//toppers
suspend fun getAllToppersFromFirebase(): List<Topper> {
    return try {
        val firestore = FirebaseFirestore.getInstance()
        val querySnapshot = firestore.collection("exam_results").get().await()

        val toppers = mutableListOf<Topper>()

        for (document in querySnapshot.documents) {
            try {
                val name = document.getString("name") ?: ""
                val exam = document.getString("exam_name") ?: ""
                val imageUrl = document.getString("image_url") ?: ""
                val year = document.getLong("year")?.toInt() ?: 0

                // Parse subjects array - fix potential field name mismatch
                val subjectsList = mutableListOf<YourSubject>()
                val subjects = document.get("subjects") as? List<Map<String, Any>> ?: emptyList()

                for (subjectData in subjects) {
                    // Try different possible field names
                    val subjectName = subjectData["subject_name"] as? String ?:
                    subjectData["subjectName"] as? String ?: ""

                    val marksSecured = (subjectData["marka_secured"] as? Long)?.toInt() ?:
                    (subjectData["marka_secured"] as? Double)?.toInt() ?:
                    (subjectData["marks"] as? Long)?.toInt() ?: 0

                    if (subjectName.isNotEmpty()) {
                        subjectsList.add(YourSubject(subjectName, marksSecured))
                    }
                }

                // Only add if we have valid data
                if (name.isNotEmpty() && exam.isNotEmpty() && subjectsList.isNotEmpty()) {
                    toppers.add(Topper(name, subjectsList, exam, year, imageUrl))
                }

            } catch (e: Exception) {
                Log.e("FirebaseHelper", "Error parsing document ${document.id}: ${e.message}")
            }
        }

        toppers
    } catch (e: Exception) {
        Log.e("FirebaseHelper", "Error fetching toppers: ${e.message}")
        emptyList()
    }
}