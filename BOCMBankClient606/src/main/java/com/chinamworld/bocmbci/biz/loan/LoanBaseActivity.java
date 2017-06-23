package com.chinamworld.bocmbci.biz.loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.loanApply.LoanApplyMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanQuery.LoanQueryMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanRepay.LoanRepayMenuActivity;
import com.chinamworld.bocmbci.biz.loan.loanUse.LoanUseMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

public class LoanBaseActivity extends BaseActivity {
	public LinearLayout tabcontentView = null;
	protected LayoutInflater mInflater;
	/** 返回按钮 */
	protected Button ibBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.loanLeftMenuList);
		mInflater = LayoutInflater.from(this);
		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 初始化底部菜单
		initFootMenu();
		tabcontentView = (LinearLayout) this.findViewById(R.id.sliding_body);
	}

	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		if(menuId.equals("loan_1")){// 贷款管理
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanQueryMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						LoanQueryMenuActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("loan_2")){// 贷款用款
				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanUseMenuActivity)) {
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent = new Intent();
					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
							LoanUseMenuActivity.class);
					context.startActivity(intent);
				}
						
		}
		else if(menuId.equals("loan_3")){// 贷款还款
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanRepayMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						LoanRepayMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("loan_4")){// 贷款在线申请
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanApplyMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanApplyMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		return true;
		
//		
//		super.setSelectedMenu(clickIndex);
//		switch (clickIndex) {
//		case 0:// 贷款管理
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanQueryMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanQueryMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 1:// 贷款用款
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanUseMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanUseMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 2:// 贷款还款
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanRepayMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanRepayMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 3:// 贷款在线申请
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LoanApplyMenuActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), LoanApplyMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//
//		default:
//			break;
//		}
	}

	/** 可执行存款质押贷款的定一本账户查询---请求 */
	public void requestPsnLoanPledgeAvaAccountQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANPLEDGEAVACCOUNTQUERY_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLoanPledgeAvaAccountQueryCallback");
	}

	/** 可执行存款质押贷款的定一本账户查询 */
	public void requestPsnLoanPledgeAvaAccountQueryCallback(Object resultObj) {

	}
	
	/**
	 * 查询个人循环贷款最低放款金额接口 
	 * 
	 * True，该接口使用的ConversationId与PsnLOANCycleLoanApplyVerify、PsnLOANCycleLoanApplySubmit一致
	 * */
	public void requestPsnLOANCycleLoanMinAmountQuery(String ConversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_CYCLELOAN_MINAMOUNTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANCycleLoanMinAmountQueryCallback");
	}

	/** 查询个人循环贷款最低放款金额接口 -------回调 */
	public void requestPsnLOANCycleLoanMinAmountQueryCallback(Object resultObj) {
     
	}
	/**
	 * 查询定一本下存单信息--------请求
	 * 
	 * @param accountId
	 *            :定一本账户Id
	 */
	public void requestPsnQueryPersonalTimeAccount(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNQUERYPERSONALTIMEACCOUNT_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryPersonalTimeAccountCallback");
	}

	/** 查询定一本下存单信息--------回调 */
	public void requestPsnQueryPersonalTimeAccountCallback(Object resultObj) {

	}

	/**
	 * 综合查询（查询汇率、质押率、贷款期限上下限、单笔限额上下限）
	 * 
	 * @param list
	 *            :币种集合开始
	 * @param accountId
	 *            :定一本账户Id
	 */
	public void requestPsnLOANMultipleQuery(List<Map<String, String>> list, String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANMULTIPLEQUERY_API);
		String commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_CNYLIST_REQ, list);
		map.put(Loan.LOAN_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANMultipleQueryCallback");
	}

	/** 综合查询（查询汇率、质押率、贷款期限上下限、单笔限额上下限） */
	public void requestPsnLOANMultipleQueryCallback(Object resultObj) {

	}

	/**
	 * 可返回结算标识查询收款账户和还款账户信息
	 * 
	 * @param accountId
	 *            :账户ID
	 */
	public void requestPsnLOANAccountsQuery(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANACCOUNTSQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAccountsQueryCallback");
	}

	/** 可返回结算标识查询收款账户和还款账户信息------回调 */
	public void requestPsnLOANAccountsQueryCallback(Object resultObj) {

	}

	/**
	 * 贷款利率查询
	 * 
	 * @param loanPeriod
	 *            :贷款期限
	 * @param floatingRate
	 *            :浮动比
	 * @param floatingValue
	 *            :浮动值
	 * @param rateIncrOpt
	 *            :利率增量选项
	 */
	public void requestPsnLoanRateQuery(String loanPeriod, String floatingRate, String floatingValue, String rateIncrOpt) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANRATEQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_LOANPERIOD_REQ, loanPeriod);
		map.put(Loan.LOAN_FLOATINGRATE_RES, floatingRate);
		map.put(Loan.LOAN_FLOATINGVALUE_RES, floatingValue);
		map.put(Loan.LOAN_RATEINCROPT_REQ, rateIncrOpt);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLoanRateQueryCallback");
	}

	/** 贷款利率查询------回调 */
	public void requestPsnLoanRateQueryCallback(Object resultObj) {

	}

	/** 获取借记卡、活期一本通、普通活期账户 */
	public void requestPsnCommonQueryAllChinaBankAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.QRY_LOAN_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_RAN, ConstantGloble.ACC_TYPE_ORD };
		params.put(Loan.LOAN_ACCOUNTTYPE_RES, s);
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCommonQueryAllChinaBankAccountCallBack");
	}

	/** 获取借记卡、活期一本通、普通活期账户-------回调 */
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {

	}

	/**
	 * 结算账户查询
	 * 
	 * @param currencyCode
	 *            :贷款币种
	 */
	public void requestPsnClearanceAccountQuery(String currencyCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNCLEARANCEACCOUNTQUERY_API);
		Map<String, Object> params = new HashMap<String, Object>();
		if(currencyCode != null){
			if (LocalData.rmbCodeList.contains(currencyCode)) {
			} else {
				params.put(Loan.LOAN_CASHREMIT_RES, ConstantGloble.CASHRMIT_PARITIES);
			}
			params.put(Loan.LOAN_CURRENCYCODE_RES, currencyCode);
			biiRequestBody.setParams(params);
		}
		biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnClearanceAccountQueryCallBack");
	}

	/** 结算账户查询-------回调 */
	public void requestPsnClearanceAccountQueryCallBack(Object resultObj) {

	}

	   /**
	    * 根据主账户查询对应的借记卡卡号
	    * @param acctNum:主账户
	    */
		public void requestPsnQueryCardNumByAcctNum(String acctNum) {
			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setMethod(Loan.PSN_LOAN_PSNQUERYCARDNUMBYACCTNUM_API);
			biiRequestBody.setConversationId(null);
			Map<String, String> map = new HashMap<String, String>();
			map.put(Loan.LOAN_ACCTNUM_REQ, acctNum);
			biiRequestBody.setParams(map);
			HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryCardNumByAcctNumCallback");
		}
		public void requestPsnQueryCardNumByAcctNumCallback(Object resultObj) {
		}
		/**
		 * 收款账户检查
		 * accountId 收款账户标识
		 * currencyCode 贷款币种*/
		public void requestPsnLOANPayeeAcountCheck(String accountId, String currencyCode){
			Map<String, Object> params = new HashMap<String, Object>();
			String currencyName = (String)LocalData.Currency.get(currencyCode);
			currencyCode = LocalData.getKeyByValueFromData(LocalData.Currency, currencyName);
			params.put(Comm.ACCOUNT_ID, accountId);
			params.put(Loan.LOANACC_CURRENCYCODE_RES, currencyCode);
			httpTools.requestHttp(Loan.LOAN_PAYEE_ACCOUNT_CHECK, "requestPsnLOANPayeeAcountCheckCallBack", params, true);
		}

		@SuppressWarnings("unchecked")
		public void requestPsnLOANPayeeAcountCheckCallBack(Object resultObj){
			BiiResponse biiResponse = (BiiResponse)resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			Map<String, List<String>> checkRessult = (Map<String, List<String>>) biiResponseBody.getResult();
			if(StringUtil.isNullOrEmpty(checkRessult)){
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
			List<String> checkResultt = (List<String>) checkRessult.get(Loan.LOAN_CHECK_RESSULT);
			String checkResult = (String)checkResultt.get(0);
			if(checkResult.equals("01")){
				/**收款账户符合条件*/
				checkPayeeAccountSuccess();
			}else if (checkResult.equals("02")) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_account_payee_check_info));
				return;
			}else if (checkResult.equals("03")) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_account_payee_check_infot));
				return;
			}else if(checkResult.equals("04")){
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_account_payee_check_infoth));
				return;
			}
		}
		/**收款账户符合条件*/
		public void checkPayeeAccountSuccess(){
			
		}
		/**
		 * 还款账户检查*/
		public void requestPsnLOANPayerAcountCheck(String accountId,String currencyCode){
			Map<String, Object> params = new HashMap<String, Object>();
			String currencyName = (String)LocalData.Currency.get(currencyCode);
			currencyCode = LocalData.getKeyByValueFromData(LocalData.Currency, currencyName);
			params.put(Comm.ACCOUNT_ID, accountId);
			params.put(Loan.LOANACC_CURRENCYCODE_RES, currencyCode);
			httpTools.requestHttp(Loan.LOAN_PAYER_ACCOUNT_CHECK, "requestPsnLOANPayerAcountCheckCallBack", params, true);
		}
		@SuppressWarnings("unchecked")
		public void requestPsnLOANPayerAcountCheckCallBack(Object resultObj){
			BiiResponse biiResponse = (BiiResponse)resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			Map<String, List<String>> checkRessult = (Map<String, List<String>>) biiResponseBody.getResult();
			if(StringUtil.isNullOrEmpty(checkRessult)){
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
			List<String> checkResultt = (List<String>)checkRessult.get(Loan.LOAN_CHECK_RESSULT);
			String checkResult = (String)checkResultt.get(0);
			if(checkResult.equals("01")){
				/**还款账户符合条件*/
				checkPayerAccountSuccess();
			}else if (checkResult.equals("02")) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_account_payer_check_info));
			}else if (checkResult.equals("03")) {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_account_payer_check_infot));
			}
		}
		/**还款账户符合条件*/
		public void checkPayerAccountSuccess(){
			
		}

		@Override
		public ActivityTaskType getActivityTaskType() {
			// TODO Auto-generated method stub
			return ActivityTaskType.OneTask;
		}
	}
