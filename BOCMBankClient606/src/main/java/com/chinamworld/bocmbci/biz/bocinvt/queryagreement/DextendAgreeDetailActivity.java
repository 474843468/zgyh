package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.ArrayList;
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
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 定时定额协议详情
 * 
 * @author wangmengmeng
 * 
 */
public class DextendAgreeDetailActivity extends BociBaseActivity {
	/** 协议详情信息页 */
	private View view;
	/** 协议详情列表 */
	private Map<String, Object> detailMap;
	/** 理财交易账户 */
	private TextView tv_number;
	/** 协议序号 */
	private TextView tv_contractSeq;
	/** 产品代码 */
	private TextView tv_prodCode;
	/** 产品名称 */
	private TextView tv_prodName;
	/** 币种/钞汇 */
	private TextView tv_curCode;
	/** 投资方式 */
	private TextView tv_investType;
	/** 定投类型 */
	private TextView tv_timeInvestType;
	/** 总期数 */
	private TextView tv_totalPeriod;
	/** 定投金额 */
	private TextView tv_redeemAmount;
	/** 定投频率 */
	private TextView tv_timeInvestRate;
	/** 定投日期 */
	private TextView tv_investTime;
	/** 本期状态 */
	private TextView tv_lastStatus;
	/** 投资状态 */
	private TextView tv_contStatus;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_dingshi));
		// 添加布局
		view = addView(R.layout.boc_dingshi_detail);
		// 界面初始化
		init();
	}

	public void init() {
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		/** 理财交易账户 */
		tv_number = (TextView) view.findViewById(R.id.tv_number);
		/** 协议序号 */
		tv_contractSeq = (TextView) view.findViewById(R.id.tv_contractSeq);
		/** 产品代码 */
		tv_prodCode = (TextView) view.findViewById(R.id.tv_prodCode);
		/** 产品名称 */
		tv_prodName = (TextView) view.findViewById(R.id.tv_prodName);
		/** 币种/钞汇 */
		tv_curCode = (TextView) view.findViewById(R.id.tv_curCode);
		/** 投资方式 */
		tv_investType = (TextView) view.findViewById(R.id.tv_investType);
		/** 定投类型 */
		tv_timeInvestType = (TextView) view
				.findViewById(R.id.tv_timeInvestType);
		/** 总期数 */
		tv_totalPeriod = (TextView) view.findViewById(R.id.tv_totalPeriod);
		/** 定投金额 */
		tv_redeemAmount = (TextView) view.findViewById(R.id.tv_redeemAmount);
		/** 定投频率 */
		tv_timeInvestRate = (TextView) view
				.findViewById(R.id.tv_timeInvestRate);
		/** 定投日期 */
		tv_investTime = (TextView) view.findViewById(R.id.tv_investTime);
		/** 本期状态 */
		tv_lastStatus = (TextView) view.findViewById(R.id.tv_lastStatus);
		/** 投资状态 */
		tv_contStatus = (TextView) view.findViewById(R.id.tv_contStatus);
		// 赋值
		tv_number.setText(StringUtil.getForSixForString((String) detailMap
				.get(BocInvt.BANCACCOUNT)));
		tv_contractSeq.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_CONTRACTSEQ_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		tv_prodCode.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_SERIALCODE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode);
		tv_prodName.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_SERIALNAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName);
		// 判断币种
		if (!StringUtil.isNull(LocalData.Currency.get((String) detailMap
				.get(BocInvt.BOC_EXTEND_PROCUR_RES)))) {
			if (LocalData.Currency.get(
					(String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES))
					.equals(ConstantGloble.ACC_RMB)) {
				tv_curCode.setText(LocalData.Currency.get((String) detailMap
						.get(BocInvt.BOC_EXTEND_PROCUR_RES)));
			} else {
				tv_curCode.setText(LocalData.Currency.get((String) detailMap
						.get(BocInvt.BOC_EXTEND_PROCUR_RES))
						+ agreeCashRemitMap.get((String) detailMap
								.get(BocInvt.BOC_EXTEND_CASHREMIT_RES)));
			}
		}
		TextView tv_redeempre = (TextView) view.findViewById(R.id.tv_redeempre);
		tv_investType.setText(investTypeMap.get("0"));
		tv_timeInvestType.setText(timeInvestTypeMap.get((String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIODTYPE_RES)));
		String timeInvestType = (String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIODTYPE_RES);
		if (timeInvestType.equals(investTypeSubList.get(0))) {
			tv_redeempre.setText(this.getString(R.string.bocinvt_redeemAmount));
		} else {
			tv_redeempre.setText(this
					.getString(R.string.bocinvt_redeemAmount_1));
		}
		String period = (String) detailMap
				.get(BocInvt.BOC_EXTEND_TOTALPERIOD_RES);
		if (!StringUtil.isNull(period)) {
			if (period.equals(NOPERIODSTR)) {
				// 不限期
				tv_totalPeriod.setText(this
						.getString(R.string.bocinvt_noperiod));
			} else {
				tv_totalPeriod.setText(period);
			}
		}

		tv_redeemAmount.setText(StringUtil.parseStringCodePattern(
				(String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES),
				(String) detailMap.get(BocInvt.TRANSAMOUNT), 2));
		tv_timeInvestRate.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIODSEQ_RES)
				+ timeInvestRateFlagSubMap.get((String) detailMap
						.get(BocInvt.BOC_EXTEND_PERIODSEQTYPE_RES)));
		tv_investTime.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_FIRSTDATE_RES));
		tv_lastStatus.setText(lastStatusMap.get((String) detailMap
				.get(BocInvt.BOC_EXTEND_LASTSTATUS_RES)));
		tv_contStatus.setText(contStatusMap.get((String) detailMap
				.get(BocInvt.BOC_EXTEND_CONTSTATUS_RES)));
		Button btn_one = (Button) view.findViewById(R.id.btn_one);
		Button btn_many = (Button) view.findViewById(R.id.btn_many);
		List<String> serlist = new ArrayList<String>();
		// 是否可修改
		String canUpdate = (String) detailMap
				.get(BocInvt.BOC_EXTEND_CANUPDATE_RES);
		// 是否可暂停
		String canPause = (String) detailMap
				.get(BocInvt.BOC_EXTEND_CANPAUSE_RES);
		// 是否可开始
		String canStart = (String) detailMap
				.get(BocInvt.BOC_EXTEND_CANSTART_RES);
		// 是否可撤销
		String canCancel = (String) detailMap
				.get(BocInvt.BOC_EXTEND_CANCANCEL_RES);

		if (!StringUtil.isNull(canUpdate)) {
			if (canUpdate.equals(investTypeSubList.get(0))) {
				serlist.add(bocSerlistList.get(0));
			}
		}
		if (!StringUtil.isNull(canPause)) {
			if (canPause.equals(investTypeSubList.get(0))) {
				serlist.add(bocSerlistList.get(1));
			}
		}
		if (!StringUtil.isNull(canStart)) {
			if (canStart.equals(investTypeSubList.get(0))) {
				serlist.add(bocSerlistList.get(2));
			}
		}
		if (!StringUtil.isNull(canCancel)) {
			if (canCancel.equals(investTypeSubList.get(0))) {
				serlist.add(bocSerlistList.get(3));
			}
		}

		if (serlist == null || serlist.size() == 0) {
			btn_one.setVisibility(View.GONE);
			btn_many.setVisibility(View.GONE);
		} else if (serlist.size() == 1) {
			btn_one.setVisibility(View.VISIBLE);
			btn_many.setVisibility(View.GONE);
			btn_one.setText(serlist.get(0));
			btn_one.setOnClickListener(btnMoreNullBocinvtClick);
		} else if (serlist.size() == 2) {
			btn_one.setVisibility(View.VISIBLE);
			btn_many.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many
					.getLayoutParams();
			btn_one.setLayoutParams(param);
			btn_one.setText(serlist.get(0));
			btn_one.setOnClickListener(btnMoreNullBocinvtClick);
			btn_many.setText(serlist.get(1));
			btn_many.setOnClickListener(btnMoreNullBocinvtClick);
		} else {
			btn_one.setVisibility(View.VISIBLE);
			btn_many.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_many
					.getLayoutParams();
			btn_one.setLayoutParams(param);
			btn_one.setText(serlist.get(0));
			btn_one.setOnClickListener(btnMoreNullBocinvtClick);
			btn_many.setText(BaseDroidApp.getInstanse().getCurrentAct()
					.getString(R.string.more));
			String[] service = new String[serlist.size() - 1];
			for (int k = 0; k < serlist.size() - 1; k++) {
				service[k] = serlist.get(k + 1);
			}
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
					BaseDroidApp.getInstanse().getCurrentAct(), btn_many,
					service, btnMoreNullBocinvtClick);
		}
	}

	/** 更多点击事件 */
	final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			moreClick(text, v);
		}
	};

	/** 判断点击事件 */
	public void moreClick(String text, View v) {
		if (text.equals(bocSerlistList.get(0))) {
			// 修改
			requestSystemDateTime();
			BaseHttpEngine.showProgressDialog();
		}
		if (text.equals(bocSerlistList.get(2))) {
			// 开始
			Intent intent = new Intent(DextendAgreeDetailActivity.this,
					DextendAgreeConfirmActivity.class);
			intent.putExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ,
					bocSerListUp.get(2));
			startActivity(intent);
		}
		if (text.equals(bocSerlistList.get(1))) {
			// 暂停
			Intent intent = new Intent(DextendAgreeDetailActivity.this,
					DextendAgreeConfirmActivity.class);
			intent.putExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ,
					bocSerListUp.get(1));
			startActivity(intent);
		}
		if (text.equals(bocSerlistList.get(3))) {
			// 撤销
			Intent intent = new Intent(DextendAgreeDetailActivity.this,
					DextendAgreeConfirmActivity.class);
			intent.putExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ,
					bocSerListUp.get(3));
			startActivity(intent);
		}
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (!StringUtil.isNull(dateTime)) {
			Intent intent = new Intent(DextendAgreeDetailActivity.this,
					DextendAgreeEditInputActivity.class);
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			startActivity(intent);

		}

	}
}
