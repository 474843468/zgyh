package com.chinamworld.bocmbci.biz.forex.rate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 外汇账户设定成功页面
 * 
 * @author 宁焰红
 * 
 */
public class ForexAccSettingSuccessActivity extends BaseActivity {
	private static final String TAG = "ForexAccSettingSuccessActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexAccSettingSuccessActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * quickTrade：关闭按钮
	 */
	private Button quickTrade = null;
	/**
	 * backButton:返回按钮
	 */
	private Button backButton = null;

	/**
	 * 账户号码
	 */
	private String accNum;
	/**
	 * 账户类型
	 */
	private String accstyle;
	/**
	 * 账户别名
	 */
	private String accalais;
	/**
	 * 账户类型
	 */
	private TextView accTypeText = null;
	/**
	 * 账户别名
	 */
	private TextView accAlisText = null;
	/**
	 * 账户
	 */
	private TextView accNumberText = null;
	private Button sureButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.biz_activity_606_layout);
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 初始化弹窗按钮
		initPulldownBtn();
		init();
		initOnClick();
		
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
				// 返回到外汇行情页面
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		if (intent != null) {
			accstyle = intent.getStringExtra(Forex.FOREX_ACCOUNTTYPE_RES);
			accalais = (String) intent.getStringExtra(Forex.FOREX_NICKNAME_RES);
			accNum = (String) intent
					.getStringExtra(Forex.FOREX_ACCOUNTNUMBER_RES);
			LogGloble.d(TAG + "----accNum", accNum);
		}else{
			return;
		}
		accNum = StringUtil.getForSixForString(accNum);
		String type=null;
		if (LocalData.AccountType.containsKey(accstyle)) {
			type = LocalData.AccountType.get(accstyle);
		}
		accNumberText.setText(accNum);
		accTypeText.setText(type);
		accAlisText.setText(accalais);
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		rateInfoView = LayoutInflater.from(ForexAccSettingSuccessActivity.this)
				.inflate(R.layout.forex_acc_setting_success, null);
		rateInfoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		tabcontent.addView(rateInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate_setAcc));
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.GONE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.GONE);
		accTypeText = (TextView) findViewById(R.id.acc_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accTypeText);
		accAlisText = (TextView) findViewById(R.id.acc_alias);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, accAlisText);
		accNumberText = (TextView) findViewById(R.id.acc_number);
		sureButton = (Button) findViewById(R.id.acc_sure);
//		StepTitleUtils.getInstance().initTitldStep(
//				this,
//				new String[] {
//						this.getResources().getString(R.string.forex_acc_step1),
//						this.getResources().getString(R.string.forex_acc_step2),
//						this.getResources().getString(R.string.forex_acc_step3) });
//		StepTitleUtils.getInstance().setTitleStep(3);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
