package com.chinamworld.bocmbci.biz.acc.relevanceaccount;

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
import android.widget.ListView;
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
import com.chinamworld.bocmbci.biz.acc.adapter.RelevanceDebitAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
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
 * 借记卡关联确认信息页
 * 
 * @author wangmengmeng
 * 
 */
public class AccDebitCardConfirmActivity extends AccBaseActivity implements
		OnClickListener {
	/** 借记关联确认页 */
	private View view;
	/** 借记卡列表 */
	private ListView lv_debit_list;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;
	/** 上一步 */
	private Button btn_last;
	/** 确定 */
	private Button btn_confirm;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	/** 用户选择的的借记卡信息 */
	private List<Map<String, String>> debitMap;
	/** 账户自助关联预交易返回信息 */
	private Map<String, Object> preResultMap;
	private List<Map<String, String>> debitConfirmList = new ArrayList<Map<String, String>>();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_relevance_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_relevanceaccount_debit_confirm);
		// 右上角按钮点击事件
		setRightBtnClick(this);
		// 请求随机数
		requestForRandomNumber();
		// 初始化界面
//		init();
	}

	/** 初始化界面 */
	private void init() {
		// 添加步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_relevance_step1),
						this.getString(R.string.acc_relevance_step2),
						this.getString(R.string.acc_relevance_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		// 通过账户自助关联结果取到要用的借记卡信息
		preResultMap = AccDataCenter.getInstance().getRelevancePremap();
		// 借记卡数据
		debitMap = AccDataCenter.getInstance().getChoosefalseDebitList();
		lv_debit_list = (ListView) view
				.findViewById(R.id.lv_acc_relevance_debit_list);
		// 列表赋值
		RelevanceDebitAdapter adapter = new RelevanceDebitAdapter(
				AccDebitCardConfirmActivity.this, debitMap);
		lv_debit_list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		/** 安全因子数组对象 */
		Map<String, Object> securityentity = (Map<String, Object>) preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_SECURITYENTITY_RES);
		factorList = (List<Map<String, Object>>) securityentity
				.get(Acc.RELEVANCEACCPRE_ACC_FACTORLIST_RES);
//		// 请求随机数
//		requestForRandomNumber();
		initSipBox();
		if(isOtp){
		// 动态口令
		ll_active_code = (LinearLayout) view.findViewById(R.id.ll_active_code);

		sipBoxActiveCode = (SipBox) view.findViewById(R.id.sipbox_active);
		ll_active_code.setVisibility(View.VISIBLE);
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
		// 手机交易码
		sipBoxSmc = (SipBox) view.findViewById(R.id.sipbox_smc);
		ll_smc.setVisibility(View.VISIBLE);
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
		for (int i = 0; i < debitMap.size(); i++) {
			String number = debitMap.get(i).get(
					Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES);
			String type = debitMap.get(i).get(
					Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES);
			if (debitConfirmList.size() == 0) {
				Map<String, String> chooseMap = new HashMap<String, String>();
				chooseMap
						.put(Acc.RELEVANCEACCRES_LIST_ACCOUNTNUMBER_REQ,
								debitMap.get(i)
										.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES));
				chooseMap
						.put(Acc.RELEVANCEACCRES_LIST_ACCOUNTIBKNUM_REQ,
								debitMap.get(i)
										.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTIBKNUM_RES));
				chooseMap.put(
						Acc.RELEVANCEACCRES_LIST_LINKACCTFLAG_REQ,
						debitMap.get(i).get(
								Acc.RELEVANCEACCRES_LIST_LINKACCTFLAG_REQ));
				chooseMap.put(Acc.RELEVANCEACCRES_LINKDEBITORNOT_REQ, debitMap
						.get(i).get(Acc.RELEVANCEACCRES_LINKDEBITORNOT_REQ));
				chooseMap.put(Acc.RELEVANCEACCRES_LINKECASHORNOT_REQ, debitMap
						.get(i).get(Acc.RELEVANCEACCRES_LINKECASHORNOT_REQ));
				chooseMap.put(
						Acc.RELEVANCEACCRES_LINKMEDICALORNOT_REQ,
						debitMap.get(i).get(
								Acc.RELEVANCEACCRES_LINKMEDICALORNOT_REQ));
				debitConfirmList.add(chooseMap);
			} else {
				boolean flag = true;
				for (int j = 0; j < debitConfirmList.size(); j++) {
					String chooseNumber = debitConfirmList.get(j).get(
							Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES);
					if (chooseNumber.equals(number)) {
						flag = false;
						// 把分散的标记综合到一个Map里面
						if (type.equals(accountTypeList.get(13))) {
							// 关联电子现金账户
							debitConfirmList.get(j).put(
									Acc.RELEVANCEACCRES_LINKECASHORNOT_REQ,
									isHaveECashAccountList.get(1));
						} else if (type.equals(MEDICALACC)) {
							// 关联医保账户
							debitConfirmList.get(j).put(
									Acc.RELEVANCEACCRES_LINKMEDICALORNOT_REQ,
									isHaveECashAccountList.get(1));
						} else {
							// 关联借记卡
							debitConfirmList.get(j).put(
									Acc.RELEVANCEACCRES_LINKDEBITORNOT_REQ,
									isHaveECashAccountList.get(1));
						}
					}

				}
				if (flag) {
					Map<String, String> chooseMap = new HashMap<String, String>();
					chooseMap
							.put(Acc.RELEVANCEACCRES_LIST_ACCOUNTNUMBER_REQ,
									debitMap.get(i)
											.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES));
					chooseMap
							.put(Acc.RELEVANCEACCRES_LIST_ACCOUNTIBKNUM_REQ,
									debitMap.get(i)
											.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTIBKNUM_RES));
					chooseMap.put(
							Acc.RELEVANCEACCRES_LIST_LINKACCTFLAG_REQ,
							debitMap.get(i).get(
									Acc.RELEVANCEACCRES_LIST_LINKACCTFLAG_REQ));
					chooseMap.put(
							Acc.RELEVANCEACCRES_LINKDEBITORNOT_REQ,
							debitMap.get(i).get(
									Acc.RELEVANCEACCRES_LINKDEBITORNOT_REQ));
					chooseMap.put(
							Acc.RELEVANCEACCRES_LINKECASHORNOT_REQ,
							debitMap.get(i).get(
									Acc.RELEVANCEACCRES_LINKECASHORNOT_REQ));
					chooseMap.put(
							Acc.RELEVANCEACCRES_LINKMEDICALORNOT_REQ,
							debitMap.get(i).get(
									Acc.RELEVANCEACCRES_LINKMEDICALORNOT_REQ));
					debitConfirmList.add(chooseMap);
				}
			}

		}
		btn_last = (Button) view.findViewById(R.id.btnLast);
		btn_last.setOnClickListener(this);

		btn_confirm = (Button) view.findViewById(R.id.btnConfirm);
		btn_confirm.setOnClickListener(this);
		
