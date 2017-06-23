package com.chinamworld.bocmbci.biz.loan.loanRepay;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanData;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanSpinnerCustomAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 变更还款账户输入信息页面
 * 
 * @author wanbing
 * 
 */
public class LoanChangeLoanRepayAccountInputActivity extends LoanBaseActivity {
	private static final String TAG = "LoanChangeLoanRepayAccountInputActivity";
	private View contentView = null;
	/** 贷款品种 */
	private TextView tv_loan_type;
	/** 贷款账号 */
	private TextView tv_loan_acc_num;
	/** 对应账户借记卡卡号 */
	private TextView tv_loan_old_pay_acc;
	String payNumber;
	String newpayNumber;
	/** 新的还款卡号/账号 */
	private Spinner tv_loan_new_pay_acc;

	private TextView tv_loan_old_pay_acc_label;
	private TextView tv_loan_new_pay_acc_label;
	//原还款账号
	private TextView tv_loan_pay_acc_num;
	

	private Button nextBtn;
	/** 变更还款账户信息 */
	private Map<String, String> loanChangeRepayAccmap;
	Map<String, Object> loanChangeRepayAccPremap;
	private String loan_type = null;
	private String currency = null;
	/** 1-人民币，2-外币 */
	private int tag = 0;
	private List<Map<String, Object>> rmbAccountList = null;
	private List<Map<String, Object>> wbAccountList = null;
	private List<String> accList;
	private int rmbSelection = -1;
	private int wbSelection = -1;
	private String isClearanceAccount = null;
	private TextView accSpinnerText = null;
	private boolean init = true;
	private boolean firstInit = true;
	//外币跳转条件
	private boolean bCheckCurreny = false;
//	private boolean bCheckBalance = false;
	private boolean isClickSpinner =false;
	private String accNum = null;
	private String currencyName ="";
//	/**根据原还款账号查询后的想对应的借记卡卡号  没处理过的数据*/
//	public static  String	cardNumsrc =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(getString(R.string.loan_change_repay_acc));
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.VISIBLE);
		setLeftSelectedPosition("loan_3");
		contentView = mInflater.inflate(R.layout.loan_change_repay_acc_input,
				null);
		tabcontentView.addView(contentView);
		loanChangeRepayAccmap = LoanDataCenter.getInstance()
				.getLoanChangeRepayAccmap();
		currency = loanChangeRepayAccmap.get(Loan.LOANACC_CURRENCYCODE_RES);
		init();
		tag = getIntent().getIntExtra(ConstantGloble.FOREX_TAG, 0);
		accSpinnerText.setVisibility(View.VISIBLE);
		if (tag == 1) {
			rmbAccountList = (List<Map<String, Object>>) BaseDroidApp
					.getInstanse().getBizDataMap()
					.get(ConstantGloble.LOAN_RMB_TOACCOUNTLIST);
			// 人民币
			// accSpinnerText.setVisibility(View.GONE);
			tv_loan_new_pay_acc.setVisibility(View.VISIBLE);
			initRmbSpinner();
		} else if (tag == 2) {
			// 现在不因为是外币而
			// accSpinnerText.setVisibility(View.GONE);
			tv_loan_new_pay_acc.setVisibility(View.VISIBLE);
			wbAccountList = (List<Map<String, Object>>) BaseDroidApp
					.getInstanse().getBizDataMap()
					.get(ConstantGloble.LOAN_WB_RESULTLIST);
			initWbSpinner();
		}
	}

	/**
	 * @Title: init
	 * @Description: 初始化控件和数据
	 * @param
	 * @return void
	 */
	private void init() {
		tv_loan_type = (TextView) findViewById(R.id.tv_loan_type);
		tv_loan_acc_num = (TextView) findViewById(R.id.tv_loan_acc_num);
		tv_loan_old_pay_acc = (TextView) findViewById(R.id.tv_loan_old_pay_acc);
		tv_loan_new_pay_acc = (Spinner) findViewById(R.id.tv_loan_new_pay_acc);
		tv_loan_old_pay_acc_label = (TextView) findViewById(R.id.tv_loan_old_pay_acc_label);
		tv_loan_new_pay_acc_label = (TextView) findViewById(R.id.tv_loan_new_pay_acc_label);
		tv_loan_pay_acc_num=(TextView) findViewById(R.id.tv_loan_pay_acc_num);
		
		nextBtn = (Button) this.findViewById(R.id.next_btn);
		accSpinnerText = (TextView) findViewById(R.id.text_spinner);
		loan_type = (String) loanChangeRepayAccmap
				.get(Loan.LOANACC_LOAN_TYPE_RES);
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
		tv_loan_type.setText(loantype);
		String loanNumber = String.valueOf(loanChangeRepayAccmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES));
		tv_loan_acc_num.setText(StringUtil.getForSixForString(loanNumber));
		payNumber = String.valueOf(loanChangeRepayAccmap
				.get(Loan.LOAN_CYCLE_REPAYACCOUNT));
		String payNum = null;
		if (StringUtil.isNullOrEmptyCaseNullString(payNumber)) {
			payNum = "-";
		} else {
			payNum = StringUtil.getForSixForString(payNumber);
		}
		//借记卡卡号,没处理过的
		String cardNumsrc=LoanChangeLoanRepayAccountChooseActivity.cardNumsrc;
		tv_loan_pay_acc_num.setText(payNum);
		
		if(cardNumsrc.equals(payNumber)){
			tv_loan_old_pay_acc.setText(ConstantGloble.BOCINVT_DATE_ADD);
		}else{
			tv_loan_old_pay_acc.setText(StringUtil.getForSixForString(cardNumsrc));
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				LoanChangeLoanRepayAccountInputActivity.this,
				tv_loan_old_pay_acc_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				LoanChangeLoanRepayAccountInputActivity.this,
				tv_loan_new_pay_acc_label);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				LoanChangeLoanRepayAccountInputActivity.this,
				tv_loan_pay_acc_num);
		// 点击新的收款账户
		tv_loan_new_pay_acc.setOnItemSelectedListener(itemSelectedListener);
		// 点击下一步
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextExcuse();
			}
		});
		// 选择新还款账户
		accSpinnerText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = tv_loan_new_pay_acc.getSelectedItemPosition();
				if (tag == 1) {
					rmbSelection = position;
				} else if (tag == 2) {
					wbSelection = position;
				}
				isClickSpinner=true;
				tv_loan_new_pay_acc.performClick();
				accSpinnerText.setVisibility(View.VISIBLE);
			}
		});
		
	}



	public void onWindowFocusChanged(boolean hasFocus) {
		if((hasFocus)&&isClickSpinner&&(tv_loan_new_pay_acc.getSelectedItemPosition() == 0)){
			if(tag == 2){
				accSpinnerText.setText(wbAccountList.get(0).get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)+ "");
//				BaseHttpEngine.showProgressDialog();
				spinnerRquest(0);
			}
			if(tag == 1){
				accSpinnerText.setText(rmbAccountList.get(0).get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES)+ "");
//				BaseHttpEngine.showProgressDialog();
				spinnerRquest(0);
			}
			isClickSpinner=false;
		}

	};

	/** 人民币----初始化账户下拉框 */
	ArrayAdapter<String> rmbAdapter;

	private void initRmbSpinner() {
		// List<String> accList = new ArrayList<String>();
		accList = new ArrayList<String>();
		int len = rmbAccountList.size();
		for (int i = 0; i < rmbAccountList.size(); i++) {
			Map<String, Object> map = rmbAccountList.get(i);
			String accountNumber = (String) map
					.get(Loan.LOAN_ACCOUNTNUMBER_RES);
			accNum = StringUtil.getForSixForString(accountNumber);

			if (!StringUtil.isNull(accountNumber)) {
				if (accountNumber.equals(payNumber)) {
//					accList.add(accNum);
					rmbAccountList.remove(i);
					i--;
				}
			}

		}
		
		
//		rmbAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner,
//				accList);
//		rmbAdapter
//		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		List<Map<String,Object>> rmbList = new ArrayList<Map<String,Object>>();
		rmbList = rmbAccountList;
		LoanSpinnerCustomAdapter rmbAdapter = new LoanSpinnerCustomAdapter(this, rmbList);
		tv_loan_new_pay_acc.setAdapter(rmbAdapter);
//		tv_loan_new_pay_acc.setSelection(0);
//		rmbSelection = 0;
	}

	/** 外币----初始化账户下拉框 */
	private void initWbSpinner() {
		accList = new ArrayList<String>();
		int len = wbAccountList.size();
		for (int i = 0; i < wbAccountList.size(); i++) {
			Map<String, Object> map = wbAccountList.get(i);
			String accountNumber = (String) map
					.get(Loan.LOAN_ACCOUNTNUMBER_RES);
			accNum = StringUtil.getForSixForString(accountNumber);

			if (!StringUtil.isNull(accountNumber)) {
				if (accountNumber.equals(payNumber)) {
//					accList.add(accNum);
					wbAccountList.remove(i);
					i--;
				}
			}
		}
//		ArrayAdapter<String> rmbAdapter = new ArrayAdapter<String>(this,
//				R.layout.spinner_myitem, accList);
		
		List<Map<String,Object>> wbList = new ArrayList<Map<String,Object>>();
		wbList = wbAccountList;
		LoanSpinnerCustomAdapter rmbAdapter = new LoanSpinnerCustomAdapter(this, wbList);
		
//		rmbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tv_loan_new_pay_acc.setAdapter(rmbAdapter);
		tv_loan_new_pay_acc.setSelection(0);
	}

	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> adapter, View view,
				int position, long id) {

			if (firstInit) {
				firstInit = false;
				return;
			}
			if (tag == 1) {
				rmbSelection = position;
//				BaseHttpEngine.showProgressDialog();
				spinnerRquest(position);
			} else if (tag == 2) {
				wbSelection = position;
//				BaseHttpEngine.showProgressDialog();
				spinnerRquest(position);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {
			if (tag == 2) {
				accSpinnerText.setVisibility(View.VISIBLE);
				tv_loan_new_pay_acc.setVisibility(View.GONE);
			}
		}
	};


	/** 下拉列表框请求数据 */
	private void spinnerRquest(int position) {
		if(tag == 1){
			String accountNumber=(String)rmbAccountList.get(position).get(Loan.LOAN_ACCOUNTNUMBER_RES);
			String accountId = (String) rmbAccountList.get(position).get(Loan.LOAN_ACCOUNTID_RES);
			accSpinnerText.setVisibility(View.VISIBLE);
			accSpinnerText.setText( StringUtil.getForSixForString(accountNumber));
//			requestQueryAccountDetail(accountId);
		}else if(tag==2){
			Map<String, Object> map;
			map = wbAccountList.get(position);
			String accountId = (String) map.get(Loan.LOAN_ACCOUNTID_RES);
			String accountNumber = (String) map.get(Loan.LOAN_ACCOUNTNUMBER_RES);
			String accNum = StringUtil.getForSixForString(accountNumber);
			accSpinnerText.setVisibility(View.VISIBLE);
			accSpinnerText.setText(accNum);
//			requestQueryAccountDetail(accountId);
		}

	}

	/**
	 * 根据用户accountId 查询账户详情
	 */
	public void requestQueryAccountDetail(String accountId) {
		// 展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.PRMS_QUERY_ACCOUNT_DETAIL);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestQueryAccountDetailCallBack");
	}

	/**
	 * 查询账户详情返回
	 * 
	 * @param resultObj
	 */
	public void requestQueryAccountDetailCallBack(Object resultObj) {
		
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		bCheckCurreny=false;
//		bCheckBalance=false;
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get(Dept.DEPT_ACCOUNTDETAILIST_RES);
          
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}

		if(tag==2){
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap = resultList.get(i); 
				currencyName = LocalData.CurrencyShort.get(currency);
				
				String responseCurrencyName = LocalData.CurrencyShort.get(resultMap.get(Loan.LOAN_CURRENCYCODES_RES));
				String valueNumber= String.valueOf(resultMap.get(Loan.LOAN_AVAILABLEBALANCE_RES));
				
				if (currencyName.equals(responseCurrencyName)
						&& "02".equals(resultMap.get(Loan.LOAN_CASHREMIT_RES))) {
					bCheckCurreny = true;
//					
//					if (Float.parseFloat(valueNumber)>0) {
//						bCheckBalance = true;
//						break;
//					}
				}
			}
		}else if(tag==1){
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap = resultList.get(i); 
				currencyName = LocalData.CurrencyShort.get(currency);
				
				String responseCurrencyName = LocalData.CurrencyShort.get(resultMap.get(Loan.LOAN_CURRENCYCODES_RES));
//				String valueNumber= String.valueOf(resultMap.get(Loan.LOAN_AVAILABLEBALANCE_RES));
				
				if (currencyName.equals(responseCurrencyName)) {
					bCheckCurreny = true;
//					bCheckBalance = true;
					
//					if (Float.parseFloat(valueNumber)>0) {
//						break;
//					}
				}
			}
		}
		resultList=null;
		
		if(bCheckCurreny == false){	//没有匹配到对应币种现汇账户
			if(tag==2){
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"您选择的账户不包含"+currencyName+"现汇子账户");
				return;
			}else{
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						"您选择的账户不包含"+currencyName);
				return;
			}

		}
