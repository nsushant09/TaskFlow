package com.neupanesushant.note.fragments.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.repo.TaskDAO
import com.neupanesushant.note.domain.repo.TaskGroupDAO
import com.neupanesushant.note.extras.Constants
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TodoTaskViewModel(private val taskDao: TaskDAO, private val taskGroupDAO: TaskGroupDAO) :
    ViewModel() {

    private var groupId = -1;

    private val cacheAllTasks: MutableLiveData<List<Task>> = MutableLiveData()

    private var _tasksToDisplay = MutableLiveData<List<Task>>()
    val tasksToDisplay: LiveData<List<Task>> get() = _tasksToDisplay

    fun setGroupId(id: Int) {
        groupId = id
    }

    fun observeAllTasks() {
        viewModelScope.launch {
            taskDao.getTaskFromGroupID(SimpleSQLiteQuery("SELECT * FROM ${Constants.TASK_TABLE} WHERE groupId = $groupId"))
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    cacheAllTasks.postValue(it)
                    _tasksToDisplay.postValue(it)
                }
        }
    }

    fun refreshTasks() {
        _tasksToDisplay.value = cacheAllTasks.value
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            taskDao.insert(
                Task(
                    0,
                    title,
                    description,
                    false,
                    Utils.getCurrentDate().toString(),
                    groupId
                )
            )
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }
}