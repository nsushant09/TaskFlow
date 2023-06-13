package com.neupanesushant.note.domain.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.neupanesushant.note.domain.model.NoteDetails

@Dao
interface NoteDetailsDAO{

    @Insert
    suspend fun insert(noteDetails : NoteDetails)

    @Update
    suspend fun update(noteDetails : NoteDetails)

    @Delete
    suspend fun delete(noteDetails : NoteDetails)

    @Query("SELECT * FROM notedetails_table ORDER BY id DESC")
    fun readAllNote() : LiveData<List<NoteDetails>>
}