package com.chinamworld.bocmbci.biz.lsforex.bail;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 保证金交易 确认信息页面 */
public class IsForexBailInfoConfirmActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexBailInfoConfirmActivity";
	/**
	 * IsForexBailInfoConfirmActivity的主布局
	 */
	private View rateInfoView = null;
	/** 资金账户 */
	private TextView zjAccTexy = null;
	/** 转账方式 */
	private TextView throwTypeText = null;
	/** 币种 */
	private TextView codeText = null;
	/** 转账金额 */
	private TextView throwMoneyText = null;
	/** 返回按钮 */
	private Button backButton = null;
	/** 确定按钮 */
	private Button sureButton = null;
	/** 币种代码 */
	private String codeCode = null;
	/** 钞汇代码 */
	private String cashRemit = null;
	/** 输入金额 */
	private String inputMoney = null;
	/** 操作方式 代码 */
	private String dealType = null;
	private String token = null;
	/** 交易序号 */
	private String transactionId = null;
	/** 保证金账户余额 */
	private String stockBalance = null;
	/** 资金账户 */
	private String accountNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_throw_title));
		LogGloble.d(TAG, "onCreate");
		init();
		initDate();
		initOnClick();
	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_bail_confirm, null);
		tabcontent.addView(rateInfoView);
		zjAccTexy = (TextView) findViewById(R.id.isForex_throw_confirm_accNumber);
		throwTypeText = (TextView) findViewById(R.id.isForex_throw_confirm_accType);
		codeText = (TextView) findViewById(R.id.isForex_throw_confirm_code);
		throwMoneyText = (TextView) findViewById(R.id.isForex_throw_money);
		backButton = (Button) findViewById(R.id.ib_back);
		sureButton = (Button) findViewById(R.id.sureButton);
	}

	private void initDate() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		accountNumber = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);// 资金账户
		inputMoney = intent.getStringExtra(ConstantGloble.ISFOREX_INPUTMONEY);
		dealType = intent.getStringExtra(ConstantGloble.ISFOREX_FUNDTRANSFERDIR);
		cashRemit = intent.getStringExtra(IsForex.ISFOREX_CASEREMIT_RES);
		codeCode = intent.getStringExtra(IsForex.ISFOREX_CODE1_RES);
		String codeName = intent.getStringExtra(IsForex.ISFOREX_CURRENCYCODE_RES);// 币种名称
		String cash = LocalData.isForexcashRemitMap.get(cashRemit);
		if (StringUtil.isNull(accountNumber)) {
			return;
		}

		String acc = StringUtil.getForSixForString(accountNumber);
		zjAccTexy.setText(acc);
		String type = LocalData.isForexfundTransferDirMap.get(dealType);
		throwTypeText.setText(type);
		if (LocalData.rebList.contains(codeCode)) {
			codeText.setText(codeName);
		} else {
			codeText.setText(codeName + cash);
		}

		String m = StringUtil.parseStringCodePattern(codeCode, inputMoney, 2);
		throwMoneyText.setText(m);

	}

	private void initOnClick() {
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
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
		if (StringUtil.isNull(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPSNGetTokenId(commConversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if (StringUtil.isNull(token)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPsnVFGBailTransfer(codeCode, cashRemit, dealType, inputMoney, token);
	}

	@Override
	public void requestPsnVFGBailTransferCallback(Object resultObj) {
		super.requestPsnVFGBailTransferCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		transactionId = result.get(IsForex.ISFOREX_TRANSACTIONID_REQ);
		stockBalance = result.get(IsForex.ISFOREX_STOCKBALANCE_REQ);
		if (StringUtil.isNull(transactionId)) {
			return;
		}
		gotoSuccessActivity();
	}

	private void gotoSuccessActivity() {
		Intent intent = new Intent(this, IsForexBailInfoSuccessActivity.class);
		intent.putExtra(IsForex.ISFOREX_TRANSACTIONID_REQ, transactionId);
		intent.putExtra(IsForex.ISFOREX_STOCKBALANCE_REQ, stockBalance);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_REQ, accountNumber);// 资金账户
		intent.putExtra(ConstantGloble.ISFOREX_FUNDTRANSFERDIR, dealType);// 操作方式代码
		intent.putExtra(IsForex.ISFOREX_CASEREMIT_RES, cashRemit);// 钞汇代码
		intent.putExtra(IsForex.ISFOREX_CODE1_RES, codeCode);// 币种代码
		intent.putExtra(ConstantGloble.ISFOREX_INPUTMONEY, inputMoney);
		//startActivity(intent);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
