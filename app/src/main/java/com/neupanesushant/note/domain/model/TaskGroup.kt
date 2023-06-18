package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.*
import com.neupanesushant.note.extras.Constants
import com.neupanesushant.note.data.RoomConvertors

@Entity(tableName = Constants.TASKGROUP_TABLE)
@TypeConverters(RoomConvertors::class)
@kotlinx.parcelize.Parcelize
data class TaskGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
) : Parcelable