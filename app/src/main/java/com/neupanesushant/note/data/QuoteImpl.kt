package com.neupanesushant.note.data

import com.neupanesushant.note.domain.model.Quote
import com.neupanesushant.note.domain.repo.QuotesAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class QuoteImpl(private val quotesAPI: QuotesAPI) {


    val quotes: MutableStateFlow<ArrayList<Quote>> = MutableStateFlow(arrayListOf())

    suspend fun getQuotes() {
        withContext(Dispatchers.IO) {
            val tempList = arrayListOf<Quote>()
            val numQuotes = 10

            val deferredQuotes = List(numQuotes) {
                async { quotesAPI.getData().quote }
            }

            tempList.addAll(deferredQuotes.awaitAll())
            quotes.value = tempList
        }
    }


}