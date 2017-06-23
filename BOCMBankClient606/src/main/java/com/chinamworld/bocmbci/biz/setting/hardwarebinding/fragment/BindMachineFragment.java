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

/**
 * 
 * 绑定安全工具页面
 * 
 * @author MeePwn
 * 
 */
public class BindMachineFragment extends SettingBaseFragment implements
		OnClickListener {

	private View rootView;
	private Button nextBtn;
	// private Spinner spinner;
	// private SpinnerAdapter adapter;

	private RelativeLayout rootLayout;

	private String bindServiceCode = "PB107";
	private Map<String, String> _combinId = new HashMap<String, String>();
	private String securityFactorId;
	private int defaultSpinnerIndex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hardware_bind_machine_frag,
				container, false);

		requestCommConversationId();
		return rootView;
	}



	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_next_btn:
			// 获取安全因子
			BaseHttpEngine.showProgressDialog();
			requestGetSecurityFactor(bindServiceCode);

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
		rootLayout = (RelativeLayout) rootView
				.findViewById(R.id.hardware_frag_root_layout);
		nextBtn = (Button) rootView.findViewById(R.id.hardware_next_btn);
		rootLayout.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		// // 获取安全因子
		// requestGetSecurityFactor(bindServiceCode);
	}

	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		// TODO 获取安全因子
		ArrayList<String> getSecurityIdList = BaseDroidApp.getInstanse()
				.getSecurityIdList();
		ArrayList<String> getSecurityNameList = BaseDroidApp.getInstanse()
				.getSecurityNameList();

		securityFactorId = BaseDroidApp.getInstanse().getDefaultCombinId() + "";
		defaultSpinnerIndex = getSecurityIdList.indexOf(securityFactorId);

		for (int i = 0; i < getSecurityIdList.size(); i++) {
			_combinId.put(getSecurityIdList.get(i), getSecurityNameList.get(i));
		}
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().showSeurityChooseDialogWithoutActive(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						sendBindRegisterPreDevice((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				});
		// 弹框，选择安全工具，绑定预交易
		// sendBindRegisterPreDevice((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID),
		// _combinId);

	}

	/**
	 * 设备绑定预交易
	 */
	public void sendBindRegisterPreDevice(String conversationId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.HARDWARE_BINDING_DEVICE);
		biiRequestBody.setConversationId(conversationId);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("_combinId", BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"bindRegisterDevicPreCallback");

	}

	/**
	 * 设备绑定预交易回调
	 * 
	 * @param resultObj
	 *            返回报文
	 */
	public void bindRegisterDevicPreCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		ObjectValueSerializable obj = new ObjectValueSerializable(result);
		BindMachineConfirmFragment fragment = new BindMachineConfirmFragment();
		fragment.setInfo(securityFactorId, obj.getMap());
		// Bundle bundle = new Bundle();
		// bundle.putString("security_factor_id", securityFactorId);
		// bundle.putSerializable("result", obj);
		// fragment.setArguments(bundle);

		((HardwareBindingActivity) getActivity()).showFragment(fragment);
	}

}
