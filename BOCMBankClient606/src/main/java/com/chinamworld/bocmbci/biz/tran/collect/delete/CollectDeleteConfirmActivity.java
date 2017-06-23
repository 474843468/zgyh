package com.chinamworld.bocmbci.biz.tran.collect.delete;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleCodeType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectDeleteConfirmActivity
 * @Description: 资金归集删除确认
 * @author luql
 * @date 2014-4-3 上午09:24:48
 */
public class CollectDeleteConfirmActivity extends CollectBaseActivity {

	private static final int request_delete_code = 10000;
	private View mViewContent;
	private View mNextView;
	// --------------------------------------
	// 上传参数
	private String mRuleNoParam;
	/** 客户手机号 */
	private String mMobileParam;
	/** 金额参数 */
	private String mAmountParam;
	/** 归集方式参数 */
	private String mImputationModeParam;
	/** 归集周期参数 */
	private String mCycleParam;
	/** 归集周期执行日参数 */
	private String mCycleCodeParam;
	/** 归集账户卡号 */
	private String mAccCardParam;
	/** 归集账户号 */
	private String mAccNumParam;

	// /** 被归集账户人行行号 */
	// private String mPayerAcctBankNo;
	/** 被归集账户开户行名称 */
	private String mPayerAccBankNameParam;
	/** 被归集账户账号/卡号 */
	private String mPayerAccountParam;
	/** 被归集账户户名 */
	private String mPayerAcntNameParam;
	/** 是否发送短信通知 */
	private String mIfMessageParam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_delete_activity);
		setTitle(getString(R.string.collect_delete_title));
		if (getIntentData()) {
			toprightBtn();
			findView();
			setListener();
		} else {
			finish();
		}
	}

	private void setListener() {
		mNextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BiiHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		requestPSNGetTokenId(conversationId);
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		deleteCollectAccount();
	}

	/** 删除归集规则 */
	private void deleteCollectAccount() {
		HashMap<String, Object> bizDataMap = BaseDroidApp.getInstanse().getBizDataMap();
		String token = (String) bizDataMap.get(ConstantGloble.TOKEN_ID);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Collect.PsnCBCollectDel);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Collect.ruleNo, mRuleNoParam);
		map.put(Collect.cbAccCard, mAccCardParam);
		map.put(Collect.cbAccNum, mAccNumParam);
		map.put(Collect.payerAccBankName, mPayerAccBankNameParam);
		map.put(Collect.payerAccount, mPayerAccountParam);
		map.put(Collect.payerAcntName, mPayerAcntNameParam);
		map.put(Collect.currency, "001"); // "人民币元"
		map.put(Collect.imputationMode, mImputationModeParam);
		map.put(Collect.cycle, mCycleParam);

		if (!CollectCycleType.DAY.equalsIgnoreCase(mCycleParam)) {
			map.put(Collect.cycleCode, mCycleCodeParam);
		}

		if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			map.put(Collect.retainAmt, new BigDecimal(mAmountParam));
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			map.put(Collect.transferAmount, new BigDecimal(mAmountParam));
		}
		map.put(Collect.ifMessage, mIfMessageParam);
		if ("Y".equalsIgnoreCase(mIfMessageParam)) {
			map.put(Collect.cbMobile, mMobileParam);
		}
		map.put(ConstantGloble.PUBLIC_TOKEN, token);
		biiRequestBody.setParams(map);
		biiRequestBody.setConversationId((String) bizDataMap.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCBCollectOnTimeCallback");
	}

	public void requestPsnCBCollectOnTimeCallback(Object resultObj) {
		// 没有异常提示解约成功
		BiiHttpEngine.dissMissProgressDialog();
		openDeleteResultActivity();
	}

	private void openDeleteResultActivity() {
		Intent intent = new Intent(this, CollectDeleteResultActivity.class);
		intent.putExtra(Collect.ruleNo, mRuleNoParam);
		intent.putExtra(Collect.cbAccCard, mAccCardParam);
		intent.putExtra(Collect.cbAccNum, mAccNumParam);
		// intent.putExtra(Collect.cbAccName, mAccountNameParam);
		// intent.putExtra(Collect.cbAccType, mAccountTypeParam);
		// intent.putExtra(Collect.cbAccBankName, mBankNameParam);
		// intent.putExtra(Collect.payerAcctBankNo, mPayerAcctBankNo);
		intent.putExtra(Collect.payerAccBankName, mPayerAccBankNameParam);
		intent.putExtra(Collect.payerAccount, mPayerAccountParam);
		intent.putExtra(Collect.payerAccountName, mPayerAcntNameParam);
		intent.putExtra(Collect.imputationMode, mImputationModeParam);
		intent.putExtra(Collect.cycle, mCycleParam);
		if (mCycleCodeParam != null) {
			intent.putExtra(Collect.cycleCode, mCycleCodeParam);
		}
		intent.putExtra(Collect.amount, mAmountParam);
		intent.putExtra(Collect.ifMessage, mIfMessageParam);
		intent.putExtra(Collect.cbMobile, mMobileParam);
		startActivityForResult(intent, request_delete_code);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == request_delete_code && resultCode == Activity.RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	private void findView() {
		/** 归集编号 */
		TextView rulenNoView = (TextView) mViewContent.findViewById(R.id.tv_rulenno);
		/** 归集账户 */
		TextView payeeAccountView = (TextView) mViewContent.findViewById(R.id.tv_payee_account);
		/** 被归集账户 */
		TextView payerAccountView = (TextView) mViewContent.findViewById(R.id.tv_payer_account);
		/** 被归集账户户名 */
		TextView payerNameView = (TextView) mViewContent.findViewById(R.id.tv_payer_name);
		/** 被归集账户开户行 */
		TextView payerBankView = (TextView) mViewContent.findViewById(R.id.tv_payer_bank);
		/** 归集方式 */
		TextView collectModeView = (TextView) mViewContent.findViewById(R.id.tv_collect_mode);
		/** 留存金额 */
		TextView retainAmountView = (TextView) mViewContent.findViewById(R.id.tv_amount);
		TextView amountLableView = (TextView) mViewContent.findViewById(R.id.tv_amount_table);
		/** 归集规则 */
		TextView collectRuleView = (TextView) mViewContent.findViewById(R.id.tv_rule);
		/** 归集规则执行日 */
		TextView collectRuleCodeView = (TextView) mViewContent.findViewById(R.id.tv_rule_code);
		/** 归集手机号 */
		TextView payeeMobileView = (TextView) findViewById(R.id.tv_payee_mobile);

		/** */
		mNextView = findViewById(R.id.btnconfirm);

		rulenNoView.setText(mRuleNoParam);
		payeeAccountView.setText(StringUtil.getForSixForString(mAccCardParam));
		payerAccountView.setText(StringUtil.getForSixForString(mPayerAccountParam));
		payerNameView.setText(mPayerAcntNameParam);
		payerBankView.setText(mPayerAccBankNameParam);
		collectModeView.setText(CollectImputationMode.getTypeStr(mImputationModeParam));

		if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
			mViewContent.findViewById(R.id.amount_layout).setVisibility(View.GONE);
		} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			mViewContent.findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			retainAmountView.setText(StringUtil.parseStringPattern(mAmountParam, 2));
			amountLableView.setText(R.string.collect_retain_amount);
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			mViewContent.findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			retainAmountView.setText(StringUtil.parseStringPattern(mAmountParam, 2));
			amountLableView.setText(R.string.collect_transferAmount_amount);
		}

		collectRuleView.setText(CollectCycleType.getCycleTypeStr(mCycleParam));
		// 执行日
		if (CollectCycleType.DAY.equals(mCycleParam)) {// 日
			mViewContent.findViewById(R.id.rule_code_layout).setVisibility(View.GONE);
			collectRuleCodeView.setText("-");
		} else if (CollectCycleType.WEEK.equals(mCycleParam)) {// 周
			collectRuleCodeView.setText(CollectCycleCodeType.getWeekTypeStr(mCycleCodeParam));
			mViewContent.findViewById(R.id.rule_code_layout).setVisibility(View.VISIBLE);
		} else if (CollectCycleType.MONTH.equals(mCycleParam)) { // 月
			collectRuleCodeView.setText(CollectCycleCodeType.getMonthTypeStr(mCycleCodeParam));
			mViewContent.findViewById(R.id.rule_code_layout).setVisibility(View.VISIBLE);
		}

		if ("Y".equalsIgnoreCase(mIfMessageParam)) {
			payeeMobileView.setText(mMobileParam);
			findViewById(R.id.mobile_layout).setVisibility(View.VISIBLE);
			mViewContent.findViewById(R.id.collect_mobile).setVisibility(View.VISIBLE);
			payeeMobileView.setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.mobile_layout).setVisibility(View.GONE);
			mViewContent.findViewById(R.id.collect_mobile).setVisibility(View.GONE);
			payeeMobileView.setVisibility(View.GONE);
		}
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerNameView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerBankView); 
	}

	private boolean getIntentData() {
		Intent intent = getIntent();
		mRuleNoParam = intent.getStringExtra(Collect.ruleNo);
		mAccCardParam = intent.getStringExtra(Collect.cbAccCard);
		mAccNumParam = intent.getStringExtra(Collect.cbAccNum);

		mPayerAccBankNameParam = intent.getStringExtra(Collect.payerAccBankName);
		mPayerAccountParam = intent.getStringExtra(Collect.payerAccount);
		mPayerAcntNameParam = intent.getStringExtra(Collect.payerAcntName);

		mImputationModeParam = intent.getStringExtra(Collect.imputationMode);
		mCycleParam = intent.getStringExtra(Collect.cycle);
		mCycleCodeParam = intent.getStringExtra(Collect.cycleCode);

		if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
			// 全额归集
			mAmountParam = null;
		} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			// 留存余额归集
			mAmountParam = intent.getStringExtra(Collect.amount);
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			// 定额归集
			mAmountParam = intent.getStringExtra(Collect.amount);
		}
		mIfMessageParam = intent.getStringExtra(Collect.ifMessage);
		mMobileParam = intent.getStringExtra(Collect.cbMobile);
		return true;
	}

	// private void requestPsnCBMobileQuery() {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Collect.PsnCBMobileQuery);
	// // biiRequestBody.setConversationId((String)
	// // BaseDroidApp.getInstanse().getBizDataMap()
	// // .get(ConstantGloble.CONVERSATION_ID));
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestPsnCBMobileQueryCallback");
	// }
	//
	// public void requestPsnCBMobileQueryCallback(Object resultObj) {
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// Map<String, String> resultData = (Map<String, String>)
	// biiResponseBody.getResult();
	// mMobileParam = resultData.get(Collect.cbMobile);
	// // 解约
	// deleteCollectAccount();
	// }
}
