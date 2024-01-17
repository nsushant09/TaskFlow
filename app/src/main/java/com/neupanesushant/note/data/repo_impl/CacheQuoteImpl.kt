package com.neupanesushant.note.data.repo_impl

import com.neupanesushant.note.data.dao.QuoteDAO
import com.neupanesushant.note.data.repo.QuoteRepo
import com.neupanesushant.note.domain.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class CacheQuoteImpl(private val dao: QuoteDAO, private val quoteAPIImpl: QuoteAPIImpl) : QuoteRepo,
    KoinComponent {
    override suspend fun insert(quote: Quote) {
        dao.insert(quote)
    }

    override suspend fun deleteQuotes() {
        dao.deleteQuotes()
    }

    override suspend fun getQuotes(): List<Quote> {
        return try {
            quoteAPIImpl.getQuotes()
        } catch (e: Exception) {
            dao.getQuotes()
        }
    }

}