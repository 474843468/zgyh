package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
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
 * 非关联信用卡转账成功
 * 
 * @author WJP
 * 
 */
public class TranSuccessActivity1 extends TranBaseActivity {
	/** 头部返回按钮 */
	private Button back;
	private TextView transactionTv;
	/** 转出账户 */
	private TextView accOutTv = null;
	private TextView accOutNicknameTv;
	/** 转入账户 */
	private TextView accInTv = null;
	private TextView accInNicknameTv;
	/** 收款人 */
	private TextView payeeTv = null;
	/** 币种 */
	private TextView currencyTv = null;
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 附言 */
	private TextView remarkTv = null;
	/** 上一步按钮 */
	private Button mLastBtn = null;
	/** 下一步按钮 */
	private Button mNextBtn = null;
	/** 手续费 */
	private TextView commissionChargeTv;

	private String accOutNo;
	private String accOutNickname;
	private String accInNickname;

	// 转出账户ID
	// 转入账户账号
	private String payeeActno = "";
	// 收款人姓名
	private String payeeName = "";
	// 转账金额
	private String amount = "";
	// 备注
	private String remark = "";
	// 币种
	private String currency = "";
	/** 手续费 */
	private String commissionCharge;
	private String transactionId;

	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc;
	private String payeeMobile;
	/** 手机号 */
	private LinearLayout mobileLayout;
	private TextView mobileTv;
	/** 403转出账户余额 */
	private LinearLayout tran_success_out_balance;
	private TextView tv_tran_success_out_balance;
	private String fromAccountType;
	private String fromAccountId;
	
	/** 转账-微信抽奖机会*/
	private View contentView ;
	private double currentAmount;
	private int index = 0;
	private boolean weixinRaffleflag = false;
	private TextView weixinRaffleTv;
	/**基准费用*/
	private LinearLayout ll_need; 
	private String shouldCharge;
	private TextView actChargeTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.tran_my_trans);
		contentView = mInflater.inflate(R.layout.tran_trans_success_activity,
				null);
		tabcontent.removeAllViews();
		tabcontent.addView(contentView);
		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.go_main));
		/**交易流水号*/
		transactionId = (String) TranDataCenter
				.getInstance().getNoRelCrcdRepaySubmitCallBackMap().get(Tran.TRANS_TRANSACTIONID_RES);
		amount = (String) (TranDataCenter
				.getInstance().getNoRelCrcdRepaySubmitCallBackMap()).get(Tran.RELTRANS_AMOUNT_RES);
		/**转账金额*/
		currentAmount =Double.parseDouble(amount);
//		if(currentAmount>=Tran.WEIXIN_RAFFLE_AMOUNT){
//			BaseHttpEngine.showProgressDialog();
//			weixin_raffle_startTime = "";
//			weixin_raffle_endTime = "";
//			/** 微信抽奖获取活动时间*/
//			sendGetActivityAction();
//		}else{
//			initViewAndData();
//		}		
		String	type="CT";	
		Weixin.getInstance().doweixin(tabcontent, TranSuccessActivity1.this, new WeixinSuccess() {
				
				@Override
				public void SuccessCallBack(boolean param) {
					weixinRaffleflag=param;	
						initViewAndData();
				}
			}, transactionId, type,currentAmount);	
		
		

		
	}
	
	/**初始化数据和控件*/
	private void initViewAndData(){
		initData();
		setupView(); // 初始化布局控件
		displayTextView();
		// P403 请求账户余额
		if (fromAccountType.equals(ConstantGloble.GREATWALL)
				|| fromAccountType.equals(ConstantGloble.ZHONGYIN)
				|| fromAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
			// 信用卡
			requestPsnCrcdOutDetail(fromAccountId, currency);
		} else {
			requestAccBankOutDetail(fromAccountId);
		}
	}
	
//	@Override
//	public void sendGetActivityActionCallback(Object resultObj) {
//		super.sendGetActivityActionCallback(resultObj);
//		if("null".equals(resultObj)){
//			BaseHttpEngine.dissMissProgressDialog();
//			initViewAndData();
//			return;
//		}
//		/** 微信抽奖获取系统时间*/
//		requestSystemDateTime();
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
//			LogGloble.e("TranSuccessActivity1", e.getMessage());
//			weixinRaffleflag = false;
//		}
//		
//		if(weixinRaffleflag){
//			// 消息推送取票接口  TODO
//			sendPsnGetTicketForMessage(transactionId);
//		}else{
//			BaseHttpEngine.dissMissProgressDialog();
//			initViewAndData();
//		}
//		contentView.postInvalidate();
//	}
	
