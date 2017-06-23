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
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 绑定完成页面
 * @author panwe
 *
 */
public class BindResultActivity extends BociBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.bocinvt_bind_title);
		addView(R.layout.bocinvt_ofacct_bind);
		goneLeftButton();
		setUpViews();
	}
	
	@SuppressWarnings("unchecked")
	private void setUpViews(){
		((TextView) findViewById(R.id.main_acct)).setText(StringUtil.getForSixForString((String)((Map<String, Object>)BociDataCenter.
				getInstance().getAcctOFmap().get(BocInvt.FINANCIALACCOUNT)).get(Comm.ACCOUNTNUMBER)));
		((TextView) findViewById(R.id.bank_acct)).setText(StringUtil.getForSixForString((String)BociDataCenter.getInstance().
				getAllAcctList().get(getIntent().getIntExtra("p", -1)).get(Comm.ACCOUNTNUMBER)));
		((TextView) findViewById(R.id.remit_title_tv)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.remit_title_tv)).setText(getString(R.string.bocinvt_bindresult_title));
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
