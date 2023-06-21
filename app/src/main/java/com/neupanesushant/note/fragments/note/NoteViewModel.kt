package com.neupanesushant.note.fragments.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.repo.NoteRepo
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

class NoteViewModel(private val noteRepo: NoteRepo) : ViewModel() {

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private var _notesToDisplay: MutableLiveData<List<NoteDetails>> = MutableLiveData()
    val notesToDisplay: LiveData<List<NoteDetails>> get() = _notesToDisplay

    init {
        viewModelScope.launch {
            noteRepo.getAllNotes().flowOn(Dispatchers.Default).collectLatest {
                _notesToDisplay.postValue(it)
            }
        }
        _isSearchFieldVisible.value = false
    }

    fun refreshNotesToDisplay() {
        _notesToDisplay.value = noteRepo.cachedAllNotes()
    }

    fun refreshNotesIfDifferent(oldNotes: List<NoteDetails>) {
        if (oldNotes != noteRepo.cachedAllNotes()) {
            refreshNotesToDisplay()
        }
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

    fun searchNoteWithString(target: String) {
        _notesToDisplay.value = noteRepo.cachedAllNotes().filter {
            Utils.isTargetInString(it.title, target)
        }
    }

}