package com.neupanesushant.note.extras

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.neupanesushant.note.R
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getCurrentDate(): Date = Calendar.getInstance().time

    @SuppressLint("SimpleDateFormat")
    fun getTodayDate(): String {
        return SimpleDateFormat("dd/MM/yyyy").format(Utils.getCurrentDate()).toString()
    }

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

    fun Activity.hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun Context.showText(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun getRawFiles(context: Context, fileName: String): Int {
        return context.resources.getIdentifier(fileName, "raw", context.packageName)
    }

    fun showSnackBar(
        context: Context,
        view: View,
        snackbarMessage: String,
        buttonText: String,
        snackbarLength: Int,
        onClick: View.OnClickListener
    ) {
        val snackbar = Snackbar.make(view, snackbarMessage, snackbarLength)
            .setAction(buttonText, onClick)
            .setBackgroundTint(ContextCompat.getColor(context, R.color.grey))
            .setTextColor(ContextCompat.getColor(context, R.color.black))
            .setActionTextColor(ContextCompat.getColor(context, R.color.primarypurple))

        snackbar.show()
    }

    fun isTargetInString(string: String, target: String): Boolean {
        val lengthOfTarget = target.length
        val list = string.split(" ")
        for (i in list) {
            try {
                if (i.substring(0, lengthOfTarget).equals(target, true)) {
                    return true
                }
            } catch (e: StringIndexOutOfBoundsException) {
                continue
            }

        }

        for (i in string.indices) {
            try {
                if (string.substring(i, i + lengthOfTarget).equals(target, true)) {
                    return true
                }
            } catch (_: Exception) {
            }
        }
        return false
    }

    fun isBeforeOneWeek(time: Long): Boolean {
        return Date(time).before(Date())
    }
}


