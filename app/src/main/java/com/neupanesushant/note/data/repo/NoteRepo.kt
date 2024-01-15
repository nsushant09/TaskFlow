package com.neupanesushant.note.data.repo

import com.neupanesushant.note.domain.model.NoteDetails
import kotlinx.coroutines.flow.Flow

interface NoteRepo {
    suspend fun insert(noteDetails: NoteDetails)
    suspend fun update(noteDetails: NoteDetails)
    suspend fun delete(noteDetails: NoteDetails)
    suspend fun getAllNotes(): Flow<List<NoteDetails>>
}