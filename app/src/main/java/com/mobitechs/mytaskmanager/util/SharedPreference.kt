package com.mobitechs.mytaskmanager.util

import android.content.Context
import com.google.gson.Gson
import com.mobitechs.mytaskmanager.model.KpiDetails
import com.mobitechs.mytaskmanager.model.StatusDetails
import com.mobitechs.mytaskmanager.model.UserData



fun clearUserSession(context: Context) {
    val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
}

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



fun setSessionKpiDetails(context: Context, list: List<KpiDetails>?) {
    val sharedPreferences = context.getSharedPreferences("KPIDetails", Context.MODE_PRIVATE)
    val json = Gson().toJson(list)
    sharedPreferences.edit().putString("kpi", json).apply()
}

fun getSessionKpiDetails(context: Context): List<KpiDetails>? {
    val sharedPreferences = context.getSharedPreferences("KPIDetails", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("kpi", null)

    return if (json != null) {
        try {
            Gson().fromJson(json, Array<KpiDetails>::class.java)?.toList()

        } catch (e: Exception) {
            null // Return null if any error occurs
        }
    } else {
        null
    }
}



fun setSessionStatusDetails(context: Context, list: List<StatusDetails>?) {
    val sharedPreferences = context.getSharedPreferences("StatusDetails", Context.MODE_PRIVATE)
    val json = Gson().toJson(list)
    sharedPreferences.edit().putString("status", json).apply()
}

fun getSessionStatusDetails(context: Context): List<StatusDetails>? {
    val sharedPreferences = context.getSharedPreferences("StatusDetails", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("status", null)

    return if (!json.isNullOrEmpty()) {
        try {
            Gson().fromJson(json, Array<StatusDetails>::class.java)?.toList()
        } catch (e: Exception) {
            null // Return null if any error occurs
        }
    } else {
        null
    }
}
