package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.chartview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 折线图（信用卡历史账单）
 * Created by liuweidong on 2016/12/25.
 */
public class LineChartView extends View {
    private Context mContext;
    private int screenWidth;
    private int screenHeight;
    private int xPoint;
    private int yPoint;
    private int xLength;
    private int yLength;
    private String[] xData = {"7月", "8月", "9月", "10月", "11月", "12月"};
    private int[] yData = {2000, 4000, 6000, 8000, 10000};
    private int[] datas = {2000, 7000, 3000, 5000, 8000, 4000};

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    private void initView() {
        WindowManager vm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = vm.getDefaultDisplay().getWidth();
        screenHeight = vm.getDefaultDisplay().getHeight();
        xPoint = 100;
        yPoint = 500;
        xLength = screenWidth - xPoint - 50;
        yLength = yPoint - 50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*画直线的画笔*/
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 消除锯齿
        paint.setStrokeWidth(1);// 画笔宽度
        paint.setStrokeJoin(Paint.Join.MITER);// 结合处为锐角
        paint.setColor(Color.WHITE);

        canvas.drawLine(50, 2, 50, 5, paint);
        drawXLine(canvas, paint);
        drawYLine(canvas, paint);
        drawXValue(canvas);
        drawYValue(canvas);
        drawLineChart(canvas);
    }

    private void drawXLine(Canvas canvas, Paint paint) {
        int xScale = (xLength - 50) / (xData.length - 1);
        for (int i = 0; i < xData.length; i++) {
            int xCoordinate = xPoint + xScale * i;
            canvas.drawLine(xCoordinate, yPoint, xCoordinate, yPoint - yLength, paint);
        }
    }

    private void drawYLine(Canvas canvas, Paint paint) {
        int yScale = (yLength - 50) / (yData.length - 1);
        for (int i = 0; i < yData.length; i++) {
            int yCoordinate = yPoint - yScale * i;
            canvas.drawLine(xPoint, yCoordinate, xPoint + xLength, yCoordinate, paint);
        }
    }

    private void drawXValue(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 消除锯齿
        paint.setStrokeWidth(1);// 画笔宽度
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);

        int xScale = (xLength - 50) / (xData.length - 1);
        for (int i = 0; i < xData.length; i++) {
            int xCoordinate = xPoint + xScale * i;
            canvas.drawText(xData[i], xCoordinate, yPoint + 30, paint);
        }
    }

    private void drawYValue(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 消除锯齿
        paint.setStrokeWidth(1);// 画笔宽度
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);

        int yScale = (yLength - 50) / (yData.length - 1);
        for (int i = 0; i < yData.length; i++) {
            int yCoordinate = yPoint - yScale * i;
            canvas.drawText(String.valueOf(yData[i]), xPoint - 40, yCoordinate, paint);
        }
    }

    private void drawLineChart(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 消除锯齿
        paint.setStrokeWidth(5);// 画笔宽度
        paint.setColor(Color.WHITE);

        int xScale = (xLength - 50) / (xData.length - 1);
        int yRange = yData[yData.length - 1] - yData[0];
        int yPonitRange = yPoint - 100;
        float[] xCoordinates = new float[xData.length];
        float[] yCoordinates = new float[xData.length];
        for (int i = 0; i < xData.length; i++) {
            xCoordinates[i] = xPoint + xScale * i;
            if(yData[0] == datas[i]){
                yCoordinates[i] = 500;
                continue;
            }
            int d = yData[yData.length - 1] - datas[i];
            float temp = ((float)yPonitRange/yRange) *d;
            yCoordinates[i] = temp +100;
        }

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(3000);
        animatorX.start();
        for (int i = 0; i < xData.length - 1; i++) {
            canvas.drawLine(xCoordinates[i], yCoordinates[i], xCoordinates[i + 1], yCoordinates[i + 1], paint);
        }
    }

    public void animateX(int durationMillis) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillis);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });
        animatorX.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
