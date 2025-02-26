package com.mobitechs.mytaskmanager.screen.user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.mobitechs.mytaskmanager.R
import com.mobitechs.mytaskmanager.util.Constant
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val viewModel: ViewModelUser = viewModel()
    val user = remember { mutableStateOf(getUserFromSession(context)) }
    var userId by remember { mutableStateOf(user.value?.userId ?: "") }
    var name by remember { mutableStateOf(user.value?.name ?: "") }
    var email by remember { mutableStateOf(user.value?.email ?: "") }
    var phone by remember { mutableStateOf(user.value?.phone ?: "") }
    var profileImage by remember { mutableStateOf(user.value?.userImage ?: "") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var permissionGranted by remember { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profileImage = it.toString()
            }
        }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                launcher.launch("image/*")
            } else {
                errorMessage = "Permission denied permanently. Go to settings to enable."
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = if (profileImage.isNotEmpty()) {
                    rememberAsyncImagePainter(Constant.serverImagePath +""+profileImage)
                } else {
                    painterResource(id = R.drawable.outline_person_24)
                },
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .border(BorderStroke(2.dp, Color.Black), CircleShape) // 🔴 Red border added
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {

                        if (permissionGranted) {
                            launcher.launch("image/*")
                        } else {
//                            errorMessage = "Permission denied permanently. Go to settings to enable."
//                            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                            ShowToast(context, "Permission not Granted")
                            launcher.launch("image/*")
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.contains("settings")) {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Open Settings")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        viewModel.updateProfile(
                            userId.toString(),
                            name,
                            email,
                            phone,
                            if (profileImage.isNotEmpty()) Uri.parse(profileImage) else null, // Convert String to Uri
                            context
                        ) { response ->
                            isLoading = false
                            response?.let {
                                if (it.statusCode == 200) {
                                    successMessage = "Profile updated successfully!"
                                } else {
                                    errorMessage = it.message
                                }
                            } ?: run {
                                errorMessage = "Unexpected error occurred"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Profile")
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(8.dp))
            }
            if (successMessage.isNotEmpty()) {
                Text(text = successMessage, color = Color.Green, modifier = Modifier.padding(8.dp))
            }
        }
    }
}