package ru.limedev.notes.model;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationSettings {

    private static volatile ApplicationSettings instance;
    private static volatile SharedPreferences sharedPreferences;

    private static final String APP_PREFERENCES = "notes_settings";

    private ApplicationSettings() {}

    private ApplicationSettings(Context context) {
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(APP_PREFERENCES,
                    Context.MODE_PRIVATE);
        }
    }

    public static void getInstance(Context context) {
        if (instance == null) {
            synchronized (ApplicationSettings.class) {
                if (instance == null) {
                    instance = new ApplicationSettings(context);
                }
            }
        }
    }

    public static synchronized void saveInt(String valueName, int value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(valueName, value);
            editor.apply();
        }
    }

    public static synchronized int loadInt(String valueName) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(valueName, 0);
        } else {
            return 0;
        }
    }
}
