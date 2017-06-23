package com.chinamworld.bocmbci.biz.login.binding;

import android.content.Context;

import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.IActionCallBack;
import com.chinamworld.bocmbci.utils.SharedPreUtils;

/** 登陆手机硬件绑定 类 */
public class DeviceBinding {

	private static IActionCallBack actionCallBack;
	public static void ActionCallBack() {
		if(actionCallBack != null)
			actionCallBack.callBack(null);
	}
	/** 进入手机硬件绑定流程  */
	public static void goToDeviceBingdingFlow(Context context,IActionCallBack callBack) {
		actionCallBack = callBack;
		// 删除本地信息
		SharedPreUtils.getInstance().deleteItem(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO);
		SharedPreUtils.getInstance().deleteItem(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC);
		// 进入绑定页面
		ActivityTaskManager.getInstance().removeAllActivity();
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		ActivityIntentTools.intentToActivity(context, LoginBindingActivity.class);
//		Intent intent = new Intent();
//		intent.setClass(this, LoginBindingActivity.class);
//		startActivity(intent);

	}
	
}
