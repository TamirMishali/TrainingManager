package com.example.tamirmishali.trainingmanager;

import android.app.Application;

/**
 * Application entry point. Installs the global crash logger as early as possible
 * so any uncaught exception anywhere in the app gets written to a file.
 */
public class TrainingManagerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashLogger.install(this);
    }
}
