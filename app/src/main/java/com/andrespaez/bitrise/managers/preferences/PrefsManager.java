package com.andrespaez.bitrise.managers.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

import static android.content.Context.MODE_PRIVATE;

public class PrefsManager {

    private Context context;

    @Inject
    PrefsManager(@ApplicationContext Context context) {
        this.context = context;
    }

    private SharedPreferences getPreferencesFile(String groupPreferenceName) {
        if (groupPreferenceName.isEmpty()) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
        return context.getSharedPreferences(groupPreferenceName, MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(String groupPreferenceName) {
        SharedPreferences customize_pref = getPreferencesFile(groupPreferenceName);
        return customize_pref.edit();
    }

    public String getString(PreferencesKey preferenceKey) {
        SharedPreferences sharedFile = getPreferencesFile(preferenceKey.getPreferenceGroupName().getFileName());
        return sharedFile.getString(preferenceKey.getKey(), (String) preferenceKey.getDefaultValue());
    }

    public String getString(PreferencesKey preferenceKey, String defaultValue) {
        SharedPreferences sharedFile = getPreferencesFile(preferenceKey.getPreferenceGroupName().getFileName());
        return sharedFile.getString(preferenceKey.getKey(), defaultValue);
    }

    public int getInt(PreferencesKey preferenceKey) {
        SharedPreferences sharedFile = getPreferencesFile(preferenceKey.getPreferenceGroupName().getFileName());
        return sharedFile.getInt(preferenceKey.getKey(), (int) preferenceKey.getDefaultValue());
    }

    public int getInt(PreferencesKey preferenceKey, int defaultValue) {
        SharedPreferences sharedFile = getPreferencesFile(preferenceKey.getPreferenceGroupName().getFileName());
        return sharedFile.getInt(preferenceKey.getKey(), defaultValue);
    }

    public float getFloat(PreferencesKey preferenceKey) {
        SharedPreferences sharedFile = getPreferencesFile(preferenceKey.getPreferenceGroupName().getFileName());
        return sharedFile.getFloat(preferenceKey.getKey(), (float) preferenceKey.getDefaultValue());
    }

    public Long getLong(PreferencesKey preferenceKey) {
        SharedPreferences sharedFile = getPreferencesFile(preferenceKey.getPreferenceGroupName().getFileName());
        return sharedFile.getLong(preferenceKey.getKey(), (Long) preferenceKey.getDefaultValue());
    }

    public boolean getBoolean(PreferencesKey preferenceKey) {
        SharedPreferences sharedFile = getPreferencesFile(preferenceKey.getPreferenceGroupName().getFileName());
        return sharedFile.getBoolean(preferenceKey.getKey(), (boolean) preferenceKey.getDefaultValue());
    }

    public <T> T getObject(PreferencesKey preferenceKey, Class<T> classOfT) {
        return new Gson().fromJson(getString(preferenceKey), classOfT);
    }

    public void set(PreferencesKey preKey, String value) {
        SharedPreferences.Editor editor = getEditor(preKey.getPreferenceGroupName().getFileName());
        editor.putString(preKey.getKey(), value);
        editor.commit();
    }

    public void set(PreferencesKey key, int value) {
        SharedPreferences.Editor editor = getEditor(key.getPreferenceGroupName().getFileName());
        editor.putInt(key.getKey(), value);
        editor.commit();
    }

    public void set(PreferencesKey key, boolean value) {
        SharedPreferences.Editor editor = getEditor(key.getPreferenceGroupName().getFileName());
        editor.putBoolean(key.getKey(), value);
        editor.commit();
    }

    public void set(PreferencesKey key, float value) {
        SharedPreferences.Editor editor = getEditor(key.getPreferenceGroupName().getFileName());
        editor.putFloat(key.getKey(), value);
        editor.commit();
    }

    public void set(PreferencesKey key, long value) {
        SharedPreferences.Editor editor = getEditor(key.getPreferenceGroupName().getFileName());
        editor.putLong(key.getKey(), value);
        editor.commit();
    }

    public void set(PreferencesKey key, Object object) {
        SharedPreferences.Editor editor = getEditor(key.getPreferenceGroupName().getFileName());
        editor.putString(key.getKey(), new Gson().toJson(object));
        editor.commit();
    }

    public void remove(PreferencesKey key) {
        SharedPreferences.Editor editor = getEditor(key.getPreferenceGroupName().getFileName());
        editor.remove(key.getKey());
        editor.commit();
    }

    public boolean contains(PreferencesKey preferencesKey) {
        return getPreferencesFile(preferencesKey.getPreferenceGroupName().getFileName()).contains(preferencesKey.getKey());
    }

    private void resetUserPreference(PreferencesKey preference) {
        Object defaultValue = preference.getDefaultValue();
        String className = defaultValue.getClass().getName();
        if (className.equals(String.class.getName())) {
            String var = (String) defaultValue;
            set(preference, var);
        } else if (className.equals(Integer.class.getName())) {
            int var = (int) defaultValue;
            set(preference, var);
        } else if (className.equals(Boolean.class.getName())) {
            boolean var = (boolean) defaultValue;
            set(preference, var);
        } else if (className.equals(Float.class.getName())) {
            float var = (float) defaultValue;
            set(preference, var);
        } else if (className.equals(Long.class.getName())) {
            long var = (long) defaultValue;
            set(preference, var);
        }
    }

    public void resetPreferences() {
        for (PreferencesKey preferencesKey : ManagerPreferenceKey.values)
            resetUserPreference(preferencesKey);
    }

    public void resetGroupPreferences(String... groupPreferenceNames) {
        for (String groupName : groupPreferenceNames) {
            getEditor(groupName).clear().commit();
        }
    }

    public void resetPreferencesLess(String... groupPreferenceNames) {
        for (PreferencesKey preferencesKey : ManagerPreferenceKey.values) {
            if (!existPreference(preferencesKey, groupPreferenceNames)) {
                resetUserPreference(preferencesKey);
            }
        }
    }

    private boolean existPreference(PreferencesKey preferencesKey, String[] groups) {
        for (String group : groups) {
            if (preferencesKey.getPreferenceGroupName().equals(group)) {
                return true;
            }
        }
        return false;
    }
}
