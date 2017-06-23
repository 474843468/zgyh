package com.chinamworld.bocmbci.biz.setting;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.chinamworld.bocmbci.R;

public class SettingUtils {
	/**
	 * 处理返回数据
	 * 
	 * @param result
	 * @return
	 */
//	public static Object httpResponseDeal(Object result) {
//		BiiResponse biiResponse = (BiiResponse) result;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		return biiResponseBody.getResult();
//	}
	
	/**
	 * 初始化下拉框
	 * @param c
	 * @param sp
	 * @param list
	 */
	public static void initSpinnerView(Context c,Spinner sp,List<String> list) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<ArrayList<String>> mAdapter = new ArrayAdapter(c,
				R.layout.spinner_item, list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(mAdapter);
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
}
