package com.chinamworld.bocmbci.biz.dept.notmg;

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
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: GalleryAdapter
 * @Description: 通知管理查询gallery的adapter
 * @author JiangWei
 * @date 2013-7-9 上午10:24:49
 */
public class NotifyGalleryAdapter extends BaseAdapter {
	private Context mContext;

	private List<Map<String, Object>> mList;

	public NotifyGalleryAdapter(Context context, List<Map<String, Object>> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dept_acc_list_item, null);
			viewHolder.accTypeTv = (TextView) convertView.findViewById(R.id.dept_type_tv);
			viewHolder.cardDescriptionTv = (TextView) convertView.findViewById(R.id.dept_card_description_tv);
			viewHolder.accNoTv = (TextView) convertView.findViewById(R.id.dept_account_num_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String strAccountType = LocalData.AccountType.get((String) mList.get(position).get(Comm.ACCOUNT_TYPE));
		viewHolder.accTypeTv.setText(strAccountType);

		String strNickName = (String) mList.get(position).get(Comm.NICKNAME);
		viewHolder.cardDescriptionTv.setText(strNickName);

		String accountNumber = (String) mList.get(position).get(Comm.ACCOUNTNUMBER);
		viewHolder.accNoTv.setText(StringUtil.getForSixForString(accountNumber));
		((FrameLayout) convertView.findViewById(R.id.acc_frame_back)).setVisibility(View.GONE);
		((ImageView) convertView.findViewById(R.id.financeic_btn_goitem)).setVisibility(View.GONE);
		return convertView;
	}

	private class ViewHolder {
		public TextView accTypeTv;
		public TextView cardDescriptionTv;
		public TextView accNoTv;
	}

}
