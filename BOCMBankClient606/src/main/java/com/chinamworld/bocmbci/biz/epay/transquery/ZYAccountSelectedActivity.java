package com.chinamworld.bocmbci.biz.epay.transquery;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TQConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.transquery.adapter.AccountListAdapter;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
/**
 * 支付交易查询-中银快付 账户选择页面
 * @author Administrator
 *
 */
public class ZYAccountSelectedActivity extends EPayBaseActivity {

	private View accountSelect;
	private LinearLayout foot_layout;
	
	private ListView lv_account_list;
	
	private Context tqContext;
	private PubHttpObserver httpObserver;
	private List<Object> accountList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		tqContext = TransContext.getTQTransContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_TREATY);
		accountSelect = LayoutInflater.from(this).inflate(R.layout.epay_tq_zy_account_select, null);
		super.setType(1);
		super.setShowBackBtn(false);
		super.setTitleName("请选择需要查询的账户");
		super.setContentView(accountSelect);
		super.onCreate(savedInstanceState);
		super.initTitleRightButton("关闭", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(101);
				finish();
			}
		});
		foot_layout = (LinearLayout)super.findViewById(R.id.foot_layout);
		foot_layout.setVisibility(View.GONE);
		
		initCurPage();
	}

	private void initCurPage() {
		lv_account_list = (ListView) accountSelect.findViewById(R.id.lv_account_list);
		accountList = tqContext.getList(TQConstants.PUB_ZY_ACCOUNTS);
		activeListView();
	}

	private void activeListView() {
		final AccountListAdapter accountListAdapter = new AccountListAdapter(this, accountList);
		lv_account_list.setAdapter(accountListAdapter);
		lv_account_list.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<Object, Object> account = EpayUtil.getMap(accountList.get(position));
				tqContext.setData(PubConstants.CONTEXT_FIELD_SELECTED_ACC, account);
				setResult(ZY_QUERY);
				finish();
			} 
		});
		BiiHttpEngine.dissMissProgressDialog();
	}

	@Override
	public void finish() {
		tqContext.clear(TQConstants.PUB_ZY_ACCOUNTS);
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
	}

}
