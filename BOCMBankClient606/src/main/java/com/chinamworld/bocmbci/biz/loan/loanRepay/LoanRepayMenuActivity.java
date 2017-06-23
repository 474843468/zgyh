package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贷款还款三级菜单
 * 
 * @author wanbing
 * 
 */
public class LoanRepayMenuActivity extends LoanBaseActivity {

	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	public static List<String> overDueStates;
	public static List<String> transFlags;
	/**提前还款or变更还款标识*/
	private Boolean isLoanRepay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.loan_left_three);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		// 添加布局
		setLeftSelectedPosition("loan_3");
		init();
	}

	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		view = mInflater.inflate(R.layout.loan_repay_menu, null);
		tabcontent.addView(view);
		LinearLayout llyt_loan_repay = (LinearLayout) findViewById(R.id.llyt_loan_repay);
		LinearLayout llyt_loan_ChangeLoanRepayAccount = (LinearLayout) findViewById(R.id.llyt_loan_ChangeLoanRepayAccount);
		// 提前还款
		llyt_loan_repay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseHttpEngine.showProgressDialog();
				/**603改造 不支持单手机交易码  改变其调用顺序，当只有单手机交易码时在该处报错*/
				isLoanRepay = true;
				requestCommConversationId();
				
//				requestGetSecurityFactor(ConstantGloble.LOAN_PB1);
			}
		});
		// 变更还款账户
		llyt_loan_ChangeLoanRepayAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseHttpEngine.showProgressDialog();
//				requestLoanList();
				/**603改造 不支持单手机交易码 改变其调用顺序， 当只有单手机交易码时 在该处报错*/
				isLoanRepay = false;
//				requestGetSecurityFactor(ConstantGloble.LOAN_PB5);
				requestCommConversationId();
			}
		});
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if(isLoanRepay){
			requestGetSecurityFactor(ConstantGloble.LOAN_PB1);
		}else {
			requestGetSecurityFactor(ConstantGloble.LOAN_PB5);
		}
	}
	
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestGetSecurityFactorCallBack(resultObj);
		/**603改造不支持单手机交易码 改造过滤*/
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
		if(isLoanRepay){
//			requestCommConversationId();
			// 提前还款账户查询
			requestPsnLOANAdvanceRepayAccountQuery();
		}else {
			requestLoanList();
		}
		
	}

//	@Override
//	public void requestCommConversationIdCallBack(Object resultObj) {
//		super.requestCommConversationIdCallBack(resultObj);
//		// 提前还款账户查询
//		requestPsnLOANAdvanceRepayAccountQuery();
//	}

	/** 提前还款账户查询 */
	private void requestPsnLOANAdvanceRepayAccountQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYACCOUNTQUERY_API);
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(conversationId);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAdvanceRepayAccountQueryCallback");
	}

	public void requestPsnLOANAdvanceRepayAccountQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> loanAccountList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(loanAccountList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		overDueStates=new ArrayList<String>();
		transFlags=new ArrayList<String>();
		for(int i=0;i<loanAccountList.size();i++){
			overDueStates.add(loanAccountList.get(i).get(Loan.LOAN_OVERDUESTATE_REQ));
			transFlags.add(loanAccountList.get(i).get(Loan.LOAN_TRANSFLAG_REQ));
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_LOANACCOUNTLIST, loanAccountList);
		Intent it = new Intent(context, LoanRepayAccountActivity.class);
		startActivity(it);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 变更还款账户--请求贷款账户列表信息 */
	public void requestLoanList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_ACCOUNT_LIST_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOANACC_E_LOAN_STATE, "10");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "loanAccountListCallBack");
	}

	/**
	 * 变更还款账户------请求贷款账户列表回调
	 * 
	 * @param resultObj
	 */
	public void loanAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框		
		Map<String, Object> loanlistmap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(loanlistmap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, String>> resultList = (List<Map<String, String>>) (loanlistmap.get(Loan.LOANACC_LOAN_LIST_RES));
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_no_acc_null));
			return;
		}
		//过滤条件:payAccountFlag这个字段只保留"0"下的账户号
		
		for(int i=0;i<resultList.size();i++){
			Map<String, String> map=resultList.get(i);
			String acc=map.get(Loan.LOANACC_PAY_ACCUNTFLAG);
			if("1".equals(acc)){
				resultList.remove(i);
				i--;
			} 
		}
		
		if(resultList.size() <= 0){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_no_acc_null));
			return;
		}
		Intent it = new Intent(context, LoanChangeLoanRepayAccountChooseActivity.class);
		startActivity(it);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_ACCCHANGE_RESULTLIST, resultList);
		BaseHttpEngine.dissMissProgressDialog();

	}
}
