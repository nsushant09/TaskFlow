package com.neupanesushant.note.domain.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup

@Dao
interface TaskGroupDAO {
    @Insert
    suspend fun insert(taskGroup: TaskGroup)

    @Update
    suspend fun update(taskGroup: TaskGroup)

    @Delete
    suspend fun delete(taskGroup: TaskGroup)

    @RawQuery
    fun getAllTaskGroup(query: SupportSQLiteQuery): List<TaskGroup>
}