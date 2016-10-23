package com.chibatching.kotpref

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


abstract class KotprefModel() {

    private var kotprefInTransaction: Boolean = false
    private var kotprefTransactionStartTime: Long = 0

    /**
     * Context set to Kotpref
     */
    val context: Context by lazy { Kotpref.context!! }

    /**
     * Preference file name
     */
    protected open val kotprefName: String = javaClass.simpleName

    /**
     * Preference read/write mode
     */
    protected open val kotprefMode: Int = Context.MODE_PRIVATE

    /**
     * Internal shared preference.
     * This property will be initialized on use.
     */
    private val kotprefPreference: KotprefPreferences by lazy {
        KotprefPreferences(context.getSharedPreferences(kotprefName, kotprefMode))
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
     * @param key custom preference key
     */
    protected fun stringPrefVar(default: String = "", key: String? = null)
            : ReadWriteProperty<KotprefModel, String> = StringPrefVar(default, key)

    /**
     * Delegate string shared preference property.
     * @param default default string value
     * @param key custom preference key resource id
     */
    protected fun stringPrefVar(default: String = "", key: Int)
            : ReadWriteProperty<KotprefModel, String> = StringPrefVar(default, context.getString(key))

    /**
     * Delegate nullable string shared preference property.
     * @param default default string value
     * @param key custom preference key
     */
    protected fun stringNullablePrefVar(default: String? = null, key: String? = null)
            : ReadWriteProperty<KotprefModel, String?> = StringNullablePrefVar(default, key)

    /**
     * Delegate nullable string shared preference property.
     * @param default default string value
     * @param key custom preference key resource id
     */
    protected fun stringNullablePrefVar(default: String? = null, key: Int)
            : ReadWriteProperty<KotprefModel, String?> = StringNullablePrefVar(default, context.getString(key))

    /**
     * Delegate int shared preference property.
     * @param default default int value
     * @param key custom preference key
     */
    protected fun intPrefVar(default: Int = 0, key: String? = null)
            : ReadWriteProperty<KotprefModel, Int> = IntPrefVar(default, key)

    /**
     * Delegate int shared preference property.
     * @param default default int value
     * @param key custom preference key resource id
     */
    protected fun intPrefVar(default: Int = 0, key: Int)
            : ReadWriteProperty<KotprefModel, Int> = IntPrefVar(default, context.getString(key))

    /**
     * Delegate long shared preference property.
     * @param default default long value
     * @param key custom preference key
     */
    protected fun longPrefVar(default: Long = 0L, key: String? = null)
            : ReadWriteProperty<KotprefModel, Long> = LongPrefVar(default, key)

    /**
     * Delegate long shared preference property.
     * @param default default long value
     * @param key custom preference key resource id
     */
    protected fun longPrefVar(default: Long = 0L, key: Int)
            : ReadWriteProperty<KotprefModel, Long> = LongPrefVar(default, context.getString(key))

    /**
     * Delegate float shared preference property.
     * @param default default float value
     * @param key custom preference key
     */
    protected fun floatPrefVar(default: Float = 0F, key: String? = null)
            : ReadWriteProperty<KotprefModel, Float> = FloatPrefVar(default, key)

    /**
     * Delegate float shared preference property.
     * @param default default float value
     * @param key custom preference key resource id
     */
    protected fun floatPrefVar(default: Float = 0F, key: Int)
            : ReadWriteProperty<KotprefModel, Float> = FloatPrefVar(default, context.getString(key))

    /**
     * Delegate boolean shared preference property.
     * @param default default boolean value
     * @param key custom preference key
     */
    protected fun booleanPrefVar(default: Boolean = false, key: String? = null)
            : ReadWriteProperty<KotprefModel, Boolean> = BooleanPrefVar(default, key)

    /**
     * Delegate boolean shared preference property.
     * @param default default boolean value
     * @param key custom preference key resource id
     */
    protected fun booleanPrefVar(default: Boolean = false, key: Int)
            : ReadWriteProperty<KotprefModel, Boolean> = BooleanPrefVar(default, context.getString(key))

    /**
     * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
     * @param enumClass Enum class to define the property
     * @param default default enum value
     * @param key custom preference key
     */
    protected fun <T : Enum<*>> enumValuePrefVar(enumClass: KClass<T>, default: T, key: String? = null)
            : ReadWriteProperty<KotprefModel, T> = EnumValuePrefVar(enumClass, default, key)

    /**
     * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
     * @param enumClass Enum class to define the property
     * @param default default enum value
     * @param key custom preference key resource id
     */
    protected fun <T : Enum<*>> enumValuePrefVar(enumClass: KClass<T>, default: T, key: Int)
            : ReadWriteProperty<KotprefModel, T> = EnumValuePrefVar(enumClass, default, context.getString(key))

    /**
     * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
     * @param enumClass Enum class to define the property
     * @param default default enum value
     * @param key custom preference key
     */
    protected fun <T : Enum<*>> enumNullableValuePrefVar(enumClass: KClass<T>, default: T? = null, key: String? = null)
            : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePrefVar(enumClass, default, key)

    /**
     * Delegate enum-based shared preference property storing and recalling by the enum value's name as a string.
     * @param enumClass Enum class to define the property
     * @param default default enum value
     * @param key custom preference key resource id
     */
    protected fun <T : Enum<*>> enumNullableValuePrefVar(enumClass: KClass<T>, default: T? = null, key: Int)
            : ReadWriteProperty<KotprefModel, T?> = EnumNullableValuePrefVar(enumClass, default, context.getString(key))

    /**
     * Delegate enum-based shared preference property storing and recalling by the enum value's ordinal as an integer.
     * @param enumClass Enum class to define the property
     * @param default default enum value
     * @param key custom preference key
     */
    protected fun <T : Enum<*>> enumOrdinalPrefVar(enumClass: KClass<T>, default: T, key: String? = null)
            : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPrefVar(enumClass, default, key)

    /**
     * Delegate enum-based shared preference property storing and recalling by the enum value's ordinal as an integer.
     * @param enumClass Enum class to define the property
     * @param default default enum value
     * @param key custom preference key resource id
     */
    protected fun <T : Enum<*>> enumOrdinalPrefVar(enumClass: KClass<T>, default: T, key: Int)
            : ReadWriteProperty<KotprefModel, T> = EnumOrdinalPrefVar(enumClass, default, context.getString(key))

    /**
     * Delegate string set shared preference property.
     * @param default default string set value
     * @param key custom preference key
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPrefVal(default: Set<String> = LinkedHashSet<String>(), key: String? = null)
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> = StringSetPrefVal({ default }, key)

    /**
     * Delegate string set shared preference property.
     * @param default default string set value
     * @param key custom preference key resource id
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPrefVal(default: Set<String> = LinkedHashSet<String>(), key: Int)
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> = StringSetPrefVal({ default }, context.getString(key))

    /**
     * Delegate string set shared preference property.
     * @param key custom preference key
     * @param default default string set value creation function
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPrefVal(key: String? = null, default: () -> Set<String>)
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> = StringSetPrefVal(default, key)

    /**
     * Delegate string set shared preference property.
     * @param key custom preference key resource id
     * @param default default string set value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPrefVal(key: Int, default: () -> Set<String>)
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> = StringSetPrefVal(default, context.getString(key))

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

    abstract inner class PrefVar<T : Any?>() : ReadWriteProperty<KotprefModel, T> {

        private var lastUpdate: Long = 0
        private var transactionData: Any? = null

        operator override fun getValue(thisRef: KotprefModel, property: KProperty<*>): T {
            if (!thisRef.kotprefInTransaction) {
                return getFromPreference(property, kotprefPreference)
            }
            if (lastUpdate < thisRef.kotprefTransactionStartTime) {
                transactionData = getFromPreference(property, kotprefPreference)
                lastUpdate = System.currentTimeMillis()
            }
            @Suppress("UNCHECKED_CAST")
            return transactionData as T
        }

        operator override fun setValue(thisRef: KotprefModel, property: KProperty<*>, value: T) {
            if (thisRef.kotprefInTransaction) {
                transactionData = value
                lastUpdate = System.currentTimeMillis()
                setToEditor(property, value, kotprefEditor!!)
            } else {
                setToPreference(property, value, kotprefPreference)
            }
        }

        abstract internal fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T
        abstract internal fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences)
        abstract internal fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor)
    }


    private inner class StringPrefVar(val default: String, val key: String?) : PrefVar<String>() {

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): String {
            return preference.getString(key ?: property.name, default)
        }

        override fun setToPreference(property: KProperty<*>, value: String, preference: SharedPreferences) {
            preference.edit().putString(key ?: property.name, value).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: String, editor: SharedPreferences.Editor) {
            editor.putString(key ?: property.name, value)
        }
    }

    private inner class StringNullablePrefVar(val default: String?, val key: String?) : PrefVar<String?>() {

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): String? {
            return preference.getString(key ?: property.name, default)
        }

        override fun setToPreference(property: KProperty<*>, value: String?, preference: SharedPreferences) {
            preference.edit().putString(key ?: property.name, value).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: String?, editor: SharedPreferences.Editor) {
            editor.putString(key ?: property.name, value)
        }
    }

    private inner class IntPrefVar(val default: Int, val key: String?) : PrefVar<Int>() {

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Int {
            return preference.getInt(key ?: property.name, default)
        }

        override fun setToPreference(property: KProperty<*>, value: Int, preference: SharedPreferences) {
            preference.edit().putInt(key ?: property.name, value).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: Int, editor: SharedPreferences.Editor) {
            editor.putInt(key ?: property.name, value)
        }
    }


    private inner class LongPrefVar(val default: Long, val key: String?) : PrefVar<Long>() {

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Long {
            return preference.getLong(key ?: property.name, default)
        }

        override fun setToPreference(property: KProperty<*>, value: Long, preference: SharedPreferences) {
            preference.edit().putLong(key ?: property.name, value).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: Long, editor: SharedPreferences.Editor) {
            editor.putLong(key ?: property.name, value)
        }
    }


    private inner class FloatPrefVar(val default: Float, val key: String?) : PrefVar<Float>() {

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Float {
            return preference.getFloat(key ?: property.name, default)
        }

        override fun setToPreference(property: KProperty<*>, value: Float, preference: SharedPreferences) {
            preference.edit().putFloat(key ?: property.name, value).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: Float, editor: SharedPreferences.Editor) {
            editor.putFloat(key ?: property.name, value)
        }
    }


    private inner class BooleanPrefVar(val default: Boolean, val key: String?) : PrefVar<Boolean>() {

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): Boolean {
            return preference.getBoolean(key ?: property.name, default)
        }

        override fun setToPreference(property: KProperty<*>, value: Boolean, preference: SharedPreferences) {
            preference.edit().putBoolean(key ?: property.name, value).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: Boolean, editor: SharedPreferences.Editor) {
            editor.putBoolean(key ?: property.name, value)
        }
    }

    private inner class EnumValuePrefVar<T : Enum<*>>(enumClass : KClass<T>, val default: T, val key: String?) : PrefVar<T>() {
        private val enumConstants = enumClass.java.enumConstants

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T {
            val value = preference.getString(key ?: property.name, default.name)
            return enumConstants.first { it.name == value }
        }

        override fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences) {
            preference.edit().putString(key ?: property.name, value.name).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
            editor.putString(key ?: property.name, value.name)
        }
    }

    private inner class EnumNullableValuePrefVar<T : Enum<*>>(enumClass : KClass<T>, val default: T?, val key: String?) : PrefVar<T?>() {
        private val enumConstants = enumClass.java.enumConstants

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T? {
            val value = preference.getString(key ?: property.name, default?.name)
            return enumConstants.firstOrNull { it.name == value }
        }

        override fun setToPreference(property: KProperty<*>, value: T?, preference: SharedPreferences) {
            preference.edit().putString(key ?: property.name, value?.name).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: T?, editor: SharedPreferences.Editor) {
            editor.putString(key ?: property.name, value?.name)
        }
    }

    private inner class EnumOrdinalPrefVar<T : Enum<*>>(enumClass : KClass<T>, val default: T, val key: String?) : PrefVar<T>() {
        private val enumConstants = enumClass.java.enumConstants

        override fun getFromPreference(property: KProperty<*>, preference: SharedPreferences): T {
            val value = preference.getInt(key ?: property.name, default.ordinal)
            return enumConstants.first { it.ordinal == value }
        }

        override fun setToPreference(property: KProperty<*>, value: T, preference: SharedPreferences) {
            preference.edit().putInt(key ?: property.name, value.ordinal).apply()
        }

        override fun setToEditor(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
            editor.putInt(key ?: property.name, value.ordinal)
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private inner class StringSetPrefVal(val default: () -> Set<String>, val key: String?) : ReadOnlyProperty<KotprefModel, MutableSet<String>> {

        private var stringSet: MutableSet<String>? = null
        private var lastUpdate: Long = 0L

        operator override fun getValue(thisRef: KotprefModel, property: KProperty<*>): MutableSet<String> {
            if (stringSet == null || lastUpdate < kotprefTransactionStartTime) {
                val prefSet = kotprefPreference.getStringSet(key ?: property.name, null)
                stringSet = PrefMutableSet(prefSet ?: default.invoke().toMutableSet(), key ?: property.name)
                lastUpdate = System.currentTimeMillis()
            }
            return stringSet!!
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    internal inner class PrefMutableSet(val set: MutableSet<String>, val key: String) : MutableSet<String> by set {

        init {
            addAll(set)
        }

        private var transactionData: MutableSet<String>? = null
            get() {
                field = field ?: set.toMutableSet()
                return field
            }

        internal fun syncTransaction() {
            synchronized(this) {
                transactionData?.let {
                    set.clear()
                    set.addAll(transactionData!!)
                    transactionData = null
                }
            }
        }

        override fun add(element: String): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.add(element)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.add(element)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun addAll(elements: Collection<String>): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.addAll(elements)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.addAll(elements)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun remove(element: String): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.remove(element)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.remove(element)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun removeAll(elements: Collection<String>): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.removeAll(elements)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.removeAll(elements)
            kotprefPreference.edit().putStringSet(key, set).apply()
            return result
        }

        override fun retainAll(elements: Collection<String>): Boolean {
            if (kotprefInTransaction) {
                val result = transactionData!!.retainAll(elements)
                kotprefEditor!!.putStringSet(key, transactionData, this)
                return result
            }
            val result = set.retainAll(elements)
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

        override fun contains(element: String): Boolean {
            if (kotprefInTransaction) {
                return element in transactionData!!
            }
            return element in set
        }

        override fun containsAll(elements: Collection<String>): Boolean {
            if (kotprefInTransaction) {
                return transactionData!!.containsAll(elements)
            }
            return set.containsAll(elements)
        }

        override fun iterator(): MutableIterator<String> {
            if (kotprefInTransaction) {
                return transactionData!!.iterator()
            }
            return set.iterator()
        }

        override val size: Int
            get() {
                if (kotprefInTransaction) {
                    return transactionData!!.size
                }
                return set.size
            }
    }
}

