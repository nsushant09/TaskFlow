package com.neupanesushant.note.koinmodules

import com.neupanesushant.note.fragments.note.AddNoteViewModel
import com.neupanesushant.note.fragments.note.NoteViewModel
import com.neupanesushant.note.fragments.quote.QuoteViewModel
import com.neupanesushant.note.fragments.todo.TodoHomeViewModel
import com.neupanesushant.note.fragments.todo.TodoTaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel {
        QuoteViewModel(get())
    }
    viewModel {
        AddNoteViewModel(get())
    }
    viewModel {
        NoteViewModel(get())
    }

    viewModel {
        TodoHomeViewModel(get(), get())
    }

    viewModel {
        TodoTaskViewModel(get(), get())
    }
}