package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟银行卡服务
 * 
 * @author huangyuchao
 * 
 */
public class VirtualBCServiceMenuActivity extends CrcdAccBaseActivity {
	/** 电子现金账户列表页 */
	private View view;
	// 点击三级菜单后判断进入哪个功能
	private int go_menu_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 初始化左边菜单
		initLeftSideList(this, LocalData.myCrcdListData);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_service_menu));
		// 添加布局
		view = addView(R.layout.mycard_virtualbc_menu);
		initLeftSideList(this, LocalData.accountManagerlistData);
		setLeftSelectedPosition("accountManager_5");
		btn_right.setVisibility(View.GONE);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().closeAllActivityExceptOne("MainActivity");
//				Intent intent = new Intent(VirtualBCServiceMenuActivity.this, MainActivity.class);
//				startActivity(intent);
//				goToMainActivity();
				ActivityTaskManager.getInstance().removeAllActivity();
				finish();
			}
		});
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	private void init() {

		LinearLayout crcd_consume_setup_ll = (LinearLayout) view.findViewById(R.id.crcd_consume_setup_ll);
		crcd_consume_setup_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 我的虚拟卡
				go_menu_id = 1;
				Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.BIZ_LOGIN_DATA);
				String segmentId = (String) returnMap.get(Crcd.CRCD_SEGMENTID);
				if (StringUtil.isNull(segmentId) || ConstantGloble.CRCD_TEN.equals(segmentId)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.crcd_no_crcd));
					return;
				} else {
					Intent it = new Intent(VirtualBCServiceMenuActivity.this, MyVirtualBCListActivity.class);
					startActivity(it);
				}
			}
		});

		LinearLayout crcd_billservice_setup_ll = (LinearLayout) view.findViewById(R.id.crcd_billservice_setup_ll);
		crcd_billservice_setup_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 虚拟卡申请
				go_menu_id = 2;
				Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.BIZ_LOGIN_DATA);
				String segmentId = (String) returnMap.get(Crcd.CRCD_SEGMENTID);
				if (StringUtil.isNull(segmentId) || ConstantGloble.CRCD_TEN.equals(segmentId)) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.crcd_no_crcd));
					return;
				} else {
					Intent it = new Intent(VirtualBCServiceMenuActivity.this, VirtualBCServiceListActivity.class);
					startActivity(it);
				}
			}
		});
	}
}
