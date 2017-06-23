package com.chinamworld.bocmbci.biz.finc.fundacc;

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
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 基金开户，
 * 
 * @author xyl
 * 
 */
public class FincRegistAccConfirmActivity extends FincBaseActivity {
	private static final String TAG = "FincRegistAccConfirmActivity";
	/**
	 * 确定按钮
	 */
	private Button cofirmBtn;
	/**
	 * 资金账户号码
	 */
	private TextView fundAccNumTv;
	private TextView fundAccTypeTv;
	private TextView addressTypeTv;
	private LinearLayout otpLayout;
	private LinearLayout smcLayout;
	private SipBox otpEdit;
	private SipBox smcEdit;
	/**中银E盾*/
	private UsbKeyText usbKeyText;
	private String smcStr;
	private String otpStr;

	private String fundAccNumStr;
	private String fundAccTypeStr;
//	private String addressTypeStr;
	private String accId;

	private boolean isOtp;
	private boolean isSmc;
	private ArrayList<String> factorListName;
	private String otp_RC;
	private String smc_RC;

	private Button getBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		BaseHttpEngine.showProgressDialog();
		requestForRandomNumber(fincControl.registAccConversationId);

	}

	@Override
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryRandomNumberCallBack(resultObj);
		init();
		initData();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		settingbaseinit();
		View v = mainInflater.inflate(R.layout.finc_registacc_confirm, null);
		tabcontent.addView(v);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForRegistAcc2());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_registfundAcc);
		
		cofirmBtn = (Button) findViewById(R.id.finc_confirm_btn);
		fundAccNumTv = (TextView) findViewById(R.id.fund_accNum_tv);
		fundAccTypeTv = (TextView) findViewById(R.id.fund_accType_tv);
		addressTypeTv = (TextView) findViewById(R.id.fund_addresstype_tv);
		otpEdit = (SipBox) findViewById(R.id.finc_otp_edit);
		otpEdit.setCipherType(SystemConfig.CIPHERTYPE);
		smcEdit = (SipBox) findViewById(R.id.finc_smc_edit);
		smcEdit.setCipherType(SystemConfig.CIPHERTYPE);
		// 设定随机数
		otpEdit.setRandomKey_S(fincControl.randomNumber);
		smcEdit.setRandomKey_S(fincControl.randomNumber);
		// 设定为数字键盘
		otpEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		smcEdit.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);

		otpEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
		smcEdit.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);

		otpEdit.setPasswordRegularExpression(CheckRegExp.PASSWORD);
		smcEdit.setPasswordRegularExpression(CheckRegExp.PASSWORD);

		otpEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		smcEdit.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
		// 设定不能被键盘覆盖
		smcEdit.setSipDelegator(this);
		otpEdit.setSipDelegator(this);

		otpLayout = (LinearLayout) findViewById(R.id.finc_otp_edit_layout);
		smcLayout = (LinearLayout) findViewById(R.id.finc_smc_edit_layout);
		cofirmBtn.setOnClickListener(this);
		factorListName = new ArrayList<String>();
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
//		if (!StringUtil.isNullOrEmpty(fincControl.factorList)) {
//			for (int i = 0; i < fincControl.factorList.size(); i++) {
//				Map<String, Object> itemMap = fincControl.factorList.get(i);
//				Map<String, String> securityMap = (Map<String, String>) itemMap
//						.get(Inves.FIELD);
//				String name = securityMap.get(Inves.NAME);
//				if (Inves.Smc.equals(name)) {
//					isSmc = true;
//					smcLayout.setVisibility(View.VISIBLE);
//					BaseHttpEngine.showProgressDialog();
//				} else if (Inves.Otp.equals(name)) {
//					isOtp = true;
//					otpLayout.setVisibility(View.VISIBLE);
//				}
//				factorListName.add(name);
//			}
//		}
		
		right.setText(getResources().getString(R.string.close));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);

		getBtn = (Button) findViewById(R.id.set_get);
		getBtn.setOnClickListener(this);
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) findViewById(R.id.sip_usbkey);
		usbKeyText.Init(fincControl.registAccConversationId,
				fincControl.randomNumber, FincRegistAccAgreeActivity.resultMap,
				this);
		isOtp = usbKeyText.getIsOtp();
		isSmc = usbKeyText.getIsSmc();
		if(isOtp){
			otpLayout.setVisibility(View.VISIBLE);
		}
		if(isSmc){
			smcLayout.setVisibility(View.VISIBLE);
		}
	}
	private void initData() {
		if (fincControl.registAccFund != null) {
			fundAccNumStr = fincControl.registAccFund.get(Finc.I_FUNDACCNUM);
			fundAccTypeStr = fincControl.registAccFund.get(Finc.I_FUNDACCTYPE);
//			addressTypeStr = fincControl.registAccFund.get(Finc.I_ADDRESSTYPE);
			accId = fincControl.registAccFund.get(Finc.I_ACCOUNTID);
		}
		fundAccNumTv.setText(StringUtil.getForSixForString(fundAccNumStr));
		fundAccTypeTv.setText(LocalData.AccountType.get(fundAccTypeStr));
//		addressTypeTv.setText(StringUtil.getForSixForString(addressTypeStr));
		SmsCodeUtils.getInstance().addSmsCodeListner(getBtn, this);
	}

	@Override
	public void sendMSCToMobileCallback(Object resultObj) {
		super.sendMSCToMobileCallback(resultObj);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm_btn:
//			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//			if (isSmc) {
//				RegexpBean regexSms = new RegexpBean(
//						getString(R.string.set_smc_no), smcEdit.getText()
//								.toString(), ConstantGloble.SIPSMCPSW);
//				lists.add(regexSms);
//			}
//			if (isOtp) {
//				RegexpBean regexOtp = new RegexpBean(
//						getString(R.string.set_otp_no), otpEdit.getText()
//								.toString(), ConstantGloble.SIPOTPPSW);
//				lists.add(regexOtp);
//			}
//			
//			LogGloble.d(TAG, "otP" + isOtp + "sms" + isSmc);
//			if (RegexpUtils.regexpDate(lists)) {
//				if (isOtp) {
//					try {
//						otpStr = otpEdit.getValue().getEncryptPassword();
//						otp_RC = otpEdit.getValue().getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.e(TAG, "smcStr 密码控件问题");
//						BaseDroidApp.getInstanse().showInfoMessageDialog(
//								e.getMessage());
//						return;
//					}
//				}
//				if (isSmc) {
//					try {
//						smcStr = smcEdit.getValue().getEncryptPassword();
//						smc_RC = smcEdit.getValue().getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.e(TAG, "smcStr 密码控件问题");
//						BaseDroidApp.getInstanse().showInfoMessageDialog(
//								e.getMessage());
//						return;
//					}
//
//				}
//				BaseHttpEngine.showProgressDialog();
//				requestPSNGetTokenId(fincControl.registAccConversationId);
//			}
			/**安全工具数据校验*/
			usbKeyText.checkDataUsbKey(otpEdit, smcEdit, new IUsbKeyTextSuccess() {
				
				@Override
				public void SuccessCallBack(String result, int errorCode) {
					// TODO Auto-generated method stub
					BaseHttpEngine.showProgressDialog();
					requestPSNGetTokenId(fincControl.registAccConversationId);
				}
			});
			
			break;
		case R.id.ib_top_right_btn:
			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;
		case R.id.set_get:
			sendMSCToMobile(fincControl.registAccConversationId);
			break;

		default:
			break;
		}
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		regisAcc(tokenId);
		// regisAcc(accId, addressTypeStr, tokenId, otpStr,smcStr);
	}

	/**
	 * 基金开户
	 * 
	 * @param token
	 */
	public void regisAcc(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_REGISTACC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_REGISTACC_CAPITALACCOUNTID, accId);
