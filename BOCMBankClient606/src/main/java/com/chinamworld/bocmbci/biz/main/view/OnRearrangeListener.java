package com.chinamworld.bocmbci.biz.main.view;

public interface OnRearrangeListener {
	
	public abstract void onRearrange(int oldIndex, int newIndex);
	
	public abstract void beginAnimation();
}
