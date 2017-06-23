package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.dealhistory.QueryHistoryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.OcrmProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager.InvestInvalidAgreeQueryActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;


/**
 * 借记卡选择关联账户
 * 
 * @author wangmengmeng
 * 
 */
public class AccDebitCardChooseActivity extends com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccDebitCardChooseActivity {
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftSideList(this, LocalData.bocinvtManagerLeftList);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		btn_confirm.setOnClickListener(confirmClickListener);
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
	
	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 请求借记卡关联结果
			if (select == null || select.size() == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						AccDebitCardChooseActivity.this
								.getString(R.string.acc_choose_debit_rel));
				return;
			}
			/** 用户选择的未关联借记卡信息 */
			List<Map<String, String>> choosefalseDebitList = new ArrayList<Map<String, String>>();

			for (int i = 0; i < select.size(); i++) {
				String type = falseDebitList.get(select.get(i)).get(
						Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES);
				if (type.equals(accountTypeList.get(13))) {
					// 关联电子现金账户
					falseDebitList.get(select.get(i)).put(
							Acc.RELEVANCEACCRES_LINKECASHORNOT_REQ,
							isHaveECashAccountList.get(1));
				} else if (type.equals(MEDICALACC)) {
					// 关联医保账户
					falseDebitList.get(select.get(i)).put(
							Acc.RELEVANCEACCRES_LINKMEDICALORNOT_REQ,
							isHaveECashAccountList.get(1));
				} else {
					// 关联借记卡
					falseDebitList.get(select.get(i)).put(
							Acc.RELEVANCEACCRES_LINKDEBITORNOT_REQ,
							isHaveECashAccountList.get(1));
				}
				choosefalseDebitList.add(falseDebitList.get(select.get(i)));
			}

			// 存储选择的借记卡账户信息
			AccDataCenter.getInstance().setChoosefalseDebitList(
					choosefalseDebitList);
			Intent intent = new Intent(AccDebitCardChooseActivity.this,
					AccDebitCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 回主页面
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
