package org.commongeoregistry.adapter.android.tasks;

import android.util.Log;

import org.commongeoregistry.adapter.android.BuildConfig;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 11/01/2019
 */

public class LogUtil {

    public static final void e(String TAG, Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public static final void e(String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message);
        }
    }

    public static final void i(String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static final void eWithoutCrashlytics(String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message);
        }
    }

    public static final void eWithoutCrashlytics(String TAG, Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
