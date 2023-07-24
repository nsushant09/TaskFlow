package com.neupanesushant.note.data.repo

import com.neupanesushant.note.domain.model.Quote
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
            val numQuotes = 15

            val deferredQuotes = List(numQuotes) {
                async { quotesAPI.getData().quote }
            }

            val filteredQuotes = deferredQuotes.awaitAll().filter { quote ->
                quote.body.length > 15
            }

            tempList.clear()
            tempList.addAll(filteredQuotes)
            quotes.emit(tempList)
        }
    }
}

