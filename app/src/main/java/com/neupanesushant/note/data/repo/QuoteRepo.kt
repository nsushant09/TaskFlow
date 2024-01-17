package com.neupanesushant.note.data.repo

import com.neupanesushant.note.domain.model.Quote

interface QuoteRepo {
    suspend fun insert(quote: Quote)
    suspend fun deleteQuotes()
    suspend fun getQuotes(): List<Quote>
}