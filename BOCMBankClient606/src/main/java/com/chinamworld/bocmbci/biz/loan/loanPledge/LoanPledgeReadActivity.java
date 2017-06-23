package com.chinamworld.bocmbci.biz.loan.loanPledge;

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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.adapter.LoanPledgeReadAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.LinearLayoutForListView;

/** 存单质押贷款——协议页面 */
public class LoanPledgeReadActivity extends LoanBaseActivity {
	private static final String TAG = "LoanPledgeReadActivity";
	private View readView = null;
	private Button refuseButton = null;
	private Button receiveButton = null;
	private TextView nameText = null;
	private TextView typeText = null;
	private TextView numberText = null;

	private String currencyCode = null;
	private String availableAmount = null;
	private String amount = null;
	private String loanPeriod = null;
	private String loanRate = null;
	/** 收款账户 */
	private String toActNum = null;
	/** 收款账户ID */
	private String toAccountId = null;
	/** 还款账户ID */
	private String payAccountId = null;
	private String toActNumber = null;
	private String payAccountNumber = null;
	private TextView proText = null;
	private LinearLayoutForListView listView = null;
	private String accountNumber = null;
	private List<Map<String, String>> cdNumberList = null;
	private List<Boolean> listFlag = null;
	private List<Map<String, String>> cdList = null;
	private LoanPledgeReadAdapter adapter = null;
    private TextView headText=null;
    /** 浮动比 */
    private String floatingRate = null;
	/** 浮动值 */
    private String floatingValue = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.loan_two_one));
		Button backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.INVISIBLE);
		cdList = new ArrayList<Map<String, String>>();
		accountNumber = getIntent().getStringExtra(Loan.LOAN_ACCOUNTNUMBER_RES);
		listFlag = (List<Boolean>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.LOAN_LISTFLAG);
		cdNumberList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.LOAN_NORMALDATELIST);
		if (StringUtil.isNullOrEmpty(cdNumberList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_title));
			return;
		}
		getCdNumberList();
		if (StringUtil.isNullOrEmpty(cdList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.loan_no_cdnumber_title));
			return;
		}
		floatingRate=LoanPledgeCdnumberInfoActivity.floatingRate;
		floatingValue=LoanPledgeCdnumberInfoActivity.floatingValue;
		initGetDate();
		init();
		initDate();
		replaceDate();
		initOnClick();
	}

	/** 得到用户选择的存单号，重新组装数据 */
	private void getCdNumberList() {
		int len = cdNumberList.size();
		for (int i = 0; i < len; i++) {
			if (listFlag.get(i)) {
				Map<String, String> map = cdNumberList.get(i);
				map.put(Loan.LOAN_ACCOUNTNUMBER_RES, accountNumber);
				cdList.add(map);
			}
		}
	}

	private void initGetDate() {
		currencyCode = getIntent().getStringExtra(Loan.LOAN_CURRENCYCODE_RES);
		availableAmount = getIntent().getStringExtra(Loan.LOAN_AVAILABLEBALANCE_RES);
		amount = getIntent().getStringExtra(Loan.LOAN_AMOUNT_REQ);
		loanPeriod = getIntent().getStringExtra(Loan.LOAN_LOANPERIOD_REQ);
		loanRate = getIntent().getStringExtra(Loan.LOAN_LOANRATE_RES);
		toAccountId = getIntent().getStringExtra(ConstantGloble.LOAN_TOACCOUNTID);
		toActNum = getIntent().getStringExtra(ConstantGloble.LOAN_TOACTNUM1);
		payAccountId = getIntent().getStringExtra(ConstantGloble.LOAN_PAYACCOUNTID);
		toActNumber = getIntent().getStringExtra(ConstantGloble.LOAN_TOACTNUM);
		payAccountNumber = getIntent().getStringExtra(ConstantGloble.LOAN_PAYACCOUNT);
	}

	private void init() {
		readView = LayoutInflater.from(this).inflate(R.layout.loan_choise_read, null);
		tabcontentView.addView(readView);
		nameText = (TextView) findViewById(R.id.loan_choise_input_loanType);
		typeText = (TextView) findViewById(R.id.loan_choise_read_two);
		numberText = (TextView) findViewById(R.id.loan_choise_read_three);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, numberText);
		proText = (TextView) findViewById(R.id.text_pro);
		refuseButton = (Button) findViewById(R.id.lastButton);
		receiveButton = (Button) findViewById(R.id.sureButton);
		listView = (LinearLayoutForListView) findViewById(R.id.loan_listView);
		headText=(TextView) findViewById(R.id.text_head);
	}

	private void initDate() {
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		nameText.setText(loginName);
		String identityType = String.valueOf(returnMap.get(Login.IDENTIFY_TYPE));
		String identityNumber = String.valueOf(returnMap.get(Login.IDENTIFY_NUM));
		String type = null;
		if (LocalData.IDENTITYTYPE.containsKey(identityType)) {
			type = LocalData.IDENTITYTYPE.get(identityType);
		} else {
			type = "-";
		}
		typeText.setText(type);
		numberText.setText(identityNumber);
	}

	private void replaceDate() {
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String loginName = String.valueOf(returnMap.get(Crcd.CRCD_CUSTOMERNAME));
		String headString = getResources().getString(R.string.loan_read_head);
		String protocol = getResources().getString(R.string.loan_dk_protocol);
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

		String toAcc = StringUtil.getForSixForString(toActNumber);
		String payAcc = StringUtil.getForSixForString(payAccountNumber);
		String lperiod = null;
		String period = loanPeriod.substring(0, 1);
		String after = loanPeriod.substring(1, loanPeriod.length());
		if ("0".equals(period)) {
			lperiod = after;
		} else {
			lperiod = loanPeriod;
		}
		headString = headString + "人民币";
		String ends="元。";
		SpannableString sp = new SpannableString(headString + money+ends);
		sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), headString.length(),
				headString.length() + money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		headText.setText(sp);
		protocol = protocol.replace("loanPeriod", lperiod);
		protocol = protocol.replace("name", loginName);
		protocol = protocol.replace("accountNumber", toAcc);
		protocol = protocol.replace("number", payAcc);
		//贷款利率显示为百分比
