package com.chinamworld.bocmbci.biz.tran.collect;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleCodeType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectStatusType;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.tran.collect.delete.CollectDeleteConfirmActivity;
import com.chinamworld.bocmbci.biz.tran.collect.modify.CollectModifyActivity;
import com.chinamworld.bocmbci.biz.tran.collect.query.CollectQueryActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectAccountActivity
 * @Description: 以设置归集账户
 * @author luql
 * @date 2014-3-18 上午11:52:25
 */
public class CollectAccountActivity extends CollectBaseActivity {

	private static final int request_delete_code = 10000;
	private static final int request_modify_code = 10001;
	private View mViewContent;
	/** 归集账户 */
	private String mPayeeAccount;
	/** 被归集账户 */
	private String mPayerAccount;
	/** 归集账户 */
	private Map<String, Object> mAccountData;
	/** 被归集账户数据 */
	private Map<String, Object> mCollectData;
	/** 明细按钮 */
	private Button mDetailView;
	/** 更多按钮[] */
	private Button mMoreView;
	private String mCbMoblile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_account_activity);
		setTitle(getString(R.string.collect_title));
		if (getIntentData()) {
			toprightBtn();
			findView();
			setListener();
		} else {
			finish();
		}
	}

	private void setListener() {
		mDetailView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 详情
				Intent intent = new Intent(CollectAccountActivity.this, CollectQueryActivity.class);
				intent.putExtra(Collect.cbAccCard, mPayeeAccount);
				intent.putExtra(Collect.payerAccount, mPayerAccount);
				startActivity(intent);
			}
		});
//		String status = (String) mCollectData.get(Collect.status);
//		if (CollectStatusType.isValidStatus(status)) {
			// 有效状态
			mMoreView.setText(R.string.more);
			String[] selects = { "修改", "删除" };
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(this, mMoreView, selects, new OnClickListener() {
				@Override
				public void onClick(View v) {
					Button tv = (Button) v;
					String text = tv.getText().toString();
					if ("修改".equals(text)) {
						Intent intent = new Intent(CollectAccountActivity.this, CollectModifyActivity.class);
						// intent.putExtra(Collect.cbAccCard, mPayeeAccount);
						// intent.putExtra(Collect.payerAccount, mPayerAccount);
						// intent.putExtra(Collect.cbMobile, mCbMoblile);
						intent.putExtra(Collect.ruleNo, (String) mCollectData.get(Collect.rusleNo));
						intent.putExtra(Collect.accountId, (String) mAccountData.get(Comm.ACCOUNT_ID));
						intent.putExtra(Collect.cbAccNum, (String) mAccountData.get(Collect.cbAccNum));
//						intent.putExtra(Collect.cbAccCard, (String) mAccountData.get(Collect.cbAccCard));
						intent.putExtra(Collect.cbAccCard, mPayeeAccount);

						// intent.putExtra(Collect.payerAcctBankNo, (String)
						// mCollectData.get(Collect.payerAccBankName));
						intent.putExtra(Collect.payerAccBankName, (String) mCollectData.get(Collect.payerAccBankName));
//						intent.putExtra(Collect.payerAccount, (String) mCollectData.get(Collect.payerAccount));
						intent.putExtra(Collect.payerAccount, mPayerAccount);
						intent.putExtra(Collect.payerAcntName, (String) mCollectData.get(Collect.payerAccountName));

						String mImputationModeParam = (String) mCollectData.get(Collect.imputationMode);
						intent.putExtra(Collect.imputationMode, mImputationModeParam);
						intent.putExtra(Collect.cycle, (String) mCollectData.get(Collect.cycle));
						intent.putExtra(Collect.cycleCode, (String) mCollectData.get(Collect.cycleCode));
						if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
							intent.putExtra(Collect.amount, (String) mCollectData.get(Collect.retainAmount));
						} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
							intent.putExtra(Collect.amount, (String) mCollectData.get(Collect.transferAmount));
						}
						intent.putExtra(Collect.ifMessage, (String) mCollectData.get(Collect.ifMessage));
						intent.putExtra(Collect.cbMobile, mCbMoblile);
						startActivityForResult(intent, request_modify_code);
					} else if ("删除".equals(text)) {
						// deleteCollectAccountDialog();
						openDeleteActivity();
					}
				}
			});
