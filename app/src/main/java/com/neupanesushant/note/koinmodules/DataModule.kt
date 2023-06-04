package com.neupanesushant.note.koinmodules

import android.app.Application
import androidx.room.Room
import com.neupanesushant.note.fragments.quote.model.QuoteImpl
import com.neupanesushant.note.fragments.quote.model.QuotesAPI
import com.neupanesushant.note.model.NoteDatabase
import com.neupanesushant.note.model.NoteDetailsDAO
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val BASEURL = "https://favqs.com/api/"

val dataModule = module {
    fun provideNoteDatabase(application: Application) : NoteDatabase {
        return Room.databaseBuilder(application, NoteDatabase::class.java, "user_database")
            .build()
    }

    fun provideNoteDetailsDao(database : NoteDatabase) : NoteDetailsDAO {
        return database.noteDetailsDao()
    }

    single(){
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesAPI::class.java)
    }

    single{
        provideNoteDatabase(androidApplication())
    }
    single{
        provideNoteDetailsDao(get())
    }

    single{
        QuoteImpl(get())
    }
}
