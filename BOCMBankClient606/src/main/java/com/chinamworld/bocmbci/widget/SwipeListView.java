/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chinamworld.bocmbci.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.acc.adapter.ACCBankAccountAdapter;

/**
 * ListView subclass that provides the swipe functionality
 */
public class SwipeListView extends ListView {

	/**
	 * Used when user want change swipe list mode on some rows
	 */
	public final static int SWIPE_MODE_DEFAULT = -1;

	/**
	 * Disables all swipes
	 */
	public final static int SWIPE_MODE_NONE = 0;

	/**
	 * Enables both left and right swipe
	 */
	public final static int SWIPE_MODE_BOTH = 1;

	/**
	 * Enables right swipe
	 */
	public final static int SWIPE_MODE_RIGHT = 2;

	/**
	 * Enables left swipe
	 */
	public final static int SWIPE_MODE_LEFT = 3;

	/**
	 * Binds the swipe gesture to reveal a view behind the row (Drawer style)
	 */
	public final static int SWIPE_ACTION_REVEAL = 0;

	/**
	 * Dismisses the cell when swiped over
	 */
	public final static int SWIPE_ACTION_DISMISS = 1;

	/**
	 * Marks the cell as checked when swiped and release
	 */
	public final static int SWIPE_ACTION_CHECK = 2;

	/**
	 * No action when swiped
	 */
	public final static int SWIPE_ACTION_NONE = 3;

	/**
	 * Default ids for front view
	 */
	public final static String SWIPE_DEFAULT_FRONT_VIEW = "swipelist_frontview";

	/**
	 * Default id for back view
	 */
	public final static String SWIPE_DEFAULT_BACK_VIEW = "swipelist_backview";

	/**
	 * Indicates no movement
	 */
	private final static int TOUCH_STATE_REST = 0;

	/**
	 * State scrolling x position
	 */
	private final static int TOUCH_STATE_SCROLLING_X = 1;

	/**
	 * State scrolling y position
	 */
	private final static int TOUCH_STATE_SCROLLING_Y = 2;

	private int touchState = TOUCH_STATE_REST;

	private float lastMotionX;
	private float lastMotionY;
	private int touchSlop;

	int swipeFrontView = 0;
	int swipeBackView = 0;

	/**
	 * Internal listener for common swipe events
	 */
	private BaseSwipeListViewListener swipeListViewListener;

	/**
	 * Internal touch listener
	 */
	private SwipeListViewTouchListener touchListener;

	// add by xby dragListview
	private final float mAlpha = 0.9f;
	private ImageView mDragView;
	private Context mContext;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	private int mDragStartPosition;
	private int mDragCurrentPostion;
	private int mScaledTouchSlop;
	private int mDragOffsetX;
	private int mDragOffSetY;
	private int mDragPointX;
	private int mDragPointY;
	private int mUpperBound;
	private int mLowerBound;
	private DropViewListener mDropViewListener;
	/** 是否在拖拽 */
	private boolean isDragging = false;

	/** 是否支持拖拽 */
	private boolean canDrag = false;

	private ACCBankAccountAdapter dragAdapter;
	/** 当前指向位置 */
	private int tempPosition;

	/**
	 * 拖拽条目的x值
	 */
	private int dragX;

	// add by wjp
	// private List<Integer> disableClickItems;

	/**
	 * If you create a View programmatically you need send back and front
	 * identifier
	 * 
	 * @param context
	 *            Context
	 * @param swipeBackView
	 *            Back Identifier
	 * @param swipeFrontView
	 *            Front Identifier
	 */
	public SwipeListView(Context context, int swipeBackView, int swipeFrontView) {
		super(context);
		mContext = context;
		this.swipeFrontView = swipeFrontView;
		this.swipeBackView = swipeBackView;
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
		init(null);
	}

	public SwipeListView(Context context, int swipeFrontView) {
		super(context);
		mContext = context;
		this.swipeFrontView = swipeFrontView;
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
		init(null);
	}

