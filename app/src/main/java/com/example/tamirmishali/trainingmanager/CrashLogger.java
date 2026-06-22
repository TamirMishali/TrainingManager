package com.example.tamirmishali.trainingmanager;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Global crash logger. When the app crashes with an uncaught exception, the full
 * stack trace (plus device / app info) is written to a text file under the app's
 * external files directory:
 *
 *     Android/data/com.example.tamirmishali.trainingmanager/files/crashlogs/
 *
 * That folder is readable over USB with any file manager (or `adb pull`) without
 * root, and survives across app restarts. After writing the log we still delegate
 * to the previous default handler so the OS shows its normal "app stopped" dialog
 * and records the crash in logcat.
 */
public class CrashLogger {

    private static final String DIR_NAME = "crashlogs";

    public static void install(final Context context) {
        final Context appContext = context.getApplicationContext();
        final Thread.UncaughtExceptionHandler previousHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            try {
                writeCrashLog(appContext, thread, throwable);
            } catch (Throwable ignored) {
                // Never let the logger itself prevent the normal crash handling.
            }
            if (previousHandler != null) {
                previousHandler.uncaughtException(thread, throwable);
            }
        });
    }

    private static void writeCrashLog(Context context, Thread thread, Throwable throwable) {
        File dir = new File(context.getExternalFilesDir(null), DIR_NAME);
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }

        String timestamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File logFile = new File(dir, "crash_" + timestamp + ".txt");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();

        String appVersion;
        try {
            appVersion = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "unknown";
        }

        try (FileWriter fw = new FileWriter(logFile)) {
            fw.write("Time: " + new Date() + "\n");
            fw.write("App version: " + appVersion + "\n");
            fw.write("Thread: " + thread.getName() + "\n");
            fw.write("Device: " + Build.MANUFACTURER + " " + Build.MODEL + "\n");
            fw.write("Android: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")\n");
            fw.write("\n--- Stack trace ---\n");
            fw.write(sw.toString());
            fw.flush();
        } catch (Exception ignored) {
            // Disk full / no permission — give up quietly.
        }
    }
}
