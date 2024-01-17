package com.neupanesushant.note.domain.usecase

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.neupanesushant.note.extras.Constants

class SharedPref(val context: Context) {
    val instance
        get() = context.getSharedPreferences(
            Constants.SHARED_PREFERENCES,
            MODE_PRIVATE
        )
    val editor get() = instance.edit()
}