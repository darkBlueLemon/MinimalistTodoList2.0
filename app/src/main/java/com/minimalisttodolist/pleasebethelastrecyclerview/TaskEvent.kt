package com.minimalisttodolist.pleasebethelastrecyclerview

interface TaskEvent {
    object SaveTask: TaskEvent
    data class SetTaskName(val taskName: String): TaskEvent
    data class SetNote(val note: String): TaskEvent
    data class SetDate(val date: String): TaskEvent
    data class SetTime(val time: String): TaskEvent
    data class SetPriority(val priority: Int): TaskEvent
    object ShowAddTaskDialog: TaskEvent
    object HideAddTaskDialog: TaskEvent
    object ShowSettingsDialog: TaskEvent
    object HideSettingsDialog: TaskEvent
    data class SortTasks(val sortType: SortType): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent
}