package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡还款方式设定详情
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupDetailActivity extends CrcdBaseActivity {
	private static final String TAG = "MyCrcdSetupDetailActivity";
	/** 信用卡还款方式设定详情 页 */
	private View view;
	TextView tv_cardNumber;

	TextView mycrcd_accounted_type;
	TextView mycrcd_selected_creditcard;
	TextView mycrcd_accounted_money;
	TextView rate_fix_sellCode;

	TextView rate_currency_buyCode;
	TextView rate_currency_type;
	TextView rate_fix_papRate;
	TextView rate_fix_comRate;
	/** 主动还款 */
	Button lastButton;
	/** 自动还款 */
	Button sureButton;

	/** 0 = 主动还款,1 = 自动还款 */
	protected static int repayType = 0;
	protected static String strRepayType;

	static String benNumber;
	static String waiNumber;

	static String strLocalPayment;
	static String strCurrencyCode;

	static String strPaymentModeType;
	static String strPayment;

	static String strForeignCurrency;
	static String strForPaymentType;

	LinearLayout ben_layout;
	LinearLayout wai_layout;

	LinearLayout ll_ben;
	LinearLayout ll_waibi;
	private String accountId = null;
	private String accountNumber = null;
	private int tag = -1;
	/** 还款标志 */
	private int benAutoPaymentTag = -1;
	private int foreanAutoPaymentTag = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_money_setup_title));

		view = addView(R.layout.crcd_setup_detail);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
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

		mycrcd_accounted_type = (TextView) view.findViewById(R.id.mycrcd_accounted_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_accounted_type);
		mycrcd_selected_creditcard = (TextView) view.findViewById(R.id.mycrcd_selected_creditcard);
		mycrcd_accounted_money = (TextView) view.findViewById(R.id.mycrcd_accounted_money);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mycrcd_accounted_money);
		rate_fix_sellCode = (TextView) view.findViewById(R.id.rate_fix_sellCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rate_fix_sellCode);
		rate_currency_buyCode = (TextView) view.findViewById(R.id.rate_currency_buyCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rate_currency_buyCode);

		rate_currency_type = (TextView) view.findViewById(R.id.rate_currency_type);
		rate_fix_papRate = (TextView) view.findViewById(R.id.rate_fix_papRate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rate_fix_papRate);
		rate_fix_comRate = (TextView) view.findViewById(R.id.rate_fix_comRate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rate_fix_comRate);
		lastButton = (Button) view.findViewById(R.id.lastButton);
		sureButton = (Button) view.findViewById(R.id.sureButton);
		ben_layout = (LinearLayout) view.findViewById(R.id.ben_layout);
		wai_layout = (LinearLayout) view.findViewById(R.id.wai_layout);
		ll_ben = (LinearLayout) view.findViewById(R.id.ll_ben);
		ll_waibi = (LinearLayout) view.findViewById(R.id.ll_waibi);
		Map<String, Object> setupMap = null;
		if (tag == 3) {
			// 我的信用卡详情页面
			setupMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.FOREX_ACTAVI_RESULT_KEY);
		} else {
			setupMap = MyCrcdSetupListActivity.returnList;
		}

		String localPayment = String.valueOf(setupMap.get(Crcd.CRCD_LOCALCURRENCYPAYMENT));
		strLocalPayment = LocalData.paymentType.get(localPayment);
		String foreignpayment = String.valueOf(setupMap.get(Crcd.CRCD_FOREIGNCURRENCYPAYMENT));
		strPayment = LocalData.paymentType.get(foreignpayment);
		rate_currency_buyCode.setText(strPayment);
		mycrcd_accounted_type.setText(strLocalPayment);

		benNumber = String.valueOf(setupMap.get(Crcd.CRCD_LOCALCURRENCYACCOUNTNO));
		mycrcd_selected_creditcard.setText(StringUtil.getForSixForString(benNumber));

		strCurrencyCode = LocalData.Currency.get(String.valueOf(setupMap.get(Crcd.CRCD_LOCALCURRENCYACCOUNTCURRENCY)));
		mycrcd_accounted_money.setText(strCurrencyCode);

		strPaymentModeType = LocalData.paymentModeType.get(String.valueOf(setupMap
				.get(Crcd.CRCD_LOCALCURRENCYPAYMENTMODE)));
		rate_fix_sellCode.setText(strPaymentModeType);

		waiNumber = String.valueOf(setupMap.get(Crcd.CRCD_FOREIGNCURRENCYACCOUNTNO));
		rate_currency_type.setText(StringUtil.getForSixForString(waiNumber));

		strForeignCurrency = LocalData.Currency.get(String.valueOf(setupMap
				.get(Crcd.CRCD_FOREIGNCURRENCYPACCOUNTCURRENCY)));
		rate_fix_papRate.setText(strForeignCurrency);
		strForPaymentType = LocalData.paymentModeType.get(String.valueOf(setupMap
				.get(Crcd.CRCD_FOREIGNCURRENCYPAYMENTMODE)));
		rate_fix_comRate.setText(strForPaymentType);

		if (StringUtil.isNullOrEmpty(localPayment)) {
			ll_ben.setVisibility(View.GONE);
		} else {
			if ("0".equals(localPayment)) {
				ben_layout.setVisibility(View.GONE);
				benAutoPaymentTag = 0;
			} else if ("1".equals(localPayment)) {
				ben_layout.setVisibility(View.VISIBLE);
				benAutoPaymentTag = 1;
			}
		}
		if (StringUtil.isNullOrEmpty(foreignpayment)) {
			ll_waibi.setVisibility(View.GONE);
		} else {
			if ("0".equals(foreignpayment)) {
				wai_layout.setVisibility(View.GONE);
				foreanAutoPaymentTag = 0;
			} else if ("1".equals(localPayment)) {
				wai_layout.setVisibility(View.VISIBLE);
				foreanAutoPaymentTag = 1;
			}
		}
		// 主动还款方式设定
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				repayType = 0;
				strRepayType = getString(R.string.mycrcd_myself_huanmoney);
				Intent it = new Intent(MyCrcdSetupDetailActivity.this, CrcdpaymentSetupAutoActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				it.putExtra(ConstantGloble.ISFOREX_CASH1, benAutoPaymentTag);
				it.putExtra(ConstantGloble.ISFOREX_CASH2, foreanAutoPaymentTag);
				startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			}
		});
		// 自动还款方式设定
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				repayType = 1;
				strRepayType = getString(R.string.mycrcd_auto_huanmoney);
				Intent it = new Intent(MyCrcdSetupDetailActivity.this, MyCrcdSetupReadActivity.class);
				it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
				// startActivity(it);
				startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			}
		});

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
