package com.chinamworld.bocmbci.biz.drawmoney.remitquery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: RemitQueryInfoActivity
 * @Description: 汇款查询详情页面
 * @author JiangWei
 * @date 2013-7-23 下午2:24:42
 */
public class RemitQueryInfoActivity extends DrawBaseActivity implements OnClickListener {

	/** 详情数据 */
	private Map<String, Object> detailMap;
	/** 汇款状态 */
	private String remitStatusStr;
	/** 汇款状态  布局*/
	private LinearLayout ll_dueDate;
	/** 有效期至*/
	private String tv_dueDate;
	/** 收款人手机号 */
	private String payeeMobileStr;
	/** 收款人姓名 */
	private String payeeNameStr;
	/** 汇款人姓名 */
	private String fromNameStr;
	/** 汇款金额 */
	private String remitAmountStr;
	/** 汇款编号 */
	private String remitNoStr;
	/** 汇款币种 */
	private String remitCurrencyCodeStr;
	private String token;
	/**接收intent传过来的参数*/
	private Button btnSendMsg;
	private String dueDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.drawmoney_remit_query_detail, null);
		tabcontent.addView(view);

		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(RemitQueryInfoActivity.this, MainActivity.class);
//				startActivity(intent);
				goToMainActivity();
				finish();

			}
		});

		setTitle(R.string.remitout_query_title);
		detailMap = DrawMoneyData.getInstance().getQueryResultDetail();
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化页面数据
	 * @param
	 * @return void
	 */
	private void init() {
		TextView textremitNoStr = (TextView) this.findViewById(R.id.tv_remitout_no);
		LinearLayout layoutRemitNo = (LinearLayout) this.findViewById(R.id.layout_remitout_no);
		TextView textAcount = (TextView) this.findViewById(R.id.tv_remit_acount);
		TextView textBiZhong = (TextView) this.findViewById(R.id.tv_remit_bizhong);
		TextView textMoneyAmount = (TextView) this.findViewById(R.id.tv_remit_money_amout);
		TextView textRemitName = (TextView) this.findViewById(R.id.tv_get_remit_name);
		TextView textRemitPhone = (TextView) this.findViewById(R.id.tv_get_remit_phone);
		TextView textFuYan = (TextView) this.findViewById(R.id.tv_remit_fuyan);
		TextView textRemitDate = (TextView) this.findViewById(R.id.tv_remit_date);
		TextView textRemitState = (TextView) this.findViewById(R.id.tv_remit_state);
		TextView textGetRemitDate = (TextView) this.findViewById(R.id.tv_get_remit_date);
		LinearLayout layoutGetRemitDate = (LinearLayout) this.findViewById(R.id.layout_get_remit_date);
		TextView textAgenceName = (TextView) this.findViewById(R.id.tv_remit_agence_name);
		LinearLayout layoutRemitAgencyName = (LinearLayout) this.findViewById(R.id.layout_remit_agence_name);
		TextView textAgenceNumber = (TextView) this.findViewById(R.id.tv_remit_agence_number);
		LinearLayout layoutRemitAgencyNumber = (LinearLayout) this.findViewById(R.id.layout_remit_agence_number);
		TextView channelView = (TextView) this.findViewById(R.id.channel);
		/**有效期至*/
		TextView tv_dueDate = (TextView) this.findViewById(R.id.tv_remit_dueDate);
		ll_dueDate = (LinearLayout) this.findViewById(R.id.ll_dueDate);
		
		btnSendMsg = (Button) this.findViewById(R.id.btn_send_msg);
		Button btnRepeal = (Button) this.findViewById(R.id.btn_cancle);
		remitNoStr = (String) detailMap.get(DrawMoney.REMIT_NO);
		dueDate = (String) detailMap.get(DrawMoney.DUE_DATE);
		remitStatusStr = (String) detailMap.get(DrawMoney.REMIT_STATUS);
		payeeMobileStr = (String) detailMap.get(DrawMoney.PAYEE_MOBILE);
		payeeNameStr = (String) detailMap.get(DrawMoney.PAYEE_NAME);
		fromNameStr = (String) detailMap.get(DrawMoney.FROM_NAME);
		remitAmountStr = (String) detailMap.get(DrawMoney.REMIT_AMOUNT);
		remitCurrencyCodeStr = (String) detailMap.get(DrawMoney.CURRENY_CODE);
		String cardNoString = "";
		String strCardNo = (String) detailMap.get(DrawMoney.CARD_NO);
		if (StringUtil.isNullOrEmpty(strCardNo)) {
			cardNoString = StringUtil.getForSixForString((String) detailMap.get(DrawMoney.FROM_ACT_NUMBER));
		} else {
			cardNoString = StringUtil.getForSixForString(strCardNo);
		}
		textAcount.setText(cardNoString);
		textBiZhong.setText(LocalData.Currency.get(remitCurrencyCodeStr));
		// textBiZhong.setText(R.string.tran_currency_rmb);
		textMoneyAmount.setText(StringUtil.parseStringPattern2(remitAmountStr, 2));
		textRemitName.setText(payeeNameStr);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textRemitName);
		textRemitPhone.setText(payeeMobileStr);
		String fuyanStr = (String) detailMap.get(DrawMoney.REMARK);
		if (StringUtil.isNullOrEmptyCaseNullString(fuyanStr)) {
			textFuYan.setText("-");
		} else {
			textFuYan.setText(fuyanStr);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textFuYan);
		String tranDate = (String) detailMap.get(DrawMoney.TRAN_DATE);
		if(StringUtil.isNullOrEmptyCaseNullString(tranDate)){
			textRemitDate.setText("-");
		}else{
			textRemitDate.setText(tranDate.substring(0, 10));
		}
		
		textRemitState.setText(LocalData.remitStatus.get(remitStatusStr));
		if (remitStatusStr.equals("OK")) {
			// 状态为Ok的时候 显示或者隐藏某些控件
			layoutRemitNo.setVisibility(View.VISIBLE);
			layoutGetRemitDate.setVisibility(View.VISIBLE);
			layoutRemitAgencyName.setVisibility(View.VISIBLE);
			layoutRemitAgencyNumber.setVisibility(View.VISIBLE);
			textremitNoStr.setText(remitNoStr);
			if(StringUtil.isNullOrEmptyCaseNullString(detailMap.get(DrawMoney.RECEIPT_DATE))){
				textGetRemitDate.setText("-");
			}else{
				textGetRemitDate.setText((String) detailMap.get(DrawMoney.RECEIPT_DATE));
			}
			if(StringUtil.isNullOrEmptyCaseNullString(detailMap.get(DrawMoney.AGENT_NAME))){
				textAgenceName.setText("-");
			}else{
				textAgenceName.setText((String) detailMap.get(DrawMoney.AGENT_NAME));
			}
			if(StringUtil.isNullOrEmptyCaseNullString(detailMap.get(DrawMoney.AGENT_NUM))){
				textAgenceNumber.setText("-");
			}else{
				textAgenceNumber.setText((String) detailMap.get(DrawMoney.AGENT_NUM));
			}
		}else if(remitStatusStr.equals("OU")) {
			// 状态为“未收款”的时候 显示或者隐藏某些控件
			btnSendMsg.setVisibility(View.VISIBLE);
			btnRepeal.setVisibility(View.VISIBLE);
			btnSendMsg.setOnClickListener(this);
			btnRepeal.setOnClickListener(this);
			ll_dueDate.setVisibility(View.VISIBLE);
		}else if(remitStatusStr.equals("CR") ) {
			btnSendMsg.setVisibility(View.GONE);
			btnRepeal.setVisibility(View.VISIBLE);
			btnRepeal.setOnClickListener(this);
			ll_dueDate.setVisibility(View.VISIBLE);
		}else if(remitStatusStr.equals("L3") ) {
			btnSendMsg.setVisibility(View.GONE);
			btnRepeal.setVisibility(View.VISIBLE);
			btnRepeal.setOnClickListener(this);
			ll_dueDate.setVisibility(View.VISIBLE);
		}else if(remitStatusStr.equals("L6")) {
			btnSendMsg.setVisibility(View.GONE);
			btnRepeal.setVisibility(View.VISIBLE);
			btnRepeal.setOnClickListener(this);
			ll_dueDate.setVisibility(View.VISIBLE);
		}else{
			ll_dueDate.setVisibility(View.GONE);
		}
		String channel = this.getIntent().getStringExtra("channel");
		channelView.setText(DrawMoney.ChannelType.getChannelStr(channel));
		
		if (!remitStatusStr.equals("OK") && !remitStatusStr.equals("CL")&& !remitStatusStr.equals("L6")) {
			if(StringUtil.isNullOrEmptyCaseNullString(dueDate)){
				tv_dueDate.setText("-");
			}else{
				tv_dueDate.setText(DateUtils.formatTime(dueDate));
			}
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_dueDate);
	}

	/**
	 * @Title: requestPsnMobileResetSendSms
	 * @Description: 请求“重新发送短信”接口
	 * @param
	 * @return void
	 */
	private void requestPsnMobileResetSendSms() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_RESET_SEND_SMS);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DrawMoney.REMIT_STATUS, remitStatusStr);
		map.put(DrawMoney.PAYEE_MOBILE, payeeMobileStr);
		map.put(DrawMoney.FROM_NAME, fromNameStr);
		// 金额返回空校验
		if (TextUtils.isEmpty(remitAmountStr)) {
			map.put(DrawMoney.REMIT_AMOUNT, "");
		} else {
			String str = remitAmountStr.substring(0, remitAmountStr.length() - 4);
			map.put(DrawMoney.REMIT_AMOUNT, str);
		}

		map.put(DrawMoney.REMIT_NO, remitNoStr);
		map.put(DrawMoney.REMIT_CURRENCY_CODE, remitCurrencyCodeStr);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnMobileResetSendSmsCallback");
	}

	/**
	 * @Title: requestPsnMobileResetSendSmsCallback
	 * @Description: 请求“重新发送短信”接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnMobileResetSendSmsCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		SmsCodeUtils.getInstance().reStartCodeSms(btnSendMsg);
		CustomDialog.toastShow(this, this.getString(R.string.send_success_for_remit));
	}

	/**
	 * @Title: requestPsnMobileCancelTrans
	 * @Description: 请求“撤销交易”的接口
	 * @param
	 * @return void
	 */
	private void requestPsnMobileCancelTrans() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_CANCEL_TRANS);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DrawMoney.REMIT_NO, remitNoStr);
		map.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnMobileCancelTransCallback");
	}

	/**
	 * @Title: requestPsnMobileCancelTransCallback
	 * @Description: 请求“撤销交易”的接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnMobileCancelTransCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this, this.getString(R.string.cancel_remit_success));
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnMobileCancelTrans();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_send_msg:
			// 发送短信
			BaseDroidApp.getInstanse().showErrorDialog(this.getResources().getString(R.string.sure_send_msg_for_remit),
					R.string.cancle, R.string.confirm, new OnClickListener() {

						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							if (v.getId() == R.id.retry_btn) {
								requestPsnMobileResetSendSms();
							}
						}
					});

			break;

		case R.id.btn_cancle:
			// 撤销
			BaseDroidApp.getInstanse().showErrorDialog(this.getResources().getString(R.string.sure_cancel_remit),
					R.string.cancle, R.string.confirm, new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (v.getId() == R.id.retry_btn) {
								BaseHttpEngine.showProgressDialog();
								requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap()
										.get(ConstantGloble.CONVERSATION_ID));
							} else {
								BaseDroidApp.getInstanse().dismissErrorDialog();
							}
						}
					});

			break;

		default:
			break;
		}
	}

}
