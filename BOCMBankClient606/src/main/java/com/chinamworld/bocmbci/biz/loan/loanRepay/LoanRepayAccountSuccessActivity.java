package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 提前还款------成功页面 */
public class LoanRepayAccountSuccessActivity extends LoanBaseActivity {
	private static final String TAG = "LoanRepayAccountSuccessActivity";
	private View successView = null;
	private TextView numberText = null;
	private TextView loanTypeText = null;
	private TextView loanAccountText = null;
	private TextView loanMoneyText = null;
	private TextView currencyText = null;
	private TextView repayAmountText = null;
//	private TextView afterRepayissueAmountText = null;
	private TextView afterRepayRemainAmountText = null;
	private TextView chargesText = null;
	private TextView loanNumberText = null;
	private TextView leftText = null;
	private TextView leftText2 = null;
	//优惠后手续费金额
	private TextView loan_repay_charges2 = null;
	/**优惠后手续费*/
	private String privilegeProcedure;
	
	private Button finishButton = null;
	private View chargesView = null;
	private Map<String, String> successResultMap = null;
	public static boolean isFinish=false;
	/** 线上标识*/
	private  String  onlineFlag=null;
	/**循环类型*/
    private  String cycleType;
    private LinearLayout comm_view_left_title = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_three_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.INVISIBLE);
		successResultMap = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_REPAY_SUCCESS);
		if (StringUtil.isNullOrEmpty(successResultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		getDate();
	}

	private void init() {
		successView = LayoutInflater.from(this).inflate(R.layout.loan_repay_success, null);
		tabcontentView.addView(successView);
		numberText = (TextView) findViewById(R.id.loan_choise_transactionId);
		loanTypeText = (TextView) findViewById(R.id.loan_choise_input_loanType);
		loanAccountText = (TextView) findViewById(R.id.loan_choise_accountNumber);
		loanMoneyText = (TextView) findViewById(R.id.loan_choise_pa_moneys);
		currencyText = (TextView) findViewById(R.id.loan_choise_input_code);
		repayAmountText = (TextView) findViewById(R.id.loan_repay_edit_money);
		//afterRepayissueAmountText = (TextView) findViewById(R.id.loan_repay_afterRepayissueAmount);
		afterRepayRemainAmountText = (TextView) findViewById(R.id.loan_repay_afterRepayRemainAmount);
		chargesText = (TextView) findViewById(R.id.loan_repay_charges1);
		loanNumberText = (TextView) findViewById(R.id.loan_repay_tqAcc);
		leftText = (TextView) findViewById(R.id.left_text1);
		leftText2 = (TextView) findViewById(R.id.left_text2);
		chargesView = findViewById(R.id.loan_chages_view);
		//优惠后手续费金额 的控件
		comm_view_left_title=(LinearLayout) findViewById(R.id.comm_view_left_title);
		loan_repay_charges2=(TextView) findViewById(R.id.loan_repay_charges2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText2);
		finishButton = (Button) findViewById(R.id.trade_nextButton);
		finishButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCommConversationId();
				isFinish=true;
//				Intent intent = new Intent(LoanRepayAccountSuccessActivity.this, LoanRepayAccountActivity.class);
//				startActivity(intent);
			}
		});

	}

	private void getDate() {
		 //线上标识
		onlineFlag=LoanRepayAccountActivity.onlineFlag;
		//循环类型
		cycleType=LoanRepayAccountActivity.cycleType;
		privilegeProcedure= getIntent().getStringExtra(Loan.LOAN_PRIVILEGE_PROCEDURE_RES);
		String transNo = successResultMap.get(Loan.LOAN_TRANSNO_RES);
		String loanType = successResultMap.get(Loan.LOAN_LOANTYPE_REQ);
		String loanAccount = successResultMap.get(Loan.LOAN_LOANACCOUNT_REQ);
		String loanAccounts = null;
		if (!StringUtil.isNull(loanAccount)) {
			loanAccounts = StringUtil.getForSixForString(loanAccount);
		} else {
			loanAccounts = "-";
		}
		String currency = successResultMap.get(Loan.LOAN_CURRENCY_REQ);
		String code = null;
		if (!StringUtil.isNull(currency) && LocalData.Currency.containsKey(currency)) {
			code = LocalData.Currency.get(currency);
		} else {
			code = "-";
		}
		String loanAmount = successResultMap.get(Loan.LOAN_LOANAMOUNT_RES);
		String loanAmounts = null;
		if (!StringUtil.isNull(loanAmount) && !StringUtil.isNull(currency)) {
			loanAmounts = StringUtil.parseStringCodePattern(currency, loanAmount, 2);
		} else {
			loanAmounts = "-";
		}
		String repayAmount = successResultMap.get(Loan.LOAN_REPAYAMOUNT_RES);
		String repayAmounts = null;
		if (!StringUtil.isNull(repayAmount) && !StringUtil.isNull(currency)) {
			repayAmounts = StringUtil.parseStringCodePattern(currency, repayAmount, 2);
		} else {
			repayAmounts = "-";
		}
		String afterRepayissueAmount = successResultMap.get(Loan.LOAN_AFTERREPAYISSUEAMOUNT_RES);
		String afterRepayissueAmounts = null;
		if (!StringUtil.isNull(afterRepayissueAmount) && !StringUtil.isNull(currency)) {
			afterRepayissueAmounts = StringUtil.parseStringCodePattern(currency, afterRepayissueAmount, 2);
		} else {
			afterRepayissueAmounts = "-";
		}
		String afterRepayRemainAmount = successResultMap.get(Loan.LOAN_AFTERREPAYREMAINAMOUNT_RES);
		String afterRepayRemainAmounts = null;
		if (!StringUtil.isNull(afterRepayRemainAmount) && !StringUtil.isNull(currency)) {
			afterRepayRemainAmounts = StringUtil.parseStringCodePattern(currency, afterRepayRemainAmount, 2);
		} else {
			afterRepayRemainAmounts = "-";
		}
		String charges = successResultMap.get(Loan.LOAN_CHARGES_RES);
		String chargess = null;
		if (!StringUtil.isNull(charges) && !StringUtil.isNull(currency)) {
			chargess = StringUtil.parseStringCodePattern(currency, charges, 2);
		} else {
			chargess = "-";
		}
		String fromAccount = successResultMap.get(Loan.LOAN_FROMACCOUNT_RES);
		String fromAccounts = null;
		if (!StringUtil.isNull(fromAccount)) {
			fromAccounts = StringUtil.getForSixForString(fromAccount);
		} else {
			fromAccounts = "-";
		}
		String type = null;
		if (LoanData.loanTypeData.containsKey(loanType)) {
			type = LoanData.loanTypeData.get(loanType);
		} else {
			type = "-";
		}
		if (LoanData.loanCycleList.contains(loanType)) {
			chargesView.setVisibility(View.VISIBLE);
		} else {
			chargesView.setVisibility(View.GONE);
		}
		numberText.setText(transNo);
		loanTypeText.setText(type);
		loanAccountText.setText(loanAccounts);
		loanMoneyText.setText(loanAmounts);
		currencyText.setText(code);
		repayAmountText.setText(repayAmounts);
//		afterRepayissueAmountText.setText(afterRepayissueAmounts);
//		afterRepayRemainAmountText.setText(afterRepayRemainAmounts);
		String repayAmountInAdvanceNews=
				StringUtil.parseStringCodePattern(currency, LoanRepayAccountConfirmActivity.repayAmountInAdvanceNew, 2);
		afterRepayRemainAmountText.setText(repayAmountInAdvanceNews);
		if("R".equals(cycleType)&&"1".equals(onlineFlag)){
			privilegeProcedure = StringUtil.parseStringCodePattern(currency, privilegeProcedure, 2);
			loan_repay_charges2.setText(privilegeProcedure);
		}else{
			comm_view_left_title.setVisibility(View.GONE);
		}
		chargesText.setText(chargess);
		loanNumberText.setText(fromAccounts);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
	/** 提前还款账户查询 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 提前还款账户查询
		requestPsnLOANAdvanceRepayAccountQuery();
	}
	
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
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_LOANACCOUNTLIST, loanAccountList);
		Intent intent = new Intent(LoanRepayAccountSuccessActivity.this, LoanRepayAccountActivity.class);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}
}
