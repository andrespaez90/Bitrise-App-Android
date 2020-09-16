package com.andrespaez.bitrise.managers.preferences;

import java.util.ArrayList;

public class ManagerPreferenceKey {

    protected static ArrayList<PreferencesKey> values;

    protected static void addPreferenceKey(PreferencesKey key) {
        if (values == null) {
            values = new ArrayList<>();
        }
        values.add(key);
    }
}
