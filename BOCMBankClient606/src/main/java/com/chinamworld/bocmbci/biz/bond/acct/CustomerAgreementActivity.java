package com.chinamworld.bocmbci.biz.bond.acct;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondConstant;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 用户协议
 * 
 * @author panwe
 * 
 */
public class CustomerAgreementActivity extends BondBaseActivity implements
		OnClickListener {
	/** 须知布局 */
	private View customKonwView;
	/** 协议布局 */
	private View customAgreementView;
	/** 控制back键 */
	private boolean isAgreement;
	private boolean isBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BondDataCenter.getInstance().addActivity(this);
		setLeftButtonPopupGone();
		setBottomTabGone();
		customAgreementView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_customer_agreement, null);
		customKonwView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_customer_know, null);
		// 添加布局
		addView(customKonwView);
		setTitle(this.getString(R.string.bond_acct_open_title));
		setText(this.getString(R.string.close));
		btnBack.setVisibility(View.GONE);
		setRightBtnClick(coloseBtnClick);
		isBuy = getIntent().getBooleanExtra(ISBUY, false);
		initV1();
	}

	private void initV1() {
		isAgreement = false;
		Button btnNext = (Button) customKonwView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		TextView tvContent = (TextView) customKonwView.findViewById(R.id.content);
		tvContent.setText(getClickableSpan(tvContent));
		tvContent.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@SuppressWarnings("unchecked")
	private void initV2() {
		isAgreement = true;
		Button btnUnAccept = (Button) customAgreementView
				.findViewById(R.id.btnunaccept);
		Button btnAccept = (Button) customAgreementView
				.findViewById(R.id.btnaccept);
		TextView tvUserName = (TextView) customAgreementView
				.findViewById(R.id.tvFirst);
		tvUserName.setText((String) ((Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(Inves.CUSTOMERNAME));
		btnUnAccept.setOnClickListener(this);
		btnAccept.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 下一步
		case R.id.btnNext:
			layoutContent.removeAllViews();
			addView(customAgreementView);
			initV2();
			break;
			// 不接受
		case R.id.btnunaccept:
			layoutContent.removeAllViews();
			addView(customKonwView);
			initV1();
			break;
		// 接受
		case R.id.btnaccept:
			// 请求资金账号列表
			requestBankAcctList();
			break;
		}
	}
	
	private SpannableString getClickableSpan(final TextView tv) {
		String text = getString(R.string.bond_customer_xz);
		final SpannableString sp = new SpannableString(text);
		sp.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				layoutContent.removeAllViews();
				addView(customAgreementView);
				initV2();
			}
		}, text.length()-35, text.length()-11, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), text.length()-35, text.length()-11,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return sp;
	}

	/** 资金账号列表返回处理 **/
	@Override
	public void bankAccListCallBack(Object resultObj) {
		super.bankAccListCallBack(resultObj);
		List<Map<String, Object>> bankAccList = BondDataCenter.getInstance()
				.getBankAccList();
		Intent it = new Intent();
		if (bankAccList == null) {
			it.setClass(this, NoBankAcctActivity.class);
		} else {
			it.setClass(this, BankAcctListActivity.class);
			it.putExtra(ISOPEN, true);
		}
		if (isBuy) {
			it.putExtra(ISBUY, isBuy);
			startActivityForResult(it, BondConstant.BOND_REQUEST_OPEN_ACCT_CODE);
		} else {
			startActivity(it);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isAgreement) {
				layoutContent.removeAllViews();
				addView(customKonwView);
				initV1();
			} else {
				finish();
				overridePendingTransition(R.anim.no_animation,
						R.anim.slide_down_out);
			}
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BondConstant.BOND_REQUEST_OPEN_ACCT_CODE:
			switch (resultCode) {
			case RESULT_OK:
				setResult(RESULT_OK);
				finish();
				break;
			
			default:
				break;
			}
			break;

		default:
			break;
		}
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftButtonPopupGone();
//	}
}
