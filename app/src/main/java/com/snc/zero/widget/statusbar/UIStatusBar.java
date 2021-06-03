package com.snc.zero.widget.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowInsetsController;

/**
 * Status Bar
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class UIStatusBar {

    public static void setWhiteColorOnBackground(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (null != activity.getWindow().getInsetsController()) {
                WindowInsetsController controller = activity.getWindow().getInsetsController();
                controller.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        activity.getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
    }

    public static void setBlackColorOnBackground(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        activity.getWindow().setStatusBarColor(Color.parseColor("#000000"));
    }

}
