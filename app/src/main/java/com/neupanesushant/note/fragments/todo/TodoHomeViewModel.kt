package com.neupanesushant.note.fragments.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoHomeViewModel : ViewModel() {
    private val _isSearchFieldVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    init {
        _isSearchFieldVisible.value = false
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }


}