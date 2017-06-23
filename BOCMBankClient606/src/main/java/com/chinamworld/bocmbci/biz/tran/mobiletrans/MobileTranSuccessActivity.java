package com.chinamworld.bocmbci.biz.tran.mobiletrans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.weixin.Weixin;
import com.chinamworld.bocmbci.biz.tran.weixin.WeixinSuccess;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author WJP
 * 
 */
public class MobileTranSuccessActivity extends TranBaseActivity implements
		OnClickListener {
	
	private View contentView ;
	private LinearLayout transactionLayout;
	private TextView transactionTv;
	private TextView accOutNoTv;
	private TextView accOutNickNameTv;
	private LinearLayout accOutAreaLayout;
	private TextView accOutAreaTv;
	private TextView payeeNameTv;
	private TextView payeeMobileTv;
	private LinearLayout accInAreaLayout;
	private TextView accInAreaTv;
	private TextView amountTv;
	private TextView currencyTv;
	// private LinearLayout comissionChargeLayout;
	// private TextView comissionChargeTv;
	private TextView remarkTv;
	private Button finishBtn;

	private String strTransaction;
	private String strAccOutNo;
	private String strAccOutNickname;
	private String strAccOutArea;
	private String strPayeeName;
	private String strPayeeMobile;
	private String strAccInArea;
	private String strAmount;
	private String strCurrency;
	private String strComissionCharge;
	private String strRemark;

	/** 应收费用 */
	private TextView shouldChargeTv;
	private String shouldCharge;
	/** 拟收费用 */
	private TextView fakeChargeTv;
	private String isHaveAcct;

	// private TextView actChargeDispalyTv;
	private TextView farkChargeDisplayTv;
	private TextView titleTv;
	/** 403转出账户余额 */
	private LinearLayout tran_success_out_balance;
	private TextView tv_tran_success_out_balance;
	private String fromAccountType;
	private String fromAccountId;
	/** 转账-微信抽奖机会*/
	private double currentAmount;
	private int index = 0;
	private boolean weixinRaffleflag = false;
	private TextView weixinRaffleTv;
	/**基准费用*/
	private LinearLayout ll_need; 
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.mobile_trans));
		contentView = mInflater.inflate(R.layout.tran_mobile_confirm_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(contentView);
		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.go_main));
		initData();
		/**判断金额是否满足微信抽奖*/
		if(isHaveAcct.equals(ConstantGloble.IS_HAVE_ACC_1)&&currentAmount>=Tran.WEIXIN_RAFFLE_AMOUNT){
//			BaseHttpEngine.showProgressDialog();
//			weixin_raffle_startTime = "";
//			weixin_raffle_endTime = "";
//			/** 微信抽奖获取活动时间*/
//			sendGetActivityAction();
			Weixin.getInstance().doweixin(tabcontent, MobileTranSuccessActivity.this, new WeixinSuccess() {
				
				@Override
				public void SuccessCallBack(boolean param) {
					weixinRaffleflag=param;	
					setupView(); // 初始化布局控件
					displayTextView();
				}
			}, strTransaction, "T",currentAmount);
			
		}else{
			setupView();
			displayTextView();
		}
	}
	
