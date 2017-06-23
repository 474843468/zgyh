package com.chinamworld.bocmbci.biz.lsforex.acc;

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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseNewActivity;
import com.chinamworld.bocmbci.biz.lsforex.manageacc.IsForexBailProduceActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class IsForexSettingBindAccSuccessActivity extends IsForexBaseActivity {
	private static final String TAG = "ForexAccSettingSuccessActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** ForexAccSettingSuccessActivity的主布局*/
	private View rateInfoView = null;
	/** quickTrade：关闭按钮*/
	private Button quickTrade = null;
	/** backButton:返回按钮*/
	private Button backButton = null;
	/** 账户号码*/
	private String accNum;
	/** 账户类型*/
	private String accstyle;
	/** 账户别名*/
	private String accalais;
	/** 账户类型*/
	private TextView accTypeText = null;
	/** 账户别名*/
	private TextView accAlisText = null;
	/** 账户*/
	private TextView accNumberText = null;
	private Button sureButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_606_layout);
		LogGloble.d(TAG, "onCreate");
		// 初始化弹窗按钮
		initPulldownBtn();
		setLeftButtonPopupGone();
		init();
		initDate();
		initOnClick();
	}

	/** 初始化所有的控件*/
	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		rateInfoView = LayoutInflater.from(IsForexSettingBindAccSuccessActivity.this).inflate(
				R.layout.isforex_set_bind_acc_success, null);
		tabcontent.addView(rateInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate_setAcc));
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.GONE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		accTypeText = (TextView) findViewById(R.id.acc_type);
		accAlisText = (TextView) findViewById(R.id.acc_alias);
		accNumberText = (TextView) findViewById(R.id.acc_number);
		sureButton = (Button) findViewById(R.id.acc_sure);
	}

	private void initDate() {
		Intent intent = getIntent();
		if (intent != null) {
			accstyle = intent.getStringExtra(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
			accalais = (String) intent.getStringExtra(IsForex.ISFOREX_NICKNAME_REQ1);
			accNum = (String) intent.getStringExtra(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1);
		} else {
			return;
		}
		accNum = StringUtil.getForSixForString(accNum);
		String type = null;
		if (LocalData.AccountType.containsKey(accstyle)) {
			type = LocalData.AccountType.get(accstyle);
		}
		accNumberText.setText(accNum);
		accTypeText.setText(type);
		accAlisText.setText(accalais);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accAlisText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accTypeText);
	}

	public void initOnClick() {
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到外汇行情页面
				setResult(RESULT_OK);
				finish();
			}
		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtil
						.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.GENDER_MAN))) {
					// 签约成功页面或变更成功页面点击登记交易账户
					int tag = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.GENDER_MAN);
					Intent intent = new Intent(IsForexSettingBindAccSuccessActivity.this,
							IsForexBailProduceActivity.class);
					startActivity(intent);
				} else {
					// 任务提示框
//					setResult(RESULT_OK);
//					finish();
                    IsForexBaseNewActivity.intent_to_IsForexTwoWayTreasureNewActivity(IsForexSettingBindAccSuccessActivity.this);
                    ActivityTaskManager.getInstance().removeAllSecondActivity();
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
