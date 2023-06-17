package com.neupanesushant.note.fragments.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.repo.NoteDetailsDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDetailsDao: NoteDetailsDAO) : ViewModel() {

    private val cacheAllNote: MutableLiveData<List<NoteDetails>> = MutableLiveData()

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private val tempArrayListForSearchedNote = ArrayList<NoteDetails>()

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
        tempArrayListForSearchedNote.clear()
        cacheAllNote.value?.forEach {
            if (isTargetInString(it.title, string)) {
                tempArrayListForSearchedNote.add(it)
            }
        }
        _notesToDisplay.value = tempArrayListForSearchedNote
    }

    private fun isTargetInString(string: String, target: String): Boolean {
        val lengthOfTarget = target.length
        val list = string.split(" ")
        for (i in list) {
            try {
                if (i.substring(0, lengthOfTarget).equals(target, true)) {
                    return true
                }
            } catch (e: StringIndexOutOfBoundsException) {
                continue
            }

        }

        for (i in string.indices) {
            try {
                if (string.substring(i, i + lengthOfTarget).equals(target, true)) {
                    return true
                }
            } catch (_: Exception) {
            }
        }
        return false
    }

}