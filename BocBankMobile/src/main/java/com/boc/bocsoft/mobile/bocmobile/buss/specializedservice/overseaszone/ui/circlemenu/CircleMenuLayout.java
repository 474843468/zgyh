package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.circlemenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


/**
 *   Created by xwg on "16/10/25". on "19:59".
 *   圆环菜单
 */
public class CircleMenuLayout extends ViewGroup
{
	private static final String TAG = "TAG";
	// 事件监听
	private OnItemClickListener mOnItemClickListener = null;
	private OnItemSelectedListener mOnItemSelectedListener = null;
	/**
	 *   圆环直径，取view的宽或高
	 */
	private int mRadius;
	/**
	 * 该容器内child item的默认尺寸
	 */
	private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 6f;
	/**
	 * 该容器的内边距,无视padding属性，如需边距请用该变量
	 */
	private static final float RADIO_PADDING_LAYOUT = 1/9f;

	/**
	 * 当每秒移动角度达到该值时，认为是快速移动
	 */
	private static final int FLINGABLE_VALUE = 200;

	/**
	 * 如果移动角度达到该值，则屏蔽点击
	 */
	private static final int NOCLICK_VALUE = 5;

	/**
	 * 当每秒移动角度达到该值时，认为是快速移动
	 */
	private int mFlingableValue = FLINGABLE_VALUE;
	/**每秒移动的角度这个值
	 */
	private float angelPerSecond;
	/**
	 * 该容器的内边距,无视padding属性，如需边距请用该变量
	 */
	private float mPadding;
	/**
	 * 布局时的开始角度,即第一个item中心点与水平方向的夹角
	 */
	private final float START_ANGLE = 180;
	/**
	 * 布局时的开始角度
	 */
	private float mStartAngle = START_ANGLE;

	/**
	 * 检测按下到抬起时旋转的角度
	 */
	private float mTmpAngle;
	/**
	 * 检测按下到抬起时使用的时间
	 */
	private long mDownTime;

	/**
	 * 判断是否正在自动滚动
	 */
	private boolean isFling;
	/**
	 *   当前选中的view位置
	 */
	private int selected = 0;

	/**
	 *   最大放大倍数
	 */
	public static   final float MAX_SCALE_COEFFICIENT=1.9f;

	/**
	 * 是否可点击 （防止在动画过程中重复点击）
	 */
	private boolean clickable=true;

	int resWidth = 0;
	int resHeight = 0;

