package com.chinamworld.llbt.userwidget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 用于实现贵金属，双向宝，基金等，布局上拉控件
 * Created by yuht on 2016/11/23.
 */
public class NewScrollView extends FrameLayout {

    /** 水平滑动最小移动距离 */
    private final int XMinMove = 30;

    /** 是否可以水平滑动，默认为不可以滑动 */
    private boolean isCanHoriontalScroll = false;

    /** 设置是否可以水平滑动 */
    public void setCanHoriontalScroll(boolean flag){
        isCanHoriontalScroll = flag;
    }

    private int mPreScrollHeight = 0;
    /** 第一个被移动的组件，预留的不可以滑动的高度。如需要预留基金的标题 */
    public void setPreScrollHeight(int preScrollHeight){
        mPreScrollHeight = preScrollHeight;
    }

    public NewScrollView(Context context) {
        super(context);
        initView(context);
    }

    public NewScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NewScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }



    private ViewGroup mRootLayout;
    private void initView(Context context){
//        mRootLayout = new LinearLayout(context);
//        mRootLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mRootLayout.setOrientation(LinearLayout.VERTICAL);
//        super.addView(mRootLayout);
//        this.setFillViewport(true);
    }

    private int getmHeight(){

        return mRootLayout.getChildAt(0).getMeasuredHeight();
    }
    /** 获得可向上移动的最大高度 */
    public int getMaxScrollHeight(){
        return getmHeight() - mPreScrollHeight;
    }
    @Override
    protected void onFinishInflate() {
//        mRootLayout = (ViewGroup)this.getChildAt(0);
//        mRootLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mHeight = mRootLayout.getChildAt(0).getHeight();
//            }
//        },20);
        mRootLayout = (ViewGroup)this.getChildAt(0);
        super.onFinishInflate();
    }

    /** 检测是否是左右滑动 */
    private boolean checkIsHoriontalScroll(int xMove,int yMove){
        if(isCanHoriontalScroll == false)
            return false;
        if(Math.abs(xMove) >  Math.abs(yMove)){
            // 可以认为此时为左右滑动
            return true;
        }
        return false;
    }

    double mLastX = 0,mLastY = 0,mTop = 0,mFirstY = 0;

    /** 表示是否消费掉操作事件，如果是屏幕上下滑动，则抬起时，事件直接被消费掉 */
    boolean isHandler = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                mFirstY = mLastY;
                mLastX = ev.getX();
                isHandler = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                double scrollY = mTop;
                int step = (int)(ev.getY() - mLastY);
                int stepX = (int)(ev.getX() - mLastX);
                if(checkIsHoriontalScroll(stepX,(int)(ev.getY() - mFirstY)))
                    return super.dispatchTouchEvent(ev);
                mLastY = ev.getY();
                if(step > 0){   // 向下拉
                    // 先判断是否需要滑动ListView
                    if(mINewScrollViewListener != null) {
                        if(false == mINewScrollViewListener.isToTop()){
                            if (true == super.dispatchTouchEvent(ev))
                                return true;
                        }
                    }
                }
                if(mTop - step < 0 && isHandler == true)
                    return true;
                if(scrollY - step  <= getMaxScrollHeight() && mTop - step >= 0 ){
                    mTop = mTop - step;
                    resetMargin();
                    if(Math.abs(step)  > 2) {
                        isHandler = true;
                    }
                    if(mIScrollChangedListener != null){
                        mIScrollChangedListener.onScrollChanged(0,(int)mTop);
                    }

                    return true;
                }
                if(scrollY - step > getMaxScrollHeight()){
                    mTop = getMaxScrollHeight();
                    resetMargin();
                    if(mIScrollChangedListener != null){
                        mIScrollChangedListener.onScrollChanged(0,(int)mTop);
                    }
//                    if(isHandler == true){
//                        return true;
//                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                mLastY = 0;
                mLastX = 0;
                if(isHandler){
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void resetMargin(){
        MarginLayoutParams l = (MarginLayoutParams)mRootLayout.getChildAt(0).getLayoutParams();
        l.setMargins(0,- (int)mTop,0,0);
        mRootLayout.getChildAt(0).setLayoutParams(l);
    }


    INewScrollViewListener mINewScrollViewListener;
    public void setINewScrollViewListener(INewScrollViewListener listener){
        mINewScrollViewListener = listener;
    }

    private IScrollChangedListener mIScrollChangedListener;
    public void setIScrollChangedListener(IScrollChangedListener listener){
        mIScrollChangedListener = listener;
    }
    /** NewScrollView 控件是否可以向下滑动监听事件 */
    public interface INewScrollViewListener{
        /**
         * 下拉时，列表是否已经滑动到了最顶部。
         * 如果已经到了最顶部，则控件继续向下滑动
         *
         * @return
         */
        boolean isToTop();
    }

    public interface IScrollChangedListener{
        boolean onScrollChanged(int x,int y);
    }

    /**
     * 设置指定的边距
     * @param topValue
     */
    public void resetAssignMargin(int topValue){
        mTop = topValue ;
        resetMargin();
    }

}
