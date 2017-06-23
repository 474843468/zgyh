package com.chinamworld.bocmbci.biz.main.view;


import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.chinamworld.bocmbci.log.LogGloble;

@SuppressLint("WrongCall")
public class DraggableGridView extends ViewGroup implements View.OnTouchListener, View.OnClickListener, View.OnLongClickListener {
	//layout vars
	public static float childRatio = .9f;
    protected int colCount, childSize, padding, vpadding, dpi, scroll = 0;
    protected float lastDelta = 0;
    protected Handler handler = new Handler();
    //dragging vars
    protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
    protected boolean enabled = true, touching = false;
    //anim vars
    public static int animT = 150;
    protected ArrayList<Integer> newPositions = new ArrayList<Integer>();
    //listeners
    protected OnRearrangeListener onRearrangeListener;
    protected OnClickListener secondaryOnClickListener;   
    private OnItemClickListener onItemClickListener;
    
    private boolean isAnima = false;
    Animation animation;
    //CONSTRUCTOR AND HELPERS
    public DraggableGridView (Context context, AttributeSet attrs) {
        super(context, attrs);
        setListeners();
        handler.removeCallbacks(updateTask);//停止这个线程
        handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);//0.5秒之后，开始这个线程
        setChildrenDrawingOrderEnabled(true);//允许子类排序

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		dpi = metrics.densityDpi;
    }
    protected void setListeners()
    {
    	setOnTouchListener(this);//设置onTouch监听
    	super.setOnClickListener(this);
        setOnLongClickListener(this);//设置长按监听
    }
    @Override
    public void setOnClickListener(OnClickListener l) {
    	secondaryOnClickListener =l;
    }
    protected Runnable updateTask = new Runnable() {
		public void run()
        {
            if (dragged != -1)
            {
            	if (lastY < padding * 3 && scroll > 0)
            		scroll -= 20;
            	else if (lastY > getBottom() - getTop() - (padding * 3) && scroll < getMaxScroll())
            		scroll += 20;
            }
            else if (lastDelta != 0 && !touching)
            {
            	scroll += lastDelta;
            	lastDelta *= .9;
            	if (Math.abs(lastDelta) < .25)
            		lastDelta = 0;
            }
            clampScroll();
            onLayout(true, getLeft(), getTop(), getRight(), getBottom());
        
            handler.postDelayed(this, 25);
        }
    };
    
    //OVERRIDES
    @Override
    public void addView(View child) {
    	super.addView(child);
    	child.setOnTouchListener(this);
    	newPositions.add(-1);
    };
    @Override
    public void removeViewAt(int index) {
    	super.removeViewAt(index);
    	newPositions.remove(index);
    };
    
    //LAYOUT
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	//compute width of view, in dp
//        float w = (r - l) / (dpi / 160f);
//        LogGloble.i("info", w+"========dpi===="+dpi);
        //determine number of columns, at least 2
        colCount = 4;
