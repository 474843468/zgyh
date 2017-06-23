package com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
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
import com.chinamworld.bocmbci.bii.constant.Ecard;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.managetrans.managetranrecords.ManageTransRecordActivity1;
import com.chinamworld.bocmbci.biz.tran.managetrans.premanage.QueryDateActivity1;
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
public class NoRelBankOtherSuccessActivity1 extends TranBaseActivity {

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

	private LinearLayout preDateLL;

	/** 执行日期 */
	private TextView exeDateTv = null;
	/** 转入账户开户行 */
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
	// private TextView actChargeDispalyTv;
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
	// private String toAccountIbknum;
	/** 转入账户开户行 */
	private String toOrgName;
	// private String needCount;
	/** 应收费用 */
	private String shouldCharge;
	/** 转入账户开户行 */
	private String toAccountBankName;

	/** 收款人姓名 */
	private String payeeName;
	/** 是否发送手机短信通知收款人标示 */
	private String isSendSmc;
	/** 手机号 */
	private LinearLayout mobileLayout;
	private TextView mobileTv;

	// 预约日期的时候显示前面手续费接口数据
	private String shouldCharge2;
	private Map<String, Object> newResultMap;
	/** 403转出账户余额 */
	private LinearLayout tran_success_out_balance;
	private TextView tv_tran_success_out_balance;
	private String fromAccountType = "";
	private String fromAccountId;
	
	/** 转账-微信抽奖机会*/
	private View contentView ;
	private double currentAmount;
	private int index = 0;
	/** 是否在抽奖活动的有效日期内*/
	private boolean weixinRaffleflag = false;
	private TextView weixinRaffleTv;
	//503转账汇款优化
	/**转账汇款 提交成功大标题*/
	private TextView tv_content_big;


	
	/**开户行*/
	private LinearLayout ll_bankname; 
	/**基准费用*/
	private LinearLayout ll_need; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.tran_my_trans);
		contentView = mInflater.inflate(
				R.layout.tran_confirm_info_no_rel_bank_other_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(contentView);
		back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.go_main));
		/** 转账方式是否支持微信抽奖*/
		boolean weiXinRaffleTrade = getIntent().getBooleanExtra(Tran.WEIXIN_RAFFLE, false);
		newResultMap = TranDataCenter.getInstance()
				.getNationalChangeBookingMap();
		setupView(); // 初始化布局控件
		initData();
		/**转账金额*/
		currentAmount =Double.parseDouble(amount);
		if(weiXinRaffleTrade&&currentAmount>=Tran.WEIXIN_RAFFLE_AMOUNT){
//			BaseHttpEngine.showProgressDialog();
//			weixin_raffle_startTime = "";
//			weixin_raffle_endTime = "";
//			/** 微信抽奖获取活动时间*/
//			sendGetActivityAction();
			Weixin.getInstance().doweixin(tabcontent, NoRelBankOtherSuccessActivity1.this, new WeixinSuccess() {
				
				@Override
				public void SuccessCallBack(boolean param) {
					weixinRaffleflag=param;	
					contentView.postInvalidate();
				}
			}, transactionId, "T",currentAmount);
		}
		else{
			weixinRaffleflag = false;
			contentView.postInvalidate();
		}
		
		
	}
	
