package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neupanesushant.note.extras.Constants

@Entity(tableName = Constants.NOTEDETAILS_TABLE)
@kotlinx.parcelize.Parcelize
data class NoteDetails (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val description : String,
    val date : String
        ) : Parcelable