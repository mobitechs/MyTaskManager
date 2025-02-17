package com.mobitechs.mytaskmanager.util

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.model.ApiResponse
import com.mobitechs.mytaskmanager.model.TaskResponse
import com.mobitechs.mytaskmanager.model.TaskStatus
import com.mobitechs.mytaskmanager.model.TeamMemberResponse
import com.mobitechs.mytaskmanager.model.TeamResponse
import com.mobitechs.mytaskmanager.model.UserData
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale



val statusOptions = listOf(
    TaskStatus(1, "To Do"),
    TaskStatus(2, "Reopened"),
    TaskStatus(3, "WIP"),
    TaskStatus(4, "Completed"),
    TaskStatus(5, "Cancelled")
)

fun sessionUserObject(context: Context, userData: List<UserData>?) {
    val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val userJson = Gson().toJson(userData)
    sharedPreferences.edit().putString("user", userJson).apply()
}

fun getUserFromSession(context: Context): UserData? {
    val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val userJson = sharedPreferences.getString("user", null)

    return if (userJson != null) {
        try {
            val userList = Gson().fromJson(userJson, Array<UserData>::class.java)?.toList()
            userList?.firstOrNull() // Return the first user object
        } catch (e: Exception) {
            null // Return null if any error occurs
        }
    } else {
        null
    }
}

inline fun <reified T> handleHttpException(e: HttpException): T? {
    val errorBody = e.response()?.errorBody()?.string()

    val errorMessage = try {
        val jsonObject = JSONObject(errorBody ?: "{}")
        jsonObject.getString("message")
    } catch (ex: Exception) {
        ex.printStackTrace()
        "Unexpected error occurred"
    }

    val errorResponseJson = """
        {
            "statusCode": ${e.code()},
            "status": "error",
            "message": "$errorMessage",
            "data": null
        }
    """.trimIndent()

    return try {
        Gson().fromJson(errorResponseJson, T::class.java)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

fun handleHttpException(e: HttpException): ApiResponse {
    val errorBody = e.response()?.errorBody()?.string()
    val errorMessage = try {
        val jsonObject = JSONObject(errorBody ?: "{}")
        jsonObject.getString("message")
    } catch (ex: Exception) {
        ex.printStackTrace()
        "Unexpected error occurred"
    }
    return ApiResponse(e.code(), "error", errorMessage, null)
}

fun handleHttpException2(e: HttpException): TeamResponse {
    val errorBody = e.response()?.errorBody()?.string()
    val errorMessage = try {
        val jsonObject = JSONObject(errorBody ?: "{}")
        jsonObject.getString("message")
    } catch (ex: Exception) {
        ex.printStackTrace()
        "Unexpected error occurred"
    }
    return TeamResponse(e.code(), "error", errorMessage, null)
}

fun handleHttpException3(e: HttpException): TeamMemberResponse {
    val errorBody = e.response()?.errorBody()?.string()
    val errorMessage = try {
        val jsonObject = JSONObject(errorBody ?: "{}")
        jsonObject.getString("message")
    } catch (ex: Exception) {
        ex.printStackTrace()
        "Unexpected error occurred"
    }
    return TeamMemberResponse(e.code(), "error", errorMessage, null)
}

fun handleHttpExceptionTask(e: HttpException): TaskResponse {
    val errorBody = e.response()?.errorBody()?.string()
    val errorMessage = try {
        val jsonObject = JSONObject(errorBody ?: "{}")
        jsonObject.getString("message")
    } catch (ex: Exception) {
        ex.printStackTrace()
        "Unexpected error occurred"
    }
    return TaskResponse(e.code(), "error", errorMessage, null)
}


fun ShowToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}


fun ShowDatePicker(
    context: Context,
    initialDate: Calendar = Calendar.getInstance(),
    onDateSelected: (year: Int, month: Int, dayOfMonth: Int) -> Unit
) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            Log.d("DatePicker", "Date selected: $year-${month + 1}-$dayOfMonth") // Debug log
            onDateSelected(year, month, dayOfMonth)
        },
        initialDate.get(Calendar.YEAR),
        initialDate.get(Calendar.MONTH),
        initialDate.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun formatDateTime(inputDateTime: String): String {
    // Define input and output date formats
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())

    // Parse the input date string
    val date: Date? = inputFormat.parse(inputDateTime)

    // Convert and return formatted date
    return date?.let { outputFormat.format(it) } ?: "Invalid Date"
}

fun main() {
    val inputDate = "2025-02-11 23:03:14"
    val formattedDate = formatDateTime(inputDate)
    println(formattedDate) // Output: 11 Feb 2025 11:03 PM
}
