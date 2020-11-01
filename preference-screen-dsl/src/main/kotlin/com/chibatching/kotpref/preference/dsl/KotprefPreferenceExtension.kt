package com.chibatching.kotpref.preference.dsl

import android.content.Context
import android.util.TypedValue
import androidx.appcompat.view.ContextThemeWrapper
import androidx.preference.CheckBoxPreference
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceScreen
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.chibatching.kotpref.KotprefModel
import kotlin.reflect.KProperty0

/**
 * Build new PreferenceScreen with KotprefModel
 * @param model KotprefModel using with this PreferenceScreen
 * @param builder Create PreferenceScreen with this Builder
 */
@ExperimentalPreferenceScreenDsl
public fun <T : KotprefModel> PreferenceFragmentCompat.kotprefScreen(
    model: T,
    builder: PreferenceScreenBuilder.(T) -> Unit
) {
    preferenceManager.sharedPreferencesName = model.kotprefName

    val context = requireContext()
    val typedValue = TypedValue()
    context.theme.resolveAttribute(
        context.resources.getIdentifier("preferenceTheme", "attr", context.packageName),
        typedValue,
        true
    )
    val themedContext = ContextThemeWrapper(context, typedValue.resourceId)

    val rootScreen: PreferenceScreen = preferenceManager.createPreferenceScreen(themedContext)
    val preferenceBuilder = PreferenceScreenBuilder(themedContext, rootScreen, model)
    builder(preferenceBuilder, model)
    preferenceScreen = rootScreen
    preferenceBuilder.dependencyBuilder.build()
}

@ExperimentalPreferenceScreenDsl
public class PreferenceScreenBuilder(
    private val context: Context,
    private val rootScreen: PreferenceGroup,
    private val model: KotprefModel
) {
    internal val dependencyBuilder: DependencyBuilder = DependencyBuilder()

    internal class DependencyBuilder {
        private val map = mutableMapOf<Preference, Preference>()

        fun addDependency(from: Preference, to: Preference) {
            map[from] = to
        }

        fun build() {
            map.forEach { entry ->
                entry.key.dependency = entry.value.key
            }
        }

        fun delegateToParentBuilder(parent: DependencyBuilder) {
            map.forEach { (t, u) ->
                parent.addDependency(t, u)
            }
        }
    }

    /**
     * Add dependency to other Preference
     */
    public fun Preference.dependsOn(preference: Preference) {
        dependencyBuilder.addDependency(this, preference)
    }

    /**
     * Create [SwitchPreferenceCompat] with provided property
     */
    public fun switch(
        property: KProperty0<Boolean>,
        title: String,
        options: (SwitchPreferenceCompat.() -> Unit)? = null
    ): SwitchPreferenceCompat {
        val preference =
            SwitchPreferenceCompat(context).applyPreferenceOptions(property, title, options)
        preference.isChecked = property.get()
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [CheckBoxPreference] with provided property
     */
    public fun checkBox(
        property: KProperty0<Boolean>,
        title: String,
        options: (CheckBoxPreference.() -> Unit)? = null
    ): CheckBoxPreference {
        val preference =
            CheckBoxPreference(context).applyPreferenceOptions(property, title, options)
        preference.isChecked = property.get()
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [EditTextPreference] with provided property
     */
    public fun editText(
        property: KProperty0<String>,
        title: String,
        options: (EditTextPreference.() -> Unit)? = null
    ): EditTextPreference {
        val preference =
            EditTextPreference(context).applyPreferenceOptions(property, title, options)
        preference.text = property.get()
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [DropDownPreference] with provided property
     */
    public fun dropDown(
        property: KProperty0<String>,
        title: String,
        options: (DropDownPreference.() -> Unit)? = null
    ): DropDownPreference {
        val preference =
            DropDownPreference(context).applyPreferenceOptions(property, title, options)
        preference.value = property.get()
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [ListPreference] with provided property
     */
    public fun list(
        property: KProperty0<String>,
        title: String,
        options: (ListPreference.() -> Unit)? = null
    ): ListPreference {
        val preference =
            ListPreference(context).applyPreferenceOptions(property, title, options)
        preference.value = property.get()
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [MultiSelectListPreference] with provided property
     */
    public fun multiSelectList(
        property: KProperty0<Set<String>>,
        title: String,
        options: (MultiSelectListPreference.() -> Unit)? = null
    ): MultiSelectListPreference {
        val preference =
            MultiSelectListPreference(context).applyPreferenceOptions(property, title, options)
        preference.values = property.get()
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [SeekBarPreference] with provided property
     */
    public fun seekBar(
        property: KProperty0<Int>,
        title: String,
        options: (SeekBarPreference.() -> Unit)? = null
    ): SeekBarPreference {
        val preference =
            SeekBarPreference(context).applyPreferenceOptions(property, title, options)
        preference.value = property.get()
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [Preference]
     */
    public fun preference(
        key: String,
        title: String,
        options: (Preference.() -> Unit)? = null
    ): Preference {
        val preference = Preference(context).apply {
            this.key = key
            this.title = title
            options?.invoke(this)
        }
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Create [PreferenceCategory]
     * @param title category title
     * @param childBuilder [PreferenceScreenBuilder] to create Preference in this category
     */
    public fun category(
        title: String,
        childBuilder: PreferenceScreenBuilder.() -> Unit
    ): PreferenceCategory {
        val category = PreferenceCategory(context).apply {
            this.title = title
        }
        rootScreen.addPreference(category)
        val builder = PreferenceScreenBuilder(context, category, model)
        childBuilder.invoke(builder)
        builder.dependencyBuilder.delegateToParentBuilder(dependencyBuilder)
        return category
    }

    /**
     * Create [PreferenceScreen]
     */
    public fun screen(
        title: String,
        options: (PreferenceScreen.() -> Unit)? = null
    ): PreferenceScreen {
        val preference = rootScreen.preferenceManager.createPreferenceScreen(context).apply {
            this.title = title
            options?.invoke(this)
        }
        rootScreen.addPreference(preference)
        return preference
    }

    /**
     * Start builder adding custom preference into PreferenceScreen
     *
     * ```
     * CustomPreference(context)
     *     .with(it::customValue)
     *     .title("Custom preference") {
     *         setCustomParam(param)
     *     }
     * ```
     *
     * @param property property to associate this custom preference
     * @return [CustomPreferenceBuilder]
     */
    public inline fun <reified T : Preference> T.with(
        property: KProperty0<Any>
    ): CustomPreferenceBuilder<T> =
        CustomPreferenceBuilder(this, property)

    private fun <T : Preference> T.applyPreferenceOptions(
        property: KProperty0<Any>,
        title: String,
        options: (T.() -> Unit)?
    ): T {
        val key = model.getPrefKey(property)
        this.key = key
        this.title = title
        options?.invoke(this)
        return this
    }

    /**
     * Builder for custom preference.
     * Calling [title] is required to add PreferenceScreen.
     */
    public inner class CustomPreferenceBuilder<T : Preference>(
        private val preference: T,
        private val property: KProperty0<Any>
    ) {
        public fun title(title: String, options: (T.() -> Unit)?): T {
            preference.applyPreferenceOptions(property, title, options)
            rootScreen.addPreference(preference)
            return preference
        }
    }
}

