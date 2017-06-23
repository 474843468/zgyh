package com.chinamworld.bocmbci.biz.dept.zntzck;

import java.util.Map;

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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 智能通知存款--签约-成功页面 */
public class DeptZntzckSignSuccessActivity extends DeptBaseActivity {
	private static final String TAG = "DeptZntzckSignSuccessActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View queryView = null;
	private TextView signAccText = null;
	private TextView signTypeText = null;
	private TextView signNickText = null;
	private Button sureButton = null;
	private Map<String, String> map = null;
	private String accountnumber = null;
	private String tag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_zntzck_sign));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.GONE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_sign_success, null);
		tabcontent.addView(queryView);
		map = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_ACCINFO);
		if (StringUtil.isNullOrEmpty(map)) {
			return;
		}
		tag = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_GOTO_TAG);
		if (!StringUtil.isNull(tag) && "1".equals(tag)) {
			// 查询页面跳转到此页面
			accountnumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		}
		init();
	}

	private void init() {
		signAccText = (TextView) findViewById(R.id.dept_zntzck_query_signAcc);
		signTypeText = (TextView) findViewById(R.id.forex_customer_accType);
		signNickText = (TextView) findViewById(R.id.forex_customer_accAlias);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, signNickText);
		sureButton = (Button) findViewById(R.id.sureButton);
		String accountType = map.get(Comm.ACCOUNT_TYPE);
		String accountNumber = map.get(Comm.ACCOUNTNUMBER);
		String nickName = map.get(Comm.NICKNAME);
		String type = null;
		if (StringUtil.isNull(accountType)) {
			type = "-";
		} else {
			if (LocalData.AccountType.containsKey(accountType)) {
				type = LocalData.AccountType.get(accountType);
			}
		}
		String accountNumbers = null;
		if (StringUtil.isNull(accountNumber)) {
			accountNumbers = "-";
		} else {
			accountNumbers = StringUtil.getForSixForString(accountNumber);
		}
		if (!StringUtil.isNull(tag) && "1".equals(tag)) {
			// 查询页面跳转到此页面
			signAccText.setText(accountnumber);
		} else {
			signAccText.setText(accountNumbers);
		}
		signTypeText.setText(type);
		signNickText.setText(nickName);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tag = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.DEPT_GOTO_TAG);
				if (StringUtil.isNull(tag) || "2".equals(tag)) { //代表用户是由 签约入口进入到这里的
					Intent intent = new Intent(DeptZntzckSignSuccessActivity.this, DeptZntzckThreeMenuActivity.class);
					startActivity(intent);
					finish();
				} else if ("1".equals(tag)) {               //代表用户是由 查询入口进入到这里的
					Intent intent = new Intent(DeptZntzckSignSuccessActivity.this, DeptZntzckThreeMenuActivity.class);
					startActivity(intent);
					finish();
				}

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
