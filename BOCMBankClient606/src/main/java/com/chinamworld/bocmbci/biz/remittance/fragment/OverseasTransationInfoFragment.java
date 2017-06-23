package com.chinamworld.bocmbci.biz.remittance.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.adapter.RemittanceCurrencyAdapter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccChangeListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccDetailListnenr;
import com.chinamworld.bocmbci.biz.remittance.interfaces.AccSelectionListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.NeedAccDetailListener;
import com.chinamworld.bocmbci.biz.remittance.interfaces.OnAreaSelectListener;
import com.chinamworld.bocmbci.biz.remittance.utils.RemittanceUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class OverseasTransationInfoFragment extends Fragment implements
		AccChangeListener, AccDetailListnenr, OnAreaSelectListener {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final String TAG = "TransationInfoFragment";
	/** 该片段依附的Activity */
	private RemittanceBaseActivity activity;
	/** 此片段状态 0-汇款流程 1-发起汇款流程 */
	private int state;
	/** 如果是发起汇款流程，模板详情放到这里 */
	private Map<String, Object> detailMap;
	/** 此片段要向此对象注册自己，以便获得得到卡详情的通知 */
	private NeedAccDetailListener needDetailListener;
	/** 扣款账户的选择 */
	private AccSelectionListener accSelectionListener;
	/** 主显示视图 */
	private View mMainView;
	/** 汇款币种 */
	private Spinner spBiZhong;
	/** 汇款币种数据源 */
	private List<String> payBiZhong = new ArrayList<String>();
	/** 现钞现汇的组合 */
	private RadioGroup rgChoose;
	/** 现汇单选按钮 */
	private RadioButton rbRemit;
	/** 现钞单选按钮 */
	private RadioButton rbBill;
	/** 现钞的提示信息 */
	private TextView tvBillContent;
	/** 汇款金额 */
	private EditText etPayNumber;
	/** 给汇款人留言 */
	private EditText etToPayeeMessage;
	/** 付费币种 */
	private Spinner spPayBiZhong;
	/** 汇款用途 */
	private Spinner spPayUse;
	/** 汇款用途详细说明 */
	private EditText etPayUse;
	/** 账户详情数据 */
	private List<Map<String, Object>> detailList;
	/** 账号详情币种代码列表  ------账户详情过来的币种列表*/
	private List<String> listBiZhongCode = new ArrayList<String>();
	/** 汇款币种代码列表  ----------根据收款银行所在地区刷新得汇款币种列表*/
	private List<String> listBiZhongCodeSp = new ArrayList<String>();
	/** 汇款币种列表 */
	private List<String> listBiZhong;
	/** 币种 钞汇对应数据 */
	private Map<String, List<String>> mapCurrencyCode_cashRemit;
	/** 用户选择的账户id */
	private String accountId;
	/** 用户选择的币种 */
	private String currency;
	/** 账户详情里是否有人民币 */
	private boolean haveRMB = false;
	/** 人民币余额 */
	private double rmbYuE;
	/** 登录人证件类型 */
	private String identityType;
	/** 用户选择的汇款币种在账户中的余额 */
	private String balance;
	private boolean isFirst;
	/** 汇款币种选择韩元时，收款币种和收款人留言下面新加的提示信息 */
	private TextView tv_won_hint, tv_won_payman_hint;
	private LinearLayout ll_won_hint;

	/** 汇款币种Adapter */
	private RemittanceCurrencyAdapter mAdapter;

	/**
	 * 现钞 现汇 可用余额
	 */
	private TextView tv_remit_balance,tv_bill_balance,tv_remit_balance_value,tv_bill_balance_value;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	public OverseasTransationInfoFragment(RemittanceBaseActivity activity,
//			NeedAccDetailListener needDetailListener) {
//		this.activity = activity;
//		this.needDetailListener = needDetailListener;
//	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (RemittanceBaseActivity)activity;
		super.onAttach(activity);
	}
	public void setNeedDetailListener(NeedAccDetailListener needDetailListener){
		this.needDetailListener = needDetailListener;
	}

	public void setAccSelectionListener(
			AccSelectionListener accSelectionListener) {
		this.accSelectionListener = accSelectionListener;
	}
	//PopupWindowUtils.getInstance().setOnShowAllTextListener(context, (TextView) convertView.findViewById(R.id.et_templateName));
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(
				R.layout.remittance_info_input_overtransaction, null);
		spBiZhong = (Spinner) mMainView.findViewById(R.id.sp_biZhong);
		etPayNumber = (EditText) mMainView.findViewById(R.id.et_payNumber);
		etToPayeeMessage = (EditText) mMainView
				.findViewById(R.id.et_toPayeeMessage);
		spPayBiZhong = (Spinner) mMainView.findViewById(R.id.sp_payBiZhong);
		spPayUse = (Spinner) mMainView.findViewById(R.id.sp_payUse);
		etPayUse = (EditText) mMainView.findViewById(R.id.et_payUse);
		tvBillContent = (TextView) mMainView.findViewById(R.id.tv_bill_content);
		tv_won_hint = (TextView) mMainView.findViewById(R.id.tv_won_hint);
		ll_won_hint = (LinearLayout) mMainView.findViewById(R.id.ll_won_hint);
		tv_won_payman_hint = (TextView) mMainView
				.findViewById(R.id.tv_won_payman_hint);

		tv_remit_balance = (TextView) mMainView.findViewById(R.id.tv_remit_balance);
		tv_bill_balance = (TextView) mMainView.findViewById(R.id.tv_bill_balance);
		tv_remit_balance_value = (TextView) mMainView.findViewById(R.id.tv_remit_balance_value);
		tv_bill_balance_value = (TextView) mMainView.findViewById(R.id.tv_bill_balance_value);

		//添加气泡
		PopupWindowUtils.getInstance().setOnShowAllTextListener(activity, tv_remit_balance_value);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(activity, tv_bill_balance_value);

		rbRemit = (RadioButton) mMainView.findViewById(R.id.rb_remit);
		rbBill = (RadioButton) mMainView.findViewById(R.id.rb_bill);
		rgChoose = (RadioGroup) mMainView.findViewById(R.id.rg_ismp);
		rgChoose.setOnCheckedChangeListener(rbChooseLis);

		EditTextUtils.setLengthMatcher(getActivity(), etPayNumber, 15);
		EditTextUtils.setLengthMatcher(getActivity(), etToPayeeMessage, 65);
		EditTextUtils.setLengthMatcher(getActivity(), etPayUse, 50);

		identityType = (String) ((Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(Comm.IDENTITYTYPE);
		/** 如果使用外交人员身份证或非居民证件类型，不需要填写汇款用途说明 */
		if (identityType.equals("11")
				|| RemittanceDataCenter.FResident.contains(identityType)) {
			mMainView.findViewById(R.id.ll_payeeUse).setVisibility(View.GONE);
		}

		RemittanceCurrencyAdapter mpAdapter = new RemittanceCurrencyAdapter(
				activity, RemittanceDataCenter.listUseCN);
		spPayUse.setAdapter(mpAdapter);
		initViewZero();
		//initViewData();
		return mMainView;
	}

	/** 设置片段状态 0-汇款流程 1-发起汇款 */
	public void setState(int state, boolean isNowNotify) {
		this.state = state;
		if (isNowNotify) {
			initViewData();
		}
	}

	public void initViewZero() {

		etPayNumber.setText("");

		if (null == listBiZhong) {
			listBiZhong = new ArrayList<String>();
			listBiZhong.add("请选择汇款币种");
		} else {
			listBiZhong.clear();
			listBiZhong.add("请选择汇款币种");
		}

		if (listBiZhongCodeSp.size()>0) {
			listBiZhongCodeSp.clear();
		}

		for (int i = 0; i < RemittanceDataCenter.currency_over_NUM.size(); i++) {
			String currency = LocalData.Currency
					.get(RemittanceDataCenter.currency_over_NUM.get(i));
			listBiZhong.add(currency);
			listBiZhongCodeSp.add(RemittanceDataCenter.currency_over_NUM.get(i));
		}
		mAdapter = new RemittanceCurrencyAdapter(
				activity, listBiZhong);
		spBiZhong.setBackgroundResource(R.drawable.bg_spinner);
		spBiZhong.setClickable(true);
		spBiZhong.setAdapter(mAdapter);
		spBiZhong.setOnItemSelectedListener(biZhongSelectedListener);

		payBiZhong.clear();
		payBiZhong.add("人民币元");
		RemittanceUtils.initSpinnerView(activity, spPayBiZhong, payBiZhong);

		//setRadioButton(0);
		setBalanceVisibility(0);
		queryBalance();

	}

	public void initViewZeroNoSp() {
		etPayNumber.setText("");
		payBiZhong.clear();
		payBiZhong.add("人民币元");
		RemittanceUtils.initSpinnerView(activity, spPayBiZhong, payBiZhong);
		setRadioButton(0);
		setBalanceVisibility(0);
		spBiZhong.setSelection(0);

		//请求接口前清空 账户详情汇款币种列表集合 账户无存单信息
		listBiZhongCode.clear();
	}


	private int position = -1;

	/** 发起汇款流程时为页面填充数据 */
	private void initViewData() {
		if (state == 1) {

			//根据模板过来得数据来设置汇款币种列表
			//setspBiZhong( RemittanceDataCenter.getInstance().getmapPayeeBankOver());
			setspBiZhongModel(RemittanceDataCenter.getInstance().getmapPayeeBankOver());

			//根据收款银行所在地区设置提示警示信息
			String region = (String) RemittanceDataCenter.getInstance().getmapPayeeBankOver().get("region");
			int position = Integer.parseInt(region);
			setMessage(position);

			detailMap = RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery();
			String currency = (String) detailMap.get(Remittance.REMITCURRENCYCODE);

//			spBiZhong.setSelection(listBiZhongCodeSp.indexOf(currency) + 1,false);

			String cashRemit = (String) detailMap.get(Remittance.CASHREMIT);
			String isLinked = (String) detailMap.get(Remittance.SWIFTACCLINKED);

			if ("true".equals(isLinked)) {

				String payCurrency = LocalData.Currency.get(currency);

				//LogGloble.i("OverseasTransationInfoFragment","赵政强 -------- 模板过来数据重新提交");
				payBiZhong.clear();
				payBiZhong.add("人民币元");
				payBiZhong.add(payCurrency);
				RemittanceUtils.initPayeeMoneySpinnerView(activity, spPayBiZhong, payBiZhong);

				boolean isContain = listBiZhongCode.contains(currency);

				//模板带过来是现钞
				if ("01".equals(cashRemit)){

					//1-都可用，默认选择现钞
					setRadioButton(1);
					LogGloble.i("100","initViewData-----01");
					if (isContain) {
						List<String> cashRemits = mapCurrencyCode_cashRemit.get(currency);
						if (cashRemits.size() == 1) {

							if (cashRemits.get(0).equals("01") && Float.parseFloat(getBalancecashRemit(currency,"01"))>0) {
								//模板带过来是现钞 并且账户里面也只有现钞  2 现钞可用 现汇不可用
								setBalanceVisibility(2);
								//tv_bill_balance_value.setText(getBalancecashRemit(currency,"01"));

								tv_bill_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"01"),2));
								tvBillContent.setVisibility(View.GONE);
								etPayNumber.setEnabled(true);
								balance = getBalancecashRemit(currency,"01");


							} else {
								//0 现钞 现汇可用余额字段都不可用
								setBalanceVisibility(0);
								tvBillContent.setVisibility(View.VISIBLE);
								tvBillContent.setText("账户内无相应外币现钞");
								etPayNumber.setEnabled(false);

							}

						} else if(cashRemits.size() == 2){
							// 现钞 现汇都有可用余额 3现汇现钞都可用
							setBalanceVisibility(3);
//							tv_remit_balance_value.setText(getBalancecashRemit(currency,"02"));
//							tv_bill_balance_value.setText(getBalancecashRemit(currency,"01"));

							tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));
							tv_bill_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"01"),2));

							tvBillContent.setVisibility(View.GONE);
							etPayNumber.setEnabled(true);
							balance = getBalancecashRemit(currency,"01");

							LogGloble.i("100","initViewData--555---01");

						}
					} else {
						//0 现钞 现汇可用余额字段都不可用
						setBalanceVisibility(0);
						tvBillContent.setVisibility(View.VISIBLE);
						tvBillContent.setText("账户内无相应外币现钞");
						etPayNumber.setEnabled(false);
					}

					//模板带过来是现汇
				} else {
					//0-都可用，默认选择现汇
					setRadioButton(0);

					if (isContain){
						List<String> cashRemits = mapCurrencyCode_cashRemit.get(currency);
						if (cashRemits.size() == 1) {
							//11yue17
							if (cashRemits.get(0).equals("02") && Float.parseFloat(getBalancecashRemit(currency,"02"))>0) {
								//模板带过来是现汇 并且账户里面也只有现汇 1现汇可用 现钞不可用
								setBalanceVisibility(1);
								//tv_bill_balance_value.setText(getBalancecashRemit(currency,"02"));

								tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));

								tvBillContent.setVisibility(View.GONE);
								etPayNumber.setEnabled(true);
								balance = getBalancecashRemit(currency,"02");

							} else {
								//0 现钞 现汇可用余额字段都不可用
								setBalanceVisibility(0);
								tvBillContent.setVisibility(View.VISIBLE);
								tvBillContent.setText("账户内无相应外币现汇");
								etPayNumber.setEnabled(false);

							}
						} else if(cashRemits.size() == 2){

							// 现钞 现汇都有可用余额  3现汇现钞都可用
							setBalanceVisibility(3);
//							tv_remit_balance_value.setText(getBalancecashRemit(currency,"02"));
//							tv_bill_balance_value.setText(getBalancecashRemit(currency,"01"));

							tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));
							tv_bill_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"01"),2));

							tvBillContent.setVisibility(View.GONE);
							etPayNumber.setEnabled(true);
							balance = getBalancecashRemit(currency,"02");

						}

					} else {
						//0 现钞 现汇可用余额字段都不可用
						setBalanceVisibility(0);
						tvBillContent.setVisibility(View.VISIBLE);
						tvBillContent.setText("账户内无相应外币现汇");
						etPayNumber.setEnabled(false);
					}
				}
			} else {
				spBiZhong.setSelection(0);
			}

			etToPayeeMessage.setText((String) detailMap.get(Remittance.REMITFURINFO2PAYEE));
			if (mMainView.findViewById(R.id.ll_payeeUse).getVisibility() == View.VISIBLE) {
				etPayUse.setText((String) detailMap.get(Remittance.REMITTANCEDESCRIPTION));
			}


		}
	}

	/** 根据取得的账户详情初始化本片段控件 */
	private void initView(final List<Map<String, Object>> detailList) {

		//11月11日
		//setRadioButton(0);
		setBalanceVisibility(0);
		spBiZhong.setSelection(0,false);

		if (!StringUtil.isNullOrEmpty(detailList)) {

			haveRMB = false;
			rmbYuE = 0.00;

			listBiZhongCode.clear();
			for (int i = 0; i < detailList.size(); i++) {

				Map<String, Object> map = detailList.get(i);
				if (map.get(Acc.DETAIL_CURRENCYCODE_RES).equals("001")) {
					haveRMB = true;
					rmbYuE = Double.parseDouble((String) map.get(Acc.DETAIL_AVAILABLEBALANCE_RES));

					//11月11日
					continue;
				} else if (! RemittanceDataCenter.currency_over_NUM.contains(map.get(Acc.DETAIL_CURRENCYCODE_RES))) {
					//11月11日
					continue;
				}
				listBiZhongCode.add((String) map.get("currencyCode"));
			}
		}

		if (StringUtil.isNullOrEmpty(listBiZhongCode)) {
			accSelectionListener.accSelection(0);
			etPayNumber.setText("");
			spPayUse.setSelection(0);
			BaseDroidApp.getInstanse().showInfoMessageDialog("无可用的汇款货币");
			return;
		}

		if (this.detailList == null) {
			this.detailList = detailList;
		} else {
			this.detailList.clear();
			this.detailList = detailList;
		}

		//Log.i("BiiHttpEngine", listBiZhongCode.toString()+"------");

		payBiZhong.clear();
		payBiZhong.add("人民币元");
		RemittanceUtils.initPayeeMoneySpinnerView(activity, spPayBiZhong,payBiZhong);

		mapCurrencyCode_cashRemit = getCashRemit(detailList);

		queryBalance();

		LogGloble.i("100","queryBalance()-----01");
		//11yue23
		initViewData();
	}

	@Override
	public void onAreaSelect(int position) {
		setMessage(position);
	}

	@Override
	public void detailCallBack(List<Map<String, Object>> detailList) {
		LogGloble.i("100","detailCallBack()-----01");
		initView(detailList);

	}

	@Override
	public void accChange(String accountId) {

		if (StringUtil.isNull(accountId)) {
			haveRMB = false;
			initViewZeroNoSp();
			return;
		}

		this.accountId = accountId;
		detailList = RemittanceDataCenter.getInstance().getAccDetail();
		if (!StringUtil.isNullOrEmpty(detailList)) {
			initView(detailList);
		} else {
			BaseHttpEngine.showProgressDialog();
			BaseHttpEngine.dissmissCloseOfProgressDialog();
			needDetailListener.setNeedDetailView(this);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Comm.ACCOUNT_ID, accountId);
			activity.getHttpTools().requestHttp(Acc.QRY_ACC_BALANCE_API,
					"requestPsnAccountQueryAccountDetailCallBack", params,
					false);
			activity.getHttpTools().registErrorCode(Acc.QRY_ACC_BALANCE_API,
					"AccQueryDetailAction.NoSubAccount");
		}
	}

	// private void getBalance() {
	// for (int i = 0; i < detailList.size(); i++) {
	// Map<String, Object> map = detailList.get(i);
	// if (map.get(Acc.DETAIL_CURRENCYCODE_RES).equals(currency)) {
	// String cashRemit = rbBill.isChecked() ? "01" : "02";
	// if (map.get(Acc.DETAIL_CASHREMIT_RES).equals(cashRemit)) {
	// balance = (String) map.get(Acc.DETAIL_AVAILABLEBALANCE_RES);
	// break;
	// }
	// }
	// }
	// }

	/** 获取用户选择的币种代码 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * 设置RadioButton状态<br>
	 * 0-都可用，默认选择现汇 1-都可用，默认选择现钞 2-选择现钞不可选 3-选择现汇不可选 4-都未选择不可选
	 *
	 * @param state
	 */
	private void setRadioButton(int state) {
		rbBill.setEnabled(false);
		rbRemit.setEnabled(false);

		switch (state) {
		case 0:
			rbBill.setEnabled(true);
			rbRemit.setEnabled(true);
			rbBill.setChecked(true);
			rbRemit.setChecked(true);
			break;
		case 1:
			rbBill.setEnabled(true);
			rbRemit.setEnabled(true);
			rbRemit.setChecked(true);
			rbBill.setChecked(true);
			break;
		case 2:
			rbRemit.setChecked(true);
			rbBill.setChecked(true);
			break;
		case 3:
			rbBill.setChecked(true);
			rbRemit.setChecked(true);
			break;
		case 4:
			rbBill.setChecked(false);
			rbRemit.setChecked(false);
			break;
		}
	}

	public void setspBiZhong(Map<String, Object> map) {

		state = 0;
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) map.get("supportCurrencyList");

		if (list.size() <= 0) {
			return;
		}

		setBalanceVisibility(0);
		etPayNumber.setText("");
		etToPayeeMessage.setText("");

		if (listBiZhong != null) {
			listBiZhong.clear();
		} else {
			listBiZhong = new ArrayList<String>();
		}

		if (listBiZhongCodeSp.size()>0) {
			listBiZhongCodeSp.clear();
		}
		listBiZhong.add("请选择汇款币种");

		for (int i = 0; i < list.size(); i++) {
			listBiZhong.add(LocalData.Currency.get(list.get(i)));
			listBiZhongCodeSp.add(list.get(i));
		}

		if (mAdapter == null) {
			mAdapter = new RemittanceCurrencyAdapter(
					activity, listBiZhong);
			spBiZhong.setBackgroundResource(R.drawable.bg_spinner);
			spBiZhong.setClickable(true);
			spBiZhong.setAdapter(mAdapter);

			spBiZhong.setOnItemSelectedListener(biZhongSelectedListener);
			spBiZhong.setSelection(0,false);
		} else {
			mAdapter.notifyDataSetChanged();
			spBiZhong.setSelection(0,false);
		}

	}

	private void setspBiZhongModel(Map<String, Object> map) {

		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) map.get("supportCurrencyList");

		if (list.size() <= 0) {
			return;
		}

		setBalanceVisibility(0);
		etPayNumber.setText("");
		etToPayeeMessage.setText("");

		if (listBiZhong != null) {
			listBiZhong.clear();
		} else {
			listBiZhong = new ArrayList<String>();
		}

		if (listBiZhongCodeSp.size()>0) {
			listBiZhongCodeSp.clear();
		}
		listBiZhong.add("请选择汇款币种");

		for (int i = 0; i < list.size(); i++) {
			listBiZhong.add(LocalData.Currency.get(list.get(i)));
			listBiZhongCodeSp.add(list.get(i));
		}

		position = listBiZhongCodeSp.indexOf(currency) + 1 ;

		detailMap = RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery();
		String currency = (String) detailMap.get(Remittance.REMITCURRENCYCODE);
		    RemittanceCurrencyAdapter mAdapter = new RemittanceCurrencyAdapter(
					activity, listBiZhong);
			spBiZhong.setBackgroundResource(R.drawable.bg_spinner);
			spBiZhong.setClickable(true);
			spBiZhong.setAdapter(mAdapter);

		spBiZhong.setOnItemSelectedListener(biZhongSelectedListener);

		spBiZhong.setSelection(listBiZhongCodeSp.indexOf(currency) + 1,false);

	}

	/** 汇款币种下拉框选择事件 */
	private OnItemSelectedListener biZhongSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			if (position == arg2) {
				position = arg2;
				return;
			}
			position = arg2;
			//只要重新选择汇款币种 就重新输入汇款金额
			etPayNumber.setText("");

			setRadioButton(0);
			if (arg2 == 0) {
				currency = "";
				payBiZhong.clear();
				payBiZhong.add("人民币元");
				setBalanceVisibility(0);
				return;
			}

			currency = listBiZhongCodeSp.get(arg2 - 1);

			Log.i("BiiHttpEngine", listBiZhongCode.toString()+"------");
			Log.i("100", listBiZhongCode.toString()+"------");
			boolean isContain = listBiZhongCode.contains(currency);
			if (isContain) {
				List<String> cashRemits = mapCurrencyCode_cashRemit
						.get(currency);
				if (cashRemits.size() == 1) {
					if (cashRemits.get(0).equals("01")) {
						setBalanceVisibility(0);
						tvBillContent.setVisibility(View.VISIBLE);
						tvBillContent.setText("账户内无相应外币现汇");
						etPayNumber.setEnabled(false);
					} else {
						setBalanceVisibility(1);
						//tv_remit_balance_value.setText(getBalancecashRemit(currency,"02"));
						tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));
						tvBillContent.setVisibility(View.GONE);
						etPayNumber.setEnabled(true);
						balance = getBalancecashRemit(currency,"02");

					}
				} else if (cashRemits.size() == 2) {

					setBalanceVisibility(3);
//					tv_remit_balance_value.setText(getBalancecashRemit(currency,"02"));
//					tv_bill_balance_value.setText(getBalancecashRemit(currency,"01"));

					tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));
					tv_bill_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"01"),2));

					tvBillContent.setVisibility(View.GONE);
					etPayNumber.setEnabled(true);
					balance = getBalancecashRemit(currency,"02");
				}
			} else {
				setBalanceVisibility(0);
				if (listBiZhongCode.size()>0){
					tvBillContent.setVisibility(View.VISIBLE);
					tvBillContent.setText("账户内无相应外币现汇");
				} else {
					tvBillContent.setVisibility(View.GONE);
				}
				etPayNumber.setEnabled(false);
			}

			// getBalance();
			payBiZhong.clear();
			payBiZhong.add("人民币元");

			if (spBiZhong.getSelectedItemPosition() != 0) {
				payBiZhong.add(LocalData.Currency.get(listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition() - 1)));

				if(listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition()-1).equals("084") ) {
					mMainView.findViewById(R.id.tip2).setVisibility(View.VISIBLE);
					mMainView.findViewById(R.id.tip3).setVisibility(View.GONE);
				}else if(listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition()-1).equals("196")){
					mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
					mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
				}else{
					mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
					mMainView.findViewById(R.id.tip3).setVisibility(View.GONE);
				}
			}

			RemittanceUtils.initPayeeMoneySpinnerView(activity, spPayBiZhong, payBiZhong);

			// 当汇款币种选择韩元时 添加提示信息
			String won = spBiZhong.getAdapter().getItem(arg2).toString();

			if ("韩元".equals(won)) {
				ll_won_hint.setVisibility(View.VISIBLE);

				tv_won_payman_hint.setVisibility(View.VISIBLE);
			} else {
				ll_won_hint.setVisibility(View.GONE);

				tv_won_payman_hint.setVisibility(View.GONE);
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	/** 获取账户的钞汇信息 */
	private List<String> getCashRemitList(Map<String, List<String>> mMap) {
		List<String> mList = new ArrayList<String>();
		for (String curreyCode : listBiZhongCode) {
			if (curreyCode.equals("001")) {
				continue;
			}
			mList.addAll(mMap.get(curreyCode));
		}
		return mList;
	}

	/** 获取币种钞汇对应数据 [币种代码[现钞，现汇]]格式 */
	private Map<String, List<String>> getCashRemit(List<Map<String, Object>> detailList) {

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String currencyCode : listBiZhongCode) {
			if (currencyCode.equals("001")) {
				continue;
			}

			List<String> cashRemit = new ArrayList<String>();
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, Object> detailMap = detailList.get(i);
				if (currencyCode.equals(detailMap
						.get(Acc.DETAIL_CURRENCYCODE_RES))) {
					cashRemit.add((String) detailMap
							.get(Acc.QUERYTRANSFER_ACC_CASHREMIT_REQ));
				}
			}
			map.put(currencyCode, cashRemit);
		}
		return map;
	}

	/** 单选按钮组合框的监听事件 */
	private OnCheckedChangeListener rbChooseLis = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// getBalance();
			int id = group.getCheckedRadioButtonId();
			RadioButton rb = (RadioButton) activity.findViewById(id);
			int position = spBiZhong.getSelectedItemPosition();
			//11yue17
			etPayNumber.setText("");
			// 选中状态为现钞时
			if (rb == rbBill) {

				if (position == 0) {
					return;
				} else if (position > 0) {
					currency = listBiZhongCodeSp.get(position - 1);
					boolean isContain = listBiZhongCode.contains(currency);
					if (isContain) {
						List<String> cashRemits = mapCurrencyCode_cashRemit
								.get(currency);
						if (cashRemits.size() == 1) {
							if (cashRemits.get(0).equals("01") && Float.parseFloat(getBalancecashRemit(currency,"01"))>0) {
								setBalanceVisibility(2);
								//tv_bill_balance_value.setText(getBalancecashRemit(currency,"01"));
								tv_bill_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"01"),2));
								etPayNumber.setEnabled(true);
								tvBillContent.setVisibility(View.GONE);
								balance = getBalancecashRemit(currency,"01");
							} else {
								tvBillContent.setVisibility(View.VISIBLE);
								tvBillContent.setText("账户内无相应外币现钞");
								etPayNumber.setEnabled(false);

							}
						} else if (cashRemits.size() == 2) {

							setBalanceVisibility(3);
//							tv_bill_balance_value.setText(getBalancecashRemit(currency,"01"));
//							tv_remit_balance_value.setText(getBalancecashRemit(currency,"02"));
							tv_bill_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"01"),2));
							tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));
							etPayNumber.setEnabled(true);
							tvBillContent.setVisibility(View.GONE);
							balance = getBalancecashRemit(currency,"01");
						}
					} else {

						setBalanceVisibility(0);
						etPayNumber.setEnabled(false);

						if (listBiZhongCode.size()>0) {
							tvBillContent.setVisibility(View.VISIBLE);
							tvBillContent.setText("账户内无相应外币现钞");

						}else {
							tvBillContent.setVisibility(View.GONE);
						}
					}

				}

			} else {
           // 选中状态为现汇时
				if (position == 0) {
					return;
				} else if (position > 0) {
					currency = listBiZhongCodeSp.get(position - 1);

					boolean isContain = listBiZhongCode.contains(currency);
					if (isContain) {
						List<String> cashRemits = mapCurrencyCode_cashRemit
								.get(currency);
						if (cashRemits.size() == 1) {
							if (cashRemits.get(0).equals("01")) {
								setBalanceVisibility(0);
								tvBillContent.setVisibility(View.VISIBLE);
								tvBillContent.setText("账户内无相应外币现汇");
								etPayNumber.setEnabled(false);
							} else {
								//11yue17
								if(Float.parseFloat(getBalancecashRemit(currency,"02"))>0) {
									setBalanceVisibility(1);
									//tv_bill_balance_value.setText(getBalancecashRemit(currency,"02"));
									tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));
									etPayNumber.setEnabled(true);
									tvBillContent.setVisibility(View.GONE);
									balance = getBalancecashRemit(currency,"02");
								} else {
									setBalanceVisibility(0);
									tvBillContent.setVisibility(View.VISIBLE);
									tvBillContent.setText("账户内无相应外币现汇");
									etPayNumber.setEnabled(false);
								}


							}
						} else if (cashRemits.size() == 2) {


							setBalanceVisibility(3);
//							tv_bill_balance_value.setText(getBalancecashRemit(currency,"01"));
//							tv_remit_balance_value.setText(getBalancecashRemit(currency,"02"));
							tv_bill_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"01"),2));
							tv_remit_balance_value.setText(StringUtil.parseStringCodePattern(currency,getBalancecashRemit(currency,"02"),2));
							etPayNumber.setEnabled(true);
							tvBillContent.setVisibility(View.GONE);
							balance = getBalancecashRemit(currency,"02");

						}
					} else {

						setBalanceVisibility(0);
						etPayNumber.setEnabled(false);

						if (listBiZhongCode.size()>0) {
							tvBillContent.setVisibility(View.VISIBLE);
							tvBillContent.setText("账户内无相应外币现汇");
							etPayNumber.setText("");

						} else {
							tvBillContent.setVisibility(View.GONE);
						}

					}

				}
			}
		}
	};

	/** 跨境汇款预交易操作，校验字段，准备上送参数 */
	public boolean remittanceConfirm() {
		if (submitRegexp(true)) {
			Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
			Map<String, Object> userInput = RemittanceDataCenter.getInstance().getUserInput();
			params.put(Remittance.REMITCURRENCYCODE, listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition() - 1));
			params.put(Remittance.CASHREMIT, rbBill.isChecked() ? "01" : "02");
			params.put(Remittance.REMITAMOUNT, StringUtil.splitStringwithCode((String) params.get(Remittance.REMITCURRENCYCODE), etPayNumber.getText().toString().trim(), 2));
			params.put(Remittance.REMITFURINFO2PAYEE, (etToPayeeMessage.getText().toString().trim()).toUpperCase());
			userInput.put(Remittance.REMITFURINFO2PAYEE, etToPayeeMessage.getText().toString().trim());
			params.put(Remittance.FEEMODE, "SHA");
			if (state == 1) {
				params.put(Remittance.OPTION, "3");
				params.put(Remittance.TEMPLATEID, detailMap.get(Remittance.TEMPLATEID));
				params.put(Remittance.ROUTEID, detailMap.get(Remittance.ROUTEID));
			} else {
				params.put(Remittance.OPTION, "1");
				params.put(Remittance.TEMPLATEID, "");
				params.put(Remittance.ROUTEID, "");
			}

			if (spPayBiZhong.getSelectedItemPosition() == 0) {
				params.put(Remittance.PAYCUR, "001");
			} else {
				params.put(Remittance.PAYCUR, listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition() - 1));
			}

			if (identityType.equals("11")) {
				params.put(Remittance.REMITTANCEINFO, "232000");
				params.put(Remittance.REMITTANCEDESCRIPTION, "别处未覆盖的政府货物和服务");
			} else if (RemittanceDataCenter.FResident.contains(identityType)) {
				params.put(Remittance.REMITTANCEINFO, "822030");
				params.put(Remittance.REMITTANCEDESCRIPTION, "非居民向境外付款");
			} else {
				params.put(Remittance.REMITTANCEINFO, RemittanceDataCenter.listUseCode.get(spPayUse.getSelectedItemPosition()));
				params.put(Remittance.REMITTANCEDESCRIPTION, (etPayUse.getText().toString().trim()).toUpperCase());
				userInput.put(Remittance.REMITTANCEDESCRIPTION, etPayUse.getText().toString().trim());
			}

			//默认送空，若为集团内跨境汇款，则送1
			params.put("isBOCPayeeBank", "1");

			return true;
		}
		return false;
	}

	/** 校验 */
	private boolean submitRegexp(boolean required) {
		if (StringUtil.isNullOrEmpty(listBiZhong)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("无可用的汇款货币");
			return false;
		}
		if (spBiZhong.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择汇款币种");
			return false;
		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		String biZhongCode = listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition() - 1);
		if (biZhongCode.equals("027") || biZhongCode.equals("088")) {
			// 如果付款币种为日元或者韩元
			if (onlyRegular(required, etPayNumber.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYNUMBER_CN, etPayNumber.getText().toString().trim(), RemittanceContent.PAYACCNUMBER);
				lists.add(name);
			}
		} else {
			if (onlyRegular(required, etPayNumber.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYNUMBER_CN, etPayNumber.getText().toString().trim(), RemittanceContent.PAYNUMBER);
				lists.add(name);
			}
		}
		if (listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition() - 1).equals("084")
				|| listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition() - 1).equals("196")
				|| listBiZhongCodeSp.get(spBiZhong.getSelectedItemPosition() - 1).equals("088")) {
			// 币种为泰铢/卢布，给收款人留言为必填项  新添加韩元
			if (onlyRegular(required, etToPayeeMessage.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.TOPAYEEMESSAGE_CN, etToPayeeMessage.getText().toString().trim(), RemittanceContent.TOPAYEEMESSAGE);
				lists.add(name);
			}
		} else {
			if (onlyRegular(!required, etToPayeeMessage.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.TOPAYEEMESSAGE_CN, etToPayeeMessage.getText().toString().trim(), RemittanceContent.TOPAYEEMESSAGE);
				lists.add(name);
			}
		}
		if (RemittanceDataCenter.FResident.contains(identityType)
				|| identityType.equals("11")) {

		} else {
			if (onlyRegular(required, etPayUse.getText().toString().trim())) {
				RegexpBean name = new RegexpBean(RemittanceContent.PAYUSE_CN,
						etPayUse.getText().toString().trim(),
						RemittanceContent.PAYUSE);
				lists.add(name);
			}
		}

		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		BigDecimal ba;
		if(!StringUtil.isNullOrEmpty(balance)){
			ba=new BigDecimal(balance);
		}else{
			ba=new BigDecimal(0);
		}
		BigDecimal pn = new BigDecimal(etPayNumber.getText().toString().trim());
		if (ba.compareTo(pn) < 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("汇款金额(" + pn + ")大于账户可用余额(" + ba + ")请重新输入汇款金额。");
			return false;
		}

		Log.i("100",ba+"----");
		Log.i("100",pn+"====----");

		if (spPayBiZhong.getSelectedItemPosition() == 0) {
			if (!haveRMB) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("付费所用人民币账户余额不足，请确认");
				return false;
			} else if (rmbYuE == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("付费所用人民币账户余额不足，请确认");
				return false;
			}
		}

		if (RemittanceDataCenter.FResident.contains(identityType)
				|| identityType.equals("11")) {

		} else {
			if (spPayUse.getSelectedItemPosition() == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择汇款用途");
				return false;
			}
		}

		if (RemittanceDataCenter.FResident.contains(identityType)
				|| identityType.equals("11")) {

		} else {
			String temp = null;
			Pattern p = Pattern.compile("[\u4E00-\u9FA5]+");
			Matcher m = p.matcher(etPayUse.getText().toString().trim());
			while (m.find()) {
				temp = m.group(0);
				break;
			}
			if (StringUtil.isNull(temp)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("汇款用途详细说明请至少输入一个中文字符，不支持全角字符和回车的录入，最大长度50个字符");
				return false;
			}
		}

		return true;
	}

	/** 只作正则校验 */
	private boolean onlyRegular(Boolean required, String content) {
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	/** 查询剩余额度 */
	private void queryBalance() {
		// 查询剩余额度链接
		mMainView.findViewById(R.id.tv_querySurplusLimit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (StringUtil.isNull(accountId)) {
							BaseDroidApp.getInstanse().showInfoMessageDialog(
									"请选择扣款账户");
							return;
						}
						if (StringUtil.isNull(currency)) {
							// BaseDroidApp.getInstanse().showInfoMessageDialog("请选择汇款币种");
							// return;
							currency = "014";
						}
						//Log.v("BiiHttpEngine",currency+"over---PsnQueryNationalTransferLimit");
						((OverseasChinaBankRemittanceInfoInputActivity) activity)
								.setLimitFlag(0);
						BaseHttpEngine.showProgressDialog();
						Map<String, Object> params = new HashMap<String, Object>();
						params.put(Remittance.SWIFTACCOUNTID, accountId);
						params.put(Acc.QUERYTRANSFER_ACC_CURRENCY_REQ, currency);
						activity.getHttpTools().requestHttp(
								Remittance.PSNQUERYNATIONALTRANSFERLIMIT,
								"requestPsnQueryNationalTransferLimitCallBack",
								params, false);
					}
				});
	}

	private void setBalanceVisibility(int state) {
		switch (state) {
			case 0:
				tv_remit_balance.setVisibility(View.INVISIBLE);
				tv_bill_balance.setVisibility(View.INVISIBLE);
				tv_remit_balance_value.setVisibility(View.INVISIBLE);
				tv_bill_balance_value.setVisibility(View.INVISIBLE);
				break;
			case 1:
				tv_remit_balance.setVisibility(View.VISIBLE);
				tv_remit_balance_value.setVisibility(View.VISIBLE);
				tv_bill_balance.setVisibility(View.INVISIBLE);
				tv_bill_balance_value.setVisibility(View.INVISIBLE);

				break;
			case 2:
				tv_remit_balance.setVisibility(View.INVISIBLE);
				tv_remit_balance_value.setVisibility(View.INVISIBLE);
				tv_bill_balance.setVisibility(View.VISIBLE);
				tv_bill_balance_value.setVisibility(View.VISIBLE);
				break;

			case 3:
				tv_remit_balance.setVisibility(View.VISIBLE);
				tv_remit_balance_value.setVisibility(View.VISIBLE);
				tv_bill_balance.setVisibility(View.VISIBLE);
				tv_bill_balance_value.setVisibility(View.VISIBLE);
				break;

		}
	}

	private String getBalancecashRemit(String currencyCode,String cashRemit) {

		for (int i = 0; i < this.detailList.size(); i++) {
			String currency = (String) this.detailList.get(i).get("currencyCode");
			String remit = (String) this.detailList.get(i).get("cashRemit");

			if (currencyCode.equals(currency) && remit.equals(cashRemit)) {

				String availableBalance = (String) this.detailList.get(i).get("availableBalance");
				return availableBalance;

			} else {
				continue;
			}
		}
		return null;
	}


	private void setMessage(int position) {
		if (position == 1) {
			// 用户在第二个片段选择了澳洲
			mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip1).setVisibility(View.VISIBLE);
		} else if (position == 5) {
			// 用户在第二个片段选择了日本
			mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		} else {
			//LogGloble.i("100",position+"---09---27---000");
			mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		}
	}

}
