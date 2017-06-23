package com.chinamworld.bocmbci.biz.loan.loanUse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
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
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 用款输入信息页面
 * 
 * @author wanbing
 * 
 */
public class LoanUseInputActivity extends LoanBaseActivity {
	private View contentView = null;
	/** 贷款品种 */
	private TextView tv_loan_type;
	/** 可用金额 */
	private TextView tv_loan_cycleAvaAmount;
	/** 本次可用金额 */
	private EditText edit_use_amt;
	/** 贷款期限 */
	private TextView tv_loan_CycleLifeTerm;
	/** 放款截止日 */
	private TextView tv_loan_cycleDrawdownDate;
	/** 贷款到期日 */
	private TextView tv_loan_cycleMatDate;
	/** 贷款利率 */
	private TextView tv_loan_CycleRate;
	/** 还款账户 */
	private TextView tv_loan_CycleRepayAccount;
	/** 收款账户 */
	private TextView tv_loan_toActNum;
	private Spinner sp_loan_toActNum;
	private TextView tv_loan_currencycode;

	private Button nextBtn;
	private Map<String, Object> loanUseMap;
	private String useAmount;
	/** 选择账户的下标 */
	private int selectPos = -1;
	/** 收款账户列表 */
	private List<Map<String, Object>> toAccountList;
	Map<String, Object> loanUsePremap;
	private String loan_accNum = null;
	private String currency = null;
	private String loanTimes = null;
	private String loanRate = null;
	private String loanPay = null;
	//最低放款额
	private String loanCycleMinAmount = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(getString(R.string.loan_use_1));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		setLeftSelectedPosition("loan_2");
		contentView = mInflater.inflate(R.layout.loan_use_input, null);
		tabcontentView.addView(contentView);
		loan_accNum = getIntent().getStringExtra(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
		loanCycleMinAmount = getIntent().getStringExtra(Loan.LOAN_CYCLE_MINAMOUNT);
		currency = getIntent().getStringExtra(Loan.LOANACC_CURRENCYCODE_RES);
		toAccountList=LoanCycleAccountActivity.toAccountList;
//		BaseHttpEngine.showProgressDialog();
//		requestPsnClearanceAccountQuery(null);
		init();
		setSpinnerData();
	}

	/**
	 * @Title: init
	 * @Description: 初始化控件和数据
	 * @param
	 * @return void
	 */
	private void init() {
		loanUseMap = LoanDataCenter.getInstance().getLoanUsemap();
		tv_loan_type = (TextView) findViewById(R.id.tv_loan_type);
		tv_loan_cycleAvaAmount = (TextView) findViewById(R.id.tv_loan_cycleAvaAmount);
		edit_use_amt = (EditText) findViewById(R.id.edit_use_amt);
		tv_loan_CycleLifeTerm = (TextView) findViewById(R.id.tv_loan_CycleLifeTerm);
		tv_loan_cycleDrawdownDate = (TextView) findViewById(R.id.tv_loan_cycleDrawdownDate);
		tv_loan_cycleMatDate = (TextView) findViewById(R.id.tv_loan_cycleMatDate);
		tv_loan_CycleRate = (TextView) findViewById(R.id.tv_loan_CycleRate);
		tv_loan_CycleRepayAccount = (TextView) findViewById(R.id.tv_loan_CycleRepayAccount);
		tv_loan_toActNum = (TextView) findViewById(R.id.tv_loan_toActNum);
		sp_loan_toActNum = (Spinner) findViewById(R.id.sp_loan_toActNum);
		tv_loan_currencycode = (TextView) contentView.findViewById(R.id.loan_currencycode_value);
		tv_loan_currencycode.setText(LocalData.Currency.get(LoanCycleAccountActivity.currency));
		nextBtn = (Button) this.findViewById(R.id.next_btn);

		String loan_type = (String) loanUseMap.get(Loan.LOANACC_LOAN_TYPE_RES);
		tv_loan_type.setText(LoanData.loanTypeData.get(loan_type));
		String loan_amount = (String.valueOf(loanUseMap.get(Loan.LOAN_CYCLE_AVA_AMOUNT)));
		tv_loan_cycleAvaAmount.setText(StringUtil.parseStringCodePattern(LoanCycleAccountActivity.currency,loan_amount, 2));
		loanTimes = String.valueOf(loanUseMap.get(Loan.LOAN_CYCLE_LIFETERM));
		tv_loan_CycleLifeTerm.setText(loanTimes + "月");
		tv_loan_cycleDrawdownDate.setText(String.valueOf(loanUseMap.get(Loan.LOAN_CYCLE_DRAWDOWNDATE)));
		String loanCycleMatDate=String.valueOf(loanUseMap.get(Loan.LOAN_CYCLE_MATDATE));
		tv_loan_cycleMatDate.setText(StringUtil.isNullOrEmptyCaseNullString(loanCycleMatDate)?ConstantGloble.BOCINVT_DATE_ADD:
			loanCycleMatDate);
		loanRate = String.valueOf(loanUseMap.get(Loan.LOAN_CYCLERATE));
		float  loanRate1 = Float.valueOf(loanRate);
		if(0.0 == loanRate1){
			tv_loan_CycleRate.setText( ConstantGloble.BOCINVT_DATE_ADD);
		}else {
			tv_loan_CycleRate.setText(StringUtil.isNullOrEmptyCaseNullString(loanRate) ? 
					ConstantGloble.BOCINVT_DATE_ADD:loanRate +"%");
		}
		loanPay=(String) loanUseMap.get(Loan.LOAN_CYCLE_REPAYACCOUNT);
		if("0".equals(loanPay)){
			tv_loan_CycleRepayAccount.setText(ConstantGloble.BOCINVT_DATE_ADD);
		}else{
			tv_loan_CycleRepayAccount.setText(
					StringUtil.isNullOrEmptyCaseNullString(loanPay)?ConstantGloble.BOCINVT_DATE_ADD:
						StringUtil.getForSixForString(loanPay));
		}

		// 点击收款账户
		tv_loan_toActNum.setOnClickListener(toChooseToAccountClickListener);
		// 点击下一步
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextExcuse();
			}
		});
	}

	/**
	 * 选择收款账户点击
	 */
	private View.OnClickListener toChooseToAccountClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			sp_loan_toActNum.performClick();
		}
	};

