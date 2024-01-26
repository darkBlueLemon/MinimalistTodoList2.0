package com.minimalisttodolist.pleasebethelastrecyclerview

import android.app.NotificationManager
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    viewModel: AddTaskViewModel,
    context: Context,
    notificationManager: NotificationManager
) {
    val interactionSource = remember { MutableInteractionSource() }

    val customFontWeight = if(PreferencesManager.thinFont) FontWeight.Light else FontWeight.Normal
    val themeColor = Color.Black
    val textColor = Color.White

    Scaffold(
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .padding(start = 35.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(percent = 7))
                        .background(Color.Black)
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 25)
                        ),
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add Task",
                        modifier = Modifier
                            .combinedClickable(
                                onLongClick = {
                                    onEvent(TaskEvent.ShowSettingsDialog)
                                }, onClick = {
                                    onEvent(TaskEvent.ShowAddTaskDialog)
                                },
                                interactionSource = interactionSource,
                                indication = null,
                            )
                    )
                }
            }
        }
    ) { padding ->

        if(state.isAddingTask){
            AddTaskDialog(state = state, onEvent = onEvent, viewModel = viewModel, context = LocalContext.current)
        }
        if(state.isChangingSettings){
            AnimatedVisibility(visible = state.isChangingSettings) {
                ChangeSettingsDialog(
                    state = state,
                    onEvent = onEvent,
                    viewModel = viewModel,
//                    modifier = Modifier.animateEnterExit(
//                        enter = fadeIn(tween(5000)),
//                        exit =  fadeOut(animationSpec = tween(500))
//                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .background(Color.Magenta)
                .background(themeColor)
                .padding(start = 5.dp, end = 15.dp, bottom = 15.dp, top = 15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.tasks) { task ->
                Row(
                    modifier = Modifier
//                                .background(themeColor)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                    ) {
                        Row (
                            modifier = Modifier
//                                        .background(themeColor)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row (
//                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if(PreferencesManager.starIcon) {
                                    Icon(
                                        modifier = Modifier
                                            .size(18.dp)
//                                                    .background(themeColor)
                                        ,
                                        imageVector = Icons.Rounded.Star,
                                        tint = when (task.priority) {
                                            0 -> Color.Transparent
                                            1 -> Color(
                                                0xFFFDFD96
                                            )
                                            2 -> Color(0xFFFF964F)
                                            else -> Color(
                                                0xFFFF5147
                                            )
                                        },
                                        contentDescription = "Priority Icon"
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.task_priority_selected_icon3),
                                        tint = when (task.priority) {
                                            0 -> Color.Transparent
                                            1 -> Color(
                                                0xFFFDFD96
                                            )
                                            2 -> Color(0xFFFF964F)
                                            else -> Color(
                                                0xFFFF5147
                                            )
                                        },
                                        contentDescription = "Priority Icon",
                                        modifier = Modifier
                                            .padding(end = 2.dp)
                                    )
                                }
                                Text(
                                    text = task.taskName,
                                    fontSize = 20.sp,
                                    fontWeight = customFontWeight,
                                    maxLines = 1, // Set the desired number of lines
                                    softWrap = true,
                                    overflow = TextOverflow.Visible,
//                                    modifier = Modifier.padding(start = 5.dp).background(Color.Magenta).size(width = 280.dp, height = 20.dp),
                                    modifier = Modifier.padding(start = 5.dp),
                                    color = textColor,
                                )
                            }

                            // Lottie Animation
                            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.task_complete_animation))
                            AnimatedContent(
                                targetState = state.tasks,
                                label = "complete task",
                                transitionSpec = {
                                    slideIntoContainer(
                                        animationSpec = tween(0),
                                        towards = Up
                                    ).togetherWith(
                                        slideOutOfContainer(
                                            animationSpec = tween(0),
                                            towards = Down
                                        )
                                    )
                                },
//                                modifier = Modifier.weight(-1f)
                            ) {
                                it
                                var isPlaying by remember {
                                    mutableStateOf(false)
                                }
                                val progress by animateLottieCompositionAsState(
                                    composition = composition,
                                    isPlaying = isPlaying,
                                    speed = 8f,
                                )
                                LottieAnimation(
                                    modifier = Modifier
//                                            .background(themeColor)
                                        .size(25.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                            onClick = {
                                                isPlaying = true
                                                onEvent(TaskEvent.DeleteTask(task))
                                                // New
                                                viewModel.cancelNotification(
                                                    task.taskName,
                                                    context = context
                                                )
                                                notificationManager.cancelAll();
                                            }
                                        ),
                                    composition = composition,
                                    progress = { progress }
                                )
                            }
                        }
                        var dateColor = Color.Blue
                        if(task.date != "") {
                            dateColor = if (isDateInThePast(task.date.toLong())) Color(0xFFCB273C) else Color(0x8FFFFFFF)
                        }
                        val date =
                            if (task.date == "") "" else viewModel.convertMillisToDate(task.date.toLong())
                        val time =
                            if (task.time == "") "" else viewModel.convertMillisToTime(task.time.toLong())
                        if (date != "") {
                            val humanFormatDate = convertMillisToDate(task.date.toLong())
                            Row (
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Spacer(
                                    modifier = Modifier.size(
                                        width = if(PreferencesManager.starIcon) 24.dp else 18.dp,
                                        height = 0.dp
                                    )
                                )
                                val dateTimeText = if (isDateToday(task.date.toLong())) "Today $time"  else if(isDateTomorrow(task.date.toLong())) "Tomorrow $time" else if(isDateYesterday(task.date.toLong())) "Yesterday $time" else "$humanFormatDate $time"
                                val noteText = if(task.note == "") "" else if(task.time != "" ) " | " + task.note else "| " + task.note
                                Row (
                                    modifier = Modifier
                                ){
                                    Text(
                                        text = dateTimeText,
                                        fontSize = 12.sp,
                                        color = dateColor,
                                        fontWeight = FontWeight.W300,
                                    )
                                    Text(
                                        text = noteText,
                                        fontSize = 12.sp,
                                        color = Color(0x8FFFFFFF),
                                        fontWeight = FontWeight.W300,
                                    )
                                }
                            }
                        } else {
                            if(task.note == "") {
                                Text(text = "", fontSize = 5.sp)
                            }
                            else {
                                Row (
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Spacer(
                                        modifier = Modifier.size(
                                            width = if (PreferencesManager.starIcon) 24.dp else 18.dp,
                                            height = 0.dp
                                        )
                                    )
                                    val noteText = task.note
                                    Text(
                                        text = noteText,
                                        fontSize = 12.sp,
                                        color = Color(0x8FFFFFFF),
                                        fontWeight = FontWeight.W300,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun isDateToday(dateInMillis: Long): Boolean {
    val calendar = Calendar.getInstance()
    val today = calendar.timeInMillis

    val taskCalendar = Calendar.getInstance()
    taskCalendar.timeInMillis = dateInMillis

    return taskCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            taskCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
}
fun isDateYesterday(dateInMillis: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1) // Move the calendar one day back to yesterday

    val yesterday = calendar.timeInMillis

    val taskCalendar = Calendar.getInstance()
    taskCalendar.timeInMillis = dateInMillis

    return taskCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            taskCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
}
fun isDateTomorrow(dateInMillis: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1) // Move the calendar one day forward to tomorrow

    val tomorrow = calendar.timeInMillis

    val taskCalendar = Calendar.getInstance()
    taskCalendar.timeInMillis = dateInMillis

    return taskCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            taskCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
}
fun isDateInThePast(dateInMillis: Long): Boolean {
    val calendar = Calendar.getInstance()
    val today = calendar.timeInMillis

    val taskCalendar = Calendar.getInstance()
    taskCalendar.timeInMillis = dateInMillis

    if(taskCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
        taskCalendar.get(Calendar.DAY_OF_YEAR) <= calendar.get(Calendar.DAY_OF_YEAR)) return true


    if(taskCalendar.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)) return true

    return taskCalendar.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)
}

fun convertMillisToDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    val date = Date(millis)

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.timeInMillis = millis
    val year = calendar.get(Calendar.YEAR)

    val formattedDate = dateFormat.format(date)

    return if (year == currentYear) {
        formattedDate
    } else {
        "$formattedDate, $year"
    }
}
