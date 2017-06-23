package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;
/**
 * 我的信用卡 外币账单自动购汇  确认界面
 * 
 * @author sunh
 * 
 */
public class CrcdSetAutoForeignPayOffConfirmActivity extends CrcdBaseActivity{
	private View view;	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;	
	/** 开通标示   “1000”： 开通“0000”： 关闭*/
	private String openFlag=null;
	TextView card_accountNumber;
	TextView card_foreignpayoffstatus_openflag;
	TextView tv_acc_loss_actnum;
	Button sureButton;
	
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	Map<String, Object> factorMap;

	/** 动态口令布局 */
	private LinearLayout ll_active_code;
	/** 手机交易码布局 */
	private LinearLayout ll_smc;

	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_password_setup));
		
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);	
		openFlag= getIntent().getStringExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES);
			
		if(openFlag.equals("1000")){			
			// 为界面标题赋值
			setTitle(this.getString(R.string.mycrcd_setautoforeignpayoff_open));
		}else{
				// 为界面标题赋值
			setTitle(this.getString(R.string.mycrcd_setautoforeignpayoff_close));
		}
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		BaseHttpEngine.showProgressDialog();
		requestForRandomNumber();


	}
	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}
	
	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}

		init();
	}
	/** 初始化界面 */
	@SuppressWarnings("unchecked")
	private void init() {
		view = addView(R.layout.mycrcd_setautoforeignpayoff_confirm_layout);
		card_foreignpayoffstatus_openflag=(TextView) view.findViewById(R.id.card_foreignpayoffstatus_openflag);
		tv_acc_loss_actnum=(TextView) view.findViewById(R.id.tv_acc_loss_actnum);
		if(openFlag.equals("1000")){
			card_foreignpayoffstatus_openflag.setText("开通");
			
			tv_acc_loss_actnum.setText(getResources().getString(R.string.mycrcd_setautoforeignpayoff_confirm_open));
			}else{
				card_foreignpayoffstatus_openflag.setText("关闭");	
				tv_acc_loss_actnum.setText(getResources().getString(R.string.mycrcd_setautoforeignpayoff_confirm_close));
				
			}
		card_accountNumber=(TextView)view.findViewById(R.id.card_accountNumber);
		card_accountNumber.setText(StringUtil.getForSixForString(accountNumber));		
//		factorMap = CrcdSetAutoForeignPayOffPreActivity.returnList;
		factorMap=(Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(Crcd.CRCD_PSNCRCDSETAUTOFOREGINPAYOFFPRE);		
		factorList = (List<Map<String, Object>>) factorMap.get(Crcd.FACTORLIST);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

//		initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

	
		
		sureButton = (Button) findViewById(R.id.sureButton);
		
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				checketDate();
				
			}
		});

	}
	
	/**安全工具数据校验*/
	private void checketDate(){
		usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				pSNGetTokenId();
		
//				requestPsnCrcdSetAutoForeignPayOff();
			}
		},true);
	}
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 是否上传短信验证码 */
	private boolean isSmc = false;
	
	private void initSipBox(){
		usbKeyText = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
		usbKeyText.Init(commConversationId, randomNumber, factorMap, this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		initOptAndSmc();
	}
	
	public void initOptAndSmc() {
		if(isOtp){
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
		// add by 2016年9月12日 luqp 修改
		sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//		// sipBoxActiveCode.setLayoutParams(param);
//		sipBoxActiveCode.setTextColor(getResources().getColor(android.R.color.black));
//		// sipBoxActiveCode.setGravity(Gravity.CENTER_VERTICAL);
//		sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setId(10002);
//		sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxActiveCode.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxActiveCode.setSingleLine(true);
//		sipBoxActiveCode.setSipDelegator(this);
//		sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
		// ll_activecode_sip.addView(sipBoxActiveCode);
		}
		if(isSmc){
		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		ll_smc.setVisibility(View.VISIBLE);
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
		// sipBoxSmc.setLayoutParams(param);
		// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
		sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
		// add by 2016年9月12日 luqp 修改
		sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setId(10002);
//		sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBoxSmc.setSingleLine(true);
//		sipBoxSmc.setSipDelegator(this);
//		sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		// ll_smc_sip.addView(sipBoxSmc);

		Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送验证码到手机
				sendMSCToMobile();
			}
		});
		}

	}
	protected void requestPsnCrcdSetAutoForeignPayOff(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSETAUTOFOREGINPAYOFF);

		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);////账户ID
//		map.put(Crcd.CRCD_TOACTIVEACT, accountNumber);//卡号
		map.put(Crcd.CRCD_GOUHUIOPENFLAG_RES, openFlag);//开通标识
//		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		map.put(Crcd.CRCD_TOKEN, tokenId);
		
		usbKeyText.InitUsbKeyResult(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdSetAutoForeignPayOffCallBack");
		
	}
	public void PsnCrcdSetAutoForeignPayOffCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
//		returnMap = (Map<String, Object>) body.getResult();
		String status=body.getStatus();
//		if("01".equals(status)){
		Intent it = new Intent(CrcdSetAutoForeignPayOffConfirmActivity.this, CrcdSetAutoForeignPayOffSuccesActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_GOUHUIOPENFLAG_RES, openFlag);
//		startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);	
//		}else{
//		
//		}
	}
	
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
	// TODO Auto-generated method stub
	super.aquirePSNGetTokenIdCallBack(resultObj);
	// 信用卡服务设置定制信息结果
	requestPsnCrcdSetAutoForeignPayOff();
}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:// 成功
			setResult(RESULT_OK);
			finish();
				break;
			default:
				break;


		case RESULT_CANCELED:// 失败
			break;
		}
	}
	
}
