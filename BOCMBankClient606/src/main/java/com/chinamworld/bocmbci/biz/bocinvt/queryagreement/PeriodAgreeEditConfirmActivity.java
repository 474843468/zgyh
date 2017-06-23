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
 * 周期性产品续约协议修改确认页面
 * 
 * @author wangmengmeng
 * 
 */
public class PeriodAgreeEditConfirmActivity extends BociBaseActivity {
	/** 协议修改输入信息页 */
	private View view;
	/** 协议详情列表 */
	private Map<String, Object> detailMap;
	/** 等级是否匹配 */
	private Map<String, Object> riskMatchMap;
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
	/** 产品风险级别 */
	private TextView tv_pro_risk;
	/** 客户风险级别 */
	private TextView tv_cust_risk;
	/** 输入信息 */
	Map<String, Object> inputMap = new HashMap<String, Object>();
	/** 是否是周期连续产品 */
	private Map<String, Object> modifyMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_zhouqi));
		// 添加布局
		view = addView(R.layout.boc_period_edit_confirm);
		// 界面初始化
		init();
	}

	private void init() {
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		riskMatchMap = BociDataCenter.getInstance().getRiskMatchMap();
		inputMap = BociDataCenter.getInstance().getInputResultMap();
		modifyMap = BociDataCenter.getInstance().getPeriodModifyMap();
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
		// 输入项
		tv_totalPeriod = (TextView) view.findViewById(R.id.tv_totalPeriod);
		tv_baseAmount_contract = (TextView) view
				.findViewById(R.id.tv_baseAmount);
		tv_minAmount_contract = (TextView) view.findViewById(R.id.tv_minAmount);
		tv_maxAmount_contract = (TextView) view.findViewById(R.id.tv_maxAmount);
		/** 产品风险级别 */
		tv_pro_risk = (TextView) view.findViewById(R.id.tv_pro_risk);
		/** 客户风险级别 */
		tv_cust_risk = (TextView) view.findViewById(R.id.tv_cust_risk);
		tv_pro_risk.setText(LocalData.boci_prodRisklvlMap.get(String
				.valueOf(riskMatchMap.get(BocInvt.BOCINVT_MATCH_PRORISK_RES))));
		tv_cust_risk
				.setText(LocalData.riskLevelMap.get(String.valueOf(riskMatchMap
						.get(BocInvt.BOCINVT_MATCH_CUSTRISK_RES))));
		TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		// （极低风险级别显示1）（低风险级别显示2）（中等风险或更高级别显示3）
		String type = (String) riskMatchMap
				.get(BocInvt.BOCINVT_MATCH_RISKMSG_RES);
		if(!StringUtil.isNullOrEmpty(type)){
		if (type.equals(investTypeSubList.get(0))) {
			tv_confirm.setText(this
					.getString(R.string.bocinvt_confirm_bottom_1));
		} else if (type.equals(investTypeSubList.get(1))) {
			tv_confirm.setText(this
					.getString(R.string.bocinvt_confirm_bottom_2));
		} else {
			tv_confirm.setText(this
					.getString(R.string.bocinvt_confirm_bottom_3));
		}}
		// 赋值
		tv_contractSeq.setText((String) inputMap
				.get(BocInvt.BOC_MODIFY_RES_CONTRACTSEQ_REQ));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		tv_serialName.setText((String) inputMap
				.get(BocInvt.BOC_MODIFY_RES_SERIALNAME_REQ));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_serialName);
		// 判断币种
		if (LocalData.Currency.get(
				(String) inputMap.get(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ))
				.equals(ConstantGloble.ACC_RMB)) {
			tv_curcode.setText(LocalData.Currency.get((String) inputMap
					.get(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ)));
		} else {
			tv_curcode.setText(LocalData.Currency.get((String) inputMap
					.get(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ))
					+ agreeCashRemitMap.get((String) inputMap
							.get(BocInvt.BOC_MODIFY_RES_XPADCASHREMIT_REQ)));
		}
		String amountType = (String) inputMap
				.get(BocInvt.BOC_MODIFY_RES_AMOUNTTYPECODE_REQ);
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
		tv_maxPeriod.setText((String) modifyMap
				.get(BocInvt.BOC_MODIFY_MAXPERIOD_RES));
		tv_period.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_PERIOD_RES));
		String totalPeriod = (String) inputMap
				.get(BocInvt.BOC_MODIFY_RES_TOTALPERIOD_REQ);
		tv_totalPeriod.setText(totalPeriod);
		String baseAmount = (String) inputMap
				.get(BocInvt.BOC_MODIFY_RES_BASEAMOUNT_REQ);
		if (!StringUtil.isNull(baseAmount)) {
			tv_baseAmount_contract.setText(StringUtil.parseStringCodePattern(
					(String) inputMap
							.get(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ),
					baseAmount, 2));
		}
		String minAmount = (String) inputMap
				.get(BocInvt.BOC_MODIFY_RES_MINAMOUNT_REQ);
		if (!StringUtil.isNull(minAmount)) {
			tv_minAmount_contract.setText(StringUtil.parseStringCodePattern(
					(String) inputMap
							.get(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ),
					minAmount, 2));
		}
		String maxAmount = (String) inputMap
				.get(BocInvt.BOC_MODIFY_RES_MAXAMOUNT_REQ);
		if (!StringUtil.isNull(maxAmount)) {
			tv_maxAmount_contract.setText(StringUtil.parseStringCodePattern(
					(String) inputMap
							.get(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ),
					maxAmount, 2));
		}

		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
		requestPsnXpadAgreementModifyResult(tokenId);
	}

	/** 修改周期性产品续约协议提交 */
	public void requestPsnXpadAgreementModifyResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADAGREEMENTMODIFYRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		inputMap.put(BocInvt.BOC_MODIFY_RES_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(inputMap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAgreementModifyResultCallBack");
	}

	/** 修改周期性产品续约协议 回调 */
	public void requestPsnXpadAgreementModifyResultCallBack(Object resultObj) {
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
		// TODO 进入成功页面
		Intent intent = new Intent(this, PeriodAgreeEditSuccessActivity.class);
		startActivity(intent);
	}
}
