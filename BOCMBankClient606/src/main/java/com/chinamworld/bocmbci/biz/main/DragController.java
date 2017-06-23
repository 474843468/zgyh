package com.chinamworld.bocmbci.biz.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.chinamworld.bocmbci.log.LogGloble;

public class DragController {

	private static final String TAG = DragController.class.getSimpleName();

	private static final int HANDLER_CALLBACK_MESSAGE = 1;
	private static final int EDGE_CALLBACK_MILLIS = 100;
	private static final int EDGE_ADJUST_ORIENTATION = 5;

	private boolean mIsSendCallback = false;
	private View mAttachtLayoutView;
	private Context mContext;
	private DragView mDragPopupView;
	private DragListener mDragListener;

	private Object mDragInfo;
	private float mLastMotionX;

	private Orientation mOrientation;
	private int mScreenWindth;
	private boolean mDragging;
//	private int mScreenHeight;
//	private float mLastY;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == HANDLER_CALLBACK_MESSAGE) {
				Orientation orientation = (Orientation) msg.obj;
				if (orientation != null && mDragListener != null) {
					mDragListener.onDragEdge(orientation);
				}
				mIsSendCallback = false;
			}
		}
	};

	public DragController(Context context, View attachView) {
		super();
		this.mContext = context;
		this.mAttachtLayoutView = attachView;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mScreenWindth = wm.getDefaultDisplay().getWidth();
//		mScreenHeight = wm.getDefaultDisplay().getHeight();
	}

	public void startDrag(View view, Object dragInfo, int xoff, int yoff) {
		if (view == null) {
			return;
		}
		this.mDragInfo = dragInfo;

		int[] location = new int[2];
		view.getLocationOnScreen(location);
		Bitmap bitmap = getViewBitmap(view);

		DragView dragView = mDragPopupView = new DragView(mContext, bitmap);
		dragView.show(null, (int) location[0] + xoff, (int) location[1] + yoff);
		mDragging = true;
		if (mDragListener != null) {
			mDragListener.onDragStart(view, dragInfo);
		}
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	public void cancelDrag() {
		endDrag();
	}

	private void endDrag() {
		mHandler.removeMessages(HANDLER_CALLBACK_MESSAGE);
		if (mDragPopupView != null) {
			if (mDragListener != null) {
				mDragListener.onDragEnd(mDragPopupView, mDragInfo);
			}
			mDragPopupView.remove();
			mDragPopupView = null;
		}
		mDragging = false;
	}

	public boolean onItemLongClickEvent(View view, Object tag) {
		startDrag(view, tag, 0, 0);
		return false;
	}

	public void setOnDragListener(DragListener listener) {
		mDragListener = listener;
	}

	public interface DragListener {

		void onDragStart(View view, Object dragInfo);

		void onDraging(DragView view, Object dragInfo, MotionEvent ev);

		void onDragEnd(DragView view, Object dragInfo);

		void onDragEdge(Orientation orientation);
	}

	// ----------------------------------------------------------------------
	public boolean dispatchKeyEvent(KeyEvent event) {
		LogGloble.i(TAG, "DragController dispatchKeyEvent");
		return false;
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		LogGloble.i(TAG, "DragController onInterceptTouchEvent");
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = ev.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mDragPopupView != null) {
				mAttachtLayoutView.getParent().requestDisallowInterceptTouchEvent(true);

				float x = ev.getRawX();
				float y = ev.getRawY();
				if ((x - mLastMotionX) > EDGE_ADJUST_ORIENTATION) {
					mOrientation = Orientation.LEFT_RIGHT;
				} else if ((mLastMotionX - x) > EDGE_ADJUST_ORIENTATION) {
					mOrientation = Orientation.RIGHT_LEFT;
				}

				int[] dragviewLocation = new int[2];
				View contentView = mDragPopupView.getContentView();
				contentView.getLocationOnScreen(dragviewLocation);

				int dragLeftLocation = dragviewLocation[0];
				int dragRightLocation = dragLeftLocation + contentView.getWidth();
				int widthFalf = contentView.getWidth() / 2;
				int heightFalf = contentView.getHeight() / 2;
				if (dragRightLocation >= mScreenWindth + widthFalf / 2 && mOrientation == Orientation.LEFT_RIGHT
						&& !mIsSendCallback) {
					mIsSendCallback = true;
					mDragPopupView.move((int) (x - widthFalf), (int) (y - heightFalf));

					Message msg = mHandler.obtainMessage();
					msg.what = HANDLER_CALLBACK_MESSAGE;
					msg.obj = mOrientation;
					mHandler.sendMessageDelayed(msg, EDGE_CALLBACK_MILLIS);
				} else if (dragLeftLocation <= (0 - widthFalf / 2) && mOrientation == Orientation.RIGHT_LEFT
						&& !mIsSendCallback) {
					mIsSendCallback = true;
					mDragPopupView.move((int) (x - widthFalf), (int) (y - heightFalf));

					Message msg = mHandler.obtainMessage();
					msg.what = HANDLER_CALLBACK_MESSAGE;
					msg.obj = mOrientation;
					mHandler.sendMessageDelayed(msg, EDGE_CALLBACK_MILLIS);
				} else {
					mDragPopupView.move((int) (x - widthFalf), (int) (y - heightFalf));

					if (mOrientation == Orientation.RIGHT_LEFT && x - mLastMotionX < 0) {
						mHandler.removeMessages(HANDLER_CALLBACK_MESSAGE);
						mIsSendCallback = false;
					} else if (mOrientation == Orientation.LEFT_RIGHT && x - mLastMotionX > 0) {
						mHandler.removeMessages(HANDLER_CALLBACK_MESSAGE);
						mIsSendCallback = false;
					}

				}

				if (mDragListener != null) {
					mDragListener.onDraging(mDragPopupView, mDragInfo, ev);
				}
				mLastMotionX = x;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mAttachtLayoutView.getParent().requestDisallowInterceptTouchEvent(false);
			endDrag();
			break;
		default:
			break;
		}
		return mDragging;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		LogGloble.i(TAG, "DragController onTouchEvent");
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = ev.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mDragPopupView != null) {
				mAttachtLayoutView.getParent().requestDisallowInterceptTouchEvent(true);

				float x = ev.getRawX();
				float y = ev.getRawY();
				if ((x - mLastMotionX) > EDGE_ADJUST_ORIENTATION) {
					mOrientation = Orientation.LEFT_RIGHT;
				} else if ((mLastMotionX - x) > EDGE_ADJUST_ORIENTATION) {
					mOrientation = Orientation.RIGHT_LEFT;
				}

				int[] dragviewLocation = new int[2];
				View contentView = mDragPopupView.getContentView();
				contentView.getLocationOnScreen(dragviewLocation);

				int dragLeftLocation = dragviewLocation[0];
				int dragRightLocation = dragLeftLocation + contentView.getWidth();
				int widthFalf = contentView.getWidth() / 2;
				int heightFalf = contentView.getHeight() / 2;
				if (dragRightLocation >= mScreenWindth + widthFalf / 2 && mOrientation == Orientation.LEFT_RIGHT
						&& !mIsSendCallback) {
					mIsSendCallback = true;
					mDragPopupView.move((int) (x - widthFalf), (int) (y - heightFalf));

					Message msg = mHandler.obtainMessage();
					msg.what = HANDLER_CALLBACK_MESSAGE;
					msg.obj = mOrientation;
					mHandler.sendMessageDelayed(msg, EDGE_CALLBACK_MILLIS);
				} else if (dragLeftLocation <= (0 - widthFalf / 2) && mOrientation == Orientation.RIGHT_LEFT
						&& !mIsSendCallback) {
					mIsSendCallback = true;
					mDragPopupView.move((int) (x - widthFalf), (int) (y - heightFalf));

					Message msg = mHandler.obtainMessage();
					msg.what = HANDLER_CALLBACK_MESSAGE;
					msg.obj = mOrientation;
					mHandler.sendMessageDelayed(msg, EDGE_CALLBACK_MILLIS);
				} else {
					mDragPopupView.move((int) (x - widthFalf), (int) (y - heightFalf));
					if (mOrientation == Orientation.RIGHT_LEFT && x - mLastMotionX < 0) {
						mHandler.removeMessages(HANDLER_CALLBACK_MESSAGE);
						mIsSendCallback = false;
					} else if (mOrientation == Orientation.LEFT_RIGHT && x - mLastMotionX > 0) {
						mHandler.removeMessages(HANDLER_CALLBACK_MESSAGE);
						mIsSendCallback = false;
					}
				}

				if (mDragListener != null) {
					mDragListener.onDraging(mDragPopupView, mDragInfo, ev);
				}
				mLastMotionX = x;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mAttachtLayoutView.getParent().requestDisallowInterceptTouchEvent(false);
			endDrag();
			break;
		default:
			break;
		}
		return true;
	}

	public boolean dispatchUnhandledMove(View focused, int direction) {
		LogGloble.i(TAG, "DragController dispatchUnhandledMove");
		return false;
	}

	private Bitmap getViewBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);

		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);

		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			LogGloble.e(TAG, "failed getViewBitmap(" + v + ")", new RuntimeException());
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);

		return bitmap;
	}
}
