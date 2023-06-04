package com.neupanesushant.note.fragments.quote

import android.app.Application
import androidx.lifecycle.*
import com.neupanesushant.note.fragments.quote.model.Quote
import com.neupanesushant.note.fragments.quote.model.QuoteImpl
import com.neupanesushant.note.fragments.quote.model.QuotesAPI
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuoteViewModel(private val quoteImpl: QuoteImpl) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listofQuotes = MutableLiveData<List<Quote>>()
    val listofQuotes: LiveData<List<Quote>> get() = _listofQuotes

    init {
        getContentData()

        viewModelScope.launch {
            quoteImpl.quotes.collect {
                _listofQuotes.postValue(it.toList())
            }
        }
    }

    private fun getContentData() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            quoteImpl.getQuotes()
        }.invokeOnCompletion {
            _isLoading.value = false
        }
    }

}