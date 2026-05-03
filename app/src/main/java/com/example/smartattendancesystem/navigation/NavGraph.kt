package com.example.smartattendancesystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.smartattendancesystem.ui.*
import com.example.smartattendancesystem.ui.theme.DashboardScreen
import com.example.smartattendancesystem.ui.theme.ProfileScreen
import com.example.smartattendancesystem.ui.theme.TakeAttendanceScreen
import com.example.smartattendancesystem.ui.theme.ViewRecordsScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(Screen.TakeAttendance.route) {
            TakeAttendanceScreen()
        }

        composable(Screen.ViewRecords.route) {
            ViewRecordsScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }

        composable("camera/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            com.example.smartattendancesystem.ai.CameraScreen(name)
        }
    }
}