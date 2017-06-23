package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.modifyQuota;

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
import com.chinamworld.bocmbci.biz.epay.constants.WithoutCardContants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 无卡支付 -限额修改-限额输入页
 * 
 */
public class ModifyQuotaInputActivity extends EPayBaseActivity {

	private String tag = "EPayModifyQuotaActivity";

	private View epayModifyQuota;

	private TextView tv_acc_number;
	// private TextView tv_acc_type;
	// private TextView tv_acc_nickname;
	private TextView tv_currency;
	private TextView tv_prompt_msg;
	private TextView tv_curCustMaxQuota;
	private TextView tv_cust_max_quota;
	private EditText et_custMaxQuota;
	// private Spinner s_confirm_type;

	private Button bt_next;

	private PubHttpObserver httpObserver;
	private Context withoutCardTransContext;

	// private List<String> securityList;
	// private List<String> securityNameList;

	private Map<Object, Object> selectedAccount;

	private String combinId;
	private String custMaxQuota;
	private String conversationId;
	private String tranDate;
	private String accId;
	private String accNumber;
	private String accType;
	private String accNickname;
	private String currency;
	private String sysMaxQuota;
	private String custName;
	private String operateType = "修改";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_WITHOUT_CARD);

		epayModifyQuota = LayoutInflater.from(this).inflate(
				R.layout.epay_wc_modify_quota_input, null);

		super.setType(0);
		super.setShowBackBtn(true);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
//		super.setTitleName(PubConstants.TITLE_WITHOUT_CARD);
		super.setContentView(epayModifyQuota);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
//		initTitleRightButton("关闭", rBtncloseListener);
		hideFoot();
		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 1, new String[] { "修改限额", "确认信息",
		// "修改成功" });
		getTransData();
	}

	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		selectedAccount = withoutCardTransContext
				.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC);
		accType = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
				"");
		accNickname = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME), "");
		accId = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
				"");
		accNumber = EpayUtil
				.getString(
						selectedAccount
								.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
						"");
		currency = EpayUtil
				.getString(
						selectedAccount
								.get(PubConstants.METHOD_QUERY_ACCOUNT_DETAIL_FIELD_CURRENCY_CODE),
						"");
		custName = EpayUtil.getString(selectedAccount
				.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM),
				"");
	
