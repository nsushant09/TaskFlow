package com.neupanesushant.note.data

import androidx.sqlite.db.SimpleSQLiteQuery
import com.neupanesushant.note.domain.dao.TaskDAO
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.repo.TaskRepo
import com.neupanesushant.note.extras.Constants
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.flow.Flow

class TaskRepoImpl(private val taskDAO: TaskDAO) : TaskRepo {

    private var cachedTasks: List<Task> = emptyList()

    override suspend fun insert(task: Task) {
        taskDAO.insert(task)
    }

    override suspend fun update(task: Task) {
        taskDAO.update(task)
    }

    override suspend fun delete(task: Task) {
        taskDAO.delete(task)
    }

    override fun getTodayEndingTasks(): Flow<List<Task>> {
        return taskDAO.getTodayEndingTasks(SimpleSQLiteQuery("SELECT * FROM ${Constants.TASK_TABLE} WHERE date = '${Utils.getTodayDate()}'"))
    }

    override fun getTasksFromGroupId(groupId: Int): Flow<List<Task>> {
        return taskDAO.getTasksFromGroupID(SimpleSQLiteQuery("SELECT * FROM ${Constants.TASK_TABLE} WHERE groupID = $groupId"))
    }

    override suspend fun getTasksFromGroupId(groupId: Int, default: Boolean): List<Task> {
        return taskDAO.getTasksFromGroupId(SimpleSQLiteQuery("SELECT * FROM ${Constants.TASK_TABLE} WHERE groupID = $groupId"))
    }

    override suspend fun deleteAllTaskFromGroupId(groupId: Int) {
        taskDAO.deleteAllTaskFromGroupId(SimpleSQLiteQuery("DELETE FROM ${Constants.TASK_TABLE} WHERE groupID = $groupId"))
    }

    override fun getCachedNotes(): List<Task> {
        return cachedTasks
    }

    override fun setCachedNotes(notesToCache: List<Task>) {
        cachedTasks = notesToCache
    }


}