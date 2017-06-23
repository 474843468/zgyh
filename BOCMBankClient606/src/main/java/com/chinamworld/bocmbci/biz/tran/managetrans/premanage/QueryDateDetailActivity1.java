package com.chinamworld.bocmbci.biz.tran.managetrans.premanage;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.ManageTransBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 执行日期查询 详情
 * 
 * @author
 * 
 */
public class QueryDateDetailActivity1 extends ManageTransBaseActivity {
	/** 撤销 */
	private Button mCancleBtn = null;

	/** 预约交易详情查询 返回集合 */
	private Map<String, Object> queryDetailQueryMap = null;
	/** 转账批次号 */
	private TextView seqBatTv;
	/** 交易序号 */
	private TextView transactionTv = null;
	/** 预约类型 */
	private TextView preTypeTv = null;
	/** 预约日期 */
	private TextView preDateTv = null;
	/** 执行日期 */
	private TextView exeDateTv = null;
	/** 转出账户 */
	private TextView accOutTv = null;
	/** 转出账户地区 */
	private TextView accOutAreaTv = null;
	/** 转入账户 */
	private TextView accInTv = null;
	/** 币种 */
	private TextView currencyTv = null;
	/** 转账金额 */
	private TextView transAmountTv = null;
	/** 渠道类型 */
	private TextView channelTv = null;
	/** 附言 */
	private TextView remarkTv = null;

	/** 需要执行次数 */
	private TextView needExeTv;
	/** 已执行次数 */
	private TextView executedTv;
	/** 未执行次数 */
	private TextView notExeTv;

	private LinearLayout transactionLayout;
	private LinearLayout seqbatLayout;

	/** 网银交易序号 */
	private String transactionId;
	/** 周期预约交易转账批次之序号 */
	// private String periodicalSeq;
	/** 转账批次号 */
	private String seqBat;
	/** 预约类型 */
	private String transMode;
	/** 预约周期总笔数 */
	private String allAmount;
	/** 周期预约已执行笔数 */
	private String executeAmount;
	/** 周期预约未执行笔数 */
	private String notExecuteAmount;
	/** 转出账户账号 */
	private String payerAccountNumber;
	/** 转出账户省行联行号 */
	// 用来显示转出账户地区
	private String payerIbknum;
	/** 转入账户账号 */
	private String payeeAccountNumber;
	/** 币种 */
	private String currency;
	/** 转账金额 */
	private String amount;
	/** 发起渠道 */
	private String channel;
	/** 附言 */
	private String furInfo;
	/** 执行日期 */
	private String paymentDate;
	/** 预约日期 */
	private String firstSubmitDate;

