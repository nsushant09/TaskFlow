package com.neupanesushant.note.domain.dao

import androidx.room.*
import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.extras.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDetailsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteDetails: NoteDetails)

    @Update
    suspend fun update(noteDetails: NoteDetails)

    @Delete
    suspend fun delete(noteDetails: NoteDetails)

    @Query("SELECT * FROM ${Constants.NOTEDETAILS_TABLE} ORDER BY id DESC")
    fun readAllNote(): Flow<List<NoteDetails>>
}