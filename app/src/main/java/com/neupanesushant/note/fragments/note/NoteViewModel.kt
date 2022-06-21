package com.neupanesushant.note.fragments.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.model.NoteDatabase
import com.neupanesushant.note.model.NoteDetailsDAO
import com.neupanesushant.note.model.NoteDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application : Application) : AndroidViewModel(application) {

    private val noteDetailsDao : NoteDetailsDAO
    val readAllNote : LiveData<List<NoteDetails>>

    private val _isSearchFieldVisible = MutableLiveData<Boolean>()
    val isSearchFieldVisible : LiveData<Boolean> get() = _isSearchFieldVisible

    private val _searchedNoteList = MutableLiveData<List<NoteDetails>>()
    val searchedNoteList : LiveData<List<NoteDetails>> get() = _searchedNoteList
    private val tempArrayListForSearchedNote = ArrayList<NoteDetails>()

    init{
        noteDetailsDao = NoteDatabase.getDatabase(application).noteDetailsDao()
        readAllNote = noteDetailsDao.readAllNote()
        _searchedNoteList.value = listOf()
        _isSearchFieldVisible.value = false
    }

    fun addNoteDetails(noteDetails: NoteDetails){
        viewModelScope.launch(Dispatchers.IO) {
            noteDetailsDao.insert(noteDetails)
        }
    }

    fun deleteNoteDetails(noteDetails: NoteDetails){
        viewModelScope.launch(Dispatchers.IO){
            noteDetailsDao.delete(noteDetails)
        }
    }

    fun setSearchFieldVisibility(boolean : Boolean){
        _isSearchFieldVisible.value = boolean
    }

    fun searchNoteWithString(string : String){
        tempArrayListForSearchedNote.clear()
        readAllNote.value?.forEach {
            val listOfTitleWords = it.title.split(" ")
            if(isStringInTitle(listOfTitleWords,it.title, string)){
                tempArrayListForSearchedNote.add(it)
                _searchedNoteList.value = tempArrayListForSearchedNote
            }
        }
    }

    private fun isStringInTitle(list: List<String>, title : String, target: String): Boolean {
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

        for(i in 0 until title.length){
            try{
                if(title.substring(i, i + lengthOfTarget).equals(target,true)){
                    return true
                }
            }catch (e : Exception){}
        }
        return false
    }

}