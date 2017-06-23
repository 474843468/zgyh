package com.boc.bocsoft.mobile.bocmobile.base.widget.gallery;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author wangyang
 *         2016/12/14 16:18
 */
public class Gallery extends android.widget.Gallery implements ViewTreeObserver.OnGlobalLayoutListener {

    private float limitOffSet = 0.7f;

    private BigDecimal halfSpacing;

    private BigDecimal limitDistance;

    private OnPageScrollListener listener;

    private boolean isCanOnScrollChange = false;

    public Gallery(Context context) {
        super(context);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public Gallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public Gallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Gallery(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setSpacing(int spacing) {
        super.setSpacing(spacing);
        halfSpacing = new BigDecimal(spacing).divide(new BigDecimal(2));
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (listener == null)
            return super.onScroll(e1, e2, distanceX, distanceY);

        isCanOnScrollChange = false;

        boolean isMoveLeft = (e2.getX() - e1.getX()) < 0;

        if (getSelectedItemPosition() == 0 && !isMoveLeft)
            return super.onScroll(e1, e2, distanceX, distanceY);

        if (getSelectedItemPosition() == getChildCount() - 1 && isMoveLeft)
            return super.onScroll(e1, e2, distanceX, distanceY);

        BigDecimal xDistance = new BigDecimal(e2.getX() - e1.getX()).abs();
        float offSet = xDistance.divide(limitDistance, 4, RoundingMode.HALF_DOWN).setScale(4).floatValue();
        if (offSet < 1) {
            int nextSelectedPosition = getSelectedItemPosition() + 1;
            if (!isMoveLeft)
                nextSelectedPosition = getSelectedItemPosition() - 1;

            listener.onScroll(getSelectedItemPosition(), nextSelectedPosition, offSet, offSet >= limitOffSet);
        }

        boolean result = super.onScroll(e1, e2, distanceX, distanceY);
        isCanOnScrollChange = true;
        return result;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (getAdapter() instanceof BaseAdapter && isCanOnScrollChange) {
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onGlobalLayout() {
        if (getChildCount() <= 0)
            return;

        if (limitDistance != null)
            return;

        limitDistance = new BigDecimal(getChildAt(0).getWidth()).divide(new BigDecimal(2), 4, RoundingMode.HALF_DOWN);
        limitDistance = limitDistance.add(halfSpacing);
    }

    public interface OnPageScrollListener {
        void onScroll(int curSelectedPosition, int nextSelectedPosition, float offSet, boolean isScrollLimitOffSet);
    }

    public void setOnPageScrollListener(OnPageScrollListener listener) {
        this.listener = listener;
    }

    public void setLimitOffSet(float limitOffSet) {
        this.limitOffSet = limitOffSet;
    }
}
