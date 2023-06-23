package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.neupanesushant.note.extras.Constants

@Entity(tableName = Constants.TASK_TABLE)
@TypeConverters(RoomConvertors::class)
@kotlinx.parcelize.Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String,
    var isCompleted: Boolean,
    var date: String,
    val groupId: Int
) : Parcelable