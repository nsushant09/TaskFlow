package com.neupanesushant.note.data.repo

import com.neupanesushant.note.data.dao.TaskGroupDAO
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class TaskGroupRepoImpl(
    private val taskGroupDAO: TaskGroupDAO,
    private val taskRepo: TaskRepo,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    TaskGroupRepo {

    override suspend fun insert(taskGroup: TaskGroup) {
        taskGroupDAO.insert(taskGroup)
    }

    override suspend fun update(taskGroup: TaskGroup) {
        taskGroupDAO.update(taskGroup)
    }

    override suspend fun delete(taskGroup: TaskGroup) {
        taskGroupDAO.delete(taskGroup)
    }

    override suspend fun getAllTaskGroup(): List<TaskGroup> {
        return taskGroupDAO.getAllTaskGroup()
    }

    override suspend fun getAllGroupWithTask(): List<TaskGroupWithAllTasks> {
        return withContext(coroutineDispatcher) {
            val temp = arrayListOf<TaskGroupWithAllTasks>()
            val deferredTasks = taskGroupDAO.getAllTaskGroup().map { group ->
                async {
                    val tasks =
                        taskRepo.getTasksFromGroupId(
                            group.id,
                            true
                        )
                    TaskGroupWithAllTasks(group.id, group.name, tasks)
                }
            }
            temp.addAll(deferredTasks.awaitAll())
            return@withContext temp
        }
    }

}