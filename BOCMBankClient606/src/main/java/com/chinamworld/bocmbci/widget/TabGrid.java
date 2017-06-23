/**
 * 文件名	：TabGrid.java
 * 创建日期	：2012-10-15
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.DeviceUtils;

/**
 * 九宫格视图 可以左右移动, 下部用小圆点来表示页数. 行数和列数需要在外部静态设置。 通过adapter作为数据接口
 */
public class TabGrid extends ViewGroup implements OnLongClickListener {
	private BaseAdapter mAdapter;
	private OnItemClickListener onItemClickListener = null;
	private OnItemLongClickListener onItemLongClickListener = null;
	/** 是否滑动的标记 */
	private boolean move;

	/** 滑动灵敏度，值越大，越灵敏 */
	private final int SENSE_VALUE = 2; // 滑动灵敏度，值越大，越灵敏. (滑动越小的距离就可以出发fling)

	private VelocityTracker mVelocityTracker = null; // 滑动速度计算器 add by wez

	private static final String TAG = "TabGrid";
	/** 实际的排版列数 */
	private int colNum = 0; // 列数
	/** 实际的排版行数 */
	private int rowNum = 0; // 行数

	/** 所有child中测量宽度的最大值 */
	private int childMaxWidth = 0;
	/** 所有child中测量高度的最大值 */
	private int childMaxHeight = 0;

	/** 每列所占的宽度 */
	private int occupyWidth = 0;
	/** 每行所占的高度 */
	private int occupyHeight = 0;
	/** 总页数 */
	private int pages = 0;
	/** 每一页最多容纳的子视图数量 */
	private int eachPageNum = 0;
	/** 子视图的总个数 */
	private int childCnt;
	private int padTop;
	private int padLeft;
	/** 松手时，本视图已经滑动的x方向的距离 */
	private int flipX = 0;
	/** 最大滑动距离，控件的实际总宽度 */
	private int maxFlipX = 0;
	private final int minFlipX = 0;
	/** 抬手后，控件需要自行滑动的距离 */
	private int autoFlipDistance;
	/** 滑动动画的时间 */
	private final int FLING_TIME = 1000; // ms

	private Context context;
	private Scroller scroller = null;
	private Event lastEvent = null;
	private Event lastDown = null;

	// 多于一屏的话，画圆圈
	private Paint paintP = new Paint();
	private List<PointF> points = null; // 放置圆圈坐标的
	private int radius = 0;
	
	public static double oneMove = 0;//一次移动的距离

	public TabGrid(Context context) {
		super(context);
		this.context = context;
		scroller = new Scroller(context);
	}

