
package com.example.smartattendancesystem.ui.theme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartattendancesystem.navigation.Screen
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*


data class Student(
    val name: String,
    var isPresent: Boolean = false
)
@Composable
fun TakeAttendanceScreen() {

    val students = remember {
        mutableStateListOf(
            Student("Rahul"),
            Student("Ananya"),
            Student("Vikram"),
            Student("Sneha"),
            Student("Arjun")
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Mark Attendance",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(students) { student ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = student.name,
                            fontSize = 18.sp
                        )

                        Checkbox(
                            checked = student.isPresent,
                            onCheckedChange = { isChecked ->
                                val index = students.indexOf(student)
                                students[index] = student.copy(isPresent = isChecked)
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val presentStudents = students
                    .filter { it.isPresent }
                    .map { it.name }

                val currentDate = java.text.SimpleDateFormat("dd-MM-yyyy")
                    .format(java.util.Date())

                val existingIndex = AttendanceData.records.indexOfFirst {
                    it.date == currentDate
                }

                if (existingIndex != -1) {

                    val oldList = AttendanceData.records[existingIndex].students

                    //  CORRECT MERGE
                    val updatedList = (oldList + presentStudents).distinct()

                    AttendanceData.records[existingIndex] =
                        AttendanceRecord(currentDate, updatedList)

                } else {
                    AttendanceData.records.add(
                        AttendanceRecord(currentDate, presentStudents)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Attendance")
        }
    }
}

private fun MutableList<AttendanceRecord>.add(element: AttendanceRecord) {}

@Composable
fun ViewRecordsScreen() {

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Attendance Records",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (AttendanceData.records.isEmpty()) {
            Text("No attendance recorded")
        } else {
            AttendanceData.records.forEach { record ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text("Date: ${record.date}")

                        Spacer(modifier = Modifier.height(6.dp))

                        record.students.forEach {
                            Text("• $it")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Text("Profile Screen")
}

@Composable
fun DashboardScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Smart Attendance System",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("camera/Rahul")
        }) {
            Text("Take Attendance (Camera)")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate(Screen.ViewRecords.route)
        }) {
            Text("View Records")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate(Screen.Profile.route)
        }) {
            Text("Profile")
        }
    }
}