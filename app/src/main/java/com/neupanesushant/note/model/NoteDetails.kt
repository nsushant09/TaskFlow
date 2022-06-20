package com.neupanesushant.note.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notedetails_table")
data class NoteDetails (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val description : String,
    val date : String
        )