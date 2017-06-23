package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.chinamworld.bocmbci.R;

public class AssortView extends Button {

	public interface OnTouchAssortListener {
		public void onTouchAssortListener(String s);
		public void onTouchAssortUP();
	}

	public AssortView(Context context) {
		super(context);
	}

	public AssortView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AssortView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 分类
	private String[] assort = {
//			"?", "#", 
			"A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };
	private Paint paint = new Paint();
	// 选择的索引
	private int selectIndex = -1;
	// 字母监听器
	private OnTouchAssortListener onTouch;

	public void setOnTouchAssortListener(OnTouchAssortListener onTouch) {
		this.onTouch = onTouch;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int interval = height / assort.length;

		for (int i = 0, length = assort.length; i < length; i++) {
			// 抗锯齿
			paint.setAntiAlias(true);
			// 默认粗体
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
			// 白色
			paint.setColor(getResources().getColor(R.color.black));
			paint.setTextSize(getResources().getDimension(R.dimen.textsize_one_three));
			if (i == selectIndex) {
				// 被选择的字母改变颜色和粗体
				paint.setColor(getResources().getColor(R.color.red));
				paint.setFakeBoldText(true);
				paint.setTextSize(2 * getResources().getDimension(R.dimen.textsize_one_three));
			}
			// 计算字母的X坐标
			float xPos = width / 2 - paint.measureText(assort[i]) / 2;
			// 计算字母的Y坐标
			float yPos = interval * i + interval;
			canvas.drawText(assort[i], xPos, yPos, paint);
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		float y = event.getY();
		int index = (int) (y / getHeight() * assort.length);
		if (index >= 0 && index < assort.length) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				// 如果滑动改变
				if (selectIndex != index) {
					selectIndex = index;
					if (onTouch != null) {
						onTouch.onTouchAssortListener(assort[selectIndex]);
					}
				}
				break;
			case MotionEvent.ACTION_DOWN:
				selectIndex = index;
				if (onTouch != null) {
					onTouch.onTouchAssortListener(assort[selectIndex]);
				}

				break;
			case MotionEvent.ACTION_UP:
				if (onTouch != null) {
					onTouch.onTouchAssortUP();
				}
				selectIndex = -1;
				break;
			}
		} else {
			selectIndex = -1;
			if (onTouch != null) {
				onTouch.onTouchAssortUP();
			}
		}
		invalidate();

		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}
