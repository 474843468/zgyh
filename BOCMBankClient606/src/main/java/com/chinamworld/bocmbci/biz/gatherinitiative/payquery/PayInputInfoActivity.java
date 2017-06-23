package com.chinamworld.bocmbci.biz.gatherinitiative.payquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: PayInputInfoActivity
 * @Description: 付款信息输入页面
 * @author JiangWei
 * @date 2013-8-29下午7:12:45
 */
public class PayInputInfoActivity extends GatherBaseActivity {

	/** 指令序号 */
	private TextView textOrderNumber;
	/** 发起日期 */
	private TextView textCreatDate;
	/** 交易状态 */
	private TextView textTranStatus;
	/** 收款人姓名 */
	private TextView textName;
	/** 收款账户 */
	private TextView textAccountNumber;
	/** 收款人手机号 */
	private TextView textPayeeMobile;
	/** 备注 */
	private TextView textBeizhu;
	/** 付款币种 */
	private TextView textPayCurrency;
	/** 付款金额 */
	private TextView textPayAmount;
	/** 实付金额 */
	private EditText editPayFactAmount;

	/** 指令序号str */
	private String strOrderNumber;
	/** 发起日期str */
	private String strCreatDate;
	/** 交易状态str */
	private String strTranStatus;
	/** 收款人姓名str */
	private String strName;
	/** 收款账户str */
	private String strAccountNumber;
	/** 收款人手机号str */
	private String strPayeeMobile;
	/** 备注str */
	private String strBeizhu;
	/** 付款币种str */
	private String strPayCurrency;
	/** 付款金额str */
	private String strPayAmount;
	/** 实付金额str */
	private String strPayFactAmount;
	/** 付款账户号 */
	private String strFromAccountNumber;
	/** 付款账户id */
	private String strFromAccountId;
	/** 付款日期 */
	private String strNotifyCurrentDate;
	/** 付款人姓名 */
	private String strPayerName;
	/** 付款人手机号 */
	private String strPayerMobile;
	/** 付款人客户号 */
	private String strPayerCustId;
	/** 发起渠道 */
	private String strNotifyCreatChannel;
	/** 下一步按钮 */
	private Button btnNext;

	/** 付款账户信息 */
	private Map<String, Object> mapPayAccountInfo;
	/** 收款账户信息 */
	private Map<String, Object> mapgatherAccountInfo;
	/** 会话id */
	private String conversationId;
	/** 安全因子数组 */
	private List<Map<String, Object>> combineList;
	/** 随机数 */
	private String randomNumber;
	/** 是否需要otp */
	private boolean isNeedOtp;
	/** 是否需要smc */
	private boolean isNeedSmc;
	/** 拟收费用 */
	private String strPreCommissionCharge;
	/** 基准费用 */
	private String actCharge;
	
	/** 转出账户类型 */
	private String fromAccountType;
	
