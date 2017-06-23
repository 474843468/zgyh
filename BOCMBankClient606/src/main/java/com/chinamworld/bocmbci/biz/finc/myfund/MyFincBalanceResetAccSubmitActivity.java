package com.chinamworld.bocmbci.biz.finc.myfund;

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

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.adapter.MyFincBalanceResetAccAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的基金 重设基金账户 填写页面
 * 
 * @author 宁焰红
 * 
 */
public class MyFincBalanceResetAccSubmitActivity extends FincBaseActivity {
	private final String TAG = "MyFincBalanceResetAccSubmitActivity";
	/** 基金持仓主view */
	private View myFincView = null;
	/** 显示所有账户 */
	private ListView listView = null;
	/** 确定按钮 */
	private Button sureButton = null;
	/** 账户详细信息 */
	private List<Map<String, Object>> resultList = null;
	/** 适配器 */
	private MyFincBalanceResetAccAdapter adapter = null;
	/** 资金账户别名 */
	private String nickName = null;
	/** 资金账户 */
	private String accountNumber = null;
	/** 资金账户标志 */
	private String accountId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingbaseinit();
		init();
		// 查询用户的所有的账户
		BaseHttpEngine.showProgressDialogCanGoBack();
		// 查询用户的所有账户
		queryAccList();
		initOnClick();
	}

	/** 初始化控件 */
	private void init() {
		myFincView = mainInflater.inflate(
				R.layout.finc_myfinc_balance_reset_submit, null);
		tabcontent.addView(myFincView);
		setTitle(getResources().getString(R.string.bond_acct_reset));
		listView = (ListView) findViewById(R.id.finc_ListView);
		sureButton = (Button) findViewById(R.id.sureButton);
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources()
								.getString(R.string.finc_myfinc_acc1),
						this.getResources()
								.getString(R.string.finc_myfinc_acc2),
						this.getResources()
								.getString(R.string.finc_myfinc_acc3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		right.setText(getString(R.string.switch_off));
		right.setOnClickListener(this);
		back.setVisibility(View.GONE);
		right.setVisibility(View.VISIBLE);
	}

	private void initOnClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> map = resultList.get(position);
				adapter.setSelectedPosition(position);
				nickName = (String) map.get(Finc.FINC_NICKNAME_RES);
				accountNumber = (String) map.get(Finc.FINC_ACCOUNTNUMBER_RES);
				accountId = (String) map.get(Finc.FINC_ACCOUNTIDM_RES);
				adapter.notifyDataSetChanged();
			}
		});
		// 确定按钮
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtil.isNullOrEmpty(accountId)) {
					if(accountNumber.equals(fincControl.accNum)){
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								getString(R.string.finc_resetAcc_error));
					}else{
						BaseHttpEngine.showProgressDialog();
						requestCommConversationId();
					}
					// Intent intent = new Intent(
					// MyFincBalanceResetAccSubmitActivity.this,
					// MyFincBalanceResetAccConfirmActivity.class);
					// intent.putExtra(Finc.FINC_NICKNAME_RES, nickName);
					// intent.putExtra(Finc.FINC_ACCOUNTNUMBER_RES,
					// accountNumber);
					// intent.putExtra(Finc.FINC_ACCOUNTIDM_RES, accountId);
					// startActivity(intent);
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.finc_notselecte_acc_error2));
				}

			}
		});
	}

	@Override
	public void queryAccListCallback(Object resultObj) {
		super.queryAccListCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		} else {
			adapter = new MyFincBalanceResetAccAdapter(
					MyFincBalanceResetAccSubmitActivity.this, resultList);
			listView.setAdapter(adapter);
		}

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取tockenId
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			requestPsnFundRegistFundAccount(accountId, tokenId);
		}
	}
	
	/** 登记基金账户---回调 */
	@Override
	public void requestPsnFundRegistFundAccountCallback(Object resultObj) {
		super.requestPsnFundRegistFundAccountCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> result = (Map<String, String>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		} else {
			String fincAccount = result.get(Finc.FINC_FINCACCOUNT_RES);
			if (StringUtil.isNullOrEmpty(fincAccount)) {
				return;
			} else {
				// 跳转到账户设置成功页面
				Intent intent = new Intent(
						MyFincBalanceResetAccSubmitActivity.this,
						MyFincBalanceResetAccSuccessActivity.class);
				intent.putExtra(Finc.FINC_NICKNAME_RES, nickName);
				intent.putExtra(Finc.FINC_ACCOUNTNUMBER_RES, accountNumber);
				intent.putExtra(Finc.FINC_FINCACCOUNT_RES, fincAccount);
				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
			}
		}

	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_top_right_btn:
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setResult(RESULT_OK);
		finish();
	}
}
