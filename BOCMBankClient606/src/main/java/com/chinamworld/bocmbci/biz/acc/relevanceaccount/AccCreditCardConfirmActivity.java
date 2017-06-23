package com.chinamworld.bocmbci.biz.acc.relevanceaccount;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 信用卡关联确认信息页
 * 
 * @author wangmengmeng
 * 
 */
public class AccCreditCardConfirmActivity extends AccBaseActivity implements
		OnClickListener {
	/** 信用卡关联确认页 */
	private View view;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;
	/** 待关联账户类型 */
	private TextView acc_relevance_type;
	private String relevance_type;
	/** 待关联账号 */
	private TextView acc_relevance_actnum;
	private String relevance_actnum;
	/** 上一步 */
	private Button btn_last;
	/** 确定 */
	private Button btn_confirm;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	/** 账户自助关联返回的信用卡信息 */
	private Map<String, String> creditMap;
	/** 账户自助关联预交易返回信息 */
	private Map<String, Object> preResultMap;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 动态口令 */
	private String otpStr;
	/** 手机交易码 */
	private String smcStr;
	/** 加密之后加密随机数 */
	private String otp_password_RC = "";
	private String smc_password_RC = "";
	/** 选择的电子现金账户复选框 */
	private CheckBox checkbox;
	/** 信用卡是否关联 */
	private String isflag;
	/** 是否有电子现金账户 */
	private String ishaveCash;
	/** 电子现金账户是否关联 */
	private String linkEleCashAcctFlag;
	/** 关联标志 */
	private String linkAcctFlag;
	/** 电子现金账户 */
	private LinearLayout checkBox_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_relevance_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_relevanceaccount_iccredit_confirm);
		// 右上角按钮点击事件
		setRightBtnClick(this);
		setBackBtnClick(backBtnClick);
		//请求随机数
		requestForRandomNumber();
		// 初始化界面
//		init();
	}

	/** 左侧返回按钮点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	/** 初始化界面 */
	@SuppressWarnings("ResourceType")
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_relevance_step1),
						this.getString(R.string.acc_relevance_step2),
						this.getString(R.string.acc_relevance_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		// 通过账户自助关联结果取到要用的信用卡信息
		preResultMap = AccDataCenter.getInstance().getRelevancePremap();
		creditMap = (Map<String, String>) (preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_RELEVANCECREDITACCOUNT_RES));
		/** 安全因子数组对象 */
		Map<String, Object> securityentity = (Map<String, Object>) preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_SECURITYENTITY_RES);
		factorList = (List<Map<String, Object>>) securityentity
				.get(Acc.RELEVANCEACCPRE_ACC_FACTORLIST_RES);
//		requestForRandomNumber();
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		if(isOtp){
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);
		ll_active_code.setVisibility(View.VISIBLE);
		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setId(10002);
//		sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxActiveCode.setSingleLine(true);
//		sipBoxActiveCode.setSipDelegator(this);
//		sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
		}
		if(isSms){
		// 手机交易码
		ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
		ll_smc.setVisibility(View.VISIBLE);
		// 手机交易码
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
		// add by 2016年9月12日 luqp 修改
		SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setId(10002);
//		sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBoxSmc.setSingleLine(true);
//		sipBoxSmc.setSipDelegator(this);
//		sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		Button smsBtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 发送验证码到手机
						sendMSCToMobile();
					}
				});
		}
//		initSmcAndOtp();

		ishaveCash = (String) preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_ISHAVEELECASHACCT_RES);
		linkEleCashAcctFlag = (String) preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_LINKELECASHACCTFLAG_RES);
		isflag = creditMap.get(Acc.RELEVANCEACCPRE_ACC_CREDITLINKEDFLAG_RES);
		acc_relevance_type = (TextView) view
				.findViewById(R.id.tv_relevance_type_value);

		acc_relevance_actnum = (TextView) view
				.findViewById(R.id.tv_relevance_actnum_value);
		checkBox_ll = (LinearLayout) view.findViewById(R.id.checkBox_ll);
		if (StringUtil.isNull(linkEleCashAcctFlag)) {
			// 没有电子现金账户——null或者0
			if (!ishaveCash.equals(isHaveECashAccountList.get(1))
					|| ishaveCash.equals(isHaveECashAccountList.get(1))
					|| ishaveCash.equals(isHaveECashAccountList.get(1))) {
				checkBox_ll.setVisibility(View.GONE);
				linkAcctFlag = linkAcctFlagList.get(0);
			} else {
				checkBox_ll.setVisibility(View.VISIBLE);
			}
		} else {
			if (!ishaveCash.equals(isHaveECashAccountList.get(1))
					|| (ishaveCash.equals(isHaveECashAccountList.get(1)) && linkEleCashAcctFlag
							.equals(isHaveECashAccountList.get(0)))
					|| (ishaveCash.equals(isHaveECashAccountList.get(1)) && linkEleCashAcctFlag
							.equals(isHaveECashAccountList.get(2)))) {
				checkBox_ll.setVisibility(View.GONE);
				linkAcctFlag = linkAcctFlagList.get(0);
			} else {
				checkBox_ll.setVisibility(View.VISIBLE);
			}
		}

		TextView tv_cashCard_value = (TextView) view
				.findViewById(R.id.tv_cashCard_value);
		checkbox = (CheckBox) view.findViewById(R.id.cbSavePhone);
		btn_last = (Button) view.findViewById(R.id.btnLast);
		btn_last.setOnClickListener(this);

		btn_confirm = (Button) view.findViewById(R.id.btnConfirm);
		btn_confirm.setOnClickListener(this);
		// relevance_type = creditMap
		// .get(Acc.RELEVANCEACCPRE_ACC_RELEVANCECREDITACCOUNTTYPE_RES);
		relevance_type = (String) preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_ACCOUNTTYPE_RES);
		acc_relevance_type
				.setText(StringUtil.isNull(relevance_type) ? ConstantGloble.BOCINVT_DATE_ADD
						: LocalData.AccountType.get(relevance_type));
		relevance_actnum = creditMap
				.get(Acc.RELEVANCEACCPRE_ACC_CREDITACCOUNTNUMBER_RES);
		acc_relevance_actnum
				.setText(StringUtil.isNull(relevance_actnum) ? ConstantGloble.BOCINVT_DATE_ADD
						: StringUtil.getForSixForString(relevance_actnum));
		tv_cashCard_value
				.setText(StringUtil.isNull(relevance_actnum) ? ConstantGloble.BOCINVT_DATE_ADD
						: StringUtil.getForSixForString(relevance_actnum));
		

	}
	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
	String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
	usbKeytext = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
	usbKeytext.Init(mmconversationId, randomNumber, preResultMap, this);
	isOtp = usbKeytext.getIsOtp();
	isSms = usbKeytext.getIsSmc();
	}
	/** 判断是动态口令还是手机交易码 */
