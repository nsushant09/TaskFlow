package com.neupanesushant.note.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.neupanesushant.note.extras.Constants

@Entity(tableName = Constants.TASKGROUP_TABLE)
@TypeConverters(RoomConvertors::class)
@kotlinx.parcelize.Parcelize
data class TaskGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
) : Parcelable