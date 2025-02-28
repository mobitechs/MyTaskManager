package com.mobitechs.mytaskmanager.screen.taskForMe

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
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
import com.mobitechs.mytaskmanager.components.setStatusColor
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TaskDetails
import com.mobitechs.mytaskmanager.model.TaskRequestDelete
import com.mobitechs.mytaskmanager.ui.theme.accentColor
import com.mobitechs.mytaskmanager.ui.theme.primaryTextColor
import com.mobitechs.mytaskmanager.ui.theme.redColor
import com.mobitechs.mytaskmanager.ui.theme.surfaceColor
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

@Composable
fun TaskForMeScreen(navController: NavController) {


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
        viewModel.getTaskListAssignedToMe(MyData(userId)) { response ->
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
                title = { Text("Tasks For Me") },
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
                            filteredTasks = taskList.filter { it.statusName == selectedStatus }
                        }
                        ) {
                            Text("To Do Tasks")
                        }
                        DropdownMenuItem(onClick = {
                            selectedStatus = "Reopened";
                            filteredTasks = taskList.filter { it.statusName == selectedStatus }
                        }
                        ) {
                            Text("Reopened Tasks")
                        }
                        DropdownMenuItem(onClick = {
                            selectedStatus = "WIP";
                            filteredTasks = taskList.filter { it.statusName == selectedStatus }
                        }) {
                            Text("WIP Tasks")
                        }
                        DropdownMenuItem(onClick = {
                            selectedStatus = "Completed";
                            filteredTasks = taskList.filter { it.statusName == selectedStatus }
                        }) {
                            Text("Completed Tasks")
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            SearchBar(
                searchQuery, taskList, selectedDate,
                onSearch = { query ->
                    searchQuery = query
                    filteredTasks = taskList.filter {
                        it.taskName.contains(query.text, ignoreCase = true) ||
                                it.taskDescription.contains(query.text, ignoreCase = true) ||
                                it.assigneeName.contains(query.text, ignoreCase = true) ||
                                it.ownerTeamName.contains(query.text, ignoreCase = true)
                    }
                },
                onDateSelect = { date ->
                    selectedDate = date
                    filteredTasks =
                        if (date == "All") taskList else taskList.filter { it.expectedDate == date }
                }
            )


            // Team Filter Section
            val teamCounts = taskList.groupingBy { it.ownerTeamName }.eachCount()
            val allCount = taskList.size

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    TeamChip(
                        ownerTeamName = "All ($allCount)",
                        isSelected = selectedTeam == "All",
                        onClick = {
                            selectedTeam = "All"
                            filteredTasks = taskList
                        }
                    )
                }
                items(teamCounts.entries.toList()) { (team, count) ->
                    TeamChip(
                        ownerTeamName = "$team ($count)",
                        isSelected = selectedTeam == team,
                        onClick = {
                            selectedTeam = team
                            filteredTasks = taskList.filter { it.ownerTeamName == team }
                        }
                    )
                }
            }

//            TaskSummaryCard(filteredTasks, selectedTeam)

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
            .padding(16.dp),
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
fun TeamChip(ownerTeamName: String, isSelected: Boolean, onClick: (String) -> Unit) {

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) accentColor else surfaceColor,
        modifier = Modifier.height(32.dp),
        onClick = { onClick(ownerTeamName) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = ownerTeamName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF101618)
            )
        }
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
    var statusColor = setStatusColor(task.statusName)


    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = statusColor,  // Use status color for border
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = task.taskName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF101618)
                    )

                    Text(
                        text = "Expected Date: ${task.expectedDate}",
                        fontSize = 14.sp,
                        color = Color(0xFF5E7F8D)
                    )
                    Text(
                        text = "Assigned By: : ${task.ownerTeamName} - ${task.ownerName}",
                        fontSize = 14.sp,
                        color = Color(0xFF5E7F8D)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ){
                    Button(
                        onClick = {
                            navController.navigate( "taskForMeDetailsScreen/${Uri.encode(Gson().toJson(task))}")
                        },

                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0F3F5),
                            contentColor = Color(0xFF101618)
                        ),
                        contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(32.dp)
                    ) {
                        Text(
                            text = "View details",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Status: ${task.statusName}",
                        fontSize = 14.sp,
                        color = statusColor
                    )
                }


            }


        }
    }


}

