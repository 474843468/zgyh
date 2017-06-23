package com.boc.bocsoft.mobile.bocmobile.base.widget.index;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 快速索引条
 * Created by zhx on 2016/7/19
 */
public class QuickIndexBar2 extends View {
    private String[] indexArr = {"我", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint paint;
    private int width;// QuickIndexView的宽
    private int height;// QuickIndexView的高度
    private float cellHeight;// 格子高度，将整个view的高度进行26等分
    private Integer bgColorNormal; // 背景颜色（未按下）
    private Integer bgColorPress; // 背景颜色（按下）
    private Integer indexTextColor; // 索引文字颜色
    private Integer indexTextColorPress; // 索引文字颜色(按下)
    private int indexTextSize = 0; // 索引文字大小

    private boolean isTouch = false; // 是否正在触摸

    public QuickIndexBar2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public QuickIndexBar2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar2(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        float letter_size = 0; // TODO
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 设置抗锯齿
        paint.setColor(Color.BLACK);// 白色字体
        paint.setTextSize(letter_size);
        paint.setTextAlign(Paint.Align.CENTER);// 设置绘制文本的起点是文本的底边的中心

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexBar);
            bgColorNormal = a.getColor(R.styleable.QuickIndexBar_bgColorNormal, 0);
            bgColorPress = a.getColor(R.styleable.QuickIndexBar_bgColorPress, 0);
            indexTextColor = a.getColor(R.styleable.QuickIndexBar_indexTextColor, Color.BLACK);
            indexTextColorPress = a.getColor(R.styleable.QuickIndexBar_indexTextColorPress, Color.WHITE);
            indexTextSize = (int) a.getDimension(R.styleable.QuickIndexBar_indexTextSize, 32);
            a.recycle();
        }

        paint.setColor(indexTextColor != null ? indexTextColor : Color.BLACK);
        paint.setTextSize(indexTextSize);

        // 设置背景颜色（未按下时）
        this.setBackgroundColor(bgColorNormal != null ? bgColorNormal : Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        cellHeight = ((float) height) / indexArr.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1.将26个字母等分的绘制到对应的位置上
        for (int i = 0; i < indexArr.length; i++) {
            float x = width / 2;
            float y = cellHeight / 2 + getTextHeight(indexArr[i]) / 2 + i
                    * cellHeight;

            if (!isTouch) {
                paint.setColor(indexTextColor != null ? indexTextColor : Color.BLACK);
            } else {
                if (indexTextColorPress != null) {
                    paint.setColor(i == lastIndex ? Color.GREEN : indexTextColorPress);
                } else {
                    paint.setColor(i == lastIndex ? Color.GREEN : Color.BLACK);
                }
            }

            canvas.drawText(indexArr[i], x, y, paint);
        }

    }

    private int lastIndex = -1;// 上次所触摸到的字母的索引

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isTouch = true;

                float y = event.getY();
                int index = (int) (y / cellHeight);// 当前触摸到的字母的索引
                if (index != lastIndex) {
                    // Log.e(tag, indexArr[index]);
                    // 对index作检查
                    if (index >= 0 && index < indexArr.length) {
                        if (listener != null) {
                            listener.onTouchLetter(indexArr[index]);
                        }
                    }
                }
                lastIndex = index;

                // 设置背景颜色（按下时）
                this.setBackgroundColor(bgColorPress != null ? bgColorPress : Color.GRAY);

                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;

                lastIndex = -1;// 抬起的时候重置索引
                if (listener != null) {
                    listener.onTouchUp();
                }

                // 设置背景颜色（未按下时）
                this.setBackgroundColor(bgColorNormal != null ? bgColorNormal : Color.TRANSPARENT);

                break;
        }
        invalidate();// 重绘
        return true;
    }

    /**
     * 获取文本的高度
     *
     * @param text
     * @return 文本的高度
     */
    private float getTextHeight(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);// 该方法一执行，bounds就有数据了
        return bounds.height();
    }

    private OnTouchLetterListener listener;

    public void setOnTouchLetterListener(OnTouchLetterListener listener) {
        this.listener = listener;
    }

    /**
     * 索引文字触摸监听器
     */
    public interface OnTouchLetterListener {
        /**
         * (在垂直方向)移动到当前索引文字时调用
         *
         * @param word
         */
        void onTouchLetter(String word);

        /**
         * 手松开时调用此方法
         */
        void onTouchUp();
    }
}