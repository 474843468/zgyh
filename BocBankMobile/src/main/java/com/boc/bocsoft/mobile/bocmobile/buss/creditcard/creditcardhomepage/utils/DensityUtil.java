package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.utils;

import android.content.Context;

/**
 * Created by liuweidong on 2016/11/25.
 */

public class DensityUtil {
    /**
     * dp转为px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dipTopx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转为dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxTodip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
