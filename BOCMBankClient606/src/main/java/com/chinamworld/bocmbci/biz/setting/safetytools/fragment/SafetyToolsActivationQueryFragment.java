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
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;


/**
 * 
 * 安全工具设置，激活输入（查询版）
 * 
 * @author MeePwn
 *
 */
public class SafetyToolsActivationQueryFragment extends SettingBaseFragment implements OnClickListener{

	private RelativeLayout rootLayout;
	private TextView phoneTv;
	private SipBox trancationCodeEt;
	private Button getCodeBtn;
	private Button confirmBtn;
	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.safety_tools_activation_query_frag, container,false);
		requestForRandomNumber();
		return rootView;
	}
	
	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 加密控件设置随机数
		initDatas();
		initViews(rootView,(String) biiResponseBody.getResult());
		setViews();
	}
	
	private void initDatas(){
		
	}
	
	private void initViews(View view,String randKey){
		rootLayout = (RelativeLayout) view.findViewById(R.id.safety_tools_root_layout);
		phoneTv = (TextView) view.findViewById(R.id.safety_tools_phone_text);
		trancationCodeEt = (SipBox) view.findViewById(R.id.safety_tools_msg_et);
		initCodeSipBox(trancationCodeEt, randKey);
		getCodeBtn = (Button) view.findViewById(R.id.safety_tools_get_msg_btn);
		confirmBtn = (Button) view.findViewById(R.id.safety_tools_setting_confirm);
	}
	
	private void setViews(){
		((SafetyToolsActivity)getActivity()).setTitle(R.string.safety_tools_setting);
		phoneTv.setText(getArguments().get("mobile")+"");
		rootLayout.setOnClickListener(this);
		getCodeBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safety_tools_get_msg_btn:
			requestForSVRPasswordChoose();
			break;
		case R.id.safety_tools_setting_confirm:
			requestPSNGetTokenId();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 请求 开通单短信获取短信
	 */
	public void requestForSVRPasswordChoose() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_PSNSENDSMSTOMOBILEFORQUERYVERSION);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryPsnSendSMSToMobileForQueryVersionCallBack");
	}
	
	/**
	 * 请求 开通单短信获取短信回调
	 */
	public void queryPsnSendSMSToMobileForQueryVersionCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		//手机验证码发送时间间隔
		String smcInterval = HttpTools.getResponseResult(resultObj);
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
		String token = (String) biiResponseBody.getResult();
		if(checkCFCACode(trancationCodeEt,"手机交易码"))
			requestForPsnSvrOpenMsgForQueryVersion(token);
		
	}
	
	/**
	 * 请求 开通单短信（查询版）
	 */
	public void requestForPsnSvrOpenMsgForQueryVersion(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_PSNSVROPENMSGFORQUERYVERSION);
		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,String> params = new HashMap<String,String>();
		params.put("Smc", smc);
		params.put("Smc_RC", smc_RC);
		params.put("token",token);
		biiRequestBody.setParams(null);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryPsnSvrOpenMsgForQueryVersionCallBack");
	}
	
	/**
	 * 请求 开通单短信（查询版）回调
	 */
	public void queryPsnSvrOpenMsgForQueryVersionCallBack(Object resultObj){
		// TODO 提交交易接口  未加
		BaseHttpEngine.dissMissProgressDialog();
		((SafetyToolsActivity)getActivity()).showFragment(new SafetyToolsSettingSuccess());
	}
	
}
