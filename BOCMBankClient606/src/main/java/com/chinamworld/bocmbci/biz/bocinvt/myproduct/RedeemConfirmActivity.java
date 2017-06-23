package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.HashMap;
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
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 赎回产品确认信息页
 * 
 * @author wangmengmeng
 * 
 */
public class RedeemConfirmActivity extends BociBaseActivity {
	private static final String TAG = "RedeemConfirmActivity";
	/** 赎回页面 */
	private View view;
	/** 产品信息 */
	private Map<String, Object> myproductMap;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 产品币种 */
	private TextView tv_currency_detail;
	/** 是否允许部分赎回 */
	private TextView tv_canPartlyRedeem_detail;
	/** 赎回价格 */
	private TextView tv_sellPrice_detail;
	/** 最低持有份额 */
	private TextView tv_lowestHoldQuantity_detail;
	/** 赎回起点金额 */
	private TextView tv_redeemStartingAmount_detail;
	/** 持有份额 */
	private TextView tv_holdQuantity_detail;
	/** 可赎回份额 */
	private TextView tv_canredeemQuantity_detail;
	/** 赎回份额 */
	private TextView et_redeemQuantity_detail;
	/** 确定 */
	private Button btn_next;
	/** 上一步 */
	private Button btn_pre;
	/** 赎回预交易 */
	private Map<String, Object> redeemVerifyMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.boci_redeem_title));
		// 添加布局
		view = addView(R.layout.bocinvt_redeem_confirm);
		// 界面初始化
		init();

		setRightBtnClick(rightBtnClick);
	}

	private void init() {
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_redeem_step1),
						this.getResources().getString(
								R.string.bocinvt_redeem_step2),
						this.getResources().getString(
								R.string.bocinvt_redeem_step3) });
		StepTitleUtils.getInstance().setTitleStep(2);
		myproductMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_MYPRODUCT_LIST);
		redeemVerifyMap = BociDataCenter.getInstance().getRedeemVerify();
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(
				BaseDroidApp.getInstanse().getCurrentAct(), tv_prodName_detail);
		tv_currency_detail = (TextView) view
				.findViewById(R.id.tv_currency_detail);
		tv_canPartlyRedeem_detail = (TextView) view
				.findViewById(R.id.tv_canPartlyRedeem_detail);
		tv_sellPrice_detail = (TextView) view
				.findViewById(R.id.tv_sellPrice_detail);
		tv_lowestHoldQuantity_detail = (TextView) view
				.findViewById(R.id.tv_lowestHoldQuantity_detail);
		tv_redeemStartingAmount_detail = (TextView) view
				.findViewById(R.id.tv_redeemStartingAmount_detail);
		tv_holdQuantity_detail = (TextView) view
				.findViewById(R.id.tv_holdQuantity_detail);
		tv_canredeemQuantity_detail = (TextView) view
				.findViewById(R.id.tv_canredeemQuantity_detail);
		et_redeemQuantity_detail = (TextView) view
				.findViewById(R.id.et_redeemQuantity_detail);
		// 赋值
		/** 产品名称 */
		tv_prodName_detail.setText((String) redeemVerifyMap
				.get(BocInvt.BOCINVT_REDVER_PRODNAME_RES));
		/** 产品币种 */
		String currency = (String) redeemVerifyMap
				.get(BocInvt.BOCINVT_REDVER_CURRENCYCODE_RES);
		/** 产品币种 */
		if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				/** 产品币种 */
				tv_currency_detail.setText(LocalData.Currency.get(currency));
			} else {
				tv_currency_detail
						.setText(LocalData.Currency.get(currency)
								+ BociDataCenter.cashMapValue.get(String.valueOf(myproductMap
										.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES))));
			}
		}
		/** 是否允许部分赎回 */
		String canPartlyRedeem = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_CANPARTLYREDEEM_RES));
		if (canPartlyRedeem.equals("0")) {
			tv_canPartlyRedeem_detail.setText(LocalData.bocicanPartlyRedeem
					.get(0));
		} else {
			tv_canPartlyRedeem_detail.setText(LocalData.bocicanPartlyRedeem
					.get(1));
		}

		/** 赎回价格 */
		String sellPrice = (String) redeemVerifyMap
				.get(BocInvt.BOCINVT_REDVER_SELLPRICE_RES);
		tv_sellPrice_detail.setText(StringUtil.parseStringCodePattern(currency,
				sellPrice, 2));
		/** 最低持有份额 */
		String lowesHoldQuantity = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_LOWESTHOLDQUANTITY_RES));
		tv_lowestHoldQuantity_detail.setText(StringUtil.parseStringPattern(
				lowesHoldQuantity, 2));
		/** 赎回起点金额 */
		String redeemStartingAmount = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_REDEEMSTARTINGAMOUNT_RES));
		tv_redeemStartingAmount_detail.setText(StringUtil
				.parseStringCodePattern(currency, redeemStartingAmount, 2));
		/** 持有份额 */
		String holdQuantity = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_HOLDINGQUANTITY_RES);
		tv_holdQuantity_detail.setText(StringUtil.parseStringPattern(
				holdQuantity, 2));
		/** 可赎回份额 */
		String canredeemQuantity = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_AVAILABLEQUANTITY_RES);
		tv_canredeemQuantity_detail.setText(StringUtil.parseStringPattern(
				canredeemQuantity, 2));
		/** 赎回份额 */
		String redeemQuantity = (String) redeemVerifyMap
				.get(BocInvt.BOCINVT_REDVER_REDEEMQUANTITY_RES);
		et_redeemQuantity_detail.setText(StringUtil.parseStringPattern(
				redeemQuantity, 2));

		btn_pre = (Button) view.findViewById(R.id.btn_pre);
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_pre.setOnClickListener(rightBtnClick);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pSNGetTokenId();
			}
		});
	}

	/** 右上角按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求持有产品赎回结果
		requestXpadHoldProductAndRedeem(tokenId);
	}

	/** 请求持有产品赎回结果 */
	public void requestXpadHoldProductAndRedeem(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADHOLDPRODUCTANDREDEEM_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(BocInvt.BOCINVT_REDEEM_TOKEN_REQ, tokenId);
//		paramsmap.put(BocInvt.DEALCODE, (String)redeemVerifyMap.get(BocInvt.DEALCODE));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestXpadHoldProductAndRedeemCallback");
	}

	/** 请求持有产品赎回结果回调 */
	public void requestXpadHoldProductAndRedeemCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		BociDataCenter.getInstance().setRedeemSuccessMap(resultMap);
		Intent intent = new Intent(this, RedeemSuccessActivity.class);
		startActivity(intent);
	}

}
