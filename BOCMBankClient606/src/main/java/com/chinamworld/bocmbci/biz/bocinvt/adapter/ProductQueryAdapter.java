package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 理财产品查询适配器
 * 
 * @author wangmengmeng
 * 
 */
public class ProductQueryAdapter extends BaseAdapter {
	private int textSize;
	/** 查询结果列表信息 */
	private OnItemClickListener mClickListener;
	private List<Map<String, Object>> mList;
	private Context mContext;

	public ProductQueryAdapter(Context context,
			List<Map<String, Object>> productQueryList) {
		this.mContext = context;
		this.mList = productQueryList;
		textSize = mContext.getResources().getDimensionPixelSize(R.dimen.textsize_one_five);
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
			convertView = View.inflate(mContext, R.layout.bocinvt_product_item, null);
			h.productName = (TextView) convertView.findViewById(R.id.textproductname);
			h.productBuyAmout = (TextView) convertView.findViewById(R.id.text1);
			h.productLimit = (TextView) convertView.findViewById(R.id.text2);
			h.productYearlyRR = (TextView) convertView.findViewById(R.id.text3);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		/** 产品名称 */
		h.productName.setText(String.valueOf(map.get(BocInvt.BOCI_PRODNAME_RES)));
		/** 起售金额 */
		h.productBuyAmout.setTextColor(mContext.getResources().getColor(R.color.red));
		h.productBuyAmout.setText(StringUtil.parseStringCodePattern(String.valueOf(map.get(BocInvt.BOCI_CURCODE_RES)), (String)map.get(BocInvt.SUBPAYAMT), 2));
		/** 产品期限 */
		switch (Integer.parseInt(map.get("isLockPeriod").toString())) {
		case 0:{//0：非业绩基准产品
			if (map.get("termType").toString().equals("3")) {//产品期限特性,3：无限开放式
				h.productLimit.setText("无固定期限");
			}else {
				h.productLimit.setText(String.valueOf(map.get("periedTime")) + "天");
			}
		}break;
		case 1:{//1：业绩基准-锁定期转低收益 
			h.productLimit.setText("最低持有"+String.valueOf(map.get("periedTime")) + "天");
		}break;
		case 2:{//2：业绩基准-锁定期后入账 
			h.productLimit.setText(String.valueOf(map.get("periedTime")) + "天");
		}break;
		case 3:{//3：业绩基准-锁定期周期滚续
			h.productLimit.setText(String.valueOf(map.get("periedTime")) + "天");
		}break;
		default:
			break;
		}
		/** 预计年收益率 */
		setYearlyRR(map, h.productYearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,h.productName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,h.productYearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,h.productLimit);
		return convertView;
	}
	
	/**
	 * 预计年收益率
	 * @param map
	 * @return
	 */
	private void setYearlyRR(Map<String, Object> map,TextView v){
		if (StringUtil.isNull((String)map.get(BocInvt.BOCI_YEARLYRR_RES))) {
			v.setText("-"); return;
		}
		String progressionflag = (String) map.get(BocInvt.PROGRESSIONFLAG);
		if (!StringUtil.isNull(progressionflag) && progressionflag.equals("1")) {
//			v.setText("收益累进"); //P603开始，不再显示"收益累进"
//			setYearlyRR1(map,v);
			v.setText(BocInvestControl.getYearlyRR(map,BocInvt.BOCI_YEARLYRR_RES,BocInvestControl.RATEDETAIL));
			return;
		}
		if (map.get(BocInvestControl.PRODUCTKIND).toString().equals("1")) {//0:结构性理财产品1:类基金理财产品
			if (map.get(BocInvestControl.ISSUETYPE).toString().equals(BocInvestControl.map_issueType.get(BocInvestControl.list_issueType.get(2)))) {
				//净值型产品		//显示单位净值
				v.setText(StringUtil.parseStringPattern2(map.get(BocInvestControl.PRICE).toString(), 2));
			}else {//显示预计年收益率
//				setYearlyRR1(map,v);
				v.setText(BocInvestControl.getYearlyRR(map,BocInvt.BOCI_YEARLYRR_RES,BocInvestControl.RATEDETAIL));
			}
		}else {
//			setYearlyRR1(map,v);
			v.setText(BocInvestControl.getYearlyRR(map,BocInvt.BOCI_YEARLYRR_RES,BocInvestControl.RATEDETAIL));
		}
//		String append2Decimals = StringUtil.append2Decimals((String)map.get(BocInvt.BOCI_YEARLYRR_RES), 2)+"%";
//		SpannableString spannableString = new SpannableString(append2Decimals);
//		spannableString.setSpan(new AbsoluteSizeSpan(textSize), 0, append2Decimals.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		spannableString.setSpan(new AbsoluteSizeSpan(textSize*2/3), append2Decimals.length()-1, append2Decimals.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		v.setText(spannableString);
		
	}
	private void setYearlyRR1(Map<String, Object> map,TextView v){
		String append2Decimals =null;
		String str_1 = map.get(BocInvt.BOCI_YEARLYRR_RES).toString();
		String str_2 = map.get(BocInvt.BOCINVT_AGRINFOQUERY_RATEDETAIL_RES).toString();
		if (StringUtil.isNullOrEmpty(str_1)&&StringUtil.isNullOrEmpty(str_2)) {
			v.setText("后台数据相应字段为null");
			return;
		}
		if (StringUtil.isNullOrEmpty(str_2)) {
			append2Decimals = StringUtil.append2Decimals(str_1, 2)+"%";
		}else {
			append2Decimals = StringUtil.append2Decimals(str_1, 2)+StringUtil.append2Decimals(str_2, 2)+"%";
		}
		SpannableString spannableString = new SpannableString(append2Decimals);
		spannableString.setSpan(new AbsoluteSizeSpan(textSize), 0, append2Decimals.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new AbsoluteSizeSpan(textSize*2/3), append2Decimals.length()-1, append2Decimals.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		v.setText(spannableString);
	}
	
	private void setProgress(TextView v,Map<String, Object> map,final int position){
		String progressionflag = (String) map.get(BocInvt.PROGRESSIONFLAG);
		if (!StringUtil.isNull(progressionflag) && progressionflag.equals("1")){
			v.setTextColor(mContext.getResources().getColor(R.color.blue));
			v.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			v.setText(mContext.getString(R.string.progression));
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mClickListener != null ) {
						mClickListener.onItemClick(null, v, position, position);
					}
				}
			});return;
		}
		v.setOnClickListener(null);
		v.getPaint().setFlags(0);
		v.setTextColor(mContext.getResources().getColor(R.color.black));
		v.setText(StringUtil.append2Decimals((String)map.get(BocInvt.BOCI_YEARLYRR_RES), 2));
	}
	
	public void setViewOnClick (OnItemClickListener onclick){
		this.mClickListener = onclick;
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
		public TextView productLimit;
		public TextView productBuyAmout;
	}
}
