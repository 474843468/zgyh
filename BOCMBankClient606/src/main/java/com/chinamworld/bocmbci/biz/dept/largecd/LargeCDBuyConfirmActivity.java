package com.chinamworld.bocmbci.biz.dept.largecd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
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
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 购买大额存单确认
 * 
 * @author liuh
 */
public class LargeCDBuyConfirmActivity extends DeptBaseActivity {

	private LinearLayout tabcontent;
	private View view;
	/** 产品编码TextView */
	private TextView productCodeTv;
	/** 起购金额TextView */
	private TextView beginMoneyTv;
	/** 存期TextView */
	private TextView saveDateTv;
	/** 签约账户TextView */
	private TextView signedAccNumTv;
	/** 存款金额TextView */
	private TextView saveMoneyTv;
	/** 附言TextView */
	private TextView messageTv;
	/** 确认Button */
	private Button confirmBtn;

	/** 签约账户 */
	private String signedAccNum;
	/** 附言 */
	private String memo;
	/** 申购金额 */
	private String amount;
	/** 起购金额 */
	private String beginMoney;
	private String conversationId;
	private Map<String, Object> result;
	private Map<String, Object> tranResult;

	/** 资金账户TextView */
	private TextView accNumber;
	/** 产品剩余金额TextView */
	private TextView surplusAmount;
	/** 利率TextView */
	private TextView rateTv;
	/** 账户类型*/
	private TextView accType ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();

		if (!getIntentData()) {
			setViews();
			setListeners();
		} else {
			finish();
		}
	}

	private void setListeners() {
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				BaseHttpEngine.dissmissCloseOfProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		requestPSNGetTokenId(conversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		requestForLargeCDBuySubmit();
	}

	/**
	 * PsnLargeCDBuySubmit购买大额存单 req
	 */
	public void requestForLargeCDBuySubmit() {
		String tokenId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.PSN_LARGE_CD_BUY_SUBMIT);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.TOKEN_REQ, tokenId);
		params.put(Dept.ACCOUNT_ID, String.valueOf(DeptDataCenter.getInstance().getSignedAcc().get(Dept.ACCOUNT_ID)));
		params.put(Dept.PRODUCT_CODE, (String) result.get(Dept.PRODUCT_CODE));
		params.put(Dept.VALUE_PRD_NUM, (String) result.get(Dept.VALUE_PRD_NUM));
		params.put(Dept.AMOUNT, amount);
		params.put(Dept.MEMO, memo);
		params.put(Dept.CASHREMIT, ConstantGloble.CASHREMIT_00);
		params.put(Dept.CD_TERM, (String) result.get(Dept.CD_TERM));
		params.put(Dept.PERIOD_TYPE, (String) result.get(Dept.PERIOD_TYPE));
		params.put(Dept.CURRENCY, ConstantGloble.CURRENCY);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "getLargeCDBuySubmitCallBack");
	}

	/**
	 * PsnLargeCDBuySubmit购买大额存单 res
	 */
	@SuppressWarnings("unchecked")
	public void getLargeCDBuySubmitCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tranResult = (Map<String, Object>) biiResponseBody.getResult();
		Intent intent = new Intent();
		intent.setClass(LargeCDBuyConfirmActivity.this, LargeCDBuyResultActivity.class);
		intent.putExtra(Dept.BATSEQ, (String) tranResult.get(Dept.BATSEQ));
		intent.putExtra(Dept.TRANSACTION_ID, (String) tranResult.get(Dept.TRANSACTION_ID));
		intent.putExtra(Dept.STATUS, (String) tranResult.get(Dept.STATUS));
		intent.putExtra(Dept.AMOUNT, amount);
		intent.putExtra(Dept.MEMO, memo);
		startActivity(intent);
	}

	private void setViews() {
		productCodeTv = (TextView) view.findViewById(R.id.tv_large_cd_product_code);
		saveDateTv = (TextView) view.findViewById(R.id.tv_large_cd_save_date);
		beginMoneyTv = (TextView) view.findViewById(R.id.tv_large_cd_begin_money_two);
		signedAccNumTv = (TextView) view.findViewById(R.id.tv_large_cd_acc_number);
		saveMoneyTv = (TextView) view.findViewById(R.id.tv_large_cd_save_money);
		messageTv = (TextView) view.findViewById(R.id.tv_large_cd_message);
		confirmBtn = (Button) view.findViewById(R.id.btn_confirm);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productCodeTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, beginMoneyTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, signedAccNumTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveMoneyTv);
		// PopupWindowUtils.getInstance().setOnShowAllTextListener(this,messageTv);

		// /////////////////////////////////////////////////////////////////////////
		// 资金账户
