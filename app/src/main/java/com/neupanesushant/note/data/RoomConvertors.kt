package com.neupanesushant.note.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConvertors {
    companion object {
        @TypeConverter
        @JvmStatic
        fun saveAny(obj: Any): String? {
            return Gson().toJson(obj)
        }

        @TypeConverter
        @JvmStatic
        fun getFromAny(obj: String): Any {
            return Gson().fromJson(
                obj,
                object : TypeToken<Any>() {}.type
            )
        }
    }
}