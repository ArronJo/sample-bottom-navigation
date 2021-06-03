package com.snc.zero.util;

import android.app.Activity;

import com.snc.zero.handler.EventHandler;
import com.snc.zero.log.Logger;

/**
 * Activity Utilities
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class ActivityUtil {
    private static final String TAG = ActivityUtil.class.getSimpleName();

    private static final EventHandler handler = new EventHandler();

    public static void killProcess(Activity activity) {
        try {
            activity.finishAffinity();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        System.runFinalization();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void finish(Activity activity) {
        try {
            //handler.postDelayed(() -> ActivityUtil.killProcess(activity), 500);
            activity.finish();
        } catch (Exception e) {
            Logger.e(TAG, e);
            ActivityUtil.killProcess(activity);
        }
    }

}
