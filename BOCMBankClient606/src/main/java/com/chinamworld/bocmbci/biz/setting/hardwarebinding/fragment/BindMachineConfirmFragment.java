package com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.device.SecResult;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 
 * 绑定短信输入页面
 * 
 * @author MeePwn
 * 
 */
@SuppressLint("ValidFragment")
public class BindMachineConfirmFragment extends SettingBaseFragment implements
		OnClickListener {

	private View rootView;
	private RelativeLayout rootLayout;
	private Button aquireMsgBtn;
	private Button confirmBtn;

	// 音频key
	private UsbKeyText usbKeyText;
	private RelativeLayout smcLayout;
	/** 手机交易码， 动态口令 */
	private SipBox smcSipBox, activeSipBox;
	// 获取
	private Button smsbtn;

	private String securityFactorId;
	private Map<String, Object> result;

	public void setInfo(String securityFactorId,
			Map<String, Object> result) {
		this.securityFactorId = securityFactorId;
		this.result = result;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hardware_bind_confirm_frag,
				container, false);

		BaseHttpEngine.showProgressDialog();
		requestForRandomNumber();

		return rootView;
	}

	private void initViews(View view, String randomNumber) {
		rootLayout = (RelativeLayout) view
				.findViewById(R.id.hardware_frag_root_layout);
		aquireMsgBtn = (Button) view.findViewById(R.id.hardware_get_msg_btn);
		confirmBtn = (Button) view.findViewById(R.id.hardware_confirm_btn);
		smcSipBox = (SipBox) view.findViewById(R.id.sipbox_smc);
		activeSipBox = (SipBox) view.findViewById(R.id.sipbox_active);
		smsbtn = (Button) view.findViewById(R.id.smsbtn);
		SmsCodeUtils.getInstance().addSmsCodeListner(smsbtn,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 发送获取短信验证码的请求
						sendSMSCToMobile();
					}
				});
		usbKeyText = (UsbKeyText) view.findViewById(R.id.hardware_sip_usbkey);
		usbKeyText.Init((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID), randomNumber, result,
				(BaseActivity) getActivity());
		boolean isOtp = usbKeyText.getIsOtp();
		boolean isSms = usbKeyText.getIsSmc();

		// 动态口令
		if (isOtp) {
			((LinearLayout) view.findViewById(R.id.ll_active_code)).setVisibility(View.VISIBLE);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(activeSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, settingControl.randomNumber, this);
//			activeSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			activeSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			activeSipBox.setId(10002);
//			activeSipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//			activeSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			activeSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			activeSipBox.setSingleLine(true);
//			activeSipBox.setSipDelegator(this);
//			activeSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			activeSipBox.setRandomKey_S(settingControl.randomNumber);
		}
		// 手机交易码
		if (isSms) {
			((LinearLayout) view.findViewById(R.id.ll_smc)).setVisibility(View.VISIBLE);
			// add by 2016年9月12日 luqp 修改
			SipBoxUtils.initSipBoxWithTwoType(smcSipBox, ConstantGloble.MIN_LENGTH,
					ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, settingControl.randomNumber, this);
//			smcSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smcSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smcSipBox.setId(10002);
//			smcSipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//			smcSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smcSipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smcSipBox.setSingleLine(true);
//			smcSipBox.setSipDelegator(this);
//			smcSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smcSipBox.setRandomKey_S(settingControl.randomNumber);
		}
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_confirm_btn:
			/** 安全工具校验数据 */
			usbKeyText.checkDataUsbKey(activeSipBox, smcSipBox,
					new IUsbKeyTextSuccess() {

						@Override
						public void SuccessCallBack(String result, int errorCode) {
							// 获取tockenId
							requestPSNGetTokenId();
						}
					});
			break;

		default:
			break;
		}
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	@Override
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryRandomNumberCallBack(resultObj);

		initViews(rootView, settingControl.randomNumber);
		((HardwareBindingActivity)getActivity()).setTitle(R.string.hardware_bind_machine);
		((HardwareBindingActivity)getActivity()).leftBtnClickType = "go_pre";
		((HardwareBindingActivity)getActivity()).setLeftBtnVisible();
		((HardwareBindingActivity)getActivity()).setRightBtnVisible();
		rootLayout.setOnClickListener(this);
		aquireMsgBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}

	/**
	 * 登录后的tokenId 请求返回
	 * 
	 * @param resultObj
	 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);

		sendBindConfirmRegisterDevice();
	}

	/**
	 * 设备绑定交易确认
	 */
	public void sendBindConfirmRegisterDevice() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.HARDWARE_BINDING_CONFIRM_DEVICE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
//		// TODO 调用CFCA接口进行加密
//		DeviceSecCryptor instance = DeviceSecCryptor.createInstatnce(getActivity());
//		String deviceInfo = "";
//		String deviceInfo_RC = "";
//		try {
//			SecResult secResult = instance.getEncryptedInfo(SystemConfig.CIPHERTYPE, false, ConstantGloble.OUT_PUT_VALUE_TYPE, 
//					settingControl.randomNumber, BaseDroidApp.getInstanse().getOperatorId());
//			deviceInfo_RC = secResult.getEncryptedRC();
//			deviceInfo = secResult.getEncryptedInfo();
//		} catch (CodeException e) {
//			e.printStackTrace();
//		}
		SecResult secResult=DeviceInfoTools.getSecDeviceInfo(getActivity(), settingControl.randomNumber, BaseDroidApp.getInstanse().getOperatorId());
		if(secResult!=null){
			params.put("deviceInfo", secResult.getEncryptedInfo());
			params.put("deviceInfo_RC", secResult.getEncryptedRC());	
		}		
		params.put("token", (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"bindConfirmRegisterDeviceCallback");
	}

	/**
	 * 设备绑定交易确认回调
	 * 
	 * @param resultObj
	 */
	public void bindConfirmRegisterDeviceCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		// 获取用户是否绑定设备
        BaseDroidApp.getInstanse().setHasBindingDevice("1");
		
		// TODO 绑定设备成功，存入本机设备信息
		SharedPreUtils.getInstance().addOrModify(
				ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO, DeviceInfoTools.getLocalCAOperatorId(getActivity(),BaseDroidApp.getInstanse().getOperatorId(),1));
		
		SharedPreUtils.getInstance().addOrModify(
				ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC, DeviceInfoTools.getLocalCAOperatorId(getActivity(),BaseDroidApp.getInstanse().getOperatorId(),2));

		((HardwareBindingActivity)getActivity()).leftBtnClickType = "go_main";
		// TODO 绑定成功后，判断是否已激活，未激活则显示绑定成功未激活页面，已激活则显示已激活页面
		((HardwareBindingActivity) getActivity())
				.showFragment(new BindMachineResultFragment());
	}
	
}
