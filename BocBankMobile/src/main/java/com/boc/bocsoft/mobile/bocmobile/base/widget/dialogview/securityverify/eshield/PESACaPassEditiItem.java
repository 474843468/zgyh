package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 音频key密码输入组件
 *
 * @author lxw
 */
public class PESACaPassEditiItem extends View {

    private int mCount;

    // 是否为空，没有输入的情况
    private boolean isNull = false;

    public PESACaPassEditiItem(Context context) {
        super(context);
    }

    public PESACaPassEditiItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PESACaPassEditiItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        for (int i = 1; i < 8; i++) {
            paint.setColor(getContext().getResources().getColor(R.color.boc_text_color_gray));
            canvas.drawLine(width / 8 * (i), 1, width / 8 * (i) - 1, height, paint);
        }

        if (!isNull) {
            int cy = height / 2;
            int radius = (int) (cy * 0.25);
            for (int i = 0; i < mCount; i++) {
                paint.setColor(Color.BLACK);
                canvas.drawCircle(width / 8 * (i) + width / 16, cy, radius, paint);
            }
        }

    }

    private void drawVerticalLine() {

    }

    public void drawBackgroundView() {
        isNull = true;
        this.invalidate();
    }

    public void drawCircle(int count) {
        this.mCount = count;
        isNull = false;
        this.invalidate();
    }
}
