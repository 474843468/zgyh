package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 双向宝解约成功页面 */
public class IsForexCancelSuccessActivity extends IsForexBaseActivity {
	private static final String TAG = "IsForexCancelSuccessActivity";
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
		backButton.setVisibility(View.INVISIBLE);
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_produce_cancel_success, null);
		tabcontent.addView(rateInfoView);
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
				Intent intent = new Intent(IsForexCancelSuccessActivity.this, IsForexBailProduceActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
