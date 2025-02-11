package com.mobitechs.mytaskmanager.team

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.MyTeamData
import com.mobitechs.mytaskmanager.model.TeamDetails
import com.mobitechs.mytaskmanager.model.TeamMemberDetails
import com.mobitechs.mytaskmanager.model.TeamMemberRequestAdd
import com.mobitechs.mytaskmanager.model.UserData
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTeam

@Composable
fun TeamDetailsScreen(navController: NavController, teamJson: String?) {
    val context = LocalContext.current
    val viewModel: ViewModelTeam = viewModel()
    val team = remember { mutableStateOf(Gson().fromJson(teamJson, TeamDetails::class.java)) }
    var teamMembers by remember { mutableStateOf<List<TeamMemberDetails>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<UserData?>(null) }
    var memberList by remember { mutableStateOf<List<UserData>>(emptyList()) }

    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()

    LaunchedEffect(team.value.teamId) {
        viewModel.fetchTeamsMembers(MyTeamData(team.value.teamId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    teamMembers = it.data ?: emptyList()
                } else {
                    errorMessage = it.message
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(team.value.teamName) }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }, backgroundColor = MaterialTheme.colors.primarySurface)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSearch = true }, shape = CircleShape) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Add Member")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Text(
                    text = "${team.value.description}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    items(teamMembers) { member ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(member.image),
                                    contentDescription = "Member Photo",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(member.name, fontSize = 16.sp, color = Color.Black)
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = {
//                                    viewModel.removeTeamMember(team.value.id, member.id)
                                    viewModel.deleteTeamMember(
                                        TeamMemberRequestAdd(
                                            team.value.teamId,
                                            member.userId,
                                            userId
                                        )
                                    ) { response ->
                                        isLoading = false
                                        response?.let {
                                            teamMembers = teamMembers.toMutableList().apply {
                                                removeIf { it.userId == member.userId }
                                            }
                                            ShowToast(context, it.message)
                                        } ?: run {
                                            errorMessage = "Unexpected error occurred"
                                            ShowToast(context, errorMessage!!)
                                        }
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove Member",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showSearch) {
                AlertDialog(
                    onDismissRequest = { showSearch = false },
                    title = { Text("Add Team Member") },
                    text = {
                        Column {
                            Spacer(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.weight(1f))
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
//                                  viewModel.getUserList(MyData(userId)) { response ->
//                                      response?.let { memberList = it.data ?: emptyList() }
//                                  }
                                    viewModel.getUserList(MyData(userId)) { response ->
                                        isLoading = false
                                        response?.let {
                                            if (it.statusCode == 200) {
                                                memberList = it.data ?: emptyList()
                                            } else {
                                                errorMessage = it.message
                                                ShowToast(context,errorMessage!!)
                                            }
                                        }
                                    }
                                },
                                label = { Text("Search Members") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            LazyColumn {
                                items(memberList) { member ->
                                    val isAlreadyAdded = teamMembers.any { it.userId == member.userId.toString() }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .clickable(enabled = !isAlreadyAdded) {
                                                viewModel.addTeamMember(
                                                    TeamMemberRequestAdd(team.value.teamId, member.userId.toString(), userId)
                                                ) { response ->
                                                    response?.let {
                                                        ShowToast(context, it.message)
                                                        if (it.statusCode == 200) {
//                                                        navController.navigate("homeScreen")
                                                            navController.popBackStack()
                                                        } else {
                                                            errorMessage = it.message
                                                        }
                                                    } ?: run {
                                                        errorMessage = "Unexpected error occurred"
                                                        ShowToast(context, errorMessage!!)
                                                    }
                                                }
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(member.userImage),
                                            contentDescription = "Member Photo",
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            member.name,
                                            fontSize = 16.sp,
                                            color = if (isAlreadyAdded) Color.Gray else Color.Black
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (isAlreadyAdded) {
                                            Icon(Icons.Default.Check, contentDescription = "Already Added", tint = Color.Gray)
                                        }

                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                    },
                    dismissButton = {
                        Button(onClick = { showSearch = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}