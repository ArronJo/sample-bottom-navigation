package com.snc.zero.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.snc.sample.bottom_navigation.ui.dialog.SpinnerDialog;
import com.snc.zero.application.SNCApplication;
import com.snc.zero.handler.EventHandler;
import com.snc.zero.security.SecureMode;

import androidx.annotation.Nullable;
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
    //private View mSpinnerView;
    //private BroadcastReceiver mFinishReceiver;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //registerFinishReceiver();
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

    @Override
    protected void onDestroy() {
        //unregisterFinishReceiver();
        super.onDestroy();
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

    /*
    public void showProgressInside() {
        mSpinnerView = LayoutInflater.from(getContext()).inflate(R.layout.view_spinner_progress_dialog, getRootView());
    }

    public void hideProgressInside() {
        getRootView().removeView(mSpinnerView);
    }
     */
}
