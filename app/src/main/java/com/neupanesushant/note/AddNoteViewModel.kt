package com.neupanesushant.note

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.neupanesushant.note.model.NoteDatabase
import com.neupanesushant.note.model.NoteDetailsDAO
import com.neupanesushant.note.model.NoteDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(val noteDetailsDao : NoteDetailsDAO) : ViewModel() {

    private val _clickedFromNoteFragment = MutableLiveData<NoteDetails>()
    val clickedFromNoteFragment : LiveData<NoteDetails> get() = _clickedFromNoteFragment

    init{
        _clickedFromNoteFragment.value = NoteDetails(0, "", "", "")
    }

    fun addNoteDetails(noteDetails: NoteDetails){
        viewModelScope.launch(Dispatchers.IO) {
            noteDetailsDao.insert(noteDetails)
        }
    }

    fun updateNoteDetails(noteDetails: NoteDetails){
        viewModelScope.launch(Dispatchers.IO){
            noteDetailsDao.update(noteDetails)
        }
    }

    fun deleteNoteDetails(noteDetails: NoteDetails){
        viewModelScope.launch(Dispatchers.IO){
            noteDetailsDao.delete(noteDetails)
        }
    }
}