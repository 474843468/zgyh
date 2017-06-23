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
 * 解绑确认页面
 * @author panwe
 *
 */
public class DisengageBindConfirmActivity extends BociBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.bocinvt_disengagebind_titile);
		addView(R.layout.bocinvt_ofacct_disengagebind);
		setUpViews();
	}
	
	@SuppressWarnings("unchecked")
	private void setUpViews(){
		Map<String, Object> acctOFmap = BociDataCenter.getInstance().getAcctOFmap();
		String mainAcct = (String) ((Map<String, Object>) acctOFmap.get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNTNUMBER);
		if (StringUtil.isNullOrEmpty(getOFBankAccountInfo())) {
			((TextView) findViewById(R.id.bank_acct)).setText(ConstantGloble.FINC_COMBINQURY_NONE);
		}else{
			String bankAcct = (String) getOFBankAccountInfo().get(Comm.ACCOUNTNUMBER);
			((TextView) findViewById(R.id.bank_acct)).setText(StringUtil.getForSixForString(bankAcct));
		}
		((TextView) findViewById(R.id.main_acct)).setText(StringUtil.getForSixForString(mainAcct));
		((TextView) findViewById(R.id.remit_title_tv)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.remit_title_tv)).setText(getString(R.string.bocinvt_disbindconfirm_title));
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
		requestOFADisengageBind(tokenId);
	}
	
	/**
	 * 请求解绑网上专属理财账户
	 * @param tokenId
	 */
	@SuppressWarnings("unchecked")
	private void requestOFADisengageBind(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.OFADISENGAGEBIND);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Comm.TOKEN_REQ, tokenId);
		param.put(BocInvt.FINANCIALACCOUNTID, ((Map<String, Object>)BociDataCenter.
				getInstance().getAcctOFmap().get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNT_ID));
//		param.put(BocInvt.BANKACCOUNTNUMBER, (getOFBankAccountInfo().get(Comm.ACCOUNTNUMBER)));
		param.put("accountKey", (getOFBankAccountInfo().get("accountKey")));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "oFADisengageBindCallBack");
	}
	
	public void oFADisengageBindCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		startActivityForResult(new Intent(this, DisengageBindResultActivity.class), 1007);
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
