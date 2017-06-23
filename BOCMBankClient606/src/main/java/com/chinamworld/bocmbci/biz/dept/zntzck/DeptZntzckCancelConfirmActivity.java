package com.chinamworld.bocmbci.biz.dept.zntzck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;

/** 智能通知存款---解约确认页面 */
public class DeptZntzckCancelConfirmActivity extends DeptBaseActivity {
	private static final String TAG = "DeptZntzckCancelConfirmActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View queryView = null;
	private TextView signAccText = null;
	private String accountId = null;
	private String accountNumber = null;
	private Button sureButton = null;
	private String commConversationId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		ibRight.setVisibility(View.VISIBLE);
		setTitle(getResources().getString(R.string.dept_zntzck_query_cancel));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_cancel_confirm, null);
		tabcontent.addView(queryView);
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		accountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		init();
	}

	private void init() {
		signAccText = (TextView) findViewById(R.id.dept_zntzck_query_signAcc);
		sureButton = (Button) findViewById(R.id.sureButton);
		signAccText.setText(accountNumber);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestPSNGetTokenId(commConversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requsetPsnIntelligentNoticeDepositCancel(accountId, token);
	}

	/** 智能通解约 */
	public void requsetPsnIntelligentNoticeDepositCancel(String accountId, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.DEPT_PSNINTELLIGENTNOTICEDEPOSITCANCEL_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.DEPT_ACC_ACCOUNTID_REQ, accountId);
		paramsmap.put(Dept.DEPT_CURRENCY_REQ, ConstantGloble.PRMS_CURRENCYCODE_RMB);
		paramsmap.put(Dept.DEPT_CASHREMIT_RES, ConstantGloble.ZERO);
		paramsmap.put(Dept.DEPT_TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnIntelligentNoticeDepositCancelCallback");

	}

	public void requsetPsnIntelligentNoticeDepositCancelCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		Intent intent = new Intent(DeptZntzckCancelConfirmActivity.this, DeptZntzckCancelSuccessActivity.class);
		intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
		startActivity(intent);
	}
}
