package com.chinamworld.bocmbci.biz.gatherinitiative.payquery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * @ClassName: PayConfirmActivity
 * @Description: 付款确认和完成页面
 * @author JiangWei
 * @date 2013-8-29下午7:34:35
 */
public class PayConfirmActivity extends GatherBaseActivity {
	/** 内容标题1 */
	private TextView textTitle1;
	/** 内容标题2 */
	private TextView textTitle2;
	/** 交易序号 */
	private TextView textTranId;
	/** 指令序号 */
	private TextView textOrderNumber;
	/** 发起日期 */
	private TextView textCreatDate;
	/** 付款日期 */
	private TextView textPayDate;
	/** 交易状态 */
	private TextView textTranStatus;
	/** 收款人姓名 */
	private TextView textPayeeName;
	/** 收款账户 */
	private TextView textAccountNumber;
	/** 收款人手机号 */
	private TextView textPayeeMobile;
	/** 发起渠道 */
	private TextView textStartWay;
	/** 备注 */
	private TextView textBeiZhu;
	/** 付款人姓名 */
	private TextView textPayerName;
	/** 付款人账户 */
	private TextView textPayerAccount;
	/** 付款人手机号 */
	private TextView textPayerPhone;
	/** 付款渠道 */
	private TextView textPayWay;
	/** 付款币种 */
	private TextView textPayCurrency;
	/** 付款金额 */
	private TextView textPayAmount;
	/** 实付金额 */
	private TextView textPayFact;
	/** 优惠后费用 */
	private TextView textChargeAfterSale;
	/** 您选择的是汇款笔数套餐内的账户 */
	private TextView textNotifyCustomerCharge;
	/** 确定按钮（完成按钮） */
	private Button btnNext;

	/** 交易序号 str */
	private String strTranId;
	/** 指令序号 str */
	private String strOrderNumber;
	/** 发起日期str */
	private String strCreatDate;
	/** 付款日期str */
	private String strPayDate;
	/** 交易状态str */
	private String strTranStatus;
	/** 收款人姓名str */
	private String strPayeeName;
	/** 收款账户str */
	private String strAccountNumber;
	/** 收款人手机号str */
	private String strPayeeMobile;
	/** 发起渠道 str */
	private String strStartWay;
	/** 备注str */
	private String strBeiZhu;
	/** 付款人姓名str */
	private String strPayerName;
	/** 付款人账户str */
	private String strPayerAccount;
	/** 付款人手机号str */
	private String strPayerPhone;
	/** 付款渠道 固定值：手机渠道 */
//	private static final String strPayWay = "2";
	/**发起渠道 “1”：web渠道 “2”：手机渠道 */
	private String strPayWay;
	/** 付款币种 str */
	private String strPayCurrency;
	/** 付款金额 str */
	private String strPayAmount;
	/** 实付金额 str */
	private String strPayFact;
	/** 付款人账户id */
	private String strFromAccountId;
	/** 付款人客户号str */
	private String strPayerCustId;
	/** 优惠后费用 str */
	private String strChargeAfterSale;
	private boolean isNeedOtp;
	private boolean isNeedSmc;
	/** 交易序号布局 */
	private LinearLayout layoutTransactionId;
	/** 手机交易码布局 */
	private LinearLayout layoutPhoneCode;
	/** 动态口令布局 */
	private LinearLayout layoutPass3;
	/** 获取交易码按钮 */
	private Button btnSmc;
	/** 手机交易码 */
	private SipBox sipBox_phone_code;
	/** 是否签约汇款笔数套餐标识位 */
	private String strRemitSetMealFlag;
	/** 加密控件 动态口令 */
	private SipBox sipBox_otp;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	private String smc;
	private String smc_RC;
	private String otp;
	private String otp_RC;
	private String tokenId;
	private boolean isFinish = false;

