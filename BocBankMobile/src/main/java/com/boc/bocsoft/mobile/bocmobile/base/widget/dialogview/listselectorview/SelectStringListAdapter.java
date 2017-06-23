package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 单选列表dialog 适配器｛不包含底部按钮-（确定和取消）｝
 * 
 * @author yx
 * @Date 2015-11-17
 */

public class SelectStringListAdapter extends BaseAdapter {
	/** 上下文 */
	private Context mContext;
	/** 数据列表 */
	private List<SelectListDialog.Option> mListData = new ArrayList<SelectListDialog.Option>();

	/**
	 * 构造方法
	 */
	public SelectStringListAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**
	 * 构造方法
	 */
	public SelectStringListAdapter(List<SelectListDialog.Option> mList, Context mContext) {
		super();
		this.mContext = mContext;
		if (null != mListData) {
			this.mListData = mList;
		}
		mListData.clear();
	}

	/**
	 * 设置数据
	 * 
	 * @param mList
	 */
	public void setData(List<SelectListDialog.Option> mList) {
		if (null != mListData) {
			this.mListData = mList;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public SelectListDialog.Option getItem(int position) {
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.radio_button_listview_item, null);
			holder.tv_message = (TextView) convertView
					.findViewById(R.id.tv_message);
			holder.view_linear = (View) convertView
					.findViewById(R.id.view_linear);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_message.setText(mListData.get(position).getName());
		if (position == (mListData.size() - 1)) {
			holder.view_linear.setVisibility(View.INVISIBLE);
		} else {
			holder.view_linear.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		/** 名称 */
		TextView tv_message;
		/** 间隔线 */
		View view_linear;
	}
}
