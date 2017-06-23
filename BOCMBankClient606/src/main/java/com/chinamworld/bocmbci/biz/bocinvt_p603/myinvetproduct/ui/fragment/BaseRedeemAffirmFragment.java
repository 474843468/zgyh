package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.RedeemVerifyInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;

/**
 * 赎回确认页面基类
 * 
 * @author HVZHUNG
 *
 */
public class BaseRedeemAffirmFragment extends Fragment {

	/** 是否允许撤单 */
	private LabelTextView ltv_redeem_revoke;
	/** 持有份额 */
	private LabelTextView ltv_hold_quantity;
	/** 可用份额 */
	private LabelTextView ltv_usable_quantity;
	/** 赎回份额 */
	private LabelTextView ltv_redeem_quantity;
	/** 赎回日期 */
	private LabelTextView ltv_assign_date;

	protected void initCommonView(View view) {
		ltv_redeem_revoke = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_revoke);
		ltv_hold_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_hold_quantity);
		ltv_hold_quantity.setValueTextColor(TextColor.Red);
		ltv_usable_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_usable_quantity);
		ltv_usable_quantity.setValueTextColor(TextColor.Red);
		ltv_redeem_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_redeem_quantity);
		ltv_redeem_quantity.setValueTextColor(TextColor.Red);
		ltv_assign_date = (LabelTextView) view
				.findViewById(R.id.ltv_assign_date);
	}

	protected void setCommonViewContent(RedeemVerifyInfo info,
			BOCProductForHoldingInfo productInfo) {
//		ltv_redeem_revoke.setValueText("是");
		ltv_hold_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyHoldingQuantity(productInfo));
		ltv_usable_quantity.setValueText(HoldingBOCProductInfoUtil
				.getFriendlyAvailableQuantity(productInfo));
		ltv_redeem_quantity.setValueText(HoldingBOCProductInfoUtil
				.formatQuantity(info.redeemQuantity,productInfo));
	}
}
