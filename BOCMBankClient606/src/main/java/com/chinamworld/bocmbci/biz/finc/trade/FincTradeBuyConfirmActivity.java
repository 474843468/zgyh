package com.chinamworld.bocmbci.biz.finc.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 基金买入确认
 * 
 * @author xyl
 * 
 */
public class FincTradeBuyConfirmActivity extends FincBaseActivity {
	private static final String TAG = "FincFundTaSettingConfirmActivity";
	private String fundCodeStr;
	private String fundNameStr;
	private String feeTypeStr;
	private String netPriceStr;
	private String productRiskLevelStr;
	private String tradeCurrencyStr, cashFlagCode;
	private String transIdStr;

	private TextView fundCodeTextView;
	private TextView fundNameTextView;
	private TextView netPriceTextView;
	private TextView productRiskLevelTextView;
	private TextView feeTypeTextView;
	private TextView tradeCurrencyTextView;
	private TextView accBalanceTextView;
	private TextView buyAmountTextView;
	//单日购买上限
	private TextView finc_daylimit_textView;
	private String accBalanceStr;
	private String buyAmountStr;
	private BigDecimal buyAmountBig;
	/** 基金交易 序号 */
	private String fundSeqStr;

	private Button confirmBtn;
	private Button preBtn;
	private String tokenId;
	private String affirmFlag;
	private String userRiskLevel;

	private String executeTypeStr = "0";

	private int flag = TRADEBUY;
	private String assignedDateStr = null;
	private TextView orderLowLimitTextView;
	private TextView applyLowLimitTextView; 
	private String orderLowLimitStr;
	private String applyLowLimitStr;
	//单日购买上限 
	private LinearLayout  finc_daylimit_title;

	//基金公司代码
	private String fundCompanyCode;
	// 基金类型
	private String fntype;
	// 基金类型(判断是否是短期理财)
	private String isShortFund;

	private static final int TRADEBUY = 0;
	/** 挂单交易 */
	private static final int TRADENIGHTBUY = 1;

