package com.neupanesushant.note.fragments.quote.model

import com.neupanesushant.note.domain.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class QuoteImpl(private val quotesAPI: QuotesAPI) {


    val quotes: MutableStateFlow<ArrayList<Quote>> = MutableStateFlow(arrayListOf())

    suspend fun getQuotes() {
        withContext(Dispatchers.IO) {
            val tempList = arrayListOf<Quote>()
            for (i in 0 until 10) {
                tempList.add(quotesAPI.getData().quote)
            }
            quotes.value = tempList
        }
    }


}