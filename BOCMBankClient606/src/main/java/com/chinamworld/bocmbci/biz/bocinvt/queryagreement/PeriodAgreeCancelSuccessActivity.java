package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 周期性产品续约协议解约成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class PeriodAgreeCancelSuccessActivity extends BociBaseActivity {
	/** 协议解约成功信息页 */
	private View view;
	/** 协议详情列表 */
	private Map<String, Object> detailMap;
	/** 网银交易序号 */
	private TextView tv_transSeq;
	/** 协议序号 */
	private TextView tv_contractSeq;
	/** 协议修改日期 */
	private TextView tv_operateDate;
	/** 产品系列名称 */
	private TextView tv_serialName;
	/** 币种/钞汇 */
	private TextView tv_curcode;
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
	private Map<String, Object> resultMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_zhouqi));
		// 添加布局
		view = addView(R.layout.boc_period_cancel_success);
		back.setVisibility(View.GONE);
		// 界面初始化
		init();
	}

	private void init() {
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		resultMap = BociDataCenter.getInstance().getEditResultMap();
		/** 网银交易序号 */
		tv_transSeq = (TextView) view.findViewById(R.id.tv_transSeq);
		/** 协议序号 */
		tv_contractSeq = (TextView) view.findViewById(R.id.tv_contractSeq);
		/** 协议解约日期 */
		tv_operateDate = (TextView) view.findViewById(R.id.tv_operateDate);
		/** 产品系列名称 */
		tv_serialName = (TextView) view.findViewById(R.id.tv_serialName);
		/** 币种/钞汇 */
		tv_curcode = (TextView) view.findViewById(R.id.tv_curcode);
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

		// 赋值
		tv_transSeq.setText((String) resultMap
				.get(BocInvt.BOC_AGREE_CANCEL_TRANSACTIONID_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_transSeq);
		tv_contractSeq.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_CONTRACTSEQ_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		tv_operateDate.setText((String) resultMap
				.get(BocInvt.BOC_AGREE_CANCEL_OPERATEDATE_RES));
		tv_serialName.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_SERIALNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_serialName);
		// 判断币种
		if (!StringUtil.isNull(LocalData.Currency.get((String) detailMap
				.get(BocInvt.BOC_AUTO_CURCODE_REQ)))) {
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
		/** 剩余期数 */
		tv_surplusPeriod = (TextView) view.findViewById(R.id.tv_surplusPeriod);
		/** 开始期数 */
		tv_startPeriod = (TextView) view.findViewById(R.id.tv_startPeriod);
		/** 结束期数 */
		tv_endPeriod = (TextView) view.findViewById(R.id.tv_endPeriod);
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
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(
						PeriodAgreeCancelSuccessActivity.this,
						QueryAgreeActivity.class);
				startActivity(intent);
				finish();

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
