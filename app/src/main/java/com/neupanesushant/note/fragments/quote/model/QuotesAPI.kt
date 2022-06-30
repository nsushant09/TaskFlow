package com.neupanesushant.note.fragments.quote.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val  BASE_URL= "https://favqs.com/api/"
val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface QuotesAPI {
    @GET("qotd/")
    suspend fun getData(): QuoteDate
}

object QuotesAPIService{
    val retrofitService : QuotesAPI by lazy { retrofit.create(QuotesAPI::class.java) }
}