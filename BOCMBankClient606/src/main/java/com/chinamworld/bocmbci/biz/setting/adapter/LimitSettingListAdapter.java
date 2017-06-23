package com.chinamworld.bocmbci.biz.setting.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.setting.limit.EditLimitMainActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属账户余额弹窗listView 适配器
 * 
 * @author xyl
 * 
 */
public class LimitSettingListAdapter extends BaseAdapter {
	/** 小数点后保留几位 */
	private final int SCALE = 2;
	private Context context;
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;

	public LimitSettingListAdapter(Context context,
			List<Map<String, String>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = getDataList(list);
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.setting_limit_listiterm,
					null);
			holder = new ViewHolder();
			holder.serviceNameTextView = (TextView) convertView
					.findViewById(R.id.set_servicename);
			holder.dayMaxTextView = (TextView) convertView
					.findViewById(R.id.set_daymaxlimit);
			holder.preMaxTextView = (TextView) convertView
					.findViewById(R.id.set_predaymaxlimit);
			holder.currencyTextView = (TextView) convertView
					.findViewById(R.id.set_currency);
			holder.editBtn = (Button) convertView.findViewById(R.id.button1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = (Map<String, String>) getItem(position);
		final String serviceId = map.get(Setting.SET_QUERYLIMIT_SERVICEID);
		final String dayMax = map.get(Setting.SET_QUERYLIMIT_MAXAMOUNT);
		final String preMax = map.get(Setting.SET_QUERYLIMIT_AMOUNT);// 用户设定最大交易限额
		final String currency = context.getString(R.string.prms_acc_rmb);

		holder.serviceNameTextView.setText(LocalData.serviceCodeMap
				.get(serviceId));
		if (!StringUtil.isNullOrEmpty(dayMax)) {// 如果限额返回为空 显示 0
			holder.dayMaxTextView.setText(StringUtil.parseStringPattern(dayMax,
					SCALE));
		}
		if (!StringUtil.isNullOrEmpty(dayMax)) {
			holder.preMaxTextView.setText(StringUtil.parseStringPattern(preMax,
					SCALE));
		}
		holder.currencyTextView.setText(currency);
		holder.editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, EditLimitMainActivity.class);
				intent.putExtra(Setting.I_DAYMAX, dayMax);
				intent.putExtra(Setting.I_PREMAX, preMax);
				intent.putExtra(Setting.I_SERVICEID, serviceId);
				intent.putExtra(Setting.I_CURRENCY, currency);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	/**
	 * 存放控件
	 * 
	 * @author XYL
	 * 
	 */
	public final class ViewHolder {
		/**
		 * 显示服务名的TextView
		 */
		public TextView serviceNameTextView;
		/**
		 * 可设定最大限额
		 */
		public TextView dayMaxTextView;
		/**
		 * 个人日限额
		 */
		public TextView preMaxTextView;
		/**
		 * 币种
		 */
		public TextView currencyTextView;
		/**
		 * 修改按钮
		 */
		public Button editBtn;

	}

	/**
	 * 根据 LocalData.serviceCodeList 筛选出要选择的 11 种可以做修改的交易限额
	 */
	private List<Map<String, String>> getDataList(List<Map<String, String>> list) {
		List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		for (Map<String, String> map : list) {
			for (String serviceId : LocalData.serviceCodeList) {
				if (map.get(Setting.SET_QUERYLIMIT_SERVICEID).equals(serviceId)) {
					tempList.add(map);
				}
			}

		}
		return tempList;
	}
}
