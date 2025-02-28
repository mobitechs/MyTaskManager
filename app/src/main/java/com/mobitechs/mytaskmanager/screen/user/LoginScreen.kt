package com.mobitechs.mytaskmanager.screen.user


import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
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
import com.mobitechs.mytaskmanager.model.LoginRequest
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.sessionUserObject
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(navController: NavController, viewModel: ViewModelUser) {
    val context: Context = LocalContext.current
    var emailPhone by remember { mutableStateOf("Sonawane.ptk@gmail.com") }
    var password by remember { mutableStateOf("PRATIK123") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }


    val isSignUpEnabled by derivedStateOf {
        errorMessage = if (emailPhone.isBlank()) "Please enter name or phone no" else ""
        errorMessage = if (password.isBlank()) "Please enter password" else ""
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

        Text(
            text = "Sign In",
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
            value = emailPhone,
            onValueChange = { emailPhone = it },
            placeholder = "Email or Phone No",
            keyboardType = KeyboardType.Text
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

        // Sign Up Button
        Button(
            onClick = {
                if (isSignUpEnabled) {
                    // Perform sign in
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
                text = "Sign In",
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
        TextButton(onClick = { navController.navigate("registerScreen") }) {
            Text("Don't have an account? Register", color = Color(0xFF6200EE))
        }

        // Bottom spacer
        Spacer(modifier = Modifier.height(20.dp))
    }


}