//		custMaxQuota = EpayUtil
//				.getString(
//						selectedAccount
//								.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA),
//						"");
		
		if (serviceType == 1) {
			custMaxQuota = EpayUtil
					.getString(
							selectedAccount
									.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA),
							"");
		} else if (serviceType == 2) {
			custMaxQuota = EpayUtil
					.getString(
							selectedAccount
									.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTQUOTA),
							"");
		}
		
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_queryWithoutCardQuota("queryWithoutCardQuotaCallback");
	}

	public void queryWithoutCardQuotaCallback(Object resultObj) {
		sysMaxQuota = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		withoutCardTransContext.setData(
				PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_MAX_AMOUNT,
				sysMaxQuota);
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		// tv_acc_type = (TextView)
		// epayModifyQuota.findViewById(R.id.tv_acc_type);
		// tv_acc_nickname = (TextView)
		// epayModifyQuota.findViewById(R.id.tv_acc_nickname);
		tv_acc_number = (TextView) epayModifyQuota
				.findViewById(R.id.tv_acc_number);
		tv_currency = (TextView) epayModifyQuota.findViewById(R.id.tv_currency);
		tv_prompt_msg = (TextView) epayModifyQuota
				.findViewById(R.id.tv_prompt_msg);
		tv_curCustMaxQuota = (TextView) epayModifyQuota
				.findViewById(R.id.tv_cur_day_max_quota);
		tv_cust_max_quota= (TextView) epayModifyQuota
				.findViewById(R.id.tv_cust_max_quota);
		et_custMaxQuota = (EditText) epayModifyQuota
				.findViewById(R.id.et_cust_max_quota);
		
		
		if (serviceType == 1) {
			tv_cust_max_quota.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_quota_text));
		} else if (serviceType == 2) {
			tv_cust_max_quota.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_collectquota_text));
		}
		// s_confirm_type = (Spinner)
		// epayModifyQuota.findViewById(R.id.s_confirm_type);

		bt_next = (Button) epayModifyQuota.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				custMaxQuota = EpayUtil.getString(et_custMaxQuota.getText(), "");
				if (!checkSubmitData())
					return;

				BiiHttpEngine.showProgressDialog();
				// 查询系统时间
				httpObserver.req_getSystemTime("getSystemTimeCallback");
			}
		});

		initDisplay();
	}

	/**
	 * 初始化显示内容
	 */
	private void initDisplay() {
		// tv_acc_type.setText(LocalData.AccountType.get(accType));
		// tv_acc_nickname.setText(accNickname);
		tv_acc_number.setText(StringUtil.getForSixForString(accNumber));
		tv_currency.setText(LocalData.Currency.get(currency));
		tv_curCustMaxQuota.setText(StringUtil.parseStringPattern(custMaxQuota,
				2));
//		StringBuffer sb = new StringBuffer("*您可设置的银联无卡自助消费日限额最高为");
//		// sb.append("<font color=\"#ba001d\">").append(StringUtil.parseStringPattern(sysMaxQuota,
//		// 2)).append("</font>");
//		sb.append(StringUtil.parseStringPattern(sysMaxQuota, 2));
//		tv_prompt_msg.setText(sb.append("（人民币元）").toString());	
//		// tv_prompt_msg.setText(Html.fromHtml(sb.append("（人民币元）").toString()));
		BiiHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 校验提交参数
	 * 
	 * @return
	 */
	private boolean checkSubmitData() {
		// if (StringUtil.isNullOrEmpty(custMaxQuota)) {
		// BaseDroidApp.getInstanse().showInfoMessageDialog("请输入每日交易累计限额");
		// return false;
		// }
		//
		// if
		// (!Pattern.matches("(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0\\.[0]*$)^\\d{1,11}(\\.\\d{1,2})?$",
		// custMaxQuota)) {
		// //
		// BaseDroidApp.getInstanse().showInfoMessageDialog("每日交易累计限额最大14位（小数点前最多11位数字、小数点后最多2位数字）由大于0的数据组成！");
		// BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.trade_amount_maximum_prompt).toString());
		// return false;
		// }

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		
		if (serviceType == 1) {
			RegexpBean transAmountReg = new RegexpBean("银联在线支付日限额", custMaxQuota,
					"tranAmount");
			lists.add(transAmountReg);
		} else if (serviceType == 2) {
			RegexpBean transAmountReg = new RegexpBean("银联跨行代扣日限额", custMaxQuota,
					"tranAmount");
			lists.add(transAmountReg);
		}

		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}

		if (!StringUtil.isNullOrEmpty(sysMaxQuota)) {
			double d_curQuota = Double.valueOf(custMaxQuota);
			double d_sysQuota = Double.valueOf(sysMaxQuota);
//			if (d_curQuota > d_sysQuota) {
//				// BaseDroidApp.getInstanse().showInfoMessageDialog("每日交易累计限额不能超过系统最高限额");
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						"银联无卡自助消费日限额不能超过系统最高限额");
//				return false;
//			}
		}
		return true;
	}

	/**
	 * 请求conversationId回调方法
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = EpayUtil.getString(BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID), "");
		withoutCardTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID,
				conversationId);
		httpObserver.setConversationId(conversationId);
		requestGetSecurityFactor("PB203");
	}

	/**
	 * 请求安全因子回调方法
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				combinId = baseDroidApp.getSecurityChoosed();
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
				params.put(
						WithoutCardContants.METHOD_MODIFY_QUOTA_PRE_FIELD_ACCOUNT_ID,
						accId);
				params.put(
						WithoutCardContants.METHOD_MODIFY_QUOTA_PRE_FIELD_ACCOUNT_NUMBER,
						accNumber);
				params.put(
						WithoutCardContants.METHOD_MODIFY_QUOTA_PRE_FIELD_CURRENT_QUOTA,
						custMaxQuota);
				params.put(
						WithoutCardContants.METHOD_MODIFY_QUOTA_PRE_FIELD_CUSTOMER_NAME,
						custName);
				params.put(
						WithoutCardContants.METHOD_MODIFY_QUOTA_PRE_FIELD_OPERATE_TYPE,
						operateType);
				params.put(
						WithoutCardContants.METHOD_MODIFY_QUOTA_PRE_FIELD_TRANDATE,
						tranDate);
				
				if (serviceType == 1) {
					params.put(
							WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
							"1");
				} else if (serviceType == 2) {
					params.put(
							WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
							"2");
				}
				httpObserver.req_setWcPaymentQuotaPre(params,
						"setWcPaymentQuotaPre");
			}
		});
	}

	/**
	 * 获取系统时间
	 */
	public void getSystemTimeCallback(Object resultObj) {
		tranDate = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		tranDate = EpayUtil.converSystemTime(tranDate);
		withoutCardTransContext.setData(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA,
				custMaxQuota);
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/**
	 * 设置支付限额预交易
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void setWcPaymentQuotaPre(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);

		Map<Object, Object> resultMap = EpayUtil.getMap(result);

		// TODO 安全因子
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);

		List<Object> factorList = EpayUtil.getFactorList(resultMap);

		withoutCardTransContext.setData(PubConstants.PUB_FIELD_FACTORLIST,
				factorList);
		Intent intent = new Intent(this, ModifyQuotaConfirmActivity.class);
		intent.putExtra("tranDate", tranDate);
		startActivityForResult(intent, 0);
	}

	@Override
	public void finish() {
		setResult(RESET_DATA);
		withoutCardTransContext.setRightButtonClick(false);
		super.finish();
		overridePendingTransition(R.anim.no_animation,
				R.anim.n_pop_exit_bottom_down);
	}

}
