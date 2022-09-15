package com.neupanesushant.note.fragments.quote.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuotesAPI {
    @GET("qotd/")
    suspend fun getData(): QuoteDate
}

