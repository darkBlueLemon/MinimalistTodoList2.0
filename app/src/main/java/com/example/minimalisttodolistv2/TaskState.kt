package com.example.minimalisttodolistv2

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val taskName: String = "",
    val note: String = "",
    val date: String = "",
    val isAddingTask: Boolean = false,
    val sortType: SortType = SortType.TASK_NAME
)