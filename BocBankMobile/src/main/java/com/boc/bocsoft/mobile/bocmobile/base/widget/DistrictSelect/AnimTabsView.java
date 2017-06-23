package com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 *带动画的动态添加tab组件
 * Created by xintong on 2016/6/4.
 */
public class AnimTabsView extends RelativeLayout {
	//选项卡动画播放时间
	private static final int ANIM_DURATION = 600;
	private Context mContext;
	//选项卡总条目
	private int mTotalItemsCount = 0;
	//选中的条目id
	private int mCurrentItemPosition = 0;
	//根视图
	private LinearLayout mItemsLayout;
	//滑动容器
	private Scroller mScroller;
	//显示勾选的图片
	private Bitmap mSlideIcon;
	//选项卡阴影图片
	private Bitmap mShadow;
	//定义区域大小
	private Rect mLeftDrawRect;
	private Rect mRightDrawRect;
	//滑动前X坐标
	private int mCurrentSlideX;
	//滑动前Y坐标
	private int mCurrentSlideY;
	private static boolean index;
	//下划线的长度
	private int lineWidth;
	//选项卡内容
	TextView tv;
	//选项卡选中位置改变监听
	private IAnimTabsItemViewChangeListener mItemViewChangeListener;
	//选项卡点击事件
	private OnTabsItemClickListener onTabsItemClickListener;
	//选项卡点击事件初始化
	private OnClickListener mItemClickListener = new TabsItemViewClickListener(this);


	public AnimTabsView(Context context) {
		this(context, null);
	}

	public AnimTabsView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		
		setWillNotDraw(false);
		
