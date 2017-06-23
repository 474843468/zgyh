package com.chinamworld.bocmbci.biz.gatherinitiative.creatgather;

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
import android.widget.CheckBox;
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
import com.chinamworld.bocmbci.bii.constant.Setting;
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
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * @ClassName: GatherConfirmActivity
 * @Description: “发起新收款”的确认和完成页面
 * @author JiangWei
 * @date 2013-8-27下午4:03:49
 */
public class GatherConfirmActivity extends GatherBaseActivity {
	/** 指令序号 */
	private TextView orderNumberText;
	/** 收款人姓名 */
	private TextView payeeNameText;
	/** 收款币种 */
	private TextView currencyText;
	/** 收款账户 */
	private TextView accountText;
	/** 收款人手机号 */
	private TextView payeePhoneText;
	/** 收款人手机号提示语 */
	private TextView payeePhoneTextHint;
	/** 收款金额 */
	private TextView payeeAmmountText;
	/** 付款人姓名 */
	private TextView payerNameText;
	/** 付款人类型 */
	private TextView payerTypeText;
	/** 付款人手机号 */
	private TextView payerPhoneText;
	/** 付款人手机号提示语 */
	private TextView payerPhoneTextHint;
	/** 备注 */
	private TextView remarkText;
	/** 指令序号str */
	private String orderNumberStr;
	/** 收款人姓名str */
	private String payeeNameStr;
	/** 收款币种str */
	private String currencyStr;
	/** 收款账户str */
	private String accountStr;
	/** 收款账户id */
	private String accountIdStr;
	/** 收款人手机号str */
	private String payeePhoneStr;
	/** 收款金额 str */
	private String payeeAmmountStr;
	/** 付款人姓名 str */
	private String payerNameStr;
	/** 付款人类型 str */
	private String payerTypeStr;
	/** 付款人手机号str */
	private String payerPhoneStr;
	/** 备注str */
	private String remarkStr;
	private String payerCustIdStr;

	private CheckBox payerSaveCheckBox;
	private TextView payerSaveTv;
	/** 确认页面的小标题 */
	private TextView titleFirst;
	/** 完成页面的小标题 */
	private TextView titleSecond;
	/** 确定按钮 */
	private Button nextBtn;
	/**otp总布局*/
	private LinearLayout linearOtp;
	/** otp布局 */
	private LinearLayout linearLayoutOtp;
	/** 保存为联系人布局 */
	private LinearLayout layoutSavePayer;
	/** 指令序号的布局 */
	private LinearLayout layoutOrderNumber;
	/** 加密控件 动态口令 */
	private SipBox sipBox_otp;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/**动态口令是否显示*/
	private Boolean isOtp;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	private String otp;
	private String otp_RC;
	/** tokenId */
	private String tokenId;
	private boolean isFinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.creat_new_gather);
		View view = LayoutInflater.from(this).inflate(R.layout.gather_creat_confirm_activity, null);
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
		payeeNameStr = this.getIntent().getStringExtra(GatherInitiative.PAYEE_NAME);
		currencyStr = this.getIntent().getStringExtra(GatherInitiative.CURRENCY);
		accountIdStr = this.getIntent().getStringExtra(GatherInitiative.TO_ACCOUNTID);
		accountStr = this.getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		payeePhoneStr = this.getIntent().getStringExtra(GatherInitiative.PAYEE_MOBILE);
		payeeAmmountStr = this.getIntent().getStringExtra(GatherInitiative.NOTIFY_PAYEE_AMOUNT);
		payerNameStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_NAME);
		payerTypeStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_CHANNEL);
		payerPhoneStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_MOBILE);
		remarkStr = this.getIntent().getStringExtra(GatherInitiative.REMARK);
		payerCustIdStr = this.getIntent().getStringExtra(GatherInitiative.PAYER_CUST_ID);
	}

	/**
	 * @Title: init
	 * @Description: 初始化页面和数据
	 * @return void
	 * @throws
	 */
	private void init() {
		titleFirst = (TextView) this.findViewById(R.id.gather_confirm_title_tv);
		titleSecond = (TextView) this.findViewById(R.id.gather_finish_title_tv);
		layoutOrderNumber = (LinearLayout) findViewById(R.id.layout_gather_order_number);
		layoutSavePayer = (LinearLayout) findViewById(R.id.layout_save_payer);
		orderNumberText = (TextView) this.findViewById(R.id.gather_order_number);
		payeeNameText = (TextView) this.findViewById(R.id.gather_money_name);
		currencyText = (TextView) this.findViewById(R.id.gather_money_type);
		accountText = (TextView) this.findViewById(R.id.gather_account_number);
		payeePhoneText = (TextView) this.findViewById(R.id.tv_payeeMobile);
		payeePhoneTextHint = (TextView) this.findViewById(R.id.tv_payeeMobile_hint);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeePhoneTextHint);
		payeeAmmountText = (TextView) this.findViewById(R.id.tv_gather_money_ammount);
		payerNameText = (TextView) this.findViewById(R.id.tv_payer_name);
		payerTypeText = (TextView) this.findViewById(R.id.tv_payer_type);
		payerPhoneText = (TextView) this.findViewById(R.id.tv_payer_phone);
		payerPhoneTextHint = (TextView) this.findViewById(R.id.tv_payer_phone_hint);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerPhoneTextHint);
		remarkText = (TextView) this.findViewById(R.id.tv_beizhu);
		nextBtn = (Button) this.findViewById(R.id.gather_confirm_next_btn);
		payerSaveCheckBox = (CheckBox) this.findViewById(R.id.check_save_payer);
		payerSaveTv = (TextView) this.findViewById(R.id.tv_save_payer);
		payerSaveTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				// 点击文字切换选中状态
				if(payerSaveCheckBox.isChecked()){
					payerSaveCheckBox.setChecked(false);
				}else {
					payerSaveCheckBox.setChecked(true);
				}
			}
		});

		if(getIntent().getBooleanExtra("noNeedSavePayer", false)){
			layoutSavePayer.setVisibility(View.GONE);
		}
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		linearOtp = (LinearLayout)findViewById(R.id.view_pass1);
		linearLayoutOtp = (LinearLayout) findViewById(R.id.layout_pass_1);
		sipBox_otp = new SipBox(this);
		initPasswordSipBox(sipBox_otp);
		if(isSms){
			// 手机交易码
			ll_smc = (LinearLayout) findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sipBoxSmc = (SipBox) findViewById(R.id.sipbox_smc);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, GatherInitiativeData.getInstance().getRandomNumber(), this);
