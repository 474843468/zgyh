package com.chinamworld.bocmbci.biz.tran.managetrans.managetranrecords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 转账记录管理详情
 * 
 * @author
 * 
 */
public class TransRecordDetailActivity1 extends ManageTransBaseActivity {

	private Button confirmBtn = null;
	/** 转账批次号 */
	private TextView mTransactonTv = null;
	/** 转出账户 */
	private TextView mAccOutv = null;
	/** 转出账户地区 */
	private TextView mPayerIbknumTv = null;
	/** 转入账户 */
	private TextView mAccInTv = null;
	/** 收款人姓名 */
	private TextView mPayeeNameTv = null;
	/** 币种 */
	private TextView mCurreyTv = null;
	/** 转账金额 */
	private TextView mAmountTv = null;
	/** 手续费 */
	private TextView mChargeTv = null;
	/** 电汇费 */
	private TextView postageTv = null;
	/** 渠道类型 */
	private TextView mChannelTv = null;
	/** 附言 */
	private TextView mfuYanTv = null;
	/** 转账状态 */
	private TextView mStateTv = null;

	private String transactionId;
	private String payerAccountNumber;
	private String payerIbknum;
	private String payeeAccountNumber;
	private String payeeAccountName;
	private String feeCur;
	private String amount;
	private String commissionCharge;
	private String channel;
	private String furInfo;
	private String status;
	/** 电汇费 */
	private String postage;
	/** 失败原因 */
	private String failReson;
	private String reexchangestate=null;//	退汇交易状态 0表示未退汇1表示已经退汇 
	private String transId=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_trans_recrods));
		View view = mInflater.inflate(
				R.layout.tran_manage_records_detail_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		// modify by wjp 步骤栏没有显示 但是这个代码不要删除 会影响布局
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);
		reexchangestate=getIntent().getStringExtra(Tran.MANAGE_RECORDS_REEXCHANGESTATE_RES);
		transId=getIntent().getStringExtra(Tran.MANAGE_RECORDS_TRANSACTIONID_RES);
		initData();
		setupView();
		displayTextView();

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private void initData() {
		Map<String, Object> detailMap = TranDataCenter.getInstance()
				.getQueryDetailCallBackMap();
//		transactionId = (String) detailMap
//				.get(Tran.MANAGE_RECORDSDETAIL_BATSEQ_RES);
		transactionId = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_TRANSACTIONID_RES);
		
		payerAccountNumber = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYERACCOUNTNUMBER_RES);
		payerIbknum = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYERIBKNUM_RES);
		payeeAccountNumber = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYEEACCOUNTNUMBER_RES);
		payeeAccountName = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYEEACCOUNTNAME_RES);
		feeCur = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_FEECUR_RES);
		amount = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_AMOUNT_RES);
		commissionCharge = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_COMMISSIONCHARGE_RES);
		channel = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_CHANNEL_RES);
		furInfo = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_FURINFO_RES);
		status = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_STATUS_RES);

		postage = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_POSTAGE_RES);
		failReson = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_RETURNCODE_RES);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		mTransactonTv = (TextView) findViewById(R.id.tv_transaction_manage_records_detail);
		mAccOutv = (TextView) findViewById(R.id.tv_accout_manage_records_detail);
		mPayerIbknumTv = (TextView) findViewById(R.id.tv_accoutarea_manage_records_detail);
		mAccInTv = (TextView) findViewById(R.id.tv_accin_manage_records_detail);
		mPayeeNameTv = (TextView) findViewById(R.id.tv_payeename_manage_records_detail);
		mCurreyTv = (TextView) findViewById(R.id.tv_currency_manage_records_detail);
		mAmountTv = (TextView) findViewById(R.id.tv_amount_manage_records_detail);
		mChargeTv = (TextView) findViewById(R.id.tv_charge_manage_records_detail);
		postageTv = (TextView) findViewById(R.id.tv_ele_charge);
		mChannelTv = (TextView) findViewById(R.id.tv_channel_manage_records_detail);
		mfuYanTv = (TextView) findViewById(R.id.tv_fuyan_manage_records_detail);
		mStateTv = (TextView) findViewById(R.id.tv_state_manage_records_detail);
		confirmBtn = (Button) findViewById(R.id.btn_confirm_manage_records_detail);
		confirmBtn.setOnClickListener(listener);
	}

	private void displayTextView() {
		mTransactonTv.setText(StringUtil.isNullChange(transactionId));
		mAccOutv.setText(StringUtil.getForSixForString(payerAccountNumber));
		mPayerIbknumTv.setText(LocalData.Province.get(payerIbknum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				mPayerIbknumTv);
		mAccInTv.setText(StringUtil.getForSixForString(payeeAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				mTransactonTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mAccOutv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mAccInTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				mPayerIbknumTv);
		mPayeeNameTv.setText(payeeAccountName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				mPayeeNameTv);
		mCurreyTv.setText(LocalData.Currency.get(feeCur));
		mAmountTv.setText(StringUtil.parseStringCodePattern(feeCur, amount, 2));
		mChargeTv.setText(StringUtil.parseStringCodePattern(feeCur,
				commissionCharge, 2));
		mChannelTv.setText(LocalData.userChannel.get(channel));
		mfuYanTv.setText(StringUtil.isNullChange(furInfo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mfuYanTv);
		mStateTv.setText(LocalData.tranStatusMap.get(status));
		postageTv
				.setText(StringUtil.parseStringCodePattern(feeCur, postage, 2));
		/** 失败原因 */
		TextView tv_failReason = (TextView) findViewById(R.id.tv_failReason);
		tv_failReason.setText(failReson);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_failReason);
		LinearLayout ll_fail = (LinearLayout) findViewById(R.id.ll_fail);
		if (!StringUtil.isNull(status)) {
			if (status.equals(showFailReson1) || status.equals(showFailReson2)
					|| status.equals(showFailReson3)) {
				// 交易失败显示失败原因
				ll_fail.setVisibility(View.VISIBLE);
			} else {
				// 不显示失败原因
				ll_fail.setVisibility(View.GONE);
			}
		} else {
			// 不显示失败原因
			ll_fail.setVisibility(View.GONE);
		}
		LinearLayout ll_tuihui = (LinearLayout) findViewById(R.id.ll_tuihui);
		
		if (!StringUtil.isNull(reexchangestate)) {
			if ("1".equals(reexchangestate) ) {
				// 显示退汇查询
				ll_tuihui.setVisibility(View.VISIBLE);
			} else {
				// 不显示退汇查询
				ll_tuihui.setVisibility(View.GONE);
			}
		} else {
			// 不显示退汇查询
			ll_tuihui.setVisibility(View.GONE);
		}
	}
	
	/** 退汇状态查询 */
	public void tran_tuihuiOnclick(View v) {
		
		
		requestPsnRemitReturnInfo();
	
		
	}

	private void requestPsnRemitReturnInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANPSNREMITRETURNINFO);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.MANAGE_RECORDSDETAIL_transId_REQ, transId);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnRemitReturnInfoCallBack");
		
	}
	
	
	
	/** 退汇状态查询 返回*/
	public void requestPsnRemitReturnInfoCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setRemitReturnInfo(result);
		Intent intent =new Intent(TransRecordDetailActivity1.this, TransRecordTuihuiDetailActivity.class);
		intent.putExtra(Tran.MANAGE_RECORDSDETAIL_FEECUR_RES, feeCur);
		startActivity(intent);
	}
}
