package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 汇款套餐签约接受协议页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitProtocolActivity extends TranBaseActivity {
	/** 接受协议页 */
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_one));
		// 添加布局
		view = addView(R.layout.tran_remit_protocol);
		// 右上角按钮赋值
		toprightBtn();
		// 界面初始化
		init();
	}

	private void init() {
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
			finish();
		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 请求账户列表
			requestRemitAccBankAccountList();
		}
	};

	@Override
	public void requestRemitAccBankAccountListCallBack(Object resultObj) {
		super.requestRemitAccBankAccountListCallBack(resultObj);
		//进入选择账户页面
		Intent intent=new Intent(this,RemitChooseAccountActivity.class);
		startActivity(intent);
	}

	
}
