package com.chibatching.kotprefsample.preferencefragment

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.booleanPref
import com.chibatching.kotpref.pref.stringPref
import com.chibatching.kotprefsample.R

object AppSettings : KotprefModel() {

    var sampleSwitch by booleanPref(
        default = context.resources.getBoolean(R.bool.sample_switch_default),
        key = context.getString(R.string.pref_key_sample_switch)
    )

    var sampleEditText by stringPref(
        default = context.getString(R.string.sample_edit_text_default),
        key = context.getString(R.string.pref_key_sample_edit_text)
    )
}
