package com.chinamworld.bocmbci.biz.gatherinitiative.gatherquery;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.biz.gatherinitiative.creatgather.CreatGatherInputInfoActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * @ClassName: GatherQueryDetailActivity
 * @Description: 收款指令查询详情页面
 * @author JiangWei
 * @date 2013-8-29上午11:09:28
 */
public class GatherQueryDetailActivity extends GatherBaseActivity {

	/** 保存详情数据的map */
	private Map<String, Object> mapDetail;
	/** 指令序号 */
	private TextView textOrderNumber;
	/** 交易序号 */
	private TextView textTransationId;
	/** 创建日期 */
	private TextView textCreatDate;
	/** 交易状态 */
	private TextView textTranStatus;
	/** 币种 */
	private TextView textMoneyType;
	/** 收款金额 */
	private TextView textMoneyAmount;
	/** 收款人姓名 */
	private TextView textPayeeName;
	/** 收款账号 */
	private TextView textAccountNumber;
	/** 收款人手机号 */
	private TextView textPayeeMobile;
	/** 付款人姓名 */
	private TextView textPayerName;
	/** 付款人手机号 */
	private TextView textPayerPhone;
	/** 发起渠道 */
	private TextView textStartWay;
	/** 付款渠道 */
	private TextView textPayWay;
	/** 备注 */
	private TextView textBeiZhu;
	/** 实付金额布局 */
	private LinearLayout layoutPayFact;
	/** 实付金额 */
	private TextView textPayFact;