//		initSmcAndOtp();

	}
	/**跟据返回初始化中银E盾加密控件*/
	private void initSipBox(){
		String mmconversationId = (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		usbKeytext = (UsbKeyText)view.findViewById(R.id.sip_usbkey);
		usbKeytext.Init(mmconversationId, randomNumber, preResultMap, this);
		isOtp = usbKeytext.getIsOtp();
		isSms = usbKeytext.getIsSmc();
	}
//	/** 判断是动态口令还是手机交易码 */
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
////			// 请求随机数
////			requestForRandomNumber();
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
		BaseHttpEngine.showProgressDialog();
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
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		// 加密控件设置随机数
//		sipBoxActiveCode.setRandomKey_S(randomNumber);
//		sipBoxSmc.setRandomKey_S(randomNumber);
		BiiHttpEngine.dissMissProgressDialog();
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
			return;
		}
		// 请求账户自助关联提交交易
		requestPsnRelevanceAccountResult(
				(String) preResultMap.get(Acc.RELEVANCEACCRES_ACCOUNTTYPE_REQ),
				AccDataCenter.getInstance().getAcc_relevance_actnum(),
				(String) preResultMap
						.get(Acc.RELEVANCEACCRES_MAINACCOUNTNUMBER_REQ),
				(String) preResultMap
						.get(Acc.RELEVANCEACCPRE_ACC_ISHAVEELECASHACCT_RES),
				ConstantGloble.BOCINVT_NULL_STRING,
				ConstantGloble.BOCINVT_NULL_STRING,
				ConstantGloble.BOCINVT_NULL_STRING,
				ConstantGloble.BOCINVT_NULL_STRING,
				ConstantGloble.BOCINVT_NULL_STRING, debitConfirmList, tokenId);
	}

	/** 请求账户自助关联提交交易 */
	@Override
	public void requestPsnRelevanceAccountResultCallback(Object resultObj) {
		super.requestPsnRelevanceAccountResultCallback(resultObj);
		Intent intent = new Intent(AccDebitCardConfirmActivity.this,
				AccDebitCardSuccessActivity.class);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLast:
			// 上一步按钮点击事件
			finish();
			break;
		case R.id.btnConfirm:
			// 确定按钮点击事件
			// 动态口令
//			RegexpBean sipRegexpBean = new RegexpBean(
//					AccDebitCardConfirmActivity.this
//							.getString(R.string.active_code_regex),
//					sipBoxActiveCode.getText().toString(),
//					ConstantGloble.SIPOTPPSW);
//			RegexpBean sipSmcpBean = new RegexpBean(
//					AccDebitCardConfirmActivity.this
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
//				pSNGetTokenId();
//			}
			BaseHttpEngine.dissMissProgressDialog();
			checkDate();
			
			break;
		case R.id.ib_top_right_btn:
			// 右侧按钮点击事件
			// 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
			break;
		default:
			break;
		}

	}
	private void checkDate(){
		/**音频Key安全认证工具*/
		usbKeytext.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
			
			@Override
			public void SuccessCallBack(String result, int errorCode) {
				// TODO Auto-generated method stub
				BaseHttpEngine.dissMissProgressDialog();
				pSNGetTokenId();
			}
		});
		
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
