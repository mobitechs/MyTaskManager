package com.mobitechs.mytaskmanager.screen.user

import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.model.UserRequest
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.sessionUserObject
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@Composable
fun RegisterScreen(navController: NavController, viewModel: ViewModelUser) {
    val context: Context = LocalContext.current
    var name by remember { mutableStateOf("Pratik") }
    var email by remember { mutableStateOf("sonawane.ptk@gmail.com") }
    var phone by remember { mutableStateOf("8655883062") }
    var password by remember { mutableStateOf("123456") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.length in 10..15 && phone.all { it.isDigit() }
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
                Text("Register", fontSize = 24.sp, color = Color(0xFF6200EE))
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            when {
                                name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() -> {
                                    errorMessage = "All fields are required"
                                }
                                !isValidEmail(email) -> {
                                    errorMessage = "Invalid email address"
                                }
                                !isValidPhone(phone) -> {
                                    errorMessage = "Invalid phone number"
                                }
                                else -> {
                                    isLoading = true
                                    viewModel.userRegister(UserRequest(name, email, phone, password)) {
                                        isLoading = false
                                        if (it?.statusCode == 200) {

                                            sessionUserObject(context,it.data)

                                            successMessage = it.message
                                            ShowToast(context,successMessage)
                                            navController.navigate("home")
                                        } else {
                                            errorMessage = it?.message ?: "Unknown error"
                                            successMessage = ""
                                            ShowToast(context,"Registration Failed: $errorMessage")
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                    ) {
                        Text("Register", color = Color.White)
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
