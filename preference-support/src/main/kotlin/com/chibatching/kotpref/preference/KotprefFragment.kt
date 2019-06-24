package com.chibatching.kotpref.preference

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragment
import androidx.preference.PreferenceScreen
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.preference.data.BasePrefData
import com.chibatching.kotpref.preference.data.BooleanPrefData
import com.chibatching.kotpref.preference.data.StringPrefData

class KotprefPrefFragment : PreferenceFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        const val PREF_NAME = "name"

        fun create(preferenceModel: KotprefModel): KotprefPrefFragment {
            val fragment = KotprefPrefFragment()
            val args = Bundle()
            PreferenceUtil.putKotprefObject(args, preferenceModel)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var kotPref: KotprefModel
    private lateinit var preferenceData: List<BasePrefData<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        kotPref = PreferenceUtil.getKotprefObject(arguments)
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferencesName = kotPref.kotprefName
        preferenceManager.sharedPreferencesMode = kotPref.kotprefMode
    }

    override fun onDestroy() {
        // if KotprefModule would support internal caching, we would need to invalidate cache here!
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        val screen = getPreferenceManager().createPreferenceScreen(activity)
        val parent: Any = screen
        preferenceData = PreferenceUtil.getPreferenceDatas(activity, kotPref)

        for (p in preferenceData) {
            when (p) {
                is StringPrefData -> addString(p.key, p.title, p.summary(activity), p.value, parent)
                is BooleanPrefData -> addBoolean(p.key, p.title, p.summary(activity), p.value, parent)
                else -> {
                    // currently silently ignored...
                }
            }
        }

        // set preference screen
        preferenceScreen = screen
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val pref = findPreference(key)

        val prefData = preferenceData.find { it.key.equals(pref.key) }

        when (prefData) {
            is StringPrefData -> prefData.value =
                preferenceScreen.sharedPreferences.getString(key, prefData.default)
            is BooleanPrefData -> prefData.value =
                preferenceScreen.sharedPreferences.getBoolean(key, prefData.default)
        }

        prefData?.let {
            pref.summary = it.summary(activity)
        }
    }
    // ----------------
    // functions
    // ----------------

    private fun addCategory(screen: PreferenceScreen, title: Int): PreferenceCategory {
        val category = PreferenceCategory(activity)
        category.title = getString(title)
        screen.addPreference(category)
        return category
    }

    private fun addBoolean(
        key: String,
        title: String,
        summary: String,
        value: Boolean,
        parent: Any
    ) {
        val checkBoxPref = CheckBoxPreference(activity)
        checkBoxPref.key = key
        checkBoxPref.title = title
        checkBoxPref.summary = summary
        checkBoxPref.isChecked = value
        addPreference(checkBoxPref, parent)
    }

    private fun addString(key: String, title: String, summary: String, value: String, parent: Any) {
        val editTextPref = EditTextPreference(activity)
        editTextPref.key = key
        editTextPref.title = title
        editTextPref.summary = summary
        editTextPref.text = value
        editTextPref.dialogTitle = title
        editTextPref.dialogLayoutResource = R.layout.preference_dialog_edittext
        addPreference(editTextPref, parent)
    }

    private fun addPreference(preference: Preference, parent: Any) {
        when (parent) {
            is PreferenceScreen -> parent.addPreference(preference)
            is PreferenceCategory -> parent.addPreference(preference)
        }
    }
}