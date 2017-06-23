package com.chinamworld.bocmbci.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;

/**
 * 向右滑动卡片引导页
 * 
 * @author xby
 * 
 */
public class CardWelcomGuideUtil {

	/**
	 * @Title: showCardWelcomGuid
	 * @Description: 我的账户页面引导页(3次显示)
	 * @param act
	 */
	public static void showCardWelcomGuid(Activity act) {
		int showTime = SharedPreUtils.getInstance().getInt(ConstantGloble.CARTGUIDTIME, 0);
		if (showTime < 3) {
			showTime++;
			SharedPreUtils.getInstance().addOrModifyInt(ConstantGloble.CARTGUIDTIME, showTime);
		} else {
			return;
		}
		final PopupWindow popupWindow = new PopupWindow(act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		// if (act.findViewById(R.id.rltotal) != null){
		// popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
		// popupWindow.setHeight(act.findViewById(R.id.rltotal).getHeight());
		// LogGloble.d("info",
		// "act.findViewById(R.id.rltotal).getHeight()--  "+act.findViewById(R.id.rltotal).getHeight());
		// }else{
		popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
		popupWindow.setHeight(LayoutValue.SCREEN_HEIGHT - LayoutValue.SCREEN_WIDTH / 7);
		// }
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setAnimationStyle(R.style.popuwindow_in_out_Animation_forCard);
		View view = LayoutInflater.from(act).inflate(R.layout.welcompopu, null);
		TextView tv_add_notice = (TextView) view.findViewById(R.id.tv_add_notice_title);
		int height = tv_add_notice.getMeasuredHeight();
		Button btn = (Button) view.findViewById(R.id.btn);
		ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
		LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_WIDTH * 165 / 637);
		lp.setMargins(0, height + act.getResources().getDimensionPixelOffset(R.dimen.fill_margin_top) * 2
				+ act.getResources().getDimensionPixelOffset(R.dimen.common_row_margin), 0, 0);
		lp.addRule(Gravity.CENTER_HORIZONTAL);
		imageview.setLayoutParams(lp);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		ImageView imageviewTwo = (ImageView) view.findViewById(R.id.imageviewtwo);
		LayoutParams lpTwo = new LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_WIDTH * 336 / 640);
		lpTwo.setMargins(
				0,
				LayoutValue.SCREEN_WIDTH * 165 / 637
						+ act.getResources().getDimensionPixelOffset(R.dimen.fill_margin_top) * 2 + height
						+ act.getResources().getDimensionPixelOffset(R.dimen.common_row_margin)
						+ act.getResources().getDimensionPixelOffset(R.dimen.listview_deviderHeight)
						+ act.getResources().getDimensionPixelOffset(R.dimen.common_listview_item__half_grqy_Height),
				0, 0);
		imageviewTwo.setLayoutParams(lpTwo);

