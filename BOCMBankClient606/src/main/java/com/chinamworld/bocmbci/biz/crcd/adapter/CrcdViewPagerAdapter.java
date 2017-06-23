package com.chinamworld.bocmbci.biz.crcd.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CrcdViewPagerAdapter extends PagerAdapter {

	private ArrayList<View> view;

	public CrcdViewPagerAdapter(ArrayList<View> view) {
		this.view = view;
	}

	/** 获取当前窗体界面数 */
	@Override
	public int getCount() {
		return view.size();
	}

	/** 用于判断是否由对象生成界面 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	/** 这个方法，是从ViewGroup中移出当前View **/
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(view.get(position));
	}

	/**
	 * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象
	 * 
	 * 放在当前的ViewPager中
	 **/
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		// TODO Auto-generated method stub
		((ViewPager) arg0).addView(view.get(arg1));
		return view.get(arg1);
	}

}
