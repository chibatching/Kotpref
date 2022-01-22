package com.chibatching.kotpref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import androidx.annotation.CallSuper
import com.chibatching.kotpref.pref.PreferenceProperty
import kotlin.reflect.KProperty

public abstract class KotprefModel(
    private val contextProvider: ContextProvider = StaticContextProvider,
    private val preferencesProvider: PreferencesProvider = defaultPreferenceProvider()
) {

    public constructor(
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
    public val context: Context
        get() = contextProvider.getApplicationContext()

    /**
     * Preference file name
     */
    public open val kotprefName: String = javaClass.simpleName

    /**
     * commit() all properties in this pref by default instead of apply()
     */
    public open val commitAllPropertiesByDefault: Boolean = false

    /**
     * Preference read/write mode
     */
    public open val kotprefMode: Int = Context.MODE_PRIVATE

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
    public val preferences: SharedPreferences
        get() = kotprefPreference

    /**
     * Clear all preferences in this model
     */
    @CallSuper
    public open fun clear() {
        beginBulkEdit()
        kotprefEditor!!.clear()
        commitBulkEdit()
    }

    /**
     * Begin bulk edit mode. You must commit or cancel after bulk edit finished.
     */
    @SuppressLint("CommitPrefEdits")
    public fun beginBulkEdit() {
        kotprefInTransaction = true
        kotprefTransactionStartTime = SystemClock.uptimeMillis()
        kotprefEditor = kotprefPreference.KotprefEditor(kotprefPreference.edit())
    }

    /**
     * Commit values set in the bulk edit mode to preferences.
     */
    public fun commitBulkEdit() {
        kotprefEditor!!.apply()
        kotprefInTransaction = false
    }

    /**
     * Commit values set in the bulk edit mode to preferences immediately, in blocking manner.
     */
    public fun blockingCommitBulkEdit() {
        kotprefEditor!!.commit()
        kotprefInTransaction = false
    }

    /**
     * Cancel bulk edit mode. Values set in the bulk mode will be rolled back.
     */
    public fun cancelBulkEdit() {
        kotprefEditor = null
        kotprefInTransaction = false
    }

    /**
     * Get preference key for a property.
     * @param property property delegated to Kotpref
     * @return preference key
     */
    public fun getPrefKey(property: KProperty<*>): String? {
        return kotprefProperties[property.name]?.preferenceKey
    }

    /**
     * Remove entry from SharedPreferences
     * @param property property to remove
     */
    @SuppressLint("ApplySharedPref")
    public fun remove(property: KProperty<*>) {
        preferences.edit().remove(getPrefKey(property)).apply {
            if (commitAllPropertiesByDefault) {
                commit()
            } else {
                apply()
            }
        }
    }
}
