package com.chibatching.kotpref

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.chibatching.kotpref.pref.*
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty


abstract class KotprefModel {

    internal var kotprefInTransaction: Boolean = false
    internal var kotprefTransactionStartTime: Long = 0

    /**
     * Context set to Kotpref
     */
    val context: Context by lazy { Kotpref.context!! }

    /**
     * Preference file name
     */
    open val kotprefName: String = javaClass.simpleName

    /**
     * commit() all properties in this pref by default instead of apply()
     */
    open val commitAllPropertiesByDefault: Boolean = false

    /**
     * Preference read/write mode
     */
    protected open val kotprefMode: Int = Context.MODE_PRIVATE

    /**
     * Internal shared preferences.
     * This property will be initialized on use.
     */
    internal val kotprefPreference: KotprefPreferences by lazy {
        KotprefPreferences(context.getSharedPreferences(kotprefName, kotprefMode))
    }

    /**
     * Internal shared preferences editor.
     */
    internal var kotprefEditor: KotprefPreferences.KotprefEditor? = null

    /**
     * SharedPreferences instance exposed.
     * Use carefully when during bulk edit, it may cause inconsistent with internal data of Kotpref.
     */
    val preferences: SharedPreferences
        get() = kotprefPreference.preferences

    /**
     * Clear all preferences in this model
     */
    fun clear() {
        beginBulkEdit()
        kotprefEditor!!.clear()
        commitBulkEdit()
    }

    /**
     * Delegate string shared preferences property.
     * @param default default string value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    protected fun stringPref(
        default: String = "",
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, String> = StringPref(default, key, commitByDefault)

    /**
     * Delegate string shared preferences property.
     * @param default default string value
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     */
    protected fun stringPref(
        default: String = "",
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, String> =
        stringPref(default, context.getString(key), commitByDefault)

    /**
     * Delegate nullable string shared preferences property.
     * @param default default string value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    protected fun nullableStringPref(
        default: String? = null,
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, String?> =
        StringNullablePref(default, key, commitByDefault)

    /**
     * Delegate nullable string shared preferences property.
     * @param default default string value
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     */
    protected fun nullableStringPref(
        default: String? = null,
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, String?> =
        nullableStringPref(default, context.getString(key), commitByDefault)

    /**
     * Delegate int shared preferences property.
     * @param default default int value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    protected fun intPref(
        default: Int = 0,
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Int> = IntPref(default, key, commitByDefault)

    /**
     * Delegate int shared preferences property.
     * @param default default int value
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     */
    protected fun intPref(
        default: Int = 0,
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Int> =
        intPref(default, context.getString(key), commitByDefault)

    /**
     * Delegate long shared preferences property.
     * @param default default long value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    protected fun longPref(
        default: Long = 0L,
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Long> = LongPref(default, key, commitByDefault)

    /**
     * Delegate long shared preferences property.
     * @param default default long value
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     */
    protected fun longPref(
        default: Long = 0L,
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Long> =
        longPref(default, context.getString(key), commitByDefault)

    /**
     * Delegate float shared preferences property.
     * @param default default float value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    protected fun floatPref(
        default: Float = 0F,
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Float> = FloatPref(default, key, commitByDefault)

    /**
     * Delegate float shared preferences property.
     * @param default default float value
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     */
    protected fun floatPref(
        default: Float = 0F,
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Float> =
        floatPref(default, context.getString(key), commitByDefault)

    /**
     * Delegate boolean shared preferences property.
     * @param default default boolean value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    protected fun booleanPref(
        default: Boolean = false,
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Boolean> = BooleanPref(default, key, commitByDefault)

    /**
     * Delegate boolean shared preferences property.
     * @param default default boolean value
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     */
    protected fun booleanPref(
        default: Boolean = false,
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadWriteProperty<KotprefModel, Boolean> =
        booleanPref(default, context.getString(key), commitByDefault)

    /**
     * Delegate string set shared preferences property.
     * @param default default string set value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(
        default: Set<String> = LinkedHashSet<String>(),
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> =
        stringSetPref(key, commitByDefault) { default }

    /**
     * Delegate string set shared preferences property.
     * @param default default string set value
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(
        default: Set<String> = LinkedHashSet<String>(),
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    )
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> =
        stringSetPref(context.getString(key), commitByDefault) { default }

    /**
     * Delegate string set shared preferences property.
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     * @param default default string set value creation function
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault,
        default: () -> Set<String>
    )
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> =
        StringSetPref(default, key, commitByDefault)

    /**
     * Delegate string set shared preferences property.
     * @param key custom preferences key resource id
     * @param commitByDefault commit this property instead of apply
     * @param default default string set value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(
        key: Int,
        commitByDefault: Boolean = commitAllPropertiesByDefault,
        default: () -> Set<String>
    )
            : ReadOnlyProperty<KotprefModel, MutableSet<String>> =
        stringSetPref(context.getString(key), commitByDefault, default)

    /**
     * Begin bulk edit mode. You must commit or cancel after bulk edit finished.
     */
    @SuppressLint("CommitPrefEdits")
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
     * Commit values set in the bulk edit mode to preferences immediately, in blocking manner.
     */
    fun blockingCommitBulkEdit() {
        kotprefEditor!!.commit()
        kotprefInTransaction = false
    }

    /**
     * Cancel bulk edit mode. Values set in the bulk mode will be rolled back.
     */
    fun cancelBulkEdit() {
        kotprefEditor = null
        kotprefInTransaction = false
    }
}

