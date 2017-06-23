package com.boc.bocsoft.remoteopenacc.common.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ViewUtils {

	/**
	 * 隐藏软键盘
	 * 
	 * @param mContext
	 * @param view
	 */
	public static void hiddenKeyboard(Context mContext, View view) {
		if (view == null || view.getWindowToken() == null ||mContext == null)
			return;
		InputMethodManager manger = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(manger == null){
			return;
		}
		manger.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public static void toggleKeyboard(Context mContext) {
		if(mContext == null)return;
		
		InputMethodManager manger = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(manger == null)return;
		manger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
