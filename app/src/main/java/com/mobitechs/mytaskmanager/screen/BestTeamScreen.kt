package com.mobitechs.mytaskmanager.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

class DesignTeamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DesignTeamScreen()
            }
        }
    }
}

data class TeamMember(
    val name: String,
    val role: String,
    val imageUrl: String,
    val isAdmin: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignTeamScreen() {
    // Colors from the HTML/CSS design
    val primaryColor = Color(0xFF101618)
    val secondaryColor = Color(0xFF5E7F8D)
    val accentColor = Color(0xFF00B7FF)
    val dividerColor = Color(0xFFDAE3E7)
    val backgroundColor = Color.White

    // Sample data
    val members = listOf(
        TeamMember(
            "Lillian",
            "@lillian",
            "https://cdn.usegalileo.ai/sdxl10/5fc925bc-fb6e-4746-92e2-417d466fc75e.png",
            true
        ),
        TeamMember(
            "Danielle",
            "Product Design",
            "https://cdn.usegalileo.ai/sdxl10/c28d3656-a0bf-46ec-a669-7d1695cd6987.png"
        ),
        TeamMember(
            "Alex",
            "Product Design",
            "https://cdn.usegalileo.ai/sdxl10/bbe14333-12fd-4d25-a0c2-18ad8d86d75e.png"
        ),
        TeamMember(
            "Samantha",
            "Product Design",
            "https://cdn.usegalileo.ai/sdxl10/d099e1f2-8e8a-4b3f-b103-7dcde7eb4153.png"
        )
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedNavItem by remember { mutableIntStateOf(2) } // Teams tab selected by default

    val tabs = listOf("Members", "Tasks")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Design Team",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* Edit action */ }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = primaryColor
                            )
                        }
                    },
                    navigationIcon = {
                        // Empty box to balance the layout
                        Box(modifier = Modifier.width(48.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundColor
                    )
                )

                // Team header with image
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https://cdn.usegalileo.ai/sdxl10/266f3fba-d639-4ad8-8a79-b7eb594ec3be.png",
                        contentDescription = "Team Image",
                        modifier = Modifier
                            .size(128.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Design Team",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                        Text(
                            text = "Created by Lillian, 3 members",
                            color = secondaryColor
                        )
                    }
                }

                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    divider = {
                        Divider(color = dividerColor)
                    },
                    containerColor = backgroundColor,
                    contentColor = primaryColor
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            },
                            selectedContentColor = primaryColor,
                            unselectedContentColor = secondaryColor
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column {
                BottomAppBar(
                    containerColor = backgroundColor,
                    contentColor = primaryColor
                ) {
                    NavigationBarItem(
                        selected = selectedNavItem == 0,
                        onClick = { selectedNavItem = 0 },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Inbox,
                                contentDescription = "Inbox"
                            )
                        },
                        label = { Text("Inbox", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            unselectedIconColor = secondaryColor,
                            unselectedTextColor = secondaryColor,
                            indicatorColor = backgroundColor
                        )
                    )

                    NavigationBarItem(
                        selected = selectedNavItem == 1,
                        onClick = { selectedNavItem = 1 },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.FormatListBulleted,
                                contentDescription = "Lists"
                            )
                        },
                        label = { Text("Lists", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            unselectedIconColor = secondaryColor,
                            unselectedTextColor = secondaryColor,
                            indicatorColor = backgroundColor
                        )
                    )

                    NavigationBarItem(
                        selected = selectedNavItem == 2,
                        onClick = { selectedNavItem = 2 },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Group,
                                contentDescription = "Teams"
                            )
                        },
                        label = { Text("Teams", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            unselectedIconColor = secondaryColor,
                            unselectedTextColor = secondaryColor,
                            indicatorColor = backgroundColor
                        )
                    )

                    NavigationBarItem(
                        selected = selectedNavItem == 3,
                        onClick = { selectedNavItem = 3 },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text("Profile", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            unselectedIconColor = secondaryColor,
                            unselectedTextColor = secondaryColor,
                            indicatorColor = backgroundColor
                        )
                    )
                }
                // Extra space at bottom
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .background(backgroundColor)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add action */ },
                containerColor = accentColor,
                contentColor = primaryColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = backgroundColor
        ) {
            Column {
                Text(
                    text = "All Members",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = secondaryColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )

                LazyColumn {
                    items(members) { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = member.imageUrl,
                                    contentDescription = "${member.name} profile picture",
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = member.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = primaryColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = member.role,
                                        fontSize = 14.sp,
                                        color = secondaryColor,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            IconButton(onClick = { /* Add/Remove action */ }) {
                                Icon(
                                    imageVector = if (member.isAdmin) Icons.Default.Add else Icons.Default.Remove,
                                    contentDescription = if (member.isAdmin) "Add" else "Remove",
                                    tint = primaryColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}