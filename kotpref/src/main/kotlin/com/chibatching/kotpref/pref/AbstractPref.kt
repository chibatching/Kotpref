package com.chibatching.kotpref.pref

import android.content.SharedPreferences
import android.os.SystemClock
import com.chibatching.kotpref.KotprefModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractPref<T : Any?> : ReadWriteProperty<KotprefModel, T>, PreferenceKey {

    private var lastUpdate: Long = 0
    private var transactionData: Any? = null

    abstract override val key: String?

    final override inline operator fun getValue(thisRef: KotprefModel, property: KProperty<*>): T =
        getValueInternal(thisRef, property.name)

    @PublishedApi internal fun getValueInternal(
        thisRef: KotprefModel,
        propertyName: String
    ): T {
        if (!thisRef.kotprefInTransaction) {
            return getFromPreference(propertyName, thisRef.kotprefPreference)
        }
        if (lastUpdate < thisRef.kotprefTransactionStartTime) {
            transactionData = getFromPreference(propertyName, thisRef.kotprefPreference)
            lastUpdate = SystemClock.uptimeMillis()
        }
        @Suppress("UNCHECKED_CAST")
        return transactionData as T
    }

    final override inline operator fun setValue(thisRef: KotprefModel, property: KProperty<*>, value: T) {
        setValueInternal(thisRef, value, property.name)
    }

    fun AbstractPref<T>.setValueInternal(
        thisRef: KotprefModel,
        value: T,
        propertyName: String
    ) {
        if (thisRef.kotprefInTransaction) {
            transactionData = value
            lastUpdate = SystemClock.uptimeMillis()
            setToEditor(propertyName, value, thisRef.kotprefEditor!!)
        } else {
            setToPreference(propertyName, value, thisRef.kotprefPreference)
        }
    }

    abstract fun getFromPreference(propertyName: String, preference: SharedPreferences): T
    abstract fun setToPreference(propertyName: String, value: T, preference: SharedPreferences)
    abstract fun setToEditor(propertyName: String, value: T, editor: SharedPreferences.Editor)
}
