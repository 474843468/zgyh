package com.chinamworld.bocmbci.biz.finc.query;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金 定期定额赎回修改确认 页面
 * 
 * @author xyl
 * 
 */
public class FundSecheduSellModifyConfirmActivity extends FincBaseActivity {
	/** 基金持仓主view */
	private View myFincView = null;
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	/**
	 * 用户选择的赎回方式,基金卖出参数 0-不连续赎回(取消赎回) ，1-连续赎回(顺延赎回)
	 */
	/** 用户输入的份额 */
	private String sellTotalValue = null;
	/** 用户选择的赎回方式,方式名称 */
	private String sellTypeValue = null;
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
	private String tokenId = null;
	/** 基金账户 */
	private String investAccount = null;
	/** 资金账户 */
	private String account = null;
	/** 资金账户别名 */
	private String accountNickName = null;

	private String dayInMonthStr;
	//原定定投定赎序号
	private String fundSeq, oldApplyDate;
	
	/**
	 * 定投周期， 每周定投日， 结束条件，指定结束日期，累计成交份额，累计成交金额
	 */
	private String transcycle, paymentdateofweek, endflag, fundpointenddate,
			endsum, fundpointendamount;
	private String paymentDate, endContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		setFundCompanyInfo();
		initOnClick();
		initRightBtnForMain();
	}


	private void init() {
		myFincView = mainInflater.inflate(
				R.layout.finc_myfinc_scheudsell_confirm, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(
				R.string.finc_title_scheduedit_sell));
		accNumber = (TextView) findViewById(R.id.finc_accId);
		fincAccNumber = (TextView) findViewById(R.id.finc_accNumber);
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		totalCountText = (TextView) findViewById(R.id.finc_sellAmount);
		sellTypeText = (TextView) findViewById(R.id.finc_sellType);
		dayInMonthTv = (TextView) findViewById(R.id.finc_dayInMonth_sale_tv);
		sureButton = (Button) findViewById(R.id.sureButton);

	}

	/***
	 * 初始化卖出页面传递的数据
	 */
	private void initData() {
		investAccount = fincControl.invAccId;
		account = fincControl.accNum;
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForSell());
		StepTitleUtils.getInstance().setTitleStep(2);
		if (!StringUtil.isNullOrEmpty(fincControl.fundScheduQueryMap)) {
			Map<String, Object> fundBalanceMap = fincControl.fundScheduQueryMap;
			Map<String, Object> fundInfoMap = (Map<String, Object>) fundBalanceMap
					.get(Finc.FINC_FUNDQUERYDQDT_FUNDINFO);
			
			foundCode = (String)fundInfoMap.get(Finc.FINC_FUNDCODE);
			foundName = (String)fundInfoMap.get(Finc.FINC_FUNDNAME);
			fundSeq = (String) fincControl.fundScheduQueryMap
					.get(Finc.FINC_FUNDBUY_FUNDSEQ);
			oldApplyDate = (String) fundBalanceMap.get(Finc.I_APPLYDATE);
			Intent intent = getIntent();
			sellTotalValue = intent.getStringExtra(Finc.FINC_SELLAMOUNT_REQ);
			dayInMonthStr = intent.getStringExtra(Finc.I_DAYINMONTH);
			sellTypeValue = intent.getStringExtra(Finc.I_SELLTYPEVALUE);
	
			accNumber.setText(StringUtil.getForSixForString(account));
			fincAccNumber.setText(investAccount);
			fincCodeText.setText(String.valueOf(foundCode));
			fincNameText.setText(String.valueOf(foundName));
			totalCountText.setText(StringUtil.parseStringPattern(sellTotalValue, 2));
			FincControl.setTextColor(totalCountText, this);	
			sellTypeText.setText(LocalData.fundSellFlagCodeToStr.get(sellTypeValue));
			dayInMonthTv.setText(dayInMonthStr);
			initP405ModiData(intent);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,accNumber);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,fincAccNumber);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,totalCountText);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,fincNameText);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					(TextView)findViewById(R.id.finc_sellType_alert));
		}
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
				findViewById(R.id.finc_dayInMonth_ll).setVisibility(View.VISIBLE);
				dayInMonthTv.setText(StringUtil.valueOf1(dayInMonthStr));
				paymentDate = dayInMonthStr;
				break;
			case 1:
				TextView fincSaleDayOfWeekTv = (TextView)findViewById(R.id.fincSaleDayOfWeekTv);
				findViewById(R.id.fincSaleDayOfWeekLl).setVisibility(View.VISIBLE);
				paymentdateofweek = intent.getStringExtra(Finc.PAYMENTDATEOFWEEK);
				fincSaleDayOfWeekTv.setText(paymentdateofweek);
				paymentDate = getValueByWeek(paymentdateofweek);
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
				this.endContext = fundpointenddate;
				endName.setText(getString(R.string.finc_scheduledbuy_end_time));
				endContext.setText(StringUtil.valueOf1(fundpointenddate));
				break;
			case 2:
				endsum = intent.getStringExtra(Finc.ENDSUM);
				this.endContext = endsum;
				endName.setText(getString(R.string.finc_scheduledbuy_total_deal_count));
				endContext.setText(StringUtil.valueOf1(endsum));
				break;
			case 3:
				fundpointendamount = intent.getStringExtra(Finc.FUNDPOINTENDAMOUNT);
				this.endContext = fundpointendamount;
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
		// 确定
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
	}

	/** 重写ConversationId回调方法 */
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
		tokenId = (String) biiResponseBody.getResult();
