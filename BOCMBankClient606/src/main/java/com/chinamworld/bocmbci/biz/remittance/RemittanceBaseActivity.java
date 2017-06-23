
package com.chinamworld.bocmbci.biz.remittance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.recordQuery.RemittanceRecordQueryListActivity;
import com.chinamworld.bocmbci.biz.remittance.templateManagement.RemittanceTemplateListActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 跨境汇款基类
 * 
 * @author Zhi
 */
public class RemittanceBaseActivity extends BaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 退出时的resultCode */
	public static final int QUIT_CODE = 4444;
	/** 二级菜单下标-汇款申请 */
	private static final int REMITTANCE_APPLICATION_FOR_REMITTANCE = 0;
	/** 二级菜单下标-模板管理 */
	private static final int REMITTANCE_TEMPLATE_MANAGEMENT = 1;
	/** 二级菜单下标-汇款记录查询 */
	private static final int REMITTANCE_RECORD_QUERY = 2;
	/** 主页面 */
	private LinearLayout mBodyLayout;
	/** 右按钮 */
	private Button mRightButton;
	/** 左按钮 */
	private Button mLeftButton;
	/** 弹出二级菜单按钮 */
	private Button btnShow;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initLeftSideList(this, LocalData.crossBorderRemitLeftListData);
		initPulldownBtn();
		initFootMenu();
		init();
	}

	private void init() {
		mBodyLayout = (LinearLayout) findViewById(R.id.sliding_body);
		mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		mLeftButton = (Button) findViewById(R.id.ib_back);
		btnShow = (Button) findViewById(R.id.btn_show);
		mRightButton.setOnClickListener(toMainOnClickListener);
		mLeftButton.setOnClickListener(backOnClickListener);
	}

	protected void addView(View v) {
		mBodyLayout.addView(v);
	}

	/**
	 * @param resource
	 *            引入布局id
	 */
	protected View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		mBodyLayout.removeAllViews();
		addView(view);
		return view;
	}

	/**
	 * 隐藏左按钮
	 */
	public void setLeftTopGone() {
		mLeftButton.setVisibility(View.INVISIBLE);
	}

	/**
	 * 隐藏右按钮
	 */
	public void setRightTopGone() {
		mRightButton.setVisibility(View.INVISIBLE);
	}

//	/**
//	 * 隐藏左侧菜单栏
//	 */
//	public void goneLeftView() {
//		// 隐藏左侧菜单
//		LinearLayout slidingTab = (LinearLayout) findViewById(R.id.sliding_tab);
//		Button btn_hide = (Button) findViewById(R.id.btn_hide);
//		Button btn_fill_show = (Button) findViewById(R.id.btn_fill_show);
//		slidingTab.setVisibility(View.GONE);
//		btnShow.setVisibility(View.GONE);
//		setLeftButtonPopupGone();
//		btn_hide.setVisibility(View.GONE);
//		btn_fill_show.setVisibility(View.GONE);
//	}

	/**
	 * 隐藏底部菜单
	 */
	public void goneBottomMenu() {
		((LinearLayout) findViewById(R.id.foot_layout)).setVisibility(View.GONE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == QUIT_CODE) {
			setResult(QUIT_CODE);
			finish();
		}
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

	/** 设置日期 */
	public OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(RemittanceBaseActivity.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					StringBuilder date = new StringBuilder();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth + "")));
					// 为日期赋值
					((TextView) v).setText(String.valueOf(date));
				}
			}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**** 左侧菜单点击事件 **/
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		RemittanceDataCenter.getInstance().clearAllData();
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("crossBorderRemit_1")){
			intent.setClass(RemittanceBaseActivity.this, RemittanceInfoInputActivity.class);
		}
		else if(menuId.equals("crossBorderRemit_2")){
			intent.setClass(RemittanceBaseActivity.this, RemittanceTemplateListActivity.class);
		}
		else if(menuId.equals("crossBorderRemit_3")){
			intent.setClass(RemittanceBaseActivity.this, RemittanceRecordQueryListActivity.class);
		} else if (menuId.equals("crossBorderRemit_4")) {
			intent.setClass(RemittanceBaseActivity.this, OverseasChinaBankRemittanceInfoInputActivity.class);
		}
		context.startActivity(intent);
		return true;
//		RemittanceDataCenter.getInstance().clearAllData();
//		ActivityTaskManager.getInstance().removeAllActivity();
//
//		Intent intent = new Intent();
//		switch (clickIndex) {
//		case REMITTANCE_APPLICATION_FOR_REMITTANCE:
//			intent.setClass(RemittanceBaseActivity.this, RemittanceInfoInputActivity.class);
//			break;
//		case REMITTANCE_TEMPLATE_MANAGEMENT:
//			intent.setClass(RemittanceBaseActivity.this, RemittanceTemplateListActivity.class);
//			break;
//		case REMITTANCE_RECORD_QUERY:
//			intent.setClass(RemittanceBaseActivity.this, RemittanceRecordQueryListActivity.class);
//			break;
//		}
//		startActivity(intent);
	}

	/** 返回主页面 */
	public OnClickListener toMainOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			RemittanceDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};

	/** 返回事件 */
	public OnClickListener backOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
			overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
