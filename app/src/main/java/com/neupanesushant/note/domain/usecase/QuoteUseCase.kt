package com.neupanesushant.note.domain.usecase

import com.neupanesushant.note.data.repo.QuoteRepo
import com.neupanesushant.note.domain.model.Quote
import com.neupanesushant.note.extras.Utils
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuoteUseCase(private val quoteRepo: QuoteRepo) : KoinComponent {
    private val sharedPref: SharedPref by inject()
    suspend fun getQuotes(): List<Quote> {
        val quotes = quoteRepo.getQuotes()
        if (isUpdateNeeded) {
            updateQuotesInDB(quotes)
        }
        return quotes
    }

    private val isUpdateNeeded: Boolean
        get() {
            val time = sharedPref.instance.getLong("quote_update_time", -1)
            if (time == -1L) return true
            return Utils.isBeforeOneWeek(time)
        }

    private suspend fun updateQuotesInDB(quotes: List<Quote>) =
        runBlocking {
            quoteRepo.deleteQuotes()
            quotes.forEach { quoteRepo.insert(it) }
        }

}