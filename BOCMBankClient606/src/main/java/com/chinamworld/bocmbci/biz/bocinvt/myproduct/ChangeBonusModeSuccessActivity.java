package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * 修改分红方式成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class ChangeBonusModeSuccessActivity extends BociBaseActivity {
	/** 修改分红方式成功页面 */
	private View view;
	/** 成功信息 */
	private Map<String, Object> changeModeSuccessMap;
	/** 产品代码 */
	private TextView tv_prodCode_detail;
	/** 产品名称 */
	private TextView tv_prodName_detail;
	/** 当前分红方式 */
	private TextView tv_currentbonusMode;
	/** 确定 */
	private Button btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_changeMode_step1));
		// 添加布局
		view = addView(R.layout.bocinvt_changemode_success);
		// 界面初始化
		init();
	}

	private void init() {
		back.setVisibility(View.GONE);
		changeModeSuccessMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_CHANGEBONUSMODE);
		tv_prodCode_detail = (TextView) view
				.findViewById(R.id.tv_prodCode_detail);
		tv_prodName_detail = (TextView) view
				.findViewById(R.id.tv_prodName_detail);
		tv_currentbonusMode = (TextView) view
				.findViewById(R.id.tv_currentbonusMode);
		/** 产品代码 */
		tv_prodCode_detail.setText(String.valueOf(changeModeSuccessMap
				.get(BocInvt.BOCINVT_SETBONUS_PRODCODE_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodCode_detail);
		/** 产品名称 */
		tv_prodName_detail.setText(String.valueOf(changeModeSuccessMap
				.get(BocInvt.BOCINVT_SETBONUS_PRODNAME_RES)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_prodName_detail);
		String currentMode = this.getIntent().getStringExtra(
				BocInvt.BOCINVT_SETBONUS_MODE_REQ);
		/** 当前分红方式 */
		tv_currentbonusMode.setText(LocalData.bocicurrentmodeMap
				.get(currentMode));
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ChangeBonusModeSuccessActivity.this,
						MyProductListActivity.class);
				startActivity(intent);
				ActivityTaskManager.getInstance().removeAllSecondActivity();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
