package com.chinamworld.bocmbci.biz;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;

public class GuidePageActivity extends Activity implements OnGestureListener {
	/** 引导页viewPager */
	private ViewPager viewPager;// 使用ViewPager来添加引导页
	/** 引导页容器 */
	private ArrayList<View> list;// 引导页的界面
	/** 绘制底部圆点layout */
	private ViewGroup pointLayout;
	private LayoutInflater inflater;
	private GestureDetector detector;
	private ViewFlipper flipper;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 只支持竖屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 添加引导页
		inflater = LayoutInflater.from(GuidePageActivity.this);
		// initPage();

		setContentView(R.layout.guide_page_layout);
		pointLayout = (ViewGroup) findViewById(R.id.point_layout);
		MarginLayoutParams layoutParams = (MarginLayoutParams) pointLayout.getLayoutParams();
		layoutParams.setMargins(0, 0, 0, LayoutValue.SCREEN_HEIGHT / 7 - 70);
		SharedPreferences sharedPreferences = this.getSharedPreferences(ConstantGloble.CONFIG_FILE, MODE_PRIVATE);
		int loginTime = sharedPreferences.getInt(ConstantGloble.LOGIN_TIMES, 0);
		if (loginTime < 1) {
			loginTime++;
			sharedPreferences.edit().putInt(ConstantGloble.LOGIN_TIMES, loginTime).commit();
			detector = new GestureDetector((android.view.GestureDetector.OnGestureListener) this);
			flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);
			flipper.addView(addImageView(R.drawable.welcom1));
			flipper.addView(addImageView(R.drawable.welcom2));
			flipper.addView(addImageView(R.drawable.welcom3));
			flipper.addView(addLastView(R.drawable.welcom4));

			// viewPager = (ViewPager) this.findViewById(R.id.viewPager);
			// viewPager.setAdapter(new WelcomePageAdapter(list));
			refreshPageView(flipper.getChildCount(), 0);
		} else {
			// 检测如果登录三次之后,不显示引导页,则直接进入登录页面
			Intent intent = new Intent(GuidePageActivity.this, LoadingActivity.class);
			startActivity(intent);
			finish();
		}
	}

	// 点击最后开始体验进入登录界面
	public void beginClick(View view) {
		Intent intent = new Intent(GuidePageActivity.this, LoadingActivity.class);
		startActivity(intent);
		finish();
		list = null;
	}

	// /**
	// * 初始化page容器
	// */
	// private void initPage() {
	// list = new ArrayList<View>();
	// list.add(inflater.inflate(R.layout.guide_page1, null));
	// list.add(inflater.inflate(R.layout.guide_page2, null));
	// list.add(inflater.inflate(R.layout.guide_page5, null));
	// }

	private View addImageView(int id) {
		ImageView iv = new ImageView(this);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iv.setLayoutParams(lp);
		iv.setBackgroundResource(id);
		return iv;
	}

	private View addLastView(int id) {
		FrameLayout ll = new FrameLayout(this);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);
		ll.setBackgroundResource(id);
		ImageView iv = new ImageView(this);
		LayoutParams ivlp = new LayoutParams(LayoutValue.SCREEN_WIDTH / 2, 70);
		ivlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		ivlp.setMargins(0, 0, 0, LayoutValue.SCREEN_HEIGHT / 7);
		iv.setLayoutParams(ivlp);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GuidePageActivity.this, LoadingActivity.class);
				startActivity(intent);
				finish();
			}
		});
		ll.addView(iv);
		return ll;
	}

	/**
	 * 回退键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			if (i < 3) {
				i++;
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.welcom_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.welcom_left_out));
				this.flipper.showNext();
				refreshPageView(flipper.getChildCount(), i);
			} else if (i == 3) {
				Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
				startActivity(intent);
				finish();
			}
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			if (i > 0) {
				i--;
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.welcom_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.welcom_right_out));
				this.flipper.showPrevious();
				refreshPageView(flipper.getChildCount(), i);
			}
			return true;
		}

		return false;
	}

	public void refreshPageView(int size, int position) {
		pointLayout.removeAllViews();
		int imgWidth = getResources().getDimensionPixelSize(R.dimen.main_page_icon_height);
		LayoutParams lp = new LayoutParams(imgWidth, imgWidth);
		int gag = getResources().getDimensionPixelSize(R.dimen.dp_five);
//		lp.setMargins(gag, gag, gag, gag);
		if (size > 1) {
			for (int page = 0; page < size; page++) {
				ImageView imgView = new ImageView(this);
				if (page == position) {
					imgView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.current_page));
				} else {
					imgView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.other_page));
				}
				imgView.setLayoutParams(lp);
				pointLayout.addView(imgView);
				MarginLayoutParams layoutParams = (MarginLayoutParams) imgView.getLayoutParams();
				layoutParams.setMargins(gag, gag, gag, gag);
				imgView.setLayoutParams(layoutParams);
				imgView.requestLayout();
			}
		}
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		flipper = null;
	}

}
