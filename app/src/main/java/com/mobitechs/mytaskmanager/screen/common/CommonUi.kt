package com.mobitechs.mytaskmanager.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobitechs.mytaskmanager.util.ShowDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentDestination?.route

    BottomNavigation {
        BottomNavigationItem(icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
//            selected = false,
            selected = currentRoute == "homeScreen",
            onClick = { navController.navigate("homeScreen") })

        BottomNavigationItem(icon = {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Delegate Task"
            )
        },
            label = { Text("Delegate") },
//            selected = false,
            selected = currentRoute == "taskDelegateListScreen",
            onClick = { navController.navigate("taskDelegateListScreen") })

        BottomNavigationItem(icon = {
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = "For Me"
            )
        },
            label = { Text("For Me") },
//            selected = false,
            selected = currentRoute == "taskForMeScreen",
            onClick = { navController.navigate("taskForMeScreen") })

        BottomNavigationItem(icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "My Team"
            )
        },
            label = { Text("My Team") },
//            selected = false,
            selected = currentRoute == "teamLisScreen",
            onClick = { navController.navigate("teamLisScreen") })
    }
}



@Composable
fun DatePickerButton(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current

    androidx.compose.material3.Button(
        onClick = {
            ShowDatePicker(
                context = context,
                onDateSelected = { year, month, dayOfMonth ->
                    val newCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(newCalendar.time)
                    onDateSelected(formattedDate)
                }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFFE2E1E8))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height - strokeWidth / 2),
                    end = Offset(size.width, size.height - strokeWidth / 2),
                    strokeWidth = strokeWidth
                )
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
        shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
    ) {
        Text(
            text = selectedDate.ifEmpty { "Select Date" }, // Show Placeholder if No Date Selected
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    context: Context
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { androidx.compose.material3.Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { androidx.compose.material3.Text(text = option) },
                    onClick = {
                        onOptionSelected(option) // Pass selected value to parent
                        isExpanded = false
//                        ShowToast(context, "$label: $option")
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String = "Confirm",
    cancelButtonText: String? = "Cancel", // Nullable to support single button
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null, // Nullable to support single button
    isDialogVisible: Boolean,
    onDismiss: () -> Unit
) {
    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { androidx.compose.material3.Text(text = title) },
            text = { androidx.compose.material3.Text(text = message) },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    androidx.compose.material3.Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                cancelButtonText?.let { text ->
                    TextButton(onClick = {
                        onCancel?.invoke()
                        onDismiss()
                    }) {
                        androidx.compose.material3.Text(text = text)
                    }
                }
            }
        )
    }
}