package com.chinamworld.bocmbci.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * @ClassName: LoadMoreListView
 * @Description: 自定义加载更多ListView
 * @author lql
 * @date 2013-8-20 上午10:32:03
 */

public class LoadMoreListView extends ListView {

	private static final String TAG = LoadMoreListView.class.getSimpleName();

	private View mFooterView;
	private ViewGroup mFooterLayoutView;
	private ProgressBar mProgressBarView;
	private TextView mTextViewView;
	private OnLoadMoreListener mOnLoadMoreListener;

	/**
	 * @ClassName: Status
	 * @Description: ListView状态
	 */
	public enum Status {
		NORMAL, LOADING
	}

	public interface OnLoadMoreListener {
		public void onLoadMore(Status status);
	}

	public LoadMoreListView(Context context) {
		this(context, null);
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		initFooterView(View.inflate(getContext(), R.layout.load_more_footer, null));
	}

	public void setOnLoadMoreListener(OnLoadMoreListener listener) {
		mOnLoadMoreListener = listener;
	}

	/**
	 * 加载更多完成
	 */
	public void onLoadMoreComplete() {
		refreshView(true, Status.NORMAL);
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore(Status.NORMAL);
		}
	}

	/**
	 * 开始加载更多
	 */
	public void onLoadMore() {
		refreshView(true, Status.LOADING);
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore(Status.LOADING);
		}
	}

	/**
	 * 显示加载更多view
	 */
	public void showLoadMoreView() {
		refreshView(true, Status.NORMAL);
	}

	/**
	 * 隐藏加载更多view
	 */
	public void hideLoadMoreView() {
		refreshView(false, Status.NORMAL);
	}

	/**
	 * 加载更多view显示状态
	 */
	public int getFooterVisibility() {
		if (mFooterLayoutView != null) {
			return mFooterLayoutView.getVisibility();
		}
		return View.GONE;
	}

	/**
	 * 在设置Adapter之前调用 View必要包含ProgressBar和TextView ProgressBar
	 * android:id="@+id/load_more_progressbar" TextView
	 * android:id="@+id/load_more_text" ViewGroup
	 * android:id="@+id/layout_load_more" 参考
	 * <p>
	 * R.layout.load_more_footer.xml
	 * </p>
	 */
	public void setFooterView(View view) {
		initFooterView(view);
	}

	@Override
	public void addFooterView(View v) {
		super.addFooterView(v);
		if (v != mFooterView) {
			throw new IllegalStateException("不能手动调用addFooterView");
		}
	}

	@Override
	public void addFooterView(View v, Object data, boolean isSelectable) {
		super.addFooterView(v, data, isSelectable);
		if (v != mFooterView) {
			throw new IllegalStateException("不能手动调用addFooterView");
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	private void initFooterView(View view) {
		if (getFooterViewsCount() > 0) {
			if (mFooterView != null) {
				removeFooterView(mFooterView);
			}
		}
		mFooterView = view;

		mFooterLayoutView = (ViewGroup) mFooterView.findViewById(R.id.layout_load_more);
		mProgressBarView = (ProgressBar) mFooterView.findViewById(R.id.load_more_progressbar);
		mTextViewView = (TextView) mFooterView.findViewById(R.id.load_more_text);

		if (!checkView()) {
			//XXX
		}

		addFooterView(mFooterView);
		hideLoadMoreView();
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTextViewView.getVisibility() == View.VISIBLE) {
					onLoadMore();
				}
			}
		});

	}

	private void refreshView(boolean isShowFooterView, Status status) {
		if (mFooterView == null) {
			LogGloble.e(TAG, "LoadMoreListView footerview is null,refreshView failed");
			return;
		}
		mFooterLayoutView.setVisibility(isShowFooterView ? View.VISIBLE : View.GONE);
		mTextViewView.setVisibility(status == Status.NORMAL ? View.VISIBLE : View.GONE);
		mProgressBarView.setVisibility(status == Status.NORMAL ? View.GONE : View.VISIBLE);
	}

	private boolean checkView() {
		return mFooterView != null && mFooterLayoutView != null && mTextViewView != null && mProgressBarView != null;
	}
}
