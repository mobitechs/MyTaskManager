package com.mobitechs.mytaskmanager.screen.team


import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.components.BottomNavigationBar
import com.mobitechs.mytaskmanager.components.ConfirmationDialog
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TeamDetails
import com.mobitechs.mytaskmanager.model.TeamRequestDelete
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTeam


@Composable
fun TeamListScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ViewModelTeam = viewModel()
    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()
    var isEnabler = user.value?.isEnabler.toString()

    var teamDetailsList by remember { mutableStateOf<List<TeamDetails>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var teamDetails by remember { mutableStateOf<TeamDetails?>(null) }
    var refreshAPITrigger by remember { mutableStateOf(0) }

    LaunchedEffect(refreshAPITrigger) {
        if (userId != "") {
            viewModel.fetchTeams(MyData(userId)) { response ->
                isLoading = false
                response?.let {
                    if (it.statusCode == 200) {
                        teamDetailsList = it.data ?: emptyList()
                    } else {
                        errorMessage = it.message
                    }
                } ?: run {
                    errorMessage = "Unexpected error occurred"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Team") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("teamAddScreen/${Uri.encode("null")}")
            }, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = "Add Team")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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

                teamDetailsList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No teams available", fontSize = 18.sp, color = Color.Gray)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(teamDetailsList) { team ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        navController.navigate(
                                            "teamDetailsScreen/${
                                                Gson().toJson(
                                                    team
                                                )
                                            }"
                                        )
                                    },

                                elevation = 4.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(team.teamName, fontSize = 18.sp, color = Color.Black)
                                    Text(team.description, fontSize = 14.sp, color = Color.Gray)

                                    if(isEnabler == "1"){
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            IconButton(onClick = {
                                                navController.navigate(
                                                    "teamAddScreen/${
                                                        Gson().toJson(
                                                            team
                                                        )
                                                    }"
                                                )
                                            }) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Edit Team",
                                                    tint = Color.Blue
                                                )
                                            }
                                            IconButton(onClick = {
//                                            viewModel.deleteTeam(TeamRequestDelete(team.teamId,userId))
                                                teamDetails = team
                                                showDeleteDialog = true

                                            }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Delete Team",
                                                    tint = Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (showDeleteDialog) {
                        ConfirmationDialog(
                            title = "Delete Team",
                            message = "Are you sure you want to delete this team?",
                            confirmButtonText = "Delete",
                            cancelButtonText = "Cancel",
                            onConfirm = {
                                viewModel.deleteTeam(
                                    TeamRequestDelete(
                                        teamDetails!!.teamId,
                                        userId
                                    )
                                ) { response ->
                                    isLoading = false
                                    response?.let {
                                        refreshAPITrigger++
                                        ShowToast(context, it.message)
                                    } ?: run {
                                        errorMessage = "Unexpected error occurred"
                                        ShowToast(context, errorMessage!!)
                                    }
                                }
                            },
                            onCancel = { showDeleteDialog = false },
                            isDialogVisible = true,
                            onDismiss = { showDeleteDialog = false }
                        )
                    }
                }
            }
        }
    }
}


