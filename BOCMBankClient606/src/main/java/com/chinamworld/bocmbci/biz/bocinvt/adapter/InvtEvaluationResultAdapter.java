package com.chinamworld.bocmbci.biz.bocinvt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.LocalData;

import java.util.List;

/**
 * 风险等级适配器
 * 
 * @author wangmengmeng
 * 
 */
public class InvtEvaluationResultAdapter extends BaseAdapter {
	/** 风险等级信息 */
	private List<String> resultList;
	private Context context;
	private int selectPosition = -1;

	public InvtEvaluationResultAdapter(Context context, List<String> resultList) {
		this.context = context;
		this.resultList = resultList;
	}

	@Override
	public int getCount() {

		return resultList.size();
	}

	@Override
	public Object getItem(int position) {

		return resultList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bocinvt_evaluation_result_item, null);
		}
		// 风险评估等级
		TextView riskLevel = (TextView) convertView
				.findViewById(R.id.riskLevel);
		riskLevel.setText(LocalData.riskLevelMap.get(resultList.get(position)));
		// 等级详情
		TextView detail = (TextView) convertView.findViewById(R.id.detail);
		detail.setText(LocalData.riskLevelDetailMap.get(resultList
				.get(position)));
		// 三角
		ImageView img = (ImageView) convertView.findViewById(R.id.img_down);
		img.setImageResource(R.drawable.img_arrow_gray_down606);
		if (selectPosition == -1) {
			// 都不显示
			detail.setVisibility(View.GONE);
			img.setImageResource(R.drawable.img_arrow_gray_down606);
		} else {
			if (position == selectPosition) {
				detail.setVisibility(View.VISIBLE);
				img.setImageResource(R.drawable.img_arrow_gray_up606);
			} else {
				detail.setVisibility(View.GONE);
				img.setImageResource(R.drawable.img_arrow_gray_down606);
			}
		}

		return convertView;
	}

	public int getSelectPosition() {
		return selectPosition;
	}

	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
		notifyDataSetChanged();
	}

}
