package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdFushuQueryDetailActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class MySupplymentDetailNewActivity extends CrcdBaseActivity{
	private static final String TAG = "MySupplymentDetailNewActivity";
	private View view = null;
	/** 主卡卡号 */
	TextView mastercrcdNum;
	/** 附属卡卡号 */
	TextView subcrcdNum;
	/** 附属卡交易限额 */
	TextView supply_limit1,supply_limit2;
	/** 附属卡交易短信 */
	TextView subcrcdsms;
	/** lastButton---设置交易限额，nextButton---设置交易短信 **/
	Button lastButton, nextButton;
	
	/** 所有的附属卡卡号----464格式 */
	List<String> list = new ArrayList<String>();
	/** 用户选择的附属卡考号----464格式化 */
//	protected static String supplyCardNumber;

	protected int position;
	/** 用户选择的applicationID */
//	protected static String applicationId;
	/** 信用卡账号 */
	private String accountNumber = null;
	private String subaccountNumber = null;
	private String accountId = null;
	private String accounttype = null;
	private int tag = -1;
	List<Map<String, Object>> supplyList = null;
	private String accountType = null;
	private String smeSendFlag = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_fushu_detail));
		
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		subaccountNumber= getIntent().getStringExtra(Crcd.CRCD_SUPPLYCARD_RES);
		accounttype= getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		psnCrcdCurrencyQuery();
		
		
//		init();
	}

	/** 初始化界面 */
	private void init() {
		if (view == null) {
			view = addView(R.layout.crcd_supplyment_setup_detail_new);
		}
		
		mastercrcdNum = (TextView) view.findViewById(R.id.mastercrcdNum);
		mastercrcdNum.setText(StringUtil.getForSixForString(accountNumber));
		subcrcdNum = (TextView) view.findViewById(R.id.subcrcdNum);
		subcrcdNum.setText(StringUtil.getForSixForString(subaccountNumber));
		supply_limit1=(TextView) view.findViewById(R.id.supply_limit1);
		supply_limit2=(TextView) view.findViewById(R.id.supply_limit2);
		if(ZHONGYIN.equals(accountType)){
			view.findViewById(R.id.ll_supply).setVisibility(View.GONE);
		}
		if(cardList.size()==1){
			String text1=StringUtil.parseStringCodePattern(cardList.get(0), tradeFlowAmountList.get(0), 2);
			text1=text1+cardList.get(0);
//			String text1=tradeFlowAmountList.get(0)+cardList.get(0);
			supply_limit1.setText(text1);	
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					MySupplymentDetailNewActivity.this, supply_limit1);
		}
		if(cardList.size()==2){
			String text1=StringUtil.parseStringCodePattern(cardList.get(0), tradeFlowAmountList.get(0), 2);
			text1=text1+cardList.get(0);
//			String text1=tradeFlowAmountList.get(0)+cardList.get(0);
			String text2=StringUtil.parseStringCodePattern(cardList.get(1), tradeFlowAmountList.get(1), 2);
			text2=text2+cardList.get(1);
//			String text2=tradeFlowAmountList.get(1)+cardList.get(1);
			supply_limit1.setText(text1);
			supply_limit2.setVisibility(View.VISIBLE);
			supply_limit2.setText(text2);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					MySupplymentDetailNewActivity.this, supply_limit1);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(
					MySupplymentDetailNewActivity.this, supply_limit2);
			
		}
		subcrcdsms=(TextView) view.findViewById(R.id.subcrcdsms);
		subcrcdsms.setText(smeSendFlagmap.get(smeSendFlag));
		lastButton = (Button) view.findViewById(R.id.lastButton);
		nextButton = (Button) view.findViewById(R.id.nextButton);

		
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 设置
				// 设置
				Intent it = new Intent(MySupplymentDetailNewActivity.this, MyMasterAndSupplSetActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);		
				it.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subaccountNumber);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, accounttype);
				// startActivity(it);
				startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//交易历史查询
				
		
				Intent it = new Intent(MySupplymentDetailNewActivity.this, CrcdFushuQueryDetailActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
				it.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subaccountNumber);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				// startActivity(it);
				startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			}
		});
	}

	/** 查询币种 */
	public void psnCrcdCurrencyQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCURRENCYQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTNUMBER_RES, subaccountNumber);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdCurrencyQueryCallBack");
	}

	String currency1 = "";
	String currency2 = "";
	 String strCurrency1 = "";
	 String strCurrency2 = "";
	/** 币种1 */
	Map<String, Object> currencyMap1;
	/** 币种1 */
	Map<String, Object> currencyMap2;
	/** 币种代码名称 */
	public static List<String> cardList = new ArrayList<String>();
	/** 币种代码 */
	public static List<String> currencyList = new ArrayList<String>();
	public  List<String> tradeFlowAmountList = new ArrayList<String>();

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
			PsnCrcdQueryAppertainAndMess1(currencyList.get(0));	

		
	}

	

	private void PsnCrcdQueryAppertainAndMess1(String currency) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYAPPERTAINANDMESS);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_CADRNO_RES, subaccountNumber);
		paramsmap.put(Crcd.CRCD_SUBCURRENCY, currency);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdQueryAppertainAndMessCallBack1");
		
	}
	public void PsnCrcdQueryAppertainAndMessCallBack1(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		//{"smeSendFlag":"0","tradeFlowAmount":"22.330"}
		tradeFlowAmountList.add((String)resultMap.get("tradeFlowAmount"));
		smeSendFlag=(String)resultMap.get("smeSendFlag");
		
		if(currencyList.size()==1){
			init();
			return;
		}
		if(currencyList.size()==2){
			PsnCrcdQueryAppertainAndMess2(currencyList.get(1));	
		}
		
		
	}
	

	private void PsnCrcdQueryAppertainAndMess2(String currency) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYAPPERTAINANDMESS);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_CADRNO_RES, subaccountNumber);
		paramsmap.put(Crcd.CRCD_SUBCURRENCY, currency);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdQueryAppertainAndMessCallBack2");
		
	}
	public void PsnCrcdQueryAppertainAndMessCallBack2(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		//{"smeSendFlag":"0","tradeFlowAmount":"22.330"}
		tradeFlowAmountList.add((String)resultMap.get("tradeFlowAmount"));
		smeSendFlag=(String)resultMap.get("smeSendFlag");		
		init();
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(100);
			finish();
			break;

		default:
			break;
		}
	}
	

	public  Map<String, String> smeSendFlagmap = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{
		
			put("0", "发送主卡");
			put("1", "发送附卡");
			put("2", "发送主卡&附卡");
		}
	};
}
