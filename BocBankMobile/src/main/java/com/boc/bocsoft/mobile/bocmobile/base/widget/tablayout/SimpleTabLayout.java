package com.boc.bocsoft.mobile.bocmobile.base.widget.tablayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.util.Pools;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

/**
 * Created by yangle on 2016/11/7.
 * 改自于support design library 中tabLayout ,example 如下：
 * SimpleTabLayout tabLayout = findViewById...
 * tabLayout.addTab(tabLayout.newTab().setText(tab1);
 * ...
 * 自定义属性见view_attrs中SimpleTabLayout
 * tabLayout.setupWithViewPager(viewPager)  可与 ViewPager 关联
 */

public class SimpleTabLayout extends HorizontalScrollView {

    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72; // dps
    static final int DEFAULT_GAP_TEXT_ICON = 8; // dps
    private static final int INVALID_WIDTH = -1;
    private static final int DEFAULT_HEIGHT = 48; // dps
    private static final int TAB_MIN_WIDTH_MARGIN = 56; //dps
    static final int FIXED_WRAP_GUTTER_MIN = 16; //dps
    static final int MOTION_NON_ADJACENT_OFFSET = 24;
    private static final int ANIMATION_DURATION = 300;
    public static final int MODE_SCROLLABLE = 0;
    public static final int MODE_FIXED = 1;
    public static final int GRAVITY_FILL = 0;
    public static final int GRAVITY_CENTER = 1;

    private static final int DEFAULT_MAX_LINES =2;
    private static final int DEFAULT_ICON_VIEW_SIZE = 24;
    private static final int DEFAULT_TAB_SCROLLABLE_MIN_WIDTH = 72;
    private static final int DEFAULT_TAB_TEXT_SIZE_2LINE = 12;

    private static final String INDICATOR_BACKGROUND_COLOR = "#e8e9ed";
    public interface OnTabSelectedListener {

        void onTabSelected(Tab tab);

        void onTabUnselected(Tab tab);

        void onTabReselected(Tab tab);
    }

    private final ArrayList<Tab> mTabs = new ArrayList<>();

    private Tab mSelectedTab;
    private final SlidingTabStrip mTabStrip;

    int mTabPaddingStart;
    int mTabPaddingTop;
    int mTabPaddingEnd;
    int mTabPaddingBottom;

//    int mTabTextAppearance;
    ColorStateList mTabTextColors;
    float mTabTextSize;
    float mTabTextMultiLineSize;

    final int mTabBackgroundResId;

    int mTabMaxWidth = Integer.MAX_VALUE >>2;
    private final int mRequestedTabMinWidth;
    private final int mRequestedTabMaxWidth;
    private final int mScrollableTabMinWidth;

    private int mContentInsetStart;

    int mTabGravity;
    int mMode;

    private OnTabSelectedListener mSelectedListener;
    private final ArrayList<OnTabSelectedListener> mSelectedListeners = new ArrayList<>();
    private OnTabSelectedListener mCurrentVpSelectedListener;

//    private ValueAnimatorCompat mScrollAnimator;
    private ValueAnimator mScrollAnimator;
    ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    private TabLayoutOnPageChangeListener mPageChangeListener;
//    private AdapterChangeListener mAdapterChangeListener;

    private boolean mSetupViewPagerImplicitly;

    private static final Pools.Pool<Tab> sTabPool = new Pools.SynchronizedPool<>(16);
    // Pool we use as a simple RecyclerBin
    private final Pools.Pool<TabView> mTabViewPool = new Pools.SimplePool<>(12);

    // 是否与文字宽度一致
    private boolean mIsSameWidthForIndicatorAndText = true;
    private boolean mHasIndicatorBackground = true;
    private Context mContext;
    public SimpleTabLayout(Context context) {
        this(context,null);
    }

    public SimpleTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public SimpleTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Add the TabStrip
        mTabStrip = new SlidingTabStrip(context);
        super.addView(mTabStrip, 0, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        // 获取属性值,没有则以默认样式中的属性值为准
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleTabLayout,defStyleAttr,R.style.SimpleTabLayout);

