package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BRemitSuccessActivity extends SBRemitBaseActivity implements
		OnClickListener {

	/** 交易序号 */
	private String transId;
	/** 账号标识 */
	private String acountId;
	/** 账号 */
	private String acountNumber;
	/** 币种 */
	private String currency;
	/** 钞汇标志 */
	private String cashRemit;
	/** 资金来源 */
	private String capitalSource;
	/** 已使用额度 */
	private String useAmount;
	/** 剩余额度 */
	private String remainAmount;
	/** 兑换牌价 */
	private String exchangeRate;
	/** 人民币金额 */
	private String rmbAmount;
	/** 购汇金额 */
	private String bRemitAmount;
	/** 交易状态 */
	private String tradeStatus;
	/** 控件 交易序号 */
	private TextView trade_serial_number;
	/** 控件 账号 */
	private TextView bremit_acc;
	/** 控件 币种 钞汇 */
	private TextView money_type_cash_remit;
	/** 控件 资金用途 */
	private TextView capital_use;
	/** 控件 年已用额度 */
	private TextView year_have_used_limit;
	/** 控件 年剩余额度 */
	private TextView year_remain_limit;
	/** 控件 兑换牌价 */
	private TextView exchange_rate;
	/** 控件 人民币金额 */
	private TextView rmb_money;
	/** 控件 购汇金额 */
	private TextView bremit_money;
	/** 控件 交易状态 */
	private TextView trade_status;
	/** 控件 下一步 */
	private Button sbremit_bremit_success_ok;
	/** 折合成人民币 */
	private String returnCnyAmt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.sbremit_trade_type_bremit));// 为界面标题赋值
		addView(R.layout.sbremit_br_succ);// 添加布局
		back.setVisibility(View.INVISIBLE);// 屏蔽返回按钮
		BaseHttpEngine.showProgressDialog();
		initViews();
		initParamsInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.ib_top_right_btn:// 返回主页
			ActivityTaskManager.getInstance().removeAllActivity();
			break;
		case R.id.sbremit_bremit_success_ok:
			// 购汇成功确定
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			startActivity(new Intent(this, ChooseAccountActivity.class));
			// setResult(RESULT_OK);
			// finish();
			break;
		default:
			break;
		}
	}

	private void initViews() {
		back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		trade_serial_number = (TextView) findViewById(R.id.trade_serial_number);
		bremit_acc = (TextView) findViewById(R.id.bremit_acc);
		money_type_cash_remit = (TextView) findViewById(R.id.money_type);
		capital_use = (TextView) findViewById(R.id.capital_use);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,capital_use);
		year_have_used_limit = (TextView) findViewById(R.id.year_have_used_limit);
		year_remain_limit = (TextView) findViewById(R.id.year_remain_limit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_have_used_limit_alert));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.year_remain_limit_alert));
		exchange_rate = (TextView) findViewById(R.id.exchange_reference_price);
		// 省略时加浮动框
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				exchange_rate);
		rmb_money = (TextView) findViewById(R.id.rmb_money);
		bremit_money = (TextView) findViewById(R.id.bremit_money);
		trade_status = (TextView) findViewById(R.id.trade_status);
		sbremit_bremit_success_ok = (Button) findViewById(R.id.sbremit_bremit_success_ok);
		sbremit_bremit_success_ok.setOnClickListener(this);
	}

	/**
	 * 接收调用页的信息，信息完整则初始本页面
	 */
	private void initParamsInfo() {
		Intent intent = getIntent();
		Map<String, Object> accInfo = SBRemitDataCenter.getInstance()
				.getAccInfo();
		transId = intent.getStringExtra(SBRemit.TRANSACTION_ID);
		acountId = (String) accInfo.get(SBRemit.ACCOUNT_ID);
		acountNumber = (String) accInfo.get(SBRemit.ACCOUNT_NUMBER);
		currency = intent.getStringExtra(SBRemit.CURRENCY);
		cashRemit = intent.getStringExtra(SBRemit.CASH_REMIT);
		capitalSource = intent.getStringExtra(SBRemit.FUND_USE_INFO);
		bRemitAmount = intent.getStringExtra(SBRemit.AMOUNT);
		tradeStatus = intent.getStringExtra(SBRemit.TRADE_STATUS_2);
		exchangeRate = intent.getStringExtra(SBRemit.EXCHANGE_RATE);
		returnCnyAmt = intent.getStringExtra(SBRemit.RETURN_CNY_AMT);
		// exchangeRate = StringUtil.parseStringPattern(
		// exchangeRate,
		// SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		useAmount = (String) SBRemitDataCenter.getInstance().getResultAmount()
				.get("annAmtUSD");
		// useAmount = StringUtil.parseStringPattern(
		// useAmount,
		// SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		remainAmount = (String) SBRemitDataCenter.getInstance()
				.getResultAmount().get("annRmeAmtUSD");
		// remainAmount = StringUtil.parseStringPattern(
		// remainAmount,
		// SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		rmbAmount = intent.getStringExtra(SBRemit.RETURN_CNY_AMT);
		// rmbAmount = StringUtil.parseStringPattern(
		// rmbAmount,
		// SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
		String acountType = (String) accInfo.get(Comm.ACCOUNT_TYPE);
		if (SBRemitDataCenter.dissMissAcc.contains(acountType)) {
			((TextView) findViewById(R.id.year_have_used_limit_alert))
					.setText(getString(R.string.validity_have_used_limit));
			((TextView) findViewById(R.id.year_remain_limit_alert))
					.setText(getString(R.string.validity_remain_limit));
		}
		try {
			initViewInfos();
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
	}

	/**
	 * 初始化本页信息单
	 */
	private void initViewInfos() throws Exception {
		BaseHttpEngine.dissMissProgressDialog();
		trade_serial_number.setText(transId);
		bremit_acc.setText(StringUtil.getForSixForString(acountNumber));
		money_type_cash_remit.setText(LocalData.Currency.get(currency)
				+"  "+LocalData.CurrencyCashremit.get(cashRemit));
		if (StringUtil.isNull(capitalUse.get(capitalSource))) {
			capital_use.setText("-");
		} else {
			capital_use.setText(capitalUse.get(capitalSource));// 资源用途没有121此代码
		}
		bremit_money.setText(StringUtil.parseStringCodePattern(currency,
				bRemitAmount,
				SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM));
		if (isStrEquals(tradeStatus, tradIng)) {
			exchange_rate.setText("-");
			rmb_money.setText("-");
			year_have_used_limit.setText("-");
			year_remain_limit.setText("-");
		} else {
			//正则表达式去掉小数点后无用的0
			if(exchangeRate.indexOf(".")>0){
				
				exchangeRate=exchangeRate.replaceAll("0+?$", "");
				exchangeRate=exchangeRate.replaceAll("[.]$", "");
				
			}
			if(StringUtil.splitStringwith2pointnew(exchangeRate)<4){
				exchange_rate.setText(exchangeRate);
			}else{
				String str = StringUtil.splitStringwith2point(exchangeRate,4);
				try{
					double d = Double.parseDouble(str);
					 str = String.valueOf(d);
					 exchange_rate.setText(str);
				}
				catch(Exception e){
					exchange_rate.setText(str);
				}
			}
			rmb_money.setText(StringUtil.parseStringPattern(rmbAmount,
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM));
			// String str = colorText(useAmount, "#ba001d", true)
			// + colorText(MEIYUAN, "", false);
			// year_have_used_limit.setText(Html.fromHtml(str));
			// str = colorText(remainAmount, "#ba001d", true)
			// + colorText(MEIYUAN, "", false);
			String str1 = "-";
			if (!StringUtil.isNull(useAmount)) {
				useAmount=StringUtil.parseStringPattern2(useAmount, 2);
				str1 = DENGZHI + useAmount + MEIYUAN;
				year_have_used_limit.setText(str1);
			}else {
				String str1_new = DENGZHI + "-" + MEIYUAN;
				year_have_used_limit.setText(str1_new);
			}
			
			String str2 = "-";
			if (!StringUtil.isNull(remainAmount)) {
				remainAmount=StringUtil.parseStringPattern2(remainAmount, 2);
				str2 = DENGZHI + remainAmount + MEIYUAN;
				year_remain_limit.setText(str2);
			}else {
				String str2_new = DENGZHI + "-" + MEIYUAN;
				year_remain_limit.setText(str2_new);
			}
			
		}
		trade_status.setText(transStatus_new.get(tradeStatus));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
