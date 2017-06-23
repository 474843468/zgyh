package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 
 * @author panwe
 *理财账户管理详情页面
 */
public class BocinvtAcctDetailActivity extends BociBaseActivity {
	private String openStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.boci_acctdetail_titlelist);
		addView(R.layout.bocinvt_acct_detail);
		setUpViews();
		setLeftSelectedPosition("bocinvtManager_1");
	}
	
	@SuppressWarnings("unchecked")
	private void setUpViews(){
		Map<String, Object> acctOFmap = BociDataCenter.getInstance().getAcctOFmap();
		openStatus = (String) acctOFmap.get(BocInvt.OPENSTATUS);
		String mainAcct = (String) ((Map<String, Object>) acctOFmap.get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNTNUMBER);
		if (StringUtil.isNullOrEmpty(getOFBankAccountInfo())) {
			((TextView) findViewById(R.id.bankNum)).setText(ConstantGloble.FINC_COMBINQURY_NONE);
		}else{
			String bankAcct = (String) getOFBankAccountInfo().get(Comm.ACCOUNTNUMBER);
			((TextView) findViewById(R.id.bankNum)).setText(StringUtil.getForSixForString(bankAcct));
		}
		((TextView) findViewById(R.id.name)).setText((String)((Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(BocInvt.CUSTOMERNAME));
		((TextView) findViewById(R.id.facctNum)).setText(StringUtil.getForSixForString(mainAcct));
		((TextView) findViewById(R.id.status)).setText(BociDataCenter.openStatus.get(openStatus));
		if (openStatus.equals(BocInvt.OPENSTATUS_S)) {
			((Button)findViewById(R.id.btn_three)).setText("解绑");
		}else if(openStatus.equals(BocInvt.OPENSTATUS_B)){
			((Button)findViewById(R.id.btn_three)).setText("绑定");
		}else if(openStatus.equals(BocInvt.OPENSTATUS_R)){
			((Button)findViewById(R.id.btn_three)).setText("自助关联");
		}
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		LayoutValue.LEWFTMENUINDEX = 3;
//	}
	
	/**
	 * 按钮操作
	 * @param v
	 */
	public void buttomOnclick(View v){
		if (openStatus.equals(BocInvt.OPENSTATUS_S)) {
			startActivityForResult(new Intent(this, DisengageBindConfirmActivity.class), 1006);
		}else if(openStatus.equals(BocInvt.OPENSTATUS_B)){
			BaseHttpEngine.showProgressDialog();
			List<String> acctype = new ArrayList<String>();
			acctype.add("101");
			acctype.add("119");
			acctype.add("188");
			requestBankAcctList(acctype);
		}else if(openStatus.equals(BocInvt.OPENSTATUS_R)){
			startActivityForResult(new Intent(this,
					BocinvtAssociateActivity.class),
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void bankAccListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;
		} 
		BociDataCenter.getInstance().setAllAcctList(list);
		startActivityForResult(new Intent(this, BindAcctListActivity.class), 1008);
	}
}
