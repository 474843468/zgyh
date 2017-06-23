package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCCurrency;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGeneralInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.CommissionDealRevokeActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 委托交易常规交易详情
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealDetailForGeneralFragment extends Fragment {

	private LabelTextView ltv_commission_date;
	private LabelTextView ltv_product_name;
	private LabelTextView ltv_currency;
	private LabelTextView ltv_deal_type;
	private LabelTextView ltv_commission_amount;
	private LabelTextView ltv_account;
	private LabelTextView ltv_predict_deal_date;
	/** 委托份额*/
	private LabelTextView ltv_commission_quantity;
	private Button bt_revoke;

	private CommissionDealForGeneralInfo info;

	public static CommissionDealDetailForGeneralFragment newInstance(
			CommissionDealForGeneralInfo info) {
		CommissionDealDetailForGeneralFragment instance = new CommissionDealDetailForGeneralFragment();
		Bundle data = new Bundle();
		data.putSerializable("info", info);
		instance.setArguments(data);

		return instance;
	}

	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_commission_deal_detail_general_fragment_p603,
				container, false);
		rootView = view;
		initView(view);
		info = (CommissionDealForGeneralInfo) getArguments().getSerializable(
				"info");
		setViewContent(info);
		return view;
	}

	private void setViewContent(final CommissionDealForGeneralInfo info) {
		if (info == null) {
			return;
		}
		((LabelTextView) rootView.findViewById(R.id.ltv_deal_attr)).setValueText(BociDataCenter.entrustTypeMapValue.get(info.entrustType+""));
		((LabelTextView) rootView.findViewById(R.id.ltv_cashremmit)).setValueText(LocalData.cashRemitMapValue.get(info.cashRemit));
		
		
		ltv_commission_date.setValueText(info.futureDate);
		ltv_product_name.setValueText(info.prodName);
		ltv_currency.setValueText(BOCCurrency.getInstanceByNumberCode(
				getActivity(), info.currencyCode).name);
//		ltv_deal_type.setValueText(info.getTradeTypeName());
		ltv_deal_type.setValueText(LocalData.bociTrfTypeMap.get(info.trfType));
		ltv_commission_amount.setValueText(info.getFormatTradeAmount());
		ltv_account.setValueText(StringUtil.getForSixForString(info.accountNumber));
		String commissionQuantity = "-";
		if(Double.parseDouble(info.trfAmount.toString()) > 0){
//			commissionQuantity = StringUtil.parseStringCodePattern(info.currencyCode,info.trfAmount.toString(), 2);
			commissionQuantity = StringUtil.parseStringPattern(info.trfAmount.toString(), 2);
		}
		ltv_commission_quantity.setValueText(commissionQuantity);
		ltv_predict_deal_date.setValueText(info.paymentDate);
		boolean canceled = info.enableCanceled();
		bt_revoke.setVisibility(canceled ? View.VISIBLE : View.GONE);
		// bt_revoke.setEnabled(canceled);
		bt_revoke.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(CommissionDealRevokeActivity.getIntent(
						getActivity(), info));

			}
		});
	}

	private void initView(View view) {
		ltv_commission_date = (LabelTextView) view
				.findViewById(R.id.ltv_commission_date);
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		ltv_deal_type = (LabelTextView) view.findViewById(R.id.ltv_deal_type);
		ltv_commission_amount = (LabelTextView) view
				.findViewById(R.id.ltv_commission_amount);
		ltv_commission_amount.setValueTextColor(TextColor.Red);
		ltv_account = (LabelTextView) view.findViewById(R.id.ltv_account);
		ltv_predict_deal_date = (LabelTextView) view
				.findViewById(R.id.ltv_predict_deal_date);
		bt_revoke = (Button) view.findViewById(R.id.bt_revoke);
		ltv_commission_quantity = (LabelTextView) view
				.findViewById(R.id.ltv_commission_quantity);
		ltv_commission_quantity.setValueTextColor(TextColor.Red);
	}
}
