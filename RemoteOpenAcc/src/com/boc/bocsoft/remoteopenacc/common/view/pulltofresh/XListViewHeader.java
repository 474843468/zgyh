package com.boc.bocsoft.remoteopenacc.common.view.pulltofresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.remoteopenacc.R;

public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	private Animation mPBAnimation;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.bocroa_xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		float dimension = getResources().getDimension(
				R.dimen.bocroa_pull_refresh_icon_width);

		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mPBAnimation = new Rotate3dAnimation(-360, 0, dimension / 2,
				dimension, 0, true);
		LinearInterpolator lin = new LinearInterpolator();
		mPBAnimation.setInterpolator(lin);
		mPBAnimation.setDuration(1000);
		mPBAnimation.setRepeatCount(Animation.INFINITE);
		mPBAnimation.setRepeatMode(Animation.RESTART);
		if (isInEditMode()) {
			return;
		}
	}

	public void setState(int state) {
		if (state == mState)
			return;
		mArrowImageView.clearAnimation();
		mArrowImageView.setImageResource(R.drawable.bocroa_refresh_logo);
		if (state == STATE_REFRESHING) { // 显示进度
			mArrowImageView.startAnimation(mPBAnimation);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				// mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(R.string.bocroa_xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				// mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.bocroa_xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.bocroa_xlistview_header_hint_loading);
			break;
		}
		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

	public void onStopRefresh(boolean b) {
		if (b) {
			mArrowImageView.setImageResource(R.drawable.bocroa_refresh_sucess);
			mHintTextView.setText(R.string.bocroa_xlistview_loading_success);
		} else {
			mArrowImageView.setImageResource(R.drawable.bocroa_refresh_fault);
			mHintTextView.setText(R.string.bocroa_xlistview_loading_fault);
		}
	}

}
