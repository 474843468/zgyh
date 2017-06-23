package com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Observable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.OverScroller;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.animation.UpDownAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.scroll.CardViewScroll;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.scroll.CardViewScrollImpl;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡片层叠View
 * Created by liuweidong on 2016/11/28.
 */
public class CardStackView extends ViewGroup implements CardViewScroll {
    private static final String TAG = "CardStackView";

    private static final int INVALID_POINTER = -1;// 无效的触控点
    public static final int INVALID_TYPE = -1;
    public static final int DEFAULT_SELECT_POSITION = -1;// 默认展开的position

    private CardStackViewDataObserver mObserver = new CardStackViewDataObserver();// 观察者
    private ItemExpandListener mItemExpandListener;// itemview展开监听
    private CardStackAdapter mStackAdapter;// 适配器
    private BaseAnimator mAnimatorAdapter;
    private CardViewScroll mCardViewScroll;// 上下动画需要的滚动
    /*自定义平滑移动效果*/
    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;// 速度追踪器
    private int mTouchSlop;// 手移动该距离认为滑动
    private int mMinVelocity;// 最小速率
    private int mMaxVelocity;// 最大速率
    /*自定义view属性值*/
    private int mDuration;// 动画显示时间
    private int mOverlapGaps;// itemview重叠间隔
    private int mOverlapGapsCollapse;

    private int mSelectPosition = DEFAULT_SELECT_POSITION;// 当前展开的itemview
    private int mShowHeight;// 通过父控件获取显示的高度
    private int mTotalHeight;// 所有itemview显示的高度
    private int mActivePointerId = INVALID_POINTER;// 触控点标识
    private int mNestedYOffset;
    private int mLastMotionY;// 按下事件y
    private int[] mScrollOffset = new int[2];
    private boolean mIsBeingDragged = false;// 是否正被滑动
    private boolean mScrollEnable = true;// 是否可以滚动

    private List<ViewHolder> mViewHolders;

    public CardStackView(Context context) {
        this(context, null);
    }

    public CardStackView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardStackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardStackView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CardStackView, defStyleAttr, defStyleRes);
        setDuration(array.getInt(R.styleable.CardStackView_stackDuration, BaseAnimator.ANIMATION_DURATION));// 动画显示时间
        setOverlapGaps(array.getDimensionPixelSize(R.styleable.CardStackView_stackOverlapGaps, DensityUtil.dipTopx(getContext(), 0)));
        setOverlapGapsCollapse(array.getDimensionPixelSize(R.styleable.CardStackView_stackOverlapGapsCollapse, DensityUtil.dipTopx(getContext(), 15)));// 展开后下边间隔
        array.recycle();

        mViewHolders = new ArrayList<>();
        mOverScroller = new OverScroller(getContext());
