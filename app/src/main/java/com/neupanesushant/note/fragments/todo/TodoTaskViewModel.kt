package com.neupanesushant.note.fragments.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.data.dao.TaskDAO
import com.neupanesushant.note.data.dao.TaskGroupDAO
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.data.repo.TaskRepo
import com.neupanesushant.note.extras.Constants
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

class TodoTaskViewModel(private val taskRepo: TaskRepo) :
    ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private var groupId = -1;

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
            taskRepo.getTasksFromGroupId(groupId).flowOn(Dispatchers.Default)
                .collectLatest {
                    _tasksToDisplay.value = it
                    taskRepo.setCachedNotes(it)
                }
        }
    }

    fun refreshTasksToDisplay() {
        _tasksToDisplay.value = taskRepo.getCachedNotes()
    }

    fun refreshTasksIfDifferent(oldTasks: List<Task>) {
        if (oldTasks != taskRepo.getCachedNotes()) {
            refreshTasksToDisplay()
        }
    }

    fun addTask(title: String, description: String, date: String) {
        scope.launch {
            taskRepo.insert(
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
        scope.launch {
            taskRepo.delete(task)
        }
    }

    fun updateTask(task: Task) {
        scope.launch {
            taskRepo.update(task)
        }
    }

    fun updateTask(task: Task, onCompleteListener: () -> Unit) {
        scope.launch {
            taskRepo.update(task)
            onCompleteListener()
        }
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

    fun searchTasksWithTarget(target: String) {
        _tasksToDisplay.value = taskRepo.getCachedNotes().filter {
            Utils.isTargetInString(it.title, target)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}