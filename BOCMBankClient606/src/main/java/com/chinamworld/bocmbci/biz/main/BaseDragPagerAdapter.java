package com.chinamworld.bocmbci.biz.main;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.chinamworld.bocmbci.biz.main.DragController.DragListener;
import com.chinamworld.bocmbci.log.LogGloble;

public abstract class BaseDragPagerAdapter extends PagerAdapter implements DragListener {
	private static final String TAG = BaseDragPagerAdapter.class.getSimpleName();

	protected Context mContext;
	private int row = 0;
	private int column = 0;
	private DragController mDragController;
	private boolean isAnimaing = false;
	private boolean isStartDragAnimation;
	private ViewPager mViewPager;
	private int ANIMATION_DURATION = 150;
	private LinkedList<DragAnim> mAnims;
	private float mLastMotionX;

//	protected List<? extends AbsListView> mViews;

	public BaseDragPagerAdapter(Context context, DragController dragController, ViewPager viewPage, int row, int clocumn) {
		super();
		this.mContext = context;
		this.row = row;
		this.column = clocumn;
		mAnims = new LinkedList<DragAnim>();
		mDragController = dragController;
		mDragController.setOnDragListener(this);
		mViewPager = viewPage;
//		mViews = getGroupViews();
	}

	public abstract List<? extends AbsListView> getGroupViews();

	public void startAnimation() {
		isAnimaing = true;
		Animation animation = createFastRotateAnimation();
		for (ViewGroup view : getGroupViews()) {
			view.setLayoutAnimation(new LayoutAnimationController(animation, 0.0f));
		}
	}

	public void stopAnimation() {
		isAnimaing = false;
		for (ViewGroup view : getGroupViews()) {
			view.getLayoutAnimation().getAnimation().cancel();
		}

	}

	public void clearDragAnimation() {
		isStartDragAnimation = false;
		mAnims.clear();
	}

	public void onDragEvent(View view, Object tag) {
		mDragController.onItemLongClickEvent(view, tag);
	}

