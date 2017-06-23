package com.chinamworld.bocmbci.biz.loan.loanUse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.loanPledge.LoanPledgeAccountActivity;
import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贷款用款三级菜单
 * 
 * @author wanbing
 * 
 */
public class LoanUseMenuActivity extends LoanBaseActivity {

	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	private String isSystemTime;
	String identityNumber;
	String identityType;
	/**603协议页面不支持单手机交易码改造 
	 * 点击存款质押贷款和个人循环贷款标识 */
	private Boolean isLoanPledge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.loan_left_two);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		// 添加布局
		setLeftSelectedPosition("loan_2");
		init();
	}

	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		view = mInflater.inflate(R.layout.loan_use_menu, null);
		tabcontent.addView(view);
		LinearLayout llyt_loan_pledge = (LinearLayout) findViewById(R.id.llyt_loan_pledge);
		LinearLayout llyt_loan_cycle = (LinearLayout) findViewById(R.id.llyt_loan_cycle);
		// 存单质押贷款
		llyt_loan_pledge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				identityNumber=LoginActivity.identityNumber;
				identityType=LoginActivity.identityType;
				BaseHttpEngine.showProgressDialogCanGoBack();
				/**603改造不支持单手机交易码，改变调用顺序当只有单手机交易码时，在该处报错*/
				// 请求安全因子组合id
				isLoanPledge = true;
				requestCommConversationId();
//				requestGetSecurityFactor(ConstantGloble.LOAN_PB);
//		    	requestSystemDateTime();
				

			}
		});
		// 个人循环贷款
		llyt_loan_cycle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseHttpEngine.showProgressDialog();
//				requestPsnLOANCycleLoanAccountListQuery();
				/**603改造不支持单手机交易码，改变调用顺序，只有单手机交易码时，在该处报错*/
				//请求安全因子
				isLoanPledge = false;
				requestCommConversationId();
//				requestGetSecurityFactor("PB094");
			}
		}); 
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		if(isLoanPledge){
			requestGetSecurityFactor(ConstantGloble.LOAN_PB);
		}else {
			requestGetSecurityFactor("PB094");
		}
	}
	// 请求安全因子组合id
		@Override
		public void requestGetSecurityFactorCallBack(Object resultObj) {
			super.requestGetSecurityFactorCallBack(resultObj);
			/**603批次改造 贷款管理协议页面过滤 短信认证码*/
			//获取安全因子
			ArrayList<String> securityIdList = BaseDroidApp.getInstanse().getSecurityIdList();
			ArrayList<String> securityNameList = BaseDroidApp.getInstanse().getSecurityNameList();
			
			int signDataIndex = securityIdList.indexOf("96");
			if(signDataIndex >= 0){
				securityIdList.remove("96");
				securityNameList.remove("手机交易码");
				if(StringUtil.isNullOrEmpty(securityNameList)){
					BaseHttpEngine.dissMissProgressDialog();
					BaseDroidApp.getInstanse().showMessageDialog("现有认证工具不支持该服务", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
					return;
				}
				BaseDroidApp.getInstanse().setSecurityIdList(securityIdList);
				BaseDroidApp.getInstanse().setSecurityNameList(securityNameList);
			}
			
			if(isLoanPledge){
				requestSystemDateTime();
			}else {
				requestPsnLOANCycleLoanAccountListQuery();
			}

		}
	
	private void setIsBirthday(String identityType,String identityNumber) {
		//2024/04/03 19:20:38
		if(!StringUtil.isNullOrEmpty(dateTime)){
			 String[] strtime=	dateTime.split(" ");
			 String[] str= strtime[0].split("/");
			 //isSystemTime "20140901"
		   isSystemTime=str[0]+str[1]+str[2];
		}
		//转换成int类型
		int isSystemTimes=Integer.parseInt(isSystemTime);
		int identityNumbers=0;
		  /***
		   * 1 身份证
		   * 4  军人身份证"
		   * 武装警察身份证", "5"
		   * "2", "临时居民身份证"
		   */
		if("4".equals(identityType)||"5".equals(identityType)||"1".equals(identityType)
				||"2".equals(identityType)){
			if("1".equals(identityType)||"2".equals(identityType)){
				if(identityNumber.length()==18){
		      	String identityNumbers1=identityNumber.substring(6, 14);
				 identityNumbers=Integer.parseInt(identityNumbers1);
			}else if(identityNumber.length()==15){
				String 	identityNb=	identityNumber.substring(6, 12);
				identityNumber="19"+identityNb;
				identityNumbers=Integer.parseInt(identityNumber);
			}else{
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_cycle_null_not_fit));
				return;
			}
			if((identityNumbers+180000) > isSystemTimes){
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_cycle_null_not_fit));
				return;
			}
			if((identityNumbers+660000) < isSystemTimes){
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_cycle_null_not_fit));
				return;
			}
	      }
		}else {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_cycle_null_not_fit));
			return;
		}
		requestPsnLoanPledgeAvaAccountQuery();
	}
	
	/**
	 * 请求系统时间返回
	 * 
	 * @param resultObj dateTime 格式例如：2015/03/31 17:36:41
	 */
	public void requestSystemDateTimeCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		dateTime = (String) resultMap.get(Comm.DATETME);
		setIsBirthday(identityType,identityNumber);
		
	}
	/** 存单质押贷款 */
	@Override
	public void requestPsnLoanPledgeAvaAccountQueryCallback(Object resultObj) {
		super.requestPsnLoanPledgeAvaAccountQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_old_title));
			return;
		}
		
		//过滤西藏 accountIbkNum="40600"
		for(int i=0; i<resultList.size();i++){
			if("40600".equals(resultList.get(i).get(Loan.LOAN_ACCOUNTIBKNUM_RES))){
				resultList.remove(i);
				i--;
			}
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_old_title));
			return;
		}
		
	
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_NUM_RESULTLIST, resultList);
		Intent it = new Intent(context, LoanPledgeAccountActivity.class);
		startActivity(it);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 个人循环贷款的贷款账户列表信息 */
	public void requestPsnLOANCycleLoanAccountListQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_CYCLELOAN_ACCOUNTLISTQUERY);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANCycleLoanAccountListQueryCallBack");
	}

	/**
	 * 个人循环贷款的贷款账户列表回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnLOANCycleLoanAccountListQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_new_null));
			return;
		}
		Intent it = new Intent(context, LoanCycleAccountActivity.class);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_CYC_RESULTLIST, resultList);
		startActivity(it);
		BaseHttpEngine.dissMissProgressDialog();
	}

}
