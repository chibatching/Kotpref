package com.chibatching.kotprefsample

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import com.chibatching.kotpref.gsonpref.gsonPref
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
