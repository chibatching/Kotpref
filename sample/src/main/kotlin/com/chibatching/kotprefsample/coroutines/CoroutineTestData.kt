package com.chibatching.kotprefsample.coroutines

import com.chibatching.kotpref.KotprefModel

object CoroutineTestData : KotprefModel() {

    var savedText: String by stringPref("default value")
}
