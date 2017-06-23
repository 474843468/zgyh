package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitForValueInfo;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 净值型产品的参考收益显示
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForValueFragment extends Fragment {

	private static final String KEY_PRODUCT_INFO = "product_info";
	private static final String KEY_REFERENCE_INFO = "reference_info";

	private TextView tv_profit_summarize;
	private LabelTextView ltv_amt;
	private LabelTextView ltv_balamt;
	private TextView tv_reminder;

	private BOCProductForHoldingInfo productInfo;
	private ReferenceProfitForValueInfo referenceInfo;

	/**
	 * 
	 * @param info
	 *            理财产品信息
	 * 
	 * @param referProfitInfo
	 *            参考收益数据
	 * @return
	 */
	public static ReferenceProfitForValueFragment newInstance(
			BOCProductForHoldingInfo info,
			ReferenceProfitForValueInfo referProfitInfo) {
		ReferenceProfitForValueFragment instance = new ReferenceProfitForValueFragment();
		Bundle data = new Bundle();
		data.putSerializable(KEY_PRODUCT_INFO, info);
		data.putSerializable(KEY_REFERENCE_INFO, referProfitInfo);
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_reference_profit_for_value, container, false);
		initView(view);
		Bundle data = getArguments();
		productInfo = (BOCProductForHoldingInfo) data
				.getSerializable(KEY_PRODUCT_INFO);
		referenceInfo = (ReferenceProfitForValueInfo) data
				.getSerializable(KEY_REFERENCE_INFO);
		setViewContent(productInfo, referenceInfo);
		return view;
	}

	/**
	 * 设置显示内容
	 */
	private void setViewContent(BOCProductForHoldingInfo productInfo,
			ReferenceProfitForValueInfo referenceInfo) {
		tv_profit_summarize.setText(getSummarizeText(referenceInfo));
		ltv_amt.setValueText(StringUtil.parseStringCodePattern(referenceInfo.procur,referenceInfo.amt, 2));
		ltv_balamt.setValueText(StringUtil.parseStringCodePattern(referenceInfo.procur,referenceInfo.balamt, 2));

	}

	private void initView(View view) {
		tv_profit_summarize = (TextView) view
				.findViewById(R.id.tv_profit_summarize);
		ltv_amt = (LabelTextView) view.findViewById(R.id.ltv_amt);
		ltv_balamt = (LabelTextView) view.findViewById(R.id.ltv_balamt);
		tv_reminder = (TextView) view.findViewById(R.id.tv_reminder);

	}

	/**
	 * 获取净值型产品收益的概述信息
	 * 
	 * @param info
	 * @return
	 */
	private CharSequence getSummarizeText(ReferenceProfitForValueInfo info) {
//		String result = "";
//		String s = getResources().getString(
//				R.string.bocinvt_profit_summarize_for_value);
//		result = String
//				.format(s, info.profitdate, info.prodName, StringUtil.parseStringCodePattern(referenceInfo.procur,info.totalamt, 2));
//		return result;
		String s1 = getResources().getString(R.string.bocinvt_profit_summarize_for_value01);
		String s2 = getResources().getString(R.string.bocinvt_profit_summarize_for_value02);
		String s3 = getResources().getString(R.string.bocinvt_profit_summarize_for_value03);
		String s4 = getResources().getString(R.string.bocinvt_profit_summarize_for_value04);
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(s1);
		builder.append(info.profitdate);
		builder.append(s2);
		builder.append(info.prodName);
		builder.append(s3);
		int startIndex = builder.length();
		builder.append(StringUtil.parseStringCodePattern(referenceInfo.procur,info.totalamt, 2));
		int endIndex = builder.length();
		builder.append(BocInvestControl.map_productCurCode_toStr2.get(referenceInfo.procur));
		builder.append(s4);
		builder.setSpan(new ForegroundColorSpan(Color.RED), startIndex,
				endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return builder;
	}
}
