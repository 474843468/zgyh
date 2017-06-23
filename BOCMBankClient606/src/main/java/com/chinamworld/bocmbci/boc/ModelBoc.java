package com.chinamworld.bocmbci.boc;

import android.app.Activity;
import android.content.Intent;

import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.BocCommonTools;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.chinamworld.bocmbci.biz.dept.ApplyForAnAccountListResult;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.interfacemodule.IActionOne;
import com.chinamworld.bocmbci.interfacemodule.IActionTwo;
import com.chinamworld.bocmbci.mode.IFunc;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.llbt.model.IActionCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跳转到开发二部编写的代码模块
 * */
public class ModelBoc {

	/**
	 * 进入银行首页
	 */
	public static void gotoMainActivity(Activity activity){
		Intent intent = new Intent();
		intent.setClassName(activity, "com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainActivity");
		activity.startActivity(intent);
	}

	public static void BocStartActivityForResult(Activity context, String className){
		Intent intent  = new Intent();
		intent.setClassName(context, className);		
		context.startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_CODE_REMOTE);
	}
	
	public static void BocStartActivity(Activity context, String className){
		Intent intent  = new Intent();
		intent.setClassName(context, className);		
		context.startActivity(intent);
	}
	
	public static void gotoBoc_Loan(Activity context){
		ActivityIntentTools.intentToActivityByClassName(context, "com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanActivity");
	}


	/** 跳转到转账页面 */
	public static void gotoTransferActivity(Activity activity,String accountID){
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("AccountFromLianLong",accountID);
		new BocCommonTools().toBocPage(activity, BocCommonTools.PageCode.PAGE_TRANSREMITBLANK,map);

	}

	/**
	 * 跳转到申请定活期账户页面
	 * @param activity 当前Activity
	 * @param map 上送null
	 * @param call 回调
     */
	public static void gotoBocPageActivity(Activity activity,HashMap<String,String> map,final IActionTwo call){
		new BocCommonTools().toBocPage(activity, BocCommonTools.PageCode.PAGE_ACCOUNT_REGULAR_APPLY, map, new BocCommonTools.IBocCallBack() {
            @Override
            public void callBack(Object obj, boolean isSuccess) {
                if(isSuccess == false) {
//                    call.callBack(null,isSuccess);
                    return;
                }
				Map<String,Object> result = (Map<String,Object>)obj;
                if(result == null ) {
//                    call.callBack(null,isSuccess);
                    return;
                }
                call.callBack(result,isSuccess);
            }
        });
	}

	/**
	 * 跳转到Boc账户详情页
	 */
	public static void gotoAccountDetailActivity(Activity activity,String accountID){
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("accountId",accountID);
		new BocCommonTools().toBocPage(activity, BocCommonTools.PageCode.PAGE_MODULE_ACCOUNT_DETAIL,map);
	}

	/**
	 * 解绑设备成功
	 */
	public static void onUnBindDeviceSuccess(){
      	new BocCommonTools().onUnBindDeviceSuccess();
	}

	/**
	 * 修改欢迎信息成功
	 * @param newMsg 新的欢迎信息
	 */
	public static void onModifyWelcomeInfoSuccess(String newMsg){
		new BocCommonTools().onModifyWelcomeInfoSuccess(newMsg);
	}


	/**
	 * 跳转到Boc账户详情页
	 */
	public static void gotoAccountModuleActivity(Activity activity){
		new BocCommonTools().toBocPage(activity, BocCommonTools.PageCode.PAGE_ACCOUNTMANGER,null);
	}


	public static void getFundProductList(final IActionTwo call){
		new BocCommonTools().getProductList(new BocCommonTools.IBocCallBack() {
			@Override
			public void callBack(Object obj, boolean isSuccess) {
				if(isSuccess == false) {
					call.callBack(null,isSuccess);
					return;
				}
				CRgetProductListResult result = (CRgetProductListResult)obj;
				if(result == null || result.getArrayList() == null ) {
					call.callBack(null,isSuccess);
					return;
				}
				List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
                for(CRgetProductListResult.ProductBean item : result.getArrayList()){
					if("0".equals(item.getType()) == false)
						continue;
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("productName",item.getName());
					map.put("productCode",item.getProductCode());
					list.add(map);
				}
				call.callBack(list,isSuccess);
			}
		});
	}

	public static void onAddCommonUsePaymentSuccess(){
		new BocCommonTools().onAddCommonUsePaymentSuccess();
	}


	/***
	 *  开通投资理财是否成功
	 * @param isOpenSuccess ： true ：成功
     */
	public static void updateOpenWealthStatus(boolean isOpenSuccess){
		new BocCommonTools().updateOpenWealthStatus(isOpenSuccess);
	}
}