	public CircleMenuLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// 无视padding
		setPadding(0, 0, 0, 0);
		// view占屏幕的尺寸  取屏幕的高度
		resWidth = resHeight = (int) (getDefaultWidth()*2);
	}

	/**
	 * 设置布局的宽高，并策略menu item宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(resWidth, resHeight);
		// 获得view直径
		mRadius = Math.min(getMeasuredWidth(), getMeasuredHeight());
		// menu item数量
		final int count = getChildCount();
		// menu item尺寸
		int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
		// menu item测量模式
		int childMode = MeasureSpec.EXACTLY;

		// 迭代测量
		for (int i = 0; i < count; i++)
		{
			final View child = getChildAt(i);

			if (child.getVisibility() == GONE)
			{
				continue;
			}

			// 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
			int makeMeasureSpec = -1;

			makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
					childMode);
			child.measure(makeMeasureSpec, makeMeasureSpec);
		}

		mPadding = RADIO_PADDING_LAYOUT * mRadius;
	}


	/**
	 * 设置menu item的位置
	 *
	 * 控制view的放大缩小系数
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		int layoutRadius = mRadius;

		final int childCount = getChildCount();

		int left, top;
		// menu item 的尺寸
		int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

		// 根据menu item的个数，计算角度
		float angleDelay = 360 / getChildCount();

		// 遍历去设置menuitem的位置
		for (int i = 0; i < childCount; i++)
		{
			final CircleIMenuItemView child = (CircleIMenuItemView)getChildAt(i);

			if (child.getVisibility() == GONE)
			{
				continue;
			}

			mStartAngle %= 360;
			child.setAngle(mStartAngle);
			child.setPosition(i);

//			Log.d(TAG, "onLayout: mStartAngle= "+mStartAngle);
			//获取进入选中区域的item
			if (Math.abs(mStartAngle - START_ANGLE) < (angleDelay / 2)) {
				if (child.getPosition()==selected&&
						Math.abs(mStartAngle-START_ANGLE)<5) {//设置初始化时第一个item放大倍数为2倍 允许角度误差5度
					scaleView(child, MAX_SCALE_COEFFICIENT);
				}
				//避免多次回调selected changed监听
				if (selected!=child.getPosition()) {
					selected = child.getPosition();
					if (mOnItemSelectedListener != null) {
						mOnItemSelectedListener.onItemSelected(child, selected,
								child.getId(), child.getName(), child.getContent());
					}
				}
			}

			//放大缩小范围
			if (Math.abs(child.getAngle()-START_ANGLE)<angleDelay){
				float scaleTo=child.getAngle()-START_ANGLE>0?MAX_SCALE_COEFFICIENT-(child.getAngle()-START_ANGLE)/angleDelay:
						MAX_SCALE_COEFFICIENT+(child.getAngle()-START_ANGLE)/angleDelay;
				scaleView(child,scaleTo);
			}

			child.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					handlClickEvent(child,child.getPosition());
				}
			});

			// 计算，中心点到menu item中心的距离
			float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

			// tmp cosa 即menu item中心点的横坐标
			left = layoutRadius/2
					+ (int) Math.round(tmp
					* Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
					* cWidth);
			// tmp sina 即menu item的纵坐标
			top = layoutRadius/ 2
					+ (int) Math.round(tmp
					* Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
					* cWidth);

			child.layout(left, top, left + cWidth, top + cWidth);
			// 叠加尺寸
			mStartAngle += angleDelay;
		}
	}

	private  boolean isClickEvent=false;
	/**
	 *   Created by xwg on "16/10/28". on "12:39".
	 *   处理点击事件，若点击非选中区域item时 执行旋转操作
	 */

	private void handlClickEvent(CircleIMenuItemView child, int position) {
		if (selected==position) {
			if (mOnItemClickListener != null) {
				if (clickable)
					mOnItemClickListener.onItemClick(child,child.getPosition());
				clickable=false;
			}
		}else{
			isClickEvent=true;
			rotateViewToCenter(child, false);
		}
	}

	/**
	 * 记录上一次的x，y坐标
	 */
	private float mLastX;
	private float mLastY;

	/**
	 * 自动滚动的Runnable
	 */
	private AutoFlingRunnable mFlingRunnable;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();

