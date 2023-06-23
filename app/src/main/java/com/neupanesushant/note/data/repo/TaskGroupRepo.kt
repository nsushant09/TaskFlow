package com.neupanesushant.note.data.repo

import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import kotlinx.coroutines.flow.Flow

interface TaskGroupRepo {
    suspend fun insert(taskGroup: TaskGroup)
    suspend fun update(taskGroup: TaskGroup)
    suspend fun delete(taskGroup: TaskGroup)
    suspend fun getAllTaskGroup(): List<TaskGroup>
    suspend fun getAllGroupWithTask(): List<TaskGroupWithAllTasks>
}