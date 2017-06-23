package com.chinamworld.bocmbci.biz.bond.acct;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.biz.bond.BondBaseActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.allbond.AllBondListActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 开户结果页
 * 
 * @author panwe
 * 
 */
public class OpendAcctResultActivity extends BondBaseActivity implements
		OnClickListener {
	/** 主布局 **/
	private View mainView;
	private boolean isBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		mainView = (View) LayoutInflater.from(this).inflate(
				R.layout.bond_openacct_confirm, null);
		setTitle(this.getString(R.string.bond_acct_open_title));
		addView(mainView);
		btnBack.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		// 隐藏左侧菜单
		setLeftButtonPopupGone();
		// 隐藏底部tab
		setBottomTabGone();

		init();
	}

	private void init() {
		isBuy = getIntent().getBooleanExtra(ISBUY, false);

		String acctNum = getIntent().getStringExtra(BANKACCTNUM);
		TextView tvBnakAcctType = (TextView) findViewById(R.id.bondtype_title);
		tvBnakAcctType.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvBnakAcctType);

		TextView tvTip = (TextView) findViewById(R.id.tv_bill_tip);
		tvTip.setText(R.string.bond_openacct_result);

		TextView tvBnakAcct = (TextView) findViewById(R.id.tv_bankacct);
		TextView tvBondAcctType = (TextView) findViewById(R.id.tv_bondaccttype);
		TextView tvName = (TextView) findViewById(R.id.tv_customName);
		TextView tvIdType = (TextView) findViewById(R.id.tv_idtype);
		TextView tvIdNumber = (TextView) findViewById(R.id.tv_idnum);
		TextView tvGender = (TextView) findViewById(R.id.tv_gender);

		TextView tvCountryCode = (TextView) findViewById(R.id.tv_countrycode);
		TextView tvAdress = (TextView) findViewById(R.id.tv_adress);
		TextView tvPostCode = (TextView) findViewById(R.id.tv_postcode);
		TextView tvPhone = (TextView) findViewById(R.id.tv_phone);
		TextView tvFax = (TextView) findViewById(R.id.tv_fax);
		TextView tvEmail = (TextView) findViewById(R.id.tv_email);

		Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setText(R.string.finish);
		btnConfirm.setOnClickListener(this);

		Map<String, Object> custMap = BondDataCenter.getInstance()
				.getOpenBondCustomerInfoMap();
		/** 资金账号 */
		tvBnakAcct.setText(StringUtil.getForSixForString(acctNum));
		/** 债券类型 */
		tvBondAcctType.setText(BondDataCenter.getInstance().bondType().get(0));
		/** 姓名 */
		tvName.setText(commSetText((String) custMap.get(Bond.CUSTOMERNAME)));
		/** 证件类型 */
		tvIdType.setText(BondDataCenter.IDENTITYTYPE.get(custMap
				.get(Bond.IDENTIFYTYPE)));
		/** 证件号 */
		tvIdNumber.setText(commSetText((String) custMap
				.get(Bond.IDENTIFYNUMBER)));
		/** 性别 */
		tvGender.setText(commSetText(BondDataCenter.gender_hq.get(custMap
				.get(Bond.CUSTOMERGENDER))));
		/** 国籍 */
		tvCountryCode.setText(BondDataCenter.countryCode.get(custMap
				.get(Bond.CUSTMOERCOUNTRY)));
		/** 通讯地址 */
		tvAdress.setText(commSetText((String) custMap.get(Bond.CUSTMOERADRESS)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvAdress);
		/** 邮编 */
		tvPostCode.setText(commSetText((String) custMap
				.get(Bond.CUSTMOERPOSTCODE)));
		/** 手机号码 */
		tvPhone.setText(commSetText((String) custMap.get(Bond.CUSTMOERPHONE)));
		/** 传真 */
		tvFax.setText(commSetText((String) custMap.get(Bond.CUSTMOERFAX)));
		/** email */
		tvEmail.setText(commSetText((String) custMap.get(Bond.CUSTMOEREMAIL)));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent it = new Intent(this, AllBondListActivity.class);
		it.putExtra(ISSUCCESS, true);
		if (isBuy) {
			setResult(RESULT_OK);
		} else {
			startActivity(it);
		}
		finish();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	@Override
	protected void onResume() {
		super.onResume();
		setLeftButtonPopupGone();
	}
}
