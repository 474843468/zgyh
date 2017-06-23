/**
 * Copyright 2012 
 * 
 * Nicolas Desjardins  
 * https://github.com/mrKlar
 * 
 * Facilite solutions
 * http://www.facilitesolutions.com/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chinamworld.bocmbci.widget.draggridview;

import android.content.Context;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;

import com.chinamworld.bocmbci.log.LogGloble;

public class PagedDragDropGrid extends HorizontalScrollView implements
		PagedContainer {

	private int mActivePage = 0;
	private DragDropGrid grid;
	private PagedDragDropGridAdapter adapter;
	private OnClickListener listener;
	
	private DrawPage drawPage;
	protected GestureDetector mGesture;
	
	private Context mContext;
	private VelocityTracker mVelocityTracker;
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private int currentPage = 0;

	public PagedDragDropGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		mGesture = new GestureDetector(context, new GestureListener());
		initPagedScroll();
		initGrid();
	}

	public PagedDragDropGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mGesture = new GestureDetector(context, new GestureListener());
		initPagedScroll();
		initGrid();
	}

	public PagedDragDropGrid(Context context) {
		super(context);
		this.mContext = context;
		initPagedScroll();
		initGrid();
	}

	public PagedDragDropGrid(Context context, AttributeSet attrs, int defStyle,
			PagedDragDropGridAdapter adapter) {
		super(context, attrs, defStyle);
		this.mContext = context;
		this.adapter = adapter;
		initPagedScroll();
		initGrid();
	}

	public PagedDragDropGrid(Context context, AttributeSet attrs,
			PagedDragDropGridAdapter adapter) {
		super(context, attrs);
		this.mContext = context;
		this.adapter = adapter;
		initPagedScroll();
		initGrid();
	}

	public PagedDragDropGrid(Context context, PagedDragDropGridAdapter adapter) {
		super(context);
		this.mContext = context;
		this.adapter = adapter;
		initPagedScroll();
		initGrid();
	}
	
	//add by wjp
	public interface DrawPage{
		public void refreshPageView();
	}

	private void initGrid() {
		grid = new DragDropGrid(getContext());
		addView(grid);
	}

	public void initPagedScroll() {
		final ViewConfiguration configuration = ViewConfiguration.get(mContext);
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				mGesture.onTouchEvent(event);
				if (mVelocityTracker == null) {
					mVelocityTracker = VelocityTracker.obtain();
				}
				mVelocityTracker.addMovement(event);
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, -1);
					if(initialVelocity < - mMinimumVelocity){
						if (canScrollToNextPage()) {
							scrollRight();
						}else{
							scrollToPage(mActivePage);
						}
//						LogGloble.d("info", "-------scrollToPage(1);前---------");
//							if(currentPage == 0){
//								LogGloble.d("info", "-------scrollToPage(1);---------");
//								scrollToPage(1);
//								currentPage = 1;
//							}
//							else{
//								scrollToPage(0);
//								currentPage = 0;
//							}
					}else if(initialVelocity > mMinimumVelocity){
						if (canScrollToPreviousPage()) {
						scrollLeft();
						}else{
							scrollToPage(mActivePage);
						}
//						LogGloble.d("info", "-------scrollToPage(0);前---------");
//							if(currentPage == 1){
//								LogGloble.d("info", "-------scrollToPage(0);---------");
//								scrollToPage(0);
//								currentPage = 0;
//							}
//							else{
//								scrollToPage(1);
//								currentPage = 1;
//							}
					}else{
						int scrollX = getScrollX();
						int onePageWidth = v.getMeasuredWidth();
						int page = ((scrollX + (onePageWidth / 2)) / onePageWidth);
						currentPage = page;
						scrollToPage(page);
					}
					
					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}
					//改变页标 wjp
					if(drawPage != null){
						drawPage.refreshPageView();
					}
					return true;
				} else {
					return false;
				}
			}
		});
	}
	
	

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}

	public void setAdapter(PagedDragDropGridAdapter adapter) {
		this.adapter = adapter;
		grid.setAdapter(adapter);
		grid.setContainer(this);
	}

	public void setClickListener(OnClickListener l) {
		this.listener = l;
		grid.setOnClickListener(l);
	}

	public boolean onLongClick(View v) {
		return grid.onLongClick(v);
	}
	public void setDeleteClickListener(OnClickListener deleteClickListener){
		
		grid.setDeleteOnClickListener(deleteClickListener);
	}
	/**
	 * 删除子项 add by xby 
	 */
	public void deleteChild(){
		grid.manageChildrenReordering();
	}

	public void notifyDataSetChanged(PagedDragDropGridAdapter adapter) {
		removeAllViews();
		initGrid();
		this.adapter = adapter;
		grid.setAdapter(adapter);
		grid.setContainer(this);
	}

	@Override
	public void scrollToPage(int page) {
		mActivePage = page;
		int onePageWidth = getMeasuredWidth();
		int scrollTo = page * onePageWidth;
		smoothScrollTo(scrollTo, 0);
	}

	@Override
	public void scrollLeft() {
		int newPage = mActivePage - 1;
		if (canScrollToPreviousPage()) {
			scrollToPage(newPage);
		}
	}

	@Override
	public void scrollRight() {
		int newPage = mActivePage + 1;
		if (canScrollToNextPage()) {
			scrollToPage(newPage);
		}
	}

	@Override
	public int currentPage() {
		return mActivePage;
	}

	@Override
	public void enableScroll() {
		requestDisallowInterceptTouchEvent(false);
	}

	@Override
	public void disableScroll() {
		requestDisallowInterceptTouchEvent(true);
	}

	@Override
	public boolean canScrollToNextPage() {
		int newPage = mActivePage + 1;
		return (newPage < adapter.pageCount());
	}

	@Override
	public boolean canScrollToPreviousPage() {
		int newPage = mActivePage - 1;
		return (newPage >= 0);
	}

	// modify by wjp
	/**
	 * 得到当前点击的条目位置
	 * @return
	 */
	public int getClicked() {
		return grid.getClicked();
	}

	public void setDrawPage(DrawPage drawPage) {
		this.drawPage = drawPage;
		grid.setDrawPage(drawPage);
	}
	public void setLongclickable(boolean longClickable){
		grid.setLongclickEnable(longClickable);
	}
	
	public void resumeChild(){
		grid.resumeChild();
		
	}
	
	public void setLongListner(OnClickListener listner){
		grid.setLongClickListner(listner);
	}

	public DragDropGrid getGrid() {
		return grid;
	}

	public void setGrid(DragDropGrid grid) {
		this.grid = grid;
	}
	class GestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1 != null && e2 != null) {
				if (e1.getX() - e2.getX() < -100) {// 向右滑动
//					int scrollX = getScrollX();
//					int onePageWidth = getMeasuredWidth();
//					int page = scrollX / onePageWidth +;
					LogGloble.d("drag", "向右滑动");
						scrollRight();
					//改变页标 wjp
					if(drawPage != null){
						drawPage.refreshPageView();
					}
					

				}

				if(e1.getX() - e2.getX() > 100){// 向左滑动
					int scrollX = getScrollX();
//					int onePageWidth = getMeasuredWidth();
//					int page = ((scrollX + (onePageWidth / 2)) / onePageWidth);
//					scrollToPage(page);
					LogGloble.d("drag", "向左滑动");
					scrollLeft();
					//改变页标 wjp
					if(drawPage != null){
						drawPage.refreshPageView();
					}
				}
			}
			return true;
		}

	}

	public int getmActivePage() {
		return mActivePage;
	}

	public void setmActivePage(int mActivePage) {
		this.mActivePage = mActivePage;
	}
	
	
}