//	/***
//	 * 微信抽奖 获取抽奖活动时间回调
//	 */
//	public void sendGetActivityActionCallback(Object resultObj){
//		super.sendGetActivityActionCallback(resultObj);
//		if("null".equals(resultObj)){
//			BaseHttpEngine.dissMissProgressDialog();
//			weixinRaffleflag = false;
//			contentView.postInvalidate();
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
//			LogGloble.e("NoRelBankOtherSuccessActivity1", e.getMessage());
//			weixinRaffleflag = false;
//		}
//		
//		if(weixinRaffleflag){
//			// 消息推送取票接口  TODO
//			sendPsnGetTicketForMessage(transactionId);
//		}else{
//			BaseHttpEngine.dissMissProgressDialog();
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
//		sendInsertTranSeq(transactionId,_dateTime,ticket);
//	}
//	
//	/***
//	 * PsnGetTicketForMessage 回调
//	 * @param resultObj
//	 */
//	@Override
//	public void sendInsertTranSeqCallback(Object resultObj){
//		BaseHttpEngine.dissMissProgressDialog();
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		//“success”为成功;“fail”为失败
//		String status = result.get("status")+"";
//		if("success".equals(status)){
//			weixinRaffleflag = true;
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
//		}
//		contentView.postInvalidate();
//	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tranTypeFlag = this.getIntent().getIntExtra(TRANS_TYPE, -1);
		if(tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER
				||tranTypeFlag == TRANTYPE_SHISHI_DIR_BANKOTHER	){
				ll_bankname.setVisibility(View.GONE);
				
			}
		newResultMap = TranDataCenter.getInstance()
				.getNationalChangeBookingMap();
		Map<String, Object> noRelBankOtherMap = TranDataCenter.getInstance()
				.getNoRelBankOtherDealCallBackMap();
		if (!StringUtil.isNullOrEmpty(newResultMap)) {
			LinearLayout ll_type = (LinearLayout) findViewById(R.id.ll_type);
			LinearLayout ll_date = (LinearLayout) findViewById(R.id.ll_date);
			TextView pre_exe_date = (TextView) findViewById(R.id.tv_pre_exe_date_confirm);
			ll_type.setVisibility(View.GONE);
			ll_date.setVisibility(View.VISIBLE);
			String date = (String) noRelBankOtherMap
					.get(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_RES);
			pre_exe_date.setText(date);
			furInfo = (String) newResultMap
					.get(Tran.TRANS_NATIONAL_FURINFO_RES);
			transactionId = (String) newResultMap
					.get(Tran.TRANS_NATIONAL_TRANSACTIONID_RES);
			batSeq = (String) newResultMap.get(Tran.TRANS_NATIONAL_BATSEQ_RES);
			currency = (String) newResultMap
					.get(Tran.TRANS_NATIONAL_CURRENCY_RES);
			amount = (String) newResultMap.get(Tran.TRANS_NATIONAL_AMOUNT_RES);
			Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
					.getCommissionChargeMap();
			if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
				commissionCharge = (String) chargeMissionMap
						.get(Tran.PRECOMMISSION_CHARGE);
				// needCount = (String)
				// noRelBankOtherMap.get(Tran.RELTRANS_NEEDCOUNT_RES);
				if (!StringUtil.isNullOrEmpty(commissionCharge)) {
					commissionCharge = StringUtil.parseStringPattern(
							commissionCharge, 2);
					
				}
			}
		} else {
			furInfo = (String) noRelBankOtherMap
					.get(Tran.TRANS_BOCNATIONAL_FURINFO_RES);
			transactionId = (String) noRelBankOtherMap
					.get(Tran.RELTRANS_TRANSACTIONID_RES);
			batSeq = (String) noRelBankOtherMap.get(Tran.RELTRANS_BATSEQ_RES);
			currency = (String) noRelBankOtherMap
					.get(Tran.RELTRANS_CURRENCY_RES);
			amount = (String) noRelBankOtherMap.get(Tran.RELTRANS_AMOUNT_RES);
			commissionCharge = (String) noRelBankOtherMap
					.get(Tran.FINAL_COMMISSIONCHARGE);
			if (!StringUtil.isNullOrEmpty(commissionCharge)) {
				commissionCharge = StringUtil.parseStringPattern(
						commissionCharge, 2);
				
			}
		}
		fromAccountType = (String) noRelBankOtherMap
				.get(Tran.TRANS_FROMACCOUNTTYPE_RES);
		fromAccountId = getIntent().getStringExtra(Tran.ACC_ACCOUNTID_REQ);
		fromAccountNumber = (String) noRelBankOtherMap
				.get(Tran.TRANS_FROMACCOUNTNUM_RES);
		fromAccountNickName = (String) noRelBankOtherMap
				.get(Tran.TRANS_FROMACCOUNTNICKNAME_RES);
		fromAccountIbknum = (String) noRelBankOtherMap
				.get(Tran.TRANS_FROMIBKNUM_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		// 常用收款人字段
		toAccountNumber = (String) accInInfoMap.get(Comm.ACCOUNTNUMBER);
		if(StringUtil.isNullOrEmpty(toAccountNumber)){
			toAccountNumber=(String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
		}
		payeeName = (String) accInInfoMap.get(Comm.ACCOUNT_NAME);
		if(StringUtil.isNullOrEmpty(payeeName)){
			payeeName=(String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);
		}
		toOrgName = (String) accInInfoMap.get(Tran.TRANS_ADDRESS_RES);
		if(StringUtil.isNullOrEmpty(toOrgName)){
			toOrgName=(String) accInInfoMap.get(Ecard.ECARD_OPENINGBANKNAME_RES);
		}
		// 转瑞账户开户行
		toAccountBankName = (String) accInInfoMap.get(Tran.TRANS_BANKNAME_RES);
		Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
				.getCommissionChargeMap();
		if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
			
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
			if(ConstantGloble.ACC_CURRENTINDEX_VALUE.equals(getChargeFlag)
					&&!chargeMissionMap.containsKey(Tran.NEED_COMMISSION_CHARGE)){
				
			}else{
				ll_need.setVisibility(View.VISIBLE);	
			}
			// 应收费用
			shouldCharge = (String) chargeMissionMap
					.get(Tran.NEED_COMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge)) {
				shouldCharge = StringUtil.parseStringPattern(shouldCharge, 2);
				
			}
			shouldCharge2 = (String) chargeMissionMap
					.get(Tran.PRECOMMISSION_CHARGE);
			if (!StringUtil.isNullOrEmpty(shouldCharge2)) {
				shouldCharge2 = StringUtil.parseStringPattern(shouldCharge2, 2);
				
			}
		}
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		if (tranTypeFlag == TRANTYPE_SHISHI_BANKOTHER) {
			findViewById(R.id.textview_tishi).setVisibility(View.GONE);
			// 跨行实时
			Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
					.getAccOutInfoMap();
			fromAccountId = (String) accOutInfoMap.get(Tran.ACC_ACCOUNTID_REQ);
			// 转出账户
			fromAccountNumber = (String) accOutInfoMap
					.get(Tran.ACCOUNTNUMBER_RES);
			
			// 转出账户昵称
			fromAccountNickName = (String) accOutInfoMap.get(Tran.NICKNAME_RES);
			// 转账账户地区
			fromAccountIbknum = (String) accOutInfoMap
					.get(Tran.ACCOUNTIBKNUM_RES);
			fromAccountType = (String) accOutInfoMap.get(Tran.ACCOUNTTYPE_RES);
			toAccountNumber = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTNO_REQ);
			if(StringUtil.isNullOrEmpty(toAccountNumber)){
				toAccountNumber=(String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNUMBER_RES);
			}
			payeeName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
			if(StringUtil.isNullOrEmpty(payeeName)){
				payeeName=(String) accInInfoMap.get(Ecard.ECARD_PAYEEACCOUNTNAME_RES);
			}
			toOrgName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
			if(StringUtil.isNullOrEmpty(toOrgName)){
				toOrgName=(String) accInInfoMap.get(Ecard.ECARD_OPENINGBANKNAME_RES);
			}
			toAccountBankName = (String) accInInfoMap
					.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
			furInfo = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
			transactionId = (String) noRelBankOtherMap
					.get(Tran.EBPSSUB_TRANSACTIONID_RES);
			batSeq = (String) noRelBankOtherMap
					.get(Tran.EBPSSUB_BATSEQUENCE_RES);
			currency = ConstantGloble.PRMS_CODE_RMB;
			amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);

			// TODO 手续费
			commissionCharge = shouldCharge2;
		}

		if (!StringUtil.isNullOrEmpty(userInputMap)) {
			// 附言没有返回数据 从用户输入带过来
			// furInfo = (String) userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
			// 预约日期执行 执行日期
			preDateExecuteDate = (String) userInputMap.get(Tran.INPUT_PRE_DATE);
			// 执行方式
			relExecuteType = userInputMap.get(Tran.MANAGE_PRE_transMode_RES);
		}

		// 如果是有短信通知收款人 则手机号码为用户输入手机号
		isSendSmc = userInputMap.get(ConstantGloble.IS_SEND_SMC);
		if (!StringUtil.isNullOrEmpty(isSendSmc)
				&& isSendSmc.equals(ConstantGloble.TRUE)) {
			String payeeMobile = userInputMap
					.get(Tran.INPUT_PAYEE_TRANSFER_MOBILE);
			mobileLayout.setVisibility(View.VISIBLE);
			mobileTv.setText(payeeMobile);
		}

		displayTextView();
		//如果是关闭 20秒倒计时dialog 进入转账完成界面 标题显示修改
		if(TranDataCenter.getInstance().getColseDialog()){
			String head = "到账时间取决于对方银行处理情况，通常会在24小时内到达收款行，如有疑问请致95566或使用";
			String middle = "转账记录";
			String end = "查询交易结果。";
			
			View.OnClickListener l = new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					
					finish();
					ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
					Intent intent =new Intent(NoRelBankOtherSuccessActivity1.this,
							ManageTransRecordActivity1.class);
					startActivity(intent);
				}
			};

			Clickable  c =new Clickable(l);

			SpannableString span = new SpannableString(head + middle + end);

			span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_real)), head.length(),
					head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			span.setSpan(c, head.length(), head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			tv_content_big.setVisibility(View.VISIBLE);
//			titleTv.setTextColor(Color.BLACK);
			titleTv.setGravity(Gravity.LEFT);
			titleTv.setText(span);
			//设置开始响应事件
			titleTv.setMovementMethod(LinkMovementMethod.getInstance());
			//重置
			TranDataCenter.getInstance().setColseDialog(false);
		}
	}

	
	class Clickable extends ClickableSpan implements OnClickListener{
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l){
			mListener = l;
		}

		@Override
		public void onClick(View v){
			mListener.onClick(v);
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

		accoutNicknameTv = (TextView) findViewById(R.id.tran_out_nickname_tv);
		accinNickNameTv = (TextView) findViewById(R.id.tran_in_nickname_tv);
		// actChargeDispalyTv = (TextView)
		// findViewById(R.id.tran_act_charge_display_tv);
		actChargeTv = (TextView) findViewById(R.id.tran_act_charge_tv);
		farkChargeDisplayTv = (TextView) findViewById(R.id.tran_fack_charge_display_tv);
		farkChargeTv = (TextView) findViewById(R.id.tran_fack_charge_tv);

		exeDateTv = (TextView) findViewById(R.id.tv_exeDate_info_rel_confirm);

		mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout);
		mobileTv = (TextView) findViewById(R.id.mobile_number_tv);

		accInAreaTv = (TextView) findViewById(R.id.tv_acc_in_area_rel_confirm);

		ll_bankname= (LinearLayout) findViewById(R.id.ll_bankname);
		
		ll_need= (LinearLayout) findViewById(R.id.ll_need);

		inBranchName = (TextView) findViewById(R.id.tv_branch_name_in_rel_confirm);
		// outBranchName = (TextView)
		// findViewById(R.id.tv_branch_name_rel_confirm);

		tv_content_big = (TextView) findViewById(R.id.tv_content_big);
