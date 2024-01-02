package com.example.minimalisttodolistv2

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

    private val _sortType = MutableStateFlow(SortType.TASK_NAME)
    private val _tasks = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                SortType.TASK_NAME -> dao.getTasksOrderedByTakeName()
                SortType.NOTE -> dao.getTasksOrderedByNote()
                SortType.DATE -> dao.getTasksOrderedByDate()
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
            TaskEvent.HideDialog-> {
                _state.update { it.copy(
                    isAddingTask = false
                ) }
            }
            TaskEvent.SaveTask -> {
                val taskName = state.value.taskName
                val note = state.value.note
                val date = state.value.date

                // We don't save anything if something empty
                if(taskName.isBlank() || note.isBlank() || date.isBlank()){
                    return
                }

                val task = Task(
                    taskName = taskName,
                    note = note,
                    date = date
                )
                viewModelScope.launch {
                    dao.upsertTask(task)
                }
                _state.update { it.copy(
                    isAddingTask = false,
                    taskName = "",
                    note = "",
                    date = ""
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
            TaskEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingTask = true
                ) }
            }
            is TaskEvent.SortTasks -> {
                _sortType.value = event.sortType
            }
        }
    }
}