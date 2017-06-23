package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview;

import android.content.Context;

/**
 * 单选列表 dialog弹框 ｛不包含底部按钮-（确定和取消）｝
 * 
 * @author yx
 * @date 2015-11-17
 */
public class SelectCenterStringListDialog extends SelectCenterListDialog<String> {
	private Context mContext;

	/**
	 * 构造函数
	 * @param context
	 */
	public SelectCenterStringListDialog(Context context) {
		super(context);
		mContext = context;
		mAdapter = new SelectListAdapter<String>(mContext){
			@Override
			public String displayValue(String model) {
				return model;
			}
			
		};
		mListView.setAdapter(mAdapter);
	}


	/**
	 * 设置数据
	 * 
	 * @param mList
	 *            数据集合
	 */
//	@Override
//	public void setListData(List<String> list) {
//		mAdapter.setData(list);
//	}

}
