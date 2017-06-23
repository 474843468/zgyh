package com.chinamworld.bocmbci.biz.finc.trade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class FincTradeBuyConfirmContractActivity extends FincBaseActivity {
	/**基金交易账户*/
	private  TextView finc_fundacc;
	/**资金帐户名*/
	private  TextView finc_card_type;
	/**资金帐户*/
	private TextView finc_card_number;
	/**资金帐户别名*/
	private TextView finc_card_alias;
	/**产品代码*/
	private TextView finc_productcode_textview;
	/**产品名称*/
	private TextView finc_productname_textview;
	/**交易币种*/
	private TextView finc_currency_textview;
	/**电子合同*/
	private TextView finc_electroncontract_textview;
	/**勾选框*/
	private CheckBox cb_signelectroncontract;
	/**确定按钮*/
	private  Button finc_confirm;
	/**基金code*/
	private   String fundCode;
	/**ConversationId*/
	private   String conversationId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initAddClick();
		conversationId=(String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
	}

	private void initAddClick() {
		finc_confirm.setOnClickListener(onClickListener);
		cb_signelectroncontract.setOnCheckedChangeListener(checkedListener);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.finc_succeed:
				if(!cb_signelectroncontract.isChecked()){
					BaseDroidApp.getInstanse().showMessageDialog("请仔细阅读电子合同书", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
					return;
				}
				
				/**tokenid*/
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestPSNGetTokenId(conversationId);
				break;


			default:
				break;
			}

		}
	};

	OnCheckedChangeListener  checkedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub

		}
	};

	private void initData() {
		if(FincControl.isAttentionFlag){
			fincControl.fundDetails = fincControl.fincFundDetails;  
		}

		String fincFundacc = null;
		if(!StringUtil.isNullOrEmpty(fincControl.accDetailsMap)){

			if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_INVESTACCOUNT_RES))){
				fincFundacc = fincControl.accDetailsMap.get(Finc.FINC_INVESTACCOUNT_RES);
			}else{
				fincFundacc = "-";
			}
		}
		finc_fundacc.setText(fincFundacc);

		String accNumStr = null;
		if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_ACCOUNT_RES))){
			accNumStr = fincControl.accDetailsMap.get(Finc.FINC_ACCOUNT_RES);
			accNumStr = StringUtil.getForSixForString(accNumStr);
		}else{
			accNumStr = "-";
		}
		finc_card_number.setText(accNumStr);

	 fundCode =  (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDCODE);
		if(StringUtil.isNull(fundCode)){
			fundCode = "-";
		}
		finc_productcode_textview.setText(fundCode);


		String fincFundAccontNameText = null;
		if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE))){
			fincFundAccontNameText = fincControl.accDetailsMap.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE);
			fincFundAccontNameText = LocalData.AccountType.get(fincFundAccontNameText.trim());
		}else{
			fincFundAccontNameText = "-";
		}
		finc_card_type.setText(fincFundAccontNameText);


		String fincFundAccontNickNameText = null;
		if(!StringUtil.isNull(fincControl.accDetailsMap.get(Finc.FINC_ACCOUNTNICKNAME_RES))){
			fincFundAccontNickNameText = fincControl.accDetailsMap.get(Finc.FINC_ACCOUNTNICKNAME_RES);
		}else{
			fincFundAccontNickNameText = "-";
		}
		finc_card_alias.setText(fincFundAccontNickNameText);

		String fundNameStr = (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDNAME);
		if(StringUtil.isNull(fundNameStr)){
			fundNameStr = "-";
		}
		finc_productname_textview.setText(fundNameStr);
		String currencyStr=null;
		if(!StringUtil.isNull((String) fincControl.fundDetails
				.get(Finc.FINC_CURRENCY))){
			 currencyStr = (String) fincControl.fundDetails
					.get(Finc.FINC_CURRENCY);
		}else {
			currencyStr="-";
		}
		String cashFlagCode=null;
		if(!StringUtil.isNull((String) fincControl.fundDetails
				.get(Finc.FINC_CASHFLAG))){

		cashFlagCode = (String) fincControl.fundDetails
				.get(Finc.FINC_CASHFLAG);
		}else{
			cashFlagCode="-";
		}

		finc_currency_textview.setText(FincControl.fincCurrencyAndCashFlag(
				currencyStr, cashFlagCode));


	}

	private void init() {
		View childview = mainInflater.inflate(R.layout.finc_signelectroncontract_countersign,
				null);
		tabcontent.addView(childview);
		setTitle("签署电子合同");

		finc_fundacc = (TextView) findViewById(R.id.finc_fundacc);
		finc_card_type = (TextView) findViewById(R.id.finc_card_type);
		finc_card_number = (TextView) findViewById(R.id.finc_card_number);
		finc_card_alias = (TextView) findViewById(R.id.finc_card_alias);
		finc_productcode_textview = (TextView) findViewById(R.id.finc_productcode_textview);
		finc_productname_textview = (TextView) findViewById(R.id.finc_productname_textview);
		finc_currency_textview = (TextView) findViewById(R.id.finc_currency_textview);
		cb_signelectroncontract = (CheckBox) findViewById(R.id.cb_signelectroncontract);
		finc_confirm = (Button) findViewById(R.id.finc_succeed);
		finc_electroncontract_textview = (TextView) findViewById(R.id.finc_electroncontract_textview);
		finc_electroncontract_textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(FincTradeBuyConfirmContractActivity.this, FincTradeProtocolActivity.class);
			startActivity(intent);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
			case RESULT_OK:
//				setResult(RESULT_OK);
				this.finish();
				break;

			default:
				break;
		}
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPsnFundSignElectronicContract(fundCode,tokenId);
		// regisAcc(accId, addressTypeStr, tokenId, otpStr,smcStr);
	}
	
	/**
	 * 4.4 004 PsnFundSignElectronicContract签约电子合同
	 *  获取签署日期
	 * @param  serviceId 服务码
	 */
	public void requestPsnFundSignElectronicContract(String fincCode,String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDSIGNELECTRONICCONTRACT);
		Map<String, String> map = new HashMap<String, String>();
		biiRequestBody.setConversationId(conversationId);
		 map.put(Finc.FINC_FINCCODE, fincCode);
		 map.put(Finc.TOKEN, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "requestPsnFundSignElectronicContractCallback");
	}

	@Override
	public void requestPsnFundSignElectronicContractCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> Result =(Map<String, Object>) biiResponseBody.getResult();
        String signDate=(String) Result.get(Finc.FINC_SIGNDATE);
        
//        Intent intent = new Intent(FincTradeBuyConfirmContractActivity.this,
//				FincTradeBuyConfirmContractSucceedActivity.class);
		Intent intent1 = getIntent();
		intent1.putExtra(Finc.FINC_SIGNDATE, signDate);
		intent1.setClass(this, FincTradeBuyConfirmContractSucceedActivity.class);
		startActivity(intent1);
		this.finish();
	}
	
	
	
	
	
	
}