//	public void initSmcAndOtp() {
//		if (!StringUtil.isNullOrEmpty(factorList)) {
//			for (int i = 0; i < factorList.size(); i++) {
//				Map<String, Object> itemMap = factorList.get(i);
//				Map<String, String> securityMap = (Map<String, String>) itemMap
//						.get(Inves.FIELD);
//				String name = securityMap.get(Inves.NAME);
//				if (Inves.Smc.equals(name)) {
//					isSms = true;
//					ll_smc.setVisibility(View.VISIBLE);
//				} else if (Inves.Otp.equals(name)) {
//					isOtp = true;
//					ll_active_code.setVisibility(View.VISIBLE);
//				}
//			}
//		}
//	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
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
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		BiiHttpEngine.dissMissProgressDialog();

		// 加密控件设置随机数
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		// 初始化界面
		init();

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

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
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
		if (checkBox_ll.getVisibility() == View.VISIBLE) {
			if (checkbox.isChecked()) {
				if (Boolean.valueOf(isflag)) {
					// 信用卡已关联
					linkAcctFlag = linkAcctFlagList.get(1);
				} else {
					// 信用卡没有关联，选择要关联电子现金账户
					linkAcctFlag = linkAcctFlagList.get(2);
				}
			} else {
				if (Boolean.valueOf(isflag)) {
					// 信用卡已关联
					linkAcctFlag = linkAcctFlagList.get(0);
				} else {
					linkAcctFlag = linkAcctFlagList.get(0);
				}
			}
		}

		// 请求账户自助关联提交交易
		requestPsnRelevanceAccountResult(
				(String) preResultMap
						.get(Acc.RELEVANCEACCPRE_ACC_ACCOUNTTYPE_RES),
				creditMap.get(Acc.RELEVANCEACCPRE_ACC_CREDITACCOUNTNUMBER_RES),
				ConstantGloble.BOCINVT_NULL_STRING,
				(String) preResultMap
						.get(Acc.RELEVANCEACCPRE_ACC_ISHAVEELECASHACCT_RES),
				linkAcctFlag,
				creditMap.get(Acc.RELEVANCEACCPRE_ACC_CREDITCURRENCYCODE2_RES),
				creditMap.get(Acc.RELEVANCEACCPRE_ACC_CREDITCURRENCYCODE_RES),
				creditMap
						.get(Acc.RELEVANCEACCPRE_ACC_CREDITCARDDESCRIPTION_RES),
				creditMap.get(Acc.RELEVANCEACCPRE_ACC_CREDITBRANCHID_RES),
				null, tokenId);
	}

	/** 请求账户自助关联提交交易——回调 */
	@Override
	public void requestPsnRelevanceAccountResultCallback(Object resultObj) {
		super.requestPsnRelevanceAccountResultCallback(resultObj);
		Intent intent = new Intent(AccCreditCardConfirmActivity.this,
				AccCreditCardSuccessActivity.class);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLast:
			finish();
			break;
		case R.id.btnConfirm:
			// 动态口令
//			RegexpBean sipRegexpBean = new RegexpBean(
//					AccCreditCardConfirmActivity.this
//							.getString(R.string.active_code_regex),
//					sipBoxActiveCode.getText().toString(),
//					ConstantGloble.SIPOTPPSW);
//			RegexpBean sipSmcpBean = new RegexpBean(
//					AccCreditCardConfirmActivity.this
//							.getString(R.string.acc_smc_regex),
//					sipBoxSmc.getText().toString(), ConstantGloble.SIPSMCPSW);
//			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//			
//			if (isSms) {
//				lists.add(sipSmcpBean);
//			}
//			if (isOtp) {
//				lists.add(sipRegexpBean);
//			}
//			if (RegexpUtils.regexpDate(lists)) {// 校验通过
//				
//				if (isSms) {
//					try {
//						smcStr = sipBoxSmc.getValue().getEncryptPassword();
//						smc_password_RC = sipBoxSmc.getValue()
//								.getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.exceptionPrint(e);
//					}
//				}
//				if (isOtp) {
//					try {
//						otpStr = sipBoxActiveCode.getValue()
//								.getEncryptPassword();
//						otp_password_RC = sipBoxActiveCode.getValue()
//								.getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.exceptionPrint(e);
//					}
//				}
//				
//			}
			checketDate();
			
			break;
		case R.id.ib_top_right_btn:
			// 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
			break;
		default:
			break;
		}
	}
	private void checketDate(){
		usbKeytext.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				pSNGetTokenId();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

}
