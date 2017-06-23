package com.chinamworld.bocmbci.biz.acc.applytermdeposite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
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
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
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
 * 申请定期活期账户确认页
 * @author Administrator
 *
 */
public class ApplyConfirmActivity extends AccBaseActivity implements
		OnClickListener {

	/**主布局*/
	private View mainView;
	/**账户绑定介质账号*/
	private TextView tv_accnum;
	/**绑定介质账户类型*/
	private TextView tv_acctype;
	/**开户类型信息*/
	private TextView tv_opentype_info;
	/**账户用途*/
	private TextView tv_account_purpose;
	/**账户开立原因*/
	private LinearLayout ll_account_open_reason;
	private TextView tv_open_reason;
	private TextView tv_account_open_reason;
	/**加密控件*/
	private SipBox smcSipBox;
	private SipBox otpSipBox;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	/**标识是否有短信验证码、动态口令*/
	private boolean isSmc=false;
	private boolean isOtp=false;
	/**确定按钮*/
	private Button btnConfirm;
	private String info;
	/**随机数*/
	private String randomNumber;
	
	private StringBuffer sbPurpose;
	private StringBuffer sbOpenReason;
	private List<String> purposeList;
	private List<String> openReasonList;
	
	/**开立国籍*/
	private Map<String, Object> countryCode;
	private String code;
	/** 存款管理申请账户标示 */
	private int interestRateFlag;
	/** 资金管理标识 */
	private Boolean isManageFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**为界面标题赋值*/
		setTitle(R.string.acc_apply_title);
		/**添加布局*/
		mainView=addView(R.layout.acc_apply_confirm);
		info=getIntent().getStringExtra("info");
		interestRateFlag = this.getIntent().getIntExtra(Dept.APPLICATION_ACCOUNT_FLAG, 0);
		/**请求随机数*/
		requestForRandomNumber();
	}
	/**
	 * 初始化布局控件
	 */
	public void initView(){
		sbPurpose=new StringBuffer();
		sbOpenReason=new StringBuffer();
		purposeList=AccDataCenter.getInstance().getPurposeList();
		openReasonList=AccDataCenter.getInstance().getReasonList();
		countryCode=AccDataCenter.getInstance().getCountryCode();
		code=(String) countryCode.get(Acc.ACC_QUERY_COUNTRYCODE);
		tv_accnum=(TextView) mainView.findViewById(R.id.tv_accnum);
		tv_accnum.setText(StringUtil.getForSixForString(AccDataCenter.getInstance().getChooseBankAccount().get(Acc.
				ACC_ACCOUNTNUMBER_RES).toString()));
		tv_acctype=(TextView) mainView.findViewById(R.id.tv_acctype);
		tv_acctype.setText(AccBaseActivity.bankAccountType.get(AccDataCenter.getInstance().getChooseBankAccount().
				get(Acc.ACC_ACCOUNTTYPE_RES).toString()));
		tv_opentype_info=(TextView) mainView.findViewById(R.id.tv_acctype_info);
		
		isManageFlag = AccDataCenter.getInstance().isManageFlag();
		if (isManageFlag) {
			tv_opentype_info.setText(this.getString(R.string.acc_choose_fixed));
		} else {
			tv_opentype_info.setText(info);
		}
		
		tv_account_purpose=(TextView) mainView.findViewById(R.id.tv_account_purpose);
		accountValue(purposeList, tv_account_purpose, sbPurpose);
		ll_account_open_reason=(LinearLayout) mainView.findViewById(R.id.ll_account_open_reason);
		if(!code.equals(CHINA)){
			ll_account_open_reason.setVisibility(View.VISIBLE);
		}
		tv_open_reason=(TextView) mainView.findViewById(R.id.tv_open_reason);
		showTitle(tv_open_reason, code);
		tv_open_reason.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_open_reason);
		tv_account_open_reason=(TextView) mainView.findViewById(R.id.tv_account_open_reason);
		accountValue(openReasonList, tv_account_open_reason, sbOpenReason);
		btnConfirm=(Button) mainView.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(this);
		/**根据返回初始化中银E盾加密控件*/
		initSipBox();
	}
	
	/**
	 * 根据返回初始化中银E盾加密控件
	 */
	public void initSipBox(){
		usbKeyText=(UsbKeyText) mainView.findViewById(R.id.sip_usbkey);
		String mconversationId=(String) BaseDroidApp.getInstanse().
				getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		Map<String,Object> resultResponse=(Map<String,Object>)
				AccDataCenter.getInstance().getApplyConfirmResult();
		usbKeyText.Init(mconversationId, randomNumber, resultResponse, ApplyConfirmActivity.this);
		isOtp=usbKeyText.getIsOtp();
		isSmc=usbKeyText.getIsSmc();
		//初始化动态口令控件
		initOtpSipBox();
		//初始化短信验证控件
		initSmcSipBox();
	}
	
	/**
	 * 初始化动态口令控件
	 */
	public void initOtpSipBox(){
		if(isOtp){
			LinearLayout layoutSip=(LinearLayout) mainView.findViewById(R.id.layout_sip);
			layoutSip.setVisibility(View.VISIBLE);
			otpSipBox=(SipBox) mainView.findViewById(R.id.et_cecurity);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(otpSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			otpSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			otpSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			otpSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			otpSipBox.setId(10002);
//			otpSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			otpSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			otpSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			otpSipBox.setSipDelegator(this);
//			otpSipBox.setRandomKey_S(randomNumber);
		}
	}
	
	/**
	 * 初始化短信验证控件
	 */
	public void initSmcSipBox(){
		if(isSmc){
			LinearLayout layoutSmc=(LinearLayout) mainView.findViewById(R.id.ll_sms);
			layoutSmc.setVisibility(View.VISIBLE);
			Button btnSendSms=(Button) mainView.findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(btnSendSms,
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//发送验证码请求
							sendSMSCToMobile();
						}
					});
			smcSipBox=(SipBox) mainView.findViewById(R.id.sip_sms);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//			smcSipBox.setCipherType(SystemConfig.CIPHERTYPE);
//			smcSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBox.setId(10002);
//			smcSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBox.setSipDelegator(this);
//			smcSipBox.setRandomKey_S(randomNumber);
		}
	}
	
	/**
	 * 获取随机数
	 */
	public void requestForRandomNumber(){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody=new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().
				getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}
	
	/**
	 * 获取随机数返回数据
	 */
	public void queryRandomNumberCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse=(BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys=biiResponse.getResponse();
		BiiResponseBody biiResponseBody=biiResponseBodys.get(0);
		randomNumber=(String) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(randomNumber)){
			return;
		}
		//初始化布局控件
		initView();
	}
	
	@Override
	public void onClick(View v) {
		contentCheck();
	}

	/**
	 * 提交校验
	 */
	public void contentCheck(){
		usbKeyText.checkDataUsbKey(otpSipBox, smcSipBox, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				//获取token请求
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId((String)BaseDroidApp.getInstanse()
						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			}
		});
	}
	
	/**
	 * token数据的返回
	 */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token=(String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestApplyResult(token);
	}
	
	/**
	 * 申请定期活期账户结果
	 */
	public void requestApplyResult(String token){
		BiiRequestBody biiRequestBody=new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNAPPLYTERMDEPOSITERESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> parms=new HashMap<String, Object>();
		Map<String,Object> logInfo=(Map<String, Object>) BaseDroidApp.
				getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		parms.put(Acc.ACC_APPLY_RESULT_ACCOUNTID, AccDataCenter.getInstance()
				.getChooseBankAccount().get(Acc.ACC_ACCOUNTID_RES));
		if (isManageFlag) {
			parms.put(Acc.ACC_APPLY_RESULT_ACCOUTTYPE, AccBaseActivity.accountTypeList.get(10));
			parms.put(Acc.ACC_APPLY_PLAN_ACCOUNTTYPESMS, AccBaseActivity.bankAccountType.get(AccBaseActivity.accountTypeList.get(10)));
		} else {
			parms.put(Acc.ACC_APPLY_RESULT_ACCOUTTYPE, getIntent().getStringExtra(
					Acc.ACC_APPLY_PLAN_ACCOUNTTYPE));
			parms.put(Acc.ACC_APPLY_RESULT_ACCOUNTTYPESMS, getIntent().getStringExtra(
					Acc.ACC_APPLY_PLAN_ACCOUNTTYPESMS));
		}
		parms.put(Acc.ACC_APPLY_RESULT_NAME,logInfo.get(Login.CUSTOMER_NAME));
//		parms.put(Acc.ACC_APPLY_RESULT_ACCOUNTPURPOSE, getIntent().getStringExtra(
//				Acc.ACC_APPLY_PLAN_ACCOUNTPURPOSE));
//		if(code.equals(CHINA)){
//			parms.put(Acc.ACC_APPLY_RESULT_OPENINGREASON, null);
//		}else{
//			parms.put(Acc.ACC_APPLY_RESULT_OPENINGREASON, getIntent().getStringExtra(
//					Acc.ACC_APPLY_PLAN_OPENINGREASON));
//		}
		parms.put(Acc.ACC_APPLY_RESULT_TOKEN, token);
		/**安全工具获取参数*/
		usbKeyText.InitUsbKeyResult(parms);
		SipBoxUtils.setSipBoxParams(parms);
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this, "applyResultCallBack");
	}
	
	/**
	 * 申请定期活期账户结果返回处理
	 */
	public void applyResultCallBack(Object resultObj){
		BiiResponse biiResponse=(BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys=biiResponse.getResponse();
		BiiResponseBody biiResponseBody=biiResponseBodys.get(0);
		Map<String,Object> result=(Map<String, Object>) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(result)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.bond_tran_error));
			return;
		}
		AccDataCenter.getInstance().setApplyResultMap(result);
		BaseHttpEngine.dissMissProgressDialog();
		String applyStatus=(String) result.get(Acc.ACC_APPLY_RESULT_APPLYSTATUS);
		if(applyStatus.equals("1")){
			if (interestRateFlag == APPLICATION_ACCOUNT) { // 存款管理申请账户
				Intent intent=new Intent(this,ApplyResultActivity.class); //开户成功
				intent.putExtra("info", info);
				intent.putExtra(Dept.APPLICATION_ACCOUNT_FLAG , interestRateFlag);
				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			} else if (isManageFlag){
				Intent intent=new Intent(this,ApplyResultActivity.class); //开户成功
				intent.putExtra("info", info);
				startActivityForResult(intent, 10023);
			} else {
				Intent intent=new Intent(this,ApplyResultActivity.class); //开户成功
				intent.putExtra("info", info);
				startActivity(intent);
			}
		}else{
			//开户失败
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_open_fail));
		}
		
	}
}
