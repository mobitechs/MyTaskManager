package com.mobitechs.mytaskmanager.screen.taskDelegate


import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.components.BottomNavigationBar
import com.mobitechs.mytaskmanager.components.ConfirmationDialog
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TaskDetails
import com.mobitechs.mytaskmanager.model.TaskRequestDelete
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTask

@Composable
fun TaskDelegateListScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ViewModelTask = viewModel()

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshAPITrigger by remember { mutableStateOf(0) }

    var taskList by remember { mutableStateOf<List<TaskDetails>>(emptyList()) }
    var filteredTasks by remember { mutableStateOf(taskList) }
    var selectedTeam by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()

    LaunchedEffect(refreshAPITrigger) {
        viewModel.getTaskListAssignedByMe(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    taskList = it.data ?: emptyList()
                    filteredTasks = taskList
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
                actions = {
                    IconButton(onClick = { refreshAPITrigger++ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("taskAddScreen/${Uri.encode("null")}") },
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 🔹 Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    filteredTasks = taskList.filter {
                        it.taskName.contains(query.text, ignoreCase = true) ||
                                it.taskDescription.contains(query.text, ignoreCase = true) ||
                                it.assigneeName.contains(query.text, ignoreCase = true) ||
                                it.taskName.contains(query.text, ignoreCase = true) ||
                                it.teamName.contains(query.text, ignoreCase = true)
                    }
                },
                label = { Text("Search Tasks") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )

            // 🔹 Team Filter Section
            val teamCounts = taskList.groupingBy { it.teamName }.eachCount()
            val allCount = taskList.size

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    TeamChip(
                        teamName = "All ($allCount)",
                        isSelected = selectedTeam == "All",
                        onClick = {
                            selectedTeam = "All"
                            filteredTasks = taskList
                        }
                    )
                }
                items(teamCounts.entries.toList()) { (team, count) ->
                    TeamChip(
                        teamName = "$team ($count)",
                        isSelected = selectedTeam == team,
                        onClick = {
                            selectedTeam = team
                            filteredTasks = taskList.filter { it.teamName == team }
                        }
                    )
                }
            }

            // 🔹 Task List
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMessage!!, fontSize = 18.sp, color = Color.Red)
                    }
                }

                filteredTasks.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tasks found", fontSize = 18.sp, color = Color.Gray)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(filteredTasks) { task ->
                            TaskCard(task, navController, viewModel, userId) { refreshAPITrigger++ }
                        }
                    }
                }
            }
        }
    }
}

// 🔹 Team Filter Chip
@Composable
fun TeamChip(teamName: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color.Blue else Color.LightGray,
            contentColor = Color.White
        ),
        shape = CircleShape,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(teamName, fontSize = 12.sp)
    }
}

// 🔹 Task Card Component
@Composable
fun TaskCard(
    task: TaskDetails,
    navController: NavController,
    viewModel: ViewModelTask,
    userId: String,
    refreshList: () -> Int
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
//            .clickable {
//                navController.navigate("taskDetailsScreen/${Gson().toJson(task)}")
//            }
        , elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.taskName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(task.taskDescription, fontSize = 14.sp, color = Color.DarkGray)
            Text("Due: ${task.expectedDate}", fontSize = 12.sp, color = Color.Red)

            // 🔹 This row now contains Status, Assigned To, Team Name & Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Status: ${task.status}", fontSize = 12.sp, color = Color.Blue)
                    Text("Assigned to: ${task.assigneeName}", fontSize = 12.sp, color = Color.Black)
                    Text("Team: ${task.teamName}", fontSize = 12.sp, color = Color.Magenta)
                }

                Row {
                    IconButton(onClick = {
                        navController.navigate("taskAddScreen/${Gson().toJson(task)}")
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Task",
                            tint = Color.Blue
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }

    // 🔹 Show Confirmation Dialog Before Deleting
    if (showDeleteDialog) {
        ConfirmationDialog(
            title = "Delete Task",
            message = "Are you sure you want to delete this Task?",
            confirmButtonText = "Delete",
            cancelButtonText = "Cancel",
            onConfirm = {
                viewModel.deleteTask(TaskRequestDelete(task.taskId, userId)) { response ->
                    response?.let {
                        refreshList() // 🔄 Refresh task list after deletion
                        ShowToast(context, it.message)
                    }
                }
                showDeleteDialog = false
            },
            onCancel = { showDeleteDialog = false },
            isDialogVisible = true,
            onDismiss = { showDeleteDialog = false }
        )
    }
}