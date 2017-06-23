package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 关联账户转账 预约周期执行
 * 
 * @author wangmengmeng
 * 
 */
public class PrePeriodExeActivity1 extends AccBaseActivity {
	private static final int START_DATE_PICKER_ID = 1;
	private static final int END_DATE_PICKER_ID = 2;

	private View view = null;
	/** 选择日期 */
	private TextView chooseStartDateTv;
	/** 选择日期 */
	private TextView chooseEndDateTv;
	/** 周期 */
	private Spinner weekSpinner;
	/** 下一步 */
	private Button nextBtn;
	/** 起始日期 */
	private String strStartDate = null;
	/** 结束日期 */
	private String strEndDate = null;
	/** 周期 */
	private String strWeek = null;
	/** 执行次数 */
	private String strExecuteTimes = null;
	/** 当前系统时间一天后 */
	private String oneDateLater;
	/** 当前系统时间七天后 */
	private String sevenDateLater;

	private int startYear, startMonth, startDay;
	private int endYear, endMonth, endDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(this.getString(R.string.rela_acc_tran));
		view = addView(R.layout.dept_save_pre_week);
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
		aquireData();
		init();
	}

	/**
	 * 获取数据
	 */
	private void aquireData() {
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		oneDateLater = QueryDateUtils.getOneDayLater(dateTime);
		sevenDateLater = QueryDateUtils.getSevenDayLater(dateTime);
	}

	/**
	 * 初始化控件
	 */
	private void init() {
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
				R.layout.custom_spinner_item, LocalData.weekListTrans);
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
		nextBtn = (Button) view.findViewById(R.id.btnNext);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				strStartDate = chooseStartDateTv.getText().toString().trim();
				strEndDate = chooseEndDateTv.getText().toString().trim();

				if (QueryDateUtils.compareDate(strStartDate, dateTime)) {
					// 起始日期在服务器日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							BaseDroidApp.getInstanse().getCurrentAct()
									.getString(R.string.choose_day_info));
					return;
				}

				if (!QueryDateUtils
						.compareStartDate(strStartDate, dateTime)) {
					BaseDroidApp.getInstanse().createDialog(
							null,
							PrePeriodExeActivity1.this.getResources()
									.getString(R.string.choose_start_day_info));
					return;
				}
				
				if (QueryDateUtils.compareDate(strEndDate, dateTime)) {
					// 结束日期在服务器日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							BaseDroidApp.getInstanse().getCurrentAct()
									.getString(R.string.choose_end_day_info1));
					return;
				}

				if (!QueryDateUtils.compareEndDate(strEndDate, dateTime)) {
					BaseDroidApp.getInstanse().createDialog(
							null,
							PrePeriodExeActivity1.this.getResources()
									.getString(R.string.choose_end_day_info));
					return;
				}
				boolean flag = QueryDateUtils.compareDate(strStartDate,
						strEndDate);
				if (!flag) {
					BaseDroidApp.getInstanse().createDialog(
							null,
							PrePeriodExeActivity1.this.getResources()
									.getString(R.string.acc_query_errordate));
					return;
				}
				strExecuteTimes = Integer.toString(QueryDateUtils
						.initExecuteTimes(strStartDate, strEndDate, strWeek));
				Map<String, String> userInputMap = TranDataCenter.getInstance()
						.getUserInputMap();
				userInputMap
						.put(Tran.INPUT_PRE_PERIOD_START_DATE, strStartDate);
				userInputMap.put(Tran.INPUT_PRE_PERIOD_END_DATE, strEndDate);
				userInputMap.put(Tran.INPUT_PRE_PERIOD_WEEK, strWeek);
				userInputMap.put(Tran.INPUT_PRE_PERIOD_EXECUTE_TIMES,
						strExecuteTimes);

				requestForTransferCommissionCharge(ConstantGloble.PB021);
			}
		});
	}

	/**
	 * 行内手续费试算返回
	 * 
	 * @param resultObj
	 */
	@Override
	public void requestForTransferCommissionChargeCallBack(Object resultObj) {
		super.requestForTransferCommissionChargeCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent0 = new Intent(PrePeriodExeActivity1.this,
				RelConfirmInfoActivity1.class);
		startActivity(intent0);
	}

	/**
	 * 手续费试算异常拦截
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Tran.TRANSFER_COMMISSIONCHARGE_API.equals(biiResponseBody
					.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {// 非会话超时错误拦截
								Intent intent0 = new Intent(BaseDroidApp
										.getInstanse().getCurrentAct(),
										RelConfirmInfoActivity1.class);
								startActivity(intent0);
							}
						}
					}
					return true;
				}
				return false;// 没有异常
			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常

		return super.httpRequestCallBackPre(resultObj);
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
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
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
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
			/** 为EditText赋值 */
			chooseEndDateTv.setText(dateStr);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_DATE_PICKER_ID:
			DatePickerDialog startDateDialog = new DatePickerDialog(
					PrePeriodExeActivity1.this, onStartDateSetListener,
					startYear, startMonth - 1, startDay);
			return startDateDialog;
		case END_DATE_PICKER_ID:
			DatePickerDialog endDateDialog = new DatePickerDialog(
					PrePeriodExeActivity1.this, onEndDateSetListener, endYear,
					endMonth - 1, endDay);
			return endDateDialog;
		default:
			break;
		}
		return null;
	}

}
