package com.neupanesushant.note.fragments.quote.model

import com.neupanesushant.note.domain.model.QuoteDate
import retrofit2.http.GET

interface QuotesAPI {
    @GET("qotd/")
    suspend fun getData(): QuoteDate
}

