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
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity;
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
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class TransationInfoFragment extends Fragment implements AccChangeListener, AccDetailListnenr, OnAreaSelectListener {

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
	/** 币种列表 */
	private List<String> listBiZhongCode = new ArrayList<String>();
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
	private TextView tv_won_hint,tv_won_payman_hint;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setNeedAccDetailListener(NeedAccDetailListener needDetailListener) {
		this.needDetailListener = needDetailListener;
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (RemittanceBaseActivity)activity;
		super.onAttach(activity);
	}


	
	public void setAccSelectionListener(AccSelectionListener accSelectionListener) {
		this.accSelectionListener = accSelectionListener;
	}


	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.remittance_info_input_transaction, null);
		spBiZhong = (Spinner) mMainView.findViewById(R.id.sp_biZhong);
		etPayNumber = (EditText) mMainView.findViewById(R.id.et_payNumber);
		etToPayeeMessage = (EditText) mMainView.findViewById(R.id.et_toPayeeMessage);
		spPayBiZhong = (Spinner) mMainView.findViewById(R.id.sp_payBiZhong);
		spPayUse = (Spinner) mMainView.findViewById(R.id.sp_payUse);
		etPayUse = (EditText) mMainView.findViewById(R.id.et_payUse);
		tvBillContent = (TextView) mMainView.findViewById(R.id.tv_bill_content);
		tv_won_hint = (TextView) mMainView.findViewById(R.id.tv_won_hint);
		tv_won_payman_hint = (TextView) mMainView.findViewById(R.id.tv_won_payman_hint);

		rbRemit = (RadioButton) mMainView.findViewById(R.id.rb_remit);
		rbBill = (RadioButton) mMainView.findViewById(R.id.rb_bill);
		rgChoose = (RadioGroup) mMainView.findViewById(R.id.rg_ismp);
		rgChoose.setOnCheckedChangeListener(rbChooseLis);
		
		EditTextUtils.setLengthMatcher(getActivity(), etPayNumber, 15);
		EditTextUtils.setLengthMatcher(getActivity(), etToPayeeMessage, 65);
		EditTextUtils.setLengthMatcher(getActivity(), etPayUse, 50);

		identityType = (String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA)).get(Comm.IDENTITYTYPE);
		/** 如果使用外交人员身份证或非居民证件类型，不需要填写汇款用途说明 */
		if (identityType.equals("11") || RemittanceDataCenter.FResident.contains(identityType)) {
			mMainView.findViewById(R.id.ll_payeeUse).setVisibility(View.GONE);
		}
