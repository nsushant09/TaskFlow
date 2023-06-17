package com.neupanesushant.note

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnScrollRefreshListener(onScrollToTop: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val isAtTop = firstVisibleItemPosition == 0 && recyclerView.getChildAt(0)?.top == 0
            if (isAtTop && dy < -100) {
                onScrollToTop()
            }
        }
    })
}

fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}