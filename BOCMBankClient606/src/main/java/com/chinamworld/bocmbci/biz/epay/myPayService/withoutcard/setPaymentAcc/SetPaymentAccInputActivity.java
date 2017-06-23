package com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.setPaymentAcc;

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
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class SetPaymentAccInputActivity extends EPayBaseActivity {

	private View ePaySetPaymentAccInput;

	private EditText et_day_max_quota;
	// private TextView tv_acc_type;
	// private TextView tv_acc_nickname;
	private TextView tv_acc_number;
	private TextView tv_prompt_msg;
	private TextView tv_day_max_quota;
	// private Spinner s_confirm_type;
	private Button bt_next;

	// private List<String> securityList;
	// private List<String> securityNameList;

	private Context withoutCardTransContext;
	private PubHttpObserver httpObserver;

	private String conversationId;

	// private String acc_type;
	// private String acc_nickname;
	private String combinId;
	private String acc_number;
	private String accId;
	private String tranDate;
	private String custName;
	private String operatorType = "开通";
	private String currentQuota;
	private String sysMaxQuota;

	private String tag = "SetPaymentAccInputActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		withoutCardTransContext = TransContext.getWithoutCardContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_WITHOUT_CARD);
		ePaySetPaymentAccInput = LayoutInflater.from(this).inflate(
				R.layout.epay_wc_spa_message_input, null);

		super.setType(0);
		super.setShowBackBtn(true);
		if (serviceType == 1) {
			super.setTitleName(PubConstants.TITLE_SECOND_PAY);
		} else if (serviceType == 2) {
			super.setTitleName(PubConstants.TITLE_SECOND_RECEVICE);
		}
		super.setContentView(ePaySetPaymentAccInput);
		super.onCreate(savedInstanceState);

		// 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[]{"选择账户","填写信息","确认信息"});

		// 获取交易数据
		getTransData();
	}

	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		Map<Object, Object> selectedAccount = EpayUtil
				.getMap(withoutCardTransContext
						.getMap(PubConstants.CONTEXT_FIELD_SELECTED_ACC));
		// acc_type =
		// EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
		// "");
		// acc_nickname =
		// EpayUtil.getString(selectedAccount.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME),
		// "");
		accId = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID),
				"");
		acc_number = EpayUtil
				.getString(
						selectedAccount
								.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER),
						"");
		custName = EpayUtil.getString(selectedAccount
				.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NAME),
				"");

		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {

		// tv_acc_type =
		// (TextView)ePaySetPaymentAccInput.findViewById(R.id.tv_acc_type);
		// tv_acc_nickname =
		// (TextView)ePaySetPaymentAccInput.findViewById(R.id.tv_acc_nickname);
		tv_acc_number = (TextView) ePaySetPaymentAccInput
				.findViewById(R.id.tv_acc_number);
		tv_prompt_msg = (TextView) ePaySetPaymentAccInput
				.findViewById(R.id.tv_prompt_msg);
		tv_day_max_quota = (TextView) ePaySetPaymentAccInput
				.findViewById(R.id.tv_day_max_quota);
		
		if (serviceType == 1) {
			tv_day_max_quota.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_quota_text));
		} else if (serviceType == 2) {
			tv_day_max_quota.setText(getResources().getString(R.string.epay_wc_spa_tv_day_max_collectquota_text));
		}
		et_day_max_quota = (EditText) ePaySetPaymentAccInput
				.findViewById(R.id.et_day_max_quota);

		bt_next = (Button) ePaySetPaymentAccInput.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentQuota = EpayUtil.getString(et_day_max_quota.getText(),
						"");
				// 校验提交参数
				if (!checkSubmitData())
					return;
				BiiHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});

		BiiHttpEngine.showProgressDialogCanGoBack();
		// //查询无卡支付系统设置限额
		httpObserver.req_queryWithoutCardQuota("queryWithoutCardQuotaCallback");
	}

	public void queryWithoutCardQuotaCallback(Object resultObj) {
		sysMaxQuota = EpayUtil.getString(httpObserver.getResult(resultObj), "");
		withoutCardTransContext.setData(
				PubConstants.METHOD_QUERY_CUST_MAX_QUOTA_FIELD_MAX_AMOUNT,
				sysMaxQuota);
		initDisplay();
	}

	private void initDisplay() {
		// tv_acc_type.setText(LocalData.AccountType.get(acc_type));
		// tv_acc_nickname.setText(acc_nickname);
		tv_acc_number.setText(StringUtil.getForSixForString(acc_number));
//		StringBuffer sb = new StringBuffer("*您可设置的银联无卡自助消费日限额最高为");
//
//		// sb.append("<font color=\"#ba001d\">").append(StringUtil.parseStringPattern(sysMaxQuota,
//		// 2)).append("</font>");
//		sb.append(StringUtil.parseStringPattern(sysMaxQuota, 2));
//
//		tv_prompt_msg.setText(sb.append("（人民币元）").toString());
		// tv_prompt_msg.setText(Html.fromHtml(sb.append("（人民币元）").toString()));
		BiiHttpEngine.dissMissProgressDialog();
	}

	private boolean checkSubmitData() {
		// if (StringUtil.isNullOrEmpty(currentQuota)) {
		// BaseDroidApp.getInstanse().showInfoMessageDialog("请输入每日交易累计限额");
		// return false;
		// }
		//
		// if
		// (!Pattern.matches("(?!^0$)(?!^[0]{2,})(?!^0[1-9]+)(?!^0\\.[0]*$)^\\d{1,11}(\\.\\d{1,2})?$",
		// currentQuota)) {
		// //
		// BaseDroidApp.getInstanse().showInfoMessageDialog("最多13位数字且不能为0（小数点前最多11位数字，小数点后最多2位数字）");
		// BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.trade_amount_maximum_prompt).toString());
		// return false;
		// }

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();

		if (serviceType == 1) {
			RegexpBean transAmountReg = new RegexpBean("银联在线支付日限额", currentQuota,
					"tranAmount");
			lists.add(transAmountReg);
		} else if (serviceType == 2) {
			RegexpBean transAmountReg = new RegexpBean("银联跨行代扣日限额", currentQuota,
					"tranAmount");
			lists.add(transAmountReg);
		}
		
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}

		if (!StringUtil.isNullOrEmpty(sysMaxQuota)) {
			double d_curQuota = Double.valueOf(currentQuota);
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
	 * 请求conversationID回调方法
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = EpayUtil.getString(httpObserver.getResult(resultObj),
				"");
		withoutCardTransContext.setData(PubConstants.PUB_FIELD_CONVERSATION_ID,
				conversationId);
		httpObserver.setConversationId(conversationId);
		requestGetSecurityFactor("PB203");
	}

	/**
	 * 请求安全因子回调方法
	 */
	// @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		final BaseDroidApp baseDroidApp = BaseDroidApp.getInstanse();
		baseDroidApp.showSeurityChooseDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				combinId = baseDroidApp.getSecurityChoosed();
				// 请求系统时间
				BiiHttpEngine.showProgressDialog();
				requestSystemDateTime();
			}
		});
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		tranDate = EpayUtil.converSystemTime(dateTime);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_ACCOUNT_ID,
				accId);
		params.put(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_ACCOUNT_NUMBER,
				acc_number);
		params.put(WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_TRANDATE,
				tranDate);
		params.put(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_OPERATE_TYPE,
				operatorType);
		params.put(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CUSTOMER_NAME,
				custName);
		params.put(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA,
				currentQuota);
		if (serviceType == 1) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"1");
		} else if (serviceType == 2) {
			params.put(
					WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE,
					"2");
		}
		
		params.put(PubConstants.PUB_FIELD_COMBIN_ID, combinId);
		httpObserver.req_setWCPaymentServiceAccPre(params,
				"setWCPaymentServiceAccPre");
	}

	/**
	 * 开通无卡支付预交易回调方法
	 * 
	 * @param resultObj
	 */
	// @SuppressWarnings("unchecked")
	public void setWCPaymentServiceAccPre(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);

		Map<Object, Object> resultMap = EpayUtil.getMap(result);

		// TODO 安全因子
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.ISFOREX_PRERESULT_KEY, resultMap);

		List<Object> factorList = EpayUtil.getFactorList(resultMap);
		// 设置显示内容
		BiiHttpEngine.dissMissProgressDialog();
		withoutCardTransContext.setData(PubConstants.PUB_FIELD_FACTORLIST,
				factorList);
		withoutCardTransContext.setData(
				WithoutCardContants.METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA,
				currentQuota);
		Intent intent = new Intent(this, SetPaymentAccConfirmActivity.class);
		intent.putExtra("tranDate", tranDate);
		this.startActivityForResult(intent, 0);
	}

	@Override
	public void finish() {
		// setResult(RESULT_OK);
		super.finish();
	}

}
