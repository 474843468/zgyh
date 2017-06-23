package com.boc.bocsoft.remoteopenacc.buss.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.boc.bocsoft.remoteopenacc.R;

/**
 * 只有一个ListView和底部按钮的侧边滑出view
 * 
 * @author gwluo
 * 
 */
public class BaseSideListView extends LinearLayout {
	protected Context mContext;
	private ListView lv;
	private TextView tv_title;
	private RelativeLayout rl_title;
	private Button btn_makesure;

	public BaseSideListView(Context context) {
		super(context);
		mContext = context;
		initView();
		initData();
	}

	public final static String TITLE = "title";

	private void initData() {
		mViewId = new int[1];
		mViewId[0] = R.id.tv_is_cbr_se_base_list_item;
		mContents = new String[1];
		mContents[0] = TITLE;
	}

	private void initView() {
		View.inflate(mContext, R.layout.bocro_base_list_view, this);
		lv = (ListView) findViewById(R.id.lv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		btn_makesure = (Button) findViewById(R.id.btn_makesure);
		btn_makesure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (btnListener != null) {
					List<String> dataList = new ArrayList<String>();
					List<Boolean> isCheckList = new ArrayList<Boolean>();
					for (int i = 0; i < lv.getCount(); i++) {
						View itemAtPosition = lv.getChildAt(i);// 获取包含名称和复选框的item
						// 将内容存入集合
						TextView content = (TextView) itemAtPosition
								.findViewById(R.id.tv_list_item);
						dataList.add(content.getText().toString());
						// 将是否选中boolean值存入集合
						isCheckList.add(((CheckBox) itemAtPosition
								.findViewById(R.id.cb_check)).isChecked());
					}
					btnListener.onClickListener(dataList, isCheckList);
				}
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mListener != null) {
					mListener.onItemClickListener(position);
				}
			}
		});
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		if (text == null || text.length() == 0) {
			rl_title.setVisibility(View.GONE);
			tv_title.setText("");
		} else {
			rl_title.setVisibility(View.VISIBLE);
			tv_title.setText(text);
		}
	}

	protected List<Map<String, Object>> mSimpleList = new ArrayList<Map<String, Object>>();
	protected int[] mViewId;
	protected String[] mContents;

	public void setData(List<Map<String, Object>> list, String[] contents,
			int[] viewId) {
		if (contents != null) {
			mContents = contents;
		}
		if (viewId != null) {
			mViewId = viewId;
		}
		mSimpleList = list;
		BaseAdapter adapter = getSimpleAdapter();
		if (adapter != null) {
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 设置数据，使用自己adapter
	 */
	public void setListViewAdapter(BaseAdapter adapter) {
		if (adapter != null) {
			lv.setAdapter(adapter);
		}
	}

	protected List<? extends Object> mList;

	public void setData(List<? extends Object> list) {
		mList = list;
		BaseAdapter adapter = getAdapter();
		if (adapter != null) {
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	protected BaseAdapter getSimpleAdapter() {
		SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, mSimpleList,
				R.layout.bocroa_base_list_item_view, mContents, mViewId);
		return simpleAdapter;
	}

	protected BaseAdapter getSimpleAdapter(int layout) {
		SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, mSimpleList,
				layout, mContents, mViewId);
		return simpleAdapter;
	}

	protected BaseAdapter getAdapter() {
		return null;
	}

	private BaseSideListViewListener mListener;

	public void setCallBackListener(BaseSideListViewListener listener) {
		mListener = listener;
	}

	/**
	 * 侧边基类listview监听
	 * 
	 * @author gwluo
	 * 
	 */
	public interface BaseSideListViewListener {
		public void onItemClickListener(int pos);
	}

	/**
	 * 确定按钮监听
	 * 
	 * @author gwluo
	 * 
	 */
	public interface BaseSideListViewBtnListener {
		public void onClickListener(List<String> dataList,
				List<Boolean> isCheckList);
	}

	private BaseSideListViewBtnListener btnListener;

	public void setBtnClickListener(BaseSideListViewBtnListener listener) {
		btnListener = listener;
		btn_makesure.setVisibility(View.VISIBLE);
	}
}
