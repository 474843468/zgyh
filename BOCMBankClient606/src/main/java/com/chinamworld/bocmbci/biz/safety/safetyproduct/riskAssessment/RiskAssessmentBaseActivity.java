package com.chinamworld.bocmbci.biz.safety.safetyproduct.riskAssessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;

/**
 * 风险评估父类
 * 
 * @author Zhi
 */
public class RiskAssessmentBaseActivity extends BaseActivity {

	/** 主页面 */
	private LinearLayout mBodyLayout;
	/** 右按钮 */
	private Button mRightButton;
	/** 左按钮 */
	public Button mLeftButton;
	/** 弹出二级菜单按钮 */
//	private Button btn_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		hineLeftSideMenu();
		initPulldownBtn();
		initFootMenu();
		init();
	}

	private void init() {
		mBodyLayout = (LinearLayout) findViewById(R.id.sliding_body);
		mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		mLeftButton = (Button) findViewById(R.id.ib_back);
		findViewById(R.id.btn_show).setVisibility(View.GONE);
		mRightButton.setOnClickListener(toMainOnClickListener);
		mLeftButton.setOnClickListener(backOnClickListener);
	}

	/**
	 * 添加视图到布局
	 * 
	 * @param resource 资源id文件
	 * @return
	 */
	protected View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		mBodyLayout.removeAllViews();
		mBodyLayout.addView(view);
		return view;
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
	}

	private OnClickListener backOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (RiskAssessmentBaseActivity.this instanceof RiskAssessmentActivity) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(RiskAssessmentBaseActivity.this, SecondMainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			} else {
				setResult(65536);
				finish();
			}
		}
	};

	private OnClickListener toMainOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SafetyDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}
}
