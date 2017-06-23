package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
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
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitDetailItemInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitForCashInfo;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 参考收益的收益明细， 显示日记月累型，业绩基准型产品的收益明细
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForDetailFragment extends BaseHttpFragment {

	private static final String KEY_PRODUCT_INFO = "product_info";
	private static final String KEY_REFERENCE_INFO = "reference_info";
	private static final String KEY_SHOW_REMINDER = "show_reminder";
	private ListView list;

	private ReferenceProfitDetailAdapter adapter;
	private BOCProductForHoldingInfo productInfo;
	private List<Map<String, Object>> resultList;
	private ArrayList<ReferenceProfitDetailItemInfo> infos;
	private int loadNumber = 0;
	
	private RelativeLayout load_more;
	private Button btn_load_more;
	/** 收益金额*/
	private TextView tv_bocinvt_profit_detail_profit;
	private ReferenceProfitForCashInfo referProfitInfo;
	private static String currency;
	/** 是否为日积月累*/
	private static boolean isShowReminder;
	
	public static ReferenceProfitForDetailFragment newInstance(
			BOCProductForHoldingInfo info,ReferenceProfitForCashInfo referProfitInfo,boolean isShowReminderView) {
		ReferenceProfitForDetailFragment instance = new ReferenceProfitForDetailFragment();
		Bundle data = new Bundle();
		data.putSerializable(KEY_PRODUCT_INFO, info);
		data.putSerializable(KEY_REFERENCE_INFO, referProfitInfo);
		data.putBoolean(KEY_SHOW_REMINDER, isShowReminderView);
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bocinvt_reference_profit_detail,
				container, false);
		Bundle data = getArguments();
		load_more = (RelativeLayout) inflater.inflate(R.layout.acc_load_more, null);
		btn_load_more = (Button) load_more.findViewById(R.id.btn_load_more);
		btn_load_more.setBackgroundResource(R.color.transparent_00);
		btn_load_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestDetailQueryMore();
			}
		});
		infos = new ArrayList<ReferenceProfitDetailItemInfo>();
		productInfo = (BOCProductForHoldingInfo) data
				.getSerializable(KEY_PRODUCT_INFO);
		currency = productInfo.curCode.numberCode;
		referProfitInfo = (ReferenceProfitForCashInfo) data
				.getSerializable(KEY_REFERENCE_INFO);
		isShowReminder = data.getBoolean(KEY_SHOW_REMINDER);
		list = (ListView) view.findViewById(R.id.list);
		list.addHeaderView(initHeaderView());
		adapter = new ReferenceProfitDetailAdapter();
		list.setAdapter(adapter);
		requestProfitDetail(productInfo);
		return view;
	}

	private void requestProfitDetail(BOCProductForHoldingInfo info) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountKey", info.bancAccountKey);
		map.put("productCode", info.prodCode);
		map.put("progressionflag", info.progressionflag);
		map.put("kind", info.productKind);
