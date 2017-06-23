package com.chinamworld.bocmbci.biz.finc.query;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金在途交易查询 详情页面
 * 
 * @author xyl
 * 
 */
public class FundQueryEffectiveDetailsActivity extends FincBaseActivity {
	private static final String TAG = "FundQueryTodayDetailsActivity";

	/** 委托日期,交易基金,交易币种,交易金额/份额 ,交易类型,交易状态,TA 账号,注册基金公司代码,
	 * 注册基金公司名称,指定日期,转入基金,分红方式 */
	private TextView entrustDateTv, fundCodeTv, currencyTv, transAmountTv,transCountTv,
			transTypeTv, transStateTv, taAccNumTv, regCommpanyCodTv,
			regCommpanyNameTv, appointDateTv, transInFundTv, bonusTypeTv;
	/** 基金名称， 钞汇标志，失败原因， 指定交易日期， 修改分红方式， 转入基金*/
	private TextView fundNameTv, fincCashremitTv, failReasonTv, fincnAppointTradeDateTv,
			fincnModiBoundsTv, fincMyfincThrowThowInTv;
	/**
	 * 委托日期,基金代码,基金名称,交易币种,炒汇,交易金额/份额 ,交易类型,交易状态,TA 账号,注册基金公司代码,注册基金公司名称,是否可撤销
	 */
	private String entrustDate, fundCode, fundName, currency, cashFlag,transAmount,
			transCount, transType, transState, taAccNum, regCommpanyCod,
			regCommpanyName, cancleFlag,appointDate,bonusType,inFundCode,inFundName;
	private String specialTransFlag, fundTranType, transTypeResult, failReason;
	/** 撤单按钮 */
	private Button consernBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		initData();
		initView();
		showViews();
		initListenner();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		View childView = mainInflater.inflate(
				R.layout.finc_entrust_deal_query_detail_2, null);
		tabcontent.addView(childView);
		setTitle(R.string.fincn_query_effective);
		entrustDateTv = (TextView) findViewById(R.id.finc_dealDate_tv);
		fundCodeTv = (TextView) findViewById(R.id.finc_fundcode_tv);
		fundNameTv = (TextView) findViewById(R.id.finc_fundname_tv);
		currencyTv = (TextView) findViewById(R.id.finc_tradecurrency_tv);
		fincCashremitTv = (TextView) findViewById(R.id.finc_cashremit_tv);
		transAmountTv = (TextView) findViewById(R.id.fincn_transAmount_tv);
		transCountTv = (TextView) findViewById(R.id.fincn_transCount_tv);
		transTypeTv = (TextView) findViewById(R.id.finc_tradetype_tv);
		transStateTv = (TextView) findViewById(R.id.finc_fundstate_tv);
//		appointDateTv = (TextView) findViewById(R.id.finc_extraDay_tv);
		transInFundTv = (TextView) findViewById(R.id.finc_myfinc_throw_thow_in_tv);
//		bonusTypeTv = (TextView) findViewById(R.id.finc_myfinc_bounds_tv);
		taAccNumTv = (TextView) findViewById(R.id.fincn_fundTaAccNum_tv);
//		regCommpanyCodTv = (TextView) findViewById(R.id.fincn_regfundCommpanyCode_tv);
		regCommpanyNameTv = (TextView) findViewById(R.id.fincn_regfundCommpanyName_tv);
		failReasonTv = (TextView) findViewById(R.id.fincn_fail_reason_tv);
		fincnAppointTradeDateTv = (TextView) findViewById(R.id.fincn_appoint_trade_date_tv);
		fincnModiBoundsTv = (TextView) findViewById(R.id.fincn_modi_bounds_tv);
		fincMyfincThrowThowInTv = (TextView) findViewById(R.id.finc_myfinc_throw_thow_in_tv);
		consernBtn = (Button) findViewById(R.id.finc_query_today_concel_btn);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				regCommpanyNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transCountTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transInFundTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transStateTv);
		
		setButtonVisible(cancleFlag);
	}

	private void initListenner() {
		consernBtn.setOnClickListener(this);
	}

	private void initData() {
		Map<String, Object> dataMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap().get(Finc.D_ENTRUST_DETAILMAP);
		if (!StringUtil.isNullOrEmpty(dataMap)) {
			entrustDate = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_PAYMENTDATE);
			fundCode = (String) dataMap.get(Finc.QUERYTRANSONTRAN_FUNDCODE);
			fundName = (String) dataMap.get(Finc.QUERYTRANSONTRAN_FUNDNAME);
			currency = (String) dataMap.get(Finc.QUERYTRANSONTRAN_CURRENCYCODE);
			cashFlag = (String) dataMap.get(Finc.QUERYTRANSONTRAN_CASHFLAG);
			transAmount =  (String)dataMap.get(Finc.QUERYTRANSONTRAN_TRANSAMOUNT);
			transCount = (String)dataMap.get(Finc.QUERYTRANSONTRAN_TRANSCOUNT);
			transType = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDTRANTYPE);
			transState = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_TRANSSTATUS);
			taAccNum = (String) dataMap.get(Finc.QUERYTRANSONTRAN_TAACCOUNTNO);
			failReason = (String) dataMap.get(Finc.QUERYHISTORY401_FAILREASON);
			regCommpanyCod = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDREGCODE);
			regCommpanyName = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDREGNAME);
			cancleFlag = (String) dataMap.get(Finc.QUERYTRANSONTRAN_CANCLEFLAG);
			appointDate = (String) dataMap.get(Finc.QUERYTRANSONTRAN_APPOINTDATE);
			bonusType = (String) dataMap.get(Finc.QUERYTRANSONTRAN_BONUSTYPE);
			inFundCode = (String) dataMap.get(Finc.QUERYTRANSONTRAN_INFUNDCODE);
			inFundName = (String) dataMap.get(Finc.QUERYTRANSONTRAN_INFUNDNAME);
			
			specialTransFlag = (String) dataMap.get(Finc.QUERYHISTORY401_SPECIALTRANSFLAG);
			fundTranType = (String) dataMap.get(Finc.FINC_FUNDTODAYQUERY_FUNDTRANTYPE);
			transTypeResult = FincControl.parseSpeCodeTradeTypeEffective(specialTransFlag, fundTranType);
			FincControl.transTypeResult = transTypeResult;
		}
	}
	
	private void showViews(){
		if(StringUtil.isContain(transTypeResult, "TA账户")){
			((LinearLayout)findViewById(R.id.fincn_fundTaAccNum_ll))
				.setVisibility(View.VISIBLE);
			setViewDataTa();
			if(transTypeResult.equals("登记基金TA账户")){
				((LinearLayout)findViewById(R.id.fincn_regfundCommpanyName_ll))
				.setVisibility(View.VISIBLE);
				regCommpanyNameTv.setText(StringUtil.valueOf1(regCommpanyName));
			}
		}else{
			((LinearLayout)findViewById(R.id.common_1)).setVisibility(View.VISIBLE);
			((LinearLayout)findViewById(R.id.common_2)).setVisibility(View.VISIBLE);
			setViewDataElse();
		}
	}
	
	/**
	 * @author fsm
	 * 给控件初始化数据   Ta账户类型
	 */
	private void setViewDataTa(){
		taAccNumTv.setText(StringUtil.valueOf1(taAccNum));
		entrustDateTv.setText(StringUtil.valueOf1(entrustDate));
		transTypeTv.setText(StringUtil.valueOf1(transTypeResult));
//		transStateTv.setText(StringUtil.valueOf1(LocalData.fincHistoryStatusTypeCodeToStr
//				.get(transState)));
		transStateTv.setText(StringUtil.valueOf1(transState));
	}
	
	/**
	 * @author fsm
	 * 给控件初始化数据  普通 类型
	 */
	private void setViewDataElse(){
		entrustDateTv.setText(StringUtil.valueOf1(entrustDate));
		fundCodeTv.setText(StringUtil.valueOf1(fundCode));
		fundNameTv.setText(StringUtil.valueOf1(fundName));
		currencyTv.setText(FincControl.fincCurrencyAndCashFlag(currency, cashFlag));
		
		transTypeTv.setText(transTypeResult);
		transAmountTv.setText(StringUtil.parseStringCodePattern(currency, transAmount, 2));
		transCountTv.setText(StringUtil.parseStringPattern(transCount, 2));
		transStateTv.setText(StringUtil.valueOf1(transState));
		if(transState.equals("2")){
			((LinearLayout)findViewById(R.id.fincn_fail_reason_ll)).setVisibility(View.VISIBLE);
			failReasonTv.setText(StringUtil.valueOf1(failReason));
		}
		if(transTypeResult != null && 
				(transTypeResult.equals("指定日期申购") || transTypeResult.equals("指定日期赎回")
						|| transTypeResult.equals("指定日期认购"))){
			((LinearLayout)findViewById(R.id.fincn_appoint_trade_date_ll)).setVisibility(View.VISIBLE);
			fincnAppointTradeDateTv.setText(StringUtil.valueOf1(appointDate));
		}
		if(transTypeResult != null && 
				(transTypeResult.equals("设置分红方式"))){
			((LinearLayout)findViewById(R.id.fincn_modi_bounds_ll)).setVisibility(View.VISIBLE);
			fincnModiBoundsTv.setText(StringUtil.valueOf1(LocalData.bonusTypeMap.get(bonusType)));
		}
		if(transTypeResult != null && 
				(transTypeResult.equals("基金转换"))){
			((LinearLayout)findViewById(R.id.finc_myfinc_throw_thow_in_ll)).setVisibility(View.VISIBLE);
			fincMyfincThrowThowInTv.setText(inFundName);
		}
	}

	/**
	 * 根据是否可撤销显示撤销按钮
	 * 
	 * @param cancleFlag
	 */
	private void setButtonVisible(String cancleFlag) {
		if (specialTransFlag != null && (specialTransFlag.equals("0") 
				|| specialTransFlag.equals("1") || specialTransFlag.equals("2"))
				&& StringUtil.parseStrToBoolean(cancleFlag)) {
			consernBtn.setVisibility(View.VISIBLE);
		} else {
			consernBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_query_today_concel_btn:// 撤销按钮
			Intent intent = new Intent();
			intent.setClass(this, FundQueryEffectiveConcernActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void finish() {
		BaseDroidApp.getInstanse().getBizDataMap()
				.remove(Finc.D_ENTRUST_DETAILMAP);
		super.finish();
	}
	
}
