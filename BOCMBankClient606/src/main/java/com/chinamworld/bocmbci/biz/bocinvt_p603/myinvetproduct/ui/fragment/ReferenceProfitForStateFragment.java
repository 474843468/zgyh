package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BaseReferProfitInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitForCashInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.widget.CakeChartView;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 显示参考收益的收益状况信息，目前用于“日积月累”，“业绩基准型”产品
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForStateFragment extends Fragment {

	private static final String KEY_PRODUCT_INFO = "product_info";
	private static final String KEY_REFERENCE_INFO = "reference_info";
	private static final String KEY_SHOW_REMINDER = "show_reminder";
	/** 饼状图 */
	private CakeChartView cake_chart;
	/** 收益状况的概述 */
	private TextView tv_summarize;
	/** 未到账收益 */
	private TextView tv_legend_unpay;
	/** 已到账收益 */
	private TextView tv_legend_paid;
	/** 温馨提示内容 */
	private TextView tv_reminder;
	/** 是否为日积月累 */
	private boolean isDayAndMonth;
	private BOCProductForHoldingInfo productInfo;
	private ReferenceProfitForCashInfo referProfitInfo;
	private boolean isShowReminder;

	public static ReferenceProfitForStateFragment newInstance(
			BOCProductForHoldingInfo info,
			ReferenceProfitForCashInfo referProfitInfo,
			boolean isShowReminderView) {
		ReferenceProfitForStateFragment instance = new ReferenceProfitForStateFragment();
		Bundle data = new Bundle();
		data.putSerializable(KEY_PRODUCT_INFO, info);
		data.putBoolean(KEY_SHOW_REMINDER, isShowReminderView);
		data.putSerializable(KEY_REFERENCE_INFO, referProfitInfo);
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bocinvt_reference_profit_state,
				container, false);
		initView(view);

		Bundle data = getArguments();
		productInfo = (BOCProductForHoldingInfo) data
				.getSerializable(KEY_PRODUCT_INFO);
		referProfitInfo = (ReferenceProfitForCashInfo) data
				.getSerializable(KEY_REFERENCE_INFO);
		isShowReminder = data.getBoolean(KEY_SHOW_REMINDER);

		setViewContent(referProfitInfo, isShowReminder);

		return view;
	}

	/**
	 * 设置各个View的显示内容
	 * 
	 * @param data
	 */
	private void setViewContent(ReferenceProfitForCashInfo info,
			boolean isShowReminderView) {
		isDayAndMonth = isShowReminderView;
		double unpayprofit = Double.parseDouble(info.unpayprofit);
		double payprofit = Double.parseDouble(info.payprofit);
		double total = (double)unpayprofit + payprofit;
		if(unpayprofit != 0 && payprofit != 0 &&unpayprofit/total < 0.01){
			cake_chart.setDataAndColor(new double[] { 1.0, 99.0 },
					new int[] { getResources().getColor(R.color.blue),
							getResources().getColor(R.color.red) });
		}else if(unpayprofit != 0 && payprofit != 0 &&payprofit/total < 0.01){
			cake_chart.setDataAndColor(new double[] { 99.0, 1.0 },
					new int[] { getResources().getColor(R.color.blue),
							getResources().getColor(R.color.red) });
		}else{
			cake_chart.setDataAndColor(new double[] { Double.parseDouble(info.unpayprofit), Double.parseDouble(info.payprofit) },
					new int[] { getResources().getColor(R.color.blue),
							getResources().getColor(R.color.red) });
		}
		// 设置未到账已到账收益显示左侧的图例（蓝绿色块）
		int width = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
						.getDisplayMetrics());
		tv_legend_unpay.setCompoundDrawablePadding(width);
		tv_legend_paid.setCompoundDrawablePadding(width);
		tv_legend_unpay.setCompoundDrawables(
				getBitmap(width, getResources().getColor(R.color.blue)), null,
				null, null);
		tv_legend_paid.setCompoundDrawables(
				getBitmap(width, getResources().getColor(R.color.red)), null,
				null, null);
