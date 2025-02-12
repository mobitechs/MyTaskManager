package com.mobitechs.mytaskmanager.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobitechs.mytaskmanager.ForgotPasswordScreen
import com.mobitechs.mytaskmanager.screen.HomeScreen
import com.mobitechs.mytaskmanager.screen.SplashScreen
import com.mobitechs.mytaskmanager.screen.task.TaskAddScreen
import com.mobitechs.mytaskmanager.screen.task.TaskListScreen
import com.mobitechs.mytaskmanager.screen.taskAssignedMe.TaskAssignedMeScreen
import com.mobitechs.mytaskmanager.screen.user.LoginScreen
import com.mobitechs.mytaskmanager.screen.user.RegisterScreen
import com.mobitechs.mytaskmanager.screen.user.SetPasswordScreen
import com.mobitechs.mytaskmanager.team.TeamAddScreen
import com.mobitechs.mytaskmanager.team.TeamDetailsScreen
import com.mobitechs.mytaskmanager.team.TeamListScreen
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@Composable
fun AppNavigation(viewModel: ViewModelUser) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splashScreen") {
        composable("splashScreen") { SplashScreen(navController) }
        composable("taskListScreen") { TaskListScreen(navController) }
        composable("taskAssignedMeScreen") { TaskAssignedMeScreen(navController) }
        composable("loginScreen") { LoginScreen(navController, viewModel) }
        composable("registerScreen") { RegisterScreen(navController, viewModel) }
        composable("forgotPasswordScreen") { ForgotPasswordScreen(navController, viewModel) }
        composable("setPasswordScreen/{email}/{otp}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            SetPasswordScreen(navController, viewModel, email,otp)
        }
        composable("homeScreen") { HomeScreen(navController) }
        composable("teamLisScreen") { TeamListScreen(navController) }

        composable(
            "teamAddScreen/{teamJson}",
            arguments = listOf(navArgument("teamJson") {
                type = NavType.StringType
                defaultValue = "null" // Handle null case
            })
        ) { backStackEntry ->
            val teamJson = backStackEntry.arguments?.getString("teamJson")
            TeamAddScreen(navController, teamJson)
        }


        composable(
            "teamDetailsScreen/{teamJson}",
            arguments = listOf(navArgument("teamJson") {
                type = NavType.StringType
                defaultValue = "null" // Handle null case
            })
        ) { backStackEntry ->
            val teamJson = backStackEntry.arguments?.getString("teamJson")
            TeamDetailsScreen(navController, teamJson)
        }



        composable(
            "taskAddScreen/{taskJson}",
            arguments = listOf(navArgument("taskJson") {
                type = NavType.StringType
                defaultValue = "null" // Handle null case
            })
        ) { backStackEntry ->
            val teamJson = backStackEntry.arguments?.getString("taskJson")
            TaskAddScreen(navController, teamJson)
        }



    }
}