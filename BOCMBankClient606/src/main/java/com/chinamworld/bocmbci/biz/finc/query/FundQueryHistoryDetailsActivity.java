package com.chinamworld.bocmbci.biz.finc.query;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
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
 * 历史交易查询 详情页面
 * 
 * @author xyl
 * 
 */
public class FundQueryHistoryDetailsActivity extends FincBaseActivity {
	private static final String TAG = "FundQueryHistoryDetailsActivity";
	/**
	 * 基金代码,基金名称,交易币种,交易金额/份额,单位净值,交易费用,交易日期,交易状态,交易类型,TA
	 * 账号,注册基金公司代码,注册基金公司名称,指定日期,转入基金,分红方式
	 */
	private TextView fundCodeTv, fundNameTv, currencyTv,transAmountTv, 
	transCountTv,netPriceTv, transFeeTv, dealDateTv, transStateTv, transTypeTv,
	taAccNumTv, regCommpanyCodTv, regCommpanyNameTv, appointDateTv,
	transInFundTv, bonusTypeTv,failReasonTv, fincCashremitTv, 
	fincnAppointTradeDateTv, fincnModiBoundsTv, fincMyfincThrowThowInTv;
	/**
	 * 基金代码,基金名称,交易币种,交易金额/份额,单位净值,交易费用,交易日期,交易状态,交易状态,交易类型,TA
	 * 账号,注册基金公司代码,注册基金公司名称,指定日期,转入基金,分红方式,备注(失败原因)
	 */
	private String fundCode, fundName, currency, cashFlag, transAmount,transCount,
	netPrice, transFee, dealDate, transState, transType, taAccNum,
	regCommpanyCod, regCommpanyName, appointDate, bonusType,
	inFundCode, inFundName,failReason,cashRemit,
	applyCount,applyAmount,isCancle,// 交易金额 和交易份额 为0时 使用applyCount,applyAmount，   isCancle错误判断的附加条件
	confirmAmount,confirmCount;
	private String specialTransFlag, transCode, transTypeResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		init();
		showViews();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Map<String, Object> map = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap().get(Finc.D_HISTORY_DETAIL);
		fundCode = (String) map.get(Finc.FINC_FUNDCODE);
		fundName = (String) map.get(Finc.FINC_FUNDNAME);
		currency = (String) map.get(Finc.QUERYHISTORY401_CURRENCYCODE);
		cashFlag = (String) map.get(Finc.QUERYHISTORY401_CASHFLAG);
		transAmount = (String) map.get(Finc.QUERYHISTORY401_CONFIRMAMOUNT);
		applyAmount = (String) map.get(Finc.I_APPLYAMOUNT);
		transCount =(String) map.get(Finc.QUERYHISTORY401_CONFIRMCOUNT) ;
		applyCount = (String) map.get(Finc.QUERYHISTORY401_APPLYCOUNT) ;
		netPrice = (String) map.get(Finc.QUERYHISTORY401_NETVALUE);
		transFee = (String) map.get(Finc.QUERYHISTORY401_TRANSFEE);
		dealDate = (String) map.get(Finc.QUERYHISTORY401_TRANSDATE);
		transState = (String) map.get(Finc.QUERYHISTORY401_TRANSSTATUS);
		taAccNum = (String) map.get(Finc.QUERYHISTORY401_TAACCOUNT);
		failReason = (String) map.get(Finc.QUERYHISTORY401_FAILREASON);
		isCancle = (String) map.get(Finc.QUERYHISTORY401_ISCANCLE);
		regCommpanyCod = (String) map
				.get(Finc.QUERYHISTORY401_REGCODE);
		regCommpanyName = (String) map
				.get(Finc.QUERYHISTORY401_REGNAME);
		appointDate = (String) map.get(Finc.QUERYTRANSONTRAN_APPOINTDATE);
		bonusType = (String) map.get(Finc.QUERYTRANSONTRAN_BONUSTYPE);
		inFundCode = (String) map.get(Finc.QUERYHISTORY401_INFNCODE);
		inFundName = (String) map.get(Finc.QUERYHISTORY401_INFNNAME);
		transType = (String) map.get(Finc.QUERYHISTORY401_TRANSCODE);
		cashRemit = (String) map.get(Finc.I_CASHFLAG);
		confirmAmount = (String) map.get(Finc.QUERYHISTORY401_CONFIRMAMOUNT);
		confirmCount = (String) map.get(Finc.QUERYHISTORY401_CONFIRMCOUNT);
		specialTransFlag = (String) map.get(Finc.QUERYHISTORY401_SPECIALTRANSFLAG);//add by fsm
		transCode = (String) map.get(Finc.QUERYHISTORY401_TRANSCODE);
		transTypeResult = FincControl.parseSpeCodeTradeTypeHistory(specialTransFlag, transCode);
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		View childView = mainInflater.inflate(
				R.layout.finc_query_history_details_2, null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_query_history);
		initRightBtnForMain();

