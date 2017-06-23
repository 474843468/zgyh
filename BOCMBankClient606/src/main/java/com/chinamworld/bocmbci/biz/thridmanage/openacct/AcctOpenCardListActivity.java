package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.thridmanage.BiiConstant.ThridProvinceType;
import com.chinamworld.bocmbci.biz.thridmanage.ServiceType;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdManagerBaseActivity;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdUtil;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.adapter.AccOpenCardAdapter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 开户卡列表
 * 
 * @author panwe
 * 
 */
public class AcctOpenCardListActivity extends ThirdManagerBaseActivity implements OnClickListener {

	/** 主布局 */
	private View viewContent;
	/** 卡列表 */
	private ListView lvAccCard;
	/** 确定按钮 */
	private Button btnOk;
	/** 列表选中条目 */
	private int selectposition = -1;
	private AccOpenCardAdapter mAdapter;
	/** 业务标识 */
	private String action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = LayoutInflater.from(this).inflate(R.layout.third_openacc_cardlist, null);
		addView(viewContent);
		action = getIntent().getStringExtra("action");
		if (action.equals("open")) {
			setTitle(this.getString(R.string.third_openacc_open));
		} else {
			setTitle(this.getString(R.string.third_openacc_query));
			// TextView texttTip = (TextView)
			// viewContent.findViewById(R.id.text_tip);
			// texttTip.setText(this.getString(R.string.third_platfor_card_tip));
		}

		findView();

		checkServiceState(ServiceType.InvestmentService);
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

		btnOk = (Button) viewContent.findViewById(R.id.btnconfirm);
		btnOk.setOnClickListener(this);

		lvAccCard = (ListView) viewContent.findViewById(R.id.cardlist);
		lvAccCard.setOnItemClickListener(cardItemClick);
		mAdapter = new AccOpenCardAdapter(AcctOpenCardListActivity.this, ThirdDataCenter.getInstance()
				.getBankAccountList());
		lvAccCard.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if (selectposition > -1) {
			if (action.equals("open")) {
				lvAccCard.setClickable(false);
				requestCustInfo();
			} else {
				Intent it = new Intent();
				it.putExtra("ACCID", (String) ThirdDataCenter.getInstance().getBankAccountList().get(selectposition)
						.get(Comm.ACCOUNT_ID));
				it.putExtra("position", selectposition);
				it.setClass(this, AcctOpenedListActivity.class);
				startActivity(it);
			}
		} else {
			if (action.equals("open")) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getString(R.string.third_please_select_reserveopen_acct));
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getString(R.string.third_please_select_query_reserveopen_acct));
			}
		}
	}

	/** 列表点击事件 **/
	private OnItemClickListener cardItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (selectposition != position) {
				selectposition = position;
				mAdapter.setSelectedPosition(selectposition);
			}
		}
	};

	// 获取用户信息
	private void requestCustInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_OPENACC_CUSTINFO);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_ID, (String) ThirdDataCenter.getInstance().getBankAccountList().get(selectposition)
				.get(Comm.ACCOUNT_ID));
		biiRequestBody.setParams(params);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "getCustInfoCallBack");
	}

	/*** 获取用户信息 返回 */
	public void getCustInfoCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		// Map<String, Object> customInfo = (Map<String, Object>)
		// biiResponseBody.getResult();
		Map<String, Object> customInfo = ThirdUtil.getResponseResultToMap(biiResponseBody);
		if (customInfo == null) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_common_error));
			return;
		}
		ThirdDataCenter.getInstance().setCustomInfo(customInfo);
		// requestProvince();
		ThirdDataCenter.getInstance().setProvinceCode(ThridProvinceType.getOldProvincesCode());
		Intent it = new Intent(this, AcctOpenMsgFillActivity.class);
		it.putExtra("position", selectposition);
		startActivity(it);
		lvAccCard.setClickable(true);
	}

	// // 获取地区
	// private void requestProvince() {
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Third.METHOD_OPENACC_ZONE);
	// biiRequestBody.setParams(null);
	// HttpManager.requestBii(biiRequestBody, this, "getZoneInfoCallBack");
	// }
	//
	// /*** 地区信息 返回 */
	// public void getZoneInfoCallBack(Object resultObj) {
	// BaseHttpEngine.dissMissProgressDialog();
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// // List<String> list = (List<String>) biiResponseBody.getResult();
	// List<String> list = ThirdUtil.getResponseResultToList(biiResponseBody);
	// if (list == null) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.third_common_error));
	// return;
	// }
	// ThirdDataCenter.getInstance().setProvinceCode(list);
	// Intent it = new Intent(this, AcctOpenMsgFillActivity.class);
	// it.putExtra("position", selectposition);
	// startActivity(it);
	// lvAccCard.setClickable(true);
	// }
}
