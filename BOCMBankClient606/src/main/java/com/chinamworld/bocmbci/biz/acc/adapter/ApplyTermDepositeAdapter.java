package com.chinamworld.bocmbci.biz.acc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 申请定期活期账户	绑定介质ListView的适配器
 * @author Administrator
 *
 */
public class ApplyTermDepositeAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Map<String,Object>> mList;
	private int selectedPosition=-1;
	
	public ApplyTermDepositeAdapter(Context context,List<Map<String,Object>> list) {
		this.mContext=context;
		this.mList=list;
	}
	
	@Override
	public int getCount() {
		if(mList==null||mList.size()==0){
			return 0;
		}else{
			return mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if(mList==null||mList.size()==0){
			return null;
		}else{
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 数据刷新
	 */
	public void changeData(List<Map<String,Object>> list){
		if(list!=null){
			this.mList=list;
			notifyDataSetChanged();
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=LinearLayout.inflate(mContext, R.layout.acc_apply_item, null);
			vh.tvAccType=(TextView) convertView.findViewById(R.id.acc_type_value);
			vh.tvAccNum=(TextView) convertView.findViewById(R.id.acc_account_num);
			vh.tvNickName=(TextView) convertView.findViewById(R.id.acc_nickname);
			vh.isChecked=(ImageView) convertView.findViewById(R.id.apply_checked);
			vh.flAccMsg=(FrameLayout) convertView.findViewById(R.id.apply_msg);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		Map<String, Object> map=mList.get(position);
		String accTypeId=String.valueOf(map.get(Acc.ACC_ACCOUNTTYPE_RES));
		vh.tvAccType.setText(AccBaseActivity.bankAccountType.get(accTypeId));
		String accNum=String.valueOf(map.get(Acc.ACC_ACCOUNTNUMBER_RES));
		vh.tvAccNum.setText(StringUtil.getForSixForString(accNum));
		String accNickName=String.valueOf(map.get(Acc.ACC_NICKNAME_RES));
		vh.tvNickName.setText(accNickName);
		if(position==selectedPosition){
			vh.flAccMsg.setBackgroundResource(R.drawable.bg_for_listview_item_half_black);
			vh.isChecked.setVisibility(View.VISIBLE);
		}else{
			vh.flAccMsg.setBackgroundResource(R.drawable.bg_for_listview_item_write);
			vh.isChecked.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}
	
	public class ViewHolder{
		private TextView tvAccType;
		private TextView tvAccNum;
		private TextView tvNickName;
		private ImageView isChecked;
		private FrameLayout flAccMsg;
	}
}
