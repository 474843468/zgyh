package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 无可登记账户信息页
 * 
 * @author wangmengmeng
 * 
 */
public class InvtBindingRelevanceActivity extends BociBaseActivity {
	/** 无登记账户页 */
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.boci_binding_title));
		// 右上角按钮赋值
		setText(this.getResources().getString(R.string.close));
		// 添加布局
		view = addView(R.layout.bocinvt_binding_relevance);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		goneLeftView();
		// 界面初始化
		init();
	}

	private void init() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		Button btn_relevance_new_acc = (Button) view
				.findViewById(R.id.btn_description);
		// 关联新账户
		btn_relevance_new_acc.setOnClickListener(relevanceNewAccClick);

	}

	/** 关联新账户点击事件 */
	OnClickListener relevanceNewAccClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			Intent intent = new Intent(InvtBindingRelevanceActivity.this,
//					AccInputRelevanceAccountActivity.class);
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

			BusinessModelControl.gotoAccRelevanceAccount(InvtBindingRelevanceActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			
		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
}
