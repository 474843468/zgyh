package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfca.mobile.sip.SipBox;

public class GoldbounsRegisterMessageActivity extends GoldBonusBaseActivity {

	private Button bremit_confirm_info_ok;
	/** 加密控件里的随机数 */
	private String randomNumber;
	/** 中银E盾 */
	private UsbKeyText usbKeytext;
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 手机交易码布局 */
	private LinearLayout layout_sms;
	/** 动态口令布局 */
	private LinearLayout layout_sip;
//	private LinearLayout layout_usbkey;
	// 获取按钮
	private Button smsbtn;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	private TextView bremit_acc;
	private TextView money_type;
	// 最上面的textvieew
	private TextView titleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (getIntent().getStringExtra("isFirst").equals("2")) {
//			setTitle(R.string.main_menu32);
			getBackgroundLayout().setTitleNewText(R.string.main_menu32);
			getBackgroundLayout().setRightButtonNewText(null);
		} else {
//			setTitle(R.string.goldbonus_account_manager);
			getBackgroundLayout().setTitleNewText(R.string.goldbonus_account_manager);
			getBackgroundLayout().setRightButtonNewText(null);
		}
		setContentView(R.layout.goldbouns_register_confirm_info);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		if(getIntent().getStringExtra("isFirst").equals("2")){
			((LinearLayout)findViewById(R.id.phonenumber_layout)).setVisibility(View.VISIBLE);
		}else {
			((LinearLayout)findViewById(R.id.phonenumber_layout)).setVisibility(View.GONE);
		}
		bremit_confirm_info_ok = (Button) findViewById(R.id.bremit_confirm_info_ok);
		usbKeytext = (UsbKeyText) findViewById(R.id.sip_usbkey);
		usbKeytext.setUsbKeyLabelColor(getResources().getColor(R.color.boc_text_color_common_gray));//设置颜色
		bremit_acc = (TextView) findViewById(R.id.bremit_acc);
		money_type = (TextView) findViewById(R.id.money_type);

		titleText = (TextView) findViewById(R.id.titleText);
		// 2为第一次签约
		if (getIntent().getStringExtra("isFirst").equals("2")) {
			titleText.setText("请确认");
		} else {
			titleText.setText("请确认新交易账户");
		}
		((TextView) findViewById(R.id.phone_number)).setText(GoldbonusLocalData
				.getInstance().phoneNumber);
		bremit_acc.setText(StringUtil.getForSixForString(getIntent()
				.getStringExtra("accountNumber")));
		money_type.setText(GoldbonusLocalData.accountType.get(getIntent()
				.getStringExtra("accountType")));
		// 动态口令
		// 手机交易码
		sipBoxSmc = (SipBox) findViewById(R.id.sip_sms);
		sipBoxActiveCode = (SipBox) findViewById(R.id.et_cecurity_ps);
		layout_sms = (LinearLayout) findViewById(R.id.layout_sms);
		layout_sip = (LinearLayout) findViewById(R.id.layout_sip);
//		layout_usbkey = (LinearLayout) findViewById(R.id.layout_usbkey);
		smsbtn = (Button) findViewById(R.id.smsbtn);
		bremit_confirm_info_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				checkDate();
				// TODO Auto-generated method stub

			}
		});
		// 请求随机数
		requestForRandomNumber();

	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		/** 随机数 */
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 关闭通讯框
		BiiHttpEngine.dissMissProgressDialog();
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// 1为账户变更
		if (getIntent().getStringExtra("isFirst").equals("1")) {
			usbKeytext
					.Init(mmconversationId,
							randomNumber,
							GoldbonusLocalData.getInstance().PsnGoldBonusModifyAccountConfirmMap,
							this);
		} else {
			usbKeytext.Init(mmconversationId, randomNumber,
					GoldbonusLocalData.getInstance().RegisterAccountConfirmMap,
					this);
		}

		isOtp = usbKeytext.getIsOtp();
		isSms = usbKeytext.getIsSmc();
		// combinIdConfirm.setVisibility(View.VISIBLE);