		this.mContext = context;
		this.mLeftDrawRect = new Rect();
		this.mRightDrawRect = new Rect();
		this.mSlideIcon = BitmapFactory.decodeResource(getResources(), R.drawable.boc_districtselect_arr);
		this.mShadow = BitmapFactory.decodeResource(getResources(), R.drawable.boc_districtselect_shadow);
		LinearLayout slideLayout = new LinearLayout(this.mContext);
		slideLayout.setOrientation(LinearLayout.VERTICAL);
		View localView = new View(this.mContext);
		localView.setBackgroundResource(R.drawable.boc_districtselect_bg);
		slideLayout.addView(localView, new LinearLayout.LayoutParams(-1, 0, 1.0F));
		slideLayout.addView(new View(this.mContext), new LinearLayout.LayoutParams(-1, this.mSlideIcon.getHeight()));
		this.mItemsLayout = new LinearLayout(this.mContext);
		this.mItemsLayout.setBackgroundColor(0);
		LayoutParams localLayoutParams = new LayoutParams( -1, -1);
		float f1 = context.getResources().getDisplayMetrics().density;
//		this.mItemsLayout.setPadding((int) (f1 * 20), 0, (int) (f1 * 20), 0);
		localLayoutParams.setMargins(0, 0, 0, (int) (f1 * 4.0F));
		addView(slideLayout);
		addView(this.mItemsLayout, localLayoutParams);
	}

	public int getCount() {
		return this.mTotalItemsCount;
	}

	/**
	 * 选项卡选中动画
	 * @param selectePosition 选中项
     */
	public void selecteItem(int selectePosition) {
		selecteItem(selectePosition, true);
	}

	/**
	 * 选项卡选中动画
	 * @param selectePosition 选中项
	 * @param isAnimate 是否播放动画
     */
	public void selecteItem(int selectePosition, boolean isAnimate) {
		if (this.mCurrentItemPosition == selectePosition)
			return;
		if (this.mScroller == null) {
			this.mScroller = new Scroller(this.mContext, new DecelerateInterpolator());
		}
		if(mCurrentItemPosition<mTotalItemsCount){
			if(((RelativeLayout) getItemView(this.mCurrentItemPosition))!=null){
				((RelativeLayout) getItemView(this.mCurrentItemPosition)).setSelected(false);
			}

		}
		
		this.mCurrentItemPosition = selectePosition;
		if(mCurrentItemPosition<mTotalItemsCount){
			if(getItemView(this.mCurrentItemPosition)!=null){
				getItemView(this.mCurrentItemPosition).setSelected(true);
			}

		}
	
		int oldX = this.mCurrentSlideX;
		int oldY = this.mCurrentSlideY;
		if ((this.mTotalItemsCount > 0) && (getItemView(this.mCurrentItemPosition) != null)) {
			View currentSeletedItemView = getItemView(this.mCurrentItemPosition);
			this.mCurrentSlideX = (currentSeletedItemView.getLeft() + currentSeletedItemView.getWidth() / 2 - this.mSlideIcon .getWidth() / 2);
			if (isAnimate) {				
				this.mScroller.startScroll(oldX, oldY, this.mCurrentSlideX - oldX, this.mCurrentSlideY - oldY, ANIM_DURATION);
			}
		}
		invalidate();
	}

	/**
	 * 设置选中位置改变监听
	 * @param listener
     */
	public void setOnAnimTabsItemViewChangeListener(IAnimTabsItemViewChangeListener listener) {
		this.mItemViewChangeListener = listener;
	}

	/**
	 * 设置选项卡点击监听
	 * @param listener
	 */
	public void setOnTabsItemClickListener(OnTabsItemClickListener listener) {
		this.onTabsItemClickListener = listener;
	}

	/**
	 * 增加选项卡条目
	 * @param itemText 条目内容
     */
	public void addItem(String itemText) {
		RelativeLayout itemLayout = (RelativeLayout) View.inflate(this.mContext, R.layout.boc_districtselect_animtabitem, null);
		tv = ((TextView) itemLayout.getChildAt(0));
		tv.setText(itemText);
		itemLayout.setTag(Integer.valueOf(this.mTotalItemsCount));
		if (this.mTotalItemsCount == 0) {
			itemLayout.setSelected(true);
		}
		itemLayout.setOnClickListener(this.mItemClickListener);
		this.mTotalItemsCount = (1 + this.mTotalItemsCount);
		this.mItemsLayout.addView(itemLayout, new LinearLayout.LayoutParams(dip2px(mContext, (float) 80), -1));
		index = true;
	
	}

	/**
	 * 移除单个选项卡
	 * @param i 条目id
     */
	public void removeItem(int i) {
		this.mItemsLayout.removeViewAt(i);
		this.mTotalItemsCount = (this.mTotalItemsCount-1);
	}

	/**
	 * 移除ID为start之后的所有选项卡
	 * @param start
     */
	public void removeItems(int start){
		int end = mItemsLayout.getChildCount();
		if(start>=0&&end>=0){
			for(int i=end-1;i>=start;i--){
				this.mItemsLayout.removeViewAt(i);
				this.mTotalItemsCount = (this.mTotalItemsCount-1);
			}
			
		}
	}

	/**
	 * 获取现在的选项卡位置id
	 * @return
     */
	public int getCurrentItemPosition() {
		return this.mCurrentItemPosition;
	}

	/**
	 * 获取现在选中的选项卡视图
	 * @param itemPosition
	 * @return
     */
	public View getItemView(int itemPosition) {
		if ((itemPosition >= 0) && (itemPosition < this.mTotalItemsCount)) {
			return this.mItemsLayout.getChildAt(itemPosition);
		}
		return null;
	}

	/**
	 * 重写父类OnDraw()方法绘制界面
	 * @param canvas
     */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if ((this.mScroller == null) || (!this.mScroller.computeScrollOffset())) {
			this.mLeftDrawRect.set(0, this.mCurrentSlideY, this.mCurrentSlideX, this.mCurrentSlideY + this.mShadow.getHeight());
			canvas.drawBitmap(this.mShadow, null, this.mLeftDrawRect, null);
			this.mRightDrawRect.set(this.mCurrentSlideX + this.mSlideIcon.getWidth(), this.mCurrentSlideY, getWidth(), this.mCurrentSlideY
					+ this.mShadow.getHeight());
			canvas.drawBitmap(this.mShadow, null, this.mRightDrawRect, null);
			canvas.drawBitmap(this.mSlideIcon, this.mCurrentSlideX, this.mCurrentSlideY, null);
			return;
		}
		int scrollX = this.mScroller.getCurrX();
		int scrollY = this.mScroller.getCurrY();
		this.mLeftDrawRect.set(0, scrollY, scrollX, scrollY + this.mShadow.getHeight());
		canvas.drawBitmap(this.mShadow, null, this.mLeftDrawRect, null);
		this.mRightDrawRect.set(scrollX + this.mSlideIcon.getWidth(), scrollY, getWidth(),
				scrollY + this.mShadow.getHeight());
		canvas.drawBitmap(this.mShadow, null, this.mRightDrawRect, null);
		canvas.drawBitmap(this.mSlideIcon, scrollX, scrollY, null);
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if ((this.mTotalItemsCount > 0) && (getItemView(this.mCurrentItemPosition) != null)) {

			View currentItemView = getItemView(this.mCurrentItemPosition);
			this.mCurrentSlideX = (currentItemView.getLeft() + currentItemView.getWidth() / 2 - this.mSlideIcon.getWidth() / 2);
			this.mCurrentSlideY = (b - t - this.mSlideIcon.getHeight());

		}
		if(index){
			selecteItem(mTotalItemsCount-1);
		}


	}

	/**
	 * 设置选项卡点击事件
	 */
	public static class TabsItemViewClickListener implements OnClickListener {
		
		private WeakReference<AnimTabsView> mAnimTabsViewReference = null;
		
		TabsItemViewClickListener(AnimTabsView animTabsView) {
			mAnimTabsViewReference = new WeakReference<AnimTabsView>(animTabsView);
		}
		public void onClick(View view) {
			index = false;
			int clickItemPosition = ((Integer) view.getTag()).intValue();
			int currentSeletedItemPosition = (mAnimTabsViewReference.get().getCurrentItemPosition());
			mAnimTabsViewReference.get().onTabsItemClickListener.changeData(clickItemPosition);
			if (currentSeletedItemPosition != clickItemPosition && mAnimTabsViewReference.get() != null) {
				mAnimTabsViewReference.get().selecteItem(clickItemPosition);
				if (mAnimTabsViewReference.get().mItemViewChangeListener != null)
					mAnimTabsViewReference.get().mItemViewChangeListener.onChange(mAnimTabsViewReference.get(), clickItemPosition, currentSeletedItemPosition);
			}
		}
	}
	public abstract interface OnTabsItemClickListener {
		public abstract void changeData(int position);
	}

	public abstract interface IAnimTabsItemViewChangeListener {
		public abstract void onChange(AnimTabsView tabsView, int oldPosition, int currentPosition);
	}

	/**
	 * dip转px
	 * @param context
	 * @param dipValue
     * @return pxValue
     */
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
} 
}