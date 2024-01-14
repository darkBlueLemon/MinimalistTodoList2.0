package com.example.minimalisttodolistv2

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInSine
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
fun ChangeSettingsDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTaskViewModel
) {
    var isBoringNotificationEnabled by remember {
        mutableStateOf(PreferencesManager.boringNotification)
    }
    var isThinFontEnabled by remember {
        mutableStateOf(PreferencesManager.thinFont)
    }
    var isStarEnabled by remember {
        mutableStateOf(PreferencesManager.starIcon)
    }

    val interactionSource = remember { MutableInteractionSource() }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(TaskEvent.HideSettingsDialog)
        }
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(percent = 7))
                .background(Color.Black)
                .size(width = 350.dp, height = 380.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(percent = 7)
                ),
        ) {
            Column (
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Settings",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onEvent(TaskEvent.HideSettingsDialog)
                            }
                    )
                    Text(
                        color = Color.White,
                        fontSize = 20.sp,
                        text = "Settings",
                    )
                    IconButton(onClick = {
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Settings",
                            tint = Color.Transparent
                        )
                    }
                }

                var isSortingOptionEnabled by remember {
                    mutableStateOf(false)
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Sorting Option",
                            color = Color.White,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    isSortingOptionEnabled = true
                                }
                                .fillMaxWidth(),
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    PreferencesManager.boringNotification =
                                        !PreferencesManager.boringNotification
                                    isBoringNotificationEnabled =
                                        PreferencesManager.boringNotification
                                }
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Boring Notifications",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(10.dp)
//                                    .clickable {
//                                        PreferencesManager.boringNotification = !PreferencesManager.boringNotification
//                                    },
                            )
                            AnimatedVisibility(visible = isBoringNotificationEnabled) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    tint = Color.White,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    PreferencesManager.thinFont = !PreferencesManager.thinFont
                                    isThinFontEnabled = PreferencesManager.thinFont
                                }
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Thin Font",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                            AnimatedVisibility(visible = isThinFontEnabled) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    tint = Color.White,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    PreferencesManager.thinFont = !PreferencesManager.thinFont
                                    isThinFontEnabled = PreferencesManager.thinFont
                                }
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Priority Icon",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(10.dp)
                            )

                            Spacer(modifier = Modifier
                                .size(width = 2.dp, height = 0.dp)
                                .weight(1f))
                            if(isStarEnabled) {
                                Icon(
                                    painter = painterResource(R.drawable.task_priority_selected_icon),
                                    tint = Color.White,
                                    contentDescription = "Close Settings",
                                    modifier = Modifier.clickable {
                                        PreferencesManager.starIcon = true
                                        isStarEnabled = PreferencesManager.starIcon
                                    }
                                )
                                Spacer(modifier = Modifier.size(width = 15.dp, height = 0.dp))
                                Icon(
                                    painter = painterResource(R.drawable.task_priority_unselected_icon2),
                                    tint = Color.White,
                                    contentDescription = "Close Settings",
                                    modifier = Modifier.clickable {
                                        PreferencesManager.starIcon = false
                                        isStarEnabled = PreferencesManager.starIcon
                                    }
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.task_priority_unselected_icon),
                                    tint = Color.White,
                                    contentDescription = "Close Settings",
                                    modifier = Modifier.clickable {
                                        PreferencesManager.starIcon = true
                                        isStarEnabled = PreferencesManager.starIcon
                                    }
                                )
                                Spacer(modifier = Modifier.size(width = 15.dp, height = 0.dp))
                                Icon(
                                    painter = painterResource(R.drawable.task_priority_selected_icon2),
                                    tint = Color.White,
                                    contentDescription = "Close Settings",
                                    modifier = Modifier.clickable {
                                        PreferencesManager.starIcon = false
                                        isStarEnabled = PreferencesManager.starIcon
                                    }
                                )
                            }
