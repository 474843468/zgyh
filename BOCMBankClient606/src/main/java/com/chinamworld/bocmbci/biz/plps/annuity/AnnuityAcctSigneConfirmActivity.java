package com.chinamworld.bocmbci.biz.plps.annuity;

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
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.PlpsUtils;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 签约账户确认页面
 * @author panwe
 *
 */
public class AnnuityAcctSigneConfirmActivity extends PlpsBaseActivity{
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_annuity_signe_confirm);
		setTitle(PlpsDataCenter.annuity[0]);
		mPosition = getIntent().getIntExtra("p", -1);
		setUpView();
	}
	
	@SuppressWarnings("unchecked")
	private void setUpView(){
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		((TextView) findViewById(R.id.name)).setText((String)logInfo.get(Login.CUSTOMER_NAME));
		((TextView) findViewById(R.id.idtype)).setText(LocalData.IDENTITYTYPE.get(logInfo.get(Comm.IDENTITYTYPE)));
		((TextView) findViewById(R.id.idnum)).setText((String)logInfo.get(Comm.IDENTITYNUMBER));
		PlpsUtils.setOnShowAllTextListener(this, (TextView) findViewById(R.id.name),
				(TextView) findViewById(R.id.idtype),(TextView) findViewById(R.id.idnum));
	}
	
	/**
	 * 确认操作
	 * @param v
	 */
	public void btnOkOnclick(View v){
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
		requestAnnuityAcctSign(tokenId);
	}
	
	/**
	 * 请求签约
	 * @param tokenId
	 */
	private void requestAnnuityAcctSign(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODANNUITYACCSIGN);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Plps.TOKEN, tokenId);
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "annuityAcctSignCallBack");
	}
	
	public void annuityAcctSignCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(new Intent(this, AnnuityAcctSigneresultActivity.class)
		.putExtra("p", mPosition));
		finish();
	}

}
