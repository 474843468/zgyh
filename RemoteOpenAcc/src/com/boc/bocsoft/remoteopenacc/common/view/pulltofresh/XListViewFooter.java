package com.boc.bocsoft.remoteopenacc.common.view.pulltofresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.remoteopenacc.R;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

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
		case STATE_READY:
			show();
			mLoadIcon.clearAnimation();
			mLoadIcon.setImageResource(R.drawable.bocroa_refresh_logo);
			mHintView.setText(R.string.bocroa_xlistview_footer_hint_ready);
			break;
		case STATE_LOADING:
			show();
			mHintView.setText(R.string.bocroa_xlistview_header_hint_loading);
			mLoadIcon.setImageResource(R.drawable.bocroa_refresh_logo);
			mLoadIcon.startAnimation(animation);
			break;
		default:
			hide();
			mLoadIcon.clearAnimation();
			mHintView.setText(R.string.bocroa_xlistview_footer_hint_normal);
			mLoadIcon.setImageResource(R.drawable.bocroa_refresh_logo);
			break;
		}
	}
	
	public void setBottomMargin(int height) {
//		if (height < 0) return ;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}
	
	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
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
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mHintView.setText(R.string.bocroa_xlistview_header_hint_loading);
		mLoadIcon.setImageResource(R.drawable.bocroa_refresh_logo);
	}
	
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
	
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.bocroa_xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mHintView = (TextView)moreView.findViewById(R.id.xlistview_footer_hint_textview);
		mLoadIcon = (ImageView) moreView.findViewById(R.id.iv_load);
		float dimension = getResources().getDimension(
				R.dimen.bocroa_pull_refresh_icon_width);
		animation = new Rotate3dAnimation(-360, 0, dimension / 2, dimension,
				0, true);
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		animation.setDuration(1000);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setRepeatMode(Animation.RESTART);
	}
	
	public void onStopLoadMore(boolean flag) {
		if (flag) {
			mLoadIcon.setImageResource(R.drawable.bocroa_refresh_sucess);
			mHintView.setText(R.string.bocroa_xlistview_loading_success);
		} else {
			mLoadIcon.setImageResource(R.drawable.bocroa_refresh_fault);
			mHintView.setText(R.string.bocroa_xlistview_loading_fault);
		}
		hide();
	}
}
