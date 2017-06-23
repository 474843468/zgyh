package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCCurrency;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGroupInfo;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 组合购买委托交易撤单页面
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealCancelForGroupFragment extends Fragment {

	private LabelTextView ltv_deal_id;
	private LabelTextView ltv_product_code;
	private LabelTextView ltv_product_name;
	private LabelTextView ltv_currency;
	private LabelTextView ltv_cash_remit;
	private LabelTextView ltv_amount;
	private LabelTextView ltv_deal_mount;

	public static CommissionDealCancelForGroupFragment newInstance(int mode,
			CommissionDealForGroupInfo info) {
		CommissionDealCancelForGroupFragment instance = new CommissionDealCancelForGroupFragment();
		Bundle data = new Bundle();
		data.putInt("mode", mode);
		data.putSerializable("info", info);
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(
						R.layout.bocinvt_commission_deal_cancel_for_group_fragment_p603,
						container, false);
		Bundle data = getArguments();
		int mode = data.getInt("mode");

		initView(mode, view);
		CommissionDealForGroupInfo info = (CommissionDealForGroupInfo) getArguments()
				.getSerializable("info");
		setViewContent(info);
		return view;
	}

	private void setViewContent(CommissionDealForGroupInfo info) {
		if (info == null) {
			return;
		}
		if (ltv_deal_id.getVisibility() == View.VISIBLE){
			ltv_deal_id.setValueText(info.transactionId);
		}
		ltv_product_code.setValueText(info.prodCode);
		ltv_product_name.setValueText(info.prodName);
		ltv_currency.setValueText(BOCCurrency.getInstanceByNumberCode(
				getActivity(), info.currency).name);
		if (LocalData.Currency.get(info.currency).equals(ConstantGloble.ACC_RMB)) {
			ltv_cash_remit.setValueText("-");
		}else{
			ltv_cash_remit.setValueText(LocalData.cashRemitMapValue.get(info.cashRemit));
		}
		ltv_amount.setValueText(StringUtil.parseStringCodePattern(info.currency,info.buyAmt+"", 2));
		ltv_deal_mount.setValueText(StringUtil.parseStringCodePattern(info.currency,info.amount+"", 2));
	}

	private void initView(int mode, View view) {

		ltv_deal_id = (LabelTextView) view.findViewById(R.id.ltv_deal_id);
		ltv_deal_id.setVisibility(mode == 0 ? View.GONE : View.VISIBLE);
		ltv_product_code = (LabelTextView) view
				.findViewById(R.id.ltv_product_code);
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		ltv_cash_remit = (LabelTextView) view.findViewById(R.id.ltv_cash_remit);
		ltv_amount = (LabelTextView) view.findViewById(R.id.ltv_amount);
		ltv_amount.setValueTextColor(TextColor.Red);
		ltv_deal_mount = (LabelTextView) view.findViewById(R.id.ltv_deal_mount);
		ltv_deal_mount.setValueTextColor(TextColor.Red);
	}
}
