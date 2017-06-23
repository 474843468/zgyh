package com.chinamworld.bocmbci.biz.main;

import android.graphics.Point;
import android.view.View;

public class ViewUtils {

	public final static int LOCATION_LEFT_TOP = 0;
	public final static int LOCATION_BOTTOM_RIGHT = 1;

	private static final Point getViewPosition(View view, int type) {
		int[] locations = new int[2];
		view.getLocationOnScreen(locations);
		Point point = new Point(locations[0], locations[1]);
		if (type == LOCATION_LEFT_TOP)
			return point;
		if (type == LOCATION_BOTTOM_RIGHT) {
			int width = view.getWidth();
			int height = view.getHeight();
			point.x += width;
			point.y += height;
			return point;
		}
		return new Point(0, 0);
	}

	public final static boolean rectInView(View src, View desc) {
		Point src_left_top = ViewUtils.getViewPosition(src, ViewUtils.LOCATION_LEFT_TOP);
		Point src_bottom_right = ViewUtils.getViewPosition(src, ViewUtils.LOCATION_BOTTOM_RIGHT);

		Point desc_left_top = ViewUtils.getViewPosition(desc, ViewUtils.LOCATION_LEFT_TOP);
		Point desc_bottom_right = ViewUtils.getViewPosition(desc, ViewUtils.LOCATION_BOTTOM_RIGHT);

		int width = src_bottom_right.x - src_left_top.x;
		int height = src_bottom_right.y - src_left_top.y;

		src_left_top.x = src_left_top.x - width / 2;
		src_left_top.y = src_left_top.y - height / 2;
		src_bottom_right.x = src_bottom_right.x + width / 2;
		src_bottom_right.y = src_bottom_right.y + height / 2;

		return src_left_top.x <= desc_left_top.x && src_left_top.y <= desc_left_top.y
				&& src_bottom_right.x >= desc_bottom_right.x && src_bottom_right.y >= desc_bottom_right.y;
	}

//	public final static boolean pointInView(Point point, View view) {
//		Point left_top = ViewUtils.getViewPosition(view, ViewUtils.LOCATION_LEFT_TOP);
//		Point bottom_right = ViewUtils.getViewPosition(view, ViewUtils.LOCATION_BOTTOM_RIGHT);
//		int width = view.getWidth();
//		int height = view.getHeight();
//		return (point.x >= left_top.x && point.x <= bottom_right.x && point.y >= left_top.y && point.y <= bottom_right.y);
////		return (point.x >= left_top.x + width / 8 && point.x <= bottom_right.x - width / 8
////				&& point.y >= left_top.y + height / 8 && point.y <= bottom_right.y - height / 8);
//	}
}
