package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.ArrayList;
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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 提前还款--账户详情页面 */
public class LoanRepayAccountDetailActivity extends LoanBaseActivity {
	private static final String TAG = "LoanRepayAccountDetailActivity";
	private View detailView = null;
	private TextView typeText = null;
	private TextView accText = null;
	private TextView moneyText = null;
	private TextView codeText = null;
	private TextView codeTitleText = null;
	private TextView timesText = null;
	private TextView loantimesText = null;
	private TextView moreMoneyText = null;
	private TextView payTypeText = null;
	private TextView loanCountText = null;
	/** 本期还款日 */
	private TextView loanTimesText = null;
	/** 本期应还款总额 */
	private TextView loanMoneyText = null;
	/** 本期截止当前应还利率 */
	private TextView loanRateText = null;
	/** 贷款放款日期 */
	private TextView loanTimeText = null;
	/** 贷款利率 */
	private TextView rateText = null;
	/** 还款账户 */
	private TextView accountText = null;
	/** 还款方式 */
	public static String interestType;
	/** 根据贷款账户显示相对应的借记卡卡号 */
	public static String cardNum = null;
	private int position = -1;
	private List<Map<String, String>> loanAccountList = null;
	private Map<String, Object> detailRasultMap = null;
	private Button loanButton = null;
	private TextView leftText = null;
	private List<Map<String, Object>> resultAccList = null;
	/** 未进行格式化的账户列表 */
	private List<String> accountNumberList = null;
	/** 进行格式化的账户列表 */
	private List<String> dealAccountNumberList = null;
	private List<String> accountTypeList = null;
	private List<String> accountIdList = null;
	private String currency = null;
	/** 提前还款周期 */
	public static String loanRepayPeriod;
	/** 对应账户借记卡卡号 */
	private TextView tv_loan_old_pay_acc;
	/** 对应账户借记卡卡号显示字段 */
	private TextView tv_loan_old_pay_acc_label;
	String payAccountNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_three_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		position = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		loanAccountList = (List<Map<String, String>>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(Loan.LOAN_LOANACCOUNTLIST_REQ);
		detailRasultMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOAN_RESULT);
		if (StringUtil.isNullOrEmpty(loanAccountList)
				|| StringUtil.isNullOrEmpty(detailRasultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (position < 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		getDate();
		BaseHttpEngine.showProgressDialog();
		requestPsnQueryCardNumByAcctNum(payAccountNumber);
	}

	private void getDate() {
		codeTitleText.setText(R.string.prms_currency_colon);
		// map里的数据是从上个页面传下的数据;detailRasultMap里的数据是接口里的数据
		Map<String, String> map = loanAccountList.get(position);
		String loanType = (String) detailRasultMap.get(Loan.LOAN_LOANTYPE_REQ);
		String loanAccount = (String) detailRasultMap
				.get(Loan.LOAN_LOANACCOUNT_REQ);
		String loanAmount = (String) detailRasultMap
				.get(Loan.LOANACC_LOAN_AMOUNT_RES);
		currency = (String) detailRasultMap.get(Loan.LOAN_CURRENCY_REQ);
		loanRepayPeriod = (String) detailRasultMap
				.get(Loan.LOAN_REPAYPERIOD_REO);
		String number = null;
		if (!StringUtil.isNull(loanAccount)) {
			number = StringUtil.getForSixForString(loanAccount);
		} else {
			number = "-";
		}
		String code = null;
		if (!StringUtil.isNull(currency)
				&& LocalData.Currency.containsKey(currency)) {
			code = LocalData.Currency.get(currency);
		} else {
			code = "-";
		}
		String money = null;
		if (!StringUtil.isNull(loanAmount) && !StringUtil.isNull(currency)) {
			money = StringUtil.parseStringCodePattern(currency, loanAmount, 2);
		} else {
			money = "-";
		}
		String loanPeriod = (String) detailRasultMap
				.get(Loan.LOAN_LOANPERIOD_REQ);
		String loanToDate = (String) detailRasultMap
				.get(Loan.LOAN_LOANTODATE_REQ);
		String remainCapital = (String) detailRasultMap
				.get(Loan.LOAN_REMAINCAPITAL_REQ);
		String moreMoney = null;
		if (!StringUtil.isNull(remainCapital)) {
			moreMoney = StringUtil.parseStringCodePattern(currency,
					remainCapital, 2);
		} else {
			moreMoney = "-";
		}
		interestType = (String) detailRasultMap.get(Loan.LOAN_INTTYPETERES_REQ);
		String remainIssue = (String) detailRasultMap
				.get(Loan.LOAN_REMAINISSUE_REQ);
		String thisIssueRepayDate = (String) detailRasultMap
				.get(Loan.LOAN_THISISSUEREPAYDATE_REQ);
		String thisIssueRepayAmount = (String) detailRasultMap
				.get(Loan.LOAN_THISISSUEREPAYAMOUNT_REQ);
		String thisIssueRepayAmounts = null;
		if (!StringUtil.isNull(thisIssueRepayAmount)) {
			thisIssueRepayAmounts = StringUtil.parseStringCodePattern(currency,
					thisIssueRepayAmount, 2);
		} else {
			thisIssueRepayAmounts = "-";
		}
		String thisIssueRepayInterest = (String) detailRasultMap
				.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ);
		String thisIssueRepayInterests = null;
		if (!StringUtil.isNull(thisIssueRepayInterest)) {
			thisIssueRepayInterests = StringUtil.parseStringCodePattern(
					currency, thisIssueRepayInterest, 2);
		} else {
			thisIssueRepayInterests = "-";
		}
		String loanDate = (String) detailRasultMap.get(Loan.LOAN_LOANDATE_REQ);
		String loanRate = (String) detailRasultMap.get(Loan.LOAN_LOANRATE_REQ);
		payAccountNumber = (String) detailRasultMap
				.get(Loan.LOAN_PAYACCOUNTNUMBER_REQ);
		String payAccountNumbers = null;
		if (!StringUtil.isNull(payAccountNumber)) {
			payAccountNumbers = StringUtil.getForSixForString(payAccountNumber);
		} else {
			payAccountNumbers = "-";
		}
		String type = null;
		if (LoanData.loanTypeData.containsKey(loanType)) {
			type = LoanData.loanTypeData.get(loanType);
		} else {
			type = "-";
		}
		String interestTypes = null;
		if (LoanData.loanInterestType_mondy.containsKey(interestType)) {
			if("B".equals(interestType) && "98".equals(detailRasultMap.get(Loan.LOAN_REPAYPERIOD_REO))){
				interestTypes = getResources().getString(R.string.loan_expire_pay_interest);
			}else{
				interestTypes = LoanData.loanInterestType_mondy.get(interestType);
			}
		
		} else {
			interestTypes = "-";
		}

		typeText.setText(type);
		accText.setText(number);
		moneyText.setText(money);
		codeText.setText(code);
		String month = getResources().getString(R.string.month);
		timesText.setText(StringUtil.isNullOrEmptyCaseNullString(loanPeriod) ? ConstantGloble.BOCINVT_DATE_ADD
				: loanPeriod + month);
		loantimesText
				.setText(StringUtil.isNullOrEmptyCaseNullString(loanToDate) ? ConstantGloble.BOCINVT_DATE_ADD
						: loanToDate);
		moreMoneyText.setText(moreMoney);
		payTypeText.setText(interestTypes);
		loanCountText.setText(remainIssue);
		loanTimesText
				.setText(StringUtil
						.isNullOrEmptyCaseNullString(thisIssueRepayDate) ? ConstantGloble.BOCINVT_DATE_ADD
						: thisIssueRepayDate);
		loanMoneyText.setText(thisIssueRepayAmounts);
		loanRateText.setText(thisIssueRepayInterests);
		loanTimeText
				.setText(StringUtil.isNullOrEmptyCaseNullString(loanDate) ? ConstantGloble.BOCINVT_DATE_ADD
						: loanDate);
		// 利率 格式化保留小树点后2为数字
		rateText.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate) ? ConstantGloble.BOCINVT_DATE_ADD
				: loanRate + "%");
		accountText.setText(payAccountNumbers);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				LoanRepayAccountDetailActivity.this, tv_loan_old_pay_acc_label);
	}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(
				R.layout.loan_repay_choise_acc_detail, null);
		tabcontentView.addView(detailView);
		typeText = (TextView) findViewById(R.id.loan_choise_input_loanType);
		accText = (TextView) findViewById(R.id.loan_choise_accountNumber1);
		moneyText = (TextView) findViewById(R.id.loan_choise_pa_moneys);
		codeText = (TextView) findViewById(R.id.loan_choise_input_code);
		codeTitleText = (TextView) findViewById(R.id.loan_choise_code);
		timesText = (TextView) findViewById(R.id.loan_repay_loanPeriod);
		loantimesText = (TextView) findViewById(R.id.loan_repay_loanToDate);
		moreMoneyText = (TextView) findViewById(R.id.loan_repay_remainCapital);
		payTypeText = (TextView) findViewById(R.id.loan_repay_interestType);
		loanCountText = (TextView) findViewById(R.id.loan_repay_remainIssue);
		loanTimesText = (TextView) findViewById(R.id.loan_repay_thisIssueRepayDate);
		loanMoneyText = (TextView) findViewById(R.id.loan_repay_thisIssueRepayAmount);
		loanRateText = (TextView) findViewById(R.id.loan_repay_thisIssueRepayInterest);
		loanTimeText = (TextView) findViewById(R.id.loan_repay_loanDate);
		rateText = (TextView) findViewById(R.id.loan_repay_loanRate);
		accountText = (TextView) findViewById(R.id.loan_repay_payAccountNumber);
		loanButton = (Button) findViewById(R.id.forex_query_deal_detailes_ok);
		leftText = (TextView) findViewById(R.id.text_left);
		tv_loan_old_pay_acc_label = (TextView) findViewById(R.id.tv_loan_old_pay_acc_label);
		tv_loan_old_pay_acc = (TextView) findViewById(R.id.tv_loan_old_pay_acc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
		loanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = LoanRepayAccountActivity.selection;// 被点击账户的位置
				/**
				 * overDueStates=new ArrayList<String>(); transFlags=new
				 * ArrayList<String>();
				 */
				/**
				 * overDueState 逾期或欠款状态 String 00未逾期或未欠款 01逾期或欠款 transFlag
				 * 标识该账户可执行的交易 String 1、“advance”可以执行提前还款 2、“none”，不可执行提前还款
				 */
				if ("01".equals(LoanRepayMenuActivity.overDueStates
						.get(position))) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(getResources()
					.getString(R.string.acc_transferquery_not_overDueState));
					return;
				} else if ("none".equals(LoanRepayMenuActivity.transFlags
						.get(position))) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(
							R.string.acc_transferquery_not_transFlags));
					return;
				}
				BaseHttpEngine.showProgressDialog();
