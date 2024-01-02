package com.example.minimalisttodolistv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = {
            onEvent(TaskEvent.HideDialog)
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = state.taskName,
                onValueChange = {
                    onEvent(TaskEvent.SetTaskName(it))
                },
                placeholder = {
                    Text(text = "Task name")
                }
            )
            TextField(
                value = state.note,
                onValueChange = {
                    onEvent(TaskEvent.SetNote(it))
                },
                placeholder = {
                    Text(text = "Note")
                }
            )
            TextField(
                value = state.date,
                onValueChange = {
                    onEvent(TaskEvent.SetDate(it))
                },
                placeholder = {
                    Text(text = "Date")
                }
            )
            Button(
                onClick = {
                onEvent(TaskEvent.SaveTask)
            }) {
                Text(text = "Save")
            }
        }
    }
}