package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
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
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 投资协议申请成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class XpadApplyAgreementSuccessActivity extends BociBaseActivity {
	/** 协议申请页 */
	private View view;
	/** 网银交易序号 */
	private TextView tv_transSeq;
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
	// 自动投资协议时显示
	/** 最低限额 */
	private TextView tv_minAmount;
	private LinearLayout ll_minAmount;
	/** 最高限额 */
	private TextView tv_maxAmount;
	private TextView tv_c_h;
	private LinearLayout ll_maxAmount;
	/** 产品详情 */
	private Map<String, Object> detailMap;
	/** 下一步 */
	private Button btn_next;
	/** 投资协议申请成功信息 */
	private Map<String, Object> agreeResultMap = new HashMap<String, Object>();
	/** 投资协议申请填写信息 */
	private Map<String, String> agreeInputMap = new HashMap<String, String>();
	private Map<String, Object> map_listview_choose;
	/** 标题 */
	private TextView tv_title_confirm;
	private Map<String, Object> acctMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_apply_agree_title));
		// 添加布局
		view = addView(R.layout.boc_apply_agree_success);
		back.setVisibility(View.GONE);
		// 界面初始化
		init();

	}

	/**
	 * 初始化界面
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		map_listview_choose=BocInvestControl.map_listview_choose;
		agreeResultMap = BociDataCenter.getInstance().getAgreeResultMap();
		agreeInputMap = BociDataCenter.getInstance().getAgreeInputMap();
		detailMap = BociDataCenter.getInstance().getDetailMap();
		acctMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP);
		tv_title_confirm = (TextView) view.findViewById(R.id.tv_title_confirm);
		/** 网银交易序号 */
		tv_transSeq = (TextView) view.findViewById(R.id.tv_transSeq);
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
		// 自动投资协议时显示
		/** 最低限额 */
		tv_minAmount = (TextView) view.findViewById(R.id.tv_minAmount);
		ll_minAmount = (LinearLayout) view.findViewById(R.id.ll_minAmount);
		/** 最高限额 */
		tv_maxAmount = (TextView) view.findViewById(R.id.tv_maxAmount);
		tv_c_h = (TextView) view.findViewById(R.id.tv_c_h);
		ll_maxAmount = (LinearLayout) view.findViewById(R.id.ll_maxAmount);
		// 赋值
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_transSeq);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodNum);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode);
		tv_transSeq.setText((String) agreeResultMap
				.get(BocInvt.BOCINVT_AGREE_TRANSSEQ_RES));
		tv_contractSeq.setText((String) agreeResultMap
				.get(BocInvt.BOCINVT_AGREE_CONTRACTSEQ_RES));
		tv_prodNum.setText(StringUtil.getForSixForString((String) acctMap
				.get(BocInvt.ACCOUNTNO)));
		tv_prodCode.setText((String) detailMap
				.get(BocInvt.BOCI_DETAILPRODCODE_RES));
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
		String investType = (String) agreeResultMap
				.get(BocInvt.BOCINVT_AGREE_CONTAMTMODE_RES);
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
			} else {
				tv_redeempre.setText(this
						.getString(R.string.bocinvt_redeemAmount_1));
			}
			tv_timeInvestType.setText(timeInvestTypeMap.get(agreeInputMap
					.get(BocInvt.BOCINVT_AGREE_TIMEINVESTTYPE_REQ)));

			String redeemAmount = agreeInputMap
					.get(BocInvt.BOCINVT_AGREE_REDEEMAMOUNT_REQ);
			tv_redeemAmount.setText(StringUtil.parseStringCodePattern(
					(String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES),
					redeemAmount, 2));
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
		tv_title_confirm.setText(map_listview_choose.get(BocInvestControl.AGRNAME).toString()+getString(R.string.bocinvt_agree_success_title));
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
				setResult(RESULT_OK);
				finish();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(
//						XpadApplyAgreementSuccessActivity.this,
//						QueryProductActivity.class);
//				startActivity(intent);
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
