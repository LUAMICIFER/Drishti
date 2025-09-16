package com.example.drishtimukesh.screen

import com.example.drishtimukesh.R

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Courses : Screen("courses")
    object Dashboard : Screen("dashboard")
    object Profile : Screen("profile")
}
sealed class BottomNavItem(val route: String, val title: String, val iconRes: Int) {
    object Home : BottomNavItem(Screen.Home.route, "Home", R.drawable.home)
    object Courses : BottomNavItem(Screen.Courses.route, "Courses", R.drawable.category)
    object Dashboard : BottomNavItem(Screen.Dashboard.route, "Dashboard", R.drawable.graph)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", R.drawable.profile)

    companion object {
        val items = listOf(Home, Courses, Dashboard, Profile)
    }
}
