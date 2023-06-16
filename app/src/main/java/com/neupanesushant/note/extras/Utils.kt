package com.neupanesushant.note.extras

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Message
import android.provider.CalendarContract.Calendars
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

    fun Context.showText(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}