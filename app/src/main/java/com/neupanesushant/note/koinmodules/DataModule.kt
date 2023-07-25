package com.neupanesushant.note.koinmodules

import android.app.Application
import androidx.room.Room
import com.neupanesushant.note.data.dao.NoteDetailsDAO
import com.neupanesushant.note.data.dao.TaskDAO
import com.neupanesushant.note.data.dao.TaskGroupDAO
import com.neupanesushant.note.data.repo.*
import com.neupanesushant.note.domain.model.Database
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

    single<TaskRepo> {
        TaskRepoImpl(get())
    }

    single<TaskGroupRepo> {
        TaskGroupRepoImpl(get(), get())
    }

    single<NoteRepo> {
        NoteRepoImpl(get())
    }
}
