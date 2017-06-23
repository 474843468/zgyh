package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 添加共享账户页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitAddSharedAccountActivity extends TranBaseActivity {
	/** 添加页面 */
	private View view;
	/** 账号 */
	private EditText et_shareno;
	private String shareno;
	/** 姓名 */
	private EditText et_sharename;
	private String sharename;
	/** 添加按钮 */
	private Button addButton;
	private List<Map<String, String>> shareAccList = new ArrayList<Map<String, String>>();
	/** 套餐编号*/
	private String PackageNumberId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_setmeal_add_title));
		back.setVisibility(View.GONE);
		mTopRightBtn.setText(this.getString(R.string.switch_off));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
				finish();
				overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
			}
		});
		// 添加布局
		view = addView(R.layout.tran_remit_add_shared_acc);
		init();
	}

	public void init() {
		PackageNumberId = getIntent().getStringExtra(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID);
		shareAccList = TranDataCenter.getInstance().getShareAccountList();
		goneLeftView();
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		et_shareno = (EditText) view.findViewById(R.id.tran_remit_add_sharedno);
		et_sharename = (EditText) view.findViewById(R.id.tran_remit_add_sharedname);
		EditTextUtils.setLengthMatcher(this, et_sharename, 120);
		addButton = (Button) view.findViewById(R.id.remit_add_acc);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sharename = et_sharename.getText().toString().trim();
				shareno = et_shareno.getText().toString().trim();
				ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
				if (StringUtil.isNull(shareno)) {
					BaseDroidApp.getInstanse()
					.showInfoMessageDialog(getString(R.string.tran_remit_input_calorific));
					return;
				}
				RegexpBean regAmount = new RegexpBean(getString(R.string.tran_remit_n_query_input_shareNo_nolabel), shareno,
						"chipCardRemitLength");
				RegexpBean regName = new RegexpBean(getString(R.string.epay_treaty_add_confirm_input_tv_cust_name), sharename,
						"payeeNameLength");
				lists.add(regAmount);
				lists.add(regName);
				if (RegexpUtils.regexpData(lists)) {
					if (!StringUtil.isNullOrEmpty(shareAccList)) {
						for (Map<String, String> sharemap : shareAccList) {
							String accountNumber = sharemap.get(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ);
							if (shareno.equals(accountNumber)) {
								// 提醒错误信息
								BaseDroidApp.getInstanse()
										.showInfoMessageDialog(getString(R.string.tran_remit_add_acc));
								return;
							}
						}
					} else {
						shareAccList = new ArrayList<Map<String, String>>();
					} 
					requestRemitcheckAcc();
				}
			}
		});
	}

	/**
	 * 请求共享账户检查
	 */
	public void requestRemitcheckAcc() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CHECKSHAREDACCOUNT_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Tran.TRAN_REMIT_CHECK_SHAREDACCOUNTNAME_REQ, sharename);
		paramsmap.put(Tran.TRAN_REMIT_CHECK_SHAREDACCOUNTNUMBER_REQ, shareno);
		paramsmap.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID, PackageNumberId);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestRemitcheckAccCallBack");
	}

	/**
	 * 请求所有汇款笔数套餐卡账户列表信息回调
	 * 
	 * @param resultObj
	 */
	public void requestRemitcheckAccCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		String type = (String) biiResponseBody.getResult();
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRAN_REMIT_APP_SHARECARDNO_REQ, shareno);
		map.put(Tran.TRAN_REMIT_APP_SHAREACCNAME_REQ, sharename);
		map.put(Tran.TRAN_REMIT_APPPRE_REMITSETMEALPRODUCPROPERTYID, PackageNumberId);
		map.put(Tran.TRAN_REMIT_APP_ACCOUNTTYPE_REQ, type);
		shareAccList.add(map);
		TranDataCenter.getInstance().setShareAccountList(shareAccList);
		setResult(RESULT_OK);
		finish();
		overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
	}

	@Override
	public void onBackPressed() {
	}
}
