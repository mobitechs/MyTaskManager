package com.mobitechs.mytaskmanager.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentDestination?.route

    BottomNavigation {
        BottomNavigationItem(icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
//            selected = false,
            selected = currentRoute == "homeScreen",
            onClick = { navController.navigate("homeScreen") })

        BottomNavigationItem(icon = {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Delegate Task"
            )
        },
            label = { Text("Delegate") },
//            selected = false,
            selected = currentRoute == "taskDelegateListScreen",
            onClick = { navController.navigate("taskDelegateListScreen") })

        BottomNavigationItem(icon = {
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = "For Me"
            )
        },
            label = { Text("For Me") },
//            selected = false,
            selected = currentRoute == "taskForMeScreen",
            onClick = { navController.navigate("taskForMeScreen") })

        BottomNavigationItem(icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "My Team"
            )
        },
            label = { Text("My Team") },
//            selected = false,
            selected = currentRoute == "teamLisScreen",
            onClick = { navController.navigate("teamLisScreen") })
    }
}