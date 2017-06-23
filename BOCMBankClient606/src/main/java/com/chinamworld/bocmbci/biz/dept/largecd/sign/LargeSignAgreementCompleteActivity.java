package com.chinamworld.bocmbci.biz.dept.largecd.sign;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.largecd.LargeCDAvailableQryActivity;
import com.chinamworld.bocmbci.biz.dept.largecd.LargeCDMenuActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 大额存单签约 协议页面 完成页面
 * 
 * @author luqp 2016年1月14日15:27:45
 */
public class LargeSignAgreementCompleteActivity extends DeptBaseActivity {
	private Context context = this;
	/** 加载布局 */
	private LayoutInflater inflater = null;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;

	/** 选择的签约账户 */
	private Map<String, Object> selectedAccount = null;
	/** 卡类型 */
	private TextView accType = null;
	/** 账户 */
	private TextView accNum = null;
	/** 账户类型 */
	private String accountType = null;
	/** 账户 */
	private String accountNumber = null;
	/** 账户信息Map */
	private Map<String, Object> signedAcc = null;
	/** 确认按钮 */
	private Button confirm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.large_cd_sign_title));
		inflater = LayoutInflater.from(this); // 加载布局
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.large_sign_complete, null); // 加载布局
		tabcontent.addView(view); // 加载布局
		// setLeftSelectedPosition(LARGE_CD_MENU); // 侧边栏
		// 用户选择的数据
		selectedAccount = DeptDataCenter.getInstance().getLargeSignSelectListMap();
		// 用来判断是否已签约
		signedAcc = DeptDataCenter.getInstance().getSignedAcc();
		// 初始化所有控件
		init();
	}

	/** 初始化view和控件 */
	private void init() {
		ibBack.setVisibility(View.INVISIBLE);
		accType = (TextView) view.findViewById(R.id.acc_account_type);
		accNum = (TextView) view.findViewById(R.id.acc_account_num);
		confirm = (Button) view.findViewById(R.id.btn_confirm);

		accountType = (String) selectedAccount.get(Dept.LargeSign_accountType);
		accountNumber = (String) selectedAccount.get(Dept.LargeSign_accountNumber);

		// 返回按钮
		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, LargeCDMenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		accType.setText(LocalData.AccountType.get(accountType));
		accNum.setText(StringUtil.getForSixForString(accountNumber));
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到查询可购买大额存单
				Intent intent = new Intent();
				intent.setClass(context, LargeCDAvailableQryActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/** 用户点击物理返回按键时返回到三级菜单页面*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(LargeSignAgreementCompleteActivity.this, LargeCDMenuActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}