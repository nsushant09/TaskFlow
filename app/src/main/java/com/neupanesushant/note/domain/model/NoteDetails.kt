package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "notedetails_table")
@kotlinx.parcelize.Parcelize
data class NoteDetails (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val description : String,
    val date : String
        ) : Parcelable