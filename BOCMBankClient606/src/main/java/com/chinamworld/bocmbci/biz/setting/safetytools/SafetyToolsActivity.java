package com.chinamworld.bocmbci.biz.setting.safetytools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.entity.ObjectValueSerializable;
import com.chinamworld.bocmbci.biz.setting.safetytools.fragment.SafetyToolsActivationFragment;
import com.chinamworld.bocmbci.biz.setting.safetytools.fragment.SafetyToolsActiveSuccess;
import com.chinamworld.bocmbci.biz.setting.safetytools.fragment.SafetyToolsFragment;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 
 * 安全工具设置主界面
 * 
 * @author MeePwn
 *
 */
public class SafetyToolsActivity extends SettingBaseActivity implements OnClickListener{
	
	private RelativeLayout rootLayout;
	private Button leftBtn;
	private Button rightBtn;
	private TextView titleTv;
	private FrameLayout contentLayout;
	/**开通单短信预交易   接口上传手机号*/
	private String phoneNumber;
	/**从交易过来激活单手机交易码*/
	private boolean isOpenActivated = false;
	
	/**
	 * 判断返回键的动作，是否回到主页面
	 */
	public String leftBtnClickType = "go_main";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safety_tools_activity);
		initViews();
		/**操作员信息*/
		Map<String, Object> resultMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		/**查询版客户类型 :1 查询版老客户;3 查询版新客户（非电子卡签约）;2 查询版新客户（电子卡签约）*/
		String qryCustType = (String)resultMap.get("qryCustType");
		String segmentId = BaseDroidApp.getInstanse().getSegmentInfo();
		if("10".equals(segmentId)&&!"1".equals(qryCustType)){
			BaseDroidApp.getInstanse().showMessageDialog("您尚未开通此服务",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse()
									.dismissMessageDialog();
							leftBtnClickType = "go_main";
							setRightBtnGone();
							getSupportFragmentManager().popBackStack();
							finish();
						}
					});
			return;
		}
		
		Fragment fragment = null;
		if(getIntent().getStringExtra("is_open_activated")!=null){
			isOpenActivated = true;
			/**从绑定设备流程直接进入激活流程*/
			requestForPsnSvrQueryMessage();
		}else{
			/**从安全工具设置进入 选择安全工具列表页面*/
			setViews(new SafetyToolsFragment());
		}
		
	}
	
	
	private void initViews(){
		leftBtn = (Button) findViewById(R.id.ib_back);
		rightBtn = (Button) findViewById(R.id.ib_main);
		titleTv = (TextView) findViewById(R.id.tv_title);
		contentLayout = (FrameLayout) findViewById(R.id.safety_tools_content_layout);
		
		//侧滑菜单选中位置
		LayoutValue.LEWFTMENUINDEX = "settingManager_13";
		initPulldownBtn();
		initLeftSideList(this, LocalData.settingManagerlistData);
		initFootMenu();
	}
	
	public void setTitle(int resId){
		String title = getResources().getString(resId);
		titleTv.setText(title);
	}
	
	public void setLeftBtnGone(){
		leftBtn.setVisibility(View.GONE);
	}
	public void setLeftBtnVisible(){
		leftBtn.setVisibility(View.VISIBLE);
	}
	
	public void setRightBtnGone(){
		rightBtn.setVisibility(View.GONE);
	}
	public void setRightBtnVisible() {
		rightBtn.setVisibility(View.VISIBLE);
	}
	
	private void setViews(Fragment fragment){
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		contentLayout.setOnClickListener(this);
		
		switchFragment(fragment);
	}
	
	private void switchFragment(Fragment fragment){
		showFragment(fragment);
	}
	
	public void showFragment(Fragment fragment){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.safety_tools_content_layout, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			if ("go_main".equals(leftBtnClickType)) {
				finish();
			}else{				
				leftBtnClickType = "go_main";
				setRightBtnGone();
				getSupportFragmentManager().popBackStack();
			}
			break;
		case R.id.ib_main:
			finish();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 请求 查询客户是否开通单短信（理财版、vip版用户）
	 */
	public void requestForPsnSvrQueryMessage() {
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
		Map<String , Object> result = HttpTools.getResponseResult(resultObj);
		//开通标识false:未开通       true:已开通
		String flag = result.get("flag")+"";
		phoneNumber = result.get("phoneNumber")+"";
		//false:未开通       true:已开通
		if("true".equals(flag)){
			//进入成功页面
			BaseHttpEngine.dissMissProgressDialog();
			Fragment fragment = new SafetyToolsActiveSuccess();
			showFragment(fragment);
		}else{
			//获取安全因子PsnGetSecurityFactor
			requestCommConversationId();
			
		}
	}
	
	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj 服务器返回数据
	 */
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialogWithoutActive(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 开通单短信预交易
				requestForSignMessageServicePre();
			}
		});
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
		requestGetSecurityFactor("PB104C");
		
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
		m_Bundle.putBoolean("isOpenActivated", isOpenActivated);
		SafetyToolsActivationFragment fragment = new SafetyToolsActivationFragment();
		fragment.setArguments(m_Bundle);
		showFragment(fragment);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK){
			SafetyToolsActivity.this.finish();
			return;
		}
		if(requestCode == 1){
//			Fragment fragment = new SafetyToolsFragment();
//			Bundle m_Bundle = new Bundle();
//			m_Bundle.putBoolean("BindingCurrentDevice", true);
//			fragment.setArguments(m_Bundle);
//			showFragment(fragment);
			//回到主页面
			SafetyToolsActivity.this.finish();
		}
	}
}
