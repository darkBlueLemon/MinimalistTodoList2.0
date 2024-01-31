package com.minimalisttodolist.pleasebethelastrecyclerview

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.minimalisttodolist.pleasebethelastrecyclerview.NotificationTitle.Companion.getNotificationTitle
import kotlinx.coroutines.delay
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddTaskDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTaskViewModel,
    context: Context
) {

    val textColor = Color.White

    // Clear date and time if the task wasn't added
    DisposableEffect(key1 = true){
        onDispose {
            viewModel.setDate("")
            viewModel.setTime(0,0)
        }
    }

    // Hide Keyboard part 1
    val controller = LocalSoftwareKeyboardController.current
    controller?.show()

    // Show Keyboard
    val showKeyboard = remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

// LaunchedEffect prevents endless focus request
    LaunchedEffect(focusRequester) {
        delay(100) // Make sure you have delay here
        focusRequester.requestFocus()
        keyboard?.show()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(TaskEvent.HideAddTaskDialog)
        }
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(percent = 7))
                .background(Color.Black)
                .size(width = 350.dp, height = 360.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(percent = 7)
                ),
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val transparencyColor = Color(0x4FFFFFFF)
            val customFontWeight = FontWeight.Light
            val customFontWeightText = if(PreferencesManager.thinFont) FontWeight.Light else FontWeight.Normal
            Column(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(15.dp)
                    .fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(2.dp)
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        TextField(
                            modifier = Modifier.focusRequester(focusRequester),
                            value = state.taskName,
                            onValueChange = {
                                onEvent(TaskEvent.SetTaskName(it))
                            },
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "I want to ...",
                                    color = transparencyColor,
                                    fontWeight = customFontWeight,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedTextColor = textColor,
                                focusedTextColor = textColor,

                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontWeight = customFontWeightText,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            ),
                        )

                        // Priority Selection
                        var isPrioritySelectionEnabled by remember {
                            mutableStateOf(false)
                        }

                        val selected_icon = if(PreferencesManager.starIcon) {
                            R.drawable.task_priority_selected_icon
                        } else {
                            R.drawable.task_priority_selected_icon2
                        }
                        val unSelected_icon = if(PreferencesManager.starIcon) {
                            R.drawable.task_priority_unselected_icon
                        } else {
                            R.drawable.task_priority_unselected_icon2
                        }

                        AnimatedContent(
                            targetState = isPrioritySelectionEnabled,
                            label = ""
                        ) { isEnabled ->
                            if (isEnabled) {
                                var prioritySelected by remember {
                                    mutableIntStateOf(1)
                                }
                                onEvent(TaskEvent.SetPriority(prioritySelected))
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = if (prioritySelected == 3) selected_icon else unSelected_icon),
                                        tint = Color(0xFFFF5147),
                                        contentDescription = "priority",
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = {
                                                    prioritySelected = 3
                                                    onEvent(TaskEvent.SetPriority(prioritySelected))
                                                }
                                            )
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 4.dp, end = 4.dp)
                                    )
                                    Icon(
                                        painter = painterResource(id = if (prioritySelected == 2) selected_icon else unSelected_icon),
                                        tint = Color(0xFFFF964F),
                                        contentDescription = "priority_",
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = {
                                                    prioritySelected = 2
                                                    onEvent(TaskEvent.SetPriority(prioritySelected))
                                                }
                                            )
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 4.dp, end = 4.dp)
                                    )
                                    Icon(
                                        painter = painterResource(id = if (prioritySelected == 1) selected_icon else unSelected_icon),
                                        tint = Color(0xFFFDFD96),
                                        contentDescription = "priority2",
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = {
                                                    prioritySelected = 1
                                                    onEvent(TaskEvent.SetPriority(prioritySelected))
                                                }
                                            )
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 4.dp, end = 4.dp)
                                    )
                                }
                            } else {
                                Row (
                                    modifier = Modifier
                                        .background(Color.Black)
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                            onClick = {
                                                isPrioritySelectionEnabled =
                                                    !isPrioritySelectionEnabled
                                            }
                                        )
                                        .padding(start = 13.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.task_priority_selected_icon_small),
                                        tint = transparencyColor,
                                        contentDescription = "priority3",
                                    )
                                    Text(
                                        text = "Add priority",
                                        color = transparencyColor,
                                        fontWeight = customFontWeight,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier
                                            .padding(15.dp)
                                    )
                                }
                            }
                        }
                        Row (
                            modifier = Modifier
                                .background(Color.Black)
                                .padding(start = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                painter = painterResource(R.drawable.note_icon),
                                tint = transparencyColor,
                                contentDescription = "Close Settings",
                            )
                            TextField(
                                value = state.note,
                                onValueChange = {
                                    onEvent(TaskEvent.SetNote(it))
                                },
                                placeholder = {
                                    Text(
                                        text = "Add note",
                                        color = transparencyColor,
                                        fontWeight = customFontWeight,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    unfocusedTextColor = textColor,
                                    focusedTextColor = textColor,
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontWeight = customFontWeightText,
                                ),
                                singleLine = true,
                                modifier = Modifier.background(Color.Black)
                            )
                        }
                        Row (
                            modifier = Modifier
                                .background(Color.Black)
                                .padding(start = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.date_icon),
                                tint = transparencyColor,
                                contentDescription = "date icon",
                            )
                            ReadonlyTextField(
                                value = if (viewModel.date != "") convertMillisToDate(viewModel.date.toLong()) else viewModel.date,
                                onValueChange = {},
                                onClick = {
                                    viewModel.toggleDatePicker()
                                },
                                transparencyColor = transparencyColor,
                                customFontWeight = customFontWeight,
                                text = "Add date"
                            )
                        }
                        if (viewModel.isDatePickerEnabled) DatePicker(viewModel) {
                            viewModel.toggleDatePicker()
                            onEvent(TaskEvent.SetDate(viewModel.date))
                        }
                        val selectedTime = viewModel.convertMillisToTime(viewModel.time.toLong())
                        if(viewModel.date != "") {
                            Row (
                                modifier = Modifier
                                    .background(Color.Black)
                                    .padding(start = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.time_icon),
                                    tint = transparencyColor,
                                    contentDescription = "time icon",
                                )
                                ReadonlyTextField(
                                    value = if (selectedTime == "00:00") "" else selectedTime,
                                    onValueChange = {},
                                    onClick = {
                                        viewModel.toggleTimePicker()
                                    },
                                    transparencyColor = transparencyColor,
                                    customFontWeight = customFontWeight,
                                    text = "Add time"
                                )
                            }
                        }
                        if (viewModel.isTimePickerEnabled) TimePicker(viewModel) {
                            onEvent(TaskEvent.SetTime(viewModel.time))
                        }

                    }
                }

                // Notifications
                var hasNotificationPermission by remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    } else mutableStateOf(true)
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        hasNotificationPermission = isGranted
                    }
                )

                // Save Button
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                if (System.currentTimeMillis() + TimeZone
                                        .getDefault()
                                        .getOffset(System.currentTimeMillis()) < viewModel.getTimeAndDateAsMillis()
                                ) {
                                } else if (state.taskName != "") {
                                    viewModel.setDate(
                                        (System.currentTimeMillis() + 60 * 60 * 1000L + TimeZone
                                            .getDefault()
                                            .getOffset(System.currentTimeMillis())).toString()
                                    )
                                }
                                onEvent(TaskEvent.SaveTask)

                                if (state.taskName != "") {
                                    // first place where alarm scheduler is called
                                    if (state.date.isNotBlank()) {
                                        val alarmManager: AlarmManager = (context.getSystemService<AlarmManager>() as AlarmManager?)!!
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                            viewModel.callNotificationScheduler(
                                                getNotificationTitle(),
                                                state.taskName,
                                                context,
                                                state.priority
                                            )

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                if(!hasNotificationPermission) {
                                                    Toast.makeText(context, "Enable Notifications", Toast.LENGTH_SHORT).show()
                                                }
                                            }
//                                            when {
//                                                alarmManager.canScheduleExactAlarms() -> {
//                                                    viewModel.callNotificationScheduler(
//                                                        getNotificationTitle(),
//                                                        state.taskName,
//                                                        context,
//                                                        state.priority
//                                                    )
//                                                }
//                                                else -> {
//                                                    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                                                        Log.d("MYTAG","called permission launcher")
//                                                    }
////                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
////                                                        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
////                                                        if (alarmManager?.canScheduleExactAlarms() == false) {
////                                                            if(PreferencesManager.canDisplayNotificationPermission) {
////                                                                Intent().also { intent ->
////                                                                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
////                                                                    context.startActivity(intent)
////                                                                }
////                                                                val count = PreferencesManager.updateNotificationPermissionCount
////                                                                if(count == 1) PreferencesManager.canDisplayNotificationPermission = false
////                                                                PreferencesManager.updateNotificationPermissionCount = count + 1
////                                                            }
////                                                        }
////                                                    }
//                                                }
//                                            }
                                        }
                                    }
                                    viewModel.setDate("")

                                    // Hide Keyboard part 2
                                    controller?.hide()
                                }
                            }
                        )
                        .align(Alignment.CenterHorizontally)
                        .background(Color.Black)
                        .size(width = 100.dp, height = 40.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 50)
                        ),
                ) {
                    Text(
                        text = "SAVE",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun ReadonlyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    transparencyColor: Color,
    customFontWeight: FontWeight,
    text: String
) {

    Box {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Black,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
            ),
            textStyle = LocalTextStyle.current.copy(
                fontWeight = if(PreferencesManager.thinFont) FontWeight.Light else FontWeight.Normal,
            ),
            placeholder = {
                Text(
                    text = text,
                    color = transparencyColor,
                    fontWeight = customFontWeight,
                    style = MaterialTheme.typography.bodySmall,
                )
            },
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}