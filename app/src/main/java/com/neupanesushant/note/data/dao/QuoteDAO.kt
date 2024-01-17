package com.neupanesushant.note.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neupanesushant.note.domain.model.Quote
import com.neupanesushant.note.extras.Constants

@Dao
interface QuoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: Quote)

    @Query("SELECT * FROM ${Constants.QUOTE_TABLE} ORDER BY id DESC")
    suspend fun getQuotes(): List<Quote>

    @Query("DELETE FROM ${Constants.QUOTE_TABLE}")
    suspend fun deleteQuotes(): Int

}