	protected void refreshAndAnimationGrieView() {
		for (AbsListView gridView : getGroupViews()) {
			((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
		}
		startAnimation();
	}

	private class DragAnimListener implements AnimationListener {
		public DragAnim _dragAnim;

		public DragAnimListener(DragAnim _dragAnim) {
			super();
			this._dragAnim = _dragAnim;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mAnims.remove(_dragAnim);
			if (mAnims.isEmpty()) {
				isStartDragAnimation = false;
				refreshAndAnimationGrieView();
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	private DragAnim getDragAnim(int oldLocation, int newLocation) {
//		1			2			3
//	 _______	 _______	 _______
//  |		|	|		|	|		|
//	|		|	|		|	|		|
//	|		|	|		|	|		|
//	|_______|	|_______|	|_______|
//	
		int o_page = oldLocation / (row * column);
		o_page = o_page % (row * column);
		int o_index = oldLocation - o_page * row * column;

		int n_page = newLocation / (row * column);
		n_page = n_page % (row * column);
		int n_index = newLocation - n_page * row * column;

		LogGloble.i(TAG, "move old: page:" + o_page + ", position:" + o_index);
		LogGloble.i(TAG, "move new: page:" + n_page + ", position:" + n_index);

		View oldChild = getGroupViews().get(o_page).getChildAt(o_index);
		View newChild = getGroupViews().get(n_page).getChildAt(n_index);

		Item oldItem = (Item) oldChild.getTag();
		if (oldItem == null) {
			return null;
		}

		oldItem.getLocation().setCurrentPage(n_page);
		oldItem.getLocation().setCurrentPosition(n_index);

		int[] oldLocationInWindow = new int[2];
		int[] newLocationInWindow = new int[2];
		oldChild.getLocationInWindow(oldLocationInWindow);
		newChild.getLocationInWindow(newLocationInWindow);

		return new DragAnim(oldChild, new Point(oldLocationInWindow[0], oldLocationInWindow[1]), new Point(
				newLocationInWindow[0], newLocationInWindow[1]));
	}

	public void onDragEvent() {

	}

	@Override
	public void onDraging(DragView dragView, Object tag, MotionEvent ev) {
		float rawX = ev.getRawX();
		boolean isDraging = Math.abs((mLastMotionX - rawX)) < 0.5f;
		LogGloble.i(TAG, "xDiff" + Math.abs((mLastMotionX - rawX)));
		mLastMotionX = rawX;
		if (!isStartDragAnimation && isDraging) {
			Item item = (Item) tag;
			if (item != null) {
				View dragPopupView = dragView.getContentView();
				int page = mViewPager.getCurrentItem();
				AbsListView gridView = getGroupViews().get(page);
				for (int position = 0, count = gridView.getChildCount(); position < count; position++) {
					View childAt = gridView.getChildAt(position);
					if (childAt.getVisibility() == View.VISIBLE) {
						if (ViewUtils.rectInView(dragPopupView, childAt)) {
							if (position != item.getLocation().getCurrentPosition()) {
								isStartDragAnimation = true;

								// 拖动位置在覆盖前
								int oldPage = page;
								int oldLocation = position + (column * row * oldPage);

								int newPage = item.getLocation().getCurrentPage();
								int newLocation = item.getLocation().getCurrentPosition() + (column * row * newPage);
								LogGloble.i(TAG, "onDraging current item: page:" + item.getLocation().getCurrentPage()
										+ ", position:" + item.getLocation().getCurrentPosition());
								LogGloble
										.i(TAG, "onDraging target item: page:" + oldPage + ", position:" + oldLocation);

								if (oldLocation < newLocation) {
									for (int p = newLocation; p > oldLocation; p--) {
										int prev = p - 1;
										DragAnim dragAnim = getDragAnim(prev, p);
										if (dragAnim != null) {
											mAnims.add(dragAnim);
										}
									}
								} else {
									for (int p = newLocation; p < oldLocation; p++) {
										int prev = p + 1;
										DragAnim dragAnim = getDragAnim(prev, p);
										if (dragAnim != null) {
											mAnims.add(dragAnim);
										}
									}
								}
								item.getLocation().setCurrentPage(page);
								item.getLocation().setCurrentPosition(position);

								startDragAnims(mAnims);
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onDragEdge(Orientation orientation) {
		int currentItem = mViewPager.getCurrentItem();
		int sizePage = mViewPager.getAdapter().getCount();
		if (orientation == Orientation.LEFT_RIGHT) {
			if (currentItem < sizePage) {
				mViewPager.setCurrentItem(++currentItem);
				refreshAndAnimationGrieView();
			}
		} else if (orientation == Orientation.RIGHT_LEFT) {
			if (currentItem > 0) {
				mViewPager.setCurrentItem(--currentItem);
				refreshAndAnimationGrieView();
			}
		}
	}

	@Override
	public void onDragEnd(DragView view, Object dragInfo) {
		clearDragAnimation();
	}

	@Override
	public void onDragStart(View view, Object dragInfo) {
		// TODO Auto-generated method stub

	}

	private void startDragAnims(LinkedList<DragAnim> anims) {
		for (int i = 0; i < anims.size(); i++) {
			DragAnim dragAnim = anims.get(i);
			Point oldPoint = dragAnim.getOldPoint();
			Point newPoint = dragAnim.getNewPoint();
			int xx = newPoint.x - oldPoint.x;
			int yy = newPoint.y - oldPoint.y;
			TranslateAnimation ta = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, xx,
					Animation.ABSOLUTE, 0, Animation.ABSOLUTE, yy);
			ta.setDuration(ANIMATION_DURATION);
			ta.setFillEnabled(true);
			ta.setFillAfter(true);
			ta.setInterpolator(new AccelerateDecelerateInterpolator());
			ta.setAnimationListener(new DragAnimListener(dragAnim));
			dragAnim.getView().clearAnimation();
			dragAnim.getView().startAnimation(ta);
		}
	}

	public boolean isAnimaing() {
		return isAnimaing;
	}

	protected Animation createFastRotateAnimation() {
		Animation rotate = new RotateAnimation(-2.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setRepeatMode(Animation.REVERSE);
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setDuration(80);
		rotate.setInterpolator(new AccelerateDecelerateInterpolator());
		return rotate;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

}
