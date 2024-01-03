package com.example.minimalisttodolistv2

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.minimalisttodolistv2.ui.theme.MinimalistTodoListV2Theme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

class MainActivity : ComponentActivity() {

    private val viewModel2 by viewModels<AddTaskViewModel>()

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

        // Fullscreen
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)

        val notificationService = NotificationService(applicationContext)

        setContent {
            MinimalistTodoListV2Theme {

                // Notifications
                val context = LocalContext.current
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
                Column (
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }) {
                       Text(text = "Request Permission")
                    }
                    Button(onClick = {
                        if(hasNotificationPermission) {
                            notificationService.showNotification(Counter.value)
                        }
                    }) {
                        Text(text = "Send Notification")
                    }
                }


                // Calling TaskScreen
                val state by viewModel.state.collectAsState()
//                TaskScreen(state = state, onEvent = viewModel::onEvent, viewModel = viewModel2)

            }
        }
    }

//    private fun showNotification() {
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notification = NotificationCompat.Builder(applicationContext, NotificationService.COUNTER_CHANNEL_ID)
//            .setContentTitle("123456789 123456789 123456789")
//            .setContentText("Content Text fjsdklf asdfjdsk fkds fjasdkj f")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .build()
//        notificationManager.notify(1, notification)
//    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(viewModel: AddTaskViewModel, onClickAction: () -> Unit) {
    val state = rememberDatePickerState()
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        DatePickerDialog(
            modifier = Modifier
                    .clip(shape = RoundedCornerShape(percent = 7)),
            colors = DatePickerDefaults.colors(
                containerColor = Color.Black,
//                titleContentColor = Color.White,
//                headlineContentColor = Color.White,
//                weekdayContentColor = Color.Green,
//                subheadContentColor = Color.Magenta,
//
//                yearContentColor = Color.White,
//                currentYearContentColor = Color.White,
//                selectedYearContentColor = Color.White,
//                selectedYearContainerColor = Color.White,
//
//                dayContentColor = Color.White,
//                disabledDayContentColor = Color.Yellow,
//                selectedDayContentColor = Color.Red,
//                disabledSelectedDayContentColor = Color.Red,
//                selectedDayContainerColor = Color.White,
//                disabledSelectedDayContainerColor = Color.Red,
//
//                todayContentColor = Color.Transparent,
//                todayDateBorderColor = Color.Green,
//
//                dayInSelectionRangeContentColor = Color.Red,
//                dayInSelectionRangeContainerColor = Color.Red
            ),
            onDismissRequest = {
                openDialog.value = false
                onClickAction.invoke()
            },
            confirmButton = {
//                TextButton(
//                    onClick = {
//                        openDialog.value = false
//                        onClickAction.invoke()
//                    }
//                ) {
//                    Text("OK")
//                }
            },
            dismissButton = {
//                TextButton(
//                    onClick = {
//                        openDialog.value = false
//                        onClickAction.invoke()
//                    }
//                ) {
//                    Text("CANCEL")
//                }
            },
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(shape = RoundedCornerShape(percent = 7))
                    .background(Color.Black)
                    .padding(2.dp)
//                    .size(width = 350.dp, height = 600.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(percent = 7)
                    ),
//            contentAlignment = Alignment.Center
            ) {
                Column (
                    modifier = Modifier,

                ) {
                    androidx.compose.material3.DatePicker(
//                        modifier = Modifier.background(Color.Yellow),
                        state = state,
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.Black,
                            titleContentColor = Color.White,
                            headlineContentColor = Color.White,
                            weekdayContentColor = Color.White,
                            subheadContentColor = Color.Magenta,

                            yearContentColor = Color.White,
                            currentYearContentColor = Color.White,
                            selectedYearContentColor = Color.Black,
                            selectedYearContainerColor = Color.White,

                            dayContentColor = Color.White,
                            disabledDayContentColor = Color.Yellow,
                            selectedDayContentColor = Color.Black,
                            disabledSelectedDayContentColor = Color.Red,
                            selectedDayContainerColor = Color.White,
                            disabledSelectedDayContainerColor = Color.Red,

                            todayContentColor = Color.White,
                            todayDateBorderColor = Color.White,

                            dayInSelectionRangeContentColor = Color.Red,
                            dayInSelectionRangeContainerColor = Color.Red
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.End
                    ){
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                onClickAction.invoke()
                            }
                        ) {
                            Text("CANCEL", color = Color.White)
                        }
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                onClickAction.invoke()
                            }
                        ) {
                            Text("OK", color = Color.White)
                        }
                    }
                }
            }
        }
    }

//    viewModel.setDate(state.selectedDateMillis?.let { viewModel.convertMillisToDate(it) }.toString())
//    if(state.selectedDateMillis != null) Log.d("MYTAG", state.selectedDateMillis.toString())
    if(state.selectedDateMillis != null) viewModel.setDate(state.selectedDateMillis.toString())
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
//                .background(
//                    color = Color.Black,
//                ),
                    ,
            onDismissRequest = {
//                showDialog = false
                viewModel.toggleTimePicker()
            }
        ) {
            Column(
                modifier = Modifier
//                    .background(
//                        color = Color.LightGray.copy(alpha = 0.3f)
//                    )
//                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                        ,
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(shape = RoundedCornerShape(percent = 7))
                        .background(Color.Black)
//                    .size(width = 350.dp, height = 600.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 7)
                        )
                        .padding(20.dp)
                        ,
//            contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier,
                        horizontalAlignment = Alignment.End
                    ){

                        // Time picker
                        TimePicker(
                            modifier = Modifier.padding(0.dp),
                            state = timePickerState,
                            colors = TimePickerDefaults.colors(
                                clockDialColor = Color.Black,
                                clockDialSelectedContentColor = Color.Black,
                                clockDialUnselectedContentColor = Color.White,

                                selectorColor = Color.White,
//                                containerColor = Color.Red,

                                periodSelectorBorderColor = Color.White,
//                                periodSelectorBorderColor = Color(0x7FFFFFFF),
//                                periodSelectorSelectedContainerColor = Color(0x7FFFFFFF),
                                periodSelectorSelectedContainerColor = Color.White,
                                periodSelectorUnselectedContainerColor = Color.Black,
                                periodSelectorSelectedContentColor = Color.Black,
                                periodSelectorUnselectedContentColor = Color.White,

                                timeSelectorSelectedContainerColor = Color.White,
                                timeSelectorUnselectedContainerColor = Color.Black,
                                timeSelectorSelectedContentColor = Color.Black,
                                timeSelectorUnselectedContentColor = Color.White
                            )
                        )

                        // Buttons
                        Row(
                            modifier = Modifier
//                                .padding(top = 12.dp)
//                                .fillMaxWidth(),
                                    ,
                            horizontalArrangement = Arrangement.End
                        ) {
                            // dismiss button
                            TextButton(onClick = {
//                        showDialog = false
                                viewModel.toggleTimePicker()
                                onClickAction.invoke()
                            }) {
                                Text(text = "CANCEL", color = Color.White)
                            }

                            // Confirm button
                            TextButton(
                                onClick = {
//                            showDialog = false
                                    selectedHour = timePickerState.hour
                                    selectedMinute = timePickerState.minute
                                    viewModel.toggleTimePicker()
                                    viewModel.setTime(selectedHour, selectedMinute)
                                    onClickAction.invoke()
                                }
                            ) {
                                Text(text = "OK", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
