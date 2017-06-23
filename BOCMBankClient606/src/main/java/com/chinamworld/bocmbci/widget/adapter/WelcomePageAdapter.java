package com.chinamworld.bocmbci.widget.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class WelcomePageAdapter extends PagerAdapter{
	//引导页的界面
	private ArrayList<View> list;
	
	public WelcomePageAdapter(ArrayList<View> list){
		this.list = list;
	}
	
	/** 这个方法，是获取当前窗体界面数 **/
	@Override
	public int getCount() {
		return list.size();
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
	
	/** 这个方法，是从ViewGroup中移出当前View **/
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(list.get(position));
	}
	
	/**
	 * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象
	 * 
	 * 放在当前的ViewPager中
	 **/
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		// TODO Auto-generated method stub
		((ViewPager) arg0).addView(list.get(arg1));
		
		return list.get(arg1);
	}
}
