package com.chinamworld.bocmbci.biz.bocnet.cashacct;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 电子现金账户
 * @author fsm
 *
 */
public class CashAcctActivity extends BocnetBaseActivity{

	private Button button, accDetail;
	private Map<String, Object> cashAccDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.bocnet_cashacc_detail);
		setTitle(R.string.acc_finance_menu_main);
		BaseHttpEngine.showProgressDialog();
		requestAccBocnetQryEcashDetail();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(getLeftBtnVisible() == View.VISIBLE)
		setLeftSelectedPosition("bocnet_2");
	}
	
	private void setupViews(){
		setLeftButtonGone();
		setRightButton(getString(R.string.exit), exitClickListener);
		
		button = (Button)findViewById(R.id.button);
		accDetail = (Button)findViewById(R.id.accDetail);
		accDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				debitDetailOnclick(null);
			}
		});
		initBankingFlagBtn(button);
		button.setOnClickListener(this);
		TextView name = (TextView) findViewById(R.id.crcd_type_value);
		TextView number = (TextView) findViewById(R.id.crcd_account_num);
		TextView nikName = (TextView) findViewById(R.id.crcd_account_nickname);
		
		TextView acctType = (TextView) findViewById(R.id.acct_type);
		TextView acctNikName = (TextView) findViewById(R.id.acc_nickname);
		TextView accStatus = (TextView) findViewById(R.id.acc_status);
		TextView accNum = (TextView) findViewById(R.id.acc_num);
		TextView eCashUpperLimit = (TextView) findViewById(R.id.eCashUpperLimit);//电子现金卡片余额上限
		TextView singleLimit = (TextView) findViewById(R.id.singleLimit);//电子现金单笔交易限额
		TextView supplyBalance = (TextView) findViewById(R.id.supplyBalance);//补登账户余额
	
		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();
		if (!StringUtil.isNullOrEmpty(loginInfo)) {
			name.setText(LocalData.AccountType.get((String)loginInfo.get(Bocnet.ACCOUNTTYPE)));
			number.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			accNum.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			nikName.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
			acctType.setText(LocalData.AccountType.get((String)loginInfo.get(Bocnet.ACCOUNTTYPE)));
			acctNikName.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
		}
		
		if(!StringUtil.isNullOrEmpty(cashAccDetail)){
			accStatus.setText(StringUtil.valueOf1(LocalData.bocnetAccountStatus
					.get(cashAccDetail.get(Bocnet.ACCOUNTSTATUS))));
			String currency = (String) cashAccDetail.get(Bocnet.CURRENCY);
			eCashUpperLimit.setText(StringUtil.parseStringCodePattern(currency,
					(String) cashAccDetail.get(Bocnet.ECASHUPPERLIMIT), 2));
			singleLimit.setText(StringUtil.parseStringCodePattern(currency,
					(String) cashAccDetail.get(Bocnet.SINGLELIMIT), 2));
			supplyBalance.setText(StringUtil.parseStringCodePattern(currency,
					(String) cashAccDetail.get(Bocnet.SUPPLYBALANCE), 2));
		}
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.button:
			eBankingFlag();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 系统时间
	 * @param v
	 */
	public void debitDetailOnclick(View v){
		BaseHttpEngine.showProgressDialog();
		requestSystemTime();
	}

	@Override
	public void systemTimeCallBack(Object resultObj) {
		super.systemTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(new Intent(this, CashTransDetailActivity.class));
	}
	
	/**
	 * 查询电子现金账户详情
	 */
	private void requestAccBocnetQryEcashDetail() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYECASHDETAIL);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Bocnet.ACCOUNTSEQ, BocnetDataCenter.getInstance().getAccountSeq());
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "accBocnetQryEcashDetailCallBack");
	}
	
	public void accBocnetQryEcashDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		cashAccDetail = HttpTools.getResponseResult(resultObj);
		setupViews();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
