package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的基金 定期定额赎回页面
 * 
 * @author 宁焰红
 * 
 */
public class MyFincScheduleSellSuccessActivity extends FincBaseActivity {
	public static final String TAG = "MyFincScheduleSellSuccessActivity";
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
	private TextView dayInMonthTv = null;
	/** 确定 */
	private Button sureButton = null;
	/** 交易序号 */
	private TextView transactionIdText = null;
	/** 基金交易流水号 */
	private TextView fundSeqText = null;

	/**
	 * 定投周期， 每周定投日， 结束条件，指定结束日期，累计成交份额，累计成交金额
	 */
	private String transcycle, paymentdateofweek, endflag, fundpointenddate,
			endsum, fundpointendamount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initOnClick();
		initRightBtnForMain();
	}


	private void init() {
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_schedusell_success, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_scheduedsellFound));
		transactionIdText = (TextView) findViewById(R.id.finc_transactionId);
		accNumber = (TextView) findViewById(R.id.finc_accId);
		fincAccNumber = (TextView) findViewById(R.id.finc_accNumber);
		fundSeqText = (TextView) findViewById(R.id.finc_fundSeq);
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		totalCountText = (TextView) findViewById(R.id.finc_sellAmount);
		sellTypeText = (TextView) findViewById(R.id.finc_sellType);
		dayInMonthTv = (TextView) findViewById(R.id.finc_dayInMonth_sale_tv);
		sureButton = (Button) findViewById(R.id.sureButton);
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		TextView finc_sellTypeTv = (TextView) findViewById(R.id.finc_sellTypeTv);
		TextView finc_myfinc_sel_success_fundSeq = (TextView) findViewById(R.id.finc_myfinc_sel_success_fundSeq);
		FincUtils.setOnShowAllTextListener(this, transactionIdText,
				fincAccNumber, fincNameText, fundSeqText, finc_sellTypeTv,finc_myfinc_sel_success_fundSeq);
	}

	/** 初始化卖出页面传递的参数，并为控件赋值 */
	private void initData() {
		Intent intent = getIntent();
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		StepTitleUtils.getInstance().setTitleStep(3);
		initP405ModiData(intent);
		String foundCode = intent.getStringExtra(Finc.FINC_FUNDCODE_REQ);
		String foundName = intent.getStringExtra(Finc.FINC_FUNDNAME_REQ);
		String sellTotalValue = intent.getStringExtra(Finc.FINC_SELLAMOUNT_REQ);
		String sellTypeValue = intent.getStringExtra(Finc.I_SELLTYPEVALUE);
		String investAccount = intent
				.getStringExtra(Finc.FINC_INVESTACCOUNT_RES);
		String account = intent.getStringExtra(Finc.FINC_ACCOUNT_RES);
		String transactionId = intent
				.getStringExtra(Finc.FINC_TRANSACTIONID_REQ);
		String fundSeq = intent.getStringExtra(Finc.FINC_FUNDSEQ_REQ);
		String dayInMonthStr = intent.getStringExtra(Finc.I_DAYINMONTH);
		transactionIdText.setText(transactionId);
		accNumber.setText(StringUtil.getForSixForString(account));
		fincAccNumber.setText(investAccount);
		fundSeqText.setText(fundSeq);
		fincCodeText.setText(foundCode);
		fincNameText.setText(foundName);
		totalCountText
				.setText(StringUtil.parseStringPattern(sellTotalValue, 2));
		FincControl.setTextColor(totalCountText, this);
		sellTypeText
				.setText(LocalData.fundSellFlagCodeToStr.get(sellTypeValue));
		dayInMonthTv.setText(dayInMonthStr);
	}
	
	
	/**
	 * 405定投优化  增加初始化数据
	 * 添加选择定投期 ，增加周定投 ，增加结束条件
	 */
	private void initP405ModiData(Intent intent){
		if(intent != null){
			transcycle = intent.getStringExtra(Finc.TRANSCYCLE);
			switch (Integer.parseInt(transcycle)) {
			case 0:
				String dayInMonthStr = intent.getStringExtra(Finc.I_DAYINMONTH);
				findViewById(R.id.finc_dayInMonth_ll).setVisibility(View.VISIBLE);
				dayInMonthTv.setText(StringUtil.valueOf1(dayInMonthStr));
				break;
			case 1:
				TextView fincSaleDayOfWeekTv = (TextView)findViewById(R.id.fincSaleDayOfWeekTv);
				findViewById(R.id.fincSaleDayOfWeekLl).setVisibility(View.VISIBLE);
				paymentdateofweek = intent.getStringExtra(Finc.PAYMENTDATEOFWEEK);
				fincSaleDayOfWeekTv.setText(paymentdateofweek);
				break;
			}
			((TextView) findViewById(R.id.fincScheduledbuyPeriod))
					.setText((String) LocalData.transCycleMap.get(transcycle));
			endflag = intent.getStringExtra(Finc.ENDFLAG);
			TextView finc_scheduledbuy_setEndTime = (TextView)findViewById(R.id.finc_scheduledbuy_setEndTime);
			finc_scheduledbuy_setEndTime.setText((String)LocalData.dsEndFlagMap.get(endflag));
			TextView endName = (TextView)findViewById(R.id.endName);
			TextView endContext = (TextView)findViewById(R.id.endContext);
			switch (Integer.parseInt(endflag)) {
			case 1:
				fundpointenddate = intent.getStringExtra(Finc.FUNDPOINTENDDATE);
				endName.setText(getString(R.string.finc_scheduledbuy_end_time));
				endContext.setText(StringUtil.valueOf1(fundpointenddate));
				break;
			case 2:
				endsum = intent.getStringExtra(Finc.ENDSUM);
				endName.setText(getString(R.string.finc_scheduledbuy_total_deal_count));
				endContext.setText(StringUtil.valueOf1(endsum));
				break;
			case 3:
				fundpointendamount = intent.getStringExtra(Finc.FUNDPOINTENDAMOUNT);
				endName.setText(getString(R.string.finc_scheduledbuy_total_deal_share));
				endContext.setText(StringUtil.parseStringPattern(fundpointendamount, 2));
				endContext.setTextColor(getResources().getColor(R.color.red));
				break;
			default:
				findViewById(R.id.end_ll).setVisibility(View.GONE);
				break;
			}
		}
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
