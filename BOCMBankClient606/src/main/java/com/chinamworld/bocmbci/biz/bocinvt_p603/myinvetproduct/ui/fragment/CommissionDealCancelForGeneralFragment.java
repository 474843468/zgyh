package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCCurrency;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGeneralInfo;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 常规委托交易撤单页面
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealCancelForGeneralFragment extends Fragment {

	private LabelTextView ltv_deal_id;
	private LabelTextView ltv_deal_date;
	private LabelTextView ltv_product_name;
	private LabelTextView ltv_currency;
	private LabelTextView ltv_deal_type;
	private LabelTextView ltv_amount;
	private LabelTextView ltv_commission_quantity;
	private LabelTextView ltv_account;
	private LabelTextView ltv_estimate_date;
	/** 提示信息*/
	private TextView tv_reminder;
	
    private int mode;
    private View rootView;
    
	public static CommissionDealCancelForGeneralFragment newInstance(int mode,
			CommissionDealForGeneralInfo info) {
		CommissionDealCancelForGeneralFragment instance = new CommissionDealCancelForGeneralFragment();
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
						R.layout.bocinvt_commission_deal_cancel_for_general_fragment_p603,
						container, false);
		rootView = view;
		Bundle data = getArguments();
		mode = data.getInt("mode");

		initView(mode, view);
		CommissionDealForGeneralInfo info = (CommissionDealForGeneralInfo) getArguments()
				.getSerializable("info");
		setViewContent(info);
		return view;
	}

	private void setViewContent(CommissionDealForGeneralInfo info) {
		if (info == null) {
			return;
		}
		((LabelTextView) rootView.findViewById(R.id.ltv_cashremmit)).setValueText(LocalData.cashRemitMapValue.get(info.cashRemit));
		
		if (ltv_deal_id.getVisibility() == View.VISIBLE){
			ltv_deal_id.setValueText(info.transactionId);
		}
		ltv_deal_date.setValueText(info.futureDate);
		ltv_product_name.setValueText(info.prodName);
		ltv_currency.setValueText(BOCCurrency.getInstanceByNumberCode(
				getActivity(), info.currencyCode).name);
		ltv_deal_type.setValueText(LocalData.bociTrfTypeMap.get(info.trfType));
//		ltv_deal_tranAtrr.setValueText(LocalData.bociTranAtrrMap.get(info.tranAtrr+""));
		ltv_amount.setValueText(info.getFormatTradeAmount());
		String commissionQuantity = "-";
		if(Double.parseDouble(info.trfAmount.toString()) > 0){
//			commissionQuantity = StringUtil.parseStringCodePattern(info.currencyCode,info.trfAmount.toString(), 2);
			commissionQuantity = StringUtil.parseStringPattern(info.trfAmount.toString(), 2);
		}
		ltv_commission_quantity.setValueText(commissionQuantity);
		ltv_account.setValueText(StringUtil.getForSixForString(info.accountNumber));
		ltv_estimate_date.setValueText(info.paymentDate);
		int entrustType = info.entrustType;
		if(entrustType == 6 && mode == 0){
			tv_reminder.setVisibility(View.VISIBLE);
		}
	}

	private void initView(int mode, View view) {

		ltv_deal_id = (LabelTextView) view.findViewById(R.id.ltv_deal_id);
		ltv_deal_id.setVisibility(mode == 0 ? View.GONE : View.VISIBLE);
		ltv_deal_date = (LabelTextView) view.findViewById(R.id.ltv_deal_date);
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		ltv_deal_type = (LabelTextView) view.findViewById(R.id.ltv_deal_type);
//		ltv_deal_tranAtrr= (LabelTextView) view.findViewById(R.id.ltv_deal_tranAtrr);
		ltv_amount = (LabelTextView) view.findViewById(R.id.ltv_amount);
		ltv_amount.setValueTextColor(TextColor.Red);
		ltv_commission_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_commission_quantity);
		ltv_commission_quantity.setValueTextColor(TextColor.Red);
		ltv_account = (LabelTextView) view.findViewById(R.id.ltv_account);
		ltv_estimate_date = (LabelTextView) view
				.findViewById(R.id.ltv_estimate_date);
		tv_reminder = (TextView) view.findViewById(R.id.tv_reminder);

	}
}
