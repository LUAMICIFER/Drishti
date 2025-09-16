package com.example.drishtimukesh

import java.sql.Date

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
    val listOfUser : List<User> =emptyList(),
    val clas : String = "",
    val price : Int = 0,
    val startDate : Date ,
    val endDate : Date,
    val id : Int =0,
    val name : String,
    val description : String,
    val baseImage : String
)

