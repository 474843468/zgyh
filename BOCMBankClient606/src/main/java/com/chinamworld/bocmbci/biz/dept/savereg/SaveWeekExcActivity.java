package com.chinamworld.bocmbci.biz.dept.savereg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

public class SaveWeekExcActivity extends DeptBaseActivity {
//	private static final String TAG = "SaveWeekExcActivity";
	private static final int START_DATE_PICKER_ID = 1;
	private static final int END_DATE_PICKER_ID = 2;

	private LinearLayout tabcontent;// 主Activity显示

	private View view = null;
	/** 选择日期 */
	private TextView chooseStartDateTv;
	/** 选择日期 */
	private TextView chooseEndDateTv;
	/** 周期 */
	private Spinner weekSpinner;
	/** 上一步 */
//	private Button lastBtn;
	/** 下一步 */
	private Button nextBtn;
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

	/** 起始日期 */
	private String strStartDate = null;
	/** 结束日期 */
	private String strEndDate = null;
	/** 周期 */
	private String strWeek = null;
	/** 执行次数 */
	private String strExecuteTimes = null;
	/** 业务类型 */
	private String businessType = null;
	/** 当前系统时间一天后*/
	private String oneDateLater;
	/** 当前系统时间七天后*/
	private String sevenDateLater;
	
	private int startYear,startMonth,startDay;
	private int endYear,endMonth,endDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);

		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_save_pre_week, null);
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
		StepTitleUtils.getInstance()
				.initTitldStep(
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
	
	/**
	 * 请求系统时间返回
	 */
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		oneDateLater = QueryDateUtils.getOneDayLater(dateTime);
		sevenDateLater = QueryDateUtils.getSevenDayLater(dateTime);
		init();
	};

	private void init() {
		setTopTitle();
		
		newTranBtn.setVisibility(View.VISIBLE);
		
		chooseStartDateTv = (TextView) view
				.findViewById(R.id.dept_choose_start_date_tv);
		startYear = Integer.parseInt(oneDateLater.substring(0, 4));
		startMonth = Integer.parseInt(oneDateLater.substring(5, 7));
		startDay = Integer.parseInt(oneDateLater.substring(8, 10));
		chooseStartDateTv.setText(oneDateLater);
		chooseStartDateTv.setOnClickListener(chooseStartDateClicklistener);

		chooseEndDateTv = (TextView) view
				.findViewById(R.id.dept_choose_end_date_tv);
		endYear = Integer.parseInt(sevenDateLater.substring(0, 4));
		endMonth = Integer.parseInt(sevenDateLater.substring(5, 7));
		endDay = Integer.parseInt(sevenDateLater.substring(8, 10));
		chooseEndDateTv.setText(sevenDateLater);
		chooseEndDateTv.setOnClickListener(chooseEndDateClicklistener);

		weekSpinner = (Spinner) view.findViewById(R.id.dept_week_spinner);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.dept_spinner, LocalData.weekListTrans);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weekSpinner.setAdapter(adapter1);
		weekSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String week = LocalData.weekListTrans.get(position);
				strWeek = LocalData.FrequencyValue.get(week);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				String week = LocalData.weekListTrans.get(0);
				strWeek = LocalData.FrequencyValue.get(week);
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
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				strStartDate = chooseStartDateTv.getText().toString().trim();
				strEndDate = chooseEndDateTv.getText().toString().trim();
				
				if (!QueryDateUtils.compareStartDate(strStartDate, dateTime)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveWeekExcActivity.this.getResources().getString(
									R.string.choose_start_day_info));
					return;
				}
				
				if (!QueryDateUtils.compareEndDate(strEndDate, dateTime)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveWeekExcActivity.this.getResources().getString(
									R.string.choose_end_day_info));
					return;
				}
				boolean flag = QueryDateUtils.compareDate(strStartDate,
						strEndDate);
				if (!flag) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							SaveWeekExcActivity.this.getResources().getString(
									R.string.acc_query_errordate));
					return;
				}
				strExecuteTimes = Integer.toString(QueryDateUtils
						.initExecuteTimes(strStartDate, strEndDate, strWeek));

				intent.putExtra(ConstantGloble.TYPE_FLAG, typeFlag);
				intent.putExtra(Dept.START_DATE, strStartDate);
				intent.putExtra(Dept.END_DATE, strEndDate);
				intent.putExtra(Dept.CYCLE_SELECT, strWeek);
				intent.putExtra(Dept.NEED_COUNT, strExecuteTimes);
				intent.putExtra(ConstantGloble.TYPE_FLAG, typeFlag);
				intent.putExtra(Dept.NEW_NOTIFY_SAVE_TRANSMODE, excuteWayFlag);// 1预约日期执行
				intent.putExtra(Dept.CURRENCY, currencyCode);
				intent.putExtra(Dept.CASHREMIT, cashRemit);
				intent.putExtra(Dept.CD_TERM, saveTime);
				intent.putExtra(Dept.AMOUNT, saveAmount);
				intent.putExtra(Dept.MEMO, attachMessage);
				intent.putExtra(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, promiseWay);
				intent.putExtra(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);

				intent.setClass(SaveWeekExcActivity.this,
						SaveConfirmActivity.class);
				startActivity(intent);
			}
		});

	}

	/**
	 * 选择开始日期监听事件
	 */
	private OnClickListener chooseStartDateClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			showDialog(START_DATE_PICKER_ID);
		}
	};
	/**
	 * 选择结束日期监听事件
	 */
	private OnClickListener chooseEndDateClicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			showDialog(END_DATE_PICKER_ID);
		}
	};
	
	/**
	 * 开始日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
			chooseStartDateTv.setText(dateStr);

		}
	};
	
	/**
	 * 结束日期控件监听
	 */
	DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
			chooseEndDateTv.setText(dateStr);
		}
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_DATE_PICKER_ID:
			DatePickerDialog startDateDialog = new DatePickerDialog(
					SaveWeekExcActivity.this, onStartDateSetListener, startYear,
					startMonth - 1, startDay);
			return startDateDialog;
		case END_DATE_PICKER_ID:
			DatePickerDialog endDateDialog = new DatePickerDialog(
					SaveWeekExcActivity.this, onEndDateSetListener, endYear,
					endMonth - 1, endDay);
			return endDateDialog;
		default:
			break;
		}
		return null;
	}
	
	/**
	 * 获取数据
	 */
	private void aquireData() {
		typeFlag = this.getIntent().getIntExtra("typeFlag", 0);
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
	private void setTopTitle(){
		if(typeFlag == WHOLE_SAVE_BOTTOM){
			setTitle(R.string.save_whole);
		}else if(typeFlag == REGULAR_RANDOM_BOTTOM){
			setTitle(R.string.save_random);
		}else if(typeFlag == NOTIFY_ONE_DAY_BOTTOM){
			setTitle(R.string.save_one_day);
		}else if(typeFlag == NOTIFY_SEVEN_DAY_BOTTOM){
			setTitle(R.string.save_seven_day);
		}
	}
}