		fundCodeTv = (TextView) findViewById(R.id.finc_fundcode_tv);
		fundNameTv = (TextView) findViewById(R.id.finc_fundname_tv);
		currencyTv = (TextView) findViewById(R.id.finc_tradecurrency_tv);
		//交易份额
		transCountTv = (TextView) findViewById(R.id.fincn_transCount_tv);
		//交易金额
		transAmountTv = (TextView) findViewById(R.id.fincn_transAmount_tv);
		netPriceTv = (TextView) findViewById(R.id.finc_myfinc_netPrice_tv);
		transFeeTv = (TextView) findViewById(R.id.fincn_transFee_tv);
		dealDateTv = (TextView) findViewById(R.id.finc_dealDate_tv);
		transStateTv = (TextView) findViewById(R.id.finc_fundstate_tv);
		transTypeTv = (TextView) findViewById(R.id.finc_tradetype_tv);
		//		appointDateTv = (TextView) findViewById(R.id.finc_extraDay_tv);
		transInFundTv = (TextView) findViewById(R.id.finc_myfinc_throw_thow_in_tv);
		//		bonusTypeTv = (TextView) findViewById(R.id.finc_myfinc_bounds_tv);
		taAccNumTv = (TextView) findViewById(R.id.fincn_fundTaAccNum_tv);
		//		regCommpanyCodTv = (TextView) findViewById(R.id.fincn_regfundCommpanyCode_tv);
		regCommpanyNameTv = (TextView) findViewById(R.id.fincn_regfundCommpanyName_tv);
		failReasonTv = (TextView) findViewById(R.id.fincn_fail_reason_tv);
		fincCashremitTv = (TextView) findViewById(R.id.finc_cashremit_tv);
		fincnAppointTradeDateTv = (TextView) findViewById(R.id.fincn_appoint_trade_date_tv);
		fincnModiBoundsTv = (TextView) findViewById(R.id.fincn_modi_bounds_tv);
		fincMyfincThrowThowInTv = (TextView) findViewById(R.id.finc_myfinc_throw_thow_in_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				regCommpanyNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundNameTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				failReasonTv);
		showViews();
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
		dealDateTv.setText(dealDate);
		transTypeTv.setText(transTypeResult);
		transStateTv.setText(LocalData.fincHistoryStatusTypeCodeToStr
				.get(transState));
	}

	/**
	 * @author fsm
	 * 给控件初始化数据  普通 类型
	 */
	private void setViewDataElse(){
		dealDateTv.setText(dealDate);
		fundCodeTv.setText(fundCode);
		fundNameTv.setText(fundName);
		currencyTv.setText(FincControl.fincCurrencyAndCashFlag(currency, cashRemit));
		transTypeTv.setText(transTypeResult);

		if(!"0".equals(specialTransFlag) || "1".equals(isCancle) ){
			transAmountTv.setText(StringUtil.parseStringCodePattern(currency, applyAmount, 2));
		}else{
			transAmountTv.setText(StringUtil.parseStringCodePattern(currency, confirmAmount, 2));	
		}
		if(!"0".equals(specialTransFlag) || "1".equals(isCancle) ){
			transCountTv.setText(StringUtil.parseStringPattern(applyCount, 2));
		}else{
			transCountTv.setText(StringUtil.parseStringPattern(confirmCount, 2));
		}
		netPriceTv.setText(StringUtil.parseStringPattern(netPrice, 4));//单位净值
		transFeeTv.setText(StringUtil.parseStringCodePattern(currency, transFee, 2));
		transStateTv.setText(StringUtil.valueOf1(LocalData.fincHistoryStatusTypeCodeToStr
				.get(transState)));

		if("2".equals(transState) && "1".equals(isCancle)){
				((LinearLayout)findViewById(R.id.fincn_fail_reason_ll)).setVisibility(View.VISIBLE);
				failReasonTv.setText(getResources().getString(R.string.finc_repeal));
		}else{
				((LinearLayout)findViewById(R.id.fincn_fail_reason_ll)).setVisibility(View.VISIBLE);
				failReasonTv.setText(StringUtil.valueOf1(failReason));
		}
		if(transTypeResult != null && 
				(transTypeResult.equals("指定日期申购") || transTypeResult.equals("指定日期赎回"))){
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
			fincMyfincThrowThowInTv.setText(FincControl.connFundCodeAndeName(inFundCode, inFundName));
		}
		//		transInFundTv.setText(FincControl.connFundCodeAndeName(inFundCode, inFundName));
	}

}
