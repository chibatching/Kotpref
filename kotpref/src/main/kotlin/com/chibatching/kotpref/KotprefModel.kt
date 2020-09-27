package com.chibatching.kotpref

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.SystemClock
import androidx.annotation.CallSuper
import com.chibatching.kotpref.pref.AbstractPref
import com.chibatching.kotpref.pref.AbstractStringSetPref
import com.chibatching.kotpref.pref.BooleanPref
import com.chibatching.kotpref.pref.FloatPref
import com.chibatching.kotpref.pref.IntPref
import com.chibatching.kotpref.pref.LongPref
import com.chibatching.kotpref.pref.PreferenceProperty
import com.chibatching.kotpref.pref.StringNullablePref
import com.chibatching.kotpref.pref.StringPref
import com.chibatching.kotpref.pref.StringSetPref
import java.util.LinkedHashSet
import kotlin.reflect.KProperty

abstract class KotprefModel(
    private val contextProvider: ContextProvider = StaticContextProvider,
    private val preferencesProvider: PreferencesProvider = defaultPreferenceProvider()
) {

    constructor(
        context: Context,
        preferencesProvider: PreferencesProvider = defaultPreferenceProvider()
    ) : this(
        ContextProvider { context.applicationContext },
        preferencesProvider
    )

    internal var kotprefInTransaction: Boolean = false
    internal var kotprefTransactionStartTime: Long = Long.MAX_VALUE

    internal val kotprefProperties: MutableMap<String, PreferenceProperty> = mutableMapOf()

    /**
     * Application Context
     */
    val context: Context
        get() = contextProvider.getApplicationContext()

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
    open val kotprefMode: Int = Context.MODE_PRIVATE

    /**
     * Internal shared preferences.
     * This property will be initialized on use.
     */
    internal val kotprefPreference: KotprefPreferences by lazy {
        KotprefPreferences(preferencesProvider.get(context, kotprefName, kotprefMode))
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
        get() = kotprefPreference

    /**
     * Clear all preferences in this model
     */
    @CallSuper
    open fun clear() {
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
    ): AbstractPref<String> = StringPref(default, key, commitByDefault)

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
    ): AbstractPref<String> =
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
    ): AbstractPref<String?> =
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
    ): AbstractPref<String?> =
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
    ): AbstractPref<Int> = IntPref(default, key, commitByDefault)

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
    ): AbstractPref<Int> =
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
    ): AbstractPref<Long> = LongPref(default, key, commitByDefault)

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
    ): AbstractPref<Long> = longPref(default, context.getString(key), commitByDefault)

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
    ): AbstractPref<Float> = FloatPref(default, key, commitByDefault)

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
    ): AbstractPref<Float> = floatPref(default, context.getString(key), commitByDefault)

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
    ): AbstractPref<Boolean> = BooleanPref(default, key, commitByDefault)

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
    ): AbstractPref<Boolean> = booleanPref(default, context.getString(key), commitByDefault)

    /**
     * Delegate string set shared preferences property.
     * @param default default string set value
     * @param key custom preferences key
     * @param commitByDefault commit this property instead of apply
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected fun stringSetPref(
        default: Set<String> = LinkedHashSet(),
        key: String? = null,
        commitByDefault: Boolean = commitAllPropertiesByDefault
    ): AbstractStringSetPref = stringSetPref(key, commitByDefault) { default }

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
    ): AbstractStringSetPref = stringSetPref(context.getString(key), commitByDefault) { default }

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
    ): AbstractStringSetPref = StringSetPref(default, key, commitByDefault)

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
    ): AbstractStringSetPref = stringSetPref(context.getString(key), commitByDefault, default)

    /**
     * Begin bulk edit mode. You must commit or cancel after bulk edit finished.
     */
    @SuppressLint("CommitPrefEdits")
    fun beginBulkEdit() {
        kotprefInTransaction = true
        kotprefTransactionStartTime = SystemClock.uptimeMillis()
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

    /**
     * Get preference key for a property.
     * @param property property delegated to Kotpref
     * @return preference key
     */
    fun getPrefKey(property: KProperty<*>): String? {
        return kotprefProperties[property.name]?.preferenceKey
    }

    /**
     * Remove entry from SharedPreferences
     * @param property property to remove
     */
    @SuppressLint("ApplySharedPref")
    fun remove(property: KProperty<*>) {
        preferences.edit().remove(getPrefKey(property)).apply {
            if (commitAllPropertiesByDefault) {
                commit()
            } else {
                apply()
            }
        }
    }
}
