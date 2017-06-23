package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 自动协议修改输入页面
 * 
 * @author wangmengmeng
 * 
 */
public class ZextendAgreeEditInputActivity extends BociBaseActivity implements
		OnCheckedChangeListener {
	/** 协议申请页 */
	private View view;
	/** 协议序号 */
	private TextView tv_contractSeq;
	/** 理财交易账户 */
	private TextView tv_prodNum;
	/** 产品代码 */
	private TextView tv_prodCode;
	/** 产品名称 */
	private TextView tv_prodName;
	/** 认购起点金额 */
	private TextView tv_buyStartingAmount;
	/** 追加认申购起点金额——后 */
	private TextView tv_appendStartingAmount;
	/** 追加认申购起点金额——前 */
	private TextView tv_appending;
	/** 币种 */
	private TextView tv_curCode;
	/** 钞汇标志—人民币 */
	private TextView rmb_submit;
	/** 钞汇选择 */
	private RadioGroup rg_cashRemit;
	/** 现钞 */
	private RadioButton rb_bill;
	/** 现汇 */
	private RadioButton rb_remit;
	/** 投资方式 */
	private TextView tv_investType;
	/** 总期数 */
	private TextView tv_totalPeriod;
	/** 最低限额 */
	private EditText et_minAmount;
	/** 最高限额 */
	private EditText et_maxAmount;
	/** 定投开始日 */
	private TextView tv_investTime;
	/** 协议详情 */
	private Map<String, Object> detailMap;
	/** 下一步 */
	private Button btn_next;
	// 数据
	/** 钞汇 */
	private String cashremit;
	/** 总期数方式 */
	private String period;
	/** 已完成期数 */
	private TextView tv_finish_period;
	/** 最低限额 */
	private String minAmount;
	/** 最高限额 */
	private String maxAmount;
	private String currentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_zidong));
		// 添加布局
		view = addView(R.layout.boc_agree_zidong_edit_input);
		// 界面初始化
		init();
	}

	public void init() {
		currentTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		/** 协议序号 */
		tv_contractSeq = (TextView) view.findViewById(R.id.tv_contractSeq);
		/** 理财交易账户 */
		tv_prodNum = (TextView) view.findViewById(R.id.tv_number);
		/** 产品代码 */
		tv_prodCode = (TextView) view.findViewById(R.id.tv_prodCode);
		/** 产品名称 */
		tv_prodName = (TextView) view.findViewById(R.id.tv_prodName);
		/** 认购起点金额 */
		tv_buyStartingAmount = (TextView) view
				.findViewById(R.id.tv_buyStartingAmount);
		/** 追加认申购起点金额——后 */
		tv_appendStartingAmount = (TextView) view
				.findViewById(R.id.tv_appendStartingAmount);
		/** 追加认申购起点金额——前 */
		tv_appending = (TextView) view.findViewById(R.id.appendStrating);
		/** 币种 */
		tv_curCode = (TextView) view.findViewById(R.id.tv_curCode);
		/** 钞汇标志—人民币 */
		rmb_submit = (TextView) view.findViewById(R.id.rmb_submit);
		/** 钞汇选择 */
		rg_cashRemit = (RadioGroup) view.findViewById(R.id.rg_cashRemit);
		/** 现钞 */
		rb_bill = (RadioButton) view.findViewById(R.id.bill);
		/** 现汇 */
		rb_remit = (RadioButton) view.findViewById(R.id.remit);
		/** 总期数 */
		tv_totalPeriod = (TextView) view.findViewById(R.id.tv_totalPeriod);
		/** 定投开始日 */
		tv_investTime = (TextView) view.findViewById(R.id.tv_investTime);
		tv_finish_period = (TextView) view.findViewById(R.id.tv_finish_period);
		/** 最低限额 */
		et_minAmount = (EditText) view.findViewById(R.id.et_minAmount);
		/** 最高限额 */
		et_maxAmount = (EditText) view.findViewById(R.id.et_maxAmount);
		dateTime = (String) detailMap.get(BocInvt.BOC_EXTEND_VALUEDATE_RES);
		/** 投资方式 */
		tv_investType = (TextView) view.findViewById(R.id.tv_investType);
		tv_investType.setText(investTypeMap.get("1"));
		// 赋值
		tv_contractSeq.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_CONTRACTSEQ_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		tv_prodNum.setText(StringUtil.getForSixForString((String) detailMap
				.get(BocInvt.BANCACCOUNT)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodNum);
		tv_prodCode.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_SERIALCODE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode);
		tv_prodName.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_SERIALNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName);
		tv_buyStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES),
				(String) detailMap.get(BocInvt.BOC_EXTEND_SUBPAMT_RES), 2));
		tv_appendStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES),
				(String) detailMap.get(BocInvt.BOC_EXTEND_ADDAMT_RES), 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_appending);
		tv_curCode.setText(LocalData.Currency.get((String) detailMap
				.get(BocInvt.BOC_EXTEND_PROCUR_RES)));
		// 判断币种
		if (((String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES))
				.equals(ConstantGloble.BOCINVT_CURRENCY_RMB)) {
			// 是人民币
			rmb_submit.setVisibility(View.VISIBLE);
			rmb_submit.setText(ConstantGloble.BOCINVT_DATE_ADD);
			cashremit = agreeCashRemitList.get(0);
			rg_cashRemit.setVisibility(View.GONE);
		} else {
			// 其它的
			rmb_submit.setVisibility(View.GONE);
			rg_cashRemit.setVisibility(View.VISIBLE);
			rg_cashRemit.setOnCheckedChangeListener(this);
			cashremit = (String) detailMap
					.get(BocInvt.BOC_EXTEND_CASHREMIT_RES);
			if (cashremit.equals(agreeCashRemitList.get(1))) {
				// 现钞
				rb_bill.setChecked(true);
			} else {
				rb_remit.setChecked(true);
			}

		}
		period = (String) detailMap.get(BocInvt.BOC_EXTEND_TOTALPERIOD_RES);
		if (!StringUtil.isNull(period)) {
			if (period.equals(NOPERIODSTR)) {
				// 不限期
				tv_totalPeriod.setText(this
						.getString(R.string.bocinvt_noperiod));
			} else {
				tv_totalPeriod.setText(period);
			}
		}
		tv_investTime.setText(QueryDateUtils.getcurrentDate(dateTime));
		tv_finish_period.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIOD_RES));
		minAmount = (String) detailMap.get(BocInvt.BOC_EXTEND_MINAMOUNT_RES);
		maxAmount = (String) detailMap.get(BocInvt.BOC_EXTEND_MAXAMOUNT_RES);
		checkCurrency((String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES),
				et_minAmount);
		checkCurrency((String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES),
				et_maxAmount);
		if (!StringUtil.isNull(minAmount)) {
			et_minAmount.setText(minAmount);
		}
		if (!StringUtil.isNull(maxAmount)) {
			et_maxAmount.setText(maxAmount);
		}
		initListener();
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String curcode = (String) detailMap
						.get(BocInvt.BOC_EXTEND_PROCUR_RES);
				String time = tv_investTime.getText().toString();
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				minAmount = et_minAmount.getText().toString().trim();
				maxAmount = et_maxAmount.getText().toString().trim();
				// 最低限额也可以为0
				RegexpBean reb_minamount = checkJapRegForMin(curcode,
						ZextendAgreeEditInputActivity.this
								.getString(R.string.bocinvt_minAmount_null),
						minAmount);
				lists.add(reb_minamount);
				// 最高限额可以为0
				RegexpBean reb_maxamount = checkJapRegForMin(curcode,
						ZextendAgreeEditInputActivity.this
								.getString(R.string.bocinvt_maxAmount_null),
						maxAmount);
				lists.add(reb_maxamount);
				if (!RegexpUtils.regexpDate(lists)) {// 校验没有通过
					return;
				}

				// 最高必须大于等于最低
				if (Double.valueOf(minAmount) <= Double.valueOf(maxAmount)) {

				} else {
					// 最高限额不能低于最低限额
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									ZextendAgreeEditInputActivity.this
											.getString(R.string.bocinvt_check_minmaxAmount));
					return;
				}
				if (!QueryDateUtils.compareDate(time, currentTime)) {
					// 结束日期在服务器日期之前
				} else {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									ZextendAgreeEditInputActivity.this
											.getString(R.string.bocinvt_check_lastdate));
					return;
				}
				Map<String, String> agreeInputMap = new HashMap<String, String>();
				agreeInputMap.put(BocInvt.BOC_AUTO_CONTRACTSEQ_REQ,
						(String) detailMap
								.get(BocInvt.BOC_EXTEND_CONTRACTSEQ_RES));
				agreeInputMap.put(BocInvt.BOC_AUTO_SERIALCODE_REQ,
						(String) detailMap
								.get(BocInvt.BOC_EXTEND_SERIALCODE_RES));
				agreeInputMap.put(BocInvt.BOC_AUTO_SERIALNAME_REQ,
						(String) detailMap
								.get(BocInvt.BOC_EXTEND_SERIALNAME_RES));
				agreeInputMap.put(BocInvt.BOC_AUTO_CURCODE_REQ,
						(String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES));
				agreeInputMap.put(BocInvt.BOC_AUTO_CASHREMIT_REQ, cashremit);
				agreeInputMap.put(BocInvt.BOC_AUTO_AGREEMENTTYPE_REQ,
						(String) detailMap
								.get(BocInvt.BOC_EXTEND_CONTAMTMODE_RES));
				agreeInputMap.put(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ,
						bocSerListUp.get(0));
				agreeInputMap.put(BocInvt.BOC_AUTO_PERIODTYPE_REQ,
						ConstantGloble.BOCINVT_NULL_STRING);
				agreeInputMap.put(BocInvt.BOC_AUTO_TOTALPERIOD_REQ, period);
				agreeInputMap.put(BocInvt.BOC_AUTO_BASEAMOUNT_REQ,
						ConstantGloble.BOCINVT_NULL_STRING);
				agreeInputMap.put(BocInvt.BOC_AUTO_PERIODSEQ_REQ,
						ConstantGloble.BOCINVT_NULL_STRING);
				agreeInputMap.put(BocInvt.BOC_AUTO_PERIODSEQTYPE_REQ,
						ConstantGloble.BOCINVT_NULL_STRING);
				agreeInputMap.put(BocInvt.BOC_AUTO_LASTDATE_REQ, time);
				agreeInputMap.put(BocInvt.BOC_AUTO_MINAMOUNT_REQ, minAmount);
				agreeInputMap.put(BocInvt.BOC_AUTO_MAXAMOUNT_REQ, maxAmount);
				BociDataCenter.getInstance().setAgreeInputMap(agreeInputMap);
				requestPsnXpadQueryRiskMatch();
			}
		});
		tv_investTime.setOnClickListener(chooseDateClick);
	}

	/** 设置查询日期 */
	OnClickListener chooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					ZextendAgreeEditInputActivity.this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};

	/** 请求查询客户风险等级与产品风险等级是否匹配 */
	public void requestPsnXpadQueryRiskMatch() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYRISKMATCH_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCINVT_MATCH_PRODUCTCODE_REQ,
				detailMap.get(BocInvt.BOC_EXTEND_SERIALCODE_RES));
		paramsmap.put(Comm.ACCOUNTNUMBER,detailMap
				.get(BocInvt.BANCACCOUNT));
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadQueryRiskMatchCallBack");
	}

	/** 请求查询客户风险等级与产品风险等级是否匹配回调 */
	public void requestPsnXpadQueryRiskMatchCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> riskMatchMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(riskMatchMap)) {
			return;
		}
		BociDataCenter.getInstance().setRiskMatchMap(riskMatchMap);
		String riskMatch = (String) riskMatchMap
				.get(BocInvt.BOCINVT_MATCH_RISKMATCH_RES);
		if (riskMatch.equals(MATCH)) {
			// TODO 进入确定页面
			Intent intent = new Intent(ZextendAgreeEditInputActivity.this,
					ZextendAgreeEditConfirmActivity.class);
			startActivity(intent);
		} else if (riskMatch.equals(NOMATCHCAN)) {
			// 1：不匹配但允许交易
			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							ZextendAgreeEditInputActivity.this
									.getString(R.string.bocinvt_error_noriskExceed),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										// 确定操作 进入确定页面
										Intent intent = new Intent(
												ZextendAgreeEditInputActivity.this,
												ZextendAgreeEditConfirmActivity.class);
										startActivity(intent);
										break;
									case CustomDialog.TAG_CANCLE:
										// 取消操作
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();

										break;
									}

								}
							});
		} else if (riskMatch.equals(NOMATCH)) {
			// 2：不匹配且拒绝交易
			// （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					ZextendAgreeEditInputActivity.this
							.getString(R.string.bocinvt_error_cannotBuy));
			return;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.bill:
			// 现钞
			cashremit = agreeCashRemitList.get(1);
			break;
		case R.id.remit:
			// 现汇
			cashremit = agreeCashRemitList.get(2);
			break;
		default:
			break;
		}
	}
}
