package com.chibatching.kotprefsample.autopreference

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotprefsample.R

object AutoPreferenceSettings : KotprefModel() {

    override val preferenceName = "Auto Preferences"

    var sampleSwitch by booleanPref(
        default = context.resources.getBoolean(R.bool.sample_switch_default),
        key = context.getString(R.string.pref_key_sample_switch),
        preferenceLabel = R.string.pref_title_sample_switch,
        preferenceSummary = R.string.pref_summary_sample_switch
    )

    var sampleEditText by stringPref(
        default = context.getString(R.string.sample_edit_text_default),
        key = context.getString(R.string.pref_key_sample_edit_text),
        preferenceLabel = R.string.pref_title_sample_edit_text,
        preferenceSummary = R.string.pref_summary_sample_edit_text
    )
}
