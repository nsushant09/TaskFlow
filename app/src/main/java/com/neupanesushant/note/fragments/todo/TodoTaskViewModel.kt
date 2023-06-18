package com.neupanesushant.note.fragments.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup
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

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    init {
        _isSearchFieldVisible.value = false
    }

    fun setGroupId(id: Int) {
        groupId = id
    }

    fun fetchAllTasks() {
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

    fun searchTaskWithString(string: String) {
        val temp = ArrayList<Task>()
        cacheAllTasks.value?.forEach {
            if (Utils.isTargetInString(it.title, string)) {
                temp.add(it)
            }
        }
        _tasksToDisplay.value = temp
    }

    fun addTask(title: String, description: String, date: String) {
        viewModelScope.launch {
            taskDao.insert(
                Task(
                    0,
                    title,
                    description,
                    false,
                    date,
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

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }
}