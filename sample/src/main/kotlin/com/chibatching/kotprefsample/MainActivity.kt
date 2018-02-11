package com.chibatching.kotprefsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.chibatching.kotpref.bulk
import com.chibatching.kotprefsample.livedata.LiveDataSampleActivity
import com.chibatching.kotprefsample.preferencefragment.PreferenceFragmentSampleActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        writeAndReadSample()

        preferenceFragmentSampleButton.setOnClickListener {
            startActivity(Intent(this, PreferenceFragmentSampleActivity::class.java))
        }

        liveDataSampleButton.setOnClickListener {
            startActivity(Intent(this, LiveDataSampleActivity::class.java))
        }
    }

    private fun writeAndReadSample() {
        Log.d(TAG, "Game level: ${UserInfo.gameLevel}")
        Log.d(TAG, "User name: ${UserInfo.name}")
        Log.d(TAG, "User code: ${UserInfo.code}")
        Log.d(TAG, "User age: ${UserInfo.age}")
        Log.d(TAG, "User high score: ${UserInfo.highScore}")
        Log.d(TAG, "User rate: ${UserInfo.rate}")
        Log.d(TAG, "Avatar: ${UserInfo.avatar}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(TAG, "prize[$i]: $s") }

        UserInfo.gameLevel = GameLevel.HARD
        UserInfo.name = "chibatching"
        UserInfo.code = "DAEF2599-7FC9-49C5-9A11-3C12B14A6898"
        UserInfo.age = 30
        UserInfo.highScore = 49219902
        UserInfo.rate = 49.21F
        UserInfo.prizes.add("Bronze")
        UserInfo.prizes.add("Silver")
        UserInfo.prizes.add("Gold")
        UserInfo.avatar = Avatar("bird", Date())

        Log.d(TAG, "Game level: ${UserInfo.gameLevel}")
        Log.d(TAG, "User name: ${UserInfo.name}")
        Log.d(TAG, "User code: ${UserInfo.code}")
        Log.d(TAG, "User age: ${UserInfo.age}")
        Log.d(TAG, "User high score: ${UserInfo.highScore}")
        Log.d(TAG, "User rate: ${UserInfo.rate}")
        Log.d(TAG, "Avatar: ${UserInfo.avatar}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(TAG, "prize[$i]: $s") }

        UserInfo.bulk {
            gameLevel = GameLevel.EASY
            name = "chibatching Jr"
            code = "451B65F6-EF95-4C2C-AE76-D34535F51B3B"
            age = 2
            highScore = 3901
            rate = 0.4F
            prizes.clear()
            prizes.add("New Born")
            avatar = Avatar("lion", Date())
        }

        Log.d(TAG, "Game level: ${UserInfo.gameLevel}")
        Log.d(TAG, "User name: ${UserInfo.name}")
        Log.d(TAG, "User code: ${UserInfo.code}")
        Log.d(TAG, "User age: ${UserInfo.age}")
        Log.d(TAG, "User high score: ${UserInfo.highScore}")
        Log.d(TAG, "User rate: ${UserInfo.rate}")
        Log.d(TAG, "Avatar: ${UserInfo.avatar}")
        UserInfo.prizes.forEachIndexed { i, s -> Log.d(TAG, "prize[$i]: $s") }
    }
}
