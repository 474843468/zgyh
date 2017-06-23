package com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * 作者：XieDu
 * 创建时间：2016/12/28 9:20
 * 描述：精确边界金额刻度尺的复用view，刻度尺内部使用，不对外开放
 */
class ExactBoundaryRulerView extends View {

    private static final int SCALE_HEIGHT = ResUtils.dip2px(ApplicationContext.getAppContext(), 25);
    private static final int SCALE_HEIGHT_LONG =
            ResUtils.dip2px(ApplicationContext.getAppContext(), 40);
    public static final int SCALE_WIDTH = ResUtils.dip2px(ApplicationContext.getAppContext(), 15);
    private static final int SCALE_HEIGHT_SIDE =
            ResUtils.dip2px(ApplicationContext.getAppContext(), 25);
    private long rulerWidth, partStartWidth;
    private int rulerHeight;
    private int scaleStartX;
    private long lineWidth;
    private long rulerMoneyMax = 10000;
    private long rulerMoneyMin = 0;
    private double realMoneyMax;
    private double realMoneyMin;
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

    public ExactBoundaryRulerView(Context context) {
        super(context);
        init();
    }

    public ExactBoundaryRulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExactBoundaryRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public ExactBoundaryRulerView setRulerStyle(RulerStyle style) {
        rulerStyle = style;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rulerHeight = MeasureSpec.getSize(heightMeasureSpec) - ResUtils.dip2px(
                ApplicationContext.getAppContext(), 5);
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
            //如果根据rulerMoneyMin和刻度间距计算出的金额小于真实最小值，说明是showmin
            //即真实最小值左边一个刻度
            //这个刻度是多余的，这时候需要在真是最小值上画长边界线
            if (money < realMoneyMin) {
                drawScale(canvas, scaleStartX, SCALE_HEIGHT_SIDE);
            } else if (money > realMoneyMax) {
                drawScale(canvas, scaleStartX + rulerWidth, SCALE_HEIGHT_SIDE);
            } else if ((money % (10 * scaleValue)) == 0) {
                String numText;
                numText = getNumTextOfLong(money);
                drawScale(canvas, scaleStartX - partStartWidth + i * SCALE_WIDTH,
                        SCALE_HEIGHT_LONG);
                drawScaleNumber(canvas, numText, scaleStartX - partStartWidth + i * SCALE_WIDTH);
            } else {
                drawScale(canvas, scaleStartX - partStartWidth + i * SCALE_WIDTH, SCALE_HEIGHT);
            }
        }
    }

    @NonNull
    private String getNumTextOfLong(long money) {
        String numText;
        if (money >= 1000000) {
            numText = money / 1000L + "K";
        } else {
            numText = money + "";
        }
        return numText;
    }

    @NonNull
    private String getNumTextOfDouble(double money) {
        String numText;
        if (money >= 1000000) {
            numText = money / 1000L + "K";
        } else {
            numText = money + "";
        }
        return numText;
    }

    private void drawScaleNumber(Canvas canvas, String text, long startX) {
        float textWidth = textPaint.measureText(text);
        long textX = (long) (startX - textWidth / 2);
        int textY = rulerHeight - SCALE_HEIGHT_LONG - ResUtils.dip2px(
                ApplicationContext.getAppContext(), 5);
        canvas.drawText(text, textX, textY, textPaint);
    }

    public ExactBoundaryRulerView setRulerMoneyMax(long rulerMoneyMax) {
        this.rulerMoneyMax = rulerMoneyMax;
        return this;
    }

    public ExactBoundaryRulerView setRulerMoneyMin(long rulerMoneyMin) {
        this.rulerMoneyMin = rulerMoneyMin;
        return this;
    }

    public ExactBoundaryRulerView setScaleValue(int scaleValue) {
        this.scaleValue = scaleValue;
        return this;
    }

    public void update() {
        scaleStartX = getContext().getResources().getDisplayMetrics().widthPixels / 2;
        rulerWidth = (long) (((rulerMoneyMax > realMoneyMax ? realMoneyMax : rulerMoneyMax) - (
                rulerMoneyMin < realMoneyMin ? realMoneyMin : rulerMoneyMin)) / scaleValue
                * SCALE_WIDTH);
        partStartWidth =
                rulerMoneyMin < realMoneyMin ? (long) ((realMoneyMin - rulerMoneyMin) * SCALE_WIDTH
                        / scaleValue) : 0;
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

    public ExactBoundaryRulerView setRealMoneyMin(double realMoneyMin) {
        this.realMoneyMin = realMoneyMin;
        return this;
    }

    public ExactBoundaryRulerView setRealMoneyMax(double realMoneyMax) {
        this.realMoneyMax = realMoneyMax;
        return this;
    }
}
