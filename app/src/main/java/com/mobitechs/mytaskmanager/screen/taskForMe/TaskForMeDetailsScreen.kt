package com.mobitechs.mytaskmanager.screen.taskForMe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.components.DatePickerButton
import com.mobitechs.mytaskmanager.components.DropdownField
import com.mobitechs.mytaskmanager.model.TaskDetails
import com.mobitechs.mytaskmanager.model.TaskRequestUpdate
import com.mobitechs.mytaskmanager.model.TaskStatus
import com.mobitechs.mytaskmanager.util.Constant
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.formatDateTime
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.util.statusOptions
import com.mobitechs.mytaskmanager.viewModel.ViewModelTask

@Composable
fun TaskForMeDetailsScreen(navController: NavController, taskJson: String?) {
    val context = LocalContext.current
    val viewModel: ViewModelTask = viewModel()

    val task = remember { Gson().fromJson(taskJson, TaskDetails::class.java) }
    var selectedStatus by remember { mutableStateOf(task.statusName) }
    var selectedDeadlineDate by remember { mutableStateOf(task.deadlineDate) }
    var commentText by remember { mutableStateOf(task.comment) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(task.teamName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primarySurface
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = 8.dp,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFF3F4F6) // Light background
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        task.taskName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TaskDetailRow(Icons.Default.Description, "Description", task.taskDescription)
                    TaskDetailRow(Icons.Default.Star, "KPI", task.kpiName)
                    TaskDetailRow(Icons.Default.Notifications, "Reminders", task.noOfReminder)
                    TaskDetailRow(Icons.Default.CalendarMonth, "Created at", formatDateTime(task.createdAt))
                    TaskDetailRow(Icons.Default.CalendarMonth, "Expected Date", formatDateTime(task.expectedDate))
                    TaskDetailRow(Icons.Default.Person, "Assigned By", task.ownerName)
                    //TaskDetailRow(Icons.Default.Group, "Team", task.teamName)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Dead Line:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    DatePickerButton(
                        selectedDate = selectedDeadlineDate,
                        onDateSelected = { newDate -> selectedDeadlineDate = newDate }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Status:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DropdownField(
                        label = "",
//                        options = statusOptions.map { it.id.toString() to it.name },
                        options = statusOptions.map { it.name },
                        selectedOption = selectedStatus,
                        onOptionSelected = { selectedName ->
                            selectedStatus = selectedName
//                            if you want id then uncomment this code and also do this
//                            selectedCategoryId =
//                                (categoryList.firstOrNull { it.categoryName == selectedName }?.categoryId
//                                    ?: "").toString()
                        },
                        context = context
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            "Comment:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            label = { Text("") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                viewModel.updateTaskDetails(
                                    TaskRequestUpdate(
                                        task.taskId,
                                        selectedStatus,
                                        commentText,
                                        selectedDeadlineDate,
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

                        ) {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TaskDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("$label: ", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        Text(value, fontSize = 14.sp, color = Color.DarkGray)
    }
}