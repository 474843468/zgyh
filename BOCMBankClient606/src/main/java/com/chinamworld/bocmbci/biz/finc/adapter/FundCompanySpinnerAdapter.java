package com.chinamworld.bocmbci.biz.finc.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.finc.control.entity.FundCompany;

public class FundCompanySpinnerAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<FundCompany> companyList ;
	private HashMap<String, Integer> alphaIndexer; // 存放存在的汉语拼音首字母和与之对应的列表位置
	

	public FundCompanySpinnerAdapter(Context context,List<FundCompany> companyList) {
		this.inflater = LayoutInflater.from(context);
		this.companyList = companyList ;
//		textData();
		alphaIndexer = new HashMap<String, Integer>();
		for (int i = 0; i < this.companyList.size(); i++) {
			// 当前汉语拼音首字母
			String currentAlpha = this.companyList.get(i).getAlpha();
			// 上一个汉语拼音首字母，如果不存在为“ ”
			String previewAlpha = (i - 1) >= 0 ? this.companyList.get(i - 1).getAlpha()
					: " ";
			if (!previewAlpha.equals(currentAlpha)) {
				String alpha = this.companyList.get(i).getAlpha();
				alphaIndexer.put(alpha, i);
			}
		}
	}
	
	public HashMap<String, Integer> getAlphaMap(){
		return alphaIndexer;
	}

	@Override
	public int getCount() {
		return companyList.size();
	}

	@Override
	public Object getItem(int position) {
		return companyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.finc_company_name_spinner_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FundCompany item = companyList.get(position);
		holder.text.setText(item.getFundCompanyName());
		String currentAlpha = companyList.get(position).getAlpha();
		String previewAlpha = (position - 1) >= 0 ? companyList.get(position - 1)
				.getAlpha() : " ";
		if (!previewAlpha.equals(currentAlpha)) {
			holder.alpha.setVisibility(View.VISIBLE);
			if("#".equals(currentAlpha))
				currentAlpha = "其他";
			holder.alpha.setText(currentAlpha);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private final class ViewHolder {
		TextView alpha;
		TextView text;
		public ViewHolder(View v) {
			alpha = (TextView) v.findViewById(R.id.alpha_text);
			text = (TextView) v.findViewById(R.id.tv_company);
		}
	}

}




