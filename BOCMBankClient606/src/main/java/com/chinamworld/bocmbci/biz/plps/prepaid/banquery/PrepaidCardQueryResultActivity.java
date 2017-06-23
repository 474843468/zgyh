package com.chinamworld.bocmbci.biz.plps.prepaid.banquery;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 预付卡充值结果页面
 * zxj*/
public class PrepaidCardQueryResultActivity extends PlpsBaseActivity{
	//卡类型
	private TextView cardTypeText;
	private String cardType;
	//预付卡卡号
	private TextView cardNoText;
	private String cardNo;
	//预付卡姓名
	private TextView nameText;
	private String name;
	//币种
	private TextView currentyText;
	private String currency;
	private String currencyStr;
	//余额
	private TextView cardBalanceText;
	private String cardBalance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_prepaid_query_result);
		setTitle(getString(R.string.plps_prepaid_title_name));
		mLeftButton.setVisibility(View.GONE);
		getIntentData();
		init();
	}
	private void getIntentData(){
		cardType = getIntent().getStringExtra(Plps.MERCHNAME);
		cardNo = getIntent().getStringExtra(Plps.PREPARDQUERYNUMBER);
		name = getIntent().getStringExtra(Plps.PREPARDQUERYNAME);
		currency = getIntent().getStringExtra(Plps.PREPARDQUERYCURRENCY);
		currencyStr = LocalData.Currency.get(currency);
		cardBalance = getIntent().getStringExtra(Plps.PREPARDQUERYCARDBALANCE);
	}
	private void init(){
		cardTypeText = (TextView)findViewById(R.id.typename);
		cardTypeText.setText(cardType);
		cardNoText = (TextView)findViewById(R.id.cardnumber);
		cardNoText.setText(cardNo);
		nameText = (TextView)findViewById(R.id.name);
		if(!StringUtil.isNullOrEmpty(name)){
			findViewById(R.id.namelayout).setVisibility(View.VISIBLE);
			nameText.setText(name);
		}
		currentyText = (TextView)findViewById(R.id.currency);
		currentyText.setText(currencyStr);
		cardBalanceText = (TextView)findViewById(R.id.balance);
		cardBalanceText.setText(StringUtil.parseStringPattern2(cardBalance, 2));
	}
	public void btnNextOnclick(View v){
		setResult(RESULT_OK);
		finish();
	}
}