package com.neupanesushant.note.domain.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.model.Task

@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @RawQuery
    suspend fun getTodayEndingTasks(query: SupportSQLiteQuery): List<Task>

    @RawQuery
    suspend fun getTaskFromGroupID(query: SupportSQLiteQuery): List<Task>
}