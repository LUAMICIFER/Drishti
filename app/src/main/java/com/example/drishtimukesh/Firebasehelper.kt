package com.example.drishtimukesh

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun getAllCourses(): List<Course> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses").get().await()
        snapshot.map { doc ->
            doc.toObject(Course::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        emptyList()
    }
}
// jo khud ke refference ke liye banaye hai n wahi wala use hoga yahan pr

suspend fun getSubjectsByCourseId(courseId: String): List<Subject> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .document(courseId)
            .collection("subjects")
            .get()
            .await()   // requires kotlinx-coroutines-play-services
        snapshot.map { doc ->
            doc.toObject(Subject::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun getCoursesByClass(clasValue: String): List<Course> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .whereEqualTo("clas", clasValue)
            .get()
            .await()  // requires kotlinx-coroutines-play-services
        snapshot.map { doc ->
            doc.toObject(Course::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {

        emptyList()
    }
}

//aaise call kar sakte hai
//lifecycleScope.launch {
//    val courses = getCoursesByClass("Class_10")
//    courses.forEach {
//        Log.d("Firestore", it.name)
//    }
//}

suspend fun getCoursesByName(courseName: String): List<Course> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .whereEqualTo("name", courseName)   // filter by course name
            .get()
            .await()  // requires kotlinx-coroutines-play-services
        snapshot.map { doc ->
            doc.toObject(Course::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

/**
 * Fetches Chapters for a specific Subject within a Course.
 * * Assumes the path structure: courses/{courseId}/subjects/{subjectId}/chapters
 */
suspend fun getChaptersBySubjectId(courseId: String, subjectId: String): List<Chapter> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("courses")
            .document(courseId)
            .collection("subjects")
            .document(subjectId)
            .collection("chapters")
            .get()
            .await()
        snapshot.map { doc ->
            // Assuming Chapter data class exists and has fields for toObject mapping
            doc.toObject(Chapter::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        // Log the error for debugging
        println("Error fetching chapters for subject $subjectId: $e")
        emptyList()
    }
}

/**
 * Fetches Lectures for a specific Chapter within a Subject.
 * * Assumes the path structure: courses/{courseId}/subjects/{subjectId}/chapters/{chapterId}/lectures
 */
suspend fun getLecturesByChapterId(courseId: String, subjectId: String, chapterId: String): List<Lecture> {
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
            // Assuming Lecture data class exists and has fields for toObject mapping
            doc.toObject(Lecture::class.java).copy(id = doc.id)
        }
    } catch (e: Exception) {
        // Log the error for debugging
        println("Error fetching lectures for chapter $chapterId: $e")
        emptyList()
    }
}

/**
 * Checks if the current user is subscribed to a specific course.
 * * Assumes a subscription tracking path like: users/{userId}/subscriptions
 * where each subscription document contains a field pointing to the courseId.
 * For simplicity, we check a direct document path.
 */
suspend fun isUserSubscribedToCourse(userId: String, courseId: String): Boolean {
    val db = FirebaseFirestore.getInstance()
    // Path: artifacts/{appId}/users/{userId}/subscriptions/{courseId}
    val docPath = "artifacts/${
        if (typeof __app_id !== 'undefined') __app_id else "default-app-id"
    }/users/$userId/subscriptions"

    return try {
        val subscriptionRef = db.collection(docPath)
            .document(courseId)
            .get()
            .await()

        // Return true if the subscription document exists
        subscriptionRef.exists()
    } catch (e: Exception) {
        // Log the error but default to false for security/functionality
        println("Error checking subscription for user $userId and course $courseId: $e")
        false
    }
}
