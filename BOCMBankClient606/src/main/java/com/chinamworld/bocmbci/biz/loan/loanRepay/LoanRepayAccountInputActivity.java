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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanSpinnerCustomAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 提前还款------输入信息页面 */
public class LoanRepayAccountInputActivity extends LoanBaseActivity {
	private static final String TAG = "LoanRepayAccountInputActivity";
	private View detailView = null;
	private TextView typeText = null;
	private TextView accText = null;
	private TextView moneyText = null;
	private TextView codeText = null;
	private TextView timesText = null;
	private TextView loantimesText = null;
	private TextView moreMoneyText = null;
	private TextView payTypeText = null;
	private TextView loanCountText = null;
	/** 本期还款日 */
	private TextView loanTimesText = null;
	/** 本期应还款总额 */
	private TextView loanMoneyText = null;
	/** 本期截止当前应还利息 */
	private TextView loanRateText = null;
	/** 提前还款金额 */
	private EditText rateText = null;
	/** 还款账户 */
	private Spinner accountSpinner = null;
	private TextView accSpinnerText = null;
	private int position = -1;
	private int accPosition = -1;
	private List<Map<String, String>> loanAccountList = null;
	private Map<String, Object> detailRasultMap = null;
	private Button loanButton = null;
	private TextView leftText = null;
	/** 未进行格式化的账户列表 */
	private List<String> accountNumberList = null;
	/** 进行格式化的账户列表 */
	private List<String> dealAccountNumberList = null;
	private List<String> accountTypeList = null;
	private List<String> accountIdList = null;
	private String accountNumberDeal = null;
	private boolean initAcc = true;
	private String loanType = null;
	private String loanAccount = null;
	private String currency = null;
	/** 提前还款金额 */
	private String repayAmount = null;
	private String fromAccountId = null;
	private String accoutNumber = null;
	//利息
	String thisIssueRepayInterests=null;
	//
	private List<Map<String, Object>> resultAccList = null;
	
//	private Map<String, String> LoanProtocolmap;

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
		detailRasultMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_RESULT);
		position = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		loanAccountList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(Loan.LOAN_LOANACCOUNTLIST_REQ);
		accountNumberList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_ACCOUNTNUMBERLIST_KEY);
		accountTypeList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_ACCOUNTTYPELIST_KEY);
		accountIdList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_ACCOUNTIDLIST_KEY);
		dealAccountNumberList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_DEALACCOUNTNUMBERLIST_KEY);
		resultAccList= (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_RESULACCLIST_REQ);
		if (StringUtil.isNullOrEmpty(loanAccountList) || StringUtil.isNullOrEmpty(detailRasultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (position < 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNullOrEmpty(accountNumberList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		getDate();
		initOnClick();
	}

	private void getDate() {
		//map里的数据是从上个页面传下的数据;detailRasultMap里的数据是接口里的数据
		//Map<String, String> map = loanAccountList.get(position);
		loanType = (String) detailRasultMap.get(Loan.LOAN_LOANTYPE_REQ);
		loanAccount = (String) detailRasultMap.get(Loan.LOAN_LOANACCOUNT_REQ);
		String loanAmount = (String) detailRasultMap.get(Loan.LOANACC_LOAN_AMOUNT_RES);
		currency = (String) detailRasultMap.get(Loan.LOAN_CURRENCY_REQ);
		String number = null;
		if (!StringUtil.isNull(loanAccount)) {
			number = StringUtil.getForSixForString(loanAccount);
		} else {
			number = "-";
		}
		String code = null;
		if (!StringUtil.isNull(currency) && LocalData.Currency.containsKey(currency)) {
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
		String loanPeriod = (String) detailRasultMap.get(Loan.LOAN_LOANPERIOD_REQ);
		String loanToDate = (String) detailRasultMap.get(Loan.LOAN_LOANTODATE_REQ);
		String remainCapital = (String) detailRasultMap.get(Loan.LOAN_REMAINCAPITAL_REQ);
		String moreMoney = null;
		if (!StringUtil.isNull(remainCapital)) {
			moreMoney = StringUtil.parseStringCodePattern(currency, remainCapital, 2);
		} else {
			moreMoney = "-";
		}
		String interestType = (String) detailRasultMap.get(Loan.LOAN_INTTYPETERES_REQ);
		String remainIssue = (String) detailRasultMap.get(Loan.LOAN_REMAINISSUE_REQ);
		String thisIssueRepayDate = (String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYDATE_REQ);
		String thisIssueRepayAmount = (String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYAMOUNT_REQ);
		String thisIssueRepayAmounts = null;
		if (!StringUtil.isNull(thisIssueRepayAmount)) {
			thisIssueRepayAmounts = StringUtil.parseStringCodePattern(currency, thisIssueRepayAmount, 2);
		} else {
			thisIssueRepayAmounts = "-";
		}
		
		String thisIssueRepayInterest = (String) detailRasultMap.get(Loan.LOAN_THISISSUEREPAYINTEREST_REQ);
		
		if (!StringUtil.isNull(thisIssueRepayInterest)) {
			thisIssueRepayInterests = StringUtil.parseStringCodePattern(currency, thisIssueRepayInterest, 2);
		} else {
			thisIssueRepayInterests = "-";
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
		String month=getResources().getString(R.string.month);
		timesText.setText(loanPeriod+month);
		loantimesText.setText(StringUtil.isNullOrEmptyCaseNullString(loanToDate)?ConstantGloble.BOCINVT_DATE_ADD:
				loanToDate);
		moreMoneyText.setText(moreMoney);
		payTypeText.setText(interestTypes);
		loanCountText.setText(remainIssue);
		loanTimesText.setText(StringUtil.isNullOrEmptyCaseNullString(thisIssueRepayDate)?ConstantGloble.BOCINVT_DATE_ADD:
				thisIssueRepayDate);
		loanMoneyText.setText(thisIssueRepayAmounts);
		loanRateText.setText(thisIssueRepayInterests);
//		ArrayAdapter<String> payAccountAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
//				dealAccountNumberList);
		
		List<Map<String, Object>> rmbList = new ArrayList<Map<String,Object>>();
		rmbList = resultAccList;
		LoanSpinnerCustomAdapter rmbAdapter = new LoanSpinnerCustomAdapter(this, resultAccList);
//		tv_loan_new_pay_acc.setAdapter(rmbAdapter);
		
//		payAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accountSpinner.setAdapter(rmbAdapter);
		accountSpinner.setSelection(0);
		
		accountNumberDeal = accountSpinner.getSelectedItem().toString().trim();
		fromAccountId = accountIdList.get(0);
		accoutNumber = accountNumberList.get(0);
		accSpinnerText.setText(StringUtil.getForSixForString(accoutNumber));
		accPosition = 0;
	}

	private void init() {
		detailView = LayoutInflater.from(this).inflate(R.layout.loan_repay_input, null);
		tabcontentView.addView(detailView);
		typeText = (TextView) findViewById(R.id.loan_choise_input_loanType);
		accText = (TextView) findViewById(R.id.loan_choise_accountNumber1);
		moneyText = (TextView) findViewById(R.id.loan_choise_pa_moneys);
		codeText = (TextView) findViewById(R.id.loan_choise_input_code);
		timesText = (TextView) findViewById(R.id.loan_repay_loanPeriod);
		loantimesText = (TextView) findViewById(R.id.loan_repay_loanToDate);
		moreMoneyText = (TextView) findViewById(R.id.loan_repay_remainCapital);
		payTypeText = (TextView) findViewById(R.id.loan_repay_interestType);
		loanCountText = (TextView) findViewById(R.id.loan_repay_remainIssue);
		loanTimesText = (TextView) findViewById(R.id.loan_repay_thisIssueRepayDate);
		loanMoneyText = (TextView) findViewById(R.id.loan_repay_thisIssueRepayAmount);
		loanRateText = (TextView) findViewById(R.id.loan_repay_thisIssueRepayInterest);
		rateText = (EditText) findViewById(R.id.loan_repay_loanRate);
		accountSpinner = (Spinner) findViewById(R.id.loan_repay_payAccountNumber);
		accSpinnerText = (TextView) findViewById(R.id.text_spinner);
		loanButton = (Button) findViewById(R.id.trade_nextButton);
		leftText = (TextView) findViewById(R.id.text_left);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
		accountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
				accPosition = position;
				fromAccountId = accountIdList.get(position);
				accoutNumber = accountNumberList.get(position);
				accSpinnerText.setText(StringUtil.getForSixForString(accoutNumber));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void initOnClick() {
		initAcc = false;
		loanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				repayAmount = rateText.getText().toString().trim();
				if (LocalData.codeNoNumber.contains(currency)) {
					// 日元
					RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.loan_repay_edit_money1),
							repayAmount, "spetialAmount", true);
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						BaseHttpEngine.showProgressDialog();
						requestCommConversationId();
					} else {
						return;
					}
				} else {
					RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.loan_repay_edit_money1),
							repayAmount, "amount", true);
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						BaseHttpEngine.showProgressDialog();
						requestCommConversationId();
					} else {
						return;
					}
				}

			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 请求安全因子组合id
//		requestGetSecurityFactor(ConstantGloble.LOAN_PB1)
		/**603添加还款账户校验*/
		requestPsnLOANPayerAcountCheck(fromAccountId, currency);
	}
	/**还款账户符合条件*/
	@Override
	public void checkPayerAccountSuccess() {
		// TODO Auto-generated method stub
		super.checkPayerAccountSuccess();
		// 请求安全因子组合id
		requestGetSecurityFactor(ConstantGloble.LOAN_PB1);
	}
	
	// 请求安全因子组合id
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
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
				// 存单质押贷款预交易
				requestPsnLOANAdvanceRepayVerify(BaseDroidApp.getInstanse().getSecurityChoosed());
			}
		}, securityIdList, securityNameList);
