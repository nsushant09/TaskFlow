package com.neupanesushant.note.fragments.quote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neupanesushant.note.domain.model.Quote
import com.neupanesushant.note.data.QuoteImpl
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