package com.mobitechs.mytaskmanager.screen

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.R
import com.mobitechs.mytaskmanager.components.BottomNavigationBar
import com.mobitechs.mytaskmanager.components.setStatusColor
import com.mobitechs.mytaskmanager.model.KpiDetails
import com.mobitechs.mytaskmanager.model.MenuItem
import com.mobitechs.mytaskmanager.model.MyData
import com.mobitechs.mytaskmanager.model.StatusDetails
import com.mobitechs.mytaskmanager.model.TaskStatusWiseCountDetails
import com.mobitechs.mytaskmanager.util.ShowToast
import com.mobitechs.mytaskmanager.util.getUserFromSession
import com.mobitechs.mytaskmanager.util.setSessionKpiDetails
import com.mobitechs.mytaskmanager.util.setSessionStatusDetails
import com.mobitechs.mytaskmanager.viewModel.ViewModelHome
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.em
import coil3.compose.rememberAsyncImagePainter
import com.mobitechs.mytaskmanager.BuildConfig
import com.mobitechs.mytaskmanager.components.setStatusIcon
import com.mobitechs.mytaskmanager.util.Constant
import com.mobitechs.mytaskmanager.util.clearUserSession


@Composable
fun HomeScreen(navController: NavController) {
    val context: Context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val viewModel: ViewModelHome = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshAPITrigger by remember { mutableStateOf(0) }


    var assignedByMeList by remember { mutableStateOf<List<TaskStatusWiseCountDetails>>(emptyList()) }
    var assignedToMeList by remember { mutableStateOf<List<TaskStatusWiseCountDetails>>(emptyList()) }
    var statusList by remember { mutableStateOf<List<StatusDetails>>(emptyList()) }
    var kpiList by remember { mutableStateOf<List<KpiDetails>>(emptyList()) }

    val user = remember { mutableStateOf(getUserFromSession(context)) }
    val userId = user.value?.userId.toString()
    var profileImage by remember { mutableStateOf(user.value?.userImage ?: "") }

    LaunchedEffect(refreshAPITrigger) {
        viewModel.getCountOfTaskListAssignedByMe(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    assignedByMeList = it.data ?: emptyList()
                } else {
                    errorMessage = it.message
                }
            }
        }
        viewModel.getCountOfTaskListAssignedToMe(MyData(userId)) { response ->
            isLoading = false
            response?.let {
                if (it.statusCode == 200) {
                    assignedToMeList = it.data ?: emptyList()
                } else {
                    errorMessage = it.message
                }
            }
        }
        viewModel.getStatusList() { response ->
            response?.let {
                if (it.statusCode == 200) {
//                    statusList = it.data ?: emptyList()
                    setSessionStatusDetails(context, it.data)
                } else {
                    errorMessage = it.message
                    ShowToast(context,it.message)
                }
            }
        }
        viewModel.getKpiList() { response ->
            response?.let {
                if (it.statusCode == 200) {
//                    kpiList = it.data ?: emptyList()
                    setSessionKpiDetails(context, it.data)
                } else {
                    errorMessage = it.message
                    ShowToast(context,it.message)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text("MyTaskManager") }, navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            })
        },
        drawerContent = {
            DrawerContent(navController,context)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF6200EE))
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMessage!!, fontSize = 18.sp, color = Color.Red)
                    }
                }

                assignedByMeList.isEmpty() && assignedToMeList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tasks available", fontSize = 18.sp, color = Color.Gray)
                    }
                }

                else -> {

                    val totalAssignedByMeList = assignedByMeList.sumOf { it.totalCount }
                    val totalAssignedToMeList = assignedToMeList.sumOf { it.totalCount }
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        item {

                            SectionTitle(text = "Tasks assigned by me:  ($totalAssignedByMeList)")
                            TaskStatusGrid2(assignedByMeList)

                            Spacer(modifier = Modifier.height(16.dp))
                            SectionTitle(text = "Tasks assigned to me:  ($totalAssignedToMeList)")
                            TaskStatusGrid2(assignedToMeList)

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskStatusGrid2(items: List<TaskStatusWiseCountDetails>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Calculate number of rows needed based on 2 items per row
        val rows = (items.size + 1) / 2

        for (i in 0 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (j in 0 until 2) {
                    val index = i * 2 + j
                    if (index < items.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            TaskStatusCard2(
                                title = items[index].statusName,
                                count = items[index].totalCount.toString(),

                                )
                        }
                    } else {
                        // Empty space to maintain grid alignment
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.015).em,
        color = Color(0xFF101618),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun TaskStatusCard2(title: String, count: String) {
    val statusColor = setStatusColor(title)  // Get the color based on status
    val icon: Painter = setStatusIcon(title) // Get the icon based on status

    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .border(
                width = 1.dp,
                color = statusColor,  // Use status color for border
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Icon with status color as tint
            Icon(
                painter = icon,
                contentDescription = null,
                tint = statusColor,  // Use status color here
                modifier = Modifier.size(24.dp)
            )

            // Text with status color
            Text(
                text = "$title: $count",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor, // Use status color here
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun DrawerMenuItem(item: MenuItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(item.route) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(text = item.title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DrawerContent(navController: NavController, context: Context) {
    val user = remember { mutableStateOf(getUserFromSession(context)) }
    var profileImage by remember { mutableStateOf(user.value?.userImage ?: "") }
    var name by remember { mutableStateOf(user.value?.name ?: "") }
    var email by remember { mutableStateOf(user.value?.email ?: "") }

    val appVersion = BuildConfig.VERSION_NAME // Dynamically fetch version from build.gradle

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // **Profile Section**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = if (profileImage.isNotEmpty()) {
                        rememberAsyncImagePainter(Constant.serverImagePath + profileImage)
                    } else {
                        painterResource(id = R.drawable.outline_person_24)
                    },
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .border(BorderStroke(2.dp, Color.Gray), CircleShape)
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(email, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))

            // **Navigation Menu Items**
            val menuItems = listOf(
                MenuItem("Profile", R.drawable.outline_person_24, "profileScreen"),
                MenuItem("Share", R.drawable.baseline_share_24, "shareScreen"),
                MenuItem("Terms & Conditions", R.drawable.baseline_rule_24, "termsScreen"),
                MenuItem("Privacy Policy", R.drawable.baseline_privacy_tip_24, "privacyScreen"),
            )

            menuItems.forEach { item ->
                DrawerMenuItem(item, navController)
            }
        }

        // **Logout Button + Divider + Version Text**
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = {
                    clearUserSession(context)
                    ShowToast(context, "Logged out successfully")
                    navController.navigate("loginScreen")
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEC5353)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = "Logout",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", color = Color.White)
            }

            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 1.dp))

            // **Version Code (Dynamically Fetched)**
            Text(
                "Version: $appVersion",  // Dynamically fetched version
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
