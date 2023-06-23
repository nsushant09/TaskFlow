package com.neupanesushant.note.domain.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neupanesushant.note.data.dao.NoteDetailsDAO
import com.neupanesushant.note.data.dao.TaskDAO
import com.neupanesushant.note.data.dao.TaskGroupDAO

@Database(
    entities = [NoteDetails::class, Task::class, TaskGroup::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun noteDetailsDao(): NoteDetailsDAO
    abstract fun taskDao() : TaskDAO
    abstract fun taskGroupDao() : TaskGroupDAO
}