//		fincScheduSell(foundCode, sellTotalValue, sellTypeValue, "", tokenId,
//				transcycle, paymentDate, endflag, endContext);
		fundScheduSellModify(foundCode, oldApplyDate, sellTotalValue,
				paymentDate, endflag, endContext, tokenId, fundSeq, transcycle);
	}
	
	@Override
	public void fundScheduSellModifyCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.fundScheduSellModifyCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 基金交易流水号
		String fundSeq = String.valueOf(resultMap.get(Finc.FINC_FUNDSEQ_REQ));
		// 交易流水号
		String transactionId = String.valueOf(resultMap.get(Finc.FINC_TRANSACTIONID_REQ));
		// 跳转到基金卖出成功页面
		Intent intent = new Intent(this,
				FundSecheduSellModifySuccessActivity.class);
		intent.putExtra(Finc.FINC_FUNDSEQ_REQ, fundSeq);
		intent.putExtra(Finc.FINC_TRANSACTIONID_REQ, transactionId);
		intent.putExtra(Finc.FINC_FUNDCODE_REQ, foundCode);
		intent.putExtra(Finc.FINC_FUNDNAME_REQ, foundName);
		intent.putExtra(Finc.FINC_SELLAMOUNT_REQ, sellTotalValue);
		intent.putExtra(Finc.I_SELLTYPEVALUE, sellTypeValue);
		intent.putExtra(Finc.FINC_INVESTACCOUNT_RES, investAccount);
		intent.putExtra(Finc.FINC_ACCOUNT_RES, account);
		intent.putExtra(Finc.FINC_ACCOUNTNICKNAME_RES, accountNickName);
		intent.putExtra(Finc.I_DAYINMONTH, dayInMonthStr);
		
		intent.putExtra(Finc.TRANSCYCLE, transcycle);// 定投周期
		intent.putExtra(Finc.PAYMENTDATEOFWEEK, paymentdateofweek);// 每周定投日
		intent.putExtra(Finc.ENDFLAG, endflag);// 结束条件
		intent.putExtra(Finc.FUNDPOINTENDDATE, fundpointenddate);// 结束日期
		intent.putExtra(Finc.ENDSUM, endsum);// 成交份额
		intent.putExtra(Finc.FUNDPOINTENDAMOUNT, fundpointendamount);// 成交金额
		
		intent.setClass(this, FundSecheduSellModifySuccessActivity.class);
		startActivityForResult(intent, 1);
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

}
