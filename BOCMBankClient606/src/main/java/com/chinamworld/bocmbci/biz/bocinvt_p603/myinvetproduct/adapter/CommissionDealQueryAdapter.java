package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter;

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
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 委托交易查询适配器
 * 
 * @author niuchf
 * 
 */
public class CommissionDealQueryAdapter extends BaseAdapter {
	/** 查询结果列表信息 */
	private List<Map<String, Object>> productQueryList;
	private Context context;
	//1:常规交易查询 2：组合购买
	private int model;
	
	/**
	 * 适配器构造方法
	 * @param context 
	 * @param productQueryList 数据源
	 */
	public CommissionDealQueryAdapter(Context context,
			List<Map<String, Object>> productQueryList,int model) {
		this.context = context;
		this.productQueryList = productQueryList;
		this.model = model;
	}

	public CommissionDealQueryAdapter() {
		super();
	}

	public void changeData() {
		
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return productQueryList.size();
	}

	@Override
	public Object getItem(int position) {
		return productQueryList.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.boc_invest_agrquery_list_item, null);
		}
		/** 委托日期*/
		TextView boci_product_name = (TextView) convertView
				.findViewById(R.id.boc_invest_agrname_tv);
		/** 产品名称 */
		TextView boci_yearlyRR = (TextView) convertView
				.findViewById(R.id.boc_invest_execpro_tv);
		/** 交易类型 */
		TextView boci_timeLimit = (TextView) convertView
				.findViewById(R.id.boc_invest_agrtype_tv);
		if(model == 1){
			boci_timeLimit.setTextColor(context.getResources()
					.getColor(R.color.red));
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_product_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_yearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
				boci_timeLimit);
		/** 右三角 */
		ImageView goDetail = (ImageView) convertView
				.findViewById(R.id.boc_invest_gotoDetail);
		goDetail.setVisibility(View.VISIBLE);
		// 赋值操作
		if(model == 0){
			boci_product_name.setText(String.valueOf(productQueryList.get(position)
					.get(BocInvt.BOCINVT_FUTUREDATE_RES)));
			boci_yearlyRR.setText(String.valueOf(productQueryList.get(position)
					.get(BocInvt.BOCINVT_AUTTRAD_PRONAME_RES)));
			String timeLimit = (String) productQueryList.get(position).get(
					BocInvt.BOCINVT_TRFTYPE_RES);
			boci_timeLimit.setText(LocalData.bociTrfTypeMap.get(timeLimit));
		}
		if(model == 1){
			boci_product_name.setText(String.valueOf(productQueryList.get(position)
					.get(BocInvt.BOCINVT_RETURNDATE_RES)));
			boci_yearlyRR.setText(String.valueOf(productQueryList.get(position)
					.get(BocInvt.BOCINVT_AUTTRAD_PRONAME_RES)));
			String timeLimit = (String) productQueryList.get(position).get(
					BocInvt.BOCINVT_BUYAMT_RES);
			String currency = (String) productQueryList.get(position).get(
					BocInvt.BOCINVT_CURRENCYCODE_RES);
			boci_timeLimit.setText(StringUtil.parseStringCodePattern(currency,
					timeLimit, 2));
		}
		
		return convertView;
	}
}
