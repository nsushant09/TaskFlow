package com.neupanesushant.note.fragments.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.data.repo.NoteRepo
import com.neupanesushant.note.domain.model.NoteDetails
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepo: NoteRepo) : ViewModel() {

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible: LiveData<Boolean> get() = _isSearchFieldVisible

    private var _notes: MutableLiveData<List<NoteDetails>> = MutableLiveData()
    val notes: LiveData<List<NoteDetails>> get() = _notes

    init {
        viewModelScope.launch {
            noteRepo.getAllNotes().flowOn(this.coroutineContext).collectLatest {
                _notes.postValue(it)
            }
        }
        _isSearchFieldVisible.value = false
    }

    fun setSearchFieldVisibility(boolean: Boolean) {
        _isSearchFieldVisible.value = boolean
    }

}