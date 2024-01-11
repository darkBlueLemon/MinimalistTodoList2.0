package com.example.minimalisttodolistv2

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChangeSettingsDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTaskViewModel
) {
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
                .size(width = 350.dp, height = 400.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(percent = 7)
                ),
//            contentAlignment = Alignment.Center
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
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        onEvent(TaskEvent.HideSettingsDialog)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Settings",
                            tint = Color.White
                        )
                    }
                    Text(
                        modifier = Modifier.padding(10.dp),
                        color = Color.White,
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
                        .fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Theme & Font",
                            color = Color.White,
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                    item {
                        Text(
                            text = "Sound",
                            color = Color.White,
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                    item {
                        Text(
                            text = "Language",
                            color = Color.White,
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                    item {
                        Text(
                            text = "Sorting Option",
                            color = Color.White,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    isSortingOptionEnabled = true
                                },
                        )
                    }
                }

                // Sorting Dialog
                if(isSortingOptionEnabled) {
                    AlertDialog(
                        modifier = Modifier,
                        onDismissRequest = {
                            Log.d("MYTAG","false")
                            onEvent(TaskEvent.HideSettingsDialog)
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
                                .padding(16.dp)
                            ,
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Sorting Option",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .align(Alignment.CenterHorizontally)
                                    ,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(TaskEvent.SortTasks(SortType.PRIORITY))
                                            PreferencesManager.sortingOrder = SortType.PRIORITY.toString()
                                            onEvent(TaskEvent.HideSettingsDialog)
                                        }
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Priority",
                                        modifier = Modifier.padding(10.dp),
                                        color = Color.White
                                    )
                                    if(state.sortType.name == "PRIORITY"){
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = ""
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(TaskEvent.SortTasks(SortType.REMAINING_TIME))
                                            PreferencesManager.sortingOrder = SortType.REMAINING_TIME.toString()
                                            onEvent(TaskEvent.HideSettingsDialog)
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
                                    if(state.sortType.name == "REMAINING_TIME"){
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = ""
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(TaskEvent.SortTasks(SortType.ALPHABETICAL))
                                            PreferencesManager.sortingOrder = SortType.ALPHABETICAL.toString()
                                            onEvent(TaskEvent.HideSettingsDialog)
                                        }
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Alphabetical",
                                        modifier = Modifier.padding(10.dp),
                                        color = Color.White
                                    )
                                    if(state.sortType.name == "ALPHABETICAL"){
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = ""
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            onEvent(TaskEvent.SortTasks(SortType.ALPHABETICAL_REV))
                                            PreferencesManager.sortingOrder = SortType.ALPHABETICAL_REV.toString()
                                            onEvent(TaskEvent.HideSettingsDialog)
                                        }
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Alphabetical z-a",
                                        modifier = Modifier.padding(10.dp),
                                        color = Color.White
                                    )
                                    if(state.sortType.name == "ALPHABETICAL_REV"){
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = ""
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
}