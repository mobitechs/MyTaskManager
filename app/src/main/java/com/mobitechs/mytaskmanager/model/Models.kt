package com.mobitechs.mytaskmanager.model

data class UserRequest(val name: String, val email: String, val phone: String, val password: String)
data class LoginRequest(val loginId: String, val password: String)
data class SetPasswordRequest(val email: String, val otp: String, val password: String)
data class ForgotPasswordRequest(val email: String)


data class ForgotPasswordResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: String = ""
)


data class ApiResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: List<UserData>?
)


data class UserData(
    val userId: Int=0,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val otp: String = "",
    val userImage: String = "",
    val isActive: Int=0,
    val isDelete: Int=0,
    val createdAt: String = "",
    val updatedAt: String = "",
    val updatedBy: Int=0,
    val isEnabler: Int = 0
)

data class MyData(val userId: String)

//Task Details
data class TaskRequestAddEdit(
    val taskId: String,
    val taskName: String,
    val taskDescription: String,
    val kpiId: String,
    val ownerId: String,
    val assigneeId: String,
    val teamId: String,
    val expectedDate: String,
    val statusId: String,
    val updatedBy: String
)

data class TaskRequestDelete(val taskId: String, val updatedBy: String)
data class TaskRequestStatus(val taskId: String, val statusId: String, val updatedBy: String)
data class TaskRequestReminder(val taskId: String, val noOfReminder: String, val updatedBy: String)

data class TaskRequestComment(
    val taskId: String = "",
    val comment: String = "",
    val expectedDate: String = "",
    val updatedBy: String = ""
)
data class TaskRequestUpdate(
    val taskId: String = "",
    val statusId: String = "",
    val comment: String = "",
    val deadlineDate: String = "",
    val updatedBy: String = ""
)

data class TaskStatus(val id: Int, val name: String)

data class TaskResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: List<TaskDetails>?
)

data class TaskDetails(
    val taskId: String = "",
    val taskName: String = "",
    val taskDescription: String = "",
    val expectedDate: String = "",
    val deadlineDate: String = "",
    val statusId: String = "",
    val statusName: String = "",
    val kpiId: String = "",
    val kpiName: String = "",
    val noOfReminder: String = "",
    val comment: String = "",
    val assigneeId: String = "",
    val assigneeName: String = "",
    val assigneeEmail: String = "",
    val assigneePhone: String = "",
    val ownerName: String = "",
    val ownerEmail: String = "",
    val ownerPhone: String = "",
    val ownerTeamName: String = "",
    val teamId: String = "",
    val teamName: String = "",
    val teamDescription: String = "",
    val createdAt: String = ""
)


// Team Details request and response
data class TeamRequestAddEdit(
    val teamId: String = "",
    val teamName: String = "",
    val description: String = "",
    val image: String = "",
    val updatedBy: String = ""
)

data class TeamRequestDelete(val teamId: String, val updatedBy: String)
data class TeamMemberRequestAdd(val teamId: String, val teamMemberId: String, val updatedBy: String)
data class TeamResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: List<TeamDetails>?
)

data class TeamDetails(
    val teamId: String = "",
    val teamName: String = "",
    val description: String = "",
    val image: String = ""
)

data class MyTeamData(val teamId: String)


data class TeamMemberResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: List<TeamMemberDetails>?
)

data class TeamMemberDetails(
    val teamMemberDetailsId: String,
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val userImage: String = "",
    val teamId: String = "",
    val teamName: String = "",
    val description: String = "",
    val image: String = ""
)

data class TaskStatusResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: List<StatusDetails>?
)

data class StatusDetails(
    val statusId: String = "",
    val statusName: String = "",
)

data class TaskKPIResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: List<KpiDetails>?
)
data class KpiDetails(
    val kpiId: String = "",
    val kpiName: String = "",
)


data class TaskStatusWiseCountResponse(
    val statusCode: Int=0,
    val status: String = "",
    val message: String = "",
    val data: List<TaskStatusWiseCountDetails>?
)


data class TaskStatusWiseCountDetails(
    val statusId: Int,
    val statusName: String,
    val totalCount: Int
)
