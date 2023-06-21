package com.neupanesushant.note.data

import androidx.sqlite.db.SimpleSQLiteQuery
import com.neupanesushant.note.domain.dao.TaskDAO
import com.neupanesushant.note.domain.dao.TaskGroupDAO
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import com.neupanesushant.note.domain.repo.TaskGroupRepo
import com.neupanesushant.note.extras.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TaskGroupRepoImpl(private val taskGroupDAO: TaskGroupDAO, private val taskDAO: TaskDAO) :
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
        return withContext(Dispatchers.IO) {
            val temp = arrayListOf<TaskGroupWithAllTasks>()
            val deferredTasks = taskGroupDAO.getAllTaskGroup().map { group ->
                async {
                    val tasks =
                        taskDAO.getTasksFromGroupId(SimpleSQLiteQuery("SELECT * FROM ${Constants.TASK_TABLE} WHERE groupId = ${group.id}"))
                    TaskGroupWithAllTasks(group.id, group.name, tasks)
                }
            }
            temp.addAll(deferredTasks.awaitAll())
            return@withContext temp
        }
    }

}