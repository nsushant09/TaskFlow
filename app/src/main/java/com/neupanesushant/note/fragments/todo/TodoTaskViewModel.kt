package com.neupanesushant.note.fragments.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.data.repo.TaskRepo
import com.neupanesushant.note.domain.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TodoTaskViewModel(private val taskRepo: TaskRepo) :
    ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private var groupId = -1;

    private var _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

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
                    _tasks.value = it
                }
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

    fun undoDelete(task: Task) {
        scope.launch {
            taskRepo.insert(task)
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

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}