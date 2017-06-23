package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.SpinnerAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.BottomButtonUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 
 * 附属卡服务定制详情
 * 
 * @author huangyuchao
 *  
 */
public class MySupplymentDetailActivity extends CrcdBaseActivity {
	private static final String TAG = "MySupplymentDetailActivity";
	private View view = null;
	/** 用户选择的信用卡账号 */
	TextView tv_cardNumber;
	/** lastButton---设置交易限额，nextButton---设置交易短信 **/
	Button lastButton, nextButton;
	/** 附属卡考号下拉框 */
	Spinner forex_rate_currency_buylCode;
	/** 所有的附属卡卡号----464格式 */
	List<String> list = new ArrayList<String>();
	/** 用户选择的附属卡考号----464格式化 */
	protected static String supplyCardNumber;

	protected int position;
	/** 用户选择的applicationID */
	protected static String applicationId;
	/** 信用卡账号 */
	private String accountNumber = null;
	private String accountId = null;
	private int tag = -1;
	List<Map<String, Object>> supplyList = null;
	private String accountType = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_fushu_service_setup));
		if (view == null) {
			view = addView(R.layout.crcd_supplyment_setup_detail);
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountType = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
		tag = getIntent().getIntExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, -1);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();
	}

	/** 初始化界面 */
	private void init() {
		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(accountNumber));

		lastButton = (Button) view.findViewById(R.id.lastButton);
		nextButton = (Button) view.findViewById(R.id.nextButton);

		forex_rate_currency_buylCode = (Spinner) view.findViewById(R.id.forex_rate_currency_buylCode);
		if (ZHONGYIN.equals(accountType)) {
			// 中信信用卡
			lastButton.setVisibility(View.GONE);
			BottomButtonUtils.setSingleLineStyleRed(nextButton);
		}
		if (tag == 4) {
			supplyList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.FOREX_ACTAVI_RESULT_KEY);
		} else {
			supplyList = MySupplymentListActivity.returnList;
		}
		for (int i = 0; i < supplyList.size(); i++) {
			Map<String, Object> map = supplyList.get(i);
			String cardNum = String.valueOf(map.get(Crcd.CRCD_SUBCREDITCARDNUM));
			String passCardNum = StringUtil.getForSixForString(cardNum);
			list.add(passCardNum);
		}
		SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.dept_spinner, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		forex_rate_currency_buylCode.setAdapter(adapter);
		forex_rate_currency_buylCode.setSelection(0);
		forex_rate_currency_buylCode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				supplyCardNumber = (String) forex_rate_currency_buylCode.getSelectedItem();
				position = forex_rate_currency_buylCode.getSelectedItemPosition();
				Map<String, Object> map = supplyList.get(position);
				applicationId = String.valueOf(map.get(Crcd.CRCD_APPLICATIONID));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 查询信用卡币种
				psnCrcdCurrencyQuery();
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置交易短信
				Intent it = new Intent(MySupplymentDetailActivity.this, MyCrcdSetupSmsActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				// startActivity(it);
				startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			}
		});
	}

	//** 查询币种 *//*
	public void psnCrcdCurrencyQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCURRENCYQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdCurrencyQueryCallBack");
	}

	String currency1 = "";
	String currency2 = "";
	static String strCurrency1 = "";
	static String strCurrency2 = "";
	//** 币种1 *//*
	Map<String, Object> currencyMap1;
	//** 币种1 *//*
	Map<String, Object> currencyMap2;
	//** 币种代码名称 *//*
	static List<String> cardList = new ArrayList<String>();
	//** 币种代码 *//*
	static List<String> currencyList = new ArrayList<String>();

	public void psnCrcdCurrencyQueryCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		currencyMap1 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY1);
		currencyMap2 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY2);

		currencyList.clear();
		cardList.clear();
		if (!StringUtil.isNullOrEmpty(currencyMap1)) {
			currency1 = String.valueOf(currencyMap1.get(Crcd.CRCD_CODE));
			strCurrency1 = LocalData.Currency.get(currency1);
			currencyList.add(currency1);
			cardList.add(strCurrency1);
		} else {
			currency1 = null;
			strCurrency1 = null;

		}
		if (!StringUtil.isNullOrEmpty(currencyMap2)) {
			currency2 = String.valueOf(currencyMap2.get(Crcd.CRCD_CODE));
			strCurrency2 = LocalData.Currency.get(currency2);
			currencyList.add(currency2);
			cardList.add(strCurrency2);
		} else {
			currency2 = null;
			strCurrency2 = null;

		}

		// 设置交易限额
		Intent it = new Intent(MySupplymentDetailActivity.this, MyCrcdSetupTransMoneyActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
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
