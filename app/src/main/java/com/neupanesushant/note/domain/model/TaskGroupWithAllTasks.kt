package com.neupanesushant.note.domain.model

import androidx.room.PrimaryKey

data class TaskGroupWithAllTasks(
    val id: Int,
    val name: String,
    val tasks: List<Task>
)