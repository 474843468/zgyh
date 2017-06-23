package com.chinamworld.bocmbci.biz.crcd.adapter;

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
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 分期查询适配器
 * 
 * @author huangyuchao
 * 
 */
public class CrcdDividedQueryAdapter extends BaseAdapter {

	/** 列表信息 */
	private List<Map<String, Object>> transferList;
	private Context context;
	/** 账户交易收入支出金额 */
	private TextView tv_divided_date;
	/** 账户交易时间 */
	private TextView tv_divided_num;
	/** 账户交易账户描述 */
	private TextView tv_divided_money;

	private ImageView iv_arrow;

	public CrcdDividedQueryAdapter(Context context, List<Map<String, Object>> transferList) {
		this.context = context;
		this.transferList = transferList;
	}

	public void dataChanged(List<Map<String, Object>> list) {
		this.transferList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return transferList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return transferList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.crcd_divided_list_item, null);
		}
		tv_divided_date = (TextView) convertView.findViewById(R.id.tv_divided_date);
		tv_divided_num = (TextView) convertView.findViewById(R.id.tv_divided_num);
		tv_divided_money = (TextView) convertView.findViewById(R.id.tv_divided_money);

		iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
		Map<String,Object> dividedMap = transferList.get(position);

		String date = String.valueOf(dividedMap.get(Crcd.CRCD_INSTMTDATE));
		String num = String.valueOf(dividedMap.get(Crcd.CRCD_INSTMTCOUNT));
		String money = String.valueOf(dividedMap.get(Crcd.CRCD_AMOUNT));

		tv_divided_date.setText(date);
		tv_divided_num.setText(num);
		tv_divided_money.setText(StringUtil.parseStringPattern(money, 2));

		return convertView;
	}

}