//		TextView tv_toast = (TextView) findViewById(R.id.tv_toast);
//		tv_toast.setVisibility(View.GONE);

		tran_success_out_balance = (LinearLayout) findViewById(R.id.tran_success_out_balance);
		tv_tran_success_out_balance = (TextView) findViewById(R.id.tv_tran_success_out_balance);
		mConfirmBtn = (Button) findViewById(R.id.btn_next_trans_rel_confirm);
		mConfirmBtn.setText(this.getString(R.string.finish));
		mConfirmBtn.setOnClickListener(listener);
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

	private void displayTextView() {

		transactionTv.setText(transactionId);
		batSeqTv.setText(batSeq);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, batSeqTv);
		// currencyTv.setText(LocalData.Currency.get(currency));
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		// currencyTv);
		accoutNicknameTv.setText(fromAccountNickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accoutNicknameTv);
		accinNickNameTv.setText(payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accinNickNameTv);
		transAmountTv.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transAmountTv);
		remarkTv.setText(StringUtil.isNull(furInfo) ? "-" : furInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);

		accInTv.setText(StringUtil.getForSixForString(toAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		accInAreaTv.setText(toAccountBankName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accInAreaTv);
		inBranchName.setText(toOrgName);
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

		switch (Integer.parseInt(relExecuteType)) {
		case 0:
			farkChargeTv.setText(commissionCharge);
			if (!StringUtil.isNullOrEmpty(newResultMap)) {
				titleTv.setText(this.getResources().getString(
						R.string.tran_success_title_order_date));
				batSeqLayout.setVisibility(View.VISIBLE);
				Button btn_order = (Button) findViewById(R.id.btn_order);
				btn_order.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btn_order
						.getLayoutParams();
				mConfirmBtn.setLayoutParams(param);
				mConfirmBtn.setBackgroundResource(R.drawable.btn_red_big);
				btn_order.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 进入预约流程
						ActivityTaskManager.getInstance().removeAllActivity();
						Intent intent = new Intent(
								NoRelBankOtherSuccessActivity1.this,
								QueryDateActivity1.class);
						startActivity(intent);
						finish();
					}
				});
			} else {
			// 	/** 跨行 */
//				protected static final int TRANTYPE_NOREL_BANKOTHER = 5;	
//				/** 跨行定向转账 */
//				protected static final int TRANTYPE_DIR_BANKOTHER = 9;
//				/** 跨行实时转账 */
//				protected static final int TRANTYPE_SHISHI_BANKOTHER = 11;
//				/** 跨行实时定向付款 */
//				protected static final int TRANTYPE_SHISHI_DIR_BANKOTHER = 12;
			if(tranTypeFlag==TRANTYPE_NOREL_BANKOTHER||tranTypeFlag==TRANTYPE_DIR_BANKOTHER){
				titleTv.setText(this.getResources().getString(
						R.string.mobile_tran_have_acc_success_title1));
			}else if(tranTypeFlag==TRANTYPE_SHISHI_BANKOTHER||tranTypeFlag==TRANTYPE_SHISHI_DIR_BANKOTHER){
				titleTv.setText(this.getResources().getString(
						R.string.mobile_tran_have_acc_success_title));
			}else{
				titleTv.setText(this.getResources().getString(
						R.string.mobile_tran_have_acc_success_title));
			}
				
				transactionLayout.setVisibility(View.VISIBLE);
				// P403 请求账户余额
				if (!StringUtil.isNullOrEmpty(fromAccountType)) {
					if (fromAccountType.equals(ConstantGloble.GREATWALL)
							|| fromAccountType.equals(ConstantGloble.ZHONGYIN)
							|| fromAccountType
									.equals(ConstantGloble.SINGLEWAIBI)) {
						// 信用卡
						requestPsnCrcdOutDetail(fromAccountId, currency);
					} else {
						requestAccBankOutDetail(fromAccountId);
					}
				}

			}

			break;
		// 预约日期执行
		case 1:
			titleTv.setText(this.getResources().getString(
					R.string.tran_success_title_date));
			farkChargeTv.setText(shouldCharge2);
			batSeqLayout.setVisibility(View.VISIBLE);
			preDateLL.setVisibility(View.VISIBLE);
			exeDateTv.setText(preDateExecuteDate);
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

				Intent intent = new Intent();
				int moduleFlag = TranDataCenter.getInstance().getModuleType();
				switch (moduleFlag) {
				case ConstantGloble.ACC_MANAGE_TYPE:// 账户管理模块
					// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
					ActivityTaskManager.getInstance().removeAllActivity();
					intent.setClass(NoRelBankOtherSuccessActivity1.this,
							AccManageActivity.class);
					startActivity(intent);
					finish();
					break;
				case ConstantGloble.CRCE_TYPE:// 信用卡模块
					// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
					ActivityTaskManager.getInstance().removeAllActivity();
					intent.setClass(NoRelBankOtherSuccessActivity1.this,
							MyCreditCardActivity.class);
					startActivity(intent);
					finish();
					break;
				default:
					
						// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity,销毁数据
						ActivityTaskManager.getInstance().removeAllActivity();
						intent.setClass(NoRelBankOtherSuccessActivity1.this,
								TransferManagerActivity1.class);
						startActivity(intent);
						finish();
					
					break;
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
		/**抽奖取票、微信银行录入流水号等接口*/
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