	/** 指令序号str */
	private String strOrderNumber;
	/** 交易序号str */
	private String strTransationId;
	/** 创建日期str */
	private String strCreatDate;
	/** 交易状态str */
	private String strTranStatus;
	/** 币种str */
	private String strMoneyType;
	/** 收款金额str */
	private String strMoneyAmount;
	/** 实付金额str */
	private String strPayFact;
	/** 收款人姓名str */
	private String strPayeeName;
	/** 收款账号str */
	private String strAccountNumber;
	/** 收款人手机号str */
	private String strPayeeMobile;
	/** 付款人姓名str */
	private String strPayerName;
	/** 付款人手机号str */
	private String strPayerPhone;
	/** 发起渠道str */
	private String strStartWay;
	/** 付款渠道str */
	private String strPayWay;
	/** 备注str */
	private String strBeiZhu;
	/** 撤销收款按钮 */
	private Button btnCancel;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.gather_instruct_query);
		View view = LayoutInflater.from(this).inflate(R.layout.gather_query_detail_activity, null);
		tabcontent.addView(view);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setText(R.string.creat_new_gather);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(GatherQueryDetailActivity.this, CreatGatherInputInfoActivity.class);
					startActivityForResult(intent, 101);
				}
			});
		}

		mapDetail = GatherInitiativeData.getInstance().getDetailInfo();
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化显示和数据
	 * @param
	 * @return void
	 * @throws
	 */
	private void init() {
		layoutPayFact = (LinearLayout) this.findViewById(R.id.layout_gather_pay_fact);
		textPayFact = (TextView) this.findViewById(R.id.tv_gather_pay_fact);
		textOrderNumber = (TextView) this.findViewById(R.id.gather_order_number);
		textTransationId = (TextView) this.findViewById(R.id.gather_transationId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textTransationId);
		textCreatDate = (TextView) this.findViewById(R.id.gather_creat_date);
		textTranStatus = (TextView) this.findViewById(R.id.gather_tran_status);
		textMoneyType = (TextView) this.findViewById(R.id.gather_money_type);
		textMoneyAmount = (TextView) this.findViewById(R.id.tv_gather_money_ammount);
		textPayeeName = (TextView) this.findViewById(R.id.gather_money_name);
		textAccountNumber = (TextView) this.findViewById(R.id.gather_account_number);
		textPayeeMobile = (TextView) this.findViewById(R.id.tv_payeeMobile);
		textPayerName = (TextView) this.findViewById(R.id.tv_payer_name);
		textPayerPhone = (TextView) this.findViewById(R.id.tv_payer_phone);
		textStartWay = (TextView) this.findViewById(R.id.gather_start_way);
		textPayWay = (TextView) this.findViewById(R.id.gather_pay_way);
		textBeiZhu = (TextView) this.findViewById(R.id.tv_beizhu);

		strOrderNumber = (String) mapDetail.get(GatherInitiative.NOTIFY_ID);
		strTransationId = (String) mapDetail.get(GatherInitiative.TRANSACTION_ID);
		strCreatDate = (String) mapDetail.get(GatherInitiative.CREATE_DATE);
		strTranStatus = (String) mapDetail.get(GatherInitiative.STATUS);
		strMoneyType = (String) mapDetail.get(GatherInitiative.TRF_CUR);
		strMoneyAmount = (String) mapDetail.get(GatherInitiative.REQUEST_AMOUNT);
		strPayFact = (String) mapDetail.get(GatherInitiative.TRF_AMOUNT);
		strPayeeName = (String) mapDetail.get(GatherInitiative.PAYEE_NAME);
		strAccountNumber = (String) mapDetail.get(GatherInitiative.PAYEE_ACCOUNT_NUMBER);
		strPayeeMobile = (String) mapDetail.get(GatherInitiative.PAYEE_MOBILE);
		strPayerName = (String) mapDetail.get(GatherInitiative.PAYER_NAME);
		strPayerPhone = (String) mapDetail.get(GatherInitiative.PAYER_MOBILE);
		strStartWay = (String) mapDetail.get(GatherInitiative.CREATE_CHANNEL);
		strPayWay = (String) mapDetail.get(GatherInitiative.TRF_CHANNEL);
		strBeiZhu = (String) mapDetail.get(GatherInitiative.FUR_INFO);
		btnCancel = (Button) this.findViewById(R.id.cancel_btn);

		if ("2".equals(strTranStatus)) {
			if (!StringUtil.isNullOrEmpty(strPayFact)) {
				layoutPayFact.setVisibility(View.VISIBLE);
				textPayFact.setText(StringUtil.parseStringPattern(strPayFact, 2));
			}
			btnCancel.setVisibility(View.GONE);
		}
		textOrderNumber.setText(strOrderNumber);
		textTransationId.setText(StringUtil.isNullOrEmpty(strTransationId) ? "-" : strTransationId);
		textCreatDate.setText(strCreatDate);
		if ("3".equals(strTranStatus)) {
			btnCancel.setVisibility(View.GONE);
		}
		textTranStatus.setText(LocalData.gatherTranStatus.get(strTranStatus));
		textMoneyType.setText(LocalData.Currency.get(strMoneyType));
		textMoneyAmount.setText(StringUtil.parseStringPattern(strMoneyAmount, 2));
		textPayeeName.setText(strPayeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textPayeeName);
		textAccountNumber.setText(StringUtil.getForSixForString(String.valueOf(strAccountNumber)));
		textPayeeMobile.setText(strPayeeMobile);
		textPayerName.setText(strPayerName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textPayerName);
		textPayerPhone.setText(strPayerPhone);
		textStartWay.setText(LocalData.startWay.get(strStartWay));
		if (StringUtil.isNullOrEmpty(strPayWay)) {
			textPayWay.setText("-");
		} else {
			textPayWay.setText(LocalData.startWay.get(strPayWay));
		}
		textBeiZhu.setText(StringUtil.isNullOrEmpty(strBeiZhu) ? "-" : strBeiZhu);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textBeiZhu);

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseDroidApp.getInstanse().showErrorDialog(
						GatherQueryDetailActivity.this.getResources().getString(R.string.are_you_sure_cancel_gather),
						R.string.cancle, R.string.confirm, new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								if (v.getId() == R.id.retry_btn) {
									BaseHttpEngine.showProgressDialog();
									requestCommConversationId();
								} else {
									// BaseDroidApp.getInstanse().dismissErrorDialog();
								}
							}
						});
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnTransActUndoReminderOrder();
	}

	/**
	 * @Title: requestPsnTransActUndoReminderOrder
	 * @Description: 请求“撤消催款指令”接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActUndoReminderOrder() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_UNDO_REMINDER_ORDER);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GatherInitiative.NOTIFY_ID, strOrderNumber);
		map.put(GatherInitiative.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransActUndoReminderOrderCallback");
	}

	/**
	 * @Title: requestPsnTransActUndoReminderOrderCallback
	 * @Description: 请求“撤消催款指令”接口的回调
	 * @param
	 * @return void
	 * @throws
	 */
	public void requestPsnTransActUndoReminderOrderCallback(Object resultObj) {
		// TODO 判断是否成功
		BaseHttpEngine.dissMissProgressDialog();
		tabcontent.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 关闭progressDialog在显示toast有阴影防止阴影
				CustomDialog.toastShow(GatherQueryDetailActivity.this, getString(R.string.cancel_gather_success));
				setResult(RESULT_OK);
				finish();
			}
		}, 100);

		// BaseDroidApp.getInstanse().showMessageDialog(this.getResources().getString(R.string.cancel_gather_success),
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// setResult(RESULT_OK);
		// finish();
		// }
		// });
	}

}