//		CharSequence s = generateSummarize(info);
		String currency = info.procur;
		tv_legend_unpay.setText(getUnPayText(StringUtil.parseStringCodePattern(currency,info.unpayprofit,2)));
		tv_legend_paid.setText(getPaidText(StringUtil.parseStringCodePattern(currency,info.payprofit, 2)));
		tv_summarize.setText(getSummarizeText(info));
		// 业绩基准型不显示温馨提示信息
		tv_reminder
				.setVisibility(isShowReminderView ? View.VISIBLE : View.GONE);
	}

	/**
	 * 获取已到账收益显示内容
	 * 
	 * @param paidProfit
	 *            已到账收益
	 * @return
	 */
	private CharSequence getPaidText(String paidProfit) {
		String paidString = getResources().getString(R.string.bocinvt_paid);
//		String unit = getResources().getString(R.string.bocinvt_unit);
		String unit = BocInvestControl.map_productCurCode_toStr2.get(referProfitInfo.procur);
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(paidString);
		builder.append(":");
		int startIndex = builder.length();
		builder.append(paidProfit == null ? "" : paidProfit);
		int endIndex = builder.length();
		if (startIndex != endIndex)
			builder.setSpan(new ForegroundColorSpan(Color.RED), startIndex,
					endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		builder.append(unit);
		return builder;
	}

	/**
	 * 获取为到到账收益显示内容，
	 * 
	 * @param unpay
	 *            未到账收益
	 * @return
	 */
	private CharSequence getUnPayText(String unpay) {
		String s = getResources().getString(R.string.bocinvt_unpay);
//		String unit = getResources().getString(R.string.bocinvt_unit);
		String unit = BocInvestControl.map_productCurCode_toStr2.get(referProfitInfo.procur);
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(s);
		builder.append(":");
		int startIndex = builder.length();
		builder.append(unpay == null ? "" : unpay);
		int endIndex = builder.length();
		if (startIndex != endIndex)
			builder.setSpan(new ForegroundColorSpan(Color.RED), startIndex,
					endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		builder.append(unit);
		return builder;
	}

	/**
	 * 生成需要显示的概述信息
	 * 
	 * @return
	 */
	private CharSequence getSummarizeText(BaseReferProfitInfo info) {
		String s1 = getResources().getString(R.string.bocinvt_profit_summarize_01);
		String s2 = getResources().getString(R.string.bocinvt_profit_summarize_02);
		String s3 = getResources().getString(R.string.bocinvt_profit_summarize_03);
//		String s4 = getResources().getString(R.string.bocinvt_profit_summarize_04);
		String s5 = getResources().getString(R.string.bocinvt_profit_summarize_05);
//		String s6 = getResources().getString(R.string.bocinvt_profit_summarize_06);
		String s7 = getResources().getString(R.string.bocinvt_profit_summarize_07);
		String s8 = getResources().getString(R.string.bocinvt_profit_summarize_08);
		SpannableStringBuilder builder = new SpannableStringBuilder();
		if (info instanceof ReferenceProfitForCashInfo) {
			ReferenceProfitForCashInfo cashInfo = (ReferenceProfitForCashInfo) info;
			String currency = cashInfo.procur;
			String profit = StringUtil.parseStringCodePattern(currency,cashInfo.profit, 2);
			String totalprofit = StringUtil.parseStringCodePattern(currency,cashInfo.totalprofit, 2);
			if(isDayAndMonth){
				builder.append(s1);
				builder.append(s7);
				String inedateLastDay = QueryDateUtils.getFincLastDay(cashInfo.intedate);
				builder.append(cashInfo.intsdate);
//				if(QueryDateUtils.compareDate(cashInfo.intsdate, inedateLastDay)
//						&& !inedateLastDay.equals(cashInfo.intsdate)){
//					builder.append("-");
//					builder.append(inedateLastDay);
//				}
				if(!inedateLastDay.equals(cashInfo.intsdate)){
					builder.append("-");
					builder.append(inedateLastDay);
				}
				builder.append(s2);
				builder.append(cashInfo.prodName);
				builder.append(s3);
			}else{
				builder.append(s1);
				builder.append(cashInfo.intedate);
				builder.append(",您持有的");
				builder.append(StringUtil.parseStringPattern(productInfo.holdingQuantity,2));
				builder.append("份额");
				builder.append(cashInfo.prodName);
				builder.append(s8);
			}
//			builder.append(s1);
//			builder.append(isDayAndMonth ? s7 : "");
//			builder.append(cashInfo.intsdate);
//			builder.append(isDayAndMonth ? "-" : s6);
//			builder.append(cashInfo.intedate);
//			builder.append(s2);
//			builder.append(cashInfo.prodName);
//			builder.append(isDayAndMonth ? "" : s6);
//			builder.append(s3);
			int startIndex1 = builder.length();
			builder.append(profit);
			int endIndex1 = builder.length();
			builder.append(BocInvestControl.map_productCurCode_toStr2.get(referProfitInfo.procur));
			builder.append(/*isDayAndMonth ? s4 : */"，总收益为");
			int startIndex2 = builder.length();
			builder.append(totalprofit);
			int endIndex2 = builder.length();
			builder.setSpan(new ForegroundColorSpan(Color.RED), startIndex1,
					endIndex1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			builder.setSpan(new ForegroundColorSpan(Color.RED), startIndex2,
				endIndex2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			builder.append(BocInvestControl.map_productCurCode_toStr2.get(referProfitInfo.procur));
			builder.append(s5);
		}
		return builder;
	}
	
	/***
	 * 生成需要显示的概述信息
	 * 
	 * @param info
	 * @return
	 */
//	private CharSequence generateSummarize(BaseReferProfitInfo info) {
//		String summarize = getResources().getString(
//				R.string.bocinvt_profit_summarize_for_multiplying);
//		if (info instanceof ReferenceProfitForCashInfo) {
//			ReferenceProfitForCashInfo cashInfo = (ReferenceProfitForCashInfo) info;
//			String currency = cashInfo.procur;
//			String profit = StringUtil.parseStringCodePattern(currency,cashInfo.profit, 2);
//			String totalprofit = StringUtil.parseStringCodePattern(currency,cashInfo.totalprofit, 2);
//			summarize = String.format(summarize, cashInfo.intedate,cashInfo.prodName,profit,totalprofit);
//		}
//
//		return summarize;
//	}

	private void initView(View view) {
		cake_chart = (CakeChartView) view.findViewById(R.id.cake_chart);
//		cake_chart.setDataAndColor(new int[] { 102, 89 },
//				new int[] { getResources().getColor(R.color.blue),
//						getResources().getColor(R.color.red) });
		tv_legend_unpay = (TextView) view.findViewById(R.id.tv_legend_unpay);
		tv_legend_paid = (TextView) view.findViewById(R.id.tv_legend_paid);
		tv_summarize = (TextView) view.findViewById(R.id.tv_summarize);
		tv_reminder = (TextView) view.findViewById(R.id.tv_reminder);
	}

	/**
	 * 获取图例位图
	 * 
	 * @param color
	 * @return 一个纯色的正方形图片
	 */
	private BitmapDrawable getBitmap(int width, int color) {

		Bitmap bit = Bitmap.createBitmap(width, width, Config.ARGB_8888);
		Canvas canvas = new Canvas(bit);
		canvas.drawColor(color);
		canvas.save();
		BitmapDrawable d = new BitmapDrawable(getResources(), bit);
		d.setBounds(0, 0, bit.getWidth(), bit.getHeight());
		return d;
	}

}
