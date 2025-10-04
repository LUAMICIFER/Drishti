package com.example.drishtimukesh

enum class ClassType {
    CLASS_9,CLASS_10, CLASS_11, CLASS_12
}

data class User(
    val userName: String? = null,       // User_Name
    val name: String? = null,           // Name
    val deviceId: String = "",          // device_id
    val email: String = "",             // Email
    val phone: String = "",             // Phone
    val userClass: String = "",         // Class (or Int if you want numeric class levels)
    val coins: Int = 0,                 // Coins
    val password: String = "",          // passwords
    val listOfCourses: List<Int> = emptyList(), // Array of course IDs
    val role : String = "Student"
)

data class Course(
    val id: String = "", // Used locally for document ID
    val name: String = "",
    val clas: String = "",
    val courseId: String = "",
    val description: String = "",
    val price: Long = 0,
    val baseImage: List<String> = emptyList(), // Changed to List<String> as requested
    val startDate: Long = 0,

    val endDate: Long = 0
)
data class Subject(
//    @JvmField
    val id: String = "",
//    @JvmField
    val name: String = "",
    val subjectID: String = "" // Retaining 'SubjectID' for consistency with images
)
data class Chapter(
//    @JvmField
    val id: String = "",
//    @JvmField
    val name: String = ""
    // driveFolderId removed
)
data class Lecture(
    val id: String = "",
    val name: String = "",
    val videoUrl: String = "",
    val pdfLink: String = ""
)
//package com.example.drishtimukesh
//
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.tasks.await
//import android.util.Log
//import com.google.firebase.firestore.CollectionReference
//import com.google.firebase.firestore.DocumentReference
//
//// --- Data Classes for Firestore Mapping ---
//
///**
// * Represents a document in the 'courses' collection.
// * The 'baseImage' is now a list of image URLs.
// * 'driveFolderId' has been removed from this level.
// */
//data class Course(
//    @JvmField
//    val id: String = "", // Used locally for document ID
//
//    @JvmField
//    val name: String = "",
//
//    @JvmField
//    val clas: String = "",
//
//    @JvmField
//    val courseId: String = "",
//
//    @JvmField
//    val description: String = "",
//
//    @JvmField
//    val price: Long = 0,
//
//    @JvmField
//    val baseImage: List<String> = emptyList(), // Changed to List<String> as requested
//
//    @JvmField
//    val startDate: Long = 0,
//
//    @JvmField
//    val endDate: Long = 0
//    // driveFolderId removed
//)
//
///**
// * Represents a document in the 'subjects' subcollection.
// */
//data class Subject(
//    @JvmField
//    val id: String = "",
//    @JvmField
//    val name: String = "",
//    @JvmField
//    val subjectID: String = "" // Retaining 'SubjectID' for consistency with images
//    // driveFolderId removed
//)
//
///**
// * Represents a document in the 'chapters' subcollection.
// */
//data class Chapter(
//    @JvmField
//    val id: String = "",
//    @JvmField
//    val name: String = ""
//    // driveFolderId removed
//)
//
///**
// * Represents a document in the 'lectures' subcollection.
// * 'driveFolderId' is now removed from this class as requested.
// */
//data class Lecture(
//    @JvmField
//    val id: String = "",
//    @JvmField
//    val name: String = "",
//    @JvmField
//    val videoUrl: String = ""
//    // driveFolderId removed
//)
//
//
//// --- Data Access Functions (From original request) ---
//
//suspend fun getAllCourses(): List<Course> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses").get().await()
//        snapshot.map { doc ->
//            doc.toObject(Course::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error fetching all courses: ${e.message}", e)
//        emptyList()
//    }
//}
//
//suspend fun getSubjectsByCourseId(courseId: String): List<Subject> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .document(courseId)
//            .collection("subjects")
//            .get()
//            .await()
//        snapshot.map { doc ->
//            doc.toObject(Subject::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error fetching subjects for course $courseId: ${e.message}", e)
//        emptyList()
//    }
//}
//
//suspend fun getCoursesByClass(clasValue: String): List<Course> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .whereEqualTo("clas", clasValue)
//            .get()
//            .await()
//        snapshot.map { doc ->
//            doc.toObject(Course::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error fetching courses by class $clasValue: ${e.message}", e)
//        emptyList()
//    }
//}
//
//suspend fun getCoursesByName(courseName: String): List<Course> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .whereEqualTo("name", courseName)
//            .get()
//            .await()
//        snapshot.map { doc ->
//            doc.toObject(Course::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error fetching courses by name $courseName: ${e.message}", e)
//        emptyList()
//    }
//}
//
//suspend fun getCourseById(courseId: String): Course? {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .document(courseId)
//            .get()
//            .await()
//        if (snapshot.exists()) {
//            snapshot.toObject(Course::class.java)?.copy(id = snapshot.id)
//        } else {
//            null
//        }
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error fetching course by ID $courseId: ${e.message}", e)
//        null
//    }
//}
//
//suspend fun getChaptersBySubjectId(courseId: String, subjectId: String): List<Chapter> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .document(courseId)
//            .collection("subjects") // Using lowercase 'subjects' for consistency
//            .document(subjectId)
//            .collection("chapters")
//            .get()
//            .await()
//
//        snapshot.map { doc ->
//            doc.toObject(Chapter::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error fetching chapters for $courseId/$subjectId: ${e.message}", e)
//        emptyList()
//    }
//}
//
//suspend fun getLecturesByChapterId(
//    courseId: String,
//    subjectId: String,
//    chapterId: String
//): List<Lecture> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .document(courseId)
//            .collection("subjects")
//            .document(subjectId)
//            .collection("chapters")
//            .document(chapterId)
//            .collection("lectures")
//            .get()
//            .await()
//        snapshot.map { doc ->
//            doc.toObject(Lecture::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error fetching lectures for $courseId/$subjectId/$chapterId: ${e.message}", e)
//        emptyList()
//    }
//}
//
//
//// --- Function to Add Full Sample Course Hierarchy ---
//
///**
// * Adds a complete sample Course hierarchy (Course -> Subject -> Chapter -> Lecture).
// * This function uses sequential writes, which is fine for setup but less performant than batches
// * for massive updates.
// *
// * @param clasValue The class (e.g., "Class_10") to assign to the new course.
// * @return The ID of the newly created course document, or null on failure.
// */
//suspend fun addFullCourseHierarchy(clasValue: String = "Class_10"): String? {
//    val db = FirebaseFirestore.getInstance()
//
//    // --- 1. Define Sample Data ---
//    val now = System.currentTimeMillis()
//    val future = now + (90L * 24 * 60 * 60 * 1000) // 90 days later
//
//    val sampleCourse = Course(
//        name = "Complete $clasValue JEE Prep",
//        clas = clasValue,
//        courseId = clasValue.toLowerCase() + "_jee_prep",
//        description = "A comprehensive preparation course for $clasValue students.",
//        price = 4999,
//        baseImage = listOf(
//            "https://placehold.co/800x450/333A73/FFFFFF?text=$clasValue+Course",
//            "https://placehold.co/400x225/A50104/FFFFFF?text=Mock+Test"
//        ),
//        startDate = now,
//        endDate = future,
//    )
//
//    // --- 2. Add Course Document ---
//    val courseDocRef: DocumentReference
//    try {
//        courseDocRef = db.collection("courses")
//            .add(sampleCourse) // Firestore generates the unique ID here
//            .await()
//        Log.d("Firestore", "Course added successfully with ID: ${courseDocRef.id}")
//    } catch (e: Exception) {
//        Log.e("Firestore", "Error adding course: ${e.message}", e)
//        return null
//    }
//
//    // --- 3. Add Subjects Subcollection ---
//    val subjectsRef: CollectionReference = courseDocRef.collection("subjects")
//    val subjectData = listOf(
//        Subject(name = "Physics", subjectID = "physics"),
//        Subject(name = "Chemistry", subjectID = "chemistry")
//    )
//
//    for (subject in subjectData) {
//        val subjectDocRef: DocumentReference
//        try {
//            subjectDocRef = subjectsRef.add(subject).await()
//            Log.d("Firestore", "Subject ${subject.name} added with ID: ${subjectDocRef.id}")
//        } catch (e: Exception) {
//            Log.e("Firestore", "Error adding subject ${subject.name}: ${e.message}", e)
//            continue
//        }
//
//        // --- 4. Add Chapters Subcollection ---
//        val chaptersRef: CollectionReference = subjectDocRef.collection("chapters")
//        val chapterData = when (subject.subjectID) {
//            "physics" -> listOf(
//                Chapter(name = "Kinematics"),
//                Chapter(name = "Newton's Laws")
//            )
//            "chemistry" -> listOf(
//                Chapter(name = "Stoichiometry"),
//                Chapter(name = "Chemical Bonding")
//            )
//            else -> emptyList()
//        }
//
//        for (chapter in chapterData) {
//            val chapterDocRef: DocumentReference
//            try {
//                chapterDocRef = chaptersRef.add(chapter).await()
//                Log.d("Firestore", "Chapter ${chapter.name} added with ID: ${chapterDocRef.id}")
//            } catch (e: Exception) {
//                Log.e("Firestore", "Error adding chapter ${chapter.name}: ${e.message}", e)
//                continue
//            }
//
//            // --- 5. Add Lectures Subcollection (driveFolderId removed) ---
//            val lecturesRef: CollectionReference = chapterDocRef.collection("lectures")
//            val lectureData = listOf(
//                Lecture(
//                    name = "Lecture 1: Introduction",
//                    videoUrl = "https://youtube.com/watch/l1-intro"
//                ),
//                Lecture(
//                    name = "Lecture 2: Numericals",
//                    videoUrl = "https://youtube.com/watch/l2-nums"
//                )
//            )
//
//            for (lecture in lectureData) {
//                try {
//                    lecturesRef.add(lecture).await()
//                    Log.d("Firestore", "Lecture ${lecture.name} added.")
//                } catch (e: Exception) {
//                    Log.e("Firestore", "Error adding lecture ${lecture.name}: ${e.message}", e)
//                }
//            }
//        }
//    }
//
//    return courseDocRef.id
//}
