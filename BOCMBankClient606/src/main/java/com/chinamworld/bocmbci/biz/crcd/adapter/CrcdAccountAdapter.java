//package com.chinamworld.bocmbci.biz.crcd.adapter;
//
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.bii.constant.Crcd;
//import com.chinamworld.bocmbci.constant.ConstantGloble;
//import com.chinamworld.bocmbci.constant.LocalData;
//import com.chinamworld.bocmbci.utils.StringUtil;
//
///**
// * 信用卡账户信息
// * 
// * @author huangyuchao
// * 
// */
//public class CrcdAccountAdapter extends BaseAdapter {
//
//	private List<Map<String, Object>> bankAccountList;
//
//	private Context context;
//	/** 详情点击事件 */
//	private OnItemClickListener oncrcdDetailListener;
//
//	/** 进入账单明细点击事件 */
//	private OnItemClickListener ontransDetailListener;
//
//	/** 取消账户关联事件 */
//	private OnItemClickListener onbanklistCancelRelationClickListener;
//
//	public OnItemClickListener getOnbanklistCancelRelationClickListener() {
//		return onbanklistCancelRelationClickListener;
//	}
//
//	public void setOnbanklistCancelRelationClickListener(OnItemClickListener onbanklistCancelRelationClickListener) {
//		this.onbanklistCancelRelationClickListener = onbanklistCancelRelationClickListener;
//	}
//
//	/**
//	 * i用来判断是否是取消关联 i=1,正常;i=2,取消关联;i=3,选择账户使用
//	 * */
//	private int i = 0;
//
//	public CrcdAccountAdapter(Context context, List<Map<String, Object>> bankAccountList, int i) {
//		this.context = context;
//		this.bankAccountList = bankAccountList;
//		this.i = i;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//
//		return bankAccountList.size();
//		// return 5;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return bankAccountList.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		CrcdViewHolder holder;
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(R.layout.crcd_myaccount_list_item, null);
//			holder = new CrcdViewHolder();
//			holder.crcd_type_value = (TextView) convertView.findViewById(R.id.crcd_type_value);
//			holder.crcd_account_nickname = (TextView) convertView.findViewById(R.id.crcd_account_nickname);
//			holder.crcd_currencycode_value = (TextView) convertView.findViewById(R.id.crcd_currencycode_value);
//			holder.crcd_account_num = (TextView) convertView.findViewById(R.id.crcd_account_num);
//			holder.crcd_bookbalance_value = (TextView) convertView.findViewById(R.id.crcd_bookbalance_value);
//			holder.ivdetail = (ImageView) convertView.findViewById(R.id.img_crcd_detail);
//			holder.ivgoitem = (ImageView) convertView.findViewById(R.id.crcd_btn_goitem);
//			holder.ivCancel = (ImageView) convertView.findViewById(R.id.crcd_btn_gocancelrelation);
//			holder.img_crcd_currencycode = (ImageView) convertView.findViewById(R.id.img_crcd_currencycode);
//			holder.crcd_currcycode = (TextView) convertView.findViewById(R.id.crcd_currcycode);
//
//			convertView.setTag(holder);
//		} else {
//			holder = (CrcdViewHolder) convertView.getTag();
//		}
//
//		Map<String, Object> map = bankAccountList.get(position);
//		Map<String, Object> mapDetail = (Map<String, Object>) map.get(Crcd.CRCD_DETAILIST);
//
//
////		String accountType = String.valueOf(map.get(Crcd.CRCD_ACCOUNTTYPE_RES));
////		String strAccountType = LocalData.AccountType.get(accountType);
////		holder.crcd_type_value.setText(strAccountType);
//		String cardDescription=String.valueOf(map.get(Crcd.CRCD_CARDDESCRIPTION));
//		
//		if(!StringUtil.isNullOrEmptyCaseNullString(cardDescription)){
//			holder.crcd_type_value.setText(cardDescription);
//		}else{
//			holder.crcd_type_value.setText(ConstantGloble.BOCINVT_DATE_ADD);	
//		}
////		holder.crcd_type_value.setText(cardDescription);
//
//		holder.crcd_account_nickname.setText(String.valueOf(map.get(Crcd.CRCD_NICKNAME_RES)));
//		holder.crcd_currencycode_value.setText(context.getResources().getString(R.string.mycrcd_bill));
//		holder.crcd_account_num.setText(StringUtil.getForSixForString(String.valueOf(map
//				.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));
//
//		String currency = LocalData.Currency.get(String.valueOf(map.get(Crcd.CRCD_CURRENCYCODE)));
//
//		holder.crcd_currcycode.setText(currency);
//		if (!StringUtil.isNullOrEmpty(mapDetail)) {
//			holder.crcd_bookbalance_value.setText(" : " + String.valueOf(mapDetail.get(Crcd.CRCD_TOTALBALANCE)));
//		}
//
//		holder.img_crcd_currencycode.setVisibility(View.GONE);
//
//		if (i == 1) {
//			holder.ivgoitem.setVisibility(View.VISIBLE);
//			holder.ivCancel.setVisibility(View.GONE);
//		} else if (i == 2) {
//			holder.ivgoitem.setVisibility(View.GONE);
//			holder.ivCancel.setVisibility(View.VISIBLE);
//		}
//
//		holder.ivdetail.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (oncrcdDetailListener != null) {
//					oncrcdDetailListener.onItemClick(null, v, position, position);
//				}
//				// Intent it = new Intent(context, MyCrcdDetailActivity.class);
//				// context.startActivity(it);
//			}
//		});
//
//		holder.ivgoitem.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (ontransDetailListener != null) {
//					ontransDetailListener.onItemClick(null, v, position, position);
//				}
//			}
//		});
//
//		holder.ivCancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (onbanklistCancelRelationClickListener != null) {
//					onbanklistCancelRelationClickListener.onItemClick(null, v, position, position);
//				}
//			}
//		});
//
//		return convertView;
//	}
//
//	class CrcdViewHolder {
//		TextView crcd_type_value;
//		TextView crcd_account_nickname;
//		TextView crcd_currencycode_value;
//		TextView crcd_account_num;
//		TextView crcd_bookbalance_value;
//
//		TextView crcd_currcycode;
//
//		ImageView ivdetail;
//		ImageView ivgoitem;
//		ImageView ivCancel;
//
//		ImageView img_crcd_currencycode;
//	}
//
//	public OnItemClickListener getOncrcdDetailListener() {
//		return oncrcdDetailListener;
//	}
//
//	public void setOncrcdDetailListener(OnItemClickListener oncrcdDetailListener) {
//		this.oncrcdDetailListener = oncrcdDetailListener;
//	}
//
//	public OnItemClickListener getOntransDetailListener() {
//		return ontransDetailListener;
//	}
//
//	public void setOntransDetailListener(OnItemClickListener ontransDetailListener) {
//		this.ontransDetailListener = ontransDetailListener;
//	}
//
//}
