package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
@kotlinx.parcelize.Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted : Boolean,
    val date : String
) : Parcelable