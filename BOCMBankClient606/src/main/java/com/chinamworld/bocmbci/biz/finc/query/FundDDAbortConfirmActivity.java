package com.chinamworld.bocmbci.biz.finc.query;

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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 基金定期定额 撤销、暂停、恢复
 * 
 * @author xyl
 * 
 */
public class FundDDAbortConfirmActivity extends FincBaseActivity {
	private static final String TAG = "FundDDAbortConfirmActivity";
	private TextView fundCodeTv;
	private TextView confirmInfoTv;
	private TextView tradetypeTv;
	private TextView fundSeqTv;
	private TextView applayDateTv;
	private Button canfirmBtn;

	private String fundCodeStr;
	private String fundNameStr;
	private String fundTransTypeStr;
	private String dayInMonthStr;
	private String transationIdStr;
	private String fundSqlStr;
	private String applyDateStr;
	private String newAmountStr;
	private String eachAmountStr;
	private String fundSellFlagStr;

	private LinearLayout shcheduBuyLayout;
	private LinearLayout shcheduSellLayout;

	private TextView scheduledBuyDayInMonthTv;
	private TextView scheduledSellDayInmounthTv;
	private TextView scheduledSellFlagTv;
	private TextView scheduledSellAmountTv;
	private TextView scheduledBuyAmountTv,feeTypeTv;
	
	
	private String feeTypeStr;
//	private TextView fundCodePreTv;
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
		View childView = mainInflater.inflate(R.layout.finc_ddabort_confirm,
				null);
		tabcontent.addView(childView);
		fundCodeTv = (TextView) childView.findViewById(R.id.finc_fundcode_tv);
		confirmInfoTv = (TextView) childView.findViewById(R.id.finc_ddabort_confirm_tv);
		tradetypeTv = (TextView) childView.findViewById(R.id.finc_tradetype_tv);
		fundSeqTv = (TextView) childView.findViewById(R.id.finc_transationId_tv);
		applayDateTv = (TextView) childView.findViewById(R.id.finc_applayDate_tv);
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
		initRightBtnForMain();
		back.setVisibility(View.GONE);
		backImage.setVisibility(View.GONE);
		right.setText(getString(R.string.switch_off));
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		
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
		fundSqlStr = intent.getStringExtra(Finc.I_FINCBUYSQL);
		applyDateStr = intent.getStringExtra(Finc.I_APPLYDATE);
		
		if (fundTransTypeStr.equals(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY)) {// 定投
			newAmountStr = intent.getStringExtra(Finc.I_AMOUNT);
			dayInMonthStr = intent.getStringExtra(Finc.I_PAYMENTDATESTR);//注意  是paymentDate
			feeTypeStr = intent.getStringExtra(Finc.I_FEETYPE);
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
		fundSeqTv.setText(fundSqlStr);
		applayDateTv.setText(applyDateStr);
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_confirm:// 确定
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
			break;
		case R.id.ib_top_right_btn:
			finish();
			break;
		default:
			break;
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
		String token = (String) biiResponseBody.getResult();
		switch (FUNCTION_FLAG) {
		case FLAG_DDABORT://撤消
			if (fundTransTypeStr.equals(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY)) {// 定投
				fundDdAbort(fundCodeStr, applyDateStr, fundSqlStr, token);
			}else{
//				fundScheduSellAbort(fundCodeStr, eachAmountStr, token, dayInMonthStr);
				fundScheduSellAbort(fundCodeStr, applyDateStr, token, fundSqlStr);
			}
			break;
		default://暂停、恢复
			if (fundTransTypeStr
					.equals(ConstantGloble.FINC_TRANSTYPE_SECHEDUBUY)) {// 定投
				requestPsnFundScheduledBuyPauseResume(fundCodeStr,
						applyDateStr, fundSqlStr, String.valueOf(FUNCTION_FLAG) , token);
			} else {
				requestPsnFundScheduledSellPauseResume(fundCodeStr,
						applyDateStr, fundSqlStr, String.valueOf(FUNCTION_FLAG), token);
			}
			break;
		}
	}
	@Override
	public void fundScheduSellAbortCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		transationIdStr = (String)map.get(Finc.I_TRANSACTIONID);
		fundSqlStr = (String)map.get(Finc.FINC_FUNDSEQ_REQ);
		startResultActivity();
	}
	@Override
	public void fundDdAbortCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		transationIdStr = (String)map.get(Finc.I_TRANSACTIONID);
		fundSqlStr = (String)map.get(Finc.FINC_FUNDSEQ_REQ);
		startResultActivity();
	}
	
	@Override
	public void fundScheduledBuyPauseResumeCallBack(Object resultObj) {
		super.fundScheduledBuyPauseResumeCallBack(resultObj);
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		transationIdStr = (String)map.get(Finc.I_TRANSACTIONID);
		fundSqlStr = (String)map.get(Finc.FINC_FUNDSEQ_REQ);
		startResultActivity();
	}
	
	@Override
	public void fundScheduledSellPauseResumeCallBack(Object resultObj) {
		super.fundScheduledSellPauseResumeCallBack(resultObj);
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		transationIdStr = (String)map.get(Finc.I_TRANSACTIONID);
		fundSqlStr = (String)map.get(Finc.FINC_FUNDSEQ_REQ);
		startResultActivity();
	}
	
	private void startResultActivity(){
		BaseHttpEngine.dissMissProgressDialog();
		Intent  intent = getIntent();
		intent.setClass(this, FundDDAbortSuccessActivity.class);
		intent.putExtra(Finc.I_TRANSACTIONID, transationIdStr);
		intent.putExtra(Finc.FINC_FUNDSEQ_REQ, fundSqlStr);
		startActivityForResult(intent, 1);
	}
	
	private void scheduBuyView() {
		shcheduBuyLayout.setVisibility(View.VISIBLE);
		shcheduSellLayout.setVisibility(View.GONE);
		scheduledBuyDayInMonthTv.setText(dayInMonthStr);
		scheduledBuyAmountTv.setText(StringUtil.parseStringPattern(
				newAmountStr, 2));
		switch (FUNCTION_FLAG) {
		case FLAG_DDABORT:
			setTitle(R.string.finc_query_dtsq_buy_consern);
			confirmInfoTv.setText(getString(R.string.finc_fundAbort_sechedubuy_confirm));
			break;
		case FLAG_PAUSE:
			setTitle(R.string.finc_scheduled_buy_pause);
			confirmInfoTv.setText(getString(R.string.finc_fundPause_sechedubuy_confirm));
			break;
		case FLAG_RESUME:
			setTitle(R.string.finc_scheduled_buy_resume);
			confirmInfoTv.setText(getString(R.string.finc_fundResume_sechedubuy_confirm));
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
			setTitle(R.string.finc_query_dtsq_sale_consern);
			confirmInfoTv.setText(getString(R.string.finc_fundAbort_secheduseal_confirm));
			break;
		case FLAG_PAUSE:
			setTitle(R.string.finc_scheduled_sale_pause);
			confirmInfoTv.setText(getString(R.string.finc_fundPause_secheduseal_confirm));
			break;
		case FLAG_RESUME:
			setTitle(R.string.finc_scheduled_sale_resume);
			confirmInfoTv.setText(getString(R.string.finc_fundResume_secheduseal_confirm));
			break;
		}
		
	}
	
	/**
	 * 设置提示信息,交易周期及结束条件
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