//				requestPsnClearanceAccountQuery(null)
				/**603更改为查询所有账户列表*/
				requestPsnCommonQueryAllChinaBankAccount();
			}
		});
	}

//	@Override
//	public void requestPsnClearanceAccountQueryCallBack(Object resultObj) {
//		super.requestPsnClearanceAccountQueryCallBack(resultObj);
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		// 得到result
//		if (!StringUtil.isNullOrEmpty(resultAccList)) {
//			resultAccList.clear();
//		}
//		resultAccList = (List<Map<String, Object>>) biiResponseBody.getResult();
//		if (StringUtil.isNullOrEmpty(resultAccList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getResources().getString(
//							R.string.acc_transferquery_not_trade));
//			return;
//		}
//		BaseHttpEngine.dissMissProgressDialog();
//		getAccountNumbe();
//		gotoActivity();
//	}
	@SuppressWarnings("unchecked")
	@Override
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPsnCommonQueryAllChinaBankAccountCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse)resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		//得到result
		if(!StringUtil.isNullOrEmpty(resultAccList)){
			resultAccList.clear();
		}
		resultAccList = (List<Map<String,Object>>)biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultAccList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(
							R.string.acc_transferquery_not_trade));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		getAccountNumbe();
		gotoActivity();
	}

	/** 根据主账户查询对应的借记卡卡号---回调 */
	public void requestPsnQueryCardNumByAcctNumCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		// 通讯结束,关闭通讯框
		cardNum = (String) (biiResponseBody.getResult());
		// 借记卡卡号,没处理过的
		if (cardNum.equals(payAccountNumber)) {
			tv_loan_old_pay_acc.setText(ConstantGloble.BOCINVT_DATE_ADD);
		} else {
			tv_loan_old_pay_acc.setText(StringUtil.getForSixForString(cardNum));
		}
	}

	/** 得到账户信息 */
	private void getAccountNumbe() {
		accountNumberList = new ArrayList<String>();
		accountTypeList = new ArrayList<String>();
		accountIdList = new ArrayList<String>();
		dealAccountNumberList = new ArrayList<String>();
		int len = resultAccList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = resultAccList.get(i);
			String accountNumber =(String) map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
			String accountType = (String) map.get(Loan.LOAN_ACCOUNTTYPE_RES);
			String accountId = (String) map.get(Loan.LOAN_ACCOUNTID_RES);
			if (!StringUtil.isNull(accountNumber)
					&& !StringUtil.isNull(accountId)
					&& !StringUtil.isNull(accountType)) {
				accountNumberList.add(accountNumber);
				accountIdList.add(accountId);
				String type = LocalData.AccountType.get(accountType);
				accountTypeList.add(type);
				String number = StringUtil.getForSixForString(accountNumber);
				dealAccountNumberList.add(number);
			}
		}
		if (accountNumberList.size() > 0) {
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(
							R.string.acc_transferquery_not_trade));
			return;
		}
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanRepayAccountDetailActivity.this,
				LoanRepayAccountInputActivity.class);
		intent.putExtra(ConstantGloble.POSITION, position);
		BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.LOAN_ACCOUNTNUMBERLIST_KEY,
						accountNumberList);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_ACCOUNTTYPELIST_KEY, accountTypeList);
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.LOAN_ACCOUNTIDLIST_KEY, accountIdList);
		BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.LOAN_DEALACCOUNTNUMBERLIST_KEY,
						dealAccountNumberList);
		BaseDroidApp.getInstanse().getBizDataMap()
		.put(ConstantGloble.LOAN_RESULACCLIST_REQ, resultAccList);
		startActivity(intent);
	}
}
