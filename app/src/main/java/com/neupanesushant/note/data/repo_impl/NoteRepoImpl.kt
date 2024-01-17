package com.neupanesushant.note.data.repo_impl

import com.neupanesushant.note.data.dao.NoteDetailsDAO
import com.neupanesushant.note.data.repo.NoteRepo
import com.neupanesushant.note.domain.model.NoteDetails
import kotlinx.coroutines.flow.Flow

class NoteRepoImpl(private val noteDetailsDAO: NoteDetailsDAO) : NoteRepo {
    override suspend fun insert(noteDetails: NoteDetails) {
        noteDetailsDAO.insert(noteDetails)
    }

    override suspend fun update(noteDetails: NoteDetails) {
        noteDetailsDAO.update(noteDetails)
    }

    override suspend fun delete(noteDetails: NoteDetails) {
        noteDetailsDAO.delete(noteDetails)
    }

    override suspend fun getAllNotes(): Flow<List<NoteDetails>> = noteDetailsDAO.getAllNote()
}