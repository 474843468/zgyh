package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;


/**
 * 我的信用卡 消费密码设置  确认界面
 * 
 * @author sunh
 * 
 */
public class CrcdSetConsumePWConfirmActivity extends CrcdBaseActivity {
	private View view;	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;
	/** 密码 */
	private String password1;
	/** 确认密码 */
	private String password2;
	private String password_RC1;
	private String password_RC2;
	TextView card_accountNumber;
	
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
	
	private String payorsearch;
//	private Boolean fromActive;
	private TextView tv_acc_loss_actnum;
	
	/** 加密控件 */
	private SipBox sipBox1;
	/** 加密控件 */
	private SipBox sipBox2;
	TextView txt1;
	TextView txt2;
	LinearLayout ll1,ll2;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_password_setup));
		
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);

		payorsearch= getIntent().getStringExtra("payorsearch");
	
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	
		
		
		BaseHttpEngine.showProgressDialog();	
		// 请求conversationId
//		requestCommConversationId();
		requestForRandomNumber();
//		BaseHttpEngine.showProgressDialog();

	}
	
	
	/**
	 * 请求conversationId 回调
	 * 
	 * @param resultObj 返回结果
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
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
	private void init() {
		
		view = addView(R.layout.mycrcd_password_confirm_layout);
		tv_acc_loss_actnum=(TextView)view.findViewById(R.id.tv_acc_loss_actnum);
		txt1=(TextView)view.findViewById(R.id.txt1);
		txt2=(TextView)view.findViewById(R.id.txt2);
		ll1=(LinearLayout)view.findViewById(R.id.ll1);
		ll2=(LinearLayout)view.findViewById(R.id.ll2);
		
		
		if(payorsearch.equals("search")){
			setTitle(this.getString(R.string.mycrcd_search_setup));
			txt1.setText(getResources().getString(R.string.mycrcd_set_searchpassword));
			txt2.setText(getResources().getString(R.string.mycrcd_confirm_searchpassword));
		
		}else{
//			if(fromActive){
//				ll1.setVisibility(View.GONE);
//				ll2.setVisibility(View.GONE);
//				setTitle(this.getString(R.string.mycrcd_creditcard_active_title));
//				txt1.setText(getResources().getString(R.string.mycrcd_set_phonepassword));
//				txt2.setText(getResources().getString(R.string.mycrcd_confirm_phonepassword));	
//			}else{				
				setTitle(this.getString(R.string.mycrcd_password_setup));				
				txt1.setText(getResources().getString(R.string.mycrcd_set_paypassword));
				txt2.setText(getResources().getString(R.string.mycrcd_confirm_paypassword));
//			}
			
		}
		
		
		card_accountNumber=(TextView)view.findViewById(R.id.card_accountNumber);
		card_accountNumber.setText(StringUtil.getForSixForString(accountNumber));
		
		
	
		
		// 加密控件1 -- start
				LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.ll_sip1);
				sipBox1 = new SipBox(this);
//				 sipBox1.setHint(this.getResources().getString(R.string.hint_reg_pwd));
				initPasswordSipBox(sipBox1);
				linearLayout1.addView(sipBox1);
				// 加密控件1 -- end

				// 加密控件2 -- start
				LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.ll_sip2);
				sipBox2 = new SipBox(this);
//				 sipBox2.setHint(this.getResources().getString(R.string.hint_findpwd_pwd_cof));
				initPasswordSipBox(sipBox2);
				linearLayout2.addView(sipBox2);

//		factorMap = CrcdSetConsumePWPreActivity.returnList;
		factorMap=(Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(Crcd.CRCD_PSNCRCDSETCONSUMEPWPER);
		
		factorList = (List<Map<String, Object>>) factorMap.get(Crcd.FACTORLIST);
		commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));

//		initOptAndSmc()
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();

		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_service_tiaozheng_setup),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(2);
		
		sureButton = (Button) findViewById(R.id.sureButton);
		
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				RegexpBean passReg = new RegexpBean(CrcdSetConsumePWConfirmActivity.this.getString(R.string.password_no_label), sipBox1.getText().toString().trim(), "phonepass");
				lists.add(passReg);
				RegexpBean passEnsureReg = new RegexpBean(CrcdSetConsumePWConfirmActivity.this.getString(R.string.password_no_label), sipBox2.getText().toString().trim(), "phonepass");
				lists.add(passEnsureReg);
				if (!RegexpUtils.regexpDate(lists)){
					return;
				}

				try {
					password1 = sipBox1.getValue().getEncryptPassword();
					password_RC1 = sipBox1.getValue().getEncryptRandomNum();
				

				} catch (CodeException e) {
					BaseDroidApp.getInstanse().createDialog(null, "密码格式不正确，请重新输入");
				}
				try {
					password2 = sipBox2.getValue().getEncryptPassword();
					password_RC2 = sipBox2.getValue().getEncryptRandomNum();
				} catch (CodeException e) {
					BaseDroidApp.getInstanse().createDialog(null, "确认密码格式不正确，请重新输入");
				}
			
				checketDate();
				
			}
		});

	}
	
	/**
	 * 初始化密码控件
	 * 
	 * @param sipBox
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		LinearLayout.LayoutParams param1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		sipBox.setLayoutParams(param1);
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setId(10002);
		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
		sipBox.setPasswordRegularExpression(CheckRegExp.ATM_PASSWORD);
		sipBox.setRandomKey_S(randomNumber);
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		sipBox.setSipDelegator(this);
//		InputFilter[] filters1 = { new InputFilter.LengthFilter(20) };
//		sipBox.setFilters(filters1);
	}
	/**安全工具数据校验*/
	private void checketDate(){
		usbKeyText.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				pSNGetTokenId();
		
//				requestPsnCrcdSetConsumePW();
			}
		});
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
			// add by 2016年9月12日 luqp 修改
			sipBoxSmc.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
			sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
			SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//		// sipBoxSmc.setLayoutParams(param);
