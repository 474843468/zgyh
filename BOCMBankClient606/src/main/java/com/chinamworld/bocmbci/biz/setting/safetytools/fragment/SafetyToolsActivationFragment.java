package com.chinamworld.bocmbci.biz.setting.safetytools.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.entity.ObjectValueSerializable;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;


/**
 * 
 * 安全工具设置，激活输入（非查询版）
 * 
 * @author MeePwn
 *
 */
public class SafetyToolsActivationFragment extends SettingBaseFragment implements OnClickListener {
	
	private View rootView ;
	private RelativeLayout rootLayout;
	//手机交易码布局
	private SipBox trancationCodeEt;
	private SipBox otherSafetyToolEt;
	private TextView otherSafetyToolTv;
	private UsbKeyText usbKeyText;
	private Button getCodeBtn;
	private Button concelBtn;
	private Button confirmBtn;
	private Map<String,Object> result;
	private String safety_ID , safety_Name;
	//此页面有2处获取token（激活单手机交易码/设置默认安全因子结果）  区分流程
	private boolean isActivated = false;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.safety_tools_activation_frag, container,false);
		result = ((ObjectValueSerializable) getArguments().getSerializable("result")).getMap();
		requestForRandomNumber();
		return rootView;
	}
	
	private void initDatas(){
		
	}
	
	private void initViews(View view ,String randomNumber){
		rootLayout = (RelativeLayout) view.findViewById(R.id.safety_tools_root_layout);
		otherSafetyToolTv = (TextView)view.findViewById(R.id.safety_tools_item02_text);
		usbKeyText = (UsbKeyText)view.findViewById(R.id.safety_tools_sip_usbkey);
		usbKeyText.Init((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID), randomNumber, result, (BaseActivity)getActivity());
		boolean isOtp = usbKeyText.getIsOtp();
		boolean isSms = usbKeyText.getIsSmc();
		if(isSms){
			view.findViewById(R.id.safety_tools_layout01).setVisibility(View.VISIBLE);
			trancationCodeEt = (SipBox)view.findViewById(R.id.safety_tools_smc_sb);
			initCodeSipBox(trancationCodeEt, randomNumber);
			getCodeBtn = (Button) view.findViewById(R.id.smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(getCodeBtn,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 发送获取短信验证码的请求
							sendSMSCToMobile();
						}
					});
		}
		if(isOtp){
			 view.findViewById(R.id.safety_tools_layout02).setVisibility(View.VISIBLE);
			otherSafetyToolEt = (SipBox)view.findViewById(R.id.safety_tools_item02_sb);
			initCodeSipBox(otherSafetyToolEt, randomNumber);
		}
		
		concelBtn = (Button) view.findViewById(R.id.safety_tools_concel_btn);
		confirmBtn = (Button) view.findViewById(R.id.safety_tools_confirm_btn);
	}
	
	private void setViews(){
		((SafetyToolsActivity)getActivity()).setTitle(R.string.safety_tools_setting);
		((SafetyToolsActivity)getActivity()).leftBtnClickType = "go_pre";
		((SafetyToolsActivity)getActivity()).setLeftBtnVisible();
		((SafetyToolsActivity)getActivity()).setRightBtnVisible();
		
		rootLayout.setOnClickListener(this);
		concelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safety_tools_concel_btn:
			getActivity().finish();
			break;
		case R.id.safety_tools_confirm_btn:
			/**安全工具校验数据*/
			usbKeyText.checkDataUsbKey(otherSafetyToolEt, trancationCodeEt, new IUsbKeyTextSuccess() {
				
				@Override
				public void SuccessCallBack(String result, int errorCode) {
					// 获取tockenId  此页面有2处获取token
					// 激活单手机交易码获取TOKEN  
					isActivated = true;
					BaseHttpEngine.showProgressDialog();
					requestPSNGetTokenId();
				}
			});
			break;
			// 手机交易码
		case R.id.smsbtn:
			
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
		super.queryRandomNumberCallBack(resultObj);
		initDatas();
		initViews(rootView ,settingControl.randomNumber);
		setViews();
		
	}
	
	
	/**
	 * 请求 开通单短信提交交易
	 */
	public void requestForSignMessageService(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_SIGNMESSAGESERVICE);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("token", token);
		params.put("phoneNumber", getArguments().getString("phoneNumber"));
		/**安全工具参数获取*/
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "querySignMessageServiceCallBack");
	}
	/**
	 * 请求 开通单短信提交交易回调
	 */
	public void querySignMessageServiceCallBack(Object resultObj){
//		Map<String , Object> result = (Map<String , Object>)BocnetUtils.httpResponseDeal(resultObj);
//		//开通标识false:未开通       true:已开通
//		String flag = result.get("openFlag")+"";
//		//false:未开通       true:已开通
//		if("true".equals(flag)){
		//加设置安全工具接口调用
		requestForPsnSVRSecurityList();
//		}else{
//			BaseHttpEngine.dissMissProgressDialog();
//			Toast.makeText(getActivity(), "激活失败~", Toast.LENGTH_LONG).show();
//		}
	}
	
	/**
	 * 请求 获取安全因子组合列表
	 */
	public void requestForPsnSVRSecurityList() {
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
		boolean open = false;
		for (int i = 0; i < securityList.size(); i++) {
			String id = securityList.get(i).get("id")+"";
			// TODO 单手机交易码安全因子ID
			if("96".equals(id)){
				safety_ID =id;
				safety_Name = "手机交易码";
				open = true;
				break;
			}
		}
		if(!open){
			Toast.makeText(getActivity(), "激活失败~", Toast.LENGTH_LONG).show();
			return;
		}
		/**从交易过来激活单手机交易码直接进入激活成功页面*/
		if(getArguments().getBoolean("isOpenActivated")){
			//进入成功页面
			((SafetyToolsActivity)getActivity()).showFragment(new SafetyToolsActiveSuccess());
		}else{  //设置默认的安全工具流程页面
			// 设置默认安全工具获取TOKEN  
			isActivated = false;
			requestPSNGetTokenId();
		}
	}
	/**
	 * 登录后的tokenId 请求返回
	 * 
	 * @param resultObj
	 */
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if(isActivated) requestForSignMessageService((String) biiResponseBody.getResult());
		else requestForPsnSVRSecurityFactorResult((String) biiResponseBody.getResult());
	}
	

	/**
	 * 请求安全因子设定结果
	 */
	public void requestForPsnSVRSecurityFactorResult(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_PSNSVRSECURITYFACTORRESULT);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,String> params = new HashMap<String,String>();
		params.put("_combinId", safety_ID);
		params.put("token", token);
		biiRequestBody.setParams(params);
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
		
		((SafetyToolsActivity)getActivity()).leftBtnClickType = "go_main";
		((SafetyToolsActivity)getActivity()).showFragment(safetyToolsSettingSuccess);
	}
	
}