//		Double loanRates=Double.valueOf(loanRate);PASSWORD
//		Double ll=loanRates*100;
//		String loan=String.valueOf(ll);
       
		float fRate,fValue,floanRate;
		float value;
		if (!StringUtil.isNull(floatingRate)) {
			// 浮动比不为空
			fRate =Float.parseFloat(floatingRate);
			if(fRate < 0){
				protocol = protocol.replace("change", "下");
			}else{
				protocol = protocol.replace("change", "上");
			}
			fRate = Math.abs(fRate);
			protocol = protocol.replace("loanRate", String.valueOf(fRate));
		} else {
			if (!StringUtil.isNull(floatingValue)) {
				// 浮动值不为空
				fValue = Float.parseFloat(floatingValue);
				if(fValue < 0){
					protocol = protocol.replace("change", "下");
				}else{
					protocol = protocol.replace("change", "上");
				}
				floanRate = Float.parseFloat(loanRate);
				fRate = (Math.abs(fValue)/(floanRate-fValue))*100;
				protocol = protocol.replace("loanRate",  StringUtil.parseStringPattern(String.valueOf(fRate), 2));
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getResources().getString(R.string.loan_choise_no_acc_status));
				return;
			}
		}

		protocol = protocol.replace("loanRate", loanRate);
		proText.setText(protocol);
		adapter = new LoanPledgeReadAdapter(this, cdList);
		listView.setAdapter(adapter);
	}

	private void initOnClick() {
		refuseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		receiveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				// 请求安全因子组合id
				requestGetSecurityFactor(ConstantGloble.LOAN_PB);
			}
		});
	}

	// 请求安全因子组合id
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		/**603不支持单手机交易码*/
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
				requestPsnLOANPledgeVerify(BaseDroidApp.getInstanse().getSecurityChoosed());
			}
		}, securityIdList, securityNameList);
//		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 存单质押贷款预交易
//				requestPsnLOANPledgeVerify(BaseDroidApp.getInstanse().getSecurityChoosed());
//			}
//		});
			
	}

	/** 存单质押贷款预交易 */
	public void requestPsnLOANPledgeVerify(String combinId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNLOANPLEDGEVERIFY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Loan.LOAN_COMBINID_REQ, combinId);
		map.put(Loan.LOAN_LOANTYPE_REQ, ConstantGloble.LOAN_TYPE);
		String availableAmounts=StringUtil.splitStringwithCode(currencyCode, availableAmount, 2);
		map.put(Loan.LOAN_AVAILABLEAMOUNT_REQ, availableAmounts);
		map.put(Loan.LOAN_AMOUNT_REQ, amount);
		map.put(Loan.LOAN_LOANPERIOD_REQ, String.valueOf(Integer.parseInt(loanPeriod)));
		map.put(Loan.LOAN_LOANRATE_RES, loanRate);
		map.put(Loan.LOAN_PAYTYPE_REQ, ConstantGloble.LOAN_B);
		map.put(Loan.LOAN_PAYCYCLE_REQ, ConstantGloble.LOAN_P);
		map.put(Loan.LOAN_TOACTNUM_REQ, toActNum);
		map.put(Loan.LOAN_TOACCOUNT_REQ, toAccountId);
		map.put(Loan.LOAN_PAYACCOUNT_REQ, payAccountId);
		map.put(Loan.LOAN_CURRENCYCODE_RES,"001");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLOANPledgeVerifyCallBack");
	}

	/** 存单质押贷款预交易-----回调 */
	public void requestPsnLOANPledgeVerifyCallBack(Object resultObj) {
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
		Intent intent = new Intent(LoanPledgeReadActivity.this, LoanPledgeconfirmActivity.class);
		intent.putExtra(Loan.LOAN_CURRENCYCODE_RES, currencyCode);
		intent.putExtra(Loan.LOAN_AVAILABLEBALANCE_RES, availableAmount);
		intent.putExtra(Loan.LOAN_AMOUNT_REQ, amount);
		intent.putExtra(Loan.LOAN_LOANPERIOD_REQ, loanPeriod);
		intent.putExtra(Loan.LOAN_LOANRATE_RES, loanRate);
		intent.putExtra(ConstantGloble.LOAN_TOACTNUM1, toActNum);
		intent.putExtra(ConstantGloble.LOAN_TOACCOUNTID, toAccountId);
		intent.putExtra(ConstantGloble.LOAN_PAYACCOUNTID, payAccountId);
		intent.putExtra(ConstantGloble.LOAN_TOACTNUM, toActNumber);
		intent.putExtra(ConstantGloble.LOAN_PAYACCOUNT, payAccountNumber);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}
}