//        int sub = 240;
//        w -= 280;
//        while (w > 0)
//        {
//        	colCount++;
//        	w -= sub;
//        	sub += 40;
//        }
        
        //determine childSize and padding, in px
        childSize = (r - l) / 4;
        childSize = Math.round(childSize * childRatio);
        padding = ((r - l) - (childSize * colCount)) / (colCount+1);
        vpadding = 4*padding;
    	
        for (int i = 0; i < getChildCount(); i++)
        	if (i != dragged)
        	{
        		//确定在那个控件的左上角的点
	            Point xy = getCoorFromIndex(i);
	            getChildAt(i).layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize+vpadding+padding);
        	}
    }
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
    	if (dragged == -1)
    		return i;
    	else if (i == childCount - 1)
    		return dragged;
    	else if (i >= dragged)
    		return i + 1;
    	return i;
    }
    
    /**
     * 根据坐标返回位置
     * @param x 坐标X
     * @param y 坐标Y
     * @return
     */
    public int getIndexFromCoor(int x, int y)
    {
        int col = getColOrRowFromCoor(x), row = getColOrRowFromCoor1(y + scroll); 
        if (col == -1 || row == -1) //touch is between columns or rows
            return -1;
        int index = row * colCount + col;
        if (index >= getChildCount())
            return -1;
        return index;
    }
    /**
     * 根据X坐标点返回行数
     * @param coor
     * @return
     */
    protected int getColOrRowFromCoor(int coor)
    {
        coor -= padding;
        for (int i = 0; coor > 0; i++)
        {
            if (coor < childSize){
            return i;}
            coor -= (childSize + padding);
        }
        return -1;
    }
     
    /**
     * 根据Y坐标点返回行数
     * @param coor
     * @return
     */
    protected int getColOrRowFromCoor1(int coor)
    {
        coor -= vpadding;
        for (int i = 0; coor > 0; i++)
        {
            if (coor < childSize){
            return i;}
            coor -= (childSize + vpadding);
        }
        return -1;
    }
    
    /**
     * 更据x，y返回目标的View的index
     * @param x
     * @param y
     * @return
     */
    protected int getTargetFromCoor(int x, int y)
    {
        if (getColOrRowFromCoor1(y + scroll) == -1) //touch is between rows
            return -1;
//        if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
//            return -1;
        
        int leftPos = getIndexFromCoor(x - (childSize / 4), y);
        int rightPos = getIndexFromCoor(x + (childSize / 4), y);
        if (leftPos == -1 && rightPos == -1) //touch is in the middle of nowhere
            return -1;
        if (leftPos == rightPos) //touch is in the middle of a visual
        	return -1;
        
        int target = -1;
        if (rightPos > -1)
            target = rightPos;
        else if (leftPos > -1)
            target = leftPos + 1;
        if (dragged < target)
            return target - 1;
        return target;
    }
    
    /**
     * 根据子index of view 确定左上角的点
     * @param index
     * @return
     */
    protected Point getCoorFromIndex(int index)
    {
        int col = index % colCount;
        int row = index / colCount;
        return new Point(childSize*col + padding*(col+1),padding + (childSize + vpadding) * row - scroll);
//        return new Point((childSize + padding) * col,
//                         padding + (childSize + vpadding) * row - scroll);
    }
    
    /**
     * 根据子view ，返回view在父view中index
     * @param child
     * @return
     */
    public int getIndexOf(View child)
    {
    	for (int i = 0; i < getChildCount(); i++)
    		if (getChildAt(i) == child)
    			return i;
    	return -1;
    }
    
    //EVENT HANDLERS
    /**
     * 点击事件
     */
    public void onClick(View view) {
    	if (enabled)//可用
    	{
    		if (secondaryOnClickListener != null)
    			secondaryOnClickListener.onClick(view);
    		if (onItemClickListener != null && getLastIndex() != -1)
    		{
    			if(!isAnima){//当不在动画时，点击事件有效
    				onItemClickListener.onItemClick(null, getChildAt(getLastIndex()), getLastIndex(), getLastIndex() / colCount);
    			}
    		}
    	}
//    	
    }
    
    /**
     * 长按事件
     */
    public boolean onLongClick(View view)
    {
    	if (!enabled)
    		return false;
        int index = getLastIndex();
        if (index != -1)
        {
        	startAnimation(this);
            dragged = index;
            animateDragged();
            onRearrangeListener.beginAnimation();
            return true;
        }
       
        return false;
    }
    
    /**
     * onTouch事件
     */
	public boolean onTouch(View view, MotionEvent event)
    {
        int action = event.getAction();
           switch (action & MotionEvent.ACTION_MASK) {
               case MotionEvent.ACTION_DOWN:
            	   LogGloble.i("info", "domw");
            	   enabled = true;
                   lastX = (int) event.getX();
                   lastY = (int) event.getY();
                   touching = true;
                   break;
               case MotionEvent.ACTION_MOVE:
            	   LogGloble.i("info", "onTouch====Move"+dragged);
            	   
//            	   if(pressTime < 500){
//            		   LogGloble.i("info", "onTouch==pressTime==Move"+dragged);
//            		   return false;
//            	   }
            	   
            	   int delta = lastY - (int)event.getY();
                   if (dragged != -1)
                   {
                       //change draw location of dragged visual
                       int x = (int)event.getX();
                       int y = (int)event.getY();
                       int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
                       getChildAt(dragged).layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));
                       //check for new target hover
                       int target = getTargetFromCoor(x, y);
                       if (lastTarget != target)
                       {
                           if (target != -1)
                           {
                               animateGap(target);
                               lastTarget = target;
                           }
                       }
                   }
                   else
                   {
                	   scroll += delta;
                	   clampScroll();
                	   if (Math.abs(delta) > 2)
                    	   enabled = false;
                	   onLayout(true, getLeft(), getTop(), getRight(), getBottom());
                   }
                   lastX = (int) event.getX();
                   lastY = (int) event.getY();
                   lastDelta = delta;
                   break;
               case MotionEvent.ACTION_UP:
                   if (dragged != -1)
                   {
                	   View v = getChildAt(dragged);
                       if (lastTarget != -1)
                           reorderChildren();
                       else
                       {
                           Point xy = getCoorFromIndex(dragged);
                           v.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize);
                       }
                       v.clearAnimation();
                       if (v instanceof ImageView)
                    	   ((ImageView)v).setAlpha(255);
                       lastTarget = -1;
                       dragged = -1;
                       v.startAnimation(createFastRotateAnimation());
                   }
                   LogGloble.i("info", "ACTION_UP==");
                   touching = false;
                   
           }
        if (dragged != -1)
        	return true;
        return false;
    }
    
    //EVENT HELPERS
	/**
	 * 长按动画
	 */
    protected void animateDragged()
    {
    	View v = getChildAt(dragged);
    	int x = getCoorFromIndex(dragged).x + childSize / 2;
    	int y = getCoorFromIndex(dragged).y + childSize / 2;
        int l = x - (3 * childSize / 4);
        int t = y - (3 * childSize / 4);
    	v.layout(l+padding, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));
    	AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(.667f, 1, .667f, 1, childSize * 3 / 4, childSize * 3 / 4);
		scale.setDuration(animT);
		AlphaAnimation alpha = new AlphaAnimation(1, .5f);
		alpha.setDuration(animT);

		animSet.addAnimation(scale);
		animSet.addAnimation(alpha);
		animSet.addAnimation(createFastRotateAnimation());
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		
		v.clearAnimation();
		v.startAnimation(animSet);
    }
    
    /**
     * 位移动画
     * @param target 移动到的目标
     */
    protected void animateGap(int target)
    {
    	for (int i = 0; i < getChildCount(); i++)
    	{
    		final View v = getChildAt(i);
    		if (i == dragged)
	    		continue;
    		int newPos = i;
    		if (dragged < target && i >= dragged + 1 && i <= target)
    			newPos--;
    		else if (target < dragged && i >= target && i < dragged)
    			newPos++;
    		
    		//animate
    		int oldPos = i;
    		if (newPositions.get(i) != -1)
    			oldPos = newPositions.get(i);
    		if (oldPos == newPos)
    			continue;
    		
    		Point oldXY = getCoorFromIndex(oldPos);
    		Point newXY = getCoorFromIndex(newPos);
    		Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
    		Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());
    		AnimationSet animSet = new AnimationSet(true);
    		TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE, oldOffset.x,
																  Animation.ABSOLUTE, newOffset.x,
																  Animation.ABSOLUTE, oldOffset.y,
																  Animation.ABSOLUTE, newOffset.y);
			translate.setDuration(animT);
			translate.setFillEnabled(true);
			animSet.addAnimation(translate);
			animSet.addAnimation(createFastRotateAnimation1());
			translate.setFillAfter(true);
			
			
			v.clearAnimation();
			v.startAnimation(animSet);
    		
			newPositions.set(i, newPos);
    	}
    }
    
    /**
     * 重新给子view排序
     */
	@SuppressLint("WrongCall")
	protected void reorderChildren()
    {
        //FIGURE OUT HOW TO REORDER CHILDREN WITHOUT REMOVING THEM ALL AND RECONSTRUCTING THE LIST!!!
    	if (onRearrangeListener != null)
    		onRearrangeListener.onRearrange(dragged, lastTarget);
        ArrayList<View> children = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i++)
        {
        	getChildAt(i).clearAnimation();
            children.add(getChildAt(i));
        }
        removeAllViews();
        while (dragged != lastTarget)
            if (lastTarget == children.size()) // dragged and dropped to the right of the last element
            {
                children.add(children.remove(dragged));
                dragged = lastTarget;
            }
            else if (dragged < lastTarget) // shift to the right
            {
                Collections.swap(children, dragged, dragged + 1);
                dragged++;
            }
            else if (dragged > lastTarget) // shift to the left
            {
                Collections.swap(children, dragged, dragged - 1);
                dragged--;
            }
        for (int i = 0; i < children.size(); i++)
        {
        	newPositions.set(i, -1);
            addView(children.get(i));
        }
        onLayout(true, getLeft(), getTop(), getRight(), getBottom());
        if(isAnima){
        	startAnimation(this);
        }
    }
	
	/**
	 * 滑动到顶部
	 */
    public void scrollToTop()
    {
    	scroll = 0;
    }
    
    /**
     * 滑动到底部
     */
    public void scrollToBottom()
    {
    	scroll = Integer.MAX_VALUE;
    	clampScroll();
    }
    
    /**
     * 控制滑动
     */
    protected void clampScroll()
    {
    	int stretch = 3; //
    	int overreach = getHeight() / 2;//最多滑动的距离
    	int max = getMaxScroll();//获取最大的滑动值
    	max = Math.max(max, 0);
    	
    	if (scroll < -overreach)
    	{
    		scroll = -overreach;
    		lastDelta = 0;
    	}
    	else if (scroll > max + overreach)
    	{
    		scroll = max + overreach;
    		lastDelta = 0;
    	}
    	else if (scroll < 0)
    	{
	    	if (scroll >= -stretch)
	    		scroll = 0;
	    	else if (!touching)
	    		scroll -= scroll / stretch;
    	}
    	else if (scroll > max)
    	{
    		if (scroll <= max + stretch)
    			scroll = max;
    		else if (!touching)
    			scroll += (max - scroll) / stretch;
    	}
    }
    
    /**
     * 最大滑动的距离里
     * @return
     */
    protected int getMaxScroll()
    {
    	int rowCount = (int)Math.ceil((double)getChildCount()/colCount);//获取有多少行
    	int max = rowCount * childSize + (rowCount + 1) * vpadding - getHeight();
    	return max;
    }
    
    /**
     * 最后触摸的坐标
     * @return
     */
    public int getLastIndex()
    {
    	return getIndexFromCoor(lastX, lastY);
    }
    
    //OTHER METHODS
    public void setOnRearrangeListener(OnRearrangeListener l)
    {
    	this.onRearrangeListener = l;
    }
    public void setOnItemClickListener(OnItemClickListener l)
    {
    	this.onItemClickListener = l;
    }
    
    
    /**
     * 计算子view的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// TODO Auto-generated method stub
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSpecSize, heightSpecSize);
        for(int i = 0; i < getChildCount();i++){
        	View child=getChildAt(i);
        	child.measure(widthMeasureSpec, heightMeasureSpec + 50);
        	setMeasuredDimension(widthSpecSize, heightSpecSize +50);
        }
    }
    
    /**
     * 为子控件，添加抖动动画
     * @param view
     */
    public void startAnimation(ViewGroup view) {
    	isAnima = true;
    	animation = createFastRotateAnimation();
		view.setLayoutAnimation(new LayoutAnimationController(animation, 0.0f));
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
    
    protected Animation createFastRotateAnimation1() {
		Animation rotate = new RotateAnimation(-1.0f, 1.0f);
		rotate.setRepeatMode(Animation.REVERSE);
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setDuration(80);
//		rotate.setInterpolator(new AccelerateDecelerateInterpolator());
		return rotate;
	}
    /**
     * 取消动画
     */
    public void cancelAnimation(){
    	if(isAnima == true){
    		isAnima = false;
    		for (int j = 0; j <getChildCount(); j++){
            	getChildAt(j).clearAnimation();
            }
    	}
    }
    
    public String getChildOrder(){
    	StringBuffer sbuffer = new StringBuffer();
    	String index = "";
    	for(int i = 0; i < getChildCount(); i++){
    		sbuffer.append(getChildAt(i).getTag()+",");
    	}
    	if(sbuffer.length() != 0){
    		index = sbuffer.substring(0, sbuffer.length()-1);
    	}
    	return index;
    }
    
    
    
    long startTime;
    
    long endTime;
    
    long pressTime;
    
    int lastYY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	// TODO Auto-generated method stub
    	
//		if (action == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
//			// 该事件可能不是我们的
//			return false;
//		}
//    	boolean isInter = false;
    	final int action = ev.getActionMasked();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			 startTime = System.currentTimeMillis();
			 enabled = true;
             lastX = (int) ev.getX();
             lastY = (int) ev.getY();
             touching = true;
			 return  false;
		case MotionEvent.ACTION_MOVE:
			
			lastYY = (int) ev.getY();
			endTime = System.currentTimeMillis();
			LogGloble.i("info", "onInten======"+(endTime-startTime));
			pressTime = endTime - startTime;
			if(pressTime > 800 )
			{
				performLongClick();
				return  true;
			}
			if(Math.abs(lastYY - lastY) > 20){
				return true;
			}
		}
		return super.onInterceptTouchEvent(ev);
    }
    
    
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//    	// TODO Auto-generated method stub
//    	
//    	
//    	int action = event.getActionMasked();
//    	LogGloble.i("info", "onTouchEvent======"+action);
////		if (action == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
////			// 该事件可能不是我们的
////			return false;
////		}
////    	boolean isInter = false;
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
//			LogGloble.i("info", "onTouchEvent===down");
//			 enabled = true;
//             lastX = (int) event.getX();
//             lastY = (int) event.getY();
//             touching = true;
//        }
////			LogGloble.i("info", "Intercept===down"+isInter);
////			return  true;
//    	return super.onTouchEvent(event);
//    }
    
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//    	// TODO Auto-generated method stub
//    	return false;
//    }
}