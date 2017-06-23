package com.chinamworld.bocmbci.biz.thridmanage.platforacct;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.CurrencyType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 台账查询详情
 * 
 * @author panwe
 * 
 */
public class PlatforAcctInfoActivity extends ThirdManagerBaseActivity {

	private View mViewContent;
	private Map<String, String> mDataMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		if (getIntentData()) {
			mViewContent = LayoutInflater.from(this).inflate(R.layout.third_platforacc_query_info_activity, null);
			addView(mViewContent);
			setTitle(R.string.third_platfor);
			findView();
		} else {
			finish();
		}
	}

	private void findView() {
		// 右上角按钮赋值
		setTitleRightText(getString(R.string.go_main));
		setRightBtnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goMainActivity();
			}
		});
		
		/** 银行账户 */
		TextView bankAccountView = (TextView) mViewContent.findViewById(R.id.tv_bank_account);
		/** 证券公司 */
		TextView openaccCompanyView = (TextView) mViewContent.findViewById(R.id.tv_openacc_company);
		/** 保证金账户 */
		TextView cecurityAccountView = (TextView) mViewContent.findViewById(R.id.tv_trade_account);
		/** 币种 */
		TextView currencyTypeView = (TextView) mViewContent.findViewById(R.id.tv_currency_type);
		/** 保证金金额 */
		TextView amountView = (TextView) mViewContent.findViewById(R.id.tv_amount_money);
		/** 交易时间 */
		TextView tradeTimeView = (TextView) mViewContent.findViewById(R.id.tv_trade_time);

		String bankAccount = mDataMap.get(Comm.ACCOUNTNUMBER);
		String currencyType = mDataMap.get(Third.CECURITY_AMOUT_RNCY);
		String availableBalance = mDataMap.get(Third.CECURITY_AMOUT_AVAI);
		String tradeDate = mDataMap.get(Third.PLATFORACC_LIST_DATE);
		String financeCompany = mDataMap.get(Third.CECURITY_AMOUT_COMANY);
		String capitalAcc = mDataMap.get(Third.CECURITY_AMOUT_CAACC);

		bankAccountView.setText(StringUtil.getForSixForString(bankAccount));
		openaccCompanyView.setText(financeCompany);
		cecurityAccountView.setText(capitalAcc);
		currencyTypeView.setText(CurrencyType.getCurrencyTypeStr(currencyType));
		amountView.setText(StringUtil.parseStringPattern(availableBalance, 2));
		tradeTimeView.setText(tradeDate);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, bankAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, openaccCompanyView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, cecurityAccountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, currencyTypeView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, amountView);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tradeTimeView);
	}

	@SuppressWarnings("unchecked")
	private boolean getIntentData() {
		mDataMap = (Map<String, String>) getIntent().getSerializableExtra("data");
		return mDataMap != null;
	}

}
