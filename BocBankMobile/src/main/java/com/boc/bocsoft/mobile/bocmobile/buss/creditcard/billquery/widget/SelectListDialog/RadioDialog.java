package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.SelectListDialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

import java.util.List;


/**
 * 单选列表 dialog弹框 ｛包含底部按钮-（确定和取消）｝
 * 
 */
public class RadioDialog extends BaseDialog {
	private Context mContext;
	/** 根view */
	protected LinearLayout ll_listview;
	/***
	 * listview
	 */
	protected ListView mListView;
	/**
	 * 当前布局view
	 */
	protected View contentView;
	/**
	 * 单选列表按钮 回调监听
	 */
	protected OnSelectListener<RadioDialog.Option> onSelectListener;
	private RadioDialog.Option selectedItem;
	/** 适配器 */
	protected DialogSelectListAdapter mAdapter;
	/**头部标题*/
	private TextView tv_title;
	/**
	 * 标题下分割线
	 */
	private View title_botline;
	private Button btnLeft,btnRight;
	private View.OnClickListener leftListener,rightListener;

	// ============================================

	/**
	 * 
	 * @param context
	 */
	public RadioDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected View onAddContentView() {
		contentView = inflateView(R.layout.radio_button_dialog);
		return contentView;
	}

	@Override
	protected void initView() {
		mListView = (ListView) contentView.findViewById(R.id.listview);
		ll_listview = (LinearLayout) contentView.findViewById(R.id.ll_listview);
		tv_title= (TextView) contentView.findViewById(R.id.tv_title);
		title_botline=contentView.findViewById(R.id.title_botline);
		btnLeft = (Button) contentView.findViewById(R.id.btn_left);
		btnRight = (Button) contentView.findViewById(R.id.btn_right);
	}

	@Override
	protected void initData() {
		setTitle("");
	}

	@Override
	protected void setListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				for (int i=0;i<mAdapter.getCount();i++){
					mAdapter.getItem(i).setCkecked(false);
				}
				mAdapter.getItem(position).setCkecked(true);
				mAdapter.notifyDataSetChanged();
				selectedItem=mAdapter.getItem(position);
//				if (onSelectListener != null) {
//					onSelectListener.onSelect(position,
//							mAdapter.getItem(position));
//				}

			}
		});

		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (leftListener == null)
					dismiss();
				else
					leftListener.onClick(v);
			}
		});

		btnRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rightListener == null)
					dismiss();
				else
					rightListener.onClick(v);
			}
		});

	}

	public Option getSelectedItem() {
		return selectedItem!=null?selectedItem:mAdapter.getItem(0);
	}

	public void setLeftButtonClickListener(View.OnClickListener listener) {
		this.leftListener = listener;
	}

	public void setRightButtonClickListener(View.OnClickListener listener) {
		this.rightListener = listener;
	}
	/**
	 * 设置左边按钮文字
	 *
	 * @param text
	 */
	public void setLeftButtonText(String text) {
		btnLeft.setText(text);
	}

	/**
	 * 设置右边按钮文字
	 *
	 * @param text
	 */
	public void setRightButtonText(String text) {
		btnRight.setText(text);
	}

	/**
	 * 设置单选列表按钮 点击事件
	 *
	 */
	public void setOnSelectListener(OnSelectListener<RadioDialog.Option> onSelectListener) {
		this.onSelectListener = onSelectListener;
	}

	public static class Option {

		public Option(int id, String name, String value,boolean isChecked) {
			this.id = id;
			this.name = name;
			this.value = value;
			this.isCkecked=isChecked;
		}

		private int id;
		// 画面显示的内容
		private String name;
		// 对应的值
		private String value;
		private boolean isCkecked;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public boolean isCkecked() {
			return isCkecked;
		}

		public void setCkecked(boolean ckecked) {
			isCkecked = ckecked;
		}
	}

	public void setListData(List<RadioDialog.Option> list) {
		mAdapter.setData(list);
		if (list.size() > 0) {
			setListViewHeightBasedOnChildren();
		}
	}

	public void setAdapter(DialogSelectListAdapter adapter) {
		this.mAdapter = adapter;
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 选择一条的回调
	 * 
	 * @author lxw4566
	 * 
	 */
	public static interface OnSelectListener<Option> {
		public void onSelect(int position, RadioDialog.Option model);
	}

	// 定义一个函数将dp转换为像素
	public int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	// 定义函数动态控制listView的高度
	public void setListViewHeightBasedOnChildren() {
		// 获取listview的适配器
		DialogSelectListAdapter listAdapter = (DialogSelectListAdapter) mListView
				.getAdapter();
		// item的高度 int itemHeight = 46;
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		if (listAdapter.getCount() > 5) {
			View listItem = mAdapter.getView(0, null, mListView);
			listItem.measure(0, 0);
			// 计算子项View 的宽高
			// 统计所有子项的总高度
			// totalHeight = Dp2Px(mContext,43);
			totalHeight = (Dp2Px(mContext, 43) * 5) + (Dp2Px(mContext, 43) / 2);
//			LogUtils.d("yx-----大于5条--->" + totalHeight);
		} else {// -2 自适应高度
			totalHeight = -2;
//			LogUtils.d("yx-----小于5条--->" + totalHeight);
		}
		ViewGroup.LayoutParams params = mListView.getLayoutParams();
		params.height = totalHeight;
		mListView.setLayoutParams(params);
		mListView.requestLayout();

		ViewGroup.LayoutParams paramsll = ll_listview.getLayoutParams();
		paramsll.height = totalHeight;
		ll_listview.setLayoutParams(params);
		ll_listview.requestLayout();
	}
	/**
	 * 是否显示头部标题
	 */
	public void isShowHeaderTitle(boolean isshow){
		if(isshow){
			tv_title.setVisibility(View.VISIBLE);
			title_botline.setVisibility(View.VISIBLE);
		}else{
			tv_title.setVisibility(View.GONE);
			title_botline.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置头部标题内容
	 * @param title
     */
	public void setHeaderTitleValue(String title){
		tv_title.setText(title);
		isShowHeaderTitle(true);
	}
}
