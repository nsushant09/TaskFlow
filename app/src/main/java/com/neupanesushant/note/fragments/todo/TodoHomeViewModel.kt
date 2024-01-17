package com.neupanesushant.note.fragments.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.data.repo.TaskGroupRepo
import com.neupanesushant.note.data.repo.TaskRepo
import com.neupanesushant.note.domain.model.Task
import com.neupanesushant.note.domain.model.TaskGroup
import com.neupanesushant.note.domain.model.TaskGroupWithAllTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TodoHomeViewModel(private val taskGroupRepo: TaskGroupRepo, private val taskRepo: TaskRepo) :
    ViewModel() {

    private val _isSearchFieldVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private val _allGroup: MutableLiveData<List<TaskGroupWithAllTasks>> = MutableLiveData()
    val allGroup: LiveData<List<TaskGroupWithAllTasks>> get() = _allGroup

    private val _todayEndingTasks: MutableLiveData<List<Task>> = MutableLiveData()
    val todayEndingTasks: LiveData<List<Task>> get() = _todayEndingTasks

    init {
        _isSearchFieldVisible.value = false
        getTodayEndingTasks()
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

    fun addNewGroup(groupName: String, callback: () -> Unit) {
        viewModelScope.launch {
            taskGroupRepo.insert(TaskGroup(0, groupName))
            callback()
        }
    }

    fun updateGroup(taskGroup: TaskGroup, callback: () -> Unit) {
        viewModelScope.launch {
            taskGroupRepo.update(taskGroup)
            callback()
        }
    }

    fun deleteGroup(taskGroup: TaskGroup, callback: () -> Unit) {
        viewModelScope.launch {
            taskRepo.deleteAllTaskFromGroupId(taskGroup.id)
            taskGroupRepo.delete(taskGroup)
            callback()
        }
    }

    fun refreshData() {
        getAllGroup()
    }

    private fun getAllGroup() {
        viewModelScope.launch {
            val newValue = taskGroupRepo.getAllGroupWithTask()
            if (allGroup.value == null || allGroup.value != newValue) {
                _allGroup.value = taskGroupRepo.getAllGroupWithTask()
            }
        }
    }

    private fun getTodayEndingTasks() {
        viewModelScope.launch {
            taskRepo.getTodayEndingTasks()
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _todayEndingTasks.value = it
                }
        }
    }
}