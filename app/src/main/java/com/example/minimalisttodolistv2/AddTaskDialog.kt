package com.example.minimalisttodolistv2

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTaskViewModel
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(TaskEvent.HideDialog)
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
            Column(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize(),
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
//                    modifier = Modifier.background(Color.Cyan)
                )
                TextField(
                    value = state.note,
                    onValueChange = {
                        onEvent(TaskEvent.SetNote(it))
                    },
                    placeholder = {
                        Text(
                            text = "Add note",
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
//                    modifier = Modifier.background(Color.Green)
                )
                TextField(
                    value = state.priority.toString(),
                    onValueChange = {
                        onEvent(TaskEvent.SetPriority(it.toInt()))
                    },
                    placeholder = {
                        Text(
                            text = "Set priority",
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
//                    modifier = Modifier.background(Color.Gray)
                )
                ReadonlyTextField(
                    value = viewModel.date,
                    onValueChange = {
                    },
                    onClick = {
                        viewModel.toggleDatePicker()
                    }) {}
                if(viewModel.isDatePickerEnabled) DatePicker(viewModel){
                    Log.e("MYTAG","datepicker closed")
                    viewModel.toggleDatePicker()
                    onEvent(TaskEvent.SetDate(viewModel.date))
                }
                ReadonlyTextField(
                    value = viewModel.time,
                    onValueChange = {
                    },
                    onClick = {
                        viewModel.toggleTimePicker()
                    }) {}
                if(viewModel.isTimePickerEnabled) TimePicker(viewModel){
                    onEvent(TaskEvent.SetTime(viewModel.time))
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        onEvent(TaskEvent.SaveTask)
                    }) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Composable
fun ReadonlyTextField(
//    value: TextFieldValue,
    value: String,
//    onValueChange: (TextFieldValue) -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {

    Box {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = label,
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
                    text = "Add date",
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