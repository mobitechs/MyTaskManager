package com.mobitechs.mytaskmanager

import android.content.Context
import android.util.Patterns
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
import com.mobitechs.mytaskmanager.model.ForgotPasswordRequest
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@Composable
fun ForgotPasswordScreen(navController: NavController, viewModel: ViewModelUser) {
    val context: Context = LocalContext.current
    var email by remember { mutableStateOf("sonawane.ptk@gmail.com") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

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

                Spacer(modifier = Modifier.height(16.dp))
                Text("Forgot Password", fontSize = 24.sp, color = Color(0xFF6200EE))
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Enter your Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            when {
                                email.isEmpty() -> {
                                    errorMessage = "Email is required"
                                }

                                !isValidEmail(email) -> {
                                    errorMessage = "Enter a valid email address"
                                }

                                else -> {
                                    isLoading = true
                                    viewModel.forgotPassword(ForgotPasswordRequest(email)) { response ->
                                        isLoading = false
                                        response?.let {
                                            if (it.statusCode == 200) {
                                                successMessage = "Your OTP is: " + it.data
                                                ShowToast(context, successMessage)
                                                navController.navigate("setPasswordScreen/${email}/${it.data}")
                                            } else {
                                                errorMessage = it.message
                                                ShowToast(context, errorMessage)
                                            }
                                        } ?: run {
                                            errorMessage = "Unexpected error occurred"
                                            ShowToast(context, errorMessage)
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                    ) {
                        Text("Get OTP", color = Color.White)
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
