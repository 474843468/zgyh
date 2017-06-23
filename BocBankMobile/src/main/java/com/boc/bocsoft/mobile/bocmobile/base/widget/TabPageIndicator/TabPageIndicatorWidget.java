package com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by wangtong on 2016/9/5.
 */
public class TabPageIndicatorWidget extends LinearLayout implements PageIndicator {

    private View rootView;
    private ViewPager viewPager;
    private TabPageIndicator indicator;
    private UnderlinePageIndicator indicatorLine;

    public TabPageIndicatorWidget(Context context) {
        super(context);
        initView();
    }

    public TabPageIndicatorWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TabPageIndicatorWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        rootView = View.inflate(getContext(), R.layout.boc_layout_tab_page, this);
    }

    public void addViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOffscreenPageLimit(4);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        indicatorLine = (UnderlinePageIndicator) findViewById(R.id.indicator_line);
        indicatorLine.setViewPager(viewPager);
        indicatorLine.setOnPageChangeListener(indicator);
        indicator.setOnPageChangeListener(this);
    }

    public void setTabReselectedListener(TabPageIndicator.OnTabReselectedListener listener) {
        indicator.setOnTabReselectedListener(listener);
    }

    /**
     * 设置下划线颜色
     */
    public void setIndicatorColor(int color) {
        indicatorLine.setSelectedColor(getResources().getColor(color));
    }
    /**
     * 设置下划线未选中时颜色
     */
    public void setIndicatorUnSelectedColor(int color) {
        indicatorLine.setBackgroundColor(getResources().getColor(color));
    }


    /**
     * 设置选中字体颜色和未选中的字体颜色
     */
    public void setSelectedTextColor(int selectedColor, int unSelectedColor) {
        indicator.setSelectedTextColor(getResources().getColor(selectedColor));
        indicator.setUnSelectedTextColor(getResources().getColor(unSelectedColor));
        indicator.notifyDataSetChanged();
    }

    /**
     * 设置下划线高度
     */
    public void setIndicatorHeight(int height) {
        indicatorLine.getLayoutParams().height = height;
    }

    @Override
    public void setViewPager(ViewPager view) {

    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {

    }

    @Override
    public void setCurrentItem(int item) {

    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
