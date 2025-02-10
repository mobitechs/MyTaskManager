package com.mobitechs.mytaskmanager.model

data class UserRequest(val name: String, val email: String, val phone: String, val password: String)
data class LoginRequest(val loginId: String, val password: String)
data class SetPasswordRequest(val email: String,val otp: String, val password: String)
data class ForgotPasswordRequest(val email: String)


data class ForgotPasswordResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val data: String
)


data class ApiResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val data: List<UserData>?
)


data class UserData(
    val userId: Int,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val otp: String,
    val userImage: String,
    val isActive: Int,
    val isDelete: Int,
    val createdAt: String,
    val updatedAt: String,
    val updatedBy: Int
)

data class MyData(val userId: String)

data class TaskRequest(val taskName: String, val taskDescription: String, val kpi: String, val ownerId: String, val assigneeId: String, val teamId: String, val expectedDate: String, val status: String, val updatedBy: String)
data class TaskResponse(val success: Boolean, val message: String, val tasks: List<Task>?)
data class Task(val id: String, val name: String, val description: String, val status: String)






// Team Details request and response
data class TeamRequestAddEdit(val teamId: String, val teamName: String, val description: String, val image: String, val updatedBy: String)
data class TeamRequestDelete(val teamId: String, val updatedBy: String)
data class TeamMemberRequestAdd(val teamId: String,val teamMemberId: String, val updatedBy: String)
data class TeamResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val data: List<TeamDetails>?
)
data class TeamDetails(val teamId: String, val teamName: String, val description: String, val image: String)
data class MyTeamData(val teamId: String)


data class TeamMemberResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val data: List<TeamMemberDetails>?
)
data class TeamMemberDetails(val teamMemberDetailsId: String, val userId: String, val name: String, val email: String,val phone: String,val userImage: String, val teamId: String, val teamName: String, val description: String, val image: String)


