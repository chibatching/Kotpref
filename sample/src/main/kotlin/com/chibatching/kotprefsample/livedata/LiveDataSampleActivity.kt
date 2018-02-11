package com.chibatching.kotprefsample.livedata

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chibatching.kotpref.livedata.asLiveData
import com.chibatching.kotprefsample.R
import kotlinx.android.synthetic.main.activity_live_data_sample.*

class LiveDataSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_sample)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        saveButton.setOnClickListener {
            EditTextData.savedText = editText.text.toString()
        }

        EditTextData
            .asLiveData(EditTextData::savedText)
            .observe(this, Observer<String> {
                textView.text = it
            })
    }
}