//		RemittanceUtils.initSpinnerView(activity, spPayUse, RemittanceDataCenter.listUseCN);
		RemittanceCurrencyAdapter mAdapter = new RemittanceCurrencyAdapter(activity, RemittanceDataCenter.listUseCN);
		spPayUse.setAdapter(mAdapter);
		initViewZero();
		initViewData();
		return mMainView;
	}

	@Override
	public void accChange(String accountId) {
		if (StringUtil.isNull(accountId)) {
			haveRMB = false;
			initViewZero();
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
			activity.getHttpTools().requestHttp(Acc.QRY_ACC_BALANCE_API, "requestPsnAccountQueryAccountDetailCallBack", params, false);
			activity.getHttpTools().registErrorCode(Acc.QRY_ACC_BALANCE_API,"AccQueryDetailAction.NoSubAccount");
		}
	}

	@Override
	public void detailCallBack(List<Map<String, Object>> detailList) {
		this.detailList = detailList;
		LogGloble.i(TAG, "detailCallBack set detail ->" + detailList.toString());
		initView(detailList);
		initViewData();
	}

	/** 根据取得的账户详情初始化本片段控件 */
	private void initView(final List<Map<String, Object>> detailList) {
		listBiZhongCode.clear();
		List<String> list = new ArrayList<String>();
		list.add("请选择汇款币种");
		if (!StringUtil.isNullOrEmpty(detailList)) {
			haveRMB = false;
			rmbYuE = 0.00;
			for (int i = 0; i < detailList.size(); i++) {
				Map<String, Object> map = detailList.get(i);
				if (map.get(Acc.DETAIL_CURRENCYCODE_RES).equals("001")) {
					haveRMB = true;
					rmbYuE = Double.parseDouble((String) map.get(Acc.DETAIL_AVAILABLEBALANCE_RES));
					continue;
				}
				// 如果列表里已存在该币种或该条为人民币详情（外币跨境汇款不支持汇人民币）
				if (list.contains(LocalData.Currency.get(map.get(Acc.DETAIL_CURRENCYCODE_RES)))) {
					continue;
				} else {
					if (RemittanceDataCenter.currency_NUM.contains(map.get(Acc.DETAIL_CURRENCYCODE_RES))) {
						listBiZhongCode.add((String) map.get(Acc.DETAIL_CURRENCYCODE_RES));
						list.add(LocalData.Currency.get(map.get(Acc.DETAIL_CURRENCYCODE_RES)));
					}
				}
			}
		}
		if (StringUtil.isNullOrEmpty(listBiZhongCode)) {
			accSelectionListener.accSelection(0);
			etPayNumber.setText("");
			spPayUse.setSelection(0);
			BaseDroidApp.getInstanse().showInfoMessageDialog("无可用的汇款货币");
			return;
		}
		
		//币种列表屏蔽林吉特
		if(list.contains("林吉特")) {
			list.remove("林吉特");
			listBiZhongCode.remove("032");
		}
		
//		RemittanceUtils.initSpinnerView(activity, spBiZhong, list);
		RemittanceCurrencyAdapter mAdapter = new RemittanceCurrencyAdapter(activity, list);
		if(mAdapter.isDefault()) {
			spBiZhong.setClickable(true);
			spBiZhong.setBackgroundResource(R.drawable.bg_spinner);
		} else {
			spBiZhong.setClickable(false);
			spBiZhong.setBackgroundResource(R.drawable.bg_spinner_default);
		}
		spBiZhong.setAdapter(mAdapter);
		mapCurrencyCode_cashRemit = getCashRemit(detailList);
		LogGloble.i(TAG, mapCurrencyCode_cashRemit.toString());
		spBiZhong.setOnItemSelectedListener(biZhongSelectedListener);
		
		payBiZhong.clear();
		payBiZhong.add("人民币元");
//		if (haveRMB) {
//			// payBiZhong.add("人民币元");
//		} else {
//			payBiZhong.remove("人民币元");
//		}
		queryBalance();
		RemittanceUtils.initPayeeMoneySpinnerView(activity, spPayBiZhong, payBiZhong);
		
		
		initViewData();
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

						Log.v("BiiHttpEngine",currency+"---PsnQueryNationalTransferLimit");
						LogGloble.v("BiiHttpEngine",currency+"---LogGloble");
						LogGloble.i(TAG,  currency+"---===LogGloble");
						((RemittanceInfoInputActivity) activity)
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
	
	/**
	 * 设置RadioButton状态<br>
	 * 0-都可用，默认选择现汇 1-都可用，默认选择现钞 2-选择现钞不可选 3-选择现汇不可选 4-都未选择不可选
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

	/** 发起汇款流程时为页面填充数据 */
	private void initViewData() {
		if (state == 1) {
			isFirst = true;
			detailMap = RemittanceDataCenter.getInstance().getMapPsnInternationalTransferTemplateDetailQuery();
			String currency = (String) detailMap.get(Remittance.REMITCURRENCYCODE);
			// spBiZhong.setSelection(listBiZhongCode.indexOf(RemittanceDataCenter.currency_NUM.get(RemittanceDataCenter.currency_CHAR.indexOf(currency)))
			// + 1);
			String isLinked = (String) detailMap.get(Remittance.SWIFTACCLINKED);
			if ("true".equals(isLinked)) {
				spBiZhong.setSelection(listBiZhongCode.indexOf(currency) + 1);
			} else {
				spBiZhong.setSelection(0);
			}
			
			etToPayeeMessage.setText((String) detailMap.get(Remittance.REMITFURINFO2PAYEE));
			if (mMainView.findViewById(R.id.ll_payeeUse).getVisibility() == View.VISIBLE) {
				etPayUse.setText((String) detailMap.get(Remittance.REMITTANCEDESCRIPTION));
			}
		}
	}

	/** 获取用户选择的币种代码 */
	public String getCurrency() {
		return currency;
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
				if (currencyCode.equals(detailMap.get(Acc.DETAIL_CURRENCYCODE_RES))) {
					cashRemit.add((String) detailMap.get(Acc.QUERYTRANSFER_ACC_CASHREMIT_REQ));
				}
			}
			map.put(currencyCode, cashRemit);
		}
		return map;
	}

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

	/** 页面控件归零 */
	public void initViewZero() {
		LogGloble.i(TAG, "initViewZero");
		List<String> list1 = new ArrayList<String>();
		list1.add("请选择汇款币种");
		RemittanceCurrencyAdapter mAdapter = new RemittanceCurrencyAdapter(activity, list1);
		spBiZhong.setBackgroundResource(R.drawable.bg_spinner_default);
		spBiZhong.setClickable(false);
		spBiZhong.setAdapter(mAdapter);
		payBiZhong.clear();
		payBiZhong.add("人民币元");
		RemittanceUtils.initSpinnerView(activity, spPayBiZhong, payBiZhong);

		if (!StringUtil.isNullOrEmpty(mapCurrencyCode_cashRemit)) {
			mapCurrencyCode_cashRemit.clear();
		}
		if (!StringUtil.isNullOrEmpty(detailList)) {
			detailList.clear();
		}
		if(!StringUtil.isNullOrEmpty(accountId)){
			this.accountId="";
		}
		queryBalance();
		setRadioButton(4);
		tvBillContent.setVisibility(View.GONE);
	}

	@Override
	public void onAreaSelect(int position) {
		if (position == 1) {
			// 用户在第二个片段选择了澳洲
			mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip1).setVisibility(View.VISIBLE);
		} else if (position == 5) {
			// 用户在第二个片段选择了日本
			mMainView.findViewById(R.id.tip0).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(R.id.tip0).setVisibility(View.GONE);
			mMainView.findViewById(R.id.tip1).setVisibility(View.GONE);
		}
	}

	/** 设置片段状态 0-汇款流程 1-发起汇款 */
	public void setState(int state, boolean isNowNotify) {
		this.state = state;
		if (isNowNotify) {
			initViewData();
		}
	}

	/** 跨境汇款预交易操作，校验字段，准备上送参数 */
	public boolean remittanceConfirm() {
		if (submitRegexp(true)) {
			Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
			Map<String, Object> userInput = RemittanceDataCenter.getInstance().getUserInput();
			params.put(Remittance.REMITCURRENCYCODE, listBiZhongCode.get(spBiZhong.getSelectedItemPosition() - 1));
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
				params.put(Remittance.PAYCUR, listBiZhongCode.get(spBiZhong.getSelectedItemPosition() - 1));
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
			LogGloble.i(TAG, params.toString());
			return true;
		}
		return false;
	}

	/** 校验 */
	private boolean submitRegexp(boolean required) {
		if (StringUtil.isNullOrEmpty(listBiZhongCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("无可用的汇款货币");
			return false;
		}
		if (spBiZhong.getSelectedItemPosition() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请选择汇款币种");
			return false;
		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		String biZhongCode = listBiZhongCode.get(spBiZhong.getSelectedItemPosition() - 1);
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
		if (listBiZhongCode.get(spBiZhong.getSelectedItemPosition() - 1).equals("084")
				|| listBiZhongCode.get(spBiZhong.getSelectedItemPosition() - 1).equals("196")
				|| listBiZhongCode.get(spBiZhong.getSelectedItemPosition() - 1).equals("088")) {
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

	private void getBalance() {
		for (int i = 0; i < detailList.size(); i++) {
			Map<String, Object> map = detailList.get(i);
			if (map.get(Acc.DETAIL_CURRENCYCODE_RES).equals(currency)) {
				String cashRemit = rbBill.isChecked() ? "01" : "02";
				if (map.get(Acc.DETAIL_CASHREMIT_RES).equals(cashRemit)) {
					balance = (String) map.get(Acc.DETAIL_AVAILABLEBALANCE_RES);
					break;
				}
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO
	// --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 汇款币种下拉框选择事件 */
	private OnItemSelectedListener biZhongSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 == 0) {
				currency = "";
				payBiZhong.clear();
				payBiZhong.add("人民币元");
				if (!StringUtil.isNullOrEmpty(mapCurrencyCode_cashRemit)) {
					List<String> mList = getCashRemitList(mapCurrencyCode_cashRemit);
					if (mList.contains("01") && mList.contains("02")) {
						setRadioButton(0);
					} else if (mList.contains("01")) {
						setRadioButton(2);
					} else if (mList.contains("02")) {
						setRadioButton(3);
					} else {
						setRadioButton(4);
					}
				}

				return;
			}
			currency = listBiZhongCode.get(arg2 - 1);
			List<String> cashRemits = mapCurrencyCode_cashRemit.get(currency);
			LogGloble.i(TAG, "biZhongSelectedListener set cashRemit");
			if (cashRemits.size() == 1) {
				if (cashRemits.get(0).equals("01")) {
					setRadioButton(2);
				} else {
					setRadioButton(3);
				}
			} else {
				if (state == 1) {
					if (detailMap.get(Remittance.CASHREMIT).equals("01") && isFirst) {
						isFirst = false;
						setRadioButton(1);
					} else {
						setRadioButton(0);
					}
				} else {
					setRadioButton(0);
				}
			}

			getBalance();
			payBiZhong.clear();
			payBiZhong.add("人民币元");
//			if (haveRMB) {
//				// payBiZhong.add("人民币元");
//			} else {
//				payBiZhong.remove("人民币元");
//			}
			if (spBiZhong.getSelectedItemPosition() != 0) {
				payBiZhong.add(LocalData.Currency.get(listBiZhongCode.get(spBiZhong.getSelectedItemPosition() - 1)));
				
				if(listBiZhongCode.get(spBiZhong.getSelectedItemPosition()-1).equals("084") ) {
					mMainView.findViewById(R.id.tip2).setVisibility(View.VISIBLE);
					mMainView.findViewById(R.id.tip3).setVisibility(View.GONE);
				}else if(listBiZhongCode.get(spBiZhong.getSelectedItemPosition()-1).equals("196")){
					mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
					mMainView.findViewById(R.id.tip3).setVisibility(View.VISIBLE);
				}else{
					mMainView.findViewById(R.id.tip2).setVisibility(View.GONE);
					mMainView.findViewById(R.id.tip3).setVisibility(View.GONE);
				}
			}
					
			RemittanceUtils.initPayeeMoneySpinnerView(activity, spPayBiZhong, payBiZhong);
			
			//当汇款币种选择韩元时 添加提示信息
			String won = spBiZhong.getAdapter().getItem(arg2).toString();
			
			if ("韩元".equals(won)) {
				tv_won_hint.setVisibility(View.VISIBLE);
				tv_won_payman_hint.setVisibility(View.VISIBLE);
			} else {
				tv_won_hint.setVisibility(View.GONE);
				tv_won_payman_hint.setVisibility(View.GONE);
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	/** 单选按钮组合框的监听事件 */
	private OnCheckedChangeListener rbChooseLis = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			getBalance();
			int id = group.getCheckedRadioButtonId();
			RadioButton rb = (RadioButton) activity.findViewById(id);

			if (rb == rbBill) {
				tvBillContent.setVisibility(View.VISIBLE);
			} else {
				tvBillContent.setVisibility(View.GONE);
			}
		}
	};
}
