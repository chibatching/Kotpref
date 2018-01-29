package com.chibatching.kotpref.livedata

import android.arch.lifecycle.LiveData
import android.content.SharedPreferences
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.pref.PreferenceKey
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

fun <T> KotprefModel.asLiveData(property: KProperty0<T>): LiveData<T> {
    return object : LiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {
        private val key: String

        init {
            value = property.get()
            property.isAccessible = true
            key = (property.getDelegate() as? PreferenceKey)?.key ?: property.name
            property.isAccessible = false
        }

        override fun onSharedPreferenceChanged(prefs: SharedPreferences, propertyName: String) {
            if (propertyName == key) {
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
