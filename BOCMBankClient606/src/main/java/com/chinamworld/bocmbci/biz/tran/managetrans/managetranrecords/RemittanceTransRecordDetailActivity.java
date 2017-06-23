package com.chinamworld.bocmbci.biz.tran.managetrans.managetranrecords;

import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 汇款记录详情页
 * 
 * @author Zhi
 */
public class RemittanceTransRecordDetailActivity extends RemittanceBaseActivity {
	/** 收款人常驻国家(地址) */
	private TextView tvPayeeEnAddress;
	/** 收款人地址 */
	private LinearLayout llPayeeAddress;
	private TextView tvPayeeAddress;
	/** 收款银行全称 */
	private LinearLayout llPayeeBankName;
	/** 电汇费提示信息 */
	private LinearLayout llInfo;
	/** 付费币种 */
	private LinearLayout llPayBiZhong;
	/** 国内外费用承担方式 */
	private LinearLayout llChengDanFangShi;
	/** 给收款人的留言 */
	private LinearLayout llToPayeeMessage;
	private TextView tvInfo;
	// 手续费
	private String handling_charge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.temit_inquire));
		addView(R.layout.remittance_record_query_detail);
		initView();
	}

	private void initView() {
		Map<String, Object> detailMap = TranDataCenter.getInstance()
				.getQueryDetailCallBackMap();
		
		((TextView) findViewById(R.id.tv_execution_style)).setText(
				RemittanceDataCenter.run_modle.get(detailMap
						.get(Remittance.TRANSMODE)));
		((TextView) findViewById(R.id.tv_batSeq)).setText((String) detailMap
				.get(Remittance.BATSEQ));
		((TextView) findViewById(R.id.tv_transactionId))
				.setText((String) detailMap.get(Remittance.TRANSACTIONID));
		((TextView) findViewById(R.id.tv_swiftAccountNumber))
				.setText((String) detailMap.get(Remittance.PAYERACCOUNTNUMBER));
		((TextView) findViewById(R.id.tv_payeePermanentCountry))
				.setText(LocalData.Province.get(detailMap
						.get(Remittance.PAYERIBKNUM)));// 暂时用的转账的地区反显规则
		((TextView) findViewById(R.id.tv_payeeEnName))
				.setText((String) detailMap.get(Remittance.PAYERACCOUNTNAME));// 收款人名称没有
		tvPayeeEnAddress = (TextView) findViewById(R.id.payeeEnAddress);
		tvPayeeEnAddress.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvPayeeEnAddress);
		((TextView) findViewById(R.id.tv_payeeEnAddress))
				.setText(RemittanceDataCenter.countryMap.get(detailMap
						.get(Remittance.PAYEECOUNTRY)));// 收款人地址没有

		llPayeeAddress = (LinearLayout) findViewById(R.id.ll_payeeAddress);
		tvPayeeAddress = (TextView) findViewById(R.id.tv_payeeAddress);
		if (!StringUtil.isNullOrEmpty(detailMap.get(Remittance.PAYEEENADDRESS))) {
			llPayeeAddress.setVisibility(View.VISIBLE);
			tvPayeeAddress.setText(detailMap.get(Remittance.PAYEEENADDRESS)
					.toString());
		}
		((TextView) findViewById(R.id.tv_payeeActno))
				.setText((String) detailMap.get(Remittance.PAYEEACCOUNTNUMBER));
		llPayeeBankName = (LinearLayout) findViewById(R.id.ll_payeeBankName);
		if (!StringUtil.isNullOrEmpty(detailMap
				.get(Remittance.PAYEEBANKFULLNAME))) {
			llPayeeBankName.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_payeeBankName))
					.setText((String) detailMap.get(Remittance.BATSEQ));// 收款银行全称没有
		}
		((TextView) findViewById(R.id.tv_remitCurrencyCode))
				.setText(LocalData.Currency.get(detailMap
						.get(Remittance.FEECUR)));
		((TextView) findViewById(R.id.tv_cashRemit))
				.setText(LocalData.CurrencyCashremit.get(detailMap
						.get(Remittance.CASHREMIT)));
		((TextView) findViewById(R.id.tv_transferAmount)).setText(StringUtil
				.parseStringPattern((String) detailMap.get(Remittance.AMOUNT),
						2));
		// 客户端手续费　＝　commissionCharge　＋　postage　＋cashRemitExchange　 日元，韩元不现实小数点
		handling_charge = String.valueOf(
				Double.parseDouble(detailMap.get(Remittance.COMMISSIONCHARGE)==null?"0":detailMap.get(Remittance.COMMISSIONCHARGE).toString())
				+ Double.parseDouble( detailMap.get(Remittance.POSTAGE)==null?"0":detailMap.get(Remittance.POSTAGE).toString())
				+ Double.parseDouble( detailMap.get(Remittance.CASHREMITEXCHANGE)==null?"0":detailMap.get(Remittance.CASHREMITEXCHANGE).toString()));

		((TextView) findViewById(R.id.tv_transFee)).setText(StringUtil
				.parseStringCodePattern(
						(String) detailMap.get(Remittance.FEECUR),
						handling_charge, 2));

		llInfo = (LinearLayout) findViewById(R.id.ll_content_info);
		tvInfo = (TextView) findViewById(R.id.tv_content_info);
		if (detailMap.get(Remittance.CASHREMIT).equals("01")) {
			llInfo.setVisibility(View.VISIBLE);
			tvInfo.setText(this.getString(R.string.remittance_bill_info));
			;
		} else if (detailMap.get(Remittance.CASHREMIT).equals("02")) {
			llInfo.setVisibility(View.VISIBLE);
			tvInfo.setText(this.getString(R.string.remittance_remit_info));
			;
		}
		// ((TextView)findViewById(R.id.tv_dianhui)).setText((String)detailMap.get(Remittance.POSTAGE));
		// 电汇费 客户端显示“-”
		((TextView) findViewById(R.id.tv_dianhui)).setText("-");
		llPayBiZhong = (LinearLayout) findViewById(R.id.ll_payBiZhong);
		// 付费币种 ，国内外费用承担方式不显示
		if (!StringUtil.isNullOrEmpty(detailMap.get(Remittance.FEECUR2))) {
			// llPayBiZhong.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_payBiZhong))
					.setText(LocalData.Currency.get((String) detailMap
							.get(Remittance.FEECUR2)));
		}
		llChengDanFangShi = (LinearLayout) findViewById(R.id.ll_chengDanFangShi);
		// 付费币种 ，国内外费用承担方式不显示
		if (!StringUtil.isNullOrEmpty(detailMap.get(Remittance.FEEMODE))) {
			// llChengDanFangShi.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_chengDanFangShi))
					.setText((String) detailMap.get(Remittance.FEEMODE));// 国内外费用承担方式没有
		}
		llToPayeeMessage = (LinearLayout) findViewById(R.id.ll_toPayeeMessage);
		if (!StringUtil.isNullOrEmpty(detailMap
				.get(Remittance.REMITFURINFO2PAYEE))) {
			llToPayeeMessage.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_toPayeeMessage))
					.setText((String) detailMap
							.get(Remittance.REMITFURINFO2PAYEE));// 给汇款人留言没有
		}
		((TextView) findViewById(R.id.tv_tranDate)).setText((String) detailMap
				.get(Remittance.PAYMENTDATE));

		((TextView) findViewById(R.id.tv_qudao))
				.setText(RemittanceDataCenter.channel_new.get(detailMap
						.get(Remittance.CHANNEL)));
		((TextView) findViewById(R.id.tv_signStatus))
				.setText(RemittanceDataCenter.channel.get(detailMap
						.get(Remittance.STATUS)));
		((TextView) findViewById(R.id.tv_fuYan)).setText((String) detailMap
				.get(Remittance.FURINFO));
	}
}