//		Log.e("TAG", "x = " + x + " , y = " + y);

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:

				mLastX = x;
				mLastY = y;
				mDownTime = System.currentTimeMillis();
				mTmpAngle = 0;

				// 如果当前已经在快速滚动
				if (isFling)
				{
					// 移除快速滚动的回调
					removeCallbacks(mFlingRunnable);
					isFling = false;
					return true;
				}

				break;
			case MotionEvent.ACTION_MOVE:
				if (clickable) {
					/**
					 * 获得开始的角度
					 */
					float start = getAngle(mLastX, mLastY);
					/**
					 * 获得当前的角度
					 */
					float end = getAngle(x, y);

//				Log.i("TAG", "start = " + start + " , end =" + end);
					// 如果是一、四象限，则直接end-start，角度值都是正值
					if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
						mStartAngle += end - start;
						mTmpAngle += end - start;
					} else
					// 二、三象限，色角度值是付值
					{
						mStartAngle += start - end;
						mTmpAngle += start - end;
					}
					// 重新布局
					requestLayout();

					mLastX = x;
					mLastY = y;
				}
				break;
			case MotionEvent.ACTION_UP:
				if(clickable) {
					// 计算，每秒移动的角度
					float anglePerSecond = mTmpAngle * 1000
							/ (System.currentTimeMillis() - mDownTime);

//				Log.e("TAG", "anglePrMillionSecond= "+anglePerSecond  +"   mTmpAngel = " +
//						mTmpAngle);

					// 如果达到该值认为是快速移动
					if (Math.abs(anglePerSecond) > mFlingableValue && !isFling) {
						// post一个任务，去自动滚动
						post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));

						return true;
					} else {
						rotateViewToCenter(getSelectedItem(), false);
					}

					// 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
					if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
						return true;
					}
				}
				break;
		}
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 主要为了action_down时，返回true
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}

	/**
	 * 根据触摸的位置，计算角度
	 *
	 * @param xTouch
	 * @param yTouch
	 * @return
	 */
	private float getAngle(float xTouch, float yTouch)
	{
		double x = xTouch - (mRadius / 2d);
		double y = yTouch - (mRadius / 2d);
		return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
	}

	/**
	 * 根据当前位置计算象限
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	private int getQuadrant(float x, float y)
	{
		int tmpX = (int) (x - mRadius / 2);
		int tmpY = (int) (y - mRadius / 2);
		if (tmpX >= 0)
		{
			return tmpY >= 0 ? 4 : 1;
		} else
		{
			return tmpY >= 0 ? 3 : 2;
		}

	}

	/**
	 *   缩放或放大view
	 */
	private void scaleView(final View view, float scaleTo){
		if (scaleTo<1||scaleTo>MAX_SCALE_COEFFICIENT){
			return;
		}
		ObjectAnimator animatorX= ObjectAnimator.ofFloat(view,"scaleX",scaleTo);
		ObjectAnimator animatorY= ObjectAnimator.ofFloat(view,"scaleY",scaleTo);
		AnimatorSet animatorSet=new AnimatorSet();
		animatorSet.setStartDelay(0);
		animatorSet.setDuration(0);
		animatorSet.playTogether(animatorX,animatorY);
		animatorSet.start();
//		animatorSet.addListener(new AnimatorListenerAdapter() {
//			@Override
//			public void onAnimationEnd(Animator animation) {
//			}
//		});

	}

	/**
	 * 如果每秒旋转角度到达该值，则认为是自动滚动
	 *
	 * @param mFlingableValue
	 */
	public void setFlingableValue(int mFlingableValue)
	{
		this.mFlingableValue = mFlingableValue;
	}

	/**
	 * 设置内边距的比例
	 *
	 * @param mPadding
	 */
	public void setPadding(float mPadding)
	{
		this.mPadding = mPadding;
	}

	/**
	 * 获得默认该layout的尺寸
	 *
	 * @return
	 */
	public int getDefaultWidth()
	{
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
	}
	/**
	 * 获取最靠近被选中位置的view
	 */
	public CircleIMenuItemView getSelectedItem() {
		return (selected >= 0) ?(CircleIMenuItemView) getChildAt(selected) : null;
	}
	/**
	 * 旋转离第一个view初始位置最近的item到初始坐标
	 * @param view			需要旋转的view
	 */
	private void rotateViewToCenter(CircleIMenuItemView view, boolean fromRunnable ) {
		float velocityTemp = 1;
		float destAngle = (float) (START_ANGLE - view.getAngle());
		float startAngle = 0;
		int reverser = 1;

		if (destAngle < 0) {
			destAngle += 360;
		}

		if (destAngle > 180) {
			reverser = -1;
			destAngle = 360 - destAngle;
		}

		while (startAngle < destAngle) {
			startAngle += velocityTemp / 40;
			velocityTemp *= 1.0666F;
		}
		post(new AutoFlingRunnable(reverser * velocityTemp,
				!fromRunnable));
	}

	/**
	 * 自动滚动的任务
	 */

	private class AutoFlingRunnable implements Runnable
	{
		boolean isFirstForwarding = true;

		public AutoFlingRunnable(float velocity)
		{
			this(velocity, true);
		}

		public AutoFlingRunnable(float velocity,boolean isFirst) {
			angelPerSecond = velocity;
			this.isFirstForwarding = isFirst;
		}

		public void run()
		{
			// 如果每秒转动角度小于10,则停止，大于则回调自身并逐渐减小角度
			if ((int) Math.abs(angelPerSecond) >5)
			{
				isFling = true;
				// 不断改变mStartAngle，让其滚动，/40为了避免滚动太快
				mStartAngle += (angelPerSecond / 40);
				// 逐渐减小这个值
				angelPerSecond /= 1.0666F;
				// 重新布局
				requestLayout();
				post(this);
			}else{
				if (isFirstForwarding) {
					isFirstForwarding = false;
					rotateViewToCenter(
							(CircleIMenuItemView) getChildAt(selected), true);
				}else{
					isFling=false;
					if (isClickEvent)
					handlClickEvent((CircleIMenuItemView) getChildAt(selected),selected);
					isClickEvent=false;
				}
			}
		}
	}
	/**
	 *   设置item是否可点击， （防止在动画过程中重复点击）
	*/
	public void setClickable(boolean clickable){
		this.clickable=clickable;
	}
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener {
		void onItemClick(CircleIMenuItemView view, int position);
	}

	public void setOnItemSelectedListener(
			OnItemSelectedListener onItemSelectedListener) {
		this.mOnItemSelectedListener = onItemSelectedListener;
	}

	public interface OnItemSelectedListener {
		void onItemSelected(View view, int position, long id, String name, String content);
	}

}
