package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 周期性产品续约协议解约
 * 
 * @author wangmengmeng
 * 
 */
public class PeriodAgreeCancelActivity extends BociBaseActivity {
	/** 协议修改解约信息页 */
	private View view;
	/** 协议详情列表 */
	private Map<String, Object> detailMap;
	/** 协议序号 */
	private TextView tv_contractSeq;
	/** 产品系列名称 */
	private TextView tv_serialName;
	/** 币种/钞汇 */
	private TextView tv_curcode;
	/** 协议最大期数 */
	private TextView tv_maxPeriod;
	/** 购买期数 */
	private TextView tv_totalPeriod;
	/** 已续期数 */
	private TextView tv_period;
	/** 基础金额模式 */
	private TextView tv_amountTypeCode;
	/** 基础金额 */
	private TextView tv_baseAmount_contract;
	/** 最低预留金额 */
	private TextView tv_minAmount_contract;
	/** 最大扣款额金额 */
	private TextView tv_maxAmount_contract;
	/** 基础金额布局 */
	private LinearLayout ll_amountType_base;
	/** 最低预留金额布局 */
	private LinearLayout ll_amountType_min;
	/** 最大扣款额金额布局 */
	private LinearLayout ll_amountType_max;
	/** 下一步按钮 */
	private Button btn_next;
	/** 剩余期数 */
	private TextView tv_surplusPeriod;
	/** 开始期数 */
	private TextView tv_startPeriod;
	/** 结束期数 */
	private TextView tv_endPeriod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_zhouqi));
		// 添加布局
		view = addView(R.layout.boc_period_cancel_confirm);
		// 界面初始化
		init();
	}

	public void init() {
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		/** 协议序号 */
		tv_contractSeq = (TextView) view.findViewById(R.id.tv_contractSeq);
		/** 产品系列名称 */
		tv_serialName = (TextView) view.findViewById(R.id.tv_serialName);
		/** 币种/钞汇 */
		tv_curcode = (TextView) view.findViewById(R.id.tv_curcode);
		/** 协议最大期数 */
		tv_maxPeriod = (TextView) view.findViewById(R.id.tv_maxPeriod);
		/** 已续期数 */
		tv_period = (TextView) view.findViewById(R.id.tv_period);
		/** 基础金额模式 */
		tv_amountTypeCode = (TextView) view
				.findViewById(R.id.tv_amountTypeCode);
		ll_amountType_base = (LinearLayout) view
				.findViewById(R.id.amountType_base);
		ll_amountType_min = (LinearLayout) view
				.findViewById(R.id.amountType_min);
		ll_amountType_max = (LinearLayout) view
				.findViewById(R.id.amountType_max);
		tv_totalPeriod = (TextView) view.findViewById(R.id.tv_totalPeriod);
		tv_baseAmount_contract = (TextView) view
				.findViewById(R.id.tv_baseAmount);
		tv_minAmount_contract = (TextView) view.findViewById(R.id.tv_minAmount);
		tv_maxAmount_contract = (TextView) view.findViewById(R.id.tv_maxAmount);
		/** 剩余期数 */
		tv_surplusPeriod = (TextView) view.findViewById(R.id.tv_surplusPeriod);
		/** 开始期数 */
		tv_startPeriod = (TextView) view.findViewById(R.id.tv_startPeriod);
		/** 结束期数 */
		tv_endPeriod = (TextView) view.findViewById(R.id.tv_endPeriod);
		// 赋值
		tv_contractSeq.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_CONTRACTSEQ_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		tv_serialName.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_SERIALNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_serialName);
		// 判断币种
		if (!StringUtil.isNull((String) detailMap
				.get(BocInvt.BOC_AUTO_CURCODE_REQ))) {
			if (LocalData.Currency.get(
					(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ))
					.equals(ConstantGloble.ACC_RMB)) {
				tv_curcode.setText(LocalData.Currency.get((String) detailMap
						.get(BocInvt.BOC_AUTO_CURCODE_REQ)));
			} else {
				tv_curcode.setText(LocalData.Currency.get((String) detailMap
						.get(BocInvt.BOC_AUTO_CURCODE_REQ))
						+ agreeCashRemitMap.get((String) detailMap
								.get(BocInvt.BOC_QUERY_AGREE_CASHREMIT_RES)));
			}
		}
		String amountType = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_AMOUNTTYPE_RES);
		if (amountType.equals(LocalData.orderTimeMap.get(0))) {
			// 定额
			ll_amountType_base.setVisibility(View.VISIBLE);
			ll_amountType_min.setVisibility(View.GONE);
			ll_amountType_max.setVisibility(View.GONE);
		} else {
			ll_amountType_base.setVisibility(View.GONE);
			ll_amountType_min.setVisibility(View.VISIBLE);
			ll_amountType_max.setVisibility(View.VISIBLE);
		}
		tv_amountTypeCode.setText(LocalData.bociAmountTypeMap.get(amountType));
		String maxPeriod = String.valueOf(Integer.valueOf((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_SERIALPRDCT_RES))
				- Integer.valueOf((String) detailMap
						.get(BocInvt.BOC_QUERY_AGREE_STARTPERIOD_RES)) + 1);
		tv_maxPeriod.setText(maxPeriod);
		tv_period.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_PERIOD_RES));
		String totalPeriod = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_TOTALPERIOD_RES);
		tv_totalPeriod.setText(totalPeriod);
		String baseAmount = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_BASEAMOUNT_RES);
		if (!StringUtil.isNull(baseAmount)) {
			tv_baseAmount_contract.setText(StringUtil.parseStringCodePattern(
					(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
					baseAmount, 2));
		}
		String minAmount = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_MINAMOUNT_RES);
		if (!StringUtil.isNull(minAmount)) {
			tv_minAmount_contract.setText(StringUtil.parseStringCodePattern(
					(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
					minAmount, 2));
		}
		String maxAmount = (String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_MAXAMOUNT_RES);
		if (!StringUtil.isNull(maxAmount)) {
			tv_maxAmount_contract.setText(StringUtil.parseStringCodePattern(
					(String) detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
					maxAmount, 2));
		}
		tv_surplusPeriod.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_SURPLUSPERIOD_RES));
		tv_startPeriod.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_STARTPERIOD_RES));
		tv_endPeriod.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_ENDPERIOD_RES));

		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestCommConversationId();
				BiiHttpEngine.showProgressDialog();
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
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPsnXpadAgreementCancel(tokenId);
	}

	/**
	 * 周期性产品续约协议解约
	 */
	public void requestPsnXpadAgreementCancel(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADAGREEMENTCANCEL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		// 产品系列名称
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_SERIALNAME_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_SERIALNAME_RES));
		// 购买期数
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_TOTALPERIOD_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_TOTALPERIOD_RES));
		// 基础金额模式
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_AMOUNTTYPE_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_AMOUNTTYPE_RES));
		// 获取币种
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_CURRENCYCODE_REQ,
				detailMap.get(BocInvt.BOC_AUTO_CURCODE_REQ));
		// 钞汇标志
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_CASHREMIT_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_CASHREMIT_RES));
		// 基础金额
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_BASEAMOUNT_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_BASEAMOUNT_RES));
		// 最低预留金额
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_MINAMOUNT_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_MINAMOUNT_RES));
		// 最大扣款金额
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_MAXAMOUNT_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_MAXAMOUNT_RES));
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_CONTRACTSEQ_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_CONTRACTSEQ_RES));
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_TOKEN_REQ, tokenId);
		paramsmap.put(Comm.ACCOUNTNUMBER, detailMap.get(BocInvt.BANCACCOUNT));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAgreementCancelCallBack");
	}

	/** 解约周期性产品续约协议 回调 */
	public void requestPsnXpadAgreementCancelCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		BociDataCenter.getInstance().setEditResultMap(resultMap);
		Intent intent = new Intent(this, PeriodAgreeCancelSuccessActivity.class);
		startActivity(intent);
	}
}
