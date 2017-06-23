package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 关联账户转账 预约日期执行
 * 
 * @author wangmengmeng
 * 
 */
public class PreDateExeActivity1 extends AccBaseActivity {
	private static final int DATE_PICKER_ID = 1;
	/** 选择日期 */
	private TextView chooseDateTv;
	/** 下一步 */
	private Button nextBtn;

	private View view = null;

	/** 当前系统时间的下一天 */
	private String nextDate;

	private int curYear;
	private int curMonth;
	private int curDate;

	private String executeDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.rela_acc_tran));
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.dept_save_reg_pre_date, null);
		tabcontent.addView(view);
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

		init();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			DatePickerDialog dateDialog = new DatePickerDialog(
					PreDateExeActivity1.this, onDateSetListener, curYear,
					curMonth - 1, curDate);
			return dateDialog;
		default:
			break;
		}
		return null;
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		// newTranBtn.setVisibility(View.VISIBLE);

		chooseDateTv = (TextView) view.findViewById(R.id.dept_choose_date_tv);
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		if (StringUtil.isNullOrEmpty(dateTime)) {
			return;
		}
		String currentDate = dateTime.substring(0, 10);
		nextDate = QueryDateUtils.getOneDayLater(currentDate);
		curYear = Integer.parseInt(nextDate.substring(0, 4));
		curMonth = Integer.parseInt(nextDate.substring(5, 7));
		curDate = Integer.parseInt(nextDate.substring(8, 10));
		chooseDateTv.setText(nextDate);
		chooseDateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_PICKER_ID);
			}
		});
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
			if (monthOfYear + 1 < 10) {// 如果月份小于10月 前面加“0”
				month = "0" + String.valueOf(monthOfYear + 1);
			}
			if (dayOfMonth < 10) {
				day = "0" + String.valueOf(dayOfMonth);
			}
			/** 选择的截止日期 */
			String dateStr = (String.valueOf(year) + "/" + month + "/" + day);
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
			executeDate = chooseDateTv.getText().toString();
			if (QueryDateUtils.compareDate(executeDate, dateTime)) {
				// 结束日期在服务器日期之前
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						BaseDroidApp.getInstanse().getCurrentAct()
								.getString(R.string.compare_date_1));
				return;
			} else {
				
			}
			boolean flag = QueryDateUtils.compareStartThreeMonthDate(
					executeDate, dateTime);
			if (!flag) {
				BaseDroidApp.getInstanse().createDialog(
						null,
						PreDateExeActivity1.this.getResources().getString(
								R.string.execute_day));
				return;
			}
			Map<String, String> userInputMap = TranDataCenter.getInstance()
					.getUserInputMap();
			userInputMap.put(Tran.INPUT_PRE_DATE, executeDate);
			requestForTransferCommissionCharge(ConstantGloble.PB021);
		}
	};

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
		Intent intent0 = new Intent(PreDateExeActivity1.this,
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
}
