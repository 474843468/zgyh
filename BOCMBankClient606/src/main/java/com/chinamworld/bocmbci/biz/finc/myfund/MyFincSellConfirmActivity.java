package com.chinamworld.bocmbci.biz.finc.myfund;

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

import java.util.List;
import java.util.Map;

/**
 * 我的基金--基金卖出确认页面
 * 
 * @author 宁焰红
 * 
 */
public class MyFincSellConfirmActivity extends FincBaseActivity {
	public static final String TAG = "MyFincSellConfirmActivity";
	/** 基金持仓主view */
	private View myFincView = null;
	/** 基金代码 */
	private String foundCode = null;
	/** 基金名称 */
	private String foundName = null;
	/** 用户输入的份额 */
	private String sellTotalValue = null;
	/** 用户选择的赎回方式 */
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
	/** 上一步 */
	private Button lastButton = null;
	/** 确定 */
	private Button sureButton = null;
	private String tokenId = null;
	/** 基金账户 */
	private String investAccount = null;
	/** 资金账户 */
	private String account = null;
	/** 资金账户别名 */

	private String feeTypeStr;

	private int flag;
	private static final int FINCSELL = 0;
	private static final int FINCNIGHTSELL = 1;

	private String assignedDate = null;
	private String executeType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		setFundCompanyInfo();
		initOnClick();
	}


	private void init() {
		setRightToMainHome();
		myFincView = mainInflater.inflate(R.layout.finc_myfinc_sell_confirm,
				null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.finc_myfinc_sellFound));
		accNumber = (TextView) findViewById(R.id.finc_accId);
		fincAccNumber = (TextView) findViewById(R.id.finc_accNumber);
		fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
		fincNameText = (TextView) findViewById(R.id.finc_fincName);
		totalCountText = (TextView) findViewById(R.id.finc_sellAmount);
		sellTypeText = (TextView) findViewById(R.id.finc_sellType);
		TextView	sellTypePreTv = (TextView) findViewById(R.id.finc_sellType_pre);
		lastButton = (Button) findViewById(R.id.lastButton);
		sureButton = (Button) findViewById(R.id.sureButton);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, sellTypePreTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincAccNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, fincNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, 
				(TextView)findViewById(R.id.finc_sellType_pre));
		initRightBtnForMain();
	}

	/***
	 * 初始化卖出页面传递的数据
	 */
	private void initData() {
		investAccount = fincControl.invAccId;
		account = fincControl.accNum;
		Intent intent = getIntent();
		if (intent.getStringExtra(Finc.I_ASSIGNEDDATE) == null) {// 当日交易
			assignedDate = null;
			executeType = ConstantGloble.FINC_EXECUTETYPE_BUY_TURNOVER;
			StepTitleUtils.getInstance().initTitldStep(this,
					fincControl.getStepsForSell());
			StepTitleUtils.getInstance().setTitleStep(2);
		} else {
			assignedDate = intent.getStringExtra(Finc.I_ASSIGNEDDATE);
			executeType = ConstantGloble.FINC_EXECUTETYPE_BUY_EXTRUDAY;
			StepTitleUtils.getInstance().initTitldStep(this,
					fincControl.getStepsForExtradeDaySell2());
			StepTitleUtils.getInstance().setTitleStep(2);
			LinearLayout executeTypeLayout = (LinearLayout) findViewById(R.id.finc_executeType_layout);
			LinearLayout exeDateLayout = (LinearLayout) findViewById(R.id.finc_exeDate_layout);
			exeDateLayout.setVisibility(View.VISIBLE);
			executeTypeLayout.setVisibility(View.VISIBLE);
			TextView exeDateTv = (TextView) findViewById(R.id.finc_exeDate_tv);
			exeDateTv.setText(assignedDate);
		}
		sellTotalValue = intent.getStringExtra(Finc.FINC_SELLAMOUNT_REQ);
		sellTypeValue = intent.getStringExtra(Finc.I_SELLTYPEVALUE);
		if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {
			Map<String, Object> fundBalanceMap = fincControl.tradeFundDetails;
//			Map<String, String> fundInfoMap = (Map<String, String>) fundBalanceMap
//					.get(Finc.FINC_FUNDINFO_REQ);
			Map<String, Object> fundInfoMap = (Map<String, Object>) fundBalanceMap
					.get(Finc.FINC_FUNDINFO_REQ);
			if(fundInfoMap == null){
				fundInfoMap = fundBalanceMap;
			}
			foundCode = (String)fundInfoMap.get(Finc.FINC_FUNDCODE);
			foundName = (String)fundInfoMap.get(Finc.FINC_FUNDNAME);
			feeTypeStr = (String) fundInfoMap.get(Finc.FINC_FEETYPE);
		}
		accNumber.setText(StringUtil.getForSixForString(account));
		fincAccNumber.setText(investAccount);
		fincCodeText.setText(foundCode);
		fincNameText.setText(foundName);
		totalCountText.setText(StringUtil.parseStringPattern(sellTotalValue, 2));
		FincControl.setTextColor(totalCountText, this);	
		sellTypeText
				.setText(LocalData.fundSellFlagCodeToStr.get(sellTypeValue));

	}

	private void initOnClick() {
		// 上一步
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到卖出填写页面
				finish();
			}
		});
		// 确定
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flag = FINCSELL;
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});
	}

	/** 重写ConversationId回调方法 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取TokenId
		requestPSNGetTokenId();
	}

	/** 重写tokenId回调方法 */
	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if ("".equals(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			switch (flag) {
			case FINCSELL:
				requestPsnFundSell(foundCode, sellTypeValue, feeTypeStr,
						sellTotalValue, tokenId, executeType, assignedDate);
				break;
			case FINCNIGHTSELL:
				fundNightSell(sellTotalValue, foundCode, feeTypeStr,
						sellTypeValue, tokenId);
				break;

			default:
				break;
			}

		}
	}

	@Override
	public void fundNightSellCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.fundNightSellCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		// 基金交易流水号
		String fundSeq = resultMap.get(Finc.FINC_FUNDNIGHTSELL_CONSIGNSEQ);
		// 交易流水号
		String transactionId = resultMap.get(Finc.FINC_TRANSACTIONID_REQ);
		// 跳转到基金卖出成功页面

		Intent intent = getIntent();
		intent.setClass(MyFincSellConfirmActivity.this,
				MyFincSellSuccessActivity.class);
		intent.putExtra(Finc.FINC_FUNDSEQ_REQ, fundSeq);
		intent.putExtra(Finc.FINC_TRANSACTIONID_REQ, transactionId);
		if (assignedDate != null) {
			intent.putExtra(Finc.I_ASSIGNEDDATE, assignedDate);
		}
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
	/**
	 * 卖出基金----回调
	 */
	@Override
	public void requestPsnFundSellCallback(Object resultObj) {
		super.requestPsnFundSellCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String tranState = resultMap.get(Finc.FINC_FUNDBUY_TRANSTATE);
		if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_SUCCESS)) {// 交易成功
			BaseHttpEngine.dissMissProgressDialog();
			String fundSeq = resultMap.get(Finc.FINC_FUNDSEQ_REQ);
			String transactionId = resultMap.get(Finc.FINC_TRANSACTIONID_REQ);
			Intent intent = getIntent();
			intent.setClass(MyFincSellConfirmActivity.this,
					MyFincSellSuccessActivity.class);
			intent.putExtra(Finc.FINC_FUNDSEQ_REQ, fundSeq);
			intent.putExtra(Finc.FINC_TRANSACTIONID_REQ, transactionId);
			if (assignedDate != null) {
				intent.putExtra(Finc.I_ASSIGNEDDATE, assignedDate);
			}
			startActivityForResult(intent, 1);
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_TIMEERROR)) {// 非工作时间
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.finc_tradetime_error),
					R.string.cancle  , R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								flag = FINCNIGHTSELL;
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
			BaseDroidApp.getInstanse().createDialog("1", getString(R.string.finc_notagree1_error),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					});
		} else if (tranState.equals(ConstantGloble.FINC_FUNDBUYSTATE_E142)) {// 未签署合同
			BaseDroidApp.getInstanse().createDialog("1", getString(R.string.finc_notagree2_error),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					});
		}
	}

}
