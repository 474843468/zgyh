package com.boc.bocsoft.mobile.bocmobile.base.widget.measureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author wangyang
 *         2016/12/20 16:47
 */
public class MeasureViewPager extends ViewPager implements ViewTreeObserver.OnGlobalLayoutListener {

    public MeasureViewPager(Context context) {
        super(context);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public MeasureViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onGlobalLayout() {
        View view = (View) getParent();
        if (view.getHeight() < getHeight()){
            requestLayout();
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }
}
