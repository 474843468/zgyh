package com.chinamworld.bocmbci.biz.safety.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class SafetyPagerAdapter extends PagerAdapter {

	List<View> mViewList;

	public SafetyPagerAdapter(List<View> viewList) {
		this.mViewList = viewList;
	}

	@Override
	public int getCount() {
		return mViewList.size();
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViewList.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViewList.get(position));
		return mViewList.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
