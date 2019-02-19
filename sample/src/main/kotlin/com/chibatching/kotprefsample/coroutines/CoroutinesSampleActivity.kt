package com.chibatching.kotprefsample.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.coroutines.asCoroutine
import com.chibatching.kotprefsample.R
import kotlinx.android.synthetic.main.activity_live_data_sample.*
import kotlinx.coroutines.runBlocking

class CoroutinesSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines_sample)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        saveButton.setOnClickListener {
            saveData()
        }

        getData()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (super.onSupportNavigateUp()) {
            return true
        }
        finish()
        return true
    }

    private fun saveData() = runBlocking {
        CoroutineTestData.savedText = editText.text.toString()
        getData()
    }

    private fun getData() = runBlocking {
        val deferred = CoroutineTestData.asCoroutine(CoroutineTestData::savedText)
        val value = deferred.await()
        textView.text = value
    }
}