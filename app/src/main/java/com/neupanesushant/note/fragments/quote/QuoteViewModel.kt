package com.neupanesushant.note.fragments.quote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.data.repo.QuoteRepo
import com.neupanesushant.note.domain.model.Quote
import com.neupanesushant.note.domain.model.UIState
import com.neupanesushant.note.domain.usecase.QuoteUseCase
import kotlinx.coroutines.launch

class QuoteViewModel(private val quoteRepo: QuoteRepo) : ViewModel() {

    private val quoteUseCase = QuoteUseCase(quoteRepo)
    private val _listOfQuotes = MutableLiveData<UIState<List<Quote>>>()
    val listOfQuotes: LiveData<UIState<List<Quote>>> get() = _listOfQuotes

    init {
        _listOfQuotes.postValue(UIState.Loading)
        getQuotes()
    }

    private fun getQuotes() =
        viewModelScope.launch {
            val quotes = quoteUseCase.getQuotes()
            if (quotes.isEmpty()) {
                _listOfQuotes.postValue(UIState.Success(quotes))
                return@launch
            }
            _listOfQuotes.postValue(UIState.Success(quotes))
        }

}
