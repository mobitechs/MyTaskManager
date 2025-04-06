package com.mobitechs.mytaskmanager.screen.user

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.R
import com.mobitechs.mytaskmanager.model.SetPasswordRequest
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@SuppressLint("UnrememberedMutableState")
@Composable
fun SetPasswordScreen(
    navController: NavController,
    viewModel: ViewModelUser,
    email: String,
    myOtp: String
) {
    val context: Context = LocalContext.current
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val isSignUpEnabled by derivedStateOf {
        errorMessage = if (otp.isBlank()) "Please enter OTP" else ""
        errorMessage = if (password.isBlank()) "Please enter Password" else ""
        errorMessage = when {
            newPassword.isBlank() -> "Please confirm Password"
            newPassword != password -> "Passwords do not match"
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
            text = "Set New Password",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101618)
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Your OTP is $myOtp",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
                color = Color(0xFF101618)
            ),
            modifier = Modifier.padding(vertical = 4.dp)
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


        SignUpTextField(
            value = otp,
            onValueChange = { otp = it },
            placeholder = "Enter OTP",
            keyboardType = KeyboardType.Number,
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Numbers,
                    contentDescription = "Password",
                    tint = Color(0xFF5E7F8D)
                )
            }
        )

        SignUpTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            keyboardType = KeyboardType.Password,
            visualTransformation = if (isPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (isPasswordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Password Visibility",
                    tint = Color(0xFF5E7F8D),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { isPasswordVisible = !isPasswordVisible }
                )
            }
        )

        SignUpTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            placeholder = "Confirm Password",
            keyboardType = KeyboardType.Password,
            visualTransformation = if (isConfirmPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (isConfirmPasswordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Password Visibility",
                    tint = Color(0xFF5E7F8D),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                )
            }
        )


        // Sign Up Button
        Button(
            onClick = {
                if (isSignUpEnabled) {
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
                text = "Sign up",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("forgotPasswordScreen") }) {
            Text("Forgot Password?", color = Color(0xFF6200EE))
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("loginScreen") }) {
            Text("Back to Login", color = Color(0xFF6200EE))
        }

        // Bottom spacer
        Spacer(modifier = Modifier.height(20.dp))
    }

}