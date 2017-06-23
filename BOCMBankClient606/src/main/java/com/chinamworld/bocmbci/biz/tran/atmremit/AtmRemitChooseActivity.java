package com.chinamworld.bocmbci.biz.tran.atmremit;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.atmremit.adapter.AtmChooseAccountAdapter;

/**
 * ATM取款选择账户
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRemitChooseActivity extends TranBaseActivity {

	/** 账户列表信息页 */
	private View view;
	/** 账户列表 */
	private ListView lvBankAccountList;
	/** 确定按钮 */
	private Button btnConfirm;
	/** 选中记录项 */
	public int selectposition = -1;
	/** 提示信息 */
	private LinearLayout dept_save_regular_bottom;
	/** 请求回来的账户列表信息 */
	protected List<Map<String, Object>> bankAccountList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_atm_title));
		toprightBtn();
		setLeftSelectedPosition("tranManager_5");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(AtmRemitChooseActivity.this, AtmThirdMenu.class);
				startActivity(intent);
				finish();

			}
		});
		bankAccountList = TranDataCenter.getInstance().getAccountList();
		// 添加布局
		view = addView(R.layout.tran_atm_choose_list);
		// 初始化界面
		init();
		// 都已请求，进行列表赋值
		final AtmChooseAccountAdapter adapter = new AtmChooseAccountAdapter(AtmRemitChooseActivity.this, bankAccountList);
		lvBankAccountList.setAdapter(adapter);
		lvBankAccountList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (selectposition == position) {
					selectposition = -1;
					adapter.setSelectedPosition(selectposition);
				} else {
					selectposition = position;
					adapter.setSelectedPosition(selectposition);
				}
				// selectposition = selectposition == position ? -1 : position;
				// adapter.setSelectedPosition(selectposition);
			}
		});
		// 确定按钮监听
		btnConfirm.setOnClickListener(confirmClickListener);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("tranManager_5");
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent(AtmRemitChooseActivity.this, AtmThirdMenu.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);

	}

	/** 初始化界面 */
	private void init() {
		lvBankAccountList = (ListView) view.findViewById(R.id.acc_accountlist);

		btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
		dept_save_regular_bottom = (LinearLayout) findViewById(R.id.dept_save_regular_bottom);
		dept_save_regular_bottom.setVisibility(View.VISIBLE);

	}

	/** 确定按钮点击监听事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (selectposition == -1) {
				// 代表没有选择账户就点击了下一步
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						AtmRemitChooseActivity.this.getString(R.string.trans_atm_choose_title));
				return;
			}
			TranDataCenter.getInstance().setAtmChooseMap(bankAccountList.get(selectposition));
			Intent intent = new Intent(AtmRemitChooseActivity.this, AtmRemitInputActivity.class);
			startActivity(intent);
		}
	};
}
