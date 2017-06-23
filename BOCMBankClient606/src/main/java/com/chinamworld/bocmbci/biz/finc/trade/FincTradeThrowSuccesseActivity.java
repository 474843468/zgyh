package com.chinamworld.bocmbci.biz.finc.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 登记基金它账户
 * 
 * @author xyl
 * 
 */
public class FincTradeThrowSuccesseActivity extends FincBaseActivity {
	private static final String TAG = "FincTradeThrowSuccesseActivity";

	/** 资金账户 */
	private TextView fundAccTv;
	/** 基金(投资)账户 */
	private TextView invAccTv;
	/** 转出基金代码 */
	private TextView fromfundCodeTv;
	/** 转入基金代码 */
	private TextView tofundCodeTv;
	/** (转出)份额 */
	private TextView amountTv;
	/** 发生巨额赎回的方式 */
	private TextView fundSellFlagTv;
	private TextView transactionIdTv, fundSeqTv;

	private String fundSellFlagStr;

	private String amountStr;

	private String inFundCodeStr, inFundNameStr;

	private String outFundNameStr;

	private String outFundCodeStr;

	private Button confirmBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		outFundCodeStr = (String) extras.get(Finc.I_OUTFUNDCODE);
		outFundNameStr = (String) extras.get(Finc.I_OUTFUDNAME);
		inFundCodeStr = (String) extras.get(Finc.I_INFUDCODE);
		inFundNameStr = (String) extras.get(Finc.I_INFUNDNAME);
		amountStr = (String) extras.get(Finc.I_AMOUNT);
		fundSellFlagStr = (String) extras.get(Finc.I_FUNDSELLFLAGSTR);

		fundSellFlagTv.setText(LocalData.fundSellFlagCodeToStr
				.get(fundSellFlagStr));
		fromfundCodeTv.setText(outFundCodeStr + "/" + outFundNameStr);
		tofundCodeTv.setText(inFundCodeStr + "/" + inFundNameStr);

		amountTv.setText(StringUtil.parseStringPattern(amountStr, 2));
		fundAccTv.setText(StringUtil.getForSixForString(fincControl.accNum));
		invAccTv.setText(fincControl.invAccId);
		String transactionIdStr = StringUtil.valueOf1(extras
				.getString(Finc.I_TRANSACTIONID));
		String fundSeqStr = StringUtil.valueOf1(extras
				.getString(Finc.I_FINCBUYSQL));
		transactionIdTv.setText(transactionIdStr);
		fundSeqTv.setText(fundSeqStr);
		
		TextView finc_myfinc_sell_sellType = (TextView) findViewById(R.id.finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fromfundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tofundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundSeqTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundAccTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, invAccTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_myfinc_sell_sellType);
	}

	private void init() {
		View childView = mainInflater.inflate(
				R.layout.finc_myfinc_balance_throw_success, null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_myfinc_button_give);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForDingTou());
		StepTitleUtils.getInstance().setTitleStep(2);
		transactionIdTv = (TextView) childView
				.findViewById(R.id.finc_transactionId_tv);
		fundSeqTv = (TextView) childView.findViewById(R.id.finc_fundseq_tv);
		fundAccTv = (TextView) childView.findViewById(R.id.finc_accNumber);
		invAccTv = (TextView) childView.findViewById(R.id.finc_accId);
		fromfundCodeTv = (TextView) childView.findViewById(R.id.finc_throw);
		tofundCodeTv = (TextView) childView.findViewById(R.id.finc_throw_in);
		amountTv = (TextView) childView.findViewById(R.id.finc_throw_total);
		fundSellFlagTv = (TextView) childView.findViewById(R.id.finc_sellType);
		confirmBtn = (Button) childView.findViewById(R.id.sureButton);
		confirmBtn.setOnClickListener(this);
		initRightBtnForMain();
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sureButton:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