//		String signedAccStr = String.valueOf(DeptDataCenter.getInstance().getSignedAcc().get(Dept.ACCOUNT_NUMBER));
//		accNumber = (TextView) view.findViewById(R.id.tv_large_cd_capital_acc);
//		accNumber.setText(StringUtil.getForSixForString(signedAccStr));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, saveMoneyTv);

		// add by 2016年3月2日 添加资金账户
		Map<String, Object> signedAcc = DeptDataCenter.getInstance().getSignedAcc();
		accNumber = (TextView) view.findViewById(R.id.tv_large_cd_capital_acc);
		String number = (String) signedAcc.get(Dept.ACCOUNT_NUMBER);
		accNumber.setText(StringUtil.getForSixForString(number));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accNumber);
		
		// add by luqp 2016年5月3日   账户后面添加账户类型    如:1005****8561 活期一本通 
		accType = (TextView) view.findViewById(R.id.tv_acc_type);
		String accTypeStr = (String) signedAcc.get(Dept.LargeSign_accountType);
		String accountType =LocalData.AccountType.get(accTypeStr);
		accType.setText(accountType);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accType);

		// add by 2016年3月2日 产品剩余金额
		surplusAmount = (TextView) view.findViewById(R.id.tv_large_cd_surplus_amount);
		String availableLimit = StringUtil.valueOf1((String) result.get(Dept.AVAIL_QUOTA));
		// add by 2016年3月16日 剩余金额减去输入金额 显示产品剩余金额
//		String surplusAmounts = getSurplusAmount(availableLimit, amount);
		surplusAmount.setText(StringUtil.parseStringPattern(availableLimit, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, surplusAmount);

		// add by 2016年3月2日 利率
		rateTv = (TextView) view.findViewById(R.id.tv_large_cd_rate);
		String rate = StringUtil.valueOf1((String) result.get(Dept.RATE));
		rateTv.setText(rate+"%"); // add by luqp 2016年3月4日 追加%号
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, rateTv);
		// /////////////////////////////////////////////////////////////////////////

		signedAccNum = StringUtil.valueOf1((String) DeptDataCenter.getInstance().getSignedAcc().get(Dept.ACCOUNT_NUMBER));
		beginMoney = (String) result.get(Dept.BEGIN_MONEY);
		// 基期
		String periodType = StringUtil.valueOf1((String) result.get(Dept.PERIOD_TYPE));

		signedAccNumTv.setText(StringUtil.getForSixForString(signedAccNum));
		productCodeTv.setText(StringUtil.valueOf1((String) result.get(Dept.PRODUCT_CODE)));
		beginMoneyTv.setText(StringUtil.parseStringPattern(beginMoney, 2));

		if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_MONTH)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.month));
		} else if (periodType.equalsIgnoreCase(ConstantGloble.PERIOD_TYPE_DAY)) {
			saveDateTv.setText(String.valueOf(result.get(Dept.CD_TERM)) + getString(R.string.day));
		} else {
			saveDateTv.setText("-");
		}
		saveMoneyTv.setText(StringUtil.parseStringPattern(amount, 2));
		if (!StringUtil.isNullOrEmpty(memo)) {
			messageTv.setText(memo);
		}

	}

	private boolean getIntentData() {
		DeptDataCenter data = DeptDataCenter.getInstance();
		Intent intent = getIntent();
		amount = StringUtil.parseStringPattern(intent.getStringExtra(Dept.AMOUNT), 2);
		memo = intent.getStringExtra(Dept.MEMO);
		result = (Map<String, Object>) data.getAvailableDetial();
		return StringUtil.isNullOrEmpty(result);
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		setTitle(getString(R.string.large_cd_add_title));

		LayoutInflater inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = inflater.inflate(R.layout.large_cd_buy_confirm, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}

	}
}
