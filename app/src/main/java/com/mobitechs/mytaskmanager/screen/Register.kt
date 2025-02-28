package com.mobitechs.mytaskmanager.screen


import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                DelegateScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DelegateScreen() {
    // Define colors to match the original design
    val primaryTextColor = Color(0xFF101618)
    val secondaryTextColor = Color(0xFF5E7F8D)
    val backgroundColor = Color.White
    val surfaceColor = Color(0xFFF0F3F5)
    val accentColor = Color(0xFF00B7FF)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Assigned Tasks",
                        fontWeight = FontWeight.Bold,
                        color = primaryTextColor
                    )
                },
                actions = {
                    IconButton(onClick = { /* Handle settings */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = primaryTextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = primaryTextColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = accentColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
                    tint = primaryTextColor
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Search bar
            item {
                SearchBar()
            }

            // Filter buttons
            item {
                FilterButtons()
            }

            // Filter chips
            item {
                FilterChips()
            }

            // Task cards
            items(3) {
                TaskCard()
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    val textFieldValue = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = textFieldValue.value,
            onValueChange = { textFieldValue.value = it },
            placeholder = { Text("Search tasks", color = Color(0xFF5E7F8D)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF5E7F8D)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFF0F3F5),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF101618)
            ),
            singleLine = true
        )
    }
}

@Composable
fun FilterButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilterButton(text = "Status")
        FilterButton(text = "Date")
        FilterButton(text = "Team")
    }
}

@Composable
fun FilterButton(text: String) {
    Button(
        onClick = { /* Handle filter */ },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF0F3F5),
            contentColor = Color(0xFF101618)
        ),
        contentPadding = PaddingValues(start = 16.dp, end = 8.dp, top = 0.dp, bottom = 0.dp),
        modifier = Modifier.height(32.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(end = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun FilterChips() {
    val filters = listOf(
        "All", "Unassigned", "Assigned to me", "In progress",
        "Overdue", "Due soon", "Custom view", "Add filter"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(filter)
        }
    }
}

@Composable
fun FilterChip(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF0F3F5),
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF101618)
            )
        }
    }
}

@Composable
fun TaskCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Create a new workout plan for the team",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF101618)
                    )

                    Text(
                        text = "Workout plan · Deadline: 12/12/2022 · Team: Design Team · Status: In Progress",
                        fontSize = 14.sp,
                        color = Color(0xFF5E7F8D)
                    )
                }

                Button(
                    onClick = { /* Handle view details */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF0F3F5),
                        contentColor = Color(0xFF101618)
                    ),
                    contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(32.dp)
                ) {
                    Text(
                        text = "View details",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Task image placeholder - would be real image in actual app
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F3F5))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DelegateScreenPreview() {
    MaterialTheme {
        DelegateScreen()
    }
}