package ru.limedev.notes.model;

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
}
