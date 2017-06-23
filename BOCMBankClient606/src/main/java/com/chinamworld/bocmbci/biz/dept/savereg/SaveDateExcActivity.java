package com.chinamworld.bocmbci.biz.dept.savereg;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

public class SaveDateExcActivity extends DeptBaseActivity {

	private static final String TAG = "SaveDateExcActivity";
	private static final int DATE_PICKER_ID = 1;

	private LinearLayout tabcontent;// 主Activity显示
	/** 选择日期 */
	private TextView chooseDateTv;
	/** 上一步 */
//	private Button lastBtn;
	/** 下一步 */
	private Button nextBtn;

	private View view = null;

	/** 存款类型标志 */
	private int typeFlag;
	private String excuteWayFlag;
	/** 币种 */
	private String currencyCode = null;
	/** 钞汇标志 */
	private String cashRemit = null;
	/** 存款金额 */
	private String saveAmount = null;
	/** 存期 */
	private String saveTime = null;
	/** 附言 */
	private String attachMessage = null;
	/** 约定方式 */
	private String promiseWay = null;
	/** 业务类型 */
	private String businessType = null;
	/** 当前系统时间的下一天 */
	private String nextDate;

	private int curYear;
	private int curMonth;
	private int curDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);

		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_save_reg_pre_date, null);
		tabcontent.addView(view);
		
		newTranBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		newTranBtn.setText(this.getResources().getString(R.string.new_save));

		newTranBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						SaveRegularActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// 步骤栏
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.save_confirm_title1),
						this.getResources().getString(
								R.string.save_confirm_title2),
						this.getResources().getString(
								R.string.save_confirm_title3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		// 获取数据
		aquireData();
		// 请求系统时间
		requestSystemDateTime();BiiHttpEngine.showProgressDialog();
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		init();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			DatePickerDialog dateDialog = new DatePickerDialog(
					SaveDateExcActivity.this, onDateSetListener, curYear,
					curMonth - 1, curDate);
			return dateDialog;
		default:
			break;
		}
		return null;
	}

	private void init() {
		setTopTitle();

		newTranBtn.setVisibility(View.VISIBLE);

		chooseDateTv = (TextView) view.findViewById(R.id.dept_choose_date_tv);
		String currentDate = dateTime.substring(0, 10);
		nextDate = QueryDateUtils.getOneDayLater(currentDate);
		curYear = Integer.parseInt(nextDate.substring(0, 4));
		curMonth = Integer.parseInt(nextDate.substring(5, 7));
		curDate = Integer.parseInt(nextDate.substring(8, 10));
		chooseDateTv.setText(nextDate);
		chooseDateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogGloble.i(TAG, "chooseDateClick");
				// 第二个参数为用户选择设置按钮后的响应事件
				// 最后的三个参数为缺省显示的年度，月份，及日期信息
				showDialog(DATE_PICKER_ID);
			}
		});

//		lastBtn = (Button) view.findViewById(R.id.btnLast);
//		lastBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});

		nextBtn = (Button) view.findViewById(R.id.btnNext);
		nextBtn.setOnClickListener(nextBtnListener);
	}

	/**
	 * 日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);
			if(monthOfYear + 1 < 10){//如果月份小于10月 前面加“0”
				month = "0"+String.valueOf(monthOfYear + 1);
			}
			if(dayOfMonth < 10){
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/"
					+ month + "/" + day);
			/** 为EditText赋值 */
			chooseDateTv.setText(dateStr);

		}
	};

	/**
	 * 下一步按钮监听
	 */
	private OnClickListener nextBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String executeDate = chooseDateTv.getText().toString();
			boolean flag = QueryDateUtils.compareStartThreeMonthDate(executeDate,
					dateTime);
			if (!flag) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(SaveDateExcActivity.this.getResources().getString(
						R.string.execute_day));
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.TYPE_FLAG, typeFlag);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, excuteWayFlag);// 1预约日期执行
			intent.putExtra(Dept.CURRENCY, currencyCode);
			intent.putExtra(Dept.CASHREMIT, cashRemit);
			intent.putExtra(Dept.CD_TERM, saveTime);
			intent.putExtra(Dept.AMOUNT, saveAmount);
			intent.putExtra(Dept.MEMO, attachMessage);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, promiseWay);
			intent.putExtra(Dept.EXECUTE_DATE, executeDate);
			intent.putExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);

			intent.setClass(SaveDateExcActivity.this, SaveConfirmActivity.class);
			startActivity(intent);
		}
	};

	/**
	 * 获取数据
	 */
	private void aquireData() {
		typeFlag = this.getIntent().getIntExtra(ConstantGloble.TYPE_FLAG, 0);
		excuteWayFlag = this.getIntent().getStringExtra(
				Dept.NEW_NOTIFY_SAVE_TRANSMODE);
		currencyCode = this.getIntent().getStringExtra(Dept.CURRENCY);
		cashRemit = this.getIntent().getStringExtra(Dept.CASHREMIT);
		saveTime = this.getIntent().getStringExtra(Dept.CD_TERM);
		saveAmount = this.getIntent().getStringExtra(Dept.AMOUNT);
		attachMessage = this.getIntent().getStringExtra(Dept.MEMO);
		promiseWay = this.getIntent().getStringExtra(
				Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		businessType = this.getIntent().getStringExtra(
				Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE);
	}

	/**
	 * 设置顶部title
	 */
	private void setTopTitle() {
		if (typeFlag == WHOLE_SAVE_BOTTOM) {
			setTitle(R.string.save_whole);
		} else if (typeFlag == REGULAR_RANDOM_BOTTOM) {
			setTitle(R.string.save_random);
		} else if (typeFlag == NOTIFY_ONE_DAY_BOTTOM) {
			setTitle(R.string.save_one_day);
		} else if (typeFlag == NOTIFY_SEVEN_DAY_BOTTOM) {
			setTitle(R.string.save_seven_day);
		}
	}
}