	public TabGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		scroller = new Scroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		LogGloble.i(TAG, "onLayout");
		reLayout();
	}

	@Override
	protected void onMeasure(int wSpec, int hSpec) {
		LogGloble.i(TAG, "onMeasure : width = " + getWidth() + " height = "
				+ getHeight() + " childCount = " + getChildCount());
		super.onMeasure(wSpec, hSpec);
		int count = getChildCount();

		// 获取child最大w h
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			measureChild(child, wSpec, hSpec);
			childMaxWidth = Math.max(childMaxWidth, child.getMeasuredWidth());
			childMaxHeight = Math
					.max(childMaxHeight, child.getMeasuredHeight());
		}

		int widthMode = MeasureSpec.getMode(wSpec);
		int widthSize = MeasureSpec.getSize(wSpec);
		int heightMode = MeasureSpec.getMode(hSpec);
		int heightSize = MeasureSpec.getSize(hSpec);
		if (widthMode != MeasureSpec.EXACTLY
				|| heightMode != MeasureSpec.EXACTLY) {

		}
		setMeasuredDimension(widthSize, heightSize);

		initGeo();
	}

	/**
	 * 初始化视图时，计算绘图所需的数据
	 */
	private void initGeo() {
		childCnt = getChildCount();
		if (childCnt == 0) {
			return;
		}
		padLeft = getPaddingLeft();
		padTop = getPaddingTop();
		int padRight = getPaddingRight();
		int padBtm = getPaddingBottom();
		int height = getHeight();
		int width = getWidth();
		// int height = getMeasuredWidth();
		// int width = getMeasuredHeight();
		if (height == 0 || width == 0) {
			return;
		}

		int usableWidth = width - padLeft - padRight;
		int usableHeight = height - padTop - padBtm;
		// 计算列数
		int colSpace = DeviceUtils.dip2px(LayoutValue.GRID_COL_SPACE, context); // 列间距
		// int maxCols = usableWidth / (colSpace + childMaxWidth); // 一屏最多能容纳的列数
		// if (colNum == 0) { // 如果没有外部指定
		// colNum = LayoutValue.GRID_COL_NUM > maxCols ? maxCols
		// : LayoutValue.GRID_COL_NUM; // 取较小值
		// } else if (colNum > 0) {
		// colNum = colNum > maxCols ? maxCols : colNum; // 取较小值
		// }
		// if (colNum == 0) { // 如果计算出来的cols == 0 说明不到一行 就按一行来绘制
		// colNum = 1;
		// }
		//
		// // 计算行数
		int rowSpace = DeviceUtils.dip2px(LayoutValue.GRID_ROW_SPACE, context); // 默认行间距
		// int maxRows = usableHeight / (childMaxHeight + rowSpace); //
		// 一屏最多能容纳的行数
		// if (rowNum == 0) {
		// rowNum = LayoutValue.GRID_ROW_NUM > maxRows ? maxRows
		// : LayoutValue.GRID_ROW_NUM; // 取较小值
		// } else if (rowNum > 0) {
		// rowNum = rowNum > maxRows ? maxRows : rowNum; // 取较小值
		// }
		//
		// if (rowNum == 0) { // 行数最小为1 不能为0
		// rowNum = 1;
		// }
//		colNum = 4;
//		rowNum = 3;
		// 计算页数
		eachPageNum = colNum * rowNum; // 一页所能容纳的图标个数
		double n = (double) childCnt / eachPageNum;
		pages = (int) Math.ceil(n); // 总共需要多少页
		maxFlipX = width * (pages - 1); // 滑动的最大x方向距离

		if (colSpace != 0) { // 如果外部指定了列间距，就不平分宽度，而是按列间距来布局
			occupyWidth = childMaxWidth + colSpace; // 每列所占宽度
		} else { // 否则就居中处理
			occupyWidth = usableWidth / colNum; // 每列所占宽度
		}
		if (rowSpace != 0) { // 行间距的处理 ，同列间距
			occupyHeight = childMaxHeight + rowSpace; // 每行所占高度
		} else {
			occupyHeight = usableHeight / rowNum; // 每行所占高度
		}

		// 圆圈初始化 个数=pages 坐标位置处理
		if (pages <= 1) {
			return; // 只有一屏 不画圈
		}

		// 根据页数绘制圆圈
		paintP = new Paint();

		// 底部圆点的位置与大小
		radius = DeviceUtils.dip2px(LayoutValue.Grid_CIRCLE_RADIUS, context);
		int gap = radius << 2; // 2个原圈之间的间距 是自身大小的一半
		int x0 = (width - pages * 2 * radius - (pages - 1) * gap) / 2 + radius;
		// int y0 = height - radius * 2 - padBtm/2;
		int y0 = (occupyHeight) * rowNum + 10 - radius * 2;
		int x = x0;
		if (points == null) {
			points = new ArrayList<PointF>();
		}
		points.clear();

		for (int i = 1; i <= pages; i++) {
			points.add(new PointF(x, y0));
			x += 2 * radius + gap;
		}
	}

	/**
	 * 更新布局
	 */
	private void reLayout() {
		LogGloble.i(TAG, "relayout : childCount = " + childCnt + " pages = "
				+ pages);
		if (childCnt == 0) { // 如果没有子视图，该方法直接返回，不进行绘制
			return;
		}
		if (pages == 0) {
			initGeo();
		}

		LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH / 4,
				LayoutValue.SCREEN_WIDTH * 5 / 16 + 20);
		android.widget.LinearLayout.LayoutParams lpImage = new android.widget.LinearLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH / 5, LayoutValue.SCREEN_WIDTH / 5);
		lpImage.gravity = Gravity.CENTER;
		for (int page = 0; page < pages; page++) { // 第几页
			int left0 = getWidth() * page + padLeft;
			int before = page * eachPageNum; // 之前页的数目和
			for (int row = 0; row < rowNum; row++) { // 第几行
				int top = padTop + row * occupyHeight;
				for (int col = 0; col < colNum; col++) { // 第几列
					int order = before + row * colNum + col;
					if (order >= childCnt) {
						return;
					}
					View child = getChildAt(order);
					//图标
					ImageView img = (ImageView) child
							.findViewById(R.id.grid_icon);
					img.setLayoutParams(lpImage);
					int left = left0 + col * LayoutValue.SCREEN_WIDTH / 4;
//					child.setPadding(15, 15, 15, 15);
					child.setLayoutParams(lp);
					left -= flipX; // 画面运动的根源
					child.layout(left, top,
							left + LayoutValue.SCREEN_WIDTH / 4, top
									+ LayoutValue.SCREEN_WIDTH * 5 / 16 + 20);
				}
			}
		}
	}

	/**
	 * 绘制圆圈 points
	 */
	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		// 绘制表示页数的小圆圈
		if (null != points) {
			int currentPage = flipX / getWidth(); // 当前所在的页数
			int size = points.size();
			paintP.setColor(Color.GRAY);
			for (int i = 0; i < size; i++) {
				PointF center = points.get(i);
				Bitmap bitmap = null;
				if (i == currentPage) {
					bitmap = ((BitmapDrawable) getResources().getDrawable(
							R.drawable.current_page)).getBitmap();
				} else {
					bitmap = ((BitmapDrawable) getResources().getDrawable(
							R.drawable.other_page)).getBitmap();
				}
				int height = bitmap.getHeight();
				canvas.drawBitmap(bitmap, center.x, center.y - height / 2,
						paintP);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// add by wez 2012.10.05 添加触摸速度计算处理器
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(e);

		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			LogGloble.i(TAG, "onTouchEvent down");
			
			oneMove = 0;
			
			lastDown = new Event(e);
			break;
		case MotionEvent.ACTION_MOVE:
			LogGloble.i(TAG, "onTouchEvent move");
			
			if (lastEvent != null) {
				flipX += (lastEvent.x - e.getX());
				if (flipX > maxFlipX) {
					flipX = maxFlipX;
				} else if (flipX < minFlipX) {
					flipX = minFlipX;
				}
				reLayout();
			}
			// added
			// 系统在判断触摸事件时较为精确，用户手指不动时也会识别为move事件，所以加上boolean值的move来通过坐标表明是否move事件
			if (lastEvent != null) {
				oneMove += Math.abs(lastEvent.x - e.getX());
				if (lastEvent.x - e.getX() <= 10) {
					move = false;
				} else {
					move = true;
				}
			}
			// added end
			lastEvent = new Event(e);
			break;
		case MotionEvent.ACTION_UP:
			LogGloble.i(TAG, "onTouchEvent action up");
			// 计算速度 像素/秒
			mVelocityTracker.computeCurrentVelocity(1000);

			autoFlip(); // 自行滑动到某个位置
			lastEvent = null;
			lastDown = null;
			break;
		}
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 首先响应自身的滑动事件，然后判断事件状态是否为move，如果是move就不响应onItemLongClick和onItemClick
		if (onTouchEvent(ev) && ev.getAction() == MotionEvent.ACTION_MOVE) {
			// // added
			// 系统在判断触摸事件时较为精确，用户手指不动时也会识别为move事件，所以加上boolean值的move来通过坐标表明是否move事件
			 if(onTouchEvent(ev) && ev.getAction() == MotionEvent.ACTION_MOVE
			 && !move) {
				 return super.dispatchTouchEvent(ev);
			 }
			// // added end
			ev.setAction(MotionEvent.ACTION_CANCEL); //
		}
		return super.dispatchTouchEvent(ev);

	}

	/**
	 * 自动滑动到某个view. lastDown和lastEvent决定滑动方向
	 */
	private void autoFlip() {
		if (null == lastEvent || null == lastDown) {
			return;
		}

		int pageWidth = getWidth(); // 每一页的宽度
		int distanceFlag = pageWidth / SENSE_VALUE;
		int fingerDistantce = (int) (lastDown.x - lastEvent.x); // 实际滑动距离 //
		// ----

		// add by wez 2012.11.05 获取滑动速度和设备的最小敏感滑动速度
		int minFlingVelocity = ViewConfiguration.get(context)
				.getScaledMinimumFlingVelocity();// 设备的最小敏感滑动速度
		float velocityX = mVelocityTracker.getXVelocity(0);// X方向的滑动速度
		int restPageDistance = flipX % pageWidth; // ------
		// 判断滑动速度是否达到最小敏感滑动速度，取绝对值
		if (Math.abs(velocityX) > minFlingVelocity) {
			// 如果速度达到设备的敏感滑动速度，不管flipX是多少，都需要滑动
			if (velocityX > 0) {
				// 反向滑动
				autoFlipDistance = (restPageDistance) * (-1);
			} else if (velocityX < 0) {
				// 正向滑动
				autoFlipDistance = pageWidth - restPageDistance;
			}
		} else {
			// 如果速度达不到，再去判断flipX是否达到临界值 来决定是否需要滑动
			if (fingerDistantce >= distanceFlag) { // 正向滑动一屏
				autoFlipDistance = pageWidth - restPageDistance;
			} else if (fingerDistantce <= distanceFlag * (-1)) { // 反向滑动一屏
				autoFlipDistance = (restPageDistance) * (-1);
			} else { // 回复原屏
				autoFlipDistance = fingerDistantce * (-1);
			}
		}

		if (flipX >= maxFlipX || flipX <= minFlipX) {
			//
			autoFlipDistance = 0;
		}

		scroller.startScroll(flipX, 0, autoFlipDistance, 0, FLING_TIME);
		postInvalidate(); // 要postInvalidate()和reLayout()一起才能刷新屏幕
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			flipX = scroller.getCurrX();
			//如果一次移动位移大于10 才让他重新布局 刷新界面
			if(oneMove > 10){
				reLayout();
				postInvalidate();
			}
		}
	}

	/**
	 * 更新数据，应把某些影响展示的全局变量重置
	 * 
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		LogGloble.i(TAG, "setAdapter");
		mAdapter = adapter;
		flipX = 0; // 数据更新后，应重新显示第一页 // added by nl. 2012/10/11
		pages = 0; // 页数置零，重新根据新数据来统计页数并绘制圆圈
		points = null;

		bindViews();
	}

	/**
	 * 初始化子视图
	 */
	private void bindViews() {
		removeAllViews();
		for (int i = 0; i < mAdapter.getCount(); i++) {
			View v = mAdapter.getView(i, null, null);
			// 使每一个item可以被点击
			v.setClickable(true);
			v.setTag(i);
			v.setOnClickListener(onclick);
			v.setLongClickable(true);
			v.setOnLongClickListener(this);
			// 使每一个item可以接受焦点
			v.setFocusable(true);
			v.setSelected(true);
			this.addView(v);
		}
		reLayout();
	}

	public OnClickListener onclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = (Integer)v.getTag();

			if (onItemClickListener != null) {
				// 调用外部定义的监听器的点击监听方法
				onItemClickListener.onItemClick(null, v, id, 0);
				LogGloble.d(TAG, "click item id = " + id);
			}
		}
	};

	@Override
	public boolean onLongClick(View v) {
//		int id = v.getId();
		int id = (Integer) v.getTag();
		ImageButton imageBtn = (ImageButton) ((RelativeLayout) v)
				.getChildAt(1);
//		ImageButton imageBtn = (ImageButton) relativeLayout.getChildAt(2);// 加号按钮
		imageBtn.setBackgroundResource(R.drawable.btn_add_selector); // 初始化加号

		if (onItemLongClickListener != null) {
			// 调用外部定义的监听器的点击监听方法
			onItemLongClickListener.onItemLongClick(null, v, id, 0);
			LogGloble.d(TAG, "longclick item id = " + id);
		}
		return true; // 使长按事件不再向下传递
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public OnItemLongClickListener getOnItemLongClickListener() {
		return onItemLongClickListener;
	}

	public void setOnItemLongClickListener(
			OnItemLongClickListener onItemLongClickListener) {
		this.onItemLongClickListener = onItemLongClickListener;
	}

	public void setColNum(int c) {
		colNum = c;
	}

	public void setRowNum(int r) {
		rowNum = r;
	}

	class Event {
		float x;
		float y;
		float t;

		public Event(MotionEvent e) {
			x = e.getX();
			y = e.getY();
			t = e.getEventTime();
		}
	}

}
