package com.chinamworld.bocmbci.biz.bocinvt.queryagreement;

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
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;

/**
 * 定时定额投资协议维护成功
 * 
 * @author wangmengmeng
 * 
 */
public class DextendAgreeSuccessActivity extends BociBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.bocinvt_agree_dingshi));
		// 添加布局
		View view = addView(R.layout.boc_agree_ding_success);
		// 界面初始化
		init();

		back.setVisibility(View.GONE);
	}

	/**
	 * 初始化界面
	 */
	private void init() {
		Map<String, Object> autoMap = BociDataCenter.getInstance()
				.getAutoResultMap();
		TextView tv_title = (TextView) findViewById(R.id.tv_title_confirm);
		tv_title.setText(dingeditSuccessList.get(Integer.valueOf(this
				.getIntent().getStringExtra(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ))));
		TextView tv_contractSeq = (TextView) findViewById(R.id.tv_contractSeq);
		// TextView tv_status = (TextView) findViewById(R.id.tv_number);
		tv_contractSeq.setText((String) autoMap
				.get(BocInvt.BOC_AUTO_TRANSACTIONID_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_contractSeq);
		// tv_status.setText(bocSerlistList.get(Integer.valueOf((String) autoMap
		// .get(BocInvt.BOC_AUTO_STATUS_RES))));
		Button btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				Intent intent = new Intent(DextendAgreeSuccessActivity.this,
						QueryAgreeActivity.class);
				intent.putExtra(ConstantGloble.ACC_POSITION, 1);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