//			sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setId(10002);
//			sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxSmc.setSingleLine(true);
//			sipBoxSmc.setSipDelegator(this);
//			sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxSmc.setRandomKey_S(GatherInitiativeData.getInstance().getRandomNumber());
			Button smsBtn = (Button) findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送验证码到手机
							sendMSCToMobile();
						}
					});
			}

//		linearLayoutOtp.addView(sipBox_otp);

		payeeNameText.setText(payeeNameStr);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeNameText);
		currencyText.setText(LocalData.Currency.get(currencyStr));
		accountText.setText(StringUtil.getForSixForString(String.valueOf(accountStr)));
		payeePhoneText.setText(payeePhoneStr);
		payeeAmmountText.setText(StringUtil.parseStringPattern(payeeAmmountStr, 2));
		payerNameText.setText(payerNameStr);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerNameText);
		payerTypeText.setText(LocalData.payerType.get(payerTypeStr));
		payerPhoneText.setText(payerPhoneStr);
		remarkText.setText(StringUtil.isNullOrEmpty(remarkStr) ? "-" : remarkStr);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkText);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				excuseNext();
			}
		});
	}
	
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox(){
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeyText = (UsbKeyText)findViewById(R.id.sip_usbkey);
		usbKeyText.Init(mmconversationId, GatherInitiativeData.getInstance().getRandomNumber(), CreatGatherInputInfoActivity.result, this);
		isOtp = usbKeyText.getIsOtp();
		isSms = usbKeyText.getIsSmc();
	}
	/** 发送验证码到手机*/
	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}
	public void sendMSCToMobileCallback(Object resultObj) {
	}
	
	/**
	 * @Title: initPasswordSipBox
	 * @Description: 初始化加密控件
	 * @param @param sipBox
	 * @return void
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		if(isOtp){
			linearOtp.setVisibility(View.VISIBLE);
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
		linearLayoutOtp.addView(sipBox);
		}
	}

	/**
	 * @Title: showSuccessView
	 * @Description: 展示成功页面
	 * @param
	 * @return void
	 * @throws
	 */
	private void showSuccessView() {
		isFinish = true;
		ibBack.setVisibility(View.GONE);
		titleFirst.setVisibility(View.GONE);
		titleSecond.setVisibility(View.VISIBLE);
		((LinearLayout) this.findViewById(R.id.view_pass1)).setVisibility(View.GONE);
		layoutSavePayer.setVisibility(View.GONE);
		layoutOrderNumber.setVisibility(View.VISIBLE);
		orderNumberText.setText(orderNumberStr);
		nextBtn.setText(R.string.finish);
		usbKeyText.setVisibility(View.GONE);
		if(isSms){			
		  ll_smc.setVisibility(View.GONE);
		}
		ScrollView scrollView = (ScrollView) this.findViewById(R.id.layout_top);
		scrollView.scrollTo(0, 0);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isNeedChooseAccount = getIntent().getBooleanExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT,
						true);
				if (isNeedChooseAccount) {
					setResult(RESULT_OK);
					finish();
				} else {
					// 账户管理进入
					ActivityTaskManager.getInstance().removeAllActivity();
					AccDataCenter.getInstance().clearAccData();
					Intent intent = new Intent();
					intent.setClass(GatherConfirmActivity.this, AccManageActivity.class);
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
//		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//		RegexpBean otpRB = new RegexpBean(this.getString(R.string.set_otp_no), sipBox_otp.getText().toString().trim(),
//				"atmpass");
//		lists.add(otpRB);
//		if (!RegexpUtils.regexpDate(lists)) {
//			return;
//		}
//		try {
//			otp = sipBox_otp.getValue().getEncryptPassword();
//			otp_RC = sipBox_otp.getValue().getEncryptRandomNum();
//			// 获取tokenId
//			BaseHttpEngine.showProgressDialog();
//			requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap()
//					.get(ConstantGloble.CONVERSATION_ID));
//		} catch (CodeException e) {
//			LogGloble.exceptionPrint(e);
//		}
		
		usbKeyText.checkDataUsbKey(sipBox_otp, sipBoxSmc, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				// 获取tokenId
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			}
		});
		
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnTransActCollectionSubmit();
	}

	/**
	 * @Title: psnTransActCollectionSubmit
	 * @Description: 请求“主动收款提交”的接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActCollectionSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_COLLECTION_SUBMIT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GatherInitiative.PAYEE_NAME, payeeNameStr);
		map.put(GatherInitiative.PAYER_CUST_ID, payerCustIdStr);
		map.put(GatherInitiative.CURRENCY, currencyStr);
		map.put(GatherInitiative.TO_ACCOUNTID, accountIdStr);
		map.put(GatherInitiative.PAYEE_ACT_NO, accountStr);
		map.put(GatherInitiative.PAYEE_MOBILE, payeePhoneStr);
		map.put(GatherInitiative.NOTIFY_PAYEE_AMOUNT, payeeAmmountStr);
		map.put(GatherInitiative.PAYER_NAME, payerNameStr);
		map.put(GatherInitiative.PAYER_CHANNEL, payerTypeStr);
		map.put(GatherInitiative.PAYER_MOBILE, payerPhoneStr);
		map.put(GatherInitiative.REMARK, remarkStr);
		map.put(GatherInitiative.TOKEN, tokenId);
//		map.put(GatherInitiative.OTP, otp);
//		map.put(GatherInitiative.OTP_RC, otp_RC);
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransActCollectionSubmitCallback");
	}

	/**
	 * @Title: requestPsnTransActCollectionSubmitCallback
	 * @Description: 请求“主动收款提交”的接口的回调
	 * @param
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransActCollectionSubmitCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> map = (Map<String, Object>) biiResponseBody.getResult();
		orderNumberStr = (String) map.get(GatherInitiative.NOTIFY_ID);
		showSuccessView();
		if (!getIntent().getBooleanExtra("noNeedSavePayer", false)&&payerSaveCheckBox.isChecked()) {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		requestSavePSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
	}

	/**
	 * @Title: requestSavePSNGetTokenId
	 * @Description: 请求保存付款人所需的token
	 * @param @param conversationId
	 * @return void
	 * @throws
	 */
	public void requestSavePSNGetTokenId(String conversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSN_GETTOKENID);
		biiRequestBody.setConversationId(conversationId);
		HttpManager.requestBii(biiRequestBody, this, "requestSavePSNGetTokenIdCallBack");
	}

	/**
	 * @Title: requestSavePSNGetTokenIdCallBack
	 * @Description: 请求保存付款人所需的token的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	public void requestSavePSNGetTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		requestPsnTransActSavePayer();
	}

	/**
	 * @Title: requestPsnTransActSavePayer
	 * @Description: 请求“主动收款保存常用付款人”接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActSavePayer() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_SAVE_PAYER);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GatherInitiative.PAYER_CUST_ID, payerCustIdStr);
		map.put(GatherInitiative.PAYER_CHANNEL, payerTypeStr);
		map.put(GatherInitiative.PAYER_MOBILE, payerPhoneStr);
		map.put(GatherInitiative.PAYER_NAME, payerNameStr);
		map.put(GatherInitiative.TOKEN, tokenId);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransActSavePayerCallback");
	}

	/**
	 * @Title: requestPsnTransActSavePayerCallback
	 * @Description: 请求“主动收款保存常用付款人”接口的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	public void requestPsnTransActSavePayerCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastInCenter(this, this.getString(R.string.save_payer_success));
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
