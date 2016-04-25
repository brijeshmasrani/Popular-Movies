package com.udacity.nanodegree.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.udacity.nanodegree.popularmovies.MoviesFinder;

import java.util.Set;

public class AppPreferences {

    public static final String HOME_SORT_PREFERENCE = "home_sort_preference";

    public static final String APP_PREFERENCES_FILE_NAME = "app_preferences";

    private static SharedPreferences sharedPreferencesSingleton;

    private static synchronized SharedPreferences getInstance() {

        if (sharedPreferencesSingleton == null) {

            Context context = MoviesFinder.getInstance().getApplicationContext();
            sharedPreferencesSingleton = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        }

        return sharedPreferencesSingleton;
    }

    public static void deletePreference(String prefKey) {

        SharedPreferences sharedPreferences = getInstance();

        if (sharedPreferences.contains(prefKey)) {

            sharedPreferences.edit().remove(prefKey).apply();
        }
    }

    public static void deleteAllPreference() {

        SharedPreferences sharedPreferences = getInstance();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean contains(String key) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.contains(key);
    }

    //Booleans
    public static boolean getBoolean(String booleanKey) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getBoolean(booleanKey, false);
    }

    public static boolean getBoolean(String booleanKey, boolean defaultValue) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getBoolean(booleanKey, defaultValue);
    }

    public static boolean putBoolean(String booleanKey, boolean value) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.edit().putBoolean(booleanKey, value).commit();
    }

    //Integers
    public static int getInt(String intKey) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getInt(intKey, 0);
    }

    public static int getInt(String intKey, int defaultValue) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getInt(intKey, defaultValue);
    }

    public static boolean putInt(String intKey, int value) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.edit().putInt(intKey, value).commit();
    }

    //Strings
    public static String getString(String stringKey) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getString(stringKey, null);
    }

    public static String getString(String stringKey, @Nullable String defaultValue) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getString(stringKey, defaultValue);
    }

    public static boolean putString(String stringKey, String value) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.edit().putString(stringKey, value).commit();
    }


    //Floats
    public static Float getFloat(String stringKey) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getFloat(stringKey, 0);
    }

    public static Float getFloat(String stringKey, float defaultValue) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getFloat(stringKey, defaultValue);
    }

    public static boolean putFloat(String stringKey, float value) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.edit().putFloat(stringKey, value).commit();
    }

    //Strings Sets
    public static Set<String> getStringSet(String stringSetKey) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getStringSet(stringSetKey, null);
    }

    public static Set<String> getStringSet(String stringSetKey, @Nullable Set<String> defaultValue) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getStringSet(stringSetKey, defaultValue);
    }

    public static boolean putStringSet(String stringSetKey, Set<String> value) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.edit().putStringSet(stringSetKey, value).commit();
    }

    //Longs
    public static long getLong(String longKey) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getLong(longKey, 0);
    }

    public static long getLong(String longKey, long defaultValue) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.getLong(longKey, defaultValue);
    }

    public static boolean putLong(String longKey, long longValue) {

        SharedPreferences sharedPreferences = getInstance();

        return sharedPreferences.edit().putLong(longKey, longValue).commit();
    }

}