//	/***
//	 * 消息推送取票接口 回调
//	 * @param resultObj
//	 */
//	public void requestPsnGetTicketForMessageCallBack(Object resultObj){
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		String ticket = result.get("ticket")+"";
//		String _dateTime = dateTime.replace(" ", "").replace("/", "").replace(":", "");
//		// TODO 注释微信银行接口
//		sendInsertTranSeq(transactionId,_dateTime,ticket);
//	}
//	
//	/***
//	 * PsnGetTicketForMessage 回调
//	 * @param resultObj
//	 */
//	public void sendInsertTranSeqCallback(Object resultObj){
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		//“success”为成功;“fail”为失败
//		String status = result.get("status")+"";
//		if("success".equals(status)){
//			weixinRaffleflag = true;
//			initViewAndData();
//			/**进入微信入口*/
//			weixinRaffleTv= (TextView)findViewById(R.id.tran_weixin_raffle_tv);
//			weixinRaffleTv.setVisibility(View.VISIBLE);
//			weixinRaffleTv.postInvalidate();
//			weixinRaffleTv.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					BaseDroidApp.getInstanse().showErrorDialog(getWXRaffleCue(0, transactionId), R.string.close, R.string.confirm,
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
//			initViewAndData();
//		}
//	}
	

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);
		transactionTv = (TextView) findViewById(R.id.tran_transaction_tv);
		accOutTv = (TextView) findViewById(R.id.tv_acc_out_confirm);
		accOutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accInTv = (TextView) findViewById(R.id.tv_acc_in_confirm);
		accInNicknameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		payeeTv = (TextView) findViewById(R.id.tv_payee_confirm);
		currencyTv = (TextView) findViewById(R.id.tv_tran_currency_confirm);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_confirm);
		commissionChargeTv = (TextView) findViewById(R.id.tv_comcharge_tv);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_confirm);

		mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout);
		mobileTv = (TextView) findViewById(R.id.mobile_number_tv);
		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);
		mLastBtn = (Button) findViewById(R.id.btn_last_trans_confirm);
		mNextBtn = (Button) findViewById(R.id.btn_next_trans_confirm);
		if (!StringUtil.isNullOrEmpty(isSendSmc)
				&& isSendSmc.equals(ConstantGloble.TRUE)) {
			mobileLayout.setVisibility(View.VISIBLE);
			mobileTv.setText(payeeMobile);
		}
		mLastBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(weixinRaffleflag&&index == 0){
					index++;
					BaseDroidApp.getInstanse().showMsgDialogOneBtn(Weixin.getInstance().getWXRaffleCue(1, transactionId),"关闭",
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
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							MainActivity.class);
//					BaseDroidApp.getInstanse().getCurrentAct()
//							.startActivity(intent);
					goToMainActivity();
				}
			}
		});
	}

	/**
	 * 显示数据
	 */
	private void initData() {
		ll_need= (LinearLayout) findViewById(R.id.ll_need);
		Map<String, Object> noRelCrcdRepaySubmitMap = TranDataCenter
				.getInstance().getNoRelCrcdRepaySubmitCallBackMap();
		transactionId = (String) noRelCrcdRepaySubmitMap
				.get(Tran.TRANS_TRANSACTIONID_RES);
		accOutNo = (String) noRelCrcdRepaySubmitMap
				.get(Tran.TRANS_FROMACCOUNTNUM_RES);
		accOutNickname = (String) noRelCrcdRepaySubmitMap
				.get(Tran.TRANS_FROMACCOUNTNICKNAME_RES);
		remark = (String) noRelCrcdRepaySubmitMap
				.get(Tran.RELTRANS_FURINFO_RES);
		fromAccountType = (String) noRelCrcdRepaySubmitMap
				.get(Tran.TRANS_FROMACCOUNTTYPE_RES);
		fromAccountId = getIntent().getStringExtra(Tran.ACC_ACCOUNTID_REQ);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		payeeActno = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		accInNickname = (String) accInInfoMap.get(Tran.TRANS_PAYEEALIAS_RES);
		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);

		currency = ConstantGloble.PRMS_CODE_RMB;
		commissionCharge = (String) noRelCrcdRepaySubmitMap
				.get(Tran.FINAL_COMMISSIONCHARGE);
		if (!StringUtil.isNullOrEmpty(commissionCharge)
				&& !commissionCharge.contains(".")) {
			commissionCharge = StringUtil.parseStringPattern(commissionCharge,
					2);
		}
		// 应收费用
		//	shouldCharge = (String) noRelCrcdRepaySubmitMap
		//			.get(Tran.COMMISSIONCHARGE);
		//	if (!StringUtil.isNullOrEmpty(shouldCharge)) {
		//		shouldCharge = StringUtil.parseStringPattern(shouldCharge, 2);
		//		
		//	}

		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
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
			shouldCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);	
			if (!StringUtil.isNullOrEmpty(shouldCharge)) {
				shouldCharge = StringUtil.parseStringPattern(shouldCharge, 2);
				
				// 转出不是信用卡 转入是信用卡 基准费用为0 不显示基准费用
//				String fromAccountType=(String) noRelCrcdRepaySubmitMap.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
//				if(StringUtil.isNullOrEmpty(fromAccountType)){
//					fromAccountType=(String) noRelCrcdRepaySubmitMap.get(Acc.ACC_ACCOUNTTYPE_RES);
//				}
//				String toAccountType=(String) accInInfoMap.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
//				if(StringUtil.isNullOrEmpty(toAccountType)){
//					toAccountType=(String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
//				}
				if(ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
//						&&LocalData.mycardList.contains(toAccountType) 转入一定是信用卡
					&&Double.parseDouble(shouldCharge)==0){
					ll_need.setVisibility(View.GONE);	
				}
			}
			
