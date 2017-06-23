/****
 * 屏蔽自定义适配器  使用适配器公共接口实现  降低方法数
 * 请勿删除  林耀龙  4/19
 */

//package com.chinamworld.bocmbci.biz.bocinvt.adapter;
//
//import java.util.List;
//import java.util.Map;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.bii.constant.BocInvt;
//import com.chinamworld.bocmbci.constant.LocalData;
//import com.chinamworld.bocmbci.utils.PopupWindowUtils;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
///**
// * 投资协议管理 失效协议结果列表数据适配器
// * @author linyl
// *
// */
//public class MyInvestAgreeQueryAdapter extends BaseAdapter {
//	/**上下文**/
//	private Context context;
//	/**数据源**/
//	private List<Map<String,Object>> data;
//	/**
//	 * 适配器构造方法
//	 * @param context  上下文
//	 * @param data  数据源
//	 */
//	public MyInvestAgreeQueryAdapter(Context context,List<Map<String,Object>>data){
//		this.context = context;
//		this.data = data;
//	}
//
//	@Override
//	public int getCount() {
//		return data.size();
//	}
//
//	@Override
//	public Map<String,Object> getItem(int position) {
//		return data.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		if(convertView == null){
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.boc_invest_agrquery_list_item, null);
//		}
//		TextView mAgrName = (TextView) convertView.findViewById(R.id.boc_invest_agrname_tv);
//		TextView mExecPro = (TextView) convertView.findViewById(R.id.boc_invest_execpro_tv);
//		TextView mAgrType = (TextView) convertView.findViewById(R.id.boc_invest_agrtype_tv);
//		ImageView mGotoDetail = (ImageView) convertView.findViewById(R.id.boc_invest_gotoDetail);
//		//添加文本的点击事件
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, mAgrName);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, mExecPro);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, mAgrType);
//		//取值
//		mAgrName.setText(String.valueOf(getItem(position).get(BocInvt.BOCINVT_CAPACITYQUERY_AGRNAE_RES)));
//		mExecPro.setText(String.valueOf(getItem(position).get(BocInvt.BOCINVT_CAPACITYQUERY_PRONAME_RES)));
//		mAgrType.setText(LocalData.bocInvestAgrTypeRes.get(String.valueOf(getItem(position).get(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_RES))));
//		mGotoDetail.setVisibility(View.VISIBLE);
//		return convertView;
//	}
//
//}
