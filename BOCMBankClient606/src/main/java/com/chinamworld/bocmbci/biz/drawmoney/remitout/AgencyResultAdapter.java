package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.utils.StringUtil;


/**
 * @ClassName: AgencyResultAdapter
 * @Description: 代理点查询结果
 * @author JiangWei
 * @date 2013-8-1 下午4:58:18
 */
public class AgencyResultAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> mList;

	public AgencyResultAdapter(Context context, List<Map<String, Object>> list) {
		mContext = context;
		mList = list;
	}
	
	public void setData(List<Map<String, Object>> list){
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.agency_result_item, null);
			viewHolder = new ViewHolder();
			viewHolder.leftStr = (TextView) convertView
					.findViewById(R.id.left_text);
			viewHolder.rightStr = (TextView) convertView
					.findViewById(R.id.right_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String addressStr = (String)mList.get(position).get(DrawMoney.AGENT_ADDRESS);
		String nameStr = (String)mList.get(position).get(DrawMoney.AGENT_NAME);
		viewHolder.rightStr.setText(StringUtil.isNullOrEmpty(addressStr) ? "-" : addressStr);
		viewHolder.leftStr.setText(StringUtil.isNullOrEmpty(nameStr) ? "-" : nameStr);
		return convertView;
	}

	class ViewHolder {
		TextView leftStr;
		TextView rightStr;
	}

}
