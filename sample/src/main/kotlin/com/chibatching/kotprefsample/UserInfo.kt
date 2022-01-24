package com.chibatching.kotprefsample

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import com.chibatching.kotpref.gsonpref.gsonPref
import com.chibatching.kotpref.pref.floatPref
import com.chibatching.kotpref.pref.intPref
import com.chibatching.kotpref.pref.longPref
import com.chibatching.kotpref.pref.nullableStringPref
import com.chibatching.kotpref.pref.stringPref
import com.chibatching.kotpref.pref.stringSetPref
import java.util.TreeSet

object UserInfo : KotprefModel() {
    var gameLevel by enumValuePref(GameLevel.NORMAL)
    var name by stringPref()
    var code by nullableStringPref()
    var age by intPref()
    var highScore by longPref()
    var rate by floatPref()
    val prizes by stringSetPref {
        val set = TreeSet<String>()
        set.add("Beginner")
        return@stringSetPref set
    }
    var avatar by gsonPref(Avatar())
}
