package com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment;

import com.chinamworld.bocmbci.boc.ModelBoc;
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
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.audio.AudioKeyManager;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 解绑确认页面
 * 
 * @author MeePwn
 * 
 */
@SuppressLint("ValidFragment")
public class UnbindMachineConfirmFragment extends SettingBaseFragment implements
		OnClickListener {

	private RelativeLayout rootLayout;
	private View rootView;
	private Button confirmBtn;

	// 音频key
	private UsbKeyText usbKeyText;
	/** 手机交易码， 动态口令 */
	private SipBox smsSipBox, activeSipbox;
	// 获取
	private Button smsbtn;

	private Map<String, Object> result;

	// 之前的页面，本机解绑还是非本机解绑？
	private String fromWhere;
	private String ownMachineType = "own_machine";
	private String otherMachineType = "other_machine";

	public UnbindMachineConfirmFragment(String fromWhere,
			Map<String, Object> result) {
		this.result = result;
		this.fromWhere = fromWhere;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hardware_unbind_confirm_frag,
				container, false);

		Bundle bundle = getArguments();
		// securityFactoType = bundle.getString("security_facto_type");
		// result = ((ObjectValueSerializable)
		// getArguments().getSerializable("result")).getMap();
		BaseHttpEngine.showProgressDialog();
		requestForRandomNumber();

		return rootView;
	}

	private void initViews(View view, String randomNumber) {
		rootLayout = (RelativeLayout) view
				.findViewById(R.id.hardware_frag_root_layout);
		confirmBtn = (Button) view.findViewById(R.id.hardware_confirm_btn);
		smsSipBox = (SipBox) view.findViewById(R.id.sipbox_smc);
		activeSipbox = (SipBox) view.findViewById(R.id.sipbox_active);
		smsbtn = (Button) view.findViewById(R.id.smsbtn);
		activeSipbox.setRandomKey_S(randomNumber);
		smsSipBox.setRandomKey_S(randomNumber);
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
			((LinearLayout) view.findViewById(R.id.ll_active_code))
					.setVisibility(View.VISIBLE);
			SipBoxUtils.initSipBoxWithTwoType(activeSipbox, ConstantGloble.MIN_LENGTH, ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, settingControl.randomNumber, this);
//			activeSipbox
//					.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			activeSipbox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			activeSipbox.setId(10002);
//			activeSipbox.setBackgroundResource(R.drawable.bg_for_edittext);
//			activeSipbox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			activeSipbox
//					.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			activeSipbox.setSingleLine(true);
//			activeSipbox.setSipDelegator(this);
//			activeSipbox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			activeSipbox.setRandomKey_S(settingControl.randomNumber);
		}
		// 手机交易码
		if (isSms) {
			((LinearLayout) view.findViewById(R.id.ll_smc))
					.setVisibility(View.VISIBLE);
			SipBoxUtils.initSipBoxWithTwoType(smsSipBox, ConstantGloble.MIN_LENGTH, ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, settingControl.randomNumber, this);

//			smsSipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//			smsSipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//			smsSipBox.setId(10002);
//			smsSipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//			smsSipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//			smsSipBox
//					.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//			smsSipBox.setSingleLine(true);
//			smsSipBox.setSipDelegator(this);
//			smsSipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//			smsSipBox.setRandomKey_S(settingControl.randomNumber);
		}
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_confirm_btn:
			/** 安全工具校验数据 */
			usbKeyText.checkDataUsbKey(activeSipbox, smsSipBox,
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
		((HardwareBindingActivity)getActivity()).setTitle(R.string.hardware_unbind_machine);
		((HardwareBindingActivity)getActivity()).leftBtnClickType = "go_pre";
		((HardwareBindingActivity)getActivity()).setLeftBtnVisible();
		((HardwareBindingActivity)getActivity()).setRightBtnVisible();
		
		rootLayout.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}

	/**
	 * 登录后的tokenId 请求返回
	 * 
	 * @param resultObj
	 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestPSNGetTokenIdCallBack(resultObj);

		sendUnbindRegisterConfirmDevice();
	}

	/**
	 * 设备解绑提交交易
	 */
	public void sendUnbindRegisterConfirmDevice() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.HARDWARE_UNBIND_CONFIRM_DEVICE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> params = new HashMap<String, Object>();
		// TODO 调用CFCA接口进行加密
		/** 安全工具参数获取 */
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		if (ownMachineType.equals(fromWhere)) {
			// params.put("deviceInfo", DeviceUtils.getIMEI(getActivity()));
//			DeviceSecCryptor instance = DeviceSecCryptor.createInstatnce(getActivity());
//			String deviceInfo = "";
//			String deviceInfo_RC = "";
//			try {
//				SecResult secResult = instance.getEncryptedInfo(SystemConfig.CIPHERTYPE, false, ConstantGloble.OUT_PUT_VALUE_TYPE, 
//						settingControl.randomNumber, BaseDroidApp.getInstanse().getOperatorId());
//				deviceInfo_RC = secResult.getEncryptedRC();
//				deviceInfo = secResult.getEncryptedInfo();
//			} catch (CodeException e) {
//				e.printStackTrace();
//			}
			
			SecResult secResult=DeviceInfoTools.getSecDeviceInfo(getActivity(), settingControl.randomNumber, BaseDroidApp.getInstanse().getOperatorId());
			if(secResult!=null){
				params.put("deviceInfo", secResult.getEncryptedInfo());
				params.put("deviceInfo_RC", secResult.getEncryptedRC());	
			}
//			params.put("deviceInfo", BaseDroidApp.getInstanse().getCAOperatorId(getActivity(),settingControl.randomNumber));
//			params.put("deviceInfo_RC", BaseDroidApp.getInstanse().getCARandom(getActivity(), settingControl.randomNumber));
		}
//		params.put("flag", "2");
		params.put("token", (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID));
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"unbindRegisterConfirmDeviceCallback");
	}

	/**
	 * 设备解绑提交交易回调
	 * 
	 * @param resultObj
	 */
	public void unbindRegisterConfirmDeviceCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		
		//TODO 解绑成功后，清除本机设备信息
		SharedPreUtils.getInstance().deleteItem(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO);
		SharedPreUtils.getInstance().deleteItem(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC);
		
		// 获取用户是否绑定设备
        BaseDroidApp.getInstanse().setHasBindingDevice("0");
        /**登录后强制绑定设备，提示用户本机解绑后直接退出登录*/
        if (ownMachineType.equals(fromWhere)) {
        	boolean connected = AudioKeyManager.getInstance().isConnected();
//			if (connected) {
//				BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.safe_exit_device_tips), cancelListener);
//			} else {
				// 发送通讯请求退出
				requestForLogout();
				CustomDialog.toastInCenter(getActivity(), "解绑成功！");
				BaseDroidApp.getInstanse().clientLogOut();
				ActivityTaskManager.getInstance().removeAllActivity();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
					ModelBoc.onUnBindDeviceSuccess();
//			}
        	return ;
        }
        ((HardwareBindingActivity)getActivity()).leftBtnClickType = "go_main";
		UnbindMachineResultFragment f = new UnbindMachineResultFragment();
		f.setInfo(fromWhere);
		((HardwareBindingActivity) getActivity())
				.showFragment(f);
	}

}
