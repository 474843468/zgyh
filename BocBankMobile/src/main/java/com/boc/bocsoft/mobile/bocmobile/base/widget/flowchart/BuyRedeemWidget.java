package com.boc.bocsoft.mobile.bocmobile.base.widget.flowchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 中银理财- 赎回-结果-步骤流程
 *
 * @author yx
 * @date 2016年11月5日 17:08:26
 */
public class BuyRedeemWidget extends View {

    private static final int RADIUS = 30;
    private static final int RACT_HEIGHT = 20;
    private static final int RACT_WIDTH = 20;
    private static final int PADING = 40;

    private Paint paint;
    private Paint paintText;
    // private String[] text = new String[] { "今日赎回", "本金到帐" };
    // private String[] date = new String[] { "2018/05/06",
    // "2018/05/06赎回，本金实时到账" };
    private String[] text = new String[] { "今日赎回", "本金到帐", "收益到帐" };
    private String[] date = new String[] { "2018/05/06", "2018/05/06赎回，本金实时到账",
            "预计2017/09/12" };

    private int viewWidth;
    private int viewHeight;
    private int textWidth;
    private int textHtight;
    private CompleteRedeemStatus status = CompleteRedeemStatus.ONE;
    /***
     * 是否是两中状态， true 是（2）；false 不是（大部分是3）
     */
    private boolean isTwoStatus = false;
    private Context mContext;

    public enum CompleteRedeemStatus {
        ONE, TWO, THREE
    }

    public BuyRedeemWidget(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BuyRedeemWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public BuyRedeemWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setTextSize(dip2px(13));
        paintText = new Paint();
        paintText.setTextSize(dip2px(13));
        paintText.setFakeBoldText(true);
        Rect textRect = new Rect();
        paint.getTextBounds(text[0], 0, text[0].length(), textRect);
        viewHeight = textHtight * 2 + RADIUS * 2 + 10;
        viewWidth = textWidth * 2 + RADIUS * 2 + 10;
        System.out.println("yx-------initView---viewHeight--->" + viewHeight);
        System.out.println("yx-------initView---viewWidth--->" + viewWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);

    }