//		layout_usbkey.setVisibility(View.VISIBLE);
		if (isOtp) {
			layout_sip.setVisibility(View.VISIBLE);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
			// 动态口令
//			sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//			sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setId(10002);
//			sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//			sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			sipBoxActiveCode.setSingleLine(true);
//			sipBoxActiveCode.setSipDelegator(this);
//			sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			sipBoxActiveCode.setRandomKey_S(randomNumber);
		}
		if (isSms) {
			// ll_smc.setVisibility(View.VISIBLE);
			layout_sms.setVisibility(View.VISIBLE);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
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
//			sipBoxSmc.setRandomKey_S(randomNumber);
			SmsCodeUtils.getInstance().addSmsCodeListner(smsbtn,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送验证码到手机
							sendMSCToMobile();
						}
					});
		}
		// }
	}

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

	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全认证工具 */
		usbKeytext.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						BaseHttpEngine.showProgressDialog();
						// 先请求token调用4号接口贵金属积利账户设定
						pSNGetTokenId();
					}
				});
	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 1为账户变更
		if (getIntent().getStringExtra("isFirst").equals("2")) {
			// 调用4接口
			requestPsnGoldBonusRegisterAccount(tokenId);
		} else {
			// 调用6接口
			requestPsnGoldBonusModifyAccount(tokenId);
		}

	}

	private void requestPsnGoldBonusRegisterAccount(String tokenId) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("token", tokenId);
		// paramMap.put("_combinId", _combinId);// 安全因子
		paramMap.put("accountId", GoldbonusLocalData.getInstance().accountId);// 账户标识
		paramMap.put("accountNumber",
				GoldbonusLocalData.getInstance().accountNumber);// 账号
		paramMap.put("custPhoneNum", GoldbonusLocalData.getInstance().phoneNumber);//手机号
		// 手机验证码
		if (isSms) {
			paramMap.put("Smc", sipBoxSmc.getText());
		} else if (isOtp) {
			// 动态口令
			paramMap.put("Otp", sipBoxActiveCode);
		} else {
			// CA密文
			paramMap.put(
					"_signedData",
					(GoldbonusLocalData.getInstance().RegisterAccountConfirmMap)
							.get(GoldBonus.Gold_PLAINDATA));
		}
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(paramMap);
		SipBoxUtils.setSipBoxParams(paramMap);
		getHttpTools().requestHttp(GoldBonus.PSNGOLDBONUSREGISTERACCOUNT,
				"requestPsnGoldBonusRegisterAccountCallBack", paramMap, true);

	}

	public void requestPsnGoldBonusRegisterAccountCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();

		Intent intent = new Intent();
		intent.setClass(GoldbounsRegisterMessageActivity.this,
				GoldbounsRegisterSusccessActivity.class);
		intent.putExtra("accountNumber",
				getIntent().getStringExtra("accountNumber"));
		intent.putExtra("accountType", getIntent()
				.getStringExtra("accountType"));
		intent.putExtra("isFirst", getIntent().getStringExtra("isFirst"));
		startActivity(intent);
	}

	private void requestPsnGoldBonusModifyAccount(String tokenId) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("token", tokenId);
		// paramMap.put("_combinId", _combinId);// 安全因子
		paramMap.put("oldAccountId",
				GoldbonusLocalData.getInstance().accountIdOld);// 账户标识
		paramMap.put("newAccountId",
				GoldbonusLocalData.getInstance().accountIdNew);// 账户标识
		paramMap.put("newAccountNumber",
				GoldbonusLocalData.getInstance().accountNumberNew);// 账号
		// 手机验证码
		if (isSms) {
			paramMap.put("Smc", sipBoxSmc.getText());
		} else if (isOtp) {
			// 动态口令
			paramMap.put("Otp", sipBoxActiveCode);
		} else {
			// CA密文
			paramMap.put(
					"_signedData",
					(GoldbonusLocalData.getInstance().PsnGoldBonusModifyAccountConfirmMap)
							.get(GoldBonus.Gold_PLAINDATA));
		}

		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(paramMap);
		SipBoxUtils.setSipBoxParams(paramMap);

		getHttpTools().requestHttp(GoldBonus.PSNGOLDBONUSMODIFYACCOUNT,
				"requestPsnGoldBonusModifyAccountCallBack", paramMap, true);
	}

	public void requestPsnGoldBonusModifyAccountCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		Intent intent = new Intent();
		intent.setClass(GoldbounsRegisterMessageActivity.this,
				GoldbounsRegisterSusccessActivity.class);
		intent.putExtra("accountNumber",
				getIntent().getStringExtra("accountNumber"));
		intent.putExtra("accountType", getIntent()
				.getStringExtra("accountType"));
		intent.putExtra("isFirst", getIntent().getStringExtra("isFirst"));
		startActivity(intent);
	}

}
