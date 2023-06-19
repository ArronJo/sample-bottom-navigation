package com.snc.zero.activity;

import android.content.Context;
import android.view.ViewGroup;

import com.snc.sample.bottom_navigation.ui.dialog.SpinnerDialog;
import com.snc.zero.application.SNCApplication;
import com.snc.zero.handler.EventHandler;
import com.snc.zero.permission.RPermission;
import com.snc.zero.permission.RPermissionListener;
import com.snc.zero.security.SecureMode;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

/**
 * Abstract base activity
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final EventHandler mHandler = new EventHandler();
    private SpinnerDialog mSpinnerDialog;

    public SNCApplication getApplicationContext() {
        return (SNCApplication) super.getApplicationContext();
    }

    protected Context getContext() {
        return this;
    }

    protected FragmentActivity getActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SecureMode.disable(getActivity());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SecureMode.enable(getActivity());
    }

    protected ViewGroup getRootView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @SuppressWarnings("SameParameterValue")
    public void postDelayed(Runnable r, long delayMillis) {
        mHandler.postDelayed(r, delayMillis);
    }

    public void showProgress() {
        if (null != mSpinnerDialog) {
            return;
        }
        mSpinnerDialog = new SpinnerDialog(getContext());
        mSpinnerDialog.show();
    }

    public void hideProgress() {
        if (null == mSpinnerDialog) {
            return;
        }
        mSpinnerDialog.dismiss();
        mSpinnerDialog = null;
    }

    public void doCheckPermissions(List<String> permissions, RPermissionListener listener) {
        RPermission.with(getContext())
                .setPermissionListener(listener)
                .setPermissions(permissions)
                .check();
    }
}
