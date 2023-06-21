package com.neupanesushant.note.fragments.note

import androidx.lifecycle.*
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.domain.repo.NoteRepo
import kotlinx.coroutines.*

class AddNoteViewModel(private val noteRepo: NoteRepo) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    fun addNoteDetails(noteDetails: NoteDetails) {
        scope.launch(Dispatchers.IO) {
            noteRepo.insert(noteDetails)
        }
    }

    fun updateNoteDetails(noteDetails: NoteDetails) {
        scope.launch(Dispatchers.IO) {
            noteRepo.update(noteDetails)
        }
    }

    fun deleteNoteDetails(noteDetails: NoteDetails) {
        scope.launch(Dispatchers.IO) {
            noteRepo.delete(noteDetails)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}