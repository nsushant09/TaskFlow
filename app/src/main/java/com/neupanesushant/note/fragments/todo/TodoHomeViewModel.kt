package com.neupanesushant.note.fragments.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.extras.Constants
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import com.neupanesushant.note.domain.repo.TaskDAO
import com.neupanesushant.note.domain.repo.TaskGroupDAO
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

class TodoHomeViewModel(private val taskDao: TaskDAO, private val taskGroupDAO: TaskGroupDAO) :
    ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + job)

    private val _isSearchFieldVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private val _allGroup: MutableLiveData<List<TaskGroupWithAllTasks>> = MutableLiveData()
    val allGroup: LiveData<List<TaskGroupWithAllTasks>> get() = _allGroup

    private val _cacheTaskGroup: MutableLiveData<List<TaskGroup>> = MutableLiveData()

    init {
        _isSearchFieldVisible.value = false
        getAllGroup()
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

    fun addNewGroup(groupName: String) {
        uiScope.launch {
            taskGroupDAO.insert(TaskGroup(0, groupName))
        }
    }

    fun refreshData() {
        _cacheTaskGroup.value?.let {
            getAllTaskFromGroups(it)
        }
    }

    private fun getAllGroup() {
        uiScope.launch {
            taskGroupDAO.getAllTaskGroup()
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    it?.let {
                        _cacheTaskGroup.postValue(it)
                        getAllTaskFromGroups(it)
                    }
                }
        }
    }

    private fun getAllTaskFromGroups(list: List<TaskGroup>) {
        viewModelScope.launch {
            val temp = ArrayList<TaskGroupWithAllTasks>()
            list.forEach {
                val tasks =
                    taskDao.getTasksFromGroupId(SimpleSQLiteQuery("SELECT * FROM ${Constants.TASK_TABLE} WHERE groupId = ${it.id}"))
                temp.add(TaskGroupWithAllTasks(it.id, it.name, tasks))
            }
            _allGroup.postValue(temp)
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            uiScope.cancel()
            job.cancel()
        } catch (_: java.lang.Exception) {
        }
    }
}