//	/***
//	 * 微信抽奖 获取抽奖活动时间回调
//	 */
//	public void sendGetActivityActionCallback(Object resultObj){
//		super.sendGetActivityActionCallback(resultObj);
//		if("null".equals(resultObj)){
//			BaseHttpEngine.dissMissProgressDialog();
//			setupView();
//			displayTextView();
//		}else{
//			/** 微信抽奖获取系统时间*/
//			requestSystemDateTime();
//		}
//	}
//	
//	/**
//	 * 请求系统时间返回
//	 * 
//	 * @param resultObj dateTime 格式例如：2015/03/31 17:36:41
//	 */
//	public void requestSystemDateTimeCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		@SuppressWarnings("unchecked")
//		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
//		dateTime = (String) resultMap.get(Comm.DATETME);
//		String _dateTime = dateTime.replace(" ", "").replace("/", "").replace(":", "");
//		try {
//			/**判断系统当前时间是否包含在抽奖的时间内*/
//			weixinRaffleflag = DateUtils.dateCompare(_dateTime, weixin_raffle_startTime, weixin_raffle_endTime);
//		} catch (Exception e) {
//			LogGloble.e("MobileTranSuccessActivity", e.getMessage());
//			weixinRaffleflag = false;
//		}
//		if(weixinRaffleflag){
//			sendPsnGetTicketForMessage(strTransaction);
//		}else{
//			BaseHttpEngine.dissMissProgressDialog();
//			setupView();
//			displayTextView();
//		}
//		contentView.postInvalidate();
//	}
//	
//	/***
//	 * 消息推送取票接口 回调
//	 * @param resultObj
//	 */
//	public void requestPsnGetTicketForMessageCallBack(Object resultObj){
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		String ticket = result.get("ticket")+"";
//		String _dateTime = dateTime.replace(" ", "").replace("/", "").replace(":", "");
//		// TODO 注释微信银行接口
//		sendInsertTranSeq(strTransaction,_dateTime,ticket);
//	}
//	
//	/***
//	 * PsnGetTicketForMessage 回调
//	 * @param resultObj
//	 */
//	@Override
//	public void sendInsertTranSeqCallback(Object resultObj){
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		//“success”为成功;“fail”为失败
//		String status = result.get("status")+"";
//		if("success".equals(status)){
//			weixinRaffleflag = true;
//			setupView();
//			displayTextView();
//			/**进入微信入口*/
//			weixinRaffleTv= (TextView)findViewById(R.id.tran_weixin_raffle_tv);
//			weixinRaffleTv.setVisibility(View.VISIBLE);
//			weixinRaffleTv.postInvalidate();
//			weixinRaffleTv.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					BaseDroidApp.getInstanse().showErrorDialog(getWXRaffleCue(0, strTransaction), R.string.close, R.string.confirm,
//							new View.OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									switch ((Integer) v.getTag()) {
//									case CustomDialog.TAG_CANCLE:
//										BaseDroidApp.getInstanse().dismissMessageDialog();
//										break;
//									case CustomDialog.TAG_SURE:
//										BaseDroidApp.getInstanse().dismissMessageDialog();
//										skipWeixin();
//										break;
//									}
//									
//								}
//							});
//				}
//			});
//		}else{
//			weixinRaffleflag = false;
//			setupView();
//			displayTextView();
//		}
//		contentView.postInvalidate();
//		BaseHttpEngine.dissMissProgressDialog();
//	}

	/**
	 * 显示数据
	 */
	private void initData() {
		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		// 转入账户地区字段 在预交易返回数据里面
		Map<String, Object> mobileTranMap = TranDataCenter.getInstance().getMobileTranCallBackMap();
		strAccInArea = (String) mobileTranMap.get(Tran.TRANS_PAYEE_BANK_NUM);
		isHaveAcct = (String) mobileTranMap.get(Tran.IS_HAVEACCT);
		Map<String, String> userInputMap = TranDataCenter.getInstance().getUserInputMap();
		strRemark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
		strPayeeName = userInputMap.get(Tran.INPUT_PAYEE_NAME);
		strPayeeMobile = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);

		Map<String, Object> mobileTranDealMap = TranDataCenter.getInstance().getMobileTranDealCallBackMap();
		strTransaction = (String) mobileTranDealMap.get(Tran.TRANS_TRANSACTIONID_RES);
		strAccOutNo = (String) mobileTranDealMap.get(Tran.TRANS_FROMACCOUNTNUM_RES);
		strAccOutNickname = (String) mobileTranDealMap.get(Tran.TRANS_FROMACCOUNTNICKNAME_RES);
		strAccOutArea = (String) mobileTranDealMap.get(Tran.TRANS_FROMIBKNUM_RES);
		fromAccountType = (String) mobileTranDealMap.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
		fromAccountId = getIntent().getStringExtra(Tran.ACC_ACCOUNTID_REQ);
		strCurrency = (String) mobileTranDealMap.get(Tran.TRANS_CURRENCY_RES);
		strComissionCharge = (String) mobileTranDealMap.get(Tran.FINAL_COMMISSIONCHARGE);
		if (!StringUtil.isNullOrEmpty(strComissionCharge)) {
			strComissionCharge = StringUtil.parseStringPattern(strComissionCharge, 2);
			
		}
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance().getCommissionChargeMap();
		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			
			TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
			tv_toast.setVisibility(View.GONE);
			String remitSetMealFlag = (String) chargeMissionMap
					.get(Tran.REMITSETMEAL_FLAG);
			String getChargeFlag = (String) chargeMissionMap
					.get(Tran.GETCHARGE_FLAG);
			if(ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
					&&!chargeMissionMap.containsKey(Tran.NEED_COMMISSION_CHARGE)){
				
			}else{
				ll_need.setVisibility(View.VISIBLE);	
			}
			if (!StringUtil.isNull(remitSetMealFlag)) {
				if (remitSetMealFlag
						.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_toast.setVisibility(View.VISIBLE);
					tv_toast.setText(getString(R.string.tran_remitSetMealFlag_success));
				}
			}
			
			// 应收费用
			shouldCharge = (String) chargeMissionMap.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge)) {
				shouldCharge = StringUtil.parseStringPattern(shouldCharge, 2);
			}
		}

		// 如果没有绑定 接口不返回转账金额 交易序号不显示
		if (isHaveAcct.equals(ConstantGloble.IS_HAVE_ACC_0)) {
			strAmount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		} else {
			strAmount = (String) mobileTranDealMap.get(Tran.TRANS_AMOUNT_RES);
		}
		/**转账金额*/
		currentAmount =Double.parseDouble(strAmount);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
	
		StepTitleUtils.getInstance().initTitldStep(this, this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);

		titleTv = (TextView) findViewById(R.id.tv_content);
		transactionLayout = (LinearLayout) findViewById(R.id.tran_transaction_layout);
		transactionTv = (TextView) findViewById(R.id.tran_transaction_tv);
		accOutNoTv = (TextView) findViewById(R.id.tv_acc_out_rel_confirm);
		accOutNickNameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accOutAreaLayout = (LinearLayout) findViewById(R.id.tran_tran_out_area_layout);
		accOutAreaTv = (TextView) findViewById(R.id.tran_out_area_tv);
		payeeNameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		payeeMobileTv = (TextView) findViewById(R.id.tv_acc_in_mobile);
		accInAreaLayout = (LinearLayout) findViewById(R.id.tran_in_area_layout);
		accInAreaTv = (TextView) findViewById(R.id.tran_in_area_tv);

		shouldChargeTv = (TextView) findViewById(R.id.tran_commission_charge_tv);
		fakeChargeTv = (TextView) findViewById(R.id.tran_commission_fake_tv);
		// actChargeDispalyTv = (TextView)
		// findViewById(R.id.tran_act_charge_display_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);

		amountTv = (TextView) findViewById(R.id.tv_transferAmount_rel_confirm);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_rel_confirm);
		currencyTv = (TextView) findViewById(R.id.tv_tran_currency_rel_confirm);
		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);