//		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				System.out.println("BaseDroidApp.getInstanse().getSecurityChoosed()===="+
//			                            BaseDroidApp.getInstanse().getSecurityChoosed());
//				BaseHttpEngine.showProgressDialog();
//				// 存单质押贷款预交易
//				requestPsnLOANAdvanceRepayVerify(BaseDroidApp.getInstanse().getSecurityChoosed());
//			}
//		});
	}

	/** 提前还款预交易 */
	private void requestPsnLOANAdvanceRepayVerify(String combinId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANADVANCEREPAYVERIFY_API);
		String commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_LOANTYPE_REQ, loanType);
		map.put(Loan.LOAN_LOANACCOUNT_REQ, loanAccount);
		map.put(Loan.LOAN_CURRENCY_REQ, currency);
		//生产问题修改 预交易repayAmount上送“提前还款本金”（用户输入）和“thisIssueRepayInterest截止当前应还利息”（详情接口返回数据）的总和。
		if(thisIssueRepayInterests.equals("-")){
			thisIssueRepayInterests = "0";
		}
		Double thisIssueRepayInterest = Double.valueOf(thisIssueRepayInterests);
		Double repayAmount = Double.valueOf(rateText.getText().toString().trim());
		map.put(Loan.LOAN_REPAYAMOUNT_RES, String.valueOf(thisIssueRepayInterest+repayAmount));
		map.put(Loan.LOAN_FROMACCOUNTID_RES, fromAccountId);
		map.put(Loan.LOAN_ACCOUNTNUMBERS_RES, accoutNumber);
		map.put(Loan.LOAN_COMBINID_REQ, combinId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANAdvanceRepayVerifyCallback");
	}

	/** 提前还款预交易 */
	public void requestPsnLOANAdvanceRepayVerifyCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> preResult = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(preResult)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_FACTORLIST, preResult);
		// 请求密码控件随机数
		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_RANDOMNUMBER, randomNumber);		
		gotoActivity();
	}

	private void gotoActivity() {
		Intent intent = new Intent(LoanRepayAccountInputActivity.this, LoanProtocolActivity.class);//原本跳转的界面LoanRepayAccountConfirmActivity
		intent.putExtra(ConstantGloble.POSITION, position);
		intent.putExtra(ConstantGloble.FOREX_SELLINLIST_POSITION, accPosition);
		intent.putExtra("repayAmount", repayAmount);
		intent.putExtra(Loan.LOAN_FROMACCOUNTID_RES, fromAccountId);
		intent.putExtra(Loan.LOAN_ACCOUNTNUMBERS_RES, accoutNumber);
		
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}
}
