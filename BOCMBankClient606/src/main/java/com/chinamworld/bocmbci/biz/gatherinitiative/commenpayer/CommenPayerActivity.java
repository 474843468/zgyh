package com.chinamworld.bocmbci.biz.gatherinitiative.commenpayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.biz.gatherinitiative.commenpayer.CommenPayerListAdapter.OnDeleteClickLisener;
import com.chinamworld.bocmbci.biz.gatherinitiative.commenpayer.CommenPayerListAdapter.OnOperateClickLisener;
import com.chinamworld.bocmbci.biz.gatherinitiative.creatgather.CreatGatherInputInfoActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * @ClassName: CommenPayerActivity
 * @Description: 常用付款人页面
 * @author JiangWei
 * @date 2013-8-20上午10:27:21
 */
public class CommenPayerActivity extends GatherBaseActivity {
	/** 列表为操作模式 */
	public static final int MODE_OPRATE = 0;
	/** 列表为删除模式 */
	public static final int MODE_DELETE = 1;
	/** 全部列表数据 */
	private List<Map<String, Object>> resultAllList;
	/** 搜索后的列表数据 */
	private List<Map<String, Object>> resultAfterSearch;
	/** 列表view */
	private ListView listView;
	/** 输入框 */
	private EditText searchEdit;
	/** 搜索按钮 */
	private Button btnSearch;
	/** listview的adapter */
	private CommenPayerListAdapter adapter;
	/** 是否是查询的模式 */
	private boolean isSearchMode = false;
	/** tokenId */
	private String tokenId;
	/** 当前选中位置 */
	private int currentPosition = -1;
	private Button btnRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.commen_payer);
		View view = LayoutInflater.from(this).inflate(
				R.layout.gather_commen_contact_activity, null);
		tabcontent.addView(view);

		init();
		requestPsnTransActQueryPayerList();
	}

	private void init() {
		listView = (ListView) this.findViewById(R.id.blpt_lv_province);
		searchEdit = (EditText) this.findViewById(R.id.edit_search);
		btnSearch = (Button) this.findViewById(R.id.btn_query_trans_records);

		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setText(R.string.to_delete_payer);
		final String strDelete = this.getResources().getString(
				R.string.to_delete_payer);
		if (btnRight != null) {
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (btnRight.getText().toString().equals(strDelete)) {
						if (!searchEdit.getText().toString().trim().equals("")) {
							if (resultAfterSearch != null
									&& !resultAfterSearch.isEmpty()) {
								btnRight.setText(R.string.finish);
								adapter.setTheMode(MODE_DELETE);
								adapter.notifyDataSetChanged();
								searchEdit.setEnabled(false);
								btnSearch.setEnabled(false);
							}
						} else {
							if (resultAllList != null
									&& !resultAllList.isEmpty()) {
								btnRight.setText(R.string.finish);
								adapter.setTheMode(MODE_DELETE);
								adapter.notifyDataSetChanged();
								searchEdit.setEnabled(false);
								btnSearch.setEnabled(false);
							}
						}
					} else {
						btnRight.setText(R.string.to_delete_payer);
						adapter.setTheMode(MODE_OPRATE);
						adapter.notifyDataSetChanged();
						searchEdit.setEnabled(true);
						btnSearch.setEnabled(true);
					}
				}
			});
		}

		resultAfterSearch = new ArrayList<Map<String, Object>>();
		// listView.setOnItemClickListener(this);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isSearchMode = true;
				excuseSearch();
			}
		});
		searchEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// 点击查询-没有查到记录，当清除输入的内容，自动显示全部的常用付款人
				String keyword = s.toString().trim();
				if (StringUtil.isNull(keyword)
						&& StringUtil.isNullOrEmpty(resultAfterSearch)) {
					excuseSearch();
				}

			}
		});
	}

	/**
	 * 执行搜索
	 */
	private void excuseSearch() {
		resultAfterSearch.clear();
		String searchStr = searchEdit.getText().toString().trim();
		if (!StringUtil.isNullOrEmpty(resultAllList)) {
			for (int i = 0; i < resultAllList.size(); i++) {
				String str = (String) resultAllList.get(i).get(
						GatherInitiative.PAYER_NAME);
				if (str.contains(searchStr)) {
					resultAfterSearch.add(resultAllList.get(i));
				}
			}
			refreshListView(resultAfterSearch);
			if (StringUtil.isNullOrEmpty(resultAfterSearch)) {
				CustomDialog.toastInCenter(this,
						this.getString(R.string.no_commen_payer_result));
			}
		}
	}

	/**
	 * 刷新列表
	 */
	private void refreshListView(List<Map<String, Object>> list) {
		closeInput();
		if (adapter == null) {
			adapter = new CommenPayerListAdapter(this, list);
			adapter.setOnDeleteClickLisener(new OnDeleteClickLisener() {

				@Override
				public void onDeleteClick(View v, int position) {
					currentPosition = position;
					BaseDroidApp.getInstanse().showErrorDialog(
							CommenPayerActivity.this.getResources().getString(
									R.string.are_you_sure_delete_payer),
							R.string.cancle, R.string.confirm,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (v.getId() == R.id.retry_btn) {
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
										BaseHttpEngine.showProgressDialog();
										requestCommConversationId();
									} else {
										BaseDroidApp.getInstanse()
												.dismissErrorDialog();
									}
								}
							});
				}
			});
			adapter.setOnOperateClickLisener(new OnOperateClickLisener() {

				@Override
				public void onOperateClick(View v, int position) {
					showOperatePop(position);
				}
			});
			listView.setAdapter(adapter);
		} else {
			adapter.setListData(list);
		}

	}

	/**
	 * @Title: showOperatePop
	 * @Description: 点击操作弹出的底部按钮
	 * @param @param position
	 * @return void
	 * @throws
	 */
	private void showOperatePop(final int position) {
		currentPosition = position;
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vv = inflater.inflate(R.layout.gather_operate_popwindow, null);
		vv.setPadding(20, 20, 20, 20);
		vv.findViewById(R.id.fidgetButton).setOnClickListener(// 修改手机号码
				new View.OnClickListener() {
					public void onClick(View v) {
						if (isSearchMode) {
							GatherInitiativeData.getInstance().setPayerInfo(
									resultAfterSearch.get(position));
						} else {
							GatherInitiativeData.getInstance().setPayerInfo(
									resultAllList.get(position));
						}
						BaseDroidApp.getInstanse().dissmissFootChooseDialog();
						Intent intent = new Intent(CommenPayerActivity.this,
								ModifyPhoneNumberActivity.class);
						startActivityForResult(intent, 1001);
						overridePendingTransition(R.anim.push_up_in,
								R.anim.no_animation);
					}
				});
		vv.findViewById(R.id.orgButton).setOnClickListener(// 发起收款
				new View.OnClickListener() {
					public void onClick(View v) {
						BaseHttpEngine.showProgressDialog();
						requestGatherAccountList();
					}
				});
		vv.findViewById(R.id.cancleButton).setOnClickListener(// 取消
				new View.OnClickListener() {
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dissmissFootChooseDialog();
					}
				});

		BaseDroidApp.getInstanse().showFootChooseDialog(vv);
	}

	/**
	 * @Title: requestPsnTransActQueryPayerList
	 * @Description: 请求“查询付款人列表”接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActQueryPayerList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(GatherInitiative.PSN_TRANS_ACT_QUERY_PAYER_LIST);
		Map<String, Object> map = new HashMap<String, Object>();
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransActQueryPayerListCallback");
	}

	/**
	 * @Title: requestPsnTransActQueryPayerListCallback
	 * @Description: 请求“查询付款人列表”接口的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnTransActQueryPayerListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultAllList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultAllList)) {
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getResources().getString(R.string.no_payer_list_data),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
//							finish();
							btnRight.setVisibility(View.GONE);
							BaseDroidApp.getInstanse().dismissMessageDialog();
						}
					});
		}
		refreshListView(resultAllList);
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestPsnTransActDeletePayer();
	}

	/**
	 * @Title: requestPsnTransActDeletePayer
	 * @Description: 请求“删除付款人”接口
	 * @param
	 * @return void
	 * @throws
	 */
	private void requestPsnTransActDeletePayer() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GatherInitiative.PSN_TRANS_ACT_DELETE_PAYER);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		String payerId = "";
		if (isSearchMode) {
			payerId = (String) resultAfterSearch.get(currentPosition).get(
					GatherInitiative.PAYER_ID);
		} else {
			payerId = (String) resultAllList.get(currentPosition).get(
					GatherInitiative.PAYER_ID);
		}
		map.put(GatherInitiative.PAYER_ID, payerId);
		map.put(GatherInitiative.TOKEN, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransActDeletePayerCallback");
	}

	/**
	 * @Title: requestPsnTransActDeletePayerCallback
	 * @Description: 请求“删除付款人”接口的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	public void requestPsnTransActDeletePayerCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.delete_payer_success));
		CustomDialog.toastInCenter(this,
				this.getString(R.string.delete_payer_success));
		// 删除以后恢复
		btnRight.performClick();
		requestPsnTransActQueryPayerList();
	}

	@Override
	public void communicationCallBack(int flag) {
		// TODO Auto-generated method stub
		super.communicationCallBack(flag);
		if (flag == QUERY_GATHER_ACCOUNT_CALLBACK) {
			BaseDroidApp.getInstanse().dissmissFootChooseDialog();
			String payerName = "";
			String payerPhone = "";
			String payerChanel = "";
			String lineBankNumber = "";
			Map<String, Object> mapInfo;
			if (isSearchMode) {
				mapInfo = (Map<String, Object>) resultAfterSearch
						.get(currentPosition);
				payerName = (String) mapInfo.get(GatherInitiative.PAYER_NAME);
				payerPhone = (String) mapInfo
						.get(GatherInitiative.PAYER_MOBILE);
				payerChanel = (String) mapInfo
						.get(GatherInitiative.IDENTIFY_TYPE);
				lineBankNumber = (String) mapInfo
						.get(GatherInitiative.PAYER_CUSTOMER_ID);
			} else {
				mapInfo = (Map<String, Object>) resultAllList
						.get(currentPosition);
				payerName = (String) mapInfo.get(GatherInitiative.PAYER_NAME);
				payerPhone = (String) mapInfo
						.get(GatherInitiative.PAYER_MOBILE);
				payerChanel = (String) mapInfo
						.get(GatherInitiative.IDENTIFY_TYPE);
				lineBankNumber = (String) mapInfo
						.get(GatherInitiative.PAYER_CUSTOMER_ID);
			}
			Intent intent = new Intent();
			intent.setClass(CommenPayerActivity.this,
					CreatGatherInputInfoActivity.class);
			intent.putExtra(GatherInitiative.PAYER_NAME, payerName);
			intent.putExtra(GatherInitiative.PAYER_MOBILE, payerPhone);
			intent.putExtra(GatherInitiative.PAYER_CHANNEL, payerChanel);
			intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, true);
			intent.putExtra("noNeedSavePayer", true);
			if ("1".equals(payerChanel)) {
				intent.putExtra(GatherInitiative.PAYER_CUST_ID, lineBankNumber);
			}
			startActivityForResult(intent, 1002);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1001) {
			if (resultCode == RESULT_OK) {
				requestPsnTransActQueryPayerList();
			}
		}
	}
}
