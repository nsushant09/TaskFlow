package com.neupanesushant.note.fragments.quote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.data.repo.QuoteImpl
import com.neupanesushant.note.domain.model.Quote
import com.neupanesushant.note.domain.model.UIState
import kotlinx.coroutines.launch

class QuoteViewModel(private val quoteImpl: QuoteImpl) : ViewModel() {

    private val _listOfQuotes = MutableLiveData<UIState<List<Quote>>>()
    val listOfQuotes: LiveData<UIState<List<Quote>>> get() = _listOfQuotes

    init {
        _listOfQuotes.postValue(UIState.Loading)

        viewModelScope.launch {
            quoteImpl.quotes.collect {
                _listOfQuotes.postValue(UIState.Success(it.toList()))
            }
        }
    }

    fun getContentData() {
        viewModelScope.launch {
            try {
                _listOfQuotes.postValue(UIState.Loading)
                quoteImpl.getQuotes()
            } catch (e: Exception) {
                _listOfQuotes.postValue(UIState.Error(e))
            }
        }
    }

}