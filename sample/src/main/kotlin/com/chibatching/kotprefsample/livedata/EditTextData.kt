package com.chibatching.kotprefsample.livedata

import com.chibatching.kotpref.KotprefModel


object EditTextData : KotprefModel() {

    var savedText: String by stringPref("")
}
