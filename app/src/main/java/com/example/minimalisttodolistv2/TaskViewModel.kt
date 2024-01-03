package com.example.minimalisttodolistv2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(
    private val dao: TaskDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.ALPHABETICAL)
    private val _tasks = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                SortType.ALPHABETICAL -> dao.getTasksOrderedByAlphabetical()
                SortType.ALPHABETICAL_REV -> dao.getTasksOrderedByAlphabeticalRev()
                SortType.REMAINING_TIME -> dao.getTasksOrderedByRemainingTime()
                SortType.PRIORITY -> dao.getTasksOrderedByPriority()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TaskState())
    val state = combine(_state, _sortType, _tasks) { state, sortType, tasks ->
        state.copy(
            tasks = tasks,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    fun onEvent(event: TaskEvent){
        when(event){
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    dao.deleteTask(event.task)
                }
            }
            TaskEvent.HideAddTaskDialog-> {
                _state.update { it.copy(
                    isAddingTask = false,
                    taskName = "",
                    note = "",
                    date = "",
                    time = "",
                    priority = 0
                ) }
            }
            TaskEvent.SaveTask -> {
                val taskName = state.value.taskName
                val note = state.value.note
                val date = state.value.date
                val time = state.value.time
                val priority = state.value.priority

                // We don't save anything if something empty
//                if(taskName.isBlank() || note.isBlank() || date.isBlank()){
                if(taskName.isBlank()){
                    Log.e("MYTAG","ERROR while saving task")
                    _state.update { it.copy(
                        isAddingTask = false,
                    )}
                    return
                }

                if(time.isBlank()){
                    Log.d("MYTAG","Time is Blank")
                }

                val task = Task(
                    taskName = taskName,
                    note = note,
                    date = date,
                    time = time,
                    priority = priority
                )
                viewModelScope.launch {
                    dao.upsertTask(task)
                }
                _state.update { it.copy(
                    isAddingTask = false,
                    taskName = "",
                    note = "",
                    date = "",
                    time = "",
                    priority = 0
                ) }
            }
            is TaskEvent.SetTaskName -> {
                _state.update { it.copy(
                    taskName = event.taskName
                ) }
            }
            is TaskEvent.SetNote -> {
                _state.update { it.copy(
                    note = event.note
                ) }
            }
            is TaskEvent.SetDate -> {
                _state.update { it.copy(
                    date = event.date
                ) }
            }
            is TaskEvent.SetTime -> {
                _state.update { it.copy(
                    time = event.time
                ) }
            }
            is TaskEvent.SetPriority -> {
                _state.update { it.copy(
                    priority = event.priority
                ) }
            }
            TaskEvent.ShowAddTaskDialog -> {
                _state.update { it.copy(
                    isAddingTask = true
                ) }
            }
            is TaskEvent.SortTasks -> {
                _sortType.value = event.sortType
            }
            TaskEvent.ShowSettingsDialog -> {
                _state.update { it.copy(
                    isChangingSettings = true
                )}
            }
            TaskEvent.HideSettingsDialog -> {
                _state.update { it.copy(
                    isChangingSettings = false
                )}
            }
        }
    }
}