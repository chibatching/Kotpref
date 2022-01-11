package com.chibatching.kotprefsample.livedata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.chibatching.kotpref.livedata.asLiveData
import com.chibatching.kotprefsample.databinding.ActivityLiveDataSampleBinding

class LiveDataSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLiveDataSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveButton.setOnClickListener {
            EditTextData.savedText = binding.editText.text.toString()
        }

        EditTextData
            .asLiveData(EditTextData::savedText)
            .observe(this, Observer<String> {
                binding.textView.text = it
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        if (super.onSupportNavigateUp()) {
            return true
        }
        finish()
        return true
    }
}
