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
