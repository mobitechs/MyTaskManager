package com.mobitechs.mytaskmanager.screen.taskDelegate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TaskDetails
import com.mobitechs.mytaskmanager.model.TaskRequestAddEdit
import com.mobitechs.mytaskmanager.model.TeamDetails
import com.mobitechs.mytaskmanager.model.UserData
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTask

@Composable
fun TaskAddScreen(navController: NavController, taskJson: String?) {
    val context = LocalContext.current

    val viewModel: ViewModelTask = viewModel()
    val isEditMode = taskJson != "null" && taskJson != null
    val taskDetails = remember {
        mutableStateOf(
            if (isEditMode) Gson().fromJson(
                taskJson,
                TaskDetails::class.java
            ) else TaskDetails(
                taskId = "",
                taskName = "",
                taskDescription = "",
                expectedDate = "",
                status = "",
                kpi = "",
                noOfReminder = "",
                comment = "",
                assigneeName = "",
                assigneeEmail = "",
                assigneePhone = "",
                ownerName = "",
                ownerEmail = "",
                ownerPhone = "",
                teamId = "",
                teamName = "",
                teamDescription = ""
            )
        )
    }


    var isLoading by remember { mutableStateOf(true) }
    var teams by remember { mutableStateOf<List<TeamDetails>>(emptyList()) }
    var users by remember { mutableStateOf<List<UserData>>(emptyList()) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()

    LaunchedEffect(Unit) {
        viewModel.getUserList(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    users = it.data ?: emptyList()
                } else {
//                    errorMessage = it.message
                    ShowToast(context, it.message)
                }
            }
        }

        viewModel.fetchTeams(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    teams = it.data ?: emptyList()
                } else {
//                    errorMessage = it.message
                    ShowToast(context, it.message)
                }
            }
        }
    }

    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var kpi by remember { mutableStateOf("") }
    var selectedTeamId by remember { mutableStateOf("") }
    var selectedAssigneeId by remember { mutableStateOf("") }
    var expectedDate by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Task" else "Add Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = kpi,
                onValueChange = { kpi = it },
                label = { Text("KPI") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(16.dp))

            DropdownMenuBox(
                "Select Team",
                teams.map { it.teamId to it.teamName },
                selectedTeamId
            ) { teamId ->
                selectedTeamId = teamId
            }

            Spacer(modifier = Modifier.height(16.dp))
            DropdownMenuBox(
                "Select Assignee",
                users.map { it.userId.toString() to it.name },
                selectedAssigneeId
            ) { userId ->
                selectedAssigneeId = userId
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = expectedDate,
                onValueChange = { expectedDate = it },
                label = { Text("Expected Date") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (taskName.isBlank()) {
                            errorMessage = "Task name cannot be empty"
                        } else {
                            isLoading = true
                            if (isEditMode) {
                                viewModel.editTask(
                                    TaskRequestAddEdit(
                                        taskDetails.value.taskId,
                                        taskName,
                                        taskDescription,
                                        kpi,
                                        userId,
                                        selectedAssigneeId,
                                        selectedTeamId,
                                        expectedDate,
                                        status,
                                        userId
                                    )
                                ) { response ->
                                    isLoading = false
                                    response?.let {
                                        ShowToast(context, it.message)
                                        if (it.statusCode == 200) {
//                                            navController.navigate("homeScreen")
                                            navController.popBackStack()
                                        } else {
                                            errorMessage = it.message
                                        }
                                    } ?: run {
                                        errorMessage = "Unexpected error occurred"
                                        ShowToast(context, errorMessage!!)
                                    }
                                }

                            } else {
                                viewModel.addTask(
                                    TaskRequestAddEdit(
                                        "",
                                        taskName,
                                        taskDescription,
                                        kpi,
                                        userId,
                                        selectedAssigneeId,
                                        selectedTeamId,
                                        expectedDate,
                                        status,
                                        userId
                                    )
                                ) { response ->
                                    isLoading = false
                                    response?.let {
                                        ShowToast(context, it.message)
                                        if (it.statusCode == 200) {
//                                            navController.navigate("homeScreen")
                                            navController.popBackStack()
                                        } else {
                                            errorMessage = it.message
                                        }
                                    } ?: run {
                                        errorMessage = "Unexpected error occurred"
                                        ShowToast(context, errorMessage!!)
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isEditMode) "Update Task" else "Create Task")
                }
            }
        }
    }


}

@Composable
fun DropdownMenuBox(
    label: String,
    items: List<Pair<String, String>>,
    selectedValue: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var displayText by remember { mutableStateOf(label) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = true }) {
        Text(text = displayText, modifier = Modifier.padding(16.dp))
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { (id, name) ->
                DropdownMenuItem(onClick = {
                    onSelected(id)
                    displayText = name
                    expanded = false
                }) {
                    Text(text = name)
                }
            }
        }
    }
}
