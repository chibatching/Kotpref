package com.chibatching.kotpref.preference.data

class BooleanPrefData(
    override val key: String,
    override val title: String,
    override val resSummary: Int?,
    override var value: Boolean,
    override var default: Boolean
) : BasePrefData<Boolean>()