package com.chinamworld.bocmbci.biz.peopleservice.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;

public class PeopleServiceListView extends ListView{

	public PeopleServiceListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PeopleServiceListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PeopleServiceListView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		int index = pointToPosition(x, y);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (index==AdapterView.INVALID_POSITION) {
				break;
			}else {
				if (index==0) {
					if (index==(getCount()-1)) {
						setSelector(R.drawable.shape_peopleservice_listview_round);
					}else {
						setSelector(R.drawable.shape_peopleservice_listview_up);
					}
				}else if (index==(getCount()-1)) {
					setSelector(R.drawable.shape_peopleservice_listview_down);
				}else {
					setSelector(R.drawable.shape_peopleservice_listview_center);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			break;

		}
		return super.onInterceptTouchEvent(ev);
	}

}
