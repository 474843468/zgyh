package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 非净值型中银理财产品赎回份额填写页面页面
 * 
 * @author HVZHUNG
 *
 */
public class RedeemForNoValueFragment extends BaseRedeemFragment {

	private static final String TAG = RedeemForNoValueFragment.class
			.getSimpleName();
	/** 产品代码 */
	private LabelTextView ltv_product_code;
	/** 产品名称 */
	private LabelTextView ltv_product_name;
	/** 产品币种 */
	private LabelTextView ltv_currency;
	/** 钞汇 */
	private LabelTextView ltv_cashRemit;
	/** 份额面值 */
	private LabelTextView ltv_redeem_worth;
	/** 最低持有份额 */
	private LabelTextView ltv_hold_quantity_min;
	/** 最低赎回份额 */
	private LabelTextView ltv_redeem_start_quantity;
	/** 是否允许撤单 */
	private LabelTextView lvt_redeem_revoke;

	private BOCProductForHoldingInfo info;
	/**产品详情 */
	private Map<String, Object> responseDeal;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_redeem_no_value_fragment_p603, container,
				false);
		initView(view);
		initCommonView(view);
		Bundle data = getArguments();
		info = (BOCProductForHoldingInfo) data.getSerializable(KEY_INFO);
		setViewContent(info);
		setCommonViewContent(info);
		return view;
	}

	private void setViewContent(BOCProductForHoldingInfo info2) {
		if (info == null) {
			LogGloble.e(TAG, "BOCProductInfo is null!!!");
			return;
		}
		responseDeal = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		ltv_product_code.setValueText(info.prodCode);
		ltv_product_name.setValueText(info.prodName);
		ltv_currency.setValueText(info.curCode.name);
		if (LocalData.Currency.get(info.curCode.numberCode).equals(ConstantGloble.ACC_RMB)) {
			ltv_cashRemit.setValueText("-");
		}else{
			ltv_cashRemit.setValueText(BociDataCenter.cashRemitMapValue2.get(info.cashRemit));
		}
		ltv_redeem_worth.setValueTextColor(TextColor.Red);
//		ltv_redeem_worth.setValueText(StringUtil.parseStringCodePattern(info.curCode.numberCode,info.shareValue, 2));
		ltv_redeem_worth.setValueText(StringUtil.parseStringPattern(info.sellPrice, 2));
//		ltv_hold_quantity_min.setValueTextColor(TextColor.Red);
		ltv_hold_quantity_min.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinHoldingQuantity(info));
//		ltv_redeem_start_quantity.setValueTextColor(TextColor.Red);
		ltv_redeem_start_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinRedeemQuantity(info));
//		lvt_redeem_revoke.setValueText(getResources().getString(
//				HoldingBOCProductInfoUtil.canRedeem(info) ? R.string.yes
//						: R.string.no));
//		lvt_redeem_revoke.setValueText(LocalData.isCanCancleStr.get((String)responseDeal.get(BocInvt.BOCINVT_ISCANCANCLE_RES)));
	}

	private void initView(View view) {
		ltv_product_code = (LabelTextView) view
				.findViewById(R.id.ltv_product_code);
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		ltv_cashRemit = (LabelTextView) view.findViewById(R.id.ltv_cashRemit);
		ltv_redeem_worth = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_worth);
		ltv_hold_quantity_min = (LabelTextView) view
				.findViewById(R.id.ltv_hold_quantity_min);
//		ltv_hold_quantity_min.setValueTextColor(TextColor.Red);
		ltv_redeem_start_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_start_quantity);
//		ltv_redeem_start_quantity.setValueTextColor(TextColor.Red);
		lvt_redeem_revoke = (LabelTextView) view
				.findViewById(R.id.lvt_redeem_revoke);

	}

}
