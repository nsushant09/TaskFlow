package com.neupanesushant.note.extras

interface GenericCallback<T> {
    fun callback(data: T, action : CallbackAction)
}