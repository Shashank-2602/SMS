package com.example.smartattendancesystem.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object TakeAttendance : Screen("take_attendance")
    object ViewRecords : Screen("view_records")
    object Profile : Screen("profile")
}