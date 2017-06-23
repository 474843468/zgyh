package com.boc.bocsoft.mobile.bocmobile.base.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by gwluo on 2016/11/25.
 */

public class StringStyleUtil {
    /**
     * 将字符串改变颜色
     *
     * @param str
     * @return
     */
    public static SpannableString getColorString(Context context, String str, int color) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, str.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
