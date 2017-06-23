package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 解约确认页
 * @author panwe
 *
 */
public class PaymentTerminateConfirmActivity extends PlpsBaseActivity{
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_payment_terminate);
//		setTitle(PlpsDataCenter.payment[0]);
		setTitle(R.string.plps_payment_termination);
		position = getIntent().getIntExtra("p", -1);
		setUpViews();
	}
	
	private void setUpViews(){
		Map<String, Object> map = PlpsDataCenter.getInstance().getSignList().get(position);
		if(!StringUtil.isNullOrEmpty(map.get(Plps.SERVICENAME))){
			((TextView) findViewById(R.id.serviceName)).setText((String)map.get(Plps.SERVICENAME));
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
			accountTypeText = "借记卡";
		}else {
			accountTypeText = "普活一本";
		}
		TextView signAcctType = (TextView)findViewById(R.id.signacct);
		signAcctType.setText(accountTypeText+StringUtil.getForSixForString((String)map.get(Plps.ACCOUNTNUMBER)));
//		((TextView) findViewById(R.id.signacct)).setText(StringUtil.getForSixForString((String)map.get(Plps.ACCOUNTNUMBER)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,signAcctType);
		((TextView) findViewById(R.id.remarks)).setText((String)map.get(Plps.REMARKS));
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.serviceName)
				,(TextView) findViewById(R.id.nickname),(TextView) findViewById(R.id.agent)
				,(TextView) findViewById(R.id.customerName),(TextView) findViewById(R.id.payuserno)
				,(TextView) findViewById(R.id.remarks));
	}
	
	/**
	 * 确定操作
	 * @param v
	 */
	public void btnConfirmOnclick(View v){
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	
	/**
	 * 取消操作
	 * @param v
	 */
	/*public void btnCancleOnclicl(View v){
		finish();
	}*/
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	
	/**
	 * 请求token
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		requestTerminate(tokenId);
	}
	
	/**
	 * 请求解约
	 * @param tokenId
	 */
	private void requestTerminate(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.PSNPROXYTERMINATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = PlpsDataCenter.getInstance().getSignList().get(position);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.TOKEN, tokenId);
		param.put(Plps.AGENTCODE, map.get(Plps.AGENTCODE));
		param.put(Plps.AGENTNAME, map.get(Plps.AGENTNAME));
		param.put(Plps.SUBAGENTCODE, map.get(Plps.SUBAGENTCODE));
		param.put(Plps.CSPCODE, map.get(Plps.CSPCODE));
		param.put(Plps.SERVICETYPE, map.get(Plps.SERVICETYPE));
		param.put(Plps.SERVICENAME, map.get(Plps.SERVICENAME));
		param.put(Plps.PAYUSERNO, map.get(Plps.PAYUSERNO));
		param.put(Plps.CUSTNAME, map.get(Plps.CUSTNAME));
		param.put(Plps.REMARKS, map.get(Plps.REMARKS));
		param.put(Plps.PHONENUMBER, map.get(Plps.PHONENUMBER));
		param.put(Plps.CUSTOMERALIAS, map.get(Plps.CUSTOMERALIAS));
		param.put(Plps.ACCOUNTNUMBER, map.get(Plps.ACCOUNTNUMBER));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "terminateCallBack");
	}
	
	public void terminateCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(this, PaymentTerminateResultActivity.class)
		.putExtra("p", position),1001);
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
