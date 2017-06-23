package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCCurrency;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.CommissionDealForGroupInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.CommissionDealGroupProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.CommissionDealRevokeActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 委托交易组合购买详情
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealDetailForGroupFragment extends Fragment implements
		OnClickListener {
	private LabelTextView ltv_commission_date;
	private LabelTextView ltv_product_name;
	private LabelTextView ltv_currency;
	private LabelTextView ltv_purchase_amount;
	private LabelTextView ltv_deal_amount;
	private LabelTextView ltv_deal_channel;
	private LabelTextView ltv_deal_status;
	private Button bt_group_list;
	private Button bt_revoke;

	private CommissionDealForGroupInfo info;
	private String ibknum;
	private String typeOfAccount;
	
	private View rootView;
//	public static CommissionDealDetailForGroupFragment newInstance(
//			CommissionDealForGroupInfo info) {
//		CommissionDealDetailForGroupFragment instance = new CommissionDealDetailForGroupFragment();
//		Bundle data = new Bundle();
//		data.putSerializable("info", info);
//		instance.setArguments(data);
//		return instance;
//	}
	public static CommissionDealDetailForGroupFragment newInstance(
			CommissionDealForGroupInfo info,String ibknum,String typeOfAccount) {
		CommissionDealDetailForGroupFragment instance = new CommissionDealDetailForGroupFragment();
		Bundle data = new Bundle();
//		data.putString("accountKey",info.accountKey);
//		data.putString("info", info.tranSeq);
		data.putSerializable("info", info);
		data.putString("ibknum", ibknum);
		data.putString("typeOfAccount", typeOfAccount);
		instance.setArguments(data);
		return instance;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_commission_deal_detail_group_fragment_p603,
				container, false);
		rootView = view;
		initView(view);
		info = (CommissionDealForGroupInfo) getArguments().getSerializable(
				"info");
		ibknum=getArguments().getString("ibknum");
		typeOfAccount=getArguments().getString("typeOfAccount");
		setViewContent(info);
		return view;
	}

	private void setViewContent(CommissionDealForGroupInfo info) {
		if (info == null) {
			return;
		}
		((LabelTextView) rootView.findViewById(R.id.ltv_cashremmit)).setValueText(LocalData.cashRemitMapValue.get(info.cashRemit));
		
		
		ltv_commission_date.setValueText(info.returnDate);
		ltv_product_name.setValueText(info.prodName);
		ltv_currency.setValueText(BOCCurrency.getInstanceByNumberCode(
				getActivity(), info.currency).name);
		ltv_purchase_amount.setValueText(StringUtil.parseStringCodePattern(info.currency,info.buyAmt+"", 2));
		ltv_deal_amount.setValueText(StringUtil.parseStringCodePattern(info.currency,info.amount+"", 2));
		ltv_deal_channel.setValueText(info.getChnanelName());
		ltv_deal_status.setValueText(info.getStatusText());
	}

	private void initView(View view) {
		ltv_commission_date = (LabelTextView) view
				.findViewById(R.id.ltv_commission_date);
		ltv_product_name = (LabelTextView) view
				.findViewById(R.id.ltv_product_name);
		ltv_currency = (LabelTextView) view.findViewById(R.id.ltv_currency);
		ltv_purchase_amount = (LabelTextView) view
				.findViewById(R.id.ltv_purchase_amount);
		ltv_purchase_amount.setValueTextColor(TextColor.Red);
		ltv_deal_amount = (LabelTextView) view
				.findViewById(R.id.ltv_deal_amount);
		ltv_deal_amount.setValueTextColor(TextColor.Red);
		ltv_deal_channel = (LabelTextView) view
				.findViewById(R.id.ltv_deal_channel);
		ltv_deal_status = (LabelTextView) view
				.findViewById(R.id.ltv_deal_status);
		bt_group_list = (Button) view.findViewById(R.id.bt_group_list);
		bt_revoke = (Button) view.findViewById(R.id.bt_revoke);
		bt_group_list.setOnClickListener(this);
		bt_revoke.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_group_list:
			Intent intent = new Intent(getActivity(),
					CommissionDealGroupProductListActivity.class);
//			intent.putExtra("tranSeq", info.tranSeq);
			intent.putExtra("info", info);
			intent.putExtra("ibknum", ibknum);
			intent.putExtra("typeOfAccount", typeOfAccount);
			intent.putExtra("accountKey", info.accountKey);
			startActivity(intent);
			break;
		case R.id.bt_revoke:
			Intent revokeit=CommissionDealRevokeActivity.getIntent(getActivity(),
					info);
			revokeit.putExtra("ibknum", ibknum);
			revokeit.putExtra("typeOfAccount", typeOfAccount);
			startActivity(revokeit);
			break;

		default:
			break;
		}

	}
}
