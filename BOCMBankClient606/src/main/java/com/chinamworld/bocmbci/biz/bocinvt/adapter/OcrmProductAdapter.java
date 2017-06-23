package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 指令交易适配器
 * 
 * @author wangmengmeng
 * 
 */
public class OcrmProductAdapter extends BaseAdapter {
	private List<Map<String, Object>> mList;
	private Context context;

	public OcrmProductAdapter(Context context,List<Map<String, Object>> list) {
		this.context = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (!StringUtil.isNullOrEmpty(mList)) {
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (!StringUtil.isNullOrEmpty(mList)) {
			return mList.get(position);
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
		h.productHoldingQuantity.setVisibility(View.GONE);
//		h.productYearlyRR.setTextColor(context.getResources().getColor(R.color.red));
		final Map<String, Object> map = mList.get(position);
		/** 产品名称 */
		h.productName.setText(String.valueOf(map.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_REQ)));
		/** 币种   */
		h.productLimit.setText(LocalData.Currency.get(map.get(BocInvt.BOCINVT_SETBONUS_CURRENCYCODE_REQ)));
		
//		if (StringUtil.isNull((String)map.get(BocInvt.CHARCODE))) {
//			h.productLimit.setText(LocalData.Currency.get(map.get(BocInvt.BOCINVT_SETBONUS_CURRENCYCODE_REQ)));
//		}else{
//			h.productLimit.setText(LocalData.Currency.get(map.get(BocInvt.BOCINVT_SETBONUS_CURRENCYCODE_REQ))+"/"+
//		LocalData.CurrencyCashremit.get(map.get(BocInvt.CHARCODE)));
//		}
		/** 理财经理   */
		h.productYearlyRR.setText(String.valueOf(map.get(BocInvt.BOCINVT_SIGNINIT_LICAIJINGLINAME_REQ)));
		/** 交易金额   **/
//		h.productYearlyRR.setText(setTransSum(map));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,h.productName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,h.productYearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,h.productLimit);
		return convertView;
	}
	
	/**
	 * 交易金额
	 * @param map
	 * @return
	 */
	private String setTransSum(Map<String, Object> map){
		String isPre = (String) map.get(BocInvt.ISPRE);
		String transSum = (String) map.get(BocInvt.TRANSSUM);
		String transType = (String) map.get(BocInvt.TRANSTYPE);
		String amount = (String) map.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES);
		String amountType = (String) map.get(BocInvt.BOC_QUERY_AGREE_AMOUNTTYPE_RES);
		String curreny = (String) map.get(BocInvt.BOCINVT_SETBONUS_CURRENCYCODE_REQ);
		
		if (!StringUtil.isNull(isPre) && isPre.equals("0") &&
				!StringUtil.isNull(transType) && transType.equals("05")) {
			if (!StringUtil.isNull(amountType) && amountType.equals("1")) {
				return "浮动";
			}
			return StringUtil.parseStringCodePattern(curreny,amount, 2);
		}else{
			if (!StringUtil.isNull(transType) && transType.equals("06")) {
				return ConstantGloble.BOCINVT_DATE_ADD;
			}else if(!StringUtil.isNull(transType) && transType.equals("08")){
				return ConstantGloble.BOCINVT_DATE_ADD;
			}
			return StringUtil.parseStringCodePattern(curreny,transSum, 2);
		}
	}

	public List<Map<String, Object>> getmList() {
		return mList;
	}

	public void setmList(List<Map<String, Object>> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	public class ViewHodler {
		public TextView productName;
		public TextView productYearlyRR;
		public TextView productHoldingQuantity;
		public TextView productLimit;
	}
}
