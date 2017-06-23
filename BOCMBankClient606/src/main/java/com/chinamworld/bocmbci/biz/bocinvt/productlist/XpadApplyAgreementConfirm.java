package com.chinamworld.bocmbci.biz.bocinvt.productlist;

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
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 投资协议申请确认页面
 * 
 * @author wangmengmeng
 * 
 */
public class XpadApplyAgreementConfirm extends BociBaseActivity {
	/** 协议申请页 */
	private View view;
	/** 理财交易账户 */
	private TextView tv_prodNum;
	/** 产品代码 */
	private TextView tv_prodCode;
	/** 产品名称 */
	private TextView tv_prodName;
	/** 认购起点金额 */
	private TextView tv_buyStartingAmount;
	/** 追加认申购起点金额——后 */
	private TextView tv_appendStartingAmount;
	/** 追加认申购起点金额——前 */
	private TextView tv_appending;
	/** 币种 */
	private TextView tv_curCode;
	/** 投资方式 */
	private TextView tv_investType;
	/** 总期数 */
	private TextView tv_totalPeriod;
	// 定时定额显示
	/** 定投类型 */
	private TextView tv_timeInvestType;
	private LinearLayout ll_timeInvestType;
	/** 定投金额 */
	private TextView tv_redeemAmount;
	private LinearLayout ll_redeemAmount;
	private TextView tv_redeempre;
	/** 定投频率 */
	private TextView tv_timeInvestRate;
	private LinearLayout ll_timeInvestRate;
	/** 定投开始日 */
	private TextView tv_investTime;
	// 自动投资协议时显示
	/** 最低限额 */
	private TextView tv_minAmount;
	private LinearLayout ll_minAmount;
	/** 最高限额 */
	private TextView tv_maxAmount;
	private LinearLayout ll_maxAmount;
	/** 产品详情 */
	private Map<String, Object> detailMap;
	/** 下一步 */
	private Button btn_next;
	/** 投资协议申请填写信息 */
	private Map<String, String> agreeInputMap = new HashMap<String, String>();
	/** 标题 */
//	private TextView tv_title_confirm;
	/** 产品风险级别 */
	private TextView tv_pro_risk;
	/** 客户风险级别 */
	private TextView tv_cust_risk;
	/** 等级是否匹配 */
	private Map<String, Object> riskMatchMap;
	/** 底部提示语 */
	private TextView tv_confirm;
	private TextView tv_c_h;
	private Map<String, Object> acctMap;
//	private Map<String, Object> map_listview_choose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_apply_agree_title));
		// 添加布局
		view = addView(R.layout.boc_apply_agree_confirm);
		// 界面初始化
		init();

	}

	/**
	 * 初始化界面
	 */
	@SuppressWarnings("unchecked")
	private void init() {
//		map_listview_choose=BocInvestControl.map_listview_choose;
		agreeInputMap = BociDataCenter.getInstance().getAgreeInputMap();
		detailMap = BociDataCenter.getInstance().getDetailMap();
		acctMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP);
		riskMatchMap = BociDataCenter.getInstance().getRiskMatchMap();
