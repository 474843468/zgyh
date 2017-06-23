package com.boc.bocsoft.mobile.bocmobile.base.widget.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * 圆环进度条
 */
public class CircleProgressBar extends View {
    private int mCircleStrokeColor;//圆的边框颜色
    private int mCircleRadius;//圆的半径
    private int mProgressColor;//进度条颜色
    private int mProgressWidth;//进度条宽度
    private int mTextColor;//文字颜色
    private int mTextSize;//文字大小
    private int mTextSecondSize;//秒字大小

    private Paint mCiclePaint;
    private Paint mProgressPaint;
    private TextPaint mTextPaint, mTextSecondPaint;

    private String mText;

    private int mProgress;//进度
    private int mMax;//最大值

    private Point mDrawCenterPoint;
    private int mDrawRadius;
    private RectF mDrawRecF;
    private float mTextWidth, mTextSecondWidth;
    private float mTextHeight, mTextSecondHeight;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a =
                getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyle,
                        0);
        mCircleStrokeColor =
                a.getColor(R.styleable.CircleProgressBar_circle_stroke_color, Color.GRAY);
        mCircleRadius = a.getDimensionPixelOffset(R.styleable.CircleProgressBar_circle_radius,
                mCircleRadius);
        mProgressColor = a.getColor(R.styleable.CircleProgressBar_progress_color, Color.GREEN);
        mProgressWidth = a.getDimensionPixelOffset(R.styleable.CircleProgressBar_progress_width,
                mProgressWidth);
        mTextColor = a.getColor(R.styleable.CircleProgressBar_text_color, Color.BLACK);
        mTextSize = a.getDimensionPixelOffset(R.styleable.CircleProgressBar_text_size, mTextSize);
        mTextSecondSize = a.getDimensionPixelOffset(R.styleable.CircleProgressBar_text_second_size,
                mTextSize);
        a.recycle();
        mDrawCenterPoint = new Point();
        initPaint();
    }

    private void initPaint() {
        mCiclePaint = new Paint();
        mCiclePaint.setAntiAlias(true);//防锯齿
        mCiclePaint.setDither(true);//防抖动
        mCiclePaint.setStyle(Paint.Style.STROKE);//描边
        mCiclePaint.setStrokeWidth(mProgressWidth);
        mCiclePaint.setColor(mCircleStrokeColor);
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);//描边
        mProgressPaint.setStrokeWidth(mProgressWidth);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextSecondPaint = new TextPaint();
        mTextSecondPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        invalidateTextPaintAndMeasurements();
        invalidateSecondTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        if (mText == null) {
            return;
        }
        mTextWidth = mTextPaint.measureText(mText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
    }

    private void invalidateSecondTextPaintAndMeasurements() {
        mTextSecondPaint.setColor(mTextColor);
        mTextSecondPaint.setTextSize(mTextSecondSize);
        mTextSecondWidth = mTextSecondPaint.measureText("秒");
        Paint.FontMetrics fontMetrics = mTextSecondPaint.getFontMetrics();
        mTextSecondHeight = fontMetrics.descent - fontMetrics.ascent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize =
                    getPaddingLeft() + getPaddingRight() + 2 * mCircleRadius + 2 * mProgressWidth;
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize =
                    getPaddingTop() + getPaddingBottom() + 2 * mCircleRadius + 2 * mProgressWidth;
        }
        final int measuredWidth = resolveSizeAndState(widthSize, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(heightSize, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawCenterPoint = new Point(w / 2, h / 2);
        int contentWidth = w - getPaddingLeft() - getPaddingRight();
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        mDrawRadius = Math.min(contentHeight, contentWidth) / 2 - mProgressWidth;
        mDrawRecF = new RectF(mDrawCenterPoint.x - mDrawRadius, mDrawCenterPoint.y - mDrawRadius,
                mDrawCenterPoint.x + mDrawRadius, mDrawCenterPoint.x + mDrawRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mDrawCenterPoint.x, mDrawCenterPoint.y, mDrawRadius, mCiclePaint);
        // 注意扫过的扇形的范围(进度)要和绘制的小圆点保持一致,所以我们需要从-90度开始
        canvas.drawArc(mDrawRecF, -90, getProgressRate() * 360, false, mProgressPaint);
        //drawText的参数位置是baseline位置,在baseline下边还有一块descent区域显示了部分文字。
        //所以要减去descent，以使整个文字真正居中。
        if (mText != null) {
            canvas.drawText(mText,
                    mDrawCenterPoint.x + ResUtils.dip2px(getContext(), 10) - mTextWidth,
                    mDrawCenterPoint.y - mTextPaint.descent() + mTextHeight / 2, mTextPaint);
        }
        //绘制秒字的位置
        canvas.drawText("秒", mDrawCenterPoint.x + ResUtils.dip2px(getContext(), 10),
                mDrawCenterPoint.y - mTextPaint.descent() + mTextHeight / 2, mTextSecondPaint);
        canvas.restore();
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * 获取进度比
     *
     * @return 小数形式的进度比
     */
    public float getProgressRate() {
        return (float) mProgress / mMax;
    }

    /**
     * 获取百分比形式的进度
     *
     * @return 百分比形式的进度，如1%
     */
    public String getProgressPercent() {
        return ((int) ((float) mProgress / mMax * 100)) + "%";
    }

    public synchronized void setProgress(int progress) {
        this.mProgress = validateProgress(progress);
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    /**
     * 验证进度。
     *
     * @param progress 你要验证的进度值。
     * @return 返回真正的进度值。
     */
    private int validateProgress(int progress) {
        if (progress > mMax) {
            progress = mMax;
        } else if (progress < 0) {
            progress = 0;
        }
        return progress;
    }

    public int getCircleStrokeColor() {
        return mCircleStrokeColor;
    }

    public void setCircleStrokeColor(int circleStrokeColor) {
        this.mCircleStrokeColor = circleStrokeColor;
    }

    public float getCircleRadius() {
        return mCircleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.mCircleRadius = circleRadius;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
    }

    public float getProgressWidth() {
        return mProgressWidth;
    }

    public void setProgressWidth(int progressWidth) {
        this.mProgressWidth = progressWidth;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        invalidateTextPaintAndMeasurements();
        invalidateSecondTextPaintAndMeasurements();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        invalidateTextPaintAndMeasurements();
    }

    public int getTextSecondSize() {
        return mTextSecondSize;
    }

    public void setTextSecondSize(int textSecondSize) {
        mTextSecondSize = textSecondSize;
        invalidateSecondTextPaintAndMeasurements();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        invalidateTextPaintAndMeasurements();
    }
}