	private OnClickListener onClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		setFundCompanyInfo();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPsnFundRiskEvaluationQueryResultCallback(Object resultObj) {
		super.requestPsnFundRiskEvaluationQueryResultCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();
		if (result == null) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		userRiskLevel = result.get(Finc.FINC_RISKLEVEL_RES);
		if (Integer.valueOf(userRiskLevel) >= Integer
				.valueOf(productRiskLevelStr)) {// 如果用户的风险级别比产品的风险级别高
			affirmFlag = ConstantGloble.FINC_AFFIRMFLAG_YES;
			flag = TRADEBUY;
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showErrorDialog(
					getString(R.string.finc_riskLevel_error), R.string.cancle,
					R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								affirmFlag = ConstantGloble.FINC_AFFIRMFLAG_YES;
								flag = TRADEBUY;
								requestCommConversationId();
								BaseHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:// 确定 购买
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		}

	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		switch (flag) {
		case TRADEBUY:
			fundBuy(buyAmountBig, fundCodeStr, feeTypeStr, affirmFlag, tokenId,
					executeTypeStr, assignedDateStr);
			break;
		case TRADENIGHTBUY:
			fundNightBuy(buyAmountBig, fundCodeStr, feeTypeStr, affirmFlag,
					tokenId);
			break;

		default:
			break;
		}

	}

	@Override
	public void fundNightBuyCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.fundNightBuyCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		String tranState = resultMap.get(Finc.FINC_FUNDBUY_TRANSTATE);

		if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_SUCCESS)) {// 交易成功
			fundSeqStr = resultMap.get(Finc.FINC_FUNDNIGHTBUY_CONSIGNSEQ);
			transIdStr = resultMap.get(Finc.FINC_FUNDBUY_TRANSACTIONID);
			Intent intent = getIntent();
			intent.setClass(this, FincTradeBuySuccessActivity.class);
			intent.putExtra(Finc.I_FINCBUYSQL, fundSeqStr);
			intent.putExtra(Finc.I_TRANSACTIONID, transIdStr);
			if (assignedDateStr != null) {
				intent.putExtra(Finc.I_ASSIGNEDDATE, assignedDateStr);
			}
			startActivityForResult(intent, 1);
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_E146)) {// 未签署约定书
			BaseDroidApp.getInstanse().createDialog("1",
					getString(R.string.finc_notagree1_error),
					new OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
				}
			});
			return;
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_E142)) {// 未签署合同
			//			BaseDroidApp.getInstanse().createDialog("1",
			//					getString(R.string.finc_notagree2_error),
			//					new OnClickListener() {
			//				@Override
			//				public void onClick(View v) {
			//					BaseDroidApp.getInstanse().dismissErrorDialog();
			//				}
			//			});
			//			return;


			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showErrorDialog(
					"该产品属于资管类产品，您需要签署电子合同。", R.string.cancle,
					R.string.signpact, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								Intent intent = getIntent();
								intent.setClass(FincTradeBuyConfirmActivity.this, FincTradeBuyConfirmContractActivity.class);
								startActivity(intent);
								break;
							case CustomDialog.TAG_CANCLE:// 确定 购买
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		}

	}

	@Override
	public void fundBuyCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.fundBuyCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		String tranState = resultMap.get(Finc.FINC_FUNDBUY_TRANSTATE);

		if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_SUCCESS)) {// 交易成功
			fundSeqStr = resultMap.get(Finc.FINC_FUNDBUY_FUNDSEQ);
			transIdStr = resultMap.get(Finc.FINC_FUNDBUY_TRANSACTIONID);
			Intent intent = getIntent();
			intent.setClass(this, FincTradeBuySuccessActivity.class);
			intent.putExtra(Finc.I_FINCBUYSQL, fundSeqStr);//交易流水号
			intent.putExtra(Finc.I_TRANSACTIONID, transIdStr);//交易序列号
			if (assignedDateStr != null) {
				intent.putExtra(Finc.I_ASSIGNEDDATE, assignedDateStr);
			}
			startActivityForResult(intent, 1);
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_TIMEERROR)) {// 非工作时间
			// TODO 提示非工作时间
			BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.finc_tradetime_error),
					R.string.cancle, R.string.confirm, new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (Integer.parseInt(v.getTag() + "")) {
					case CustomDialog.TAG_SURE:
						BaseDroidApp.getInstanse().dismissErrorDialog();
						affirmFlag = ConstantGloble.FINC_AFFIRMFLAG_YES;
						flag = TRADENIGHTBUY;
						requestCommConversationId();
						BaseHttpEngine.showProgressDialog();
						break;
					case CustomDialog.TAG_CANCLE:
						BaseDroidApp.getInstanse().dismissErrorDialog();
						break;
					}
				}
			});
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_E146)) {// 未签署约定书
			BaseDroidApp.getInstanse().createDialog("1",
					getString(R.string.finc_notagree1_error),
					new OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
				}
			});
			return;
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_E142)) {// 未签署合同
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showErrorDialog(
					"该产品属于资管类产品，您需要签署电子合同。", R.string.back,
					R.string.signpact, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								Intent intent = getIntent();
								intent.setClass(FincTradeBuyConfirmActivity.this, FincTradeBuyConfirmContractActivity.class);
								startActivity(intent);
								break;
							case CustomDialog.TAG_CANCLE:// 确定 购买
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		}
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childview = mainInflater.inflate(R.layout.finc_trade_buy_confirm,
				null);
		tabcontent.addView(childview);
		setTitle(R.string.finc_title_buy);
		fundCodeTextView = (TextView) childview
				.findViewById(R.id.finc_fundCode_textview);
		fundNameTextView = (TextView) childview
				.findViewById(R.id.finc_fundName_textview);
		netPriceTextView = (TextView) childview
				.findViewById(R.id.finc_netvalue_textView);
		productRiskLevelTextView = (TextView) childview
				.findViewById(R.id.finc_productrisklevel_textView);
		feeTypeTextView = (TextView) childview
				.findViewById(R.id.finc_feetype_textView);
		orderLowLimitTextView = (TextView) childview
				.findViewById(R.id.finc_rebuyLowLimit_textView);
		applyLowLimitTextView = (TextView) childview
				.findViewById(R.id.finc_shenbuyLowLimit_textView);
		tradeCurrencyTextView = (TextView) childview
				.findViewById(R.id.finc_tradecurrency_textView);
		accBalanceTextView = (TextView) childview
				.findViewById(R.id.finc_accbalance_textView);
		buyAmountTextView = (TextView) childview
				.findViewById(R.id.finc_buyamount_TextView);
		//单日购买上限
		finc_daylimit_title=(LinearLayout) findViewById(R.id.finc_daylimit_title);
		finc_daylimit_textView = (TextView) childview
				.findViewById(R.id.finc_daylimit_textView);
		confirmBtn = (Button) childview.findViewById(R.id.finc_confirm);
		preBtn = (Button) childview.findViewById(R.id.finc_pre);
		confirmBtn.setOnClickListener(this);
		preBtn.setOnClickListener(this);
		initRightBtnForMain();

		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();

			}
		};
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras.getString(Finc.I_ASSIGNEDDATE) == null) {
			assignedDateStr = null;
			executeTypeStr = ConstantGloble.FINC_EXECUTETYPE_BUY_TURNOVER;
			StepTitleUtils.getInstance().initTitldStep(this,
					fincControl.getStepsFortradeBuy());
			StepTitleUtils.getInstance().setTitleStep(2);
		} else {
			assignedDateStr = extras.getString(Finc.I_ASSIGNEDDATE);
			executeTypeStr = ConstantGloble.FINC_EXECUTETYPE_BUY_EXTRUDAY;
			StepTitleUtils.getInstance().initTitldStep(this,
					fincControl.getStepsForExtradeDayBuy2());
			StepTitleUtils.getInstance().setTitleStep(2);
			LinearLayout executeTypeLayout = (LinearLayout) findViewById(R.id.finc_executeType_layout);
			LinearLayout exeDateLayout = (LinearLayout) findViewById(R.id.finc_exeDate_layout);
			exeDateLayout.setVisibility(View.VISIBLE);
			executeTypeLayout.setVisibility(View.VISIBLE);
			TextView exeDateTv = (TextView) findViewById(R.id.finc_exeDate_tv);
			exeDateTv.setText(assignedDateStr);
		}
		accBalanceStr = extras.getString(Finc.I_ACCBALANCE);
		buyAmountStr = extras.getString(Finc.I_BUYAMOUNT);
		buyAmountBig = new BigDecimal(buyAmountStr.toCharArray());
		fundCodeStr = (String) extras.get(Finc.I_FUNDCODE);
		fundNameStr = (String) extras.get(Finc.I_FUNDNAME);
		netPriceStr = (String) extras.get(Finc.I_NETPRICE);
		productRiskLevelStr = (String) extras.get(Finc.I_RISKLEVEL);
		feeTypeStr = extras.getString(Finc.I_FEETYPE);// 收费方式.
		orderLowLimitStr = (String) extras.get(Finc.FINC_ORDERLOWLIMIT);
		applyLowLimitStr = (String) extras.get(Finc.FINC_APPLYLOWLIMIT);
		tradeCurrencyStr = extras.getString(Finc.I_CURRENCYCODE);
		cashFlagCode = extras.getString(Finc.I_CASHFLAG);
		if(cashFlagCode==null||"null".equals(cashFlagCode)||"".equals(cashFlagCode)){
			if(fincControl.fundDetails!=null){
				cashFlagCode = (String) fincControl.fundDetails.get(Finc.FINC_CASHFLAG);
			}
		}
		// 基金类型
		fntype = extras.getString(Finc.FINC_FNTYPE);
		// 基金类型（判断是否是短期理财）
		isShortFund = extras.getString(Finc.FINC_IS_SHORT_FUND);
		//公司基金代码
		fundCompanyCode = extras.getString(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE);
		if (!StringUtil.isNullOrEmpty(accBalanceStr)) {
			accBalanceTextView.setText(StringUtil.parseStringPattern(
					accBalanceStr, 2));
		}
		fundCodeTextView.setText(fundCodeStr); // 只有详情中详情中进入才显示
		fundNameTextView.setText(fundNameStr); // 只有详情中详情中进入才显示
		netPriceTextView.setText(StringUtil.parseStringPattern(netPriceStr,4));
		// LocalData.fincRiskLevelCodeToStr.get(productRiskLevelStr)
		productRiskLevelTextView.setText(LocalData.fincRiskLevelCodeToStrFUND
				.get(productRiskLevelStr));
		feeTypeTextView.setText(LocalData.fundfeeTypeCodeToStr.get(feeTypeStr));
		orderLowLimitTextView.setText(StringUtil.parseStringCodePattern(
				tradeCurrencyStr, orderLowLimitStr, 2));
		applyLowLimitTextView.setText(StringUtil.parseStringCodePattern(
				tradeCurrencyStr, applyLowLimitStr, 2));
		tradeCurrencyTextView.setText(FincControl.fincCurrencyAndCashFlag(
				tradeCurrencyStr, cashFlagCode));
		buyAmountTextView.setText(StringUtil
				.parseStringCodePattern(tradeCurrencyStr, buyAmountStr, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fundNameTextView);
		//单日购买上限title
		finc_daylimit_title.setVisibility(View.VISIBLE);
		// 单日购买上限显示
		String mDayMaxSumBuy = extras.getString(Finc.FINC_DAYMAXSUMBUY);
		if(mDayMaxSumBuy == null || "".equals(mDayMaxSumBuy)){
			finc_daylimit_title.setVisibility(View.GONE);
		}else{
			finc_daylimit_textView.setText(StringUtil.parseStringPattern(mDayMaxSumBuy, 2));
		}
		
		

		//代表是指定日期界面跳转过来的 需要显示指定的提示语
		if ("11".equals(intent.getStringExtra("11"))){
			findViewById(R.id.finc_myfinc_appoint_date_show_layout).setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_pre:// 上一步
			this.finish();
			break;
		case R.id.finc_confirm:// 确定
			BaseHttpEngine.showProgressDialog();
			//检查属否签署电子约定书 
			//			if("01".equals(fntype)&&"Y".equals(FincControl.getInstance().fundDetails.get(Finc.FINC_IS_SHORT_FUND))){
			//				doCheckRequestPsnFundIsSignedFundStipulation(fundCompanyCode);
			//			}else{
			requestPsnFundRiskEvaluationQueryResult();
			//			}

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
}
