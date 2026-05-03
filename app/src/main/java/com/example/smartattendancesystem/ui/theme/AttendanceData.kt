package com.example.smartattendancesystem.ui.theme

data class AttendanceRecord(
    val date: String,
    val students: List<String>
)

object AttendanceData {
    val records = mutableListOf<AttendanceRecord>()
}
