package com.neupanesushant.note.fragments.todo

import android.annotation.SuppressLint
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
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat

class TodoHomeViewModel(private val taskDao: TaskDAO, private val taskGroupDAO: TaskGroupDAO) :
    ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private val _isSearchFieldVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private val _allGroup: MutableLiveData<List<TaskGroupWithAllTasks>> = MutableLiveData()
    val allGroup: LiveData<List<TaskGroupWithAllTasks>> get() = _allGroup

    private val _todayEndingTasks: MutableLiveData<List<Task>> = MutableLiveData()
    val todayEndingTasks: LiveData<List<Task>> get() = _todayEndingTasks

    private val _cacheTaskGroup: MutableLiveData<List<TaskGroup>> = MutableLiveData()

    init {
        _isSearchFieldVisible.value = false
        getAllGroup()
        getTodayEndingTasks()
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

    fun addNewGroup(groupName: String) {
        scope.launch {
            taskGroupDAO.insert(TaskGroup(0, groupName))
        }
    }

    fun refreshData() {
        _cacheTaskGroup.value?.let {
            getAllTaskFromGroups(it)
        }
    }

    private fun getAllGroup() {
        scope.launch {
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
        scope.launch {
            val temp = ArrayList<TaskGroupWithAllTasks>()
            list.forEach {
                val tasks =
                    taskDao.getTasksFromGroupId(SimpleSQLiteQuery("SELECT * FROM ${Constants.TASK_TABLE} WHERE groupId = ${it.id}"))
                temp.add(TaskGroupWithAllTasks(it.id, it.name, tasks))
            }
            _allGroup.postValue(temp)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTodayEndingTasks() {
        scope.launch {
            val todayDate = SimpleDateFormat("dd/MM/yyyy").format(Utils.getCurrentDate()).toString()
            val query = "SELECT * FROM ${Constants.TASK_TABLE} WHERE date = '$todayDate'"
            taskDao.getTodayEndingTasks(SimpleSQLiteQuery(query))
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _todayEndingTasks.postValue(it)
                }
        }
    }

    fun refreshGroupItem(groupId: Int, itemId: Int) {
        val temp = allGroup.value
        temp?.forEach {
            if (it.id == groupId) {
                it.tasks.forEach { task ->
                    if (task.id == itemId) {
                        task.isCompleted = !task.isCompleted
                        return
                    }
                }
                return
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        try {
            scope.cancel()
        } catch (_: java.lang.Exception) {
        }
    }
}