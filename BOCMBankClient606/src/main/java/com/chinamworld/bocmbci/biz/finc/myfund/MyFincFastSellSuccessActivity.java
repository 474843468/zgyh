package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 我的基金----快速赎回基金成功页面
 * 
 * @author fsm
 * 
 */
public class MyFincFastSellSuccessActivity extends FincBaseActivity {
	/** 主view */
	private View myFincView = null;
	/** 资金账户 */
	private TextView accNumber = null;
	/** 基金账户 */
	private TextView fincAccNumber = null;
	/** 基金代码 */
	private TextView fincCodeText = null;
	/** 基金名称 */
	private TextView fincNameText = null;
	/** 赎回份额 */
	private TextView totalCountText = null;
	/** 确定 */
	private Button sureButton = null;
	/** 交易序号 */
	private TextView transactionIdText = null;
	/** 基金交易流水号 */
	private TextView fundSeqText = null;
	private String sellTotalValue;
	private String foundCode;
	private String foundName;
	private String account;
	private String investAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initOnClick();
	}


	private void init() {
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_sell_success,
				null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_fastsellFound));
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		((TextView)findViewById(R.id.tv_confirm_title_2))
			.setText(getString(R.string.finc_step_fastsell_success));
		((LinearLayout)findViewById(R.id.finc_sellType_ll)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.finc_accId_ll)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.finc_accNumber_ll)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.finc_sellAmount_tv))
				.setText(getString(R.string.finc_myfinc_sell_fastSellAmount));
		transactionIdText = (TextView) findViewById(R.id.finc_transactionId);
		accNumber = (TextView) findViewById(R.id.finc_accId);
		fincAccNumber = (TextView) findViewById(R.id.finc_accNumber);
		fundSeqText = (TextView) findViewById(R.id.finc_fundSeq);
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		totalCountText = (TextView) findViewById(R.id.finc_sellAmount);
		sureButton = (Button) findViewById(R.id.sureButton);
		initRightBtnForMain();
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		TextView sellTypePreTv = (TextView) findViewById(R.id.finc_sellType_pre);
		FincControl.setOnShowAllTextListener(this, sellTypePreTv, accNumber, fincAccNumber, 
				transactionIdText, fincNameText);
	}

	/** 初始化卖出页面传递的参数，并为控件赋值 */
	private void initData() {
		account = fincControl.accNum;
		investAccount = fincControl.invAccId;
		Intent intent = getIntent();
		sellTotalValue = intent.getStringExtra(Finc.FINC_SELLAMOUNT_REQ);
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
			Map<String, String> fundInfoMap = (Map<String, String>) fincControl.tradeFundDetails
					.get(Finc.FINC_FUNDINFO_REQ);
			foundCode = fundInfoMap.get(Finc.FINC_FUNDCODE);
			foundName = fundInfoMap.get(Finc.FINC_FUNDNAME);
		}
		String transactionId = intent
				.getStringExtra(Finc.FINC_TRANSACTIONID_REQ);
		String fundSeq = intent.getStringExtra(Finc.FINC_FUNDSEQ_REQ);
		transactionIdText.setText(transactionId);
		accNumber.setText(StringUtil.getForSixForString(account));
		fincAccNumber.setText(investAccount);
		fundSeqText.setText(fundSeq);
		fincCodeText.setText(foundCode);
		fincNameText.setText(foundName);
		totalCountText.setText(StringUtil.parseStringPattern(sellTotalValue, 2));
		FincControl.setTextColor(totalCountText, this);	
		
	}

	private void initOnClick() {
		sureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
