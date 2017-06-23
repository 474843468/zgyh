package com.chinamworld.bocmbci.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.chinamworld.bocmbci.R;

/**
 * 字母排序列表
 * @author Administrator
 *
 */
public class AlphabeticalView extends ImageView {
	private Drawable alphaDrawable;
	private int choose; // 当前选中首字母的位置
	private String[] ALPHAS;
	private OnAlphaChangedListener listener;

	public AlphabeticalView(Context context) {
		super(context);
		initAlphaView();
	}

	public AlphabeticalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAlphaView();
	}

	public AlphabeticalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAlphaView();
	}

	private void initAlphaView() {
		choose = -1;
		setImageResource(R.drawable.alpha_normal);
		alphaDrawable = getDrawable();
		
		ALPHAS = new String[28];
		ALPHAS[0] = "推荐"; // " "代表推荐
		ALPHAS[27] = "#";
		for (int i = 0; i < 26; i++) {
			ALPHAS[i + 1] = String.valueOf((char) (65 + i));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		setImageResource(R.drawable.alpha_normal);
		alphaDrawable = getDrawable();
		alphaDrawable.setBounds(0, 0, getWidth(), getHeight());
		canvas.save();
		alphaDrawable.draw(canvas);
		canvas.restore();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final float y = event.getY();
		final int oldChoose = choose;
		final int c = (int) (y / getHeight() * 28);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < ALPHAS.length) {
					listener.OnAlphaChanged(ALPHAS[c], c);
					choose = c;
				}
			}
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < ALPHAS.length) {
					listener.OnAlphaChanged(ALPHAS[c], c);
					choose = c;
				}
			}
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	// 设置事件
	public void setOnAlphaChangedListener(OnAlphaChangedListener listener) {
		this.listener = listener;
	}

	// 事件接口
	public interface OnAlphaChangedListener {
		public void OnAlphaChanged(String s, int index);
	}

}
