package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 我的外汇双向宝 签约管理    变更确认页面*/
public class IsForexChangeConfirmActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexChangeConfirmActivity";
	private Button backButton = null;
	private Button sureButton = null;
	private View rateInfoView = null;
	private TextView bailAccText = null;
	private TextView tradeAccText = null;
	private TextView jsCodeText = null;
	private TextView zcRateText = null;
	private TextView newTradeAccSpinner = null;
	private TextView nickNameText = null;
	private String tradeAcc = null;
	private String bailAcc = null;
	private String settleCurrency = null;
	private String liquitRate = null;
	private String nickName = null;
	/** 过滤后符合条件的借记卡 */
	private List<Map<String, String>> newTradeAccResultList = null;
	private int selectPosition = -1;
	private String newAcc = null;
	private String newAccountNo = null;
	private String accountId = null;
	private String prefixType = null;
	private TextView tradeAccTypeText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_change_title));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		newTradeAccResultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		if (newTradeAccResultList == null || newTradeAccResultList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.isForex_times_change_noacc));
			return;
		}
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getDate();
		init();
		initOnClick();
	}

	/** 得到签约账户信息 */
	private void getDate() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		tradeAcc = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		bailAcc = intent.getStringExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		settleCurrency = intent.getStringExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		liquitRate = intent.getStringExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);
		selectPosition = intent.getIntExtra(ConstantGloble.FOREX_customerSelectedPosition, -1);
		Map<String, String> map = newTradeAccResultList.get(selectPosition);
		newAccountNo = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		nickName = map.get(IsForex.ISFOREX_NICKNAME_RES1);
		accountId = map.get(IsForex.ISFOREX_ACCOUNTID_REQ);
		if (!StringUtil.isNull(newAccountNo)) {
			newAcc = StringUtil.getForSixForString(newAccountNo);
		}
		prefixType = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_produce_change_confirm, null);
		tabcontent.addView(rateInfoView);
		bailAccText = (TextView) findViewById(R.id.isforex_bailAcc);
		tradeAccText = (TextView) findViewById(R.id.isforex_tradeAcc);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency1);
		zcRateText = (TextView) findViewById(R.id.isForex_manage_liquidationRatio);
		sureButton = (Button) findViewById(R.id.trade_nextButton);
		newTradeAccSpinner = (TextView) findViewById(R.id.new_tradeAcc);
		nickNameText = (TextView) findViewById(R.id.isforex_nickName);
		tradeAccTypeText = (TextView) findViewById(R.id.isforex_acctype);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bailAccText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tradeAccText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, jsCodeText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zcRateText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickNameText);
		String accountNumber = null;
		String marginAccountNo = null;
		if (!StringUtil.isNull(tradeAcc)) {
			accountNumber = StringUtil.getForSixForString(tradeAcc);
		}
		if (!StringUtil.isNull(bailAcc)) {
			marginAccountNo = StringUtil.getForSixForString(bailAcc);
		}
		String jsCode = null;
		if (!StringUtil.isNull(settleCurrency) && LocalData.Currency.containsKey(settleCurrency)) {
			jsCode = LocalData.Currency.get(settleCurrency);
		}
		bailAccText.setText(marginAccountNo);
		tradeAccText.setText(accountNumber);
		jsCodeText.setText(jsCode);
		bailAccText.setText(marginAccountNo);
		String zcRate = null;
		if (StringUtil.isNull(liquitRate)) {
			zcRateText.setText("-");
		} else {
			zcRate = StringUtil.dealNumber(liquitRate);
			zcRateText.setText(zcRate);
		}
		newTradeAccSpinner.setText(newAcc);
		nickNameText.setText(nickName);
		tradeAccTypeText.setText(prefixType);
	}

	private void initOnClick() {
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
		String tocken = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnVFGChangeContract(tradeAcc, accountId, settleCurrency, bailAcc, tocken);
	}

	/**
	 * 变更签约账户
	 * 
	 * @param oldAccountNumber
	 *            :原借记卡卡号
	 * @param accountId
	 *            :变更后借记卡账户标识
	 * @param settleCurrency
	 *            :结算货币
	 * @param marginAccountNo
	 *            :保证金账号
	 * @param token
	 */
	private void requestPsnVFGChangeContract(String oldAccountNumber, String accountId, String settleCurrency,
			String marginAccountNo, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCCTHANGECONTRA_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_OLDACCOUNTNUMBER_RES, oldAccountNumber);
		map.put(IsForex.ISFOREX_ACCOUNTID_REQ, accountId);
		map.put(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		map.put(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, marginAccountNo);
		map.put(IsForex.ISFOREX_TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGBailDetailQueryCallback");
	}

	/** 变更签约账户---回调 */
	public void requestPsnVFGBailDetailQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Object resultDatail = biiResponseBody.getResult();
		Intent intent = new Intent(IsForexChangeConfirmActivity.this, IsForexChangeSuccessActivity.class);
		intent.putExtra(IsForex.ISFOREX_OLDACCOUNTNUMBER_RES, tradeAcc);
		intent.putExtra(IsForex.ISFOREX_NICKNAME_RES1, nickName);
		intent.putExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, bailAcc);
		intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		intent.putExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES, liquitRate);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES, newAccountNo);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTTYPE_REQ1, prefixType);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}
}
