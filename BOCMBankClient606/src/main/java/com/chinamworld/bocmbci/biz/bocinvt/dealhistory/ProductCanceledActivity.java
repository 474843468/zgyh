package com.chinamworld.bocmbci.biz.bocinvt.dealhistory;

import java.util.Hashtable;
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
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 历史查询产品撤单确认页
 * 
 * @author wangmengmeng
 * 
 */
public class ProductCanceledActivity extends BociBaseActivity {
	/** 历史产品撤单确认页 */
	private View view;
	/** 产品详情 */
	private Map<String, Object> historyDetailMap;
	/** 交易日期 */
	private TextView tv_paymentDate_detail;
	/** 产品代码 */
	private TextView tv_prodCode_detail;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 交易类型 */
	private TextView tv_trfType_detail;
	/** 产品币种 */
	private TextView tv_curCode_detail;
	/** 交易份额 */
	private TextView tv_histrfAmount_detail;
	/** 成交价格 */
	private TextView tv_cjAmount_detail;
	/** 交易金额 */
	private TextView tv_payPrice_detail;
	// 按钮//////////////////////////////////
	/** 上一步 */
	private Button btn_pre;
	/** 确定 */
	private Button btn_canceled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.bocinvt_cancel_titile));
		// 添加布局
		view = addView(R.layout.bocinvt_myproduct_history_canceled_confirm);
		init();
		setBackBtnClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	/** 初始化界面 */
	private void init() {
		historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);
		tv_paymentDate_detail = (TextView) view
				.findViewById(R.id.tv_paymentDate_detail);
		tv_prodCode_detail = (TextView) view
				.findViewById(R.id.tv_prodCode_detail);
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		tv_trfType_detail = (TextView) view
				.findViewById(R.id.tv_trfType_detail);
		tv_curCode_detail = (TextView) view
				.findViewById(R.id.tv_curCode_detail);
		tv_histrfAmount_detail = (TextView) view
				.findViewById(R.id.tv_histrfAmount_detail);
		tv_cjAmount_detail = (TextView) view
				.findViewById(R.id.tv_cjAmount_detail);
		tv_payPrice_detail = (TextView) view
				.findViewById(R.id.tv_payPrice_detail);
		btn_pre = (Button) view.findViewById(R.id.btn_pre);
		btn_canceled = (Button) view.findViewById(R.id.btn_canceled);
		btn_pre.setOnClickListener(rightBtnClick);
		btn_canceled.setOnClickListener(canceledClick);
		// 赋值
		/** 交易日期 */
		tv_paymentDate_detail.setText(String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PAYMENTDATE_RES)));
		/** 产品代码 */
		tv_prodCode_detail.setText(String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PRODCODE_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PRODNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName_detail);
		/** 交易类型 */
		tv_trfType_detail.setText(LocalData.bociTrfTypeMap.get(String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES))));
		/** 产品币种 */
		String currency = String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES));
		if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
			if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
				/** 产品币种 */
				tv_curCode_detail.setText(LocalData.Currency.get(currency));
			} else {
				tv_curCode_detail
						.setText(LocalData.Currency.get(currency)
								+ LocalData.cashMapValue.get(String.valueOf(historyDetailMap
										.get(BocInvt.BOCINCT_XPADTRAD_CASHREMIT_RES))));
			}
		}
		/** 交易份额 */
		String histrfAmount = (String) historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_TRFAMOUNT_RES);
		tv_histrfAmount_detail.setText(StringUtil.parseStringPattern(
				histrfAmount, 2));
		/** 成交价格 */
		String cjAmount = (String) historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_TRFPRICE_RES);
		tv_cjAmount_detail.setText(StringUtil.parseStringCodePattern(currency,
				cjAmount, 2));
		/** 交易金额 */
		String payPrice = (String) historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES);
		tv_payPrice_detail.setText(StringUtil.parseStringCodePattern(currency,
				payPrice, 2));
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
		// 请求撤单确认
		requestXpadHoldProRedeem(tokenId);
	}

	/**
	 * 请求撤单确认
	 */
	public void requestXpadHoldProRedeem(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADCANCELTRAD_API);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(BocInvt.BOCINVT_CANCEL_XPADTRANSEQ_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_TRANSEQ_RES)));
		map.put(BocInvt.BOCINVT_CANCEL_AMOUNT_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES)));
		map.put(BocInvt.BOCINVT_CANCEL_ACCOUNTID_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINVT_XPADPRETRAD_ACCOUNTID_RES)));
		map.put(BocInvt.BOCINVT_CANCEL_XPADCODE_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_PRODCODE_RES)));
		map.put(BocInvt.BOCINVT_CANCEL_XPADNAME_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_PRODNAME_RES)));
		map.put(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES)));
		map.put(BocInvt.BOCINVT_CANCEL_XPADCASHREMIT_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_CASHREMIT_RES)));
		map.put(BocInvt.BOCINVT_CANCEL_TOKEN_REQ, tokenId);
		map.put(BocInvt.BOCINVT_CANCEL_STATUS_REQ, String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES)));
		map.put(BocInvt.IBKNUM, (String)historyDetailMap.get(BocInvt.IBKNUM));
		map.put(Comm.ACCOUNTNUMBER, (String)historyDetailMap.get(Comm.ACCOUNTNUMBER));
		map.put(BocInvt.BOCINCT_XPADTRAD_TRANATRR_RES, (String)historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRANATRR_RES));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestXpadHoldProRedeemCallback");
	}

	/**
	 * 请求撤单确认账户---回调
	 * 
	 * @param resultObj
	 */
	public void requestXpadHoldProRedeemCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {

		} else {
			// 得到result
			Map<String, Object> holdProRedeemMap = (Map<String, Object>) biiResponseBody
					.getResult();
			BaseDroidApp
					.getInstanse()
					.getBizDataMap()
					.put(ConstantGloble.BOCINVT_XPAD_CANCELED_MAP,
							holdProRedeemMap);
			Intent intent = new Intent(this,
					ProductCanceledSuccessActivity.class);
			startActivityForResult(intent, ACTIVITY_BUY_CODE);
		}

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};
	/** 提交撤单点击事件 */
	OnClickListener canceledClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			break;
		default:
			break;
		}
	}
}
