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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
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
 * 定时定额协议修改输入页面
 * 
 * @author wangmengmeng
 * 
 */
public class DextendAgreeEditInputActivity extends BociBaseActivity implements
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
	/** 总期数方式 */
	private RadioGroup rg_period;
	/** 限期 */
	private RadioButton rb_period;
	/** 不限期 */
	private RadioButton rb_noperiod;
	// 限期时显示
	private LinearLayout ll_totalPeriod;
	/** 总期数 */
	private EditText et_totalPeriod;
	// 定时定额显示
	/** 定投类型 */
	private Spinner sp_timeInvestType;
	/** 定投金额 */
	private EditText et_redeemAmount;
	private TextView tv_redeempre;
	/** 定投频率 */
	private EditText et_timeInvestRate;
	/** 频率单位 */
	private Spinner sp_timeInvestRate;
	/** 定投开始日 */
	private TextView tv_investTime;
	/** 协议详情 */
	private Map<String, Object> detailMap;
	/** 下一步 */
	private Button btn_next;
	// 数据
	/** 钞汇 */
	private String cashremit;
	/** 定投类型 */
	private String timeInvestType;
	/** 定投频率 */
	private String timeInvestRate;
	/** 定投频率单位 */
	private String timeInvestRateFlag;
	/** 总期数方式 */
	private String period;
	/** 定投金额 */
	private String redeemAmount;
	/** 已完成期数 */
	private TextView tv_finish_period;
	private boolean isfirst = true;
	private String currentTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_dingshi));
		// 添加布局
		view = addView(R.layout.boc_agree_ding_edit_input);
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
		/** 总期数方式 */
		rg_period = (RadioGroup) view.findViewById(R.id.rg_period);
		/** 限期 */
		rb_period = (RadioButton) view.findViewById(R.id.period);
		/** 不限期 */
		rb_noperiod = (RadioButton) view.findViewById(R.id.noperiod);
		// 限期时显示
		ll_totalPeriod = (LinearLayout) view.findViewById(R.id.ll_totalPeriod);
		/** 总期数 */
		et_totalPeriod = (EditText) view.findViewById(R.id.et_totalPeriod);
		// 定时定额显示
		/** 定投类型 */
		sp_timeInvestType = (Spinner) view.findViewById(R.id.sp_timeInvestType);
		/** 定投金额 */
		et_redeemAmount = (EditText) view.findViewById(R.id.et_redeemAmount);
		tv_redeempre = (TextView) view.findViewById(R.id.tv_redeempre);
		/** 定投频率 */
		et_timeInvestRate = (EditText) view
				.findViewById(R.id.et_timeInvestRate);
		/** 频率单位 */
		sp_timeInvestRate = (Spinner) view.findViewById(R.id.sp_timeInvestRate);
		/** 定投开始日 */
		tv_investTime = (TextView) view.findViewById(R.id.tv_investTime);
		tv_finish_period = (TextView) view.findViewById(R.id.tv_finish_period);
		spinnerInit();
		dateTime = (String) detailMap.get(BocInvt.BOC_EXTEND_VALUEDATE_RES);
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
		if (!StringUtil.isNull((String) detailMap
				.get(BocInvt.BOC_EXTEND_PROCUR_RES))) {
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
		}

		rg_period.setOnCheckedChangeListener(this);
		period = (String) detailMap.get(BocInvt.BOC_EXTEND_TOTALPERIOD_RES);
		if (!StringUtil.isNull(period)) {
			if (period.equals(NOPERIODSTR)) {
				rb_noperiod.setChecked(true);
			} else {
				rb_period.setChecked(true);
				et_totalPeriod.setText(period);
				if (period.length() <= 2) {
					et_totalPeriod.setSelection(period.length());
				}
			}
		}
		timeInvestType = (String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIODTYPE_RES);
		if (!StringUtil.isNull(timeInvestType)) {
			if (timeInvestType.equals(investTypeSubList.get(0))) {
				// 购买
				sp_timeInvestType.setSelection(0);
			} else {
				sp_timeInvestType.setSelection(1);
			}
		}
		timeInvestRateFlag = (String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIODSEQTYPE_RES);
		if (!StringUtil.isNull(timeInvestRateFlag)) {
			if (timeInvestRateFlag.equals(timeInvestRateFlagSubList.get(0))) {
				sp_timeInvestRate.setSelection(0);
			} else if (timeInvestRateFlag.equals(timeInvestRateFlagSubList
					.get(1))) {
				sp_timeInvestRate.setSelection(1);
			} else if (timeInvestRateFlag.equals(timeInvestRateFlagSubList
					.get(2))) {
				sp_timeInvestRate.setSelection(2);
			} else {
				sp_timeInvestRate.setSelection(3);
			}
		}
		timeInvestRate = (String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIODSEQ_RES);
		if (!StringUtil.isNull(timeInvestRate)) {
			et_timeInvestRate.setText(timeInvestRate);
			et_timeInvestRate.setSelection(timeInvestRate.length());
		}
		tv_investTime.setText(QueryDateUtils.getcurrentDate(dateTime));
		tv_finish_period.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIOD_RES));
		checkCurrency((String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES),
				et_redeemAmount);
		redeemAmount = (String) detailMap
				.get(BocInvt.TRANSAMOUNT);
		if (!StringUtil.isNull(redeemAmount)) {
			et_redeemAmount.setText(redeemAmount);
		}
		initListener();
	}

	private void spinnerInit() {
		/** 定投类型 */
		ArrayAdapter<ArrayList<String>> adapter2 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, timeInvestTypeList);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_timeInvestType.setAdapter(adapter2);
		sp_timeInvestType
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						timeInvestType = investTypeSubList.get(position);
						if (position == 0) {
							tv_redeempre.setText(DextendAgreeEditInputActivity.this
									.getString(R.string.bocinvt_redeemAmount));
							et_redeemAmount
									.setHint(ConstantGloble.BOCINVT_NULL_STRING);
						} else {
							tv_redeempre.setText(DextendAgreeEditInputActivity.this
									.getString(R.string.bocinvt_redeemAmount_1));
							et_redeemAmount.setHint(DextendAgreeEditInputActivity.this
									.getString(R.string.bocinvt_redeemAmount_hint));
						}
						if (isfirst) {
							isfirst = false;
						} else {
							et_redeemAmount
									.setText(ConstantGloble.BOCINVT_NULL_STRING);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						timeInvestType = investTypeSubList.get(0);
						tv_redeempre.setText(DextendAgreeEditInputActivity.this
								.getString(R.string.bocinvt_redeemAmount));
					}
				});
		/** 定投频率 */
		ArrayAdapter<ArrayList<String>> adapter3 = new ArrayAdapter(this,
				R.layout.custom_spinner_item, timeInvestRateFlagList);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_timeInvestRate.setAdapter(adapter3);
		sp_timeInvestRate
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						timeInvestRateFlag = timeInvestRateFlagSubList
								.get(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						timeInvestRateFlag = timeInvestRateFlagSubList.get(0);
					}
				});
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (rb_noperiod.isChecked()) {
					// 不限期
				} else {
					// 限期——验证
					period = et_totalPeriod.getText().toString().trim();
				}
				String time = tv_investTime.getText().toString();
				// 总期数——定时定额总期数请输入以1到9开头的数字，最大长度2个字符
				RegexpBean reb_ding = new RegexpBean(
						DextendAgreeEditInputActivity.this
								.getString(R.string.bocinvt_totalPeriod_null),
						period, "totalPeriod");
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				// 定时定额——总期数请输入以1到9开头的数字，最大长度2个字符
				if (rb_noperiod.isChecked()) {
					// 不限期
				} else {
					// 限期——验证
					lists.add(reb_ding);
				}

				// 验证
				/** 定投金额 */
				redeemAmount = et_redeemAmount.getText().toString().trim();
				if (timeInvestType.equals(investTypeSubList.get(0))) {
					// 购买——定投金额
					RegexpBean reb_amount = checkJapReg(
							(String) detailMap
									.get(BocInvt.BOC_EXTEND_PROCUR_RES),
							DextendAgreeEditInputActivity.this
									.getString(R.string.bocinvt_redeemAmount_null),
							redeemAmount);
					lists.add(reb_amount);
				} else {
					// 赎回——定赎份额
					RegexpBean reb_amount_1 = new RegexpBean(
							DextendAgreeEditInputActivity.this
									.getString(R.string.bocinvt_redeemAmount_1_null),
							redeemAmount, "minAmount2");
					lists.add(reb_amount_1);
				}
				/** 定投频率 */
				timeInvestRate = et_timeInvestRate.getText().toString().trim();
				RegexpBean reb_rate = new RegexpBean(
						DextendAgreeEditInputActivity.this
								.getString(R.string.bocinvt_timeInvestRate_null),
						timeInvestRate, "totalPeriod");
				lists.add(reb_rate);

				if (!RegexpUtils.regexpDate(lists)) {// 校验没有通过
					return;
				}
					if (!QueryDateUtils.compareDate(time, currentTime)) {
						// 结束日期在服务器日期之前
					} else {
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										DextendAgreeEditInputActivity.this
												.getString(R.string.bocinvt_check_lastdate));
						return;
					}

				// // TODO 进入确认页面——保存条件
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
				agreeInputMap.put(BocInvt.BOC_AUTO_CASHREMIT_REQ,
						cashremit);
				agreeInputMap.put(BocInvt.BOC_AUTO_AGREEMENTTYPE_REQ,
						(String) detailMap
								.get(BocInvt.BOC_EXTEND_CONTAMTMODE_RES));
				agreeInputMap.put(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ,
						bocSerListUp.get(0));
				agreeInputMap.put(BocInvt.BOC_AUTO_PERIODTYPE_REQ,
						timeInvestType);
				agreeInputMap.put(BocInvt.BOC_AUTO_TOTALPERIOD_REQ, period);
				agreeInputMap
						.put(BocInvt.BOC_AUTO_BASEAMOUNT_REQ, redeemAmount);
				agreeInputMap.put(BocInvt.BOC_AUTO_PERIODSEQ_REQ,
						timeInvestRate);
				agreeInputMap.put(BocInvt.BOC_AUTO_PERIODSEQTYPE_REQ,
						timeInvestRateFlag);
				agreeInputMap.put(BocInvt.BOC_AUTO_LASTDATE_REQ, time);
				agreeInputMap.put(BocInvt.BOC_AUTO_MINAMOUNT_REQ,
						ConstantGloble.BOCINVT_NULL_STRING);
				agreeInputMap.put(BocInvt.BOC_AUTO_MAXAMOUNT_REQ,
						ConstantGloble.BOCINVT_NULL_STRING);
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
					DextendAgreeEditInputActivity.this,
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
			Intent intent = new Intent(DextendAgreeEditInputActivity.this,
					DextendAgreeEditConfirmActivity.class);
			startActivity(intent);
		} else if (riskMatch.equals(NOMATCHCAN)) {
			// 1：不匹配但允许交易
			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							DextendAgreeEditInputActivity.this
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
												DextendAgreeEditInputActivity.this,
												DextendAgreeEditConfirmActivity.class);
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
					DextendAgreeEditInputActivity.this
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
		case R.id.period:
			// 限期
			ll_totalPeriod.setVisibility(View.VISIBLE);
			break;
		case R.id.noperiod:
			// 不限期
			ll_totalPeriod.setVisibility(View.GONE);
			period = NOPERIODSTR;
			break;
		default:
			break;
		}
	}
}
