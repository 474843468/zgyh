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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 智能通知存款--签约-确认页面 */
public class DeptZntzckSignConfirmActivity extends DeptBaseActivity {
	private static final String TAG = "DeptZntzckSignConfirmActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View queryView = null;
	private TextView signAccText = null;
	private TextView signTypeText = null;
	private TextView signNickText = null;
	private Button sureButton = null;
	private Map<String, String> map = null;
	private String commConversationId = null;
	private String accountId = null;
	private String accountnumber = null;
	private String tag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_zntzck_sign));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.VISIBLE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_sign_confirm, null);
		tabcontent.addView(queryView);
		map = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_ACCINFO);
		if (StringUtil.isNullOrEmpty(map)) {
			return;
		}
		tag = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_GOTO_TAG);
		if (!StringUtil.isNull(tag) && "1".equals(tag)) {
			// 查询页面跳转到此页面
			accountnumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		}
		init();
	}

	private void init() {
		signAccText = (TextView) findViewById(R.id.dept_zntzck_query_signAcc);
		signTypeText = (TextView) findViewById(R.id.forex_customer_accType);
		signNickText = (TextView) findViewById(R.id.forex_customer_accAlias);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, signNickText);
		sureButton = (Button) findViewById(R.id.sureButton);
		String accountType = map.get(Comm.ACCOUNT_TYPE);
		String accountNumber = map.get(Comm.ACCOUNTNUMBER);
		String nickName = map.get(Comm.NICKNAME);
		accountId = map.get(Comm.ACCOUNT_ID);

		String type = null;
		if (StringUtil.isNull(accountType)) {
			type = "-";
		} else {
			if (LocalData.AccountType.containsKey(accountType)) {
				type = LocalData.AccountType.get(accountType);
			}
		}
		String accountNumbers = null;
		if (StringUtil.isNull(accountNumber)) {
			accountNumbers = "-";
		} else {
			accountNumbers = StringUtil.getForSixForString(accountNumber);
		}
		if (!StringUtil.isNull(tag) && "1".equals(tag)) {
			// 查询页面跳转到此页面
			signAccText.setText(accountnumber);
		} else {
			signAccText.setText(accountNumbers);
		}
		signTypeText.setText(type);
		signNickText.setText(nickName);
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
		requsetPsnIntelligentNoticeDepositSign(accountId, token);
	}

	/** 智能通签约 */
	public void requsetPsnIntelligentNoticeDepositSign(String accountId, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.DEPT_PSNINTELLIGENTNOTICEDEPOSITSIGN_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.DEPT_ACC_ACCOUNTID_REQ, accountId);
		paramsmap.put(Dept.DEPT_CURRENCY_REQ, ConstantGloble.PRMS_CURRENCYCODE_RMB);
		paramsmap.put(Dept.DEPT_CASHREMIT_RES, ConstantGloble.ZERO);
		paramsmap.put(Dept.DEPT_TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnIntelligentNoticeDepositSignCallback");

	}

	public void requsetPsnIntelligentNoticeDepositSignCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		Intent intent = new Intent(DeptZntzckSignConfirmActivity.this, DeptZntzckSignSuccessActivity.class);
		if (!StringUtil.isNull(tag) && "1".equals(tag)) {
			// 查询页面跳转到此页面
			intent.putExtra(Comm.ACCOUNTNUMBER, accountnumber);
		}
		startActivity(intent);
	}
}
