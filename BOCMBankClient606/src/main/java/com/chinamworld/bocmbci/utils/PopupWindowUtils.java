package com.chinamworld.bocmbci.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BasePopupWindowUtils;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 自定义的显示PopupWindow
 * 
 * @author xiabaoying
 * 
 *         2013-4-16
 * 
 */
public class PopupWindowUtils extends BasePopupWindowUtils implements OnClickListener {
	private static final String TAG = PopupWindowUtils.class.getSimpleName();
	OnClickListener mClick;
	public static PopupWindowUtils pu;
	private PopupWindow popupWindow;
	private int itemHight;
	/** 查询条件的显示窗体 */
	private PopupWindow mQueryPopupWindow;

	private BaseActivity mAct;

	private boolean isToastPopupWindowShow = false;
	private int popShowPointX, popSHowPointY;

	public boolean isToastPopupWindowShow() {
		return isToastPopupWindowShow;
	}

	public void setToastPopupWindowShow(boolean isToastPopupWindowShow) {
		this.isToastPopupWindowShow = isToastPopupWindowShow;
	}

	public PopupWindowUtils() {
		pu=this;
		Instance=this;
	}

	public static PopupWindowUtils getInstance() {
		if (pu == null) {
			pu = new PopupWindowUtils();
		}
		return pu;
	}

