package com.neupanesushant.note.fragments.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.repo.NoteDetailsDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDetailsDao: NoteDetailsDAO) : ViewModel() {

    lateinit var readAllNote: LiveData<List<NoteDetails>>

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private val _searchedNoteList = MutableLiveData<List<NoteDetails>>()
    val searchedNoteList: LiveData<List<NoteDetails>> get() = _searchedNoteList
    private val tempArrayListForSearchedNote = ArrayList<NoteDetails>()

    init {
        readAllNote()
        _searchedNoteList.value = listOf()
        _isSearchFieldVisible.value = false
    }

    private fun readAllNote() {
        readAllNote = noteDetailsDao.readAllNote()
    }

    fun addNoteDetails(noteDetails: NoteDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDetailsDao.insert(noteDetails)
        }
    }

    fun deleteNoteDetails(noteDetails: NoteDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDetailsDao.delete(noteDetails)
        }
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

    fun searchNoteWithString(string: String) {
        tempArrayListForSearchedNote.clear()
        readAllNote.value?.forEach {
            val listOfTitleWords = it.title.split(" ")
            if (isStringInTitle(listOfTitleWords, it.title, string)) {
                tempArrayListForSearchedNote.add(it)
                _searchedNoteList.value = tempArrayListForSearchedNote
            }
        }
    }

    private fun isStringInTitle(list: List<String>, title: String, target: String): Boolean {
        val lengthOfTarget = target.length
        for (i in list) {
            try {
                if (i.substring(0, lengthOfTarget).equals(target, true)) {
                    return true
                }
            } catch (e: StringIndexOutOfBoundsException) {
                continue
            }

        }

        for (i in title.indices) {
            try {
                if (title.substring(i, i + lengthOfTarget).equals(target, true)) {
                    return true
                }
            } catch (_: Exception) {
            }
        }
        return false
    }

}