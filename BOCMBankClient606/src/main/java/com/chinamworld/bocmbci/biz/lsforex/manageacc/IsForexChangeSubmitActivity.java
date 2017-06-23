package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 账户变更填写页面 */
public class IsForexChangeSubmitActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexChangeSubmitActivity";
	private Button backButton = null;
	private Button sureButton = null;
	private View rateInfoView = null;
	private TextView bailAccText = null;
	private TextView tradeAccText = null;
	private TextView jsCodeText = null;
	private TextView zcRateText = null;
	private Spinner newTradeAccSpinner = null;
	private String tradeAcc = null;
	private String nickName = null;
	private String bailAcc = null;
	private String settleCurrency = null;
	private String liquitRate = null;
	private String prefixType = null;
	/** 过滤后符合条件的借记卡 */
	private List<Map<String, String>> newTradeAccResultList = null;
	private List<String> accResultList = null;
	private int selectPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_change_title));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		newTradeAccResultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		accResultList = new ArrayList<String>();
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
		nickName = intent.getStringExtra(IsForex.ISFOREX_NICKNAME_RES1);
		bailAcc = intent.getStringExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		settleCurrency = intent.getStringExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		liquitRate = intent.getStringExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);
		int len = newTradeAccResultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = newTradeAccResultList.get(i);
			String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
			String accountType = map.get(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
			if (!StringUtil.isNull(accountNumber)) {
				String account = StringUtil.getForSixForString(accountNumber);
				if (!StringUtil.isNull(accountType) && LocalData.AccountType.containsKey(accountType)) {
					prefixType = LocalData.AccountType.get(accountType);
				}
				accResultList.add(prefixType + " " + account);
			}
		}
	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_produce_change_submit, null);
		tabcontent.addView(rateInfoView);
		bailAccText = (TextView) findViewById(R.id.isforex_bailAcc);
		tradeAccText = (TextView) findViewById(R.id.isforex_tradeAcc);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency1);
		zcRateText = (TextView) findViewById(R.id.isForex_manage_liquidationRatio);
		sureButton = (Button) findViewById(R.id.trade_nextButton);
		newTradeAccSpinner = (Spinner) findViewById(R.id.new_tradeAcc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bailAccText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tradeAccText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, jsCodeText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, zcRateText);
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
		if (accResultList == null || accResultList.size() <= 0) {
			return;
		}
		ArrayAdapter<String> accAdapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, accResultList);
		accAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		newTradeAccSpinner.setAdapter(accAdapter);
		newTradeAccSpinner.setSelection(0);
		selectPosition = 0;
	}

	private void initOnClick() {
		newTradeAccSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				selectPosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoActivity();
			}
		});
	}

	/** 跳转到账户变更页面 */
	private void gotoActivity() {
		Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), IsForexChangeConfirmActivity.class);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES, tradeAcc);
		intent.putExtra(IsForex.ISFOREX_NICKNAME_RES1, nickName);
		intent.putExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1, bailAcc);
		intent.putExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1, settleCurrency);
		intent.putExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES, liquitRate);
		intent.putExtra(ConstantGloble.FOREX_customerSelectedPosition, selectPosition);
		intent.putExtra(IsForex.ISFOREX_ACCOUNTTYPE_REQ1, prefixType);
		startActivity(intent);
	}
}
