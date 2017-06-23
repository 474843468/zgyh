package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的信用卡 临时额度调整 预交易
 * 
 * @author sunh
 * 
 */
public class CardApplyTmpLimitPreActivity extends CrcdBaseActivity{
	private View view;
	
	/** 账户ID accountId */
	private String accountId=null;
	/** 信用卡卡号 */
	private String accountNumber=null;
	/** 最大可申请额度*/
	private String maxLimit=null;
	private LinearLayout lin1,lin2,lin3,lin4;
	private TextView card_accountNumber,crcd_text1,	crcd_text2;
	private EditText crcd_edit1,crcd_edit2;
	private Button sureButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.card_messageandset_tittle));
		view = addView(R.layout.crcd_applytmplimit_layout);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		// 请求conversationId
		BaseHttpEngine.showProgressDialog();
		requestCommConversationId();

		
	}

	
	/**
	 * 请求conversationId 回调
	 * 
	 * @param resultObj 返回结果
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
//		requestPsnCrcdQueryMaxLimit();
		
		init();
	}
	
	private void requestPsnCrcdQueryMaxLimit() {
		// 信用卡最大可申请额度查询
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYMAXLIMIT);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);//账户ID
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdQueryMaxLimitCallBack");
	}
	
	public void PsnCrcdQueryMaxLimitCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
//		maxLimit=(String)returnList.get(Crcd.CRCD_MAXLIMIT_RES);
		
		init();
		
	}
	private void init() {
		card_accountNumber=(TextView)view.findViewById(R.id.card_accountNumber);
		card_accountNumber.setText(StringUtil.getForSixForString(accountNumber));
		crcd_text1=(TextView)view.findViewById(R.id.crcd_text1);
		crcd_text2=(TextView)view.findViewById(R.id.crcd_text2);
		lin1=(LinearLayout)view.findViewById(R.id.lin1);
		lin2=(LinearLayout)view.findViewById(R.id.lin2);
		lin3=(LinearLayout)view.findViewById(R.id.lin3);
		lin4=(LinearLayout)view.findViewById(R.id.lin4);
		crcd_edit1=(EditText)view.findViewById(R.id.crcd_edit1);
		crcd_edit2=(EditText)view.findViewById(R.id.crcd_edit2);
		sureButton=(Button)view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				requestGetSecurityFactor(psnSersecurityId);	
			}
			
			
		});
		
		
	}
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//信用卡临时额度申请预交易
				requestPsnCrcdApplyTmpLimitPre();
				
			}
		});
	}


	protected void requestPsnCrcdApplyTmpLimitPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQAPPLYTMPLIMITPRE);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);//账户ID
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());//安全因子组合id
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdApplyTmpLimitPreCallBack");
		
	}
	 Map<String, Object> returnList;

	public void PsnCrcdApplyTmpLimitPreCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(Crcd.CRCD_PSNCRCDQAPPLYTMPLIMITPRE, returnList);
		Intent it = new Intent(CardApplyTmpLimitPreActivity.this, CardApplyTmpLimitConfirmActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		startActivity(it);
	}

}
