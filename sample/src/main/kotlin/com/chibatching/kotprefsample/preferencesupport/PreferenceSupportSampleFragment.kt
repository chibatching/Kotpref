package com.chibatching.kotprefsample.preferencesupport

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chibatching.preference.kotprefScreen

class PreferenceSupportSampleFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        kotprefScreen(PreferenceSupportSampleSettings) {
            category("Sample category 1") {
                switch(it::sampleSwitch, "Sample switch preference") {
                    summaryOn = "sample is on"
                    summaryOff = "sample is off"
                }

                val enableOption = checkBox(it::sampleCheckBox, "Sample check box preference")

                editText(it::sampleEditText, "Sample edit text preference") {
                    summary = it.sampleEditText
                    dialogTitle = "Enter new value"
                    onPreferenceChangeListener =
                        Preference.OnPreferenceChangeListener { _, newValue ->
                            summary = newValue as String
                            true
                        }
                    dependsOn(enableOption)
                }
            }

            category("Sample category 2") {
                dropDown(it::sampleDropDown, "Sample drop down preference") {
                    summary = PreferenceSupportSampleSettings.Item
                        .find(it.sampleDropDown)
                        .displayName
                    entries =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.displayName }
                            .toTypedArray()
                    entryValues =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.value }
                            .toTypedArray()
                    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                        summary = PreferenceSupportSampleSettings.Item
                            .find(newValue as String)
                            .displayName
                        true
                    }
                }
            }

            category("Sample category 3") {
                list(it::sampleList, "Sample list preference") {
                    summary = PreferenceSupportSampleSettings.Item
                        .find(it.sampleList)
                        .displayName
                    entries =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.displayName }
                            .toTypedArray()
                    entryValues =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.value }
                            .toTypedArray()
                    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                        summary = PreferenceSupportSampleSettings.Item
                            .find(newValue as String)
                            .displayName
                        true
                    }
                }

                multiSelectList(it::sampleMultiSelectList, "Sample multi select list preference") {
                    summary = it.sampleMultiSelectList
                        .map { PreferenceSupportSampleSettings.Item.find(it).displayName }
                        .joinToString()
                    entries =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.displayName }
                            .toTypedArray()
                    entryValues =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.value }
                            .toTypedArray()
                    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                        summary = (newValue as Set<String>)
                            .map { PreferenceSupportSampleSettings.Item.find(it).displayName }
                            .joinToString()
                        true
                    }
                }

                seekBar(it::sampleSeekBar, "Sample seek bar preference") {
                    min = 0
                    max = 100
                }
            }

            category("Info") {
                screen("Version 1.0")
            }
        }
    }
}