package com.chibatching.kotprefsample.encrypt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotprefsample.databinding.ActivityEncryptionBinding
import java.io.File

class EncryptionSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEncryptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = EncryptionSamplePref(this)

        binding.editText.setText(pref.token)
        binding.encryptedText.text = readSharedPrefFile(pref)

        binding.saveButton.setOnClickListener {
            pref.token = binding.editText.text.toString()
            binding.editText.setText(pref.token)

            binding.encryptedText.text = readSharedPrefFile(pref)
        }
    }

    private fun readSharedPrefFile(pref: EncryptionSamplePref): String =
        File(filesDir.parent, "shared_prefs/${pref.kotprefName}.xml").readText()
}