//		TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
//		tv_toast.setVisibility(View.GONE);
		finishBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);
		finishBtn.setText(getString(R.string.finish));
		finishBtn.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				if(weixinRaffleflag&&index == 0){
					index++;
					BaseDroidApp.getInstanse().showMsgDialogOneBtn(Weixin.getInstance().getWXRaffleCue(1, strTransaction),"关闭",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse()
											.dismissMessageDialog();
								}
							});

				}else{
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent = new Intent();
					intent.setClass(MobileTranSuccessActivity.this, MobileTransThirdMenu.class);
					startActivity(intent);
					finish();
				}
			}
		});
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(weixinRaffleflag&&index == 0){
					index++;
					BaseDroidApp.getInstanse().showMsgDialogOneBtn(Weixin.getInstance().getWXRaffleCue(1, strTransaction),"关闭",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse()
											.dismissMessageDialog();
								}
							});

				}else{
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//					BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
					goToMainActivity();
				}
			}
		});
		
	}

	private void displayTextView() {
		accOutAreaLayout.setVisibility(View.VISIBLE);
		accInAreaLayout.setVisibility(View.VISIBLE);
		// comissionChargeLayout.setVisibility(View.VISIBLE);
		transactionTv.setText(strTransaction);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, transactionTv);
		accOutNoTv.setText(StringUtil.getForSixForString(strAccOutNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutNoTv);
		accOutNickNameTv.setText(strAccOutNickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutNickNameTv);
		accOutAreaTv.setText(LocalData.Province.get(strAccOutArea));
		payeeNameTv.setText(strPayeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeNameTv);
		payeeMobileTv.setText(strPayeeMobile);
		if (isHaveAcct.equals("0")) {
			// 如果该值为“0”，转入账户地区代码返回为空
			accInAreaTv.setText("-");
		} else {
			accInAreaTv.setText(LocalData.Province.get(strAccInArea));
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInAreaTv);
		amountTv.setText(StringUtil.parseStringPattern(strAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, amountTv);
		// comissionChargeTv.setText(StringUtil.parseStringPattern(strComissionCharge,
		// 2));
		remarkTv.setText(StringUtil.isNull(strRemark) ? "-" : strRemark);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);
		currencyTv.setText(LocalData.Currency.get(strCurrency));

		// farkChargeDisplayTv.setText(this.getString(R.string.trans_fact_charge));

		if (isHaveAcct.equals(ConstantGloble.IS_HAVE_ACC_1)) {
			String strTitle = this.getString(R.string.mobile_tran_have_acc_success_title);
			titleTv.setText(strTitle);
			transactionLayout.setVisibility(View.VISIBLE);
			// shouldChargeTv.setText(StringUtil.isNull(shouldCharge)?"-":shouldCharge);
			shouldChargeTv.setText(shouldCharge);
			shouldChargeTv.setTextColor(getResources().getColor(R.color.red));
			// fakeChargeTv.setText(StringUtil.isNull(strComissionCharge)?"-":strComissionCharge);
			fakeChargeTv.setText(strComissionCharge);
			fakeChargeTv.setTextColor(getResources().getColor(R.color.red));
			// P403 请求账户余额
			if (fromAccountType.equals(ConstantGloble.GREATWALL)
					|| fromAccountType.equals(ConstantGloble.ZHONGYIN)
					|| fromAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
				// 信用卡
				requestPsnCrcdOutDetail(fromAccountId, strCurrency);
			} else {
				requestAccBankOutDetail(fromAccountId);
			}
		} else {
			String strTitle = this.getString(R.string.mobile_tran_success);
			titleTv.setText(strTitle);
		}
	}
	
	// 屏蔽返回键
	@Override
	public void onBackPressed() {}

	@Override
	public void requestAccBankOutDetailCallback(Object resultObj) {
		super.requestAccBankOutDetailCallback(resultObj);
		refreshTranOutBalance(false, tran_success_out_balance, tv_tran_success_out_balance, accountDetailList, strCurrency, null);
	}

	@Override
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		super.requestPsnCrcdOutDetailCallBack(resultObj);
		refreshTranOutBalance(true, tran_success_out_balance, tv_tran_success_out_balance, detailList, strCurrency, null);
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		if (resultObj instanceof BiiResponse) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		/**抽奖取票、微信银行录入流水号等接口*/
		if(Tran.PSNGETTICKETFORMESSAGE.equals(biiResponseBody.getMethod())|| Tran.INSERTTRANSEQ.equals(biiResponseBody.getMethod())){
			weixinRaffleflag = false;
			setupView();
			displayTextView();
			return false;
		}
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Acc.QRY_ACC_BALANCE_API.equals(biiResponseBody.getMethod()) || Crcd.CRCD_ACCOUNTDETAIL_API.equals(biiResponseBody.getMethod())) {// 余额显示
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {

							}
						}
					}
					return true;
				}
				return false;// 没有异常
			}
		}
		}
		return super.httpRequestCallBackPre(resultObj);
	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		if (Acc.QRY_ACC_BALANCE_API.equals(requestMethod) || Crcd.CRCD_ACCOUNTDETAIL_API.equals(requestMethod)) {

		} else {
			super.commonHttpErrorCallBack(requestMethod);
		}
	}
	
}
