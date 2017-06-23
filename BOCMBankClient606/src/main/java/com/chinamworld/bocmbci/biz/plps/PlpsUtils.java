package com.chinamworld.bocmbci.biz.plps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PlpsUtils {
	
	/**
	 * 处理返回数据
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
	 * 构造spinner数据
	 * @param list
	 * @param key
	 * @param title
	 * @return
	 */
//	public static List<String> initSpinnerData(List<Map<String, Object>> list,String key,String title) {
//		if (StringUtil.isNullOrEmpty(list)) return null;
//		ArrayList<String> mList = new ArrayList<String>();
//		if (!StringUtil.isNull(title)) {
//			mList.add(title);
//		}
//		for (int i = 0; i < list.size(); i++) {
//			if (key.equals(Comm.ACCOUNTNUMBER)) {
//				mList.add(StringUtil.getForSixForString((String) list.get(i).get(key)));
//			}else{
//				mList.add((String) list.get(i).get(key));
//			}
//		}
//		return mList;
//	}
	/**
	 * 构造spinner数据
	 * @param list
	 * @param key
	 * @param title
	 * @return
	 */
	public static List<String> initSpinnerPlanData(List<Map<String, Object>> list,String key,String key1,String title) {
		if (StringUtil.isNullOrEmpty(list)) return null;
		ArrayList<String> mList = new ArrayList<String>();
		if (!StringUtil.isNull(title)) {
			mList.add(title);
		}
		for (int i = 0; i < list.size(); i++) {
			mList.add((String) list.get(i).get(key)+"/"+(String)list.get(i).get(key1));
		}
		return mList;
	}
	/**
	 * 构造spinner数据
	 * @param list
	 * @param key
	 * @param title
	 * @return
	 */
	public static List<String> initSpinnerCardData(List<Map<String, Object>> list,String key,String key1,String title) {
		if (StringUtil.isNullOrEmpty(list)) return null;
		ArrayList<String> mList = new ArrayList<String>();
		if (!StringUtil.isNull(title)) {
			mList.add(title);
		}
		for (int i = 0; i < list.size(); i++) {
			mList.add(StringUtil.getForSixForString((String) list.get(i).get(key))+"（"+(String)list.get(i).get(key1)+"）");
		}
		return mList;
	}
	/**
	 * 构造省spinner列表*/
	public static List<String> initSpinnerProvinceData(List<String> list,String title) {
		if (StringUtil.isNullOrEmpty(list)) return null;
		ArrayList<String> mList = new ArrayList<String>();
		if (!StringUtil.isNull(title)) {
			mList.add(title);
		}
		for (int i = 0; i < list.size(); i++) {
			mList.add((String) list.get(i));
		}
		return mList;
	}
	/**
	 * 两个字段*/
	public static List<String> initSpinnerDataThre(List<Map<String, Object>> list,String keyt,String key,String keys,String keyf,String title) {
		if (StringUtil.isNullOrEmpty(list)) return null;
		ArrayList<String> mList = new ArrayList<String>();
		if (!StringUtil.isNull(title)) {
			mList.add(title);
		}
		for (int i = 0; i < list.size(); i++) {
			if(StringUtil.isNullOrEmpty(keyt)){
				mList.add(LocalData.AccountType.get((String) list.get(i).get(keys))+" "+StringUtil.getForSixForString((String) list.get(i).get(key)));
			}else {
				if (key.equals(Comm.ACCOUNTNUMBER)) {
					mList.add(LocalData.AccountType.get((String) list.get(i).get(keys))+" "+StringUtil.getForSixForString((String) list.get(i).get(key))+" "+list.get(i).get(keyt)+" "+PlpsDataCenter.Province.get((String) list.get(i).get(keyf)));
				}else{
					mList.add((String) list.get(i).get(key));
				}
			}
			
		}
		return mList;
	}
	
	/**
	 * 初始化下拉框
	 * @param c
	 * @param sp
	 * @param list
	 */
	public static void initSpinnerView(Context c,Spinner sp,List<String> list) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<ArrayList<String>> mAdapter = new ArrayAdapter(c,R.layout.spinner_item, list);
//		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mAdapter.setDropDownViewResource(R.layout.plps_simple_spinner_dropdown_item);
		sp.setAdapter(mAdapter);
	}
	
	/**
	 * 显示文本浮动框
	 * @param context
	 * @param textViews
	 */
	public static void setOnShowAllTextListener(Context context, TextView... textViews){
		if(textViews == null || textViews.length <= 0) return;
		for(TextView tv : textViews){
			if(tv != null){
				PopupWindowUtils.getInstance().setOnShowAllTextListener(context,tv);
			}
		}
	}
}
