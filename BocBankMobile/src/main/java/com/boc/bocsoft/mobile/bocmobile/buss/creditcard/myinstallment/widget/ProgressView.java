package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yangle on 2016/11/16.
 * 分期进度的view
 */
public class ProgressView extends View {

    public static final int DEFAULT_MAX_VALUE= 12;
    public static final String BACKGROUND_COLOR = "#33111000";
    public static final String PROGRESS_COLOR = "#00c192";
    private float mMaxValue = DEFAULT_MAX_VALUE;
    private int mCurrentValue = 0;
    private Paint mPaint;

    public ProgressView(Context context) {
        this(context ,null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float ratio = mCurrentValue * 1.0f / mMaxValue;
        // draw background
        mPaint.setColor(Color.parseColor(BACKGROUND_COLOR));
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

        // draw progress
        mPaint.setColor(Color.parseColor(PROGRESS_COLOR));
        canvas.drawRect(0,0,getWidth() * ratio,getHeight(),mPaint);
    }

    public void setProgressAndMaxValue(int progressValue,int maxValue) {
        if (progressValue < 0 || maxValue < 0) {
            throw new IllegalArgumentException("progressValue or maxValue cannot be < 0");
        }
        if (mCurrentValue != progressValue && progressValue <= maxValue ) {
            this.mCurrentValue = progressValue;
            this.mMaxValue = maxValue;
            invalidate();
        }
    }

    public void setMaxValue(int maxValue) {
        if (this.mMaxValue != maxValue && maxValue > 0) {
            this.mMaxValue = maxValue;
            invalidate();
        }
    }

    public void setCurrentValue(int currentValue) {
        if (this.mCurrentValue != currentValue && currentValue <= mMaxValue) {
            this.mCurrentValue = currentValue;
            invalidate();
        }
    }
}
