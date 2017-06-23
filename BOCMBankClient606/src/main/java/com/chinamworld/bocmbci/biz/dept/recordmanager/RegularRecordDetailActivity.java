package com.chinamworld.bocmbci.biz.dept.recordmanager;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: RegularRecordDetailActivity
 * @Description: 定期存款查询详情
 * @author JiangWei
 * @date 2013-7-1 上午11:57:24
 */
public class RegularRecordDetailActivity extends DeptBaseActivity {

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
	/** 电汇费*/
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
	/**电汇费*/
	private String postage;
	private LinearLayout tabcontent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.regular_record_query));
		View view = mInflater.inflate(
				R.layout.tran_manage_records_detail_activity, null);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		// modify by wjp 步骤栏没有显示 但是这个代码不要删除 会影响布局
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);

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
				.get(Tran.MANAGE_PRE_TRANSACTIONID_RES);
		
		payerAccountNumber = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYERACCOUNTNUMBER_RES);
		payerIbknum = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYERIBKNUM_RES);
		payeeAccountNumber = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYEEACCOUNTNUMBER_RES);
		payeeAccountName = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_PAYERACCOUNTNAME_RES);
		feeCur = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_FEECUR_RES);
		amount = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_AMOUNT_RES);
		commissionCharge = (String) detailMap
				.get(Tran.MANAGE_RECORDSDETAIL_COMMISSIONCHARGE_RES);
		channel = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_CHANNEL_RES);
		furInfo = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_FURINFO_RES);
		status = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_STATUS_RES);
		
		postage = (String) detailMap.get(Tran.MANAGE_RECORDSDETAIL_POSTAGE_RES);
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
		//小标题改为 定期存款详情 
		TextView tv_title = (TextView)findViewById(R.id.second_title_cotent);
		tv_title.setText(R.string.regular_record_detail);
		
		mTransactonTv.setText(transactionId);
		mAccOutv.setText(StringUtil.getForSixForString(payerAccountNumber));
		mPayerIbknumTv.setText(LocalData.Province.get(payerIbknum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mPayerIbknumTv);
		mAccInTv.setText(StringUtil.getForSixForString(payeeAccountNumber));
		mPayeeNameTv.setText(payeeAccountName);
		mCurreyTv.setText(LocalData.Currency.get(feeCur));
		/////////////////////
		String strMoney = StringUtil.parseStringCodePattern(feeCur, amount, 2);
		mAmountTv.setText(strMoney);
		////////////////////////////////////////////////////////////
		// add by 2015年12月8日18:10:36 格式化手续费电费 如果手续费于电费为空或为0显示 "-"
		String Charge = null;
		String postages = null;
		// 如果收费电费不为空正常显示,为空显示"-"
		if (!StringUtil.isNull(commissionCharge) && !StringUtil.isNull(postage)) {
			Charge = StringUtil.parseStringCodePattern(feeCur, commissionCharge, 2);
			postages = StringUtil.parseStringCodePattern(feeCur, postage, 2);
		}else{
			Charge = "-";
			postages = "-";
		}
		mChargeTv.setText(Charge);
		postageTv.setText(postages);
		///////////////////////////////////////////////////////////
		mChannelTv.setText(LocalData.userChannel.get(channel));
		mfuYanTv.setText(StringUtil.isNullOrEmpty(furInfo)?"-":furInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, mfuYanTv);
		mStateTv.setText(LocalData.tranStatusMap.get(status));
		// 原手续费电费
//		mChargeTv.setText(StringUtil.isNullOrEmpty(commissionCharge)?"-":commissionCharge);
//		postageTv.setText(StringUtil.isNullOrEmpty(postage)?"-":postage);
	}

}
