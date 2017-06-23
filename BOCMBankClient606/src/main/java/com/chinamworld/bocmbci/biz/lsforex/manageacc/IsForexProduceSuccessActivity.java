package com.chinamworld.bocmbci.biz.lsforex.manageacc;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseActivity;
import com.chinamworld.bocmbci.biz.lsforex.acc.IsForexSettingBindAccActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/** 签约成功页面 */
public class IsForexProduceSuccessActivity extends IsForexBaseActivity {
	public static final String TAG = "IsForexProduceSuccessActivity";
	private Button backButton = null;
	private View rateInfoView = null;
	private TextView tradeAccSpinner = null;
	private TextView baillAccSpinner = null;
	private TextView typeText = null;
	private TextView nickText = null;
	private TextView cNameText = null;
	private TextView eNameText = null;
	private TextView jsCodeText = null;
	/** 完成按钮 */
	private Button nextButton = null;
	/** 登记交易账户 */
	private Button signAccButton = null;
	/** 保证金产品list */
	private Map<String, String> baillResultList = null;
	/** 符合条件的借记卡信息 */
	private List<Map<String, String>> resultList = null;
	private int tradePosition = -1;
	private String jsCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.isForex_manage_right));
		LogGloble.d(TAG, "onCreate");
		backButton = (Button) findViewById(R.id.ib_back);
		backButton.setVisibility(View.INVISIBLE);
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_manage_sign_success, null);
		tabcontent.addView(rateInfoView);
		baillResultList = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_LIST);
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_LIST_REQ);
		tradePosition = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		jsCode = getIntent().getStringExtra(IsForex.ISFOREX_SETTLECURRENCY_RES1);
		init();
		setTradeValue();
		initOnClick();
	}

	private void init() {
		tradeAccSpinner = (TextView) findViewById(R.id.isForex_tradeAcc);
		typeText = (TextView) findViewById(R.id.forex_customer_accType);
		nickText = (TextView) findViewById(R.id.forex_customer_accAlias);
		baillAccSpinner = (TextView) findViewById(R.id.isForex_BaillAcc);
		cNameText = (TextView) findViewById(R.id.isForex_manage_bailCNamen);
		eNameText = (TextView) findViewById(R.id.isForex_manage_bailEName);
		jsCodeText = (TextView) findViewById(R.id.isForex_vfgRegCurrency1);
		nextButton = (Button) findViewById(R.id.trade_nextButton);
		signAccButton = (Button) findViewById(R.id.trade_signAcc);
		if (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_FLAG_RES))) {
			// 值为空，任务提示框请求,不显示登记交易账户按钮
			signAccButton.setVisibility(View.GONE);
//			BottomButtonUtils.setSingleLineStyleRed(nextButton);
			setSingleLineStyleRed(nextButton);
		}

		// 保证金产品编号 保证金中文名称 保证金英文名称 弹出框
		TextView tv_chinese = (TextView) findViewById(R.id.tv_chinese);
		TextView tv_english = (TextView) findViewById(R.id.tv_english);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_chinese);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_english);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cNameText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, eNameText);
	}
	/**
	 * 设置按钮底部的单独一行的样式(红色)
	 * @param bottomBtn
	 */
	public static void setSingleLineStyleRed(Button bottomBtn) {
		((ViewGroup.MarginLayoutParams) bottomBtn.getLayoutParams()).setMargins(
				BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.dp_three_zero), 0,
				BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.dp_three_zero), 0);
		bottomBtn.setBackgroundResource(R.drawable.selector_btn_blue);
		bottomBtn.requestLayout();
	}

	/** 为控件赋值 */
	private void setTradeValue() {
		// 资金账户
		Map<String, String> map = resultList.get(tradePosition);
		String accountNumber = map.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ1);
		String account = null;
		if (!StringUtil.isNull(accountNumber)) {
			account = StringUtil.getForSixForString(accountNumber);
		}
		String accountType = map.get(IsForex.ISFOREX_ACCOUNTTYPE_REQ1);
		String type = null;
		if (!StringUtil.isNull(accountType) && LocalData.AccountType.containsKey(accountType)) {
			type = LocalData.AccountType.get(accountType);
		}
		String nickName = map.get(IsForex.ISFOREX_NICKNAME_REQ1);
		tradeAccSpinner.setText(account);
		typeText.setText(type);
		nickText.setText(nickName);
		String marginAccountNo = baillResultList.get(IsForex.ISFOREX_MARGINACCOUNTNO_RES1);
		String marginAccount = null;
		if (!StringUtil.isNull(marginAccountNo)) {
			marginAccount = StringUtil.getForSixForString(marginAccountNo);
		}
		String bailEName = baillResultList.get(IsForex.ISFOREX_BAILENAME_RES);
		String bailCName = baillResultList.get(IsForex.ISFOREX_BAILCNAME_RES);
		jsCodeText.setText(jsCode);
		baillAccSpinner.setText(marginAccount);
		cNameText.setText(bailCName);
		eNameText.setText(bailEName);
	}

	private void initOnClick() {
		// 完成按钮
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到签约信息页面
				if (StringUtil.isNullOrEmpty(BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_FLAG_RES))) {
					// 值为空，任务提示框请求
					LogGloble.d(TAG, "========任务提示框请求");
					setResult(RESULT_OK);
					finish();
				} else {
					int tag = (Integer) BaseDroidApp.getInstanse().getBizDataMap().get(IsForex.ISFOREX_FLAG_RES);
					// if (tag == 11) {
					// 点击签约按钮
					Intent intent = new Intent(IsForexProduceSuccessActivity.this, IsForexBailProduceActivity.class);
					startActivity(intent);
					// }
				}

			}
		});
		// 登记交易账户
		signAccButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IsForexProduceSuccessActivity.this, IsForexSettingBindAccActivity.class);
				startActivity(intent);
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
