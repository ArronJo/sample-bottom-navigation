package com.snc.sample.bottom_navigation.ui.fragment.splash;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snc.sample.bottom_navigation.R;
import com.snc.zero.fragment.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Splash Fragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class SplashFragment extends BaseFragment {
    //private static final String TAG = SplashFragment.class.getSimpleName();

    private static final int defaultInterval = 1000;
    private static long startTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        startTime = SystemClock.elapsedRealtime();
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long elapseTime = (SystemClock.elapsedRealtime() - startTime);
        long delayMillis = defaultInterval - elapseTime;
        postDelayed(() -> sendMessage("remove", (Object[]) null), Math.max(delayMillis, 0));
    }

}