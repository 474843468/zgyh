package com.chinamworld.bocmbci.biz.infoserve.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;

public abstract class DeleteBaseAdapter extends BaseAdapter {

	protected BaseActivity mActivity;
	private float x, ux;
	private float y, uy;
	private int mSelectPosition = -1;
	private int mUnDeletePostion = -1;
	private ListView mListView;

	public DeleteBaseAdapter(BaseActivity activity, ListView listView) {
		mActivity = activity;
		this.mListView = listView;
		mListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (mSelectPosition != -1) {
						mSelectPosition = -1;
						notifyDataSetChanged();
						return true;
					}
				}
				return false;
			}
		});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IViewHodler h = null;
		if (convertView == null) {
			h = getViewHodler();
			View subView = h.getRootView();
			View rootView = addDeleteView(subView);
			convertView = rootView;
			convertView.setTag(h);
		} else {
			h = (IViewHodler) convertView.getTag();
		}
		bindView(position, h, parent);

		setDeleteViewTouchListener(convertView, position, mUnDeletePostion != position);
		mUnDeletePostion = -1;

		View db = convertView.findViewById(R.id.delete);
		if (position == mSelectPosition) {
			db.setVisibility(View.VISIBLE);
		} else {
			db.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	protected void unDeleteListener(int position) {
		mUnDeletePostion = position;
	}

	private void setDeleteViewTouchListener(final View convertView, final int position, boolean isAdd) {
		if (isAdd) {
			convertView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// 当按下时处理
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						// TODO设置背景为选中状态
						// 获取按下时的x轴坐标
						x = event.getX();
						y = event.getY();

					} else if (event.getAction() == MotionEvent.ACTION_UP
							|| event.getAction() == MotionEvent.ACTION_CANCEL) {
						// 松开处理
						// TODO设置背景为未选中正常状态
						// 获取松开时的x坐标
						ux = event.getX();
						uy = event.getY();

						if (Math.abs(x - ux) > 10) {
							View deleteView = convertView.findViewById(R.id.delete);
							int deleteViewWidth = deleteView.getWidth();
							int deleteViewHeight = deleteView.getHeight();
							int convertViewWidth = convertView.getWidth();
							int convertViewHeight = convertView.getHeight();

							MarginLayoutParams ml = (MarginLayoutParams) deleteView.getLayoutParams();
							ml.leftMargin = convertViewWidth - deleteViewWidth - deleteViewWidth / 2;
							ml.topMargin = deleteViewHeight / 2 - deleteViewHeight / 2;
							deleteView.setLayoutParams(ml);
							mSelectPosition = position;
							notifyDataSetChanged();
						}
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						// TODO当滑动时背景为选中状态
					}

					return true;
				}
			});
		} else {
			convertView.setOnTouchListener(null);
		}
	}

	private View addDeleteView(View subView) {
		final View deleteView = View.inflate(mActivity, R.layout.base_delete_item, null);
		ViewGroup subLayoutGroup = (ViewGroup) deleteView.findViewById(R.id.layout_view);
		subLayoutGroup.addView(subView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		return deleteView;
	}

	protected abstract void bindView(int position, IViewHodler viewHolder, ViewGroup parent);

	protected abstract IViewHodler getViewHodler();

}
