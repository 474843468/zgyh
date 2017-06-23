package com.boc.bocsoft.mobile.bocmobile.base.widget.gridview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import com.boc.bocsoft.mobile.bocmobile.R;
import java.util.LinkedList;
import java.util.List;


/**
 * 自定义gridView
 *
 * @author gwluo
 *
 */
public class DragGridView extends GridView {
	/**
	 * 正在拖拽的position
	 */
	private int mDragPosition;


	/**
	 * 用于拖拽的镜像，这里直接用一个ImageView
	 */
	private ImageView mDragImageView;

	/**
	 * 震动器
	 */
	private Vibrator mVibrator;

	private WindowManager mWindowManager;
	/**
	 * item镜像的布局参数
	 */
	private WindowManager.LayoutParams mWindowLayoutParams;

	/**
	 * 我们拖拽的item对应的Bitmap
	 */
	private Bitmap mDragBitmap;

	/**
	 * 状态栏的高度
	 */
	private int mStatusHeight;

	private boolean mAnimationEnd = true;

	private DragGridBaseAdapter mDragAdapter;
	private int mNumColumns;
	private int mColumnWidth;
	private boolean mNumColumnsSet;
	private int mHorizontalSpacing;
	private int mVerticalSpacing;

	private boolean isEditMode = false;
	private boolean hasDrag = false;
	private boolean isDraging = false;

	private boolean isCanDrag = true;//是否可以拖动

	private int horizontalSpacingColor = Color.TRANSPARENT;
	private int verticalSpacingColor = Color.TRANSPARENT;
	private Paint spacePaint;


	public DragGridView(Context context) {
		this(context, null);
	}

