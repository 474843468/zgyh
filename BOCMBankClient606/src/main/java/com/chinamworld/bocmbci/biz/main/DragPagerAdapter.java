package com.chinamworld.bocmbci.biz.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.main.DragController.DragListener;
import com.chinamworld.bocmbci.log.LogGloble;

public class DragPagerAdapter extends BaseDragPagerAdapter {

	private static final String TAG = DragPagerAdapter.class.getSimpleName();
	private List<Item> mItemLists;
	private List<GridView> mViews;
	private DragListener mDragListener;
	private OnClickListener mOnClickListener;

	public DragPagerAdapter(Context context, DragController dragController, List<Item> lists, ViewPager viewPage,
			int row, int clocumn) {
		super(context, dragController, viewPage, row, clocumn);
		this.mItemLists = lists;
		mViews = initGridView();
	}

	public void setOnDragListener(DragListener listener) {
		mDragListener = listener;
	}

	public void setOnClickListener(OnClickListener listener) {
		mOnClickListener = listener;
	}

	private List<GridView> initGridView() {
		ArrayList<GridView> views = new ArrayList<GridView>();
		for (int i = 0; i < getCount(); i++) {
			final int page = i;
			final GridView gridView = createGridView(page, getColumn());
			GridAdapter adapter = new GridAdapter(mContext, mItemLists, page);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					if (mOnClickListener != null) {
						mOnClickListener.onClick(view);
					}
				}
			});
			gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					Item popItem = null;
					for (Item item : mItemLists) {
						if (item.getLocation().getCurrentPosition() == position
								&& item.getLocation().getCurrentPage() == page) {
							popItem = item;
							break;
						}
					}
					if (popItem != null) {
						onDragEvent(view, popItem);
					}
					return true;
				}
			});
			views.add(gridView);
		}
		return views;
	}

	@Override
	public int getCount() {
		int page = 0;
		for (Item item : mItemLists) {
			// 页是从0开始的
			int p = item.getLocation().getCurrentPage() + 1;
			if (p > page) {
				page = p;
			}
		}
		return page;
	}

	@Override
	public void stopAnimation() {
		super.stopAnimation();
		for (Item it : mItemLists) {
			ItemPosition location = it.getLocation();
			if (location != null) {
				location.setPage(location.getCurrentPage());
				location.setCurrentPosition(location.getCurrentPosition());
			}
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		GridView gridView = mViews.get(position);
		// TODO 适配
		LogGloble.i(TAG, "container height" + container.getMeasuredHeight());
//		LogGloble.i(TAG, "Spacing height" + container.getMeasuredHeight() / 40);
//		LogGloble.i(TAG, "gridView height" + gridView.getMeasuredHeight());

		container.addView(gridView);
		return mViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViews.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void onDragStart(View view, Object tag) {
		super.onDragStart(view, tag);
		Item item = (Item) tag;
		if (item != null) {
			ItemPosition location = item.getLocation();
			if (location != null) {
				item.getLocation().setVisibility(View.INVISIBLE);
				refreshAndAnimationGrieView();
			} else {
				LogGloble.e(TAG, item.getName() + ":onDragStart item location is null");
			}
		}

		if (mDragListener != null) {
			mDragListener.onDragStart(view, tag);
		}
	}

	@Override
	public void onDragEnd(DragView view, Object tag) {
		super.onDragEnd(view, tag);
//		Item item = (Item) tag;
//		if (item != null) {
//			item.getLocation().setVisibility(View.VISIBLE);
//		}
		for (Item it : mItemLists) {
			ItemPosition location = it.getLocation();
			if (location != null) {
				location.setPage(location.getCurrentPage());
				location.setPosition(location.getCurrentPosition());
				location.setVisibility(View.VISIBLE);
			} else {
				LogGloble.e(TAG, it.getName() + ":onDragEnd item location is null");
			}
		}
		refreshAndAnimationGrieView();

		if (mDragListener != null) {
			mDragListener.onDragEnd(view, tag);
		}
	}

	// -----------------------------------------------------------

	// -------------------------------------------------------------------------------------------
	private GridView createGridView(int page, int column) {
		GridView gridView = new GridView(mContext);
		gridView.setId(page);
		gridView.setNumColumns(column);
		gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int windowHeight = wm.getDefaultDisplay().getHeight();
		if (windowHeight == 800) {
			gridView.setVerticalSpacing(windowHeight / 19);
		}else if(windowHeight == 854){
			gridView.setVerticalSpacing(windowHeight / 15);
		} else {
			gridView.setVerticalSpacing(windowHeight / 18);
		}

		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setScrollbarFadingEnabled(true);
		gridView.setHorizontalFadingEdgeEnabled(true);
		gridView.setVerticalFadingEdgeEnabled(true);
		gridView.setFadingEdgeLength(0);
		gridView.setBackgroundColor(Color.TRANSPARENT);
		return gridView;
	}

	private class GridAdapter extends BaseAdapter {

		private Context _context;
		private List<Item> _items;
		private int _page;

		public GridAdapter(Context context, List<Item> items, int page) {
			super();
			this._context = context;
			this._items = items;
			this._page = page;
		}

		@Override
		public int getCount() {
			int size = 0;
			for (Item item : mItemLists) {
				if (item.getLocation().getCurrentPage() == _page) {
//					size++;
					int cp = item.getLocation().getCurrentPosition() + 1;
					if (size < cp) {
						size = cp;
					}
				}
			}
			return size;
		}

		@Override
		public Object getItem(int position) {
			return _items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			clearDragAnimation();
			View view = View.inflate(_context, R.layout.grid_item_img_text_2, null);
			ImageView iv = (ImageView) view.findViewById(R.id.grid_icon);
			TextView tv = (TextView) view.findViewById(R.id.grid_text);

			for (Item item : mItemLists) {
				if (item.getLocation().getCurrentPage() == _page && item.getLocation().getCurrentPosition() == position) {
//					tv.setText(item.getLocation().getPage() + "-" + item.getLocation().getPosition() + "|"
//							+ item.getName());
					tv.setText(item.getName());
					iv.setImageResource(item.getDrawableResourcesId());
					view.setTag(item);
					view.setVisibility(item.getLocation().getVisibility());
					break;
				}
			}
			if (view.getTag() == null) {
				LogGloble.e(TAG, "adapter tag null. position :" + position);
//				view.setVisibility(View.INVISIBLE);
			}
			return view;
		}
	}

	@Override
	public List<? extends AbsListView> getGroupViews() {
		return mViews;
	}

}
