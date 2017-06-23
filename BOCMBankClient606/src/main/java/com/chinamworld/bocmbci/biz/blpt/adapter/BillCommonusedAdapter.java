package com.chinamworld.bocmbci.biz.blpt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
/**
 * 常用缴费项列表适配器
 * @author panwe
 *
 */
public class BillCommonusedAdapter extends BaseAdapter{

	private OnItemClickListener mOnClickListener;
	private Context mContext;
	private List<Map<String, Object>> mList;
	private boolean mIsdelete;
	
	public BillCommonusedAdapter(Context cn,List<Map<String, Object>> list,boolean isdelete){
		this.mContext = cn;
		this.mList = list;
		this.mIsdelete = isdelete;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mList != null && mList.size() > 0) {
			return mList.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mList != null && mList.size() > 0) {
			return mList.get(position);
		}else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.blpt_main_item, null);
			h.tvPtionName = (TextView)convertView.findViewById(R.id.tv_company);
			h.tvDisName = (TextView)convertView.findViewById(R.id.tv_dispname);
			h.imBtn = (ImageView)convertView.findViewById(R.id.btn_goitem);
			h.imBtnDelete = (ImageView)convertView.findViewById(R.id.btn_gocancelrelation);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		Map<String, Object> map = mList.get(position);
		h.tvPtionName.setText((String)(map.get(Blpt.BILL_COMMON_DISPNAME)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
				h.tvPtionName);
		h.tvDisName.setVisibility(View.GONE);
		if (mIsdelete) {
			h.imBtnDelete.setClickable(true);
			h.imBtnDelete.setVisibility(View.VISIBLE);
			h.imBtn.setVisibility(View.GONE);
		}else{
			h.imBtnDelete.setClickable(false);
			h.imBtn.setVisibility(View.VISIBLE);
			h.imBtnDelete.setVisibility(View.GONE);
		}
		h.imBtnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mOnClickListener != null ) {
					mOnClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		return convertView;
	}
	
	public class ViewHodler {
		public TextView tvPtionName;
		public TextView tvDisName;
		public ImageView imBtnDelete;
		public ImageView imBtn;
	}
	
	public void setImageViewClick (OnItemClickListener onclick){
		this.mOnClickListener = onclick;
	}

}
