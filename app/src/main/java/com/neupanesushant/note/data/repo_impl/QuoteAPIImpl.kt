package com.neupanesushant.note.data.repo_impl

import com.neupanesushant.note.data.repo.QuotesAPI
import com.neupanesushant.note.domain.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class QuoteAPIImpl(private val quotesAPI: QuotesAPI) {
    suspend fun getQuotes(): List<Quote> {
        return withContext(Dispatchers.IO) {
            val tempList = arrayListOf<Quote>()
            val numQuotes = 10

            val deferredQuotes = List(numQuotes) {
                async { quotesAPI.getData().quote }
            }

            val filteredQuotes = deferredQuotes.awaitAll().filter { quote ->
                quote.body.length > 15
            }

            tempList.clear()
            tempList.addAll(filteredQuotes)
            tempList
        }
    }
}

