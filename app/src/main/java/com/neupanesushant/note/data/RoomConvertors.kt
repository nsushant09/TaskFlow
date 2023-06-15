package com.neupanesushant.note.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neupanesushant.note.domain.model.Task

class RoomConvertors {
    companion object {

        @TypeConverter
        @JvmStatic
        fun toJson(obj: List<Task>): String? {
            return Gson().toJson(obj)
        }

        @TypeConverter
        @JvmStatic
        fun fromJsonListTask(obj: String): List<Task> {
            return Gson().fromJson(
                obj,
                object : TypeToken<List<Task>>() {}.type
            )
        }

        @TypeConverter
        @JvmStatic
        fun toJson(obj: Any): String? {
            return Gson().toJson(obj)
        }

        @TypeConverter
        @JvmStatic
        fun fromJson(obj: String): Any {
            return Gson().fromJson(
                obj,
                object : TypeToken<Any>() {}.type
            )
        }
    }
}