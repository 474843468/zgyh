package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 产品赎回输入信息页
 * 
 * @author wangmengmeng
 * 
 */
public class RedeemInputActivity extends BociBaseActivity {
	private static final String TAG = "RedeemInputActivity";
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
	private EditText et_redeemQuantity_detail;
	/** 下一步 */
	private Button btn_next;
	private String redeemQuantity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.boci_redeem_title));
		// 添加布局
		view = addView(R.layout.bocinvt_redeem_input);
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
		StepTitleUtils.getInstance().setTitleStep(1);
		myproductMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_MYPRODUCT_LIST);
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
		et_redeemQuantity_detail = (EditText) view
				.findViewById(R.id.et_redeemQuantity_detail);
		((TextView)findViewById(R.id.tv_acctnum)).setText(StringUtil.getForSixForString((String)myproductMap.get(BocInvt.BANCACCOUNT)));
		// 赋值
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES)));
		/** 产品币种 */
		String currency = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES);
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
		LinearLayout ll_redeem = (LinearLayout) view
				.findViewById(R.id.ll_redeemQuantity);
		/** 是否允许部分赎回 */
		String canPartlyRedeem = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_CANPARTLYREDEEM_RES);
		if (canPartlyRedeem.equals("0")) {
			// 允许部分赎回
			tv_canPartlyRedeem_detail.setText(LocalData.bocicanPartlyRedeem
					.get(0));
		} else {
			// 不允许部分赎回
			tv_canPartlyRedeem_detail.setText(LocalData.bocicanPartlyRedeem
					.get(1));

			ll_redeem.setVisibility(View.GONE);
		}

		/** 赎回价格 */
		String sellPrice = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_SELLPRICE_RES));
		tv_sellPrice_detail.setText(StringUtil.parseStringCodePattern(currency,
				sellPrice, 2));
		/** 最低持有份额 */
		String lowesHoldQuantity = (String) myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_LOWESTHOLDQUANTITY_RES);
		tv_lowestHoldQuantity_detail.setText(StringUtil.parseStringPattern(
				lowesHoldQuantity, 2));
		/** 赎回起点金额 */
		String redeemStartingAmoung = String.valueOf(myproductMap
				.get(BocInvt.BOCINVT_HOLDPRO_REDEEMSTARTINGAMOUNT_RES));
		tv_redeemStartingAmount_detail.setText(StringUtil
				.parseStringCodePattern(currency, redeemStartingAmoung, 2));
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

		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断允许部分赎回：
				// 赎回份额不能大于可用份额
				// 非空 数字
				// 不允许部分赎回：为持有份额

				/** 可赎回份额 */
				double canredeemQuantity = Double.valueOf(String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_AVAILABLEQUANTITY_RES)));
				String canPartlyRedeem = String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_HOLDPRO_CANPARTLYREDEEM_RES));
				if (canPartlyRedeem.equals("0")) {
					/** 赎回份额 */
					redeemQuantity = et_redeemQuantity_detail.getText()
							.toString().trim();
					// 以下为验证
					// 赎回份额
					RegexpBean reb = new RegexpBean(RedeemInputActivity.this
							.getString(R.string.bocinvt_redeem_regex),
							redeemQuantity, "redeemShare");
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb);
					if (!RegexpUtils.regexpDate(lists)) {// 校验没有通过
						return;
					}
					// 允许部分赎回
					if (Double.valueOf(redeemQuantity) > canredeemQuantity) {
						BaseDroidApp
								.getInstanse()
								.showInfoMessageDialog(
										RedeemInputActivity.this
												.getString(R.string.bocinvt_error_canredeem1));
						return;
					}
					
				} else {
					// 不允许部分赎回
					redeemQuantity = String.valueOf(myproductMap
							.get(BocInvt.BOCINVT_HOLDPRO_HOLDINGQUANTITY_RES));
				}
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();

			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();

	}

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
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求持有产品赎回预交易
		requestPsnXpadHoldProductRedeemVerify(tokenId);
	}

	/** 请求持有产品赎回预交易 */
	public void requestPsnXpadHoldProductRedeemVerify(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADHOLDPRODUCTREDEEMVERIFY_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
//		paramsmap.put(BocInvt.BOCINVT_REDVER_CASHREMIT_REQ, String
//				.valueOf(myproductMap
//						.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES)));
		paramsmap
				.put(BocInvt.BOCINVT_REDVER_REDEEMQUANTITY_REQ, redeemQuantity);
		paramsmap.put(BocInvt.BOCINVT_REDVER_PRODCODE_REQ,
				String.valueOf(myproductMap
						.get(BocInvt.BOCINVT_REDVER_PRODCODE_REQ)));
		paramsmap.put(BocInvt.BOCINVT_REDVER_TOKEN_REQ, tokenId);
		paramsmap.put(BocInvt.BOCINVT_SIGNRESULT_XPADCASHREMIT_REQ, cashRemit((String)myproductMap.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES)));
		paramsmap.put(BocInvt.BOCINVT_BUYINIT_ACCOUNTNUMBER_RES, (String)myproductMap.get(BocInvt.BANCACCOUNT));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadHoldProductRedeemVerifyCallback");
	}

	/** 请求持有产品赎回预交易回调 */
	public void requestPsnXpadHoldProductRedeemVerifyCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		final Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		String tranflag = (String) resultMap
				.get(BocInvt.BOCINVT_REDVER_TRANFLAG_RES);
		if (LocalData.tranFlagMap.get(tranflag).equals(
				ConstantGloble.BOCINVT_TRANFLAG)) {
			BaseDroidApp.getInstanse().showErrorDialog(
					this.getString(R.string.bocinvt_error_orderTime),
					R.string.cancle, R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								BociDataCenter.getInstance().setRedeemVerify(
										resultMap);
								Intent intent = new Intent(
										RedeemInputActivity.this,
										RedeemConfirmActivity.class);
								startActivity(intent);
								break;
							case CustomDialog.TAG_CANCLE:
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		} else {
			BociDataCenter.getInstance().setRedeemVerify(resultMap);
			Intent intent = new Intent(RedeemInputActivity.this,
					RedeemConfirmActivity.class);
			startActivity(intent);
		}

	}

	/** 右上角按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	
	/**
	 * 将接口返回的转换  忒恶心
	 * @param oCashRemit
	 * @return
	 */
	private String cashRemit(String oCashRemit){
		if (StringUtil.isNull(oCashRemit)) {
			return "";
		}else if(oCashRemit.equals("0")){
			return "00";
		}else if(oCashRemit.equals("1")){
			return "01";
		}else if(oCashRemit.equals("2")){
			return "02";
		}else{
			return "";
		}
	}

}
