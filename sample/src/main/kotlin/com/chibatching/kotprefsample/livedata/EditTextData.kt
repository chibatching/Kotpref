package com.chibatching.kotprefsample.livedata

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.stringPref

object EditTextData : KotprefModel() {

    var savedText: String by stringPref("")
}
