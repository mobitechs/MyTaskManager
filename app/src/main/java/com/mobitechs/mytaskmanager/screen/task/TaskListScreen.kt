package com.mobitechs.mytaskmanager.screen.task


import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.mobitechs.mytaskmanager.components.BottomNavigationBar
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TaskDetails
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTask

@Composable
fun TaskListScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ViewModelTask = viewModel()
    var taskList by remember { mutableStateOf<List<TaskDetails>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSortOptions by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("None") }

    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()

    LaunchedEffect(Unit) {
        viewModel.getTaskListAssignedByMe(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    taskList = it.data ?: emptyList()
                } else {
                    errorMessage = it.message
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assigned Tasks") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                },
                actions = {
                    IconButton(onClick = { showSortOptions = !showSortOptions }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Sort Options")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("taskAddScreen/${Uri.encode("null")}") },
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (showSortOptions) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        sortOption = "Date"
                        taskList = taskList.sortedBy { it.expectedDate }
                        showSortOptions = false
                    }) {
                        Text("Sort by Date")
                    }
                    Button(onClick = {
                        sortOption = "Status"
                        taskList = taskList.sortedBy { it.status }
                        showSortOptions = false
                    }) {
                        Text("Sort by Status")
                    }
                }
            }
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    items(taskList) { task ->
                        TaskItem(task)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 6.dp,
        backgroundColor = Color(0xFFE3F2FD)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.taskName,
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = task.taskDescription,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Due: ${task.expectedDate}",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Status: ${task.status}",
                fontSize = 12.sp,
                color = Color.Blue,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Assigned by: ${task.ownerName}",
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Team: ${task.teamName}",
                fontSize = 12.sp,
                color = Color.Magenta,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

