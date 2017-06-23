//package com.chinamworld.bocmbci.biz.crcd.adapter;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.biz.crcd.mycrcd.CardEntity;
//import com.chinamworld.bocmbci.utils.StringUtil;
//
//public class MasterAndSupplAdapter  extends BaseAdapter{
//	private List<CardEntity> mList;
//	private Context mContext;
//	
//	
//	public MasterAndSupplAdapter(Context context,
//			List<CardEntity> CardEntityList) {
//		this.mContext = context;
//		this.mList = CardEntityList;
//		
//	}
//	
//	@Override
//	public int getCount() {
//		if (!StringUtil.isNullOrEmpty(mList)) {
//			return mList.size();
//		}
//		return 0;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		if (!StringUtil.isNullOrEmpty(mList)) {
//			return mList.get(position);
//		}
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHodler h;
//
//		if (convertView == null) {
//			h = new ViewHodler();
//			convertView = View.inflate(mContext, R.layout.card_masterandsuppl_item,null);			
//			h.card_bg= (LinearLayout) convertView.findViewById(R.id.card_bg);
//			h.num= (RelativeLayout) convertView.findViewById(R.id.num);
//			h.name= (RelativeLayout) convertView.findViewById(R.id.name);
//			h.card_num= (TextView) convertView.findViewById(R.id.card_num);
//			h.card_numvalue= (TextView) convertView.findViewById(R.id.card_numvalue);
//			h.card_name= (TextView) convertView.findViewById(R.id.card_name);
//			h.card_namevalue= (TextView) convertView.findViewById(R.id.card_namevalue);
//			
//			convertView.setTag(h);
//		} else {
//			h = (ViewHodler) convertView.getTag();
//		}
//		
//		CardEntity cardentity = mList.get(position);
//		
//		if(cardentity.getMasterorsuppl().equals("1")){
//			// 显示组卡信息
//			h.card_bg.setBackgroundResource(R.color.outlay_line);
//			h.num.setVisibility(View.VISIBLE);
//			h.name.setVisibility(View.GONE);
//			h.card_num.setText(mContext.getResources().getString(R.string.mycrcd_carFlag_zhuka1));
//			h.card_numvalue.setText(StringUtil.getForSixForString(cardentity.getMastercrcdNum()));
//			
//		}else// (cardentity.getMasterorsuppl().equals("2"))
//		{
//			h.card_bg.setBackgroundResource(R.color.white);
//			h.name.setVisibility(View.VISIBLE);
//			h.card_num.setText(mContext.getResources().getString(R.string.mycrcd_fushuka_num));
//			h.card_numvalue.setText(StringUtil.getForSixForString(cardentity.getSubcrcdNum()));
//			h.card_name.setText(mContext.getResources().getString(R.string.mycrcd_fushuka_card_name));
//			h.card_namevalue.setText(cardentity.getSubcrcdname());
//			
//		}
//		
//		return convertView;
//	}
//	public class ViewHodler {
//		public  LinearLayout card_bg;
//		public  RelativeLayout num;// 卡号布局
//		public  RelativeLayout name;	//	
//		public TextView card_num;// 卡号
//		public TextView card_numvalue;// 卡号值
//		public TextView card_name;// 账号名称
//		public TextView card_namevalue;//账号名称 值
//		
//	}
//}
