package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

/*
 * zxj
 * 缴费项目的adapter
 */
public class PaymentProjectAdapter extends BaseAdapter{
	private Context context;
	protected ArrayList<HashMap<String, Object>> mList;
	
	public PaymentProjectAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
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
			convertView = LinearLayout.inflate(context, R.layout.plps_grid_all_item, null);
			holder.imageView = (ImageView)convertView.findViewById(R.id.image_item);
			holder.textView = (TextView)convertView.findViewById(R.id.text_item);
			convertView.setTag(holder);
		}else {
			holder = (viewHolder)convertView.getTag();
		}
		int iPosition = Integer.valueOf(mList.get(position).get("image").toString());
//		int tPosition = Integer.valueOf(mList.get(position).get("text").toString());
		if(iPosition != -1){
			holder.imageView.setImageDrawable(context.getResources().getDrawable(iPosition));
		}else {
			holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.baise));
		}
		String text = mList.get(position).get("text").toString();
		if(text.length()>=7){
			holder.textView.setSingleLine(true);
			holder.textView.setEllipsize(TruncateAt.MARQUEE);
			holder.textView.setMarqueeRepeatLimit(-1);
		}else {
			holder.textView.setSingleLine(false);
			holder.textView.setLines(2);
		}
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