//		map.put("startDate", info.prodBegin);
//		map.put("endDate", info.prodEnd);
		map.put("pageSize", "10");
		map.put("_refresh", "true");
		map.put("cashRemit", info.cashRemit);
		if(!"0".equals(info.standardPro)){
			map.put("tranSeq", info.tranSeq);
		}
		map.put("currentIndex", adapter.getCount());
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod("PsnXpadReferProfitDetailQuery");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestProfitDetailCallback");
		BaseHttpEngine.showProgressDialog();

	}

	@SuppressWarnings("unchecked")
	public void requestProfitDetailCallback(Object resultObj) {
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
			ArrayList<ReferenceProfitDetailItemInfo> infos = new ArrayList<ReferenceProfitDetailItemInfo>();
			for(Map<String, Object> mapInfo : resultList){
				infos.add(new ReferenceProfitDetailItemInfo(mapInfo));
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
		if(!"0".equals(productInfo.standardPro)){
			map.put("tranSeq", productInfo.tranSeq);
		}
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
			ArrayList<ReferenceProfitDetailItemInfo> infos = new ArrayList<ReferenceProfitDetailItemInfo>();
			for(Map<String, Object> mapInfo : resultList){
				infos.add(new ReferenceProfitDetailItemInfo(mapInfo));
			}
			this.infos.addAll(infos);
			adapter.changeData(infos);
		}
	}

	/**
	 * 初始化表头
	 * 
	 * @param inflater
	 * @param container
	 * @return
	 */
	private View initHeaderView() {
		View listHeader = View.inflate(getActivity(),
				R.layout.bocinvt_reference_profit_detail_list_item, null);
		tv_bocinvt_profit_detail_profit = (TextView) listHeader.findViewById(R.id.tv_bocinvt_profit_detail_profit);
		tv_bocinvt_profit_detail_profit.setText("收益金额（"+
		BocInvestControl.map_productCurCode_toStr.get(referProfitInfo.procur)+"）");
		return listHeader;
	}

	public static class ReferenceProfitDetailAdapter extends BaseAdapter {

		private ArrayList<ReferenceProfitDetailItemInfo> details = new ArrayList<ReferenceProfitDetailItemInfo>();

		public void changeData(ArrayList<ReferenceProfitDetailItemInfo> details) {
			this.details.clear();
			this.details.addAll(details);
			notifyDataSetChanged();
		}

		public void addData(ArrayList<ReferenceProfitDetailItemInfo> details) {
			if (details == null || details.size() <= 0) {
				return;
			}
			this.details.addAll(details);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {

			return details.size();
		}

		@Override
		public ReferenceProfitDetailItemInfo getItem(int position) {
			return details.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DedailViewHolder holder;
			if (convertView == null) {
				holder = new DedailViewHolder();
				convertView = holder.getView();
				convertView.setTag(holder);
			} else {
				holder = (DedailViewHolder) convertView.getTag();
			}
			holder.setData(getItem(position));
			return holder.getView();
		}

	}

	public static class DedailViewHolder {

		public View view;
		private TextView tv_bocinvt_profit_detail_compute_cycle;
		private TextView tv_bocinvt_profit_detail_profit;
		private TextView tv_bocinvt_profit_detail_pay_state;
		private TextView tv_bocinvt_profit_intedate;
		public DedailViewHolder() {
			view = View.inflate(BaseDroidApp.context,
					R.layout.bocinvt_reference_profit_detail_list_item, null);
			tv_bocinvt_profit_detail_compute_cycle = (TextView) view
					.findViewById(R.id.tv_bocinvt_profit_detail_compute_cycle);
			tv_bocinvt_profit_detail_profit = (TextView) view
					.findViewById(R.id.tv_bocinvt_profit_detail_profit);
			tv_bocinvt_profit_detail_pay_state = (TextView) view
					.findViewById(R.id.tv_bocinvt_profit_detail_pay_state);
			tv_bocinvt_profit_intedate = (TextView) view
					.findViewById(R.id.tv_bocinvt_profit_intedate);
//			tv_bocinvt_profit_intedate.setVisibility(View.VISIBLE);

		}

		public void setData(ReferenceProfitDetailItemInfo info) {
			String inedateLastDay = QueryDateUtils.getFincLastDay(info.intedate);
			if(QueryDateUtils.compareDate(info.intsdate, inedateLastDay)
					&& !inedateLastDay.equals(info.intsdate)){
				tv_bocinvt_profit_intedate.setVisibility(View.VISIBLE);
				tv_bocinvt_profit_detail_compute_cycle.setText(info.intsdate+"-");
				tv_bocinvt_profit_intedate.setText(inedateLastDay);
			}else{
				tv_bocinvt_profit_detail_compute_cycle.setText(info.intsdate);
			}	
//			if(!isShowReminder){//业绩基准型
//				tv_bocinvt_profit_intedate.setVisibility(View.VISIBLE);
//				tv_bocinvt_profit_detail_compute_cycle.setText(info.intsdate+"-");
//				tv_bocinvt_profit_intedate.setText(info.intedate);
//			}else{
//				if(inedateLastDay.equals(info.intsdate)){
//					tv_bocinvt_profit_detail_compute_cycle.setText(info.intsdate);
//				}else{
//					tv_bocinvt_profit_intedate.setVisibility(View.VISIBLE);
//					tv_bocinvt_profit_detail_compute_cycle.setText(info.intsdate+"-");
//					tv_bocinvt_profit_intedate.setText(inedateLastDay);
//				}
//			}
			tv_bocinvt_profit_detail_profit.setText(StringUtil.parseStringCodePattern(currency,info.payprofit.toString(), 2));
			tv_bocinvt_profit_detail_pay_state.setText(BociDataCenter.payflagMap.get(info.payflag));
		}

		public View getView() {

			return view;
		}
	}
}
