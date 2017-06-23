package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
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
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 网上专属理财账户关联
 * @author panwe
 *
 */
public class BocinvtAssociateActivity extends BociBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.acc_myaccount_relevance_title);
		addView(R.layout.bocinvt_associate);
		setUpViews();
	}
	
	@SuppressWarnings("unchecked")
	private void setUpViews(){
		Map<String, Object> acctOFmap = BociDataCenter.getInstance().getAcctOFmap();
		String mainAcct = (String) ((Map<String, Object>) acctOFmap.get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNTNUMBER);
		((TextView)findViewById(R.id.acctNo)).setText(StringUtil.getForSixForString(mainAcct));
	}
	
	/**
	 * 确定操作
	 * @param v
	 */
	public void btnConfirmOnclick(View v){
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
		requestAssociate(tokenId);
	}
	
	/**
	 * 请求网上专属账户关联
	 * @param tokenId
	 */
	private void requestAssociate(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVTASSOCIATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Comm.TOKEN_REQ, tokenId);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "associateCallBack");
	}

	public void associateCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this,"关联成功");
		setResult(RESULT_OK);
		finish();
	}

}
