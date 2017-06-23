package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

/*
 * 常用缴费项目adapter
 * zxj
 */
public class PaymentCommonServiceAdapter extends BaseAdapter{
	private Context context;
	protected ArrayList<HashMap<String, Object>> mList;
	/**删除常用缴费项目*/
	private OnItemClickListener onbanklistCancelRelationClickListener;
	//删除标识
	  private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示

	
	public PaymentCommonServiceAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		this.context = context;
		this.mList = list;
		
	}
	  public void setIsShowDelete(boolean isShowDelete){
		   this.isShowDelete=isShowDelete;
		   notifyDataSetChanged();
		  }
	@Override
	public int getCount() {
		if(StringUtil.isNullOrEmpty(mList)){
			return 0;
		}
		return mList.size();
	}

	public OnItemClickListener getOnbanklistCancelRelationClickListener() {
		return onbanklistCancelRelationClickListener;
	}
	public void setOnbanklistCancelRelationClickListener(
			OnItemClickListener onbanklistCancelRelationClickListener) {
		this.onbanklistCancelRelationClickListener = onbanklistCancelRelationClickListener;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder holder;
		if(convertView == null){
			holder = new viewHolder();
			convertView = LinearLayout.inflate(context, R.layout.plps_gride_item, null);
			holder.imageView = (ImageView)convertView.findViewById(R.id.image_item);
			holder.textView = (TextView)convertView.findViewById(R.id.text_item);
			holder.cancleImageView = (ImageView)convertView.findViewById(R.id.crcd_btn_gocancelrelation);
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
//		holder.textView.setText(context.getResources().getText(tPosition));
		String text = mList.get(position).get("text").toString();
		if(text.length()>=9){
			holder.textView.setSingleLine(true);
			holder.textView.setEllipsize(TruncateAt.MARQUEE);
			holder.textView.setMarqueeRepeatLimit(-1);
//			PopupWindowUtils.getInstance().setOnShowAllTextListenert(context, holder.textView);
		}else {
			holder.textView.setSingleLine(false);
			holder.textView.setLines(2);
		}
		holder.textView.setText(text);
		holder.textView.setGravity(Gravity.CENTER);
		holder.cancleImageView.setVisibility(isShowDelete?View.VISIBLE:View.GONE);//设置删除按钮是否显示
		Object isOther = mList.get(position).get("Other");
		if(isOther != null &&  Boolean.parseBoolean(isOther.toString()) == true){
			holder.cancleImageView.setVisibility(View.GONE);
		}

//		if(i==1){
//			holder.cancleImageView.setVisibility(View.GONE);
//		}else if (i==2) {
//			holder.cancleImageView.setVisibility(View.VISIBLE);
//		}
//		holder.imageView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (onbanklistCancelRelationClickListener != null) {
//					onbanklistCancelRelationClickListener.onItemClick(null, v, position, position);
//				}
//			}
//		});
		holder.cancleImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onbanklistCancelRelationClickListener != null) {
					onbanklistCancelRelationClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		return convertView;
	}
	public class viewHolder{
		public ImageView imageView;
		public TextView textView;
		public ImageView cancleImageView;
	}

}
