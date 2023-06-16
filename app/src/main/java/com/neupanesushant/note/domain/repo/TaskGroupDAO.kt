package com.neupanesushant.note.domain.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.extras.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskGroupDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskGroup: TaskGroup)

    @Update
    suspend fun update(taskGroup: TaskGroup)

    @Delete
    suspend fun delete(taskGroup: TaskGroup)

    @Query("SELECT * FROM " + Constants.TASKGROUP_TABLE)
    fun getAllTaskGroup(): Flow<List<TaskGroup>?>
}