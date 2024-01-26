package com.minimalisttodolist.pleasebethelastrecyclerview

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

//    @Query("SELECT * FROM task ORDER BY priority DESC, date ASC")
@Query("SELECT * FROM task ORDER BY priority DESC, CASE WHEN date = '' THEN 1 ELSE 0 END, date ASC, time ASC")
    fun getTasksOrderedByPriority(): Flow<List<Task>>

//    @Query("SELECT * FROM task ORDER BY date ASC, time ASC")
//    @Query("SELECT * FROM task WHERE date<> '' ORDER ")
    @Query("SELECT * FROM task ORDER BY CASE WHEN date = '' THEN 1 ELSE 0 END, date ASC, time ASC")
    fun getTasksOrderedByRemainingTime(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY taskName ASC")
    fun getTasksOrderedByAlphabetical(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY taskName DESC")
    fun getTasksOrderedByAlphabeticalRev(): Flow<List<Task>>

}