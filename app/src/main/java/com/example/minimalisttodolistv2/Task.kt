package com.example.minimalisttodolistv2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val taskName: String,
    val note: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
