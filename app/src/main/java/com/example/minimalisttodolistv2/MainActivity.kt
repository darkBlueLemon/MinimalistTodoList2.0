package com.example.minimalisttodolistv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.minimalisttodolistv2.ui.theme.MinimalistTodoListV2Theme

class MainActivity : ComponentActivity() {

//    private val viewModel by viewModels<AddTaskViewModel>()

    // Room Database Initialization
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "tasks.db"
        ).build()
    }
    private val viewModel by viewModels<TaskViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(db.dao) as T
                }
            }
        }
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinimalistTodoListV2Theme {
//                AddItems(){
//                    viewModel.toggleDatePicker()
//                }
//                if(viewModel.isDatePickerEnabled) DatePicker(viewModel){
//                    viewModel.toggleDatePicker()
//                }
//                if(viewModel.isTimePickerEnabled) TimePicker(viewModel){
//                    viewModel.toggleTimePicker()
//                }
//
//                Column (
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Top
//                ) {
//                    Text(
//                        text = if (viewModel.date != "null") viewModel.date else "null",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .fillMaxWidth()
//                    )
//                    Text(
//                        text = viewModel.hour.toString() + ":" + viewModel.min.toString(),
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .fillMaxWidth()
//                            .clickable {
//                                viewModel.toggleTimePicker()
//                            }
//                    )
//                }
//
//                DisplayTasks()

                val state by viewModel.state.collectAsState()
                TaskScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}

@Composable
fun DisplayTasks() {
    // Sample list of items
    val items = (1..100).map { "Item $it" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items.count()) { item ->
            Column (
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "date",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                    )
                    Text(
                        text = " | ",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                    )
                    Text(
                        text = "note",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun AddItems(onClickAction: () -> Unit) {

    // Editable text field
    var itemText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // The size only changes for the text and not the hint

        TextField(
            value = itemText,
            onValueChange = { newText -> itemText = newText },
//            label = { Text("Editable Text") },
            placeholder = { Text("I want to ...") }, // Set the hint text
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
//                .background(Color.White) // Set background to transparent
//                .border(
//                    BorderStroke(1.dp, LocalContentColor.current), // Border with underline
//                    shape = MaterialTheme.shapes.small
//                )
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp) // Adjust the text size here
        )
        TextField(
            value = noteText,
            onValueChange = { newText -> noteText = newText },
//            label = { Text("Editable Text") },
            placeholder = { Text("Add note") }, // Set the hint text
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
//                .background(Color.White) // Set background to transparent
//                .border(
//                    BorderStroke(1.dp, LocalContentColor.current), // Border with underline
//                    shape = MaterialTheme.shapes.small
//                )
            textStyle = LocalTextStyle.current.copy(fontSize = 15.sp) // Adjust the text size here
        )

        Text(
            text = "Add Date",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable {
                    onClickAction.invoke()
                }
        )

        // Non-editable text view
        Text(
            text = noteText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(viewModel: AddTaskViewModel, onClickAction: () -> Unit) {
    val state = rememberDatePickerState()
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        DatePickerDialog(
//            colors = DatePickerDefaults.colors(
//                containerColor = Color.Red,
//                titleContentColor = Color.Yellow,
//                headlineContentColor = Color.Green,
//                weekdayContentColor = Color.Blue
//            ),
            onDismissRequest = {
                openDialog.value = false
                onClickAction.invoke()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onClickAction.invoke()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onClickAction.invoke()
                    }
                ) {
                    Text("CANCEL")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(
                state = state
            )
        }
    }

    viewModel.setDate(state.selectedDateMillis?.let { viewModel.convertMillisToDate(it) }.toString())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(viewModel: AddTaskViewModel, onClickAction: () -> Unit) {
    var selectedHour by remember {
        mutableIntStateOf(0) // or use  mutableStateOf(0)
    }

    var selectedMinute by remember {
        mutableIntStateOf(0) // or use  mutableStateOf(0)
    }

    var showDialog by remember {
        mutableStateOf(true)
    }

    val timePickerState = rememberTimePickerState(
        initialHour = selectedHour,
        initialMinute = selectedMinute
    )

    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            onDismissRequest = {
                showDialog = false
                viewModel.toggleTimePicker()
            }
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Time picker
                TimePicker(state = timePickerState)

                // Buttons
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // dismiss button
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.toggleTimePicker()
                    }) {
                        Text(text = "Dismiss")
                    }

                    // Confirm button
                    TextButton(
                        onClick = {
                            showDialog = false
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                            viewModel.toggleTimePicker()
                            viewModel.setTime(selectedHour, selectedMinute)
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}
