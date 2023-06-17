package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.neupanesushant.note.data.RoomConvertors
import com.neupanesushant.note.extras.Constants

@Entity(tableName = Constants.TASK_TABLE)
@TypeConverters(RoomConvertors::class)
@kotlinx.parcelize.Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val date: String,
    val groupId: Int
) : Parcelable