package com.chibatching.kotpref.preference.data

class StringPrefData(
    override val key: String,
    override val title: String,
    override val resSummary: Int?,
    override var value: String,
    override var default: String
) : BasePrefData<String>()