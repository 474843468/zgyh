package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.RedeemVerifyInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 净值型中银理财产品的确认页面内
 * 
 * @author HVZHUNG
 *
 */
public class RedeemAffirmNoValueFragment extends BaseRedeemAffirmFragment {

	private static final String KEY_PRODUCT_INFO = "product_info";
	private static final String KEY_VERIFY_INFO = "verify_info";

	private BOCProductForHoldingInfo productInfo;
	private RedeemVerifyInfo verifyInfo;

	private LabelTextView ltv_product_code;
	private LabelTextView ltv_product_name;
	private LabelTextView ltv_currency;
	private LabelTextView ltv_redeem_worth;
	private LabelTextView ltv_hold_quantity_min;
	private LabelTextView ltv_redeem_start_quantity;
	/** 钞汇 */
	private LabelTextView ltv_cashRemit;
	/** 提示信息*/
	private TextView tv_reminder;
	/** 指定赎回日期 */
	private LabelTextView ltv_assign_date;

	public static RedeemAffirmNoValueFragment newInstance(
			BOCProductForHoldingInfo productInfo, RedeemVerifyInfo verifyInfo) {
		RedeemAffirmNoValueFragment instance = new RedeemAffirmNoValueFragment();
		Bundle data = new Bundle();
		data.putSerializable(KEY_PRODUCT_INFO, productInfo);
		data.putSerializable(KEY_VERIFY_INFO, verifyInfo);
		instance.setArguments(data);

		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_redeem_affirm_no_value_fragment, container,
				false);
		Bundle data = getArguments();
		productInfo = (BOCProductForHoldingInfo) data
				.getSerializable(KEY_PRODUCT_INFO);
		verifyInfo = (RedeemVerifyInfo) data.getSerializable(KEY_VERIFY_INFO);
		initCommonView(view);
		initView(view);
		setCommonViewContent(verifyInfo, productInfo);
		setViewContent(productInfo, verifyInfo);
		return view;
	}

	private void setViewContent(BOCProductForHoldingInfo productInfo,
			RedeemVerifyInfo verifyInfo) {
		ltv_product_code.setValueText(productInfo.prodCode);
		ltv_product_name.setValueText(productInfo.prodName);
		ltv_currency.setValueText(productInfo.curCode.name);
		ltv_redeem_worth.setValueTextColor(TextColor.Red);
//		ltv_redeem_worth.setValueText(StringUtil.parseStringCodePattern(productInfo.curCode.numberCode,productInfo.shareValue, 2));
		ltv_redeem_worth.setValueText(StringUtil.parseStringPattern(productInfo.sellPrice, 2));
		ltv_hold_quantity_min.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinHoldingQuantity(productInfo));
//		ltv_hold_quantity_min.setValueTextColor(TextColor.Red);
		ltv_redeem_start_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinRedeemQuantity(productInfo));
//		ltv_redeem_start_quantity.setValueTextColor(TextColor.Red);
		if (!StringUtil.isNull(LocalData.Currency.get(verifyInfo.currencyCode))) {
			if (LocalData.Currency.get(verifyInfo.currencyCode).equals(ConstantGloble.ACC_RMB)) {
				ltv_cashRemit.setValueText("-");
			}else{
				ltv_cashRemit.setValueText(BociDataCenter.cashRemitMapValue2.get(productInfo.cashRemit));
			}
		}
		tv_reminder.setText(getReminder());
		ltv_assign_date.setValueText(verifyInfo.redeemDate);
	}

	private void initView(View view) {
		ltv_product_code = (LabelTextView) view
				.findViewById(R.id.ltv_product_code);
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		ltv_redeem_worth = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_worth);
		ltv_hold_quantity_min = (LabelTextView) view
				.findViewById(R.id.ltv_hold_quantity_min);
		ltv_redeem_start_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_start_quantity);
		/** 钞汇*/
		ltv_cashRemit = (LabelTextView) view.findViewById(R.id.ltv_cashRemit);
		tv_reminder = (TextView) view.findViewById(R.id.tv_reminder);
		ltv_assign_date =  (LabelTextView) view.findViewById(R.id.ltv_assign_date);
		ltv_assign_date.setVisibility(BociDataCenter.isRedeem ? View.VISIBLE : View.GONE);
	}
	
	private String getReminder(){
		String reminder = "如遇节假日，您的赎回交易申请将顺延至下一交易日进行。";
		String redeemDate = verifyInfo.redeemDate;
		if(!StringUtil.isNullOrEmpty(redeemDate)){
			redeemDate = DateUtils.formatStr(redeemDate);
		}
		if(!BociDataCenter.isRedeem){
			reminder = "根据您的选择，预计将于"+redeemDate+"发起赎回；"+reminder;
		}else{
			
		}
		return reminder;
	}
}
