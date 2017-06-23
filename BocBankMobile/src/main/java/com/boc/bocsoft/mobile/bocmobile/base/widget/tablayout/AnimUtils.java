package com.boc.bocsoft.mobile.bocmobile.base.widget.tablayout;

import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;

/**
 * Created by leevin on 2016/11/8.
 */
public class AnimUtils {

    static final FastOutLinearInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutLinearInInterpolator();

    static int lerp(int startValue, int endValue, float fraction) {
        return startValue + Math.round(fraction * (endValue - startValue));
    }

    static float lerp(float startValue, float endValue, float fraction) {
        return startValue + fraction * (endValue - startValue);
    }

    public static float spToPx(Context context, int sp) {
       final float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return scaledDensity * sp;
    }
}
