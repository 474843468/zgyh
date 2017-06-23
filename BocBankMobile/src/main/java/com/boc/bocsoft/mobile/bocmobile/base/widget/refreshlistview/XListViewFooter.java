package com.boc.bocsoft.mobile.bocmobile.base.widget.refreshlistview;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	public final static int STATE_NO_MORE = 3;
	private Context mContext;

	private View mContentView;
	private TextView mHintView;
	private ImageView mLoadIcon;
	private Animation animation;
	
	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}
	
	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	
	public void setState(int state) {
		switch (state) {
		case STATE_READY://查看更多
			show();
			mLoadIcon.clearAnimation();
			mLoadIcon.setImageResource(R.drawable.refresh_logo);
			mHintView.setText(R.string.boc_moedel);
			break;
		case STATE_LOADING://正在载入...
			show();
			mHintView.setText(R.string.boc_loading);
			mLoadIcon.setImageResource(R.drawable.refresh_logo);
			mLoadIcon.startAnimation(animation);
			break;
		case STATE_NO_MORE://没有更多数据
			show();
			mHintView.setText(R.string.boc_no_more_data);
			mLoadIcon.setImageResource(R.drawable.refresh_logo);
			mLoadIcon.clearAnimation();
			mLoadIcon.setVisibility(GONE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					hide();
				}
			}, 500);
			break;
		default://加载更多
			hide();
			mLoadIcon.clearAnimation();
			mHintView.setText(R.string.boc_loading);
			mLoadIcon.setImageResource(R.drawable.refresh_logo);
			break;
		}
	}
	
	public void setBottomMargin(int height) {
//		if (height < 0) return ;
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}
	
	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	
	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
	}
	
	
	/**
	 * loading status 
	 */
	public void loading() {//正在载入
		mHintView.setVisibility(View.GONE);
		mHintView.setText(R.string.boc_loading);
		mLoadIcon.setImageResource(R.drawable.refresh_logo);
	}
	
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
	
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mHintView = (TextView)moreView.findViewById(R.id.xlistview_footer_hint_textview);
		mLoadIcon = (ImageView) moreView.findViewById(R.id.iv_load);
		float dimension = getResources().getDimension(
				R.dimen.pull_refresh_icon_width);
		//设置加载动画
		animation = new RotateAnimation(0, 359, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//		animation = new Rotate3dAnimation(-360, 0, dimension / 2, dimension,
//				0, true);
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		animation.setDuration(1000);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setRepeatMode(Animation.RESTART);
	}
	
	public void onStopLoadMore(boolean flag) {
		
		if (flag) {
			mLoadIcon.setImageResource(R.drawable.boc_load_succeed);
			mHintView.setText(R.string.boc_load_success);//加载成功
		} else {
			mLoadIcon.setImageResource(R.drawable.boc_load_failed);
			mHintView.setText(R.string.boc_load_fail);//加载失败
		}
		hide();
	}
}