		popupWindow.setContentView(view);
		// popupWindow.showAtLocation(BaseDroidApp.getInstanse().getCurrentAct()
		// .getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		//
		popupWindow.showAsDropDown((Button) act.findViewById(R.id.popuBtn));
	}
	/**
	 * @Title: showMainWelcomGuid
	 * @Description: MainActivity页面引导页(3次显示)
	 * @param act
	 */
	
	public static void showMainWelcomGuid(Activity act) {
		int showTime = SharedPreUtils.getInstance().getInt(
				ConstantGloble.MAINGUIDTIME, 0);
		if (showTime < 3) {
			showTime++;
			SharedPreUtils.getInstance().addOrModifyInt(
					ConstantGloble.MAINGUIDTIME, showTime);
		} else {
			return;
		}
		final PopupWindow popupWindow = new PopupWindow(
				act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
		popupWindow.setHeight(LayoutValue.SCREEN_HEIGHT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow
				.setAnimationStyle(R.style.popuwindow_in_out_Animation_forCard);
		View view = LayoutInflater.from(act).inflate(
				R.layout.welcommain, null);
		Button btn = (Button) view.findViewById(R.id.btn);
		ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
		LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH,
				LayoutValue.SCREEN_WIDTH * 500 / 640);
		lp.setMargins(0, LayoutValue.SCREEN_WIDTH / 4, 0, 0);
		lp.addRule(Gravity.CENTER_HORIZONTAL);
		imageview.setLayoutParams(lp);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setContentView(view);
		
		popupWindow.showAtLocation(act.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
	}
	
	public static void showCardEvaluationGuid(Activity act) {
		int showTime = SharedPreUtils.getInstance().getInt(ConstantGloble.EVALUATIONTIME, 0);
		if (showTime < 3) {
			showTime++;
			SharedPreUtils.getInstance().addOrModifyInt(ConstantGloble.EVALUATIONTIME, showTime);
		} else {
			return;
		}
		final PopupWindow popupWindow = new PopupWindow(act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
		popupWindow.setHeight(LayoutValue.SCREEN_HEIGHT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setAnimationStyle(R.style.popuwindow_in_out_Animation_forCard);
		View view = LayoutInflater.from(act).inflate(R.layout.welcome_boc_popu, null);
		Button btn = (Button) view.findViewById(R.id.btn);
		ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
		LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_WIDTH * 500 / 640);
		lp.setMargins(0, LayoutValue.SCREEN_WIDTH / 4, 0, 0);
		lp.addRule(Gravity.CENTER_HORIZONTAL);
		imageview.setLayoutParams(lp);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setContentView(view);

		popupWindow.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}

	/**
	 * 贵金属帮助
	 * @param act
	 *
	 * @param srcRestF
	 */
	public static void showCardPriceGuid(Activity act, RectF srcRestF) {
		String key = ConstantGloble.PRMSPRICEGUIDTIME;
		int showTime = SharedPreUtils.getInstance().getInt(key, 0);
		if (showTime < 3) {
			showTime++;
			SharedPreUtils.getInstance().addOrModifyInt(key, showTime);
		} else {
			return;
		}
		final PopupWindow popupWindow = new PopupWindow(act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
		popupWindow.setHeight(LayoutValue.SCREEN_HEIGHT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setAnimationStyle(R.style.popuwindow_in_out_Animation_forCard);
		ImageView rootImg = new ImageView(act);
		rootImg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		Bitmap bitmap = getCardEvaluationGuidBitmap(act, LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT, srcRestF);
		rootImg.setImageBitmap(bitmap);
		rootImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setContentView(rootImg);

		popupWindow.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}

	/**
	 * 生成贵金属bitmap
	 * @param context
	 * @param width 屏幕宽度
	 * @param height 屏幕高度
	 * @param srcRestF 目标
	 * @return
	 */
	private static Bitmap getCardEvaluationGuidBitmap(Context context, int width, int height, RectF srcRestF) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		Bitmap handBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_guide_hand);
		Bitmap wordBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_guide_word);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		// 挖洞
		canvas.save();
		Path path = new Path();
		path.addOval(srcRestF, Direction.CW);
		canvas.clipPath(path);
		canvas.clipRect(0, 0, width, height, Region.Op.XOR);
		// 背景
		canvas.drawColor(context.getResources().getColor(R.color.transparent_dark));
		// 修洞
		Paint dPaint = new Paint();
		dPaint.setAntiAlias(true);
		dPaint.setColor(Color.WHITE);
		float d = 2;
		RectF dSrc = new RectF(srcRestF.left - d, srcRestF.top - d, srcRestF.right + d, srcRestF.bottom + d);
		dPaint.setStrokeWidth(5);
		canvas.drawOval(dSrc, dPaint);
		canvas.restore();
		// ----------------------------------------
		// 位置 原点 - 控件大小/2-手/2-手的偏移
		int xPoint = (int) (srcRestF.left + srcRestF.width() / 2 - handBitmap.getWidth() / 2 + 30);
		int yPoint = (int) (srcRestF.bottom - 15);
		Point point = new Point(xPoint, yPoint);

		// 添加手
		Rect handScr = new Rect(0, 0, handBitmap.getWidth(), handBitmap.getHeight());
		Rect handDst = new Rect(point.x, point.y, (int) (point.x + handBitmap.getWidth() / 2),
				(int) (point.y + handBitmap.getHeight() / 2));
		canvas.drawBitmap(handBitmap, handScr, handDst, paint);

		// 添加文字
		Point WordPoint = new Point(point.x - 50, point.y + handDst.height() + 25);
		Rect wordScr = new Rect(0, 0, wordBitmap.getWidth(), wordBitmap.getHeight());
		Rect wordDst = new Rect(WordPoint.x, WordPoint.y, (int) (WordPoint.x + wordBitmap.getWidth() / 2),
				(int) (WordPoint.y + wordBitmap.getHeight() / 2));
		canvas.drawBitmap(wordBitmap, wordScr, wordDst, paint);
		return bitmap;
	}
	
	
	/**
	 *  保险 添加引导页
	 * @param key
	 */
	public static void showSafetyCardPriceGuid(Activity act,String key) {
		int showTime = SharedPreUtils.getInstance().getInt(key, 0);
		if (showTime < 3) {
			final PopupWindow popupWindow = new PopupWindow(
					act);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
			popupWindow.setHeight(LayoutValue.SCREEN_HEIGHT);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setFocusable(true);
			popupWindow.setAnimationStyle(R.style.popuwindow_in_out_Animation_forCard);
			View view = LayoutInflater.from(act).inflate(
					R.layout.safety_customer_guide, null);
			View layout = view.findViewById(R.id.layout);
			LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH,
					LayoutValue.SCREEN_WIDTH * 266 / 640);
			lp.setMargins(0, LayoutValue.SCREEN_WIDTH / 3, 0, 0);
			lp.addRule(Gravity.CENTER_HORIZONTAL);
			layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popupWindow.dismiss();
				}
			});
			popupWindow.setContentView(view);
			popupWindow.showAtLocation(act.getWindow().getDecorView(),
					Gravity.CENTER, 0, 0);
			showTime++;
			SharedPreUtils.getInstance().addOrModifyInt(key, showTime);
		}
	}
	
	/**
	 * 贵金属积利金 定存管理 活期转定期 引导页
	 * @param  act
	 */
	public static void showGoldBonusPositionGuid(Activity act,int location) {
		int showTime = SharedPreUtils.getInstance().getInt(ConstantGloble.GOLDBONUSPOSIT, 0);
		if (showTime < 3) {
			showTime++;
			SharedPreUtils.getInstance().addOrModifyInt(ConstantGloble.GOLDBONUSPOSIT, showTime);
		} else {
			return;
		}
//		LinearLayout ll_list = (LinearLayout)v.findViewById(R.id.ll_listviewone);
		final PopupWindow popupWindow = new PopupWindow(act);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(LayoutValue.SCREEN_WIDTH);
		popupWindow.setHeight(LayoutValue.SCREEN_HEIGHT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setAnimationStyle(R.style.popuwindow_in_out_Animation_forCard);
		View view = LayoutInflater.from(act).inflate(R.layout.welcomegoldbonus_position, null);
		Button btn = (Button) view.findViewById(R.id.btn);
//		TextView tv = (TextView) view.findViewById(R.id.textview);
//		tv.setHeight(ll_list.getTop());
		//文本
		ImageView ima = (ImageView) view.findViewById(R.id.ima_click);
		LayoutParams lp_ima = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		lp_ima.setMargins(0, LayoutValue.SCREEN_HEIGHT/160 * 35 , 0, 0);
		lp_ima.setMargins(0, location , 0, 0);
		lp_ima.addRule(Gravity.CENTER_HORIZONTAL);
		ima.setLayoutParams(lp_ima);
		//背景
		ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
		LayoutParams lp = new LayoutParams(LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT);
		lp.setMargins(0, 0, 0, 0);
		lp.addRule(Gravity.CENTER_HORIZONTAL);
		imageview.setLayoutParams(lp);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setContentView(view);

		popupWindow.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}
	

}
