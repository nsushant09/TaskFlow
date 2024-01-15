package com.neupanesushant.note.data.repo

import com.neupanesushant.note.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepo {
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
    fun getTodayEndingTasks(): Flow<List<Task>>
    fun getTasksFromGroupId(groupId: Int): Flow<List<Task>>
    suspend fun getTasksFromGroupId(groupId: Int, default: Boolean = true): List<Task>
    suspend fun deleteAllTaskFromGroupId(groupId: Int)
}