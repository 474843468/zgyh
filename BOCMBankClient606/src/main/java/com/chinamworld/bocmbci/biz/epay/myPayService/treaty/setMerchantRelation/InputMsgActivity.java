package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.setMerchantRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.adapter.AccountSpinnerAdapter;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class InputMsgActivity extends EPayBaseActivity {

	private View inputMsg;

	private LinearLayout ll_agreement;

	private TextView tv_merchant_name;
	private TextView tv_per_quota;
	private TextView tv_addup_quota;
	private TextView tv_agreement_checked;
	private TextView tv_agreement_title;

	private EditText et_merchant_id;
	private EditText et_day_quota;

	private Spinner s_accounts;

	private Button bt_next;

	private PubHttpObserver httpObserver;
	private Context treatyContext;

	private String merchantName;
	private String perQuota;
	private String addUpQuota;
	private String merchantId;
	private String dayQuota;
	private String accountId;
	private String accountType;
	private String accountNumber;
	private String accNickname;
	private boolean isReadAndAllow;

	private Map<Object, Object> selectedMerchant;

	private List<String> allowAccTypes;

	private List<Object> allowAccounts;
	private String[] allowAccountsDis;
	private Map<Object, Object> account;

	private String serviceId;

	private String bocNo;

	private String tag = "InputMsgActivity";

	private int excuteType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_TREATY);
		treatyContext = TransContext.getTreatyTransContext();
		inputMsg = LayoutInflater.from(this).inflate(R.layout.epay_treaty_add_input, null);

		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(inputMsg);
		super.onCreate(savedInstanceState);

		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "修改限额", "确认信息",
		// "修改成功" });
		getTransData();
	}

	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		excuteType = getIntent().getIntExtra("excuteType", 0);
		allowAccTypes = new ArrayList<String>();
		selectedMerchant = treatyContext.getMap(PubConstants.CONTEXT_FIELD_SELECTED_MERCHANT);
		// 是否支持借记卡
		boolean isDebitCard = PublicTools.getInt(
				selectedMerchant.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_IS_DEBIT_CARD), 0) == 1 ? true
				: false;
		// 是否支持长城信用卡
		boolean isQccCard = PublicTools.getInt(
				selectedMerchant.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_IS_QCCCARD), 0) == 1 ? true : false;
		// 是否支持信用卡
		boolean isCreditCard = PublicTools.getInt(
				selectedMerchant.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_IS_CREDIT_CARD), 0) == 1 ? true
				: false;
		// 商户名称
		merchantName = EpayUtil.getString(
				selectedMerchant.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_NAME), "");
		bocNo = EpayUtil.getString(selectedMerchant.get(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_ID), "");

		if (isDebitCard) {
			allowAccTypes.add("119");
		}
		if (isQccCard) {
			allowAccTypes.add("104");
			allowAccTypes.add("109");
		}
		if (isCreditCard) {
			allowAccTypes.add("103");
			allowAccTypes.add("108");
			allowAccTypes.add("107");
		}
		treatyContext.setData("readAndAllow", true);
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_queryAllAccount(allowAccTypes, "queryAllowAccCallback");
	}

	/**
	 * 查询支持签约当前商户的账户回调方法
	 * 
	 * @param resultObj
	 */
	public void queryAllowAccCallback(Object resultObj) {
		allowAccounts = EpayUtil.getList(httpObserver.getResult(resultObj));
		allowAccountsDis = new String[allowAccounts.size()];

		if (allowAccounts.isEmpty()) {
			// StringBuffer sb = new
			// StringBuffer("没有可执行商户签约的账户！是否立即关联新账户？\n支持签约协议支付功能的卡类型有：");
			// StringBuffer sb = new
			// StringBuffer(getText(R.string.no_open_electronic_payment_is_correlation).toString());
			//
			// String temp = "";
			// for (Object accTypeStr : allowAccTypes) {
			// String accTypeName = LocalData.AccountType.get(accTypeStr);
			// if (!temp.equals(accTypeName)) {
			// temp = accTypeName;
			// sb.append("“").append(accTypeName).append("”、");
			// }
			// }

			BiiHttpEngine.dissMissProgressDialog();
			String sb = getText(R.string.no_open_treaty_payment_is_correlation).toString();
			BaseDroidApp.getInstanse().showErrorDialog(null, sb.toString(), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					switch (PublicTools.getInt(v.getTag(), 0)) {
					case CustomDialog.TAG_SURE:
//						goRelevanceAccount();
						finish();
						break;
					}
				}
			});

			return;
		}

		for (int i = 0; i < allowAccounts.size(); i++) {
			Map<Object, Object> map = EpayUtil.getMap(allowAccounts.get(i));
			String accountNumber = EpayUtil.getString(
					map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "");
			String accType = EpayUtil.getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE), "");
			String accNickname = EpayUtil.getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), "");
			StringBuffer sb = new StringBuffer();
			sb.append(StringUtil.getForSixForString(accountNumber)).append(" ")
					.append(LocalData.AccountType.get(accType));
			allowAccountsDis[i] = sb.toString();
		}

		httpObserver.req_queryTreatyQuota("queryTreatyQuotaCallback");
	}

	/**
	 * 查询银行端协议支付限额回调方法
	 * 
	 * @param resultObj
	 */
	public void queryTreatyQuotaCallback(Object resultObj) {
		Map<Object, Object> quotaInfo = EpayUtil.getMap(httpObserver.getResult(resultObj));
		perQuota = EpayUtil.getString(quotaInfo.get(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_PER_MAX_QUOTA), "");
		addUpQuota = EpayUtil.getString(quotaInfo.get(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_DAY_MAX_QUOTA),
				"");
		serviceId = EpayUtil.getString(quotaInfo.get(TreatyConstants.METHOD_QUERY_TREATY_QUOTA_FIELD_SERVICE_ID), "");
		initCurPage();
	}

	private void initCurPage() {
		final AccountSpinnerAdapter accountsAdapter = new AccountSpinnerAdapter(this, R.layout.epay_spinner,
				allowAccountsDis);
		accountsAdapter.setmDropDownResource(android.R.layout.simple_spinner_dropdown_item);

		s_accounts = (Spinner) inputMsg.findViewById(R.id.s_accounts);
		s_accounts.setAdapter(accountsAdapter);
		s_accounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				account = EpayUtil.getMap(allowAccounts.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (!allowAccounts.isEmpty())
					account = EpayUtil.getMap(allowAccounts.get(0));
			}
		});

		ll_agreement = (LinearLayout) inputMsg.findViewById(R.id.ll_agreement);
		ll_agreement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkReadAndAllow();
			}
		});

		tv_addup_quota = (TextView) inputMsg.findViewById(R.id.tv_addup_quota);
		tv_addup_quota.setText(StringUtil.parseStringPattern(addUpQuota, 2));
		tv_per_quota = (TextView) inputMsg.findViewById(R.id.tv_per_quota);
		tv_per_quota.setText(StringUtil.parseStringPattern(perQuota, 2));
		tv_merchant_name = (TextView) inputMsg.findViewById(R.id.tv_merchant_name);
		tv_merchant_name.setText(merchantName);
		// 同意协议选择框
		tv_agreement_title = (TextView) inputMsg.findViewById(R.id.tv_agreement_title);
		tv_agreement_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InputMsgActivity.this, ReadAgreementActivity.class);
				// intent.putExtra("merchantName", merchantName);
				InputMsgActivity.this.startActivity(intent);
				InputMsgActivity.this.overridePendingTransition(R.anim.n_pop_enter_bottom_up, R.anim.no_animation);
			}
		});
		// 协议标题
		tv_agreement_checked = (TextView) inputMsg.findViewById(R.id.tv_agreement_checked);

		et_merchant_id = (EditText) inputMsg.findViewById(R.id.et_merchant_id);
		et_day_quota = (EditText) inputMsg.findViewById(R.id.et_day_quota);

		bt_next = (Button) inputMsg.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkSubmitData()) {
					return;
				}
				BiiHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
		checkReadAndAllow();
		BiiHttpEngine.dissMissProgressDialog();
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_merchant_name);
	}

	/**
	 * 请求conversationID
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		httpObserver.setConversationId(conversationId);
		treatyContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID, conversationId);
		requestGetSecurityFactor(serviceId);
	}

	/**
	 * 请求安全因子
	 */
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BiiHttpEngine.showProgressDialog();
				accountId = EpayUtil.getString(account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
				accountNumber = EpayUtil.getString(
						account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "");
				accountType = EpayUtil.getString(account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
						"");
				accNickname = EpayUtil.getString(account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), "");
				String combinId = baseDroidApp.getSecurityChoosed();
				HashMap<Object, Object> params = new HashMap<Object, Object>();
				params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_HOLDER_MERID, merchantId);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_INPUT_QUOTA, dayQuota);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_SINGLE_QUOTA, perQuota);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_CN_NAME, merchantName);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_BOC_NO, bocNo);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_ACCOUNT_NUMBER, accountNumber);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_ACCOUNT_TYPE, accountType);
				params.put(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_DAY_QUOTA, addUpQuota);
				httpObserver.req_addTreatyMerchantPre(params, "addTreatyMerchantPreCallback");
			}
		});
	}

	/**
	 * 签约商户预交易回调方法
	 * 
	 * @param resultObj
	 */
	public void addTreatyMerchantPreCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);

		Map<Object, Object> resultMap = EpayUtil.getMap(result);
		
		// TODO 安全因子
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);
		List<Object> factorList = EpayUtil.getFactorList(resultMap);

		String userName = EpayUtil.getString(
				resultMap.get(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_USER_NAME), "");
		String identityType = EpayUtil.getString(
				resultMap.get(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_TYPE), "");
		String identityNumber = EpayUtil.getString(
				resultMap.get(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_NUMBER), "");
		String mobile = EpayUtil.getString(resultMap.get(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_MOBILE),
				"");

		// 设置显示内容
		BiiHttpEngine.dissMissProgressDialog();
		treatyContext.setData(PubConstants.PUB_FIELD_FACTORLIST, factorList);
		Intent intent = new Intent(this, ConfirmMsgActivity.class);

		intent.putExtra(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_ID, merchantId);
		intent.putExtra(TreatyConstants.METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_NAME, merchantName);
		intent.putExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID, accountId);
		intent.putExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER, accountNumber);
		intent.putExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME, accNickname);
		intent.putExtra(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE, accountType);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_USER_NAME, userName);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_TYPE, identityType);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_NUMBER, identityNumber);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_MOBILE, mobile);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_DAY_QUOTA, addUpQuota);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_INPUT_QUOTA, dayQuota);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_SINGLE_QUOTA, perQuota);
		intent.putExtra(TreatyConstants.METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_BOC_NO, bocNo);

		startActivityForResult(intent, 0);
	}

	/**
	 * 交易提交参数
	 * 
	 * @return
	 */
	private boolean checkSubmitData() {
		dayQuota = EpayUtil.getString(et_day_quota.getText(), "");
		BaseDroidApp bdApp = BaseDroidApp.getInstanse();
		if (StringUtil.isNullOrEmpty(EpayUtil.getString(et_merchant_id.getText(), ""))) {
			bdApp.showInfoMessageDialog(getText(R.string.commerce_account_no_empty).toString());
			return false;
		}
		merchantId = EpayUtil.getString(et_merchant_id.getText(), "");
		// if (merchantId.startsWith(" ") || merchantId.endsWith(" ")) {
		// bdApp.showInfoMessageDialog("商户端ID前段和末尾不能包含空格！");
		// return false;
		// }

		if (StringUtil.isNullOrEmpty(merchantId)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入商户端ID");
			return false;
		}

		if (null == account || account.isEmpty()) {
			bdApp.showInfoMessageDialog("请选择签约账户");
			return false;
		}

//		if (StringUtil.isNullOrEmpty(dayQuota)) {
//			// bdApp.showInfoMessageDialog("请输入自设每日交易限额");
//			bdApp.showInfoMessageDialog(getText(R.string.set_everyday_limit_no_empty).toString());
//			return false;
//		}
//
//		if (!Pattern.matches("(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0\\.[0]*$)^\\d{1,11}(\\.\\d{1,2})?$", dayQuota)) {
//			// bdApp.showInfoMessageDialog("自设每日交易限额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成");
//			// bdApp.showInfoMessageDialog("自设每日交易最多13位数字且不能为0（小数点前最多11位数字，小数点后最多2位数字）");
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.trade_amount_maximum_prompt).toString());
//			return false;
//		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = new RegexpBean("自设每日最高限额", dayQuota, "tranAmount");
		lists.add(transAmountReg);
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}

		if (!StringUtil.isNullOrEmpty(addUpQuota)) {
			double d_curQuota = Double.valueOf(dayQuota);
			double d_sysQuota = Double.valueOf(addUpQuota);
			if (d_curQuota > d_sysQuota) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("自设每日最高限额不能超过每日交易累计限额");
				return false;
			}
		}

		if (!isReadAndAllow) {
			// bdApp.showInfoMessageDialog("请阅读并同意《中国银行协议支付服务协议》");
			bdApp.showInfoMessageDialog("您还没有同意协议，如对协议不清楚，请点击协议查看");
			return false;
		}

		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isReadAndAllow = treatyContext.getBoolean("readAndAllow");
		checkReadAndAllow();
	}

	private void checkReadAndAllow() {
		if (null == tv_agreement_checked)
			return;
		if (isReadAndAllow) {
			tv_agreement_checked.setBackgroundResource(R.drawable.bg_check_box);
			isReadAndAllow = false;
		} else {
			tv_agreement_checked.setBackgroundResource(R.drawable.bg_check_box_checked);
			isReadAndAllow = true;
		}
		treatyContext.setData("readAndAllow", !isReadAndAllow);
	}
}
