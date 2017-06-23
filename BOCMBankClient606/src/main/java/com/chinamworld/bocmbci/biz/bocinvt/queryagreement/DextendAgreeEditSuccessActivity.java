package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.HashMap;
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
 * 投资协议申请修改成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class DextendAgreeEditSuccessActivity extends BociBaseActivity {
	/** 协议申请页 */
	private View view;
	/** 协议序号 */
	private TextView tv_contractSeq;
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
	/** 产品详情 */
	private Map<String, Object> detailMap;
	/** 下一步 */
	private Button btn_next;
	/** 投资协议申请填写信息 */
	private Map<String, String> agreeInputMap = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_dingshi));
		// 添加布局
		view = addView(R.layout.boc_agree_ding_edit_success);
		back.setVisibility(View.GONE);
		// 界面初始化
		init();

	}

	/**
	 * 初始化界面
	 */
	private void init() {
		agreeInputMap = BociDataCenter.getInstance().getAgreeInputMap();
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		/** 协议序号 */
		tv_contractSeq = (TextView) view.findViewById(R.id.tv_contractSeq);
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
		tv_appendStartingAmount = (TextView) view
				.findViewById(R.id.tv_appendStartingAmount);
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
		tv_timeInvestType = (TextView) view
				.findViewById(R.id.tv_timeInvestType);
		ll_timeInvestType = (LinearLayout) view
				.findViewById(R.id.ll_timeInvestType);
		/** 定投金额 */
		tv_redeemAmount = (TextView) view.findViewById(R.id.tv_redeemAmount);
		ll_redeemAmount = (LinearLayout) view
				.findViewById(R.id.ll_redeemAmount);
		tv_redeempre = (TextView) view.findViewById(R.id.tv_redeempre);
		/** 定投频率 */
		tv_timeInvestRate = (TextView) view
				.findViewById(R.id.tv_timeInvestRate);
		ll_timeInvestRate = (LinearLayout) view
				.findViewById(R.id.ll_timeInvestRate);
		/** 定投开始日 */
		tv_investTime = (TextView) view.findViewById(R.id.tv_investTime);
		// 赋值
		tv_contractSeq.setText((String) agreeInputMap
				.get(BocInvt.BOC_AUTO_CONTRACTSEQ_REQ));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		tv_prodNum.setText(StringUtil.getForSixForString((String) detailMap
				.get(BocInvt.BANCACCOUNT)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodNum);
		tv_prodCode.setText((String) agreeInputMap
				.get(BocInvt.BOC_AUTO_SERIALCODE_REQ));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode);
		tv_prodName.setText((String) agreeInputMap
				.get(BocInvt.BOC_AUTO_SERIALNAME_REQ));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName);
		tv_buyStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) agreeInputMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				(String) detailMap.get(BocInvt.BOC_EXTEND_SUBPAMT_RES), 2));
		tv_appendStartingAmount.setText(StringUtil.parseStringCodePattern(
				(String) agreeInputMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				(String) detailMap.get(BocInvt.BOC_EXTEND_ADDAMT_RES), 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_appending);
		// 判断币种
		if (LocalData.Currency.get(
				(String) agreeInputMap.get(BocInvt.BOC_AUTO_CURCODE_REQ))
				.equals(ConstantGloble.ACC_RMB)) {
			tv_curCode.setText(LocalData.Currency.get((String) agreeInputMap
					.get(BocInvt.BOC_AUTO_CURCODE_REQ)));
		} else {
			tv_curCode.setText(LocalData.Currency.get((String) agreeInputMap
					.get(BocInvt.BOC_AUTO_CURCODE_REQ))
					+ agreeCashRemitMap.get(agreeInputMap
							.get(BocInvt.BOC_AUTO_CASHREMIT_REQ)));
		}
		String investType = agreeInputMap
				.get(BocInvt.BOC_AUTO_AGREEMENTTYPE_REQ);
		tv_investType.setText(investTypeMap.get(investType));
		// 定时定额
		/** 定投类型 */
		ll_timeInvestType.setVisibility(View.VISIBLE);
		/** 定投金额 */
		ll_redeemAmount.setVisibility(View.VISIBLE);
		/** 定投频率 */
		ll_timeInvestRate.setVisibility(View.VISIBLE);
		String timeInvestType = agreeInputMap
				.get(BocInvt.BOC_AUTO_PERIODTYPE_REQ);
		if (timeInvestType.equals(investTypeSubList.get(0))) {
			tv_redeempre.setText(this.getString(R.string.bocinvt_redeemAmount));
		} else {
			tv_redeempre.setText(this
					.getString(R.string.bocinvt_redeemAmount_1));
		}
		tv_timeInvestType.setText(timeInvestTypeMap.get(timeInvestType));

		String redeemAmount = agreeInputMap
				.get(BocInvt.BOC_AUTO_BASEAMOUNT_REQ);
		tv_redeemAmount.setText(StringUtil.parseStringCodePattern(
				(String) agreeInputMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				redeemAmount, 2));
		String timeInvestRate = agreeInputMap
				.get(BocInvt.BOC_AUTO_PERIODSEQ_REQ)
				+ timeInvestRateFlagSubMap.get(agreeInputMap
						.get(BocInvt.BOC_AUTO_PERIODSEQTYPE_REQ));
		tv_timeInvestRate.setText(timeInvestRate);
		tv_investTime.setText(agreeInputMap.get(BocInvt.BOC_AUTO_LASTDATE_REQ));
		TextView tv_finish_period = (TextView) view
				.findViewById(R.id.tv_finish_period);
		tv_finish_period.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIOD_RES));
		String period = agreeInputMap.get(BocInvt.BOC_AUTO_TOTALPERIOD_REQ);
		if (period.equals(NOPERIODSTR)) {
			// 不限期
			tv_totalPeriod.setText(this.getString(R.string.bocinvt_noperiod));
		} else {
			tv_totalPeriod.setText(period);
		}
		/** 网银交易序号 */
		TextView tv_transSeq = (TextView) view.findViewById(R.id.tv_transSeq);
		Map<String, Object> autoMap = BociDataCenter.getInstance()
				.getAutoResultMap();
		tv_transSeq.setText((String) autoMap
				.get(BocInvt.BOC_AUTO_TRANSACTIONID_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_transSeq);
		initListener();
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(
						DextendAgreeEditSuccessActivity.this,
						QueryAgreeActivity.class);
				intent.putExtra(ConstantGloble.ACC_POSITION, 1);
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
