package com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * Created by wangtong on 2016/9/8.
 */
public class RulerView extends View {

    private static final int SCALE_HEIGHT = ResUtils.dip2px(ApplicationContext.getAppContext(),25);
    private static final int SCALE_HEIGHT_LONG = ResUtils.dip2px(ApplicationContext.getAppContext(),40);
    public static final int SCALE_WIDTH = ResUtils.dip2px(ApplicationContext.getAppContext(),15);

    private long rulerWidth;
    private int rulerHeight;
    private int scaleStartX;
    private long lineWidth;
    private long rulerMoneyMax = 10000;
    private long rulerMoneyMin = 0;
    private double realMoneyMax;
    private long realMoneyMin;
    private int scaleValue = 100;
    private Paint paint;
    private TextPaint textPaint;

    private RulerStyle rulerStyle;

    private StringBuilder textStringBuilder;

    enum RulerStyle {
        START, END, NORMAL, BOTH
    }

    /**
     * 监听滑动到当前位置的金额
     */
    public interface CurrentMoneyListener {
        public void currentMoney(int currentMoney);
    }

    public RulerView(Context context) {
        super(context);
        init();
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.boc_text_color_common_gray));
        textPaint = new TextPaint();
        textPaint.setTextSize(36);
        textPaint.setColor(getResources().getColor(R.color.boc_text_color_common_gray));
        rulerStyle = RulerStyle.NORMAL;
        update();
    }

    public RulerView setRulerStyle(RulerStyle style) {
        rulerStyle = style;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rulerHeight = MeasureSpec.getSize(heightMeasureSpec) - ResUtils.dip2px(ApplicationContext.getAppContext(),5);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawScales(canvas);
    }

    private void drawLine(Canvas canvas) {
        canvas.drawLine(0, rulerHeight, lineWidth, rulerHeight, paint);
    }

    private void drawScale(Canvas canvas, long startX, int startY) {
        canvas.drawLine(startX, rulerHeight - startY, startX, rulerHeight, paint);
    }

    private void drawScales(Canvas canvas) {
        long number = (rulerMoneyMax - rulerMoneyMin) / scaleValue;
        for (long i = 0; i <= number; i++) {
            long money = i * scaleValue + rulerMoneyMin;
            if ((money % (10 * scaleValue)) == 0) {
                String numText;
                if (money < realMoneyMin) {
                    money = realMoneyMin;
                } else if (money > realMoneyMax) {
                    money = (long) realMoneyMax;
                }
                if (money >= 1000000) {
                    numText = money / 1000L + "K";
                } else {
                    numText = money + "";
                }
                drawScale(canvas, scaleStartX + i * SCALE_WIDTH, SCALE_HEIGHT_LONG);
                drawScaleNumber(canvas, numText, scaleStartX + i * SCALE_WIDTH);
            } else {
                drawScale(canvas, scaleStartX + i * SCALE_WIDTH, SCALE_HEIGHT);
            }
        }
    }

    private void drawScaleNumber(Canvas canvas, String text, long startX) {
        float textWidth = textPaint.measureText(text);
        long textX = (long) (startX - textWidth / 2);
        int textY = rulerHeight - SCALE_HEIGHT_LONG - ResUtils.dip2px(ApplicationContext.getAppContext(),5);
        canvas.drawText(text, textX, textY, textPaint);
    }

    public RulerView setRulerMoneyMax(long rulerMoneyMax) {
        this.rulerMoneyMax = rulerMoneyMax;
        return this;
    }

    public RulerView setRulerMoneyMin(long rulerMoneyMin) {
        this.rulerMoneyMin = rulerMoneyMin;
        return this;
    }

    public RulerView setScaleValue(int scaleValue) {
        this.scaleValue = scaleValue;
        return this;
    }

    public void update() {
        scaleStartX = getContext().getResources().getDisplayMetrics().widthPixels / 2;
        rulerWidth = (rulerMoneyMax - rulerMoneyMin) / scaleValue * SCALE_WIDTH;
        switch (rulerStyle) {
            case START:
                lineWidth = rulerWidth + scaleStartX;
                break;
            case END:
                lineWidth = rulerWidth + scaleStartX;
                scaleStartX = 0;
                break;
            case NORMAL:
                lineWidth = rulerWidth;
                scaleStartX = 0;
                break;
            case BOTH:
                lineWidth = rulerWidth + scaleStartX * 2;
                break;
        }
        postInvalidate();
    }

    public RulerView setRealMoneyMin(long realMoneyMin) {
        this.realMoneyMin = realMoneyMin;
        return this;
    }

    public RulerView setRealMoneyMax(double realMoneyMax) {
        this.realMoneyMax = realMoneyMax;
        return this;
    }
}
