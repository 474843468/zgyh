package com.chinamworld.bocmbci.biz.infoserve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: NonFixedProductRemindAccountListActivity
 * @Description: 理财产品到期提醒设置——设置页面
 * @author wanbing
 * @date 2013-11-21
 */
public class NonFixedProductRemindSetActivity extends InfoServeBaseActivity {

	private TextView accountTv;
	private TextView currencyTv;
	private TextView fromdateTv;
	private TextView fromtimeTv;
	private EditText beginamtEt;
	private EditText endamtEt;
	private Button btnSubmit;
	private static final int DATE_PICKER_ID = 1;
	private static final int TIME_PICKER_ID = 2;
	private String curDate;
	private String curTime;
	private String optFlag;
	private String accountId;
	private String currency;
	private String beginAmt;
	private String endAmt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.service_non_fixed_product_remind_setting);
		setTitle(R.string.infoserve_daedaozhang_alarm);
		btn_right.setVisibility(View.GONE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();
			}
		});
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化view和数据
	 * @param
	 * @return void
	 */
	private void init() {
		accountTv = (TextView) findViewById(R.id.account_tv);
		currencyTv = (TextView) findViewById(R.id.currency_tv);
		fromdateTv = (TextView) findViewById(R.id.fromdate_tv);
		fromtimeTv = (TextView) findViewById(R.id.fromtime_tv);
		beginamtEt = (EditText) findViewById(R.id.beginamt_et);
		endamtEt = (EditText) findViewById(R.id.endamt_et);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		optFlag = getIntent().getStringExtra(Push.NON_FIXED_OPT_FLAG);
		if (optFlag == null || optFlag.equals("")) {
			return;
		}

		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		accountTv.setText(StringUtil.getForSixForString(accountId));
		currency = getIntent().getStringExtra(Push.NON_FIXED_CURRENCY);
		currencyTv.setText(LocalData.Currency.get(currency));
		if (Push.NON_FIXED_OPT_ADD.equals(optFlag)) {
			// 签约
			// 获取系统时间
			requestSystemDateTime();
			BiiHttpEngine.showProgressDialog();
		} else if (Push.NON_FIXED_OPT_MODIFY.equals(optFlag)) {
			// 修改
			curDate = getIntent().getStringExtra(Push.NON_FIXED_FROMDATE);
			curTime = getIntent().getStringExtra(Push.NON_FIXED_FROMTIME);
			beginAmt = getIntent().getStringExtra(Push.NON_FIXED_BEGINAMT);
			beginamtEt.setText(beginAmt);
			endAmt = getIntent().getStringExtra(Push.NON_FIXED_ENDAMT);
			endamtEt.setText(endAmt);
			setViewData();
		}
		btnSubmit.setOnClickListener(submitClick);
	}

	private void setViewData() {
		fromdateTv.setText(curDate);
		fromdateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 第二个参数为用户选择设置按钮后的响应事件
				// 最后的三个参数为缺省显示的年度，月份，及日期信息
				showDialog(DATE_PICKER_ID);
			}
		});
		fromtimeTv.setText(curTime);
		fromtimeTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_PICKER_ID);
			}
		});
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		curDate = dateTime.substring(0, 10);
		curTime = dateTime.substring(dateTime.length() - 8, dateTime.length());
		setViewData();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		int curYear = Integer.parseInt(curDate.substring(0, 4));
		int curMonth = Integer.parseInt(curDate.substring(5, 7));
		int curDay = Integer.parseInt(curDate.substring(8, 10));
		int hour = Integer.parseInt(curTime.substring(0, 2));
		int minute = Integer.parseInt(curTime.substring(3, 5));
		switch (id) {
		case DATE_PICKER_ID:
			DatePickerDialog dateDialog = new DatePickerDialog(
					NonFixedProductRemindSetActivity.this, onDateSetListener,
					curYear, curMonth - 1, curDay);
			return dateDialog;
		case TIME_PICKER_ID:
			TimePickerDialog timeDialog = new TimePickerDialog(
					NonFixedProductRemindSetActivity.this, onTimeSetListener,
					hour, minute, true);
			return timeDialog;
		default:
			break;
		}
		return null;
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
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			fromdateTv.setText(dateStr);
		}
	};

	/**
	 * 时间控件监听
	 */
	TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
			String hour = String.valueOf(hourOfDay);
			String min = String.valueOf(minute);
			if (hourOfDay < 10 && hour.length() < 2) {
				hour = "0" + hour;
			}
			if (minute < 10 && min.length() < 2) {
				min = "0" + min;
			}
			/** 选择的时间 */
			String timeStr = (hour + ":" + min + ":" + "00");
			/** 为EditText赋值 */
			fromtimeTv.setText(timeStr);
		}

	};

	/**
	 * 提交点击
	 */
	private View.OnClickListener submitClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			curDate = fromdateTv.getText().toString().trim();
			curTime = fromtimeTv.getText().toString().trim();
			beginAmt = beginamtEt.getText().toString().trim();
			endAmt = endamtEt.getText().toString().trim();
			if (beginAmt == null || beginAmt.equals("")) {
				beginAmt = null;
			} else {
				RegexpBean amount = new RegexpBean(getResources().getString(
						R.string.infoserve_daedaozhang_beginamt_label),
						beginAmt, "amount");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(amount);
				if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
					return;
				}
			}
			if (endAmt == null || endAmt.equals("")) {
				endAmt = "50000";
			} else {
				RegexpBean amount = new RegexpBean(getResources().getString(
						R.string.infoserve_daedaozhang_endamt_label), endAmt,
						"amount");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(amount);
				if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
					return;
				} else if (Double.parseDouble(endAmt) < 50000) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									getResources()
											.getString(
													R.string.infoserve_daedaozhang_endamt_error));
					return;
				}
			}
			// 请求token
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		// 签约或修改
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		if (paramsmap != null) {
			// token
			paramsmap.put(Comm.TOKEN_REQ, token);
			// accountId
			paramsmap.put(Comm.ACCOUNT_ID, accountId);
			// 操作类型，增-A，删-D，改-U
			paramsmap.put(Push.NON_FIXED_OPT_FLAG, optFlag);

			paramsmap.put(Push.NON_FIXED_FROMDATE, curDate);
			paramsmap.put(Push.NON_FIXED_FROMDATE, curTime);
			paramsmap.put(Push.NON_FIXED_BEGINAMT, beginAmt);
			paramsmap.put(Push.NON_FIXED_ENDAMT, endAmt);
			paramsmap.put(Push.NON_FIXED_NIGHTSIGN, Push.NON_FIXED_NIGHT_YES);
			paramsmap.put(Push.NON_FIXED_OPT_FLAG, optFlag);
		}
		requestPsnNonFixedProductRemind(null,paramsmap);

	}

	@Override
	public void requestPsnNonFixedProductRemindCallback(Object resultObj) {
		super.requestPsnNonFixedProductRemindCallback(resultObj);
		String msg = "";
		if (Push.NON_FIXED_OPT_ADD.equals(optFlag)) {
			msg = getResources().getString(
					R.string.infoserve_daedaozhang_sign_success_notice);
		} else if (Push.NON_FIXED_OPT_MODIFY.equals(optFlag)) {
			msg = getResources().getString(
					R.string.infoserve_daedaozhang_modify_success_notice);
		}
		BaseDroidApp.getInstanse().showMessageDialog(msg,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
						BaseDroidApp.getInstanse().dismissMessageDialog();
						finish();
					}
				});
	}
}
