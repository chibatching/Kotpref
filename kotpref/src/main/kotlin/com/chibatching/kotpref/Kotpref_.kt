package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

public object Kotpref {
    public fun stringPrefVar(context: () -> Context, default: String = "",
                             preferenceName: String? = null, mode: Int = Context.MODE_PRIVATE)
            : ReadWriteProperty<Any?, String> = StringPrefVar(context, default, preferenceName, mode)

    public fun intPrefVar(context: () -> Context, default: Int = 0,
                          preferenceName: String? = null, mode: Int = Context.MODE_PRIVATE)
            : ReadWriteProperty<Any?, Int> = IntPrefVar(context, default, preferenceName, mode)

    public fun longPrefVar(context: () -> Context, default: Long = 0L,
                           preferenceName: String? = null, mode: Int = Context.MODE_PRIVATE)
            : ReadWriteProperty<Any?, Long> = LongPrefVar(context, default, preferenceName, mode)

    public fun floatPrefVar(context: () -> Context, default: Float = 0F,
                            preferenceName: String? = null, mode: Int = Context.MODE_PRIVATE)
            : ReadWriteProperty<Any?, Float> = FloatPrefVar(context, default, preferenceName, mode)

    public fun booleanPrefVar(context: () -> Context, default: Boolean = false,
                            preferenceName: String? = null, mode: Int = Context.MODE_PRIVATE)
            : ReadWriteProperty<Any?, Boolean> = BooleanPrefVar(context, default, preferenceName, mode)

    public fun stringSetPrefVar(context: () -> Context, default: Set<String>,
                                preferenceName: String? = null, mode: Int = Context.MODE_PRIVATE)
            : ReadWriteProperty<Any?, MutableSet<String>> = StringSetPrefVar(context, default, preferenceName, mode)
}

private abstract class PrefVar(context: () -> Context, preferenceName: String?, mode: Int) {
    val preference: SharedPreferences by Delegates.lazy {
        if (preferenceName != null) {
            context.invoke().getSharedPreferences(preferenceName, mode)
        } else {
            PreferenceManager.getDefaultSharedPreferences(context.invoke())
        }
    }
}

private class StringPrefVar(context: () -> Context, val default: String, preferenceName: String?, mode: Int) :
        ReadWriteProperty<Any?, String>, PrefVar(context, preferenceName, mode) {

    override fun get(thisRef: Any?, desc: PropertyMetadata): String {
        return preference.getString(desc.name, default)
    }

    override fun set(thisRef: Any?, desc: PropertyMetadata, value: String) {
        preference.edit().putString(desc.name, value).apply()
    }
}

private class IntPrefVar(context: () -> Context, val default: Int, preferenceName: String?, mode: Int) :
        ReadWriteProperty<Any?, Int>, PrefVar(context, preferenceName, mode) {

    override fun get(thisRef: Any?, desc: PropertyMetadata): Int {
        return preference.getInt(desc.name, default)
    }

    override fun set(thisRef: Any?, desc: PropertyMetadata, value: Int) {
        preference.edit().putInt(desc.name, value).apply()
    }
}

private class LongPrefVar(context: () -> Context, val default: Long, preferenceName: String?, mode: Int) :
        ReadWriteProperty<Any?, Long>, PrefVar(context, preferenceName, mode)  {

    override fun get(thisRef: Any?, desc: PropertyMetadata): Long {
        return preference.getLong(desc.name, default)
    }

    override fun set(thisRef: Any?, desc: PropertyMetadata, value: Long) {
        preference.edit().putLong(desc.name, value).apply()
    }
}

private class FloatPrefVar(context: () -> Context, val default: Float, preferenceName: String?, mode: Int) :
        ReadWriteProperty<Any?, Float>, PrefVar(context, preferenceName, mode)  {

    override fun get(thisRef: Any?, desc: PropertyMetadata): Float {
        return preference.getFloat(desc.name, default)
    }

    override fun set(thisRef: Any?, desc: PropertyMetadata, value: Float) {
        preference.edit().putFloat(desc.name, value).apply()
    }
}

private class BooleanPrefVar(context: () -> Context, val default: Boolean, preferenceName: String?, mode: Int) :
        ReadWriteProperty<Any?, Boolean>, PrefVar(context, preferenceName, mode)  {

    override fun get(thisRef: Any?, desc: PropertyMetadata): Boolean {
        return preference.getBoolean(desc.name, default)
    }

    override fun set(thisRef: Any?, desc: PropertyMetadata, value: Boolean) {
        preference.edit().putBoolean(desc.name, value).apply()
    }
}

private class StringSetPrefVar(val context: () -> Context, val default: Set<String>, val preferenceName: String?, val mode: Int) :
        ReadWriteProperty<Any?, MutableSet<String>>, PrefVar(context, preferenceName, mode)  {

    override fun get(thisRef: Any?, desc: PropertyMetadata): MutableSet<String> {
        val set = preference.getStringSet(desc.name, default)
        return PrefMutableSet(set, desc.name, context, preferenceName, mode)
    }

    override fun set(thisRef: Any?, desc: PropertyMetadata, value: MutableSet<String>) {
        preference.edit().putStringSet(desc.name, value).apply()
    }
}

private class PrefMutableSet(val set: MutableSet<String>, val key: String,
                             context: () -> Context, preferenceName: String?, mode: Int) :
        MutableSet<String> by set, PrefVar(context, preferenceName, mode) {
    override fun add(e: String): Boolean {
        val result = set.add(e)
        preference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun addAll(c: Collection<String>): Boolean {
        val result = set.addAll(c)
        preference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun remove(o: Any?): Boolean {
        val result = set.remove(o)
        preference.edit().putStringSet(key, set).apply()
        return result

    }

    override fun removeAll(c: Collection<Any?>): Boolean {
        val result = set.removeAll(c)
        preference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun retainAll(c: Collection<Any?>): Boolean {
        val result = set.retainAll(c)
        preference.edit().putStringSet(key, set).apply()
        return result
    }

    override fun clear() {
        set.clear()
        preference.edit().putStringSet(key, set).apply()
    }
}
