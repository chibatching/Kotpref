package com.chibatching.kotprefsample.preferencefragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chibatching.kotprefsample.R

class PreferenceFragmentSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_fragment_sample)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
