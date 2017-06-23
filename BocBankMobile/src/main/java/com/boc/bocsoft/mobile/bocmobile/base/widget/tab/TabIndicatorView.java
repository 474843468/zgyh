package com.boc.bocsoft.mobile.bocmobile.base.widget.tab;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import android.view.View;
import android.widget.FrameLayout;

//导航指示器
public abstract class TabIndicatorView extends FrameLayout{

	protected Context mContext;

	public TabIndicatorView(Context context) {
		this(context,null);
	}

	public TabIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
		initView();
	}

	private void init(){
		View view = getContentView();
		this.removeAllViews();
		this.addView(view);
	}

	protected abstract void initView();
	protected abstract View getContentView();

	public abstract String getTagId();


	public abstract Class<? extends Fragment> getFragmentClass();
}
