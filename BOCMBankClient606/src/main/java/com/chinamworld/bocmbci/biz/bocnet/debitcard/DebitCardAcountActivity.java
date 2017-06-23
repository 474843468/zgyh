package com.chinamworld.bocmbci.biz.bocnet.debitcard;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 借记卡账户主页
 * @author panwe
 *
 */
public class DebitCardAcountActivity extends BocnetBaseActivity{

	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.bocnet_debitdetail);
		setTitle(R.string.acc_main_title);
		setupViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(getLeftBtnVisible() == View.VISIBLE)
			setLeftSelectedPosition("bocnet_1");
	}

	private void setupViews(){
		setLeftButtonGone();
		setRightButton(getString(R.string.exit), exitClickListener);

		button = (Button)findViewById(R.id.button);
		initBankingFlagBtn(button);
		button.setOnClickListener(this);
		TextView name = (TextView) findViewById(R.id.crcd_type_value);
		TextView number = (TextView) findViewById(R.id.crcd_account_num);
		TextView nikName = (TextView) findViewById(R.id.crcd_account_nickname);

		TextView acctType = (TextView) findViewById(R.id.acct_type);
		TextView acctNikName = (TextView) findViewById(R.id.acc_nickname);
		TextView acctNumber = (TextView) findViewById(R.id.acc_num);
		TextView acctState = (TextView) findViewById(R.id.acc_state);
		TextView acctBranchName = (TextView) findViewById(R.id.acc_branchname);
		TextView acctOpenTime = (TextView) findViewById(R.id.acc_opentime);

		Map<String, Object> loginInfo = BocnetDataCenter.getInstance().getLoginInfo();
		if (!StringUtil.isNullOrEmpty(loginInfo)) {
			name.setText(LocalData.AccountType.get((String)loginInfo.get(Bocnet.ACCOUNTTYPE)));
			number.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			acctNumber.setText(StringUtil.getForSixForString((String)loginInfo.get(Bocnet.ACCOUNTNUMBER)));
			nikName.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
			acctNikName.setText(StringUtil.valueOf1((String)loginInfo.get(Bocnet.NAME)));
		}
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, acctBranchName);

		Map<String, Object> map = BocnetDataCenter.getInstance().getDebitDetail();
		if(!StringUtil.isNullOrEmpty(map)){
			acctType.setText(LocalData.AccountType.get((String)map.get(Bocnet.ACCOUNTTYPE)));
			/** 开户行   **/
			acctBranchName.setText(StringUtil.valueOf1((String)map.get(Bocnet.OPENBANK)));
			/** 账户状态   **/
			acctState.setText(LocalData.SubAccountsStatus.get(map.get(Bocnet.ACCOUNTSTATUS)));
			/** 开户日期   **/
			acctOpenTime.setText(StringUtil.valueOf1((String)map.get(Bocnet.OPENDATE)));
			setCurrencyDatas((List<Map<String, Object>>)map.get(Bocnet.ACCOUNTDETAILIST));
		}
	}

	private void setCurrencyDatas(List<Map<String, Object>> list){
		if(StringUtil.isNullOrEmpty(list))
			return;
		LinearLayout ll_add_currency = (LinearLayout)findViewById(R.id.ll_add_currency);
		for (int i = 0; i < list.size(); i++) {
			View currency_view = LayoutInflater.from(this).inflate(
					R.layout.bocnet_debit_currency_item, null);
			ll_add_currency.addView(currency_view, i);

			TextView tv_acc_currencycode = (TextView) currency_view
					.findViewById(R.id.acc_currencycode);
			TextView tv_acc_accbookbalance = (TextView) currency_view
					.findViewById(R.id.acc_bookbalance);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					tv_acc_accbookbalance);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					tv_acc_currencycode);
			String currencyname = (String) list.get(i).get(Bocnet.CURRENCYCODE);
			String cashRemit = (String) list.get(i).get(Bocnet.CASHREMIT);
			if (LocalData.Currency.get(currencyname).equals(
					ConstantGloble.ACC_RMB)) {
				tv_acc_currencycode.setText(LocalData.Currency
						.get(currencyname) + ConstantGloble.ACC_COLON);
			} else {
				if("-".equals(LocalData.CurrencyCashremit.get(cashRemit)) ){
					tv_acc_currencycode.setText(LocalData.Currency
							.get(currencyname)+ConstantGloble.ACC_COLON);
				}else{
					tv_acc_currencycode.setText(LocalData.Currency
							.get(currencyname)
							+ ConstantGloble.ACC_STRING
							+ LocalData.CurrencyCashremit.get(cashRemit)
							+ ConstantGloble.ACC_COLON);}
			}
			tv_acc_accbookbalance.setText(StringUtil.parseStringCodePattern(
					currencyname,
					(String) list.get(i).get(Bocnet.AVAILABLEBALANCE),
					2));
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.button:
			eBankingFlag();
			break;

		default:
			break;
		}
	}

	/**
	 * 系统时间
	 * @param v
	 */
	public void debitDetailOnclick(View v){
		BaseHttpEngine.showProgressDialog();
		requestSystemTime();
	}

	@Override
	public void systemTimeCallBack(Object resultObj) {
		super.systemTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(new Intent(this, DebitCardTransDetailActivity.class));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
