package com.chinamworld.bocmbci.biz.main;

import android.graphics.Point;
import android.view.View;
import android.view.animation.Animation;

public class DragAnim {

	private View view;
	private Point oldPoint;
	private Point newPoint;

	public DragAnim(View view, Point oldPoint, Point newPoint) {
		super();
		this.view = view;
		this.oldPoint = oldPoint;
		this.newPoint = newPoint;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Point getOldPoint() {
		return oldPoint;
	}

	public void setOldPoint(Point oldPoint) {
		this.oldPoint = oldPoint;
	}

	public Point getNewPoint() {
		return newPoint;
	}

	public void setNewPoint(Point newPoint) {
		this.newPoint = newPoint;
	}
	
	public static Animation createDragAnim(DragAnim anim){
		
		return null;
	}
}
