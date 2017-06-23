package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 基金Ta账户 适配器
 * 
 * @author 宁焰红
 * 
 */
public class FundTaListAdapter extends BaseAdapter {
	private final String TAG = "FundTaListAdapter";
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public FundTaListAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
		   convertView = mInflater.inflate(R.layout.finc_query_history_list_listiterm_2,null);
		   holder = new ViewHolder();
		   holder.taCompanyTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv1);
		   holder.taAccTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv2);
		   holder.ifPositionsTv=(TextView) convertView.findViewById(R.id.finc_listiterm_tv3);
		   convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.taCompanyTv.setTag(position);
		holder.taAccTv.setTag(position);
		holder.ifPositionsTv.setTag(position);
		Map<String, Object> map=list.get(position);
		//注册基金公司名称
		String taCompany=StringUtil.valueOf1((String)map.get(Finc.QUERYTAACCDETAILLIST_FUNDREGNAME));
		//Ta 账户
		String taAcc = StringUtil.valueOf1((String)map.get(Finc.QUERYTAACCDETAILLIST_TAACCOUNTNO));
		//是否有持仓
		boolean ifPositions=StringUtil.parseStrToBoolean((String)map.get(Finc.QUERYTAACCDETAILLIST_ISPOSITION));
		//账户状态
		String accState = map.get(Finc.QUERYTAACCDETAILLIST_ACCOUNTSTATUS).toString();
		
		holder.taCompanyTv.setText(taAcc);
		holder.taAccTv.setText(taCompany);
		holder.ifPositionsTv.setText(StringUtil.valueOf1(LocalData.fincTaAccTypeMap.get(accState)));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.taCompanyTv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				holder.taAccTv);
		return convertView;
	}

	/**
	 * 内部类--存放控件
	 */
	public final class ViewHolder {
		/** 注册登记基金公司 ,Ta账户 ,是否持仓  
		 * 
		 * @author modi by fsm 2013-10-24 10:29:52
		 * 根据基金业务评审，字段作相应调整，调整如下
		 * 列表显示字段“注册登记基金公司，TA账户，是否持仓”改成“TA账户，注册公司，账户状态”
		 * 此处xml布局未作调整
		 */
		private TextView taCompanyTv,taAccTv,ifPositionsTv;
		
	}

}
