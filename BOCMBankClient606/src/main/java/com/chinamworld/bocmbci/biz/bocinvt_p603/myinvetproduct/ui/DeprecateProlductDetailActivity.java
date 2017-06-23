package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BocProductForDeprecateInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoTransformUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 过期产品详情
 * 
 * @author HVZHUNG
 *
 */
public class DeprecateProlductDetailActivity extends BocInvtBaseActivity {

	private static final String TAG = DeprecateProlductDetailActivity.class
			.getSimpleName();
	public static final String KEY_INFO = "info";
	/** 产品到期日 */
	private LabelTextView ltv_product_deprecate_date;
	/** 产品名称 */
	private LabelTextView ltv_product_name;
	/** 币种 */
	private LabelTextView ltv_currency;
	/** 产品期限 */
	private LabelTextView ltv_product_deadline;
	/** 投资本金 */
	private LabelTextView ltv_investment_principal;
	/** 真实收益 */
	private LabelTextView ltv_actuality_rate;
	/** 真实年收益率 */
	private LabelTextView ltv_actuality_yield_rate;
	/** 资金账户 */
	private LabelTextView ltv_capital_account;
	/** 钞汇 */
	private LabelTextView ltv_remit;

	private BocProductForDeprecateInfo info;

	/** 产品详情 */
	private Map<String, Object> infoDetailMap ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bocinvt_deprecate_product_detail_actvity_p603);
		setTitle("到期产品详情");
		//getBackgroundLayout().setRightButtonText(null);
		initView();
		infoDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get("bocinvtDeprecateDetailMap");
		info = HoldingBOCProductInfoTransformUtil.map2BocProductForDeprecateInfo(infoDetailMap);
		/*info = (BocProductForDeprecateInfo) getIntent().getSerializableExtra(
				KEY_INFO);*/
		if (info == null) {
			LogGloble.e(TAG, "BocProductForDeprecateInfo is null!!!");
			return;
		}
		setViewContent(info);
	}

	private void setViewContent(BocProductForDeprecateInfo info) {
		if (info == null) {
			return;
		}
		/** 产品到期日 */
		ltv_product_deprecate_date.setValueText(info.eDate);
		/** 产品名称 */
		ltv_product_name.setValueText(info.proname);
		/** 币种 */
		ltv_currency.setValueText(info.procur.name);
		/** 产品期限 */
		ltv_product_deadline.setValueText(info.proterm+"天");
		/** 投资本金 */
		ltv_investment_principal.setValueText(StringUtil.parseStringCodePattern(info.procur.numberCode,info.amount.toString(), 2));
		ltv_investment_principal.setValueTextColor(TextColor.Red);
		/** 真实收益 */
		if(!StringUtil.isNullOrEmpty(info.payFlag)&&"0".equals(info.payFlag)){
			ltv_actuality_rate.setValueText("结算中");//真实收益
			ltv_actuality_yield_rate.setValueText("结算中");// 真实年收益率 
		}else if(!StringUtil.isNullOrEmpty(info.payFlag)&&"2".equals(info.payFlag)){
			ltv_actuality_rate.setValueText("-");
			ltv_actuality_yield_rate.setValueText("-");
		}else{
			ltv_actuality_rate.setValueText(StringUtil.parseStringCodePattern(info.procur.numberCode,info.payProfit.toString(), 2));
			ltv_actuality_rate.setValueTextColor(TextColor.Red);
			ltv_actuality_yield_rate.setValueText(StringUtil.append2Decimals(info.payRate.toString(), 2)+"%");
		}
		/** 资金账户 */
		ltv_capital_account.setValueText(StringUtil.getForSixForString(info.accno));
		if (!StringUtil.isNull(LocalData.Currency.get(info.procur.numberCode))) {
			if (LocalData.Currency.get(info.procur.numberCode).equals(ConstantGloble.ACC_RMB)) {
				/** 钞汇 */
				ltv_remit.setValueText("-");
			} else {
				ltv_remit.setValueText(BociDataCenter.cashRemitMapValue2.get(info.buyMode));

			}
		}
	}

	private void initView() {
		ltv_product_deprecate_date = (LabelTextView) findViewById(R.id.ltv_product_deprecate_date);
		ltv_product_name = (LabelTextView) findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) findViewById(R.id.ltv_currency);
		ltv_product_deadline = (LabelTextView) findViewById(R.id.ltv_product_deadline);
		ltv_investment_principal = (LabelTextView) findViewById(R.id.ltv_investment_principal);
		ltv_actuality_rate = (LabelTextView) findViewById(R.id.ltv_actuality_rate);
		ltv_actuality_yield_rate = (LabelTextView) findViewById(R.id.ltv_actuality_yield_rate);
		ltv_capital_account = (LabelTextView) findViewById(R.id.ltv_capital_account);
		ltv_remit = (LabelTextView) findViewById(R.id.ltv_remit);
	}

	public static Intent getIntent(Context context,
			BocProductForDeprecateInfo info) {

		Intent intent = new Intent(context,
				DeprecateProlductDetailActivity.class);
		intent.putExtra(KEY_INFO, info);

		return intent;
	}
}
