package com.mobitechs.mytaskmanager.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.TaskKPIResponse
import com.mobitechs.mytaskmanager.model.TaskStatusResponse
import com.mobitechs.mytaskmanager.model.TaskStatusWiseCountResponse
import com.mobitechs.mytaskmanager.util.RetrofitClient
import com.mobitechs.mytaskmanager.util.handleHttpExceptionTaskCount
import com.mobitechs.mytaskmanager.util.handleHttpExceptionTaskKpiList
import com.mobitechs.mytaskmanager.util.handleHttpExceptionTaskStatusList
import kotlinx.coroutines.launch
import retrofit2.HttpException


class ViewModelHome : ViewModel() {
    var response by mutableStateOf("")

    fun getCountOfTaskListAssignedByMe(
        myData: MyData,
        onResponse: (TaskStatusWiseCountResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getCountOfTaskListAssignedByMe(myData)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpExceptionTaskCount(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(
                    TaskStatusWiseCountResponse(
                        500,
                        "error",
                        "Unexpected error occurred",
                        null
                    )
                )
            }
        }
    }

    fun getCountOfTaskListAssignedToMe(
        myData: MyData,
        onResponse: (TaskStatusWiseCountResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getCountOfTaskListAssignedToMe(myData)
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpExceptionTaskCount(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(
                    TaskStatusWiseCountResponse(
                        500,
                        "error",
                        "Unexpected error occurred",
                        null
                    )
                )
            }
        }
    }


    fun getStatusList(
        onResponse: (TaskStatusResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getStatusList()
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpExceptionTaskStatusList(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(TaskStatusResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }

    fun getKpiList(
        onResponse: (TaskKPIResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getKpiList()
                if (res.statusCode == 200) {
                    response = res.message
                }
                onResponse(res)
            } catch (e: HttpException) {
                e.printStackTrace()
                onResponse(handleHttpExceptionTaskKpiList(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(TaskKPIResponse(500, "error", "Unexpected error occurred", null))
            }
        }
    }

}