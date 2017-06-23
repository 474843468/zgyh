package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCCurrency;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.RedeemVerifyInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 净值型中银理财产品的确认页面内
 * 
 * @author HVZHUNG
 *
 */

public class RedeemAffirmValueFragment extends BaseRedeemAffirmFragment {

	public static final String KEY_REDEEM_VERIFY_INFO = "verify_info";
	public static final String KEY_PRODUCT_INFO = "product_info";

	private BOCProductForHoldingInfo productInfo;
	private RedeemVerifyInfo verifyInfo;

	private LabelTextView ltv_product_code;
	/** 产品名称 */
	private LabelTextView ltv_product_name;
	/** 币种/钞汇 */
	private LabelTextView ltv_currency;
	/** 最小持有份额 */
	private LabelTextView ltv_hold_quantity_min;
	/** 最小赎回份额 */
	private LabelTextView ltv_redeem_starting_quantity;
	/** 交易手续费 */
	private LabelTextView ltv_charge;
	/** 业绩报酬 */
	private LabelTextView ltv_reward;
	/**产品详情 */
	private Map<String, Object> responseDeal;
	/** 钞汇 */
	private LabelTextView ltv_cashRemit;
	/** 提示信息*/
	private TextView tv_reminder;
	/** 指定赎回日期 */
	private LabelTextView ltv_assign_date;

	public static RedeemAffirmValueFragment newInstance(
			BOCProductForHoldingInfo productInfo, RedeemVerifyInfo info) {
		RedeemAffirmValueFragment instance = new RedeemAffirmValueFragment();
		Bundle data = new Bundle();
		data.putSerializable(KEY_PRODUCT_INFO, productInfo);
		data.putSerializable(KEY_REDEEM_VERIFY_INFO, info);
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.bocinvt_redeem_affirm_value_fragment,
						container, false);
		Bundle data = getArguments();
		productInfo = (BOCProductForHoldingInfo) data
				.getSerializable(KEY_PRODUCT_INFO);
		verifyInfo = (RedeemVerifyInfo) data
				.getSerializable(KEY_REDEEM_VERIFY_INFO);
		initCommonView(view);
		initView(view);
		setCommonViewContent(verifyInfo, productInfo);
		setViewContent(productInfo, verifyInfo);
		return view;
	}

	private void setViewContent(BOCProductForHoldingInfo productInfo,
			RedeemVerifyInfo verifyInfo) {
		responseDeal = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		ltv_product_code.setValueText(productInfo.prodCode);
		ltv_product_name.setValueText(verifyInfo.prodName);
		ltv_currency.setValueText(BOCCurrency.getInstanceByNumberCode(
				getActivity(), verifyInfo.currencyCode).name);
		ltv_hold_quantity_min.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinHoldingQuantity(productInfo));
		ltv_redeem_starting_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinRedeemQuantity(productInfo));
		if(StringUtil.isNullOrEmpty(responseDeal.get("redeemFee"))){
			ltv_charge.setValueText("无");
		}else{
			String redeemFee = (String)responseDeal.get("redeemFee");
			ltv_charge.setValueText(redeemFee.replace("|", "\n"));
		}
		ltv_reward.setValueText("实际年化收益大于" + (String)responseDeal.get("pfmcDrawStart") + "%时，超出部分收益按照" + (String)responseDeal.get("pfmcDrawScale") + "%收取业绩报酬");
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
		/** 产品名称 */
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		/** 币种/钞汇 */
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		/** 最小持有份额 */
		ltv_hold_quantity_min = (LabelTextView) view
				.findViewById(R.id.ltv_hold_quantity_min);
//		ltv_hold_quantity_min.setValueTextColor(TextColor.Red);
		/** 最小赎回份额 */
		ltv_redeem_starting_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_starting_quantity);
//		ltv_redeem_starting_quantity.setValueTextColor(TextColor.Red);
		/** 交易手续费 */
		ltv_charge = (LabelTextView) view.findViewById(R.id.ltv_charge);
		/** 业绩报酬 */
		ltv_reward = (LabelTextView) view.findViewById(R.id.ltv_reward);
		/** 钞汇*/
		ltv_cashRemit = (LabelTextView) view.findViewById(R.id.ltv_cashRemit);
		tv_reminder = (TextView) view.findViewById(R.id.tv_reminder);
		ltv_assign_date =  (LabelTextView) view.findViewById(R.id.ltv_assign_date);
		ltv_assign_date.setVisibility(BociDataCenter.isRedeem ? View.VISIBLE : View.GONE);
		ltv_product_code = (LabelTextView) view
				.findViewById(R.id.ltv_product_code);
	}
	/**
	 * 提示信息
	 * @return
	 */
	private String getReminder(){
		String reminder = "如遇节假日，您的赎回交易申请将顺延至下一交易日进行。";
		String redeemDate = (String)verifyInfo.redeemDate;
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
