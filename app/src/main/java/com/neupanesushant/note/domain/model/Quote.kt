package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neupanesushant.note.extras.Constants

@Entity(tableName = Constants.QUOTE_TABLE)
@kotlinx.parcelize.Parcelize
data class Quote(
    val author: String,
    val body: String,
    @PrimaryKey
    val id: Int,
) : Parcelable