package com.chinamworld.bocmbci.biz.remittance.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 汇款记录查询列表适配器
 * 
 * @author Zhi
 */
public class RecordQueryAdapter extends BaseAdapter{

	/** 上下文 */
	private Context context;
	/** 数据源 */
	private List<Map<String, Object>> mList;
	
	public RecordQueryAdapter(Context c, List<Map<String, Object>> list) {
		context = c;
		mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.finc_listheader, null);
			vh.imgv = (ImageView) arg1.findViewById(R.id.list_header_right);
			vh.tv1 = (TextView) arg1.findViewById(R.id.list_header_tv1);
			vh.tv2 = (TextView) arg1.findViewById(R.id.list_header_tv2);
			((TextView) arg1.findViewById(R.id.list_header_tv2)).setTextColor(context.getResources().getColor(R.color.red));
			vh.tv3 = (TextView) arg1.findViewById(R.id.list_header_tv3);
			vh.divider = (TextView) arg1.findViewById(R.id.divider);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		Map<String, Object> tempMap = (Map<String, Object>) getItem(arg0);
		vh.imgv.setVisibility(View.VISIBLE);
		vh.tv1.setText(StringUtil.valueOf1((String)tempMap.get(Remittance.PAYMENTDATE)));
		vh.tv2.setText(StringUtil.valueOf1(
				StringUtil.parseStringCodePattern((String) tempMap.get(Remittance.FEECUR), (String)tempMap.get(Remittance.AMOUNT), 2)));
		vh.tv3.setText(StringUtil.valueOf1((String)tempMap.get(Remittance.PAYEEACCOUNTNAME)));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tv1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tv2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, vh.tv3);
		
		vh.divider.setVisibility(View.INVISIBLE);
		return arg1;
	}
	
	private class ViewHolder {
		ImageView imgv;
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView divider;
	}
	
	public void setData(List<Map<String, Object>> list) {
		mList = list;
		notifyDataSetChanged();
	}
}
