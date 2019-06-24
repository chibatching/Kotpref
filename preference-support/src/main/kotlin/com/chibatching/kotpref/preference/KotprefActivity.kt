package com.chibatching.kotpref.preference

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.KotprefModel

class KotprefPrefActivity : AppCompatActivity() {

    companion object {

        fun show(activity: AppCompatActivity, preferenceModel: KotprefModel) {
            val intent = Intent(activity, KotprefPrefActivity::class.java)
            PreferenceUtil.putKotprefObject(intent, preferenceModel)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val kotPref = PreferenceUtil.getKotprefObject(intent)

        supportActionBar?.setTitle(kotPref.preferenceName)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tag = KotprefPrefFragment::class.java.name
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            val prefsFragment =
                KotprefPrefFragment.create(kotPref)
            fragmentManager
                .beginTransaction()
                .replace(android.R.id.content, prefsFragment, tag)
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (super.onSupportNavigateUp()) {
            return true
        }
        finish()
        return true
    }
}