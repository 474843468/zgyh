package com.chinamworld.llbt.userwidget.selectaccountview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 实现CommonAdapter 适配器中的getView()方法
 * */
public interface ICommonAdapter<T> {
	/**
	 * CommonAdapter 适配器 获得Item项 必须实现的接口
	 * @param arg0  ：当前项下标
	 * @param currentItem ： 当前适配器的当前显示项
	 * @param inflater ： 加载布局参数
	 * @param convertView ： 当前显示项View布局
	 * @return
	 */
	View getView(int arg0, T currentItem, LayoutInflater inflater, View convertView, ViewGroup viewGroup);
}