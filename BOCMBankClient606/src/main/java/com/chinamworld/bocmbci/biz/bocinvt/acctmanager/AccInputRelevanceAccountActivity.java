package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.dealhistory.QueryHistoryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.OcrmProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager.InvestInvalidAgreeQueryActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 填写关联账户信息页面
 * 
 * @author wangmengmeng
 * 
 */

public class AccInputRelevanceAccountActivity extends com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccInputRelevanceAccountActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftSideList(this, LocalData.bocinvtManagerLeftList);
		setLeftSelectedPosition("bocinvtManager_1");
		setRightBtnClick(rightBtnClick);
	}	
	
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent intent = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("bocinvtManager_1")){
			intent.setClass(this, BocinvtAcctListActivity.class);
		}
		else if(menuId.equals("bocinvtManager_2")){
			intent.setClass(this, MyInvetProductActivity.class);
		}
		else if(menuId.equals("bocinvtManager_3")){
			intent.setClass(this, QueryProductActivity.class);
		}
		else if(menuId.equals("bocinvtManager_4")){
			intent.setClass(this, InvestInvalidAgreeQueryActivity.class);
		}
		else if(menuId.equals("bocinvtManager_5")){
			intent.setClass(this, OcrmProductListActivity.class);
			intent.putExtra("flag", true);
		}
		else if(menuId.equals("bocinvtManager_6")){
			intent.setClass(this, QueryHistoryProductActivity.class);
		}
		context.startActivity(intent);
		return true;
//		
//		super.setSelectedMenu(clickIndex);
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
//		Intent intent = new Intent();
//		switch (clickIndex) {
//		case 0:
//			intent.setClass(this, MyInvetProductActivity.class);
//			break;
//		case 1:
//			intent.setClass(this, QueryProductActivity.class);
//			break;
//		case 2:
////			intent.setClass(this, QueryAgreeActivity.class);
//			intent.setClass(this, InvestInvalidAgreeQueryActivity.class);
//			break;
//		case 3:
//			intent.setClass(this, BocinvtAcctListActivity.class);
//			break;
//		case 4:
//			intent.setClass(this, QueryHistoryProductActivity.class);
//			break;
//		case 5:
//			intent.setClass(this, OcrmProductListActivity.class);
//			intent.putExtra("flag", true);
//			break;
//		}
//		startActivity(intent);
	}
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 回主页面
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};
	/**
	 * 请求账户自助关联预交易回调
	 * 
	 * @param resultObj
	 */
	@Override
	public void requestPsnRelevanceAccountPreCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		// 通讯返回result
		Map<String, Object> relevancePremap = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(relevancePremap)) {
			return;
		}
		// 储存返回信息
		AccDataCenter.getInstance().setRelevancePremap(relevancePremap);
		String accountType = (String) relevancePremap
				.get(Acc.RELEVANCEACCPRE_ACC_ACCOUNTTYPE_RES);

		if (accountType.equals(accountTypeList.get(3))) {
			// 借记卡
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccDebitCardChooseActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		} else if (accountType.equals(accountTypeList.get(13))) {
			// 电子现金账户
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccICCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		} else if (accountType.equals(accountTypeList.get(1))
				|| accountType.equals(accountTypeList.get(2))
				|| accountType.equals(accountTypeList.get(4))) {
			// 信用卡
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccCreditCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		}
	}
	
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
		
}
