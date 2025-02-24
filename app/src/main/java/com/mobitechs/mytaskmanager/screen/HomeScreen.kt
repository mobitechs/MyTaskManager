package com.mobitechs.mytaskmanager.screen

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.components.BottomNavigationBar
import com.mobitechs.mytaskmanager.components.setStatusColor
import com.mobitechs.mytaskmanager.model.KpiDetails
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.StatusDetails
import com.mobitechs.mytaskmanager.model.TaskStatusWiseCountDetails
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.util.setSessionKpiDetails
import com.mobitechs.mytaskmanager.util.setSessionStatusDetails
import com.mobitechs.mytaskmanager.viewModel.ViewModelHome
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {
    val context: Context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val viewModel: ViewModelHome = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshAPITrigger by remember { mutableStateOf(0) }


    var assignedByMeList by remember { mutableStateOf<List<TaskStatusWiseCountDetails>>(emptyList()) }
    var assignedToMeList by remember { mutableStateOf<List<TaskStatusWiseCountDetails>>(emptyList()) }
    var statusList by remember { mutableStateOf<List<StatusDetails>>(emptyList()) }
    var kpiList by remember { mutableStateOf<List<KpiDetails>>(emptyList()) }


    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()

    LaunchedEffect(refreshAPITrigger) {
        viewModel.getCountOfTaskListAssignedByMe(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    assignedByMeList = it.data ?: emptyList()
                } else {
                    errorMessage = it.message
                }
            }
        }
        viewModel.getCountOfTaskListAssignedToMe(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    assignedToMeList = it.data ?: emptyList()
                } else {
                    errorMessage = it.message
                }
            }
        }
        viewModel.getStatusList() { response ->
            response?.let {
                if (it.statusCode == 200) {
//                    statusList = it.data ?: emptyList()
                    setSessionStatusDetails(context, it.data)
                } else {
                    errorMessage = it.message
                }
            }
        }
        viewModel.getKpiList() { response ->
            response?.let {
                if (it.statusCode == 200) {
//                    kpiList = it.data ?: emptyList()
                    setSessionKpiDetails(context, it.data)
                } else {
                    errorMessage = it.message
                }
            }
        }
    }

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
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Welcome to MyTaskManager!", fontSize = 20.sp, color = Color.Gray)
//        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF6200EE))
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMessage!!, fontSize = 18.sp, color = Color.Red)
                    }
                }

                assignedByMeList.isEmpty() && assignedToMeList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tasks available", fontSize = 18.sp, color = Color.Gray)
                    }
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        item {
                            TaskBox("Tasks Assigned By Me", assignedByMeList, navController)
                            Spacer(modifier = Modifier.height(16.dp))
                            TaskBox("Tasks Assigned To Me", assignedToMeList, navController)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TaskBox(title: String, taskList: List<TaskStatusWiseCountDetails>, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 6.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                taskList.forEach { task ->
                    TaskStatusItem(task, navController)
                }
            }
        }
    }
}

@Composable
fun TaskStatusItem(task: TaskStatusWiseCountDetails, navController: NavController) {
    var statusColor = setStatusColor(task.statusName)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp) // Reduced padding for a refined look
            .clickable {
//                navController.navigate("taskDetailsScreen/${task.statusId}")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp, // Subtle elevation
        border = BorderStroke(1.dp, statusColor), // Dark outline
//        backgroundColor = statusColor.copy(alpha = 0.1f) // Lighter background shade
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(task.statusName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = statusColor)
            Text("Count: ${task.totalCount}", fontSize = 16.sp, color = Color.Black)
        }
    }
}
