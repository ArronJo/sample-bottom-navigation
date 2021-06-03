package com.snc.sample.bottom_navigation.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.snc.sample.bottom_navigation.R;

/**
 * Spinner Progress Dialog
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class SpinnerDialog extends Dialog {

    public SpinnerDialog(Context context) {
        super(context, R.style.SNCDialog_NoDim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        setContentView(R.layout.view_spinner_progress_dialog);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

}