//		map.put(Finc.FINC_REGISTACC_ADDRESSTYPE,
//				LocalData.FincAddressTypeValue.get(addressTypeStr));
		map.put(Finc.TOKEN, token);
//		if (isOtp) {
//			map.put(Comm.Otp, otpStr);
//			map.put(Comm.Otp_Rc, otp_RC);
//		}
//		if (isSmc) {
//			map.put(Comm.Smc, smcStr);
//			map.put(Comm.Smc_Rc, smc_RC);
//		}
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "regisAccCallback");
	}

	@Override
	public void regisAccCallback(Object resultObj) {
		super.regisAccCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(this, FincRegistAccSuccessActivity.class);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_REGISTFUNCACC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:

			break;
		case ConstantGloble.FINC_CLOSE:
			setResult(ConstantGloble.FINC_CLOSE);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (biiResponseBody.getError().getCode()
					.equals(ErrorCode.FINC_REGESIT_ERROR)) {
				BaseDroidApp.getInstanse().showMessageDialog(
						biiResponseBody.getError().getMessage(),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								FincRegistAccConfirmActivity.this
										.setResult(ConstantGloble.FINC_CLOSE);
								FincRegistAccConfirmActivity.this.finish();
							}
						});
				return true;
			}

		}
		return super.httpRequestCallBackPre(resultObj);
	}
}
