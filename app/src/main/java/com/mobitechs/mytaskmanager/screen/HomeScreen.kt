package com.mobitechs.mytaskmanager.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.components.BottomNavigationBar
import com.mobitechs.mytaskmanager.util.ShowToast
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {
    val context: Context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text("MyTaskManager") }, navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            })
        },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Profile", fontSize = 18.sp, modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate("profileScreen") })
                Text("Share", fontSize = 18.sp, modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate("shareScreen") })
                Text("Terms & Conditions", fontSize = 18.sp, modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate("termsScreen") })
                Text("Privacy Policy", fontSize = 18.sp, modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.navigate("privacyScreen") })
                Text("Version: 1.0.0", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    ShowToast(context, "Logged out successfully")
                    navController.navigate("loginScreen")
                }) {
                    Text("Logout")
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { navController.navigate("taskAddScreen/${Uri.encode("null")}") },
//                shape = CircleShape
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Add Task")
//            }
//        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Welcome to MyTaskManager!", fontSize = 20.sp, color = Color.Gray)
        }
    }
}
