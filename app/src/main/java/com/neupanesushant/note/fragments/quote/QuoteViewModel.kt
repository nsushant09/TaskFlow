package com.neupanesushant.note.fragments.quote

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neupanesushant.note.fragments.quote.model.Quote
import com.neupanesushant.note.fragments.quote.model.QuoteDate
import com.neupanesushant.note.fragments.quote.model.QuotesAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val  BASE_URL= "https://favqs.com/api/"

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val _listofQuotes = MutableLiveData<List<Quote>>()
    val listofQuotes : LiveData<List<Quote>> get() = _listofQuotes
    private val tempListOfQuotes = ArrayList<Quote>()

    init{
        _isLoading.value = true
        getListOfQuotes()
    }

    fun getListOfQuotes(){
        _isLoading.value = true
        tempListOfQuotes.clear()
        for(i in 0 until 10){
            getContentData()
        }
    }


    fun getContentData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(QuotesAPI::class.java)


        val retrofitData = retrofit.getData()
        retrofitData.enqueue(object : Callback<QuoteDate> {
            override fun onResponse(call: Call<QuoteDate>, response: Response<QuoteDate>) {
                if(response.body()!=null){
                    tempListOfQuotes.add(response.body()!!.quote)
                    _listofQuotes.value = tempListOfQuotes.toList()
                    if(_listofQuotes.value!!.size >= 10){
                        _isLoading.value = false
                    }
                }
            }

            override fun onFailure(call: Call<QuoteDate>, t: Throwable) {
            }

        })

    }

}