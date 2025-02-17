package com.mobitechs.mytaskmanager.screen.team

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
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
import coil3.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.model.TeamDetails
import com.mobitechs.mytaskmanager.model.TeamRequestAddEdit
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelTeam

@Composable
fun TeamAddScreen(navController: NavController, teamJson: String?) {
    val context = LocalContext.current
    val viewModel: ViewModelTeam = viewModel()
    val isEditMode = teamJson != "null" && teamJson != null
    val teamDetails = remember {
        mutableStateOf(
            if (isEditMode) Gson().fromJson(
                teamJson,
                TeamDetails::class.java
            ) else TeamDetails(teamId = "", teamName = "", description = "", image = "")
        )
    }

    var teamName by remember { mutableStateOf(teamDetails.value.teamName) }
    var description by remember { mutableStateOf(teamDetails.value.description) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Team" else "Add Team") },
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
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text("Team Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(8.dp))

            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(120.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (teamName.isBlank()) {
                            errorMessage = "Team name cannot be empty"
                        } else {
                            isLoading = true
                            if (isEditMode) {
                                viewModel.editTeam(
                                    TeamRequestAddEdit(
                                        teamDetails.value.teamId,
                                        teamName,
                                        description,
                                        imageUri.toString(),
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
                                viewModel.addTeam(
                                    TeamRequestAddEdit(
                                        "",
                                        teamName,
                                        description,
                                        imageUri.toString(),
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
                    Text(if (isEditMode) "Update Team" else "Create Team")
                }
            }
        }
    }
}
