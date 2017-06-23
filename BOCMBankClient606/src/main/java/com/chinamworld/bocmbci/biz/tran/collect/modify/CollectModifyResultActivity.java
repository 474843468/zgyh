package com.chinamworld.bocmbci.biz.tran.collect.modify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleCodeType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectCycleType;
import com.chinamworld.bocmbci.bii.constant.Collect.CollectImputationMode;
import com.chinamworld.bocmbci.biz.tran.collect.CollectBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CollectModifyResultActivity
 * @Description: 跨行资金归集修改结果页面
 * @author luql
 * @date 2014-3-18 下午02:33:33
 */
public class CollectModifyResultActivity extends CollectBaseActivity {
	private View mViewContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewContent = addView(R.layout.collect_modify_result_activity);
		setTitle(getString(R.string.collect_modify_title));
		toprightBtn();
		goneBackView();
		findView();
		setResult(RESULT_OK);
	}

	private void findView() {
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
		TextView retainAmountView = (TextView) mViewContent.findViewById(R.id.tv_retain_amount);
		/** 归集规则 */
		TextView collectRuleView = (TextView) mViewContent.findViewById(R.id.tv_rule);
		/** 归集规则执行日 */
		TextView collectRuleCodeView = (TextView) mViewContent.findViewById(R.id.tv_rule_code);
		/** 归集手机号 */
		TextView payeeMobileView = (TextView) mViewContent.findViewById(R.id.tv_payee_mobile);
		/** 完成 */
		View finishView = findViewById(R.id.btnfinish);

		//
		Intent intent = getIntent();
		String cbAccCard = intent.getStringExtra(Collect.cbAccNum);
		String payerAccount = intent.getStringExtra(Collect.payerAccount);
		String payerAccName = intent.getStringExtra(Collect.payerAccountName);
		String payerAccBankName = intent.getStringExtra(Collect.payerAccBankName);

		String imputationModeCodeParam = intent.getStringExtra(Collect.imputationMode);
		String cycleParam = intent.getStringExtra(Collect.cycle);
		String cycleCodeParam = intent.getStringExtra(Collect.cycleCode);
		String amountParam = intent.getStringExtra(Collect.amount);
		boolean ifMessageParam = intent.getBooleanExtra(Collect.ifMessage, true);
		String cbMobile = intent.getStringExtra(Collect.cbMobile);

		payeeAccountView.setText(StringUtil.getForSixForString(cbAccCard));
		payerAccountView.setText(StringUtil.getForSixForString(payerAccount));
		payerNameView.setText(payerAccName);
		payerBankView.setText(payerAccBankName);

		collectModeView.setText(CollectImputationMode.getTypeStr(imputationModeCodeParam));
		if (CollectImputationMode.ALL.equals(imputationModeCodeParam)) {
			findViewById(R.id.amount_layout).setVisibility(View.GONE);
		} else if (CollectImputationMode.KEEP.equals(imputationModeCodeParam)) {
			findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			TextView amountTableView = (TextView) findViewById(R.id.amount_lable);
			amountTableView.setText(R.string.collect_retain_amount);
			retainAmountView.setText(StringUtil.parseStringPattern(amountParam, 2));
		} else if (CollectImputationMode.QUOTA.equals(imputationModeCodeParam)) {
			findViewById(R.id.amount_layout).setVisibility(View.VISIBLE);
			TextView amountTableView = (TextView) findViewById(R.id.amount_lable);
			amountTableView.setText(R.string.collect_transferAmount_amount);
			retainAmountView.setText(StringUtil.parseStringPattern(amountParam, 2));
		}

		// collect_setting_result_activity
		collectRuleView.setText(CollectCycleType.getCycleTypeStr(cycleParam));

		if (CollectCycleType.DAY.equals(cycleParam)) {
			findViewById(R.id.rule_code_layout).setVisibility(View.GONE);
		} else if (CollectCycleType.WEEK.equals(cycleParam)) {
			findViewById(R.id.rule_code_layout).setVisibility(View.VISIBLE);
			collectRuleCodeView.setText(CollectCycleCodeType.getWeekTypeStr(cycleCodeParam));
		} else if (CollectCycleType.MONTH.equals(cycleParam)) {
			findViewById(R.id.rule_code_layout).setVisibility(View.VISIBLE);
			collectRuleCodeView.setText(CollectCycleCodeType.getMonthTypeStr(cycleCodeParam));
		}

		payeeMobileView.setText(cbMobile);
//		if (ifMessageParam) {
//			findViewById(R.id.mobile_layout).setVisibility(View.VISIBLE);
//		} else {
//			findViewById(R.id.mobile_layout).setVisibility(View.GONE);
//		}

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerNameView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payerBankView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, collectModeView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, retainAmountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, collectRuleView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, collectRuleCodeView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, payeeMobileView);

		// 完成
		finishView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
