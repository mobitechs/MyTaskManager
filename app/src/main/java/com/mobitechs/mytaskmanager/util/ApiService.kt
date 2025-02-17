package com.mobitechs.mytaskmanager.util


import com.mobitechs.mytaskmanager.model.ApiResponse
import com.mobitechs.mytaskmanager.model.ForgotPasswordRequest
import com.mobitechs.mytaskmanager.model.ForgotPasswordResponse
import com.mobitechs.mytaskmanager.model.LoginRequest
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.MyTeamData
import com.mobitechs.mytaskmanager.model.SetPasswordRequest
import com.mobitechs.mytaskmanager.model.TaskRequestAddEdit
import com.mobitechs.mytaskmanager.model.TaskRequestComment
import com.mobitechs.mytaskmanager.model.TaskRequestDelete
import com.mobitechs.mytaskmanager.model.TaskRequestReminder
import com.mobitechs.mytaskmanager.model.TaskRequestStatus
import com.mobitechs.mytaskmanager.model.TaskRequestUpdate
import com.mobitechs.mytaskmanager.model.TaskResponse
import com.mobitechs.mytaskmanager.model.TeamMemberRequestAdd
import com.mobitechs.mytaskmanager.model.TeamMemberResponse
import com.mobitechs.mytaskmanager.model.TeamRequestAddEdit
import com.mobitechs.mytaskmanager.model.TeamRequestDelete
import com.mobitechs.mytaskmanager.model.TeamResponse
import com.mobitechs.mytaskmanager.model.UserRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("userRegister")
    suspend fun userRegister(@Body user: UserRequest): ApiResponse

    @POST("userLogin")
    suspend fun userLogin(@Body login: LoginRequest): ApiResponse

    @POST("forgotPassword")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("setPassword")
    suspend fun setPassword(@Body setPasswordRequest: SetPasswordRequest): ApiResponse


//    @GET("getTaskListAssignedToMe/{userId}")
//    suspend fun getTasksAssignedToMe(@Path("userId") userId: String): TaskResponse


    //    team Details
    @POST("createTeam")
    suspend fun createTeam(@Body team: TeamRequestAddEdit): ApiResponse

    @POST("editTeam")
    suspend fun editTeam(@Body team: TeamRequestAddEdit): ApiResponse

    @POST("deleteTeam")
    suspend fun deleteTeam(@Body team: TeamRequestDelete): ApiResponse

    @POST("getTeamList")
    suspend fun getTeams(@Body team: MyData): TeamResponse

    @POST("getTeamMembers")
    suspend fun getTeamMembers(@Body team: MyTeamData): TeamMemberResponse

    @POST("addTeamMember")
    suspend fun addTeamMember(@Body team: TeamMemberRequestAdd): ApiResponse

    @POST("deleteTeamMember")
    suspend fun deleteTeamMember(@Body team: TeamMemberRequestAdd): ApiResponse

    @POST("getUserList")
    suspend fun getUserList(@Body team: MyData): ApiResponse


// task Details

    @POST("getTaskListAssignedToMe")
    suspend fun getTaskListAssignedToMe(@Body team: MyData): TaskResponse

    @POST("getTaskListAssignedByMe")
    suspend fun getTaskListAssignedByMe(@Body team: MyData): TaskResponse

    @POST("addTask")
    suspend fun addTask(@Body task: TaskRequestAddEdit): ApiResponse

    @POST("editTask")
    suspend fun editTask(@Body task: TaskRequestAddEdit): ApiResponse

    @POST("deleteTask")
    suspend fun deleteTask(@Body task: TaskRequestDelete): ApiResponse



    @POST("addReminderForTask")
    suspend fun addReminderForTask(@Body task: TaskRequestReminder): ApiResponse

    @POST("updateStatus")
    suspend fun updateStatus(@Body task: TaskRequestStatus): ApiResponse

    @POST("addComment")
    suspend fun addComment(@Body task: TaskRequestComment): ApiResponse

    @POST("editComment")
    suspend fun editComment(@Body task: TaskRequestComment): ApiResponse

    @POST("updateTaskDetails")
    suspend fun updateTaskDetails(@Body task: TaskRequestUpdate): ApiResponse


}

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://mobitechs.in/MyTaskMangerAPI/api/") // Replace with your API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}