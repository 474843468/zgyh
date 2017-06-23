package com.chinamworld.bocmbci.biz.tran.remit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 套餐修改成功信息页面
 * 
 * @author luqipeng
 * 
 */
public class RemitSetMealModifySuccessActivity extends TranBaseActivity {
	/** 成功信息页面 */
	private View view;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_one));
		toprightBtn();
		back.setVisibility(View.GONE);
		// 添加布局
		view = addView(R.layout.tran_remit_setmeal_modify_success);
		
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {

		

		// 赋值
	
		// 续约布局根据账号是否支持续约进行显示隐藏
		/*Map<String, Object> tag2 = (Map<String, Object>) remitSetMealTypeResDic.getTagFromValue((String) remitinputMap
				.get(Tran.TRAN_REMIT_APP_REMITSETMEALPRODUCTTYPE_REQ));
		String flag = (String) tag2.get(Tran.MealTypeQuery_remitSetMealautoFlag);
		extension_flag_layout.setVisibility("Y".equalsIgnoreCase(flag) ? View.VISIBLE : View.GONE);*/
		// 用户是否选择续约
	
	}

	OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			ActivityTaskManager.getInstance().removeAllActivity();
			CustomDialog.toastShow(RemitSetMealModifySuccessActivity.this, getString(R.string.trans_remit_modify_success));
			Intent intent = new Intent(RemitSetMealModifySuccessActivity.this, RemitThirdMenu.class);
			startActivity(intent);
		}
	};

	@Override
	public void onBackPressed() {
	}
}
