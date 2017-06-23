package com.chinamworld.bocmbci.biz;

import java.util.Map;

import com.chinamworld.bocmbci.base.activity.BaseActivity;

/** Activity页面跳转 */
public class BusinessModelControl {
	 /*是否屏蔽自助关联*/

 
	

	/**
	 * 带返回参数的页面跳转
	 * @param context ：Activity类型的参数
	 * @param classType ： 需要跳转的Activity组件名
	 * @param requestCode ： 发起跳转的请求码
	 */
	public static boolean gotoAccRelevanceAccount(BaseActivity activity,int requestCode,Map<String,Object> mapData) {
	
//			BaseDroidApp.getInstanse().showMessageDialog(activity.getString(R.string.acc_myaccount_info), new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					BaseDroidApp.getInstanse().dismissErrorDialog();
		
//				}
//			});
		return false;
//		Class<?> classType=activity.getActivityTaskType()==ActivityTaskType.OneTask?AccInputRelevanceAccountActivity.class:com.chinamworld.bocmbci.biz.bocinvt.acctmanager.AccInputRelevanceAccountActivity.class;
//		
//		if(requestCode==-1){
//			ActivityIntentTools.intentToActivityWithData(activity, classType, mapData);
//		}else{
//			ActivityIntentTools.intentToActivityForResult(activity, classType, requestCode,mapData);
//		}
//		return true;
		
		
		}
	
	
	


	
}
