package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟卡关联网银成功
 * 
 * @author huangyuchao
 * 
 */
public class MyVirtualGuanLianSuccessActivity extends CrcdAccBaseActivity {

	private View view = null;
	Button sureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setTitle(this.getString(R.string.mycrcd_virtual_guanlian_title));
		if (view == null) {
			view = addView(R.layout.crcd_virtualcard_guanlian_success);
		}
		back.setVisibility(View.GONE);
		init();
	}

	TextView mycrcd_account, mycrcd_shiti_num, mycrcd_xuni_num;

	/** 初始化界面 */
	private void init() {

		mycrcd_account = (TextView) findViewById(R.id.mycrcd_account);
		mycrcd_shiti_num = (TextView) findViewById(R.id.mycrcd_shiti_num);
		mycrcd_xuni_num = (TextView) findViewById(R.id.mycrcd_xuni_num);

		mycrcd_account.setText(MyVirtualBCListActivity.accountId);
		mycrcd_shiti_num.setText(StringUtil
				.getForSixForString(MyVirtualBCListActivity.accountNumber));
		mycrcd_xuni_num.setText(StringUtil
				.getForSixForString(VirtualBCListActivity.virtualCardNo));

		sureButton = (Button) findViewById(R.id.sureButton);

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(MyVirtualGuanLianSuccessActivity.this,
						MyVirtualBCListActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				//ActivityTaskManager.getInstance().removeAllActivity();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

}
