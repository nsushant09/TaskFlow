package com.neupanesushant.note.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @RawQuery(observedEntities = [Task::class, TaskGroup::class])
    fun getTodayEndingTasks(query: SupportSQLiteQuery): Flow<List<Task>>

    @RawQuery(observedEntities = [Task::class])
    fun getTasksFromGroupID(query: SupportSQLiteQuery): Flow<List<Task>>

    @RawQuery
    suspend fun getTasksFromGroupId(query: SupportSQLiteQuery): List<Task>

    //Int represents number of deleted rows
    @RawQuery
    suspend fun deleteAllTaskFromGroupId(query: SupportSQLiteQuery): Int

}