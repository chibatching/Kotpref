package com.chibatching.kotprefsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.chibatching.kotpref.Kotpref
import java.util.TreeSet

public class MainActivity : AppCompatActivity() {

    private var userName: String by Kotpref.stringPrefVar({ this })
    private var userAge: Int by Kotpref.intPrefVar({ this }, default = 18)
    private var userScore: Long by Kotpref.longPrefVar({ this })
    private var threshold: Float by Kotpref.floatPrefVar({ this })
    private var enableFunc: Boolean by Kotpref.booleanPrefVar({ this }, default = true)
    private var itemCart: MutableSet<String> by Kotpref.stringSetPrefVar({ this }, default = TreeSet<String>())

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(javaClass.getSimpleName(), "userName: $userName")
        Log.d(javaClass.getSimpleName(), "userAge: $userAge")
        Log.d(javaClass.getSimpleName(), "userScore: $userScore")
        Log.d(javaClass.getSimpleName(), "threshold: $threshold")
        Log.d(javaClass.getSimpleName(), "enableFunc: $enableFunc")
        Log.d(javaClass.getSimpleName(), "itemCart size: ${itemCart.size()}")
        itemCart.forEachIndexed { index, value ->
            Log.d(javaClass.getSimpleName(), "itemCart[$index]: $value")
        }

        userName = "chibatching"
        userAge = 30
        userScore = 212434085
        threshold = 0.3551f
        enableFunc = false
        itemCart.add("aaa")
        itemCart.add("bbb")
        itemCart.add("ccc")

        Log.d(javaClass.getSimpleName(), "userName: $userName")
        Log.d(javaClass.getSimpleName(), "userAge: $userAge")
        Log.d(javaClass.getSimpleName(), "userScore: $userScore")
        Log.d(javaClass.getSimpleName(), "threshold: $threshold")
        Log.d(javaClass.getSimpleName(), "enableFunc: $enableFunc")
        Log.d(javaClass.getSimpleName(), "itemCart size: ${itemCart.size()}")
        itemCart.forEachIndexed { index, value ->
            Log.d(javaClass.getSimpleName(), "itemCart[$index]: $value")
        }

        // Using AppPreference object
        AppPreferences.context = this

        Log.d(javaClass.getSimpleName(), "User name: ${AppPreferences.userName}")
        Log.d(javaClass.getSimpleName(), "User nickname: ${AppPreferences.userNickName}")
        Log.d(javaClass.getSimpleName(), "User age: ${AppPreferences.userAge}")
        AppPreferences.userAge = 23
        AppPreferences.userName = "Hoge Fuga"
        AppPreferences.userNickName = "chibatching"
        Log.d(javaClass.getSimpleName(), "User name: ${AppPreferences.userName}")
        Log.d(javaClass.getSimpleName(), "User nickname: ${AppPreferences.userNickName}")
        Log.d(javaClass.getSimpleName(), "User age: ${AppPreferences.userAge}")

        Log.d(javaClass.getSimpleName(), "Enable func1: ${AppPreferences.enableFunc1}")
        Log.d(javaClass.getSimpleName(), "Threshold: ${AppPreferences.threshold}")
        AppPreferences.enableFunc1 = false
        AppPreferences.threshold = 80322
        Log.d(javaClass.getSimpleName(), "Enable func1: ${AppPreferences.enableFunc1}")
        Log.d(javaClass.getSimpleName(), "Threshold: ${AppPreferences.threshold}")
    }
}
