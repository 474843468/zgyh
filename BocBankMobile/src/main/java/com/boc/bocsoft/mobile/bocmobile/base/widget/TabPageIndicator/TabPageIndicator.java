/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
    /**
     * Title text used when no title is provided by the adapter.
     */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position, View view);
    }

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected, view);
            }
        }
    };

    private LinearLayout mTabLayout;
    private ViewPager mViewPager;
    private OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    private int selectedTextColor;
    private int unSelectedTextColor;
    private float textSize;

    private OnTabReselectedListener mTabReselectedListener;

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        textSize = getResources().getDimension(R.dimen.boc_space_between_26px);
        selectedTextColor = getResources().getColor(R.color.boc_text_color_red);
        unSelectedTextColor = getResources().getColor(R.color.boc_text_color_common_gray);
        mTabLayout = new LinearLayout(getContext());
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, FILL_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mViewPager.getAdapter().getCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void addTab(CharSequence text, int index) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setGravity(Gravity.CENTER);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setText(text);
        tabView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);//设置Indicator字体大小
        String familyName = null;
        tabView.setTypeface(Typeface.create(familyName, Typeface.BOLD));
        if (index == 0) {
            tabView.setTextColor(selectedTextColor);
        } else {
            tabView.setTextColor(unSelectedTextColor);
        }
        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, FILL_PARENT, 1));
    }

    public TabView getTabViewByIndex(int index){
        return (TabView)mTabLayout.getChildAt(index);
    }

    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    public void onPageSelected(int arg0) {
        int count = mTabLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mTabLayout.getChildAt(i);
            if (view instanceof TabView) {
                TabView t = (TabView) view;
                if (t.mIndex == arg0) {
                    t.setTextColor(selectedTextColor);
                } else {
                    t.setTextColor(unSelectedTextColor);
                }
            }
        }
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            addTab(title, i);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mViewPager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                // animateToTab(item);
            }
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    public class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.TabPageIndicatorStyle);
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }

    public void setUnSelectedTextColor(int unSelectedTextColor) {
        this.unSelectedTextColor = unSelectedTextColor;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }
}
