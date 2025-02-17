package com.mobitechs.mytaskmanager.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobitechs.mytaskmanager.util.AppNavigation
import com.mobitechs.mytaskmanager.viewModel.ViewModelUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ViewModelUser = viewModel()
            AppNavigation(viewModel)
        }
    }
}


//userRegister(name,email,phone,password)
//userLogin(loginId,password)
//forgotPassword(email)
//setPassword(email,otp,password)
//getOTPOnEmail(email)
//getUserDetails(userId)
//-------------------------------------------
//createTeam(teamName,description,image,updatedBy)
//editTeam(teamId,teamName,description,image,updatedBy)
//deleteTeam(teamId,updatedBy)
//addTeamMember(teamId,teamMemberId,updatedBy)
//deleteTeamMember(teamId,teamMemberId,updatedBy)
//----------------------------------------------
//addTask(taskName,taskDescription,kpi,taskAssignerId,assigneeId,teamId,expectedDate,status,updatedBy)
//editTask(taskId,taskName,taskDescription,kpi,taskAssignerId,assigneeId,teamId,expectedDate,status,updatedBy)
//deleteTask(taskId,updatedBy)
//updateStatus(taskId,status,updatedBy)
//addReminderForTask(taskId,reminderNo,updatedBy)
//addComment(taskId,comment,dueDate,updatedBy)
//editComment(taskId,comment,dueDate,updatedBy)
//----------------------------------------------
//getTaskListAssignedToMe(userId)
//getTaskListAssignedByMe(userId)


//https://chatgpt.com/share/67a25c37-80ec-8001-beeb-fd0b591defd6
