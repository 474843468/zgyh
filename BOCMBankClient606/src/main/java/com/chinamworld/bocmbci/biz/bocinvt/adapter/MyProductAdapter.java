package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的理财产品查询适配器
 * 
 * @author wangmengmeng
 * 
 */
public class MyProductAdapter extends BaseAdapter {
	/** 查询结果列表信息 */
	private OnItemClickListener mClickListener;
	private List<Map<String, Object>> productQueryList;
	private Context context;

	public MyProductAdapter(Context context,List<Map<String, Object>> productQueryList) {
		this.context = context;
		this.productQueryList = productQueryList;
	}

	@Override
	public int getCount() {
		if (!StringUtil.isNullOrEmpty(productQueryList)) {
			return productQueryList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (!StringUtil.isNullOrEmpty(productQueryList)) {
			return productQueryList.get(position);
		}
		return null;

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = View.inflate(context, R.layout.bocinvt_product_item, null);
			h.productName = (TextView) convertView.findViewById(R.id.textproductname);
			h.productHoldingQuantity = (TextView) convertView.findViewById(R.id.text1);
			h.productLimit = (TextView) convertView.findViewById(R.id.text2);
			h.productYearlyRR = (TextView) convertView.findViewById(R.id.text3);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		h.productHoldingQuantity.setTextColor(context.getResources().getColor(R.color.red));
		final Map<String, Object> map = productQueryList.get(position);
//		String progressionflag = (String) map.get(BocInvt.PROGRESSIONFLAG);
		String holdingQuantity = (String) map.get(BocInvt.BOCINVT_HOLDPRO_HOLDINGQUANTITY_RES);
//		toProgress(h.productYearlyRR, progressionflag,position);
		/** 产品名称 */
		h.productName.setText(String.valueOf(map.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES)));
		/** 持有份额 */
		h.productHoldingQuantity.setText(StringUtil.parseStringPattern(holdingQuantity, 2));
		/** 产品到期日 */
		h.productLimit.setText(prodEnd(String.valueOf(map.get(BocInvt.BOCINVT_HOLDPRO_PRODEND_RES))));
		/** 预计年收益率 */
		h.productYearlyRR.setText(getYearlyRR(map));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,h.productName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,h.productYearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,h.productLimit);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,h.productHoldingQuantity);
		return convertView;
	}
	
	private String prodEnd(String prodEnd){
		if (!StringUtil.isNull(prodEnd)) {
			if (prodEnd.equals("-1")) {
				return "无限期";
			}
			return DateUtils.DateFormatter(prodEnd);
		}
		return "-";
	}
	
	/**
	 * 预计年收益率
	 * @param map
	 * @return
	 */
	private String getYearlyRR(Map<String, Object> map){
		String progressionflag = (String) map.get(BocInvt.PROGRESSIONFLAG);
		if (!StringUtil.isNull(progressionflag) && progressionflag.equals("1")) {
			return "收益累进";
		}
		return StringUtil.append2Decimals((String)map.get(BocInvt.BOCINVT_HOLDPRO_YEARLYRR_RES), 2)+"%";
	}
	
	private void toProgress(TextView v,String progressionflag,final int position){
		if (!StringUtil.isNull(progressionflag) && progressionflag.equals("1")) {
			v.setTextColor(context.getResources().getColor(R.color.blue));
			v.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mClickListener != null ) {
						mClickListener.onItemClick(null, v, position, position);
					}
				}
			});
		}
	}
	
	public void setViewOnClick (OnItemClickListener onclick){
		this.mClickListener = onclick;
	}
	
	public List<Map<String, Object>> getProductQueryList() {
		return productQueryList;
	}

	public void setProductQueryList(List<Map<String, Object>> productQueryList) {
		this.productQueryList = productQueryList;
		notifyDataSetChanged();
	}

	public class ViewHodler {
		public TextView productName;
		public TextView productYearlyRR;
		public TextView productHoldingQuantity;
		public TextView productLimit;
	}
}
