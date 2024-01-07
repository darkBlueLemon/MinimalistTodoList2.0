package com.example.minimalisttodolistv2

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date

class AddTaskViewModel: ViewModel() {

    // DatePicker Toggle
    private var _isDatePickerEnabled by mutableStateOf(false)
    val isDatePickerEnabled: Boolean get() = _isDatePickerEnabled

    fun toggleDatePicker() {
        _isDatePickerEnabled = !_isDatePickerEnabled
    }

    // Date
    private var _date by mutableStateOf("")
    val date: String get() = _date

    fun setDate(newDate: String){
        _date = newDate
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(Date(millis))
    }

    // TimePicker Toggle
    private var _isTimePickerEnabled by mutableStateOf(false)
    val isTimePickerEnabled: Boolean get() = _isTimePickerEnabled

    fun toggleTimePicker() {
        _isTimePickerEnabled = !_isTimePickerEnabled
    }

    // Time
    private var _hour by mutableStateOf(0)
    private var _min by mutableStateOf(0)
    val time: String get() = ((_hour * 3600 + _min * 60) * (1000)).toString()
    val hour: Int get() = _hour
    val min: Int get() = _min

    fun setTime(newHour: Int, newMin: Int){
        _hour = newHour
        _min = newMin
    }

    fun convertMillisToTime(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)

        return String.format("%02d:%02d", hours, minutes)
    }

    fun getTimeAndDateAsMillis(): Long {
        return _date.toLong() + time.toLong()
    }

    fun callNotificationScheduler(title: String, message: String, context: Context, priority: Int){
        Log.d("MYTAG", "call notification scheduler from addTaskViewModel")
        val notificationService = NotificationService(context = context)
        notificationService.scheduleNotification(title, message, getTimeAndDateAsMillis(), priority)
    }

    fun cancelNotification(message: String, context: Context) {
        val notificationService = NotificationService(context = context)
        notificationService.cancelAlarm(message)
    }
}