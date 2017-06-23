package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 周期性产品续约协议修改输入页面
 * 
 * @author wangmengmeng
 * 
 */
public class PeriodAgreeEditInputActivity extends BociBaseActivity implements
		OnCheckedChangeListener {
	/** 协议修改输入信息页 */
	private View view;
	/** 协议详情列表 */
	private Map<String, Object> detailMap;
	/** 协议序号 */
	private TextView tv_contractSeq;
	/** 产品系列名称 */
	private TextView tv_serialName;
	/** 币种/钞汇 */
	private TextView tv_curcode;
	/** 协议最大期数 */
	private TextView tv_maxPeriod;
	/** 购买期数 */
	private EditText et_totalPeriod;
	private String totalPeriod;
	/** 已续期数 */
	private TextView tv_period;
	/** 基础金额模式 */
	private TextView tv_amountTypeCode;
	/** 基础金额模式 */
	private RadioGroup rg_amounttype;
	/** 基础金额模式 */
	private String amountType;
	/** 定额 */
	private RadioButton rb_dinge;
	/** 不定额 */
	private RadioButton rb_budinge;
	/** 基础金额 */
	private EditText et_baseAmount_contract;
	private String baseAmount;
	/** 最低预留金额 */
	private EditText et_minAmount_contract;
	private String minAmount;
	/** 最大扣款额金额 */
	private EditText et_maxAmount_contract;
	private String maxAmount;
	/** 基础金额布局 */
	private LinearLayout ll_amountType_base;
	/** 最低预留金额布局 */
	private LinearLayout ll_amountType_min;
	/** 最大扣款额金额布局 */
	private LinearLayout ll_amountType_max;
	/** 是否是周期连续产品 */
	private Map<String, Object> modifyMap;
	/** 是否是周期性连续 */
	private String isPeriodContinue;
	/** 下一步按钮 */
	private Button btn_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_zhouqi));
		// 添加布局
		view = addView(R.layout.boc_period_edit_input);
		// 界面初始化
		init();
	}

	private void init() {
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		modifyMap = BociDataCenter.getInstance().getPeriodModifyMap();
		/** 协议序号 */
		tv_contractSeq = (TextView) view.findViewById(R.id.tv_contractSeq);
		/** 产品系列名称 */
		tv_serialName = (TextView) view.findViewById(R.id.tv_serialName);
		/** 币种/钞汇 */
		tv_curcode = (TextView) view.findViewById(R.id.tv_curcode);
		/** 协议最大期数 */
		tv_maxPeriod = (TextView) view.findViewById(R.id.tv_maxPeriod);
		/** 已续期数 */
		tv_period = (TextView) view.findViewById(R.id.tv_period);
		/** 基础金额模式 */
		tv_amountTypeCode = (TextView) view
				.findViewById(R.id.tv_amountTypeCode);
		ll_amountType_base = (LinearLayout) view
				.findViewById(R.id.amountType_base);
		ll_amountType_min = (LinearLayout) view
				.findViewById(R.id.amountType_min);
		ll_amountType_max = (LinearLayout) view
				.findViewById(R.id.amountType_max);
		// 输入项
		et_totalPeriod = (EditText) view.findViewById(R.id.et_totalPeriod);
		rg_amounttype = (RadioGroup) view.findViewById(R.id.rg_amounttype);
		rb_dinge = (RadioButton) view.findViewById(R.id.rb_dinge);
		rb_budinge = (RadioButton) view.findViewById(R.id.rb_budinge);
		et_baseAmount_contract = (EditText) view
				.findViewById(R.id.et_baseAmount_contract);
		et_minAmount_contract = (EditText) view.findViewById(R.id.et_minAmount);
		et_maxAmount_contract = (EditText) view.findViewById(R.id.et_maxAmount);

		isPeriodContinue = (String) modifyMap
				.get(BocInvt.BOC_MODIFY_PERIODCONTINUE_RES);
		amountType = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_AMOUNTTYPE_RES);
		// 判断是周期连续则基础金额模式不可选择，只可以是定额
		if (!StringUtil.isNull(isPeriodContinue)) {
			if (isPeriodContinue.equals(investTypeSubList.get(0))) {
				// 不连续

				tv_amountTypeCode.setVisibility(View.GONE);
				rg_amounttype.setVisibility(View.VISIBLE);
				rg_amounttype.setOnCheckedChangeListener(this);
				if (amountType.equals(LocalData.orderTimeMap.get(0))) {
					// 定额
					rb_dinge.setChecked(true);
				} else {
					rb_budinge.setChecked(true);
				}

			} else {
				// 连续产品,只可以选择定额
				tv_amountTypeCode.setVisibility(View.VISIBLE);
				rg_amounttype.setVisibility(View.GONE);
				if (amountType.equals(LocalData.orderTimeMap.get(0))) {
					// 定额
					amountType = LocalData.orderTimeMap.get(0);
					ll_amountType_base.setVisibility(View.VISIBLE);
					ll_amountType_min.setVisibility(View.GONE);
					ll_amountType_max.setVisibility(View.GONE);

				} else {
					// 不定额
					amountType = LocalData.orderTimeMap.get(1);
					ll_amountType_base.setVisibility(View.GONE);
					ll_amountType_min.setVisibility(View.VISIBLE);
					ll_amountType_max.setVisibility(View.VISIBLE);
				}
			}

		}

		tv_contractSeq.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_CONTRACTSEQ_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		tv_serialName.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_SERIALNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_serialName);
		// 判断币种
		if (!StringUtil.isNull((String) detailMap
				.get(BocInvt.BOC_AUTO_CURCODE_REQ))) {
			if (LocalData.Currency.get(
					(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ))
					.equals(ConstantGloble.ACC_RMB)) {
				tv_curcode.setText(LocalData.Currency.get((String) detailMap
						.get(BocInvt.BOC_AUTO_CURCODE_REQ)));
			} else {
				tv_curcode.setText(LocalData.Currency.get((String) detailMap
						.get(BocInvt.BOC_AUTO_CURCODE_REQ))
						+ agreeCashRemitMap.get((String) detailMap
								.get(BocInvt.BOC_QUERY_AGREE_CASHREMIT_RES)));
			}
		}
		checkCurrency(
				(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				et_baseAmount_contract);
		checkCurrency(
				(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				et_minAmount_contract);
		checkCurrency(
				(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				et_maxAmount_contract);
		tv_amountTypeCode.setText(LocalData.bociAmountTypeMap.get(amountType));
		tv_maxPeriod.setText((String) modifyMap
				.get(BocInvt.BOC_MODIFY_MAXPERIOD_RES));
		tv_period.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_PERIOD_RES));
		totalPeriod = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_TOTALPERIOD_RES);
		et_totalPeriod.setText(totalPeriod);
		et_totalPeriod.setSelection(totalPeriod.length());
		baseAmount = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_BASEAMOUNT_RES);
		if (!StringUtil.isNull(baseAmount)) {
			et_baseAmount_contract.setText(baseAmount);
		}
		minAmount = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_MINAMOUNT_RES);
		if (!StringUtil.isNull(minAmount)) {
			et_minAmount_contract.setText(minAmount);
		}
		maxAmount = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_MAXAMOUNT_RES);
		if (!StringUtil.isNull(maxAmount)) {
			et_maxAmount_contract.setText(maxAmount);
		}
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 判断
				if (StringUtil.isNull(et_totalPeriod.getText().toString()
						.trim())) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									PeriodAgreeEditInputActivity.this
											.getString(R.string.bocinvt_error_buytotalPeriod));
					return;
				}
				// 以下为验证
				// 购买期数
				RegexpBean rebbuy = new RegexpBean(
						PeriodAgreeEditInputActivity.this
								.getString(R.string.totalPeriod_null),
						et_totalPeriod.getText().toString().trim(), "buyPeriod");
				// 基础金额
				RegexpBean reb = checkJapReg((String) detailMap
						.get(BocInvt.BOC_QUERY_AGREE_PROCUR_RES),
						PeriodAgreeEditInputActivity.this
								.getString(R.string.bocinvt_baseAmount_regex),
						et_baseAmount_contract.getText().toString().trim());
				// 最低预留金额
				RegexpBean reb1 = checkJapReg((String) detailMap
						.get(BocInvt.BOC_QUERY_AGREE_PROCUR_RES),
						PeriodAgreeEditInputActivity.this
								.getString(R.string.bocinvt_minAmount_regex),
						et_minAmount_contract.getText().toString().trim());
				// 最大扣款额金额
				RegexpBean reb2 = checkJapReg((String) detailMap
						.get(BocInvt.BOC_QUERY_AGREE_PROCUR_RES),
						PeriodAgreeEditInputActivity.this
								.getString(R.string.bocinvt_maxAmount_regex),
						et_maxAmount_contract.getText().toString().trim());
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				lists.add(rebbuy);
				if (rg_amounttype.isShown()) {
					if (rb_dinge.isChecked()) {
						lists.add(reb);
					} else {
						lists.add(reb1);
						lists.add(reb2);
					}
				} else {
					if (amountType.equals(LocalData.orderTimeMap.get(0))) {
						lists.add(reb);
					} else {
						lists.add(reb1);
						lists.add(reb2);
					}
				}

				if (RegexpUtils.regexpDate(lists)) {// 校验通过
					// 最大可购买期数
					double remain = Double.valueOf((String) modifyMap
							.get(BocInvt.BOC_MODIFY_MAXPERIOD_RES));
					// 购买期数
					double et_remain = Double.valueOf(et_totalPeriod.getText()
							.toString().trim());
					if (remain < et_remain) {
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										PeriodAgreeEditInputActivity.this
												.getString(R.string.bocinvt_error_remainbuy));
						return;
					}
					baseAmount = et_baseAmount_contract.getText().toString()
							.trim();
					minAmount = et_minAmount_contract.getText().toString()
							.trim();
					maxAmount = et_maxAmount_contract.getText().toString()
							.trim();
					// TODO 保存输入信息
					Map<String, Object> inputMap = new HashMap<String, Object>();
					inputMap.put(
							BocInvt.BOC_MODIFY_RES_CONTRACTSEQ_REQ,
							(String) detailMap
									.get(BocInvt.BOC_QUERY_AGREE_CONTRACTSEQ_RES));
					inputMap.put(BocInvt.BOC_MODIFY_RES_TOTALPERIOD_REQ,
							et_totalPeriod.getText().toString().trim());
					inputMap.put(BocInvt.BOC_MODIFY_RES_AMOUNTTYPECODE_REQ,
							amountType);
					inputMap.put(
							BocInvt.BOC_MODIFY_RES_SERIALNAME_REQ,
							(String) detailMap
									.get(BocInvt.BOC_QUERY_AGREE_SERIALNAME_RES));
					inputMap.put(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ,
							(String) detailMap
									.get(BocInvt.BOC_AUTO_CURCODE_REQ));
					inputMap.put(BocInvt.BOC_MODIFY_RES_XPADCASHREMIT_REQ,
							(String) detailMap
									.get(BocInvt.BOC_QUERY_AGREE_CASHREMIT_RES));
					inputMap.put(BocInvt.BOC_MODIFY_RES_ADDAMOUNT_REQ,
							(String) detailMap
									.get(BocInvt.BOC_QUERY_AGREE_ADDAMOUNT_RES));
					inputMap.put(
							BocInvt.BOC_MODIFY_RES_CONTAMTMODE_REQ,
							(String) detailMap
									.get(BocInvt.BOC_QUERY_AGREE_CONTAMTMODE_RES));
					inputMap.put(BocInvt.BOC_MODIFY_RES_BASEAMOUNT_REQ,
							baseAmount);
					inputMap.put(BocInvt.BOC_MODIFY_RES_MINAMOUNT_REQ,
							minAmount);
					inputMap.put(BocInvt.BOC_MODIFY_RES_MAXAMOUNT_REQ,
							maxAmount);
					inputMap.put(Comm.ACCOUNTNUMBER,detailMap.get(BocInvt.BANCACCOUNT));
					BociDataCenter.getInstance().setInputResultMap(inputMap);
					requestPsnXpadQueryRiskMatch();
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.rb_dinge:
			// 定额
			amountType = LocalData.orderTimeMap.get(0);
			ll_amountType_base.setVisibility(View.VISIBLE);
			ll_amountType_min.setVisibility(View.GONE);
			ll_amountType_max.setVisibility(View.GONE);
			break;
		case R.id.rb_budinge:
			// 不定额
			amountType = LocalData.orderTimeMap.get(1);
			ll_amountType_base.setVisibility(View.GONE);
			ll_amountType_min.setVisibility(View.VISIBLE);
			ll_amountType_max.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	/** 请求查询客户风险等级与产品风险等级是否匹配 */
	public void requestPsnXpadQueryRiskMatch() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYRISKMATCH_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOCINVT_MATCH_SERIALCODE_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_SERIALCODE_RES));
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
			// TODO 进入确认页面
			Intent intent = new Intent(this,
					PeriodAgreeEditConfirmActivity.class);
			startActivity(intent);
		} else if (riskMatch.equals(NOMATCHCAN)) {
			// 1：不匹配但允许交易
			// （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp
					.getInstanse()
					.showErrorDialog(
							PeriodAgreeEditInputActivity.this
									.getString(R.string.bocinvt_error_noriskExceed),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									switch (Integer.parseInt(v.getTag() + "")) {
									case CustomDialog.TAG_SURE:
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										// 确定操作 可以进行购买
										// TODO 进入确认页面
										Intent intent = new Intent(
												PeriodAgreeEditInputActivity.this,
												PeriodAgreeEditConfirmActivity.class);
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
					PeriodAgreeEditInputActivity.this
							.getString(R.string.bocinvt_error_cannotBuy));
			return;
		}

	}
}
