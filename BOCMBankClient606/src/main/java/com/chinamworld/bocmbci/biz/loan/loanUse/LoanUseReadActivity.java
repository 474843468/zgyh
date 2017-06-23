package com.chinamworld.bocmbci.biz.loan.loanUse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 个人循环贷款协议 */
public class LoanUseReadActivity extends LoanBaseActivity {
	private static final String TAG = "LoanUseReadActivity";
	private View readView = null;
	private TextView nameText = null;
	private TextView cdNameText = null;
	private TextView cdCrcdText = null;
	private TextView headText = null;
	private TextView topText = null;
	private TextView middleText = null;
	private String loan_accNum = null;
	private String useAmount = null;
	private String currency = null;
	private String loanTimes = null;
	private String loanRate = null;
	private String loanPay = null;
	private Button refuseButton = null;
	private Button receptButton = null;
	private Map<String, Object> loanUseMap = null;
	private Map<String, Object> loanUsePremap = null;
	private int selectPos = -1;
	private List<Map<String, Object>> toAccountList;
	/**币种*/
	private String code = null;
	String conversationId=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_use_1));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		loan_accNum =LoanCycleAccountActivity.loan_accNum;
		useAmount = getIntent().getStringExtra("useAmount");
		currency = getIntent().getStringExtra(Loan.LOANACC_CURRENCYCODE_RES);
		loanTimes = getIntent().getStringExtra(Loan.LOAN_CYCLE_LIFETERM);
		loanRate= getIntent().getStringExtra(Loan.LOAN_CYCLERATE);
		if("0".equals(loanRate)){
			loanRate=ConstantGloble.BOCINVT_DATE_ADD;
		}else{
			loanRate = StringUtil.isNull(getIntent().getStringExtra(Loan.LOAN_CYCLERATE)) ?
					ConstantGloble.BOCINVT_DATE_ADD: getIntent().getStringExtra(Loan.LOAN_CYCLERATE)+"%";
		}
		//		getIntent().getStringExtra(Loan.LOAN_CYCLERATE);
		loanPay = getIntent().getStringExtra(Loan.LOAN_CYCLE_REPAYACCOUNT);
		loanUseMap = LoanDataCenter.getInstance().getLoanUsemap();
		selectPos = getIntent().getIntExtra(ConstantGloble.LOAN_SELECTPOS, -1);
		toAccountList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_TOACCOUNTLISTS);
		init();
		getDate();
		initOnClick();
	}

	private void init() {
		readView = LayoutInflater.from(this).inflate(R.layout.loan_cycle_pro, null);
		tabcontentView.addView(readView);
		nameText = (TextView) findViewById(R.id.loan_choise_input_loanType);
		cdNameText = (TextView) findViewById(R.id.loan_choise_read_two);
		cdCrcdText = (TextView) findViewById(R.id.loan_choise_read_three);
		headText = (TextView) findViewById(R.id.text_head);
		refuseButton = (Button) findViewById(R.id.lastButton);
		receptButton = (Button) findViewById(R.id.sureButton);
		topText = (TextView) findViewById(R.id.text_top);
		middleText = (TextView) findViewById(R.id.text_middle);
	}

	private void getDate() {
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		String identityType = String.valueOf(returnMap.get(Login.IDENTIFY_TYPE));
		String identityNumber = String.valueOf(returnMap.get(Login.IDENTIFY_NUM));
		String type = null;
		if (LocalData.IDENTITYTYPE.containsKey(identityType)) {
			type = LocalData.IDENTITYTYPE.get(identityType);
		} else {
			type = "-";
		}
		String money = null;
		if (!StringUtil.isNull(useAmount) && !StringUtil.isNull(currency)) {
			money = StringUtil.parseStringCodePattern(currency, useAmount, 2);
		} else {
			money = "-";
		}
		nameText.setText(loginName);
		cdNameText.setText(type);
		cdCrcdText.setText(identityNumber);

		String toptext = getResources().getString(R.string.loan_cycle_top);

		String loanNumber = StringUtil.getForSixForString(loan_accNum);
		if (StringUtil.isNull(currency)) {
			code = "-";
		} else {
			code = LocalData.Currency.get(currency);
		}
		toptext = toptext.replace("loanNumber", loanNumber);
		topText.setText(toptext);
		String middle = getResources().getString(R.string.loan_cycle_middle);
		middle += code;
		String mil = getResources().getString(R.string.loan_cycle_middle_pro);
		middle += mil;
		String ends = "元。";
		SpannableString sp = new SpannableString(middle + money + ends);
		sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), middle.length(),
				middle.length() + money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		middleText.setText(sp);
		String headPro = getResources().getString(R.string.loan_cycle_head);
		int month = Integer.valueOf(loanTimes);
		String m = String.valueOf(month);
		headPro = headPro.replace("loantimes", m);
//		headPro = headPro.replace("loanrate", loanRate);
		String loanTo = getIntent().getStringExtra(Acc.ACC_ACCOUNTNUMBER_RES);
		String numberAccount = null;
		if(StringUtil.isNull(loanPay) || "0".equals(loanPay)){
			numberAccount = "-";
		}else{
			numberAccount = StringUtil.getForSixForString(loanPay);
		}
		String loginNameNew=null;
		if("-".equals(numberAccount)){
			loginNameNew="-";
		}else{
			loginNameNew=loginName;
		}
		headPro = headPro.replace("namenew", loginNameNew);
		headPro = headPro.replace("name", loginName);
		headPro = headPro.replace("lnumberS",numberAccount );// 还款账户
		headPro = headPro.replace("number", loanTo);
		headText.setText(headPro);

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
				requestGetSecurityFactor("PB094");
