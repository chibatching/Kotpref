package com.chibatching.kotpref.livedata

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.chibatching.kotpref.KotprefModel
import kotlin.reflect.KProperty0

fun <T> KotprefModel.asLiveData(property: KProperty0<T>): LiveData<T> {
    return object : LiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {
        private val key: String = this@asLiveData.getPrefKey(property)
            ?: throw IllegalArgumentException("Failed to get preference key, check property ${property.name} is delegated to Kotpref")

        init {
            postValue(property.get())
        }

        override fun onSharedPreferenceChanged(prefs: SharedPreferences, propertyName: String) {
            if (propertyName == key) {
                postValue(property.get())
            }
        }

        override fun onActive() {
            this@asLiveData.preferences.registerOnSharedPreferenceChangeListener(this)
            if (value != property.get()) {
                value = property.get()
            }
        }

        override fun onInactive() {
            this@asLiveData.preferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }
}