//		} else {
//			// 无效状态
//			mMoreView.setText(R.string.delete);
//			mMoreView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					openDeleteActivity();
//				}
//			});
//		}
	}

	private void openDeleteActivity() {
		Intent intent = new Intent(CollectAccountActivity.this, CollectDeleteConfirmActivity.class);
		intent.putExtra(Collect.ruleNo, (String) mCollectData.get(Collect.rusleNo));
		intent.putExtra(Collect.cbAccNum, (String) mAccountData.get(Collect.cbAccNum));
//		intent.putExtra(Collect.cbAccCard, (String) mAccountData.get(Collect.cbAccCard));
		intent.putExtra(Collect.cbAccCard, mPayeeAccount);


		// intent.putExtra(Collect.payerAcctBankNo, (String)
		// mCollectData.get(Collect.payerAccBankName));
		intent.putExtra(Collect.payerAccBankName, (String) mCollectData.get(Collect.payerAccBankName));
//		intent.putExtra(Collect.payerAccount, (String) mCollectData.get(Collect.payerAccount));
		intent.putExtra(Collect.payerAccount, mPayerAccount);
		intent.putExtra(Collect.payerAcntName, (String) mCollectData.get(Collect.payerAccountName));
		String mImputationModeParam = (String) mCollectData.get(Collect.imputationMode);
		intent.putExtra(Collect.imputationMode, mImputationModeParam);
		intent.putExtra(Collect.cycle, (String) mCollectData.get(Collect.cycle));
		intent.putExtra(Collect.cycleCode, (String) mCollectData.get(Collect.cycleCode));
		if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			intent.putExtra(Collect.amount, (String) mCollectData.get(Collect.retainAmount));
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			intent.putExtra(Collect.amount, (String) mCollectData.get(Collect.transferAmount));
		}
		intent.putExtra(Collect.ifMessage, (String) mCollectData.get(Collect.ifMessage));
		intent.putExtra(Collect.cbMobile, mCbMoblile);
		startActivityForResult(intent, request_delete_code);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == request_delete_code || requestCode == request_modify_code) {
			if (resultCode == Activity.RESULT_OK) {
				setResult(CollectListActivity.refresh_list);
				finish();
			}
		}
	}

	private void findView() {
		mDetailView = (Button) mViewContent.findViewById(R.id.btn_detail);
		mMoreView = (Button) mViewContent.findViewById(R.id.btn_more);

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
		/** 金额 */
		TextView amountView = (TextView) mViewContent.findViewById(R.id.tv_amount);
		/** 归集规则 */
		TextView collectRuleView = (TextView) mViewContent.findViewById(R.id.tv_collect_rule);
		/** 归集规则执行日 */
		TextView collectRuleCodeView = (TextView) mViewContent.findViewById(R.id.tv_rule_code);
		/** 签约日期 */
		TextView settingDateView = (TextView) mViewContent.findViewById(R.id.tv_setting_date);
		/** 协议状态 */
		TextView statusView = (TextView) mViewContent.findViewById(R.id.tv_status);

		String payerName = (String) mCollectData.get(Collect.payerAccountName);
		String payerBank = (String) mCollectData.get(Collect.payerAccBankName);
		String mImputationModeParam = (String) mCollectData.get(Collect.imputationMode);

		if (CollectImputationMode.ALL.equals(mImputationModeParam)) {
			findViewById(R.id.amount_layout).setVisibility(View.GONE);
		} else if (CollectImputationMode.KEEP.equals(mImputationModeParam)) {
			String amountParam = (String) mCollectData.get(Collect.retainAmount);
			findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			TextView amountTableView = (TextView) findViewById(R.id.amount_lable);
			amountTableView.setText(R.string.collect_retain_amount);
			amountView.setText(StringUtil.parseStringPattern(amountParam, 2));
		} else if (CollectImputationMode.QUOTA.equals(mImputationModeParam)) {
			String amountParam = (String) mCollectData.get(Collect.transferAmount);
			findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			TextView amountTableView = (TextView) findViewById(R.id.amount_lable);
			amountTableView.setText(R.string.collect_transferAmount_amount);
			amountView.setText(StringUtil.parseStringPattern(amountParam, 2));
		}

		String collectRule = (String) mCollectData.get(Collect.cycle);
		String collectCodeRule = (String) mCollectData.get(Collect.cycleCode);

		if (CollectCycleType.DAY.equals(collectRule)) {
			findViewById(R.id.rule_code_layout).setVisibility(View.GONE);
		} else if (CollectCycleType.WEEK.equals(collectRule)) {
			findViewById(R.id.rule_code_layout).setVisibility(View.VISIBLE);
			collectRuleCodeView.setText(CollectCycleCodeType.getWeekTypeStr(collectCodeRule));
		} else if (CollectCycleType.MONTH.equals(collectRule)) {
			findViewById(R.id.rule_code_layout).setVisibility(View.VISIBLE);
			collectRuleCodeView.setText(CollectCycleCodeType.getMonthTypeStr(collectCodeRule));
		}

		String cbAddDate = (String) mCollectData.get(Collect.cbAddDate);
		String status = (String) mCollectData.get(Collect.status);

		payeeAccountView.setText(StringUtil.getForSixForString(mPayeeAccount));
		payerAccountView.setText(StringUtil.getForSixForString(mPayerAccount));
		payerNameView.setText(payerName);
		payerBankView.setText(payerBank);
		collectModeView.setText(CollectImputationMode.getTypeStr(mImputationModeParam));
		collectRuleView.setText(CollectCycleType.getCycleTypeStr(collectRule));
		settingDateView.setText(cbAddDate);
		statusView.setText(CollectStatusType.getTypeStr(status));

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerNameView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerBankView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, collectModeView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, amountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, collectRuleView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, statusView);
	}

	private boolean getIntentData() {
		CollectData data = CollectData.getInstance();
		Intent intent = getIntent();
		mPayeeAccount = intent.getStringExtra(Collect.cbAccCard);
		mPayerAccount = intent.getStringExtra(Collect.payerAccount);
		mCbMoblile = intent.getStringExtra(Collect.cbMobile);
		mAccountData = data.getAccountData(mPayeeAccount);
		mCollectData = data.getCollectData(mPayeeAccount, mPayerAccount);
		return mCollectData != null && mAccountData != null;
	}
}
