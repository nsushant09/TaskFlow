package com.neupanesushant.note.fragments.quote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.domain.model.Quote
import com.neupanesushant.note.data.repo.QuoteImpl
import com.neupanesushant.note.domain.model.UIState
import kotlinx.coroutines.launch

class QuoteViewModel(private val quoteImpl: QuoteImpl) : ViewModel() {

    private val _uiState = MutableLiveData<UIState>().apply {
        this.value = UIState.READY
    }
    val uiState: LiveData<UIState> get() = _uiState

    private val _listOfQuotes = MutableLiveData<List<Quote>>()
    val listOfQuotes: LiveData<List<Quote>> get() = _listOfQuotes

    init {
        viewModelScope.launch {
            quoteImpl.quotes.collect {
                _listOfQuotes.postValue(it.toList())
                _uiState.postValue(UIState.READY)
            }
        }
    }

    fun getContentData() {
        viewModelScope.launch {
            try {
                _uiState.postValue(UIState.LOADING)
                quoteImpl.getQuotes()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.postValue(UIState.ERROR)
            }
        }
    }

}