	/** 执行次数layout */
	private LinearLayout executeTimesLayout;
	/** 执行日期layout */
	// private LinearLayout executeDateLayout;
	/** 查询类型 预约日期 还是 执行日期 */
	private int QueryType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_pre));
		View view = mInflater.inflate(
				R.layout.tran_manage_exedate_detail_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);

		aquireData();

		setupView(); // 初始化布局控件

		displayTextView();
	}

	private void aquireData() {
		dateTime = this.getIntent().getStringExtra(ConstantGloble.DATE_TIEM);
		QueryType = this.getIntent().getIntExtra(
				ConstantGloble.TRAN_MANAGER_QUERY_TYPE, -1);

		queryDetailQueryMap = TranDataCenter.getInstance()
				.getQueryDetailCallBackMap();
		transactionId = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_TRANSACTIONID_RES);
		// periodicalSeq = (String) queryDetailQueryMap
		// .get(Tran.MANAGE_PREDETAIL_PERIODICALSEQ_RES);
		seqBat = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_BATSEQ_RES);
		transMode = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_TRANSMODE_RES);
		allAmount = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_ALLAMOUNT_RES);
		executeAmount = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_EXECUTEAMOUNT_RES);
		notExecuteAmount = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_NOTEXECUTEAMOUNT_RES);
		payerAccountNumber = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_PAYERACCOUNTNUMBER_RES);
		payerIbknum = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_PAYERIBKNUM_RES);
		payeeAccountNumber = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_PAYEEACCOUNTNUMBER_RES);
		currency = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_CURRENCY_RES);
		amount = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_AMOUNT_RES);
		channel = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_CHANNEL_RES);
		furInfo = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_FURINFO_RES);

		// 如果是 预约日期的 预约周期执行 paymentDate是从上个页面列表里的字段带过来的
		if (QueryType == PRE_DATE
				&& transMode.equals(ConstantGloble.PREPERIODEXE)) {// 如果是预约周期
			paymentDate = this.getIntent().getStringExtra(
					ConstantGloble.PAYMENTDATE);

		} else {
			paymentDate = (String) queryDetailQueryMap
					.get(Tran.MANAGE_PREDETAIL_PAYMENTDATE_RES);
		}
		firstSubmitDate = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_FIRSTSUBMITDATE_RES);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = getIntent();
			intent.setClass(QueryDateDetailActivity1.this,
					CancleDateConfirmActivity1.class);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		transactionLayout = (LinearLayout) findViewById(R.id.tran_transaction_layout);
		seqbatLayout = (LinearLayout) findViewById(R.id.tran_seqbat_layout);
		transactionTv = (TextView) findViewById(R.id.tran_transaction_tv);
		seqBatTv = (TextView) findViewById(R.id.tv_transaction_manage_exedate_detail);

		executeTimesLayout = (LinearLayout) findViewById(R.id.tran_execute_layout);
		// executeDateLayout =
		// (LinearLayout)findViewById(R.id.tran_pre_date_layout);

		needExeTv = (TextView) findViewById(R.id.tran_need_exetimes_tv);
		executedTv = (TextView) findViewById(R.id.tran_executedtimes_tv);
		notExeTv = (TextView) findViewById(R.id.tran_notexe_times_tv);

		preTypeTv = (TextView) findViewById(R.id.tv_pretype_manage_exedate_detail);
		preDateTv = (TextView) findViewById(R.id.tv_predate_manage_exedate_detail);
		exeDateTv = (TextView) findViewById(R.id.tv_exedate_manage_exedate_detail);

		accOutTv = (TextView) findViewById(R.id.tv_accout_manage_exedate_detail);
		accOutAreaTv = (TextView) findViewById(R.id.tv_accoutarea_manage_exedate_detail);
		accInTv = (TextView) findViewById(R.id.tv_accin_manage_exedate_detail);
		currencyTv = (TextView) findViewById(R.id.tv_currency_manage_exedate_detail);
		transAmountTv = (TextView) findViewById(R.id.tv_trans_amount_manage_exedate_detail);
		channelTv = (TextView) findViewById(R.id.tv_channel_manage_exedate_detail);
		remarkTv = (TextView) findViewById(R.id.tv_remark_manage_exedate_detail);

		mCancleBtn = (Button) findViewById(R.id.btn_cancle_manage_exedate_detail);
		mCancleBtn.setOnClickListener(listener);

	}

	private void displayTextView() {
		if (transMode.equals(ConstantGloble.PREPERIODEXE)) {// 如果是预约周期
			transactionLayout.setVisibility(View.GONE);
			seqBatTv.setText(seqBat);
			// executeDateLayout.setVisibility(View.GONE);
		} else if (transMode.equals(ConstantGloble.PREDATEEXE)) {// 如果是预约日期
			transactionLayout.setVisibility(View.GONE);
			seqBatTv.setText(seqBat);
			executeTimesLayout.setVisibility(View.GONE);
		} else {
			seqbatLayout.setVisibility(View.GONE);
			transactionTv.setText(transactionId);
			executeTimesLayout.setVisibility(View.GONE);
		}

		preTypeTv.setText(LocalData.TransModeDisplay.get(transMode));
		needExeTv.setText(StringUtil.isNullChange(allAmount));
		executedTv.setText(StringUtil.isNullChange(executeAmount));
		notExeTv.setText(StringUtil.isNullChange(notExecuteAmount));
		preDateTv.setText(StringUtil.isNullChange(firstSubmitDate));
		if (QueryType == PRE_DATE
				&& transMode.equals(ConstantGloble.PREPERIODEXE)) {// 如果是预约周期
			exeDateTv.setText("-");
		} else {
			exeDateTv.setText(StringUtil.isNullChange(paymentDate));
		}
		accOutTv.setText(StringUtil.getForSixForString(payerAccountNumber));
		accOutAreaTv.setText(LocalData.Province.get(payerIbknum));
		accInTv.setText(StringUtil.getForSixForString(payeeAccountNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				transactionTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, seqBatTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accInTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accOutAreaTv);
		currencyTv.setText(LocalData.Currency.get(currency));
		transAmountTv.setText(StringUtil.parseStringCodePattern(currency,
				amount, 2));
		channelTv.setText(LocalData.userChannel.get(channel));
		remarkTv.setText(StringUtil.isNullChange(furInfo));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);

		// 判断撤销按钮显示与否 根据当前时间和paymentDate比较，如果paymentDate大于系统当前时间 有撤销按钮 否则没有
		if (QueryDateUtils.compareDate(paymentDate, dateTime)) {
			mCancleBtn.setVisibility(View.GONE);
		} else {
			String status = (String) queryDetailQueryMap
					.get(Tran.ACCOUNTDETAIL_STATUS_RES);
			if (!StringUtil.isNull(status)) {
				if (status.equals(showFailReson2)) {
					mCancleBtn.setVisibility(View.GONE);
				}
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 103) {
			setResult(104);
			finish();
		}
	}

}
