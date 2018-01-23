package com.chibatching.kotpref.livedata

import android.arch.lifecycle.LiveData
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import kotlin.reflect.KProperty0

fun <T> KotprefModel.asLiveData(property: KProperty0<T>): LiveData<T> {
    return object : LiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {
        init {
            value = property.get()
        }

        override fun onSharedPreferenceChanged(prefs: SharedPreferences, propertyName: String) {
            if (propertyName == property.name) {
                postValue(property.get())
            }
        }

        override fun onActive() {
            this@asLiveData.preferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onInactive() {
            this@asLiveData.preferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }
}