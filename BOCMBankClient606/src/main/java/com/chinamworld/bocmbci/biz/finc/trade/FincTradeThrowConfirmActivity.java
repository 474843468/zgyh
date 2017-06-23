package com.chinamworld.bocmbci.biz.finc.trade;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 登记转换 确认
 * 
 * @author xyl
 * 
 */
public class FincTradeThrowConfirmActivity extends FincBaseActivity {
	private static final String TAG = "FincTradeThrowConfirmActivity";

	/** 资金账户 */
	private TextView fundAccTv;
	/** 基金(投资)账户 */
	private TextView invAccTv;
	/** 转出基金代码 */
	private TextView fromfundCodeTv;
	/** 转入基金代码 */
	private TextView tofundCodeTv;
	/** (转出)份额 */
	private TextView amountTv;
	/** 发生巨额赎回的方式 */
	private TextView fundSellFlagTv;

	private String fundSellFlagStr;

	private String amountStr, feeType;

	private String inFundCodeStr, inFundNameStr;

	private String outFundNameStr;

	private String outFundCodeStr;

	private Button lastBtn;
	private Button confirmBtn;

	private String productRiskLevelStr;

	private String affirmFlag;
	private int flag;
	private static final int TRADENIGHTBUY = 1;
	private static final int TRADEBUY = 2;

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
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		switch (flag) {
		case TRADENIGHTBUY:
			fundThrowResultNight(outFundCodeStr, inFundCodeStr,
					fundSellFlagStr, amountStr, feeType, tokenId);
			break;
		case TRADEBUY:
			fundThrowResult(outFundCodeStr, inFundCodeStr, fundSellFlagStr,
					amountStr, tokenId);
			break;
		}

	}

	@Override
	public void fundThrowResultNightCallback(Object resultObj) {
		super.fundThrowResultNightCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();
		Intent intent = getIntent();
		intent.setClass(this, FincTradeThrowSuccesseActivity.class);
		intent.putExtra(Finc.I_FINCBUYSQL, map.get(Finc.FINC_FUNDNIGHTBUY_CONSIGNSEQ));
		intent.putExtra(Finc.I_TRANSACTIONID,
				map.get(Finc.FINC_TRANSACTIONID_REQ));
		startActivityForResult(intent, 1);
	}

	@Override
	public void fundThrowResultCallback(Object resultObj) {
		super.fundThrowResultCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> map = (Map<String, String>) biiResponseBody
				.getResult();

		String tranState = map.get(Finc.FINC_FUNDBUY_TRANSTATE);
		if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_SUCCESS)) {// 交易成功
			Intent intent = getIntent();
			intent.setClass(this, FincTradeThrowSuccesseActivity.class);
			intent.putExtra(Finc.I_FINCBUYSQL, map.get(Finc.FINC_FUNDSEQ_REQ));
			intent.putExtra(Finc.I_TRANSACTIONID,
					map.get(Finc.FINC_TRANSACTIONID_REQ));
			startActivityForResult(intent, 1);
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_TIMEERROR)) {// 非工作时间
			// TODO 提示非工作时间
			BaseDroidApp.getInstanse().showErrorDialog(
					getString(R.string.finc_tradetime_error), R.string.cancle,
					R.string.confirm, new OnClickListener() {
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
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_E142)) {// 未签署合同
			BaseDroidApp.getInstanse().createDialog("1",
					getString(R.string.finc_notagree2_error),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					});
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

	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		outFundCodeStr = (String) extras.get(Finc.I_OUTFUNDCODE);
		outFundNameStr = (String) extras.get(Finc.I_OUTFUDNAME);
		inFundCodeStr = (String) extras.get(Finc.I_INFUDCODE);
		inFundNameStr = (String) extras.get(Finc.I_INFUNDNAME);
		amountStr = (String) extras.get(Finc.I_AMOUNT);
		fundSellFlagStr = (String) extras.get(Finc.I_FUNDSELLFLAGSTR);

		fundSellFlagTv.setText(LocalData.fundSellFlagCodeToStr
				.get(fundSellFlagStr));
		fromfundCodeTv.setText(outFundCodeStr + "/" + outFundNameStr);
		tofundCodeTv.setText(inFundCodeStr + "/" + inFundNameStr);

		amountTv.setText(StringUtil.parseStringPattern(amountStr, 2));

		fundAccTv.setText(StringUtil.getForSixForString(fincControl.accNum));
		invAccTv.setText(fincControl.invAccId);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fromfundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tofundCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				fundAccTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				invAccTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView)findViewById(R.id.finc_sellType_alert));
	}

	private void init() {
		View childView = mainInflater.inflate(
				R.layout.finc_myfinc_balance_throw_confirm, null);
		tabcontent.addView(childView);
		setTitle(R.string.finc_myfinc_button_give);
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForDingTou());
		StepTitleUtils.getInstance().setTitleStep(2);
		fundAccTv = (TextView) childView.findViewById(R.id.finc_accNumber);
		invAccTv = (TextView) childView.findViewById(R.id.finc_accId);
		fromfundCodeTv = (TextView) childView.findViewById(R.id.finc_throw);
		tofundCodeTv = (TextView) childView.findViewById(R.id.finc_throw_in);
		amountTv = (TextView) childView.findViewById(R.id.finc_throw_total);
		fundSellFlagTv = (TextView) childView.findViewById(R.id.finc_sellType);

		lastBtn = (Button) childView.findViewById(R.id.lastButton);
		confirmBtn = (Button) childView.findViewById(R.id.sureButton);
		lastBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		initRightBtnForMain();
	}

	@Override
	public void getFundDetailByFundCodeCallback(Object resultObj) {
		super.getFundDetailByFundCodeCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		productRiskLevelStr = result.get(Finc.FINC_RISKLV);
		feeType = result.get(Finc.I_FEETYPE);
		requestPsnFundRiskEvaluationQueryResult();
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
		String userRiskLevel = result.get(Finc.FINC_RISKLEVEL_RES);
		if (Integer.valueOf(userRiskLevel) >= Integer
				.valueOf(productRiskLevelStr)) {// 如果用户的风险级别比产品的风险级别高
			requestCommConversationId();
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
								requestCommConversationId();
								BaseHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.lastButton:// 上一步
			finish();
			break;
		case R.id.sureButton:
			BaseHttpEngine.showProgressDialog();
			flag = TRADEBUY;
			getFundDetailByFundCode(inFundCodeStr);
			break;

		default:
			break;
		}
	}

}