//                            AnimatedVisibility(visible = isThinFontEnabled) {
//                                Icon(
//                                    imageVector = Icons.Default.Check,
//                                    contentDescription = ""
//                                )
//                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Theme & Font",
                                color = Color.White,
                                modifier = Modifier.padding(10.dp),
                            )
                            Text(
                                text = "work in progress",
                                fontStyle = FontStyle.Italic,
                                color = Color(0x4FFFFFFF),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(10.dp),
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Language",
                                color = Color.White,
                                modifier = Modifier.padding(10.dp),
                            )
                            Text(
                                text = "work in progress",
                                fontStyle = FontStyle.Italic,
                                color = Color(0x4FFFFFFF),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(10.dp),
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Widgets",
                                color = Color.White,
                                modifier = Modifier.padding(10.dp),
                            )
                            Text(
                                text = "work in progress",
                                fontStyle = FontStyle.Italic,
                                color = Color(0x4FFFFFFF),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(10.dp),
                            )
                        }
                    }
                }

                // Sorting Dialog
                AnimatedVisibility(
                    visible = isSortingOptionEnabled,
                ) {
//                    if (isSortingOptionEnabled) {
                        AlertDialog(
                            modifier = Modifier.animateEnterExit(
                                enter = fadeIn(animationSpec = tween(500)),
//                                exit =  fadeOut(animationSpec = tween(500))
                                ),
                            onDismissRequest = {
                                Log.d("MYTAG", "false")
//                                onEvent(TaskEvent.HideSettingsDialog)
                                isSortingOptionEnabled = false
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(percent = 7))
                                    .background(Color.Black)
//                                    .size(width = 350.dp, height = 400.dp)
                                    .border(
                                        width = 2.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(percent = 7)
                                    )
                                    .padding(16.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Sorting Option",
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .align(Alignment.CenterHorizontally),
                                        color = Color.White,
                                        fontSize = 20.sp
                                    )

                                    Row(
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                onEvent(TaskEvent.SortTasks(SortType.PRIORITY))
                                                PreferencesManager.sortingOrder =
                                                    SortType.PRIORITY.toString()
//                                                onEvent(TaskEvent.HideSettingsDialog)
                                            }
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Priority",
                                            modifier = Modifier.padding(10.dp),
                                            color = Color.White
                                        )
                                        AnimatedVisibility(visible = state.sortType.name == "PRIORITY") {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                tint = Color.White,
                                                contentDescription = ""
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                onEvent(TaskEvent.SortTasks(SortType.REMAINING_TIME))
                                                PreferencesManager.sortingOrder =
                                                    SortType.REMAINING_TIME.toString()
//                                                onEvent(TaskEvent.HideSettingsDialog)
                                            }
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Remaining Time",
                                            modifier = Modifier.padding(10.dp),
                                            color = Color.White
                                        )
                                        Log.d("MYTAG", state.sortType.name)
                                        AnimatedVisibility(visible = state.sortType.name == "REMAINING_TIME") {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                tint = Color.White,
                                                contentDescription = ""
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                onEvent(TaskEvent.SortTasks(SortType.ALPHABETICAL))
                                                PreferencesManager.sortingOrder =
                                                    SortType.ALPHABETICAL.toString()
//                                                onEvent(TaskEvent.HideSettingsDialog)
                                            }
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Alphabetical",
                                            modifier = Modifier.padding(10.dp),
                                            color = Color.White
                                        )
                                        AnimatedVisibility(visible = state.sortType.name == "ALPHABETICAL") {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                tint = Color.White,
                                                contentDescription = ""
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                onEvent(TaskEvent.SortTasks(SortType.ALPHABETICAL_REV))
                                                PreferencesManager.sortingOrder =
                                                    SortType.ALPHABETICAL_REV.toString()
//                                                onEvent(TaskEvent.HideSettingsDialog)
                                            }
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Alphabetical z-a",
                                            modifier = Modifier.padding(10.dp),
                                            color = Color.White
                                        )
                                        AnimatedVisibility(visible = state.sortType.name == "ALPHABETICAL_REV") {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                tint = Color.White,
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                }
                            }
                        }
//                    }
                }
            }
        }
    }
}