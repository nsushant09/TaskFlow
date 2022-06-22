package com.neupanesushant.note.fragments.quote.model

import retrofit2.Call
import retrofit2.http.GET

interface QuotesAPI {
    @GET("qotd/")
    fun getData() : Call<QuoteDate>
}