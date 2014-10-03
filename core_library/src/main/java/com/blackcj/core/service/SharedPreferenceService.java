package com.blackcj.core.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Service to make accessing shared preferences a little easier.
 *
 */
public class SharedPreferenceService {

    //Storage of preferences for this app
    private static final String PREFERENCE_NAME = SharedPreferenceService.class.getPackage()
            + ".sharedprefs";

    //Place Preference Keys Here


    private final SharedPreferences mSharedPreferences;

    /**
     * Constructor
     *
     * @param context application context to get the shared preferences from
     */
    public SharedPreferenceService(final Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves boolean user data in shared preferences.
     *
     * @param key   value to save and get on
     * @param value value to store
     */
    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor prefs = mSharedPreferences.edit();
        prefs.putBoolean(key, value);
        prefs.commit();
    }

    /**
     * gets boolean value from stored preferences
     *
     * @param key        key to look value up on
     * @param defaultVal value returned if no value is found
     * @return value stored with key if found, defaultVal otherwise
     */
    public boolean getBoolean(String key, boolean defaultVal) {
        return mSharedPreferences.getBoolean(key, defaultVal);
    }

    /**
     * Saves int user data in shared preferences.
     *
     * @param key   value to save and get on
     * @param value value to store
     */
    public void saveInt(String key, int value) {
        SharedPreferences.Editor prefs = mSharedPreferences.edit();
        prefs.putInt(key, value);
        prefs.commit();
    }

    /**
     * gets int value from stored preferences
     *
     * @param key        key to look value up on
     * @param defaultVal value returned if no value is found
     * @return value stored with key if found, defaultVal otherwise
     */
    public int getInt(String key, int defaultVal) {
        return mSharedPreferences.getInt(key, defaultVal);
    }

    /**
     * Saves int user data in shared preferences.
     *
     * @param key   value to save and get on
     * @param value value to store
     */
    public void saveLong(String key, long value) {
        SharedPreferences.Editor prefs = mSharedPreferences.edit();
        prefs.putLong(key, value);
        prefs.commit();
    }

    /**
     * gets int value from stored preferences
     *
     * @param key        key to look value up on
     * @param defaultVal value returned if no value is found
     * @return value stored with key if found, defaultVal otherwise
     */
    public long getLong(String key, long defaultVal) {
        return mSharedPreferences.getLong(key, defaultVal);
    }

    /**
     * Saves float user data in shared preferences.
     *
     * @param key   value to save and get on
     * @param value value to store
     */
    public void saveFloat(String key, float value) {
        SharedPreferences.Editor prefs = mSharedPreferences.edit();
        prefs.putFloat(key, value);
        prefs.commit();
    }

    /**
     * gets float value from stored preferences
     *
     * @param key        key to look value up on
     * @param defaultVal value returned if no value is found
     * @return value stored with key if found, defaultVal otherwise
     */
    public float getFloat(String key, float defaultVal) {
        return mSharedPreferences.getFloat(key, defaultVal);
    }

    /**
     * Saves string user data in shared preferences.
     *
     * @param key   value to save and get on
     * @param value value to store
     */
    public void saveString(String key, String value) {
        SharedPreferences.Editor prefs = mSharedPreferences.edit();
        prefs.putString(key, value);
        prefs.commit();
    }

    /**
     * gets string value from stored preferences
     *
     * @param key        key to look value up on
     * @param defaultVal value returned if no value is found
     * @return value stored with key if found, defaultVal otherwise
     */
    public String getString(String key, String defaultVal) {
        return mSharedPreferences.getString(key, defaultVal);
    }

    //Add other methods here for items you may store/get a lot in application
}