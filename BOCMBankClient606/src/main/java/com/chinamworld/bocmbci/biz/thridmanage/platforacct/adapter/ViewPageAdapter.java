package com.chinamworld.bocmbci.biz.thridmanage.platforacct.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPageAdapter extends PagerAdapter {
	
	private List<View> mList;
	
	public ViewPageAdapter(List<View> list){
		this.mList = list;
	}
	
	/** 这个方法，是获取当前窗体界面数 **/
	@Override
	public int getCount() {
		return mList.size();
	}

	/**
	 * 这个方法，在帮助文档中原文是could be implemented as return view == object,
	 * 
	 * 也就是用于判断是否由对象生成界面
	 **/
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {

		return super.getItemPosition(object);
	}

	/** 这个方法，是从ViewGroup中移出当前View **/
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {

		((ViewPager) arg0).removeView(mList.get(arg1));
	}

	/**
	 * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象
	 * 
	 * 放在当前的ViewPager中
	 **/
	@Override
	public Object instantiateItem(View arg0, int arg1) {

		((ViewPager) arg0).addView(mList.get(arg1));

		return mList.get(arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {

		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}
}
