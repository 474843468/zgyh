//package com.chinamworld.bocmbci.biz.plps.adapter;
//
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.bii.constant.Plps;
//import com.chinamworld.bocmbci.utils.StringUtil;
///**
// * 养老金账户ListView适配器
// * @author panwe
// *
// */
//public class AnnuityAcctAdapter extends BaseAdapter{
//	private Context mContext;
//	private List<Map<String, Object>> mList;
//	
//	public AnnuityAcctAdapter(Context c,List<Map<String, Object>> list){
//		this.mContext = c;
//		this.mList = list;
//	}
//
//	@Override
//	public int getCount() {
//		if (StringUtil.isNullOrEmpty(mList)) {
//			return 0;
//		}
//		return mList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		if (StringUtil.isNullOrEmpty(mList)) {
//			return null;
//		}
//		return mList.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHodler h;
//		if (convertView == null) {
//			h = new ViewHodler();
//			convertView = LinearLayout.inflate(mContext, R.layout.plps_annuity_acct_tran_item, null);
//			h.accountNo = (TextView) convertView.findViewById(R.id.userid);
//			h.accountName = (TextView) convertView.findViewById(R.id.username);
//			h.investCompoundingName = (TextView) convertView.findViewById(R.id.investcompoundingname);
//			h.amount = (TextView) convertView.findViewById(R.id.amount);
//			h.unitValue = (TextView) convertView.findViewById(R.id.unitvalue);
//			h.purityDate = (TextView) convertView.findViewById(R.id.puritydate);
//			h.worth = (TextView) convertView.findViewById(R.id.worth);
//			convertView.setTag(h);
//		}else{
//			h = (ViewHodler) convertView.getTag();
//		}
//		final Map<String, Object> map = mList.get(position);
//		if(!StringUtil.isNullOrEmpty(map.get(Plps.ACCOUNTNO))){
//			h.accountNo.setText((String)map.get(Plps.ACCOUNTNO));
//		}
//		if(!StringUtil.isNullOrEmpty(map.get(Plps.ACCOUNTNAME))){
//			h.accountName.setText((String)map.get(Plps.ACCOUNTNAME));
//		}
//		if(!StringUtil.isNullOrEmpty(map.get(Plps.INVESTCOMPOUNDINGNAME))){
//			h.investCompoundingName.setText((String)map.get(Plps.INVESTCOMPOUNDINGNAME));
//		}
//		if (!StringUtil.isNullOrEmpty(map.get(Plps.AMOUNT))) {
//			String ammountNumber = StringUtil.parseStringPattern(
//					(String) map.get(Plps.AMOUNT), 2);
//			if(ammountNumber.equals("0.00")){
//				h.amount.setText("-");
//			}else {
//				h.amount.setText(ammountNumber);
//			}
//		}
//		if(!StringUtil.isNullOrEmpty(map.get(Plps.UNITVALUE))){
//			String unitValueNumber = StringUtil.parseStringPattern((String)map.get(Plps.UNITVALUE), 0);
//			h.unitValue.setText(unitValueNumber);
//		}
//		if(!StringUtil.isNullOrEmpty(map.get(Plps.PURITYDATE))){
//			h.purityDate.setText((String)map.get(Plps.PURITYDATE));
//		}
//		if(!StringUtil.isNullOrEmpty(map.get(Plps.WORTH))){
//			h.worth.setText((String)map.get(Plps.WORTH));
//		}
//		return convertView;
//	}
//	
//	public void setData(List<Map<String, Object>> list) {
//		this.mList = list;
//		notifyDataSetChanged();
//	}
//	
//	public class ViewHodler {
//		public TextView accountNo;
//		public TextView accountName;
//		public TextView investCompoundingName;
//		public TextView amount;
//		public TextView unitValue;
//		public TextView purityDate;
//		public TextView worth;
//	}
//}
