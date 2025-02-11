package com.mobitechs.mytaskmanager.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.mytaskmanager.model.ApiResponse
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.MyTeamData
import com.mobitechs.mytaskmanager.model.TaskResponse
import com.mobitechs.mytaskmanager.model.TeamDetails
import com.mobitechs.mytaskmanager.model.TeamMemberRequestAdd
import com.mobitechs.mytaskmanager.model.TeamMemberResponse
import com.mobitechs.mytaskmanager.model.TeamRequestAddEdit
import com.mobitechs.mytaskmanager.model.TeamRequestDelete
import com.mobitechs.mytaskmanager.model.TeamResponse
import com.mobitechs.mytaskmanager.util.RetrofitClient
import com.mobitechs.mytaskmanager.util.handleHttpException
import com.mobitechs.mytaskmanager.util.handleHttpException2
import com.mobitechs.mytaskmanager.util.handleHttpException3
import com.mobitechs.mytaskmanager.util.handleHttpExceptionTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun fetchTeamsMembers(
        myData: MyTeamData,
        onResponse: (TeamMemberResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getTeamMembers(myData)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpException3(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(TeamMemberResponse(500, "error", "Unexpected error occurred", null))
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



    fun addTeam(
        teamReq: TeamRequestAddEdit,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.createTeam(teamReq)
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

    fun editTeam(
        teamReq: TeamRequestAddEdit,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.editTeam(teamReq)
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

    fun deleteTeam(
        teamReq: TeamRequestDelete,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.deleteTeam(teamReq)
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


    fun addTeamMember(
        teamReq: TeamMemberRequestAdd,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.addTeamMember(teamReq)
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

    fun deleteTeamMember(
        teamReq: TeamMemberRequestAdd,
        onResponse: (ApiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.deleteTeamMember(teamReq)
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
