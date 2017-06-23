package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 绑定确定页面
 * @author panwe
 *
 */
public class BindConfirmActivity extends BociBaseActivity{
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.bocinvt_bind_title);
		addView(R.layout.bocinvt_ofacct_bind);
		position = getIntent().getIntExtra("p", -1);
		setUpViews();
	}
	
	@SuppressWarnings("unchecked")
	private void setUpViews(){
		((TextView) findViewById(R.id.main_acct)).setText(StringUtil.getForSixForString((String)((Map<String, Object>)BociDataCenter.
				getInstance().getAcctOFmap().get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNTNUMBER)));
		((TextView) findViewById(R.id.bank_acct)).setText(StringUtil.getForSixForString((String)BociDataCenter.getInstance().
				getAllAcctList().get(position).get(Comm.ACCOUNTNUMBER)));
		((TextView) findViewById(R.id.remit_title_tv)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.remit_title_tv)).setText(getString(R.string.bocinvt_bindconfirm_title));
		((Button) findViewById(R.id.button)).setText(getString(R.string.confirm));
	}
	
	/**
	 * 确定操作
	 * @param v
	 */
	public void buttonOnclick(View v){
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();
	}
	
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
		requestOFABind(tokenId);
	}
	
	/**
	 * 请求绑定网上专属理财账户
	 * @param tokenId
	 */
	@SuppressWarnings("unchecked")
	private void requestOFABind(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.OFAINCREASEBIND);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Comm.TOKEN_REQ, tokenId);
		param.put(BocInvt.BANKACCOUNTID, BociDataCenter.getInstance().
				getAllAcctList().get(position).get(Comm.ACCOUNT_ID));
		param.put(BocInvt.FINANCIALACCOUNTID, ((Map<String, Object>)BociDataCenter.
				getInstance().getAcctOFmap().get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNT_ID/*Comm.ACCOUNTNUMBER*/));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "oFABindCallBack");
	}
	
	public void oFABindCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(this, BindResultActivity.class).putExtra("p", position), 1009);
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
