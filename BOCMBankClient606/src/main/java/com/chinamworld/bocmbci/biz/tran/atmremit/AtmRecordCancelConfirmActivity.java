package com.chinamworld.bocmbci.biz.tran.atmremit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * Atm取款查询撤销确认页面
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRecordCancelConfirmActivity extends TranBaseActivity {

	private View view;
	/** 汇款编号 */
	private TextView tv_atm_remitNo;
	/** 汇出账号 */
	private TextView tv_remitNumber;
	// /** 汇款人名称 */
	// private TextView tv_atm_remitName;
	/** 收款人姓名 */
	private TextView tv_payeeName;
	/** 收款人手机号码 */
	private TextView tv_payeeMobile;
	/** 汇款金额 */
	private TextView tv_money;
	/** 有效时间至 */
	private TextView tv_dueDate;
	/** 附言 */
	private TextView tv_furInfo;
	/** 状态 */
	private TextView tv_state;
	// 有效期
	private String dueDateStr;
	/** 详情 */
	private Map<String, Object> detailMap;
	private Button btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.trans_atm_cancel_title));
		view = addView(R.layout.tran_atm_records_confirm_activity);
		setLeftSelectedPosition("tranManager_5");
		back.setVisibility(View.GONE);
		goneLeftView();
		mTopRightBtn.setText(this.getString(R.string.close));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
			}
		});
		detailMap = TranDataCenter.getInstance().getMobileTransDetailMap();
		dueDateStr = getIntent().getStringExtra(Tran.TRAN_ATM_DETAIL_DUEDATE);
		init();
	}

	public void init() {

		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(this.getString(R.string.trans_atm_cancel_title_con));
		tv_atm_remitNo = (TextView) view.findViewById(R.id.tv_atm_remitNo);
		tv_atm_remitNo.setText((String) detailMap.get(Tran.TRAN_ATM_DETAIL_REMITNUMBER_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_atm_remitNo);
		tv_remitNumber = (TextView) view.findViewById(R.id.tv_remitNumber);
		String remitNumber = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_CARDNO_RES);
		String fromActNumber = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_FROMACTNUMBER_RES);
		tv_remitNumber.setText(StringUtil.isNull(remitNumber) ? StringUtil.getForSixForString(fromActNumber) : StringUtil
				.getForSixForString(remitNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_remitNumber);
		// tv_atm_remitName = (TextView)
		// view.findViewById(R.id.tv_atm_remitName);
		// tv_atm_remitName.setText((String) detailMap
		// .get(Tran.TRAN_ATM_DETAIL_FROMNAME_RES));
		/** 收款人姓名 */
		tv_payeeName = (TextView) view.findViewById(R.id.tv_payeeName);
		tv_payeeName.setText((String) detailMap.get(Tran.TRAN_ATM_DETAIL_TONAME_RES));
		/** 收款人手机号码 */
		tv_payeeMobile = (TextView) view.findViewById(R.id.tv_payeeMobile);
		tv_payeeMobile.setText((String) detailMap.get(Tran.TRAN_ATM_DETAIL_TOMOBILE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_payeeMobile);
		/** 转账金额 */
		tv_money = (TextView) view.findViewById(R.id.tv_money);
		String amount = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_PAYMENTAMOUNT_RES);
		tv_money.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_money);
		/** 备注 */
		tv_furInfo = (TextView) view.findViewById(R.id.tv_furInfo);
		String furInfo = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_COMMENT_RES);
		tv_furInfo.setText(StringUtil.isNull(furInfo) ? ConstantGloble.BOCINVT_DATE_ADD : furInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_furInfo);
		/** 有效期至 */
		tv_dueDate = (TextView) view.findViewById(R.id.tv_dueDate);

		/** 状态 */
		tv_state = (TextView) view.findViewById(R.id.tv_state);
		String status = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_STATUS_RES);
		tv_state.setText((String) AtmStatusMap.get(status));

		if (TextUtils.isEmpty(dueDateStr)) {
			view.findViewById(R.id.ll_dueDate).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.ll_dueDate).setVisibility(View.VISIBLE);
			tv_dueDate.setText(dueDateStr);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_dueDate);

		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_cancel.setText(this.getString(R.string.confirm));
		btn_cancel.setVisibility(View.VISIBLE);
		btn_cancel.setOnClickListener(new OnClickListener() {
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
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnPasswordRemitFreeCancel(token);
	}

	public void requestPsnPasswordRemitFreeCancel(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Tran.TRAN_PSNPASSWORDREMITFREECANCEL_API);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.TRAN_ATM_CANCEL_REMITNO_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_REMITNUMBER_RES));
		map.put(Tran.TRAN_ATM_CANCEL_TYPE_REQ, ConstantGloble.ZERO);
		map.put(Tran.TRAN_ATM_CANCEL_FROMCARDNO_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_CARDNO_RES));
		map.put(Tran.TRAN_ATM_CANCEL_AMOUNT_REQ,
				StringUtil.parseStringPattern((String) detailMap.get(Tran.TRAN_ATM_DETAIL_PAYMENTAMOUNT_RES), 2));
		map.put(Tran.TRAN_ATM_CANCEL_CURRENCYCODE_REQ, ConstantGloble.BOCINVT_CURRENCY_RMB);
		map.put(Tran.TRAN_ATM_CANCEL_FROMACTNUMBER_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_FROMACTNUMBER_RES));
		map.put(Tran.TRAN_ATM_CANCEL_FROMACTTYPE_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_FROMACTTYPE_RES));
		map.put(Tran.TRAN_ATM_CANCEL_PAYEENAME_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_TONAME_RES));
		map.put(Tran.TRAN_ATM_CANCEL_PAYEEMOBILE_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_TOMOBILE_RES));
		map.put(Tran.TRAN_ATM_CANCEL_RECEIPTMODE_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_RECEIPTMODE_RES));
		map.put(Tran.TRAN_ATM_CANCEL_DRAWMODE_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_DRAWMODE_RES));
		map.put(Tran.TRAN_ATM_CANCEL_FURINF_REQ, detailMap.get(Tran.TRAN_ATM_DETAIL_COMMENT_RES));
		map.put(Tran.TRAN_ATM_CANCEL_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPasswordRemitFreeCancelCallBack");
	}

	/**
	 * atm汇款转账撤销回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnPasswordRemitFreeCancelCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String transferback = (String) biiResponseBody.getResult();
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(transferback)) {
			return;
		}
		Intent intent = new Intent(this, AtmRecordCancelSuccessActivity.class);
		intent.putExtra(Tran.TRAN_ATM_DETAIL_DUEDATE, dueDateStr);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
	}
}
