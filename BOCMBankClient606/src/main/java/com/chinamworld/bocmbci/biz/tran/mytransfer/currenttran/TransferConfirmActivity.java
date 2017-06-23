package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 资金转出确认页面
 * 
 * @author panwe
 * 
 */
public class TransferConfirmActivity extends TranBaseActivity {
	private String amount;
	private String currency;
	private String cashRemit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.tran_my_trans);
		addView(R.layout.bocinvt_transfer_confirm);
		Intent intent = getIntent();
		amount = intent.getStringExtra(BocInvt.AMOUNT);
		currency = intent.getStringExtra(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ);
		cashRemit = intent.getStringExtra(BocInvt.BOCINVT_CANCEL_CASHREMIT_RES);
		setUpViews();
		toprightBtn();
		setLeftSelectedPosition("tranManager_1");
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_1");
//	}

	@SuppressWarnings("unchecked")
	private void setUpViews() {
		Map<String, Object> acctOFmap = BociDataCenter.getInstance()
				.getAcctOFmap();
		((TextView) findViewById(R.id.main_acct)).setText(StringUtil
				.getForSixForString((String) ((Map<String, Object>) acctOFmap
						.get(BocInvt.FINANCIALACCOUNT))
						.get(BocInvt.ACCOUNTNUMBER_OF)));
		((TextView) findViewById(R.id.bank_acct)).setText(StringUtil
				.getForSixForString((String) ((Map<String, Object>) acctOFmap
						.get(BocInvt.BANKACCOUNT)).get(Comm.ACCOUNTNUMBER)));
		if (LocalData.rmbCodeList.contains(currency)) {
			// 人民币
			((TextView) findViewById(R.id.currency)).setText(LocalData.Currency
					.get(currency));
		} else {
			((TextView) findViewById(R.id.currency))
					.setText(LocalData.Currency.get(currency)
							+ LocalData.CurrencyCashremit.get(cashRemit));
		}

		// ((TextView)
		// findViewById(R.id.cashremit)).setText(LocalData.CurrencyCashremit.get(cashRemit));
		((TextView) findViewById(R.id.transmoney)).setText(StringUtil
				.parseStringCodePattern(currency, amount, 2));

	}

	/**
	 * 下一步操作
	 * 
	 * @param v
	 */
	public void buttonOnclick(View v) {
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
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestTransfer(tokenId);
	}

	/**
	 * 请求资金转出
	 * 
	 * @param tokenId
	 */
	@SuppressWarnings("unchecked")
	private void requestTransfer(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.OFAFINANCETRANSFER);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Comm.TOKEN_REQ, tokenId);
		param.put(BocInvt.FINANCIALACCOUNTID,
				((Map<String, Object>) BociDataCenter.getInstance()
						.getAcctOFmap().get(BocInvt.FINANCIALACCOUNT))
						.get(Comm.ACCOUNT_ID));
		param.put(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ, currency);
		param.put(BocInvt.BOCINVT_CANCEL_CASHREMIT_RES, cashRemit);
		param.put(BocInvt.AMOUNT, amount);
		param.put(BocInvt.BANKACCOUNTID, ((Map<String, Object>) BociDataCenter
				.getInstance().getAcctOFmap().get(BocInvt.BANKACCOUNT))
				.get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "transferCallBack");
	}

	@SuppressWarnings("unchecked")
	public void transferCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		startActivity(new Intent(this, TransferResultActivity.class)
				.putExtra(BocInvt.AMOUNT, amount)
				.putExtra(BocInvt.BOCINVT_BUYRES_TRANSACTIONID_RES,
						(String) result.get(BocInvt.TRANSID))
				.putExtra(BocInvt.TRANSSTATUS,
						(String) result.get(BocInvt.TRANSSTATUS))
				.putExtra(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ, currency)
				.putExtra(BocInvt.BOCINVT_CANCEL_CASHREMIT_RES, cashRemit));
	}

}
