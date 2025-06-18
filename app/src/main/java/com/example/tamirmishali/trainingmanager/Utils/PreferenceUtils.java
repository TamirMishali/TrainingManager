package com.example.tamirmishali.trainingmanager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    private static final String PREF_NAME = "MainPrefs";
    private static final String MAIN_ROUTINE_ID_KEY = "MainRoutineId";

    public static void saveMainRoutineId(Context context, int routineId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(MAIN_ROUTINE_ID_KEY, routineId).apply();
    }

    public static int getMainRoutineId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(MAIN_ROUTINE_ID_KEY, -1);  // -1 = no routine yet
    }
}
