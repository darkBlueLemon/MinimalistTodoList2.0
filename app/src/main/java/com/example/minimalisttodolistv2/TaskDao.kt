package com.example.minimalisttodolistv2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task ORDER BY priority DESC, date ASC")
    fun getTasksOrderedByPriority(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY date ASC")
    fun getTasksOrderedByRemainingTime(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY taskName ASC")
    fun getTasksOrderedByAlphabetical(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY taskName DESC")
    fun getTasksOrderedByAlphabeticalRev(): Flow<List<Task>>

}