//		tv_title_confirm = (TextView) view.findViewById(R.id.tv_title_confirm);
		/** 理财交易账户 */
		tv_prodNum = (TextView) view.findViewById(R.id.tv_number);
		/** 产品代码 */
		tv_prodCode = (TextView) view.findViewById(R.id.tv_prodCode);
		/** 产品名称 */
		tv_prodName = (TextView) view.findViewById(R.id.tv_prodName);
		/** 认购起点金额 */
		tv_buyStartingAmount = (TextView) view
				.findViewById(R.id.tv_buyStartingAmount);
		/** 追加认申购起点金额——后 */
		tv_appendStartingAmount = (TextView) view.findViewById(R.id.tv_appendStartingAmount);
		/** 追加认申购起点金额——前 */
		tv_appending = (TextView) view.findViewById(R.id.appendStrating);
		/** 币种 */
		tv_curCode = (TextView) view.findViewById(R.id.tv_curCode);
		/** 投资方式 */
		tv_investType = (TextView) view.findViewById(R.id.tv_investType);
		/** 总期数 */
		tv_totalPeriod = (TextView) view.findViewById(R.id.tv_totalPeriod);
		// 定时定额显示
		/** 定投类型 */
		tv_timeInvestType = (TextView) view.findViewById(R.id.tv_timeInvestType);
		ll_timeInvestType = (LinearLayout) view.findViewById(R.id.ll_timeInvestType);
		/** 定投金额 */
		tv_redeemAmount = (TextView) view.findViewById(R.id.tv_redeemAmount);
		ll_redeemAmount = (LinearLayout) view.findViewById(R.id.ll_redeemAmount);
		tv_redeempre = (TextView) view.findViewById(R.id.tv_redeempre);
		/** 定投频率 */
		tv_timeInvestRate = (TextView) view.findViewById(R.id.tv_timeInvestRate);
		ll_timeInvestRate = (LinearLayout) view.findViewById(R.id.ll_timeInvestRate);
		/** 定投开始日 */
		tv_investTime = (TextView) view.findViewById(R.id.tv_investTime);
		// 自动投资协议时显示
		/** 最低限额 */
		tv_minAmount = (TextView) view.findViewById(R.id.tv_minAmount);
		ll_minAmount = (LinearLayout) view.findViewById(R.id.ll_minAmount);
		/** 最高限额 */
		tv_maxAmount = (TextView) view.findViewById(R.id.tv_maxAmount);
		ll_maxAmount = (LinearLayout) view.findViewById(R.id.ll_maxAmount);
		tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		tv_c_h = (TextView) view.findViewById(R.id.tv_c_h);
		// 赋值
		tv_prodNum.setText(StringUtil.getForSixForString((String) acctMap
				.get(BocInvt.ACCOUNTNO)));
		tv_prodCode.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODCODE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode);
		tv_prodName.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName);
		tv_buyStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
				(String) detailMap
						.get(BocInvt.SUBAMOUNT), 2));
		tv_appendStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
				(String) detailMap
						.get(BocInvt.ADDAMOUNT), 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_appending);
		// 判断币种
//		if (LocalData.Currency.get(
//				(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES)).equals(
//				ConstantGloble.ACC_RMB)) {
			tv_curCode.setText(LocalData.Currency.get((String) detailMap
					.get(BocInvt.BOCI_DETAILCURCODE_RES)));
