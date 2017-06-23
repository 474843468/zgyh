package com.boc.bocsoft.mobile.bocmobile.base.widget.SpringPressageView;



import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 中银理财-持仓详情-预期年华收益率->自定义动态加载进度条，按百分比显示
 * Created by zn on 2016/11/3.
 */
public class SpringProgressView extends View {
    /**
     * 设置画笔颜色
     */
    public static final String PROGRESS_COLOR = "#99ff0000";
    /**
     * 根据比例获取绘制宽度
     */
    private float mProgressWidth;
    /**
     * 分段颜色
     */
    private static final int[] SECTION_COLORS = {Color.rgb(242, 46, 17), Color.rgb(242, 46, 17), Color.rgb(242, 46, 17)};
    /**
     * 进度条最大值
     */
    private float maxCount;
    /**
     * 进度条当前值
     */
    private float currentCount;
    /**
     * 画笔
     */
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float progress_incom;
    public static final int HANDLER_CODE=0x1111;
    /**
     * 计算比例
     */
    private float thread_progress = 0;
    /**
     * 设置动画执行时间
     */
    Timer time = new Timer();
    private float adpter_progress_incom;


    public SpringProgressView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SpringProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpringProgressView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        int round = mHeight / 2;
//        mPaint.setColor(Color.WHITE);
//        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
//        canvas.drawRoundRect(rectBg, round, round, mPaint);
//        float section = currentCount / maxCount;
//        mProgressWidth = mWidth * section;
//        RectF rectBlackBg = new RectF(0, 0, mProgressWidth, mHeight);
//        mPaint.setColor(Color.parseColor(PROGRESS_COLOR));
//        canvas.drawRoundRect(rectBlackBg, round, round, mPaint);
//        time.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (thread_progress < 100) {
//                    Log.i("thread_progress =====",thread_progress+"");
//                    thread_progress++;
//                    mHandler.sendEmptyMessage(HANDLER_CODE);
//                }
//            }
//        }, 300, 50);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int round = mHeight / 2;
        mPaint.setColor(Color.WHITE);
        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, round, round, mPaint);
        float section = currentCount / maxCount;
        mProgressWidth = mWidth * section;
        RectF rectBlackBg = new RectF(0, 0, mProgressWidth, mHeight);
        mPaint.setColor(Color.parseColor(PROGRESS_COLOR));
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint);
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_CODE) {
                invalidate();
            }
        }
    };
    /**
     * 在onDestory方法中调用
     */
    public void cancelTimer() {
        time.cancel();
        time = null;
    }
    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     *
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(40);
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    //public void setContext(float maxCount, float currentCount,float progress_incom) {
//        float percent = 100 - progress_incom;
//        this.progress_incom = progress_incom;
//        this.thread_progress = progress_incom;
//        setMaxCount(maxCount);
//        LogUtils.i("--------yx------%="+progress_incom);
//        setCurrentCount(progress_incom);
//        doAnimator();
   // }
//    private void doAnimator( ) {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(SpringProgressView.this, "currentCount",0,progress_incom);
//        animator.setDuration(2000);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.start();
//    }
    public void setContext(float maxCount, float currentCount,float progress_incom) {
        setMaxCount(maxCount);
        setCurrentCount(currentCount);
        doAnimator();
    }

    private void doAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(SpringProgressView.this, "currentCount", 0, currentCount);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
    public void setAdapterContext(String Max, String Min) {
        Float intMax = Float.valueOf(Max);
        Float intMin = Float.valueOf(Min);
        if (intMax!=0 && intMin<=intMax){
            adpter_progress_incom = (intMin / intMax) * 100f;
            Log.i("progress_show =",adpter_progress_incom+"");
        }
        setContext(intMax,intMin,adpter_progress_incom);
    }
}