	public DragGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);


		final TypedArray a = context.obtainStyledAttributes(
				attrs, R.styleable.DragGridView, defStyle, 0);
		horizontalSpacingColor = a.getColor(R.styleable.DragGridView_horizontalSpacingColor,Color.TRANSPARENT);
		verticalSpacingColor = a.getColor(R.styleable.DragGridView_verticalSpacingColor,Color.TRANSPARENT);

		a.recycle();

		setOnItemClickListener(null);
		mVibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mStatusHeight = getStatusHeight(context); // 获取状态栏的高度

		if (!mNumColumnsSet) {
			mNumColumns = AUTO_FIT;
		}

	}


	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

		if (adapter instanceof DragGridBaseAdapter) {
			mDragAdapter = (DragGridBaseAdapter) adapter;
			mDragAdapter.bindDragView(this);
		} else {
			throw new IllegalStateException(
					"the adapter must be implements DragGridAdapter");
		}
		setOnItemLongClickListener(longClickListener);
	}

	public void setCanDrag(boolean canDrag) {
		isCanDrag = canDrag;
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(getChildCount() == 0)return;
		View childAt = getChildAt(0);
		if(spacePaint == null){
			spacePaint = new Paint();
			spacePaint.setStyle(Paint.Style.FILL_AND_STROKE);
			spacePaint.setAntiAlias(true);
		}

		int w = childAt.getMeasuredWidth();
		int h = childAt.getMeasuredHeight();

		ViewGroup group;

		spacePaint.setColor(horizontalSpacingColor);
		spacePaint.setStrokeWidth(mHorizontalSpacing);

		for(int index=1;index<mNumColumns;index++){
			canvas.drawLine(w*index+(index-1)*mHorizontalSpacing+mHorizontalSpacing/2.f,0,w*index+(index-1)*mHorizontalSpacing+mHorizontalSpacing/2,canvas.getHeight(),spacePaint);
		}


		int rows = getCount() / getNumColumns() + (getCount() % getNumColumns() == 0 ? 0 : 1);
		if(rows<=1)return;
		spacePaint.setColor(verticalSpacingColor);
		spacePaint.setStrokeWidth(mVerticalSpacing);
		for(int index=1;index<rows;index++){
			canvas.drawLine(0,h*index+mVerticalSpacing*(index-1)+mVerticalSpacing/2.f,canvas.getWidth(),h*index+mVerticalSpacing*(index-1)+mVerticalSpacing/2.f,spacePaint);
		}
	}

	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if(mDragAdapter == null || !isCanDrag){
				return true;
			}

			if(position == getAdapter().getCount()-1){
				return true;
			}

			startEditMode(position);
			onSubItemLongClick(view,position);
			return true;
		}
	};


	private void startEditMode(int pos){
		isDraging = true;
		mDragPosition = pos;
		hasDrag = false;
		isEditMode = true;
		mDragAdapter.startEditMode();
		setOnItemLongClickListener(null);
	}


	public void endEditMode(boolean notify){
		isEditMode = false;
		setOnItemLongClickListener(longClickListener);
		if(notify){
			mDragAdapter.endEidt();
		}
	}


	private void onSubItemLongClick(final View view, final int position){
		setOnTouchListener(new OnTouchListener() {
			@Override public boolean onTouch(View v, MotionEvent event) {
				if(!isEditMode)return  false;
				isDraging = true;
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					moveDragView(view,(int)event.getRawX(),(int)event.getRawY()-getStatusHeight(getContext()));
					requestDisallowInterceptTouchEvent(true);
					onSwapItem((int)event.getX(),(int) event.getY());
				}else {
					isDraging = false;
					if(!hasDrag){
						mDragAdapter.showDelIcon(view,mDragPosition);
					}else{
						setOnItemLongClickListener(longClickListener);
					}
					cancleDrag();
					requestDisallowInterceptTouchEvent(false);
					setOnTouchListener(null);
					mDragAdapter.endDrag();
					mDragAdapter.setHideItem(-1);
				}
				return true;
			}
		});
	}


	private void moveDragView(View view,int x,int y){

		if(mWindowLayoutParams == null){
			mWindowLayoutParams = new WindowManager.LayoutParams();
			mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // 图片之外的其他地方透明
			mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

			mWindowLayoutParams.alpha = 0.8f; // 透明度
			mWindowLayoutParams.width = view.getMeasuredWidth();
			mWindowLayoutParams.height = view.getMeasuredHeight();
			mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

		}

		if(mDragImageView == null){
			mDragImageView = new ImageView(getContext());
			mDragImageView.setBackgroundColor(Color.RED);

			view.setDrawingCacheEnabled(true);
			view.setDrawingCacheEnabled(true);
			// 获取mDragItemView在缓存中的Bitmap对象
			mDragBitmap = Bitmap.createBitmap(view
					.getDrawingCache());
			view.destroyDrawingCache();

			mDragImageView.setImageBitmap(mDragBitmap);
			mWindowManager.addView(mDragImageView, mWindowLayoutParams);
		}


		mWindowLayoutParams.x = x-view.getMeasuredWidth()/2;
		mWindowLayoutParams.y = y-view.getMeasuredHeight()/2;
		mWindowManager.updateViewLayout(mDragImageView,mWindowLayoutParams);

	}

	private void cancleDrag(){
		if(mDragImageView !=null){
			mWindowManager.removeView(mDragImageView);
			mDragImageView = null;
		}
	}

	@Override
	public void setNumColumns(int numColumns) {
		super.setNumColumns(numColumns);
		mNumColumnsSet = true;
		this.mNumColumns = numColumns;
	}

	@Override
	public void setColumnWidth(int columnWidth) {
		super.setColumnWidth(columnWidth);
		mColumnWidth = columnWidth;
	}

	@Override
	public void setHorizontalSpacing(int horizontalSpacing) {
		super.setHorizontalSpacing(horizontalSpacing);
		this.mHorizontalSpacing = horizontalSpacing;
	}

	@Override public void setVerticalSpacing(int verticalSpacing) {
		super.setVerticalSpacing(verticalSpacing);
		this.mVerticalSpacing = verticalSpacing;
	}

	/**
	 * 若设置为AUTO_FIT，计算有多少列
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mNumColumns == AUTO_FIT) {
			int numFittedColumns;
			if (mColumnWidth > 0) {
				int gridWidth = Math.max(MeasureSpec.getSize(widthMeasureSpec)
						- getPaddingLeft() - getPaddingRight(), 0);
				numFittedColumns = gridWidth / mColumnWidth;
				if (numFittedColumns > 0) {
					while (numFittedColumns != 1) {
						if (numFittedColumns * mColumnWidth
								+ (numFittedColumns - 1) * mHorizontalSpacing > gridWidth) {
							numFittedColumns--;
						} else {
							break;
						}
					}
				} else {
					numFittedColumns = 1;
				}
			} else {
				numFittedColumns = 2;
			}
			mNumColumns = numFittedColumns;
		}
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(100000,MeasureSpec.AT_MOST));

	}





	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		//super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}


	/**
	 * 交换item,并且控制item之间的显示与隐藏效果
	 *
	 * @param moveX
	 * @param moveY
	 */
	private void onSwapItem(int moveX, int moveY) {
		// 获取我们手指移动到的那个item的position
		final int tempPosition = pointToPosition(moveX, moveY);
		if(tempPosition != mDragPosition){
			hasDrag = true;
		}
		if (tempPosition == getChildCount() - 1) {// 如果移动到最后一个增加item不交换，lgw
			return;
		}
		// 假如tempPosition 改变了并且tempPosition不等于-1,则进行交换
		if (tempPosition != mDragPosition
				&& tempPosition != AdapterView.INVALID_POSITION
				&& mAnimationEnd) {
			mDragAdapter.reorderItems(mDragPosition, tempPosition);
			mDragAdapter.setHideItem(tempPosition);

			final ViewTreeObserver observer = getViewTreeObserver();
			observer.addOnPreDrawListener(new OnPreDrawListener() {

				@Override
				public boolean onPreDraw() {
					observer.removeOnPreDrawListener(this);
					animateReorder(mDragPosition, tempPosition);
					mDragPosition = tempPosition;
					return true;
				}
			});

		}
	}

	/**
	 * 创建移动动画
	 *
	 * @param view
	 * @param startX
	 * @param endX
	 * @param startY
	 * @param endY
	 * @return
	 */
	private AnimatorSet createTranslationAnimations(View view, float startX,
			float endX, float startY, float endY) {
		ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
				startX, endX);
		ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
				startY, endY);
		AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.playTogether(animX, animY);
		return animSetXY;
	}

	/**
	 * item的交换动画效果
	 *
	 * @param oldPosition
	 * @param newPosition
	 */
	private void animateReorder(final int oldPosition, final int newPosition) {
		boolean isForward = newPosition > oldPosition;
		List<Animator> resultList = new LinkedList<Animator>();
		if (isForward) {
			for (int pos = oldPosition; pos < newPosition; pos++) {
				View view = getChildAt(pos - getFirstVisiblePosition());

				if ((pos + 1) % mNumColumns == 0) {
					resultList.add(createTranslationAnimations(view,
							-view.getWidth() * (mNumColumns - 1), 0,
							view.getHeight(), 0));
				} else {
					resultList.add(createTranslationAnimations(view,
							view.getWidth(), 0, 0, 0));
				}
			}
		} else {
			for (int pos = oldPosition; pos > newPosition; pos--) {
				View view = getChildAt(pos - getFirstVisiblePosition());
				if ((pos + mNumColumns) % mNumColumns == 0) {
					resultList.add(createTranslationAnimations(view,
							view.getWidth() * (mNumColumns - 1), 0,
							-view.getHeight(), 0));
				} else {
					resultList.add(createTranslationAnimations(view,
							-view.getWidth(), 0, 0, 0));
				}
			}
		}

		AnimatorSet resultSet = new AnimatorSet();
		resultSet.playTogether(resultList);
		resultSet.setDuration(300);
		resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
		resultSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				mAnimationEnd = false;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mAnimationEnd = true;
			}
		});
		resultSet.start();
	}


	/**
	 * 获取状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	private static int getStatusHeight(Context context) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	private OnItemClickListener itemClickListener;
	@Override public void setOnItemClickListener(OnItemClickListener listener) {
		this.itemClickListener = listener;
		super.setOnItemClickListener(innerOnItemClickListener);
	}

	private OnItemClickListener innerOnItemClickListener = new OnItemClickListener() {
		@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(isEditMode && !isDraging){

				endEditMode(true);
				if(position == mDragPosition){
					return;
				}

				if(itemClickListener != null){
					itemClickListener.onItemClick(parent,view,position,id);
				}
			}else{
				if(itemClickListener != null){
					itemClickListener.onItemClick(parent,view,position,id);
				}
			}

		}
	};
}
