package ru.limedev.notes.model;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import static ru.limedev.notes.model.Constants.ACTION;

public final class Utilities {

    public static boolean checkStrings(String... strings) {
        boolean result = true;
        if (strings != null && strings.length != 0) {
            for (String s : strings) {
                if (s == null || s.length() == 0) {
                    return false;
                }
            }
        }
        return result;
    }

    public static void showSnackbar(View view, String text) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
        }
    }
}
