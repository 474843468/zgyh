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
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 净值型理财产品赎回份额输入页面
 * 
 * @author HVZHUNG
 *
 */
public class RedeemForValueFragment extends BaseRedeemFragment {

	private static final String TAG = "RedeemForValueFragment";
	/** 产品代码 */
	private LabelTextView ltv_product_code;
	/** 产品名称 */
	private LabelTextView ltv_product_name;
	/** 币种 */
	private LabelTextView ltv_currency;
	/** 钞汇 */
	private LabelTextView ltv_cashRemit;
	/** 最低持有份额 */
	private LabelTextView ltv_hold_quantity_min;
	/** 购回起点份额 */
	private LabelTextView ltv_redeem_starting_quantity;
	/** 交易手续费 */
	private LabelTextView ltv_charge;
	/** 业绩报酬(浮动管理费) */
	private LabelTextView ltv_reward;

	private BOCProductForHoldingInfo info;
	/**产品详情 */
	private Map<String, Object> responseDeal;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_redeem_value_fragment_p603, container, false);
		initView(view);
		Bundle data = getArguments();
		info = (BOCProductForHoldingInfo) data.getSerializable(KEY_INFO);
		setViewContent(info);
		return view;
	}

	private void setViewContent(BOCProductForHoldingInfo info) {
		if (info == null) {
			LogGloble.e(TAG, "BOCProductInfo is null!!!");
			return;
		}
		responseDeal = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		ltv_product_code.setValueText(info.prodCode);
		ltv_product_name.setValueText(info.prodName);
		ltv_currency.setValueText(info.curCode.name);
		ltv_hold_quantity_min.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinHoldingQuantity(info));
		ltv_redeem_starting_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyMinRedeemQuantity(info));
		if(StringUtil.isNullOrEmpty(responseDeal.get("redeemFee"))){
			ltv_charge.setValueText("无");
		}else{
			String redeemFee = (String)responseDeal.get("redeemFee");
			ltv_charge.setValueText(redeemFee.replace("|", "\n"));
		}
		ltv_reward.setValueText("实际年化收益大于" + (String)responseDeal.get("pfmcDrawStart") + "%时，超出部分收益按照" + (String)responseDeal.get("pfmcDrawScale") + "%收取业绩报酬");
		if (!StringUtil.isNull(LocalData.Currency.get(info.curCode.numberCode))) {
			if (LocalData.Currency.get(info.curCode.numberCode).equals(ConstantGloble.ACC_RMB)) {
				ltv_cashRemit.setValueText("-");
			}else{
				ltv_cashRemit.setValueText(BociDataCenter.cashRemitMapValue2.get(info.cashRemit));
			}
		}
		setCommonViewContent(info);
	}

	private void initView(View view) {
		ltv_product_code = (LabelTextView) view
				.findViewById(R.id.ltv_product_code);
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		ltv_hold_quantity_min = (LabelTextView) view
				.findViewById(R.id.ltv_hold_quantity_min);
//		ltv_hold_quantity_min.setValueTextColor(TextColor.Red);
		ltv_redeem_starting_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_starting_quantity);
//		ltv_redeem_starting_quantity.setValueTextColor(TextColor.Red);
		ltv_charge = (LabelTextView) view.findViewById(R.id.ltv_charge);
		ltv_reward = (LabelTextView) view.findViewById(R.id.ltv_reward);
		ltv_cashRemit = (LabelTextView) view.findViewById(R.id.ltv_cashRemit);
		initCommonView(view);
	}

}
