package com.neupanesushant.note.fragments.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.repo.NoteDetailsDAO
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList

class NoteViewModel(private val noteDetailsDao: NoteDetailsDAO) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private var _cacheAllNote: MutableLiveData<List<NoteDetails>> = MutableLiveData()
    private val cacheAllNote: LiveData<List<NoteDetails>> get() = _cacheAllNote

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private var _notesToDisplay: MutableLiveData<List<NoteDetails>> = MutableLiveData()
    val notesToDisplay: LiveData<List<NoteDetails>> get() = _notesToDisplay

    init {
        cacheAllNote()
        _isSearchFieldVisible.value = false
    }

    private fun cacheAllNote() {
        scope.launch {
            noteDetailsDao.readAllNote()
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _cacheAllNote.postValue(it)
                    _notesToDisplay.postValue(it)
                }
        }
    }

    fun refreshNotesToDisplay() {
        scope.launch {
            _notesToDisplay.postValue(cacheAllNote.value)
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