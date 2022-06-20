package com.neupanesushant.note.fragments.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.model.NoteDatabase
import com.neupanesushant.note.model.NoteDetaillsDAO
import com.neupanesushant.note.model.NoteDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application : Application) : AndroidViewModel(application) {

    private val noteDetailsDao : NoteDetaillsDAO
    val readAllNote : LiveData<List<NoteDetails>>

    init{
        noteDetailsDao = NoteDatabase.getDatabase(application).noteDetailsDao()
        readAllNote = noteDetailsDao.readAllNote()
    }

    fun addNoteDetails(noteDetails: NoteDetails){
        viewModelScope.launch(Dispatchers.IO) {
            noteDetailsDao.insert(noteDetails)
        }
    }
}