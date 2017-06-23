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
import com.chinamworld.bocmbci.utils.SipBoxUtils;

/**
 * 已绑定非本机设备（解绑本机无此页面）
 * 
 * @author MeePwn
 * 
 */
public class UnbindMachineFragment extends SettingBaseFragment implements
		OnClickListener {

	private View rootView;
	private Button confirmBtn;
	private RelativeLayout rootLayout;

	private String unbindServiceCode = "PB106";
	private String usbKeyId = "4";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hardware_unbind_machine_frag,
				container, false);
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_confirm_btn:
			// 获取安全因子
			BaseHttpEngine.showProgressDialog();
			requestGetSecurityFactor(unbindServiceCode);
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
		super.requestCommConversationIdCallBack(resultObj);

		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryRandomNumberCallBack(resultObj);

		rootLayout = (RelativeLayout) rootView
				.findViewById(R.id.hardware_frag_root_layout);
		confirmBtn = (Button) rootView.findViewById(R.id.hardware_confirm_btn);
		
		((HardwareBindingActivity)getActivity()).setTitle(R.string.hardware_unbind_machine);
		rootLayout.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}

	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		// TODO 非本机设备解绑，安全因子仅支持音频Key
		// TODO 获取安全因子
		ArrayList<String> getSecurityIdList = BaseDroidApp.getInstanse()
				.getSecurityIdList();
		//不不包含音频Key则直接报错
		if (!getSecurityIdList.contains(usbKeyId)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(BaseDroidApp.getInstanse().getNoSafetyCombinsMsg()+"");
			return;
		}
		
		// TODO 添加安全因子（解绑非本机设备安全因子只允许音频Key）
		sendUnbindRegisterDevice((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	/**
	 * 设备解绑预交易
	 */
	public void sendUnbindRegisterDevice(String conversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.HARDWARE_UNBIND_DEVICE);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		// TODO 调用CFCA接口进行加密
//		params.put("deviceInfo", "");
//		params.put("deviceInfo_RC", "");
		params.put("_combinId", usbKeyId);
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
		Map<String, Object> result =HttpTools.getResponseResult(resultObj);
		ObjectValueSerializable obj = new ObjectValueSerializable(result);

		// TODO 传值
		Fragment fragment = new UnbindMachineConfirmFragment("other_machine",
				obj.getMap());
		Bundle bundle = new Bundle();
		// bundle.putString("from_where", "other_machine");
		// fragment.setArguments(bundle);
		((HardwareBindingActivity) getActivity()).showFragment(fragment);

	}

}
