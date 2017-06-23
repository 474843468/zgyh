package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitDetailForProgressionItemInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitForCashInfo;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 收益累进型产品的参考收益显示
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForProgressionFragment extends BaseHttpFragment {
	private static final String KEY_PRODUCT_INFO = "product_info";
	private static final String KEY_REFERENCE_INFO = "reference_info";
	/** 收益概述 */
	private TextView tv_profit_summarize;
	private ListView list;

	/** 温馨提示 */
	private TextView tv_reminder;
	private RelativeLayout load_more;
	private Button btn_load_more;
	private BOCProductForHoldingInfo productInfo;
	private ReferenceProfitForCashInfo referProfitInfo;
	private ReferenceProfitDetailForProgressionItemAdapter adapter;
	private List<Map<String, Object>> resultList;
	private ArrayList<ReferenceProfitDetailForProgressionItemInfo> infos;
	private int loadNumber = 0;
	private static String currency;
	
	public static ReferenceProfitForProgressionFragment newInstance(
			BOCProductForHoldingInfo productInfo,
			ReferenceProfitForCashInfo referProfitInfo) {
		ReferenceProfitForProgressionFragment instance = new ReferenceProfitForProgressionFragment();
		Bundle data = new Bundle();

		data.putSerializable(KEY_PRODUCT_INFO, productInfo);
		data.putSerializable(KEY_REFERENCE_INFO, referProfitInfo);
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.bocinvt_reference_profit_for_progression, container,
				false);
		initView(view);
		load_more = (RelativeLayout) inflater.inflate(R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setBackgroundResource(R.color.transparent_00);
		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestDetailQueryMore();
			}
		});
		productInfo = (BOCProductForHoldingInfo) getArguments()
				.getSerializable(KEY_PRODUCT_INFO);
		currency = productInfo.curCode.numberCode;
		referProfitInfo = (ReferenceProfitForCashInfo) getArguments()
				.getSerializable(KEY_REFERENCE_INFO);
		setViewContent(referProfitInfo);
		infos = new ArrayList<ReferenceProfitDetailForProgressionItemInfo>();
		return view;
	}

	/**
	 * 设置显示内容
	 * 
	 * @param info
	 */
	private void setViewContent(ReferenceProfitForCashInfo info) {
		tv_profit_summarize.setText(getSummarize(info.intedate, info.prodName,
				StringUtil.parseStringCodePattern(info.procur,info.totalprofit, 2),StringUtil.parseStringCodePattern(info.procur,info.profit, 2)));
//		String reminder = "温馨提示：\n1、收益累进类产品采取“"+BociDataCenter.redeemrule.get(info.redeemrule)+
//				"”原则，保障您收益最大化。\n2、T日赎回，本金"+BociDataCenter.redpayamtmode.get(info.redpayamtmode)
//				+",收益"+BociDataCenter.redpayprofitmode.get(info.redpayprofitmode)+"；到账日若遇节假日顺延至下一工作日。";
		String reminder = "温馨提示：\n1、收益累进类产品采取“后进先出”原则，保障您收益最大化。\n2、T日赎回，本金和收益T+1日到账；到账日若遇节假日顺延至下一工作日。";
		tv_reminder.setText(reminder);
		requestPsnXpadReferProfitDetailQuery();
		// adapter.changeData(infos);
	}

	/**
	 * 获取收益概述
	 * 
	 * @param date
	 *            计息截止日期
	 * @param proName
	 *            产品名称
	 * @param profit
	 *            总收益
	 * @return
	 */
	private CharSequence getSummarize(String date, String proName, String totalprofit,String profit) {
//		String sumarize = getResources().getString(
//				R.string.bocinvt_profit_summarize_for_progression);
//		String format = String.format(sumarize, date, proName, profit);
//		return format;
		String s1 = getResources().getString(R.string.bocinvt_profit_summarize_for_progression01);
		String s2 = getResources().getString(R.string.bocinvt_profit_summarize_for_progression02);
		String s3 = getResources().getString(R.string.bocinvt_profit_summarize_for_progression03);
		String s4 = getResources().getString(R.string.bocinvt_profit_summarize_for_progression04);
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(s1);
		builder.append(date);
		builder.append(s2);
		builder.append(proName);
		builder.append(s3);
		int startIndex = builder.length();
		builder.append(totalprofit);
		int endIndex = builder.length();
		builder.append(BocInvestControl.map_productCurCode_toStr2.get(referProfitInfo.procur));
		builder.append("，当前持仓收益预估为");
		int startIndex2 = builder.length();
		builder.append(profit);
		int endIndex2 = builder.length();
		builder.append(BocInvestControl.map_productCurCode_toStr2.get(referProfitInfo.procur));
		builder.append(s4);
		builder.setSpan(new ForegroundColorSpan(Color.RED), startIndex,
				endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		builder.setSpan(new ForegroundColorSpan(Color.RED), startIndex2,
				endIndex2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return builder;
	}

	private void initView(View view) {
		tv_profit_summarize = (TextView) view
				.findViewById(R.id.tv_profit_summarize);
		list = (ListView) view.findViewById(R.id.list);
		adapter = new ReferenceProfitDetailForProgressionItemAdapter();

		list.setAdapter(adapter);
		tv_reminder = (TextView) view.findViewById(R.id.tv_reminder);
		
	}

	public static class ReferenceProfitDetailForProgressionItemAdapter extends
			BaseAdapter {

		private ArrayList<ReferenceProfitDetailForProgressionItemInfo> infos = new ArrayList<ReferenceProfitDetailForProgressionItemInfo>();

		public void changeData(
				ArrayList<ReferenceProfitDetailForProgressionItemInfo> infos) {
			this.infos.clear();
			if (infos != null)
				this.infos.addAll(infos);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public ReferenceProfitDetailForProgressionItemInfo getItem(int position) {
			return infos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder(BaseDroidApp.context);
				convertView = holder.getView();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.setData(getItem(position));
			return holder.getView();
		}

	}

	public static class ViewHolder {

		/** 持有份额 */
		public LabelTextView ltv_hold_lot;
		/** 起息日期 */
		public LabelTextView ltv_date_of_value;
		/** 已持有天数 */
//		public LabelTextView ltv_hold_days;
		public TextView ltv_hold_days;
		/** 年收益率 */
		public LabelTextView ltv_apy;
		/** 参考收益 */
		public LabelTextView ltv_reference_profit;

		private View view;

		private Context context;
		public ViewHolder(Context context) {
			this.context = context;
			view = View
					.inflate(
							context,
							R.layout.bocinvt_reference_profit_detail_list_item_for_progression,
							null);
			ltv_hold_lot = (LabelTextView) view.findViewById(R.id.ltv_hold_lot);
			ltv_date_of_value = (LabelTextView) view
					.findViewById(R.id.ltv_date_of_value);
//			ltv_hold_days = (LabelTextView) view
//					.findViewById(R.id.ltv_hold_days);
			ltv_hold_days = (TextView) view
					.findViewById(R.id.ltv_hold_days);
			ltv_apy = (LabelTextView) view.findViewById(R.id.ltv_apy);
			ltv_reference_profit = (LabelTextView) view
					.findViewById(R.id.ltv_reference_profit);
		}

		public void setData(ReferenceProfitDetailForProgressionItemInfo info) {
//			ltv_hold_lot.setValueText(StringUtil.parseStringCodePattern(currency,info.balunit.toString(), 2));
			ltv_hold_lot.setValueText(StringUtil.parseStringPattern(info.balunit.toString(), 2));
			ltv_date_of_value.setValueText(info.startdate);
//			ltv_hold_days.setValueText(info.baldays);
			ltv_hold_days.setText(info.baldays);
			final String nextdays = info.nextdays;
			if(!StringUtil.isNullOrEmpty(nextdays)&&!(Integer.valueOf(nextdays) > 7)){
				ltv_hold_days.setTextColor(context.getResources().getColor(
						R.color.blue));
				ltv_hold_days.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				ltv_hold_days.getPaint().setAntiAlias(true);
				ltv_hold_days.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						PopupWindowUtils.getInstance().showAllTextPopUpWindow(context, 
								ltv_hold_days, "再持有"+nextdays+"天就可获得更高收益率！", true);
					}
				});
			}
			ltv_apy.setValueText(StringUtil.parseStringPattern(info.exyield.toString(), 2)+"%");
			ltv_reference_profit.setValueTextColor(TextColor.Red);
			ltv_reference_profit.setValueText(StringUtil.parseStringCodePattern(currency,info.payprofit.toString(), 2));
		}

		public View getView() {
			return view;
		}
	}
	
	/**
	 * 参考收益详情查询 
	 */
	private void requestPsnXpadReferProfitDetailQuery(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountKey", productInfo.bancAccountKey);
		map.put("productCode", productInfo.prodCode);
		map.put("progressionflag", productInfo.progressionflag);
		map.put("kind", productInfo.productKind);
//		map.put("startDate", productInfo.prodBegin);
//		map.put("endDate", productInfo.prodEnd);
		map.put("startDate", "");
		map.put("endDate", "");
		map.put("pageSize", "10");
		map.put("currentIndex", adapter.getCount());
		map.put("_refresh", "true");
		map.put("cashRemit", productInfo.cashRemit);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod("PsnXpadReferProfitDetailQuery");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadReferProfitDetailQueryCallback");
		BaseHttpEngine.showProgressDialog();
	}
	
	/**
	 * 参考收益详情查询回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadReferProfitDetailQueryCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recordNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> listFirst = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(listFirst)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;
		}else{
			resultList = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
			loadNumber = resultList.size();
			if (Integer.valueOf(recordNumber) > 10) {
				list.addFooterView(load_more);
			}
			ArrayList<ReferenceProfitDetailForProgressionItemInfo> infos = new ArrayList<ReferenceProfitDetailForProgressionItemInfo>();
			for(Map<String, Object> mapInfo : resultList){
				infos.add(new ReferenceProfitDetailForProgressionItemInfo(mapInfo));
			}
			this.infos.addAll(infos);
			adapter.changeData(infos);
		}
	}
	/**
	 * 参考收益详情查询 更多
	 */
	private void requestDetailQueryMore(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountKey", productInfo.bancAccountKey);
		map.put("productCode", productInfo.prodCode);
		map.put("progressionflag", productInfo.progressionflag);
		map.put("kind", productInfo.productKind);
//		map.put("startDate", productInfo.prodBegin);
//		map.put("endDate", productInfo.prodEnd);
		map.put("pageSize", "10");
		map.put("_refresh", "false");
		map.put("cashRemit", productInfo.cashRemit);
		map.put("currentIndex", adapter.getCount());
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod("PsnXpadReferProfitDetailQuery");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestDetailQueryMoreCallback");
		BaseHttpEngine.showProgressDialog();
	}
	
	/**
	 * 参考收益详情查询更多回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestDetailQueryMoreCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recordNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> listForMore = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(listForMore)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;
		}else{
			resultList.addAll(listForMore);
			loadNumber = resultList.size();
			if (loadNumber < Integer.valueOf(recordNumber)) {
				
			}else{
				list.removeFooterView(load_more);
			}
			ArrayList<ReferenceProfitDetailForProgressionItemInfo> infos = new ArrayList<ReferenceProfitDetailForProgressionItemInfo>();
			for(Map<String, Object> mapInfo : resultList){
				infos.add(new ReferenceProfitDetailForProgressionItemInfo(mapInfo));
			}
			this.infos.addAll(infos);
			adapter.changeData(infos);
		}
	}
	
}
