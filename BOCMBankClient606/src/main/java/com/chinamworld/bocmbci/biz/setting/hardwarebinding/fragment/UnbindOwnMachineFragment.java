package com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cfca.mobile.device.SecResult;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.entity.ObjectValueSerializable;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 
 * 解绑选择安全工具页面（解绑非本机无此页面）
 * 
 * @author MeePwn
 * 
 */
public class UnbindOwnMachineFragment extends SettingBaseFragment implements
		OnClickListener {

	private View rootView;
	private Button nextBtn;
	// private Spinner spinner;
	// private SpinnerAdapter adapter;

	private RelativeLayout rootLayout;

	private String unbindOwnServiceCode = "PB107";
	private Map<String, String> _combinId = new HashMap<String, String>();
	private String securityFactorId = "";
	private int defaultSpinnerIndex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hardware_unbind_own_machine_frag,
				container, false);

		requestCommConversationId();

		return rootView;
	}


	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_next_btn:
			/**登录后强制绑定设备，提示用户本机解绑后直接退出登录*/
			BaseDroidApp.getInstanse().showErrorDialog(null, "手机设备解除绑定后，您将退出手机银行，是否继续？", new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					switch ((Integer) v.getTag()) {
					case CustomDialog.TAG_SURE:// 确定
						// 获取安全因子
						BaseHttpEngine.showProgressDialog();
						requestGetSecurityFactor(unbindOwnServiceCode);
					}
				}
			});
			
			break;

		default:
			break;
		}
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestCommConversationIdCallBack(resultObj);

		requestForRandomNumber();

		// // 获取安全因子
		// requestGetSecurityFactor(unbindOwnServiceCode);
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		super.queryRandomNumberCallBack(resultObj);

		rootLayout = (RelativeLayout) rootView
				.findViewById(R.id.hardware_frag_root_layout);
		nextBtn = (Button) rootView.findViewById(R.id.hardware_next_btn);
		// spinner = (Spinner) view
				// .findViewById(R.id.hardware_dynamic_password_spinner);
				//
				// adapter = new SpinnerAdapter(getActivity(),
				// R.layout.custom_spinner_item, BaseDroidApp.getInstanse()
				// .getSecurityNameList());
	
		rootLayout.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestGetSecurityFactorCallBack(resultObj);

		// TODO 获取安全因子
		ArrayList<String> getSecurityIdList = BaseDroidApp.getInstanse()
				.getSecurityIdList();
		ArrayList<String> getSecurityNameList = BaseDroidApp.getInstanse()
				.getSecurityNameList();

		securityFactorId = BaseDroidApp.getInstanse().getSecurityIdList()
				.get(0);
		defaultSpinnerIndex = getSecurityIdList.indexOf(securityFactorId);

		for (int i = 0; i < getSecurityIdList.size(); i++) {
			_combinId.put(getSecurityIdList.get(i), getSecurityNameList.get(i));
		}

		// TODO 弹框，选择安全因子后，解绑本机预交易
		BaseDroidApp.getInstanse().showSeurityChooseDialogWithoutActive(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						sendUnbindRegisterDevice((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});

		// // 解绑本机预交易
		// sendUnbindRegisterDevice((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

	}

	/**
	 * 设备解绑预交易
	 */
	public void sendUnbindRegisterDevice(String conversationId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.HARDWARE_UNBIND_DEVICE);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		// TODO 调用CFCA接口进行加密
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
//		params.put("deviceInfo", BaseDroidApp.getInstanse().getCAOperatorId(getActivity(),settingControl.randomNumber));
//		params.put("deviceInfo_RC", BaseDroidApp.getInstanse().getCARandom(getActivity(), settingControl.randomNumber));
		params.put("_combinId", BaseDroidApp.getInstanse().getSecurityChoosed());
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"unbindRegisterDevicCallback");
	}

	/**
	 * 设备解绑预交易回调
	 * 
	 * @param resultObj
	 *            返回报文
	 */
	public void unbindRegisterDevicCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		ObjectValueSerializable obj = new ObjectValueSerializable(result);

		Fragment fragment = new UnbindMachineConfirmFragment("own_machine",
				obj.getMap());
		Bundle bundle = new Bundle();
		// bundle.putString("from_where", "own_machine");
		// bundle.putString("security_facto_type", "own_machine");
		// fragment.setArguments(bundle);
		((HardwareBindingActivity) getActivity()).showFragment(fragment);

	}

}
