package com.chinamworld.bocmbci.biz.loan.loanPledge;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 成功办理存款质押贷款-完成页面
 * @author Administrator
 *
 */
public class LoanPledgeSuccessActivity extends LoanBaseActivity {
	private static final String TAG = "LoanPledgeSuccessActivity";
	private View confirmView = null;
	private TextView toAccTextView = null;
	private TextView payAccText = null;
	private TextView moneyText = null;
	private TextView codeText = null;
	private TextView timeText = null;
	private TextView rateText = null;
	private TextView transactionIdText = null;
	private TextView accountNumberText = null;
	private Button confirmButton = null;
	private String currencyCode = null;
	private String amount = null;
	private String loanPeriod = null;
	private String loanRate = null;
	private String toActNumber = null;
	private String payAccountNumber = null;
	private String transactionId = null;
	private String accountNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_two_one));
		confirmView = LayoutInflater.from(this).inflate(R.layout.loan_select_success, null);
		tabcontentView.addView(confirmView);
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.INVISIBLE);
		init();
		initDate();
		setValue();
	}

	private void init() {
		toAccTextView = (TextView) findViewById(R.id.loan_choise_input_toActNum);
		payAccText = (TextView) findViewById(R.id.loan_choise_input_payAccoun);
		moneyText = (TextView) findViewById(R.id.loan_choise_input_amount);
		codeText = (TextView) findViewById(R.id.loan_choise_input_code);
		timeText = (TextView) findViewById(R.id.loan_choise_input_loanPeriod);
		rateText = (TextView) findViewById(R.id.loan_choise_input_loanRate);
		transactionIdText = (TextView) findViewById(R.id.loan_choise_transactionId);
		accountNumberText = (TextView) findViewById(R.id.loan_choise_accountNumber);
		confirmButton = (Button) findViewById(R.id.trade_nextButton);
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoanPledgeSuccessActivity.this, LoanPledgeAccountActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initDate() {
		currencyCode = getIntent().getStringExtra(Loan.LOAN_CURRENCYCODE_RES);
		amount = getIntent().getStringExtra(Loan.LOAN_AMOUNT_REQ);
		loanPeriod = getIntent().getStringExtra(Loan.LOAN_LOANPERIOD_REQ);
		loanRate = getIntent().getStringExtra(Loan.LOAN_LOANRATE_RES);
		toActNumber = getIntent().getStringExtra(ConstantGloble.LOAN_TOACTNUM);
		payAccountNumber = getIntent().getStringExtra(ConstantGloble.LOAN_PAYACCOUNT);
		transactionId = getIntent().getStringExtra(Loan.LOAN_TRANSACTIONID_REQ);
		accountNumber = getIntent().getStringExtra(Loan.LOAN_ACCOUNTNUMBER_REQ);
	}

	private void setValue() {
		String code = null;
		if (!StringUtil.isNull(currencyCode) && LocalData.Currency.containsKey(currencyCode)) {
			code = LocalData.Currency.get(currencyCode);
		} else {
			code = "-";
		}

		String money = null;
		if (!StringUtil.isNull(amount) && !StringUtil.isNull(currencyCode)) {
			money = StringUtil.parseStringCodePattern(currencyCode, amount, 2);
		} else {
			money = "-";
		}
		codeText.setText("人民币元");
		String toNumber = StringUtil.getForSixForString(toActNumber);
		toAccTextView.setText(toNumber);
		payAccText.setText(payAccountNumber);
		moneyText.setText(money);
		String month = getResources().getString(R.string.month);
		timeText.setText(loanPeriod + month);
		rateText.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate)?ConstantGloble.BOCINVT_DATE_ADD:
            loanRate +"%");
		transactionIdText.setText(transactionId);
		String accto = StringUtil.getForSixForString(accountNumber);
		accountNumberText.setText(accto);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
