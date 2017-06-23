package com.chinamworld.bocmbci.biz.remittance.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class UseModelAdapter extends BaseAdapter {
	private static final String TAG = "UseModelAdapter";
	/** 数据源 */
	private List<Map<String, Object>> modelList;
	/** 上下文 */
	private Context context;
	/** 选择的监听事件 */
	private OnItemClickListener onItemClickLis;

	public UseModelAdapter(List<Map<String, Object>> list, Context context) {
		this.modelList = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return modelList.size();
	}

	@Override
	public Object getItem(int position) {
		return modelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.remittance_user_model_lv_item, null);
			holder.rl_usemodel = (RelativeLayout) convertView.findViewById(R.id.rl_usemodel);
			holder.tv_modelName = (TextView) convertView
					.findViewById(R.id.tv_remittance_modelName);
			holder.tv_userName = (TextView) convertView
					.findViewById(R.id.tv_userName);
			holder.tv_accountNum = (TextView) convertView
					.findViewById(R.id.tv_accountNum);
			holder.tv_bankName = (TextView) convertView
					.findViewById(R.id.tv_bankName);
			holder.tv_choose = (Button) convertView
					.findViewById(R.id.tv_choose);
			convertView.setTag(holder);
		} else {
			holder =  (ViewHolder) convertView.getTag();
		}
		final View view = convertView;
		final int mPosition = position;
		final Map<String, Object> tempMap = modelList.get(position);
		holder.tv_modelName.setText((String)tempMap.get(Remittance.TEMPLATENAME));
		holder.tv_userName.setText((String)tempMap.get(Remittance.PAYEEENNAME));
		holder.tv_accountNum.setText(StringUtil.getForSixForString((String)
				tempMap.get(Remittance.PAYEEACTNO)));
		holder.tv_bankName.setText((String)tempMap.get(Remittance.PAYEEBANKNAME));
		holder.tv_choose.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (onItemClickLis != null) {
					onItemClickLis.onItemClick(null, view, mPosition, 0);
				}
			}
		});
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(context, holder.rl_usemodel);
		return convertView;
	}

	class ViewHolder {
		private RelativeLayout rl_usemodel;
		private TextView tv_modelName;
		private TextView tv_userName;
		private TextView tv_accountNum;
		private TextView tv_bankName;
		private Button tv_choose;
	}

	public void setOnItemClickLis(OnItemClickListener onItemClickLis) {
		this.onItemClickLis = onItemClickLis;
	}

	/**
	 * 数据刷新
	 */
	public void changeData(List<Map<String, Object>> list) {
		if (list != null) {
			this.modelList = list;
			notifyDataSetChanged();
		}
	}
}
