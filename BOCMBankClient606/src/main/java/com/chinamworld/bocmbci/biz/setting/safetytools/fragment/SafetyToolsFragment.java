package com.chinamworld.bocmbci.biz.setting.safetytools.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.SettingUtils;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.entity.ObjectValueSerializable;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.widget.CustomDialog;


/**
 * 
 * 安全工具设置
 * 
 * @author MeePwn
 *
 */
public class SafetyToolsFragment extends SettingBaseFragment implements OnClickListener {
	
	private View rootView;
	private RelativeLayout rootLayout;
	private RadioGroup safety_tools_radio_group;
	private RadioButton defaultRadio;
	private RadioButton trancationCodeRadio;
	private Button confirmBtn;
	private String safety_ID;
	private String safety_Name;
	//此号码请从PsnSvrQueryMessage接口返回报文中获取, 用户默认的安全组合ID,是否绑定硬件
	private String phoneNumber,defaultSafety,hasBindingDevice;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.safety_tools_frag, container,false);
		Bundle m_Bundle = getArguments();
		if(m_Bundle==null){
			requestCommConversationId();
		}else{
			boolean bindingCurrentDevice = m_Bundle.getBoolean("BindingCurrentDevice");
			if(bindingCurrentDevice) requestForBindCommConversationId();
			else requestCommConversationId();
		}
		return rootView;
	}
	
	private void initDatas(){
		
	}
	
	private void initViews(View view){
		((SafetyToolsActivity)getActivity()).setLeftBtnVisible();
		((SafetyToolsActivity)getActivity()).setRightBtnGone();
		rootLayout = (RelativeLayout) view.findViewById(R.id.safety_tools_root_layout);
		safety_tools_radio_group = (RadioGroup)view.findViewById(R.id.safety_tools_radio_group);
		defaultRadio = (RadioButton) view.findViewById(R.id.safety_tools_default_radio);
		defaultRadio.setText("不设定默认的安全工具");
		defaultRadio.setTag("0");
		trancationCodeRadio = (RadioButton) view.findViewById(R.id.safety_tools_tran_code_radio);
		trancationCodeRadio.setText("手机交易码");
		trancationCodeRadio.setTag("96");
		confirmBtn = (Button)view.findViewById(R.id.safety_tools_complete);
	}
	
	private RadioButton initRadioButton(int id , String name , String tag){
		final RadioButton rButton = new RadioButton(getActivity());
		RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.topMargin = SettingUtils.dip2px(getActivity(),20);
		rButton.setLayoutParams(params);
		rButton.setButtonDrawable(R.drawable.radiobtn_selector);
		rButton.setId(id);
		rButton.setText(name);
		rButton.setTag(tag);
		rButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					safety_ID = rButton.getTag()+"";
					safety_Name = rButton.getText()+"";
				}
			}
		});
		return rButton;
	}
	
	private void setViews(List<Map<String , Object>> securityList){
		((SafetyToolsActivity)getActivity()).setTitle(R.string.safety_tools_setting);
		rootLayout.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		trancationCodeRadio.setVisibility(View.GONE); // 96不需要放在第二个位置。
//		boolean checked = defaultSafety == null || "".equals(defaultSafety)?false:true;
		for (int i = 0; i < securityList.size(); i++) {
			Map<String,Object> item = securityList.get(i);
			String id = item.get("id")+"";
			String name = item.get("name")+"";
			if("4".equals(id)) name = "中银e盾";
			if("32".equals(id))
				continue;
//			boolean check = true ? id.equals(defaultSafety) : false;
			boolean check = id.equals(defaultSafety);
			if(check){
				safety_ID = id;
				safety_Name = name;
			}
			if("96".equals(id)){
				item.put("name",trancationCodeRadio.getText()+"");
				name= item.get("name") + "";
//				trancationCodeRadio.setChecked(check);
//				if(check)
//					safety_Name = trancationCodeRadio.getText()+"";
			}
			
			if("0".equals(id)){
				defaultRadio.setChecked(check);
				if(check)
					safety_Name = defaultRadio.getText()+"";
			}else{
				RadioButton radioButton = initRadioButton(10000+i,name,id);
				radioButton.setChecked(check);
				safety_tools_radio_group.addView(radioButton);
			}
			
		}
		defaultRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					safety_ID = defaultRadio.getTag()+"";
					safety_Name = defaultRadio.getText()+"";
				}
			}
		});
		trancationCodeRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					safety_Name = trancationCodeRadio.getText()+"";
					safety_ID = trancationCodeRadio.getTag()+"";
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safety_tools_complete:
			if(safety_ID == null || "".equals(safety_ID)){
				BaseDroidApp.getInstanse().showMessageDialog("请选择您要设置的安全工具",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
				return;
			}
			if("96".equals(safety_ID)){ //单手机交易码
				/**进入判断是否绑定设备流程   */
				/**   0没有绑定*/
				if("0".equals(hasBindingDevice)){
					// 服务器返回未设定
					String localBindInfo = SharedPreUtils.getInstance().getString(
							ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO, "");
					String cfcaString = DeviceInfoTools.getLocalCAOperatorId(getActivity(),BaseDroidApp.getInstanse().getOperatorId(),1);
					
					String localBindInfo_mac = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC, "");// MAC
			    	String cfcamacString = DeviceInfoTools.getLocalCAOperatorId(getActivity(), BaseDroidApp.getInstanse().getOperatorId(),2);// mac
			     	
					if((!"".equals(localBindInfo)&&!cfcaString.equals(localBindInfo))
							||(!"".equals(localBindInfo_mac)&&!cfcamacString.equals(localBindInfo_mac))){
						
						BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.setting_safety_tool_unbind_can_not_unbind_info),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										BaseDroidApp.getInstanse()
												.dismissMessageDialog();
									}
								});
					}else{
						BaseDroidApp.getInstanse().showErrorDialog(
								getString(R.string.set_safety_tool_no_binding_devices), R.string.cancle, 
								R.string.confirm,new OnClickListener() {
	
									@Override
									public void onClick(View v) {
										switch ((Integer) v.getTag()) {
										case CustomDialog.TAG_CANCLE:
											BaseDroidApp.getInstanse().dismissErrorDialog();
											break;
										case CustomDialog.TAG_SURE:
											BaseDroidApp.getInstanse().dismissErrorDialog();
											Intent intent = new Intent(getActivity(),HardwareBindingActivity.class);
											intent.putExtra("is_open_activated", "is_setting");
											getActivity().startActivityForResult(intent, 1);
											break;
										}
									}
								});
					}	
				}else{
					requestForPsnSvrQueryMessage();
				}
				
			}else{
				BaseHttpEngine.showProgressDialog();
				requestPSNGetTokenId();
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 请求 获取安全因子组合列表
	 */
	public void requestForPsnSVRSecurityList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_PSNSVRSECURITYLIST);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryPsnSVRSecurityListCallBack");
	}
	
	/**
	 * 请求 获取安全因子组合列表回调
	 */
	public void queryPsnSVRSecurityListCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		//{"id":"40" ,"name":"动态口令加手机交易码"}
		List<Map<String , Object>> securityList =  HttpTools.getResponseResult(resultObj);
		if(securityList == null || securityList.size() == 0){
			BaseDroidApp.getInstanse().showMessageDialog("您没有可用的安全工具，请选择其他交易。",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse()
									.dismissMessageDialog();
							getActivity().getSupportFragmentManager().popBackStack();
							getActivity().finish();
							ActivityTaskManager.getInstance().removeAllActivity();
						}
					});
			return ;
		}
		initDatas();
		initViews(rootView);
		setViews(securityList);
	}
	
	
	
	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, commConversationId);
		requestForPsnCommonQueryOprLoginInfo();
	}
	
	/**
	 * 请求查询操作员信息
	 */
	public void requestForPsnCommonQueryOprLoginInfo() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnCommonQueryOprLoginInfo");
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryPsnCommonQueryOprLoginInfoCallBack");
	}
	
	/**
	 * 请求 获取安全因子组合列表回调
	 */
	public void queryPsnCommonQueryOprLoginInfoCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		//{"id":"40" ,"name":"动态口令加手机交易码"}
		Map<String , Object> infos =  HttpTools.getResponseResult(resultObj);
		if(infos!=null){
			defaultSafety = infos.get("defaultSafety")+"";
			/** 0没有绑定     1绑定*/
			hasBindingDevice = infos.get("hasBindingDevice")+"";
		}
		
		requestForPsnSVRSecurityList();
	}
	
	
	/**
	 * 登录后的tokenId 请求返回
	 * 
	 * @param resultObj
	 */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		requestForPsnSVRSecurityFactorResult((String) biiResponseBody.getResult());
	}
	
	/**
	 * 请求安全因子设定结果
	 */
	public void requestForPsnSVRSecurityFactorResult(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_PSNSVRSECURITYFACTORRESULT);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,String> params = new HashMap<String,String>();
		params.put("_combinId", safety_ID+"");
		params.put("token", token);
		biiRequestBody.setParams(params);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryPsnSVRSecurityFactorResultCallBack");
	}
	
	/**
	 * 请求 安全因子设定结果回调
	 */
	public void queryPsnSVRSecurityFactorResultCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		SafetyToolsSettingSuccess safetyToolsSettingSuccess= new SafetyToolsSettingSuccess();
		Bundle mBundle = new Bundle();
		mBundle.putString("safety_Name", safety_Name);
		safetyToolsSettingSuccess.setArguments(mBundle);
		((SafetyToolsActivity)getActivity()).showFragment(safetyToolsSettingSuccess);
	}

	/**
	 * 请求 判断客户是否使用安全工具(查询版or 非查询版)
	 */
	public void requestForSVRPasswordChoose() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_SVRPASSWORDCHOOSE);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "querySVRPasswordChooseCallBack");
	}
	
	/**
	 * 请求 判断客户是否使用安全工具(查询版or 非查询版)回调
	 */
	public void querySVRPasswordChooseCallBack(Object resultObj){
		String askflag = HttpTools.getResponseResult(resultObj);
		//返回选择结果：0：查询版用户1：理财版、vip版用户
		if("0".equals(askflag)){
			BaseHttpEngine.dissMissProgressDialog();
			Toast.makeText(getActivity(), "查询版用户不能执行此交易！", Toast.LENGTH_LONG).show();
			getActivity().finish();
			return;
		}else{
			/** 是否绑定硬件   0没有绑定*/
			if("0".equals(hasBindingDevice)){
				Intent intent = new Intent(getActivity(),HardwareBindingActivity.class);
				intent.putExtra("is_open_activated", "is_setting");
				getActivity().startActivityForResult(intent, 1);
			}else{
				requestForPsnSvrQueryMessage();
			}
		}
	}
	
	/**
	 * 请求conversationId 来登录之外的conversation出Id
	 */
	public void requestForBindCommConversationId() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this, "requestBindCommConversationIdCallBack");
	}
	
	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestBindCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, commConversationId);
		requestForPsnSvrQueryMessage();
	}
	
	
	/**
	 * 请求 查询客户是否开通单短信（理财版、vip版用户）
	 */
	public void requestForPsnSvrQueryMessage() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_PSNSVRQUERYMESSAGE);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryPsnSvrQueryMessageCallBack");
	}
	/**
	 * 请求 查询客户是否开通单短信（理财版、vip版用户）回调
	 */
	public void queryPsnSvrQueryMessageCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String , Object> result = HttpTools.getResponseResult(resultObj);
		//开通标识false:未开通       true:已开通
		String flag = result.get("flag")+"";
		phoneNumber = result.get("phoneNumber")+"";
		//false:未开通       true:已开通 
		if("true".equals(flag)){
			requestPSNGetTokenId();
		}else{
			//获取安全因子PsnGetSecurityFactor
			BaseDroidApp.getInstanse().showErrorDialog(
					getString(R.string.set_safety_tool_not_open_transaction_code), R.string.cancle, 
					R.string.confirm,new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch ((Integer) v.getTag()) {
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								BaseHttpEngine.showProgressDialog();
								requestGetSecurityFactor("PB104C");
								break;
							}
						}
					});
			
		}
	}
	
	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj 服务器返回数据  
	 */
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialogWithoutActive(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseHttpEngine.showProgressDialog();
						// SignMessageServicePre（服务码：PB104C）开通单短信预交易
						requestForSignMessageServicePre();
					}
				});
	}
	
	/**
	 * 请求 开通单短信预交易（理财版、vip版用户）
	 */
	public void requestForSignMessageServicePre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_SIGNMESSAGESERVICEPRE);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String , String> params = new HashMap<String,String>();
		params.put("_combinId", BaseDroidApp.getInstanse().getSecurityChoosed());
		params.put("phoneNumber", phoneNumber);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "querySignMessageServicePreCallBack");
	}
	/**
	 * 请求 开通单短信预交易（理财版、vip版用户）回调
	 */
	public void querySignMessageServicePreCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		Map<String , Object> result = HttpTools.getResponseResult(resultObj);
		ObjectValueSerializable obj = new ObjectValueSerializable(result);
		Bundle m_Bundle = new Bundle();
		m_Bundle.putSerializable("result", obj);
		m_Bundle.putString("phoneNumber", phoneNumber);
		SafetyToolsActivationFragment fragment = new SafetyToolsActivationFragment();
		fragment.setArguments(m_Bundle);
		((SafetyToolsActivity)getActivity()).showFragment(fragment);
	}

}