	/**手续费试算*/
	/**基准费用*/
	private LinearLayout ll_need; 
	private String actCharge;
	private TextView actChargeDispalyTv;
	private TextView actChargeTv;
	/** 转出账户类型 */
	private String fromAccountType;	
	/** 转入账户类型 */
	private String toAccountType;
	/** 试算结果0 失败 1成功 */
	private String getChargeFlag;
	private boolean iscontains=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.to_pay);
		View view = LayoutInflater.from(this).inflate(
				R.layout.gather_pay_confirm_activity, null);
		tabcontent.addView(view);

		getIntentData();
		init();
	}

	/**
	 * @Title: getIntentData
	 * @Description: 获取intent携带的数据
	 * @param
	 * @return void
	 * @throws
	 */
	private void getIntentData() {
		Intent intent = this.getIntent();
		strOrderNumber = intent.getStringExtra(GatherInitiative.NOTIFY_ID);
		strCreatDate = intent
				.getStringExtra(GatherInitiative.NOTIFY_CREATE_DATE);
		strPayDate = intent
				.getStringExtra(GatherInitiative.NOTIFY_CURRENT_DATE);
		strTranStatus = intent.getStringExtra(GatherInitiative.STATUS);
		strPayeeName = intent.getStringExtra(GatherInitiative.PAYEE_NAME);
		strAccountNumber = intent.getStringExtra(GatherInitiative.PAYEE_ACT_NO);
		strFromAccountId = intent
				.getStringExtra(GatherInitiative.FROM_ACCOUNT_ID);
		strPayeeMobile = intent.getStringExtra(GatherInitiative.PAYEE_MOBILE);
		strStartWay = intent
				.getStringExtra(GatherInitiative.NOTIFY_CREATE_CHANNEL);
		strBeiZhu = intent.getStringExtra(GatherInitiative.FUR_INFO);
		strPayerName = intent.getStringExtra(GatherInitiative.PAYER_NAME);
		strPayerAccount = intent.getStringExtra(Comm.ACCOUNTNUMBER);
		strPayerPhone = intent.getStringExtra(GatherInitiative.PAYER_MOBILE);
		 strPayWay =intent.getStringExtra(GatherInitiative.NOTIFY_CREATE_CHANNEL);
		strPayCurrency = intent.getStringExtra(GatherInitiative.NOTIFY_TRF_CUR);
		strPayAmount = intent
				.getStringExtra(GatherInitiative.NOTIFY_REQUEST_AMOUNT);
		strPayFact = intent
				.getStringExtra(GatherInitiative.NOTIFY_PAYEE_AMOUNT);
		// isNeedOtp = intent.getBooleanExtra(GatherInitiative.ISNEEDOTP,
		// false);
		// isNeedSmc = intent.getBooleanExtra(GatherInitiative.ISNEEDSMC,
		// false);
		strPayerCustId = intent.getStringExtra(GatherInitiative.PAYER_CUST_ID);
		strChargeAfterSale = intent
				.getStringExtra(GatherInitiative.PRE_COMMISSION_CHARGE);
		strRemitSetMealFlag = intent
				.getStringExtra(GatherInitiative.REMIT_SET_MEAL_FLAG);
		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		
		actCharge=intent
		.getStringExtra(GatherInitiative.NEED_COMMISSION_CHARGE);
		getChargeFlag=intent.getStringExtra(Tran.GETCHARGE_FLAG);
		fromAccountType=intent.getStringExtra("fromAccountType");
		toAccountType=intent.getStringExtra("toAccountType");			
		iscontains=intent.getBooleanExtra("iscontains", false);
		if(ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
				&&!iscontains){
			
		}else{
			ll_need.setVisibility(View.VISIBLE);	
		}
		
		if (!StringUtil.isNullOrEmpty(actCharge)) {
			actCharge = StringUtil.parseStringCodePattern(strPayCurrency,
					actCharge, 2);
			// 转出是借记卡 转入是信用卡 基准费用为0 不显示基准费用		
			if(ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
				&&LocalData.mycardList.contains(toAccountType)
				&&Double.parseDouble(actCharge)==0){
				ll_need.setVisibility(View.GONE);	
			}
		}
		
	
	}

	/**
	 * @Title: init
	 * @Description: 初始化view和数据
	 * @param
	 * @return void
	 * @throws
	 */
	private void init() {
		

		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		
		textTitle1 = (TextView) this.findViewById(R.id.gather_confirm_title_tv);
		textTitle2 = (TextView) this
				.findViewById(R.id.gather_confirm_title_tv_2);
		textTranId = (TextView) this.findViewById(R.id.text_transaction_id);
		textOrderNumber = (TextView) this
				.findViewById(R.id.gather_order_number);
		textCreatDate = (TextView) this.findViewById(R.id.gather_creat_date);
		textPayDate = (TextView) this.findViewById(R.id.gather_pay_date);
		textTranStatus = (TextView) this.findViewById(R.id.gather_tran_status);
		textPayeeName = (TextView) this.findViewById(R.id.gather_money_name);
		textAccountNumber = (TextView) this
				.findViewById(R.id.gather_account_number);
		textPayeeMobile = (TextView) this.findViewById(R.id.tv_payeeMobile);
		textStartWay = (TextView) this.findViewById(R.id.gather_start_way);
		textBeiZhu = (TextView) this.findViewById(R.id.tv_beizhu);
		textPayerName = (TextView) this.findViewById(R.id.tv_payer_name);
		textPayerAccount = (TextView) this
				.findViewById(R.id.tv_gather_payer_acount);
		textPayerPhone = (TextView) this.findViewById(R.id.tv_payer_phone);
		textPayWay = (TextView) this.findViewById(R.id.gather_pay_way);
		textPayCurrency = (TextView) this
				.findViewById(R.id.gather_pay_currency);
		textPayAmount = (TextView) this.findViewById(R.id.gather_pay_amount);
		textPayFact = (TextView) this.findViewById(R.id.gather_pay_fact);
		textChargeAfterSale = (TextView) this
				.findViewById(R.id.charge_after_sale);
		textNotifyCustomerCharge = (TextView) this
				.findViewById(R.id.notify_customer_content);
		TextView tv_payeeMobile_label = (TextView) this
				.findViewById(R.id.tv_payeeMobile_label);
		TextView tv_payer_phone_label = (TextView) this
				.findViewById(R.id.tv_payer_phone_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_payeeMobile_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_payer_phone_label);
		if ("1".equals(strRemitSetMealFlag)) {
			textNotifyCustomerCharge
					.setText(getString(R.string.tran_remitSetMealFlag));
		} else {
			textNotifyCustomerCharge
					.setText(getString(R.string.tran_remitSetMealFlag_normal));
		}
		layoutTransactionId = (LinearLayout) this
				.findViewById(R.id.layout_transaction_id);
		layoutPhoneCode = (LinearLayout) this
				.findViewById(R.id.view_phone_code);
		layoutPass3 = (LinearLayout) this.findViewById(R.id.view_pass3);
		btnSmc = (Button) this.findViewById(R.id.btn_phone_check);
		// btnSmc.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// }
		// });
		SmsCodeUtils.getInstance().addSmsCodeListner(btnSmc,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						sendSMSCToMobile();
					}
				});
		btnNext = (Button) this.findViewById(R.id.btnConfirm);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				excuseNext();
			}
		});

		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		LinearLayout phoneCode = (LinearLayout) findViewById(R.id.layout_phone_code);
		sipBox_phone_code = new SipBox(this);
		initPasswordSipBox(sipBox_phone_code);
		phoneCode.addView(sipBox_phone_code);

		LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.layout_pass_3);
		sipBox_otp = new SipBox(this);
		initPasswordSipBox(sipBox_otp);
		linearLayout3.addView(sipBox_otp);

		if (isNeedSmc) {
			layoutPhoneCode.setVisibility(View.VISIBLE);
		}
		if (isNeedOtp) {
			layoutPass3.setVisibility(View.VISIBLE);
		}
		textOrderNumber.setText(strOrderNumber);
		textCreatDate.setText(strCreatDate);
		textPayDate.setText(strPayDate);
		textTranStatus.setText(LocalData.gatherTranStatus.get(strTranStatus));
		textPayeeName.setText(strPayeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				textPayeeName);
		textAccountNumber.setText(StringUtil.getForSixForString(String
				.valueOf(strAccountNumber)));
		textPayeeMobile.setText(strPayeeMobile);
		textStartWay.setText(LocalData.startWay.get(strStartWay));
		textBeiZhu.setText(StringUtil.isNullOrEmpty(strBeiZhu) ? "-"
				: strBeiZhu);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				textBeiZhu);
		textPayerName.setText(strPayerName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				textPayerName);
		textPayerAccount.setText(StringUtil.getForSixForString(String
				.valueOf(strPayerAccount)));
		textPayerPhone.setText(strPayerPhone);
		textPayWay.setText(LocalData.startWay.get(strPayWay));
		textPayCurrency.setText(LocalData.Currency.get(strPayCurrency));
		textPayAmount.setText(StringUtil.parseStringPattern(strPayAmount, 2));
		textPayFact.setText(StringUtil.parseStringPattern(strPayFact, 2));
		textChargeAfterSale.setText(StringUtil.parseStringPattern(
				strChargeAfterSale, 2));
		actChargeTv.setText(StringUtil.parseStringPattern(
				actCharge, 2));
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, GatherInitiativeData.getInstance()
				.getRandomNumber(), PayInputInfoActivity.result, this);
		isNeedOtp = usbKeyText.getIsOtp();
		isNeedSmc = usbKeyText.getIsSmc();
	}

	/**
	 * @Title: showSuccessView
	 * @Description: 展示成功页面
	 * @param
	 * @return void
	 */
	private void showSuccessView() {
		isFinish = true;
		ibBack.setVisibility(View.GONE);
		layoutPhoneCode.setVisibility(View.GONE);
		layoutPass3.setVisibility(View.GONE);
		
		if ("1".equals(strRemitSetMealFlag)) {
			textNotifyCustomerCharge
					.setText(getString(R.string.tran_remitSetMealFlag_success));
		} else {
			textNotifyCustomerCharge.setVisibility(View.GONE);
		}
		
		layoutTransactionId.setVisibility(View.VISIBLE);
		textTitle1.setText(R.string.pay_success_second_title_top);
		textTitle1.setVisibility(View.GONE);
		textTitle2.setVisibility(View.VISIBLE);
		usbKeyText.setVisibility(View.GONE);
		textTranId.setText(strTranId);
		textChargeAfterSale.setText(StringUtil.parseStringPattern(
				strChargeAfterSale, 2));
		actChargeTv.setText(StringUtil.parseStringPattern(
				actCharge, 2));
		textTranStatus.setText(LocalData.gatherTranStatus.get(strTranStatus));

		btnNext.setText(this.getResources().getString(R.string.finish));
		ScrollView scrollView = (ScrollView) this.findViewById(R.id.layout_top);
		scrollView.scrollTo(0, 0);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isNeedChooseAccount = getIntent().getBooleanExtra(
						GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, true);
				if (isNeedChooseAccount) {
					setResult(RESULT_OK);
					finish();
				} else {
					// 账户管理进入
					ActivityTaskManager.getInstance().removeAllActivity();
					AccDataCenter.getInstance().clearAccData();
					Intent intent = new Intent();
					intent.setClass(PayConfirmActivity.this,
							AccManageActivity.class);
					startActivity(intent);
					finish();
				}
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
		// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// if (isNeedSmc) {
		// RegexpBean phone_code_rb = new
		// RegexpBean(this.getString(R.string.acc_smc_regex), sipBox_phone_code
		// .getText().toString().trim(), "atmpass");
		// lists.add(phone_code_rb);
		// }
		// if (isNeedOtp) {
		// RegexpBean otpRB = new
		// RegexpBean(this.getString(R.string.set_otp_no),
		// sipBox_otp.getText().toString()
		// .trim(), "atmpass");
		// lists.add(otpRB);
		// }
		// if (!RegexpUtils.regexpDate(lists)) {
		// return;
		// }
		// try {
		// if (isNeedSmc) {
		// smc = sipBox_phone_code.getValue().getEncryptPassword();
		// smc_RC = sipBox_phone_code.getValue().getEncryptRandomNum();
		// }
		// if (isNeedOtp) {
		// otp = sipBox_otp.getValue().getEncryptPassword();
		// otp_RC = sipBox_otp.getValue().getEncryptRandomNum();
		// }
		// // 获取tokenId
		// BaseHttpEngine.showProgressDialog();
		// requestPSNGetTokenId((String)
		// BaseDroidApp.getInstanse().getBizDataMap()
		// .get(ConstantGloble.CONVERSATION_ID));
		// // if(StringUtil.isNullOrEmpty(tokenId)){
		// // requestPSNGetTokenId((String)
		// BaseDroidApp.getInstanse().getBizDataMap()
		// // .get(ConstantGloble.CONVERSATION_ID));
		// // }else{
		// // requestPsnTransActPaymentSubmit();
		// // }
		// } catch (CodeException e) {
		// LogGloble.exceptionPrint(e);
		// }
		/** 安全工具数据校验 */
		usbKeyText.checkDataUsbKey(sipBox_otp, sipBox_phone_code,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						// 获取tokenId
						BaseHttpEngine.showProgressDialog();
						requestPSNGetTokenId((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});

	}

	/**
	 * @Title: initPasswordSipBox
	 * @Description: 初始化加密控件
	 * @param @param sipBox
	 * @return void
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		LinearLayout.LayoutParams param1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param1);
		// add by 2016年9月12日 luqp 修改
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, GatherInitiativeData.getInstance().getRandomNumber(), this);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.PASSWORD);
//		sipBox.setRandomKey_S(GatherInitiativeData.getInstance().getRandomNumber());
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setSipDelegator(this);
		InputFilter[] filters1 = { new InputFilter.LengthFilter(20) };
		sipBox.setFilters(filters1);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnTransActPaymentSubmit();
	}

	/**
	 * @Title: requestPsnTransActPaymentSubmit
	 * @Description: 请求“主动收款付款交易提交”接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActPaymentSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_PAYMENT_SUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(GatherInitiative.FROM_ACCOUNT_ID, strFromAccountId);
		map.put(GatherInitiative.NOTIFY_ID, strOrderNumber);
		map.put(GatherInitiative.NOTIFY_TRF_CUR, strPayCurrency);
		map.put(GatherInitiative.NOTIFY_TRF_AMOUNT, strPayFact);
		map.put(GatherInitiative.TOKEN, tokenId);
		map.put(GatherInitiative.NOTIFY_CREATE_DATE, strCreatDate);
		map.put(GatherInitiative.NOTIFY_CURRENT_DATE, strPayDate);
		map.put(GatherInitiative.PAYEE_ACT_NO, strAccountNumber);
		map.put(GatherInitiative.NOTIFY_REQUEST_AMOUNT, strPayAmount);
		map.put(GatherInitiative.PAYEE_MOBILE, strPayeeMobile);
		map.put(GatherInitiative.PAYEE_NAME, strPayeeName);
		map.put(GatherInitiative.PAYER_NAME, strPayerName);
		map.put(GatherInitiative.PAYER_MOBILE, strPayerPhone);
		map.put(GatherInitiative.PAYER_CUST_ID, strPayerCustId);
		// map.put(GatherInitiative.NOTIFY_CREATE_CHANNEL, strStartWay);
		map.put(GatherInitiative.NOTIFY_CREATE_CHANNEL, strPayWay);// 发起渠道 “1”：web渠道 “2”：手机渠道 
		// map.put(DrawMoney.SMC, smc);
		// map.put(DrawMoney.SMC_RC, smc_RC);
		// map.put(DrawMoney.OTP, otp);
		// map.put(DrawMoney.OTP_RC, otp_RC);
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransActPaymentSubmitCallback");
	}

	/**
	 * @Title: requestPsnTransActPaymentSubmitCallback
	 * @Description: 请求“主动收款付款交易提交”接口的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransActPaymentSubmitCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		strChargeAfterSale = (String) result
				.get(GatherInitiative.FINAL_COMMISSION_CHARGE);
		strTranId = (String) result.get(GatherInitiative.TRANSACTION_ID);
		strTranStatus = (String) result.get(GatherInitiative.STATUS);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_SUCCESS)) {
			showSuccessView();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isFinish) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				setResult(RESULT_OK);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
