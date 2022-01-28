package com.chibatching.kotpref.livedata

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.chibatching.kotpref.KotprefModel
import kotlin.reflect.KProperty0

public fun <T> KotprefModel.asLiveData(property: KProperty0<T>): LiveData<T> {
    return object : LiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {
        private val key: String = this@asLiveData.getPrefKey(property)
            ?: throw IllegalArgumentException("Failed to get preference key, check property ${property.name} is delegated to Kotpref")

        override fun onSharedPreferenceChanged(prefs: SharedPreferences, propertyName: String?) {
            // propertyName will be null when preferences are cleared on Android R
            val isCleared = propertyName == null
            if (isCleared || propertyName == key) {
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
