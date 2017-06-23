package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 本地缴费项目弹出框
 * @author zxj
 *
 */
public class PaymentDialogAdapter extends BaseAdapter{
	private Context context;
	protected ArrayList<HashMap<String, Object>> mList;
	
	public PaymentDialogAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		this.context = context;
		this.mList = list;
	}
	@Override
	public int getCount() {
		if(StringUtil.isNullOrEmpty(mList)){
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if(StringUtil.isNullOrEmpty(mList)){
			return null;
		}	
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder holder;
		if(convertView == null){
			holder = new viewHolder();
			convertView = LinearLayout.inflate(context, R.layout.plps_pension_grid_item, null);
			holder.imageView = (ImageView)convertView.findViewById(R.id.image_item);
			holder.textView = (TextView)convertView.findViewById(R.id.text_item);
			convertView.setTag(holder);
		}else {
			holder = (viewHolder)convertView.getTag();
		}
		int iPosition = Integer.valueOf(mList.get(position).get("image").toString());
//		int tPosition = Integer.valueOf(mList.get(position).get("text").toString());
		holder.imageView.setImageDrawable(context.getResources().getDrawable(iPosition));
//		holder.textView.setText(context.getResources().getText(tPosition));
		holder.textView.setText(mList.get(position).get("text").toString());
		holder.textView.setGravity(Gravity.CENTER);
		return convertView;
	}
	public class viewHolder{
		public ImageView imageView;
		public TextView textView;
	}
}
