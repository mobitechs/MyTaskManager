package com.mobitechs.mytaskmanager.screen.taskDelegate

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
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
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TaskDetails
import com.mobitechs.mytaskmanager.model.TaskRequestDelete
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

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
    var selectedStatus by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf("All") }
    var showMenu by remember { mutableStateOf(false) }

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
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Filter Menu")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(onClick = {
                            selectedTeam = "All"; filteredTasks = taskList
                        }) {
                            Text("Show All Teams")
                        }
                        DropdownMenuItem(onClick = {
                            selectedStatus = "To Do";
                            filteredTasks = taskList.filter { it.status == selectedStatus }
                        }
                        ) {
                            Text("To Do Tasks")
                        }
                        DropdownMenuItem(onClick = {
                            selectedStatus = "Reopened";
                            filteredTasks = taskList.filter { it.status == selectedStatus }
                        }
                        ) {
                            Text("Reopened Tasks")
                        }
                        DropdownMenuItem(onClick = {
                            selectedStatus = "WIP";
                            filteredTasks = taskList.filter { it.status == selectedStatus }
                        }) {
                            Text("WIP Tasks")
                        }
                        DropdownMenuItem(onClick = {
                            selectedStatus = "Completed";
                            filteredTasks = taskList.filter { it.status == selectedStatus }
                        }) {
                            Text("Completed Tasks")
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("taskAddScreen/${Uri.encode("null")}") },
                shape = CircleShape
            ) { Icon(Icons.Default.Add, contentDescription = "Add Task") }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            SearchBar(
                searchQuery, taskList, selectedDate,
                onSearch = { query ->
                    searchQuery = query
                    filteredTasks = taskList.filter {
                        it.taskName.contains(query.text, ignoreCase = true) ||
                                it.taskDescription.contains(query.text, ignoreCase = true) ||
                                it.assigneeName.contains(query.text, ignoreCase = true) ||
                                it.teamName.contains(query.text, ignoreCase = true)
                    }
                },
                onDateSelect = { date ->
                    selectedDate = date
                    filteredTasks =
                        if (date == "All") taskList else taskList.filter { it.expectedDate == date }
                }
            )

            // Team Filter Section
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

            TaskListView(
                navController,
                filteredTasks,
                viewModel,
                userId,
                context
            ) { refreshAPITrigger++ }
        }
    }
}

// Search Bar with Date Filter
@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    taskList: List<TaskDetails>,
    selectedDate: String,
    onSearch: (TextFieldValue) -> Unit,
    onDateSelect: (String) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { onSearch(it) },
            label = { Text("Search Tasks") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier
                .weight(1f)  // Allows the search bar to take most of the space
        )

        IconButton(
            onClick = {
                showDatePicker(context) { selectedDate ->
                    onDateSelect(selectedDate)
                }
            },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = "Pick Date", tint = Color.Blue)
        }
    }
}

// Show Date Picker
fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                GregorianCalendar(year, month, dayOfMonth).time
            )
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}


// Custom Chip for Teams
@Composable
fun TeamChip(teamName: String, isSelected: Boolean, onClick: (String) -> Unit) {
    Button(
        onClick = { onClick(teamName) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color.Blue else Color.Gray,
            contentColor = Color.White
        ),
        shape = CircleShape,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(teamName, fontSize = 12.sp)
    }
}

// Task List View
@Composable
fun TaskListView(
    navController: NavController,
    tasks: List<TaskDetails>,
    viewModel: ViewModelTask,
    userId: String,
    context: Context,
    refreshList: () -> Unit
) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        items(tasks) { task ->
            TaskCard(task, navController, viewModel, userId, context, refreshList)
        }
    }
}

@Composable
fun TaskCard(
    task: TaskDetails,
    navController: NavController,
    viewModel: ViewModelTask,
    userId: String,
    context: Context,
    refreshList: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
//            .clickable { navController.navigate("taskDetailsScreen/${Gson().toJson(task)}") }
            ,elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.taskName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(task.taskDescription, fontSize = 14.sp, color = Color.DarkGray)
            Text("Due: ${task.expectedDate}", fontSize = 12.sp, color = Color.Red)

            // Row for Status, Assigned To, Team Name & Buttons
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
                    // 🖊 Edit Button
                    IconButton(onClick = {
                        navController.navigate("taskAddScreen/${Gson().toJson(task)}")
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Task",
                            tint = Color.Blue
                        )
                    }

                    // 🗑 Delete Button
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

    // Show Confirmation Dialog Before Deleting
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTask(TaskRequestDelete(task.taskId, userId)) { response ->
                            response?.let {
                                refreshList() // 🔄 Refresh task list after deletion
                                ShowToast(context, it.message)
                            }
                        }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}