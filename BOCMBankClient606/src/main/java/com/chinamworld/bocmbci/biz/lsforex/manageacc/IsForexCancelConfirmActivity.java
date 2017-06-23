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

/** 双向宝解约确认页面 */
public class IsForexCancelConfirmActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexCancelConfirmActivity";
	private Button backButton = null;
	private Button sureButton = null;
	private View rateInfoView = null;
	private TextView bailAccText = null;
	private TextView tradeAccText = null;
	private TextView nickNameText = null;
	private TextView jsCodeText = null;
	private TextView zcRateText = null;
	private String tradeAcc = null;
	private String nickName = null;
	private String bailAcc = null;
	private String settleCurrency = null;
	private String liquitRate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_cancel_title));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_produce_cancel_confirm, null);
		tabcontent.addView(rateInfoView);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		init();
		getDate();
		initOnClick();
	}

	private void init() {
		bailAccText = (TextView) findViewById(R.id.isforex_bailAcc);
		tradeAccText = (TextView) findViewById(R.id.isforex_tradeAcc);
		nickNameText = (TextView) findViewById(R.id.isforex_tradeAcc_nickname);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency1);
		zcRateText = (TextView) findViewById(R.id.isForex_manage_liquidationRatio);
		sureButton = (Button) findViewById(R.id.trade_nextButton);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bailAccText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tradeAccText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, jsCodeText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zcRateText);
	}

	/** 得到签约账户信息 */
	private void getDate() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		String accountNumber = null;
		String marginAccountNo = null;
		tradeAcc = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		nickName = intent.getStringExtra(IsForex.ISFOREX_NICKNAME_RES1);
		bailAcc = intent.getStringExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		settleCurrency = intent.getStringExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		liquitRate = intent.getStringExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);

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
		if (StringUtil.isNull(nickName)) {
			nickNameText.setText("-");
		} else {
			nickNameText.setText(nickName);
		}
		jsCodeText.setText(jsCode);
		bailAccText.setText(marginAccountNo);
		String zcRate = null;
		if (StringUtil.isNull(liquitRate)) {
			zcRateText.setText("-");
		} else {
			zcRate = StringUtil.dealNumber(liquitRate);
			zcRateText.setText(zcRate);
		}
	}

	private void initOnClick() {
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				BaseDroidApp.getInstanse().showErrorDialog(
						getResources().getString(R.string.isForex_manage_cancel_confirm_titles), R.string.cancle,
						R.string.confirm, new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								switch (v.getId()) {
								case R.id.exit_btn:// 取消

									break;
								case R.id.retry_btn:// 确定
									BaseHttpEngine.showProgressDialog();
									requestCommConversationId();
									break;
								default:
									break;
								}
							}
						});

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
		requestPsnVFGCancelContract(tradeAcc, bailAcc, settleCurrency, token);
	}

	/**
	 * 双向宝解约
	 * 
	 * @param accountNumber
	 *            :借记卡卡号
	 * @param marginAccountNo
	 *            :保证金账号
	 * @param settleCurrency
	 *            :结算货币
	 * @param token
	 */
	private void requestPsnVFGCancelContract(String accountNumber, String marginAccountNo, String settleCurrency,
			String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCANCELCONTRACT_API);
		biiRequestBody.setConversationId(commConversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(IsForex.ISFOREX_ACCOUNTNUMBER_RES, accountNumber);
		map.put(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, marginAccountNo);
		map.put(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		map.put(IsForex.ISFOREX_TOCKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGCancelContractCallback");
	}

	/** 双向宝解约----回调 */
	public void requestPsnVFGCancelContractCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Object result = biiResponseBody.getResult();
		Intent intent = new Intent(IsForexCancelConfirmActivity.this, IsForexCancelSuccessActivity.class);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES, tradeAcc);
		intent.putExtra(IsForex.ISFOREX_NICKNAME_RES1, nickName);
		intent.putExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, bailAcc);
		intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		intent.putExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES, liquitRate);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}
}
