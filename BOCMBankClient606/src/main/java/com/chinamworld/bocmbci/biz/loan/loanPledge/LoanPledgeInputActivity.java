package com.chinamworld.bocmbci.biz.loan.loanPledge;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 存单质押贷款——输入信息页面 */
public class LoanPledgeInputActivity extends LoanBaseActivity implements OnItemSelectedListener {
	private static final String TAG = "LoanPledgeInputActivity";
	private View inputView = null;
	private TextView loanMoneyText = null;
	private TextView loanCodeText = null;
	private EditText inputMoneyEdit = null;
	private Spinner monthSpinner = null;
	private TextView loanRateText = null;
	/** 收款账户 */
	private Spinner toActNumSpinner = null;
	/** 还款账户 */
	private Spinner payAccountSpinner = null;
	/** 未进行格式化的账户列表 */
	private List<String> accountNumberList = null;
	/** 进行格式化的账户列表 */
	private List<String> dealAccountNumberList = null;
	private List<String> accountTypeList = null;
	private List<String> accountIdList = null;
	private String loanRate = null;
	private String currencyCode = null;
	private String availbalance = null;
	private String mothValue = null;
	// private int position = -1;
	private String toActNum = null;
	private String payAccount = null;
	private boolean initMonth = true;
	/** 浮动比 */
	private String floatingRate = null;
	/** 浮动值 */
	private String floatingValue = null;
	private int toAccPosition = -1;
	private int payAccPosition = -1;
	private Button nextButton = null;
	private String inputMoney = null;
	private String accountNumber = null;
	private String loanPeriodMax = null;
	private String loanPeriodMin = null;
	private List<String> monthList = null;
	//单笔限额上限
	private String singleQuotaMax = null;
	//单笔限额上限
	private	double singleQuotaMaxs;
	//单笔限额下限
	private String singleQuotaMin = null;
	//单笔限额下限
	private	double singleQuotaMins;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_two_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		accountNumberList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_ACCOUNTNUMBERLIST);
		accountTypeList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_ACCOUNTTYPELIST);
		accountIdList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_ACCOUNTIDLIST);
		dealAccountNumberList = (List<String>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_DEALACCOUNTNUMBERLIST);

		loanRate = getIntent().getStringExtra(Loan.LOAN_LOANRATE_RES);
		currencyCode = getIntent().getStringExtra(Loan.LOAN_CURRENCYCODE_RES);
		availbalance = getIntent().getStringExtra(Loan.LOAN_AVAILABLEBALANCE_RES);
		floatingRate = getIntent().getStringExtra(Loan.LOAN_FLOATINGRATE_RES);
		floatingValue = getIntent().getStringExtra(Loan.LOAN_FLOATINGVALUE_RES);
		accountNumber = getIntent().getStringExtra(Loan.LOAN_ACCOUNTNUMBER_RES);
		loanPeriodMax = getIntent().getStringExtra(Loan.LOAN_LOANPERIODMAX_RES);
		loanPeriodMin = getIntent().getStringExtra(Loan.LOAN_LOANPERIODMIN_RES);
		singleQuotaMax=getIntent().getStringExtra(Loan.LOAN_SINGLEQUOTAMAX_RES);
		singleQuotaMin=getIntent().getStringExtra(Loan.LOAN_SINGLEQUOTAMIN_RES);
		//单笔限额上限
	    singleQuotaMaxs=Double.parseDouble(singleQuotaMax);
		//单笔限额下限
		singleQuotaMins=Double.parseDouble(singleQuotaMin);
		if (StringUtil.isNullOrEmpty(accountNumberList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNull(loanRate) || StringUtil.isNull(currencyCode) || StringUtil.isNull(availbalance)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (StringUtil.isNull(loanPeriodMin) || StringUtil.isNull(loanPeriodMax)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		getMonthList();
		if (StringUtil.isNullOrEmpty(monthList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initDate();
		initOnClick();
	}

	private void getMonthList() {
		monthList = new ArrayList<String>();
		int max = Integer.valueOf(loanPeriodMax);
		int min = Integer.valueOf(loanPeriodMin);
		for (int i = min; i <= max; i++) {
			if (i < 10) {
				String a = "0" + String.valueOf(i);
				monthList.add(String.valueOf(a));
			} else {
				monthList.add(String.valueOf(i));
			}
		}
	}

	private void init() {
		inputView = LayoutInflater.from(this).inflate(R.layout.loan_select_input, null);
		tabcontentView.addView(inputView);
		TextView leftText = (TextView) findViewById(R.id.acc_financeic_trans_icact);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
		loanMoneyText = (TextView) findViewById(R.id.loan_money);
		loanCodeText = (TextView) findViewById(R.id.loan_code);
		inputMoneyEdit = (EditText) findViewById(R.id.loan_choise_input_amount);
		monthSpinner = (Spinner) findViewById(R.id.loan_choise_input_loanPeriod);
		loanRateText = (TextView) findViewById(R.id.loan_choise_input_loanRate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, loanRateText);
		toActNumSpinner = (Spinner) findViewById(R.id.loan_choise_input_toActNum);
		payAccountSpinner = (Spinner) findViewById(R.id.loan_choise_input_payAccoun);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		monthSpinner.setOnItemSelectedListener(this);
		toActNumSpinner.setOnItemSelectedListener(this);
		payAccountSpinner.setOnItemSelectedListener(this);
	}

	private void initDate() {
		String money =StringUtil.parseStringPattern(availbalance, 2) ;
//				StringUtil.parseStringCodePattern(availbalance, 2);
		String code = null;
		double moneys=Double.parseDouble(availbalance);
		if(moneys >singleQuotaMaxs){
			availbalance = String.valueOf(singleQuotaMaxs);
			money = StringUtil.parseStringPattern(availbalance, 2) ;
			moneys = Double.parseDouble(availbalance);
			loanMoneyText.setText(StringUtil.parseStringCodePattern(currencyCode, String.valueOf(singleQuotaMaxs), 2));
		}else{
			loanMoneyText.setText(money);
		}
		if (LocalData.Currency.containsKey(currencyCode)) {
			code = LocalData.Currency.get(currencyCode);
		} else {
			code = "-";
		}
//		loanMoneyText.setText(money);
		loanCodeText.setText("人民币元");
		loanRateText.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate) ? ConstantGloble.BOCINVT_DATE_ADD:
            loanRate +"%");
		ArrayAdapter<String> mothAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, monthList);
		mothAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSpinner.setAdapter(mothAdapter);
		monthSpinner.setSelection(0);
		mothValue = monthSpinner.getSelectedItem().toString().trim();

		ArrayAdapter<String> toActNumAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				dealAccountNumberList);
		toActNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toActNumSpinner.setAdapter(toActNumAdapter);
		toActNumSpinner.setSelection(0);
		toActNum = toActNumSpinner.getSelectedItem().toString().trim();

		ArrayAdapter<String> payAccountAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
				dealAccountNumberList);
		payAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		payAccountSpinner.setAdapter(payAccountAdapter);
		payAccountSpinner.setSelection(0);
		payAccount = payAccountSpinner.getSelectedItem().toString().trim();
		toAccPosition = 0;
		payAccPosition = 0;
	}

	private void initOnClick() {
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				inputMoney = inputMoneyEdit.getText().toString().trim();

				if (LocalData.codeNoNumber.contains(currencyCode)) {
					// 日元
					RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.loan_choise_check), inputMoney,
							"spetialAmount", true);
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						checkMoney();
					} else {
						return;
					}
				} else {
					RegexpBean reb1 = new RegexpBean(getResources().getString(R.string.loan_choise_check), inputMoney,
							"newamounts", true);
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						checkMoney();
					} else {
						return;
					}
				}

			}
		});
	}

	/** 查询贷款利率 */
	private void searchRate() {
		if (!StringUtil.isNull(floatingRate)) {
			// 浮动比不为空
			requestPsnLoanRateQuery(mothValue, floatingRate, floatingValue, ConstantGloble.REDRAWTYPE_AUTO);
		} else {
			if (!StringUtil.isNull(floatingValue)) {
				// 浮动值不为空
				requestPsnLoanRateQuery(mothValue, floatingRate, floatingValue, ConstantGloble.REDRAWTYPE_SELF);
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.loan_choise_no_acc_status));
				return;
			}
		}
	}

	/** 查询贷款汇率 */
	@Override
	public void requestPsnLoanRateQueryCallback(Object resultObj) {
		super.requestPsnLoanRateQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		loanRate = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(loanRate)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		loanRateText.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate) ? ConstantGloble.BOCINVT_DATE_ADD:
            loanRate +"%");
	}

	private void checkMoney() {
		double money = Double.valueOf(inputMoney);
		double avai = Double.valueOf(availbalance);
		if (money > avai) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_choise_com));
			return;
		}
		if (money >= singleQuotaMins && money <= singleQuotaMaxs) {
			BaseHttpEngine.showProgressDialog();
			/**603收款账户币种校验*/
//			requestCommConversationId();
			String accountId = (String)accountIdList.get(toAccPosition);
			requestPsnLOANPayeeAcountCheck(accountId, "001");
			
//			Intent intent = new Intent(LoanPledgeInputActivity.this, LoanPledgeReadActivity.class);
//			intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currencyCode);
//			intent.putExtra(Loan.LOAN_AVAILABLEBALANCE_RES, availbalance);
//			intent.putExtra(Loan.LOAN_AMOUNT_REQ, inputMoney);
//			intent.putExtra(Loan.LOAN_LOANPERIOD_REQ, mothValue);
//			intent.putExtra(Loan.LOAN_LOANRATE_RES, loanRate);
//			String toActNum1 = accountNumberList.get(toAccPosition);
//			String toAccountId = accountIdList.get(toAccPosition);
//			String payAccountId = accountIdList.get(payAccPosition);
//			intent.putExtra(ConstantGloble.LOAN_TOACTNUM, toActNum);
//			intent.putExtra(ConstantGloble.LOAN_PAYACCOUNT, payAccount);
//			intent.putExtra(ConstantGloble.LOAN_TOACCOUNTID, toAccountId);
//			intent.putExtra(ConstantGloble.LOAN_PAYACCOUNTID, payAccountId);
//			intent.putExtra(ConstantGloble.LOAN_TOACTNUM1, toActNum1);
//			intent.putExtra(Loan.LOAN_ACCOUNTNUMBER_RES, accountNumber);
//			startActivity(intent);
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse()
					.showInfoMessageDialog(getResources().getString(R.string.loan_choise_input_money));
			return;
		}
	}
	
	/**收款账户符合条件*/
	@Override
	public void checkPayeeAccountSuccess() {
		// TODO Auto-generated method stub
		super.checkPayeeAccountSuccess();
		/**还款账户校验*/
		String accountId = (String)accountIdList.get(payAccPosition);
		requestPsnLOANPayerAcountCheck(accountId, "001");
	}
	/**还款账户符合条件*/
	@Override
	public void checkPayerAccountSuccess() {
		// TODO Auto-generated method stub
		super.checkPayerAccountSuccess();
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(LoanPledgeInputActivity.this, LoanPledgeReadActivity.class);
		intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currencyCode);
		intent.putExtra(Loan.LOAN_AVAILABLEBALANCE_RES, availbalance);
		intent.putExtra(Loan.LOAN_AMOUNT_REQ, inputMoney);
		intent.putExtra(Loan.LOAN_LOANPERIOD_REQ, mothValue);
		intent.putExtra(Loan.LOAN_LOANRATE_RES, loanRate);
		String toActNum1 = accountNumberList.get(toAccPosition);
		String toAccountId = accountIdList.get(toAccPosition);
		String payAccountId = accountIdList.get(payAccPosition);
		intent.putExtra(ConstantGloble.LOAN_TOACTNUM, toActNum);
		intent.putExtra(ConstantGloble.LOAN_PAYACCOUNT, payAccount);
		intent.putExtra(ConstantGloble.LOAN_TOACCOUNTID, toAccountId);
		intent.putExtra(ConstantGloble.LOAN_PAYACCOUNTID, payAccountId);
		intent.putExtra(ConstantGloble.LOAN_TOACTNUM1, toActNum1);
		intent.putExtra(Loan.LOAN_ACCOUNTNUMBER_RES, accountNumber);
		startActivity(intent);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
		switch (adapter.getId()) {
		case R.id.loan_choise_input_loanPeriod:// 贷款期限
			mothValue = monthSpinner.getSelectedItem().toString().trim();
			if (!initMonth) {
				BaseHttpEngine.showProgressDialog();
				loanRateText.setText("");
				// 查询贷款利率
				searchRate();
			} else {
				initMonth = false;
			}
			break;
		case R.id.loan_choise_input_toActNum:// 收款账户
			toAccPosition = position;
			toActNum = toActNumSpinner.getSelectedItem().toString().trim();

			break;
		case R.id.loan_choise_input_payAccoun:// 还款账户
			payAccPosition = position;
			payAccount = payAccountSpinner.getSelectedItem().toString().trim();
			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
