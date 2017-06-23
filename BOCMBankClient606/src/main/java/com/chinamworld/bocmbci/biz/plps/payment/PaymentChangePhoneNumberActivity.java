package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PaymentChangePhoneNumberActivity extends PlpsBaseActivity{
	//当前手机号
	private String currPhoneNumber;
	private TextView bindPhone;
	private EditText modifyPhone;
	//修改后的手机号
	private String modifyPhoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.plps_modify_phone));
		cityAdress.setVisibility(View.GONE);
		mRightButton.setVisibility(View.VISIBLE);
		inflateLayout(R.layout.plps_payment_phone_change_dialog);
		currPhoneNumber = getIntent().getStringExtra(Plps.PHONENUMBER);
		init();
	}
	private void init(){
		bindPhone = (TextView)findViewById(R.id.bindphone);
		//bindPhone.setText(StringUtil.getThreeFourThreeString(currPhoneNumber));
		bindPhone.setText(StringUtil.isNullChange(currPhoneNumber));
		modifyPhone = (EditText)findViewById(R.id.phone);
	}
	public void btnConfirmOnclick(View v){
		modifyPhoneNumber = modifyPhone.getText().toString().trim();
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean reMobile = new RegexpBean(PaymentChangePhoneNumberActivity.this.getString(R.string.trans_remit_phone_nolabel),modifyPhoneNumber, "shoujiH_1");
		lists.add(reMobile);
		if (RegexpUtils.regexpData(lists)) {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	/***
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
		requestPhoneChange(tokenId);
	}
	/**
	 * 请求修改手机号
	 * @param tokenId
	 */
	private void requestPhoneChange(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.CUSTOMERINFOUPDATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.TOKEN, tokenId);
		param.put(Plps.PHONENUMBER, modifyPhoneNumber);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "phoneChangeCallBack");
	}
	public void phoneChangeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		String result = (String) this.getHttpTools().getResponseResult(resultObj);
		if(StringUtil.isNull(result)){
			startActivityForResult(new Intent(PaymentChangePhoneNumberActivity.this, PaymentPhoneSuccessActivity.class)
			.putExtra(Plps.PHONENUMBER, modifyPhoneNumber), 1001);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
}
