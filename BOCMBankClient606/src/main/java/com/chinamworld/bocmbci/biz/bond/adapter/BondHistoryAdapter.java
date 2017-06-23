package com.chinamworld.bocmbci.biz.bond.adapter;
/**
 * 债券历史交易记录查询
 */
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

public class BondHistoryAdapter extends BaseAdapter {
	private List<Map<String,Object>> mList;
	private Context context;
	public BondHistoryAdapter(Context context,List<Map<String,Object>> list) {
		this.context=context;
		this.mList=list;
	}
	@Override
	public int getCount() {
		if(mList!=null&&mList.size()>0){
			return mList.size();
		}else{
			return 0;
		}
		
	}

	@Override
	public Object getItem(int position) {
		if (mList != null && mList.size() > 0) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.bond_historytran_item, null);
			vHolder=new ViewHolder();
			vHolder.tvBondDate=(TextView) convertView.findViewById(R.id.tv_tran_date);
			vHolder.tvBondName=(TextView) convertView.findViewById(R.id.tv_bond_name);
			vHolder.tvBondType=(TextView) convertView.findViewById(R.id.tv_bond_type);
			convertView.setTag(vHolder);
		}else{
			vHolder=(ViewHolder) convertView.getTag();
		}
		vHolder.tvBondDate.setText((String) ((Map<String, Object>)getItem(position)).get(Bond.RE_HISTORYTRAN_QUERY_TRANDATE));
		vHolder.tvBondName.setText((String) ((Map<String, Object>)getItem(position)).get(Bond.BOND_SHORTNAME));
		vHolder.tvBondName.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vHolder.tvBondName);
		vHolder.tvBondType.setText((String) ((Map<String, Object>)getItem(position)).get(Bond.RE_HISTORYTRAN_QUERY_TRANTYPE));
		return convertView;
	}
	
	class ViewHolder{
		TextView tvBondDate;
		TextView tvBondName;
		TextView tvBondType;
	}
}
