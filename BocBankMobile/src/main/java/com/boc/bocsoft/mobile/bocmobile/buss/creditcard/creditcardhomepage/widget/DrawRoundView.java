package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;


/**
 * 自定义球体
 * Created by wangf on 2016/11/23.
 */

public class DrawRoundView extends View {

    private Context mContext;
    //屏幕宽度
    private int screenWidth;
    //屏幕高度
    private int screenHeight;

    /**
     * 大圆数据
     */
    //圆的顶部距屏幕上部的距离
    private float roundOffsetTopY = 0;
    //圆的底部Y坐标
    private float roundBigBottomY;
    //圆的底部X坐标
    private float roundBigBottomX;
    //圆的顶部Y坐标
    private float roundBigTopY;
    //圆的顶部X坐标
    private float roundBigTopX;
    //圆的半径
    private float radiusBig;
    //圆心X坐标
    private float roundBigCX;
    //圆心Y坐标
    private float roundBigCY;

    /**
     * 小圆数据
     */
    //圆的顶部Y坐标
    private float roundSmallTopY;
    //圆的顶部X坐标
    private float roundSmallTopX;
    //圆的半径
    private float radiusSmall;
    //圆心X坐标
    private float roundSmallCX;
    //圆心Y坐标
    private float roundSmallCY;

    //大圆是否位于前端
    private boolean isBigFirst;
    //小圆是否位于前端
    private boolean isSmallFirst;

    /**
     * 页面数据
     */
    private int[] colorBigRound;
    private int[] colorSmallRound;
    //大圆文字数据
    private String[] textBig;
    //小圆文字数据
    private String[] textSmall;

    private RoundTouchCallBack roundTouchCallBack;



    public DrawRoundView(Context context) {
        this(context, null);
    }

