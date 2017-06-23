package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;

/**
 * 产品说明书
 * 
 * @author wangmengmeng
 * 
 */
public class ProductDesActivity extends BociBaseActivity {
	private static final String TAG = "ProductDesActivity";
	/** 产品说明书页 */
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.description));
		// 添加布局
		view = addView(R.layout.bocinvt_des_activity);
		// 右上角按钮赋值
		setText(this.getString(R.string.close));
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
		Button btn_description = (Button) view
				.findViewById(R.id.btn_description);
		btn_description.setOnClickListener(rightBtnClick);

	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭
			finish();
			overridePendingTransition(R.anim.no_animation,
					R.anim.slide_down_out);
		}
	};


}
