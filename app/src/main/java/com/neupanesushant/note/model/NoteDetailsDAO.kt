package com.neupanesushant.note.model

import androidx.lifecycle.LiveData
import androidx.room.*

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