package com.boc.bocsoft.mobile.bocmobile.base.widget.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;


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
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		float dimension = getResources().getDimension(
				R.dimen.pull_refresh_icon_width);

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
		mArrowImageView.setImageResource(R.drawable.refresh_logo);
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
			mHintTextView.setText(R.string.boc_pull_down_load);//下拉刷新
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				// mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.boc_release_to_load);//释放加载
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.boc_loading);//正在载入...
			break;
		}
		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

	public void onStopRefresh(boolean b) {
		if (b) {
			mArrowImageView.setImageResource(R.drawable.boc_load_succeed);
			mHintTextView.setText(R.string.boc_load_success);
		} else {
			mArrowImageView.setImageResource(R.drawable.boc_load_failed);
			mHintTextView.setText(R.string.boc_load_fail);
		}
	}

}