//		else if(bCheckBalance == false){
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getResources().getString(
//							R.string.loan_choise_acc_status_balance));
//		}
		
		
		
		
		
	}


	/**
	 * @Title: nextExcuse
	 * @Description: 执行下一步
	 * @param
	 * @return void
	 */
	private void nextExcuse() {
//		if (tag == 1) {
//			// 人民币
//			if(rmbSelection<0){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						getResources().getString(
//								R.string.loan_change_choise_acc));
//				return;
//			}
//			if(bCheckCurreny == false){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						"您选择的账户不包含"+currencyName);
//				return;
//			} 
//			else if(bCheckBalance == false){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						getResources().getString(
//								R.string.loan_choise_acc_status_balance));
//				return;
//			}
		
//				gotoNextActivity();
			
//		} else if (tag == 2 ) {
//			//外币
//			if(wbSelection<0){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						getResources().getString(
//								R.string.loan_change_choise_acc));
//				return;
//			}
//			if(bCheckCurreny == false){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						"您选择的账户不包含"+currencyName+"现汇子账户");
//				return;
//			} 
//			else if(bCheckBalance == false){
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						getResources().getString(
//								R.string.loan_choise_acc_status_balance));
//				return;
//			}
//			gotoNextActivity()
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			
//		}
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		/**603添加还款账户接口币种校验*/
		String accountId = (String) wbAccountList.get(wbSelection).get(Loan.LOAN_ACCOUNTID_RES);
		/**603添加还款账户接口币种校验*/
		requestPsnLOANPayerAcountCheck(accountId, currency);
	}
	/**还款账号符合条件*/
	@Override
	public void checkPayerAccountSuccess() {
		// TODO Auto-generated method stub
		super.checkPayerAccountSuccess();
		BaseHttpEngine.dissMissProgressDialog();
		gotoNextActivity();
	}
	
	private void gotoNextActivity() {
		Intent intent = new Intent(
				LoanChangeLoanRepayAccountInputActivity.this,
				LoanChangeLoanRepayReadActivity.class);
		intent.putExtra(Loan.LOANACC_LOAN_TYPE_RES, loan_type);
		String loanNumber = String.valueOf(loanChangeRepayAccmap
				.get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES));
		intent.putExtra("loanNumber", loanNumber);
		String number = null;
		if (tag == 1) {
			number=(String)	rmbAccountList.get(rmbSelection).get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
//			number = accList.get(rmbSelection-1);
			intent.putExtra(ConstantGloble.LOAN_SELECTPOS, rmbSelection);
			BaseDroidApp.getInstanse().getBizDataMap()
			.put(ConstantGloble.LOAN_TOACCOUNTLIST, rmbAccountList);
		} else if (tag == 2) {
			number=(String)	wbAccountList.get(wbSelection).get(Loan.LOANACC_LOAN_ACCOUNTNUMBER_RES);
//			number = accList.get(wbSelection);
			intent.putExtra(ConstantGloble.LOAN_SELECTPOS, wbSelection);
			BaseDroidApp.getInstanse().getBizDataMap()
			.put(ConstantGloble.LOAN_TOACCOUNTLIST, wbAccountList);
		}
		intent.putExtra(Acc.ACC_ACCOUNTNUMBER_RES, number);
		intent.putExtra(Loan.LOANACC_CURRENCYCODE_RES, currency);
		startActivity(intent);
	}

}