//			// 拟收费用
//			commissionCharge = (String) chargeMissionMap
//								.get(Tran.PRECOMMISSION_CHARGE);
//			if (!StringUtil.isNullOrEmpty(commissionCharge)) {
//				commissionCharge = StringUtil.parseStringPattern(commissionCharge, 2);
//				
//			}
		
		}
		
		

//		amount = (String) noRelCrcdRepaySubmitMap.get(Tran.TRANS_AMOUNT_RES);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		// 如果是有短信通知收款人 则手机号码为用户输入手机号
		isSendSmc = userInputMap.get(ConstantGloble.IS_SEND_SMC);
		if (!StringUtil.isNullOrEmpty(isSendSmc)
				&& isSendSmc.equals(ConstantGloble.TRUE)) {
			payeeMobile = userInputMap
					.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		}
		if (StringUtil.isNullOrEmpty(remark)) {
			remark = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
		}
	}

	private void displayTextView() {
		transactionTv.setText(transactionId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		accOutTv.setText(StringUtil.getForSixForString(accOutNo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		accOutNicknameTv.setText(accOutNickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accOutNicknameTv);
		accInTv.setText(StringUtil.getForSixForString(payeeActno));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accInNicknameTv.setText(accInNickname);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInNicknameTv);
		payeeTv.setText(payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeTv);
		currencyTv.setText(LocalData.Currency.get(currency));
		transAmountTv.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transAmountTv);
		// commissionChargeTv.setText(StringUtil.isNullOrEmpty(commissionCharge)?"-":commissionCharge);
		commissionChargeTv.setText(commissionCharge);
		actChargeTv.setText(shouldCharge);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				commissionChargeTv);
		remarkTv.setText(StringUtil.isNull(remark) ? "-" : remark);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_last_trans_confirm:// 用户点击上一步
			finish();
			break;
		case R.id.btn_next_trans_confirm:// 用户点击下一步

			if(weixinRaffleflag&&index == 0){
				index++;
				BaseDroidApp.getInstanse().showMsgDialogOneBtn(Weixin.getInstance().getWXRaffleCue(1, transactionId),"关闭",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
							}
						});
				return;
			}
			
			// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			int moduleFlag = TranDataCenter.getInstance().getModuleType();
			switch (moduleFlag) {
			case ConstantGloble.ACC_MANAGE_TYPE:// 账户管理模块
				intent.setClass(TranSuccessActivity1.this,
						AccManageActivity.class);
				startActivity(intent);
				finish();
				break;
			case ConstantGloble.CRCE_TYPE:// 信用卡模块
				intent.setClass(TranSuccessActivity1.this,
						MyCreditCardActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				intent.setClass(TranSuccessActivity1.this,
						TransferManagerActivity1.class);
				startActivity(intent);
				finish();
				break;
			}
			}

		
	}

	@Override
	public void requestAccBankOutDetailCallback(Object resultObj) {
		super.requestAccBankOutDetailCallback(resultObj);
		refreshTranOutBalance(false, tran_success_out_balance,
				tv_tran_success_out_balance, accountDetailList, currency, null);
	}

	@Override
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		super.requestPsnCrcdOutDetailCallBack(resultObj);
		refreshTranOutBalance(true, tran_success_out_balance,
				tv_tran_success_out_balance, detailList, currency, null);
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		if (resultObj instanceof BiiResponse) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String method = biiResponseBody.getMethod();
		if(Tran.PSNGETTICKETFORMESSAGE.equals(method)
				|| Tran.INSERTTRANSEQ.equals(method)
				|| "GetActivityAction".equals(method)){
			weixinRaffleflag = false;
			setupView();
			displayTextView();
			contentView.postInvalidate();
			return false;
		}
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Acc.QRY_ACC_BALANCE_API.equals(method)
					|| Crcd.CRCD_ACCOUNTDETAIL_API.equals(biiResponseBody
							.getMethod())) {// 余额显示
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
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

	// 屏蔽返回键
	@Override
	public void onBackPressed() {

	}

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		if (Acc.QRY_ACC_BALANCE_API.equals(requestMethod)
				|| Crcd.CRCD_ACCOUNTDETAIL_API.equals(requestMethod)) {

		} else if(Tran.PSNGETTICKETFORMESSAGE.equals(requestMethod)
				|| Tran.INSERTTRANSEQ.equals(requestMethod)
				|| "GetActivityAction".equals(requestMethod)){
			weixinRaffleflag = false;
			setupView();
			displayTextView();
			contentView.postInvalidate();
		} else {
			super.commonHttpErrorCallBack(requestMethod);
		}
	}
}
