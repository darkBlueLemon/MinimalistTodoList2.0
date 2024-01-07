package com.example.minimalisttodolistv2

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.EaseInQuad
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.minimalisttodolistv2.NotificationTitle.Companion.getNotificationTitle
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    viewModel: AddTaskViewModel,
    context: Context
) {
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .padding(start = 35.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(percent = 7))
                        .background(Color.Black)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {}
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 25)
                        ),
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    onClick = {
                        onEvent(TaskEvent.ShowSettingsDialog)
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
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
                    onEvent(TaskEvent.ShowAddTaskDialog)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        }
    ) { padding ->

        if(state.isAddingTask){
            AddTaskDialog(state = state, onEvent = onEvent, viewModel = viewModel, context = LocalContext.current)
        }
        if(state.isChangingSettings){
            ChangeSettingsDialog(state = state, onEvent = onEvent, viewModel = viewModel)
        }

        AnimatedContent(
            targetState = state.tasks,
            transitionSpec = {
                fadeOut(animationSpec = tween(durationMillis = 850, easing = EaseInExpo) )
                             slideIntoContainer(
                                 animationSpec = tween(0, easing = EaseIn),
                                 towards = Up
                             ).with(
                                 slideOutOfContainer(
                                     animationSpec = tween(0, easing = EaseOut),
                                     towards = Down
                                 )
                             )
            },
            label = "",
        ) {
            Text(text = it.toString())

            LazyColumn(
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .background(Color.Black)
                    .padding(5.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.tasks) { task ->
                    // Lottie Animation
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.task_complete_animation))
                    var isPlaying by remember {
                        mutableStateOf(false)
                    }
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        isPlaying = isPlaying,
                        speed = 3f,
                    )

                    // Animate visibility for each item
                    AnimatedVisibility(
//                        visible = true, // Change this to control the visibility
                        visible = !isPlaying, // Change this to control the visibility
//                        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 850, easing = EaseInExpo))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Rounded.Star,
                                tint = if (task.priority == 0) Color.Transparent else if (task.priority == 1) Color(
                                    0xFFFDFD96
                                ) else if (task.priority == 2) Color(0xFFFF964F) else Color(
                                    0xFFFF6961
                                ),
                                contentDescription = "Priority Icon"
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 5.dp)
                            ) {
                                Text(
                                    text = task.taskName,
                                    fontSize = 20.sp,
                                )
                                if (task.note != "") {
                                    Text(
                                        text = task.note,
//                            text = "testing",
                                        fontSize = 12.sp,
                                        color = Color(0x8FFFFFFF)
                                    )
                                }
                                val date =
                                    if (task.date == "") "" else viewModel.convertMillisToDate(task.date.toLong())
                                val time =
                                    if (task.time == "") "" else viewModel.convertMillisToTime(task.time.toLong())
                                if (date != "") {
                                    Text(
                                        text = "$date $time",
                                        fontSize = 12.sp,
                                        color = Color(0x8FFFFFFF)
                                    )
                                }
                            }
                            LottieAnimation(
                                modifier = Modifier
                                    .background(Color.Black)
                                    .size(25.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        onClick = {
                                            isPlaying = true
                                            onEvent(TaskEvent.DeleteTask(task))
//                                            viewModel.cancelNotification(getNotificationTitle(), context = context)
                                            // New
//                                            viewModel.cancelNotification(task.taskName, context = context)
                                        }
                                    ),
                                composition = composition,
                                progress = { progress },

                                )
                        }
                    }
                }
            }
        }
    }
}