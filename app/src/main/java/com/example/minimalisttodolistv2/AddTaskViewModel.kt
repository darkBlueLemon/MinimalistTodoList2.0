package com.example.minimalisttodolistv2

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    private var _date by mutableStateOf("null")
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
    private var _hour by mutableStateOf(-1)
    private var _min by mutableStateOf(-1)
    val hour: Int get() = _hour
    val min: Int get() = _min

    fun setTime(newHour: Int, newMin: Int){
        _hour = newHour
        _min = newMin
    }

}