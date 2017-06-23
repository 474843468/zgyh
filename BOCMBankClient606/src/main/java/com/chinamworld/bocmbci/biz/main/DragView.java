package com.chinamworld.bocmbci.biz.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

public class DragView {

	private static final int DRAG_SCALE = 0;
	private static final int defaul_width = 60;

	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	private ImageView mView;
	private int mRegistrationX;
	private int mRegistrationY;
	private float mScale;
	private Bitmap mBitmap;

	public DragView(Context context, Bitmap bitmap) {
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(defaul_width, defaul_width, Bitmap.Config.RGB_565);
		}
		Matrix scale = new Matrix();
		float scaleFactor = bitmap.getWidth();
		scaleFactor = mScale = (scaleFactor + DRAG_SCALE) / scaleFactor;
		scale.setScale(scaleFactor, scaleFactor);

		mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), scale, true);

		mView = new ImageView(context);
		mView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mView.setImageBitmap(mBitmap);

	}

	public void show(IBinder windowToken, int touchX, int touchY) {
		WindowManager.LayoutParams lp;
		int pixelFormat;

		pixelFormat = PixelFormat.TRANSLUCENT;

		lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
				touchX/* - mRegistrationX */, touchY /*- mRegistrationY*/,
				WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
				/* | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM */, pixelFormat);
		// lp.token = mStatusBarView.getWindowToken();
		lp.gravity = Gravity.LEFT | Gravity.TOP;
		// lp.token = windowToken;
		lp.setTitle("DragView");
		mLayoutParams = lp;

		mWindowManager.addView(mView, lp);

		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.5f, 0.5f,
		// 1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		// scaleAnimation.setDuration(2000);
//		scaleAnimation.setFillEnabled(true);
//		scaleAnimation.setFillBefore(true);
//		mView.startAnimation(scale);

	}

	public void move(int touchX, int touchY) {
		WindowManager.LayoutParams lp = mLayoutParams;
		lp.x = touchX - mRegistrationX;
		lp.y = touchY - mRegistrationY;
		mWindowManager.updateViewLayout(mView, lp);
	}

	public void remove() {
		mWindowManager.removeView(mView);
	}

	public View getContentView() {
		return mView;
	}

}
