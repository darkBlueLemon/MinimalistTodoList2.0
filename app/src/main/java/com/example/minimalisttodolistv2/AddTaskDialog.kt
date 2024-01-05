package com.example.minimalisttodolistv2

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minimalisttodolistv2.NotificationTitle.Companion.getNotificationTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTaskViewModel,
    context: Context
) {

    // Clear date and time if the task wasn't added
    DisposableEffect(key1 = true){
        onDispose {
            viewModel.setDate("")
            viewModel.setTime(0,0)
        }
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
                .size(width = 350.dp, height = 400.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(percent = 7)
                ),
//            contentAlignment = Alignment.Center
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val transparencyColor = Color(0x4FFFFFFF)
            val customFontWeight = FontWeight.Light
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
//                        modifier = Modifier
//                            .background(Color.Black)
//                            .padding(15.dp)
//                            .fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(2.dp)
                        verticalArrangement = Arrangement.Top
                    ) {
                        TextField(
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
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontWeight = customFontWeight,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = true,
                            ),
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
                            ),
                        )
                        ReadonlyTextField(
                            value = if (viewModel.date != "") viewModel.convertMillisToDate(
                                viewModel.date.toLong()
                            ) else viewModel.date,
//                            value = viewModel.date,
                            onValueChange = {},
                            onClick = {
                                viewModel.toggleDatePicker()
                            },
                            transparencyColor = transparencyColor,
                            customFontWeight = customFontWeight,
                            text = "Add date"
                        )
                        if (viewModel.isDatePickerEnabled) DatePicker(viewModel) {
                            Log.e("MYTAG", "datepicker closed")
                            viewModel.toggleDatePicker()
                            onEvent(TaskEvent.SetDate(viewModel.date))
                        }
                        val selectedTime = viewModel.convertMillisToTime(viewModel.time.toLong())
                        if(viewModel.date != "") {
                            ReadonlyTextField(
                                value = if (selectedTime == "00:00") "" else selectedTime,
//                            value = viewModel.time,
                                onValueChange = {},
                                onClick = {
                                    viewModel.toggleTimePicker()
                                },
                                transparencyColor = transparencyColor,
                                customFontWeight = customFontWeight,
                                text = "Add time"
                            )
                        }
                        if (viewModel.isTimePickerEnabled) TimePicker(viewModel) {
                            onEvent(TaskEvent.SetTime(viewModel.time))
                        }

                        // Priority Selection
                        var isPrioritySelectionEnabled by remember {
                            mutableStateOf(false)
                        }

                        AnimatedContent(
                            targetState = isPrioritySelectionEnabled,
                            label = ""
                        ) { isEnabled ->
                            if (isEnabled) {
                                var prioritySelected by remember {
                                    mutableIntStateOf(1)
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = if (prioritySelected == 3) R.drawable.task_priority_selected_icon else R.drawable.task_priority_unselected_icon),
                                        tint = Color(0xFFFF6961),
                                        contentDescription = "Close Settings",
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = {
                                                    prioritySelected = 3
                                                    onEvent(TaskEvent.SetPriority(prioritySelected))
                                                }
                                            )
                                    )
                                    Icon(
                                        painter = painterResource(id = if (prioritySelected == 2) R.drawable.task_priority_selected_icon else R.drawable.task_priority_unselected_icon),
                                        tint = Color(0xFFFF964F),
                                        contentDescription = "Close Settings",
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = {
                                                    prioritySelected = 2
                                                    onEvent(TaskEvent.SetPriority(prioritySelected))
                                                }
                                            )
                                    )
                                    Icon(
                                        painter = painterResource(id = if (prioritySelected == 1) R.drawable.task_priority_selected_icon else R.drawable.task_priority_unselected_icon),
                                        tint = Color(0xFFFDFD96),
                                        contentDescription = "Close Settings",
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                                onClick = {
                                                    prioritySelected = 1
                                                    onEvent(TaskEvent.SetPriority(prioritySelected))
                                                }
                                            )
                                    )
                                }
                            } else {
                                Text(
                                    text = "Add priority",
                                    color = transparencyColor,
                                    fontWeight = customFontWeight,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                            onClick = {
                                                isPrioritySelectionEnabled =
                                                    !isPrioritySelectionEnabled
                                            }
                                        )
                                        .padding(15.dp)
                                )
                            }
                        }
                    }
                }

                // Save Button
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onEvent(TaskEvent.SaveTask)
                                // first place where alarm scheduler is called
                                if (state.date.isNotBlank()) viewModel.callNotificationScheduler(
//                                    state.taskName,
                                    getNotificationTitle(),
                                    state.note,
                                    context
                                )
                                viewModel.setDate("")
                            }
                        )
                        .align(Alignment.CenterHorizontally)
//                        .clip(shape = RoundedCornerShape(percent = 7))
                        .background(Color.Black)
//                        .size(width = 350.dp, height = 600.dp)
                        .size(width = 100.dp, height = 40.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 50)
                        )
//                        .padding(20.dp)
                    ,
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