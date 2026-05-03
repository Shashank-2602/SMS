package com.example.smartattendancesystem

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartattendancesystem.ui.theme.SmartAttendanceSystemTheme
import androidx.navigation.compose.rememberNavController
import com.example.smartattendancesystem.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartAttendanceSystemTheme {

                val navController = rememberNavController()
                NavGraph(navController)

            }
        }

            }

    }


@Composable
fun MainScreen() {
    Column(modifier = Modifier.padding(20.dp)) {

        Text(
            text = "Smart Attendance System",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { }) {
            Text("Register Student")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { }) {
            Text("Start Attendance")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { }) {
            Text("View Dashboard")
        }
    }
}



