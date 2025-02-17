package com.mobitechs.mytaskmanager.viewModel

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
import com.mobitechs.mytaskmanager.util.handleHttpException
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException


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
}
