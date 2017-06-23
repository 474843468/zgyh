package com.chinamworld.bocmbci.biz.finc.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 基金定期定额申请 成功
 * 
 * @author xyl
 * 
 */
public class FundDDAbortSuccessActivity extends FincBaseActivity {
	private static final String TAG = "FundDDAbortSuccessActivity";
	/** 交易序号 */
	private TextView transationIdTv;
	/** 基金交易流水号 */
	private TextView fundSeqTv;
	private TextView successInfoTv;
	private TextView fundCodeTv;
	private TextView tradetypeTv;
	private Button canfirmBtn;

	private String fundCodeStr;
	private String fundNameStr;
	private String fundTransTypeStr;
	private String dayInMonthStr;
	private String transationIdStr;

	private LinearLayout shcheduBuyLayout;
	private LinearLayout shcheduSellLayout;
	private TextView scheduledBuyDayInMonthTv;
	private TextView scheduledSellDayInmounthTv;
	private TextView scheduledSellFlagTv;
	private TextView scheduledSellAmountTv;
	private TextView scheduledBuyAmountTv;
	private String eachAmountStr;
	private String fundSellFlagStr;
	private String newAmountStr;
	private String fundSeqStr;
	private String applyDateStr;
	private TextView applayDateTv;

	private int FUNCTION_FLAG = FLAG_DDABORT;
	/** 币种，定投定赎周期，结束标志，结束日期，*/
	private String currency, dtdsFlag, endFlag, endDate, endSum, endAmt, subDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		initData();
		setTranScycleEndConditon();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.finc_ddabort_success,
				null);
		tabcontent.addView(childView);
		transationIdTv = (TextView) childView
				.findViewById(R.id.finc_transationId_tv);
		fundSeqTv = (TextView) childView.findViewById(R.id.finc_fundseq_tv);
		successInfoTv = (TextView) childView
				.findViewById(R.id.finc_ddabort_success_info);
		fundCodeTv = (TextView) childView.findViewById(R.id.finc_fundcode_tv);
		applayDateTv = (TextView) childView
				.findViewById(R.id.finc_applayDate_tv);
		tradetypeTv = (TextView) childView.findViewById(R.id.finc_tradetype_tv);
		shcheduSellLayout = (LinearLayout) childView
				.findViewById(R.id.schedusell_layout);
		scheduledSellAmountTv = (TextView) childView
				.findViewById(R.id.finc_scheduledsellAmount_tv);
		scheduledSellFlagTv = (TextView) childView
				.findViewById(R.id.finc_sellflag_tv);
		shcheduBuyLayout = (LinearLayout) childView
				.findViewById(R.id.schedubuy_layout);
		scheduledBuyAmountTv = (TextView) childView
				.findViewById(R.id.finc_scheduledbuyAmount_tv);
		scheduledBuyDayInMonthTv = (TextView) childView
				.findViewById(R.id.finc_dayInMonth_tv);
		scheduledSellDayInmounthTv = (TextView) childView
				.findViewById(R.id.finc_scheduselldayInMonth_tv);
		canfirmBtn = (Button) childView.findViewById(R.id.finc_confirm);
		canfirmBtn.setOnClickListener(this);
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		setRightToMainHome();
		TextView finc_myfinc_sell_sellType = (TextView) findViewById(R.id.finc_myfinc_sell_sellType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_myfinc_sell_sellType);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		FUNCTION_FLAG = intent.getIntExtra(ConstantGloble.ISFOREX_FLAG, FLAG_DDABORT);
		fundCodeStr = intent.getStringExtra(Finc.I_FUNDCODE);
		fundNameStr = intent.getStringExtra(Finc.I_FUNDNAME);
		fundTransTypeStr = intent.getStringExtra(Finc.I_TRANSTYPE);
		fundSeqStr = intent.getStringExtra(Finc.I_FINCBUYSQL);
		applyDateStr = intent.getStringExtra(Finc.I_APPLYDATE);
		if (fundTransTypeStr.equals(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY)) {// 定投
			newAmountStr = intent.getStringExtra(Finc.I_AMOUNT);
			dayInMonthStr = intent.getStringExtra(Finc.I_PAYMENTDATESTR);// 注意
																	// 是paymentDate
			// feeTypeStr = extras.getString(Finc.I_FEETYPE);
			scheduBuyView();
		} else {
			eachAmountStr = intent.getStringExtra(Finc.I_AMOUNT);
			dayInMonthStr = intent.getStringExtra(Finc.I_DAYINMONTH);
			fundSellFlagStr = intent.getStringExtra(Finc.I_SELLFLAG);
			scheduSellView();
		}
		fundCodeTv.setText(fundCodeStr + " " + fundNameStr);
		tradetypeTv.setText(LocalData.tradeTypeCodeToStrMap
				.get(fundTransTypeStr));
		fundSeqTv.setText(fundSeqStr);
		applayDateTv.setText(applyDateStr);
		transationIdStr = intent.getStringExtra(Finc.I_TRANSACTIONID);
		transationIdTv.setText(transationIdStr);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm:// 确定
			setResult(RESULT_OK);
			activityTaskManager.removeAllActivity();
			break;
		default:
			break;
		}
	}

	private void scheduBuyView() {
		shcheduBuyLayout.setVisibility(View.VISIBLE);
		shcheduSellLayout.setVisibility(View.GONE);
		scheduledBuyDayInMonthTv.setText(dayInMonthStr);
		scheduledBuyAmountTv.setText(StringUtil.parseStringPattern(
				newAmountStr, 2));
		TextView riskLevelTv = (TextView)findViewById(R.id.finc_risklevel_tv);
		String fundriskLevelStr = getIntent().getExtras().getString(Finc.I_RISKLEVEL);
		riskLevelTv.setText(LocalData.fincRiskLevelCodeToStrFUND
				.get(fundriskLevelStr));
		switch (FUNCTION_FLAG) {
		case FLAG_DDABORT:
			setTitle(R.string.finc_query_dtsq_buy_consern);
			successInfoTv.setText(getString(R.string.finc_fundAbort_sechedubuy_success));
			break;
		case FLAG_PAUSE:
			setTitle(R.string.finc_scheduled_buy_pause);
			successInfoTv.setText(getString(R.string.finc_fundPause_sechedubuy_success));
			break;
		case FLAG_RESUME:
			setTitle(R.string.finc_scheduled_buy_resume);
			successInfoTv.setText(getString(R.string.finc_fundResume_sechedubuy_success));
			break;
		}
	}

	private void scheduSellView() {
		shcheduBuyLayout.setVisibility(View.GONE);
		shcheduSellLayout.setVisibility(View.VISIBLE);
		scheduledSellAmountTv.setText(StringUtil.parseStringPattern(
				eachAmountStr, 2));
		scheduledSellDayInmounthTv.setText(dayInMonthStr);
		scheduledSellFlagTv.setText(LocalData.fundSellFlagCodeToStr
				.get(fundSellFlagStr));
		switch (FUNCTION_FLAG) {
		case FLAG_DDABORT:
			setTitle(R.string.finc_query_dtsq_buy_consern);
			successInfoTv.setText(getString(R.string.finc_fundAbort_sechedusale_success));
			break;
		case FLAG_PAUSE:
			setTitle(R.string.finc_scheduled_buy_pause);
			successInfoTv.setText(getString(R.string.finc_fundPause_sechedusale_success));
			break;
		case FLAG_RESUME:
			setTitle(R.string.finc_scheduled_buy_resume);
			successInfoTv.setText(getString(R.string.finc_fundResume_sechedubuy_success));
			break;
		}
	}
	
	/**
	 * 设置交易周期及结束条件
	 */
	private void setTranScycleEndConditon(){
		dtdsFlag = (String) fincControl.fundScheduQueryMap
				.get(Finc.DTDSFLAG);
		endFlag = (String) fincControl.fundScheduQueryMap.get(Finc.ENDFLAG);
		endDate = (String) fincControl.fundScheduQueryMap
				.get(Finc.FINC_ENDDATE);
		endSum = (String) fincControl.fundScheduQueryMap.get(Finc.ENDSUM);
		endAmt = (String) fincControl.fundScheduQueryMap.get(Finc.ENDAMT);
		subDate = (String) fincControl.fundScheduQueryMap.get(Finc.SUBDATE);
		Map<String, String> map = (Map<String, String>) fincControl.fundScheduQueryMap
				.get(Finc.FINC_FUNDINFO);
		currency = map.get(Finc.FINC_CURRENCY);
		TextView endCondition = (TextView)findViewById(R.id.finc_scheduledbuy_setEndTime); 
		if (fundTransTypeStr.equals(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY)) {// 定投
			endCondition.setText(StringUtil.valueOf1((String)LocalData.dtEndFlagMap.get(endFlag)));
			switch (Integer.parseInt(dtdsFlag)) {
			case 0:
				findViewById(R.id.finc_dayInMonth_ll).setVisibility(View.VISIBLE);
				scheduledBuyDayInMonthTv.setText(StringUtil.valueOf1(dayInMonthStr));
				break;
			case 1:
				TextView fincBuyDayOfWeekTv = (TextView)findViewById(R.id.fincBuyDayOfWeekTv);
				findViewById(R.id.fincBuyDayOfWeekLl).setVisibility(View.VISIBLE);
				fincBuyDayOfWeekTv.setText(getWeekByValue(subDate));
				break;
			}
		} else {
			endCondition.setText(StringUtil.valueOf1((String)LocalData.dsEndFlagMap.get(endFlag)));
			switch (Integer.parseInt(dtdsFlag)) {
			case 0:
				findViewById(R.id.finc_scheduselldayInMonth_ll).setVisibility(View.VISIBLE);
				scheduledSellDayInmounthTv.setText(StringUtil.valueOf1(subDate));
				break;
			case 1:
				TextView fincSaleDayOfWeekTv = (TextView)findViewById(R.id.fincSaleDayOfWeekTv);
				findViewById(R.id.fincSaleDayOfWeekLl).setVisibility(View.VISIBLE);
				fincSaleDayOfWeekTv.setText(getWeekByValue(subDate));
				break;
			}
		}
		
		TextView endName = (TextView)findViewById(R.id.endName);
		TextView endContext = (TextView)findViewById(R.id.endContext);
		switch (Integer.parseInt(endFlag)) {
		case 1:
			endName.setText(getString(R.string.finc_scheduledbuy_end_time));
			endContext.setText(StringUtil.valueOf1(endDate));
			break;
		case 2:
			endName.setText(getString(R.string.finc_scheduledbuy_total_deal_count));
			endContext.setText(StringUtil.valueOf1(endSum));
			break;
		case 3:
			endName.setText(getString(R.string.finc_scheduledbuy_total_deal_amount));
			endContext.setText(StringUtil.parseStringCodePattern(currency, endAmt, 2));
			break;
		default:
			findViewById(R.id.end_ll).setVisibility(View.GONE);
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