	/**
	 * @see android.widget.ListView#ListView(android.content.Context,
	 *      android.util.AttributeSet)
	 */
	public SwipeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
		mContext = context;
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
	}

	/**
	 * @see android.widget.ListView#ListView(android.content.Context,
	 *      android.util.AttributeSet, int)
	 */
	public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
		mContext = context;
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
	}

	/**
	 * Init ListView
	 * 
	 * @param attrs
	 *            AttributeSet
	 */
	private void init(AttributeSet attrs) {

		int swipeMode = SWIPE_MODE_BOTH;
		boolean swipeOpenOnLongPress = false;
		// boolean swipeCloseAllItemsWhenMoveList = true;
		long swipeAnimationTime = 0;
		float swipeOffsetLeft = 0;
		float swipeOffsetRight = 0;

		int swipeActionLeft = SWIPE_ACTION_REVEAL;
		int swipeActionRight = SWIPE_ACTION_REVEAL;

		if (attrs != null) {
			TypedArray styled = getContext().obtainStyledAttributes(attrs,
					R.styleable.SwipeListView);
			swipeMode = styled.getInt(R.styleable.SwipeListView_swipeMode,
					SWIPE_MODE_BOTH);
			swipeActionLeft = styled.getInt(
					R.styleable.SwipeListView_swipeActionLeft,
					SWIPE_ACTION_REVEAL);
			swipeActionRight = styled.getInt(
					R.styleable.SwipeListView_swipeActionRight,
					SWIPE_ACTION_REVEAL);
			swipeOffsetLeft = styled.getDimension(
					R.styleable.SwipeListView_swipeOffsetLeft, 0);
			swipeOffsetRight = styled.getDimension(
					R.styleable.SwipeListView_swipeOffsetRight, 0);
			// swipeOpenOnLongPress =
			// styled.getBoolean(R.styleable.SwipeListView_swipeOpenOnLongPress,
			// true);
			swipeAnimationTime = styled.getInteger(
					R.styleable.SwipeListView_swipeAnimationTime, 0);
			// swipeCloseAllItemsWhenMoveList =
			// styled.getBoolean(R.styleable.SwipeListView_swipeCloseAllItemsWhenMoveList,
			// true);
			swipeFrontView = styled.getResourceId(
					R.styleable.SwipeListView_swipeFrontView, 0);
			swipeBackView = styled.getResourceId(
					R.styleable.SwipeListView_swipeBackView, 0);
		}

		if (swipeFrontView == 0) {
			swipeFrontView = getContext().getResources().getIdentifier(
					SWIPE_DEFAULT_FRONT_VIEW, "id",
					getContext().getPackageName());
			swipeBackView = getContext().getResources().getIdentifier(
					SWIPE_DEFAULT_BACK_VIEW, "id",
					getContext().getPackageName());

			if (swipeFrontView == 0 || swipeBackView == 0) {
				throw new RuntimeException(
						String.format(
								"You forgot the attributes swipeFrontView or swipeBackView. You can add this attributes or use '%s' and '%s' identifiers",
								SWIPE_DEFAULT_FRONT_VIEW,
								SWIPE_DEFAULT_BACK_VIEW));
			}
		}

		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		touchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(configuration);
		touchListener = new SwipeListViewTouchListener(this, swipeFrontView);
		if (swipeAnimationTime > 0) {
			touchListener.setAnimationTime(swipeAnimationTime);
		}
		touchListener.setRightOffset(swipeOffsetRight);
		touchListener.setLeftOffset(swipeOffsetLeft);
		touchListener.setSwipeActionLeft(swipeActionLeft);
		touchListener.setSwipeActionRight(swipeActionRight);
		touchListener.setSwipeMode(swipeMode);
		// touchListener.setSwipeClosesAllItemsWhenListMoves(swipeCloseAllItemsWhenMoveList);
		touchListener.setSwipeOpenOnLongPress(swipeOpenOnLongPress);
		setOnTouchListener(touchListener);
		setOnScrollListener(touchListener.makeScrollListener());
	}

	/**
	 * @see android.widget.ListView#setAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		if (adapter instanceof ACCBankAccountAdapter) {
			dragAdapter = (ACCBankAccountAdapter) adapter;
		}
		super.setAdapter(adapter);
		touchListener.resetItems();
		adapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				onListChanged();
				touchListener.resetItems();
			}
		});
	}

	/**
	 * Open ListView's item
	 * 
	 * @param position
	 *            Position that you want open
	 */
	public void openAnimate(int position) {
		touchListener.openAnimate(position);
	}

	/**
	 * Close ListView's item
	 * 
	 * @param position
	 *            Position that you want open
	 */
	public void closeAnimate(int position) {
		touchListener.closeAnimate(position);
	}

	/**
	 * Notifies onDismiss
	 * 
	 * @param reverseSortedPositions
	 *            All dismissed positions
	 */
	protected void onDismiss(int[] reverseSortedPositions) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onDismiss(reverseSortedPositions);
		}
	}

	/**
	 * Start open item
	 * 
	 * @param position
	 *            list item
	 * @param action
	 *            current action
	 * @param right
	 *            to right
	 */
	protected void onStartOpen(int position, int action, boolean right) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onStartOpen(position, action, right);
		}
	}

	/**
	 * Start close item
	 * 
	 * @param position
	 *            list item
	 * @param right
	 */
	protected void onStartClose(int position, boolean right) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onStartClose(position, right);
		}
	}

	/**
	 * Notifies onClickFrontView
	 * 
	 * @param position
	 *            item clicked
	 */
	protected void onClickFrontView(int position) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onClickFrontView(position);
		}
	}

	/**
	 * Notifies onClickBackView
	 * 
	 * @param position
	 *            back item clicked
	 */
	protected void onClickBackView(int position) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onClickBackView(position);
		}
	}

	/**
	 * Notifies onOpened
	 * 
	 * @param position
	 *            Item opened
	 * @param toRight
	 *            If should be opened toward the right
	 */
	protected void onOpened(int position, boolean toRight) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onOpened(position, toRight);
		}
	}

	/**
	 * Notifies onClosed
	 * 
	 * @param position
	 *            Item closed
	 * @param fromRight
	 *            If open from right
	 */
	protected void onClosed(int position, boolean fromRight) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onClosed(position, fromRight);
		}
	}

	/**
	 * Notifies onListChanged
	 */
	protected void onListChanged() {
		if (swipeListViewListener != null) {
			swipeListViewListener.onListChanged();
		}
	}

	/**
	 * Notifies onMove
	 * 
	 * @param position
	 *            Item moving
	 * @param x
	 *            Current position
	 */
	protected void onMove(int position, float x) {
		if (swipeListViewListener != null) {
			swipeListViewListener.onMove(position, x);
		}
	}

	protected int changeSwipeMode(int position) {
		if (swipeListViewListener != null) {
			return swipeListViewListener.onChangeSwipeMode(position);
		}
		return SWIPE_MODE_DEFAULT;
	}

	/**
	 * Sets the Listener
	 * 
	 * @param swipeListViewListener
	 *            Listener
	 */
	public void setSwipeListViewListener(
			BaseSwipeListViewListener swipeListViewListener) {
		this.swipeListViewListener = swipeListViewListener;
	}

	/**
	 * Resets scrolling
	 */
	public void resetScrolling() {
		touchState = TOUCH_STATE_REST;
	}

	/**
	 * Set offset on right
	 * 
	 * @param offsetRight
	 *            Offset
	 */
	public void setOffsetRight(float offsetRight) {
		touchListener.setRightOffset(offsetRight);
	}

	/**
	 * Set offset on left
	 * 
	 * @param offsetLeft
	 *            Offset
	 */
	public void setOffsetLeft(float offsetLeft) {
		touchListener.setLeftOffset(offsetLeft);
	}

	/**
	 * Set if all items opened will be closed when the user moves the ListView
	 * 
	 * @param swipeCloseAllItemsWhenMoveList
	 */
	public void setSwipeCloseAllItemsWhenMoveList(
			boolean swipeCloseAllItemsWhenMoveList) {
		touchListener
				.setSwipeClosesAllItemsWhenListMoves(swipeCloseAllItemsWhenMoveList);
	}

	/**
	 * Sets if the user can open an item with long pressing on cell
	 * 
	 * @param swipeOpenOnLongPress
	 */
	public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress) {
		touchListener.setSwipeOpenOnLongPress(swipeOpenOnLongPress);
	}

	/**
	 * Set swipe mode
	 * 
	 * @param swipeMode
	 */
	public void setSwipeMode(int swipeMode) {
		touchListener.setSwipeMode(swipeMode);
	}

	/**
	 * Return action on left
	 * 
	 * @return Action
	 */
	public int getSwipeActionLeft() {
		return touchListener.getSwipeActionLeft();
	}

	/**
	 * Set action on left
	 * 
	 * @param swipeActionLeft
	 *            Action
	 */
	public void setSwipeActionLeft(int swipeActionLeft) {
		touchListener.setSwipeActionLeft(swipeActionLeft);
	}

	/**
	 * Return action on right
	 * 
	 * @return Action
	 */
	public int getSwipeActionRight() {
		return touchListener.getSwipeActionRight();
	}

	/**
	 * Set action on right
	 * 
	 * @param swipeActionRight
	 *            Action
	 */
	public void setSwipeActionRight(int swipeActionRight) {
		touchListener.setSwipeActionRight(swipeActionRight);
	}

	/**
	 * Sets animation time when user drops cell
	 * 
	 * @param animationTime
	 *            milliseconds
	 */
	public void setAnimationTime(long animationTime) {
		touchListener.setAnimationTime(animationTime);
	}

	/**
	 * @see android.widget.ListView#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isDragging) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				if (isDragging) {
					stopDragging();
				}
			}
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				final int x = (int) ev.getX();
				final int y = (int) ev.getY();
				final int itemNum = pointToPosition(x, y);
				if (itemNum == AdapterView.INVALID_POSITION) {
					break;
				}
				final ViewGroup item = (ViewGroup) getChildAt(itemNum
						- getFirstVisiblePosition());
				mDragPointX = x - item.getLeft();
				mDragPointY = y - item.getTop();
				mDragOffsetX = ((int) ev.getRawX()) - x;
				mDragOffSetY = ((int) ev.getRawY()) - y;

				if (canDrag) {

					OnLongClickListener longclick = new OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {
							final int height = getHeight();
							mUpperBound = Math.min(y - mScaledTouchSlop,
									height / 3);
							mLowerBound = Math.max(y + mScaledTouchSlop,
									height * 2 / 3);
							mDragCurrentPostion = mDragStartPosition = itemNum;

							item.setDrawingCacheEnabled(true);
							Bitmap bitmap = Bitmap.createBitmap(item
									.getDrawingCache());
							startDragging(bitmap, x, y);
							return true;
						}
					};
					for (int i = 0; i < item.getChildCount(); i++) {
						item.getChildAt(i).setOnLongClickListener(longclick);
					}
					item.setOnLongClickListener(longclick);
				}
				break;
			}
		} else {

			int action = MotionEventCompat.getActionMasked(ev);
			final int x = (int) ev.getX();
			final int y = (int) ev.getY();

			if (touchState == TOUCH_STATE_SCROLLING_X) {
				return touchListener.onTouch(this, ev);
			}

			switch (action) {
			case MotionEvent.ACTION_MOVE:
				checkInMoving(x, y);
				return touchState == TOUCH_STATE_SCROLLING_Y;
			case MotionEvent.ACTION_DOWN:
				// final int x = (int) ev.getX();
				// final int y = (int) ev.getY();
				final int itemNum = pointToPosition(x, y);
				if (itemNum == AdapterView.INVALID_POSITION) {
					break;
				}

				final RelativeLayout item = (RelativeLayout) getChildAt(itemNum
						- getFirstVisiblePosition());
				mDragPointX = x - item.getLeft();
				mDragPointY = y - item.getTop();
				mDragOffsetX = ((int) ev.getRawX()) - x;
				mDragOffSetY = ((int) ev.getRawY()) - y;
				if (canDrag) {
					// 如果没有footerView，这里不用-1，如果有就需要-1，不能滑动
					int count = getFooterViewsCount() > 0 ? getAdapter().getCount() - 1 : getAdapter().getCount();
					if (itemNum < count) {
						OnLongClickListener longclick = new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								final int height = getHeight();
								mUpperBound = Math.min(y - mScaledTouchSlop,
										height / 3);
								mLowerBound = Math.max(y + mScaledTouchSlop,
										height * 2 / 3);
								mDragCurrentPostion = mDragStartPosition = itemNum;

								item.setDrawingCacheEnabled(true);
								Bitmap bitmap = Bitmap.createBitmap(item
										.getDrawingCache());
								startDragging(bitmap, x, y);
								return true;
							}
						};
						for (int i = 0; i < item.getChildCount(); i++) {
							item.getChildAt(i)
									.setOnLongClickListener(longclick);
						}
						item.setOnLongClickListener(longclick);
					}
				}
				touchListener.onTouch(this, ev);
				touchState = TOUCH_STATE_REST;
				lastMotionX = x;
				lastMotionY = y;

				break;
			case MotionEvent.ACTION_CANCEL:
				touchState = TOUCH_STATE_REST;
				break;
			case MotionEvent.ACTION_UP:
				touchListener.onTouch(this, ev);
				return touchState == TOUCH_STATE_SCROLLING_Y;
			default:
				break;
			}
		}
		super.onInterceptTouchEvent(ev);
		return isDragging;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getPointerCount() > 1) {
			if (isDragging) {
				stopDragging();
			}
			if (dragAdapter != null)
				dragAdapter.setDateDrageChanged(0, 0, false);
			return false;

		}
		if (mDragView != null && mDragCurrentPostion != INVALID_POSITION) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				stopDragging();
				if (dragAdapter != null){
					dragAdapter.setDragging(false);
					setAdapter(dragAdapter);
				}
				if (mDragCurrentPostion >= 0
						&& mDragCurrentPostion < getCount()) {
					if (mDropViewListener != null) {
						if (tempPosition != INVALID_POSITION) {
							mDropViewListener.drop(mDragStartPosition,
									mDragCurrentPostion);
						}
					}
				}

				break;
			case MotionEvent.ACTION_MOVE:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				dragView(x, y);
				if (y >= getHeight() / 3) {
					mUpperBound = getHeight() / 3;
				}
				if (y <= getHeight() * 2 / 3) {
					mLowerBound = getHeight() * 2 / 3;
				}
				int speed = 0;
				if (y > mLowerBound) {
					if (getLastVisiblePosition() < getCount() - 1) {
						speed = y > (getHeight() + mLowerBound) / 2 ? 16 : 4;
					} else {
						speed = 1;
					}
				} else if (y < mUpperBound) {
					speed = y < mUpperBound / 2 ? -16 : -4;
					if (getFirstVisiblePosition() == 0
							&& getChildAt(0).getTop() >= getPaddingTop()) {
						speed = 0;
					}
				}
				if (speed != 0) {
					smoothScrollBy(speed, 30);
				}
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);

	}

	/**
	 * Check if the user is moving the cell
	 * 
	 * @param x
	 *            Position X
	 * @param y
	 *            Position Y
	 */
	private void checkInMoving(float x, float y) {
		final int xDiff = (int) Math.abs(x - lastMotionX);
		final int yDiff = (int) Math.abs(y - lastMotionY);

		final int touchSlop = this.touchSlop;
		boolean xMoved = xDiff > touchSlop;
		boolean yMoved = yDiff > touchSlop;

		if (xMoved) {
			touchState = TOUCH_STATE_SCROLLING_X;
			lastMotionX = x;
			lastMotionY = y;
		}

		if (yMoved) {
			touchState = TOUCH_STATE_SCROLLING_Y;
			lastMotionX = x;
			lastMotionY = y;
		}
	}

	// add by wjp
	public void setLastPositionClickable(boolean clickable) {
		touchListener.setLastPositionClickable(clickable);
	}

	public void setAllPositionClickable(boolean allPositionClickable) {
		touchListener.setAllPositionClickable(allPositionClickable);
	}

	// public void setDisableClickItems(List<Integer> disableList){
	// this.disableClickItems = disableList;
	// }
	//
	// public List<Integer> getDisableClickItems(){
	// return disableClickItems;
	// }

	private void startDragging(Bitmap bitm, int x, int y) {
		stopDragging();
		isDragging = true;

		mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mLayoutParams.x = x - mDragPointX + mDragOffsetX;
		dragX = x - mDragPointX + mDragOffsetX;
		mLayoutParams.y = y - mDragPointY + mDragOffSetY;
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		mLayoutParams.format = PixelFormat.TRANSLUCENT;
		mLayoutParams.windowAnimations = 0;

		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(bitm);
		imageView.setBackgroundResource(R.drawable.shap_for_drag_list);
		imageView.setPadding(2, 2, 2, 2);
		mWindowManager.addView(imageView, mLayoutParams);
		mDragView = imageView;
	}

	private void stopDragging() {
		isDragging = false;
		if (mDragView != null) {
			mWindowManager.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
	}

	private void dragView(int x, int y) {
		if (mDragView != null) {
			mLayoutParams.alpha = mAlpha;
			mLayoutParams.y = y - mDragPointY + mDragOffSetY;
			mLayoutParams.x = dragX;
			mWindowManager.updateViewLayout(mDragView, mLayoutParams);
		}
		tempPosition = pointToPosition(0, y);
		// if(mDragStartPosition!= tempPosition){
		// }
		if (tempPosition != INVALID_POSITION && dragAdapter != null) {
			mDragCurrentPostion = tempPosition;
			dragAdapter.setDateDrageChanged(mDragStartPosition,
					mDragCurrentPostion, true);
		} 
		else {
//			dragAdapter.setDateDrageChanged(mDragStartPosition, tempPosition,
//					true);
		}

		int scrollY = 0;
		if (y < mUpperBound) {
			scrollY = 8;
		} else if (y > mLowerBound) {
			scrollY = -8;
		}

		if (scrollY != 0) {
			int top = getChildAt(
					mDragCurrentPostion - getFirstVisiblePosition()).getTop();
			setSelectionFromTop(mDragCurrentPostion, top + scrollY);
		}
	}

	public void setDropViewListener(DropViewListener listener) {
		this.mDropViewListener = listener;
	}

	public interface DropViewListener {
		void drop(int from, int to);
	}

	public boolean isDragging() {
		return isDragging;
	}

	public void setDragging(boolean isDragging) {
		this.isDragging = isDragging;
	}

	public boolean isCanDrag() {
		return canDrag;
	}

	public void setCanDrag(boolean canDrag) {
		this.canDrag = canDrag;
	}

}
