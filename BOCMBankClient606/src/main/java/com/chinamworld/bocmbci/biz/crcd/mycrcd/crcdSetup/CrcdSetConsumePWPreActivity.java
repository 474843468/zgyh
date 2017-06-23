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
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的信用卡 消费密码设置  预交易界面
 * 
 * @author sunh
 * 
 */

public class CrcdSetConsumePWPreActivity extends CrcdBaseActivity{
	private View view;
	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;
	
	/** 随机数 */
	private String randomNumber;
	

	private static final String TAG = "ResetPwdActivity";
	/** 加密控件 */
	private SipBox sipBox1;
	/** 加密控件 */
	private SipBox sipBox2;
	/** 动态因子layout */
//	private LinearLayout linearLayout;
	
	/** 动态口令编辑框 */
	// private EditText etActivecode;
	/** 密码 */
	private String password1;
	/** 确认密码 */
	private String password2;
	/** 随机数 */
	private String password_RC1;
	private String password_RC2;
	/** 上一步 */
	// private Button lastBtn;
	/** 确认 */
//	private Button confirmBtn;
	// private Button aquireActive;
	/** 证件类型 */
//	private String identityType;
	/** 证件号码 */
//	private String identityNumber;
	/** 手机验证码layout */
//	private LinearLayout mobileLayout;
	/** 手机验证码 */
//	private SipBox mobileSip;
	/** 动态口令Layout */
//	private LinearLayout activeLayout;
	/** 动态口令 */
//	private SipBox activeSip;
	/** 绑定标示 */
//	private String combineFlag;
	/** string动态口令 */
	private String otp;
	private String otp_RC;
	/** String手机验证码 */
	private String smc;
	private String smc_RC;
	private String conversationId;
	private String payorsearch;
	
	TextView card_accountNumber;
	TextView txt1;
	TextView txt2;
	
	Button sureButton;
	Boolean fromActive;
	LinearLayout ll1,ll2;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_password_setup));
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		payorsearch= getIntent().getStringExtra("payorsearch");
		fromActive=getIntent().getBooleanExtra("fromActive", false);
		
		view = addView(R.layout.mycrcd_password_setup_layout);
		card_accountNumber=(TextView)view.findViewById(R.id.card_accountNumber);
		card_accountNumber.setText(StringUtil.getForSixForString(accountNumber));
		txt1=(TextView)view.findViewById(R.id.txt1);
		txt2=(TextView)view.findViewById(R.id.txt2);
		ll1=(LinearLayout)view.findViewById(R.id.ll1);
		ll2=(LinearLayout)view.findViewById(R.id.ll2);
		
		if(fromActive){
			ll1.setVisibility(View.VISIBLE);
			ll2.setVisibility(View.VISIBLE);
			setTitle(this.getString(R.string.mycrcd_creditcard_active_title));
			txt1.setText(getResources().getString(R.string.mycrcd_set_phonepassword));
			txt2.setText(getResources().getString(R.string.mycrcd_confirm_phonepassword));	
		}else{
		
			if(payorsearch.equals("search")){
				setTitle(this.getString(R.string.mycrcd_search_setup));
				txt1.setText(getResources().getString(R.string.mycrcd_set_searchpassword));
				txt2.setText(getResources().getString(R.string.mycrcd_confirm_searchpassword));
			
			}else{							
					setTitle(this.getString(R.string.mycrcd_password_setup));				
					txt1.setText(getResources().getString(R.string.mycrcd_set_paypassword));
					txt2.setText(getResources().getString(R.string.mycrcd_confirm_paypassword));
				}
			
		}
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		if(fromActive){
			requestForRandomNumber();
		}else{
			BaseHttpEngine.showProgressDialog();	
//			 请求conversationId
			requestCommConversationId();	
		}

	}
	
	
	private void init() {
		if(fromActive){
		// 加密控件1 -- start
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.ll_sip1);
		sipBox1 = new SipBox(this);
//		 sipBox1.setHint(this.getResources().getString(R.string.hint_reg_pwd));
		initPasswordSipBox(sipBox1);
		linearLayout1.addView(sipBox1);
		// 加密控件1 -- end

		// 加密控件2 -- start
		LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.ll_sip2);
		sipBox2 = new SipBox(this);
