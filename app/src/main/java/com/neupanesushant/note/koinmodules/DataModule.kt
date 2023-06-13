package com.neupanesushant.note.koinmodules

import android.app.Application
import androidx.room.Room
import com.neupanesushant.note.fragments.quote.model.QuoteImpl
import com.neupanesushant.note.fragments.quote.model.QuotesAPI
import com.neupanesushant.note.domain.model.Database
import com.neupanesushant.note.domain.repo.NoteDetailsDAO
import com.neupanesushant.note.domain.repo.TaskDAO
import com.neupanesushant.note.domain.repo.TaskGroupDAO
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val BASEURL = "https://favqs.com/api/"

val dataModule = module {
    fun provideNoteDatabase(application: Application): Database {
        return Room.databaseBuilder(application, Database::class.java, "user_database")
            .build()
    }

    fun provideNoteDetailsDao(database: Database): NoteDetailsDAO {
        return database.noteDetailsDao()
    }

    fun provideTaskGroupDao(database: Database): TaskGroupDAO {
        return database.taskGroupDao()
    }

    fun provideTaskDao(database: Database): TaskDAO {
        return database.taskDao()
    }

    single() {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesAPI::class.java)
    }

    single {
        provideNoteDatabase(androidApplication())
    }
    single {
        provideNoteDetailsDao(get())
    }
    single {
        provideTaskDao(get())
    }
    single {
        provideTaskGroupDao(get())
    }

    single {
        QuoteImpl(get())
    }
}
