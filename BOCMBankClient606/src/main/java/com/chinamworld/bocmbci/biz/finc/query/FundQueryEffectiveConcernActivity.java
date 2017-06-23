package com.chinamworld.bocmbci.biz.finc.query;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金在途交易查询 撤单确认页
 * 
 * @author xyl
 * 
 */
public class FundQueryEffectiveConcernActivity extends FincBaseActivity {
	private static final String TAG = "FundQueryEffectiveConcernActivity";
	
	/** 基金交易流水号,基金代码,基金名称,交易币种,交易金额/份额 ,交易类型,交易状态,TA 账号,
	 * 注册基金公司代码,注册基金公司名称,转入基金 */
	private TextView fundSeqTv, fundCodeTv, currencyTv, transCountTv,
			transTypeTv, transStateTv, taAccNumTv, regCommpanyCodTv,
			regCommpanyNameTv, appointDateTv, transInFundTv, bonusTypeTv, 
			fincEntrustDateTv, fincCashremitTv, fincTradeamountTv, fincTradenumTv,fincMyfincThrowThowInTv;
	/**
	 * 基金交易流水号,基金代码,基金名称,交易币种,炒汇,交易金额/份额 ,交易类型,交易状态,TA
	 * 账号,注册基金公司代码,注册基金公司名称,是否可撤销 特殊交易标志,指定交易日期,委托日期(普通交易撤单时上送),夜间交易标志
	 */
	private String fundSeq, fundCode, fundName, currency, cashFlag, transCount,
			transType, transState, taAccNum, regCommpanyCod, regCommpanyName,
			cancleFlag, appointDate, bonusType, inFundCode, inFundName,
			specialTransFlag, paymentDate, nightFlag;
	/** 交易金额*/
	private String transAmount;
	/** 撤单按钮 */
	private Button consernBtn;
	/** 判断是调用哪个撤单接口 */
	private int flag;
	/** 当日交易撤单,指定日期撤单,定期定额撤单 */
	private static final int CURRENTDAY = 1, APPOINTDAY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		settingbaseinit();
		initView();
		initListenner();
		initData();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		View childView = mainInflater.inflate(
				R.layout.finc_entrust_deal_consern, null);
		tabcontent.addView(childView);
		setTitle(R.string.fincn_effective_consern);
		fundSeqTv = (TextView) findViewById(R.id.finc_fundsql_tv);
		fundCodeTv = (TextView) findViewById(R.id.finc_myfinc_fincCode_tv);
		currencyTv = (TextView) findViewById(R.id.finc_tradecurrency_tv);
		transCountTv = (TextView) findViewById(R.id.finc_tradenum_tv);
		transTypeTv = (TextView) findViewById(R.id.finc_tradetype_tv);
		appointDateTv = (TextView) findViewById(R.id.finc_extraDay_tv);
		fincEntrustDateTv = (TextView) findViewById(R.id.finc_entrust_date_tv);
		fincCashremitTv = (TextView) findViewById(R.id.finc_cashremit_tv);
		fincTradeamountTv = (TextView) findViewById(R.id.finc_tradeamount_tv);
		fincTradenumTv = (TextView) findViewById(R.id.finc_tradenum_tv);
		fincMyfincThrowThowInTv = (TextView) findViewById(R.id.finc_myfinc_throw_thow_in_tv);
		consernBtn = (Button) findViewById(R.id.finc_query_today_concel_btn);
		right.setText(getString(R.string.switch_off));
		right.setVisibility(View.VISIBLE);//add by fsm 添加撤单关闭按钮
		right.setOnClickListener(this);
		back.setVisibility(View.GONE);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincMyfincThrowThowInTv);
	}

	private void initListenner() {
		consernBtn.setOnClickListener(this);
		right.setOnClickListener(this);
	}

	private void initData() {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap().get(Finc.D_ENTRUST_DETAILMAP);
		if (!StringUtil.isNullOrEmpty(dataMap)) {
			fundSeq = (String) dataMap.get(Finc.QUERYTRANSONTRAN_ORIGNFUNDSEQ);
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
			regCommpanyCod = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDREGCODE);
			regCommpanyName = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_FUNDREGNAME);
			cancleFlag = (String) dataMap.get(Finc.QUERYTRANSONTRAN_CANCLEFLAG);
			bonusType = (String) dataMap.get(Finc.QUERYTRANSONTRAN_BONUSTYPE);
			inFundCode = (String) dataMap.get(Finc.QUERYTRANSONTRAN_INFUNDCODE);
			inFundName = (String) dataMap.get(Finc.QUERYTRANSONTRAN_INFUNDNAME);
			specialTransFlag = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_SPECIALTRANSFLAG);
			appointDate = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_APPOINTDATE);
			paymentDate = (String) dataMap
					.get(Finc.QUERYTRANSONTRAN_PAYMENTDATE);
		}
		setButtonVisible(cancleFlag);
		setFlagValue(specialTransFlag);
		fundSeqTv.setText(StringUtil.valueOf1(fundSeq));
		fundCodeTv.setText(FincControl.connFundCodeAndeName(fundCode, fundName));
		fincEntrustDateTv.setText(StringUtil.valueOf1(paymentDate));
		if(FincControl.transTypeResult.equals("指定日期申购") || 
				FincControl.transTypeResult.equals("指定日期赎回")){
			((LinearLayout)findViewById(R.id.finc_extraDay_ll)).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.finc_extraDay_tv)).setText(appointDate);
		}
		currencyTv.setText(FincControl.fincCurrencyAndCashFlag(currency,cashFlag));
		fincTradeamountTv.setText(StringUtil.parseStringCodePattern(currency, transAmount, 2));
		transCountTv.setText(StringUtil.parseStringPattern(transCount, 2));
		transTypeTv.setText(FincControl.transTypeResult);
		if(FincControl.transTypeResult != null && 
				(FincControl.transTypeResult.equals("基金转换"))){
			((LinearLayout)findViewById(R.id.finc_myfinc_throw_thow_in_ll)).setVisibility(View.VISIBLE);
			fincMyfincThrowThowInTv.setText(inFundName);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundSeqTv);
	}

	/**
	 * 根据特殊交易标志 设定flag 的值
	 * 
	 * @param specialTransFlag
	 */
	private void setFlagValue(String specialTransFlag) {
		if (specialTransFlag != null && specialTransFlag.equals(Finc.C_SPECIALTRANSFLAG_APPOINT)) {
			flag = APPOINTDAY;// 指定日期撤单
		} else {
			flag = CURRENTDAY;// 当日交易撤单
			if (specialTransFlag.equals(Finc.C_SPECIALTRANSFLAG_NIGHT)) {
				nightFlag = "true";
			} else {
				nightFlag = "false";
			}
		}
	}

	/**
	 * 根据是否可撤销显示撤销按钮
	 * 
	 * @param cancleFlag
	 */
	private void setButtonVisible(String cancleFlag) {
		if (StringUtil.parseStrToBoolean(cancleFlag)) {
			consernBtn.setVisibility(View.VISIBLE);
		} else {
			consernBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		switch (flag) {
		case APPOINTDAY:
			fundAppointCancel(fundSeq, transType, fundCode, appointDate,
					tokenId);
			break;
		case CURRENTDAY:
			fundConsernDeal(fundSeq, transCount, transType, fundCode,
					paymentDate, nightFlag, tokenId);
			break;
		}
	}

	@Override
	public void fundAppointCancelCallback(Object resultObj) {
		super.fundAppointCancelCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String newFundSeq = (String) biiResponseBody.getResult();
//		String transactionId = (String) map.get(Finc.I_TRANSACTIONID);
		Intent intent = new Intent();
		intent.setClass(this, FundQueryEffectiveConcernSuccessActivity.class);
		intent.putExtra(Finc.I_TRANSACTIONID, newFundSeq);
		intent.putExtra(Finc.I_FINCBUYSQL, fundSeq);
		startActivityForResult(intent, 1);

	}

	@Override
	public void fundConsernDealCallback(Object resultObj) {
		super.fundConsernDealCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();
		String newFundSeq = (String) map.get(Finc.I_FINCBUYSQL);
		String transactionId = (String) map.get(Finc.I_TRANSACTIONID);
		Intent intent = new Intent();
		intent.setClass(this, FundQueryEffectiveConcernSuccessActivity.class);
		intent.putExtra(Finc.I_FINCBUYSQL, newFundSeq);
		intent.putExtra(Finc.I_TRANSACTIONID, transactionId);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_query_today_concel_btn:// 撤销按钮
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;
		case R.id.ib_top_right_btn:
			finish();
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
		super.finish();
	}
}
