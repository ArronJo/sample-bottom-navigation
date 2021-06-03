package com.snc.zero.security;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Secure
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class SecureMode {

    public static void enable(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public static void disable(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

}
