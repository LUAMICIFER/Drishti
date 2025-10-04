package com.example.drishtimukesh

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

//suspend fun getAllCourses(): List<Course> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses").get().await()
//        snapshot.map { doc ->
//            doc.toObject(Course::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        emptyList()
//    }
//}
//// jo khud ke refference ke liye banaye hai n wahi wala use hoga yahan pr
//
//suspend fun getSubjectsByCourseId(courseId: String): List<Subject> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .document(courseId)
//            .collection("subjects")
//            .get()
//            .await()   // requires kotlinx-coroutines-play-services
//        snapshot.map { doc ->
//            doc.toObject(Subject::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
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
//            .await()  // requires kotlinx-coroutines-play-services
//        snapshot.map { doc ->
//            doc.toObject(Course::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//
//        emptyList()
//    }
//}
//
////aaise call kar sakte hai
////lifecycleScope.launch {
////    val courses = getCoursesByClass("Class_10")
////    courses.forEach {
////        Log.d("Firestore", it.name)
////    }
////}
////
//suspend fun getCoursesByName(courseName: String): List<Course> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .whereEqualTo("name", courseName)   // filter by course name
//            .get()
//            .await()  // requires kotlinx-coroutines-play-services
//        snapshot.map { doc ->
//            doc.toObject(Course::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        emptyList()
//    }
//}
//suspend fun getCourseById(courseId: String): Course? {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .document(courseId)
//            .get()
//            .await()   // needs kotlinx-coroutines-play-services
//        if (snapshot.exists()) {
//            snapshot.toObject(Course::class.java)?.copy(id = snapshot.id)
//        } else {
//            null
//        }
//    } catch (e: Exception) {
//        null
//    }
//}
//suspend fun getChaptersBySubjectId(courseId: String, subjectId: String): List<Chapter> {
//    val db = FirebaseFirestore.getInstance()
//    return try {
//        val snapshot = db.collection("courses")
//            .document(courseId)
//            .collection("subjects")
//            .document(subjectId)
//            .collection("chapters")
//            .get()
//            .await()
//
//        snapshot.map { doc ->
//            doc.toObject(Chapter::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        emptyList()
//    }
//}
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
//            .await()   // requires kotlinx-coroutines-play-services
//        snapshot.map { doc ->
//            doc.toObject(Lecture::class.java).copy(id = doc.id)
//        }
//    } catch (e: Exception) {
//        emptyList()
//    }
//}
//
//
//
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
//        price = 4999.0,
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
//            // --- 5. Add Lectures Subcollection (Contains the driveFolderId) ---
//            val lecturesRef: CollectionReference = chapterDocRef.collection("lectures")
//            val lectureData = listOf(
//                Lecture(
//                    name = "Lecture 1: Introduction",
//                    videoUrl = "https://youtube.com/watch/l1-intro",
//                    driveFolderId = "drive-f1-${chapter.id}" // Unique ID per lecture/chapter
//                ),
//                Lecture(
//                    name = "Lecture 2: Numericals",
//                    videoUrl = "https://youtube.com/watch/l2-nums",
//                    driveFolderId = "drive-f2-${chapter.id}"
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


// --- Function to Add Full Sample Course Hierarchy ---

/**
 * Adds a complete sample Course hierarchy (Course -> Subject -> Chapter -> Lecture).
 * This function uses sequential writes, which is fine for setup but less performant than batches
 * for massive updates.
 *
 * @param clasValue The class (e.g., "Class_10") to assign to the new course.
 * @return The ID of the newly created course document, or null on failure.
 */
//suspend fun addFullCourseHierarchy(clasValue: String = "Class_10"): String? {
//    val db = FirebaseFirestore.getInstance()
//
//    // --- 1. Define Sample Data ---
//    val now = System.currentTimeMillis()
//    val future = now + (90L * 24 * 60 * 60 * 1000) // 90 days later
//
//    val sampleCourse = Course(
//        name = "Complete $clasValue Prep",
//        clas = clasValue,
//        courseId = clasValue.toLowerCase()+"full_length",
//        description = "A comprehensive preparation course for $clasValue students.",
//        price = 4999,
//        baseImage = listOf(
//            "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?auto=format&fit=crop&w=800&q=80",
//            "https://images.unsplash.com/photo-1581090700227-1e37b190418e?auto=format&fit=crop&w=800&q=80"
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