package com.chinamworld.bocmbci.biz.dept.appointmanager;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 撤销预约 确认
 * 
 * @author
 * 
 */
public class CancleDateConfirmActivity1 extends DeptBaseActivity
		implements OnClickListener {

	/** 标题 */
	private TextView titleTv;
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
	/** 头部右边按钮 */
	private Button mTopRightBtn = null;

	/** 底部layout */
	private LinearLayout footLayout;
	/** 左侧菜单 */
	private Button showBtn;

	private int queryType;
	/**执行次数layout*/
	private LinearLayout executeTimesLayout;
	/**执行日期layout*/
	private LinearLayout executeDateLayout;
	
	private LinearLayout tabcontent;
	private boolean isFinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.manage_cancle_predate));
		View view = mInflater.inflate(
				R.layout.tran_manage_exedate_detail_cancle_activity, null);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		ibBack.setVisibility(View.GONE);
		mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		mTopRightBtn.setVisibility(View.VISIBLE);
		mTopRightBtn.setText(this.getString(R.string.forex_rate_close));
		mTopRightBtn.setOnClickListener(this);

		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		setLeftButtonPopupGone();

		// modify by wjp 步骤栏没有显示 但是这个代码不要删除 会影响布局
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.tran_my_trans));
		StepTitleUtils.getInstance().setTitleStep(2);

		aquireData();
		setupView();
		displayTextView();
	}

	private void aquireData() {

		queryType = getIntent().getIntExtra(
				ConstantGloble.TRAN_MANAGER_QUERY_TYPE, -1);

		queryDetailQueryMap = TranDataCenter.getInstance()
				.getQueryDetailCallBackMap();
		transactionId = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_TRANSACTIONID_RES);
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

		paymentDate = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_PAYMENTDATE_RES);
		firstSubmitDate = (String) queryDetailQueryMap
				.get(Tran.MANAGE_PREDETAIL_FIRSTSUBMITDATE_RES);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		titleTv = (TextView) findViewById(R.id.tv_content);

		transactionLayout = (LinearLayout) findViewById(R.id.tran_transaction_layout);
		seqbatLayout = (LinearLayout) findViewById(R.id.tran_seqbat_layout);
		transactionTv = (TextView) findViewById(R.id.tran_transaction_tv);
		seqBatTv = (TextView) findViewById(R.id.tv_transaction_manage_exedate_detail);

		executeTimesLayout = (LinearLayout) findViewById(R.id.tran_execute_layout);
		executeDateLayout = (LinearLayout)findViewById(R.id.tran_pre_date_layout);
		
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
		mCancleBtn.setText(getString(R.string.confirm));
		mCancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		});

	}

	private void displayTextView() {
		if (transMode.equals(ConstantGloble.PREPERIODEXE)) {// 如果是预约周期
			transactionLayout.setVisibility(View.GONE);
			seqBatTv.setText(seqBat);
			executeDateLayout.setVisibility(View.GONE);
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
		needExeTv.setText(allAmount);
		executedTv.setText(executeAmount);
		notExeTv.setText(notExecuteAmount);

		preDateTv.setText(firstSubmitDate);
		exeDateTv.setText(paymentDate);
		accOutTv.setText(StringUtil.getForSixForString(payerAccountNumber));
		accOutAreaTv.setText(LocalData.Province.get(payerIbknum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accOutAreaTv);
		accInTv.setText(StringUtil.getForSixForString(payeeAccountNumber));
		currencyTv.setText(LocalData.Currency.get(currency));
		transAmountTv.setText(StringUtil.parseStringPattern(amount, 2));
		channelTv.setText(LocalData.userChannel.get(channel));
		furInfo=StringUtil.isNull(furInfo)?ConstantGloble.BOCINVT_DATE_ADD:furInfo;
		remarkTv.setText(furInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remarkTv);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_top_right_btn:
			finish();
			break;
		}
	}

	/**
	 * 请求conversation 回调
	 * 
	 * @param resultObj
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestForTransPreRecordDelete(tokenId);
	}

	/**
	 * PsnTransPreRecordDelete预约交易删除 req
	 */
	public void requestForTransPreRecordDelete(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSPRERECORDDELETE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		// 转账批次号
		Map<String, String> map = new HashMap<String, String>();
		// if (queryType == PRE_DATE) {// 预约日期
		// 查询
		map.put(Tran.MANAGE_CANCLEPREDATE_DATETYPE_REQ, queryType + "");
		map.put(Tran.MANAGE_CANCLEPREDATE_BATSEQ_REQ, seqBat);

		// } else if (queryType == EXE_DATE) {// 执行日期
		// 查询
		// map.put(Tran.MANAGE_CANCLEPREDATE_DATETYPE_REQ,
		// ConstantGloble.EXEDATE_QUERY);
		map.put(Tran.MANAGE_CANCLEPREDATE_transactionId_REQ, transactionId);

		// }
		map.put(Tran.MANAGE_CANCLEPREDATE_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"transPreRecordDeleteCallBack");
	}

	/**
	 * 预约交易删除res
	 */
	public void transPreRecordDeleteCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		biiResponse.getResult();
		
		isFinish = true;
		mTopRightBtn.setVisibility(View.INVISIBLE);
		titleTv.setText(getString(R.string.manage_canclesuccess_detail));
		mCancleBtn.setText(getString(R.string.finish));
		mCancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 重新刷新列表
				TranDataCenter.getInstance().setQueryType(queryType);
				setResult(103);
				finish();
			}
		});

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(isFinish){
			if(keyCode == KeyEvent.KEYCODE_BACK){
				TranDataCenter.getInstance().setQueryType(queryType);
				setResult(103);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
