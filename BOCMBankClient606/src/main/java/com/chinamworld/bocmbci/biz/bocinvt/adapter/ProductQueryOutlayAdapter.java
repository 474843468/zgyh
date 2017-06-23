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
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 功能外置 查询结果列表信息适配器
 * 
 * @author sunh
 * 
 */

public class ProductQueryOutlayAdapter extends BaseAdapter {
	private int textSize;

	private OnItemClickListener mClickListener;
	private List<Map<String, Object>> mList;
	private Context mContext;

	public ProductQueryOutlayAdapter(Context context,
			List<Map<String, Object>> productQueryList) {
		this.mContext = context;
		this.mList = productQueryList;
		textSize = mContext.getResources().getDimensionPixelSize(
				R.dimen.textsize_one_eight);
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
			convertView = View.inflate(mContext,
					R.layout.bocinvt_product_outlay_item, null);
			h.productName = (TextView) convertView
					.findViewById(R.id.textproductname);
			h.productBuyAmout = (TextView) convertView.findViewById(R.id.text1);
			h.productLimit = (TextView) convertView.findViewById(R.id.text2);
			h.productYearlyRR = (TextView) convertView.findViewById(R.id.text3);
			h.productLeftBg = (ImageView) convertView
					.findViewById(R.id.left_bg);
			convertView.setTag(h);
		} else {
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		if (position % 3 == 0) {
			h.productLeftBg.setBackgroundResource(R.drawable.outlay_left_fen);
		} else if (position % 3 == 1) {
			h.productLeftBg.setBackgroundResource(R.drawable.outlay_left_ceng);
		} else if (position % 3 == 2) {
			h.productLeftBg.setBackgroundResource(R.drawable.outlay_left_lan);
		}
		// else if (position % 5 == 3) {
		// h.productLeftBg.setBackgroundResource(R.drawable.outlay_left_lv);
		// } else if (position % 5 == 4) {
		// h.productLeftBg.setBackgroundResource(R.drawable.outlay_left_huang);
		// }
		if(!StringUtil.isNullOrEmpty(map)){
		/** 产品名称 */
		h.productName.setText(String.valueOf(map.get(BocInvt.BOCI_PRODNAME_RES)));

		/** 起售金额 */
		String mBuyAmout = getBuyAmout((String) map.get(BocInvt.SUBAMOUNT));
		String mcurCode = getcurCode(String.valueOf(map
				.get(BocInvt.BOCI_CURCODE_RES)));
		h.productBuyAmout.setText(mBuyAmout + mcurCode);
		// h.productBuyAmout.setTextColor(mContext.getResources().getColor(
		// R.color.red));
		// h.productBuyAmout.setText(StringUtil.parseStringPattern(
		// (String) map.get(BocInvt.SUBAMOUNT), 2));
		/** 产品期限 */
		switch (Integer.parseInt(map.get("isLockPeriod").toString())) {
		case 0:{//0：非业绩基准产品
			if (map.get("termType").toString().equals("3")) {//产品期限特性,3：无限开放式
				h.productLimit.setText("无固定期限");
			}else {
				h.productLimit.setText(String.valueOf(map.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
			}
		}break;
		case 1:{//1：业绩基准-锁定期转低收益 
			h.productLimit.setText("最低持有"+String.valueOf(map.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
		}break;
		case 2:{//2：业绩基准-锁定期后入账 
			h.productLimit.setText(String.valueOf(map.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
		}break;
		case 3:{//3：业绩基准-锁定期周期滚续
			h.productLimit.setText(String.valueOf(map.get(BocInvt.BOCI_PRODTIMELIMIT_RES)) + "天");
		}break;
		default:
			break;
		}
		/** 预计年收益率 */
		h.productYearlyRR.setTextColor(mContext.getResources().getColor(
				R.color.red));
		setYearlyRR(map, h.productYearlyRR);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
				h.productName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
				h.productYearlyRR);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,
				h.productLimit);
		return convertView;
	}

	private String getcurCode(String curCode) {
		String reult = "";
		if (curCode.equals("001")) {
			reult = "元";

		} else if (curCode.equals("014")) {
			reult = "美元";

		} else if (curCode.equals("012")) {
			reult = "英镑";

		} else if (curCode.equals("013")) {
			reult = "港币";

		} else if (curCode.equals("028")) {
			reult = "加拿大元";

		} else if (curCode.equals("029")) {
			reult = "澳大利亚元";

		} else if (curCode.equals("038")) {
			reult = "欧元";

		} else if (curCode.equals("027")) {
			reult = "日元";

		}
		return reult;
	}

	private String getBuyAmout(String string) {
		String result = "";
		if(string!=null&&!string.equals("")){
		if (string.contains(".")) {
			String[] str = string.split("[.]");
			String second = str[1];

			if (Integer.valueOf(second) > 0) {
				result = StringUtil.parseStringPattern(string, 2);
			} else {
				result = StringUtil.parseStringPattern(string, 0);
			}

			return result;
		} else {
			result = StringUtil.parseStringPattern(string, 0);
			return result;
		}
		}else{
			return result;
		}
	}

	/**
	 * 预计年收益率
	 * 
	 * @param map
	 * @return
	 */
	private void setYearlyRR(Map<String, Object> map, TextView v) {
		if (map.get("issueType").toString().equals("2")) {//净值型产品，显示单位净值
			String str = StringUtil.append2Decimals(map.get("price").toString(), 4);
			SpannableString span_str = new SpannableString(str);
			span_str.setSpan(new AbsoluteSizeSpan(textSize), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			v.setText(span_str);
			return;
		}
		if (StringUtil.isNull((String) map.get(BocInvt.BOCI_YEARLYRR_RES))) {
			v.setText("-");
			return;
		}
		String progressionflag = (String) map.get(BocInvt.PROGRESSIONFLAG);
		if (!StringUtil.isNull(progressionflag) && progressionflag.equals("1")) {
//			v.setText("收益累进");
			setYearRR(v,BocInvestControl.getYearlyRR(map,BocInvt.BOCI_YEARLYRR_RES,BocInvestControl.RATEDETAIL));
			return;
		}
		setYearRR(v,BocInvestControl.getYearlyRR(map,BocInvt.BOCI_YEARLYRR_RES,BocInvestControl.RATEDETAIL));
//		String append2Decimals = StringUtil.append2Decimals((String) map.get(BocInvt.BOCI_YEARLYRR_RES), 2)+ "%";
//		SpannableString spannableString = new SpannableString(append2Decimals);
//		spannableString.setSpan(new AbsoluteSizeSpan(textSize), 0,append2Decimals.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		spannableString.setSpan(new AbsoluteSizeSpan(textSize * 2 / 3),append2Decimals.length() - 1, append2Decimals.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		v.setText(spannableString);
	}
	private void setYearRR(TextView tv,String value){
		int index_mid = value.indexOf("-");
		if (index_mid<0) {
			SpannableString span_str = new SpannableString(value);
			span_str.setSpan(new AbsoluteSizeSpan(textSize), 0, value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			span_str.setSpan(new AbsoluteSizeSpan(textSize * 2 / 3),value.length() - 1, value.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv.setText(span_str);
		}else {
			SpannableString span_str = new SpannableString(value);
			span_str.setSpan(new AbsoluteSizeSpan(textSize), 0, index_mid-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			span_str.setSpan(new AbsoluteSizeSpan(textSize*2/3), index_mid-1, index_mid, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			span_str.setSpan(new AbsoluteSizeSpan(textSize), index_mid+1, value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			span_str.setSpan(new AbsoluteSizeSpan(textSize*2/3), value.length()-1, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv.setText(span_str);
		}
	}

	private void setProgress(TextView v, Map<String, Object> map,
			final int position) {
		String progressionflag = (String) map.get(BocInvt.PROGRESSIONFLAG);
		if (!StringUtil.isNull(progressionflag) && progressionflag.equals("1")) {
			v.setTextColor(mContext.getResources().getColor(R.color.blue));
			v.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			v.setText(mContext.getString(R.string.progression));
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mClickListener != null) {
						mClickListener.onItemClick(null, v, position, position);
					}
				}
			});
			return;
		}
		v.setOnClickListener(null);
		v.getPaint().setFlags(0);
		v.setTextColor(mContext.getResources().getColor(R.color.black));
		v.setText(StringUtil.append2Decimals(
				(String) map.get(BocInvt.BOCI_YEARLYRR_RES), 2));
	}

	public void setViewOnClick(OnItemClickListener onclick) {
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
		public ImageView productLeftBg;
	}
}
