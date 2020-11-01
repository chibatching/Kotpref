package com.chibatching.kotpref.pref

import android.content.SharedPreferences
import android.os.SystemClock
import com.chibatching.kotpref.KotprefModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public abstract class AbstractPref<T : Any?> : ReadWriteProperty<KotprefModel, T>, PreferenceProperty {

    private var lastUpdate: Long = 0
    private var transactionData: Any? = null

    public abstract val key: String?

    private lateinit var property: KProperty<*>

    override val propertyName: String
        get() = property.name

    override val preferenceKey: String
        get() = key ?: property.name

    public operator fun provideDelegate(
        thisRef: KotprefModel,
        property: KProperty<*>
    ): ReadWriteProperty<KotprefModel, T> {
        this.property = property
        thisRef.kotprefProperties[property.name] = this
        return this
    }

    override operator fun getValue(thisRef: KotprefModel, property: KProperty<*>): T {
        if (!thisRef.kotprefInTransaction) {
            return getFromPreference(property, thisRef.kotprefPreference)
        }
        if (lastUpdate < thisRef.kotprefTransactionStartTime) {
            transactionData = getFromPreference(property, thisRef.kotprefPreference)
            lastUpdate = SystemClock.uptimeMillis()
        }
        @Suppress("UNCHECKED_CAST")
        return transactionData as T
    }

    override operator fun setValue(thisRef: KotprefModel, property: KProperty<*>, value: T) {
        if (thisRef.kotprefInTransaction) {
            transactionData = value
            lastUpdate = SystemClock.uptimeMillis()
            setToEditor(property, value, thisRef.kotprefEditor!!)
        } else {
            setToPreference(property, value, thisRef.kotprefPreference)
        }
    }

    public abstract fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T
    public abstract fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences)
    public abstract fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor)
}
