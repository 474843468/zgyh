//package com.chinamworld.bocmbci.biz.crcd.adapter;
//
//import java.util.List;
//import java.util.Map;
//
//import com.chinamworld.bocmbci.R;
//import com.chinamworld.bocmbci.bii.constant.Crcd;
//import com.chinamworld.bocmbci.biz.forex.adapter.ForexAccSettingAdapter.ViewHolder;
//import com.chinamworld.bocmbci.constant.LocalData;
//import com.chinamworld.bocmbci.utils.StringUtil;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class CrcdGlobalRMBAccAdapter extends BaseAdapter {
//	/**
//	 * 信用卡设定-全球交易人民币记账功能设置--选择信用卡页面---适配器
//	 */
//	// 默认选中第一个
//	public static int selectedPosition = -1;
//
//	private Context context;
//	private List<Map<String, String>> list;
//
//	public CrcdGlobalRMBAccAdapter(Context context, List<Map<String, String>> list) {
//		super();
//		this.context = context;
//		this.list = list;
//	}
//
//	public void changeDate(List<Map<String, String>> list) {
//		this.list = list;
//		notifyDataSetChanged();
//	}
//
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder;
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(R.layout.forex_acc_setting_item, null);
//			holder = new ViewHolder();
//			holder.forexStyle = (TextView) convertView.findViewById(R.id.forex_acc_type);
//			holder.forexAlias = (TextView) convertView.findViewById(R.id.forex_acc_alias);
//			holder.forexCountNum = (TextView) convertView.findViewById(R.id.forex_acc_accountnumber);
//			holder.img = (ImageView) convertView.findViewById(R.id.imageViewright);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		holder.forexStyle.setTag(position);
//		holder.forexAlias.setTag(position);
//		holder.forexCountNum.setTag(position);
//		holder.img.setTag(position);
//
//		Map<String, String> map = list.get(position);
//		// 选中的颜色
//		if (position == selectedPosition) {
//			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
//			holder.img.setVisibility(View.VISIBLE);
//		} else {
//			convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
//			holder.img.setVisibility(View.GONE);
//		}
//		// 账户类型
//		String accountType = map.get(Crcd.CRCD_ACCOUNTTYPE_RES);
//		String accountTypeTrans = null;
//		if (!StringUtil.isNull(accountType) && LocalData.AccountType.containsKey(accountType)) {
//			accountTypeTrans = LocalData.AccountType.get(accountType);
//		}
//		// 账户别名
//		String nickName = map.get(Crcd.CRCD_NICKNAME_RES);
//		// 账号
//		String accountNumber = map.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
//		String number = null;
//		if (!StringUtil.isNull(accountNumber)) {
//			number = StringUtil.getForSixForString(accountNumber);
//		}
//		holder.forexStyle.setText(accountTypeTrans);
//		holder.forexAlias.setText(nickName);
//		holder.forexCountNum.setText(number);
//
//		return convertView;
//	}
//
//	/**
//	 * 存放控件
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	public final class ViewHolder {
//		/**
//		 * 账户类型
//		 */
//		public TextView forexStyle;
//		/**
//		 * 账户别名
//		 */
//		public TextView forexAlias;
//		/**
//		 * 账户号码
//		 */
//		public TextView forexCountNum;
//
//		public ImageView img;
//	}
//
//}
