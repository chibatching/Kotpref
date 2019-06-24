package com.chibatching.kotpref.preference.data

import android.content.Context

abstract class BasePrefData<T> {

    abstract val key: String
    abstract val title: String
    abstract val resSummary: Int?
    abstract var value: T
    abstract val default: T

    fun summary(context: Context) =
        resSummary?.let { context.getString(it, value) } ?: value.toString()
}