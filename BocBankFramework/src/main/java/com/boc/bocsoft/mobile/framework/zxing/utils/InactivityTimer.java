package com.boc.bocsoft.mobile.framework.zxing.utils;

import android.os.Handler;

/**
 * Finishes an context after a period
 */
public final class InactivityTimer {

    private static final String TAG = InactivityTimer.class.getSimpleName();

    private static final long INACTIVITY_DELAY_MS = 5 * 60 * 1000L;

    private Handler handler;
    private Runnable callback;

    public InactivityTimer(Runnable callback) {
        handler = new Handler();
        this.callback = callback;
    }

    /**
     * Trigger onActivity, resetting the timer.
     */
    private void onActivity() {
        cancelCallback();
        handler.postDelayed(callback, INACTIVITY_DELAY_MS);
    }

    /**
     * Start the onActivity timer.
     */
    public void start() {
        onActivity();
    }

    /**
     * Cancel the onActivity timer.
     */
    public void cancel() {
        cancelCallback();
    }


    private void cancelCallback() {
        handler.removeCallbacksAndMessages(null);
    }
}