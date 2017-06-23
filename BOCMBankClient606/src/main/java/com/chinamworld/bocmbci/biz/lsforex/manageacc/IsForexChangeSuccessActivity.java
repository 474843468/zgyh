package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.acc.IsForexSettingBindAccActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 我的外汇双向宝 签约管理    变更成功页面*/
public class IsForexChangeSuccessActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexChangeSuccessActivity";
	private Button backButton = null;
	/** 完成按钮 */
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
	private String newAcc = null;
	private String newAccountNo = null;
	/** 登记交易账户 */
	private Button signAccButton = null;
	private String prefixType = null;
	private TextView tradeAccTypeText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_change_title));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.INVISIBLE);
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
		newAccountNo = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTNUMBER_RES);
		tradeAcc = intent.getStringExtra(IsForex.ISFOREX_OLDACCOUNTNUMBER_RES);
		bailAcc = intent.getStringExtra(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		settleCurrency = intent.getStringExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		liquitRate = intent.getStringExtra(IsForex.ISFOREX_LIQUIDATIONRATIO_RES);
		nickName = intent.getStringExtra(IsForex.ISFOREX_NICKNAME_RES1);
		prefixType = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_produce_change_success, null);
		tabcontent.addView(rateInfoView);
		bailAccText = (TextView) findViewById(R.id.isforex_bailAcc);
		tradeAccText = (TextView) findViewById(R.id.isforex_tradeAcc);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency1);
		zcRateText = (TextView) findViewById(R.id.isForex_manage_liquidationRatio);
		sureButton = (Button) findViewById(R.id.trade_nextButton);
		newTradeAccSpinner = (TextView) findViewById(R.id.new_tradeAcc);
		nickNameText = (TextView) findViewById(R.id.isforex_nickName);
		signAccButton = (Button) findViewById(R.id.trade_signAcc);
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
		if (!StringUtil.isNull(marginAccountNo)) {
			newAcc = StringUtil.getForSixForString(newAccountNo);
		}
		newTradeAccSpinner.setText(newAcc);
		nickNameText.setText(nickName);
		tradeAccTypeText.setText(prefixType);
	}

	private void initOnClick() {
		// 完成按钮
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IsForexChangeSuccessActivity.this, IsForexBailProduceActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 登记交易账户
		signAccButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IsForexChangeSuccessActivity.this, IsForexSettingBindAccActivity.class);
				startActivity(intent);
			}
		});
	}
}
