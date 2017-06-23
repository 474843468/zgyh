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
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardPaymentActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1;
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
 * @author WJP 行内转账
 */
public class NoRelSuccessActivity1 extends TranBaseActivity {

	/** 确定按钮 */
	private Button mConfirmBtn = null;
	/** 标题 */
	private TextView titleTv = null;
	/** 交易序号 */
	private TextView transactionTv = null;
	/** 转账批次号 */
	private TextView batSeqTv;
	/** 转出账户 */
	private TextView accOutTv = null;
	/** 转出账户地区 */
	private TextView accountIbkNumTv = null;
	/** 转入账户 */
	private TextView accInTv = null;
	/** 币种 */
	// private TextView currencyTv = null;
	/** 钞汇标志 */
	// private TextView cashRemitTv = null;
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 应收费用 */
	// private TextView commissionChargeTv = null;
	/** 实收费用 */
	// private TextView dianHuiTv = null;
	/** 转账类型 */
	// private TextView transTypeTv = null;
	/** 执行方式 */
	private TextView exeTypeTv = null;
	/** 附言 */
	private TextView remarkTv = null;

	private String relExecuteType = null;

	private LinearLayout preDateLL, prePeriodLl;

	/** 执行日期 */
	private TextView exeDateTv = null;
	/** 开始日期 */
	private TextView startDateTv = null;
	/** 结束日期 */
	private TextView endDateTv = null;
	/** 执行周期 */
	private TextView cycleDateTv = null;
	/** 执行次数 */
	private TextView executeTimesTv = null;
	/** 转入账户地区 */
	private TextView accInAreaTv = null;
	/** 转出账户开户行 */
	// private TextView outBranchName = null;
	/** 转入账户开户行 */
	private TextView inBranchName = null;
	/** 转出账户昵称 */
	private TextView accoutNicknameTv;
	/** 转入账户昵称 */
	private TextView accinNickNameTv;
	/** 应收费用 */
	private TextView actChargeDispalyTv;
	private TextView actChargeTv;
	/** 拟收费用 */
	private TextView farkChargeDisplayTv;
	private TextView farkChargeTv;
	/** 头部返回按钮 */
	private Button back;

	/** 交易序列号 */
	private LinearLayout transactionLayout;
	/** 转账批次号 */
	private LinearLayout batSeqLayout;

	/** 周期 */
	private String strWeek = null;
	/** 起始日期 */
	private String strStartDate = null;
	/** 结束日期 */
	private String strEndDate = null;
	/** 执行次数 */
	private String strExecuteTimes = null;
	/** 预约日期执行 执行日期 */
	private String preDateExecuteDate = null;

	private String currency;
	private String transactionId;
	private String amount;
	private String batSeq;
	private String commissionCharge;
	private String furInfo;
	// private String postage;
	// private String cashRemit;
	// private String fromAccountType;
	private String fromAccountNumber;
	private String fromAccountNickName;
	private String fromAccountIbknum;
	// private String toAccountType;
	private String toAccountNumber;
	/** 收款人姓名 */
	private String payeeName;
	private String toAccountIbknum;
	// private String needCount;
	/** 应收费用 */
	private String shouldCharge;
	// 预约日期的时候显示前面手续费接口数据
	private String shouldCharge2;
	/** 转入账户开户行 */
	private String toAccountBankName;

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
	/** 转账-微信抽奖机会 */
	private View contentView;
	private double currentAmount;
	private int index = 0;
	private boolean weixinRaffleflag = false;
	private TextView weixinRaffleTv;
	/** 基准费用 */
	private LinearLayout ll_need;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 二级菜单标识
		secondMenuFlag = getIntent().getIntExtra(SENCOND_MENU_KEY, -1);
		if (secondMenuFlag == TWO_DIMEN_TRAN) {// 从二维码转账那边跳转过来
			setTitle(R.string.two_dimen_scan);
		} else {
			setTitle(this.getString(R.string.tran_my_trans));
		}
		contentView = mInflater.inflate(
				R.layout.tran_confirm_info_no_rel_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(contentView);
		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.go_main));
		initData();
		amount = (String) (TranDataCenter.getInstance()
				.getNoRelBankInDealCallBackMap()).get(Tran.RELTRANS_AMOUNT_RES);

		/** 转账金额 */
		currentAmount = Double.parseDouble(amount);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String toAccountType = (String) accInInfoMap
				.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
		if (StringUtil.isNullOrEmpty(toAccountType)) {
			toAccountType = (String) accInInfoMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		}

		
