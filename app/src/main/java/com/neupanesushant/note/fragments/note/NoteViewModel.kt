package com.neupanesushant.note.fragments.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.repo.NoteDetailsDAO
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDetailsDao: NoteDetailsDAO) : ViewModel() {

    private val cacheAllNote: MutableLiveData<List<NoteDetails>> = MutableLiveData()

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private var _notesToDisplay: MutableLiveData<List<NoteDetails>> = MutableLiveData()
    val notesToDisplay: LiveData<List<NoteDetails>> get() = _notesToDisplay

    init {
        cacheAllNote()
        _isSearchFieldVisible.value = false
    }

    private fun cacheAllNote() {
        viewModelScope.launch {
            noteDetailsDao.readAllNote()
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    cacheAllNote.postValue(it)
                    _notesToDisplay.postValue(it)
                }
        }
    }

    fun refreshNotesToDisplay() {
        _notesToDisplay.value = cacheAllNote.value
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

    fun searchNoteWithString(string: String) {
        val temp = ArrayList<NoteDetails>()
        cacheAllNote.value?.forEach {
            if (Utils.isTargetInString(it.title, string)) {
                temp.add(it)
            }
        }
        _notesToDisplay.value = temp
    }

}