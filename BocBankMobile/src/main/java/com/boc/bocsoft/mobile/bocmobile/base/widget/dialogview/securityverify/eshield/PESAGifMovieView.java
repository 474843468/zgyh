package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PESAGifMovieView extends View {
    private static final int DEFAULT_MOVIEW_DURATION = 1000;

    private int mMovieResourceId;
    private Movie mMovie;

    private long mMovieStart;
    private int mCurrentAnimationTime = 0;

    /**
     * Position for drawing animation frames in the center of the view.
     */
    private float mLeft;
    private float mTop;

    /**
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale;

    /**
     * Scaled movie frames width and height.
     */
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;

    private volatile boolean mPaused = false;
    private boolean mVisible = true;

    public PESAGifMovieView(Context context) {
        this(context, null);
    }

    public PESAGifMovieView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CustomTheme_gifMoviewViewStyle);
    }

    public PESAGifMovieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setViewAttributes(context, attrs, defStyle);
    }

    private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {

        /**
         * Starting from HONEYCOMB have to turn off HW acceleration to draw
         * Movie on Canvas.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifMoviewView,
                defStyle, R.style.Widget_GifMoviewView);

        mMovieResourceId = array.getResourceId(R.styleable.GifMoviewView_gif, -1);
        mPaused = array.getBoolean(R.styleable.GifMoviewView_paused, false);

        array.recycle();

        if (mMovieResourceId != -1) {
            mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        }
    }

    public void setMovieResource(int movieResId) {
        this.mMovieResourceId = movieResId;
        mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        requestLayout();
    }

    /**
     * 是否显示gif
     *
     * @return
     */
    public boolean isCanGif() {
        return mMovie != null && mMovie.duration() > 0 ? true : false;
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        requestLayout();
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;

        /**
         * Calculate new movie start time, so that it resumes from the same frame.
         */
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;
        }

        invalidate();
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMovie != null) {
            int movieWidth = mMovie.width();
            int movieHeight = mMovie.height();

            /**
             * Calculate horizontal scaling
             */
            float scaleH = 1f;
            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
                //Log.d("dding", "----w:" + maximumWidth);
                if (movieWidth > maximumWidth) {
                    scaleH = (float) movieWidth / (float) maximumWidth;
                }
            }

            /**
             * calculate vertical scaling
             */
            float scaleW = 1f;
            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                //Log.d("dding", "----h:" + maximumHeight);
                if (movieHeight < maximumHeight) {
                    scaleW = (float) movieHeight / (float) maximumHeight;
                }
            }

            //Log.d("dding", scaleH + "," + scaleW + "&" + movieWidth + "," + movieHeight);
            /**
             * calculate overall scale
             */
            mScale = 1f / Math.max(scaleH, scaleW);

            //Log.d("dding", "---scale  " + mScale);
            mMeasuredMovieWidth = (int) (movieWidth * mScale);
            mMeasuredMovieHeight = (int) (movieHeight * mScale);

            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);

        } else {
            /**
             * No movie set, just set minimum available size.
             */
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        /**
         * Calculate left / top for drawing in center
         */
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

        mVisible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    /**
     * Invalidates view only if it is visible. <br>
     * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
     */
    private void invalidateView() {
        if (mVisible) {
            if (Build.VERSION.SDK_INT >= 16) {
                try {
                    Method method = View.class.getDeclaredMethod("postInvalidateOnAnimation");
                    method.setAccessible(true);
                    method.invoke(this);
                } catch (Exception e) {
                    invalidate();
                }
            } else {
                invalidate();
            }
        }
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int dur = mMovie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIEW_DURATION;
        }

        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas) {

        mMovie.setTime(mCurrentAnimationTime);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(getWidth() * 1.0f / mMovie.width(), getHeight() * 1.0f / mMovie.height());
        mMovie.draw(canvas, 0, 0);
        canvas.restore();
    }

    public void onScreenStateChanged(int screenState) {
        int screenOn = 0x01;
        try {
            Method method = View.class.getDeclaredMethod("onScreenStateChanged", int.class);
            method.setAccessible(true);

            method.invoke(this, screenState);

            Field field = View.class.getDeclaredField("SCREEN_STATE_ON");
            field.setAccessible(true);
            screenOn = (Integer) field.get(this);

        } catch (Exception e) {
            e.printStackTrace();
        }


        mVisible = screenState == screenOn;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }
}
