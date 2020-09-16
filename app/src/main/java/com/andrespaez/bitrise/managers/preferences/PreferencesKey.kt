package com.andrespaez.bitrise.managers.preferences

open class PreferencesKey(
    val preferenceGroupName: FilePreference = FilePreference.DefaultPreference,
    val key: String,
    val defaultValue: Any
) {

    init {
        ManagerPreferenceKey.addPreferenceKey(this)
    }

    override fun toString(): String {
        return key
    }
}

