package com.example.minimalisttodolistv2

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.minimalisttodolistv2.ui.theme.MinimalistTodoListV2Theme
import kotlinx.coroutines.delay

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

    override fun onCreate(savedInstanceState: Bundle?) {

        // Creating shared prefs
        PreferencesManager.initialize(this)

        // Cancel all notifications
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        super.onCreate(savedInstanceState)

        // can be removed
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



                LaunchedEffect(key1 = true) {
                    delay(1000)
                    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
//                Column (
//                    modifier = Modifier,
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Button(onClick = {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                        }
//                    }) {
//                       Text(text = "Request Permission")
//                    }
//                    Button(onClick = {
//                        if(hasNotificationPermission) {
//                            notificationService.showNotification(Counter.value)
////                            viewModel2.callNotificationScheduler("title", "message", applicationContext)
////                            notificationService.scheduleNotification("title","message", viewModel2.getTimeAndDateAsMillis())
//                        }
//                    }) {
//                        Text(text = "Send Notification")
//                    }
//                }







                // Delete Notifications
                deleteNotification(context = context, notificationManager = notificationManager, hasNotificationPermission, notificationService)

                // Calling TaskScreen
                val state by viewModel.state.collectAsState()
                TaskScreen(state = state, onEvent = viewModel::onEvent, viewModel = viewModel2, context = context, notificationManager = notificationManager)

            }
        }
    }
}

@Composable
fun deleteNotification(context: Context, notificationManager: NotificationManager, hasNotificationPermission: Boolean, notificationService: NotificationService, lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    // on resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            if(event == Lifecycle.Event.ON_RESUME) {
                Log.d("MYTAG","On Resume")
                notificationManager.cancelAll();
                // Bday Manager
//                    PreferencesManager.bdayVisible = false
//                    PreferencesManager.bdayNotificationCount = 0
//                if(hasNotificationPermission && PreferencesManager.bdayNotificationCount <= 3) {
                if(PreferencesManager.bdayNotificationCount < 3) {
                    notificationService.scheduleBdayNotification()
                }
                if(PreferencesManager.bdayVisible) {
                    val intent = Intent(context, MessageActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
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
            ),
            onDismissRequest = {
                openDialog.value = false
                viewModel.setDate("")
                onClickAction.invoke()
            },
            confirmButton = {
            },
            dismissButton = {
            },
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(shape = RoundedCornerShape(percent = 7))
                    .background(Color.Black)
                    .fillMaxWidth()
//                    .size(width = 500.dp, height = 700.dp)
                    .padding(start = 4.dp, end = 4.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(percent = 7)
                    )
                ,
            ) {
                Column (
                    modifier = Modifier,

                ) {
                    androidx.compose.material3.DatePicker(
                        modifier = Modifier
//                            .size(100.dp)
//                            .align(Alignment.Start)
//                            .background(Color.Yellow)
                                ,
                        state = state,
                        showModeToggle = false,
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.Black,
                            titleContentColor = Color.White,
                            headlineContentColor = Color.White,
                            weekdayContentColor = Color.White,
                            subheadContentColor = Color.White,

                            yearContentColor = Color.White,
                            currentYearContentColor = Color.White,
                            selectedYearContentColor = Color.Black,
                            selectedYearContainerColor = Color.White,

                            dayContentColor = Color.White,
                            disabledDayContentColor = Color.White,
                            selectedDayContentColor = Color.Black,
                            disabledSelectedDayContentColor = Color.White,
                            selectedDayContainerColor = Color.White,
                            disabledSelectedDayContainerColor = Color.White,

                            todayContentColor = Color.White,
                            todayDateBorderColor = Color.White,

                            dayInSelectionRangeContentColor = Color.White,
                            dayInSelectionRangeContainerColor = Color.White,

                        )
                    )
                    Row(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxWidth()
                            .size(100.dp)
                            .padding(bottom = 20.dp, end = 20.dp)
                                ,
                        horizontalArrangement = Arrangement.End
                    ){
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                viewModel.setDate("")
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
        initialMinute = selectedMinute,
        true
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
                        .size(width = 350.dp, height = 480.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 7)
                        )
                        .padding(20.dp)
                        ,
            contentAlignment = Alignment.Center
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
