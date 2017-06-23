package com.chinamworld.bocmbci.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.widget.MyRelativeLayout;

public class ViewUtils {
	/**
	 * 查询的 listView footer
	 * @param context
	 * @return
	 */
	public static View getQuryListFooterView(Context context) {
		return LayoutInflater.from(context).inflate(R.layout.query_list_footer, null);
	}
	
	/**
	 * 查询的 listView footer
	 * @param context
	 * @return
	 */
	public static View getQuryListFooterView(Context context, int style) {
		return LayoutInflater.from(context).inflate(style, null);
	}
	
	
	/**
	 * 只有一个按钮 按钮的样式
	 * 
	 * @param btn
	 */
	public static void initBtnParams(Button btn, Context context) {
		android.widget.LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, context.getResources()
						.getDimensionPixelSize(R.dimen.btn_bottom_height));
		lp.gravity = Gravity.CENTER;
		lp.setMargins(
				context.getResources().getDimensionPixelSize(
						R.dimen.dp_three_zero), 0, context.getResources()
						.getDimensionPixelSize(R.dimen.dp_three_zero), 0);
		btn.setLayoutParams(lp);
		btn.setGravity(Gravity.CENTER);
	}

	/**
	 * 两个按钮 按钮的样式
	 * 
	 * @param btn
	 */
	public static void initBtnParamsTwo(Button btn, Context context) {
		android.widget.LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, context.getResources()
						.getDimensionPixelSize(R.dimen.btn_bottom_height));
		lp.gravity = Gravity.CENTER;
		lp.weight = 1;
		lp.setMargins(
				context.getResources().getDimensionPixelSize(
						R.dimen.btncommon_marglr), 0, context.getResources()
						.getDimensionPixelSize(R.dimen.btncommon_marglr), 0);
		btn.setLayoutParams(lp);
		btn.setGravity(Gravity.CENTER);
	}
	public static void initBtnParamsTwo606(Button btn, Context context) {
		android.widget.LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, context.getResources()
				.getDimensionPixelSize(R.dimen.boc_space_between_88px));
		lp.gravity = Gravity.CENTER;
		lp.weight = 1;
		lp.setMargins(
				context.getResources().getDimensionPixelSize(
						R.dimen.btncommon_marglr), 0, context.getResources()
						.getDimensionPixelSize(R.dimen.btncommon_marglr), 0);
		btn.setLayoutParams(lp);
		btn.setGravity(Gravity.CENTER);
	}
	public static void initBtnParamsTwoLeft(Button btn, Context context) {
		android.widget.LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, context.getResources()
				.getDimensionPixelSize(R.dimen.boc_space_between_88px));
		lp.gravity = Gravity.CENTER;
		lp.weight = 1;
		lp.setMargins(
				0, 0, context.getResources()
						.getDimensionPixelSize(R.dimen.fill_margin_right), 0);
		btn.setLayoutParams(lp);
		btn.setGravity(Gravity.CENTER);
	}
	public static void initBtnParamsTwoRight(Button btn, Context context) {
		android.widget.LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, context.getResources()
				.getDimensionPixelSize(R.dimen.boc_space_between_88px));
		lp.gravity = Gravity.CENTER;
		lp.weight = 1;
		lp.setMargins(
				context.getResources()
						.getDimensionPixelSize(R.dimen.fill_margin_right), 0, 0, 0);
		btn.setLayoutParams(lp);
		btn.setGravity(Gravity.CENTER);
	}

	/**
	 * 讲当前的View 移动到ScrollView 中可以显示的位置
	 * @param v
	 */
	public static void scrollShow(View v){
		View tmp = v;
		double top = 0,left = 0;
		if(tmp == null)
			return ;
		if(tmp instanceof ScrollView)
			return;
		do{
			top += tmp.getTop();
			left += tmp.getLeft();
			tmp = (View)tmp.getParent();
			if(tmp == null || tmp instanceof MyRelativeLayout)
				return;
		}while(!(tmp instanceof ScrollView));
		if(tmp == null)
			return;
		if(top + v.getHeight()  - ((ScrollView)tmp).getScrollY() < tmp.getHeight() && top - ((ScrollView)tmp).getScrollY()  > 0){
			return;
		}
		((ScrollView)tmp).smoothScrollTo((int)(left - tmp.getWidth() + v.getWidth() + 30), (int)(top - tmp.getHeight() + v.getHeight() + 30));
	}

	public static void  initWebView(WebView webView){
		if(webView == null)
			return;
		webView.getSettings().setSavePassword(false);
	}
	

	
	
}
