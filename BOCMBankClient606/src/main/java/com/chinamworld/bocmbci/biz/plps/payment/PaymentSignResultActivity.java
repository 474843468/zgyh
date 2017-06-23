package com.chinamworld.bocmbci.biz.plps.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 签约成功页
 * @author panwe
 *
 */
public class PaymentSignResultActivity extends PlpsBaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_sign_result);
		setTitle(PlpsDataCenter.payment[1]);
		mLeftButton.setVisibility(View.GONE);
		setUpViews();
	}
	
	private void setUpViews(){
		Intent it = getIntent();
		((TextView) findViewById(R.id.servicename)).setText(it.getStringExtra(Plps.SERVICENAME));
		((TextView) findViewById(R.id.agent)).setText(it.getStringExtra(Plps.AGENTNAME));
		if(!StringUtil.isNullOrEmpty(it.getStringExtra(Plps.CUSTOMERALIAS))){
			((TextView) findViewById(R.id.nickname)).setText(it.getStringExtra(Plps.CUSTOMERALIAS));
		}
//		((TextView) findViewById(R.id.customerName)).setText(it.getStringExtra(Plps.CUSTNAME));
		TextView customerName = (TextView)findViewById(R.id.customerName);
		customerName.setText(it.getStringExtra(Plps.CUSTNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, customerName);
		((TextView) findViewById(R.id.phone)).setText(it.getStringExtra(Plps.PHONENUMBER));
		((TextView) findViewById(R.id.payuserno)).setText(it.getStringExtra(Plps.PAYUSERNO));
		String accountTypeName = it.getStringExtra(Comm.ACCOUNT_TYPE);
		String nickName = it.getStringExtra(Comm.NICKNAME);
		String acctNum = it.getStringExtra(Comm.ACCOUNTNUMBER);
		String accountIbkNum = it.getStringExtra(Comm.ACCOUNTIBKNUM);
		TextView signAcct = (TextView)findViewById(R.id.signacct);
		TextView signAcctext = (TextView)findViewById(R.id.signacctext);
		signAcct.setText(accountTypeName);
		signAcctext.setText(StringUtil.getForSixForString(acctNum)+" "+nickName+" "+accountIbkNum);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, signAcct);
//		((TextView) findViewById(R.id.signacct)).setText(StringUtil.getForSixForString(it.getStringExtra(Comm.ACCOUNTNUMBER)));
		if(!StringUtil.isNullOrEmpty(it.getStringExtra(Plps.REMARKS))){
			((TextView) findViewById(R.id.remarks)).setText(it.getStringExtra(Plps.REMARKS));
		}
		((Button)findViewById(R.id.finish)).setText(getString(R.string.finish));
//		((TextView) findViewById(R.id.remit_title_tv)).setText(getString(R.string.plps_sign_resulttip));
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.servicename)
				,(TextView) findViewById(R.id.agent),(TextView) findViewById(R.id.remarks));
	}
	
	/**
	 * 完成
	 * @param v
	 */
	public void btnNextOnclick(View v){
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