//		if(currentAmount>=Tran.WEIXIN_RAFFLE_AMOUNT){
//			BaseHttpEngine.showProgressDialog();
//			weixin_raffle_startTime = "";
//			weixin_raffle_endTime = "";
//			/** 微信抽奖获取活动时间*/
//			sendGetActivityAction();
//		}else{
//			setupView(); // 初始化布局控件
//			displayTextView();
//		}
		String type="T";	
		if(LocalData.mycardList.contains(toAccountType)&&"0".equals(relExecuteType)){
			type="CT";	
		}
			Weixin.getInstance().doweixin(tabcontent,
					NoRelSuccessActivity1.this, new WeixinSuccess() {

						@Override
						public void SuccessCallBack(boolean param) {
							weixinRaffleflag = param;

								setupView(); // 初始化布局控件
								displayTextView();
			

						}
					}, transactionId, type,currentAmount);
		
	}
	

	// @Override
	// public void sendGetActivityActionCallback(Object resultObj) {
	// super.sendGetActivityActionCallback(resultObj);
	// if("null".equals(resultObj)){
	// BaseHttpEngine.dissMissProgressDialog();
	// setupView(); // 初始化布局控件
	// displayTextView();
	// return;
	// }
	// /** 微信抽奖获取系统时间*/
	// requestSystemDateTime();
	// }
	//
	// /**
	// * 请求系统时间返回
	// *
	// * @param resultObj dateTime 格式例如：2015/03/31 17:36:41
	// */
	// public void requestSystemDateTimeCallBack(Object resultObj) {
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// @SuppressWarnings("unchecked")
	// Map<String, Object> resultMap = (Map<String, Object>)
	// biiResponseBody.getResult();
	// dateTime = (String) resultMap.get(Comm.DATETME);
	// String _dateTime = dateTime.replace(" ", "").replace("/",
	// "").replace(":", "");
	// try {
	// /**判断系统当前时间是否包含在抽奖的时间内*/
	// weixinRaffleflag = DateUtils.dateCompare(_dateTime,
	// weixin_raffle_startTime, weixin_raffle_endTime);
	// } catch (Exception e) {
	// LogGloble.e("NoRelSuccessActivity1", e.getMessage());
	// weixinRaffleflag = false;
	// }
	//
	// if(weixinRaffleflag){
	// sendPsnGetTicketForMessage(transactionId);
	// }else{
	// BaseHttpEngine.dissMissProgressDialog();
	// setupView(); // 初始化布局控件
	// displayTextView();
	// }
	// contentView.postInvalidate();
	// }
	//
	// /***
	// * 消息推送取票接口 回调
	// * @param resultObj
	// */
	// public void requestPsnGetTicketForMessageCallBack(Object resultObj){
	// Map<String, Object> result = HttpTools.getResponseResult(resultObj);
	// String ticket = result.get("ticket")+"";
	// String _dateTime = dateTime.replace(" ", "").replace("/",
	// "").replace(":", "");
	// // TODO 注释微信银行接口
	// sendInsertTranSeq(transactionId,_dateTime,ticket);
	// }
	//
	// /***
	// * PsnGetTicketForMessage 回调
	// * @param resultObj
	// */
	// @Override
	// public void sendInsertTranSeqCallback(Object resultObj){
	// Map<String, Object> result = HttpTools.getResponseResult(resultObj);
	// //“success”为成功;“fail”为失败
	// String status = result.get("status")+"";
	// if("success".equals(status)){
	// weixinRaffleflag = true;
	// setupView(); // 初始化布局控件
	// displayTextView();
	// /**进入微信入口*/
	// weixinRaffleTv= (TextView)findViewById(R.id.tran_weixin_raffle_tv);
	// weixinRaffleTv.setVisibility(View.VISIBLE);
	// weixinRaffleTv.postInvalidate();
	// weixinRaffleTv.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// BaseDroidApp.getInstanse().showErrorDialog(getWXRaffleCue(0,
	// transactionId), R.string.close, R.string.confirm,
	// new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// switch ((Integer) v.getTag()) {
	// case CustomDialog.TAG_CANCLE:
	// BaseDroidApp.getInstanse().dismissMessageDialog();
	// break;
	// case CustomDialog.TAG_SURE:
	// BaseDroidApp.getInstanse().dismissMessageDialog();
	// skipWeixin();
	// break;
	// }
	//
	// }
	// });
	// }
	// });
	// }else{
	// weixinRaffleflag = false;
	// setupView(); // 初始化布局控件
	// displayTextView();
	// }
	// contentView.postInvalidate();
	// BaseHttpEngine.dissMissProgressDialog();
	// }

	/**
	 * 初始化数据
	 */
	private void initData() {
		ll_need = (LinearLayout) findViewById(R.id.ll_need);
		Map<String, Object> noRelBankInMap = TranDataCenter.getInstance()
				.getNoRelBankInDealCallBackMap();
		fromAccountNumber = (String) noRelBankInMap
				.get(Tran.TRANS_FROMACCOUNTNUM_RES);
		fromAccountNickName = (String) noRelBankInMap
				.get(Tran.TRANS_FROMACCOUNTNICKNAME_RES);
		fromAccountIbknum = (String) noRelBankInMap
				.get(Tran.TRANS_FROMIBKNUM_RES);
		furInfo = (String) noRelBankInMap
				.get(Tran.MANAGE_PREDETAIL_FURINFO_RES);
		fromAccountType = (String) noRelBankInMap
				.get(Tran.TRANS_FROMACCOUNTTYPE_RES);
		fromAccountId = getIntent().getStringExtra(Tran.ACC_ACCOUNTID_REQ);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		// 常用收款人字段
		toAccountNumber = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		if (StringUtil.isNullOrEmpty(toAccountNumber)) {
			toAccountNumber = (String) accInInfoMap.get(Crcd.CRCD_ACCTNUM);
		}

		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		if (StringUtil.isNullOrEmpty(payeeName)) {
			payeeName = (String) accInInfoMap.get(Crcd.CRCD_ACCTNAME_RES);
		}
		// 预交易有返回 收款行转入账户地区
		Map<String, Object> noRelMap = TranDataCenter.getInstance()
				.getNoRelBankInCallBackMap();
		toAccountIbknum = (String) noRelMap.get(Tran.TRANS_PAYEEBANKNUM_REQ);

		transactionId = (String) noRelBankInMap
				.get(Tran.RELTRANS_TRANSACTIONID_RES);
		batSeq = (String) noRelBankInMap.get(Tran.RELTRANS_BATSEQ_RES);
		currency = (String) noRelBankInMap.get(Tran.RELTRANS_CURRENCY_RES);
		commissionCharge = (String) noRelBankInMap
				.get(Tran.FINAL_COMMISSIONCHARGE);
		// needCount = (String) noRelBankInMap.get(Tran.RELTRANS_NEEDCOUNT_RES);
		if (!StringUtil.isNullOrEmpty(commissionCharge)) {
			commissionCharge = StringUtil.parseStringPattern(commissionCharge,
					2);
		}
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {// 预约日期，周期 都用前面数据

			TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
			tv_toast.setVisibility(View.GONE);
			String remitSetMealFlag = (String) chargeMissionMap
					.get(Tran.REMITSETMEAL_FLAG);
			if (!StringUtil.isNull(remitSetMealFlag)) {
				if (remitSetMealFlag
						.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_toast.setVisibility(View.VISIBLE);
					tv_toast.setText(getString(R.string.tran_remitSetMealFlag_success));
				}
			}
			String getChargeFlag = (String) chargeMissionMap
					.get(Tran.GETCHARGE_FLAG);
			if (ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
					&& !chargeMissionMap
							.containsKey(Tran.NEED_COMMISSION_CHARGE)) {

			} else {
				ll_need.setVisibility(View.VISIBLE);
			}
			// 应收费用
			shouldCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge)) {
				shouldCharge = StringUtil.parseStringPattern(shouldCharge, 2);
				// 转出是借记卡 转入是信用卡 基准费用为0 不显示基准费用
				String fromAccountType = (String) noRelBankInMap
						.get(Tran.RELTRANS_FROMACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(fromAccountType)) {
					fromAccountType = (String) noRelBankInMap
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				String toAccountType = (String) accInInfoMap
						.get(Tran.RELTRANS_TOACCOUNTTYPE_RES);
				if (StringUtil.isNullOrEmpty(toAccountType)) {
					toAccountType = (String) accInInfoMap
							.get(Acc.ACC_ACCOUNTTYPE_RES);
				}
				if (ConstantGloble.ACC_TYPE_BRO.equals(fromAccountType)
						&& LocalData.mycardList.contains(toAccountType)
						&& Double.parseDouble(shouldCharge) == 0) {
					ll_need.setVisibility(View.GONE);
				}
			}
			shouldCharge2 = (String) chargeMissionMap
					.get(Tran.PRECOMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge2)) {
				shouldCharge2 = StringUtil.parseStringPattern(shouldCharge2, 2);

			}
		}

		// 转瑞账户开户行
		toAccountBankName = (String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES);
		if (StringUtil.isNullOrEmpty(toAccountBankName)) {
			toAccountBankName = (String) accInInfoMap.get(Crcd.CRCD_ACCTBANK);
		}
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		if (!StringUtil.isNullOrEmpty(userInputMap)) {
			// 附言没有返回数据 从用户输入带过来
			if (StringUtil.isNullOrEmpty(furInfo)) {
				furInfo = (String) userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
			}
			// 预约日期执行 执行日期
			preDateExecuteDate = (String) userInputMap.get(Tran.INPUT_PRE_DATE);
			// 起始日期
			strStartDate = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_START_DATE);
			// 结束日期
			strEndDate = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_END_DATE);
			// 执行次数
			strExecuteTimes = (String) userInputMap
					.get(Tran.INPUT_PRE_PERIOD_EXECUTE_TIMES);
			/** 执行周期 */
			strWeek = (String) userInputMap.get(Tran.INPUT_PRE_PERIOD_WEEK);
			// 执行方式
			relExecuteType = userInputMap.get(Tran.MANAGE_PRE_transMode_RES);
		}

		// 如果是有短信通知收款人 则手机号码为用户输入手机号
		isSendSmc = userInputMap.get(ConstantGloble.IS_SEND_SMC);
		if (!StringUtil.isNullOrEmpty(isSendSmc)
				&& isSendSmc.equals(ConstantGloble.TRUE)) {
			payeeMobile = userInputMap.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
		}
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(3);

		transactionLayout = (LinearLayout) findViewById(R.id.tran_transaction_layout);
		transactionTv = (TextView) findViewById(R.id.tran_transaction_tv);
		batSeqLayout = (LinearLayout) findViewById(R.id.tran_batseq_layout);
		batSeqTv = (TextView) findViewById(R.id.tran_batseq_tv);
		titleTv = (TextView) findViewById(R.id.tv_content);

		accInTv = (TextView) findViewById(R.id.tv_acc_in_rel_confirm);
		accountIbkNumTv = (TextView) findViewById(R.id.tv_acc_out_area_rel_confirm);
		accOutTv = (TextView) findViewById(R.id.tv_acc_out_rel_confirm);
		// currencyTv = (TextView)
		// findViewById(R.id.tv_tran_currency_rel_confirm);
		transAmountTv = (TextView) findViewById(R.id.tv_transferAmount_rel_confirm);
		exeTypeTv = (TextView) findViewById(R.id.tv_executeType_rel_confirm);
		remarkTv = (TextView) findViewById(R.id.tv_remark_info_rel_confirm);

		preDateLL = (LinearLayout) findViewById(R.id.ll_preDate_confirm_info);
		prePeriodLl = (LinearLayout) findViewById(R.id.ll_prePeriod_confirm_info);

		accoutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accinNickNameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		actChargeDispalyTv = (TextView) findViewById(R.id.tran_act_charge_display_tv);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);

		exeDateTv = (TextView) findViewById(R.id.tv_exeDate_info_rel_confirm);
		startDateTv = (TextView) findViewById(R.id.tv_startDate_info_rel_confirm);
		endDateTv = (TextView) findViewById(R.id.tv_endDate_info_rel_confirm);
		cycleDateTv = (TextView) findViewById(R.id.tv_cycleDate_info_rel_confirm);
		executeTimesTv = (TextView) findViewById(R.id.tv_execute_times_info_rel_confirm);

		accInAreaTv = (TextView) findViewById(R.id.tv_acc_in_area_rel_confirm);
		inBranchName = (TextView) findViewById(R.id.tv_branch_name_in_rel_confirm);
		// outBranchName = (TextView)
		// findViewById(R.id.tv_branch_name_rel_confirm);
		// TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
		// tv_toast.setVisibility(View.GONE);
		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);
		mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout);
		mobileTv = (TextView) findViewById(R.id.mobile_number_tv);
		if (!StringUtil.isNullOrEmpty(isSendSmc)
				&& isSendSmc.equals(ConstantGloble.TRUE)) {
			mobileLayout.setVisibility(View.VISIBLE);
			mobileTv.setText(payeeMobile);
		}

		mConfirmBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);
		mConfirmBtn.setText(this.getString(R.string.finish));
		mConfirmBtn.setOnClickListener(listener);
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (weixinRaffleflag && index == 0) {
					index++;
					BaseDroidApp.getInstanse().showMsgDialogOneBtn(
							Weixin.getInstance().getWXRaffleCue(1,
									transactionId), "关闭",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse()
											.dismissMessageDialog();
								}
							});

				} else {
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

	private void displayTextView() {

		transactionTv.setText(transactionId);
		batSeqTv.setText(batSeq);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, batSeqTv);
		// currencyTv.setText(LocalData.Currency.get(currency));
		accoutNicknameTv.setText(fromAccountNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accoutNicknameTv);
		accinNickNameTv.setText(payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accinNickNameTv);
		transAmountTv.setText(StringUtil.parseStringPattern(amount, 2));
		remarkTv.setText(StringUtil.isNullOrEmpty(furInfo) ? "-" : furInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);

		accInTv.setText(StringUtil.getForSixForString(toAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accInAreaTv.setText(LocalData.Province.get(toAccountIbknum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInAreaTv);
		inBranchName.setText(toAccountBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				inBranchName);

		accOutTv.setText(StringUtil.getForSixForString(fromAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		accountIbkNumTv.setText(LocalData.Province.get(fromAccountIbknum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accountIbkNumTv);
		// outBranchName.setText(fromAccountBankName);
		exeTypeTv.setText(LocalData.TransModeDisplay.get(relExecuteType));
		actChargeTv.setText(shouldCharge);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				actChargeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				farkChargeTv);
		switch (Integer.parseInt(relExecuteType)) {
		case 0:
			titleTv.setText(this.getResources().getString(
					R.string.mobile_tran_have_acc_success_title));
			// 显示实收费用
			// farkChargeDisplayTv.setText(this.getResources().getString(
			// R.string.trans_fact_charge));
			transactionLayout.setVisibility(View.VISIBLE);
			// farkChargeTv.setText(StringUtil.isNullOrEmpty(commissionCharge)?"-":commissionCharge);
			farkChargeTv.setText(commissionCharge);
			// P403 请求账户余额
			if (fromAccountType.equals(ConstantGloble.GREATWALL)
					|| fromAccountType.equals(ConstantGloble.ZHONGYIN)
					|| fromAccountType.equals(ConstantGloble.SINGLEWAIBI)) {
				// 信用卡
				requestPsnCrcdOutDetail(fromAccountId, currency);
			} else {
				requestAccBankOutDetail(fromAccountId);
			}
			break;
		// 预约日期执行
		case 1:
			titleTv.setText(this.getResources().getString(
					R.string.tran_success_title_date));
			// 显示实收费用
			// farkChargeDisplayTv.setText(this.getResources().getString(
			// R.string.trans_fact_charge));
			// farkChargeTv.setText(StringUtil.isNull(shouldCharge2) ? "-"
			// : shouldCharge2);
			farkChargeTv.setText(shouldCharge2);

			batSeqLayout.setVisibility(View.VISIBLE);
			preDateLL.setVisibility(View.VISIBLE);
			prePeriodLl.setVisibility(View.GONE);
			exeDateTv.setText(preDateExecuteDate);
			break;
		case 2:
			titleTv.setText(this.getResources().getString(
					R.string.tran_success_title_date));
			// 单笔应收 单笔拟收
			actChargeDispalyTv.setText(this.getResources().getString(
					R.string.trans_single_act_charge));
			farkChargeDisplayTv.setText(this.getResources().getString(
					R.string.trans_single_fack_charge));
			// farkChargeTv.setText(StringUtil.isNull(shouldCharge2) ? "-"
			// : shouldCharge2);
			farkChargeTv.setText(shouldCharge2);

			batSeqLayout.setVisibility(View.VISIBLE);
			prePeriodLl.setVisibility(View.VISIBLE);
			preDateLL.setVisibility(View.GONE);
			startDateTv.setText(strStartDate);
			endDateTv.setText(strEndDate);
			cycleDateTv.setText(LocalData.Frequency.get(strWeek));
			executeTimesTv.setText(strExecuteTimes);
			break;
		}
	}

	/**
	 * 屏蔽返回键
	 */
	@Override
	public void onBackPressed() {
	}

	/**
	 * 点击事件
	 */
	private OnClickListener listener = new OnClickListener() {
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
				return;
			}
			// 关闭所有活动的Activity，除了MainActivity，并开跳转到我要转账页面
			ActivityTaskManager.getInstance().removeAllActivity();
			// 二级菜单标识
			secondMenuFlag = getIntent().getIntExtra(SENCOND_MENU_KEY, -1);
			if (secondMenuFlag == TWO_DIMEN_TRAN) {// 从二维码转账那边跳转过来
				Intent intent = new Intent(NoRelSuccessActivity1.this,
						TwoDimenTransActivity1.class);
				startActivity(intent);
				finish();
			} else {
				// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				int moduleFlag = TranDataCenter.getInstance().getModuleType();
				switch (moduleFlag) {
				case ConstantGloble.ACC_MANAGE_TYPE:// 账户管理模块
					intent.setClass(NoRelSuccessActivity1.this,
							AccManageActivity.class);
					startActivity(intent);
					finish();
					break;
				case ConstantGloble.CRCE_TYPE:// 信用卡模块
					intent.setClass(NoRelSuccessActivity1.this,
							MyCreditCardActivity.class);
					startActivity(intent);
					finish();
					break;
				case ConstantGloble.CRCE_TYPE1:// 信用卡模块
					intent.setClass(NoRelSuccessActivity1.this,
							MyCardPaymentActivity.class);
					startActivity(intent);
					finish();
					break;
				default:
					intent.setClass(NoRelSuccessActivity1.this,
							TransferManagerActivity1.class);
					startActivity(intent);
					finish();
					break;
				}

			}
		}
	};

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
			if (Tran.PSNGETTICKETFORMESSAGE.equals(method)
					|| Tran.INSERTTRANSEQ.equals(method)
					|| "GetActivityAction".equals(method)) {
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

	@Override
	public void commonHttpErrorCallBack(String requestMethod) {
		if (Acc.QRY_ACC_BALANCE_API.equals(requestMethod)
				|| Crcd.CRCD_ACCOUNTDETAIL_API.equals(requestMethod)) {

		} else if (Tran.PSNGETTICKETFORMESSAGE.equals(requestMethod)
				|| Tran.INSERTTRANSEQ.equals(requestMethod)
				|| "GetActivityAction".equals(requestMethod)) {
			weixinRaffleflag = false;
			setupView();
			displayTextView();
			contentView.postInvalidate();
		} else {

			super.commonHttpErrorCallBack(requestMethod);
		}
	}
}
