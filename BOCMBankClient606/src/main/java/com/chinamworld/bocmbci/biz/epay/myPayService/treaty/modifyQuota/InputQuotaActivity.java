package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.modifyQuota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class InputQuotaActivity extends EPayBaseActivity {

	private View inputQuota;

	private TextView tv_per_quota;
	private TextView tv_addup_quota;
	private TextView tv_cust_quota;
	private EditText et_day_quota;

	private Button bt_next;

	private PubHttpObserver httpObserver;
	private Context treatyContext;

	private String serviceId;
	private String perMaxQuota;
	private String custMaxQuota;
	private String dayMaxQuota;
	private String custMaxQuotaInput;

	private String treatyNum;
	private String merchantAcc;
	private String merchantName;
	private String payAccount;
	private String payAccountType;

	private Map<Object, Object> merchant;

	protected String combinId;

	private String bocNo;

	/** 默认人民币 */
	private String CURRENCY_CODE = "001";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		treatyContext = TransContext.getTreatyTransContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_TREATY);
		inputQuota = LayoutInflater.from(this).inflate(R.layout.epay_treaty_modify_quota_input, null);

		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(inputQuota);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
		initTitleRightButton("关闭", rBtncloseListener);
		hideFoot();
		getTransData();

	}

	private void getTransData() {
		merchant = treatyContext.getMap(TreatyConstants.PUB_FEILD_SELECTED_MERCHANT);

		treatyNum = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID),
				"");
		merchantAcc = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID), "");
		bocNo = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO), "");
		merchantName = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME), "");
		payAccount = EpayUtil.getString(merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO), "");
		payAccountType = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE), "");
		custMaxQuota = EpayUtil.getString(
				merchant.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA), "");
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_queryTreatyQuota("queryTreatyQuotaCallback");
	}

	/**
	 * 查询协议支付系统限额回调方法
	 * 
	 * @param resultObj
	 */
	public void queryTreatyQuotaCallback(Object resultObj) {
		Map<Object, Object> quotaInfo = EpayUtil.getMap(httpObserver.getResult(resultObj));
		serviceId = EpayUtil.getString(quotaInfo.get(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_SERVICE_ID), "");
		perMaxQuota = EpayUtil.getString(quotaInfo.get(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_PER_MAX_QUOTA),
				"");
		dayMaxQuota = EpayUtil.getString(quotaInfo.get(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_DAY_MAX_QUOTA),
				"");
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		tv_per_quota = (TextView) inputQuota.findViewById(R.id.tv_per_quota);
		tv_per_quota.setText(StringUtil.parseStringPattern(perMaxQuota, 2));
		tv_addup_quota = (TextView) inputQuota.findViewById(R.id.tv_addup_quota);
		tv_addup_quota.setText(StringUtil.parseStringPattern(dayMaxQuota, 2));
		tv_cust_quota = (TextView) inputQuota.findViewById(R.id.tv_cust_quota);
		tv_cust_quota.setText(StringUtil.parseStringPattern(custMaxQuota, 2));

		et_day_quota = (EditText) inputQuota.findViewById(R.id.et_day_quota);

		// 币种显示
		TextView currencyView = (TextView) findViewById(R.id.tv_currency);
		currencyView.setText(LocalData.Currency.get(CURRENCY_CODE));

		bt_next = (Button) inputQuota.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkSubmit()) {
					return;
				}
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
		BiiHttpEngine.dissMissProgressDialog();
	}

	private boolean checkSubmit() {
		custMaxQuotaInput = EpayUtil.getString(et_day_quota.getText(), "");
//		BaseDroidApp bdApp = BaseDroidApp.getInstanse();
//		if (StringUtil.isNullOrEmpty(custMaxQuotaInput)) {
////			bdApp.showInfoMessageDialog("请输入自设每日交易限额！");
//			bdApp.showInfoMessageDialog(getText(R.string.set_everyday_limit_no_empty).toString());
//			return false;
//		}
//
//		if (!Pattern.matches("(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0\\.[0]*$)^\\d{1,11}(\\.\\d{1,2})?$", custMaxQuotaInput)) {
////			bdApp.showInfoMessageDialog("自设最多13位数字且不能为0（小数点前最多11位数字，小数点后最多2位数字）");
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.trade_amount_maximum_prompt).toString());
//			return false;
//		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = new RegexpBean("自设每日最高限额", custMaxQuotaInput, "tranAmount");
		lists.add(transAmountReg);
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}

		if (!StringUtil.isNullOrEmpty(dayMaxQuota)) {
			double d_curQuota = Double.valueOf(custMaxQuotaInput);
			double d_sysQuota = Double.valueOf(dayMaxQuota);
			if (d_curQuota > d_sysQuota) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("自设每日最高限额不能超过每日交易累计限额");
				return false;
			}
		}

		return true;
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		treatyContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		httpObserver.setConversationId(conversationId);
		requestGetSecurityFactor(serviceId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BiiHttpEngine.showProgressDialog();
				combinId = baseDroidApp.getSecurityChoosed();
				String combinId = baseDroidApp.getSecurityChoosed();
				HashMap<Object, Object> params = new HashMap<Object, Object>();
				params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_MERCHANT_NO, bocNo);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_AGREEMENT_ID, treatyNum);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_HOLDER_MER_ID, merchantAcc);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_MERCHANT_NAME, merchantName);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_DAILY_QUOTA, custMaxQuota);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_NEW_DAILY_QUOTA, custMaxQuotaInput);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_ACCOUNT_NUMBER, payAccount);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_ACCOUNT_TYPE, payAccountType);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_BANK_DAILY_QUOTA, dayMaxQuota);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_BANK_SINGLE_QUOTA, perMaxQuota);
				params.put(TreatyConstants.METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_CURRENCY, "001");
				httpObserver.req_modifyTreatyMaxQuotaPre(params, "modifyTreatyMaxQuotaPreCallback");
			}
		});
	}

	public void modifyTreatyMaxQuotaPreCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);

		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		
		// TODO 安全因子
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);
				
		List<Object> factorList = EpayUtil.getFactorList(resultMap);

		treatyContext.setData(PubConstants.PUB_FIELD_FACTORLIST, factorList);
		Intent intent = new Intent(this, ConfirmMsgActivity.class);
		intent.putExtra("currency_code", CURRENCY_CODE);
		intent.putExtra("newCustQuota", custMaxQuotaInput);
		intent.putExtra(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_PER_MAX_QUOTA, perMaxQuota);
		intent.putExtra(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_DAY_MAX_QUOTA, dayMaxQuota);
		startActivityForResult(intent, 0);
	}

	@Override
	public void finish() {
		setResult(RESET_DATA);
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
	}

}
