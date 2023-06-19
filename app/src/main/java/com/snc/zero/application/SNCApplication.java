package com.snc.zero.application;

import android.content.Context;

import com.snc.zero.log.Logger;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * Application
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class SNCApplication extends MultiDexApplication {
    private static final String TAG = SNCApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Logger.i(TAG, ">>>>>>>>>> onCreate <<<<<<<<<<");
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        // https://stackoverflow.com/questions/60472222/e-loadedapk-unable-to-instantiate-appcomponentfactory-only-on-android-q-api-29
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        Logger.i(TAG, ">>>>>>>>>> onLowMemory <<<<<<<<<<");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Logger.i(TAG, ">>>>>>>>>> onTrimMemory(" + level + ") <<<<<<<<<<");
        super.onTrimMemory(level);
    }

}
