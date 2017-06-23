package com.chinamworld.bocmbci.biz.lsforex.bail;

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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 保证金交易   成功页面*/
public class IsForexBailInfoSuccessActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexBailInfoSuccessActivity";
	/** IsForexBailInfoSuccessActivity的主布局*/
	private View rateInfoView = null;
	/** 交易序号 */
	private TextView accNumberText = null;
	/** 资金账户 */
	private TextView zjAccTexy = null;
	/** 转账方式 */
	private TextView throwTypeText = null;
	/** 币种 */
	private TextView codeText = null;
	/** 转账金额 */
	private TextView throwMoneyText = null;
	/** 保证金余额 */
	private TextView stockBalanceText = null;
	/** 币种代码 */
	private String codeCode = null;
	/** 钞汇代码 */
	private String cashRemit = null;
	/** 输入金额 */
	private String inputMoney = null;
	/** 操作方式 代码 */
	private String dealType = null;
	/** 资金账户 */
	private String accountNumber = null;
	/** 交易序号 */
	private String transactionId = null;
	/** 保证金账户余额 */
	private String stockBalance = null;
	/** 完成按钮 */
	private Button finishButton = null;
	/** 返回按钮 */
	private Button backButton = null;
	private TextView leftText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_throw_title));
		LogGloble.d(TAG, "onCreate");
		init();
		initDate();
		initClick();
	}

	private void init() {
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_bail_success, null);
		tabcontent.addView(rateInfoView);
		zjAccTexy = (TextView) findViewById(R.id.isForex_throw_confirm_accNumber);
		throwTypeText = (TextView) findViewById(R.id.isForex_throw_confirm_accType);
		codeText = (TextView) findViewById(R.id.isForex_throw_confirm_code);
		throwMoneyText = (TextView) findViewById(R.id.isForex_throw_money);
		accNumberText = (TextView) findViewById(R.id.isForex_myrate_tradeNumber);
		stockBalanceText = (TextView) findViewById(R.id.isForex_throw_confirm_money);
		finishButton = (Button) findViewById(R.id.sureButton);
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		leftText = (TextView) findViewById(R.id.left_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, leftText);
	}

	private void initDate() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		accountNumber = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);// 资金账户
		inputMoney = intent.getStringExtra(ConstantGloble.ISFOREX_INPUTMONEY);
		dealType = intent.getStringExtra(ConstantGloble.ISFOREX_FUNDTRANSFERDIR);
		cashRemit = intent.getStringExtra(IsForex.ISFOREX_CASEREMIT_RES);
		codeCode = intent.getStringExtra(IsForex.ISFOREX_CODE1_RES);
		if (StringUtil.isNull(codeCode)) {
			return;
		}
		String codeName = LocalData.Currency.get(codeCode);// 币种名称

		if (StringUtil.isNull(cashRemit)) {
			return;
		}
		String cash = LocalData.isForexcashRemitMap.get(cashRemit);
		if (StringUtil.isNull(accountNumber)) {
			return;
		}
		if (StringUtil.isNull(dealType)) {
			return;
		}
		String type = LocalData.isForexfundTransferDirMap.get(dealType);
		transactionId = intent.getStringExtra(IsForex.ISFOREX_TRANSACTIONID_REQ);
		stockBalance = intent.getStringExtra(IsForex.ISFOREX_STOCKBALANCE_REQ);

		accNumberText.setText(transactionId);
		String acc = StringUtil.getForSixForString(accountNumber);
		zjAccTexy.setText(acc);
		throwTypeText.setText(type);
		if (LocalData.rebList.contains(codeCode)) {
			codeText.setText(codeName);
		} else {
			codeText.setText(codeName + cash);
		}
		String mm = StringUtil.parseStringCodePattern(codeCode, inputMoney, fourNumber);
		throwMoneyText.setText(mm);
		String cc = StringUtil.parseStringCodePattern(codeCode, stockBalance, fourNumber);
		if (StringUtil.isNullOrEmpty(stockBalance)) {
			stockBalanceText.setText("-");
		} else {
			stockBalanceText.setText(cc);
		}
	}

	private void initClick() {
		finishButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new
				// Intent(IsForexBailInfoSuccessActivity.this,
				// IsForexBailInfoActivity.class);
				// startActivity(intent);
				setResult(RESULT_OK);
				finish();
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
