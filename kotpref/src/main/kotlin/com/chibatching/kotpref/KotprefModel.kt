package com.chibatching.kotpref

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty


open class KotprefModel() {

    private  var kotprefInTransaction: Boolean = false
    private  var kotprefTransactionStartTime: Long = 0

    /**
     * Preference file name
     */
    internal open val kotprefName: String = javaClass.simpleName

    /**
     * Preference read/write mode
     */
    internal open val kotprefMode: Int = Context.MODE_PRIVATE

    /**
     * Internal shared preference.
     * This property will be initialized on use.
     */
    private val kotprefPreference: KotprefPreferences by lazy {
        KotprefPreferences(Kotpref.context!!.getSharedPreferences(kotprefName, kotprefMode))
    }

    /**
     * Internal shared preference editor.
     */
    private var kotprefEditor: KotprefPreferences.KotprefEditor? = null

    /**
     * Clear all preferences in this model
     */
    fun clear() {
        beginBulkEdit()
        kotprefEditor!!.clear()
        commitBulkEdit()
    }

    /**
     * Delegate string shared preference property.
     * @param default default string value
     */
    protected fun stringPrefVar(default: String = "")
            : ReadWriteProperty<KotprefModel, String> = StringPrefVar(default)

    /**
     * Delegate Int shared preference property.
     * @param default default int value
     */
    protected fun intPrefVar(default: Int = 0)
            : ReadWriteProperty<KotprefModel, Int> = IntPrefVar(default)

    /**
     * Delegate long shared preference property.
     * @param default default long value
     */
    protected fun longPrefVar(default: Long = 0L)
            : ReadWriteProperty<KotprefModel, Long> = LongPrefVar(default)

    /**
     * Delegate float shared preference property.
     * @param default default float value
     */
    protected fun floatPrefVar(default: Float = 0F)
            : ReadWriteProperty<KotprefModel, Float> = FloatPrefVar(default)

    /**
     * Delegate boolean shared preference property.
     * @param default default boolean value
     */
    protected fun booleanPrefVar(default: Boolean = false)
            : ReadWriteProperty<KotprefModel, Boolean> = BooleanPrefVar(default)

    /**
     * Delegate string set shared preference property.
     * @param default default string set value
     */
    protected fun stringSetPrefVal(default: Set<String> = LinkedHashSet<String>())
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> = StringSetPrefVal{ default }

    /**
     * Delegate string set shared preference property.
     * @param default default string set value creation function
     */
    protected fun stringSetPrefVal(default: () -> Set<String>)
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> = StringSetPrefVal(default)

    /**
     * Begin bulk edit mode. You must commit or cancel after bulk edit finished.
     */
    fun beginBulkEdit() {
        kotprefInTransaction = true
        kotprefTransactionStartTime = System.currentTimeMillis()
        kotprefEditor = kotprefPreference.KotprefEditor(kotprefPreference.edit())
    }

    /**
     * Commit values set in the bulk edit mode to preferences.
     */
    fun commitBulkEdit() {
        kotprefEditor!!.apply()
        kotprefInTransaction = false
    }

    /**
     * Cancel bulk edit mode. Values set in the bulk mode will be rolled back.
     */
    fun cancelBulkEdit() {
        kotprefEditor = null
        kotprefInTransaction = false
    }


    abstract inner class PrefVar<T>() : ReadWriteProperty<KotprefModel, T> {

        private var lastUpdate: Long = 0
        abstract protected var transactionData: T

        override fun get(thisRef: KotprefModel, property: PropertyMetadata): T {
            if (!thisRef.kotprefInTransaction) {
                return getFromPreference(property, kotprefPreference)
            }
            if (lastUpdate < thisRef.kotprefTransactionStartTime) {
                transactionData = getFromPreference(property, kotprefPreference)
                lastUpdate = System.currentTimeMillis()
            }
            return transactionData
        }

        override fun set(thisRef: KotprefModel, property: PropertyMetadata, value: T) {
            if (thisRef.kotprefInTransaction) {
                transactionData = value
                lastUpdate = System.currentTimeMillis()
                setToEditor(property, value, kotprefEditor!!)
            } else {
                setToPreference(property, value, kotprefPreference)
            }
        }

        abstract internal fun getFromPreference(desc: PropertyMetadata, preference: SharedPreferences) : T
        abstract internal fun setToPreference(desc: PropertyMetadata, value: T, preference: SharedPreferences)
        abstract internal fun setToEditor(desc: PropertyMetadata, value: T, editor: SharedPreferences.Editor)
    }


    private inner class StringPrefVar(val default: String) : PrefVar<String>() {

        override var transactionData: String by Delegates.notNull()

        override fun getFromPreference(desc: PropertyMetadata, preference: SharedPreferences): String {
            return preference.getString(desc.name, default)
        }

        override fun setToPreference(desc: PropertyMetadata, value: String, preference: SharedPreferences) {
            preference.edit().putString(desc.name, value).apply()
        }

        override fun setToEditor(desc: PropertyMetadata, value: String, editor: SharedPreferences.Editor) {
            editor.putString(desc.name, value)
        }
    }


