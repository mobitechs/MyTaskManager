package com.mobitechs.mytaskmanager.screen.user


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.model.SetPasswordRequest
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@Composable
fun SetPasswordScreen(
    navController: NavController,
    viewModel: ViewModelUser,
    email: String,
    myOtp: String
) {
    val context: Context = LocalContext.current
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Set New Password", fontSize = 24.sp, color = Color(0xFF6200EE))
//                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Email: $email", fontSize = 16.sp, color = Color.Gray)
                Text("OTP: $myOtp", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("Enter OTP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Enter New Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            when {
                                otp.isEmpty() || newPassword.isEmpty() -> {
                                    errorMessage = "OTP and Password are required"
                                }

                                else -> {
                                    isLoading = true
                                    viewModel.setPassword(
                                        SetPasswordRequest(
                                            email,
                                            otp,
                                            newPassword
                                        )
                                    ) { response ->
                                        isLoading = false
                                        response?.let {
                                            if (it.statusCode == 200) {
                                                successMessage = "Password changed successfully"
                                                ShowToast(context, successMessage)
                                                navController.navigate("loginScreen")
                                            } else {
                                                errorMessage = it.message
                                                ShowToast(context, "Error: ${it.message}")
                                            }
                                        } ?: run {
                                            errorMessage = "Unexpected error occurred"
                                            ShowToast(context, "Unexpected error occurred")
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                    ) {
                        Text("Set Password", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { navController.navigate("loginScreen") }) {
                        Text("Back to Login", color = Color(0xFF6200EE))
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }
                if (successMessage.isNotEmpty()) {
                    Text(text = successMessage, color = Color.Green)
                }
            }
        }
    }
}