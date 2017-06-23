package com.chinamworld.bocmbci.biz.bond.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 债券持仓ListView适配器
 * 
 * @author panwe
 * 
 */
public class BondGridViewAdapter extends BaseAdapter {

	private OnItemClickListener mOnClickListener;
	private Context mContext;
	private List<Map<String, Object>> mList;

	public BondGridViewAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (mList != null && mList.size() > 0) {
			return mList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mList != null && mList.size() > 0) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder h;
		LayoutParams lp;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bond_gridview_item, null);
			h = new ViewHolder();
			h.bondName = (TextView) convertView.findViewById(R.id.tv_bond_name);
			h.balance = (TextView) convertView
					.findViewById(R.id.tv_shiji_price);
			h.availabl = (TextView) convertView.findViewById(R.id.tv_key_price);
			h.btnSell = (LinearLayout) convertView.findViewById(R.id.tv_sell);
			convertView.setTag(h);
		} else {
			h = (ViewHolder) convertView.getTag();
		}
		// 只有一条数据
		if (getCount() == 1) {
			convertView.setBackgroundResource(R.drawable.prms_img_card_only);
			lp = getButtomLayoutParams();
			// 多条数据
		} else {
			if (position == 0) {
				convertView.setBackgroundResource(R.drawable.prms_img_card_top);
				lp = getTopLayoutParams();
			} else if (position+1 == getCount()) {
				convertView
						.setBackgroundResource(R.drawable.prms_img_card_buttom);
				lp = getButtomLayoutParams();
			} else {
				convertView.setBackgroundResource(R.drawable.prms_img_card_mid);
				lp = getMidLayoutParams();
			}
		}
		convertView.setLayoutParams(lp);

		Map<String, Object> dataMap = mList.get(position);
		h.bondName.setText((String) dataMap.get(Bond.BOND_SHORTNAME));

//		换肤，左灰色右黑色
//		h.balance.setText(Html.fromHtml("实际面额：<font color=\"#ba001d\">"+ (String) dataMap.get(Bond.MYBOND_BALAC) + "</font>"));
//		h.availabl.setText(Html.fromHtml("可用面额：<font color=\"#ba001d\">"+ (String) dataMap.get(Bond.MYBOND_AVAFACE) + "</font>"));
		String str_tem="实际面额："+ String.valueOf(dataMap.get(Bond.MYBOND_BALAC));
		String str_tem_1="可用面额："+ String.valueOf(dataMap.get(Bond.MYBOND_AVAFACE));
		setSpanStr(h.balance,str_tem, str_tem.length());
		setSpanStr(h.availabl,str_tem_1, str_tem_1.length());


		h.btnSell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mOnClickListener != null) {
					mOnClickListener.onItemClick(null, v, position, position);
				}
			}
		});

		return convertView;
	}

	/**
	 * 将textview的内容字串指定位置左边置灰、右边置红
	 * @param tv 显示内容的textview
	 * @param str 要显示的字串内容
	 * @return
	 */
	private void setSpanStr(TextView tv,String str,int position_split){
		if (tv==null||StringUtil.isNullOrEmpty(str)||position_split<=0){
			return ;
		}
		SpannableStringBuilder span_str = new SpannableStringBuilder(str);
		span_str.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.fonts_dark_gray)),0,position_split, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		span_str.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.fonts_pink)),position_split,span_str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(span_str);
	}

	/** 卖出点击事件 */
	public void setImageViewClick(OnItemClickListener onclick) {
		this.mOnClickListener = onclick;
	}

	/** 头部卡片的布局尺寸 */
	private LayoutParams getTopLayoutParams() {
		int height = (int) mContext.getResources().getDimension(
				R.dimen.prms_accbalance_top_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	/** 中间卡片的布局尺寸 */
	private LayoutParams getMidLayoutParams() {
		int height = (int) mContext.getResources().getDimension(
				R.dimen.prms_accbalance_mid_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	/** 底部卡片的布局尺寸 */
	private LayoutParams getButtomLayoutParams() {
		int height = (int) mContext.getResources().getDimension(
				R.dimen.prms_accbalance_buttom_height);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		return lp;
	}

	public final class ViewHolder {
		public TextView bondName;
		public TextView balance;
		public TextView availabl;
		public LinearLayout btnSell;
	}
}
