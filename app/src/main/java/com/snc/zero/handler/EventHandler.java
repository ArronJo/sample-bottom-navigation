package com.snc.zero.handler;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Handler
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class EventHandler {

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                if (msg.obj instanceof Runnable) {
                    ((Runnable) msg.obj).run();
                }
            }
            super.handleMessage(msg);
        }
    };

    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, delayMillis);
    }

}
