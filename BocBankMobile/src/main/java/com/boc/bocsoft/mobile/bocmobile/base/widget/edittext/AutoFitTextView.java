package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * 自动缩小字体的TextView
 * Created by wangf on 2016/9/9.
 */
public class AutoFitTextView extends TextView {

    private Paint mTextPaint;
    private float mTextSize;


    public AutoFitTextView(Context context) {
        super(context);
        mTextSize = getTextSize();
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextSize = getTextSize();
    }


    private void refitText(String text, int textViewWidth) {
        if (text == null || textViewWidth <= 0)
            return;
        mTextPaint = new Paint();
        mTextPaint.set(this.getPaint());
        int availableTextViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float[] charsWidthArr = new float[text.length()];
        Rect boundsRect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), boundsRect);
        int textWidth = boundsRect.width();
        while (textWidth > availableTextViewWidth) {
            mTextSize -= 1;
            mTextPaint.setTextSize(mTextSize);
            textWidth = mTextPaint.getTextWidths(text, charsWidthArr);
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refitText(this.getText().toString(), this.getWidth());
    }


    /**
     * 设置默认字体大小
     *
     * @param size
     */
    public void setDefaultTextSize(float size) {
        mTextSize = size;
    }

}
