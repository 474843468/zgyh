package com.chinamworld.bocmbci.biz.bocinvt.acctmanager;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 解绑完成页面
 * @author panwe
 *
 */
public class DisengageBindResultActivity extends BociBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.bocinvt_disengagebind_titile);
		addView(R.layout.bocinvt_ofacct_disengagebind);
		goneLeftButton();
		setUpViews();
	}
	
	@SuppressWarnings("unchecked")
	private void setUpViews(){
		Map<String, Object> acctOFmap = BociDataCenter.getInstance().getAcctOFmap();
		String mainAcct = (String) ((Map<String, Object>) acctOFmap.get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNTNUMBER);
		if (StringUtil.isNullOrEmpty(getOFBankAccountInfo())) {
			((TextView) findViewById(R.id.bank_acct)).setText(ConstantGloble.FINC_COMBINQURY_NONE);
		}else{
			String bankAcct = (String) getOFBankAccountInfo().get(Comm.ACCOUNTNUMBER);
			((TextView) findViewById(R.id.bank_acct)).setText(StringUtil.getForSixForString(bankAcct));
		}
		((TextView) findViewById(R.id.main_acct)).setText(StringUtil.getForSixForString(mainAcct));
		((TextView) findViewById(R.id.remit_title_tv)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.remit_title_tv)).setText(getString(R.string.bocinvt_disbindresult_title));
	}
	
	/**
	 * 完成操作
	 * @param v
	 */
	public void buttonOnclick(View v){
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
