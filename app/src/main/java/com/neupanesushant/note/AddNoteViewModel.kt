package com.neupanesushant.note

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.model.NoteDatabase
import com.neupanesushant.note.model.NoteDetailsDAO
import com.neupanesushant.note.model.NoteDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(application : Application) : AndroidViewModel(application) {
    private val noteDetailsDao : NoteDetailsDAO

    private val _clickedFromNoteFragment = MutableLiveData<NoteDetails>()
    val clickedFromNoteFragment : LiveData<NoteDetails> get() = _clickedFromNoteFragment

    init{
        noteDetailsDao = NoteDatabase.getDatabase(application).noteDetailsDao()
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

//   fun getCurrentNoteDetails(id : Int){
//       viewModelScope.launch{
//           _clickedFromNoteFragment.value = noteDetailsDao.getNoteDetails(id).value
//           Log.i("AddNoteViewModel", "The id is : ${clickedFromNoteFragment.value?.id}")
//       }
//   }
}