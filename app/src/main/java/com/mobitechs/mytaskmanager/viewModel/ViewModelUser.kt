package com.mobitechs.mytaskmanager.viewModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.mytaskmanager.model.ApiResponse
import com.mobitechs.mytaskmanager.model.ForgotPasswordRequest
import com.mobitechs.mytaskmanager.model.ForgotPasswordResponse
import com.mobitechs.mytaskmanager.model.LoginRequest
import com.mobitechs.mytaskmanager.model.SetPasswordRequest
import com.mobitechs.mytaskmanager.model.UserRequest
import com.mobitechs.mytaskmanager.util.RetrofitClient
import com.mobitechs.mytaskmanager.util.getFileFromUri
import com.mobitechs.mytaskmanager.util.handleHttpException
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File

class ViewModelUser : ViewModel() {
    var response by mutableStateOf("")

    fun userRegister(
        user: UserRequest,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.userRegister(user)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                onResponse(handleHttpException(e))
            } catch (e: Exception) {
                onResponse(ApiResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }


    fun userLogin(
        user: LoginRequest,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.userLogin(user)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                onResponse(handleHttpException(e))
            } catch (e: Exception) {
                onResponse(ApiResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }


//    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest, onResponse: (ApiResponse?) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val res = RetrofitClient.apiService.forgotPassword(forgotPasswordRequest)
//                onResponse(res)
//            } catch (e: HttpException) {
//                onResponse(handleHttpException(e))
//            } catch (e: Exception) {
//                onResponse(ApiResponse(500, "error", "Unexpected error occurred", null))
//            }
//        }
//    }

    fun forgotPassword(
        forgotPasswordRequest: ForgotPasswordRequest,
        onResponse: (ForgotPasswordResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.forgotPassword(forgotPasswordRequest)
                onResponse(res)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody ?: "{}")
                    jsonObject.getString("message")
                } catch (ex: Exception) {
                    "Unexpected error occurred"
                }
                onResponse(ForgotPasswordResponse(e.code(), "error", errorMessage, ""))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(ForgotPasswordResponse(500, "error", "Unexpected error occurred", ""))
            }
        }
    }


    fun setPassword(setPasswordRequest: SetPasswordRequest, onResponse: (ApiResponse?) -> Unit) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.setPassword(setPasswordRequest)
                onResponse(res)
            } catch (e: HttpException) {
                onResponse(handleHttpException(e))
            } catch (e: Exception) {
                onResponse(ApiResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }

//    fun updateProfile(name:String, email:String, phone:String, profileImage:String, onResponse: (ApiResponse?) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val res = RetrofitClient.apiService.updateProfile(setPasswordRequest)
//                onResponse(res)
//            } catch (e: HttpException) {
//                onResponse(handleHttpException(e))
//            } catch (e: Exception) {
//                onResponse(ApiResponse(500, "error", "Unexpected error occurred", null))
//            }
//        }
//    }

    fun updateProfile(
        userId: String,
        name: String,
        email: String,
        phone: String,
        profileImageUri: Uri?,  // Change String to Uri
        context: Context,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userIdPart = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)
                val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
                val emailPart = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                val phonePart = RequestBody.create("text/plain".toMediaTypeOrNull(), phone)

                // ✅ Convert Uri to File for Multipart Request
                val imagePart = profileImageUri?.let { uri ->
                    val file = getFileFromUri(context, uri) // Convert Uri to File
                    file?.let {
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
                        MultipartBody.Part.createFormData("userImage", it.name, requestFile)
                    }
                }

                val response = RetrofitClient.apiService.updateProfile(
                    userId = userIdPart,
                    name = namePart,
                    email = emailPart,
                    phone = phonePart,
                    profileImage = imagePart
                )
                onResponse(response)
            } catch (e: HttpException) {
                onResponse(handleHttpException(e))
            } catch (e: Exception) {
                onResponse(ApiResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }
}

