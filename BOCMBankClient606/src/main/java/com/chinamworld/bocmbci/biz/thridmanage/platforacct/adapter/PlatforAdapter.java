package com.chinamworld.bocmbci.biz.thridmanage.platforacct.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.CurrencyType;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: PlatforAdapter
 * @Description: 台账查询
 * @author lql
 * @date 2013-8-19 下午08:40:07
 */
public class PlatforAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, String>> mList;

	public PlatforAdapter(Context cn, List<Map<String, String>> list) {
		this.mContext = cn;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Map<String, String> getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = LinearLayout.inflate(mContext, R.layout.third_commonlist_item, null);
			h.tvTime = (TextView) convertView.findViewById(R.id.tv_trade_time);
			h.tvType = (TextView) convertView.findViewById(R.id.tv_trade_type);
			h.tvAmout = (TextView) convertView.findViewById(R.id.tv_trade_amout);

			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}

		Map<String, String> map = mList.get(position);
		String currencyType = map.get(Third.CECURITY_AMOUT_RNCY);
		String availableBalance = map.get(Third.CECURITY_AMOUT_AVAI);
		
		h.tvTime.setText(map.get(Third.PLATFORACC_LIST_DATE));
		h.tvType.setText(CurrencyType.getCurrencyTypeStr(currencyType));
		h.tvAmout.setText(StringUtil.parseStringPattern(availableBalance, 2));
		return convertView;
	}

	public void setData(List<Map<String, String>> list) {
		this.mList = list;
	}

	private static class ViewHodler {
		public TextView tvTime;
		public TextView tvType;
		public TextView tvAmout;
	}
}
