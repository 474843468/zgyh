package com.chinamworld.bocmbci.biz.finc.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class FincTradeBuyConfirmContractSucceedActivity extends
		FincBaseActivity {
	/** 基金交易账户 */
	private TextView finc_fundacc;
	/** 资金帐户名 */
	private TextView finc_card_type;
	/** 资金帐户 */
	private TextView finc_card_number;
	/** 资金帐户别名 */
	private TextView finc_card_alias;
	/** 产品代码 */
	private TextView finc_productcode_textview;
	/** 产品名称 */
	private TextView finc_productname_textview;
	/** 交易币种 */
	private TextView finc_currency_textview;
	/** 勾选框 */
	private CheckBox cb_signelectroncontract;
	/** 完成按钮 */
	private Button finc_succeed;
	/** 签署日期 */
	private TextView finc_sign_date_textview;
	 String signDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initAddClick();

		  back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);

	}

	private void initAddClick() {
		finc_succeed.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.finc_succeed:

//				Intent intent = new Intent();
				Intent intent = getIntent();
				intent.setClass(
						FincTradeBuyConfirmContractSucceedActivity.this,
						FincTradeBuyConfirmActivity.class);
//				intent.putExtra(Finc.FINC_SIGNDATE, signDate);
				startActivity(intent);
//				setResult(RESULT_OK);
//				FincTradeBuyConfirmContractSucceedActivity.this.finish();

				break;
			default:
				break;
			}

		}
	};

	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		signDate= (String) extras.get(Finc.FINC_SIGNDATE);
		if (FincControl.isAttentionFlag) {
			fincControl.fundDetails = fincControl.fincFundDetails;
		}

		String fincFundacc = null;
		if (!StringUtil.isNullOrEmpty(fincControl.accDetailsMap)) {

			if (!StringUtil.isNull(fincControl.accDetailsMap
					.get(Finc.FINC_INVESTACCOUNT_RES))) {
				fincFundacc = fincControl.accDetailsMap
						.get(Finc.FINC_INVESTACCOUNT_RES);
			} else {
				fincFundacc = "-";
			}
		}
		finc_fundacc.setText(fincFundacc);

		String accNumStr = null;
		if (!StringUtil.isNull(fincControl.accDetailsMap
				.get(Finc.FINC_ACCOUNT_RES))) {
			accNumStr = fincControl.accDetailsMap.get(Finc.FINC_ACCOUNT_RES);
			accNumStr = StringUtil.getForSixForString(accNumStr);
		} else {
			accNumStr = "-";
		}
		finc_card_number.setText(accNumStr);

		String fundCode = (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDCODE);
		if (StringUtil.isNull(fundCode)) {
			fundCode = "-";
		}
		finc_productcode_textview.setText(fundCode);

		String fincFundAccontNameText = null;
		if (!StringUtil.isNull(fincControl.accDetailsMap
				.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE))) {
			fincFundAccontNameText = fincControl.accDetailsMap
					.get(Finc.FINC_QUERYACCLIST_ACCOUNTTYPE);
			fincFundAccontNameText = LocalData.AccountType
					.get(fincFundAccontNameText.trim());
		} else {
			fincFundAccontNameText = "-";
		}
		finc_card_type.setText(fincFundAccontNameText);

		String fincFundAccontNickNameText = null;
		if (!StringUtil.isNull(fincControl.accDetailsMap
				.get(Finc.FINC_ACCOUNTNICKNAME_RES))) {
			fincFundAccontNickNameText = fincControl.accDetailsMap
					.get(Finc.FINC_ACCOUNTNICKNAME_RES);
		} else {
			fincFundAccontNickNameText = "-";
		}
		finc_card_alias.setText(fincFundAccontNickNameText);

		String fundNameStr = (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDNAME);
		if (StringUtil.isNull(fundNameStr)) {
			fundNameStr = "-";
		}
		finc_productname_textview.setText(fundNameStr);

		String currencyStr = (String) fincControl.fundDetails
				.get(Finc.FINC_CURRENCY);
		String cashFlagCode = (String) fincControl.fundDetails
				.get(Finc.FINC_CASHFLAG);

		finc_currency_textview.setText(FincControl.fincCurrencyAndCashFlag(
				currencyStr, cashFlagCode));
       
		finc_sign_date_textview.setText(StringUtil.isNullOrEmpty(signDate)?"-":signDate);

	}

	private void init() {
		View childview = mainInflater.inflate(
				R.layout.finc_signelectroncontract_succeed, null);
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
		finc_succeed = (Button) findViewById(R.id.finc_succeed);
		finc_sign_date_textview = (TextView) findViewById(R.id.finc_sign_date_textview);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

}