package com.neupanesushant.note.koinmodules

import com.neupanesushant.note.fragments.note.AddNoteViewModel
import com.neupanesushant.note.fragments.note.NoteViewModel
import com.neupanesushant.note.fragments.quote.QuoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel{
        QuoteViewModel(get())
    }
    viewModel{
        AddNoteViewModel(get())
    }
    viewModel{
        NoteViewModel(get())
    }
}