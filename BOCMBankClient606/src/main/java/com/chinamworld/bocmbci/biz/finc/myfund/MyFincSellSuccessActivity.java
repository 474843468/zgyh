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
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 我的基金 卖出基金成功页面
 * 
 * @author xyl
 * 
 */
public class MyFincSellSuccessActivity extends FincBaseActivity {
	public static final String TAG = "MyFincSellSuccessActivity";
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
	/** 赎回方式 */
	private TextView sellTypeText = null;
	/** 确定 */
	private Button sureButton = null;
	/** 交易序号 */
	private TextView transactionIdText = null;
	/** 基金交易流水号 */
	private TextView fundSeqText = null;
	private String assignedDate;
	private String sellTotalValue;
	private String sellTypeValue;
	private String foundCode;
	private String foundName;
	private String account;
	private String investAccount;
	private TextView finc_myfinc_sel_success_fundSeq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initOnClick();
	}

	private void init() {
		setRightToMainHome();
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_sell_success,
				null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_sellFound));
		transactionIdText = (TextView) findViewById(R.id.finc_transactionId);
		accNumber = (TextView) findViewById(R.id.finc_accId);
		fincAccNumber = (TextView) findViewById(R.id.finc_accNumber);
		fundSeqText = (TextView) findViewById(R.id.finc_fundSeq);
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		totalCountText = (TextView) findViewById(R.id.finc_sellAmount);
		sellTypeText = (TextView) findViewById(R.id.finc_sellType);
		sureButton = (Button) findViewById(R.id.sureButton);
		finc_myfinc_sel_success_fundSeq = (TextView) findViewById(R.id.finc_myfinc_sel_success_fundSeq);
		initRightBtnForMain();
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		TextView	sellTypePreTv = (TextView) findViewById(R.id.finc_sellType_pre);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, sellTypePreTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincAccNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, transactionIdText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_myfinc_sel_success_fundSeq);
	}

	/** 初始化卖出页面传递的参数，并为控件赋值 */
	private void initData() {
		account = fincControl.accNum;
		investAccount = fincControl.invAccId;
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras.getString(Finc.I_ASSIGNEDDATE) == null) {// 当日交易
			assignedDate = null;
			StepTitleUtils.getInstance().initTitldStep(this,
					fincControl.getStepsForSell());
			StepTitleUtils.getInstance().setTitleStep(3);
		} else {
			assignedDate = extras.getString(Finc.I_ASSIGNEDDATE);
			StepTitleUtils.getInstance().initTitldStep(this,
					fincControl.getStepsForExtradeDaySell2());
			StepTitleUtils.getInstance().setTitleStep(3);
			LinearLayout executeTypeLayout = (LinearLayout)findViewById(R.id.finc_executeType_layout);
			LinearLayout exeDateLayout = (LinearLayout)findViewById(R.id.finc_exeDate_layout);
			exeDateLayout.setVisibility(View.VISIBLE);
			executeTypeLayout.setVisibility(View.VISIBLE);
			TextView exeDateTv = (TextView)findViewById(R.id.finc_exeDate_tv);
			exeDateTv.setText(assignedDate);
		}

		sellTotalValue = intent.getStringExtra(Finc.FINC_SELLAMOUNT_REQ);
		sellTypeValue = intent.getStringExtra(Finc.I_SELLTYPEVALUE);
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
//			Map<String, String> fundInfoMap = (Map<String, String>) fincControl.tradeFundDetails
//					.get(Finc.FINC_FUNDINFO_REQ);
			Map<String, Object> fundBalanceMap = fincControl.tradeFundDetails;
//			Map<String, String> fundInfoMap = (Map<String, String>) fundBalanceMap
//					.get(Finc.FINC_FUNDINFO_REQ);
			Map<String, Object> fundInfoMap = (Map<String, Object>) fundBalanceMap
					.get(Finc.FINC_FUNDINFO_REQ);
			if(fundInfoMap == null){
				fundInfoMap = fundBalanceMap;
			}			
			foundCode = (String)fundInfoMap.get(Finc.FINC_FUNDCODE);
			foundName = (String)fundInfoMap.get(Finc.FINC_FUNDNAME);
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
		sellTypeText.setText(LocalData.ffundSellDealTypeMap.get(sellTypeValue));
		
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
