package com.chinamworld.bocmbci.biz.peopleservice.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 
 * @author dl	</p>
 * 功能描述：dp、sp转px
 */

public class UnitTransition {

	/**
	 * 功能：dp转px
	 * @param context
	 * @param dpValue
	 * (dp单位值)
	 * @return int
	 */
	public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
	
	/**
	 * 功能：sp转px
	 * @param context
	 * @param spVal
	 * (sp单位值)
	 * @return int
	 */
	public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

}
