package com.neupanesushant.note.fragments.note

import androidx.lifecycle.*
import com.neupanesushant.note.domain.repo.NoteDetailsDAO
import com.neupanesushant.note.domain.model.NoteDetails
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