    public DrawRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawRoundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        screenWidth = ResUtils.getScreenWidth(mContext);
        screenHeight = ResUtils.getScreenHeight(mContext);
        initViewData();
        initDefaultData();
    }

    /**
     * 初始化页面数据
     */
    private void initViewData() {
        radiusBig = screenWidth * 2 / 7;
        roundBigBottomX = screenWidth / 2;
        roundBigBottomY = radiusBig * 2 + roundOffsetTopY;
        roundBigCX = roundBigBottomX;
        roundBigCY = roundBigBottomY - radiusBig;
        roundBigTopX = roundBigBottomX;
        roundBigTopY = roundBigBottomY - radiusBig * 2;

        radiusSmall = screenWidth * 3 / 14;
        roundSmallCX = roundBigBottomX;
        roundSmallCY = roundBigBottomY - radiusSmall;
        roundSmallTopX = roundBigBottomX;
        roundSmallTopY = roundBigBottomY - radiusSmall * 2;
    }

    /**
     * 初始化默认数据
     */
    private void initDefaultData() {
        isSmallFirst = true;
        isBigFirst = false;

        textBig = new String[]{" ", " "};
        textSmall = new String[]{" ", " "};
        colorBigRound = new int[]{R.color.boc_blue_start, R.color.boc_blue_end};
        colorSmallRound = new int[]{R.color.boc_yellow_start, R.color.boc_yellow_end};
    }


    /**
     * 设置额度数据
     * @param isAvailableGreater 可用额度是否大于授信额度
     * @param textAvailableLimit 可用额度
     * @param textCreditLimit 授信额度
     */
    public void setLimitData(boolean isAvailableGreater, String[] textAvailableLimit, String[] textCreditLimit){
        if (!isAvailableGreater){
            textBig = textCreditLimit;
            textSmall = textAvailableLimit;
            colorBigRound = new int[]{R.color.boc_blue_start, R.color.boc_blue_end};
            colorSmallRound = new int[]{R.color.boc_yellow_start, R.color.boc_yellow_end};
        }else{
            textBig = textAvailableLimit;
            textSmall = textCreditLimit;
            colorBigRound = new int[]{R.color.boc_yellow_start, R.color.boc_yellow_end};
            colorSmallRound = new int[]{R.color.boc_blue_start, R.color.boc_blue_end};
        }
    }


    /**
     * 重新测量View所需要的空间大小
     * EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
     * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
     * UNSPECIFIED：表示子布局想要多大就多大，很少使用
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            // 此处为设置View的宽度，由于目前View左右都无其他控件，故直接给画布width为屏幕的宽度
            width = widthSize;
            // 将画布的width设置为View的宽度
//            width = (int)radiusBig * 2;
//            roundBigBottomX = width / 2;
//            roundBigCX = roundBigBottomX;
//            roundBigTopX = roundBigBottomX;
//            roundSmallCX = roundBigBottomX;
//            roundSmallTopX = roundBigBottomX;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int)roundBigBottomY;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isBigFirst) {
            drawSmallRound(canvas, isSmallFirst, colorSmallRound, textSmall);
            drawBigRound(canvas, isBigFirst, colorBigRound, textBig);
        } else {
            drawBigRound(canvas, isBigFirst, colorBigRound, textBig);
            drawSmallRound(canvas, isSmallFirst, colorSmallRound, textSmall);
        }
    }

    /**
     * 大圆
     *
     * @param canvas
     * @param isFirst
     * @param colorGradient
     * @param text
     */
    private void drawBigRound(Canvas canvas, boolean isFirst, int[] colorGradient, String[] text) {
        Paint p = new Paint();
        p.setAntiAlias(true);// 设置画笔的锯齿效果, true为去除锯齿

        p.setColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        p.setTextSize(25);
        Rect boundsRectTitle = new Rect();
        p.getTextBounds(text[0], 0, text[0].length(), boundsRectTitle);
        int textWidthTitle = boundsRectTitle.width();
        if (!isFirst) {
            p.setAlpha(100);
        }
        float textTitleX = roundBigTopX - textWidthTitle / 2;
        float textTitleY = roundBigTopY + radiusBig / 5;
        canvas.drawText(text[0], textTitleX, textTitleY, p);

        if (isFirst) {
            p.setTextSize(30);
            Rect boundsRectContent1 = new Rect();
            p.getTextBounds(text[1], 0, text[1].length(), boundsRectContent1);
            int textWidthContent1 = boundsRectContent1.width();
            p.setFakeBoldText(true);
            p.setAlpha(255);
            float textContentX1 = roundBigTopX - textWidthContent1 / 2;
            float textContentY1 = textTitleY + boundsRectContent1.height() + 10;
            canvas.drawText(text[1], textContentX1, textContentY1, p);

            if (text.length == 3){
                p.setTextSize(30);
                Rect boundsRectContent2 = new Rect();
                p.getTextBounds(text[2], 0, text[2].length(), boundsRectContent2);
                int textWidthContent2 = boundsRectContent2.width();
                p.setFakeBoldText(true);
                p.setAlpha(255);
                float textContentX2 = roundBigTopX - textWidthContent2 / 2;
                float textContentY2 = textContentY1 + boundsRectContent2.height() + 10;
                canvas.drawText(text[2], textContentX2, textContentY2, p);
            }
        }

        RadialGradient lg = new RadialGradient(roundBigCX, roundBigCY - radiusBig / 2, radiusBig * 2, getResources().getColor(colorGradient[0]), getResources().getColor(colorGradient[1]), Shader.TileMode.REPEAT);
        p.setShader(lg);

        if (isFirst) {
            p.setAlpha(150);
        } else {
            p.setAlpha(100);
        }
        canvas.drawCircle(roundBigCX, roundBigCY, radiusBig, p);
    }

    /**
     * 小圆
     *
     * @param canvas
     * @param isFirst
     * @param colorGradient
     * @param text
     */
    private void drawSmallRound(Canvas canvas, boolean isFirst, int[] colorGradient, String[] text) {
        Paint p = new Paint();
        p.setAntiAlias(true);// 设置画笔的锯齿效果, true为去除锯齿

        p.setColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        p.setTextSize(25);
        Rect boundsRectTitle = new Rect();
        p.getTextBounds(text[0], 0, text[0].length(), boundsRectTitle);
        int textWidthTitle = boundsRectTitle.width();
        if (!isFirst) {
            p.setAlpha(100);
        }
        float textTitleX = roundSmallTopX - textWidthTitle / 2;
        float textTitleY = roundSmallTopY + radiusSmall / 5 + 10;
        canvas.drawText(text[0], textTitleX, textTitleY, p);


        if (isFirst) {
            p.setTextSize(30);
            Rect boundsRectContent1 = new Rect();
            p.getTextBounds(text[1], 0, text[1].length(), boundsRectContent1);
            int textWidthContent1 = boundsRectContent1.width();
            p.setFakeBoldText(true);
            p.setAlpha(255);
            float textContentX1 = roundSmallTopX - textWidthContent1 / 2;
            float textContentY1 = textTitleY + boundsRectContent1.height() + 10;
            canvas.drawText(text[1], textContentX1, textContentY1, p);

            if (text.length == 3){
                p.setTextSize(30);
                Rect boundsRectContent2 = new Rect();
                p.getTextBounds(text[2], 0, text[2].length(), boundsRectContent2);
                int textWidthContent2 = boundsRectContent2.width();
                p.setFakeBoldText(true);
                p.setAlpha(255);
                float textContentX2 = roundSmallTopX - textWidthContent2 / 2;
                float textContentY2 = textContentY1 + boundsRectContent2.height() + 10;
                canvas.drawText(text[2], textContentX2, textContentY2, p);
            }
        }

        RadialGradient lg = new RadialGradient(roundSmallCX, roundSmallCY - radiusSmall / 2, radiusSmall * 2, getResources().getColor(colorGradient[0]), getResources().getColor(colorGradient[1]), Shader.TileMode.REPEAT);
        p.setShader(lg);

        if (isFirst) {
            p.setAlpha(150);
        } else {
            p.setAlpha(100);
        }
        canvas.drawCircle(roundSmallCX, roundSmallCY, radiusSmall, p);
    }


    /**
     * 判断点击屏幕的区域
     *
     * @param x
     * @param y
     */
    private void whichRound(int x, int y) {
        if (judgeRoundInside(x, y, roundSmallCX, roundSmallCY, radiusSmall)) {
            // 点击小圆
            isSmallFirst = true;
            isBigFirst = false;
            if (roundTouchCallBack != null){
                roundTouchCallBack.onTouchRoundInside(false);
            }
            return;
        }
        if (judgeRoundInside(x, y, roundBigCX, roundBigCY, radiusBig)) {
            // 点击大圆
            isSmallFirst = false;
            isBigFirst = true;
            if (roundTouchCallBack != null){
                roundTouchCallBack.onTouchRoundInside(true);
            }
        }
    }


    /**
     * 判断点击位置是否在圆的内部
     *
     * @param x1 点击位置X坐标
     * @param y1 点击位置Y坐标
     * @param x2 圆心X坐标
     * @param y2 圆心Y坐标
     * @return 是否在圆内部
     */
    private boolean judgeRoundInside(float x1, float y1, float x2, float y2, float r) {
        float distanceX = x2 - x1;
        float distanceY = y2 - y1;
        float distanceZ = (float) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        if (distanceZ > r) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            whichRound(x, y);
            invalidate();
        }
        return super.onTouchEvent(event);
    }



    public void setRoundTouchListener(RoundTouchCallBack roundTouchCallBack){
        this.roundTouchCallBack = roundTouchCallBack;
    }

    public interface RoundTouchCallBack{
        void onTouchRoundInside(boolean isTouchBigRound);
    }

}
