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
import com.mobitechs.mytaskmanager.screen.taskDelegate.TaskAddScreen
import com.mobitechs.mytaskmanager.screen.taskDelegate.TaskDelegateListScreen
import com.mobitechs.mytaskmanager.screen.taskForMe.TaskForMeDetailsScreen
import com.mobitechs.mytaskmanager.screen.taskForMe.TaskForMeScreen
import com.mobitechs.mytaskmanager.screen.user.LoginScreen
import com.mobitechs.mytaskmanager.screen.user.RegisterScreen
import com.mobitechs.mytaskmanager.screen.user.SetPasswordScreen
import com.mobitechs.mytaskmanager.screen.team.TeamAddScreen
import com.mobitechs.mytaskmanager.screen.team.TeamListScreen
import com.mobitechs.mytaskmanager.screen.team.TeamMemberDetailsScreen
import com.mobitechs.mytaskmanager.screen.team.TeamWiseTaskDetails
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@Composable
fun AppNavigation(viewModel: ViewModelUser) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splashScreen") {
        composable("splashScreen") { SplashScreen(navController) }
        composable("taskDelegateListScreen") { TaskDelegateListScreen(navController) }
        composable("taskForMeScreen") { TaskForMeScreen(navController) }
        composable("loginScreen") { LoginScreen(navController, viewModel) }
        composable("registerScreen") { RegisterScreen(navController, viewModel) }
        composable("forgotPasswordScreen") { ForgotPasswordScreen(navController, viewModel) }
        composable("setPasswordScreen/{email}/{otp}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            SetPasswordScreen(navController, viewModel, email, otp)
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
            "teamMemberDetailsScreen/{teamJson}",
            arguments = listOf(navArgument("teamJson") {
                type = NavType.StringType
                defaultValue = "{}" // Use empty JSON instead of "null"
            })
        ) { backStackEntry ->
            val teamJson = backStackEntry.arguments?.getString("teamJson")
            TeamMemberDetailsScreen(navController, teamJson)
        }



        composable(
            "teamWiseTaskDetails/{teamJson}",
            arguments = listOf(navArgument("teamJson") {
                type = NavType.StringType
                defaultValue = "null" // Handle null case
            })
        ) { backStackEntry ->
            val teamJson = backStackEntry.arguments?.getString("teamJson")
            TeamWiseTaskDetails(navController, teamJson)
        }

        composable(
            "taskForMeDetailsScreen/{taskJson}",
            arguments = listOf(navArgument("taskJson") {
                type = NavType.StringType
                defaultValue = "{}" // Handle null case
            })
        ) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("taskJson")
            TaskForMeDetailsScreen(navController, taskJson)
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