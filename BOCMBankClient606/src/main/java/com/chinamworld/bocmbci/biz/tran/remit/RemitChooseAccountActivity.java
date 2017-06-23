package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.atmremit.adapter.AtmChooseAccountAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 汇款套餐选择账户
 * 
 * @author wangmengmeng
 * 
 */
public class RemitChooseAccountActivity extends TranBaseActivity {

	/** 账户列表信息页 */
	private View view;
	/** 账户列表 */
	private ListView lvBankAccountList;
	/** 确定按钮 */
	private Button btnConfirm;
	/** 选中记录项 */
	public int selectposition = -1;
	/** 请求回来的账户列表信息 */
	protected List<Map<String, Object>> bankAccountList;
	/** 套餐修改 */
	private boolean isModify = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_menu_one));
		toprightBtn();
		bankAccountList = TranDataCenter.getInstance().getAccountList();
		// 添加布局
		view = addView(R.layout.tran_atm_choose_list);
		Intent intent = this.getIntent();
		if (intent != null) {
			isModify = intent.getBooleanExtra(ConstantGloble.ACC_ISMY, false);
			if (isModify) {
				setTitle(this.getString(R.string.trans_remit_menu_two));
			}

		}
		// 初始化界面
		init();
		// 都已请求，进行列表赋值
		final AtmChooseAccountAdapter adapter = new AtmChooseAccountAdapter(
				RemitChooseAccountActivity.this, bankAccountList);
		lvBankAccountList.setAdapter(adapter);
		lvBankAccountList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selectposition == position) {
					return;
				} else {
					selectposition = position;
					adapter.setSelectedPosition(position);
				}
			}
		});
		// 确定按钮监听
		btnConfirm.setOnClickListener(confirmClickListener);
	}

	/** 初始化界面 */
	private void init() {
		lvBankAccountList = (ListView) view.findViewById(R.id.acc_accountlist);
		TextView tv_acc_lose_title = (TextView) view
				.findViewById(R.id.tv_acc_lose_title);
		tv_acc_lose_title.setText(this
				.getString(R.string.trans_remit_choose_account_title));
		btnConfirm = (Button) view.findViewById(R.id.btn_confirm);

	}

	/** 确定按钮点击监听事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (selectposition == -1) {
				// 代表没有选择账户就点击了下一步
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						RemitChooseAccountActivity.this
								.getString(R.string.trans_remit_choose_account_title));
				return;
			}
			TranDataCenter.getInstance().setAtmChooseMap(
					bankAccountList.get(selectposition));
			if (isModify) {
				// 套餐修改
				String accountId = (String) bankAccountList.get(selectposition)
						.get(Tran.ACC_ACCOUNTID_REQ);
				// 请求查询套餐
				remitSetMealQuery(accountId);
				BiiHttpEngine.showProgressDialog();
			} else {
				// 请求系统时间
				requestSystemDateTime();
				BiiHttpEngine.showProgressDialog();
			}

		}
	};

	/** 汇款套餐查询 */
	private void remitSetMealQuery(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_REMITSETMEALQUERY_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"remitSetMealQueryCallBack");
	}

	/** 汇款套餐查询------回调 */
	public void remitSetMealQueryCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		TranDataCenter.getInstance().setShareQueryMap(returnList);
		// 进入修改信息页面
		Intent intent = new Intent(this, RemitSetMealModifyInputActivity.class);
		startActivity(intent);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		if (!StringUtil.isNull(dateTime)) {
			Intent intent = new Intent(RemitChooseAccountActivity.this,
					RemitSetMealInputActivity.class);
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			startActivity(intent);

		}
	}

}
