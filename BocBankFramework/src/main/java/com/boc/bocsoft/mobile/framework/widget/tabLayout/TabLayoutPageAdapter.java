package com.boc.bocsoft.mobile.framework.widget.tabLayout;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/8/10 14:10
 * 描述：
 */
public class TabLayoutPageAdapter extends PagerAdapter {
    List<View> mViews = null;
    List<String> mTitles = null;

    public TabLayoutPageAdapter() {
        mViews = new ArrayList<View>();
        mTitles = new ArrayList<String>();
    }

    public View getItem(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        View view = mViews.get(position);
        container.addView(view);//添加页卡
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void addPage(View pageView, String pageTitle) {
        mViews.add(pageView);
        mTitles.add(pageTitle);
        notifyDataSetChanged();
    }
}
