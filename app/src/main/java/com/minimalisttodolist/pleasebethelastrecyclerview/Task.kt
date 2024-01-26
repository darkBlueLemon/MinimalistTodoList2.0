package com.minimalisttodolist.pleasebethelastrecyclerview

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val taskName: String,
    val note: String,
    val date: String,
    val time: String,
    val priority: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
