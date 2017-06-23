package com.chinamworld.bocmbci.biz.prms.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 贵金属账户列表 
 * @author xyl 
 *
 */
public class PrmsAccsListAdapter extends BaseAdapter{
	
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;
	
	private String selectedAccNum ="";
	
	public PrmsAccsListAdapter(Context context,List<Map<String, String>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list=list;
	}	
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
			 convertView = mInflater.inflate(R.layout.prms_acc_listiterm,null);
			 holder = new ViewHolder();
			 holder.prmsStyle=(TextView) convertView.findViewById(R.id.prms_acc_style);
			 holder.prmsAlias=(TextView) convertView.findViewById(R.id.prms_acc_alias);
			 holder.prmsCountNum=(TextView) convertView.findViewById(R.id.prms_acc_accountId);
			 holder.rightFlag = (ImageView) convertView.findViewById(R.id.imageViewright);
			 //			 holder.prmsBalance=(TextView) convertView.findViewById(R.id.prms_acc_balance);
			 convertView.setTag(holder);
			}
			else{
				holder=(ViewHolder) convertView.getTag();
			}
		
		
			Map<String, String> map=list.get(position);
			final String acctype= map.get(Prms.QUERY_PRMSACCS_ACCOUNTTYPE);
			final String nickName=map.get(Prms.QUERY_PRMSACCS_NICKNAME);
			final String accountNumber=map.get(Prms.QUERY_PRMSACCS_ACCOUNTNUMBER);
			
			if(accountNumber.equals(selectedAccNum)) {
				convertView.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
				 holder.rightFlag.setVisibility(View.VISIBLE);
			}else{
				holder.rightFlag.setVisibility(View.INVISIBLE);
				convertView.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			}
			
			
			holder.prmsStyle.setText(LocalData.AccountType.get(acctype));
			holder.prmsAlias.setText(nickName);
			holder.prmsCountNum.setText(StringUtil.getForSixForString(accountNumber));
			
		return convertView;
	}
	/**
	 * 给被选中的某一项设置高亮背景
	 * @param selectedPosition
	 * @author nl.
	 */
	public void setSelectedPosition(String acccNum) {
		this.selectedAccNum = acccNum;
		notifyDataSetChanged();
//		notifyDataSetInvalidated(); // 通知系统调用getView方法 刷新视图
	}

	/**
	 * 存放控件
	 * @author Administrator
	 *
	 */
	public final class ViewHolder{
		/**
		 * 账户名字
		 */
		public TextView prmsStyle;
		/**
		 * 账户别名
		 */
		public TextView prmsAlias;
		/**
		 * 账户号码
		 */
		public TextView prmsCountNum;
		/**
		 * 账户余额
		 */
		public TextView prmsBalance;
		public ImageView rightFlag;
		
	}



}

