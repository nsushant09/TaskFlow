package com.neupanesushant.note

import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.CalendarContract.Calendars
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.*

object Utils {
    fun getCurrentDate(): Date = Calendar.getInstance().time


    @RequiresApi(Build.VERSION_CODES.Q)
    fun showKeyboard(activity: Activity?, view: View) {
        view.requestFocus()
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun hideKeyboard(activity: Activity?, view: View) {
        view.clearFocus()
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}