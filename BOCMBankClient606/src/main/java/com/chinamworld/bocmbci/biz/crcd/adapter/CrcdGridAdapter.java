package com.chinamworld.bocmbci.biz.crcd.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 
 * @author huangyuchao
 * 
 */
public class CrcdGridAdapter extends BaseAdapter {

	private Context context;

	/** 引入布局id */
	private int layoutResource;

	/** 账户信息 */
	private List<Map<String, Object>> crcdAccountList;

	public CrcdGridAdapter(Context context, int layoutResource,
			List<Map<String, Object>> crcdAccountList) {
		this.context = context;
		this.layoutResource = layoutResource;
		this.crcdAccountList = crcdAccountList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return crcdAccountList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return crcdAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CrcdViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(layoutResource,
					null);
			holder = new CrcdViewHolder();
			holder.crcd_type_value = (TextView) convertView
					.findViewById(R.id.crcd_type_value);
			holder.crcd_account_nickname = (TextView) convertView
					.findViewById(R.id.crcd_account_nickname);
			holder.crcd_currencycode_value = (TextView) convertView
					.findViewById(R.id.crcd_currencycode_value);
			holder.crcd_account_num = (TextView) convertView
					.findViewById(R.id.crcd_account_num);
			holder.crcd_bookbalance_value = (TextView) convertView
					.findViewById(R.id.crcd_bookbalance_value);
			holder.ivdetail = (ImageView) convertView
					.findViewById(R.id.img_crcd_detail);
			holder.ivgoitem = (ImageView) convertView
					.findViewById(R.id.crcd_btn_goitem);

			holder.crcd_frame_left = (FrameLayout) convertView
					.findViewById(R.id.crcd_frame_left);
			holder.crcd_frame_right = (FrameLayout) convertView
					.findViewById(R.id.crcd_frame_right);
			

			convertView.setTag(holder);
		} else {
			holder = (CrcdViewHolder) convertView.getTag();
		}

		Map<String, Object> map = crcdAccountList.get(position);
		Map<String, Object> mapDetail = (Map<String, Object>) map
				.get(Crcd.CRCD_DETAILIST);
		String accountType = String.valueOf(map.get(Crcd.CRCD_ACCOUNTTYPE_RES));
		String strAccountType = LocalData.AccountType.get(accountType);

		holder.crcd_type_value.setText(strAccountType);
		holder.crcd_account_nickname.setText((String) map
				.get(Crcd.CRCD_NICKNAME_RES));
		holder.crcd_currencycode_value.setText(context.getResources()
				.getString(R.string.mycrcd_bill));
		holder.crcd_account_num.setText(StringUtil.getForSixForString(String
				.valueOf(map.get(Crcd.CRCD_ACCOUNTNUMBER_RES))));
		holder.crcd_bookbalance_value.setText(" : "
				+ (String) mapDetail.get(Crcd.CRCD_CURRENTBALANCE));

		holder.ivdetail.setVisibility(View.GONE);
		return convertView;
	}

	class CrcdViewHolder {
		TextView crcd_type_value;
		TextView crcd_account_nickname;
		TextView crcd_currencycode_value;
		TextView crcd_account_num;
		TextView crcd_bookbalance_value;

		ImageView ivdetail;
		ImageView ivgoitem;

		FrameLayout crcd_frame_left;
		FrameLayout crcd_frame_right;

	}

}
