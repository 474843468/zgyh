package com.boc.bocsoft.mobile.bocmobile.base.utils;



import android.util.Log;


/**
 * 日志工具类
 * @author xianwei
 *
 */
public class LogUtils {
	
	private static boolean debug = true;
	
	public static void i(String msg){
		if(debug){
			if(msg == null)return;
			Log.i("logutil", msg);
		}
	}
	public static void d(String msg){
		if(debug){
			if(msg == null)return;
			Log.d("logutil", msg);
		}
	}
	public static void e(String msg){
		if(debug){
			if(msg == null)return;
			Log.e("logutil", msg);
		}
	}
	
	public static void i(String tag, String msg){
		if(debug){
			if(msg == null)return;
			Log.i(tag, msg);
		}
		
	}
	public static void d(String tag, String msg){
		if(debug){
			if(msg == null)return;
			Log.d(tag, msg);
		}
	}
	
	public static void e(String tag, String msg){
		if(debug){
			if(msg == null)return;
			Log.e(tag, msg);
		}
	}
}