    private inner class IntPrefVar(val default: Int) : PrefVar<Int>() {

        override var transactionData: Int by Delegates.notNull()

        override fun getFromPreference(desc: PropertyMetadata, preference: SharedPreferences): Int {
            return preference.getInt(desc.name, default)
        }

        override fun setToPreference(desc: PropertyMetadata, value: Int, preference: SharedPreferences) {
            preference.edit().putInt(desc.name, value).apply()
        }

        override fun setToEditor(desc: PropertyMetadata, value: Int, editor: SharedPreferences.Editor) {
            editor.putInt(desc.name, value)
        }
    }


    private inner class LongPrefVar(val default: Long) : PrefVar<Long>() {

        override var transactionData: Long by Delegates.notNull()

        override fun getFromPreference(desc: PropertyMetadata, preference: SharedPreferences): Long {
            return preference.getLong(desc.name, default)
        }

        override fun setToPreference(desc: PropertyMetadata, value: Long, preference: SharedPreferences) {
            preference.edit().putLong(desc.name, value).apply()
        }

        override fun setToEditor(desc: PropertyMetadata, value: Long, editor: SharedPreferences.Editor) {
            editor.putLong(desc.name, value)
        }
    }


    private inner class FloatPrefVar(val default: Float) : PrefVar<Float>() {

        override var transactionData: Float by Delegates.notNull()

        override fun getFromPreference(desc: PropertyMetadata, preference: SharedPreferences): Float {
            return preference.getFloat(desc.name, default)
        }

        override fun setToPreference(desc: PropertyMetadata, value: Float, preference: SharedPreferences) {
            preference.edit().putFloat(desc.name, value).apply()
        }

        override fun setToEditor(desc: PropertyMetadata, value: Float, editor: SharedPreferences.Editor) {
            editor.putFloat(desc.name, value)
        }
    }


    private inner class BooleanPrefVar(val default: Boolean) : PrefVar<Boolean>() {

        override var transactionData: Boolean by Delegates.notNull()

        override fun getFromPreference(desc: PropertyMetadata, preference: SharedPreferences): Boolean {
            return preference.getBoolean(desc.name, default)
        }

        override fun setToPreference(desc: PropertyMetadata, value: Boolean, preference: SharedPreferences) {
            preference.edit().putBoolean(desc.name, value).apply()
        }

        override fun setToEditor(desc: PropertyMetadata, value: Boolean, editor: SharedPreferences.Editor) {
            editor.putBoolean(desc.name, value)
        }
    }


    private inner class StringSetPrefVal(val default: () -> Set<String>) : ReadOnlyProperty<KotprefModel, MutableSet<String>> {

        private var stringSet: MutableSet<String>? = null
        private var lastUpdate: Long = 0L

        override fun get(thisRef: KotprefModel, property: PropertyMetadata): MutableSet<String> {
            if (stringSet == null || lastUpdate < kotprefTransactionStartTime) {
                val prefSet = kotprefPreference.getStringSet(property.name, null)
                stringSet = PrefMutableSet(prefSet ?: default.invoke().toMutableSet(), property.name)
                if (prefSet == null) {
                    stringSet?.addAll(stringSet!!)
                }
                lastUpdate = System.currentTimeMillis()
            }
            return stringSet!!
        }
    }


    internal inner class PrefMutableSet(var set: MutableSet<String>, val key: String) : MutableSet<String> by set {

        private var transactionData: MutableSet<String>? = null
            get() {
                if ($transactionData == null) {
                    $transactionData = set.toMutableSet()
                }
                return $transactionData
            }

        internal fun syncTransaction() {
            synchronized(this) {
                if (transactionData != null) {
                    set.clear()
                    set.addAll(transactionData!!)
                    transactionData = null
                }
            }
        }

        override fun add(e: String): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.add(e)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.add(e)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun addAll(c: Collection<String>): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.addAll(c)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.addAll(c)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun remove(o: Any?): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.remove(o)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.remove(o)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun removeAll(c: Collection<Any?>): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.removeAll(c)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.removeAll(c)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun retainAll(c: Collection<Any?>): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.retainAll(c)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.retainAll(c)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun clear() {
            if (kotprefInTransaction) {
                val result = transactionData!!.clear()
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            set.clear()
            kotprefPreference.edit().putStringSet(key, set).apply()
        }

        override fun contains(o: Any?): Boolean {
            if (kotprefInTransaction) {
                return transactionData!!.contains(o)
            }
            return set.contains(o)
        }

        override fun containsAll(c: Collection<Any?>): Boolean {
            if (kotprefInTransaction) {
                return transactionData!!.containsAll(c)
            }
            return set.containsAll(c)
        }

        override fun iterator(): MutableIterator<String> {
            if (kotprefInTransaction) {
                return transactionData!!.iterator()
            }
            return set.iterator()
        }

        override fun size(): Int {
            if (kotprefInTransaction) {
                return transactionData!!.size()
            }
            return set.size()
        }
    }
}
