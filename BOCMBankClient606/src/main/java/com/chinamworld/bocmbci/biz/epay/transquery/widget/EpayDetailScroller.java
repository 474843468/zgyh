package com.chinamworld.bocmbci.biz.epay.transquery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class EpayDetailScroller extends ScrollView {
	
	private String tag = "EpayDetailScroller";
	
	private GestureDetector mGestureDetector;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	public EpayDetailScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new XYScrollDetector());
	}
	
	public EpayDetailScroller(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(new XYScrollDetector());
	}
	
	class XYScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			
			if (distanceY != 0 && distanceX != 0) {
			}
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}

}
