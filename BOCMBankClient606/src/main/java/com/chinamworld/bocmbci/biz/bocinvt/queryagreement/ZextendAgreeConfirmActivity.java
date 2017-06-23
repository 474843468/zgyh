package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 自动投资协议开始、暂停、撤销页面
 * 
 * @author wangmengmeng
 * 
 */
public class ZextendAgreeConfirmActivity extends BociBaseActivity {
	/** 协议维护页 */
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
	/** 定投开始日 */
	private TextView tv_investTime;
	/** 产品详情 */
	private Map<String, Object> detailMap;
	/** 下一步 */
	private Button btn_next;
	/** 最低限额 */
	private String minAmount;
	/** 最高限额 */
	private String maxAmount;
	/** 最低限额 */
	private TextView tv_minAmount;
	/** 最高限额 */
	private TextView tv_maxAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_zidong));
		// 添加布局
		view = addView(R.layout.boc_agree_zidong_confirm);
		// 界面初始化
		init();

	}

	/**
	 * 初始化界面
	 */
	private void init() {
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		Map<String, String> agreeInputMap = new HashMap<String, String>();
		agreeInputMap.put(BocInvt.BOC_AUTO_CONTRACTSEQ_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_CONTRACTSEQ_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_SERIALCODE_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_SERIALCODE_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_SERIALNAME_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_SERIALNAME_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_CURCODE_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_PROCUR_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_CASHREMIT_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_CASHREMIT_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_AGREEMENTTYPE_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_CONTAMTMODE_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ, this.getIntent()
				.getStringExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ));
		agreeInputMap.put(BocInvt.BOC_AUTO_PERIODTYPE_REQ,
				ConstantGloble.BOCINVT_NULL_STRING);
		agreeInputMap.put(BocInvt.BOC_AUTO_TOTALPERIOD_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_TOTALPERIOD_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_BASEAMOUNT_REQ,
				ConstantGloble.BOCINVT_NULL_STRING);
		agreeInputMap.put(BocInvt.BOC_AUTO_PERIODSEQ_REQ,
				ConstantGloble.BOCINVT_NULL_STRING);
		agreeInputMap.put(BocInvt.BOC_AUTO_PERIODSEQTYPE_REQ,
				ConstantGloble.BOCINVT_NULL_STRING);
		dateTime = (String) detailMap.get(BocInvt.BOC_EXTEND_VALUEDATE_RES);
		agreeInputMap.put(BocInvt.BOC_AUTO_LASTDATE_REQ, dateTime);
		agreeInputMap.put(BocInvt.BOC_AUTO_MINAMOUNT_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_MINAMOUNT_RES));
		agreeInputMap.put(BocInvt.BOC_AUTO_MAXAMOUNT_REQ,
				(String) detailMap.get(BocInvt.BOC_EXTEND_MAXAMOUNT_RES));
		BociDataCenter.getInstance().setAgreeInputMap(agreeInputMap);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title_confirm);
		tv_title.setText(zidongeditConfirmList.get(Integer.valueOf(this
				.getIntent().getStringExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ))));
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
		/** 最低限额 */
		tv_minAmount = (TextView) view.findViewById(R.id.tv_minAmount);
		/** 最高限额 */
		tv_maxAmount = (TextView) view.findViewById(R.id.tv_maxAmount);
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
		tv_investType.setText(investTypeMap.get("1"));
		tv_investTime.setText(agreeInputMap.get(BocInvt.BOC_AUTO_LASTDATE_REQ));
		String period = agreeInputMap.get(BocInvt.BOC_AUTO_TOTALPERIOD_REQ);
		if (period.equals(NOPERIODSTR)) {
			// 不限期
			tv_totalPeriod.setText(this.getString(R.string.bocinvt_noperiod));
		} else {
			tv_totalPeriod.setText(period);
		}
		TextView tv_finish_period = (TextView) view
				.findViewById(R.id.tv_finish_period);
		tv_finish_period.setText((String) detailMap
				.get(BocInvt.BOC_EXTEND_PERIOD_RES));
		minAmount = agreeInputMap.get(BocInvt.BOC_AUTO_MINAMOUNT_REQ);
		tv_minAmount.setText(StringUtil.parseStringCodePattern(
				(String) agreeInputMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				minAmount, 2));
		maxAmount = agreeInputMap.get(BocInvt.BOC_AUTO_MAXAMOUNT_REQ);
		tv_maxAmount.setText(StringUtil.parseStringCodePattern(
				(String) agreeInputMap.get(BocInvt.BOC_AUTO_CURCODE_REQ),
				maxAmount, 2));
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
				requestCommConversationId();
				BiiHttpEngine.showProgressDialog();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestPsnXpadAutomaticAgreementMaintainResult("1");
	}

	@Override
	public void requestPsnXpadAutomaticAgreementMaintainResultCallBack(
			Object resultObj) {
		super.requestPsnXpadAutomaticAgreementMaintainResultCallBack(resultObj);
		// CustomDialog.toastShow(this, zidongeditSuccessList.get(Integer
		// .valueOf(this.getIntent().getStringExtra(
		// BocInvt.BOC_AUTO_MAINTAINFLAG_REQ))));
		// 进入成功页面
		Intent intent = new Intent(this, ZextendAgreeSuccessActivity.class);
		intent.putExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ, this.getIntent()
				.getStringExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ));
		startActivity(intent);
	}
}
