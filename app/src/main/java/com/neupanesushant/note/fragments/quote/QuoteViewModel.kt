package com.neupanesushant.note.fragments.quote

import android.app.Application
import androidx.lifecycle.*
import com.neupanesushant.note.fragments.quote.model.Quote
import com.neupanesushant.note.fragments.quote.model.QuotesAPI
import kotlinx.coroutines.launch

class QuoteViewModel(private val QuoteAPIService : QuotesAPI) : ViewModel(){

    val BASE_URL = "https://favqs.com/api/"

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listofQuotes = MutableLiveData<List<Quote>>()
    val listofQuotes: LiveData<List<Quote>> get() = _listofQuotes
    private val tempListOfQuotes = ArrayList<Quote>()

    init {
        _isLoading.value = true
        getListOfQuotes()
    }

    fun getListOfQuotes() {
        _isLoading.value = true
        tempListOfQuotes.clear()
        for (i in 0 until 10) {
            getContentData()
        }
    }


    fun getContentData() {
        viewModelScope.launch {
            try {
                tempListOfQuotes.add(QuoteAPIService.getData().quote)
                _listofQuotes.value = tempListOfQuotes.toList()
                if (_listofQuotes.value!!.size >= 10) {
                    _isLoading.value = false
                }
            } catch (e: Exception) {
            }
        }

    }

}