//		 sipBox2.setHint(this.getResources().getString(R.string.hint_findpwd_pwd_cof));
		initPasswordSipBox(sipBox2);
		linearLayout2.addView(sipBox2);
		// 加密控件2 -- end
		}
		sureButton=(Button)view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(fromActive){
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					RegexpBean passReg = new RegexpBean(CrcdSetConsumePWPreActivity.this.getString(R.string.password_no_label), sipBox1.getText().toString().trim(), "phonepass");
					lists.add(passReg);
					RegexpBean passEnsureReg = new RegexpBean(CrcdSetConsumePWPreActivity.this.getString(R.string.password_no_label), sipBox2.getText().toString().trim(), "phonepass");
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
					
					}
					
					
					if(fromActive){
					Intent data=new Intent();					
					data.putExtra(Crcd.CRCD_QUERYPW_RES, password1);//
					data.putExtra(Crcd.CRCD_QUERYPWCONFIRM_RES, password2);//
					data.putExtra(Crcd.CRCD_QUERYPW_RC_RES, password_RC1);//
					data.putExtra(Crcd.CRCD_QUERYPWCONFIRM_RC_RES, password_RC2);//
					data.putExtra("randomNumber",randomNumber);//
					setResult(100, data);
					finish();	
						
					}else{
						BaseHttpEngine.showProgressDialog();
						if(payorsearch.equals("pay")){
							requestGetSecurityFactor(psnXisofeisecurityId);
						}else{
							requestGetSecurityFactor(psnChaxunsecurityId);
						}	
					}
					
					
					
				}
			});
	}
	
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(payorsearch.equals("pay")){
					//信用卡消费密码设置预交易
					requestPsnCrcdSetConsumePWPre();
				}else{
					//信用卡查询密码设置预交易
					requestPsnCrcdSetQueryPWPre();
				}
				
				
			}
		});
	}
	//信用卡消费密码设置预交易
	protected void requestPsnCrcdSetConsumePWPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSETCONSUMEPWPER);

		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, String> map = new HashMap<String, String>();
		if(accountId!=null){
			map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);//	
		}
		
		map.put(Crcd.CRCD_TOACTIVEACT, accountNumber);//卡号
//		map.put(Crcd.CRCD_CONSUMEPW_RES, password1);//
//		map.put(Crcd.CRCD_CONSUMEPWCONFIRM_RES, password2);//
//		map.put(Crcd.CRCD_CONSUMEPW_RC_RES, password_RC1);//
//		map.put(Crcd.CRCD_CONSUMEPWCONFIRM_RC_RES, password_RC2);//
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdSetConsumePWPreCallBack");
	}

	

	public void psnCrcdSetConsumePWPreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		 Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(Crcd.CRCD_PSNCRCDSETCONSUMEPWPER, returnList);
		Intent it = new Intent(CrcdSetConsumePWPreActivity.this, CrcdSetConsumePWConfirmActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);		
		it.putExtra("payorsearch", payorsearch);//
		
//		startActivity(it);
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
	}
	
	//信用卡查询密码设置预交易	
	protected void requestPsnCrcdSetQueryPWPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDSETQUERYPWPER);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId
		Map<String, String> map = new HashMap<String, String>();
		if(accountId==null){
			map.put(Crcd.CRCD_ACCOUNTID_RES, "");//	
		}else{
			map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);//	
		}
		
		map.put(Crcd.CRCD_TOACTIVEACT, accountNumber);//卡号
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdSetQueryPWPreCallBack");
	}



	public void PsnCrcdSetQueryPWPreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		 Map<String, Object> returnList= (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(Crcd.CRCD_PSNCRCDSETCONSUMEPWPER, returnList);
		Intent it = new Intent(CrcdSetConsumePWPreActivity.this, CrcdSetConsumePWConfirmActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra("payorsearch", payorsearch);//
		it.putExtra("fromActive", fromActive);//
		startActivityForResult(it,
				ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
	}
	
	
	/**
	 * 初始化密码控件
	 * 
	 * @param sipBox
	 */
	private void initPasswordSipBox(SipBox sipBox) {
		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
		LinearLayout.LayoutParams param1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// add by 2016年9月12日 luqp 修改
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		sipBox.setTextColor(getResources().getColor(android.R.color.black));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox.setLayoutParams(param1);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setId(10002);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.ATM_PASSWORD);
//		sipBox.setRandomKey_S(randomNumber);
//		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBox.setSipDelegator(this);
//		InputFilter[] filters1 = { new InputFilter.LengthFilter(20) };
//		sipBox.setFilters(filters1);
	}
	/**
	 * 请求conversationId 回调
	 * 
	 * @param resultObj 返回结果
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
//		if(fromActive){
//		requestForRandomNumber();
//		}else{
			BaseHttpEngine.dissMissProgressDialog();	
			init();
//		}
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
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();	
		// 加密控件设置随机数
		init();
	
	}

	@Override
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
