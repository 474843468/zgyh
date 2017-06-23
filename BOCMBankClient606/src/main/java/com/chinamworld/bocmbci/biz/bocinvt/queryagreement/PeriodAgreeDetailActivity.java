package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
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
 * 周期性产品续约协议详情
 * 
 * @author wangmengmeng
 * 
 */
public class PeriodAgreeDetailActivity extends BociBaseActivity implements
		OnClickListener {
	/** 协议详情信息页 */
	private View view;
	/** 协议详情列表 */
	private Map<String, Object> detailMap;
	/** 协议序号 */
	private TextView tv_contractSeq;
	/** 产品系列名称 */
	private TextView tv_serialName;
	/** 币种/钞汇 */
	private TextView tv_curcode;
	/** 基础金额模式 */
	private TextView tv_amountTypeCode;
	/** 开始期数 */
	private TextView tv_startPeriod;
	/** 结束期数 */
	private TextView tv_endPeriod;
	/** 当前期数 */
	private TextView tv_issuePeriod;
	/** 续约状态 */
	private TextView tv_contStatus;
	/** 修改 */
	private Button btn_edit;
	/** 解约 */
	private Button btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_zhouqi));
		// 添加布局
		view = addView(R.layout.boc_period_agree_detail);
		// 界面初始化
		init();
	}

	private void init() {
		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		/** 协议序号 */
		tv_contractSeq = (TextView) view.findViewById(R.id.tv_contractSeq);
		/** 产品系列名称 */
		tv_serialName = (TextView) view.findViewById(R.id.tv_serialName);
		/** 币种/钞汇 */
		tv_curcode = (TextView) view.findViewById(R.id.tv_curcode);
		/** 基础金额模式 */
		tv_amountTypeCode = (TextView) view
				.findViewById(R.id.tv_amountTypeCode);
		/** 开始期数 */
		tv_startPeriod = (TextView) view.findViewById(R.id.tv_startPeriod);
		/** 结束期数 */
		tv_endPeriod = (TextView) view.findViewById(R.id.tv_endPeriod);
		/** 当前期数 */
		tv_issuePeriod = (TextView) view.findViewById(R.id.tv_issuePeriod);
		/** 续约状态 */
		tv_contStatus = (TextView) view.findViewById(R.id.tv_contStatus);
		btn_edit = (Button) view.findViewById(R.id.btn_edit);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_edit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
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

		tv_amountTypeCode.setText(LocalData.bociAmountTypeMap
				.get((String) detailMap
						.get(BocInvt.BOC_QUERY_AGREE_AMOUNTTYPE_RES)));
		tv_startPeriod.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_STARTPERIOD_RES));
		tv_endPeriod.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_ENDPERIOD_RES));
		tv_issuePeriod.setText((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_ISSUEPERIOD_RES));
		tv_contStatus.setText(agreecontStatusMap.get((String) detailMap
				.get(BocInvt.BOC_QUERY_AGREE_CONTFLAG_RES)));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit:
			// 修改 判断是否是周期性连续产品
			requestPsnXpadAgreementModify();
			BiiHttpEngine.showProgressDialog();
			break;
		case R.id.btn_cancel:
			// 解约
			Intent intent = new Intent(PeriodAgreeDetailActivity.this,
					PeriodAgreeCancelActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	/** 请求是否是周期性连续产品 */
	public void requestPsnXpadAgreementModify() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADAGREEMENTMODIFY_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(BocInvt.BOC_MODIFY_SERIALCODE_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_SERIALCODE_RES));
		paramsmap.put(BocInvt.BOC_MODIFY_STARTPERIOD_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_STARTPERIOD_RES));
		paramsmap.put(BocInvt.BOC_MODITY_SERIALPRDCT_REQ,
				detailMap.get(BocInvt.BOC_QUERY_AGREE_SERIALPRDCT_RES));
		paramsmap.put(Comm.ACCOUNTNUMBER, detailMap.get(BocInvt.BANCACCOUNT));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAgreementModifyCallBack");
	}

	/**
	 * 请求是否是周期性连续产品 回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnXpadAgreementModifyCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> modifyMap = (Map<String, Object>) biiResponseBody
				.getResult();
		BociDataCenter.getInstance().setPeriodModifyMap(modifyMap);
		Intent intent = new Intent(this, PeriodAgreeEditInputActivity.class);
		startActivity(intent);
	}
}