//	@Override
//	public void requestPsnClearanceAccountQueryCallBack(Object resultObj) {
//		super.requestPsnClearanceAccountQueryCallBack(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		toAccountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
//		if (StringUtil.isNullOrEmpty(toAccountList)) {
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_not_trade));
//			return;
//		}
//		init();
//		setSpinnerData();
//	}

	private void setSpinnerData() {
		// 收款账户选择
		List<String> toList = new ArrayList<String>();
		for (int i = 0; i < toAccountList.size(); i++) {
			Map<Object, Object> tempMap = EpayUtil.getMap(toAccountList.get(i));
			String accType = EpayUtil.getString(tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE),
					"");
			String accNum = StringUtil.getForSixForString(EpayUtil.getString(
					tempMap.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER), ""));
			toList.add(accNum);
		}

		ArrayAdapter<String> toAccountAdapter = new ArrayAdapter<String>(LoanUseInputActivity.this,
				R.layout.custom_spinner_item, toList);
		toAccountAdapter.setDropDownViewResource(R.layout.epay_spinner_list_item);
		sp_loan_toActNum.setAdapter(toAccountAdapter);
		sp_loan_toActNum.setOnItemSelectedListener(spSelectedClick);
		sp_loan_toActNum.setSelection(0);
		selectPos = 0;
	}

	/** sp点击事件 **/
	private OnItemSelectedListener spSelectedClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			selectPos = position;
			setToAccTextView();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	};

	/**
	 * 设置收款账户文本
	 */
	private void setToAccTextView() {
		if (selectPos == -1) {
			tv_loan_toActNum.setText("请选择");
		} else {
			String accNum = StringUtil.getForSixForString((String) toAccountList.get(selectPos).get(
					Acc.ACC_ACCOUNTNUMBER_RES));
			tv_loan_toActNum.setText(accNum);
		}
	}

	/**
	 * @Title: nextExcuse
	 * @Description: 执行下一步
	 * @param
	 * @return void
	 */
	private void nextExcuse() {
		useAmount = edit_use_amt.getText().toString().trim();
		if (LocalData.codeNoNumber.contains(currency)) {
			// 日元
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regAmount = new RegexpBean(this.getString(R.string.loan_choise_check), useAmount,
					"jpnAmount");
			lists.add(regAmount);
			if (RegexpUtils.regexpDate(lists)) {
				chackMoney();
			} else {
				return;
			}
		} else {
			// 非日元
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regAmount = new RegexpBean(this.getString(R.string.loan_choise_check), useAmount, "newamounts");
			lists.add(regAmount);
			if (RegexpUtils.regexpDate(lists)) {
				chackMoney();
			} else {
				return;
			}
		}

	}

	private void chackMoney() {
		double intputmoney = Double.valueOf(useAmount);
		//最低放款额 double
		double mLoanCycleMinAmount = Double.valueOf(loanCycleMinAmount);
		
		String loan_amount = (String.valueOf(loanUseMap.get(Loan.LOAN_CYCLE_AVA_AMOUNT)));
		String loan_amounts=StringUtil.parseStringCodePattern(LoanCycleAccountActivity.currency,loan_amount, 2);
		String loan_amountsTwo=StringUtil.parseStringCodePattern(LoanCycleAccountActivity.currency,loanCycleMinAmount, 2);
		double balance = Double.valueOf(loan_amount);
		if (intputmoney > balance || intputmoney < mLoanCycleMinAmount) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_use_input_money_new_one)+
					loan_amountsTwo+getResources().getString(R.string.loan_use_input_money_new_two)+loan_amounts+"的数值");
			return;
		} else {
			BaseHttpEngine.showProgressDialog();
			/**603收款账户检查校验*/
			String accountId = (String)toAccountList.get(selectPos).get(Acc.ACC_ACCOUNTID);
			requestPsnLOANPayeeAcountCheck(accountId, currency);
//			gotoNextActivity()
		}
	}
	/**收款账户符合条件*/
	@Override
	public void checkPayeeAccountSuccess() {
		// TODO Auto-generated method stub
		super.checkPayeeAccountSuccess();
		BaseHttpEngine.dissMissProgressDialog();
		gotoNextActivity();
	}
	
	private void gotoNextActivity() {
		Intent intent = new Intent(this, LoanUseReadActivity.class);
		intent.putExtra(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES, loan_accNum);
		intent.putExtra("useAmount", useAmount);
		intent.putExtra(Loan.LOANACC_CURRENCYCODE_RES, currency);
		intent.putExtra(Loan.LOAN_CYCLE_LIFETERM, loanTimes);
		intent.putExtra(Loan.LOAN_CYCLERATE, loanRate);
		intent.putExtra(Loan.LOAN_CYCLE_REPAYACCOUNT, loanPay);
		String loanTo = (String) toAccountList.get(selectPos).get(Acc.ACC_ACCOUNTNUMBER_RES);
		loanTo = StringUtil.getForSixForString(loanTo);
		intent.putExtra(Acc.ACC_ACCOUNTNUMBER_RES, loanTo);
		intent.putExtra(ConstantGloble.LOAN_SELECTPOS, selectPos);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_TOACCOUNTLISTS, toAccountList);
		startActivity(intent);
	}

}
