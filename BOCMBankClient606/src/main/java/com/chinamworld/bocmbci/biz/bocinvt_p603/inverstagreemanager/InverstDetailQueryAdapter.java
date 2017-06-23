/**
 * 代码优化降低方法数 屏蔽自定义适配器 采用通用的适配器接口来实现 
 * 此代码保留，请勿删除！
 * 联龙博通  林耀龙   2016-4-8 
 **/

//package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;
//
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.chinamworld.bocmbci.bii.constant.BocInvt;
//import com.chinamworld.bocmbci.constant.LocalData;
//import com.chinamworld.bocmbci.utils.PopupWindowUtils;
//import com.chinamworld.bocmbci.R;
//
///**
// * 投资明细查询适配器
// * 
// * @author niuchf
// * 
// */
//public class InverstDetailQueryAdapter extends BaseAdapter {
//	/** 查询结果列表信息 */
//	private List<Map<String, Object>> inverstDetailQueryList;
//	private Context context;
//
//	public InverstDetailQueryAdapter(Context context,
//			List<Map<String, Object>> inverstDetailQueryList) {
//		this.context = context;
//		this.inverstDetailQueryList = inverstDetailQueryList;
//	}
//
//	@Override
//	public int getCount() {
//		return inverstDetailQueryList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return inverstDetailQueryList.get(position);
//
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		ViewHolder viewHolder = new ViewHolder();
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.bocinvt_hisproduct_list_item, null);
//			viewHolder.tv_date = (TextView) convertView
//					.findViewById(R.id.boci_product_name);
//			viewHolder.tv_type = (TextView) convertView
//					.findViewById(R.id.boci_yearlyRR);
//			viewHolder.tv_status = (TextView) convertView
//					.findViewById(R.id.boci_timeLimit);
//			PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//					viewHolder.tv_date);
//			PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//					viewHolder.tv_type);
//			PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//					viewHolder.tv_status);
//			viewHolder.iv_go = (ImageView) convertView
//					.findViewById(R.id.boci_gotoDetail);
//			viewHolder.iv_go.setVisibility(View.VISIBLE);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//
//		// 赋值操作
//		viewHolder.tv_date.setText(String.valueOf(inverstDetailQueryList.get(position)
//				.get(BocInvt.BOCINVT_TDSDATE_RES)));
//		viewHolder.tv_type.setText(LocalData.inverstTradeTypeStr.get(String.valueOf(inverstDetailQueryList
//				.get(position).get(BocInvt.BOCINVT_TDSTYPE_RES))));
//		String state = (String) inverstDetailQueryList.get(position).get(
//				BocInvt.BOCINVT_TDSSTATE_RES);
//		viewHolder.tv_status.setText(LocalData.fundTradeStateStr.get(state));	
//		return convertView;
//	}
//
//	public List<Map<String, Object>> getInverstDetailQueryList() {
//		return inverstDetailQueryList;
//	}
//
//	public void setInverstDetailQueryList(
//			List<Map<String, Object>> inverstDetailQueryList) {
//		this.inverstDetailQueryList = inverstDetailQueryList;
//		notifyDataSetChanged();
//	}
//
//	private class ViewHolder {
//		/** 交易日期 */
//		public TextView tv_date;
//		/** 交易类型 */
//		public TextView tv_type;
//		/** 状态 */
//		public TextView tv_status;
//		public ImageView iv_go;
//	}
//
//}