    /**
     * 根据手机分辨率吧dp转换成px
     */
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale - 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
        drawCircle(canvas);
    }

    // 线
    private void drawLines(Canvas canvas) {
        int top = textHtight / 2 + PADING;
        int left = (RADIUS * 2 - RACT_WIDTH) / 2;
        // int bottom = viewHeight / 2;//修改
        int bottom = viewHeight / 2;
        int right = left + RACT_WIDTH;
        int top2 = left;
        int bottom2 = viewHeight - textHtight / 2 - PADING;

        if (isTwoStatus) {
            Rect rect = new Rect(left, top, right, bottom);
            if (status == CompleteRedeemStatus.ONE) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawRect(rect, paint);
            } else if (status == CompleteRedeemStatus.TWO) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawRect(rect, paint);
            }
        } else {
            Rect rect = new Rect(left, top, right, bottom);
            Rect rect2 = new Rect(left, top2, right, bottom2);
            if (status == CompleteRedeemStatus.ONE) {
                paintText.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_gray));
                canvas.drawRect(rect2, paintText);
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawRect(rect, paint);
            } else if (status == CompleteRedeemStatus.TWO) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawRect(rect, paint);
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawRect(rect2, paint);
            } else if (status == CompleteRedeemStatus.THREE) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawRect(rect, paint);
                canvas.drawRect(rect2, paint);
            }
        }
    }

    // 圆
    private void drawCircle(Canvas canvas) {
        int cx = textHtight / 2 + PADING / 2 + RACT_HEIGHT;
        int cx1 = viewHeight / 2;

        if (isTwoStatus) {
            if (status == CompleteRedeemStatus.ONE) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawCircle(RADIUS, cx, RADIUS, paint);
                drawTextView(canvas, 0);
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_gray));
                canvas.drawCircle(RADIUS, cx1, RADIUS, paint);
                drawTextView(canvas, 1);
            } else if (status == CompleteRedeemStatus.TWO) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawCircle(RADIUS, cx, RADIUS, paint);
                canvas.drawCircle(RADIUS, cx1, RADIUS, paint);
                drawTextView(canvas, 0);
                drawTextView(canvas, 1);

            }
        } else {
            int cx2 = viewHeight - textHtight / 2 - PADING;
            if (status == CompleteRedeemStatus.ONE) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawCircle(RADIUS, cx, RADIUS, paint);
                // drawText(canvas, 0);
                drawTextView(canvas, 0);
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_gray));

                canvas.drawCircle(RADIUS, cx1, RADIUS, paint);
                canvas.drawCircle(RADIUS, cx2, RADIUS, paint);
                // drawText(canvas, 1);
                // drawText(canvas, 2);
                drawTextView(canvas, 1);
                drawTextView(canvas, 2);
            } else if (status == CompleteRedeemStatus.TWO) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawCircle(RADIUS, cx, RADIUS, paint);
                canvas.drawCircle(RADIUS, cx1, RADIUS, paint);
                // drawText(canvas, 0);
                // drawText(canvas, 1);
                drawTextView(canvas, 0);
                drawTextView(canvas, 1);
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_gray));
                canvas.drawCircle(RADIUS, cx2, RADIUS, paint);
                // drawText(canvas, 2);
                drawTextView(canvas, 2);
            } else if (status == CompleteRedeemStatus.THREE) {
                paint.setColor(getResources().getColor(
                        R.color.boc_round_progressbar_green));
                canvas.drawCircle(RADIUS, cx, RADIUS, paint);
                canvas.drawCircle(RADIUS, cx1, RADIUS, paint);
                canvas.drawCircle(RADIUS, cx2, RADIUS, paint);
                // drawText(canvas, 0);
                // drawText(canvas, 1);
                // drawText(canvas, 2);
                drawTextView(canvas, 0);
                drawTextView(canvas, 1);
                drawTextView(canvas, 2);
            }
        }

    }

    // 文本
    private void drawText(Canvas canvas, int index) {
        int top = RACT_HEIGHT;
        int top2 = 0;
        int right = RADIUS * 2 + textWidth + 10;

        Rect textRect = new Rect();
        if (index == 0) {
            top = RADIUS;
            paint.getTextBounds(date[index], 0, date[index].length(), textRect);
            top2 = textHtight / 2 + RADIUS - textRect.height() / 2 + PADING
                    + 20;
        } else if (index == 1) {
            top = (viewHeight - textHtight) / 2 - 10;
            paint.getTextBounds(date[index], 0, date[index].length(), textRect);
            top2 = (viewHeight - textRect.height()) / 2 + RADIUS * 2;
        } else if (index == 2) {
            top = viewHeight - textHtight - RADIUS * 2;
            paint.getTextBounds(date[index], 0, date[index].length(), textRect);
            top2 = viewHeight - textRect.height() + RADIUS;
        }
        canvas.drawText(text[index], right, top, paint);
        canvas.drawText(date[index], right, top2 + textHtight, paint);
    }

    /**
     * 文本
     *
     * @param canvas
     * @param index
     */
    private void drawTextView(Canvas canvas, int index) {
        int top = RACT_HEIGHT;
        int top2 = 0;
        int right = RADIUS * 2 + textWidth + 10;

        Rect textRect = new Rect();

        if (isTwoStatus) {
            if (status == CompleteRedeemStatus.ONE) {
                if (index == 0) {
                    top = RADIUS + 10;
                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[index], 0,
                            date[index].length(), textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    top2 = textHtight / 2 + RADIUS + 15 - textRect.height() / 2
                            + PADING + 20;
                    canvas.drawText(text[index], right, top, paint);
                    canvas.drawText(date[index], right, top2 + textHtight,
                            paintText);
                } else if (index == 1) {
                    top = (viewHeight - textHtight) / 2 - 10;
                    top2 = (viewHeight - textRect.height()) / 2 + RACT_HEIGHT
                            * 2;
                    paint.setColor(getResources().getColor(R.color.boc_black));
                    paintText.getTextBounds(date[1], 0, date[1].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_black));
                    canvas.drawText(text[1], right, top, paint);
                    canvas.drawText(date[1], right, top2 + textHtight,
                            paintText);
                }
            } else if (status == CompleteRedeemStatus.TWO) {
                if (index == 0) {
                    top = RADIUS + 10;
                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[index], 0,
                            date[index].length(), textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    top2 = textHtight / 2 + RADIUS + 15 - textRect.height() / 2
                            + PADING + 20;
                    canvas.drawText(text[0], right, top, paint);
                    canvas.drawText(date[0], right, top2 + textHtight,
                            paintText);
                } else if (index == 1) {
                    top = (viewHeight - textHtight) / 2 - 10;
                    top2 = (viewHeight - textRect.height()) / 2 + RACT_HEIGHT
                            * 2;

                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[index], 0,
                            date[index].length(), textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    canvas.drawText(text[1], right, top, paint);
                    canvas.drawText(date[1], right, top2 + textHtight,
                            paintText);
                }
            }
        } else {
            if (status == CompleteRedeemStatus.ONE) {
                if (index == 0) {
                    top = RADIUS + 10;
                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[index], 0,
                            date[index].length(), textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    top2 = textHtight / 2 + RADIUS + 15 - textRect.height() / 2
                            + PADING + 20;
                    canvas.drawText(text[0], right, top, paint);
                    canvas.drawText(date[0], right, top2 + textHtight,
                            paintText);
                } else if (index == 1) {
                    top = (viewHeight - textHtight) / 2 - 10;
                    top2 = (viewHeight - textRect.height()) / 2 + RACT_HEIGHT
                            * 2;

                    paint.setColor(getResources().getColor(R.color.boc_black));
                    paintText.getTextBounds(date[index], 0,
                            date[index].length(), textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_black));
                    canvas.drawText(text[1], right, top, paint);
                    canvas.drawText(date[1], right, top2 + textHtight,
                            paintText);
                } else if (index == 2) {
                    top = viewHeight - textHtight - 55;
                    top2 = viewHeight - textRect.height() - 5;

                    paint.setColor(getResources().getColor(R.color.boc_black));
                    paintText.getTextBounds(date[2], 0, date[index].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_black));
                    canvas.drawText(text[2], right, top, paint);
                    canvas.drawText(date[2], right, top2 + textHtight,
                            paintText);
                }
            } else if (status == CompleteRedeemStatus.TWO) {
                if (index == 0) {
                    top = RADIUS + 10;
                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[0], 0, date[0].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    top2 = textHtight / 2 + RADIUS + 15 - textRect.height() / 2
                            + PADING + 20;
                    canvas.drawText(text[0], right, top, paint);
                    canvas.drawText(date[0], right, top2 + textHtight,
                            paintText);
                } else if (index == 1) {
                    top = (viewHeight - textHtight) / 2;
                    top2 = (viewHeight - textRect.height()) / 2 + RACT_HEIGHT
                            * 2 + 10;

                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[1], 0, date[1].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    canvas.drawText(text[1], right, top, paint);
                    canvas.drawText(date[1], right, top2 + textHtight,
                            paintText);
                } else if (index == 2) {
                    top = viewHeight - textHtight - 55;
                    top2 = viewHeight - textRect.height() - 5;

                    paint.setColor(getResources().getColor(R.color.boc_black));
                    paintText.getTextBounds(date[2], 0, date[2].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_black));
                    canvas.drawText(text[2], right, top, paint);
                    canvas.drawText(date[2], right, top2 + textHtight,
                            paintText);
                }
            } else if (status == CompleteRedeemStatus.THREE) {
                if (index == 0) {
                    top = RADIUS + 10;
                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[0], 0, date[0].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    top2 = textHtight / 2 + RADIUS + 15 - textRect.height() / 2
                            + PADING + 20;
                    canvas.drawText(text[0], right, top, paint);
                    canvas.drawText(date[0], right, top2 + textHtight,
                            paintText);
                } else if (index == 1) {
                    top = (viewHeight - textHtight) / 2 - 10;
                    top2 = (viewHeight - textRect.height()) / 2 + RACT_HEIGHT
                            * 2;

                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[1], 0, date[1].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    canvas.drawText(text[1], right, top, paint);
                    canvas.drawText(date[1], right, top2 + textHtight,
                            paintText);
                } else if (index == 2) {
                    top = viewHeight - textHtight - 55;
                    top2 = viewHeight - textRect.height() - 5;

                    paint.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    paintText.getTextBounds(date[2], 0, date[2].length(),
                            textRect);
                    paintText.setColor(getResources().getColor(
                            R.color.boc_round_progressbar_green));
                    canvas.drawText(text[2], right, top, paint);
                    canvas.drawText(date[2], right, top2 + textHtight,
                            paintText);
                }
            }
        }
    }

    public CompleteRedeemStatus getStatus() {
        return status;
    }

    /**
     * \ 设置流程走到的位置
     *
     * @param status
     */
    public void setStatus(CompleteRedeemStatus status) {
        this.status = status;
    }

    /**
     * 设置所显示的 文本信息 value
     *
     * @param date
     */
    public void setItemViewValue(String[] date) {
        this.date = date;
    }

    /**
     * 设置所显示的 文本信息 title
     *
     * @param text
     */
    public void setItemViewTitle(String[] text) {
        this.text = text;
        if (text.length == 2) {
            isTwoStatus = true;
        } else if (text.length == 3) {
            isTwoStatus = false;
        }

    }

}