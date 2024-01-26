package com.minimalisttodolist.pleasebethelastrecyclerview

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val taskName: String = "",
    val note: String = "",
    val date: String = "",
    val time: String = "",
    val priority: Int = 0,
    val isAddingTask: Boolean = false,
    val isChangingSettings: Boolean = false,
    val sortType: SortType = SortType.PRIORITY
)
