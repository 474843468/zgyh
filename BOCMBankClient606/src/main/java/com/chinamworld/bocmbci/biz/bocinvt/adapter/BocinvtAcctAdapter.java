package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 *理财账户
 * @author panwe
 *
 */
public class BocinvtAcctAdapter extends BaseAdapter{
	private int iconSize;
	private Context mContext;
	private List<Map<String, Object>> mList;
	private OnItemClickListener mOnClickListener;
	
	public BocinvtAcctAdapter(Context c,List<Map<String, Object>> list){
		this.mContext = c;
		this.mList = list;
		iconSize = mContext.getResources().getDimensionPixelSize(R.dimen.dp_for_zero)/2;
	}

	@Override
	public int getCount() {
		if (StringUtil.isNullOrEmpty(mList)) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(mList)) {
			return null;
		}
		return mList.get(position);
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
			convertView = View.inflate(mContext, R.layout.bocinvt_acct_item, null);
			h.mTextAcctType = (TextView) convertView.findViewById(R.id.textacctType);
			h.mTextAcctNum = (TextView) convertView.findViewById(R.id.textacctNum);
			h.mTextView1 = (TextView) convertView.findViewById(R.id.text1);
			h.mTextView2 = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		final Map<String, Object> map = mList.get(position);
		String accountType = (String) map.get(Comm.ACCOUNT_TYPE);
		String accountId = (String) map.get(Comm.ACCOUNT_ID);
		
		h.mTextAcctNum.setText(StringUtil.getForSixForString((String)map.get(BocInvt.ACCOUNTNO)));
		h.mTextAcctType.setText(LocalData.AccountType.get(accountType));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,h.mTextView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(mContext,h.mTextView2);
		
		if (!StringUtil.isNull(accountType)) {
			if (accountType.equals("119") || accountType.equals("188")) {
				if (StringUtil.isNull(accountId)) {
					h.mTextView1.setClickable(false);
					h.mTextView1.setText("【未关联电子银行】");
					h.mTextView2.setText("自助关联");
					h.mTextView1.setTextColor(mContext.getResources().getColor(R.color.black));
					Drawable drawable = mContext.getResources().getDrawable(R.drawable.bocinvt_associat);
					drawable.setBounds(0, 0, iconSize, iconSize);
					h.mTextView1.setCompoundDrawables(null, null, null, null);
					h.mTextView2.setCompoundDrawables(drawable, null, null, null);
				}else{
					h.mTextView1.setClickable(false);
					h.mTextView1.setText("【已关联电子银行】");
					h.mTextView2.setText("取消登记");
					h.mTextView1.setTextColor(mContext.getResources().getColor(R.color.black));
					Drawable drawable = mContext.getResources().getDrawable(R.drawable.bocinvt_canceregister);
					drawable.setBounds(0, 0, iconSize, iconSize);
					h.mTextView1.setCompoundDrawables(null, null, null, null);
					h.mTextView2.setCompoundDrawables(drawable, null, null, null);
				}
			}else{
				h.mTextView1.setClickable(true);
				h.mTextView1.setText("资金转出");
				h.mTextView2.setText("绑定状态查询");
				h.mTextView1.setTextColor(mContext.getResources().getColor(R.color.gray));
				Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.bocinvt_amout_tranfer);
				Drawable drawable2 = mContext.getResources().getDrawable(R.drawable.bocinvt_bind_query);
				drawable1.setBounds(0, 0, iconSize, iconSize);
				drawable2.setBounds(0, 0, iconSize, iconSize);
				h.mTextView1.setCompoundDrawables(drawable1, null, null, null);
				h.mTextView2.setCompoundDrawables(drawable2, null, null, null);
			}
		}
		h.mTextView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnClickListener != null && v.isClickable()) {
					mOnClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		h.mTextView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnClickListener != null ) {
					mOnClickListener.onItemClick(null, v, position, position);
				}
			}
		});
		return convertView;
	}
	
	public List<Map<String, Object>> getmList() {
		return mList;
	}

	public void setmList(List<Map<String, Object>> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	public void setViewOnClick (OnItemClickListener onclick){
		this.mOnClickListener = onclick;
	}

	public class ViewHodler {
		public TextView mTextAcctType;
		public TextView mTextAcctNum;
		public TextView mTextView1;
		public TextView mTextView2;
	}
}