//		} else {
//			tv_curCode.setText(LocalData.Currency.get((String) detailMap
//					.get(BocInvt.BOCI_DETAILCURCODE_RES))
//					+ agreeCashRemitMap.get(agreeInputMap
//							.get(BocInvt.BOCINVT_AGREE_XPADCASHREMIT_REQ)));
//		}
		tv_c_h.setText(agreeCashRemitMap.get(agreeInputMap.get(BocInvt.BOCINVT_AGREE_XPADCASHREMIT_REQ)));
		String investType = agreeInputMap
				.get(BocInvt.BOCINVT_AGREE_INVESTTYPE_REQ);
		tv_investType.setText(investTypeMap.get(investType));
		if (investType.equals(investTypeSubList.get(0))) {
			// 定时定额
			/** 定投类型 */
			ll_timeInvestType.setVisibility(View.VISIBLE);
			/** 定投金额 */
			ll_redeemAmount.setVisibility(View.VISIBLE);
			/** 定投频率 */
			ll_timeInvestRate.setVisibility(View.VISIBLE);
			/** 最低限额 */
			ll_minAmount.setVisibility(View.GONE);
			/** 最高限额 */
			ll_maxAmount.setVisibility(View.GONE);
			String timeInvestType = agreeInputMap
					.get(BocInvt.BOCINVT_AGREE_TIMEINVESTTYPE_REQ);
			if (timeInvestType.equals(investTypeSubList.get(0))) {
				tv_redeempre.setText(this
						.getString(R.string.bocinvt_redeemAmount));
				String redeemAmount = agreeInputMap
						.get(BocInvt.BOCINVT_AGREE_REDEEMAMOUNT_REQ);
				tv_redeemAmount.setText(StringUtil.parseStringCodePattern(
						(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
						redeemAmount, 2));
			} else {
				tv_redeempre.setText(this
						.getString(R.string.bocinvt_redeemAmount_1));
				String redeemAmount = agreeInputMap
						.get(BocInvt.BOCINVT_AGREE_REDEEMAMOUNT_REQ);
				tv_redeemAmount.setText(StringUtil.parseStringPattern(
						redeemAmount, 2));
			}
			tv_timeInvestType.setText(timeInvestTypeMap.get(agreeInputMap
					.get(BocInvt.BOCINVT_AGREE_TIMEINVESTTYPE_REQ)));

			String timeInvestRate = agreeInputMap
					.get(BocInvt.BOCINVT_AGREE_TIMEINVESTRATE_REQ)
					+ timeInvestRateFlagSubMap.get(agreeInputMap
							.get(BocInvt.BOCINVT_AGREE_TIMEINVESTRATEFLAG_REQ));
			tv_timeInvestRate.setText(timeInvestRate);
		} else {
			// 自动投资
			/** 定投类型 */
			ll_timeInvestType.setVisibility(View.GONE);
			/** 定投金额 */
			ll_redeemAmount.setVisibility(View.GONE);
			/** 定投频率 */
			ll_timeInvestRate.setVisibility(View.GONE);
			/** 最低限额 */
			ll_minAmount.setVisibility(View.VISIBLE);
			/** 最高限额 */
			ll_maxAmount.setVisibility(View.VISIBLE);
			String minAmount = agreeInputMap
					.get(BocInvt.BOCINVT_AGREE_MINAMOUNT_REQ);
			String maxAmount = agreeInputMap
					.get(BocInvt.BOCINVT_AGREE_MAXAMOUNT_REQ);
			tv_minAmount.setText(StringUtil.parseStringCodePattern(
					(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
					minAmount, 2));
			tv_maxAmount.setText(StringUtil.parseStringCodePattern(
					(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
					maxAmount, 2));

		}
//		tv_title_confirm.setText(map_listview_choose.get(BocInvestControl.AGRNAME).toString()+getString(R.string.bocinvt_agree_confirm_title));
		/** 产品风险级别 */
		tv_pro_risk = (TextView) view.findViewById(R.id.tv_pro_risk);
		/** 客户风险级别 */
		tv_cust_risk = (TextView) view.findViewById(R.id.tv_cust_risk);
		tv_pro_risk.setText(LocalData.boci_prodRisklvlMap.get(String
				.valueOf(riskMatchMap.get(BocInvt.BOCINVT_MATCH_PRORISK_RES))));
		tv_cust_risk
				.setText(LocalData.riskLevelMap.get(String.valueOf(riskMatchMap
						.get(BocInvt.BOCINVT_MATCH_CUSTRISK_RES))));
		tv_investTime.setText(agreeInputMap
				.get(BocInvt.BOCINVT_AGREE_INVESTTIME_REQ));
		String period = agreeInputMap
				.get(BocInvt.BOCINVT_AGREE_TOTALPERIOD_REQ);
		if (period.equals(NOPERIODSTR)) {
			// 不限期
			tv_totalPeriod.setText(this.getString(R.string.bocinvt_noperiod));
		} else {
			tv_totalPeriod.setText(period);
		}
		// （极低风险级别显示1）（低风险级别显示2）（中等风险或更高级别显示3）
//		String type = (String) riskMatchMap.get(BocInvt.BOCINVT_MATCH_PRORISK_RES);
		String type = (String) riskMatchMap.get(BocInvt.BOCINVT_MATCH_RISKMSG_RES);
		if (type.equals(/*investTypeSubList.get(0)*/"0")) {
			tv_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_1));
		} else if (type.equals(/*investTypeSubList.get(1)*/"1")) {
			tv_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_2));
		} else {
			tv_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_3));
		}
		initListener();
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
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
		requestXpadApplyAgreementResult(tokenId);
	}

	/** 投资协议申请提交 */
	public void requestXpadApplyAgreementResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADAPPLYAGREEMENTRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		agreeInputMap.put(BocInvt.BOCINVT_SIGNRESULT_TOKENID_REQ, tokenId);
		agreeInputMap.put(Comm.ACCOUNT_ID, (String)acctMap.get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(agreeInputMap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestXpadApplyAgreementResultCallBack");
	}

	/** 投资协议申请提交 回调 */
	@SuppressWarnings("unchecked")
	public void requestXpadApplyAgreementResultCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		BociDataCenter.getInstance().setAgreeResultMap(resultMap);
		// TODO 进入成功信息页面
		Intent intent = new Intent(this,XpadApplyAgreementSuccessActivity.class);
		startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
//		startActivity(intent);
	}
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
