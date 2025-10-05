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
    val liveUrl: String = "",
    val endDate: Long = 0
)
data class Subject(
    val id: String = "",
    val name: String = "",
    val subjectID: String = "" // Retaining 'SubjectID' for consistency with images
)
data class Chapter(
    val id: String = "",
    val name: String = ""
)
data class Lecture(
    val id: String = "",
    val name: String = "",
    val videoUrl: String = "",
    val pdfLink: String = ""
)