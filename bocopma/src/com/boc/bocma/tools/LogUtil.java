package com.boc.bocma.tools;

import android.util.Log;

public class LogUtil {

	private static boolean debug = true;
	
	public static void i(String msg){
		if(debug){
			Log.i("logutil", msg);
		}
	}
	public static void d(String msg){
		if(debug){
			Log.d("logutil", msg);
		}
	}
	public static void e(String msg){
		if(debug){
			Log.e("logutil", msg);
		}
	}
}
