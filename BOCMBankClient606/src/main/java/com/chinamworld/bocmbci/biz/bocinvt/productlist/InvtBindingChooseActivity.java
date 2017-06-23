package com.chinamworld.bocmbci.biz.bocinvt.productlist;

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
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.adapter.BindingResetAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 选择登记账户信息页
 * 
 * @author wangmengmeng
 * 
 */
public class InvtBindingChooseActivity extends BociBaseActivity {
	/** 登记账户选择账户页 */
	private View view;
	/** 账户列表信息 */
	private List<Map<String, Object>> xpadResetList;
	/** 选择账户列表List */
	private ListView xpadListView;
	/** 选中记录项 */
	public int selectposition = -1;
	/** 下一步按钮 */
	private Button btnNext;
	/** 选择的资金账户信息 */
	private Map<String, Object> chooseBindingMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getResources().getString(R.string.boci_binding_title));
		// 右上角按钮赋值
		setText(this.getResources().getString(R.string.close));
		// 添加布局
		view = addView(R.layout.bocinvt_binding_choose);
		goneLeftView();
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		// 初始化界面
		init();
	}

	private void init() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		// 步骤条
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] {
						this.getResources().getString(
								R.string.bocinvt_rel_step1),
						this.getResources().getString(
								R.string.bocinvt_rel_step2),
						this.getResources().getString(
								R.string.bocinvt_rel_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
//		xpadResetList 为null
		
		if(!StringUtil.isNullOrEmpty(BociDataCenter.getInstance().getUnSetAcctList())){
			xpadResetList = BociDataCenter.getInstance().getUnSetAcctList();
		}else{
			xpadResetList=(List<Map<String, Object>>)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_XPADRESET_LIST);
		}
		

		xpadListView = (ListView) view.findViewById(R.id.bocinvt_binding_list);
		final BindingResetAdapter adapter = new BindingResetAdapter(this,
				xpadResetList);
		xpadListView.setAdapter(adapter);
		xpadListView.setOnItemClickListener(new OnItemClickListener() {
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
		btnNext = (Button) view.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(nextBtnClick);
	}

	/** 选择账户下一步按钮点击事件 */
	OnClickListener nextBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (selectposition == -1) {
				// 没有选择账户
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						InvtBindingChooseActivity.this
								.getString(R.string.bocinvt_binding_title));
				return;
			}
			chooseBindingMap = xpadResetList.get(selectposition);
			BaseDroidApp.getInstanse().getBizDataMap()
			.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, chooseBindingMap);
			Intent intent = new Intent(InvtBindingChooseActivity.this, InvtBindingConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
//			requestAccBankAccountDetail(String.valueOf(chooseBindingMap
//					.get(BocInvt.BOCINVT_BINDING_ACCOUNTID_RES)));

		}
	};

//	/**
//	 * 请求账户余额
//	 * 
//	 * @param accountId
//	 *            账户标识Id
//	 */
//	public void requestAccBankAccountDetail(String accountId) {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
//		Map<String, Object> paramsmap = new HashMap<String, Object>();
//		paramsmap.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, String.valueOf(accountId));
//		biiRequestBody.setParams(paramsmap);
//		BiiHttpEngine.showProgressDialog();
//		HttpManager.requestBii(biiRequestBody, this,
//				"accBankAccountDetailCallback");
//
//	}
//
//	/**
//	 * 请求账户余额回调
//	 * 
//	 * @param resultObj
//	 */
//	public void accBankAccountDetailCallback(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 通讯结束,关闭通讯框
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		Map<String, Object> bankactmap = (Map<String, Object>) (biiResponseBody
//				.getResult());
//		if (StringUtil.isNullOrEmpty(bankactmap)) {
//			chooseBindingMap.put(Acc.DETAIL_ACCOUNTDETAILIST_RES, null);
//			return;
//		}
//		/**
//		 * 账户详情余额列表信息
//		 */
//		List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (bankactmap
//				.get(Acc.DETAIL_ACCOUNTDETAILIST_RES));
//		if (accountDetailList == null || accountDetailList.size() == 0) {
//			chooseBindingMap.put(Acc.DETAIL_ACCOUNTDETAILIST_RES, null);
//			return;
//		}
//		/** 把账户列表与详情列表拼接 */
//		chooseBindingMap
//				.put(Acc.DETAIL_ACCOUNTDETAILIST_RES, accountDetailList);
//		BaseDroidApp.getInstanse().getBizDataMap()
//				.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, chooseBindingMap);
//		// 进入确认信息页
//		Intent intent = new Intent(this, InvtBindingConfirmActivity.class);
//		startActivityForResult(intent,
//				ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
//	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
}
