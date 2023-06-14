package com.neupanesushant.note.extras.adapter

import androidx.viewbinding.ViewBinding

fun interface RecyclerCallback<VM : ViewBinding, T> {
    fun bindData(binding: VM, item: T, listItems: List<T>)
}