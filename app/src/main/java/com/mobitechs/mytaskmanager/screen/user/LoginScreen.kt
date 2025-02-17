package com.mobitechs.mytaskmanager.screen.user


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
import com.mobitechs.mytaskmanager.model.LoginRequest
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.sessionUserObject
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@Composable
fun LoginScreen(navController: NavController, viewModel: ViewModelUser) {
    val context: Context = LocalContext.current
    var emailPhone by remember { mutableStateOf("Sonawane.ptk@gmail.com") }
    var password by remember { mutableStateOf("PRATIK123") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }


    fun isValidLoginId(loginId: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(loginId)
            .matches() || (loginId.length in 10..15 && loginId.all { it.isDigit() })
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
                Text("Login", fontSize = 24.sp, color = Color(0xFF6200EE))
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = emailPhone,
                    onValueChange = { emailPhone = it },
                    label = { Text("Email or Phone No") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            when {
                                emailPhone.isEmpty() || password.isEmpty() -> {
                                    errorMessage = "Email and password are required"
                                }

                                !isValidLoginId(emailPhone) -> {
                                    errorMessage = "Enter a valid Email or Phone Number"
                                }

                                else -> {
                                    isLoading = true
                                    viewModel.userLogin(
                                        LoginRequest(
                                            emailPhone,
                                            password
                                        )
                                    ) { response ->
                                        isLoading = false
                                        response?.let {
                                            ShowToast(context, it.message)
                                            if (it.statusCode == 200) {
                                                sessionUserObject(context, it.data)
                                                navController.navigate("homeScreen")
                                            } else {
                                                errorMessage = it.message
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
                        Text("Login", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { navController.navigate("forgotPasswordScreen") }) {
                        Text("Forgot Password?", color = Color(0xFF6200EE))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { navController.navigate("registerScreen") }) {
                        Text("Don't have an account? Register", color = Color(0xFF6200EE))
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }
            }
        }
    }
}