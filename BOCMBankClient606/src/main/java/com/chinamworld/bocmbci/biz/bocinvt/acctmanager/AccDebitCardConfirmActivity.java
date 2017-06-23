package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.bocinvt.dealhistory.QueryHistoryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.OcrmProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager.InvestInvalidAgreeQueryActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;


/**
 * 借记卡关联确认信息页
 * 
 * @author wangmengmeng
 * 
 */
public class AccDebitCardConfirmActivity extends com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccDebitCardConfirmActivity{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftSideList(this, LocalData.bocinvtManagerLeftList);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		
	}
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {super.selectedMenuItemHandler(context, menuItem);
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
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 回主页面
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};
	@Override
	public void requestPsnRelevanceAccountResultCallback(Object resultObj) {
		super.requestPsnRelevanceAccountResultCallback(resultObj);
		Intent intent = new Intent(AccDebitCardConfirmActivity.this,
				AccDebitCardSuccessActivity.class);
		startActivityForResult(intent,
				ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
