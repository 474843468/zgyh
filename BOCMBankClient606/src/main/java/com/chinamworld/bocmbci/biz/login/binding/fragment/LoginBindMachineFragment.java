package com.chinamworld.bocmbci.biz.login.binding.fragment;

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
import com.chinamworld.bocmbci.biz.login.binding.LoginBindingActivity;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
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
public class LoginBindMachineFragment extends SettingBaseFragment implements
		OnClickListener {

	private View rootView;
	private Button nextBtn;

	private RelativeLayout rootLayout;

	private String bindServiceCode = "PB107";
	private Map<String, String> _combinId = new HashMap<String, String>();
	private String securityFactorId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hardware_bind_machine_frag,
				container, false);

		initDatas();

		return rootView;
	}

	private void initDatas() {
		Bundle bundle = getArguments();

		requestCommConversationId();
	}

	private void initViews(View view) {
		rootLayout = (RelativeLayout) view
				.findViewById(R.id.hardware_frag_root_layout);
		nextBtn = (Button) view.findViewById(R.id.hardware_next_btn);
	}

	private void setViews() {		
		((LoginBindingActivity)getActivity()).setTitle(R.string.hardware_bind_machine);
		rootLayout.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_next_btn:
			// 获取安全因子
			BaseHttpEngine.showProgressDialog();
			BaseDroidApp.getInstanse().setIsTransNoSafetyCombinMessage(true);
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

		initViews(rootView);
		setViews();

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
		/**登录后强制绑定设备 过滤音频key*/
		int signDataIndex = getSecurityIdList.indexOf("4");
		if(signDataIndex >= 0){
			getSecurityIdList.remove("4");
			getSecurityNameList.remove("中银e盾");
			BaseDroidApp.getInstanse().setSecurityIdList(getSecurityIdList);
			BaseDroidApp.getInstanse().setSecurityNameList(getSecurityNameList);
		}
		securityFactorId = BaseDroidApp.getInstanse().getDefaultCombinId() + "";

		for (int i = 0; i < getSecurityIdList.size(); i++) {
			_combinId.put(getSecurityIdList.get(i), getSecurityNameList.get(i));
		}
		BaseHttpEngine.dissMissProgressDialog();
//		BaseDroidApp.getInstanse().showSeurityChooseDialogWithoutActive(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						sendBindRegisterPreDevice((String) BaseDroidApp
//								.getInstanse().getBizDataMap()
//								.get(ConstantGloble.CONVERSATION_ID));
//					}
//				});

		BaseDroidApp.getInstanse().showSeurityChooseDialogWithoutActive(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						sendBindRegisterPreDevice((String) BaseDroidApp
								.getInstanse().getBizDataMap()
								.get(ConstantGloble.CONVERSATION_ID));
					}
				},"无可用安全工具，请登录网银进入“个人设定-安全工具设置”激活手机交易码认证方式或到我行网点申领动态口令牌，再进行手机硬件绑定");

		
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

		Fragment fragment = new LoginBindMachineConfirmFragment(securityFactorId,
				obj.getMap());
		
		((LoginBindingActivity) getActivity()).showFragment(fragment);
	}

}
