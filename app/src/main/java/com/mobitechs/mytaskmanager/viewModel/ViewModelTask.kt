package com.mobitechs.mytaskmanager.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.mytaskmanager.model.ApiResponse
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TaskRequestAddEdit
import com.mobitechs.mytaskmanager.model.TaskRequestComment
import com.mobitechs.mytaskmanager.model.TaskRequestDelete
import com.mobitechs.mytaskmanager.model.TaskRequestReminder
import com.mobitechs.mytaskmanager.model.TaskRequestStatus
import com.mobitechs.mytaskmanager.model.TaskResponse
import com.mobitechs.mytaskmanager.model.TeamResponse
import com.mobitechs.mytaskmanager.util.RetrofitClient
import com.mobitechs.mytaskmanager.util.handleHttpException
import com.mobitechs.mytaskmanager.util.handleHttpException2
import com.mobitechs.mytaskmanager.util.handleHttpExceptionTask
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ViewModelTask : ViewModel() {

    var response by mutableStateOf("")


    fun getTaskListAssignedToMe(
        myData: MyData,
        onResponse: (TaskResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getTaskListAssignedToMe(myData)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpExceptionTask(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(TaskResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }

    fun getTaskListAssignedByMe(
        myData: MyData,
        onResponse: (TaskResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getTaskListAssignedByMe(myData)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpExceptionTask(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(TaskResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }


    fun getUserList(
        myData: MyData,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getUserList(myData)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpException(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(ApiResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }

    fun fetchTeams(
        myData: MyData,
        onResponse: (TeamResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getTeams(myData)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpException2(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(TeamResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }


    fun addTask(
        teamReq: TaskRequestAddEdit,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.addTask(teamReq)
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

    fun editTask(
        teamReq: TaskRequestAddEdit,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.editTask(teamReq)
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

    fun deleteTask(
        teamReq: TaskRequestDelete,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.deleteTask(teamReq)
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

    fun updateStatus(
        teamReq: TaskRequestStatus,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.updateStatus(teamReq)
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

    fun addReminderForTask(
        teamReq: TaskRequestReminder,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.addReminderForTask(teamReq)
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

    fun addComment(
        teamReq: TaskRequestComment,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.addComment(teamReq)
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

    fun editComment(
        teamReq: TaskRequestComment,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.editComment(teamReq)
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


}