	/** 转入账户类型 */
	private String toAccountType;
	private String payeeAccountType="payeeAccountType";
	/** 试算结果0 失败 1成功 */
	private String getChargeFlag;
	private boolean iscontains=false;
	/** 是否签约汇款笔数套餐标识位 */
	private String strRemitSetMealFlag;
	private boolean isNeedChooseAccount = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.to_pay);
		View view = LayoutInflater.from(this).inflate(R.layout.gather_pay_input_info, null);
		tabcontent.addView(view);

		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化
	 * @param
	 * @return void
	 * @throws
	 */
	private void init() {
		int positon = this.getIntent().getIntExtra(CURRENT_POSITION, 0);
		dateTime = this.getIntent().getStringExtra(CURRENT_DATETIME);
		isNeedChooseAccount = this.getIntent().getBooleanExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, true);
		mapPayAccountInfo = GatherInitiativeData.getInstance().getPayAcountCallBackList().get(positon);
		mapgatherAccountInfo = GatherInitiativeData.getInstance().getDetailInfo();

		textOrderNumber = (TextView) this.findViewById(R.id.gather_order_number);
		textCreatDate = (TextView) this.findViewById(R.id.gather_creat_date);
		textTranStatus = (TextView) this.findViewById(R.id.gather_tran_status);
		textName = (TextView) this.findViewById(R.id.gather_money_name);
		textAccountNumber = (TextView) this.findViewById(R.id.gather_account_number);
		textPayeeMobile = (TextView) this.findViewById(R.id.gather_payee_mobile);
		textBeizhu = (TextView) this.findViewById(R.id.tv_beizhu);
		textPayCurrency = (TextView) this.findViewById(R.id.gather_pay_currency);
		textPayAmount = (TextView) this.findViewById(R.id.gather_pay_amount);
		editPayFactAmount = (EditText) this.findViewById(R.id.edit_gather_pay_fact);
		btnNext = (Button) this.findViewById(R.id.next_btn);

		TextView gather_payee_mobile_label=(TextView) this.findViewById(R.id.gather_payee_mobile_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, gather_payee_mobile_label);
		
		strOrderNumber = (String) mapgatherAccountInfo.get(GatherInitiative.NOTIFY_ID);
		strCreatDate = (String) mapgatherAccountInfo.get(GatherInitiative.CREATE_DATE);
		strTranStatus = (String) mapgatherAccountInfo.get(GatherInitiative.STATUS);
		strName = (String) mapgatherAccountInfo.get(GatherInitiative.PAYEE_NAME);
		strAccountNumber = (String) mapgatherAccountInfo.get(GatherInitiative.PAYEE_ACCOUNT_NUMBER);
		strPayeeMobile = (String) mapgatherAccountInfo.get(GatherInitiative.PAYEE_MOBILE);
		strBeizhu = (String) mapgatherAccountInfo.get(GatherInitiative.FUR_INFO);
		strPayCurrency = (String) mapgatherAccountInfo.get(GatherInitiative.TRF_CUR);
		strPayAmount = (String) mapgatherAccountInfo.get(GatherInitiative.REQUEST_AMOUNT);

		strFromAccountNumber = (String) mapPayAccountInfo.get(Comm.ACCOUNTNUMBER);
		strFromAccountId = (String) mapPayAccountInfo.get(Comm.ACCOUNT_ID);
		strNotifyCurrentDate = (String) mapgatherAccountInfo.get(GatherInitiative.PAYMENT_DATE);
		strPayerName = (String) mapgatherAccountInfo.get(GatherInitiative.PAYER_NAME);
		strPayerMobile = (String) mapgatherAccountInfo.get(GatherInitiative.PAYER_MOBILE);
		strPayerCustId = (String) mapgatherAccountInfo.get(GatherInitiative.PAYER_CUSTOMER_ID);
		strNotifyCreatChannel = (String) mapgatherAccountInfo.get(GatherInitiative.CREATE_CHANNEL);

		textOrderNumber.setText(strOrderNumber);
		textCreatDate.setText(strCreatDate);
		textTranStatus.setText(LocalData.gatherTranStatus.get(strTranStatus));
		textName.setText(strName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textName);
		textAccountNumber.setText(StringUtil.getForSixForString(String.valueOf(strAccountNumber)));
		textPayeeMobile.setText(strPayeeMobile);
		textBeizhu.setText(StringUtil.isNullOrEmpty(strBeizhu) ? "-" : strBeizhu);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textBeizhu);
		textPayCurrency.setText(LocalData.Currency.get(strPayCurrency));
		textPayAmount.setText(StringUtil.parseStringPattern(strPayAmount, 2));

		
		fromAccountType= (String) mapPayAccountInfo.get(Acc.ACC_ACCOUNTTYPE_RES);
		
		toAccountType= (String) mapgatherAccountInfo.get(payeeAccountType);
		
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				excuseNext();
			}
		});

	}

	/**
	 * @Title: excuseNext
	 * @Description: 执行下一步操作
	 * @param
	 * @return void
	 * @throws
	 */
	private void excuseNext() {
		strPayFactAmount = editPayFactAmount.getText().toString().trim();

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regAmount = new RegexpBean(this.getString(R.string.gather_pay_fact), strPayFactAmount, "amount");
		lists.add(regAmount);
		if (RegexpUtils.regexpDate(lists)) {
			// 请求conversationId
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// 请求安全因子
		requestGetSecurityFactor("PB037C");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 请求预交易
				requestPsnTransActPaymentVerify();
			}
		});
	}

	/**
	 * @Title: requestPsnTransActPaymentVerify
	 * @Description: 请求“主动收款付款预交易”接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActPaymentVerify() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_PAYMENT_VERIFY);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GatherInitiative.FROM_ACCOUNT_ID, strFromAccountId);
		map.put(GatherInitiative.NOTIFY_TRF_CUR, strPayCurrency);
//		map.put(GatherInitiative.NOTIFY_PAYEE_AMOUNT, strPayFactAmount);
		map.put(GatherInitiative.NOTIFY_TRF_AMOUNT, strPayFactAmount);
		map.put(GatherInitiative.PAYEE_NAME, strName);
		map.put(GatherInitiative.PAYEE_ACT_NO, strAccountNumber);
		map.put(GatherInitiative.COMBIN_ID, BaseDroidApp.getInstanse().getSecurityChoosed());
		map.put(GatherInitiative.NOTIFY_ID, strOrderNumber);
		map.put(GatherInitiative.NOTIFY_CREATE_DATE, strCreatDate);
		map.put(GatherInitiative.NOTIFY_CURRENT_DATE, dateTime);
		map.put(GatherInitiative.NOTIFY_REQUEST_AMOUNT, strPayAmount);
		map.put(GatherInitiative.PAYEE_MOBILE, strPayeeMobile);
		map.put(GatherInitiative.PAYER_NAME, strPayerName);
		map.put(GatherInitiative.PAYER_MOBILE, strPayerMobile);
		map.put(GatherInitiative.PAYER_CUST_ID, strPayerCustId);
		map.put(GatherInitiative.NOTIFY_CREATE_CHANNEL, strNotifyCreatChannel);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransActPaymentVerifyCallback");
	}

	/**
	 * @Title: requestPsnTransActPaymentVerifyCallback
	 * @Description: 请求“主动收款付款预交易”接口的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	public static Map<String, Object> result;
	@SuppressWarnings("unchecked")
	public void requestPsnTransActPaymentVerifyCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		result = (Map<String, Object>) biiResponseBody.getResult();
		combineList = (List<Map<String, Object>>) result.get(DrawMoney.FACTORLIST);
		if (StringUtil.isNullOrEmpty(combineList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 获取factorList 判断交易需要的安全因子
//		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < combineList.size(); i++) {
//			Map<String, Object> defaultCombine = (Map<String, Object>) combineList.get(i).get("field");
//			listMap.add(defaultCombine);
//		}
//		if (combineList.size() == 1) {
//			String str = (String) listMap.get(0).get(Login.NAME);
//			if (str.equals(DrawMoney.SMC)) {
//				isNeedSmc = true;
//				isNeedOtp = false;
//			} else {
//				isNeedSmc = false;
//				isNeedOtp = true;
//			}
//		} else {
//			isNeedSmc = true;
//			isNeedOtp = true;
//		}
		requestPsnTransGetBocTransferCommissionCharge("PB037C");
	}

	/**
	 * @Title: requestForTransferCommissionCharge
	 * @Description: 中行内转账费用试算（Svr-01）
	 * @param @param serviceId
	 * @return void
	 * @throws
	 */
	private void requestPsnTransGetBocTransferCommissionCharge(String serviceId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSFER_COMMISSIONCHARGE_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(GatherInitiative.SERVICE_ID, serviceId);
		map.put(GatherInitiative.FROM_ACCOUNT_ID, strFromAccountId);
//		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(GatherInitiative.CURRENCY, strPayCurrency);
		map.put(GatherInitiative.AMOUNT, strPayFactAmount);
		map.put(GatherInitiative.CASH_REMIT, ConstantGloble.CASHREMIT_00);
		map.put(GatherInitiative.REMARK, strBeizhu);
		map.put(GatherInitiative.PAYEE_ACT_NO, strAccountNumber);
		map.put(GatherInitiative.PAYEE_NAME, strName);
		map.put(GatherInitiative.NOTIFY_ID, strOrderNumber);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransGetBocTransferCommissionChargeCallBack");
	}

	/**
	 * @Title: requestPsnTransGetBocTransferCommissionChargeCallBack
	 * @Description: 请求“中行内转账费用试算（Svr-01）”接口的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	public void requestPsnTransGetBocTransferCommissionChargeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		
		 getChargeFlag = (String) result.get(Tran.GETCHARGE_FLAG);		
		strPreCommissionCharge = (String) result.get(GatherInitiative.PRE_COMMISSION_CHARGE);
		actCharge = (String) result.get(GatherInitiative.NEED_COMMISSION_CHARGE);
		strRemitSetMealFlag = (String) result.get(GatherInitiative.REMIT_SET_MEAL_FLAG);
		if(result.containsKey(Tran.NEED_COMMISSION_CHARGE)){
			iscontains=true;
		}
		// 请求随机数
		requestForRandomNumber();
	}

	/**
	 * @Title: requestForRandomNumber
	 * @Description: 请求随机数
	 * @param
	 * @return void
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * @Title: queryRandomNumberCallBack
	 * @Description: 请求随机数的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		GatherInitiativeData.getInstance().setRandomNumber(randomNumber);

		Intent intent = new Intent(this, PayConfirmActivity.class);
		intent.putExtra(GatherInitiative.FROM_ACCOUNT_ID, strFromAccountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, strFromAccountNumber);
		intent.putExtra(GatherInitiative.NOTIFY_TRF_CUR, strPayCurrency);
		intent.putExtra(GatherInitiative.NOTIFY_PAYEE_AMOUNT, strPayFactAmount);
		intent.putExtra(GatherInitiative.PAYEE_NAME, strName);
		intent.putExtra(GatherInitiative.PAYEE_ACT_NO, strAccountNumber);
		intent.putExtra(GatherInitiative.NOTIFY_ID, strOrderNumber);
		intent.putExtra(GatherInitiative.NOTIFY_CREATE_DATE, strCreatDate);
		intent.putExtra(GatherInitiative.NOTIFY_CURRENT_DATE, dateTime);
		intent.putExtra(GatherInitiative.NOTIFY_REQUEST_AMOUNT, strPayAmount);
		intent.putExtra(GatherInitiative.PAYEE_MOBILE, strPayeeMobile);
		intent.putExtra(GatherInitiative.PAYER_NAME, strPayerName);
		intent.putExtra(GatherInitiative.PAYER_MOBILE, strPayerMobile);
		intent.putExtra(GatherInitiative.PAYER_CUST_ID, strPayerCustId);
		intent.putExtra(GatherInitiative.NOTIFY_CREATE_CHANNEL, strNotifyCreatChannel);
		intent.putExtra(GatherInitiative.STATUS, strTranStatus);
		intent.putExtra(GatherInitiative.FUR_INFO, strBeizhu);
//		intent.putExtra(GatherInitiative.ISNEEDOTP, isNeedOtp);
//		intent.putExtra(GatherInitiative.ISNEEDSMC, isNeedSmc);
		intent.putExtra(GatherInitiative.NEED_COMMISSION_CHARGE, actCharge);
		intent.putExtra(GatherInitiative.PRE_COMMISSION_CHARGE, strPreCommissionCharge);
		intent.putExtra(GatherInitiative.REMIT_SET_MEAL_FLAG, strRemitSetMealFlag);
		intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, isNeedChooseAccount);
		intent.putExtra(Tran.GETCHARGE_FLAG, getChargeFlag);
		intent.putExtra("iscontains", iscontains);
		intent.putExtra("fromAccountType", fromAccountType);
		intent.putExtra("toAccountType", toAccountType);
		startActivityForResult(intent, 1001);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case (1001):
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				finish();
			}
			break;
		}
	}

}
