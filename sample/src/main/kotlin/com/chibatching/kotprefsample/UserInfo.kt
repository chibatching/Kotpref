package com.chibatching.kotprefsample

import com.chibatching.kotpref.*
import java.util.*


public object UserInfo : KotprefModel() {
    var name: String by stringPrefVar()
    var age: Int by intPrefVar()
    var highScore: Long by longPrefVar()
    var threshold: Float by floatPrefVar()
    var prizes: MutableSet<String> by stringSetPrefVar(TreeSet<String>())
}