	/**
	 * 添加文本显示全部的监听
	 * 
	 * @param context
	 *            上下文对象
	 * @param tv
	 *            要弹出显示的TextView
	 */
	public void setOnShowAllTextListener(final Context context,
			final TextView tv) {
		if (tv == null) {
			LogGloble.e(TAG, "setOnShowAllTextListener textview is null");
			return;
		}
		tv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					int[] location = new int[2];
					v.getLocationOnScreen(location);
					int y = location[1];
					int x = location[0];

					int w = View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED);
					int h = View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED);
					tv.measure(w, h);
					tv.setSingleLine(true);
					int width = tv.getMeasuredWidth();
					if (width > tv.getWidth()) {
						// 要减去状态栏的高度
						showPopupWindowOnTop(context, x, y, tv.getText()
								.toString().trim(), BaseDroidApp.getInstanse()
								.getCurrentAct().getWindow().getDecorView(), tv);
						isToastPopupWindowShow = true;
						return true;
					} else {
						return false;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * 弹出不能显示完全的文本弹出框
	 * 返回true：需要弹出文本框
	 * 返回false：不需要弹出文本框
	 * @param context
	 * @param v
	 * @param isShow true :直接显示，false:根据显示状况判断是否显示
	 * @return
	 */
	
	public boolean showAllTextPopUpWindow(Context context,View v,String showText,boolean isShow){
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		int y = location[1];
		int x = location[0];

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		if(v instanceof TextView){
			((TextView)v).setSingleLine(true);
		}
		int width = v.getMeasuredWidth();
		if (isShow == false && width <= v.getWidth()) {
			return false;
		} 
		final TextView tv = (TextView) LayoutInflater.from(context).inflate(
				R.layout.textview_for_showtop, null);
		tv.setText(showText.toString().trim().replaceAll("　", "")
				.replaceAll("\t", ""));	
		tv.setGravity(Gravity.LEFT);
		popupWindow = new PopupWindow(tv, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAsDropDown(v);
		return true;
	}
	
	/***
	 * 弹出框在控件上方
	 * @param v
	 * @param showText ：
	 * @param isShow
	 * @return
	 */
	public boolean showAllTextPopUpWindowOnTop(Context context,View v,String showText,boolean isShow){
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		int y = location[1];
		int x = location[0];

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		if(v instanceof TextView){
			((TextView)v).setSingleLine(true);
		}
		int width = v.getMeasuredWidth();
		if (isShow == false && width <= v.getWidth()) {
			return false;
		} 
		
		TextView tv1 = new TextView(context);
		int w1 = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h1 = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        tv1.setWidth(1000);
        tv1.setText(showText.toString().trim().replaceAll("　", "")
				.replaceAll("\t", ""));
        tv1.measure(w1, h1);
        int height = tv1.getMeasuredHeight();
        
		final TextView tv = (TextView) LayoutInflater.from(context).inflate(
				R.layout.textview_for_showtop, null);
		tv.setText(showText.toString().trim().replaceAll("　", "")
				.replaceAll("\t", ""));	
		tv.setGravity(Gravity.LEFT);
		tv.setBackgroundResource(R.drawable.click_up);
		popupWindow = new PopupWindow(tv, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
//			popupWindow.showAsDropDown(v);
//		popupWindow.showAsDropDown(v, 0, 0 - height -v.getHeight() - 30, Gravity.CENTER);
		popupWindow.showAtLocation(BaseDroidApp.getInstanse()
				.getCurrentAct().getWindow().getDecorView(),
				Gravity.NO_GRAVITY, x, y
						-  height -v.getHeight());
		
		return true;
	}
	
	
	public void setOnShowAllTextListenert(final Context context,
			final TextView tv) {
		if (tv == null) {
			LogGloble.e(TAG, "setOnShowAllTextListener textview is null");
			return;
		}
		tv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					int[] location = new int[2];
					v.getLocationOnScreen(location);
					int y = location[1];
					int x = location[0];

					int w = View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED);
					int h = View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED);
					tv.measure(w, h);
					tv.setSingleLine(true);
					int width = tv.getMeasuredWidth();
					if (width > tv.getWidth()) {
						// 要减去状态栏的高度
						showPopupWindowOnTopt(context, x, y, tv.getText()
								.toString().trim(), BaseDroidApp.getInstanse()
								.getCurrentAct().getWindow().getDecorView(), tv);
						isToastPopupWindowShow = true;
						return true;
					} else {
						return false;
					}
				}
				return false;
			}
		});
	}

	/**
	 * 添加文本显示全部的监听
	 * 
	 * @param context
	 *            上下文对象
	 * @param view
	 *            要遍历添加子控件TextView 的父容器
	 */
	public void setOnShowAllTextListener(final Context context,
			final ViewGroup view) {
		int childCount = view.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = view.getChildAt(i);
			if (child instanceof TextView) {
				final TextView tv = (TextView) child;
				setOnShowAllTextListener(context, tv);
			}
		}
	}
	
	/**
	 * 添加文本显示全部的监听，该方法遍历父容器内所有控件，给所有TextView设置弹出框监听<br>
	 * 如果有其他TextView子类不需要设置弹出框的，可以修改
	 * 
	 * 如果有不需要设置弹出框的TextView控件，可以为该控件setTag为“noPopup”
	 * 
	 * @param context
	 *            上下文对象
	 * @param view
	 *            要遍历添加子控件的父容器
	 */
	public void setOnShowAllTextListenerByViewGroup(Context context, ViewGroup view) {
		int childCount = view.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = view.getChildAt(i);
			if (child instanceof Button || child instanceof RadioButton || child instanceof EditText) {
				continue;
			}
			if ("noPopup".equals(child.getTag())) {
				continue;
			}
			if (child instanceof TextView) {
				TextView tv = (TextView) child;
				setOnShowAllTextListener(context, tv);
			} else if (child instanceof ViewGroup) {
				setOnShowAllTextListenerByViewGroup(context, (ViewGroup) child);
			}
		}
	}

	/**
	 * 显示弹出的文本框
	 * 
	 * @param context
	 * @param x
	 * @param y
	 * @param text
	 * @param parentView
	 */
	private void showPopupWindowOnTop(Context context, final int x,
			final int y, String text, View parentView, TextView textview) {

		final TextView tv = (TextView) LayoutInflater.from(context).inflate(
				R.layout.textview_for_showtop, null);
		tv.setText(text.toString().trim().replaceAll("　", "")
				.replaceAll("\t", ""));
		// int w = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// int h = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// tv.measure(w, h);
		//
		// int height = tv.getMeasuredHeight();
		// int width = tv.getMeasuredWidth();

		popupWindow = new PopupWindow(tv, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		// if (width > LayoutValue.SCREEN_WIDTH) {
		// if (LayoutValue.SCREEN_WIDTH <= 480
		// && (width > (LayoutValue.SCREEN_WIDTH * 1.4))) {
		// popupWindow.setWidth(LayoutValue.SCREEN_WIDTH - 20);
		// popupWindow.setHeight((int) (height * 2));
		// } else {
		// popupWindow.setWidth(LayoutValue.SCREEN_WIDTH - 50);
		// popupWindow.setHeight((int) (height * 1.5));
		// }
		//
		// } else {
		// popupWindow.setWidth(width);
		// popupWindow.setHeight(height);
		// }
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAsDropDown(textview);
		ViewParent parent = textview.getParent();
		if (parent != null) {
			Activity activity = ActivityTaskManager.getInstance().getCurrentActivity();
			if(activity!=null && !activity.getClass().getSimpleName().equals("FincTradeScheduledBuyActivity")){
				parent.requestDisallowInterceptTouchEvent(true);
			}
		}
		// // 上方显示
		// if (BaseDroidApp.getInstanse().isDialogAct()) {// 是Dialog形式的Activity
		// popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, x, y
		// - popupWindow.getHeight() - LayoutValue.TITLEHEIGHT);
		// } else {
		// popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, x, y
		// - popupWindow.getHeight());
		// }

		// popupWindow.showAsDropDown(textview);
		// Toast toast = new Toast(context);
		// toast.setView(tv);
		// toast.setGravity(Gravity.CENTER, x, y);

	}
	private void showPopupWindowOnTopt(Context context, final int x,
			final int y, String text, View parentView, TextView textview) {

		final TextView tv = (TextView) LayoutInflater.from(context).inflate(
				R.layout.plps_textview_for_showtop, null);
		tv.setText(text.toString().trim().replaceAll("　", "")
				.replaceAll("\t", ""));
		// int w = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// int h = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// tv.measure(w, h);
		//
		// int height = tv.getMeasuredHeight();
		// int width = tv.getMeasuredWidth();

		popupWindow = new PopupWindow(tv, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		// if (width > LayoutValue.SCREEN_WIDTH) {
		// if (LayoutValue.SCREEN_WIDTH <= 480
		// && (width > (LayoutValue.SCREEN_WIDTH * 1.4))) {
		// popupWindow.setWidth(LayoutValue.SCREEN_WIDTH - 20);
		// popupWindow.setHeight((int) (height * 2));
		// } else {
		// popupWindow.setWidth(LayoutValue.SCREEN_WIDTH - 50);
		// popupWindow.setHeight((int) (height * 1.5));
		// }
		//
		// } else {
		// popupWindow.setWidth(width);
		// popupWindow.setHeight(height);
		// }
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAsDropDown(textview);
		ViewParent parent = textview.getParent();
		if (parent != null) {
			Activity activity = ActivityTaskManager.getInstance().getCurrentActivity();
			if(activity!=null && !activity.getClass().getSimpleName().equals("FincTradeScheduledBuyActivity")){
				parent.requestDisallowInterceptTouchEvent(true);
			}
		}
		// // 上方显示
		// if (BaseDroidApp.getInstanse().isDialogAct()) {// 是Dialog形式的Activity
		// popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, x, y
		// - popupWindow.getHeight() - LayoutValue.TITLEHEIGHT);
		// } else {
		// popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, x, y
		// - popupWindow.getHeight());
		// }

		// popupWindow.showAsDropDown(textview);
		// Toast toast = new Toast(context);
		// toast.setView(tv);
		// toast.setGravity(Gravity.CENTER, x, y);

	}



	public void showTextViewPopupWindow(PopupWindow pop,View v){
		if(pop != null) {
			popupWindow.setOutsideTouchable(false);
			popupWindow.showAsDropDown(v);
		}
	}
	
	public void dismissTextViewPopupWindow(PopupWindow pop){
		if(pop != null) {
			pop.dismiss();
		}
	}
	
	/**
	 * 查询PopupWindow是否显示
	 * 
	 * @return false：未显示 true：显示中
	 */
	public boolean isQueryPopupShowing() {
		if (mQueryPopupWindow != null) {
			return mQueryPopupWindow.isShowing();
		}
		return false;
	}

	/**
	 * 添加弹出下拉选择菜单的监听
	 * 
	 * @param context
	 *            上下文对象
	 * @param tv
	 *            要增加监听的对象
	 * @param selectors
	 *            显示文字
	 * @param drawbles
	 *            右侧图标
	 * @param listener
	 *            条目监听事件
	 */
	public void setOnPullSelecterListener(final Activity act, final View v,
			final String[] selectors, final int[] drawbles,
			final OnClickListener listener) {
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(act, v, selectors, drawbles, listener,
						v.getWidth());

			}
		});
	}

	View parentView;

	/**
	 * 添加弹出下拉选择菜单的监听
	 * 
	 * @param context
	 *            上下文对象
	 * @param tv
	 *            要增加监听的对象
	 * @param selectors
	 *            显示文字
	 * @param drawbles
	 *            右侧图标
	 * @param listener
	 *            条目监听事件
	 */
	public void setOnPullSelecterListener(final Activity act, final View v,
			final String[] selectors, final int[] drawbles,
			final OnClickListener listener, View parentView) {
		this.parentView = parentView;
		getViewScrollPointDelayed(v);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// showPopupWindow(act, v, selectors, drawbles,listener,
				// v.getWidth());
				showPopupWindowAsDrop(act, v, selectors, listener,
						v.getWidth(), 1);

			}
		});
	}

	private void getViewScrollPointDelayed(final View v) {
		v.postDelayed(new Runnable() {

			@Override
			public void run() {
				int[] location = new int[2];
				v.getLocationOnScreen(location);

				popSHowPointY = location[1];
				popShowPointX = location[0];

			}
		}, 100);
	}
	
	

	/**
	 * 显示下拉的多选的PopupWindow
	 * 
	 * @param x
	 * @param y
	 * @param selectors
	 *            显示文字
	 * @param drawbles
	 *            右侧图标
	 * @param itemClicklistener
	 *            id分别为 tv_text1 tv_text2 tv_text3
	 */
	private void showPopupWindow(Activity act, View v, String[] selectors,
			int[] drawbles, OnClickListener clicklistener, int widthss) {
		mClick = clicklistener;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(act).inflate(
				R.layout.dialog_for_pullpup, null);
		Button btn1 = ((Button) layout.findViewById(R.id.ll1));
		btn1.setText(selectors[0]);
		Button btn2 = ((Button) layout.findViewById(R.id.ll2));
		btn2.setText(selectors[1]);
		Button btn3 = ((Button) layout.findViewById(R.id.ll3));
		btn3.setText(selectors[2]);
		if (selectors.length == 4) {
			((LinearLayout) layout.findViewById(R.id.tv_text4))
					.setVisibility(View.VISIBLE);
			((LinearLayout) layout.findViewById(R.id.tv_text4))
					.setOnClickListener(this);
			((TextView) layout.findViewById(R.id.lastdividerblack))
					.setVisibility(View.VISIBLE);

			((TextView) layout.findViewById(R.id.lastdividergray))
					.setVisibility(View.VISIBLE);

			Button btn4 = ((Button) layout.findViewById(R.id.ll4));
			if (drawbles != null) {
				((ImageView) layout.findViewById(R.id.iv4))
						.setBackgroundResource(drawbles[3]);
			} else {
				((ImageView) layout.findViewById(R.id.iv4))
						.setVisibility(View.GONE);
			}
			btn4.setText(selectors[3]);
		}
		if (drawbles != null) {

			((ImageView) layout.findViewById(R.id.iv1))
					.setBackgroundResource(drawbles[0]);
			((ImageView) layout.findViewById(R.id.iv2))
					.setBackgroundResource(drawbles[1]);
			((ImageView) layout.findViewById(R.id.iv3))
					.setBackgroundResource(drawbles[2]);
		} else {
			((ImageView) layout.findViewById(R.id.iv1))
					.setVisibility(View.GONE);
			((ImageView) layout.findViewById(R.id.iv2))
					.setVisibility(View.GONE);
			((ImageView) layout.findViewById(R.id.iv3))
					.setVisibility(View.GONE);
		}
		((LinearLayout) layout.findViewById(R.id.tv_text1))
				.setOnClickListener(this);
		((LinearLayout) layout.findViewById(R.id.tv_text2))
				.setOnClickListener(this);
		((LinearLayout) layout.findViewById(R.id.tv_text3))
				.setOnClickListener(this);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		layout.measure(w, h);
		int height = layout.getMeasuredHeight();
		popupWindow = new PopupWindow(act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(widthss);
		popupWindow.setHeight(height);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// popupWindow.showAtLocation(BaseDroidApp.getInstanse().getCurrentAct()
		// .getWindow().getDecorView(), Gravity.LEFT | Gravity.TOP, x, y);
		// popupWindow.setAnimationStyle(R.style.popuwindow_in_out_Animation);
		popupWindow.showAsDropDown(v);
		// if(popupWindow.isShowing()){
		// popupWindow.dismiss();
		// }

	}

	boolean hasMeasured = false;

	/**
	 * 关闭popupWindow，在自己增加的监听里面调用此方法
	 */
	public void dissMissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		if (mClick != null)
			mClick.onClick(v);

	}

	/**
	 * 获取查询条件的下拉展示的Popupwindow
	 * 
	 * @param ContentView
	 *            查询条件的界面
	 * @param act
	 *            Acttivity实例
	 * @return
	 */
	public PopupWindow getQueryPopupWindow(View ContentView, BaseActivity act) {
		mAct = act;
		mQueryPopupWindow = new PopupWindow(ContentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mQueryPopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		mQueryPopupWindow.setOutsideTouchable(true);
		mQueryPopupWindow.setFocusable(false);
		mQueryPopupWindow
				.setAnimationStyle(R.style.popuwindow_in_out_Animation);
		return mQueryPopupWindow;

	}

	/**
	 * 展示下拉选择条件
	 */
	public void showQueryPopupWindow() {
		if (mQueryPopupWindow != null && mAct != null) {
			if (mQueryPopupWindow.isShowing()) {
				mQueryPopupWindow.dismiss();

			}
			mQueryPopupWindow
					.setAnimationStyle(R.style.popuwindow_in_out_Animation);
			mQueryPopupWindow.showAsDropDown((Button) mAct
					.findViewById(R.id.popuBtn));

		}
		if (mAct != null) {
			mAct.showLeftButton();
		}

	}

	/**
	 * 首次展示下拉选择条件(区别 展示时的动画 效果不同)
	 */
	public void showQueryPopupWindowFirst() {
		if (mQueryPopupWindow != null && mAct != null) {
			if (mQueryPopupWindow.isShowing()) {
				mQueryPopupWindow.dismiss();
			}
			mQueryPopupWindow
					.setAnimationStyle(R.style.popuwindow_in_out_Animation_First);
			mQueryPopupWindow.showAsDropDown((Button) mAct
					.findViewById(R.id.popuBtn));

		}
		mAct.showLeftButton();

	}

	/**
	 * 关闭查询条件popupwindow
	 */
	public void dissMissQueryPopupWindow() {
		if (mQueryPopupWindow != null && mQueryPopupWindow.isShowing()) {
			mQueryPopupWindow.dismiss();
		}
	}

	/**
	 * 关闭引用的资源 在BaseActivity onDestrory里面调用释放
	 */
	public void clearAllPopupWindow() {
		if (mQueryPopupWindow != null && mQueryPopupWindow.isShowing()) {
			mQueryPopupWindow.dismiss();
			LogGloble.d("info", "---mQueryPopupWindow.dismiss()---");
		}
		// popupWindow = null;
		// mQueryPopupWindow = null;
		// mAct = null;
	}

	/**
	 * 弹出更多选择的(下面显示)
	 * 
	 * @param context
	 *            上下文对象
	 * @param tv
	 *            要增加监听的对象
	 * @param selectors
	 *            显示文字
	 * @param drawbles
	 *            右侧图标
	 * @param listener
	 *            条目监听事件 监听事件 自己添加监听以int（View.getTag()） 作为标识
	 */
	public void setshowMoreChooseDownListener(final Activity act, final View v,
			final String[] selectors, final OnClickListener listener) {
		getViewScrollPointDelayed(v);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindowAsDrop(act, v, selectors, listener,
						v.getWidth(), 0);

			}
		});
	}

	public void setshowMoreChooseteListener(final Activity act, final View v,
			final String[] selectors, final OnClickListener listener) {
		getViewScrollPointDelayed(v);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showtePopupWindowAsDrop(act, v, selectors, listener,
						v.getWidth(), 0);

			}
		});
	}	
	
	
	/**
	 * 弹出更多选择的(上面显示)
	 * 
	 * @param context
	 *            上下文对象
	 * @param tv
	 *            要增加监听的对象
	 * @param selectors
	 *            显示文字
	 * @param drawbles
	 *            右侧图标
	 * @param listener
	 *            条目监听事件 监听事件 自己添加监听以int（View.getTag()） 作为标识
	 */
	public void setshowMoreChooseUpListener(final Activity act, final View v,
			final String[] selectors, final OnClickListener listener) {
		getViewScrollPointDelayed(v);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int[] location = new int[2];
				v.getLocationOnScreen(location);

				popSHowPointY = location[1];
				popShowPointX = location[0];
				showPopupWindowAsDrop(act, v, selectors, listener,
						v.getWidth(), 1);

			}
		});
	}

	/**
	 * 显示下拉的多选的PopupWindow
	 * 
	 * @param x
	 * @param y
	 * @param selectors
	 *            显示文字
	 * @param drawbles
	 *            右侧图标
	 * @param itemClicklistener
	 *            id分别为 tv_text1 tv_text2 tv_text3
	 * @param upOrDown
	 *            1 代表上面显示
	 */
	private void showPopupWindowAsDrop(Activity act, View v,
			String[] selectors, OnClickListener clicklistener, int widthss,
			int upOrDown) {
		BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
		if(activity != null)
			activity.closeInput();
		mClick = clicklistener;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(act).inflate(
				R.layout.layout_for_morechoose, null);
		if (upOrDown == 1) {// 代表上面显示
			layout.setBackgroundResource(R.drawable.clickup);
		}
		ListView lv = (ListView) layout.findViewById(R.id.listview);
		ChooseAdapter adapter = new ChooseAdapter(act, selectors);
		lv.setAdapter(adapter);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		layout.measure(w, h);
		popupWindow = new PopupWindow(act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(widthss);
		popupWindow.setHeight(itemHight
				* selectors.length
				+ selectors.length
				+ Integer.valueOf(act.getResources().getString(
						R.string.more_up_add_height)));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		if (upOrDown == 1) {// 代表上面显示{
			// 上方显示
			if (BaseDroidApp.getInstanse().isDialogAct()) {// 是Dialog形式的Activity
				popupWindow.showAtLocation(BaseDroidApp.getInstanse()
						.getCurrentAct().getWindow().getDecorView(),
						Gravity.NO_GRAVITY, popShowPointX, popSHowPointY
								- popupWindow.getHeight()
								- LayoutValue.TITLEHEIGHT);
			} else {
				popupWindow.showAtLocation(BaseDroidApp.getInstanse()
						.getCurrentAct().getWindow().getDecorView(),
						Gravity.NO_GRAVITY, popShowPointX, popSHowPointY
								- popupWindow.getHeight());
			}

		} else {
			popupWindow.showAsDropDown(v);
		}

	}
	private void showtePopupWindowAsDrop(Activity act, View v,
			String[] selectors, OnClickListener clicklistener, int widthss,
			int upOrDown) {
		BaseActivity activity = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
		if(activity != null)
			activity.closeInput();
		mClick = clicklistener;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(act).inflate(
				R.layout.layout_for_morechoose, null);
		
			layout.setBackgroundResource(R.drawable.clickup);
			ListView lv = (ListView) layout.findViewById(R.id.listview);
		ChooseAdapter adapter = new ChooseAdapter(act, selectors);
		lv.setAdapter(adapter);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		layout.measure(w, h);
		popupWindow = new PopupWindow(act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(widthss);
		popupWindow.setHeight(itemHight
				* selectors.length
				+ selectors.length
				+ Integer.valueOf(act.getResources().getString(
						R.string.more_up_add_height)));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
	
			popupWindow.showAsDropDown(v);
	

	}
	class ChooseAdapter extends BaseAdapter {

		private String[] selects;
		private Context context;

		public ChooseAdapter(Context context, String[] selects) {
			this.context = context;
			this.selects = selects;
		}

		@Override
		public int getCount() {
			return selects.length;
		}

		@Override
		public Object getItem(int position) {
			return selects[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final View view = LayoutInflater.from(context).inflate(
					R.layout.morechoose_item, null);

			Button btn = (Button) view.findViewById(R.id.btn);
			view.setTag(position);
			btn.setText(selects[position]);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setTag(position);
					PopupWindowUtils.this.onClick(v);
				}
			});
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			view.measure(w, h);
			itemHight = view.getMeasuredHeight();
			return view;
		}

	}

}
