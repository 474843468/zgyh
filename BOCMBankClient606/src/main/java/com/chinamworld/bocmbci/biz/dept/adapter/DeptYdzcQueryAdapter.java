package com.chinamworld.bocmbci.biz.dept.adapter;

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
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

public class DeptYdzcQueryAdapter extends BaseAdapter {
	private List<Map<String, String>> list = null;
	private Context context = null;
	private OnItemClickListener onItemClickListener;

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public DeptYdzcQueryAdapter(Context context, List<Map<String, String>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void refshDate(List<Map<String, String>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = LayoutInflater.from(context).inflate(R.layout.dept_dqydzc_query_item, null);
			holder = new ViewHolder();
			holder.accountTypeText = (TextView) contentView.findViewById(R.id.dept_accounttype);
			holder.cdAndVolumText = (TextView) contentView.findViewById(R.id.dept_dqyzc_cdandvolum);
			holder.codeAndCashText = (TextView) contentView.findViewById(R.id.dept_dqyzc_codeandcash);
			holder.moneyText = (TextView) contentView.findViewById(R.id.dept_dqyzc_money);
			holder.statusText = (TextView) contentView.findViewById(R.id.dept_dqyzc_status);
			holder.rightImage = (ImageView) contentView.findViewById(R.id.right_img);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		Map<String, String> map = list.get(position);
		String type = map.get(Dept.DEPT_TYPE_RES);
		String volumeNumber = map.get(Dept.DEPT_VOLUMENUMBER_RES);
		String cdNumber = map.get(Dept.DEPT_CDNUMBER_RES);
		String currencyCode = map.get(Dept.DEPT_CURRENCYCODE_RES);
		String cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
		String availableBalance = map.get(Dept.DEPT_BOOKBALANCE_RES);
		String convertType = map.get(Dept.DEPT_APPOINTSTATUS_RES);
		String types = null;
		if (StringUtil.isNull(type)) {
			types = "-";
		} else {
			if (LocalData.fixAccTypeMap.containsKey(type)) {
				types = LocalData.fixAccTypeMap.get(type);
			} else {
				types = "-";
			}
		}
		String code = null;
		if (StringUtil.isNull(currencyCode)) {
			code = "-";
		} else {
			if (LocalData.Currency.containsKey(currencyCode)) {
				code = LocalData.Currency.get(currencyCode);
			} else {
				code = "-";
			}
		}
		String cash = null;
		if (StringUtil.isNull(cashRemit)) {
			cash = "-";
		} else {
			if (LocalData.CurrencyCashremit.containsKey(cashRemit)) {
				cash = LocalData.CurrencyCashremit.get(cashRemit);
			} else {
				cash = "-";
			}
		}
		String money = null;
		if (StringUtil.isNull(availableBalance)) {
			money = "-";
		} else {
			money = StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2);
		}
		String convertTypes = null;
		if (StringUtil.isNull(convertType)) {
			convertTypes = "-";
		} else {
			if (LocalData.appointStatusremitautoFlagMap.containsKey(convertType)) {
				convertTypes = LocalData.appointStatusremitautoFlagMap.get(convertType);
			} else {
				convertTypes = "-";
			}

		}
		holder.accountTypeText.setText(types);
		holder.cdAndVolumText.setText(volumeNumber + "/" + cdNumber);
		if (LocalData.rmbCodeList.contains(currencyCode)) {
			holder.codeAndCashText.setText(code);
		} else {
			holder.codeAndCashText.setText(code + "/" + cash);
		}
		holder.moneyText.setText(money);
		holder.statusText.setText(convertTypes);
		holder.rightImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		return contentView;
	}

	private class ViewHolder {
		private TextView accountTypeText = null;
		private TextView cdAndVolumText = null;
		private TextView codeAndCashText = null;
		private TextView moneyText = null;
		private TextView statusText = null;
		private ImageView rightImage = null;

	}
}