//				requestCommConversationId();
 			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// TODO 服务因子
		requestGetSecurityFactor("PB094");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
//		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseHttpEngine.showProgressDialog();
//				requestPsnLOANAdvanceRepayAccountDetailQuery((String) loanUseMap
//						.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES));
//			}
//		});
		BaseHttpEngine.dissMissProgressDialog();
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
				requestPsnLOANAdvanceRepayAccountDetailQuery((String) loanUseMap
						.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES));
			}
		}, securityIdList, securityNameList);
	}

	/**
	 * 贷款账户详情查询
	 * 
	 * @param loanAccount
	 */
	private void requestPsnLOANAdvanceRepayAccountDetailQuery(String loanAccount) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYACCOUNTDETAILQUERY_API);
		biiRequestBody.setConversationId(null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Loan.LOAN_LOANACCOUNT_REQ, loanAccount);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAdvanceRepayAccountDetailQueryCallback");

	}

	/** 贷款账户详情查询-----回调 */
	public void requestPsnLOANAdvanceRepayAccountDetailQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		requestPsnLOANCycleLoanApplyVerify(result);
	}

	/**
	 * 个人循环贷款预交易
	 */
	public void requestPsnLOANCycleLoanApplyVerify(Map<String, Object> detailMap) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.PSN_LOAN_CYCLELOAN_APPLYVERIFY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		loanUsePremap = new HashMap<String, Object>();
		loanUsePremap.put(Tran.TRAN_ATM_COMBINID_REQ, BaseDroidApp.getInstanse().getSecurityChoosed());
		loanUsePremap.put(Loan.LOAN_LOANTYPE_REQ, (String) loanUseMap.get(Loan.LOANACC_LOAN_TYPE_RES));
		loanUsePremap.put(Loan.LOAN_ACT_NUM, (String) loanUseMap.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES));
		//LOAN_AVAILABLEAMOUNT_REQ 可用金额
		Float loanCycleAvaAmount=Float.parseFloat((String)loanUseMap.get(Loan.LOAN_CYCLE_AVA_AMOUNT));
		//String loanCycleAvaAmounts=StringUtil.parseStringCodePattern(code, loanCycleAvaAmount, 2);
		loanUsePremap.put(Loan.LOAN_AVAILABLEAMOUNT_REQ,loanCycleAvaAmount);
		loanUsePremap.put(Loan.LOAN_CURRENCYCODE_RES, (String) loanUseMap.get(Loan.LOAN_CURRENCYCODE_RES));

		loanUsePremap.put(Loan.LOAN_AMOUNT_REQ, useAmount);
		loanUsePremap.put(Loan.LOAN_LOANPERIOD_REQ, (String) loanUseMap.get(Loan.LOAN_CYCLE_LIFETERM));
		loanUsePremap.put(Loan.LOAN_LOANRATE_REQ, (String) loanUseMap.get(Loan.LOAN_CYCLERATE));
		loanUsePremap.put(Loan.LOAN_CYCLE_DRAWDOWNDATE, (String) loanUseMap.get(Loan.LOAN_CYCLE_DRAWDOWNDATE));
		loanUsePremap.put(Loan.LOAN_CYCLE_MATDATE, (String) loanUseMap.get(Loan.LOAN_CYCLE_MATDATE));

		String accNum = EpayUtil.getString(
				toAccountList.get(selectPos).get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), "");
		String accId = EpayUtil.getString(
				toAccountList.get(selectPos).get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
		loanUsePremap.put(Loan.LOAN_CYCLE_TOACTNUM_REQ, accNum);
		loanUsePremap.put(Loan.TO_ACCOUNT_ID, accId);
		loanUsePremap.put(Loan.LOAN_PAYACCOUNT_REQ, (String) loanUseMap.get(Loan.LOAN_CYCLE_REPAYACCOUNT));
		//添加上送字段,paycycle还款周期
		loanUsePremap.put(Loan.LOAN_PAYCYCLE_REQ,(String) detailMap.get(Loan.LOAN_REPAY_PERIOD));
		// 还款方式
		String interestType = (String) detailMap.get(Loan.LOANACC_INTERESTTYPE_RES);
		
		// 还款周期
		String loanRepayPeriod =(String) detailMap.get(Loan.LOAN_REPAY_PERIOD);

		if (!StringUtil.isNull(interestType) && !StringUtil.isNull(loanRepayPeriod)
				&& interestType.equals(ConstantGloble.LOAN_ACTTYPE)
				&& loanRepayPeriod.equals(ConstantGloble.LOAN_PAYCYCLE)) {
			loanUsePremap.put(Loan.LOAN_PAYTYPE_REQ, ConstantGloble.LOAN_ACTTYPE1);
		} else {
			loanUsePremap.put(Loan.LOAN_PAYTYPE_REQ, interestType);
		}
		biiRequestBody.setParams(loanUsePremap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANCycleLoanApplyVerifyCallBack");
	}

	/**
	 * 个人循环贷款预交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestPsnLOANCycleLoanApplyVerifyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		LoanDataCenter.getInstance().setLoanUsePremap(loanUsePremap);
		LoanDataCenter.getInstance().setLoanUsePreResultmap(result);
		Intent intent = new Intent(this, LoanUseConfirmActivity.class);
		startActivity(intent);
	}
}
