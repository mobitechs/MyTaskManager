package com.mobitechs.mytaskmanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.model.ForgotPasswordRequest
import com.mobitechs.mytaskmanager.screen.user.SignUpTextField
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser


@SuppressLint("UnrememberedMutableState")
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

    val isSignUpEnabled by derivedStateOf {
        errorMessage = if (email.isBlank()) "Please enter email" else ""
        errorMessage = when {
            isValidEmail(email) -> "Please enter valid email"
            else -> ""
        }
        errorMessage.isEmpty()
    }

    // Main container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Background Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(218.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.signup_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Title
        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101618)
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if( errorMessage !=""){
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFF44336)
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Input Fields
        SignUpTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter your Email",
            keyboardType = KeyboardType.Text
        )


        // Sign Up Button
        Button(
            onClick = {
                if (isSignUpEnabled) {
                    // Perform sign in
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
            },
            enabled = isSignUpEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00B7FF),
                contentColor = Color(0xFF101618)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Get OTP",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }


        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("loginScreen") }) {
            Text("Back to Login", color = Color(0xFF6200EE))
        }

        // Bottom spacer
        Spacer(modifier = Modifier.height(20.dp))
    }

}
