package com.chinamworld.bocmbci.biz.finc.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金定期定额申购修改成功页面
 * 
 * 
 */
public class FundSecheduBuyModifySuccessActivity extends FincBaseActivity {

	/** 基金代码Tv */
	private TextView fundCodeTextView;
	private TextView fundNameTextView;
	/** 收费方式Tv */
	private TextView feeTypeTextView;
	/** 交易币种Tv */
	private TextView tradeCurrencyTextView;
	/** 每月申购日期 Tv */
	private TextView saleDayOfMonthTextView;
	/**
	 * 申购金额 Tv
	 */
	private TextView saleAmountTextView;
	/** 资金账户 TV */
	private TextView invAccNumTv;
	/** 基金交易账户 TV */
	private TextView invAccIdTv;
	/** 交易序号 TV */
	private TextView transIdTv;
	/** 基金交易流水号TV */
	private TextView fundSeqTv;

	/**
	 * 下一步按钮
	 */
	private Button confirmBtn;
	// 页面显示的值
	private String fundCodeStr;
	private String fundNameStr;
	private String feeTypeStr;
	private String tradeCurrencyStr,cashFlagCode;
	private String dayInMonthStr;
	/**
	 * 申购金额
	 */
	private String saleAmoundStr;
	private String transIdStr;
	private String fundSeqStr;

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
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childview = mainInflater.inflate(
				R.layout.finc_trade_scheduledbuy_success, null);
		tabcontent.addView(childview);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForDingTou());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_scheduedit_buy);
		transIdTv = (TextView) childview
				.findViewById(R.id.finc_transIdTv_TextView);
		invAccNumTv = (TextView) childview
				.findViewById(R.id.finc_invAccNum_TextView);
		invAccIdTv = (TextView) childview
				.findViewById(R.id.finc_invaccId_TextView);
		fundSeqTv = (TextView) childview
				.findViewById(R.id.finc_fundSeq_TextView);
		fundCodeTextView = (TextView) childview
				.findViewById(R.id.finc_fundCode_textview);
		fundNameTextView = (TextView) childview
				.findViewById(R.id.finc_fundName_textview);
		feeTypeTextView = (TextView) childview
				.findViewById(R.id.finc_feetype_textView);
		tradeCurrencyTextView = (TextView) childview
				.findViewById(R.id.finc_tradecurrency_textView);
		saleDayOfMonthTextView = (TextView) childview
				.findViewById(R.id.finc_dayInMonth_TextView);
		saleAmountTextView = (TextView) childview
				.findViewById(R.id.finc_scheduledbuyAmount_TextView);
		confirmBtn = (Button) childview.findViewById(R.id.finc_confirm);
		confirmBtn.setOnClickListener(this);
		initRightBtnForMain();
		back.setVisibility(View.GONE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, transIdTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, invAccNumTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, invAccIdTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundSeqTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundNameTextView);
	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		initP405ModiData(extra);
		fundCodeStr = extra.getString(Finc.I_FUNDCODE);
		fundNameStr = extra.getString(Finc.I_FUNDNAME);
		feeTypeStr = extra.getString(Finc.I_FEETYPE);
		tradeCurrencyStr = extra.getString(Finc.I_CURRENCYCODE);
		cashFlagCode = extra.getString(Finc.I_CASHFLAG);
		dayInMonthStr = extra.getString(Finc.I_DAYINMOUNTH);
		saleAmoundStr = extra.getString(Finc.I_SALEAMOUNT);
		transIdStr = extra.getString(Finc.I_TRANSACTIONID);
		fundSeqStr = extra.getString(Finc.I_FINCBUYSQL);

		transIdTv.setText(transIdStr);
		invAccNumTv.setText(StringUtil.getForSixForString(fincControl.accNum));
		invAccIdTv.setText(fincControl.invAccId);
		fundSeqTv.setText(fundSeqStr);
		fundCodeTextView.setText(fundCodeStr);
		fundNameTextView.setText(fundNameStr); 
		feeTypeTextView.setText(LocalData.fundfeeTypeCodeToStr.get(feeTypeStr));
		tradeCurrencyTextView.setText(FincControl.fincCurrencyAndCashFlag(tradeCurrencyStr, cashFlagCode));
		saleDayOfMonthTextView.setText(dayInMonthStr);
		saleAmountTextView.setText(StringUtil.parseStringCodePattern(
				tradeCurrencyStr, saleAmoundStr, 2));

	}
	
	/**
	 * 405定投优化  增加初始化数据
	 * 添加选择定投期 ，增加周定投 ，增加结束条件
	 */
	private void initP405ModiData(Bundle extra){
		if(extra != null){
			transcycle = extra.getString(Finc.TRANSCYCLE);
			switch (Integer.parseInt(transcycle)) {
			case 0:
				findViewById(R.id.finc_dayInMonth_ll).setVisibility(View.VISIBLE);
				saleDayOfMonthTextView.setText(StringUtil.valueOf1(dayInMonthStr));
				break;
			case 1:
				TextView fincSaleDayOfWeekTv = (TextView)findViewById(R.id.fincSaleDayOfWeekTv);
				findViewById(R.id.fincSaleDayOfWeekLl).setVisibility(View.VISIBLE);
				paymentdateofweek = extra.getString(Finc.PAYMENTDATEOFWEEK);
				fincSaleDayOfWeekTv.setText(getWeekByValue(paymentdateofweek));
				break;
			}
			((TextView) findViewById(R.id.fincScheduledbuyPeriod))
					.setText((String) LocalData.transCycleMap.get(transcycle));
			endflag = extra.getString(Finc.ENDFLAG);
			TextView finc_scheduledbuy_setEndTime = (TextView)findViewById(R.id.finc_scheduledbuy_setEndTime);
			finc_scheduledbuy_setEndTime.setText((String)LocalData.dtEndFlagMap.get(endflag));
			TextView endName = (TextView)findViewById(R.id.endName);
			TextView endContext = (TextView)findViewById(R.id.endContext);
			switch (Integer.parseInt(endflag)) {
			case 1:
				fundpointenddate = extra.getString(Finc.FUNDPOINTENDDATE);
				endName.setText(getString(R.string.finc_scheduledbuy_end_time));
				endContext.setText(StringUtil.valueOf1(fundpointenddate));
				break;
			case 2:
				endsum = extra.getString(Finc.ENDSUM);
				endName.setText(getString(R.string.finc_scheduledbuy_total_deal_count));
				endContext.setText(StringUtil.valueOf1(endsum));
				break;
			case 3:
				fundpointendamount = extra.getString(Finc.FUNDPOINTENDAMOUNT);
				endName.setText(getString(R.string.finc_scheduledbuy_total_deal_amount));
				endContext.setText(StringUtil.parseStringCodePattern(tradeCurrencyStr, fundpointendamount, 2));
				endContext.setTextColor(getResources().getColor(R.color.red));
				break;
			default:
				findViewById(R.id.end_ll).setVisibility(View.GONE);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm:// 确定
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