        mTabStrip.setSelectedIndicatorHeight(
                a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabIndicatorHeight, 0));
        mTabStrip.setSelectedIndicatorColor(a.getColor(R.styleable.SimpleTabLayout_tabIndicatorColor,0));

        mTabPaddingStart = mTabPaddingTop = mTabPaddingEnd = mTabPaddingBottom = a
                .getDimensionPixelSize(R.styleable.SimpleTabLayout_tabPadding, 0);
        mTabPaddingStart = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabPaddingStart,
                mTabPaddingStart);
        mTabPaddingTop = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabPaddingTop,
                mTabPaddingTop);
        mTabPaddingEnd = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabPaddingEnd,
                mTabPaddingEnd);
        mTabPaddingBottom = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabPaddingBottom,
                mTabPaddingBottom);
        // 获取设定的tabTextSize
        if (a.hasValue(R.styleable.SimpleTabLayout_tabTextSize)) {
            mTabTextSize = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabTextSize, 0);
        }
        // 如果默认属性中有tabTextColor
        if (a.hasValue(R.styleable.SimpleTabLayout_tabTextColor)) {
            // If we have an explicit text color set, use it instead
            mTabTextColors = a.getColorStateList(R.styleable.SimpleTabLayout_tabTextColor);
        }

        if (a.hasValue(R.styleable.SimpleTabLayout_tabSelectedTextColor)) {
            // We have an explicit selected text color set, so we need to make merge it with the
            // current colors. This is exposed so that developers can use theme attributes to set
            // this (theme attrs in ColorStateLists are Lollipop+)
            final int selected = a.getColor(R.styleable.SimpleTabLayout_tabSelectedTextColor, 0);
            mTabTextColors = createColorStateList(mTabTextColors.getDefaultColor(), selected);
        }
        mRequestedTabMinWidth = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabMinWidth,
                INVALID_WIDTH);
        mRequestedTabMaxWidth = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabMaxWidth,
                INVALID_WIDTH);
        mTabBackgroundResId = a.getResourceId(R.styleable.SimpleTabLayout_tabBackground, 0);
        mContentInsetStart = a.getDimensionPixelSize(R.styleable.SimpleTabLayout_tabContentStart, 0);
        mMode = a.getInt(R.styleable.SimpleTabLayout_tabMode, MODE_FIXED);
        mTabGravity = a.getInt(R.styleable.SimpleTabLayout_tabGravity, GRAVITY_FILL);
        a.recycle();

        // TODO add attr for these
        mTabTextMultiLineSize = spToPx(DEFAULT_TAB_TEXT_SIZE_2LINE);
        mScrollableTabMinWidth = dpToPx(DEFAULT_TAB_SCROLLABLE_MIN_WIDTH);

        // Now apply the tab mode and gravity
        applyModeAndGravity();
    }

    public boolean isHasIndicatorBackground() {
        return mHasIndicatorBackground;
    }

    public void setHasIndicatorBackground(boolean mHasIndicatorBackground) {
        this.mHasIndicatorBackground = mHasIndicatorBackground;
    }

    public boolean isSameWidthForIndicatorAndText() {
        return mIsSameWidthForIndicatorAndText;
    }

    public void setSameWidthForIndicatorAndText(boolean sameWidthForIndicatorAndText) {
        mIsSameWidthForIndicatorAndText = sameWidthForIndicatorAndText;
    }

    public float getTabTextSize() {
        return mTabTextSize;
    }

    public void setTabTextSize(float tabTextSize) {
        mTabTextSize = tabTextSize;
    }

    public void setSelectedTabIndicatorColor(@ColorInt int color) {
        mTabStrip.setSelectedIndicatorColor(color);
    }

    public void setSelectedTabIndicatorHeight(int height) {
        mTabStrip.setSelectedIndicatorHeight(height);
    }

    public void setScrollPosition(int position, float positionOffset, boolean updateSelectedText) {
        setScrollPosition(position, positionOffset, updateSelectedText, true);
    }

    void setScrollPosition(int position, float positionOffset, boolean updateSelectedText,
                           boolean updateIndicatorPosition) {
        final int roundedPosition = Math.round(position + positionOffset);
        if (roundedPosition < 0 || roundedPosition >= mTabStrip.getChildCount()) {
            return;
        }

        // Set the indicator position, if enabled
        if (updateIndicatorPosition) {
            mTabStrip.setIndicatorPositionFromTabPosition(position, positionOffset);
        }

        // Now update the scroll position, canceling any running animation
        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }
        scrollTo(calculateScrollXForTab(position, positionOffset), 0);

        // Update the 'selected state' view as we scroll, if enabled
        if (updateSelectedText) {
            setSelectedTabView(roundedPosition);
        }
    }

    private float getScrollPosition() {
        return mTabStrip.getIndicatorPosition();
    }

    public void addTab(@NonNull Tab tab) {
        addTab(tab, mTabs.isEmpty());
    }

    public void addTab(@NonNull Tab tab, int position) {
        addTab(tab, position, mTabs.isEmpty());
    }

    public void addTab(@NonNull Tab tab, boolean setSelected) {
        addTab(tab, mTabs.size(), setSelected);
    }

    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        configureTab(tab, position);
        addTabView(tab);

        if (setSelected) {
            tab.select();
        }
    }

    private void addTabFromItemView(@NonNull TabItem item) {
        final Tab tab = newTab();
        if (item.mText != null) {
            tab.setText(item.mText);
        }
        if (item.mIcon != null) {
            tab.setIcon(item.mIcon);
        }
        addTab(tab);
    }

    @Deprecated
    public void setOnTabSelectedListener(@Nullable OnTabSelectedListener listener) {
        // The logic in this method emulates what we had before support for multiple
        // registered listeners.
        if (mSelectedListener != null) {
            removeOnTabSelectedListener(mSelectedListener);
        }
        // Update the deprecated field so that we can remove the passed listener the next
        // time we're called
        mSelectedListener = listener;
        if (listener != null) {
            addOnTabSelectedListener(listener);
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        if (!mSelectedListeners.contains(listener)) {
            mSelectedListeners.add(listener);
        }
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener listener) {
        mSelectedListeners.remove(listener);
    }

    public void clearOnTabSelectedListeners() {
        mSelectedListeners.clear();
    }

    @NonNull
    public Tab newTab() {
        Tab tab = sTabPool.acquire();
        if (tab == null) {
            tab = new Tab();
        }
        tab.mParent = this;
        tab.mView = createTabView(tab);
        return tab;
    }

    public int getTabCount() {
        return mTabs.size();
    }

    public Tab getTabAt(int index) {
        return (index < 0 || index >= getTabCount()) ? null : mTabs.get(index);
    }

    public int getSelectedTabPosition() {
        return mSelectedTab != null ? mSelectedTab.getPosition() : -1;
    }

    public void removeTab(Tab tab) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
        }

        removeTabAt(tab.getPosition());
    }

    public void removeTabAt(int position) {
        final int selectedTabPosition = mSelectedTab != null ? mSelectedTab.getPosition() : 0;
        removeTabViewAt(position);

        final Tab removedTab = mTabs.remove(position);
        if (removedTab != null) {
            removedTab.reset();
            sTabPool.release(removedTab);
        }

        final int newTabCount = mTabs.size();
        for (int i = position; i < newTabCount; i++) {
            mTabs.get(i).setPosition(i);
        }

        if (selectedTabPosition == position) {
            selectTab(mTabs.isEmpty() ? null : mTabs.get(Math.max(0, position - 1)));
        }
    }

    public void removeAllTabs() {
        // Remove all the views
        for (int i = mTabStrip.getChildCount() - 1; i >= 0; i--) {
            removeTabViewAt(i);
        }

        for (final Iterator<Tab> i = mTabs.iterator(); i.hasNext();) {
            final Tab tab = i.next();
            i.remove();
            tab.reset();
            sTabPool.release(tab);
        }

        mSelectedTab = null;
    }

    public void setTabMode( int mode) {
        if (mode != mMode) {
            mMode = mode;
            applyModeAndGravity();
        }
    }

    public int getTabMode() {
        return mMode;
    }

    public void setTabGravity( int gravity) {
        if (mTabGravity != gravity) {
            mTabGravity = gravity;
            applyModeAndGravity();
        }
    }

    public int getTabGravity() {
        return mTabGravity;
    }

    public void setTabTextColors(@Nullable ColorStateList textColor) {
        if (mTabTextColors != textColor) {
            mTabTextColors = textColor;
            updateAllTabs();
        }
    }

    public ColorStateList getTabTextColors() {
        return mTabTextColors;
    }

    public void setTabTextColors(int normalColor, int selectedColor) {
        setTabTextColors(createColorStateList(normalColor, selectedColor));
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(@Nullable final ViewPager viewPager, boolean autoRefresh) {
        setupWithViewPager(viewPager, autoRefresh, false);
    }

    private void setupWithViewPager(@Nullable final ViewPager viewPager, boolean autoRefresh,
                                    boolean implicitSetup) {
        if (mViewPager != null) {
            // If we've already been setup with a ViewPager, remove us from it
            if (mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            }
//            if (mAdapterChangeListener != null) {
//                mViewPager.removeOnAdapterChangeListener(mAdapterChangeListener);
//            }
        }

        if (mCurrentVpSelectedListener != null) {
            // If we already have a tab selected listener for the ViewPager, remove it
            removeOnTabSelectedListener(mCurrentVpSelectedListener);
            mCurrentVpSelectedListener = null;
        }

        if (viewPager != null) {
            mViewPager = viewPager;

            // Add our custom OnPageChangeListener to the ViewPager
            if (mPageChangeListener == null) {
                mPageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            mPageChangeListener.reset();
            viewPager.addOnPageChangeListener(mPageChangeListener);

            // Now we'll add a tab selected listener to set ViewPager's current item
            mCurrentVpSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            addOnTabSelectedListener(mCurrentVpSelectedListener);

            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                // Now we'll populate ourselves from the pager adapter, adding an observer if
                // autoRefresh is enabled
                setPagerAdapter(adapter, autoRefresh);
            }

//            // Add a listener so that we're notified of any adapter changes
//            if (mAdapterChangeListener == null) {
//                mAdapterChangeListener = new AdapterChangeListener();
//            }
//            mAdapterChangeListener.setAutoRefresh(autoRefresh);
//            viewPager.addOnAdapterChangeListener(mAdapterChangeListener);
            // Now update the scroll position to match the ViewPager's current item
            setScrollPosition(viewPager.getCurrentItem(), 0f, true);
        } else {
            // We've been given a null ViewPager so we
            // need to clear out the internal state,
            // listeners and observers
            mViewPager = null;
            setPagerAdapter(null, false);
        }

        mSetupViewPagerImplicitly = implicitSetup;
    }

    @Deprecated
    public void setTabsFromPagerAdapter(@Nullable final PagerAdapter adapter) {
        setPagerAdapter(adapter, false);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        // Only delay the pressed state if the tabs can scroll
        return getTabScrollRange() > 0;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager == null) {
            // If we don't have a ViewPager already, check if our parent is a ViewPager to
            // setup with it automatically
            final ViewParent vp = getParent();
            if (vp instanceof ViewPager) {
                // If we have a ViewPager parent and we've been added as part of its decor, let's
                // assume that we should automatically setup to display any titles
                setupWithViewPager((ViewPager) vp, true, true);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mSetupViewPagerImplicitly) {
            // If we've been setup with a ViewPager implicitly, let's clear out any listeners, etc
            setupWithViewPager(null);
            mSetupViewPagerImplicitly = false;
        }
    }

    private int getTabScrollRange() {
        return Math.max(0, mTabStrip.getWidth() - getWidth() - getPaddingLeft()
                - getPaddingRight());
    }

    void setPagerAdapter(@Nullable final PagerAdapter adapter, final boolean addObserver) {
        if (mPagerAdapter != null && mPagerAdapterObserver != null) {
            // If we already have a PagerAdapter, unregister our observer
            mPagerAdapter.unregisterDataSetObserver(mPagerAdapterObserver);
        }

        mPagerAdapter = adapter;

        if (addObserver && adapter != null) {
            // Register our observer on the new adapter
            if (mPagerAdapterObserver == null) {
                mPagerAdapterObserver = new PagerAdapterObserver();
            }
            adapter.registerDataSetObserver(mPagerAdapterObserver);
        }

        // Finally make sure we reflect the new adapter
        populateFromPagerAdapter();
    }

    void populateFromPagerAdapter() {
        removeAllTabs();

        if (mPagerAdapter != null) {
            final int adapterCount = mPagerAdapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                addTab(newTab().setText(mPagerAdapter.getPageTitle(i)), false);
            }

            // Make sure we reflect the currently set ViewPager item
            if (mViewPager != null && adapterCount > 0) {
                final int curItem = mViewPager.getCurrentItem();
                if (curItem != getSelectedTabPosition() && curItem < getTabCount()) {
                    selectTab(getTabAt(curItem));
                }
            }
        }
    }

    private void updateAllTabs() {
        for (int i = 0, z = mTabs.size(); i < z; i++) {
            mTabs.get(i).updateView();
        }
    }

    private TabView createTabView(@NonNull final Tab tab) {
        TabView tabView = mTabViewPool != null ? mTabViewPool.acquire() : null;
        if (tabView == null) {
            tabView = new TabView(getContext());
        }
        tabView.setTab(tab);
        tabView.setFocusable(true);
        tabView.setMinimumWidth(getTabMinWidth());
        return tabView;
    }

    private void configureTab(Tab tab, int position) {
        tab.setPosition(position);
        mTabs.add(position, tab);

        final int count = mTabs.size();
        for (int i = position + 1; i < count; i++) {
            mTabs.get(i).setPosition(i);
        }
    }

    private void addTabView(Tab tab) {
        final TabView tabView = tab.mView;
        mTabStrip.addView(tabView, tab.getPosition(), createLayoutParamsForTabs());
    }

    @Override
    public void addView(View child) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    private void addViewInternal(final View child) {
        if (child instanceof TabItem) {
            addTabFromItemView((TabItem) child);
        } else {
            throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
        }
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        updateTabViewLayoutParams(lp);
        return lp;
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        if (mMode == MODE_FIXED && mTabGravity == GRAVITY_FILL) {
            lp.width = 0;
            lp.weight = 1;
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0;
        }
    }

    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    int spToPx(int sp) {
        return Math.round(getResources().getDisplayMetrics().scaledDensity * sp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If we have a MeasureSpec which allows us to decide our height, try and use the default
        // height
        final int idealHeight = dpToPx(getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)),
                        MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, MeasureSpec.EXACTLY);
                break;
        }

        final int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            // If we don't have an unspecified width spec, use the given size to calculate
            // the max tab width
            mTabMaxWidth = mRequestedTabMaxWidth > 0
                    ? mRequestedTabMaxWidth
                    : specWidth - dpToPx(TAB_MIN_WIDTH_MARGIN);
        }

        // Now super measure itself using the (possibly) modified height spec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 1) {
            // If we're in fixed mode then we need to make the tab strip is the same width as us
            // so we don't scroll
            final View child = getChildAt(0);
            boolean remeasure = false;

            switch (mMode) {
                case MODE_SCROLLABLE:
                    // We only need to resize the child if it's smaller than us. This is similar
                    // to fillViewport
                    remeasure = child.getMeasuredWidth() < getMeasuredWidth();
                    break;
                case MODE_FIXED:
                    // Resize the child so that it doesn't scroll
                    remeasure = child.getMeasuredWidth() != getMeasuredWidth();
                    break;
            }

            if (remeasure) {
                // Re-measure the child with a widthSpec set to be exactly our measure width
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop()
                        + getPaddingBottom(), child.getLayoutParams().height);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                        getMeasuredWidth(), MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    private void removeTabViewAt(int position) {
        final TabView view = (TabView) mTabStrip.getChildAt(position);
        mTabStrip.removeViewAt(position);
        if (view != null) {
            view.reset();
            mTabViewPool.release(view);
        }
        requestLayout();
    }

    private void animateToTab(int newPosition) {
        if (newPosition == Tab.INVALID_POSITION) {
            return;
        }

        if (getWindowToken() == null || !ViewCompat.isLaidOut(this)
                || mTabStrip.childrenNeedLayout()) {
            // If we don't have a window token, or we haven't been laid out yet just draw the new
            // position now
            setScrollPosition(newPosition, 0f, true);
            return;
        }

        final int startScrollX = getScrollX();
        final int targetScrollX = calculateScrollXForTab(newPosition, 0);
        // TODO: 2016/11/8 动画
        if (startScrollX != targetScrollX) {
            if (mScrollAnimator == null) {
                mScrollAnimator = new ValueAnimator();
                mScrollAnimator.setInterpolator(AnimUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                mScrollAnimator.setDuration(ANIMATION_DURATION);
                mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        scrollTo((Integer) animator.getAnimatedValue(), 0);
                    }
                });
            }

            mScrollAnimator.setIntValues(startScrollX, targetScrollX);
            mScrollAnimator.start();
        }

        // Now animate the indicator
        mTabStrip.animateIndicatorToPosition(newPosition, ANIMATION_DURATION);
    }

    private void setSelectedTabView(int position) {
        final int tabCount = mTabStrip.getChildCount();
        if (position < tabCount) {
            for (int i = 0; i < tabCount; i++) {
                final View child = mTabStrip.getChildAt(i);
                child.setSelected(i == position);
            }
        }
    }

    void selectTab(Tab tab) {
        selectTab(tab, true);
    }

    void selectTab(final Tab tab, boolean updateIndicator) {
        final Tab currentTab = mSelectedTab;

        if (currentTab == tab) {
            if (currentTab != null) {
                dispatchTabReselected(tab);
                animateToTab(tab.getPosition());
            }
        } else {
            final int newPosition = tab != null ? tab.getPosition() : Tab.INVALID_POSITION;
            if (updateIndicator) {
                if ((currentTab == null || currentTab.getPosition() == Tab.INVALID_POSITION)
                        && newPosition != Tab.INVALID_POSITION) {
                    // If we don't currently have a tab, just draw the indicator
                    setScrollPosition(newPosition, 0f, true);
                } else {
                    animateToTab(newPosition);
                }
                if (newPosition != Tab.INVALID_POSITION) {
                    setSelectedTabView(newPosition);
                }
            }
            if (currentTab != null) {
                dispatchTabUnselected(currentTab);
            }
            mSelectedTab = tab;
            if (tab != null) {
                dispatchTabSelected(tab);
            }
        }
    }

    private void dispatchTabSelected(final Tab tab) {
        for (int i = 0; i < mSelectedListeners.size(); i++) {
            mSelectedListeners.get(i).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(@NonNull final Tab tab) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabUnselected(tab);
        }
    }

    private void dispatchTabReselected(@NonNull final Tab tab) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabReselected(tab);
        }
    }

    // 计算tab x方向上滑到的坐标位置
    private int calculateScrollXForTab(int position, float positionOffset) {
        if (mMode == MODE_SCROLLABLE) {
            final View selectedChild = mTabStrip.getChildAt(position);
            final View nextChild = position + 1 < mTabStrip.getChildCount()
                    ? mTabStrip.getChildAt(position + 1)
                    : null;
            final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
            final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;

            return selectedChild.getLeft()
                    + ((int) ((selectedWidth + nextWidth) * positionOffset * 0.5f))
                    + (selectedChild.getWidth() / 2)
                    - (getWidth() / 2);
        }
        return 0;
    }

    private void applyModeAndGravity() {
        int paddingStart = 0;
        if (mMode == MODE_SCROLLABLE) {
            // If we're scrollable, or fixed at start, inset using padding
            paddingStart = Math.max(0, mContentInsetStart - mTabPaddingStart);
        }
        ViewCompat.setPaddingRelative(mTabStrip, paddingStart, 0, 0, 0);

        switch (mMode) {
            case MODE_FIXED:
                mTabStrip.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case MODE_SCROLLABLE:
                mTabStrip.setGravity(GravityCompat.START);
                break;
        }

        updateTabViews(true);
    }

    void updateTabViews(final boolean requestLayout) {
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View child = mTabStrip.getChildAt(i);
            child.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LinearLayout.LayoutParams) child.getLayoutParams());
            if (requestLayout) {
                child.requestLayout();
            }
        }
    }

    public static final class Tab {

        public static final int INVALID_POSITION = -1;

        private Object mTag;
        private Drawable mIcon;
        private CharSequence mText;
        private CharSequence mContentDesc;
        private int mPosition = INVALID_POSITION;

        TabView mView;
        SimpleTabLayout mParent;

        Tab() {
            //
        }

        public Object getTag() {
            return mTag;
        }

        public Tab setTag(Object tag) {
            mTag = tag;
            return this;
        }

        public Drawable getIcon() {
            return mIcon;
        }


        public void setIcon(Drawable icon) {
            mIcon = icon;
        }

        public int getPosition() {
            return mPosition;
        }

        void setPosition(int postion) {
            mPosition = postion;
        }

        public CharSequence getText() {
            return mText;
        }

        public Tab setText(@Nullable CharSequence text) {
            mText = text;
            updateView();
            return this;
        }

        public Tab setText(@StringRes int resId) {
            if (mParent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            return setText(mParent.getResources().getText(resId));
        }

        public void select() {
            if (mParent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            mParent.selectTab(this);
        }

        public boolean isSelected() {
            if (mParent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            return mParent.getSelectedTabPosition() == mPosition;
        }

        public Tab setContentDescription(int resId) {
            if (mParent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            return setContentDescription(mParent.getResources().getText(resId));
        }

        public Tab setContentDescription(CharSequence contentDesc) {
            mContentDesc = contentDesc;
            updateView();
            return this;
        }

        public CharSequence getContentDescription() {
            return mContentDesc;
        }

        void updateView() {
            if (mView != null) {
                mView.update();
            }
        }

        void reset() {
            mParent = null;
            mView = null;
            mTag = null;
            mIcon = null;
            mText = null;
            mPosition = INVALID_POSITION;
        }

    }

    public class TabView extends LinearLayout implements OnLongClickListener {
        private Tab mTab;
        private TextView mTextView;
        private ImageView mIconView;

        private int mDefaultMaxLines = DEFAULT_MAX_LINES;

        public TabView(Context context) {
            super(context);
            // 设置布局属性
            if (mTabBackgroundResId != 0) {
                //  TODO: 2016/11/8  暂时无效果,因要用到v7包中的方法
                setBackgroundResource(mTabBackgroundResId);
            }
            ViewCompat.setPaddingRelative(this, mTabPaddingStart, mTabPaddingTop,
                    mTabPaddingEnd, mTabPaddingBottom);
            setGravity(Gravity.CENTER);
            setOrientation(VERTICAL);
            setClickable(true);
        }

        @Override
        public boolean performClick() {
            // 响应点击事件入口
            final boolean handled = super.performClick();
            if (mTab != null) {
                mTab.select();
                return true;
            } else {
                return handled;
            }
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);

            if (mTextView != null) {
                mTextView.setSelected(selected);
            }
            if (mIconView != null) {
                mIconView.setSelected(selected);
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onMeasure(int origWidthMeasureSpec, int origHeightMeasureSpec) {
            final int specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec);
            final int specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec);
            final int maxWidth = getTabMaxWidth();

            final int widthMeasureSpec;
            final int heightMeasureSpec = origHeightMeasureSpec;

            if (maxWidth > 0 && (specWidthMode == MeasureSpec.UNSPECIFIED
                    || specWidthSize > maxWidth)) {
                // If we have a max width and a given spec which is either unspecified or
                // larger than the max width, update the width spec using the same mode
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mTabMaxWidth, MeasureSpec.AT_MOST);
            } else {
                // Else, use the original width spec
                widthMeasureSpec = origWidthMeasureSpec;
            }

            // Now lets measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // We need to switch the text size based on whether the text is spanning 2 lines or not
            if (mTextView != null) {
                final Resources res = getResources();
                float textSize = mTabTextSize;
                int maxLines = mDefaultMaxLines;

                if (mIconView != null && mIconView.getVisibility() == VISIBLE) {
                    // If the icon view is being displayed, we limit the text to 1 line
                    maxLines = 1;
                } else if (mTextView != null && mTextView.getLineCount() > 1) {
                    // Otherwise when we have text which wraps we reduce the text size
                    textSize = mTabTextMultiLineSize;
                }

                final float curTextSize = mTextView.getTextSize();
                final int curLineCount = mTextView.getLineCount();
//                final int curMaxLines = TextViewCompat.getMaxLines(mTextView);
                final int curMaxLines =  mDefaultMaxLines = mTextView.getMaxLines();

                if (textSize != curTextSize || (curMaxLines >= 0 && maxLines != curMaxLines)) {
                    // We've got a new text size and/or max lines...
                    boolean updateTextView = true;

                    if (mMode == MODE_FIXED && textSize > curTextSize && curLineCount == 1) {
                        // If we're in fixed mode, going up in text size and currently have 1 line
                        // then it's very easy to get into an infinite recursion.
                        // To combat that we check to see if the change in text size
                        // will cause a line count change. If so, abort the size change and stick
                        // to the smaller size.
                        final Layout layout = mTextView.getLayout();
                        if (layout == null || approximateLineWidth(layout, 0, textSize)
                                > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) {
                            updateTextView = false;
                        }
                    }

                    if (updateTextView) {
                        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        mTextView.setMaxLines(maxLines);
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                }
            }
        }

        void setTab(@Nullable final Tab tab) {
            if (tab != mTab) {
                mTab = tab;
                update();
            }
        }

        void reset() {
            setTab(null);
            setSelected(false);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        final void update() {
            final Tab tab = mTab;
            // TODO: 2016/11/8 是否支持添加customerView
            if (tab == null) {
                throw new IllegalArgumentException("tab == null");
            }
            // If there isn't a custom view, we'll us our own in-built layouts
            if (mIconView == null) {
                ImageView iconView = createImageView();
                addView(iconView, 0);
                mIconView = iconView;
            }
            if (mTextView == null) {
                TextView textView = createTextView();
                addView(textView);
                mTextView = textView;
//                mDefaultMaxLines = TextViewCompat.getMaxLines(mTextView);
                mDefaultMaxLines = mTextView.getMaxLines();

            }
//            TextViewCompat.setTextAppearance(mTextView, mTabTextAppearance);
            mTextView.setTextColor(mTabTextColors);
            mTextView.setTextSize(mTabTextSize);
            if (mTabTextColors != null) {
                mTextView.setTextColor(mTabTextColors);
            }
            mTextView.getPaint().setFakeBoldText(true);
            updateTextAndIcon(mTextView, mIconView);

            // Finally update our selected state
            setSelected(tab != null && tab.isSelected());
        }

        // 生成默认ImageView
        private ImageView createImageView() {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(DEFAULT_ICON_VIEW_SIZE),dpToPx(DEFAULT_ICON_VIEW_SIZE)));
            return imageView;
        }
        // 生成默认TextView
        private TextView createTextView() {
            TextView textView = new TextView(getContext());
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER);
            textView.setMaxLines(DEFAULT_MAX_LINES);
            return textView;
        }

        private void updateTextAndIcon( final TextView textView,
                                        final ImageView iconView) {
            final CharSequence text = mTab != null ? mTab.getText() : null;
            final Drawable icon = mTab != null ? mTab.getIcon() : null;
            final CharSequence contentDesc = mTab != null ? mTab.getContentDescription() : null;

            if (iconView != null) {
                if (icon != null) {
                    iconView.setImageDrawable(icon);
                    iconView.setVisibility(VISIBLE);
                } else {
                    iconView.setImageDrawable(null);
                    iconView.setVisibility(GONE);
                }
                iconView.setContentDescription(contentDesc);
            }

            final boolean hasText = !TextUtils.isEmpty(text);
            if (textView != null) {
                if (hasText) {
                    textView.setText(text);
                    textView.setVisibility(VISIBLE);
                    setVisibility(VISIBLE);
                } else {
                    textView.setVisibility(GONE);
                    textView.setText(null);
                }
                textView.setContentDescription(contentDesc);
            }

            if (iconView != null) {
                MarginLayoutParams lp = (MarginLayoutParams) iconView.getLayoutParams();
                int bottomMargin = 0;
                if (hasText && iconView.getVisibility() == VISIBLE) {
                    // icon 与text之间添加一定的距离
                    bottomMargin = dpToPx(DEFAULT_GAP_TEXT_ICON);
                }
                if (bottomMargin != lp.bottomMargin) {
                    lp.bottomMargin = bottomMargin;
                    iconView.requestLayout();
                }
            }

            if (!hasText && !TextUtils.isEmpty(contentDesc)) {
                setOnLongClickListener(this);
            } else {
                setOnLongClickListener(null);
                setLongClickable(false);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            // TODO: 2016/11/8 暂时当没有文字且有contentDesc描述信息是有效
           // Toast.makeText(getContext(), "onLongClick", Toast.LENGTH_SHORT).show();
            return false;
        }

        public Tab getTab() {
            return mTab;
        }
        
        /**
         * Approximates a given lines width with the new provided text size.
         */
        private float approximateLineWidth(Layout layout, int line, float textSize) {
            return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize());
        }

        public int getTextSpaceWidth() {
            if (mTextView != null) {
                TextPaint paint = mTextView.getPaint();
                return (getWidth() - Math.round(paint.measureText(mTextView.getText().toString()))) / 2;
            }
            return 0;
        }
    }

    // TabView 的容器View
    private class SlidingTabStrip extends LinearLayout {
        
        private int mSelectedIndicatorHeight;
        private final Paint mSelectedIndicatorPaint;
        private final Paint mIndicatorBackgrounPaint;
        int mSelectedPosition = -1;
        float mSelectionOffset;

        private int mIndicatorLeft = -1;
        private int mIndicatorRight = -1;

        private ValueAnimator mIndicatorAnimator;// // TODO: 2016/11/7 暂时先用不考虑兼容
        public SlidingTabStrip(Context context) {
            super(context);
            setWillNotDraw(false);
            mSelectedIndicatorPaint = new Paint();
            mIndicatorBackgrounPaint = new Paint();
            mIndicatorBackgrounPaint.setStyle(Paint.Style.FILL);
            mIndicatorBackgrounPaint.setColor(Color.parseColor(INDICATOR_BACKGROUND_COLOR));
        }

        void setSelectedIndicatorColor(int color) {
            if (mSelectedIndicatorPaint.getColor() != color) {
                mSelectedIndicatorPaint.setColor(color);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        void setSelectedIndicatorHeight(int height) {
            if (mSelectedIndicatorHeight != height) {
                mSelectedIndicatorHeight = height;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        boolean childrenNeedLayout() {
            for (int i = 0, z = getChildCount(); i < z; i++) {
                final View child = getChildAt(i);
                if (child.getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
            if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
                mIndicatorAnimator.cancel();
            }

            mSelectedPosition = position;
            mSelectionOffset = positionOffset;
            updateIndicatorPosition();
        }

        float getIndicatorPosition() {
            return mSelectedPosition + mSelectionOffset;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
                // HorizontalScrollView will first measure use with UNSPECIFIED, and then with
                // EXACTLY. Ignore the first call since anything we do will be overwritten anyway
                return;
            }
            // TODO: 2016/11/8  需优化修改
            if (mMode == MODE_FIXED && mTabGravity == GRAVITY_CENTER) {
                final int count = getChildCount();

                // First we'll find the widest tab
                int largestTabWidth = 0;
                for (int i = 0, z = count; i < z; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() == VISIBLE) {
                        largestTabWidth = Math.max(largestTabWidth, child.getMeasuredWidth());
                    }
                }

                if (largestTabWidth <= 0) {
                    // If we don't have a largest child yet, skip until the next measure pass
                    return;
                }

                final int gutter = dpToPx(FIXED_WRAP_GUTTER_MIN);
                boolean remeasure = false;

                if (largestTabWidth * count <= getMeasuredWidth() - gutter * 2) {
                    // If the tabs fit within our width minus gutters, we will set all tabs to have
                    // the same width
                    for (int i = 0; i < count; i++) {
                        final LayoutParams lp =
                                (LayoutParams) getChildAt(i).getLayoutParams();
                        if (lp.width != largestTabWidth || lp.weight != 0) {
                            lp.width = largestTabWidth;
                            lp.weight = 0;
                            remeasure = true;
                        }
                    }
                } else {
                    // If the tabs will wrap to be larger than the width minus gutters, we need
                    // to switch to GRAVITY_FILL
                    mTabGravity = GRAVITY_FILL;
                    updateTabViews(false);
                    remeasure = true;
                }

                if (remeasure) {
                    // Now re-measure after our changes
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (mIndicatorAnimator != null &&mIndicatorAnimator.isRunning()) {
                mIndicatorAnimator.cancel();
                final long duration = mIndicatorAnimator.getDuration();
                animateIndicatorToPosition(mSelectedPosition, Math.round(1f - mIndicatorAnimator.getAnimatedFraction() * duration));
            } else {
                updateIndicatorPosition();
            }
        }

        private void updateIndicatorPosition() {
            final TabView selectedTitle = (TabView) getChildAt(mSelectedPosition);
            int left, right;

            if (selectedTitle != null && selectedTitle.getWidth() > 0) {
                left = selectedTitle.getLeft();
                right = selectedTitle.getRight();
                // 若indicator与文字宽度一致,则修正值获取文本字本的space
                int curTextSpaceWidth = selectedTitle.getTextSpaceWidth();
                left = mIsSameWidthForIndicatorAndText ? left + curTextSpaceWidth : left;
                right = mIsSameWidthForIndicatorAndText ? right - curTextSpaceWidth : right;

                if (mSelectionOffset > 0f && mSelectedPosition < getChildCount() - 1) {
                    // Draw the selection partway between the tabs
                    TabView nextTitle = (TabView) getChildAt(mSelectedPosition + 1);
                    // 同理修正下一个位置的值
                    int nextTextSpaceWidth = nextTitle.getTextSpaceWidth();
                    int nextLeft = mIsSameWidthForIndicatorAndText ? nextTitle.getLeft() + nextTextSpaceWidth : nextTitle.getLeft();
                    int nextRight = mIsSameWidthForIndicatorAndText ? nextTitle.getRight() - nextTextSpaceWidth : nextTitle.getRight();

                    left = (int) (mSelectionOffset * nextLeft +
                            (1.0f - mSelectionOffset) * left);
                    right = (int) (mSelectionOffset * nextRight +
                            (1.0f - mSelectionOffset) * right);
                }
            } else {
                left = right = -1;
            }

            setIndicatorPosition(left, right);
        }

        void animateIndicatorToPosition(final int position, int duration) {
            if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
                mIndicatorAnimator.cancel();
            }

            final boolean isRtl = ViewCompat.getLayoutDirection(this)
                    == ViewCompat.LAYOUT_DIRECTION_RTL;

            final TabView targetView = (TabView) getChildAt(position);
            if (targetView == null) {
                // If we don't have a view, just update the position now and return
                updateIndicatorPosition();
                return;
            }
            // 若indicator与文字宽度一致,则修正值
            int targetTextSpaceWidth = targetView.getTextSpaceWidth();
           final int targetLeft = mIsSameWidthForIndicatorAndText ? targetView.getLeft() + targetTextSpaceWidth : targetView.getLeft() ;
           final int targetRight = mIsSameWidthForIndicatorAndText ? targetView.getRight() - targetTextSpaceWidth : targetView.getRight();

            final int startLeft;
            final int startRight;

            if (Math.abs(position - mSelectedPosition) <= 1) {
                // If the views are adjacent, we'll animate from edge-to-edge
                startLeft = mIsSameWidthForIndicatorAndText ? mIndicatorLeft + mTabPaddingStart : mIndicatorLeft;
                startRight = mIsSameWidthForIndicatorAndText ? mIndicatorRight - mTabPaddingEnd : mIndicatorRight;
            } else {
                // Else, we'll just grow from the nearest edge
                final int offset = dpToPx(MOTION_NON_ADJACENT_OFFSET);
                if (position < mSelectedPosition) {
                    // We're going end-to-start
                    if (isRtl) {
                        startLeft = startRight = targetLeft - offset;
                    } else {
                        startLeft = startRight = targetRight + offset;
                    }
                } else {
                    // We're going start-to-end
                    if (isRtl) {
                        startLeft = startRight = targetRight + offset;
                    } else {
                        startLeft = startRight = targetLeft - offset;
                    }
                }
            }
            // TODO: 2016/11/8 动画 
            if (startLeft != targetLeft || startRight != targetRight) {
                ValueAnimator animator = mIndicatorAnimator = new ValueAnimator();
                animator.setInterpolator(AnimUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                animator.setDuration(duration);
                animator.setFloatValues(0, 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        final float fraction = animator.getAnimatedFraction();
                        setIndicatorPosition(
                                AnimUtils.lerp(startLeft, targetLeft, fraction),
                                AnimUtils.lerp(startRight, targetRight, fraction));
                    }
                });

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSelectedPosition = position;
                        mSelectionOffset = 0f;
                    }

                });
                animator.start();
            }
        }

        void setIndicatorPosition(int left, int right) {
            if (left != mIndicatorLeft || right != mIndicatorRight) {
                // If the indicator's left/right has changed, invalidate
                mIndicatorLeft = left;
                mIndicatorRight = right;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            // 先画背景线
            if (mHasIndicatorBackground) {
                canvas.drawRect(0,getHeight() - mSelectedIndicatorHeight,getWidth(),getHeight(),mIndicatorBackgrounPaint);
            }
            // Thick colored underline below the current selection
            if (mIndicatorLeft >= 0 && mIndicatorRight > mIndicatorLeft) {
                canvas.drawRect(mIndicatorLeft, getHeight() - mSelectedIndicatorHeight,
                        mIndicatorRight, getHeight(), mSelectedIndicatorPaint);
            }
        }
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        return new ColorStateList(states, colors);
    }

    private int getDefaultHeight() {
        boolean hasIconAndText = false;
        for (int i = 0, count = mTabs.size(); i < count; i++) {
            Tab tab = mTabs.get(i);
            if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                hasIconAndText = true;
                break;
            }
        }
        return hasIconAndText ? DEFAULT_HEIGHT_WITH_TEXT_ICON : DEFAULT_HEIGHT;
    }

    private int getTabMinWidth() {
        if (mRequestedTabMinWidth != INVALID_WIDTH) {
            // If we have been given a min width, use it
            return mRequestedTabMinWidth;
        }
        // Else, we'll use the default value
        return mMode == MODE_SCROLLABLE ? mScrollableTabMinWidth : 0;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        // We don't care about the layout params of any views added to us, since we don't actually
        // add them. The only view we add is the SlidingTabStrip, which is done manually.
        // We return the default layout params so that we don't blow up if we're given a TabItem
        // without android:layout_* values.
        return generateDefaultLayoutParams();
    }

    int getTabMaxWidth() {
        return mTabMaxWidth;
    }

    /***********************与viewpager关联的监听***************************************/
    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<SimpleTabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;

        public TabLayoutOnPageChangeListener(SimpleTabLayout tabLayout) {
            mTabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                                   final int positionOffsetPixels) {
            final SimpleTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                final boolean updateText = mScrollState != SCROLL_STATE_SETTLING ||
                        mPreviousScrollState == SCROLL_STATE_DRAGGING;
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                final boolean updateIndicator = !(mScrollState == SCROLL_STATE_SETTLING
                        && mPreviousScrollState == SCROLL_STATE_IDLE);
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator);
            }
        }

        @Override
        public void onPageSelected(final int position) {
            final SimpleTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position
                    && position < tabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                final boolean updateIndicator = mScrollState == SCROLL_STATE_IDLE
                        || (mScrollState == SCROLL_STATE_SETTLING
                        && mPreviousScrollState == SCROLL_STATE_IDLE);
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator);
            }
        }

        void reset() {
            mPreviousScrollState = mScrollState = SCROLL_STATE_IDLE;
        }
    }

    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(Tab tab) {
            // No-op
        }

        @Override
        public void onTabReselected(Tab tab) {
            // No-op
        }
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        @Override
        public void onChanged() {
            populateFromPagerAdapter();
        }

        @Override
        public void onInvalidated() {
            populateFromPagerAdapter();
        }
    }

//    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
//        private boolean mAutoRefresh;
//
//        AdapterChangeListener() {
//        }
//
//        @Override
//        public void onAdapterChanged(@NonNull ViewPager viewPager,
//                                     @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
//            if (mViewPager == viewPager) {
//                setPagerAdapter(newAdapter, mAutoRefresh);
//            }
//        }
//
//        void setAutoRefresh(boolean autoRefresh) {
//            mAutoRefresh = autoRefresh;
//        }
//    }
}
