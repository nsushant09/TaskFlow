package com.neupanesushant.note.fragments.note

import androidx.lifecycle.*
import com.neupanesushant.note.domain.repo.NoteDetailsDAO
import com.neupanesushant.note.domain.model.NoteDetails
import kotlinx.coroutines.*

class AddNoteViewModel(private val noteDetailsDao: NoteDetailsDAO) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    fun addNoteDetails(noteDetails: NoteDetails) {
        scope.launch(Dispatchers.IO) {
            noteDetailsDao.insert(noteDetails)
        }
    }

    fun updateNoteDetails(noteDetails: NoteDetails) {
        scope.launch(Dispatchers.IO) {
            noteDetailsDao.update(noteDetails)
        }
    }

    fun deleteNoteDetails(noteDetails: NoteDetails) {
        scope.launch(Dispatchers.IO) {
            noteDetailsDao.delete(noteDetails)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}