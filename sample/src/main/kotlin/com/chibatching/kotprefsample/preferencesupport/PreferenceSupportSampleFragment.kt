package com.chibatching.kotprefsample.preferencesupport

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
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
                    dialogTitle = "Enter new value"
                    summaryProvider = Preference.SummaryProvider<EditTextPreference> {
                        it.text
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
                    summaryProvider = Preference.SummaryProvider<DropDownPreference> {
                        PreferenceSupportSampleSettings.Item
                            .find(it.value)
                            .displayName
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
                    summaryProvider = Preference.SummaryProvider<ListPreference> {
                        PreferenceSupportSampleSettings.Item
                            .find(it.value)
                            .displayName
                    }
                }

                multiSelectList(it::sampleMultiSelectList, "Sample multi select list preference") {
                    summary = it.sampleMultiSelectList.joinToString {
                        PreferenceSupportSampleSettings.Item
                            .find(it)
                            .displayName
                    }
                    entries =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.displayName }
                            .toTypedArray()
                    entryValues =
                        PreferenceSupportSampleSettings.Item.values()
                            .map { it.value }
                            .toTypedArray()
                    summaryProvider = Preference.SummaryProvider<MultiSelectListPreference> {
                        it.values.joinToString {
                            PreferenceSupportSampleSettings.Item.find(it).displayName
                        }
                    }
                }

                seekBar(it::sampleSeekBar, "Sample seek bar preference") {
                    min = 0
                    max = 100
                    this.showSeekBarValue = true
                }
            }

            category("Info") {
                screen("Version 1.0")
            }
        }
    }
}
