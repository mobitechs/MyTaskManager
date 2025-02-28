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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.R
import com.mobitechs.mytaskmanager.model.UserRequest
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.sessionUserObject
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

@SuppressLint("UnrememberedMutableState")
@Composable
fun RegisterScreen(navController: NavController, viewModel: ViewModelUser) {
    val context: Context = LocalContext.current
    val viewModel: ViewModelUser = viewModel()

    var name by remember { mutableStateOf("Pratik") }
    var email by remember { mutableStateOf("sonawane.ptk@gmail.com") }
    var phone by remember { mutableStateOf("8655883062") }
    var password by remember { mutableStateOf("123456") }
    var confirmPassword by remember { mutableStateOf("123456") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }



    val isSignUpEnabled by derivedStateOf {
        errorMessage = if (name.isBlank()) "Please enter name" else ""
        errorMessage = if (phone.isBlank()) "Please enter phone no" else ""
        errorMessage = if (password.isBlank()) "Please enter password" else ""
        errorMessage = when {
            password.isBlank() -> "Please confirm Password"
            confirmPassword != password -> "Passwords do not match"
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
            text = "Sign up",
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
            value = name,
            onValueChange = { name = it },
            placeholder = "Name",
            keyboardType = KeyboardType.Text
        )

        SignUpTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            keyboardType = KeyboardType.Email
        )

        SignUpTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = "Phone",
            keyboardType = KeyboardType.Phone,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Phone",
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
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
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
                // Handle sign up logic here
                if (isSignUpEnabled) {
                    // Perform sign up
                    viewModel.userRegister(
                        UserRequest(
                            name,
                            email,
                            phone,
                            password
                        )
                    ) {
                        isLoading = false
                        if (it?.statusCode == 200) {

                            sessionUserObject(context, it.data)
                            successMessage = it.message
                            ShowToast(context, successMessage)
                            //navController.navigate("home")
                        } else {
                            errorMessage = it?.message ?: "Unknown error"
                            successMessage = ""
                            ShowToast(context, "Registration Failed: $errorMessage")
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

        // Bottom spacer
        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF5E7F8D)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF0F3F5),
            focusedContainerColor = Color(0xFFF0F3F5),
            unfocusedTextColor = Color(0xFF101618),
            focusedTextColor = Color(0xFF101618),
            cursorColor = Color(0xFF00B7FF)
        ),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}