package com.boc.bocsoft.mobile.bocmobile.base.widget.waterwaveballview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * @author  ws3820
 * @date 2016-05-26
 * 球形中间为晃动水波的组件
 * 组件四周为方形，中间部分显示球形
 * 设置水面最终显示的高度  {@link #setFinallyWaterHeight(float)}
 * 水波动画开始 {@link #startWave()}
 * 水波动画结束 {@link #stopWave()} 务必在onDestroy的时候调用此方法
 */
public class WaterWaveBallView extends View {

    private Context mContext;

    // 当前view的宽和高
    private int mViewWidth;
    private int mViewHeight;

    // 画笔
//    private Paint mBallPaint;
    private Paint mWaterPaint;

    // 水的透明度
    private int mWaterAlpha = 200;

    // 球的颜色和水的颜色  都是渐变色
    private int ballColorTop = 0xFFB5A9D8;
    private int ballColorBottom = 0xFF487AB1;
    private int waterColorTop = 0xFF3992FF;
    private int waterColorBottom = 0xFF3DEAFF;

    // 控制水波的晃动的参数
    private Handler mHandler;
    private long c = 0L;
    private boolean isStarted = false;
    private final float f = 0.033F;
    private float scale = 1.0f;
    private float mAmplitude = 15.0F; // 振幅`
    private float mWaveLength = 1.5f; //水波长度
    private long mSpeed = 1L; //水波移动速度
    private float mWaterHeight = 0.0F;// 水高(0~1)
    private float mFinallyWaterHeight;
    private float mWaterRisingSpeed = 0.025F;

    public WaterWaveBallView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public WaterWaveBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public WaterWaveBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        mWaterPaint = new Paint();
        mWaterPaint.setStrokeWidth(1.0F);
        mWaterPaint.setAlpha(mWaterAlpha);

        // 此handler不断的调用invalidate来重新出发ondraw实现动画
        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    if(mWaterHeight < mFinallyWaterHeight) {
                        // 水位从底部逐渐上升到固定高度
                        mWaterHeight = mWaterHeight + mWaterRisingSpeed;
                        setWaterHeight(mWaterHeight);
                    } else {
                        setWaterHeight(mFinallyWaterHeight);
                    }
                    invalidate();
                    if (isStarted) {
                        // 不断发消息给自己，使自己不断被重绘
                        mHandler.sendEmptyMessageDelayed(0, 20L);
                    }
                }
            }
        };
    }

    /**
     * 对要绘制的view的宽和高做处理
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = measure(widthMeasureSpec, true);
//        int height = measure(heightMeasureSpec, false);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 无论横屏还是竖屏，view被设置成长方形，都以宽和高中的短边为边长，将view绘制成正方形
        if (width < height) {
            setMeasuredDimension(width, width);
        } else {
            setMeasuredDimension(height, height);
        }

    }

    /**
     *  此方法已废弃不用
     */
    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight()
                : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth()
                    : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    /**
     * 在这个方法绘制view的每一帧
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 先将canvas裁剪成圆形区域，后续所有的绘制都只能显示在这个圆形区域内
        canvas.save();
        Path path = new Path();
        path.reset();
        canvas.clipPath(path);
        path.addCircle(mViewWidth / 2, mViewHeight / 2, scale * mViewWidth / 2 - 15, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);

        // 得到控件的宽高
        int width = getWidth();
        int height = getHeight();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boc_waterwavebg);
        Rect bgMap = new Rect(0, 0, width, height);
        canvas.drawBitmap(bitmap, null, bgMap, null);

        // 防止c不断增加，使得float型溢出
        if (this.c >= 8388607L) {
            this.c = 0L;
        }

        // 每次onDraw时c都会自增
        c = (mSpeed + c);
        float f1  = mViewHeight * (0.5f + scale * (0.5f - mWaterHeight)) - mAmplitude;
        int top = (int) (f1 + mAmplitude);

        // 水波振幅之下的水，由于水有透明图，为了与水波颜色上过度自然，故而绘制了两层
        LinearGradient waterGradient = new LinearGradient(mViewWidth / 2, top, mViewWidth / 2,
                mViewHeight * (1 + scale) / 2, waterColorBottom, waterColorTop, Shader.TileMode.MIRROR);

        /*LinearGradient waterGradient = new LinearGradient(mViewWidth / 2, top, mViewWidth / 2,
                mViewHeight * (1 + scale) / 2, 0, 0, Shader.TileMode.MIRROR);*/
        mWaterPaint.setShader(waterGradient);
        canvas.drawRect(mViewWidth * (1 - scale) / 2, top,
                mViewWidth * (1 + scale) / 2, mViewHeight * (1 + scale) / 2, mWaterPaint);
        canvas.drawRect(mViewWidth * (1 - scale) / 2, top,
                mViewWidth * (1 + scale) / 2, mViewHeight * (1 + scale) / 2, mWaterPaint);

        // 如果未开始
        if ((!isStarted) || (mViewWidth == 0) || (mViewHeight == 0)) {
            return;
        }

        // 起始振动X坐标，结束振动X坐标
        int startX = (int) (mViewWidth * (1 - scale) / 2);
        int endX = (int) (mViewWidth * (1 + scale) / 2);

        int startX2 = startX;
        // 底层波浪效果
        while (startX2 < endX) {
            int startY;
            startY = (int) (f1 - mAmplitude
                    * Math.sin(Math.PI
                    * (mWaveLength * (startX2 + this.c * width * this.f))
                    / width + Math.PI * 3 / 4));
            canvas.drawLine(startX2, startY, startX2, top, mWaterPaint);
            startX2++;
        }

        // 上层波浪效果
        while (startX < endX) {
            int startY;
            startY = (int) (f1 - mAmplitude
                    * Math.sin(Math.PI
                    * (mWaveLength * (startX + this.c * width * this.f))
                    / width));
            canvas.drawLine(startX, startY, startX, top, mWaterPaint);
            startX++;
        }

//		canvas.restore();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.progress = (int) c;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        c = ss.progress;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 关闭硬件加速，防止异常unsupported operation exception
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * @category 开始波动
     */
    public void startWave() {
        if (!isStarted) {
            this.c = 0L;
            isStarted = true;
            this.mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * @category 停止波动
     */
    public void stopWave() {
        if (isStarted) {
            this.c = 0L;
            isStarted = false;
            this.mHandler.removeMessages(0);
        }
    }

    /**
     * @category 保存状态
     */
    static class SavedState extends BaseSavedState {
        int progress;

        /**
         * Constructor called from {@link ProgressBar#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * 设置水面高度
     */
    private void setWaterHeight(float mWaterHeight) {
        this.mWaterHeight = mWaterHeight;
    }

    /**
     * 设置最终水面高度
     */
    public void setFinallyWaterHeight(float mFinallyWaterHeight) {
        this.mFinallyWaterHeight = mFinallyWaterHeight;
    }

}