//        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);// 子控件不需要焦点时才获取焦点
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();// 获取能够进行手势滑动的距离
        mMinVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
        setAnimatorAdapter(new UpDownAnimator(this));// 初始化动画
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.i(TAG + "--------onMeasure--------");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View parentView = (View) getParent();
        mShowHeight = parentView.getMeasuredHeight() - parentView.getPaddingTop() - parentView.getPaddingBottom();
        measureChild(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算所有itemview宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void measureChild(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;// 宽
        mTotalHeight = 0;
        mTotalHeight += getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int totalLength = mTotalHeight;
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.mHeaderHeight == -1)
                lp.mHeaderHeight = child.getMeasuredHeight();
            int childHeight = lp.mHeaderHeight;
            mTotalHeight = Math.max(totalLength, totalLength + childHeight + lp.topMargin + lp.bottomMargin);
            mTotalHeight -= mOverlapGaps * 2;//
            final int measuredWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;// 计算宽度
            maxWidth = Math.max(maxWidth, measuredWidth);
        }
        mTotalHeight += mOverlapGaps * 2;
        int heightSize = mTotalHeight;
        heightSize = Math.max(heightSize, mShowHeight);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(heightSize, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.i(TAG + "--------onLayout--------");
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            childTop += lp.topMargin;
            if (i != 0) {
                childTop -= mOverlapGaps * 2;
            }
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childTop += lp.mHeaderHeight;
        }
    }

    /**
     * 设置适配器（必须调用）
     *
     * @param stackAdapter
     */
    public void setAdapter(CardStackAdapter stackAdapter) {
        LogUtils.i(TAG + "--------setAdapter--------");
        mStackAdapter = stackAdapter;
        mStackAdapter.registerObserver(mObserver);// 注册观察者
        refreshView();
    }

    /**
     * 刷新页面显示的View
     */
    private void refreshView() {
        removeAllViews();
        mViewHolders.clear();
        for (int i = 0; i < mStackAdapter.getItemCount(); i++) {
            ViewHolder holder = getViewHolder(i);// 获取
            holder.mPosition = i;
            holder.onItemExpand(i == mSelectPosition);// 展开项
            addView(holder.itemView);
            setClickAnimator(holder, i);
            mStackAdapter.bindViewHolder(holder, i);
        }
        requestLayout();
    }

    /**
     * 设置动画
     *
     * @param animatorAdapter
     */
    public void setAnimatorAdapter(BaseAnimator animatorAdapter) {
        clearScrollYAndTranslation();
        mAnimatorAdapter = animatorAdapter;
        mCardViewScroll = this;
    }

    /**
     * 重置滚动位置
     */
    public void clearScrollYAndTranslation() {
        if (mSelectPosition != DEFAULT_SELECT_POSITION) {
            post(new Runnable() {
                @Override
                public void run() {
                    doCardClickAnimation(mViewHolders.get(mSelectPosition), mSelectPosition);
                }
            });
        }
        if (mCardViewScroll != null)
            mCardViewScroll.setViewScrollY(0);
        requestLayout();
    }

    /**
     * 获取ViewHolder
     *
     * @param i
     * @return
     */
    ViewHolder getViewHolder(int i) {
        if (i == DEFAULT_SELECT_POSITION)
            return null;
        ViewHolder viewHolder;
        if (mViewHolders.size() <= i || mViewHolders.get(i).mItemViewType != mStackAdapter.getItemViewType(i)) {
            viewHolder = mStackAdapter.createViewHolder(this, mStackAdapter.getItemViewType(i));
            mViewHolders.add(viewHolder);
        } else {
            viewHolder = mViewHolders.get(i);
        }
        return viewHolder;
    }

    /**
     * 设置点击动画
     *
     * @param holder
     * @param position
     */
    private void setClickAnimator(final ViewHolder holder, final int position) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPosition == DEFAULT_SELECT_POSITION)
                    return;
                performItemClick(mViewHolders.get(mSelectPosition));
            }
        });
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performItemClick(holder);
            }
        });
    }

    /**
     * 点击itemview
     *
     * @param viewHolder
     */
    public void performItemClick(ViewHolder viewHolder) {
        doCardClickAnimation(viewHolder, viewHolder.mPosition);
    }

    /**
     * 卡片点击动画
     *
     * @param viewHolder
     * @param position
     */
    private void doCardClickAnimation(final ViewHolder viewHolder, int position) {
        View parentView = (View) getParent();
        mShowHeight = parentView.getMeasuredHeight() - parentView.getPaddingTop() - parentView.getPaddingBottom();
        mAnimatorAdapter.itemClick(viewHolder, position);
    }

    /**
     * 重置速度追踪器
     */
    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    /**
     * 预处理事件，改变事件的传递方向（截断）
     *
     * @param event
     * @return true：事件在当前的ViewGroup被处理，调用onTouchEvent；false：事件向子控件传递；
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        LogUtils.i(TAG + "--------onInterceptTouchEvent--------");
        if ((event.getAction() == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        if (getViewScrollY() == 0 && !canScrollVertically(1)) {// 已经在顶端不能再下滑
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {// 处理多点触摸
            case MotionEvent.ACTION_DOWN: {
                LogUtils.i(TAG + "--------onInterceptTouchEvent：ACTION_DOWN--------");
                final int y = (int) event.getY();// Remember where we started (for dragging)
                mLastMotionY = y;
                mActivePointerId = event.getPointerId(0);// Save the ID of this pointer (for dragging)
                initOrResetVelocityTracker();// 重置速度追踪器
                mVelocityTracker.addMovement(event);
                mIsBeingDragged = !mOverScroller.isFinished();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                LogUtils.i(TAG + "--------onInterceptTouchEvent：ACTION_MOVE--------");
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = event.findPointerIndex(mActivePointerId);// Find the index of the active pointer and fetch its position
                if (pointerIndex == -1) {
                    break;
                }
                final int y = (int) event.getY(pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);
                if (yDiff > mTouchSlop) {// 滑动大于限定值时进行事件处理
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();// Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    }
                    mVelocityTracker.addMovement(event);// 将事件加入到VelocityTracker实例中
                    mNestedYOffset = 0;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);// 阻止父View截获touch事件
                    }
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                LogUtils.i(TAG + "--------onInterceptTouchEvent：ACTION_UP--------");
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                recycleVelocityTracker();
                if (mOverScroller.springBack(getViewScrollX(), getViewScrollY(), 0, 0, 0, getScrollRange())) {
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                LogUtils.i(TAG + "--------onInterceptTouchEvent：ACTION_POINTER_UP--------");
                onSecondaryPointerUp(event);
                break;
        }
        if (!mScrollEnable) {
            mIsBeingDragged = false;
        }
        return mIsBeingDragged;
    }

    /**
     * @param event
     * @return 决定当前控件是否消费了这个事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.i(TAG + "--------onTouchEvent--------");
        if (!mIsBeingDragged) {
            super.onTouchEvent(event);
        }
        if (!mScrollEnable) {
            return true;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        MotionEvent motionEvent = MotionEvent.obtain(event);
        final int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {// 按下
            mNestedYOffset = 0;
        }
        motionEvent.offsetLocation(0, mNestedYOffset);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                LogUtils.i(TAG + "--------onTouchEvent：ACTION_DOWN--------");
                if (getChildCount() == 0) {
                    return false;
                }
                if ((mIsBeingDragged = !mOverScroller.isFinished())) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                mLastMotionY = (int) event.getY();
                mActivePointerId = event.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                LogUtils.i(TAG + "--------onTouchEvent：ACTION_DOWN--------");
                final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

                final int y = (int) event.getY(activePointerIndex);
                int deltaY = mLastMotionY - y;
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    mLastMotionY = y - mScrollOffset[1];
                    final int range = getScrollRange();
                    if (mCardViewScroll instanceof CardViewScrollImpl) {
                        mCardViewScroll.scrollViewTo(0, deltaY + mCardViewScroll.getViewScrollY());
                    } else {
                        if (overScrollBy(0, deltaY, 0, getViewScrollY(), 0, range, 0, 0, true)) {
                            mVelocityTracker.clear();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i(TAG + "--------onTouchEvent：ACTION_UP--------");
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaxVelocity);// 初始化速率单位
                    int yVelocity = (int) velocityTracker.getYVelocity(mActivePointerId);// 竖向速率
                    if (getChildCount() > 0) {
                        if ((Math.abs(yVelocity) > mMinVelocity)) {
                            fling(-yVelocity);
                        } else {
                            if (mOverScroller.springBack(getViewScrollX(), mCardViewScroll.getViewScrollY(), 0, 0, 0, getScrollRange())) {
                                postInvalidate();// computeScroll调用
                            }
                        }
                        mActivePointerId = INVALID_POINTER;
                    }
                }
                endDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.i(TAG + "--------onTouchEvent：ACTION_DOWN--------");
                if (mIsBeingDragged && getChildCount() > 0) {
                    if (mOverScroller.springBack(getViewScrollX(), mCardViewScroll.getViewScrollY(), 0, 0, 0, getScrollRange())) {
                        postInvalidate();
                    }
                    mActivePointerId = INVALID_POINTER;
                }
                endDrag();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                LogUtils.i(TAG + "--------onTouchEvent：ACTION_DOWN--------");
                final int index = event.getActionIndex();
                mLastMotionY = (int) event.getY(index);
                mActivePointerId = event.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                LogUtils.i(TAG + "--------onTouchEvent：ACTION_DOWN--------");
                onSecondaryPointerUp(event);
                mLastMotionY = (int) event.getY(event.findPointerIndex(mActivePointerId));
                break;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(motionEvent);
        }
        motionEvent.recycle();
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            scrollRange = Math.max(0, mTotalHeight - mShowHeight);
        }
        return scrollRange;
    }

    @Override
    protected int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = mShowHeight;
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = mTotalHeight;
        final int scrollY = mCardViewScroll.getViewScrollY();
        final int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }

        return scrollRange;
    }

    /**
     * View类方法
     *
     * @param scrollX  距离原点x轴距离
     * @param scrollY  距离原点y轴距离
     * @param clampedX 滑到左侧边界true
     * @param clampedY 滑到下侧边界true
     */
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        LogUtils.i(TAG + "--------onOvrScrolled--------" + mOverScroller.isFinished());
        if (!mOverScroller.isFinished()) {
            final int oldX = mCardViewScroll.getViewScrollX();
            final int oldY = mCardViewScroll.getViewScrollY();
            mCardViewScroll.setViewScrollX(scrollX);
            mCardViewScroll.setViewScrollY(scrollY);
            onScrollChanged(mCardViewScroll.getViewScrollX(), mCardViewScroll.getViewScrollY(), oldX, oldY);
            if (clampedY) {// 滑动到下边界
                mOverScroller.springBack(mCardViewScroll.getViewScrollX(), mCardViewScroll.getViewScrollY(), 0, 0, 0, getScrollRange());
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {// Call this when you want to know the new location. If it returns true, the animation is not yet finished.
            mCardViewScroll.scrollViewTo(0, mOverScroller.getCurrY());// 设置当前的滚动位置
            postInvalidate();
        }
    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = mShowHeight;
            int bottom = mTotalHeight;
            mOverScroller.fling(mCardViewScroll.getViewScrollX(), mCardViewScroll.getViewScrollY(), 0, velocityY, 0, 0, 0,
                    Math.max(0, bottom - height), 0, 0);
            postInvalidate();
        }
    }

    /**
     * Set the scrolled position of your view.
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), getWidth());
            y = clamp(y, mShowHeight, mTotalHeight);
            if (x != mCardViewScroll.getViewScrollX() || y != mCardViewScroll.getViewScrollY()) {
                super.scrollTo(x, y);
            }
        }
    }

    @Override
    public void scrollViewTo(int x, int y) {
        LogUtils.i(TAG + "--------scrollViewTo--------");
        scrollTo(x, y);
    }

    @Override
    public void setViewScrollX(int x) {
        setScrollX(x);
    }

    @Override
    public int getViewScrollX() {
        return getScrollX();
    }

    @Override
    public void setViewScrollY(int y) {
        setScrollY(y);
    }

    @Override
    public int getViewScrollY() {
        return getScrollY();
    }

    /**
     * 事件结束
     */
    private void endDrag() {
        mIsBeingDragged = false;
        recycleVelocityTracker();
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if ((my + n) > child) {
            return child - my;
        }
        return n;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * 获取展开的itemview position
     *
     * @return
     */
    public int getSelectPosition() {
        return mSelectPosition;
    }

    /**
     * 设置展开的itemview position
     *
     * @param selectPosition
     */
    public void setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;
        mItemExpandListener.onItemExpand(mSelectPosition != DEFAULT_SELECT_POSITION);
    }

    public void setScrollEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }

    /**
     * 获取显示高度
     *
     * @return
     */
    public int getShowHeight() {
        return mShowHeight;
    }

    /**
     * 获取所有itemview显示的高度
     *
     * @return
     */
    public int getTotalHeight() {
        return mTotalHeight;
    }

    /**
     * 设置动画显示时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * 获取动画持续时间
     *
     * @return
     */
    public int getDuration() {
        if (mAnimatorAdapter != null)
            return mDuration;
        return 0;
    }

    public int getOverlapGaps() {
        return mOverlapGaps;
    }

    public void setOverlapGaps(int overlapGaps) {
        mOverlapGaps = overlapGaps;
    }

    public int getOverlapGapsCollapse() {
        return mOverlapGapsCollapse;
    }

    public void setOverlapGapsCollapse(int overlapGapsCollapse) {
        mOverlapGapsCollapse = overlapGapsCollapse;
    }

    /**
     * itemview展开监听
     *
     * @param itemExpandListener
     */
    public void setItemExpandListener(ItemExpandListener itemExpandListener) {
        mItemExpandListener = itemExpandListener;
    }

    public interface ItemExpandListener {
        void onItemExpand(boolean expend);
    }

    /**
     * 定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖它的对象都将得到通知，并自动更新。
     * 观察者模式
     */
    public static class AdapterDataObservable extends Observable<AdapterDataObserver> {

        /**
         * 数据改变
         */
        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }

        /**
         * 某一条数据改变
         *
         * @param positionStart
         * @param itemCount
         */
        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeChanged(positionStart, itemCount);
            }
        }
    }

    /**
     * 观察者类
     */
    public static abstract class AdapterDataObserver {
        public void onChanged() {

        }

        public void onItemRangeChanged(int positionStart, int itemCount) {

        }
    }

    /**
     * 观察者类
     */
    private class CardStackViewDataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            refreshView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            ViewHolder holder = getViewHolder(positionStart);
            mStackAdapter.bindViewHolder(holder, positionStart);
            requestLayout();
        }
    }

    /**
     * Base class for an Adapter
     *
     * @param <VH>
     */
    public static abstract class Adapter<VH extends ViewHolder> {
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        /**
         * Called when CardStackView needs a new {@link ViewHolder} of the given type to represent
         * an item.
         *
         * @param parent
         * @param viewType
         * @return
         */
        public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

        /**
         * Called by CardStackView to display the data at the specified position. This method
         * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
         * the given position.
         *
         * @param holder
         * @param position
         */
        public abstract void onBindViewHolder(VH holder, int position);

        /**
         * This method calls {@link #onCreateViewHolder(ViewGroup, int)} to create a new
         * {@link ViewHolder} and initializes some private fields to be used by CardStackView.
         *
         * @param parent
         * @param viewType
         * @return
         */
        public final VH createViewHolder(ViewGroup parent, int viewType) {
            final VH holder = onCreateViewHolder(parent, viewType);
            holder.mItemViewType = viewType;
            return holder;
        }

        public abstract int getItemCount();

        public void bindViewHolder(VH holder, int position) {
            onBindViewHolder(holder, position);
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int position) {
            mObservable.notifyItemRangeChanged(position, 1);
        }

        public void registerObserver(AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the CardStackView.
     */
    public static abstract class ViewHolder {
        public final View itemView;
        int mPosition;
        int mItemViewType = INVALID_TYPE;

        /**
         * itemview展开
         *
         * @param b
         */
        public abstract void onItemExpand(boolean b);

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        public Context getContext() {
            return itemView.getContext();
        }

        /**
         * 动画状态改变
         *
         * @param state
         * @param willBeSelect
         */
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int mHeaderHeight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            LogUtils.i(TAG + "--------LayoutParams--------");
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.CardStackView);
            mHeaderHeight = array.getDimensionPixelSize(R.styleable.CardStackView_stackHeaderHeight, -1);
            array.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

}
