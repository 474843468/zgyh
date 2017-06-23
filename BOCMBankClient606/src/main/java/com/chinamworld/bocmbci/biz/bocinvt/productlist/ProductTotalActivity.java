package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 中国银行股份有限公司理财产品总协议书
 * 
 * @author wangmengmeng
 * 
 */
public class ProductTotalActivity extends BociBaseActivity {
	private static final String TAG = "ProductTotalActivity";
	/** 产品说明书页 */
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.totalDetail));
		// 添加布局
		view = addView(R.layout.bocinvt_totaldes_activity);
		// 右上角按钮赋值
		setText(this.getString(R.string.close));
		// 右上角按钮点击事件
		setRightBtnClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(105);
				finish();
				overridePendingTransition(R.anim.no_animation,
						R.anim.slide_down_out);
			}
		});
		goneLeftView();
		// 界面初始化
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(105);
			finish();
			overridePendingTransition(R.anim.no_animation,
					R.anim.slide_down_out);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void init() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		TextView tv_jiafang = (TextView) view.findViewById(R.id.tv_jiafang);
		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		String cusName = (String) loginMap.get(Login.CUSTOMER_NAME);
		tv_jiafang.setText(cusName);
		Button btn_description = (Button) view
				.findViewById(R.id.btn_description);
		btn_description.setOnClickListener(rightBtnClick);
		Button btn_noaccept = (Button) view.findViewById(R.id.btn_noaccept);
		btn_noaccept.setOnClickListener(noAcceptBtnClick);

	}

	/** 不接受按钮点击事件 */
	OnClickListener noAcceptBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭
			setResult(RESULT_CANCELED);
			finish();
			overridePendingTransition(R.anim.no_animation,
					R.anim.slide_down_out);
		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭
			setResult(RESULT_OK);
			finish();
			overridePendingTransition(R.anim.no_animation,
					R.anim.slide_down_out);
		}
	};


}