//		// sipBoxSmc.setGravity(Gravity.CENTER_VERTICAL);
//		sipBoxSmc.setTextColor(getResources().getColor(android.R.color.black));
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
	
	protected void requestPsnCrcdSetConsumePW(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSETCONSUMEPW);

		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, Object> map = new HashMap<String, Object>();
		if(accountId!=null){
			map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);//	
		}
		map.put(Crcd.CRCD_TOACTIVEACT, accountNumber);//卡号
		map.put(Crcd.CRCD_CONSUMEPW_RES, password1);//
		map.put(Crcd.CRCD_CONSUMEPWCONFIRM_RES, password2);//
		map.put(Crcd.CRCD_CONSUMEPW_RC_RES, password_RC1);//
		map.put(Crcd.CRCD_CONSUMEPWCONFIRM_RC_RES, password_RC2);//
		map.put(Crcd.CRCD_TOKEN, tokenId);//
//		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
//		GetPhoneInfo.initActFirst(this);
//		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdSetConsumePWCallBack");
		
	}
	
	public void psnCrcdSetConsumePWCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
//		returnMap = (Map<String, Object>) body.getResult();

		Intent it = new Intent(CrcdSetConsumePWConfirmActivity.this, CrcdSetConsumePWSuccessActivity.class);

		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra("payorsearch", payorsearch);//

//		startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}
	

	
	protected void requestPsnCrcdSetQueryPW(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSETQUERYPW);

		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, Object> map = new HashMap<String, Object>();
		if(accountId==null){
			map.put(Crcd.CRCD_ACCOUNTID_RES, "");//	
		}else{
			map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);//	
		}
		map.put(Crcd.CRCD_TOACTIVEACT, accountNumber);//卡号
		map.put(Crcd.CRCD_QUERYPW_RES, password1);//
		map.put(Crcd.CRCD_QUERYPWCONFIRM_RES, password2);//
		map.put(Crcd.CRCD_QUERYPW_RC_RES, password_RC1);//
		map.put(Crcd.CRCD_QUERYPWCONFIRM_RC_RES, password_RC2);//
		map.put(Crcd.CRCD_TOKEN, tokenId);//
//		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdSetQueryPWCallBack");
		
	}
	
	public void PsnCrcdSetQueryPWCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
//		returnMap = (Map<String, Object>) body.getResult();
		Intent it = new Intent(CrcdSetConsumePWConfirmActivity.this, CrcdSetConsumePWSuccessActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra("payorsearch", payorsearch);//
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}
	
	
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.aquirePSNGetTokenIdCallBack(resultObj);
		// 信用卡服务设置定制信息结果
		
		
		if(payorsearch.equals("pay")){
			//信用卡消费密码设置提交
			requestPsnCrcdSetConsumePW();
		}else{
			
			//信用卡查询密码设置提交
			requestPsnCrcdSetQueryPW();
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		BaseDroidApp.getInstanse().setCurrentAct(this);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		case RESULT_CANCELED:// 失败
			break;
		}
	}	
}
