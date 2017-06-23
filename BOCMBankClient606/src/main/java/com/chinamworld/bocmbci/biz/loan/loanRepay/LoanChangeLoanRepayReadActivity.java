package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 变更还款账户协议 */
public class LoanChangeLoanRepayReadActivity extends LoanBaseActivity {
	private static final String TAG = "LoanChangeLoanRepayReadActivity";
	private View readView = null;
	private Button receptButton = null;
	private Button refuseButton = null;
	private TextView nameText = null;
	private TextView proText = null;
	private String loan_type = null;
	private String loanNumber = null;
	public static   String numbers = null;
	private String number = null;
	private Map<String, Object> loanChangeRepayAccPremap;
	/** 变更还款账户信息 */
	private Map<String, String> loanChangeRepayAccmap;
	/** 收款账户列表 */
	private List<Map<String, Object>> toAccountList;
	private int selectPos = -1;
	private String currency = null;
    private String cardNum=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_change_repay_acc));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.INVISIBLE);
		getDate();
		init();
		initOnClick();
	}

	private void getDate() {
		currency = getIntent().getStringExtra(Loan.LOANACC_CURRENCYCODE_RES);
		loan_type = getIntent().getStringExtra(Loan.LOANACC_LOAN_TYPE_RES);
		loanNumber = getIntent().getStringExtra("loanNumber");
		number = getIntent().getStringExtra(Acc.ACC_ACCOUNTNUMBER_RES);
		numbers=number;
		loanChangeRepayAccmap = LoanDataCenter.getInstance().getLoanChangeRepayAccmap();
		toAccountList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_TOACCOUNTLIST);
		selectPos = getIntent().getIntExtra(ConstantGloble.LOAN_SELECTPOS, -1);
	}

	private void init() {
		readView = LayoutInflater.from(this).inflate(R.layout.loan_change_protoal, null);
		tabcontentView.addView(readView);
		nameText = (TextView) findViewById(R.id.tv_jianfang);
		proText = (TextView) findViewById(R.id.loan_change_pro);
		refuseButton = (Button) findViewById(R.id.lastButton);
		receptButton = (Button) findViewById(R.id.sureButton);
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		String jf = getResources().getString(R.string.mycrcd_jiafang);
		nameText.setText(jf + loginName);
		String pro = getResources().getString(R.string.loan_change_pro);
		String loantype = null;
		if (StringUtil.isNull(loan_type)) {
			loantype = "-";
		} else {
			if (LoanData.loanTypeData.containsKey(loan_type)) {
				loantype = LoanData.loanTypeData.get(loan_type);
			} else {
				loantype = "-";
			}
		}
		String loan_number = StringUtil.getForSixForString(loanNumber);
		String num = StringUtil.getForSixForString(number);
		pro = pro.replace("loanType", loantype);
		pro = pro.replace("loanNumber", loan_number);
		pro = pro.replace("name", loginName);
		pro = pro.replace("nnumber", num);
		proText.setText(pro);
	}

	private void initOnClick() {
		refuseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		receptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 请求conversationId
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String acctNum=(String) loanChangeRepayAccmap.get(Loan.LOAN_CYCLE_REPAYACCOUNT);
		requestPsnQueryCardNumByAcctNum(acctNum);
		
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
	 /** 根据主账户查询对应的借记卡卡号---回调*/
	public void requestPsnQueryCardNumByAcctNumCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		cardNum = (String) (biiResponseBody.getResult());
		// TODO 服务因子
		requestGetSecurityFactor(ConstantGloble.LOAN_PB5);
		
	}
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		/**603协议页面不支持单手机交易码改造 */
		ArrayList<String> securityIdList = BaseDroidApp.getInstanse().getSecurityIdList();
		ArrayList<String> securityNameList = BaseDroidApp.getInstanse().getSecurityNameList();
		int signDataIndex = securityIdList.indexOf("96");
		if(signDataIndex >= 0){
			securityIdList.remove("96");
			securityNameList.remove("手机交易码");
		}
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialog();
				requestPsnLOANChangeLoanRepayAccountVerify();
			}
		}, securityIdList, securityNameList);
//		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				requestPsnLOANChangeLoanRepayAccountVerify();
//			}
//		});
	}
	/**
	 * 变更还款账户预交易接口
	 */
	public void requestPsnLOANChangeLoanRepayAccountVerify() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_CHANGE_LOANREPAY_ACCOUNTVERIFY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		loanChangeRepayAccPremap = new HashMap<String, Object>();
		loanChangeRepayAccPremap.put(Tran.TRAN_ATM_COMBINID_REQ, BaseDroidApp.getInstanse().getSecurityChoosed());
		// 贷款品种
		loanChangeRepayAccPremap.put(Loan.LOAN_LOANTYPE_REQ,
				(String) loanChangeRepayAccmap.get(Loan.LOANACC_LOAN_TYPE_RES));
		// 贷款账号
		loanChangeRepayAccPremap.put(Loan.LOAN_ACT_NUM,
				(String) loanChangeRepayAccmap.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES));
		// 原还款账户  605改造 修改字段
		loanChangeRepayAccPremap.put(Loan.OLD_PAY_ACCOUNTNUM,
				(String) loanChangeRepayAccmap.get(Loan.LOAN_CYCLE_REPAYACCOUNT));
		// 原还款账户对应卡号
		loanChangeRepayAccPremap.put(Loan.OLD_PAY_CARDNUM,cardNum);

		String accNum = EpayUtil.getString(
				toAccountList.get(selectPos).get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "");
		String accId = EpayUtil.getString(
				toAccountList.get(selectPos).get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
		// 新还款账户
		loanChangeRepayAccPremap.put(Loan.NEW_PAY_ACCOUNTNUM, accNum);
		// 新还款账户ID
		loanChangeRepayAccPremap.put(Loan.NEW_PAY_ACCOUNTID, accId);
		loanChangeRepayAccPremap.put(Loan.LOANACC_CURRENCYCODE_RES, currency);
		if (LocalData.rmbCodeList.contains(currency)) {
			loanChangeRepayAccPremap.put(Loan.LOAN_CASHREMIT_RES, ConstantGloble.CASHRMIT_RMB);
		} else {
			loanChangeRepayAccPremap.put(Loan.LOAN_CASHREMIT_RES, ConstantGloble.CASHRMIT_PARITIES);
		}

		biiRequestBody.setParams(loanChangeRepayAccPremap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANChangeLoanRepayAccountVerifyCallBack");
	}

	/**
	 * 变更还款账户预交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestPsnLOANChangeLoanRepayAccountVerifyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框		
		Map<String, Object> result = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		LoanDataCenter.getInstance().setLoanChangeRepayAccPremap(loanChangeRepayAccPremap);
		LoanDataCenter.getInstance().setLoanChangeRepayAccPreResultmap(result);		
		Intent intent = new Intent(this, LoanChangeLoanRepayAccountConfirmActivity.class);
		intent.putExtra(Loan.LOANACC_CURRENCYCODE_RES, currency);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}
}
