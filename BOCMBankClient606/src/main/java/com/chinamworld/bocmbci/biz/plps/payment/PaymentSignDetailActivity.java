package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 签约详情
 * @author panwe
 *
 */
public class PaymentSignDetailActivity extends PlpsBaseActivity{
	private int flag = 0;
	private int position;
	private String phoneNumber;
	Map<String, Object> map= new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getIntent().getIntExtra("p", -1);
		map = PlpsDataCenter.getInstance().getSignList().get(position);
		inflateLayout(R.layout.plps_payment_sign_detail);
		if (map.get(Plps.SIGNTYPE).equals("0")) {
			setTitle(R.string.plps_payment_infot);
		}else {
			setTitle(R.string.plps_message_info);
		}
//		setTitle(PlpsDataCenter.payment[0]);
		phoneNumber = getIntent().getStringExtra(Plps.PHONENUMBER);
		setUpViews();
	}
	
	private void setUpViews(){
		
		findViewById(R.id.btn_sign).setOnClickListener(this);
		if (map.get(Plps.SIGNTYPE).equals("0")) {
			findViewById(R.id.btn_change).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_terminate).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_channel).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_sign).setVisibility(View.GONE);
			findViewById(R.id.btn_change).setOnClickListener(this);
			findViewById(R.id.btn_terminate).setOnClickListener(this);
			if(!StringUtil.isNullOrEmpty(PlpsDataCenter.signChannel.get(map.get(Plps.CHANNEL)))){
				((TextView) findViewById(R.id.channel)).setText(PlpsDataCenter.signChannel.get(map.get(Plps.CHANNEL)));
			}
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.CUSTOMERALIAS))){
			((TextView) findViewById(R.id.nickname)).setText((String)map.get(Plps.CUSTOMERALIAS));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.AGENTNAME))){
			((TextView) findViewById(R.id.agent)).setText((String)map.get(Plps.AGENTNAME));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.CUSTNAME))){
			((TextView) findViewById(R.id.customerName)).setText((String)map.get(Plps.CUSTNAME));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.PAYUSERNO))){
			((TextView) findViewById(R.id.payuserno)).setText((String)map.get(Plps.PAYUSERNO));
		}
		String accountType = (String)map.get(Plps.ACCOUNTTYPE);
		String accountTypeText = null;
		if(accountType.equals("0")){
			accountTypeText = "长城电子借记卡";
		}else {
			accountTypeText = "普活一本";
		}
		TextView signAcctType = (TextView)findViewById(R.id.signacct);
		if(!StringUtil.isNullOrEmpty(accountTypeText)&&!StringUtil.isNullOrEmpty(map.get(Plps.ACCOUNTNUMBER))){
			signAcctType.setText(accountTypeText+StringUtil.getForSixForString((String)map.get(Plps.ACCOUNTNUMBER)));
		}
//		((TextView) findViewById(R.id.signacct)).setText(accountTypeText+StringUtil.getForSixForString((String)map.get(Plps.ACCOUNTNUMBER)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,signAcctType);
		if(!StringUtil.isNullOrEmpty(map.get(Plps.REMARKS))){
			((TextView) findViewById(R.id.remarks)).setText((String)map.get(Plps.REMARKS));
		}
		if(!StringUtil.isNullOrEmpty(map.get(Plps.SIGNTYPE))){
			((TextView) findViewById(R.id.statue)).setText(PlpsDataCenter.signType.get(map.get(Plps.SIGNTYPE)));
		}
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.nickname)
				,(TextView) findViewById(R.id.agent),(TextView) findViewById(R.id.payuserno)
				,(TextView) findViewById(R.id.remarks));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_sign:
			flag = 1;
			BaseHttpEngine.showProgressDialog();
			requestAcctList();
			break;
		case R.id.btn_change:
			BaseHttpEngine.showProgressDialog();
			requestAcctList();
			break;
		case R.id.btn_terminate:
			startActivityForResult(new Intent(this, PaymentTerminateConfirmActivity.class)
			.putExtra("p", position),1001);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bankAccListCallBack(Object resultObj) {
//		super.bankAccListCallBack(resultObj);
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(list)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showMessageDialog("无可签约的签约帐号", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
			 return;
		}else {
			BaseHttpEngine.dissMissProgressDialog();
			PlpsDataCenter.getInstance().setAcctList(list);
			if (flag == 1) {
				Map<String, Object> map = PlpsDataCenter.getInstance().getSignList().get(position);
				startActivityForResult(new Intent(this, PaymentSignInputActivity.class)
				.putExtra("flag",1)
				.putExtra(Plps.PHONENUMBER, phoneNumber)
				.putExtra(Plps.AGENTNAME, (String)map.get(Plps.AGENTNAME))
				.putExtra(Plps.SERVICENAME, (String)map.get(Plps.SERVICENAME))
				.putExtra(Plps.PAYUSERNO, (String)map.get(Plps.PAYUSERNO))
				.putExtra(Plps.CUSTNAME, (String)map.get(Plps.CUSTNAME))
				.putExtra(Plps.AGENTCODE, (String)map.get(Plps.AGENTCODE))
				.putExtra(Plps.SUBAGENTCODE, (String)map.get(Plps.SUBAGENTCODE))
				.putExtra(Plps.CSPCODE, (String)map.get(Plps.CSPCODE))
				.putExtra(Plps.SERVICETYPE, (String)map.get(Plps.SERVICETYPE)),1001); return;
			}
			startActivityForResult(new Intent(this, PaymentSignMsgChangeInputActivity.class)
			.putExtra("p", position),1001);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
}
