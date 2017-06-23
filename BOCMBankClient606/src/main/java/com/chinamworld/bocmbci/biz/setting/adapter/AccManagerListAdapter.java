package com.chinamworld.bocmbci.biz.setting.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属账户余额弹窗listView 适配器
 * 
 * @author xyl
 * 
 */
public class AccManagerListAdapter extends BaseAdapter {
	/** 正常状况 */
	public static final int NORMAL = 1;
	/**
	 * 取消关联状泰
	 */
	public static final int REMOVECONNECTION = 2;
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;
	private OnItemClickListener itemClick;
	/**
	 * flag 用来判断是否是取消关联 i=1,正常;i=2,取消关联;i=3,选择账户使用
	 * */
	private int flag = 0;
	/** 默认账户 */
	private String defaultAccStr;

	public AccManagerListAdapter(Context context,
			List<Map<String, Object>> list, int flag,
			OnItemClickListener itemClick) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.flag = flag;
		this.itemClick = itemClick;
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

	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.setting_accmanager_listiterm, null);
			holder = new ViewHolder();
			holder.editViewBtn = (TextView) convertView
					.findViewById(R.id.set_accmanager_edit);
			holder.accNickeNameTextView = (TextView) convertView
					.findViewById(R.id.set_accmanager_nickname);
			holder.accTypeTextView = (TextView) convertView
					.findViewById(R.id.set_accmanager_style);
			holder.accNumTextView = (TextView) convertView
					.findViewById(R.id.set_accmanager_accnum);
			holder.conserconnection = (ImageView) convertView
					.findViewById(R.id.set_btn_gocancelrelation);
			holder.linerLayout = (LinearLayout) convertView
					.findViewById(R.id.set_accountmanager_layout);
			holder.defaultAccview = (TextView) convertView
					.findViewById(R.id.set_accmanager_iconTextView1);

			if (flag == NORMAL) {
				holder.conserconnection.bringToFront();
				holder.conserconnection.setVisibility(View.GONE);
			} else if (flag == REMOVECONNECTION) {
				holder.conserconnection.bringToFront();
				holder.conserconnection.setVisibility(View.VISIBLE);
				LayoutParams params = (LayoutParams) holder.linerLayout
						.getLayoutParams();
				params.setMargins(
						0,
						0,
						context.getResources().getDimensionPixelOffset(
								R.dimen.icon_dele_HW_margarin), 0);
				holder.linerLayout.setLayoutParams(params);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = (Map<String, Object>) getItem(position);
		final String nickName = (String) map
				.get(Setting.SET_QUERYACCOUNTS_NICKNAME);
		final String accType = (String) map
				.get(Setting.SET_QUERYACCOUNTS_ACCOUNTTYPE);
		final String accNum = (String) map
				.get(Setting.SET_QUERYACCOUNTS_ACCOUNTNUMBER);
		final String accNumber = (String) map.get(Setting.SET_QUERYACCOUNTS_ACCOUNTNUMBER);

		holder.accTypeTextView.setText(LocalData.AccountType.get(accType));
		holder.accNickeNameTextView.setText(nickName);
		holder.accNumTextView.setText(StringUtil.getForSixForString(accNum));
		if(!StringUtil.isNullOrEmpty(defaultAccStr)&&accNumber.equals(defaultAccStr)){
			holder.defaultAccview.setVisibility(View.VISIBLE);
		}else{
			holder.defaultAccview.setVisibility(View.GONE);
		}
		// if (!StringUtil
		// .isNullOrEmpty(SettingControl.getInstance().defaultAccNum)) {
		// if (SettingControl.getInstance().defaultAccNum.equals(accNum)) {
		// holder.viewList.get(0).setVisibility(View.VISIBLE);
		// } else {
		// holder.viewList.get(0).setVisibility(View.GONE);
		// }
		// } else {// TODO 这个默认账户是没有调接口拿到的 ，有没有设定默认账户的接口
		// SettingControl.getInstance().defaultAccNum = accNum;
		// holder.viewList.get(0).setVisibility(View.VISIBLE);
		// }

		switch (flag) {
		case NORMAL:
			holder.editViewBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					itemClick.onItemClick(null, v, position, position);
				}
			});
			break;
		case REMOVECONNECTION:
			holder.conserconnection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					itemClick.onItemClick(null, v, position, position);
				}
			});
			break;
		default:
			break;
		}
		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author XYL
	 * 
	 */
	public final class ViewHolder {
		public TextView defaultAccview;
		public TextView accTypeTextView;
		public TextView accNickeNameTextView;
		public TextView accNumTextView;
		public TextView editViewBtn;
		public ImageView conserconnection;
		public LinearLayout linerLayout;

	}
	public OnItemClickListener getItemClick() {
		return itemClick;
	}

	public void setItemClick(OnItemClickListener itemClick) {
		this.itemClick = itemClick;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getDefaultAccIdStr() {
		return defaultAccStr;
	}

	public void setDefaultAccStr(String defaultAccStr) {
		this.defaultAccStr = defaultAccStr;
		notifyDataSetChanged();
	}
	

}
