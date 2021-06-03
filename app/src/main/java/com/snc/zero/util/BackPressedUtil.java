package com.snc.zero.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.snc.sample.bottom_navigation.R;
import com.snc.zero.resource.ResourceUtil;

/**
 * Back Pressed Utilities
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class BackPressedUtil {

    private static long backKeyPressedTime = 0;
    private static Toast toast;

    public static boolean onBackPressed(final Activity activity) {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide(activity);
            return true;
        }
        if (null != toast) {
            toast.cancel();
        }
        ActivityUtil.finish(activity);
        return false;
    }

    @SuppressLint("ShowToast")
    public static void showGuide(final Context context) {
        if (null == toast) {
            toast = Toast.makeText(context, ResourceUtil.getString(context, R.string.back_pressed), Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
