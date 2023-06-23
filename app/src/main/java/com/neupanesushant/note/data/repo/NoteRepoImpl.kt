package com.neupanesushant.note.data.repo

import com.neupanesushant.note.domain.model.NoteDetails
import com.neupanesushant.note.data.dao.NoteDetailsDAO
import com.neupanesushant.note.data.repo.NoteRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NoteRepoImpl(private val noteDetailsDAO: NoteDetailsDAO) : NoteRepo {

    private var cachedAllNote: List<NoteDetails> = emptyList()
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

    override fun getAllCachedNotes(): List<NoteDetails> = cachedAllNote
    override fun setAllNotesCache(notes: List<NoteDetails>) {
        cachedAllNote = notes
    }
}