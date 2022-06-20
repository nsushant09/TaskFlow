package com.neupanesushant.note.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteDetails::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDetailsDao() : NoteDetaillsDAO

    companion object{
        @Volatile
        private var INSTANCE : NoteDatabase? = null
        fun getDatabase(context : Context) : NoteDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!= null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}