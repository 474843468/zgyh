package com.chinamworld.bocmbci.biz.lsforex.adapter;

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
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 成交状况查询 斩仓交易查询 未平仓交易查询 对账单查询 */
public class IsForexQueryResultAdapter extends BaseAdapter {
	private final String TAG = "IsForexQueryResultAdapter";
	private Context context;
	private List<Map<String, Object>> list;
	private OnItemClickListener onItemClickListener = null;
	private int tag;
	// 委托序号
	private String consignNumber = null;
	// 货币对
	private String code1 = null;
	private String code2 = null;
	// 委托类型
	private String exchangeTranType = null;
	/** 金额 */
	private String orderStatus = null;
	private Map<String, Object> map = null;
	private String jcCodeCode = null;

	public IsForexQueryResultAdapter(Context context, List<Map<String, Object>> list, int tag, String codes) {
		this.context = context;
		this.list = list;
		this.tag = tag;
		this.jcCodeCode = codes;
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
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
	public long getItemId(int position) {
		return position;
	}

	public void dataChange(List<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	/** 成交查询 未平仓查询 */
	public void getData() {
		consignNumber = (String) map.get(IsForex.ISFOREX_CONSIGNNUMBER_REQ);
		Map<String, String> currency1 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY1_REQ);
		Map<String, String> currency2 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY2_REQ);
		if (!StringUtil.isNullOrEmpty(currency1) && !StringUtil.isNullOrEmpty(currency2)) {
			String codes1 = currency1.get(IsForex.ISFOREX_CODE_REQ);
			String codes2 = currency2.get(IsForex.ISFOREX_CODE_REQ);
			if (LocalData.Currency.containsKey(codes1)) {
				code1 = LocalData.Currency.get(codes1);
			}
			if (LocalData.Currency.containsKey(codes2)) {
				code2 = LocalData.Currency.get(codes2);
			}
		}

		String direction = (String) map.get(IsForex.ISFOREX_DIRECTION_REQ);
		if (!StringUtil.isNull(direction) && LocalData.isForexdirectionMap.containsKey(direction)) {
			exchangeTranType = LocalData.isForexdirectionMap.get(direction);
		} else {
			exchangeTranType = "-";
		}
		orderStatus = (String) map.get(IsForex.ISFOREX_AMOUNT_REQ);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogGloble.d(TAG, "getView");
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.isforex_query_result_item, null);
			holder = new ViewHolder();
			holder.number = (TextView) convertView.findViewById(R.id.isForex_result_number);
			holder.code = (TextView) convertView.findViewById(R.id.isForex_result_code);
			holder.type = (TextView) convertView.findViewById(R.id.isForex_result_type);
			holder.arrow = (ImageView) convertView.findViewById(R.id.rate_gotoDetail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.number.setTag(position);
		holder.code.setTag(position);
		holder.type.setTag(position);
		holder.arrow.setTag(position);
		switch (tag) {
		case 1:// 成交状况查询
		case 3:// 未平仓交易查询
		case 5:// 当前委托
		case 6:// 历史委托
			map = list.get(position);
			getData();
			// 货币对
			if (StringUtil.isNull(code1) || StringUtil.isNull(code2)) {
				holder.number.setText("-");
			} else {
				holder.number.setText(code1 + "/" + code2);

			}
			// 买卖方向
			holder.code.setText(exchangeTranType);
			if (StringUtil.isNull(orderStatus)) {
				// 交易金额
				holder.type.setText("-");
			} else {
				String amount = StringUtil.parseStringCodePattern(jcCodeCode, orderStatus, 2);
				holder.type.setText(amount);
			}

			break;
		case 2:// 斩仓交易查询
			map = list.get(position);
			// 结算币种
			String vfgRegCode = (String) map.get(IsForex.ISFOREX_SETTLECURRENCY_REQ);
			String jsCode = null;
			if (!StringUtil.isNull(vfgRegCode) && LocalData.Currency.containsKey(vfgRegCode)) {
				jsCode = LocalData.Currency.get(vfgRegCode);
			}
			// 斩仓金额
			String liquidationAmount = (String) map.get(IsForex.ISFOREX_LIQUIDATIONAMOUNT_REQ);
			String mm = null;
			if (!StringUtil.isNull(liquidationAmount)) {
				mm = StringUtil.parseStringCodePattern(vfgRegCode, liquidationAmount, 2);
			}
			Map<String, String> currency1 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY1_REQ);
			Map<String, String> currency2 = (Map<String, String>) map.get(IsForex.ISFOREX_CURRENCY2_REQ);
			// 货币对
			if (!StringUtil.isNullOrEmpty(currency1) && !StringUtil.isNullOrEmpty(currency2)) {
				String codes1 = currency1.get(IsForex.ISFOREX_CODE_REQ);
				String codes2 = currency2.get(IsForex.ISFOREX_CODE_REQ);
				if (LocalData.Currency.containsKey(codes1)) {
					code1 = LocalData.Currency.get(codes1);
				}
				if (LocalData.Currency.containsKey(codes2)) {
					code2 = LocalData.Currency.get(codes2);
				}
			}
			holder.number.setText(jsCode);
			holder.code.setText(code1 + "/" + code2);
			holder.type.setText(mm);

			break;
		case 4:// 对账单查询
			map = list.get(position);
			// 转账标志
			String transferDir = (String) map.get(IsForex.ISFOREX_TRANSFERDIR_REQ);
			String tag = null;
			if (!StringUtil.isNull(transferDir) && LocalData.isForextransferDirMap.containsKey(transferDir)) {
				tag = LocalData.isForextransferDirMap.get(transferDir);
			}
			// 转账类型
			String fundTransferType = (String) map.get(IsForex.ISFOREX_FUNDTRANSFERTYPE_REQ);
			if (!StringUtil.isNull(fundTransferType)) {
				fundTransferType = LocalData.isForexfundTransferTypeMap.get(fundTransferType);
			}
			String transferAmount = (String) map.get(IsForex.ISFOREX_TRANSFERAMOUNT_REQ);
			String amount = null;
			if (!StringUtil.isNull(transferAmount)) {
				amount = StringUtil.parseStringCodePattern(jcCodeCode, transferAmount, 2);
			}
			holder.number.setText(fundTransferType);
			holder.code.setText(tag);
			holder.type.setText(amount);

			break;
		default:
			break;
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(null, v, position, position);
				}
			}
		});

		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context, holder.code);
		return convertView;
	}

	/**
	 * 内部类--控件
	 */
	public class ViewHolder {
		public TextView number;
		public TextView code;
		public TextView type;
		public ImageView arrow;
	}
}
